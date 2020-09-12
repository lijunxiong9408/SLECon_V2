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
import slecon.setting.modules.FaultStateGroupSuspension.GeneralBean;
import slecon.setting.modules.FaultStateGroupSuspension.StrategyBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Error;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




/**
 * Data of binding bean for Setup -> Module -> Fault State Group Suspension.
 */
@SetupView(
    path      = "Modules::Fault State Group Suspension",
    sortIndex = 0x30c
)
public class FaultStateGroupSuspensionSetting extends SettingPanel<FaultStateGroupSuspension> implements Page, LiftDataChangedListener {

    private static final long serialVersionUID = -4388100753797992562L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( FaultStateGroupSuspensionSetting.class );

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
     * The handler for Setup - Module - Fault State Group Suspension.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public FaultStateGroupSuspensionSetting ( LiftConnectionBean connBean ) {
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
    public void onOK ( FaultStateGroupSuspension panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( FaultStateGroupSuspension panel ) {
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
        final String string = FaultStateGroupSuspension.TEXT.getString( "fault_state_group_suspension" );
        return string;
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Fault_State_Group_Suspension"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final FaultStateGroupSuspension.GeneralBean  bean_general  = new FaultStateGroupSuspension.GeneralBean();
            final FaultStateGroupSuspension.StrategyBean bean_strategy = new FaultStateGroupSuspension.StrategyBean();

            // General
            bean_general.setEnabled( module.ngp.isEnabled() );
            bean_general.setEnabledFrontBuzzer( module.ngp.isEnable_front_buzzer() );
            bean_general.setEnabledRearBuzzer( module.ngp.isEnable_rear_buzzer() );
            bean_general.setTimerToActivateModule( ( long )module.ngp.getActivation_time() );

            // Strategy
            bean_strategy.setScheduleNextCall( module.ngp.getScheme() == 0 );
            bean_strategy.setGoToNearestFloor( module.ngp.getScheme() == 1 );
            if ( solid == null )
                solid = new Solid( bean_general, bean_strategy );

            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setStrategyBean( bean_strategy );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            // General
            FaultStateGroupSuspension.GeneralBean  bean_general  = app.getGeneralBean();
            FaultStateGroupSuspension.StrategyBean bean_strategy = app.getStrategyBean();
            module.ngp.setEnabled( bean_general.getEnabled() );
            module.ngp.setEnable_front_buzzer( bean_general.getEnabledFrontBuzzer() );
            module.ngp.setEnable_rear_buzzer( bean_general.getEnabledRearBuzzer() );
            module.ngp.setActivation_time( bean_general.getTimerToActivateModule().intValue() );

            // Strategy
            module.ngp.setScheme( ( byte )( bean_strategy.getScheduleNextCall() ? 0 : 1 ) );
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
                    app.setStrategyBean( solid.bean_strategy );
                    app.start();
                }
            } );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private final static class Solid {
        private FaultStateGroupSuspension.GeneralBean  bean_general;
        private FaultStateGroupSuspension.StrategyBean bean_strategy;




        private Solid ( GeneralBean bean_general, StrategyBean bean_strategy ) {
            super();
            this.bean_general  = bean_general;
            this.bean_strategy = bean_strategy;
        }
    }
}
