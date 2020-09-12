/*
 ============================================================================================================================================
 Description : Data of binding bean for Setup -> Module -> Attendant Service Operation.
 Status      : RELEASE
 ============================================================================================================================================
 */
package slecon.setting.modules;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import slecon.setting.modules.AttendantServiceOperation.GeneralBean;
import slecon.setting.modules.AttendantServiceOperation.IOSettingsBean;
import slecon.setting.modules.AttendantServiceOperation.LEDBehavior;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Error;
import comm.Parser_Event;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.DeviceMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




/**
 * Data of binding bean for Setup -> Module -> Attendant Service Operation.
 */
@SetupView(
    path      = "Modules::Attendant Service Operation",
    sortIndex = 0x304
)
public class AttendantServiceOperationSetting extends SettingPanel<AttendantServiceOperation> implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = 3522407279111851157L;

    /**
     * Logger.
     */
    private final Logger   logger = LogManager.getLogger( AttendantServiceOperationSetting.class );

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid solid  = null;

    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;

    // TODO
    private Parser_Error  error;
    
    private Parser_Module module;
    private Parser_Event  event;

    /**
     * The handler for Setup - Module - Attendant Service Operation.
     *
     * @param connBean
     *            It specifies the instance of Connectivity information.
     */
    public AttendantServiceOperationSetting ( LiftConnectionBean connBean ) {
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
            module = new Parser_Module( connBean.getIp(), connBean.getPort() );
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
    public void onOK ( AttendantServiceOperation panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( AttendantServiceOperation panel ) {
        reset();
    }


    @Override
    public void onConnCreate () {
        app.start();
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
        return AttendantServiceOperation.TEXT.getString( "attendant_service_operation" );
    }

	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Attendant_Service_Operation"), this.getClass());
		return map;
	}
	
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final AttendantServiceOperation.GeneralBean    bean_general    = new AttendantServiceOperation.GeneralBean();
            final AttendantServiceOperation.IOSettingsBean bean_iOSettings = new AttendantServiceOperation.IOSettingsBean();

            /* General */
            bean_general.setEnabled( module.att.isEnabled() );
            bean_general.setCarLedBehavior( LEDBehavior.get( module.att.getCar_led_behavior() ) );
            bean_general.setEnabledFrontBuzzerOnHallCall( module.att.isEnabled_front_buzzer_on_hall_call() );
            bean_general.setEnabledRearBuzzerOnHallCall( module.att.isEnabled_rear_buzzer_on_hall_call() );
            bean_general.setCarMessage( DeviceMessage.get( module.att.getCar_message() ) );
            bean_general.setHallMessage( DeviceMessage.get( module.att.getHall_message() ) );

            /* IOSettings */
            EventAggregator ea = EventAggregator.toEventAggregator( event.getEvent() );
            bean_iOSettings.setAttSwitch( ea.getEvent( EventID.ATT_FRONT.eventID ) );
            bean_iOSettings.setUpSwitch( ea.getEvent( EventID.ATT_UP_FRONT.eventID ) );
            bean_iOSettings.setDownSwitch( ea.getEvent( EventID.ATT_DOWN_FRONT.eventID ) );
            bean_iOSettings.setNonStopSwitch( ea.getEvent( EventID.ATT_NONSTOP_FRONT.eventID ) );
            if ( solid == null )
                solid = new Solid( bean_general, bean_iOSettings );

            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setIOSettingsBean( bean_iOSettings );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            final AttendantServiceOperation.GeneralBean    bean_general    = app.getGeneralBean();
            final AttendantServiceOperation.IOSettingsBean bean_iOSettings = app.getIOSettingsBean();

            /* General */
            module.att.setEnabled( bean_general.getEnabled() );
            module.att.setCar_led_behavior( bean_general.getCarLedBehavior().id );
            module.att.setEnabled_front_buzzer_on_hall_call( bean_general.getEnabledFrontBuzzerOnHallCall() );
            module.att.setEnabled_rear_buzzer_on_hall_call( bean_general.getEnabledRearBuzzerOnHallCall() );
            module.att.setCar_message( bean_general.getCarMessage().getCode() );
            module.att.setHall_message( bean_general.getHallMessage().getCode() );

            /* IOSettings */
            final EventAggregator ea = EventAggregator.toEventAggregator( event.getEvent() );
            ea.setEvent( EventID.ATT_FRONT.eventID, bean_iOSettings.getAttSwitch() );
            ea.setEvent( EventID.ATT_UP_FRONT.eventID, bean_iOSettings.getUpSwitch() );
            ea.setEvent( EventID.ATT_DOWN_FRONT.eventID, bean_iOSettings.getDownSwitch() );
            ea.setEvent( EventID.ATT_NONSTOP_FRONT.eventID, bean_iOSettings.getNonStopSwitch() );
            event.setEvent( ea.toByteArray() );
            event.setInstalledDevices( ea.getInstalledDevices() );
            event.commit();
            module.commit();
            return true;
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
            logger.catching( Level.FATAL, e );
        }
        return false;
    }


    public void reset () {

        // Update returned data to visualization components.
        if ( solid != null )
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    if ( solid != null ) {
                        app.stop();
                        app.setGeneralBean( solid.bean_general );
                        app.setIOSettingsBean( solid.bean_iOSettings );
                        app.start();
                    }
                }
            } );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final AttendantServiceOperation.GeneralBean    bean_general;
        private final AttendantServiceOperation.IOSettingsBean bean_iOSettings;




        public Solid ( GeneralBean bean_general, IOSettingsBean bean_iOSettings ) {
            super();
            this.bean_general    = bean_general;
            this.bean_iOSettings = bean_iOSettings;
        }
    }
}
