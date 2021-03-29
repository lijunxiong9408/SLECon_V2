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
import slecon.setting.modules.CarCallOption.ASPBEAN;
import slecon.setting.modules.CarCallOption.AntiNuisanceCarCallOperationBean;
import slecon.setting.modules.CarCallOption.CallsInDoubleClickClearBean;
import slecon.setting.modules.CarCallOption.CallsInOppositeDirectionAutoClearBean;
import slecon.setting.modules.CarCallOption.FloorTextChangeStategy;
import slecon.setting.modules.CarCallOption.NearStopBean;
import slecon.setting.modules.CarCallOption.Point2OperationBean;

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
 * Data of binding bean for Setup -> Module -> Car Call Option.
 */
@SetupView(
    path      = "Modules::Car Call Option",
    sortIndex = 0x30f
)
public class CarCallOptionSetting extends SettingPanel<CarCallOption> implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = 1037880613910068900L;

    /**
     * Logger.
     */
    private final Logger   logger = LogManager.getLogger( DoorTimingSetting.class );
    
    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid solid  = null;

    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;
    
    // TODO
    private Parser_Error       error;
    
    /**
     * Module data.
     */
    private Parser_Module      module;

    private Parser_Deploy 	   deploy;
    private Parser_Event 	   event;
    /**
     * The handler for Setup - Module - Car Call Option.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public CarCallOptionSetting ( LiftConnectionBean connBean ) {
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
            deploy = new Parser_Deploy( connBean.getIp(), connBean.getPort() );
            event  = new Parser_Event( connBean.getIp(), connBean.getPort() );
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
                                  AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() | AgentMessage.EVENT.getCode() );
        
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
    public void onOK ( CarCallOption panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( CarCallOption panel ) {
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
        return CarCallOption.TEXT.getString( "car_call_option" );
    }
    
    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Car_Call_Option"), this.getClass());
		return map;
	}
	
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
    	    final EventAggregator            ea                  = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );
    	    final CarCallOption.FloorTextChangeStategy bean_stategy = new CarCallOption.FloorTextChangeStategy();
            final CarCallOption.AntiNuisanceCarCallOperationBean bean_antiNuisanceCarCallOperation =
                new CarCallOption.AntiNuisanceCarCallOperationBean();
            final CarCallOption.CallsInOppositeDirectionAutoClearBean bean_callsInOppositeDirectionAutoClear =
                new CarCallOption.CallsInOppositeDirectionAutoClearBean();
            
            final CarCallOption.CallsInDoubleClickClearBean bean_callsInDoubleClickAutoClear =
                new CallsInDoubleClickClearBean();
            
            final CarCallOption.NearStopBean bean_nearstop =  new CarCallOption.NearStopBean();
            
            final ASPBEAN	bean_asp = new ASPBEAN();
            
            final Point2OperationBean bean_p2o = new Point2OperationBean();
            
            /* Floor text change stategy. */
            bean_stategy.setStategy(module.cco.getFloorChangeStategy());
            
            /* AntiNuisanceCarCallOperation */
            bean_antiNuisanceCarCallOperation.setEnabled( module.cco.isAnti_nuisance_car_call_operation_enabled() );
            bean_antiNuisanceCarCallOperation.setPercentage( ( long )module.cco.getPercentage() );
            bean_antiNuisanceCarCallOperation.setCarCallCount( ( long )module.cco.getCar_call_count() );

            /* CallsInOppositeDirectionAutoClear */
            bean_callsInOppositeDirectionAutoClear.setEnabled0( module.cco.isCalls_in_opposite_direction_auto_clear_enabled() );
           
            bean_callsInDoubleClickAutoClear.setEnabled1(module.cco.isCalls_in_double_click_auto_clear_enabled());
            
            bean_nearstop.setEnable(module.cco.getNearEnabled());
            bean_nearstop.setDirect(module.cco.getDirect());
            
            bean_asp.setEnable( module.cco.isASPEnabled() );
            bean_asp.setAspEvent( ea.getEvent( EventID.ALL_STATION_PARKING_SWITCH.eventID ));
            
            bean_p2o.setEnabled(module.p2o.getEnable());
            String[] floorText = new String[deploy.getFloorCount()];
            for(int i=0; i < deploy.getFloorCount(); i++) {
            	floorText[i] = deploy.getFloorText((byte)i) ;
            }
            bean_p2o.setFloorText(floorText);
            bean_p2o.setFloorcount(deploy.getFloorCount());
            bean_p2o.setDoorEnableAction(module.p2o.getStrategy());
            
            if ( solid == null )
                 solid = new Solid( bean_stategy, bean_antiNuisanceCarCallOperation, bean_callsInOppositeDirectionAutoClear,
                				   bean_callsInDoubleClickAutoClear, bean_nearstop, bean_asp, bean_p2o );

            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setFloorChangeStategy(bean_stategy);
                    app.setAntiNuisanceCarCallOperationBean( bean_antiNuisanceCarCallOperation );
                    app.setCallsInOppositeDirectionAutoClearBean( bean_callsInOppositeDirectionAutoClear );
                    app.setCallsInDoubleClickAutoClearBean(bean_callsInDoubleClickAutoClear);
                    app.setNearStopBean(bean_nearstop);
                    app.setAspBean(bean_asp);
                    app.setP2oBean(bean_p2o);
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
        	final CarCallOption.FloorTextChangeStategy bean_stategy = app.getFloorChangeStategy();
            final CarCallOption.AntiNuisanceCarCallOperationBean bean_antiNuisanceCarCallOperation =
                app.getAntiNuisanceCarCallOperationBean();
            final CarCallOption.CallsInOppositeDirectionAutoClearBean bean_callsInOppositeDirectionAutoClear =
                app.getCallsInOppositeDirectionAutoClearBean();
            final CarCallOption.CallsInDoubleClickClearBean bean_callsInDoubleClickAutoClear =
                app.getCallsInDoubleClickAutoClearBean();
            
            final CarCallOption.NearStopBean    bean_near_stop    = app.getNearStopBean();
            
            final CarCallOption.ASPBEAN			bean_asp		  = app.getASPBean();
            
            final CarCallOption.Point2OperationBean bean_p2o	  = app.getP2oBean();
            
            final EventAggregator      ea                  = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );
            
            /* Floor text change stategy */
            module.cco.setFloorChangeStategy( (byte)bean_stategy.getStategy() );
            
            /* AntiNuisanceCarCallOperation */
            module.cco.setAnti_nuisance_car_call_operation_enabled( bean_antiNuisanceCarCallOperation.getEnabled() );
            module.cco.setPercentage( bean_antiNuisanceCarCallOperation.getPercentage().intValue() );
            module.cco.setCar_call_count( bean_antiNuisanceCarCallOperation.getCarCallCount().intValue() );
            
            /* CallsInOppositeDirectionAutoClear */
            module.cco.setCalls_in_opposite_direction_auto_clear_enabled( bean_callsInOppositeDirectionAutoClear.getEnabled0() );
            
            module.cco.setCalls_in_double_click_auto_clear_enabled(bean_callsInDoubleClickAutoClear.getEnabled1());
                        
            module.cco.setNearEnabled(bean_near_stop.isEnable());
            module.cco.setDirect((byte)bean_near_stop.getDirect());
            
            module.cco.setASPEnabled(bean_asp.isEnable());
            
            module.p2o.setEnable(bean_p2o.getEnabled());
            module.p2o.setStrategy(bean_p2o.getDoorEnableAction());
            
            module.commit();
            
            ea.setEvent(EventID.ALL_STATION_PARKING_SWITCH.eventID, bean_asp.getAspEvent());
            // Commit data to XML RPC server.
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


    private void reset () {
        // Update returned data to visualization components.
        if ( solid != null )
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    if ( solid != null ) {
                        app.stop();
                        app.setAntiNuisanceCarCallOperationBean( solid.bean_antiNuisanceCarCallOperation );
                        app.setCallsInOppositeDirectionAutoClearBean( solid.bean_callsInOppositeDirectionAutoClear );
                        app.setCallsInDoubleClickAutoClearBean(solid.bean_callsInDoubleClickAutoClear);
                        app.setNearStopBean(solid.bean_Near_stop);
                        app.start();
                    }
                }
            } );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
    	final CarCallOption.FloorTextChangeStategy bean_stategy;
        final CarCallOption.AntiNuisanceCarCallOperationBean      bean_antiNuisanceCarCallOperation;
        final CarCallOption.CallsInOppositeDirectionAutoClearBean bean_callsInOppositeDirectionAutoClear;
        final CarCallOption.CallsInDoubleClickClearBean			  bean_callsInDoubleClickAutoClear;
        final CarCallOption.NearStopBean  bean_Near_stop;
        final CarCallOption.ASPBEAN bean_asp;
        final CarCallOption.Point2OperationBean bean_p2o;


        public Solid ( FloorTextChangeStategy bean_stategy, AntiNuisanceCarCallOperationBean bean_antiNuisanceCarCallOperation,
                       CallsInOppositeDirectionAutoClearBean bean_callsInOppositeDirectionAutoClear,
                       CallsInDoubleClickClearBean bean_callsInDoubleClickAutoClear,NearStopBean bean_Near_stop,
                       ASPBEAN bean_asp, Point2OperationBean bean_p2o) {
        	this.bean_stategy = bean_stategy;
            this.bean_antiNuisanceCarCallOperation      = bean_antiNuisanceCarCallOperation;
            this.bean_callsInOppositeDirectionAutoClear = bean_callsInOppositeDirectionAutoClear;
            this.bean_callsInDoubleClickAutoClear = bean_callsInDoubleClickAutoClear;
            this.bean_Near_stop = bean_Near_stop;
            this.bean_asp = bean_asp;
            this.bean_p2o = bean_p2o;
        }
    }
}
