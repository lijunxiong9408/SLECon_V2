package slecon.setting.setup.event;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.EventID;
import logic.connection.LiftConnectionBean;
import ocsjava.remote.configuration.EventAggregator;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.setup.event.Others.IOSettingsBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Error;
import comm.Parser_Event;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




@SetupView(
    sortIndex = 0x221,
    path      = "Setup::Event::Others"
)
public class OthersSetting extends SettingPanel<Others> implements Page, LiftDataChangedListener {
    
    private static final long           serialVersionUID = -9119004120061866725L;

    /**
     * Text resource.
     */
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Logger.
     */
    private final Logger   logger = LogManager.getLogger( OthersSetting.class );
    
    private volatile long  lastestTimeStamp = -1;
    private final Object   mutex            = new Object(); 
    private volatile Solid solid            = null;
    
    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;
    
    private Parser_Error       error;
    
    private Parser_Event       event;

    private boolean IsVerify = false;



    public OthersSetting ( LiftConnectionBean conn ) {
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
            event  = new Parser_Event( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() | AgentMessage.EVENT.getCode() );
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
                                  AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() | AgentMessage.EVENT.getCode() );
        
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
    public void onOK ( Others panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( Others panel ) {
    	synchronized ( mutex ) {
            setEnabled(false);
            reset();
            setEnabled(true);
        }
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
        return TEXT.getString( "Others.title" );
    }

	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("Event"), slecon.setting.setup.event.FloorSetting.class);
		map.put(Dict.lookup("Others"), this.getClass());
		return map;
	}
	
    private static final class Solid {
        private final IOSettingsBean bean_iOSettings;

        private Solid(IOSettingsBean bean_iOSettings) {
            super();
            this.bean_iOSettings = bean_iOSettings;
        }
    }
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final EventAggregator ea       = EventAggregator.toEventAggregator( event.getEvent() );

            /* IOSettings */
            final Others.IOSettingsBean bean_iOSettings = new Others.IOSettingsBean();
            bean_iOSettings.setFrontbuzzerEvent( ea.getEvent( EventID.BUZZER_FRONT.eventID ) );
            bean_iOSettings.setRearbuzzerEvent( ea.getEvent( EventID.BUZZER_REAR.eventID ) );
            bean_iOSettings.setOpenFrontDoorEvent( ea.getEvent( EventID.DOOR_OPEN_BUTTON_FRONT.eventID ) );
            bean_iOSettings.setCloseFrontDoorEvent( ea.getEvent( EventID.DOOR_CLOSE_BUTTON_FRONT.eventID ) );
            bean_iOSettings.setOpenRearDoorEvent( ea.getEvent( EventID.DOOR_OPEN_BUTTON_REAR.eventID ) );
            bean_iOSettings.setCloseRearDoorEvent( ea.getEvent( EventID.DOOR_CLOSE_BUTTON_REAR.eventID ) );
            bean_iOSettings.setOpenDisabledDoorEvent(ea.getEvent(EventID.DOOR_OPEN_BUTTON_DISABLED.eventID));
            bean_iOSettings.setCloseDisabledDoorEvent(ea.getEvent(EventID.DOOR_CLOSE_BUTTON_DISABLED.eventID));
            bean_iOSettings.setUpIndicatorEvent( ea.getEvent( EventID.UP_INDICATOR.eventID ) );
            bean_iOSettings.setDnIndicatorEvent( ea.getEvent( EventID.DOWN_INDICATOR.eventID ) );
            bean_iOSettings.setLightFlashEvent( ea.getEvent( EventID.LIGHT_FLASH.eventID ) );
            bean_iOSettings.setLightFastFlashEvent( ea.getEvent( EventID.LIGHT_FAST_FLASH.eventID ) );
            bean_iOSettings.setLightBlinkEvent( ea.getEvent( EventID.LIGHT_BLINK.eventID ) );
            bean_iOSettings.setLightFastBlinkEvent( ea.getEvent( EventID.LIGHT_FAST_BLINK.eventID ) );
            bean_iOSettings.setLightCust1Event( ea.getEvent( EventID.LIGHT_CUST1.eventID ) );
            bean_iOSettings.setLightCust2Event( ea.getEvent( EventID.LIGHT_CUST2.eventID ) );
            
            if(solid==null)
                solid = new Solid(bean_iOSettings);
            
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setIOSettingsBean( bean_iOSettings );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
    	if(IsVerify) {
	        try {
	            IOSettingsBean bean_iOSettings = app.getIOSettingsBean();
	            EventAggregator ea       = EventAggregator.toEventAggregator( event.getEvent() );
	            
	            ea.setEvent( EventID.DOOR_OPEN_BUTTON_FRONT.eventID, bean_iOSettings.getOpenFrontDoorEvent() );
	            ea.setEvent( EventID.DOOR_CLOSE_BUTTON_FRONT.eventID, bean_iOSettings.getCloseFrontDoorEvent() );
	            ea.setEvent( EventID.DOOR_OPEN_BUTTON_REAR.eventID, bean_iOSettings.getOpenRearDoorEvent() );
	            ea.setEvent( EventID.DOOR_CLOSE_BUTTON_REAR.eventID, bean_iOSettings.getCloseRearDoorEvent() );
	            ea.setEvent( EventID.DOOR_OPEN_BUTTON_DISABLED.eventID, bean_iOSettings.getOpenDisabledDoorEvent() );
	            ea.setEvent( EventID.DOOR_CLOSE_BUTTON_DISABLED.eventID, bean_iOSettings.getCloseDisabledDoorEvent() );
	            	
	            ea.setEvent( EventID.BUZZER_FRONT.eventID, bean_iOSettings.getFrontbuzzerEvent() );
	            ea.setEvent( EventID.BUZZER_REAR.eventID, bean_iOSettings.getRearbuzzerEvent() );
	            
	            ea.setEvent( EventID.UP_INDICATOR.eventID, bean_iOSettings.getUpIndicatorEvent() );
	            ea.setEvent( EventID.DOWN_INDICATOR.eventID, bean_iOSettings.getDnIndicatorEvent() );
	            ea.setEvent( EventID.LIGHT_FLASH.eventID, bean_iOSettings.getLightFlashEvent() );
	            ea.setEvent( EventID.LIGHT_FAST_FLASH.eventID, bean_iOSettings.getLightFastFlashEvent() );
	            ea.setEvent( EventID.LIGHT_BLINK.eventID, bean_iOSettings.getLightBlinkEvent() );
	            ea.setEvent( EventID.LIGHT_FAST_BLINK.eventID, bean_iOSettings.getLightFastBlinkEvent() );
	            ea.setEvent( EventID.LIGHT_CUST1.eventID, bean_iOSettings.getLightCust1Event() );
	            ea.setEvent( EventID.LIGHT_CUST2.eventID, bean_iOSettings.getLightCust2Event() );
	            
	            event.setEvent( ea.toByteArray() );
	            event.setInstalledDevices( ea.getInstalledDevices() );
	            event.commit();
	            return true;
	        } catch ( Exception e ) {
	            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
	            logger.catching( Level.FATAL, e );
	        }
    	}
    	return false;
    }


    public void reset () {

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
	    	if ( solid != null )
	            SwingUtilities.invokeLater( new Runnable() {
	                @Override
	                public void run () {
	                    if ( solid != null ) {
	                        app.stop();
	                        app.setIOSettingsBean( solid.bean_iOSettings );
	                        app.start();
	                    }
	                }
	            } );
    	}
    }

}
