package slecon.setting.modules;
import static logic.util.SiteManagement.MON_MGR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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
import slecon.setting.modules.AccessControl.GeneralBean;
import slecon.setting.modules.AccessControl.IOSettingsBean;

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



@SetupView(
    path      = "Modules::Access Control",
    sortIndex = 0x30e
)
public class AccessControlSetting extends SettingPanel<AccessControl> implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = -7106072268418662867L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( AccessControlSetting.class );

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid solid = null;

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
     * The handler for Setup - Module - Access Control.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public AccessControlSetting ( LiftConnectionBean connBean ) {
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
                                      AgentMessage.MODULE.getCode() | 
                                      AgentMessage.ERROR.getCode()| 
                                      AgentMessage.EVENT.getCode() );
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
                AgentMessage.MODULE.getCode() | 
                AgentMessage.ERROR.getCode() | 
                AgentMessage.EVENT.getCode() );
        
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
    public void onOK ( AccessControl panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( AccessControl panel ) {
        reset();
    }


    @Override
    public void onConnCreate () {
        app.start();
        setEnabled( false );
    }


    @Override
    public void onDataChanged ( long timestamp, int msg ) {
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
        return AccessControl.TEXT.getString( "access_control" );
    }
    
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Access_Control"), this.getClass());
		return map;
	}

    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final EventAggregator              ea              = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );
            final AccessControl.GeneralBean    bean_general    = new AccessControl.GeneralBean();
            final AccessControl.IOSettingsBean bean_iOSettings = new AccessControl.IOSettingsBean();

            // General
            bean_general.setEnabled( module.acc.isEnabled() );

            // Event Settings
            bean_iOSettings.setSwitchAEvent( ea.getEvent( EventID.ACCESS1.eventID ) );
            bean_iOSettings.setSwitchBEvent( ea.getEvent( EventID.ACCESS2.eventID ) );
            bean_iOSettings.setSwitchCEvent( ea.getEvent( EventID.ACCESS3.eventID ) );
            bean_iOSettings.setSwitchDEvent( ea.getEvent( EventID.ACCESS4.eventID ) );
            bean_iOSettings.setSwitchEEvent( ea.getEvent( EventID.ACCESS5.eventID ) );
            bean_iOSettings.setSwitchFEvent( ea.getEvent( EventID.ACCESS6.eventID ) );
            bean_iOSettings.setSwitchGEvent( ea.getEvent( EventID.ACCESS7.eventID ) );

            // Operation set and access table.
            bean_iOSettings.setEnableOperationSet( module.acc.isForce_use_selected_operation_set() );
            bean_iOSettings.setIOOperationMode( ( int )module.acc.getOperation_set() );
            byte[][] AccData = new byte[128][128];
            List<byte[]> list = new ArrayList<byte[]>(Arrays.<byte[]>asList(module.acc.getAccess_table()));
            list.toArray(AccData);
            bean_iOSettings.setCallAvailability(AccData);
    
            // Floor text.
            /*String[] ftext = new String[ 128 ];
            for ( int i = 0 ; i < 128 ; i++ )
                ftext[ i ] = deploy.getFloorText( ( byte )i );
            */
            String[] floorText = new String[deploy.getFloorCount()];
			for(byte i=0 ; i < deploy.getFloorCount(); i++) {
				floorText[ i ] = new String( deploy.getFloorText( i ) );
			}
			
            bean_iOSettings.setFloorText( floorText );
            bean_iOSettings.setFloorCounts(deploy.getFloorCount());
            if ( solid == null )
                solid = new Solid( bean_general, bean_iOSettings );

            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setIOSettingsBean( bean_iOSettings );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    private boolean submit () {
        try {
            final AccessControl.GeneralBean    bean_general    = app.getGeneralBean();
            final AccessControl.IOSettingsBean bean_iOSettings = app.getIOSettingsBean();
            final EventAggregator              ea              = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );

            /* General */
            module.acc.setEnabled( bean_general.getEnabled() );

            // Event settings.
            ea.setEvent( EventID.ACCESS1.eventID, bean_iOSettings.getSwitchAEvent() );
            ea.setEvent( EventID.ACCESS2.eventID, bean_iOSettings.getSwitchBEvent() );
            ea.setEvent( EventID.ACCESS3.eventID, bean_iOSettings.getSwitchCEvent() );
            ea.setEvent( EventID.ACCESS4.eventID, bean_iOSettings.getSwitchDEvent() );
            ea.setEvent( EventID.ACCESS5.eventID, bean_iOSettings.getSwitchEEvent() );
            ea.setEvent( EventID.ACCESS6.eventID, bean_iOSettings.getSwitchFEvent() );
            ea.setEvent( EventID.ACCESS7.eventID, bean_iOSettings.getSwitchGEvent() );

            // Operation mode.
            module.acc.setForce_use_selected_operation_set( bean_iOSettings.getEnableOperationSet() );
            module.acc.setOperation_set( bean_iOSettings.getIOOperationMode().byteValue() );

            // Available set.
            module.acc.setAccess_table( bean_iOSettings.getCallAvailability() );

            // Update Event with OCS Agent.
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
        private final AccessControl.GeneralBean    bean_general;
        private final AccessControl.IOSettingsBean bean_iOSettings;




        private Solid ( GeneralBean bean_general, IOSettingsBean bean_iOSettings ) {
            super();
            this.bean_general    = bean_general;
            this.bean_iOSettings = bean_iOSettings;
        }
    }
}
