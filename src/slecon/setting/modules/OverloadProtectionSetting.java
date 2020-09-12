package slecon.setting.modules;
import static logic.util.SiteManagement.MON_MGR;

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
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.modules.OverloadProtection.GeneralBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Error;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.constants.DeviceMessage;
import comm.event.LiftDataChangedListener;



@SetupView(
    path      = "Modules::Overload protection",
    sortIndex = 0x30b
)
public class OverloadProtectionSetting extends SettingPanel<OverloadProtection> implements Page, LiftDataChangedListener {

    private static final long serialVersionUID = 8446919292916774421L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( OverloadProtectionSetting.class );

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid      solid  = null;

    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;

    // TODO
    private Parser_Error  error;
    
    private Parser_Module module;




    /**
     * The handler for Setup - Module - Overload protection.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public OverloadProtectionSetting ( LiftConnectionBean connBean ) {
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
                                      AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() );
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
                                  AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() );
        
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
    public void onOK ( OverloadProtection panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( OverloadProtection panel ) {
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
        return OverloadProtection.TEXT.getString( "overload_protection" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Overload_protection"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final OverloadProtection.GeneralBean bean_general = new OverloadProtection.GeneralBean();

            // General
            bean_general.setEnabled( module.olp.isEnabled() );
            bean_general.setPercentage( ( long )module.olp.getPercentage() );
            bean_general.setCarMessage( DeviceMessage.get( module.olp.getCar_message() ) );
            bean_general.setHallMessage( DeviceMessage.get( module.olp.getHall_message() ) );
            bean_general.setEnabledFrontBuzzer( module.olp.isEnable_front_buzzer() );
            bean_general.setEnabledRearBuzzer( module.olp.isEnable_rear_buzzer() );
            bean_general.setClearCarCalls( module.olp.isClear_car_calls() );
            bean_general.setClearCarCallsTimer( ( long )module.olp.getClear_car_calls_activation_time() );
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


    public boolean submit () {
        try {
            OverloadProtection.GeneralBean bean_general = app.getGeneralBean();

            // General
            module.olp.setEnabled( bean_general.getEnabled() );
            module.olp.setPercentage( bean_general.getPercentage().intValue() );
            module.olp.setCar_message( bean_general.getCarMessage().getCode() );
            module.olp.setHall_message( bean_general.getHallMessage().getCode() );
            module.olp.setEnable_front_buzzer( bean_general.getEnabledFrontBuzzer() );
            module.olp.setEnable_rear_buzzer( bean_general.getEnabledRearBuzzer() );
            module.olp.setClear_car_calls( bean_general.getClearCarCalls() );
            module.olp.setClear_car_calls_activation_time( bean_general.getClearCarCallsTimer().intValue() );
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
        private OverloadProtection.GeneralBean bean_general;




        public Solid ( GeneralBean bean_general ) {
            super();
            this.bean_general = bean_general;
        }
    }
}
