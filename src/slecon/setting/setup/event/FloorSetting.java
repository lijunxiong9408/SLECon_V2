package slecon.setting.setup.event;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import ocsjava.remote.configuration.EventAggregator;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.modules.DoorTimingSetting;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.Parser_Event;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




@SetupView(
    sortIndex = 0x220,
    path      = "Setup::Event::Floor"
)
public class FloorSetting extends SettingPanel<Floor> implements Page, LiftDataChangedListener {
    /**
     * Text resource.
     */
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.SettingPanel" );
    
    /**
     * Logger.
     */
    private final Logger   logger = LogManager.getLogger( DoorTimingSetting.class );
    
    private volatile long  lastestTimeStamp = -1;
    private final Object   mutex            = new Object(); 
    private volatile Solid solid            = null;

    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;
    
    private Parser_Error       error;
    
    private Parser_Deploy      deploy;
    
    private Parser_Event       event;

    private boolean IsVerify = false;



    public FloorSetting ( LiftConnectionBean conn ) {
    	super(conn);
        this.connBean = conn;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart () throws Exception {
        try {
            error  = new Parser_Error( connBean.getIp(), connBean.getPort() );
            deploy = new Parser_Deploy( connBean.getIp(), connBean.getPort() );
            event  = new Parser_Event( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.DEPLOYMENT.getCode()
                                      | AgentMessage.EVENT.getCode() 
                                      | AgentMessage.ERROR.getCode() );
            setHot();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.nanoTime();
        }
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
    }


    @Override
    public void onResume () throws Exception {
        setEnabled( true );
        MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                  AgentMessage.DEPLOYMENT.getCode() | AgentMessage.ERROR.getCode() | AgentMessage.EVENT.getCode() );
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
    }


    @Override
    public void onPause () throws Exception {
        setEnabled( false );
        MON_MGR.removeEventListener( this );
    }


    @Override
    public void onStop () throws Exception {
        MON_MGR.removeEventListener( this );
    }


    @Override
    public void onOK ( Floor panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( Floor panel ) {
    	synchronized ( mutex ) {
            setEnabled(false);
            reset();
            setEnabled(true);
        }
    }


    @Override
    public void onConnCreate () {
        setEnabled( false );
    }


    @Override
    public void onDataChanged(long timestamp, int msg) {
        if(msg==AgentMessage.ERROR.getCode()) 
            ToolBox.showRemoteErrorMessage(connBean, error);

        synchronized (mutex) {
            setEnabled(false);
            if (solid != null && timestamp > lastestTimeStamp+1500*1000000) {
                int result = JOptionPane.showConfirmDialog(StartUI.getFrame(), "The config of this lift has changed. Reload it?", "Update",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    solid = null;
                    event  = new Parser_Event( connBean.getIp(), connBean.getPort() );
                    setHot();
                }
            } else {
                setHot();
            }
            setEnabled(true);
        }
    }


    @Override
    public void onConnLost () {
        setEnabled( true );
    }


    @Override
    public void onDestroy () throws Exception {
    }


    @Override
    protected String getPanelTitle () {
        return TEXT.getString( "Floor.title" );
    }

	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("Event"), slecon.setting.setup.event.FloorSetting.class);
		map.put(Dict.lookup("Floor"), this.getClass());
		return map;
	}
    //////////////////////////////////////////////////////////////////////////////////

    private static final class Solid {
        private final LiftConnectionBean connBean;
        private final TreeMap<Integer, String> floorMap;
        private final EventAggregator ea;

        public Solid(LiftConnectionBean connBean, TreeMap<Integer, String> floorMap, EventAggregator ea) {
            this.connBean = connBean;
            this.floorMap = floorMap;
            this.ea = ea;
        }
        
    }
    
    public void setHot() {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final EventAggregator ea        = EventAggregator.toEventAggregator( event.getEvent() );
  
            // TODO pls excluding doorzone  
            final TreeMap<Integer, String> floorMap = new TreeMap<>();
            for ( Integer i = 0 ; i < deploy.getFloorCount() ; i++ ) {
                floorMap.put( i, new String( deploy.getFloorText( i.byteValue() ) ) );
            }
            
            if (solid == null)
                solid = new Solid( connBean, floorMap, ea );


            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.setData( connBean, floorMap, ea );
                }
            } );
        } catch (Exception e) {
            logger.catching(Level.FATAL, e);
        }
    }

    public boolean submit() {
    	if(IsVerify) {
	        try {
	            EventAggregator ea = app.getEventAggregator();
	            event.setEvent( ea.toByteArray() );
	            event.setInstalledDevices( ea.getInstalledDevices() );
	            event.commit();
	            return true;
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(StartUI.getFrame(), "an error has come. " + e.getMessage());
	            logger.catching(Level.FATAL, e);
	        }
    	}
        return false;
    }

    public void reset() {
        // Update returned data to visualization components.
    	if(!IsVerify) {
    		try {
    			JPasswordField pwd = new JPasswordField();
    			Object[] message = {TEXT.getString("Password.text"), pwd};
    			int res = JOptionPane.showConfirmDialog(this, message, TEXT.getString("Password.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    			if(res == JOptionPane.OK_OPTION) {
    				if("282828".equals(new String(pwd.getPassword()))) {
    					IsVerify = true;
    					app.SetWidgetEnable(true);
            			
    				}else {
    					JOptionPane.showMessageDialog(null,TEXT.getString("Password.error"));
    				}
    				
    			}
    		}catch(Exception e) {
    			JOptionPane.showMessageDialog(null,TEXT.getString("Password.error"));
    		}
    		
    	}else { 
		    if (solid != null)
		        SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		                if (solid != null) {
		                    app.setData( solid.connBean, solid.floorMap, solid.ea );
		                }
		            }
		        });
    	}   
    }
}
