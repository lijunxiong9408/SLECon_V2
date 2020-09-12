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
import logic.connection.LiftConnectionBean;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.installation.Shaft.DoorZoneCountsBean;
import slecon.setting.installation.Shaft.DoorZoneLengthsBean;

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
    path      = "Installation::Shaft",
    sortIndex = 0x120
)
public class ShaftSetting extends SettingPanel<Shaft> implements Page, LiftDataChangedListener {
    private static final long     serialVersionUID = 7101343997072703517L;
    private static ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Logger.
     */
    private final Logger       logger = LogManager.getLogger( ShaftSetting.class );
    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid     solid  = null;
    private LiftConnectionBean connBean;
    private Parser_Error       error;
    private Parser_McsConfig   mcsconfig;
    private Parser_McsNvram    nvram;




    public ShaftSetting ( LiftConnectionBean connBean ) {
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
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
    }


    @Override
    public void onResume () throws Exception {
        setEnabled( true );
        MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                  AgentMessage.MCS_NVRAM.getCode() | AgentMessage.ERROR.getCode() );
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
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
    public void onOK ( Shaft panel ) {
        synchronized ( mutex  ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( Shaft panel ) {
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
        return TEXT.getString( "Shaft.title" );
    }

	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Installation"), slecon.setting.installation.OverviewSetting.class);
		map.put(Dict.lookup("Shaft"), null);
		return map;
	}
	
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final Shaft.DoorZoneCountsBean  bean_DoorZoneCounts  = new Shaft.DoorZoneCountsBean();
            final Shaft.DoorZoneLengthsBean bean_DoorZoneLengths = new Shaft.DoorZoneLengthsBean();
            float                           mmratio              = mcsconfig.getMMRatio();

            /** Door zone counts. */
            bean_DoorZoneCounts.setBuildingDoorZoneCounts( nvram.getUnsignedByte( ( short )0x1001 ) );    // NVADDR_BUILDING_DOORZONE_COUNT (uc)
            bean_DoorZoneCounts.setDoorZonesWithinLsl( nvram.getUnsignedByte( ( short )0x1002 ) );    // NVADDR_LOWER_SHAFT_LIMIT_DOORZONE_COUNT (uc)
            bean_DoorZoneCounts.setDoorZonesWithinUsl( nvram.getUnsignedByte( ( short )0x1003 ) );    // NVADDR_UPPER_SHAFT_LIMIT_DOORZONE_COUNT (uc)

            /** Door zone lengths. */
            bean_DoorZoneLengths.setDoorZoneHeight( ( double )( mmratio * nvram.getFloat( ( short )0x0904 ) ) );    // NVADDR_DOORZONE_HEIGHT (f)
            bean_DoorZoneLengths.setLdzUdzSpacing( ( double )( mmratio * nvram.getFloat( ( short )0x0908 ) ) );    // NVADDR_LV_SPACING (f)
            if ( solid == null )
                solid = new Solid( bean_DoorZoneCounts, bean_DoorZoneLengths );
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setDoorZoneCountsBean( bean_DoorZoneCounts );
                    app.setDoorZoneLengthsBean( bean_DoorZoneLengths );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            final Shaft.DoorZoneCountsBean  bean_DoorZoneCounts  = app.getDoorZoneCountsBean();
            final Shaft.DoorZoneLengthsBean bean_DoorZoneLengths = app.getDoorZoneLengthsBean();
            float                           mmratio              = mcsconfig.getMMRatio();

            /** Door zone counts. */
            nvram.setByte( ( short )0x1001, bean_DoorZoneCounts.getBuildingDoorZoneCounts().byteValue() );    			// NVADDR_BUILDING_DOORZONE_COUNT (uc)
            nvram.setByte( ( short )0x1002, bean_DoorZoneCounts.getDoorZonesWithinLsl().byteValue() );    				// NVADDR_LOWER_SHAFT_LIMIT_DOORZONE_COUNT (uc)
            nvram.setByte( ( short )0x1003, bean_DoorZoneCounts.getDoorZonesWithinUsl().byteValue() );    				// NVADDR_UPPER_SHAFT_LIMIT_DOORZONE_COUNT (uc)

            /** Door zone lengths. */
            nvram.setFloat( ( short )0x0904, ( 1 / mmratio ) * bean_DoorZoneLengths.getDoorZoneHeight().floatValue() );    // NVADDR_DOORZONE_HEIGHT (f)
            nvram.setFloat( ( short )0x0908, ( 1 / mmratio ) * bean_DoorZoneLengths.getLdzUdzSpacing().floatValue() );    // NVADDR_LV_SPACING (f)
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
                        app.setDoorZoneCountsBean( solid.bean_DoorZoneCounts );
                        app.setDoorZoneLengthsBean( solid.bean_DoorZoneLengths );
                        app.start();
                    }
                }
            } );
    }


    //////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final Shaft.DoorZoneCountsBean  bean_DoorZoneCounts;
        private final Shaft.DoorZoneLengthsBean bean_DoorZoneLengths;




        private Solid ( DoorZoneCountsBean bean_DoorZoneCounts, DoorZoneLengthsBean bean_DoorZoneLengths ) {
            super();
            this.bean_DoorZoneCounts  = bean_DoorZoneCounts;
            this.bean_DoorZoneLengths = bean_DoorZoneLengths;
        }
    }
}
