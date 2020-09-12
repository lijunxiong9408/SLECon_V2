package slecon.setting.modules;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.PageTreeExpression;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.Parser_Misc;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;


@SetupView(
    path      = "Modules::Random Run",
    sortIndex = 0x313
)
public class RandomRunSetting extends SettingPanel<RandomRun> implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = 6398741682602149481L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( RandomRunSetting.class );
    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.SettingPanel");
    private static final PageTreeExpression WRITE_OCS_EXPRESSION = new PageTreeExpression("write_ocs");

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid     solid = null;

    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;

    // TODO
    private Parser_Error  error;
    
    private Parser_Module module;
    private Parser_Deploy deploy;
    private Parser_Misc        misc;

    /**
     * The handler for Setup - Module - Access Control.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public RandomRunSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart () throws Exception {
        try {
            error     = new Parser_Error( connBean.getIp(), connBean.getPort() );
            module    = new Parser_Module( connBean.getIp(), connBean.getPort() );
            deploy 	  = new Parser_Deploy( connBean.getIp(), connBean.getPort() );  
            misc      = new Parser_Misc( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.MODULE.getCode() | 
                                      AgentMessage.ERROR.getCode() |
                                      AgentMessage.OCS_CONFIG.getCode() |
                                      AgentMessage.MISC.getCode() );
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
                AgentMessage.MODULE.getCode() | 
                AgentMessage.ERROR.getCode() |
                AgentMessage.OCS_CONFIG.getCode() |
                AgentMessage.MISC.getCode() );
        
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
    public void onConnCreate () {
        app.start();
        setEnabled( false );
    }


    @Override
    public void onDataChanged ( long timestamp, int msg ) {
        if(msg==AgentMessage.ERROR.getCode()) 
            ToolBox.showRemoteErrorMessage(connBean, error);

        synchronized (mutex) {
            setEnabled(false);
            /*
            if (solid != null && timestamp > lastestTimeStamp+1500*1000000) {
                int result = JOptionPane.showConfirmDialog(StartUI.getFrame(), "The config of this lift has changed. Reload it?", "Update",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    solid = null;
                    setHot();
                }
            } else {
                setHot();
            }
            */
            setHot();
            setEnabled(true);
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
        return RandomRun.TEXT.getString( "random_run" );
    }
    
    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Random_Run"), this.getClass());
		return map;
	}

    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final RandomRun.GeneralBean     bean_general     = new RandomRun.GeneralBean();
            // General
            bean_general.setEnabled(module.rdr.getEnable()==1?true:false);
            bean_general.setInterval_time((Long)module.rdr.getInterval_timer());
            bean_general.setDoorEnableAction(module.rdr.getRunStrategy());
            bean_general.setRun_times(module.rdr.getRunTimes());
            
            String[] floorText = new String[128];
            for(int i=0; i < deploy.getFloorCount(); i++) {
            	floorText[i] = deploy.getFloorText((byte)i) ;
            }
            bean_general.setFloorText(floorText);
            bean_general.setFloorcount(deploy.getFloorCount());
            
            if ( solid == null )
	            solid = new Solid( bean_general );
            
            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    private boolean submit () { 
    	try {
            final RandomRun.GeneralBean    bean_general    = app.getGeneralBean();
            
            /* General */
            module.rdr.setEnable(bean_general.getEnabled()?1:0);
            module.rdr.setUpdate();
            module.rdr.setInterval_timer(bean_general.getInterval_time().intValue());
            module.rdr.setRunStrategy(bean_general.getDoorEnableAction());
            module.rdr.setRunTimes(bean_general.getRun_times());
            
            module.commit();
            return true;
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
            logger.catching( Level.FATAL, e );
        }
        return false;
    }


    private void reset () {
    	if ( solid != null )
	        SwingUtilities.invokeLater( new Runnable() {
	            @Override
	            public void run () {
	                if ( solid != null ) {
	                    app.stop();
	                    app.setGeneralBean( solid.bean_general );
	                    app.start();
	                }
	            }
	        } );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final RandomRun.GeneralBean    bean_general;
        private Solid ( RandomRun.GeneralBean bean_general ) {
            super();
            this.bean_general = bean_general ;
        }
    }

	@Override
	public void onOK(RandomRun panel) {
		// TODO Auto-generated method stub
		synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
	}


	@Override
	public void onReset(RandomRun panel) {
		// TODO Auto-generated method stub
		reset();
	}

	public void sendCanData(RandomRun.CanVoiceBean Bean) {
       // misc.mcs(cmd.cmdID, new byte[]{});
	   // default 0x1001
		short cmd = 0x1001;
		byte[] Data = new byte[4];
		Data[0] = (byte)Bean.getID();
		Data[1] = (byte)Bean.getBUS();
		switch(Bean.getVoiceType()) {
			case 0:
				Data[2] = 0x00;
				Data[3] = 0x01;
			break;
			case 1:
				Data[2] = 0x00;
				Data[3] = (byte) 0x82;
			break;
			case 2:
				Data[2] = 0x00;
				Data[3] = (byte) 0x96;
			break;
			case 3:
				Data[2] = 0x00;
				Data[3] = (byte) 0xAA;
			break;
			case 4:
				Data[2] = 0x01;
				Data[3] = 0x01;
			break;
			case 5:
				Data[2] = 0x00;
				Data[3] = (byte) 0xD2;
			break;
		}
		
		misc.can(cmd, Data);
	}

}
