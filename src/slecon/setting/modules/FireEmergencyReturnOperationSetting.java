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
import slecon.setting.modules.FireEmergencyReturnOperation.DcsFanBean;
import slecon.setting.modules.FireEmergencyReturnOperation.DcsLightBean;
import slecon.setting.modules.FireEmergencyReturnOperation.DimDevicesBean;
import slecon.setting.modules.FireEmergencyReturnOperation.GeneralBean;
import slecon.setting.modules.FireEmergencyReturnOperation.IOSettingsBean;
import slecon.setting.modules.FireEmergencyReturnOperation.StrategyBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.Parser_Event;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




/**
 * Data of binding bean for Setup -> Module -> Fire Emergency Return Operation.
 */
@SetupView(
    path      = "Modules::Fire Emergency Return Operation",
    sortIndex = 0x306
)
public class FireEmergencyReturnOperationSetting extends SettingPanel<FireEmergencyReturnOperation> implements Page, LiftDataChangedListener {

    private static final long serialVersionUID = -2784778540191731025L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( AutoReturnSetting.class );

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid      solid  = null;

    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;
    
    // TODO
    private Parser_Error       error;
    
    private Parser_Module      module;
    private Parser_Deploy      deploy;
    private Parser_Event       event;

    /**
     * A floor v.s. floor text mapping table.
     */
    private ArrayList<FloorText> floorTexts;




    /**
     *  The handler for Setup - Module - Fire Emergency Return.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public FireEmergencyReturnOperationSetting ( LiftConnectionBean connBean ) {
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
            event  = new Parser_Event( connBean.getIp(), connBean.getPort() );
            module = new Parser_Module( connBean.getIp(), connBean.getPort() );
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
    public void onOK ( FireEmergencyReturnOperation panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( FireEmergencyReturnOperation panel ) {
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
        return FireEmergencyReturnOperation.TEXT.getString( "fire_emergency_return_operation" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Fire_Emergency_Return_Operation"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final FireEmergencyReturnOperation.GeneralBean    bean_general    = new FireEmergencyReturnOperation.GeneralBean();
            final FireEmergencyReturnOperation.IOSettingsBean bean_iOSettings = new FireEmergencyReturnOperation.IOSettingsBean();
            final FireEmergencyReturnOperation.DimDevicesBean bean_dimDevices = new FireEmergencyReturnOperation.DimDevicesBean();
            final FireEmergencyReturnOperation.DcsFanBean     bean_dcsFan     = new FireEmergencyReturnOperation.DcsFanBean();
            final FireEmergencyReturnOperation.DcsLightBean   bean_dcsLight   = new FireEmergencyReturnOperation.DcsLightBean();
            final FireEmergencyReturnOperation.StrategyBean   bean_strategy    = new FireEmergencyReturnOperation.StrategyBean();
            final EventAggregator                             ea              = EventAggregator.toEventAggregator( event.getEvent() );
            
            
            // Initialize internal data.
            initFloorText();

            /* General */
            bean_general.setEnabled( module.fro.isEnabled() );
            bean_general.setReturnFloor( getFloorTextByFloor( module.fro.getReturn_floor() ) );
            bean_general.setLedBehavior( LedBehavior.Led_Behavior.get(module.fro.getLedBehavior()));
            bean_general.setDoorCloseTimer( (long)module.fro.getDoor_Close_timer());
            bean_general.setCarMessage( comm.constants.DeviceMessage.get( module.fro.getCar_message() ) );
            bean_general.setHallMessage( comm.constants.DeviceMessage.get( module.fro.getHall_message() ) );
            bean_general.setEdp_enable(module.fro.isEnable_edp());
            bean_general.setSgs_enable(module.fro.isEnable_sgs());
            bean_general.setFront_buzzer_enable(module.fro.isEnable_front_buzzer());
            bean_general.setRear_buzzer_enable(module.fro.isEnable_rear_buzzer());
            
            /* IOSettings */
            bean_iOSettings.setFroSwitchEvent( ea.getEvent( EventID.FRO.eventID ) );

            // DimDevices */
            bean_dimDevices.setEnabledDimDeviceOnCar( module.fro.isEnable_energy_saving_on_car() );
            bean_dimDevices.setEnabledDimDeviceOnHall( module.fro.isEnable_energy_saving_on_hall() );
            bean_dimDevices.setDimTimer( ( long )module.fro.getEnable_energy_saving_activation_time() );

            /* DcsFan */
            bean_dcsFan.setDisableDcsFan( module.fro.isDisable_cabin_fan() );
            bean_dcsFan.setActivationTimer( ( long )module.fro.getDisable_cabin_fan_activation_time() );

            /* DcsLight */
            bean_dcsLight.setDisableDcsLight( module.fro.isDisable_cabin_light() );
            bean_dcsLight.setActivationTimer0( ( long )module.fro.getDisable_cabin_light_activation_time() );
            
            /* Strategy */
            bean_strategy.setEnabledStragetyA( module.fro.getRunStrategy() == 1? true : false );
            bean_strategy.setEnabledStragetyB( module.fro.getRunStrategy() == 2? true : false );
            
            if ( solid == null )
                solid = new Solid( bean_general, bean_iOSettings, bean_dimDevices, bean_dcsFan, bean_dcsLight, bean_strategy );

            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setDcsFanBean( bean_dcsFan );
                    app.setDimDevicesBean( bean_dimDevices );
                    app.setDcsLightBean( bean_dcsLight );
                    app.setIOSettingsBean( bean_iOSettings );
                    app.setStrategyBean(bean_strategy);
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            final FireEmergencyReturnOperation.GeneralBean    bean_general    = app.getGeneralBean();
            final FireEmergencyReturnOperation.IOSettingsBean bean_iOSettings = app.getIOSettingsBean();
            final FireEmergencyReturnOperation.DimDevicesBean bean_dimDevices = app.getDimDevicesBean();
            final FireEmergencyReturnOperation.DcsFanBean     bean_dcsFan     = app.getDcsFanBean();
            final FireEmergencyReturnOperation.DcsLightBean   bean_dcsLight   = app.getDcsLightBean();
            final FireEmergencyReturnOperation.StrategyBean   bean_strategy    = app.getStrategyBean();
            final EventAggregator                             ea              = EventAggregator.toEventAggregator( event.getEvent() );

            // General
            module.fro.setEnabled( bean_general.getEnabled() );
            module.fro.setReturn_floor( bean_general.getReturnFloor().getFloor() );
            module.fro.setLedBehavior( bean_general.getLedBehavior().getCode() );
            module.fro.setDoor_Close_timer( bean_general.getDoorCloseTimer().intValue() );
            module.fro.setCar_message( bean_general.getCarMessage().getCode() );
            module.fro.setHall_message( bean_general.getHallMessage().getCode() );
            module.fro.setEnable_edp(bean_general.getEdp_enable());
            module.fro.setEnable_sgs(bean_general.getSgs_enable());
            module.fro.setEnable_front_buzzer(bean_general.getFront_buzzer_enable());
            module.fro.setEnable_rear_buzzer(bean_general.getRear_buzzer_enable());
            // IOSettings
            ea.setEvent( EventID.FRO.eventID, bean_iOSettings.getFroSwitchEvent() );

            // DimDevices
            module.fro.setEnable_energy_saving_on_car( bean_dimDevices.getEnabledDimDeviceOnCar() );
            module.fro.setEnable_energy_saving_on_hall( bean_dimDevices.getEnabledDimDeviceOnHall() );
            module.fro.setEnable_energy_saving_activation_time( bean_dimDevices.getDimTimer().intValue() );

            // DcsFan
            module.fro.setDisable_cabin_fan( bean_dcsFan.getDisableDcsFan() );
            module.fro.setDisable_cabin_fan_activation_time( bean_dcsFan.getActivationTimer().intValue() );

            // DcsLight
            module.fro.setDisable_cabin_light( bean_dcsLight.getDisableDcsLight() );
            module.fro.setDisable_cabin_light_activation_time( bean_dcsLight.getActivationTimer0().intValue() );
            
            // Strategy
            if ( bean_strategy.getEnabledStragetyA() ) {
                module.fro.setRunStrategy( (byte)1 );
            } else if ( bean_strategy.getEnabledStragetyB() ) {
                module.fro.setRunStrategy( (byte)2 );
            }

            // Update Event with OCS Agent.
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
                        app.setDcsFanBean( solid.bean_dcsFan );
                        app.setDimDevicesBean( solid.bean_dimDevices );
                        app.setDcsLightBean( solid.bean_dcsLight );
                        app.setIOSettingsBean( solid.bean_iOSettings );
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
    protected FloorText getFloorTextByFloor ( int floor ) {
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
        private FireEmergencyReturnOperation.GeneralBean    bean_general;
        private FireEmergencyReturnOperation.IOSettingsBean bean_iOSettings;
        private FireEmergencyReturnOperation.DimDevicesBean bean_dimDevices;
        private FireEmergencyReturnOperation.DcsFanBean     bean_dcsFan;
        private FireEmergencyReturnOperation.DcsLightBean   bean_dcsLight;
        private FireEmergencyReturnOperation.StrategyBean   bean_stategy;

        private Solid ( GeneralBean bean_general, IOSettingsBean bean_iOSettings, DimDevicesBean bean_dimDevices, DcsFanBean bean_dcsFan,
                        DcsLightBean bean_dcsLight, StrategyBean bean_stategy ) {
            super();
            this.bean_general    = bean_general;
            this.bean_iOSettings = bean_iOSettings;
            this.bean_dimDevices = bean_dimDevices;
            this.bean_dcsFan     = bean_dcsFan;
            this.bean_dcsLight   = bean_dcsLight;
            this.bean_stategy    = bean_stategy;
        }
    }
}
