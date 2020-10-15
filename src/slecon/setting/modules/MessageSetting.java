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
import slecon.setting.modules.Message.FaultInspectionSettingsBean;
import slecon.setting.modules.Message.FaultWarningSignBean;
import slecon.setting.modules.Message.GeneralBean;

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
 * Data of binding bean for Setup -> Module -> Inspection.
 */
@SetupView(
    path      = "Modules::Message",
    sortIndex = 0x302
)
public class MessageSetting extends SettingPanel<Message> implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = -7337773316029197666L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( MessageSetting.class );

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
     * The handler for Setup - Module - Inspection.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public MessageSetting ( LiftConnectionBean connBean ) {
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
                                      AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() | AgentMessage.EVENT.getCode() );
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
    public void onOK ( Message panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( Message panel ) {
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
        return Message.TEXT.getString( "Message" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Message"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final Message.GeneralBean bean_general = new Message.GeneralBean();
            final Message.FaultInspectionSettingsBean bean_fault = new Message.FaultInspectionSettingsBean();
            final Message.FaultWarningSignBean bean_fault_sign = new Message.FaultWarningSignBean();
            final EventAggregator ea = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );
            
            /* General. */
            bean_general.setCarMessage( DeviceMessage.get( module.insp.getCar_message() ) );
            bean_general.setHallMessage( DeviceMessage.get( module.insp.getHall_message() ) );
            
            /* FaultInspectionSettingsBean. */
            bean_fault.setCarMessage( DeviceMessage.get( module.insp.getFault_Car_message() ) );
            bean_fault.setHallMessage( DeviceMessage.get( module.insp.getFault_Hall_message() ) );
            
            bean_fault_sign.setWarning_keep_timer( module.insp.getFault_sign_keep_timer() );
            bean_fault_sign.setWarning_event( ea.getEvent( EventID.EVTID_SYSTEM_FAULT_WARNING.eventID ) );
            
            if ( solid == null ) {
                solid = new Solid( bean_general, bean_fault, bean_fault_sign );
            }

            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setFaultInspectionSettingsBean( bean_fault );
                    app.setFaultWarningSignBean( bean_fault_sign );
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    private boolean submit () {
        try {
            final Message.GeneralBean bean_general = app.getGeneralBean();
            final Message.FaultInspectionSettingsBean bean_fault = app.getFaultInspectionSettingsBean();
            final Message.FaultWarningSignBean bean_fault_sign = app.getFaultWarningSignBean();

            /* General */
            module.insp.setCar_message( bean_general.getCarMessage().getCode() );
            module.insp.setHall_message( bean_general.getHallMessage().getCode() );
            
            /* FaultInspectionSettingsBean */
            module.insp.setFault_Car_message( bean_fault.getCarMessage().getCode() );
            module.insp.setFault_Hall_message( bean_fault.getHallMessage().getCode() );
            
            module.insp.setFault_sign_keep_timer( bean_fault_sign.getWarning_keep_timer() );
            final EventAggregator ea = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );
            ea.setEvent( EventID.EVTID_SYSTEM_FAULT_WARNING.eventID, bean_fault_sign.getWarning_event() );
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


    private void reset () {

        // Update returned data to visualization components.
        if ( solid != null )
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( solid.bean_general );
                    app.setFaultInspectionSettingsBean( solid.bean_fault );
                    app.setFaultWarningSignBean(solid.bean_fault_sign);
                    app.start();
                }
            } );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        final Message.GeneralBean bean_general;
        final Message.FaultInspectionSettingsBean bean_fault;
        final Message.FaultWarningSignBean bean_fault_sign;



        public Solid ( GeneralBean bean_general, FaultInspectionSettingsBean bean_fault, FaultWarningSignBean bean_fault_sign ) {
            super();
            this.bean_general = bean_general;
            this.bean_fault = bean_fault;
            this.bean_fault_sign = bean_fault_sign;
        }
    }
}
