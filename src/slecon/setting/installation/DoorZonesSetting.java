package slecon.setting.installation;

import static logic.util.SiteManagement.MON_MGR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import slecon.setting.installation.DoorZones.ShaftPositionsBean;
import slecon.setting.modules.DoorTimingSetting;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Error;
import comm.Parser_McsConfig;
import comm.Parser_McsNvram;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




@SetupView(
    path      = "Installation::DoorZones",
    sortIndex = 0x130
)
public class DoorZonesSetting extends SettingPanel<DoorZones> implements Page, LiftDataChangedListener {
    private static final long           serialVersionUID = -2962322576291697998L;
    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Logger.
     */
    private final Logger       logger = LogManager.getLogger( DoorTimingSetting.class );
    private volatile Solid     solid  = null;
    public final LiftConnectionBean connBean;
    private Parser_Error       error;
    private Parser_McsConfig   mcsconfig;
    private Parser_McsNvram    nvram;
    private final Object mutex = new Object();
    private volatile long      lastestTimeStamp = -1;
    private boolean isStopped  = false;




    public DoorZonesSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }

    @Override
    public void onStart () throws Exception {
        try {
            error     = new Parser_Error( connBean.getIp(), connBean.getPort() );
            nvram     = new Parser_McsNvram( connBean.getIp(), connBean.getPort() );
            mcsconfig = new Parser_McsConfig( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.MCS_CONFIG.getCode() | AgentMessage.MCS_NVRAM.getCode() | AgentMessage.ERROR.getCode() );
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
        if (!isStopped) {
            setEnabled(true);
            MON_MGR.addEventListener(this, connBean.getIp(), connBean.getPort(), AgentMessage.MCS_NVRAM.getCode() | AgentMessage.ERROR.getCode());

            setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
            setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
        }
    }


    @Override
    public void onPause () throws Exception {
        if (!isStopped) {
            setEnabled(false);
            MON_MGR.removeEventListener(this);
        }
    }


    @Override
    public void onStop () throws Exception {
        MON_MGR.removeEventListener( this );
        isStopped = true;
    }


    @Override
    public void onOK ( DoorZones panel ) {
        synchronized ( mutex  ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( DoorZones panel ) {
        if (!isStopped) {
            reset();
        }
    }


    @Override
    public void onConnCreate () {
        app.start();
        setEnabled( false );
    }


    @Override
    public void onDataChanged(long timestamp, int msg) {
        if (!isStopped) {
            if (msg == AgentMessage.ERROR.getCode())
                ToolBox.showRemoteErrorMessage(connBean, error);

            synchronized (mutex) {
                setEnabled(false);
                if (solid != null && timestamp > lastestTimeStamp + 3000 ) {
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
        return TEXT.getString( "DoorZones.title" );
    }

	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Installation"), slecon.setting.installation.OverviewSetting.class);
		map.put(Dict.lookup("DoorZones"), null);
		return map;
	}
	
    public void setHot () {
        lastestTimeStamp = System.currentTimeMillis();
        
        try {
            final DoorZones.ControlBean        bean_Control        = new DoorZones.ControlBean();
            final DoorZones.ShaftPositionsBean bean_ShaftPositions = new DoorZones.ShaftPositionsBean();
            float                              mmratio             = mcsconfig.getMMRatio();

            /** Control. */
            bean_Control.setDzCount( Long.toString( nvram.getUnsignedByte( ( short )0x1001 ) ) );    // NVADDR_BUILDING_DOORZONE_COUNT (uc)

            /** Shaft Positions. */
            bean_ShaftPositions.setUsl( ( double )( mmratio * nvram.getSignedInt( ( short )0x00fc ) ) );    // NVADDR_USL (sl)
            bean_ShaftPositions.setLsl( ( double )( mmratio * nvram.getSignedInt( ( short )0x00f8 ) ) );    // NVADDR_LSL (sl)

            final List<Map<String, Number>> doorZoneDetails = new ArrayList<Map<String, Number>>();
            long                            dzCount         = nvram.getUnsignedByte( ( short )0x1001 );       // NVADDR_BUILDING_DOORZONE_COUNT (uc)
            for ( int doorzone = 0 ; doorzone < dzCount ; doorzone++ ) {
                float               ldz            = nvram.getSignedInt( ( short )( doorzone * 4 + 0x0100 ) );
                float               udz            = nvram.getSignedInt( ( short )( doorzone * 4 + 0x0300 ) );
                float               fine_tune_up   = nvram.getSignedInt( ( short )( doorzone * 4 + 0x0500 ) );
                float               fine_tune_down = nvram.getSignedInt( ( short )( doorzone * 4 + 0x0700 ) );
                Map<String, Number> map            = new HashMap<>();
                map.put( "dz", doorzone );
                map.put( "ldz", ldz * mmratio );
                map.put( "udz", udz * mmratio );
                map.put( "dist", udz * mmratio - ldz * mmratio );
                map.put( "mid_point", ldz / 2 * mmratio + udz / 2 * mmratio );
                map.put( "fine_tune_up", fine_tune_up * mmratio );
                map.put( "fine_tune_down", fine_tune_down * mmratio );
                doorZoneDetails.add( map );
            }
            bean_ShaftPositions.setDoorZoneDetails( doorZoneDetails );
            if ( solid == null )
                solid = new Solid( bean_ShaftPositions );
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.setControlBean( bean_Control );
                    app.setShaftPositionsBean( bean_ShaftPositions );
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            final ShaftPositionsBean bean_ShaftPositions = app.getShaftPositionsBean();
            float                    mmratio             = mcsconfig.getMMRatio();

            /** Shaft Positions. */
            nvram.setInt( ( short )0x00fc, ( int )( bean_ShaftPositions.getUsl() / mmratio ) );    // NVADDR_USL (sl)
            nvram.setInt( ( short )0x00f8, ( int )( bean_ShaftPositions.getLsl() / mmratio ) );    // NVADDR_LSL (sl)
            for ( Map<String, Number> map : bean_ShaftPositions.getDoorZoneDetails() ) {
                int dz             = ( Integer )map.get( "dz" );
                if ( 0 <= dz && dz < 128 ) {
                    int ldz = (int) ( map.get( "ldz" ).floatValue() / mmratio );
                    int udz = (int) ( map.get( "udz" ).floatValue() / mmratio );
                    int fine_tune_up = (int) ( map.get( "fine_tune_up" ).floatValue() / mmratio );
                    int fine_tune_down = (int) ( map.get( "fine_tune_down" ).floatValue() / mmratio );
                    nvram.setInt( (short) ( dz * 4 + 0x0100 ), (int) ldz );
                    nvram.setInt( (short) ( dz * 4 + 0x0300 ), (int) udz );
                    nvram.setInt( (short) ( dz * 4 + 0x0500 ), (int) fine_tune_up );
                    nvram.setInt( (short) ( dz * 4 + 0x0700 ), (int) fine_tune_down );
                }
            }
            nvram.commit();
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
                        app.setShaftPositionsBean( solid.bean_ShaftPositions );
                        app.start();
                    }
                }
            } );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final DoorZones.ShaftPositionsBean bean_ShaftPositions;




        public Solid ( ShaftPositionsBean bean_ShaftPositions ) {
            super();
            this.bean_ShaftPositions = bean_ShaftPositions;
        }
    }

}
