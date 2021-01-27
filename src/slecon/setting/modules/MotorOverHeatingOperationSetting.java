package slecon.setting.modules;
import static logic.util.SiteManagement.MON_MGR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.EventID;
import logic.connection.LiftConnectionBean;
import ocsjava.remote.configuration.EventAggregator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.modules.MotorOverHeatingOperation.DcsFanBean;
import slecon.setting.modules.MotorOverHeatingOperation.DcsLightBean;
import slecon.setting.modules.MotorOverHeatingOperation.DimDevicesBean;
import slecon.setting.modules.MotorOverHeatingOperation.GeneralBean;

import comm.Parser_Error;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




/**
 * Data of binding bean for Setup -> Module -> Temperature Detector Emergency Operation.
 */
@SetupView(
    path      = "Modules::Temperature Detector Emergency Operation",
    sortIndex = 0x30f
)
public class MotorOverHeatingOperationSetting extends SettingPanel<MotorOverHeatingOperation> implements Page, LiftDataChangedListener {

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
    /**
     * A floor v.s. floor text mapping table.
     */
    private ArrayList<FloorText> floorTexts;




    /**
     *  The handler for Setup - Module - Fire Emergency Return.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public MotorOverHeatingOperationSetting ( LiftConnectionBean connBean ) {
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
    public void onOK ( MotorOverHeatingOperation panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( MotorOverHeatingOperation panel ) {
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
        return MotorOverHeatingOperation.TEXT.getString( "Motor_overheating_operation" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Motor_OverHeating_Operation"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final MotorOverHeatingOperation.GeneralBean    bean_general    = new MotorOverHeatingOperation.GeneralBean();
            final MotorOverHeatingOperation.DimDevicesBean bean_dimDevices = new MotorOverHeatingOperation.DimDevicesBean();
            final MotorOverHeatingOperation.DcsFanBean     bean_dcsFan     = new MotorOverHeatingOperation.DcsFanBean();
            final MotorOverHeatingOperation.DcsLightBean   bean_dcsLight   = new MotorOverHeatingOperation.DcsLightBean();

            /* General */
            bean_general.setEnabled( module.moh.isEnabled() );
            bean_general.setBehavior( LedBehavior.Led_Behavior.get(module.moh.getLedBehavior()) );
            bean_general.setDoor_close_time( ( long )module.moh.getDoor_close_timer() );
            bean_general.setCarMessage( comm.constants.DeviceMessage.get( module.moh.getCar_message() ) );
            bean_general.setHallMessage( comm.constants.DeviceMessage.get( module.moh.getHall_message() ) );
            bean_general.setFront_buzzer_enable(module.moh.isEnable_front_buzzer());
            bean_general.setRear_buzzer_enable(module.moh.isEnable_rear_buzzer());
            
            // DimDevices */
            bean_dimDevices.setEnabledDimDeviceOnCar( module.moh.isEnable_energy_saving_on_car() );
            bean_dimDevices.setEnabledDimDeviceOnHall( module.moh.isEnable_energy_saving_on_hall() );
            bean_dimDevices.setDimTimer( ( long )module.moh.getEnable_energy_saving_activation_time() );

            /* DcsFan */
            bean_dcsFan.setDisableDcsFan( module.moh.isDisable_cabin_fan() );
            bean_dcsFan.setActivationTimer( ( long )module.moh.getDisable_cabin_fan_activation_time() );

            /* DcsLight */
            bean_dcsLight.setDisableDcsLight( module.moh.isDisable_cabin_light() );
            bean_dcsLight.setActivationTimer0( ( long )module.moh.getDisable_cabin_light_activation_time() );
            if ( solid == null )
                solid = new Solid( bean_general, bean_dimDevices, bean_dcsFan, bean_dcsLight );

            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setDcsFanBean( bean_dcsFan );
                    app.setDimDevicesBean( bean_dimDevices );
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
            final MotorOverHeatingOperation.GeneralBean    bean_general    = app.getGeneralBean();
            final MotorOverHeatingOperation.DimDevicesBean bean_dimDevices = app.getDimDevicesBean();
            final MotorOverHeatingOperation.DcsFanBean     bean_dcsFan     = app.getDcsFanBean();
            final MotorOverHeatingOperation.DcsLightBean   bean_dcsLight   = app.getDcsLightBean();

            // General
            module.moh.setEnabled( bean_general.getEnabled() );
            module.moh.setLedBehavior( bean_general.getBehavior().getCode() );
            module.moh.setDoor_close_timer( bean_general.getDoor_close_time().byteValue() );
            module.moh.setCar_message( bean_general.getCarMessage().getCode() );
            module.moh.setHall_message( bean_general.getHallMessage().getCode() );
            module.moh.setEnable_front_buzzer(bean_general.getFront_buzzer_enable());
            module.moh.setEnable_rear_buzzer(bean_general.getRear_buzzer_enable());
            
            // DimDevices
            module.moh.setEnable_energy_saving_on_car( bean_dimDevices.getEnabledDimDeviceOnCar() );
            module.moh.setEnable_energy_saving_on_hall( bean_dimDevices.getEnabledDimDeviceOnHall() );
            module.moh.setEnable_energy_saving_activation_time( bean_dimDevices.getDimTimer().intValue() );

            // DcsFan
            module.moh.setDisable_cabin_fan( bean_dcsFan.getDisableDcsFan() );
            module.moh.setDisable_cabin_fan_activation_time( bean_dcsFan.getActivationTimer().intValue() );

            // DcsLight
            module.moh.setDisable_cabin_light( bean_dcsLight.getDisableDcsLight() );
            module.moh.setDisable_cabin_light_activation_time( bean_dcsLight.getActivationTimer0().intValue() );

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

    ///////////////////////////////////////////////////////////////////////////////////////
    private final static class Solid {
        private MotorOverHeatingOperation.GeneralBean    bean_general;
        private MotorOverHeatingOperation.DimDevicesBean bean_dimDevices;
        private MotorOverHeatingOperation.DcsFanBean     bean_dcsFan;
        private MotorOverHeatingOperation.DcsLightBean   bean_dcsLight;




        private Solid ( GeneralBean bean_general, DimDevicesBean bean_dimDevices, DcsFanBean bean_dcsFan,
                        DcsLightBean bean_dcsLight ) {
            super();
            this.bean_general    = bean_general;
            this.bean_dimDevices = bean_dimDevices;
            this.bean_dcsFan     = bean_dcsFan;
            this.bean_dcsLight   = bean_dcsLight;
        }
    }
}
