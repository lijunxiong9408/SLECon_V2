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
import slecon.setting.setup.motion.Timing.FaultTrippingBean;
import slecon.setting.setup.motion.Timing.StablizationDelayBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import comm.Parser_Error;
import comm.Parser_McsNvram;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




@SetupView(
    path      = "Setup::Motion::Timing",
    sortIndex = 0x213
)
public class TimingSetting extends SettingPanel<Timing> implements Page, LiftDataChangedListener {
    private static final long           serialVersionUID = -9017839004439548693L;
    
    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.SettingPanel" );
    
    /**
     * Logger.
     */
    private final Logger logger = LogManager.getLogger(TimingSetting.class);

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex            = new Object(); 
    private volatile Solid     solid            = null;

    private final LiftConnectionBean connBean;

    private Parser_Error error;

    private Parser_McsNvram nvram;

    private boolean IsVerify = false;


    public TimingSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate(Workspace workspace) throws Exception {
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
    public void onOK(Timing panel) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }

    
    @Override
    public void onReset(Timing panel) {
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
        return TEXT.getString( "MotionTiming.title" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("Motion"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("Timing"), this.getClass());
		return map;
	}
	
    //////////////////////////////////////////////////////////////////////////////////

    private static final class Solid {
        private Timing.FaultTrippingBean     bean_FaultTripping;
        private Timing.StablizationDelayBean bean_StablizationDelay;
        
        private Solid(FaultTrippingBean bean_FaultTripping, StablizationDelayBean bean_StablizationDelay) {
            super();
            this.bean_FaultTripping = bean_FaultTripping;
            this.bean_StablizationDelay = bean_StablizationDelay;
        }
    }


    public void setHot() {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final Timing.FaultTrippingBean     bean_FaultTripping     = new Timing.FaultTrippingBean();
            final Timing.StablizationDelayBean bean_StablizationDelay = new Timing.StablizationDelayBean();

            /* FaultTripping */
            bean_FaultTripping.setCarRunOverTime( nvram.getUnsignedShort( ( short )0x100c ) );               // NVADDR_CAR_RUN_TIMEOUT_LIMIT (ui)
            bean_FaultTripping.setUdzLdzToggleTimeout( nvram.getUnsignedShort( ( short )0x100e ) );          // NVADDR_LV_TOGGLE_TIMEOUT_LIMIT (ui)
            bean_FaultTripping.setDriverEnableFaultHoldoff( nvram.getUnsignedShort( ( short )0x103E ) );     // NVADDR_DRVENF_FAULT_HOLDOFF (ui)
            bean_FaultTripping.setBrakeJamTimeRatio( nvram.getUnsignedByte( ( short )0x1040 ) );             // NVADDR_BRAKE_JAM_TIME_RATIO (uc)
            bean_FaultTripping.setShaftLimitOverSpeedHoldOff( nvram.getUnsignedShort( ( short )0x1044 ) );   // NVADDR_SHAFT_LIMIT_OVER_SPEED_HOLDOFF (ui)
            bean_FaultTripping.setBadUslLslHoldOff( nvram.getUnsignedShort( ( short )0x104A ) );             // NVADDR_BAD_SHAFT_LIMIT_HOLDOFF (ui)
            bean_FaultTripping.setBrakeJamTimeLimit( nvram.getUnsignedShort( ( short )0x106A ) );            // NVADDR_BRAKE_JAM_TIME_LIMIT (ui)

            /* StablizationDelay */
            bean_StablizationDelay.setIdleStableDelay( nvram.getUnsignedShort( ( short )0x1012 ) );          // NVADDR_IDLE_STABLE_DELAY (ui)
            bean_StablizationDelay.setEmergencyStopHoldoff( nvram.getUnsignedShort( ( short )0x101e ) );     // NVADDR_EMERGENCY_STOP_HOLDOFF (ui)
            bean_StablizationDelay.setExitDcsInspectionHoldoff( nvram.getUnsignedShort( ( short )0x1010 ) ); // NVADDR_DCS_INSPECTION_EXIT_DELAY (ui)
            // bean_StablizationDelay.setSafetyChainGoodDelay( nvram.get( XXX ));
            
            if (solid==null)
                solid=new Solid(bean_FaultTripping, bean_StablizationDelay);

            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.setFaultTrippingBean( bean_FaultTripping );
                    app.setStablizationDelayBean( bean_StablizationDelay );
                }
            } );
        } catch (Exception e) {
            logger.catching(Level.FATAL, e);
        }
    }

    public boolean submit() {
    	if(IsVerify) {
    		try {
	            final FaultTrippingBean bean_FaultTripping = app.getFaultTrippingBean();
	            final StablizationDelayBean bean_StablizationDelay = app.getStablizationDelayBean();
	
	            /** Fault tripping. */
	            nvram.setShort( ( short )0x100c, bean_FaultTripping.getCarRunOverTime().shortValue() );               // NVADDR_CAR_RUN_TIMEOUT_LIMIT (ui)
	            nvram.setShort( ( short )0x100e, ( short )bean_FaultTripping.getUdzLdzToggleTimeout().shortValue() ); // NVADDR_LV_TOGGLE_TIMEOUT_LIMIT (ui)
	            nvram.setShort( ( short )0x103E, bean_FaultTripping.getDriverEnableFaultHoldoff().shortValue() );     // NVADDR_DRVEN_FAULT_HOLDOFF (ui)
	            nvram.setShort( ( short )0x1040, bean_FaultTripping.getBrakeJamTimeRatio().shortValue() );            // NVADDR_BRAKE_JAM_TIME_RATIO (uc)
	            nvram.setShort( ( short )0x1044, bean_FaultTripping.getShaftLimitOverSpeedHoldOff().shortValue() );   // NVADDR_SHAFT_LIMIT_OVER_SPEED_HOLDOFF (ui)
	            nvram.setShort( ( short )0x104A, bean_FaultTripping.getBadUslLslHoldOff().shortValue() );             // NVADDR_BAD_SHAFT_LIMIT_HOLDOFF (ui)
	            nvram.setShort( ( short )0x106A, bean_FaultTripping.getBrakeJamTimeLimit().shortValue() );
	//          mcsnvram.setXXX( (short) 0x0000, bean_FaultTripping.car_run_over_time_en ); // NVADDR_CAR_RUN_TIMEOUT_LIMIT_EN (??)
	//          mcsnvram.setXXX( (short) 0x0000, bean_FaultTripping.udz_ldz_toggle_timeout_en ); // NVADDR_LV_TOGGLE_TIMEOUT_LIMIT_EN (??)
	//          mcsnvram.setXXX( (short) 0x0000, bean_FaultTripping.driver_enable_fault_holdoff_en ); // NVADDR_DRVEN_FAULT_HOLDOFF_EN (??)
	//          mcsnvram.setXXX( (short) 0x0000, bean_FaultTripping.brake_jam_time_ratio_en ); // NVADDR_BRAKE_JAM_TIME_RATIO_EN (??)
	            
	            /** Stablization delay. */
	            nvram.setShort( ( short )0x1012, bean_StablizationDelay.getIdleStableDelay().shortValue() );           // NVADDR_IDLE_STABLE_DELAY (ui)
	            nvram.setShort( ( short )0x101e, bean_StablizationDelay.getEmergencyStopHoldoff().shortValue() );      // NVADDR_EMERGENCY_STOP_HOLDOFF (ui)
	            nvram.setShort( ( short )0x1010, bean_StablizationDelay.getExitDcsInspectionHoldoff().shortValue() );  // NVADDR_DCS_INSPECTION_EXIT_DELAY (ui)
	//          nvram.setXXX( ( short ) XXX, bean_StablizationDelay.safety_chain_good_delay );
	
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
	                        app.setFaultTrippingBean( solid.bean_FaultTripping );
	                        app.setStablizationDelayBean( solid.bean_StablizationDelay );
	                        app.start();
	                    }
	                }
	            });
    	}
    }
}
