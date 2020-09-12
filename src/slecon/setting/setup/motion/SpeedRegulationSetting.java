package slecon.setting.setup.motion;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.SetupPanel;
import slecon.setting.setup.motion.SpeedRegulation.SpeedCheckingBean;
import slecon.setting.setup.motion.SpeedRegulation.SpeedOutputBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Error;
import comm.Parser_McsNvram;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




@SetupView(
    path      = "Setup::Motion::SpeedRegulation",
    sortIndex = 0x214
)
public class SpeedRegulationSetting extends SettingPanel<SpeedRegulation> implements Page, LiftDataChangedListener {

    private static final long           serialVersionUID = 6140279319878031210L;
    
    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.SettingPanel" );
    
    /**
     * Logger.
     */
    private final Logger logger = LogManager.getLogger(SpeedRegulationSetting.class);

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex            = new Object(); 
    private volatile Solid     solid            = null;

    private final LiftConnectionBean connBean;

    private Parser_Error error;

    private Parser_McsNvram nvram;

    private Workspace workspace;
    
    private boolean IsVerify = false;



    public SpeedRegulationSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate(Workspace workspace) throws Exception {
        this.workspace = workspace;
    }

    
    @Override
    public void onStart() throws Exception {
        try {
            error = new Parser_Error(connBean.getIp(), connBean.getPort());
            nvram = new Parser_McsNvram(connBean.getIp(), connBean.getPort());
            MON_MGR.addEventListener(this, connBean.getIp(), connBean.getPort(),
                    AgentMessage.MCS_NVRAM.getCode() 
                    | AgentMessage.ERROR.getCode());
            setHot();
        } catch (Exception e) {
            e.printStackTrace();
            System.nanoTime();
        }
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
    }
    

    @Override
    public void onResume() throws Exception {
        setEnabled(true);
        MON_MGR.addEventListener(this, connBean.getIp(), connBean.getPort(),
                AgentMessage.MCS_NVRAM.getCode() 
                | AgentMessage.ERROR.getCode());
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
    }
    

    @Override
    public void onPause() throws Exception {
        setEnabled(false);
        MON_MGR.removeEventListener(this);
    }

    
    @Override
    public void onStop() throws Exception {
        MON_MGR.removeEventListener(this);
    }

    
    @Override
    public void onOK(SpeedRegulation panel) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }

    
    @Override
    public void onReset(SpeedRegulation panel) {
    	synchronized ( mutex ) {
            setEnabled(false);
            reset();
            setEnabled(true);
        }
    }

    
    @Override
    public void onConnCreate() {
        app.start();
        setEnabled(false);
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
    public void onConnLost() {
        app.stop();
        setEnabled(true);
    }

    
    @Override
    public void onDestroy() throws Exception {
    }
    

    @Override
    protected String getPanelTitle () {
        return TEXT.getString( "SpeedRegulation.title" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("Motion"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("SpeedRegulation"), this.getClass());
		return map;
	}
	
    //////////////////////////////////////////////////////////////////////////////////

    private static final class Solid {
        private SpeedRegulation.SpeedCheckingBean bean_SpeedChecking;
        private SpeedRegulation.SpeedOutputBean   bean_SpeedOutput;
        
        private Solid(SpeedCheckingBean bean_SpeedChecking, SpeedOutputBean bean_SpeedOutput) {
            super();
            this.bean_SpeedChecking = bean_SpeedChecking;
            this.bean_SpeedOutput = bean_SpeedOutput;
        }
    }


    public void setHot() {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final SpeedRegulation.SpeedCheckingBean bean_SpeedChecking = new SpeedRegulation.SpeedCheckingBean();
            final SpeedRegulation.SpeedOutputBean   bean_SpeedOutput   = new SpeedRegulation.SpeedOutputBean();

            /* SpeedChecking */
            bean_SpeedChecking.setOverSpeedThreshold( (double) nvram.getFloat( ( short )0x0928 ) );    // NVADDR_OVERSPEED_THRESHOLD (f)
            bean_SpeedChecking.setOverSpeedTimeLimit( nvram.getUnsignedInt( ( short )0x0924 ) );     // NVADDR_OVERSPEED_COUNT_LIMIT (ul)
            bean_SpeedChecking.setSpeedLossThreshold( (double) nvram.getFloat( ( short )0x0930 ) );    // NVADDR_SPEEDLOSS_THRESHOLD (f)
            bean_SpeedChecking.setSpeedLossTimeLimit( nvram.getUnsignedInt( ( short )0x092c ) );     // NVADDR_SPEEDLOSS_COUNT_LIMIT (ul)
//          TODO bean_SpeedChecking.setAccelerationSpeedLossThreshold( nvram.getXXX( (short) 0x0000 ) );
//          TODO bean_SpeedChecking.setAccelerationSpeedLossTimeLimit( nvram.getXXX( (short) 0x0000 ) );
            bean_SpeedChecking.setUslLslOverSpeedThreshold( (double) nvram.getFloat( ( short )0x1034 ) );    // NVADDR_SHAFT_LIMIT_OVER_SPEED_LIMIT (f)
            bean_SpeedChecking.setSpeedCheckUpperThreshold( (double) nvram.getFloat( ( short )0x0934 ) );     // NVADDR_SPEED_CHECK_UPPER_THRESHOLD (f)
            bean_SpeedChecking.setSpeedCheckLowerThreshold( (double) nvram.getFloat( ( short )0x0938 ) );     // NVADDR_SPEED_CHECK_LOWER_THRESHOLD (f)
            bean_SpeedChecking.setSc1_speedCheckUpperThreshold( (double) nvram.getFloat( ( short )0x1950 ) );
            bean_SpeedChecking.setSc1_speedCheckLowerThreshold( (double) nvram.getFloat( ( short )0x1954 ) );
            bean_SpeedChecking.setSc2_speedCheckUpperThreshold( (double) nvram.getFloat( ( short )0x1958 ) );
            bean_SpeedChecking.setSc2_speedCheckLowerThreshold( (double) nvram.getFloat( ( short )0x195c ) );
            bean_SpeedChecking.setSc3_speedCheckUpperThreshold( (double) nvram.getFloat( ( short )0x1960 ) );
            bean_SpeedChecking.setSc3_speedCheckLowerThreshold( (double) nvram.getFloat( ( short )0x1964 ) );
            
            /* SpeedOutput */
            bean_SpeedOutput.setReferenceSpeedAnalogOutputOffset( (double) nvram.getFloat( ( short )0x0910 ) );    // NVADDR_ANALOG_OUTPUT_ZERO_OFFSET (f)
            
            if (solid==null)
                solid = new Solid(bean_SpeedChecking, bean_SpeedOutput);
            
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.setSpeedCheckingBean( bean_SpeedChecking );
                    app.setSpeedOutputBean( bean_SpeedOutput );
                }
            } );
        } catch (Exception e) {
            logger.catching(Level.FATAL, e);
        }
    }

    public boolean submit() {
    	if(IsVerify) {
	        try {
	            final SpeedCheckingBean bean_SpeedChecking = app.getSpeedCheckingBean();
	            final SpeedOutputBean bean_SpeedOutput = app.getSpeedOutputBean();
	            
	            /** Speed Checking. */
	            nvram.setFloat( ( short )0x0928, bean_SpeedChecking.getOverSpeedThreshold().floatValue() );    // NVADDR_OVERSPEED_THRESHOLD (f)
	            nvram.setInt( ( short )0x0924, bean_SpeedChecking.getOverSpeedTimeLimit().intValue() );     // NVADDR_OVERSPEED_COUNT_LIMIT (ul)
	            nvram.setFloat( ( short )0x0930, bean_SpeedChecking.getSpeedLossThreshold().floatValue() );    // NVADDR_SPEEDLOSS_THRESHOLD (f)
	            nvram.setInt( ( short )0x092c, bean_SpeedChecking.getSpeedLossTimeLimit().intValue() );     // NVADDR_SPEEDLOSS_COUNT_LIMIT (ul)
	//          TODO mcsnvram.setXXX( (short) 0x0000, hot.acceleration_speed_loss_threshold );
	//          TODO mcsnvram.setXXX( (short) 0x0000, hot.acceleration_speed_loss_time_limit );
	            nvram.setFloat( ( short )0x1034, bean_SpeedChecking.getUslLslOverSpeedThreshold().floatValue() );    // NVADDR_SHAFT_LIMIT_OVER_SPEED_LIMIT (f)
	            nvram.setFloat( ( short )0x0934, bean_SpeedChecking.getSpeedCheckUpperThreshold().floatValue() );     // NVADDR_SPEED_CHECK_UPPER_THRESHOLD (f)
	            nvram.setFloat( ( short )0x0938, bean_SpeedChecking.getSpeedCheckLowerThreshold().floatValue() );     // NVADDR_SPEED_CHECK_LOWER_THRESHOLD (f)
	            nvram.setFloat( ( short )0x1950, bean_SpeedChecking.getSc1_speedCheckUpperThreshold().floatValue() );
	            nvram.setFloat( ( short )0x1954, bean_SpeedChecking.getSc1_speedCheckLowerThreshold().floatValue() );
	            nvram.setFloat( ( short )0x1958, bean_SpeedChecking.getSc2_speedCheckUpperThreshold().floatValue() );
	            nvram.setFloat( ( short )0x195c, bean_SpeedChecking.getSc2_speedCheckLowerThreshold().floatValue() );
	            nvram.setFloat( ( short )0x1960, bean_SpeedChecking.getSc3_speedCheckUpperThreshold().floatValue() );
	            nvram.setFloat( ( short )0x1964, bean_SpeedChecking.getSc3_speedCheckLowerThreshold().floatValue() );
	            
	            /** Speed output. */
	            nvram.setFloat( ( short )0x0910, bean_SpeedOutput.getReferenceSpeedAnalogOutputOffset().floatValue() );    // NVADDR_ANALOG_OUTPUT_ZERO_OFFSET (f)
	            
	            nvram.commit();
	
	            return true;
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(StartUI.getFrame(), "an error has come. " + e.getMessage());
	            logger.catching(Level.FATAL, e);
	        }
    	}
        return false;
    }

    public void reset() {
        // Update returned data to visualization components.
    	if(!IsVerify) {
    		try {
    			JPasswordField pwd = new JPasswordField();
    			Object[] message = {TEXT.getString("Password.text"), pwd};
    			int res = JOptionPane.showConfirmDialog(this, message, TEXT.getString("Password.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    			if(res == JOptionPane.OK_OPTION) {
    				if("282828".equals(new String(pwd.getPassword()))) {
    					IsVerify = true;
            			app.SetWidgetEnable(true);
    				}else {
    					JOptionPane.showMessageDialog(null,TEXT.getString("Password.error"));
    				}
    				
    			}
    		}catch(Exception e) {
    			JOptionPane.showMessageDialog(null,TEXT.getString("Password.error"));
    		}
    		
    	}else {
	        if (solid != null)
	            SwingUtilities.invokeLater(new Runnable() {
	                @Override
	                public void run() {
	                    if (solid != null) {
	                        app.stop();
	                        app.setSpeedCheckingBean( solid.bean_SpeedChecking );
	                        app.setSpeedOutputBean( solid.bean_SpeedOutput );
	                        app.start();
	                    }
	                }
	            });
    	}
    }
   
    public void gotoCommissionPanel() {
        StartUI.getTopMain().push( SetupPanel.build( connBean, slecon.setting.installation.CommissionSetting.class ) );
    }
}
