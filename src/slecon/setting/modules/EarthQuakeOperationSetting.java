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
import slecon.setting.modules.EarthQuakeOperation.GeneralBean;
import slecon.setting.modules.EarthQuakeOperation.IOSettingsBean;
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
    path      = "Modules::EarthQuake Operation",
    sortIndex = 0x30d
)
public class EarthQuakeOperationSetting extends SettingPanel<EarthQuakeOperation> implements Page, LiftDataChangedListener {

    private static final long serialVersionUID = -2399004013558351140L;

    /**
     * Logger.
     */
    private final Logger   logger = LogManager.getLogger( EarthQuakeOperationSetting.class );

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
     * The handler for Setup - Module - Emergency Power Operation.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public EarthQuakeOperationSetting ( LiftConnectionBean connBean ) {
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
                                      AgentMessage.DEPLOYMENT.getCode() | 
                                      AgentMessage.EVENT.getCode() | 
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
    public void onOK ( EarthQuakeOperation panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( EarthQuakeOperation panel ) {
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
        return EarthQuakeOperation.TEXT.getString( "earthquake_operation" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Earthquake_Operation"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final EventAggregator                         ea           = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );
            final EarthQuakeOperation.GeneralBean    bean_general  = new EarthQuakeOperation.GeneralBean();
            final EarthQuakeOperation.IOSettingsBean bean_io       = new EarthQuakeOperation.IOSettingsBean();

            /* General */
            bean_general.setEnabled( module.eqo.isEnabled() );
            bean_general.setBehavior( LedBehavior.Led_Behavior.get(module.eqo.getLedBehavior()) );
            bean_general.setDoor_close_time( ( long )module.eqo.getDoor_close_timer() );
            bean_general.setFront_buzzer_enable(module.eqo.isFrontBuzzerEnabled());
            bean_general.setRear_buzzer_enable(module.eqo.isRearBuzzerEnabled());
            bean_general.setCarMessage( DeviceMessage.get( module.eqo.getCar_message() ) );
            bean_general.setHallMessage( DeviceMessage.get( module.eqo.getHall_message() ) );

            /* IO setting */
            bean_io.setEqo_P_Ware_Event( ea.getEvent( EventID.EQO_P_WARE.eventID ) );
            bean_io.setEqo_Low_Ware_Event( ea.getEvent( EventID.EQO_LOW_WARE.eventID ) );
            bean_io.setEqo_Low_Ware_Reset_Event( ea.getEvent( EventID.EQO_LOW_WARE_RESET.eventID ) );
            bean_io.setEqoLightEvent( ea.getEvent( EventID.EQO_LIGHT.eventID ));

            if (solid == null)
                solid = new Solid(bean_general, bean_io);
            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setIOSettingsBean( bean_io );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            final EarthQuakeOperation.GeneralBean  bean_general  = app.getGeneralBean();
            final EarthQuakeOperation.IOSettingsBean bean_io     = app.getIOSettingsBean();
            final EventAggregator                      ea            = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );


            /* General */
            module.eqo.setEnabled( bean_general.getEnabled() );
            module.eqo.setLedBehavior( bean_general.getBehavior().getCode() );
            module.eqo.setDoor_close_timer( bean_general.getDoor_close_time().byteValue() );
            module.eqo.setFrontBuzzerEnabled( bean_general.getFront_buzzer_enable() );
            module.eqo.setRearBuzzerEnabled( bean_general.getRear_buzzer_enable() );
            module.eqo.setCar_message( bean_general.getCarMessage().getCode() );
            module.eqo.setHall_message( bean_general.getHallMessage().getCode() );
            module.commit();

            /* IO setting */
            ea.setEvent( EventID.EQO_P_WARE.eventID, bean_io.getEqo_P_Ware_Event() );
            ea.setEvent( EventID.EQO_LOW_WARE.eventID, bean_io.getEqo_Low_Ware_Event() );
            ea.setEvent( EventID.EQO_LOW_WARE_RESET.eventID, bean_io.getEqo_Low_Ware_Reset_Event() );
            ea.setEvent(EventID.EQO_LIGHT.eventID, bean_io.getEqoLightEvent() );
            /* Update Event with OCS Agent. */
            event.setEvent( ea.toByteArray( this.connBean ) );
            event.setInstalledDevices( ea.getInstalledDevices() );
            event.commit();
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
                    app.setIOSettingsBean( solid.bean_io );
                    app.start();
                }
            } );
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public class Solid {
        private EarthQuakeOperation.GeneralBean  bean_general;
        private EarthQuakeOperation.IOSettingsBean bean_io;

        public Solid ( GeneralBean bean_general, IOSettingsBean bean_io ) {
            super();
            this.bean_general  = bean_general;
            this.bean_io  = bean_io;
        }
    }
}
