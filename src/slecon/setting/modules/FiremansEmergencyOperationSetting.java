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
import slecon.setting.modules.FiremansEmergencyOperation.GeneralBean;
import slecon.setting.modules.FiremansEmergencyOperation.IOSettingsBean;
import slecon.setting.modules.FiremansEmergencyOperation.StrategyBean;

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
 * Data of binding bean for Setup -> Module -> Fireman's Emergency Operation.
 */
@SetupView(
    path      = "Modules::Fireman's Emergency Operation",
    sortIndex = 0x307
)
public class FiremansEmergencyOperationSetting extends SettingPanel<FiremansEmergencyOperation> implements Page, LiftDataChangedListener {

    private static final long serialVersionUID = 8167744302432304953L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( FiremansEmergencyOperationSetting.class );

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
     * The handler for Setup - Module - Fireman's Emergency Operation.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public FiremansEmergencyOperationSetting ( LiftConnectionBean connBean ) {
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
    public void onOK ( FiremansEmergencyOperation panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( FiremansEmergencyOperation panel ) {
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
        return FiremansEmergencyOperation.TEXT.getString( "firemans_emergency_operation" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Fireman_s_Emergency_Operation"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            // Internal data initialization.
            final FiremansEmergencyOperation.GeneralBean    bean_general    = new FiremansEmergencyOperation.GeneralBean();
            final FiremansEmergencyOperation.StrategyBean   bean_strategy   = new FiremansEmergencyOperation.StrategyBean();
            final FiremansEmergencyOperation.IOSettingsBean bean_iOSettings = new FiremansEmergencyOperation.IOSettingsBean();
            final EventAggregator                           ea              = EventAggregator.toEventAggregator( event.getEvent() );

            // Initialize internal data.
            initFloorText();

            // General
            bean_general.setEnabled( module.feo.isEnabled() );
            bean_general.setReturnFloor( getFloorTextByFloor( module.feo.getReturn_floor() ) );
            bean_general.setCarMessage( DeviceMessage.get( module.feo.getCar_message() ) );
            bean_general.setHallMessage( DeviceMessage.get( module.feo.getHall_message() ) );
            bean_general.setEdp_enable(module.feo.isEnable_edp());
            bean_general.setSgs_enable(module.feo.isEnable_sgs());
            
            // Strategy
            bean_strategy.setEnabledStragetyA( module.feo.getStrategy() == 1 );
            bean_strategy.setEnabledStragetyB( module.feo.getStrategy() == 2 );
            bean_strategy.setEnabledStragetyC( module.feo.getStrategy() == 3 );

            // IOSettings
            bean_iOSettings.setFeoSwitchEvent( ea.getEvent( EventID.FEO_FRONT.eventID ) );
            
            if (solid==null)
                solid = new Solid(bean_general, bean_iOSettings, bean_strategy);

            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setIOSettingsBean( bean_iOSettings );
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
            final FiremansEmergencyOperation.GeneralBean    bean_general    = app.getGeneralBean();
            final FiremansEmergencyOperation.IOSettingsBean bean_iOSettings = app.getIOSettingsBean();
            final FiremansEmergencyOperation.StrategyBean   bean_strategy   = app.getStrategyBean();
            final EventAggregator                           ea              = EventAggregator.toEventAggregator( event.getEvent() );

            // General
            module.feo.setEnabled( bean_general.getEnabled() );
            module.feo.setReturn_floor( bean_general.getReturnFloor().getFloor() );
            module.feo.setCar_message( bean_general.getCarMessage().getCode() );
            module.feo.setHall_message( bean_general.getHallMessage().getCode() );
            module.feo.setEnable_edp(bean_general.getEdp_enable());
            module.feo.setEnable_sgs(bean_general.getSgs_enable());
            
            // Strategy
            if ( bean_strategy.getEnabledStragetyA() ) {
                module.feo.setStrategy( 1 );
            } else if ( bean_strategy.getEnabledStragetyB() ) {
                module.feo.setStrategy( 2 );
            }else if(bean_strategy.getEnabledStragetyC() ) {
            	module.feo.setStrategy( 3 );
            }

            // IOSettings
            ea.setEvent( EventID.FEO_FRONT.eventID, bean_iOSettings.getFeoSwitchEvent() );

            // Update Event with OCS Agent.
            event.setEvent( ea.toByteArray() );
            event.setInstalledDevices( ea.getInstalledDevices() );
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
                        app.setStrategyBean( solid.bean_strategy );
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
        private FiremansEmergencyOperation.GeneralBean    bean_general;
        private FiremansEmergencyOperation.IOSettingsBean bean_iOSettings;
        private FiremansEmergencyOperation.StrategyBean   bean_strategy;




        public Solid ( GeneralBean bean_general, IOSettingsBean bean_iOSettings, StrategyBean bean_strategy ) {
            super();
            this.bean_general    = bean_general;
            this.bean_iOSettings = bean_iOSettings;
            this.bean_strategy   = bean_strategy;
        }
    }
}
