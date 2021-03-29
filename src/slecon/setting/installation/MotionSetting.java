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
import slecon.setting.installation.Motion.BrakeControlBean;
import slecon.setting.installation.Motion.DriverTypeBean;
import slecon.setting.installation.Motion.EncoderBean;
import slecon.setting.installation.Motion.MotorSpecificationBean;

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
    path      = "Installation::Motion",
    sortIndex = 0x110
)
public class MotionSetting extends SettingPanel<Motion> implements Page, LiftDataChangedListener {
    private static final long     serialVersionUID = 4925212864229138862L;
    private static ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Logger.
     */
    private final Logger       logger = LogManager.getLogger( MotionSetting.class );
    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid     solid  = null;
    private LiftConnectionBean connBean;
    private Parser_Error       error;
    private Parser_McsConfig   mcsconfig;
    private Parser_McsNvram    nvram;




    public MotionSetting ( LiftConnectionBean connBean ) {
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
            new Parser_McsConfig( connBean.getIp(), connBean.getPort() );
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
                AgentMessage.MCS_CONFIG.getCode() | AgentMessage.MCS_NVRAM.getCode() | AgentMessage.ERROR.getCode() );

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
    public void onOK ( Motion panel ) {
        synchronized ( mutex  ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( Motion panel ) {
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
        return TEXT.getString( "Motion.title" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Installation"), slecon.setting.installation.OverviewSetting.class);
		map.put(Dict.lookup("Motion"), null);
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final Motion.MotorSpecificationBean bean_MotorSpecification = new Motion.MotorSpecificationBean();
            final Motion.EncoderBean            bean_Encoder            = new Motion.EncoderBean();
            final Motion.InformationBean        bean_Information        = new Motion.InformationBean();
            final Motion.DriverTypeBean         bean_drivertype         = new Motion.DriverTypeBean();
            final Motion.BrakeControlBean		bean_brake_control      = new Motion.BrakeControlBean();

            /** Motor Specification. */
            bean_MotorSpecification.setRevolutionPerMinuteRpm( nvram.getUnsignedInt( ( short )0x0920 ) );       // NVADDR_MOTOR_RPM (ul)
            bean_MotorSpecification.setMaximumLinearSpeedMmS( ( double )nvram.getFloat( ( short )0x090c ) );    // NVADDR_MAX_SPEED (f)

            /** Encoder. */
            bean_Encoder.setEncoderCountPerRevolutionCpr( nvram.getUnsignedInt( ( short )0x0914 ) );    // NVADDR_QEI_COUNT_PER_REV (ul)
            bean_Encoder.setInvertPhase( nvram.getUnsignedByte( ( short )0x091c ) == 1 );                       // NVADDR_QEI_DIR (uc)

            /** Information. */
            bean_Information.setEncoderCprAtMaximumSpeed( String.format( "%.2f",
                                                                         ( double )bean_Encoder.getEncoderCountPerRevolutionCpr()
                                                                         * bean_MotorSpecification.getRevolutionPerMinuteRpm() / 60 ) );
            bean_Information.setCountToMmRatio( mcsconfig.getMMRatio() + "" );
            
            /** Driver Type. */
            bean_drivertype.setFujiVFDriverOverCANBusEnabled( nvram.getUnsignedShort( (short)0x1080 ) == 1 );
            
            /** Brake Control. */
            bean_brake_control.setBrakeOpenVoltage( nvram.getUnsignedByte((short)0x1976) );
            bean_brake_control.setBrakeKeepVoltage( nvram.getUnsignedByte((short)0x1977) );
            

            Double maxlinears = bean_MotorSpecification.getMaximumLinearSpeedMmS();
            double value      = 1000.0 * ( 0.4 * Math.pow( maxlinears / 1000, 2.0 ) + 0.1 * ( maxlinears / 1000.0f ) + 1.0 );
            bean_Information.setRecommendedShaftLimitLslUslLength( String.format( "%.2f", value ) );

            if (solid == null)
                solid = new Solid(bean_MotorSpecification, bean_Encoder, bean_drivertype, bean_brake_control);
            
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.setMotorSpecificationBean( bean_MotorSpecification );
                    app.setEncoderBean( bean_Encoder );
                    app.setInformationBean( bean_Information );
                    app.setDriverTypeBean( bean_drivertype );
                    app.setBrakeControlBean(bean_brake_control);
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            MotorSpecificationBean bean_MotorSpecification = app.getMotorSpecificationBean();
            EncoderBean            bean_Encoder            = app.getEncoderBean();
            DriverTypeBean         bean_DriverType         = app.getDriverTypeBean();
            BrakeControlBean	   bean_brake_control	   = app.getBrakeControlBean();

            /** Motor Specification. */
            nvram.setInt( ( short )0x0920, bean_MotorSpecification.getRevolutionPerMinuteRpm().intValue() );       // NVADDR_MOTOR_RPM (ul)
            nvram.setFloat( ( short )0x090c, bean_MotorSpecification.getMaximumLinearSpeedMmS().floatValue() );    // NVADDR_MAX_SPEED (f)

            /** Encoder. */
            nvram.setInt( ( short )0x0914, bean_Encoder.getEncoderCountPerRevolutionCpr().intValue() );    // NVADDR_QEI_COUNT_PER_REV (ul)
            nvram.setByte( ( short )0x091c, bean_Encoder.getInvertPhase()
                                            ? ( byte )1
                                            : ( byte )0 );                                                 // NVADDR_QEI_DIR (uc)
            
            /** Driver Type. */
            nvram.setShort( ( short )0x1080, bean_DriverType.isFujiVFDriverOverCANBusEnabled()
                    ? ( byte )1
                    : ( byte )0 );   
            
            /** Brake Control. */
            nvram.setByte( ( short )0x1976, (byte)bean_brake_control.getBrakeOpenVoltage() );  
            nvram.setByte( ( short )0x1977, (byte)bean_brake_control.getBrakeKeepVoltage() );  
            
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
                        app.setEncoderBean( solid.bean_Encoder );
                        app.setMotorSpecificationBean( solid.bean_MotorSpecification );
                        app.setDriverTypeBean( solid.bean_drivertype );
                        app.start();
                    }
                }
            } );
    }


    //////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final Motion.MotorSpecificationBean bean_MotorSpecification;
        private final Motion.EncoderBean            bean_Encoder;
        private final Motion.DriverTypeBean         bean_drivertype;
        private final Motion.BrakeControlBean		bean_brake_control;



        private Solid ( MotorSpecificationBean bean_MotorSpecification, EncoderBean bean_Encoder, DriverTypeBean bean_drivertype, BrakeControlBean bean_brake_control ) {
            super();
            this.bean_MotorSpecification = bean_MotorSpecification;
            this.bean_Encoder            = bean_Encoder;
            this.bean_drivertype         = bean_drivertype;
            this.bean_brake_control      = bean_brake_control;
        }
    }
}
