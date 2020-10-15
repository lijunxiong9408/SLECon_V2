package slecon.setting.modules;
import static logic.util.SiteManagement.MON_MGR;

import java.util.ArrayList;
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
import slecon.interfaces.ConvertException;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.modules.Parking.CarCallSettingBean;
import slecon.setting.modules.Parking.DcsFanBean;
import slecon.setting.modules.Parking.DcsLightBean;
import slecon.setting.modules.Parking.DimDevicesBean;
import slecon.setting.modules.Parking.GeneralBean;
import slecon.setting.modules.Parking.IOSettingsBean;
import slecon.setting.modules.Parking.OpenDoorButtonBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.Parser_Event;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.constants.DeviceMessage;
import comm.event.LiftDataChangedListener;




/**
 * Data of binding bean for Setup -> Module -> Parking.
 */
@SetupView(
    path      = "Modules::Parking",
    sortIndex = 0x308
)
public class ParkingSetting extends SettingPanel<Parking> implements Page, LiftDataChangedListener {

    private static final long serialVersionUID = -4726668778961882195L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( ParkingSetting.class );
    
    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid      solid  = null;

    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;

    /**
     * Error data.
     */
    // TODO
    private Parser_Error error;

    /**
     * Module data.
     */
    private Parser_Module module;

    /**
     * Deployment data.
     */
    private Parser_Deploy deploy;

    /**
     * Event data.
     */
    private Parser_Event event;

    /**
     * A floor v.s. floor text mapping table.
     */
    private ArrayList<FloorText> floorTexts;




    /**
     * The handler for Setup - Module - Parking.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public ParkingSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart () throws Exception {
        try {
            deploy = new Parser_Deploy( connBean.getIp(), connBean.getPort() );
            error  = new Parser_Error( connBean.getIp(), connBean.getPort() );
            module = new Parser_Module( connBean.getIp(), connBean.getPort() );
            event  = new Parser_Event( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.DEPLOYMENT.getCode() | AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode()
                                      | AgentMessage.EVENT.getCode() );
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
                                  AgentMessage.DEPLOYMENT.getCode() | AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode()
                                  | AgentMessage.EVENT.getCode() );
        
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
    public void onOK ( Parking panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( Parking panel ) {
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
                    lastestTimeStamp = System.nanoTime();
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
        return Parking.TEXT.getString( "parking" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Parking"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final EventAggregator            ea                  = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );
            final Parking.GeneralBean        bean_general        = new Parking.GeneralBean();
            final Parking.IOSettingsBean     bean_iOSettings     = new Parking.IOSettingsBean();
            final Parking.DcsFanBean         bean_dcsFan         = new Parking.DcsFanBean();
            final Parking.DimDevicesBean     bean_dimDevices     = new Parking.DimDevicesBean();
            final Parking.OpenDoorButtonBean bean_openDoorButton = new Parking.OpenDoorButtonBean();
            final Parking.DcsLightBean       bean_dcsLight       = new Parking.DcsLightBean();
            final Parking.CarCallSettingBean bean_carCallSetting = new Parking.CarCallSettingBean();

            // Initialize internal data.
            initFloorText();

            /* General */
            bean_general.setEnabled( module.pak.isEnabled() );
            bean_general.setReturnFloor( getFloorTextByFloor( module.pak.getReturn_floor() ) );
            bean_general.setLedBehavior( LedBehavior.Led_Behavior.get(module.pak.getLedBehavior()));
            bean_general.setCarMessage( DeviceMessage.get( module.pak.getCar_message() ) );
            bean_general.setHallMessage( DeviceMessage.get( module.pak.getHall_message() ) );
            bean_general.setOpenDoorBeforeGoToReturnFloor( module.pak.isOpen_door_before_go_to_return_floor() );
            bean_general.setOpenDoorOnceArrivalReturnFloor( module.pak.isOpen_door_once_arrival_return_floor() );
            bean_general.setEnabled_edp(module.pak.isEnable_edp());
            bean_general.setEnabled_sgs(module.pak.isEnable_sgs());

            /* IOSettings */
            bean_iOSettings.setParkingSwitchEvent( ea.getEvent( EventID.PARKING_SWITCH.eventID ) );

            /* CarCallSetting */
            bean_carCallSetting.setClearCarCalls( module.pak.isClear_car_call() );
            bean_carCallSetting.setRejectAnyIncommingCarCalls( module.pak.isReject_new_car_call() );

            /* DimDevices */
            bean_dimDevices.setEnabledDimDeviceOnCar( module.pak.isEnable_energy_saving_on_car() );
            bean_dimDevices.setEnabledDimDeviceOnHall( module.pak.isEnable_energy_saving_on_hall() );
            bean_dimDevices.setDimTimer( ( long )module.pak.getEnable_energy_saving_activation_time() );

            /* OpenDoorButton */
            bean_openDoorButton.setEnabledOpenDoorButtonLed( module.pak.isEnabled_open_door_button_led() );
            bean_openDoorButton.setActivationTimer( ( long )module.pak.getOpen_door_button_led_activation_time() );

            /* DcsFan */
            bean_dcsFan.setDisableDcsFan( module.pak.isDisable_cabin_fan() );
            bean_dcsFan.setActivationTimer0( ( long )module.pak.getDisable_cabin_fan_activation_time() );

            /* DcsLight */
            bean_dcsLight.setDisableDcsLight( module.pak.isDisable_cabin_light() );
            bean_dcsLight.setActivationTimer1( ( long )module.pak.getDisable_cabin_light_activation_time() );
            if ( solid == null )
                solid = new Solid( bean_general, bean_iOSettings, bean_carCallSetting, bean_dimDevices, bean_openDoorButton, bean_dcsFan,
                                   bean_dcsLight );

            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setIOSettingsBean( bean_iOSettings );
                    app.setCarCallSettingBean( bean_carCallSetting );
                    app.setDimDevicesBean( bean_dimDevices );
                    app.setOpenDoorButtonBean( bean_openDoorButton );
                    app.setDcsFanBean( bean_dcsFan );
                    app.setDcsLightBean( bean_dcsLight );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            Parking.GeneralBean        bean_general        = app.getGeneralBean();
            Parking.IOSettingsBean     bean_iOSettings     = app.getIOSettingsBean();
            Parking.CarCallSettingBean bean_carCallSetting = app.getCarCallSettingBean();
            Parking.DimDevicesBean     bean_dimDevices     = app.getDimDevicesBean();
            Parking.OpenDoorButtonBean bean_openDoorButton = app.getOpenDoorButtonBean();
            Parking.DcsFanBean         bean_dcsFan         = app.getDcsFanBean();
            Parking.DcsLightBean       bean_dcsLight       = app.getDcsLightBean();
            final EventAggregator      ea                  = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );

            /* General */
            module.pak.setEnabled( bean_general.getEnabled() );
            module.pak.setReturn_floor( bean_general.getReturnFloor().getFloor() );
            module.pak.setLedBehavior( bean_general.getLedBehavior().getCode() );
            module.pak.setCar_message( bean_general.getCarMessage().getCode() );
            module.pak.setHall_message( bean_general.getHallMessage().getCode() );
            module.pak.setOpen_door_before_go_to_return_floor( bean_general.getOpenDoorBeforeGoToReturnFloor() );
            module.pak.setOpen_door_once_arrival_return_floor( bean_general.getOpenDoorOnceArrivalReturnFloor() );
            module.pak.setEnable_edp(bean_general.getEnabled_edp());
            module.pak.setEnable_sgs(bean_general.getEnabled_sgs());
            
            // IOSettings
            ea.setEvent( EventID.PARKING_SWITCH.eventID, bean_iOSettings.getParkingSwitchEvent() );

            // CarCallSetting
            module.pak.setClear_car_call( bean_carCallSetting.getClearCarCalls() );
            module.pak.setReject_new_car_call( bean_carCallSetting.getRejectAnyIncommingCarCalls() );

            // DimDevices
            module.pak.setEnable_energy_saving_on_car( bean_dimDevices.getEnabledDimDeviceOnCar() );
            module.pak.setEnable_energy_saving_on_hall( bean_dimDevices.getEnabledDimDeviceOnHall() );
            module.pak.setEnable_energy_saving_activation_time( bean_dimDevices.getDimTimer().intValue() );

            // OpenDoorButton
            module.pak.setEnabled_open_door_button_led( bean_openDoorButton.getEnabledOpenDoorButtonLed() );
            module.pak.setOpen_door_button_led_activation_time( bean_openDoorButton.getActivationTimer().intValue() );

            // DcsFan
            module.pak.setDisable_cabin_fan( bean_dcsFan.getDisableDcsFan() );
            module.pak.setDisable_cabin_fan_activation_time( bean_dcsFan.getActivationTimer0().intValue() );

            // DcsLight
            module.pak.setDisable_cabin_light( bean_dcsLight.getDisableDcsLight() );
            module.pak.setDisable_cabin_light_activation_time( bean_dcsLight.getActivationTimer1().intValue() );

            // Update Event with OCS Agent.
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
        if ( solid != null )
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    if ( solid != null ) {
                        app.stop();
                        app.setGeneralBean( solid.bean_general );
                        app.setIOSettingsBean( solid.bean_iOSettings );
                        app.setCarCallSettingBean( solid.bean_carCallSetting );
                        app.setDimDevicesBean( solid.bean_dimDevices );
                        app.setOpenDoorButtonBean( solid.bean_openDoorButton );
                        app.setDcsFanBean( solid.bean_dcsFan );
                        app.setDcsLightBean( solid.bean_dcsLight );
                        app.start();
                    }
                }
            } );
    }


    /**
     * Get the floor text by a floor.
     * @param floor     It specifies the floor number. It starts from 0.
     * @return Returns the floor text on success; otherwise, return null.
     */
    protected FloorText getFloorTextByFloor ( int floor ) throws ConvertException {
        if ( floorTexts != null )
            for ( int i = 0 ; i < floorTexts.size() ; i++ )
                if ( floorTexts.get( i ).index == floor )
                    return floorTexts.get( i );
        return null;
    }


    /**
     * Construct the internal table for floor v.s. floor text.
     */
    private void initFloorText () {
        floorTexts = new ArrayList<>( 128 );
        for ( byte i = 0, count = deploy.getFloorCount() ; i < count ; i++ ) {
            int dz = deploy.getDoorZone( i );
            if ( dz != -1 )
                floorTexts.add( new FloorText( i, dz, true, deploy.getFloorText( i ) ) );
        }
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                app.setFloorText( floorTexts );
            }
        } );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private final static class Solid {
        private Parking.GeneralBean        bean_general;
        private Parking.IOSettingsBean     bean_iOSettings;
        private Parking.CarCallSettingBean bean_carCallSetting;
        private Parking.DimDevicesBean     bean_dimDevices;
        private Parking.OpenDoorButtonBean bean_openDoorButton;
        private Parking.DcsFanBean         bean_dcsFan;
        private Parking.DcsLightBean       bean_dcsLight;




        private Solid ( GeneralBean bean_general, IOSettingsBean bean_iOSettings, CarCallSettingBean bean_carCallSetting,
                        DimDevicesBean bean_dimDevices, OpenDoorButtonBean bean_openDoorButton, DcsFanBean bean_dcsFan,
                        DcsLightBean bean_dcsLight ) {
            this.bean_general        = bean_general;
            this.bean_iOSettings     = bean_iOSettings;
            this.bean_carCallSetting = bean_carCallSetting;
            this.bean_dimDevices     = bean_dimDevices;
            this.bean_openDoorButton = bean_openDoorButton;
            this.bean_dcsFan         = bean_dcsFan;
            this.bean_dcsLight       = bean_dcsLight;
        }
    }
}
