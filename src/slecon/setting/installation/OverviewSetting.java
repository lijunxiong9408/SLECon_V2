package slecon.setting.installation;
import static logic.util.SiteManagement.MON_MGR;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import slecon.setting.installation.Overview.IdentificationBean;
import slecon.setting.installation.Overview.StatisticBean;
import slecon.setting.installation.Overview.StatusBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_DcsConfig;
import comm.Parser_Error;
import comm.Parser_McsConfig;
import comm.Parser_McsNvram;
import comm.Parser_OcsConfig;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.constants.DoorAction;
import comm.constants.McsStatus;
import comm.constants.OcsModule;
import comm.event.LiftDataChangedListener;




@SetupView(
    path      = "Installation::Overview",
    sortIndex = 0x001
)
public class OverviewSetting extends SettingPanel<Overview> implements Page, LiftDataChangedListener {
    private static final long           serialVersionUID = 6890721534223732367L;
    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Logger.
     */
    private final Logger       logger = LogManager.getLogger( OverviewSetting.class );
    private final Object       mutex  = new Object(); 
    private volatile Solid     solid  = null;
    private LiftConnectionBean connBean;
    private Parser_Error       error;
    private Parser_McsConfig   mcsconfig;
    private Parser_McsNvram    nvram;
    private Parser_OcsConfig   ocsconfig;
    private Parser_Status      status;
    private Parser_DcsConfig   dcsconfig;




    public OverviewSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
        setOKButtonEnabled(false);
        setResetButtonEnabled(false);
    }


    @Override
    public void onStart () throws Exception {
        try {
            nvram     = new Parser_McsNvram( connBean.getIp(), connBean.getPort() );
            error     = new Parser_Error( connBean.getIp(), connBean.getPort() );
            mcsconfig = new Parser_McsConfig( connBean.getIp(), connBean.getPort() );
            ocsconfig = new Parser_OcsConfig( connBean.getIp(), connBean.getPort() );
            status    = new Parser_Status( connBean.getIp(), connBean.getPort() );
            dcsconfig = new Parser_DcsConfig( connBean.getIp(), connBean.getPort() );
            
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                    AgentMessage.MCS_NVRAM.getCode() | AgentMessage.ERROR.getCode() | AgentMessage.MCS_CONFIG.getCode() |
                    AgentMessage.OCS_CONFIG.getCode() | AgentMessage.STATUS.getCode() | AgentMessage.DCS_CONFIG.getCode());
            setHot();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.nanoTime();
        }
    }


    @Override
    public void onResume () throws Exception {
        setEnabled( true );
        MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                AgentMessage.MCS_NVRAM.getCode() | AgentMessage.ERROR.getCode() | AgentMessage.MCS_CONFIG.getCode() |
                AgentMessage.OCS_CONFIG.getCode() | AgentMessage.STATUS.getCode());
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
    public void onOK ( Overview panel ) {
        synchronized ( mutex  ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( Overview panel ) {
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
            setHot();
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
        return TEXT.getString( "Overview.title" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Installation"), slecon.setting.installation.OverviewSetting.class);
		map.put(Dict.lookup("Overview"), null);
		return map;
	}
    
    static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000L;
    static final long MILLISECONDS_PER_HOUR = 60 * 60 * 1000L;
    static final long MILLISECONDS_PER_MINUTE = 60 * 1000L;
    static final long MILLISECONDS_PER_SECOND = 1000L;

    public void setHot () {        
        try {
            final Overview.StatusBean         bean_status         = new Overview.StatusBean();
            final Overview.StatisticBean      bean_statistic      = new Overview.StatisticBean();
            final Overview.IdentificationBean bean_identification = new Overview.IdentificationBean();
            McsStatus                         mcsstatus           = status.getMcsStatus();
            DoorAction                        frontdcsaction      = status.getDoorStatus( true );
            DoorAction                        reardcsaction       = status.getDoorStatus( false );
            OcsModule                         ocsmodule           = status.getOCSModule();

            /* Status */
            bean_status.setFrontdoorAction(frontdcsaction);
            bean_status.setReardoorAction(reardcsaction);
            bean_status.setMcsStatus( mcsstatus );
            bean_status.setOcsStatus( ocsmodule );

            /* Identification */
            bean_identification.setOcsVersion( ocsconfig.getVersion() );
            bean_identification.setMcsContractVersion( mcsconfig.getContractNumber() );
            bean_identification.setMcsFirmwareVersion( mcsconfig.getFirmwareVersion() );
            bean_identification.setMcsHardwareVersion( mcsconfig.getBoardVersion() );
            bean_identification.setMcsSerialNumber( mcsconfig.getSerialNumber() );
            bean_identification.setDcsFirmwareVersion(dcsconfig.getDCSFirmwareVersion());
            bean_identification.setDcsHardwareVersion(dcsconfig.getDCSBoardVersion());
            bean_identification.setEpsFirmwareVersion(dcsconfig.getEPSFirmwareVersion());
            bean_identification.setEpsHardwareVersion(dcsconfig.getEPSBoardVersion());
            /* Statistic */
            bean_statistic.setRunCount( String.valueOf( nvram.getUnsignedInt( ( short )0x0008 ) ) );    // NVADDR_CUMULATIVE_CAR_RUN_COUNT (ul)
            long millisecond = (status.getTime() - mcsconfig.getBootupTime());
            long second = (millisecond/MILLISECONDS_PER_SECOND) % 60;
            long minute = (millisecond/MILLISECONDS_PER_MINUTE) % 60;
            long hour   = (millisecond/MILLISECONDS_PER_HOUR) % 24;
            long day    = (millisecond/MILLISECONDS_PER_DAY);
            String displayText = "";
            displayText += (day>0 ? String.format("%d %s ", day, Dict.lookup("DAY")) : "");
            displayText += (hour>0 ? String.format("%d %s ", hour, Dict.lookup("HOUR")) : "");
            displayText += (minute>0 ? String.format("%d %s ", minute, Dict.lookup("MINUTE")) : "");
            displayText += second + " " + Dict.lookup("SECOND");
            
            bean_statistic.setUpTime(displayText);
            bean_statistic.setLastMaintenance( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date( ( long )mcsconfig.getLastMaintenance() ) ) );
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.setStatusBean( bean_status );
                    app.setStatisticBean( bean_statistic );
                    app.setIdentificationBean( bean_identification );
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            //nvram.commit();
            return true;
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
            logger.catching( Level.FATAL, e );
        }
        return false;
    }


    public void reset () {
        setHot();
    }


    //////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final Overview.StatusBean         bean_status;
        private final Overview.StatisticBean      bean_statistic;
        private final Overview.IdentificationBean bean_identification;




        private Solid ( StatusBean bean_status, StatisticBean bean_statistic, IdentificationBean bean_identification ) {
            super();
            this.bean_status         = bean_status;
            this.bean_statistic      = bean_statistic;
            this.bean_identification = bean_identification;
        }
    }
}
