package slecon.setting.modules;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
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
import slecon.setting.modules.EmergencyBatteryOperation.GeneralBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import comm.Parser_Error;
import comm.Parser_Event;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.constants.DeviceMessage;
import comm.event.LiftDataChangedListener;


/**
 * Data of binding bean for Setup -> Module -> EarthQuake Operation.
 */
@SetupView(
    path      = "Modules::Emergency Battery Operation",
    sortIndex = 0x30d
)
public class EmergencyBatteryOperationSetting extends SettingPanel<EmergencyBatteryOperation> implements Page, LiftDataChangedListener {

    private static final long serialVersionUID = -2399004013558351140L;

    /**
     * Logger.
     */
    private final Logger   logger = LogManager.getLogger( EmergencyBatteryOperationSetting.class );

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

    /**
     * The handler for Setup - Module - Emergency Battery Operation.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public EmergencyBatteryOperationSetting ( LiftConnectionBean connBean ) {
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
                                      AgentMessage.DEPLOYMENT.getCode() | 
                                      AgentMessage.MODULE.getCode() | 
                                      AgentMessage.ERROR.getCode() );
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
                                  AgentMessage.DEPLOYMENT.getCode() | 
                                  AgentMessage.EVENT.getCode() | 
                                  AgentMessage.MODULE.getCode() | 
                                  AgentMessage.ERROR.getCode() );

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
    public void onOK ( EmergencyBatteryOperation panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( EmergencyBatteryOperation panel ) {
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
        return EmergencyBatteryOperation.TEXT.getString( "EmergencyBatteryOperation" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Emergency_Battery_Operation"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final EmergencyBatteryOperation.GeneralBean    bean_general  = new EmergencyBatteryOperation.GeneralBean();

            /* General */
            bean_general.setEnabled( module.epb.isEnabled() );
            bean_general.setBehavior( LedBehavior.Led_Behavior.get(module.epb.getLedBehavior()) );
            bean_general.setDoor_close_time( ( long )module.epb.getDoor_close_timer() );
            bean_general.setFront_buzzer_enable(module.epb.isFrontBuzzerEnabled());
            bean_general.setRear_buzzer_enable(module.epb.isRearBuzzerEnabled());
            bean_general.setCarMessage( DeviceMessage.get( module.epb.getCar_message() ) );
            bean_general.setHallMessage( DeviceMessage.get( module.epb.getHall_message() ) );

            if (solid == null)
                solid = new Solid(bean_general);
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


    public boolean submit () {
        try {
            final EmergencyBatteryOperation.GeneralBean  bean_general  = app.getGeneralBean();

            /* General */
            module.epb.setEnabled( bean_general.getEnabled() );
            module.epb.setLedBehavior( bean_general.getBehavior().getCode() );
            module.epb.setDoor_close_timer( bean_general.getDoor_close_time().byteValue() );
            module.epb.setFrontBuzzerEnabled( bean_general.getFront_buzzer_enable() );
            module.epb.setRearBuzzerEnabled( bean_general.getRear_buzzer_enable() );
            module.epb.setCar_message( bean_general.getCarMessage().getCode() );
            module.epb.setHall_message( bean_general.getHallMessage().getCode() );
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
                    app.stop();
                    app.setGeneralBean( solid.bean_general );
                    app.start();
                }
            } );
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public class Solid {
        private EmergencyBatteryOperation.GeneralBean  bean_general;

        public Solid ( GeneralBean bean_general ) {
            super();
            this.bean_general  = bean_general;
        }
    }
}
