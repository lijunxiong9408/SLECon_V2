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
import logic.connection.LiftConnectionBean;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.ConvertException;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.modules.EnergySaving.ArrivalEnergySaveBean;
import slecon.setting.modules.EnergySaving.DcsFanBean;
import slecon.setting.modules.EnergySaving.DcsLightBean;
import slecon.setting.modules.EnergySaving.DimDevicesBean;
import slecon.setting.modules.EnergySaving.OpenDoorButtonBean;
import slecon.setting.modules.LedBehavior.Led_Behavior;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




/**
 * Data of binding bean for Setup -> Module -> Auto Return.
 */
@SetupView(
    path      = "Modules::Energy Saving",
    sortIndex = 0x0311
)
public class EnergySavingSetting extends SettingPanel<EnergySaving> implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = 9097571441531736737L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( EnergySavingSetting.class );

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

    /**
     * A floor v.s. floor text mapping table.
     */
    private ArrayList<FloorText> floorTexts;

    /**
     * The handler for Setup - Module - Auto Return.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public EnergySavingSetting ( LiftConnectionBean connBean ) {
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
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.DEPLOYMENT.getCode() | AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() );
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
    public void onOK ( EnergySaving panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( EnergySaving panel ) {
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
        return EnergySaving.TEXT.getString( "energy_saving" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Energy_Saving"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final EnergySaving.DimDevicesBean     bean_dimDevices     = new EnergySaving.DimDevicesBean();
            final EnergySaving.OpenDoorButtonBean bean_openDoorButton = new EnergySaving.OpenDoorButtonBean();
            final EnergySaving.DcsFanBean         bean_dcsFan         = new EnergySaving.DcsFanBean();
            final EnergySaving.DcsLightBean       bean_dcsLight       = new EnergySaving.DcsLightBean();
            final EnergySaving.ArrivalEnergySaveBean    bean_arrival     = new EnergySaving.ArrivalEnergySaveBean();

            /* Energy saving. */
            bean_dimDevices.setEnabledDimDeviceOnCar( module.aro.isEnable_energy_saving_on_car() );
            bean_dimDevices.setEnabledDimDeviceOnHall( module.aro.isEnable_energy_saving_on_hall() );
            bean_dimDevices.setDimTimer( ( long )module.aro.getEnable_energy_saving_activation_time() );

            /* OpenDoorButton */
            bean_openDoorButton.setEnabledOpenDoorButtonLed( module.aro.isEnabled_open_door_button_led() );
            bean_openDoorButton.setActivationTimer0( ( long )module.aro.getOpen_door_button_led_activation_time() );
            bean_openDoorButton.setLed_behavoir( LedBehavior.Led_Behavior.get(module.aro.getLedBehavior()));
            

            /* DcsFan */
            bean_dcsFan.setDisableDcsFan( module.aro.isDisable_cabin_fan() );
            bean_dcsFan.setActivationTimer1( ( long )module.aro.getDisable_cabin_fan_activation_time() );

            // DcsLight
            bean_dcsLight.setDisableDcsLight( module.aro.isDisable_cabin_light() );
            bean_dcsLight.setActivationTimer2( ( long )module.aro.getDisable_cabin_light_activation_time() );
            
            //Arrival Stop Energy saving
            bean_arrival.setEnable(module.rds.getEnable());
            
            if ( solid == null )
                solid = new Solid( bean_dimDevices, bean_openDoorButton, bean_dcsFan, bean_dcsLight,bean_arrival );
            
            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    if ( solid != null ) {
                        app.stop();
                        app.setDimDevicesBean( bean_dimDevices );
                        app.setOpenDoorButtonBean( bean_openDoorButton );
                        app.setDcsFanBean( bean_dcsFan );
                        app.setDcsLightBean( bean_dcsLight );
                        app.setArrivalEnergySaveBean(bean_arrival);
                        app.start();
                    }
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            final EnergySaving.DimDevicesBean     bean_dimDevices     = app.getDimDevicesBean();
            final EnergySaving.OpenDoorButtonBean bean_openDoorButton = app.getOpenDoorButtonBean();
            final EnergySaving.DcsFanBean         bean_dcsFan         = app.getDcsFanBean();
            final EnergySaving.DcsLightBean       bean_dcsLight       = app.getDcsLightBean();
            final EnergySaving.ArrivalEnergySaveBean    bean_arrival     = app.getArrivalEnergySaveBean();

            /* Energy saving. */
            module.aro.setEnable_energy_saving_on_car( bean_dimDevices.getEnabledDimDeviceOnCar() );
            module.aro.setEnable_energy_saving_on_hall( bean_dimDevices.getEnabledDimDeviceOnHall() );
            module.aro.setEnable_energy_saving_activation_time( bean_dimDevices.getDimTimer().intValue() );

            /* Open door button LED. */
            module.aro.setEnabled_open_door_button_led( bean_openDoorButton.getEnabledOpenDoorButtonLed() );
            module.aro.setOpen_door_button_led_activation_time( bean_openDoorButton.getActivationTimer0().intValue() );
            module.aro.setLedBehavior( bean_openDoorButton.getLed_behavoir().getCode() );

            /* Cabin fan. */
            module.aro.setDisable_cabin_fan( bean_dcsFan.getDisableDcsFan() );
            module.aro.setDisable_cabin_fan_activation_time( bean_dcsFan.getActivationTimer1().intValue() );

            /* Cabin light. */
            module.aro.setDisable_cabin_light( bean_dcsLight.getDisableDcsLight() );
            module.aro.setDisable_cabin_light_activation_time( bean_dcsLight.getActivationTimer2().intValue() );
            
            module.rds.setEnable((byte)(bean_arrival.getEnable()?1:0));
            module.commit();
            return true;
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
            logger.catching( Level.FATAL, e );
        }
        return false;
    }


    /**
     * Get the floor text by a floor.
     * @param floor     It specifies the floor number. It starts from 0.
     * @return Returns the floor text on success; otherwise, return null.
     */
    protected final FloorText getFloorTextByFloor ( int floor ) {
        if ( floorTexts != null )
            for ( int i = 0 ; i < floorTexts.size() ; i++ )
                if ( floorTexts.get( i ).index == floor )
                    return floorTexts.get( i );
        return null;
    }


    public void reset () {
        // Update returned data to visualization components.
        if ( solid != null )
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    if ( solid != null ) {
                        app.stop();
                        app.setDimDevicesBean( solid.bean_dimDevices );
                        app.setOpenDoorButtonBean( solid.bean_openDoorButton );
                        app.setDcsFanBean( solid.bean_dcsFan );
                        app.setDcsLightBean( solid.bean_dcsLight );
                        app.setArrivalEnergySaveBean(solid.bean_arrival);
                        app.start();
                    }
                }
            } );
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private final static class Solid {
        private final EnergySaving.DimDevicesBean     bean_dimDevices;
        private final EnergySaving.OpenDoorButtonBean bean_openDoorButton;
        private final EnergySaving.DcsFanBean         bean_dcsFan;
        private final EnergySaving.DcsLightBean       bean_dcsLight;
        private final EnergySaving.ArrivalEnergySaveBean    bean_arrival; 




        private Solid ( DimDevicesBean bean_dimDevices, OpenDoorButtonBean bean_openDoorButton,
                        DcsFanBean bean_dcsFan, DcsLightBean bean_dcsLight, ArrivalEnergySaveBean    bean_arrival ) {
            super();
            this.bean_dimDevices     = bean_dimDevices;
            this.bean_openDoorButton = bean_openDoorButton;
            this.bean_dcsFan         = bean_dcsFan;
            this.bean_dcsLight       = bean_dcsLight;
            this.bean_arrival 		 = bean_arrival;
        }
    }
}
