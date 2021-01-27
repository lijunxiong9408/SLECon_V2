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
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.modules.EmergencyPowerOperation.GeneralBean;
import slecon.setting.modules.LedBehavior.Led_Behavior;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.Parser_Event;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.DeviceMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




/**
 * Data of binding bean for Setup -> Module -> Emergency Power Operation.
 */
@SetupView(
    path      = "Modules::Emergency Power Operation",
    sortIndex = 0x30d
)
public class EmergencyPowerOperationSetting extends SettingPanel<EmergencyPowerOperation> implements Page, LiftDataChangedListener {

    private static final long serialVersionUID = -2399004013558351140L;

    /**
     * Logger.
     */
    private final Logger   logger = LogManager.getLogger( EmergencyPowerOperationSetting.class );

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
    private Parser_Deploy deploy;

    /**
     * A floor v.s. floor text mapping table.
     */
    private ArrayList<FloorText> floorTexts;

    /**
     * The handler for Setup - Module - Emergency Power Operation.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public EmergencyPowerOperationSetting ( LiftConnectionBean connBean ) {
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
            deploy = new Parser_Deploy( connBean.getIp(), connBean.getPort() );
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
    public void onOK ( EmergencyPowerOperation panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( EmergencyPowerOperation panel ) {
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
        return EmergencyPowerOperation.TEXT.getString( "emergency_power_operation" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Emergency_Power_Operation"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final EventAggregator                         ea           = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );
            final EmergencyPowerOperation.GeneralBean    bean_general  = new EmergencyPowerOperation.GeneralBean();

            // Initialize internal data.
            initFloorText();
            
            bean_general.setEnabled( module.epo.isEnabled() );
            bean_general.setDoorOpenLed( Led_Behavior.get(module.epo.getLedBehavior()) );
            bean_general.setDoorFrontOpen( module.epo.isFrontDoorEnabled() );
            bean_general.setDoorRearOpen( module.epo.isRearDoorEnabled() );
            bean_general.setStragetyAuto( module.epo.getScheme() == 1 );
            bean_general.setStragetyManual( module.epo.getScheme() == 2 );
            bean_general.setReturnFloor( getFloorTextByFloor(module.epo.getReturn_floor()) );
            bean_general.setCloseDoorTime( (long)module.epo.getDoorCloseTime() );
            bean_general.setCarMessage( DeviceMessage.get( module.epo.getCar_message() ) );
            bean_general.setHallMessage( DeviceMessage.get( module.epo.getHall_message() ) );
            bean_general.setAutoRunSeletor( module.epo.getAutoRunSelector() );
            
            bean_general.setEpoEvent( ea.getEvent( EventID.EPO_SWITCH.eventID ) );
            bean_general.setManualEvent( ea.getEvent( EventID.EVTID_EPO_MANUAL.eventID ) );

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
            final EmergencyPowerOperation.GeneralBean  bean_general  = app.getGeneralBean();
            final EventAggregator                      ea            = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );

            module.epo.setEnabled( bean_general.getEnabled() );
            module.epo.setLedBehavior( bean_general.getDoorOpenLed().getCode() );
            module.epo.setFrontDoorEnabled( bean_general.getDoorFrontOpen() );
            module.epo.setRearDoorEnabled( bean_general.getDoorRearOpen() );
            
            if( bean_general.getStragetyAuto() )
            	module.epo.setScheme( (byte)1 );
            else 
            	module.epo.setScheme( (byte)2 );
            
            module.epo.setReturn_floor( bean_general.getReturnFloor().getFloor() );
            module.epo.setDoorCloseTime( (byte)bean_general.getCloseDoorTime().intValue() );
            module.epo.setCar_message( bean_general.getCarMessage().getCode() );
            module.epo.setHall_message( bean_general.getHallMessage().getCode() );
            module.epo.setAutoRunSelector( bean_general.getAutoRunSeletor() );
            
            ea.setEvent( EventID.EPO_SWITCH.eventID, bean_general.getEpoEvent() );
            ea.setEvent( EventID.EVTID_EPO_MANUAL.eventID, bean_general.getManualEvent() );

            event.setEvent( ea.toByteArray( this.connBean ) );
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
                    app.stop();
                    app.setGeneralBean( solid.bean_general );
                    app.start();
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
    public class Solid {
        private EmergencyPowerOperation.GeneralBean  bean_general;

        public Solid ( GeneralBean bean_general ) {
            super();
            this.bean_general  = bean_general;
        }
    }
}
