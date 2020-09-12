package slecon.setting.management;

import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.SiteManagement;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.management.Utilities.NetworkBean;
import slecon.setting.management.Utilities.SystemClockBean;
import slecon.setting.modules.DoorTimingSetting;

import comm.Parser_Error;
import comm.Parser_Misc;
import comm.Parser_NetWork;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;



@SetupView(
    path      = "Management::Utilities",
    sortIndex = 0x410
)
public class UtilitiesSetting extends SettingPanel<Utilities> implements Page, LiftDataChangedListener {
    private static final long           serialVersionUID = -3944112770351037862L;
    /**
     * Logger.
     */
    private final Logger   logger = LogManager.getLogger( DoorTimingSetting.class );
    
    /**
     * Text resource.
     */
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Connectivity information.
     */
    private final LiftConnectionBean connBean;
    
    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private boolean IsVerify = false;
    
    private Parser_Error       error;
    private Parser_Misc        misc;
    private Parser_Status      status;
    private Parser_NetWork	   network;
    
    private NetworkBean netWorkbean_bak;


    public UtilitiesSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart () throws Exception {
        try {
            error  = new Parser_Error( connBean.getIp(), connBean.getPort() );
            status = new Parser_Status( connBean.getIp(), connBean.getPort() );
            misc   = new Parser_Misc( connBean.getIp(), connBean.getPort() );
            network = new Parser_NetWork( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.ERROR.getCode() | AgentMessage.MCS_CONFIG.getCode() |
                                      AgentMessage.MISC.getCode() | AgentMessage.NETWORK.getCode() );
            setHot();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.nanoTime();
        }
        
        app.start();
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MANAGER));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MANAGER));
    }


    @Override
    public void onResume () throws Exception {
        app.start();
        setEnabled( true );
        MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                AgentMessage.ERROR.getCode() | AgentMessage.MCS_CONFIG.getCode() | AgentMessage.MISC.getCode() );
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
    }


    @Override
    public void onPause () throws Exception {
        app.stop();
        setEnabled( false );
        MON_MGR.removeEventListener( this );
    }


    @Override
    public void onStop () throws Exception {
        app.stop();
        MON_MGR.removeEventListener( this );
    }


    @Override
    public void onOK ( Utilities panel ) {
        synchronized ( mutex  ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( Utilities panel ) {
        reset();
    }


    @Override
    public void onConnCreate () {
        app.start();
        setEnabled( false );
    }


    @Override
    public void onDataChanged ( long timestamp, int msg ) {
        if (msg==AgentMessage.ERROR.getCode())
            ToolBox.showRemoteErrorMessage(connBean, error);

        synchronized (mutex) {
            if (solid != null && timestamp > lastestTimeStamp+1500*1000000 ) {
//                int result = JOptionPane.showConfirmDialog(StartUI.getFrame(), "The config of this lift has changed. Reload it?", "Update",
//                        JOptionPane.YES_NO_OPTION);
//                if (result == JOptionPane.OK_OPTION) {
//                    solid = null;
//                    setHot();
//                }
            } else {
                setEnabled(false);
                setHot();
                setEnabled(true);
            }
        }
    }


    @Override
    public void onConnLost () {
        app.stop();
        setEnabled( true );
    }


    @Override
    public void onDestroy () throws Exception {
    }


    @Override
    protected String getPanelTitle () {
        return TEXT.getString( "Utilities.title" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Management"), slecon.setting.management.UtilitiesSetting.class);
		map.put(Dict.lookup("Utilities"), null);
		return map;
	}
    //////////////////////////////////////////////////////////////////////////////////

    private static final class Solid {
        private final Utilities.SystemClockBean bean_systemClock;
        private final Utilities.NetworkBean netWorkbean;

        private Solid(SystemClockBean bean_systemClock, NetworkBean netWorkbean) {
            super();
            this.bean_systemClock = bean_systemClock;
            this.netWorkbean = netWorkbean;
        }
    }
    
    private volatile Solid solid  = null;

    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final SystemClockBean bean = new SystemClockBean();
            final NetworkBean netWorkbean = new NetworkBean();
            netWorkbean_bak = new NetworkBean();
            bean.setSetSystemClock(new Date(status.getTime()));
            
            netWorkbean.setIp( network.getLocalIP() );
            netWorkbean.setMask( network.getLocalMask() );
            netWorkbean.setGateway( network.getLocalGateway() );
            
            netWorkbean_bak.setIp( network.getLocalIP() );
            netWorkbean_bak.setMask( network.getLocalMask() );
            netWorkbean_bak.setGateway( network.getLocalGateway() );
            
            if(solid==null)
                solid = new Solid(bean, netWorkbean);
            SwingUtilities.invokeLater(new Runnable() {
                
                @Override
                public void run() {
                    app.setSystemClockBean(bean);
                    app.setNetWorkBean(netWorkbean);
                }
            });
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    private boolean submit () {
    	if(IsVerify) {
	        try {
	            SystemClockBean bean = app.getSystemClockBean();
	            Date date = bean.getSetSystemClock();
	            misc.time(date.getTime());
	            
	            NetworkBean netWorkbean = app.getNetworkBean();
	            String actualIP = new String(netWorkbean.getIp());
	            String bakIP = new String(netWorkbean_bak.getIp());
	            String actualMask = new String(netWorkbean.getMask());
	            String bakMask = new String(netWorkbean_bak.getMask());
	            String actualGateway = new String(netWorkbean.getGateway());
	            String bakGateway = new String(netWorkbean_bak.getGateway());
	            
	        	if(!actualIP.trim().equals(bakIP.trim()) || !actualMask.trim().equals(bakMask.trim()) || !actualGateway.trim().equals(bakGateway.trim()) ) {
	    			if( JOptionPane.showConfirmDialog( this, TEXT.getString( "Warning_desc" ), TEXT.getString( "Warning_title" ),
	    	                JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {
	    				network.setLocalIP( netWorkbean.getIp() );
	                    network.setLocalMask( netWorkbean.getMask() );
	                    network.setLocalGateWay( netWorkbean.getGateway() );
	                    network.commit();
	    			}else {
	    				reset ();
	    			}
	        	}
	            
	            return true;
	        } catch ( Exception e ) {
	            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
	            logger.catching( Level.FATAL, e );
	        }
    	}
        return false;
    }


    private void reset () {
        if(!IsVerify) {
    		try {
    			JPasswordField pwd = new JPasswordField();
    			Object[] message = {TEXT.getString("Password.text"), pwd};
    			int res = JOptionPane.showConfirmDialog(this, message, TEXT.getString("Password.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    			if(res == JOptionPane.OK_OPTION) {
    				if("282828".equals(new String(pwd.getPassword()))) {
    					IsVerify = true;
            			app.SetWeightEnable(true);
            			// Update returned data to visualization components.
            	        if ( solid != null )
            	            SwingUtilities.invokeLater( new Runnable() {
            	                @Override
            	                public void run () {
            	                    if ( solid != null ) {
            	                        app.stop();
            	                        app.setSystemClockBean( solid.bean_systemClock );
            	                        app.start();
            	                    }
            	                }
            	            } );
    				}else {
    					JOptionPane.showMessageDialog(null,TEXT.getString("Password.error"));
    				}
    			}
    		}catch(Exception e) {
    			JOptionPane.showMessageDialog(null,TEXT.getString("Password.error"));
    		}
    		
    	}
    }

}
