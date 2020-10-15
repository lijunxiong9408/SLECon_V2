package slecon.setting.installation;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.EventID;
import logic.connection.LiftConnectionBean;
import ocsjava.remote.configuration.Event;
import ocsjava.remote.configuration.EventAggregator;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.installation.FloorTextAndMapping.FloorSettingBean;
import slecon.setting.installation.FloorTextAndMapping.GeneralBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.Parser_Event;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




@SetupView(
    path      = "Installation::Floor text and mapping",
    sortIndex = 0x140
)
public class FloorTextAndMappingSetting extends SettingPanel<FloorTextAndMapping> implements Page, LiftDataChangedListener {
    private static final long           serialVersionUID = 3333600223582736088L;
    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Logger.
     */
    private final Logger       logger = LogManager.getLogger( FloorTextAndMappingSetting.class );
    
    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid     solid  = null;
    private LiftConnectionBean connBean;
    private Parser_Error       error;
    private Parser_Deploy      deploy;
    private Parser_Event	   event;




    public FloorTextAndMappingSetting ( LiftConnectionBean connBean ) {
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
            deploy = new Parser_Deploy( connBean.getIp(), connBean.getPort() );
            event = new Parser_Event( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.MCS_NVRAM.getCode() | AgentMessage.ERROR.getCode() | AgentMessage.DEPLOYMENT.getCode() );
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
                                  AgentMessage.MCS_NVRAM.getCode() | AgentMessage.ERROR.getCode() );
        
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
    public void onOK ( FloorTextAndMapping panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( FloorTextAndMapping panel ) {
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
        return TEXT.getString( "FloorTextAndMapping.title" );
    }

	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Installation"), slecon.setting.installation.OverviewSetting.class);
		map.put(Dict.lookup("Floor_text_and_mapping"), null);
		return map;
	}

    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final FloorTextAndMapping.GeneralBean      bean_general      = new FloorTextAndMapping.GeneralBean();
            final FloorTextAndMapping.FloorSettingBean bean_floorSetting = new FloorTextAndMapping.FloorSettingBean();

            /* General */
            bean_general.setFloorCount( ( long )deploy.getFloorCount() );

            /* FloorSetting */
            String[]  ftext = new String[ 128 ];
            Integer[] dz    = new Integer[ 128 ];
            for ( byte i = 0 ; i < deploy.getFloorCount() ; i++ ) {
                ftext[ i ] = new String( deploy.getFloorText( i ) );
                dz[ i ]    = deploy.getDoorZone( i );
            }
            bean_floorSetting.setFloorText( ftext );
            bean_floorSetting.setDoorzone( dz );
            if ( solid == null )
                solid = new Solid( bean_general, bean_floorSetting );
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setFloorSettingBean( bean_floorSetting );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            GeneralBean      bean_general      = app.getGeneralBean();
            FloorSettingBean bean_floorSetting = app.getFloorSettingBean();
            byte floor_count = bean_general.getFloorCount().byteValue();
            /* General */
            deploy.setFloorCount( floor_count );

            /* FloorList */
            for ( int i = 0 ; i < bean_general.getFloorCount() ; i++ ) {
                String text = bean_floorSetting.getFloorText()[ i ];
                int    dz   = bean_floorSetting.getDoorzone()[ i ];
                deploy.setFloorText( ( byte )i, text );
                deploy.setDoorZone( ( byte )i, ( byte )dz );
            }
            deploy.commit();
            
            EventAggregator ea = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );
            for(int i = floor_count; i < 128; i++ ) {
            	if( ea.getEvent(EventID.getCarCallFrontID(i)) != null )
            		ea.setEvent( EventID.getCarCallFrontID(i), null );
            	
            	if( ea.getEvent(EventID.getCarCallRearID(i)) != null )
            		ea.setEvent( EventID.getCarCallRearID( i ), null );
            	
            	if( ea.getEvent(EventID.getCarCallDisabledID(i)) != null )
            		ea.setEvent( EventID.getCarCallDisabledID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallUpCallFrontID(i)) != null )
            		ea.setEvent( EventID.getHallUpCallFrontID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallDownCallFrontID(i)) != null )
            		ea.setEvent( EventID.getHallDownCallFrontID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallUpCallRearID(i)) != null )
            		ea.setEvent( EventID.getHallUpCallRearID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallDownCallRearID(i)) != null )
            		ea.setEvent( EventID.getHallDownCallRearID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallUpCallFrontDisabledID(i)) != null )
            		ea.setEvent( EventID.getHallUpCallFrontDisabledID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallDownCallFrontDisabledID(i)) != null )
            		ea.setEvent( EventID.getHallDownCallFrontDisabledID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallUpCallRearDisabledID(i)) != null )
            		ea.setEvent( EventID.getHallUpCallRearDisabledID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallDownCallRearDisabledID(i)) != null )
            		ea.setEvent( EventID.getHallDownCallRearDisabledID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallUpArrivalLightFrontID(i)) != null )
            		ea.setEvent( EventID.getHallUpArrivalLightFrontID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallDownArrivalLightFrontID(i)) != null )
            		ea.setEvent( EventID.getHallDownArrivalLightFrontID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallUpArrivalLightRearID(i)) != null )
            		ea.setEvent( EventID.getHallUpArrivalLightRearID( i ), null );
            	
            	if( ea.getEvent(EventID.getHallDownArrivalLightRearID(i)) != null )
            		ea.setEvent( EventID.getHallDownArrivalLightRearID( i ), null );
            }
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
                    if ( solid != null ) {
                        app.stop();
                        app.setGeneralBean( solid.bean_general );
                        app.setFloorSettingBean( solid.bean_floorSetting );
                        app.start();
                    }
                }
            } );
    }


    //////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final FloorTextAndMapping.GeneralBean      bean_general;
        private final FloorTextAndMapping.FloorSettingBean bean_floorSetting;




        private Solid ( GeneralBean bean_general, FloorSettingBean bean_floorSetting ) {
            super();
            this.bean_general      = bean_general;
            this.bean_floorSetting = bean_floorSetting;
        }
    }
}
