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
import slecon.setting.modules.IndependentService.GeneralBean;
import slecon.setting.modules.IndependentService.IOSettingsBean;
import slecon.setting.modules.IndependentService.StrategyBean;

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
 * Data of binding bean for Setup -> Module -> Independent Service.
 */
@SetupView(
    path      = "Modules::Independent Services",
    sortIndex = 0x305
)
public class IndependentServiceSetting extends SettingPanel<IndependentService> implements Page, LiftDataChangedListener {

    private static final long serialVersionUID = 4698061164588617425L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( IndependentServiceSetting.class );

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
    private Parser_Event       event;




    /**
     * The handler for Setup - Module - Independent Services.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public IndependentServiceSetting ( LiftConnectionBean connBean ) {
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
            event = new Parser_Event( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                    AgentMessage.EVENT.getCode() | AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() );
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
                AgentMessage.EVENT.getCode() | AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() );
        
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
    public void onOK ( IndependentService panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( IndependentService panel ) {
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
        return IndependentService.TEXT.getString( "independent_service" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Independent_Services"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final IndependentService.GeneralBean    bean_general    = new IndependentService.GeneralBean();
            final IndependentService.IOSettingsBean bean_iOSettings = new IndependentService.IOSettingsBean();
            final IndependentService.StrategyBean   bean_strategy    = new IndependentService.StrategyBean();
            final EventAggregator                   ea              = EventAggregator.toEventAggregator( event.getEvent() );

            // General
            bean_general.setEnabled( module.isc.isEnabled() );
            bean_general.setCarMessage( DeviceMessage.get( module.isc.getCar_message() ) );
            bean_general.setHallMessage( DeviceMessage.get( module.isc.getHall_message() ) );

            // IOSettings
            bean_iOSettings.setIscSwitchEvent( ea.getEvent( EventID.ISC_FRONT.eventID ) );
            if ( solid == null )
                solid = new Solid( bean_general, bean_iOSettings, bean_strategy );
            
            /* Strategy */
            bean_strategy.setEnabledStragetyA( module.isc.getRunStrategy() == 1? true : false );
            bean_strategy.setEnabledStragetyB( module.isc.getRunStrategy() == 2? true : false );
            
            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setIOSettingsBean( bean_iOSettings );
                    app.setStrategyBean(bean_strategy);
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    private boolean submit () {
        try {
            final IndependentService.GeneralBean    bean_general    = app.getGeneralBean();
            final IndependentService.IOSettingsBean bean_iOSettings = app.getIOSettingsBean();
            final IndependentService.StrategyBean   bean_strategy   = app.getStrategyBean();
            final EventAggregator                   ea              = EventAggregator.toEventAggregator( event.getEvent() );

            /* General */
            module.isc.setEnabled( bean_general.getEnabled() );
            module.isc.setCar_message( bean_general.getCarMessage().getCode() );
            module.isc.setHall_message( bean_general.getHallMessage().getCode() );
            
            // Strategy
            if ( bean_strategy.getEnabledStragetyA() ) {
            	module.isc.setRunStrategy( (byte)1 );
            } else if ( bean_strategy.getEnabledStragetyB() ) {
            	module.isc.setRunStrategy( (byte)2 );
            }
            
            /* IOSettings */
            ea.setEvent( EventID.ISC_FRONT.eventID, bean_iOSettings.getIscSwitchEvent() );
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


    private void reset () {

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
        private IndependentService.GeneralBean    bean_general;
        private IndependentService.IOSettingsBean bean_iOSettings;
        private IndependentService.StrategyBean   bean_strategy;

        public Solid ( GeneralBean bean_general, IOSettingsBean bean_iOSettings, StrategyBean bean_strategy ) {
            super();
            this.bean_general    = bean_general;
            this.bean_iOSettings = bean_iOSettings;
            this.bean_strategy = bean_strategy;
        }
    }
}
