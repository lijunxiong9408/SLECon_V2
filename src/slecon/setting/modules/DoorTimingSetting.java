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
import slecon.setting.modules.DoorTiming.DoorHoldButtonBean;
import slecon.setting.modules.DoorTiming.GeneralBean;
import slecon.setting.modules.DoorTiming.IndependentControlOfCarDoorAndLandingDoorBean;
import slecon.setting.modules.DoorTiming.SafetyChainFailBean;
import slecon.setting.modules.DoorTiming.TimeoutBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Error;
import comm.Parser_Event;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




/**
 * Data of binding bean for Setup -> Module -> Door Timing.
 */
@SetupView(
    path      = "Modules::Door Timing",
    sortIndex = 0x301
)
public class DoorTimingSetting extends SettingPanel<DoorTiming> implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = -1298927779881117194L;

    /**
     * Logger.
     */
    private final Logger   logger = LogManager.getLogger( DoorTimingSetting.class );

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid solid  = null;

    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;
    
    // TODO
    private Parser_Error       error;
    
    private Parser_Module      module;
    private Parser_Event       event;


    /**
     * The handler for Setup - Module - Door Timing.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public DoorTimingSetting ( LiftConnectionBean connBean ) {
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
    public void onOK ( DoorTiming panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() ) {
                solid = null;
            }
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( DoorTiming panel ) {
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
                System.out.printf("timestamp:%d lastest:%d\n", timestamp, lastestTimeStamp);
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
        return DoorTiming.TEXT.getString( "doortiming" );
    }
    
    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Door_Timing"), this.getClass());
		return map;
	}

    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        try {
            final DoorTiming.GeneralBean                                   bean_general                                      =
                new DoorTiming.GeneralBean();
            final DoorTiming.TimeoutBean                                   bean_timeout                                      =
                new DoorTiming.TimeoutBean();
            final DoorTiming.SafetyChainFailBean                           bean_door_close_failure_recovery                  =
                new DoorTiming.SafetyChainFailBean();
            final DoorTiming.DoorHoldButtonBean                            bean_door_hold_button                             =
                new DoorTiming.DoorHoldButtonBean();
            final DoorTiming.IndependentControlOfCarDoorAndLandingDoorBean bean_independent_control_of_car_door_landing_door =
                new DoorTiming.IndependentControlOfCarDoorAndLandingDoorBean();
            final EventAggregator ea = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );

            /* General */
            bean_general.setDoorCloseTimer( ( long )module.dcs.getDoor_close_time() );
            bean_general.setDisabledDoorCloseTimer( ( long )module.dcs.getDisabled_Door_close_time() );
            bean_general.setDdoCallCoolTime( ( long )module.dcs.getDDOWait_time() );
            bean_general.setEnabel_fcd( module.dcs.isForced_close_door_enabled() );

            /* Timeout. */
            bean_timeout.setDoorOpeningTimeout( ( long )module.dcs.getDoor_opening_timeout() );
            bean_timeout.setDoorOpenedTimeout( ( long )module.dcs.getDoor_opened_timeout() );
            bean_timeout.setDoorClosingTimeout( ( long )module.dcs.getDoor_closing_timeout() );
            bean_timeout.setDoorClosedTimeout( ( long )module.dcs.getDoor_closed_timeout() );
            bean_timeout.setDoorForcedClosedTimeout( ( long )module.dcs.getDoor_forced_closed_timeout() );

            /* Safety Chain Fail. */
            bean_door_close_failure_recovery.setActivationTime( ( long )module.dcs.getActivation_time() );
            bean_door_close_failure_recovery.setRetryCount( ( long )module.dcs.getRetry_count() );

            /* Door Hold Button. */
            bean_door_hold_button.setEnabled( module.dcs.isDoor_hold_button_enabled() );
            bean_door_hold_button.setHoldingTime( ( long )module.dcs.getHolding_time() );
            bean_door_hold_button.setDoorHoldButtonEvent( ea.getEvent( EventID.DOOR_HOLD_BUTTON.eventID ) );

            /* Independent control of car door and landing door. */
            bean_independent_control_of_car_door_landing_door.setEnabled0(
                module.dcs.isIndependent_control_of_car_door_and_landing_door_enabled() );
            bean_independent_control_of_car_door_landing_door.setFrontDoorCarCalls( ( long )module.dcs.getFront_door_car_calls() );
            bean_independent_control_of_car_door_landing_door.setFrontDoorHallCalls( ( long )module.dcs.getFront_door_hall_calls() );
            bean_independent_control_of_car_door_landing_door.setRearDoorCarCalls( ( long )module.dcs.getRear_door_car_calls() );
            bean_independent_control_of_car_door_landing_door.setRearDoorHallCalls( ( long )module.dcs.getRear_door_hall_calls() );
            if ( solid == null ) {
                solid = new Solid( bean_general, bean_timeout, bean_door_close_failure_recovery, bean_door_hold_button,
                                   bean_independent_control_of_car_door_landing_door );
            }

            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setTimeoutBean( bean_timeout );
                    app.setSafetyChainFailBean( bean_door_close_failure_recovery );
                    app.setDoorHoldButtonBean( bean_door_hold_button );
                    app.setIndependentControlOfCarDoorAndLandingDoorBean( bean_independent_control_of_car_door_landing_door );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    private boolean submit () {
        try {
            final TimeoutBean                                   bean_timeout                                   = app.getTimeoutBean();
            final GeneralBean                                   bean_general                                   = app.getGeneralBean();
            final SafetyChainFailBean                           bean_safetychainbean                           = app.getSafetyChainFailBean();
            final DoorHoldButtonBean                            bean_doorHoldButton                            = app.getDoorHoldButtonBean();
            final IndependentControlOfCarDoorAndLandingDoorBean bean_independentControlOfCarDoorAndLandingDoor =
                app.getIndependentControlOfCarDoorAndLandingDoorBean();

            /* General */
            module.dcs.setDoor_close_time( bean_general.getDoorCloseTimer().intValue() );
            module.dcs.setDisabled_Door_close_time( bean_general.getDisabledDoorCloseTimer().intValue() );
            module.dcs.setDDOWait_time( bean_general.getDdoCallCoolTime().intValue() );
            module.dcs.setForced_close_door_enabled( bean_general.getEnabel_fcd() );

            /* Timeout. */
            module.dcs.setDoor_opening_timeout( bean_timeout.getDoorOpeningTimeout().intValue() );
            module.dcs.setDoor_opened_timeout( bean_timeout.getDoorOpenedTimeout().intValue() );
            module.dcs.setDoor_closing_timeout( bean_timeout.getDoorClosingTimeout().intValue() );
            module.dcs.setDoor_closed_timeout( bean_timeout.getDoorClosedTimeout().intValue() );
            module.dcs.setDoor_forced_closed_timeout( bean_timeout.getDoorForcedClosedTimeout().intValue() );

            /* Safety Chain Fail. */
            module.dcs.setActivation_time( bean_safetychainbean.getActivationTime().intValue() );
            module.dcs.setRetry_count( bean_safetychainbean.getRetryCount().intValue() );

            /* Door Hold Button. */
            module.dcs.setDoor_hold_button_enabled( bean_doorHoldButton.getEnabled() );
            module.dcs.setHolding_time( bean_doorHoldButton.getHoldingTime().intValue() );

            /* Independent control of car door and landing door. */
            module.dcs.setIndependent_control_of_car_door_and_landing_door_enabled(
                bean_independentControlOfCarDoorAndLandingDoor.getEnabled0() );
            module.dcs.setFront_door_car_calls( bean_independentControlOfCarDoorAndLandingDoor.getFrontDoorCarCalls().intValue() );
            module.dcs.setFront_door_hall_calls( bean_independentControlOfCarDoorAndLandingDoor.getFrontDoorHallCalls().intValue() );
            module.dcs.setRear_door_car_calls( bean_independentControlOfCarDoorAndLandingDoor.getRearDoorCarCalls().intValue() );
            module.dcs.setRear_door_hall_calls( bean_independentControlOfCarDoorAndLandingDoor.getRearDoorHallCalls().intValue() );

            final EventAggregator ea = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );
            ea.setEvent( EventID.DOOR_HOLD_BUTTON.eventID, bean_doorHoldButton.getDoorHoldButtonEvent() );
            event.setEvent( ea.toByteArray( this.connBean ) );
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


    private void reset () {
        // Update returned data to visualization components.
        if ( solid != null )
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    if ( solid != null ) {
                        app.stop();
                        app.setGeneralBean( solid.bean_general );
                        app.setTimeoutBean( solid.bean_timeout );
                        app.setSafetyChainFailBean( solid.bean_door_close_failure_recovery );
                        app.setDoorHoldButtonBean( solid.bean_door_hold_button );
                        app.setIndependentControlOfCarDoorAndLandingDoorBean( solid.bean_independent_control_of_car_door_landing_door );
                        app.start();
                    }
                }
            } );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        final DoorTiming.GeneralBean                                   bean_general;
        final DoorTiming.TimeoutBean                                   bean_timeout;
        final DoorTiming.SafetyChainFailBean                           bean_door_close_failure_recovery;
        final DoorTiming.DoorHoldButtonBean                            bean_door_hold_button;
        final DoorTiming.IndependentControlOfCarDoorAndLandingDoorBean bean_independent_control_of_car_door_landing_door;




        public Solid ( GeneralBean bean_general, TimeoutBean bean_timeout, SafetyChainFailBean bean_door_close_failure_recovery,
                       DoorHoldButtonBean bean_door_hold_button,
                       IndependentControlOfCarDoorAndLandingDoorBean bean_independent_control_of_car_door_landing_door ) {
            super();
            this.bean_general                                      = bean_general;
            this.bean_timeout                                      = bean_timeout;
            this.bean_door_close_failure_recovery                  = bean_door_close_failure_recovery;
            this.bean_door_hold_button                             = bean_door_hold_button;
            this.bean_independent_control_of_car_door_landing_door = bean_independent_control_of_car_door_landing_door;
        }
    }
}
