package slecon.setting.advanced;
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
import slecon.setting.advanced.Advanced.AdvanceDoorOpenBean;
import slecon.setting.advanced.Advanced.BrakeBean;
import slecon.setting.advanced.Advanced.DynamicStatus;
import slecon.setting.advanced.Advanced.OpenBrakeMonitor;
import slecon.setting.advanced.Advanced.OthersBean;
import slecon.setting.advanced.Advanced.PositionAdjustmentBean;
import slecon.setting.advanced.Advanced.ReLevelBean;
import slecon.setting.advanced.Advanced.RunningBrakeMonitor;
import slecon.setting.advanced.Advanced.StandbyBrakeMonitor;
import slecon.setting.advanced.Advanced.StopBrakeMonitor;
import slecon.setting.advanced.Advanced.TemperatureBean;
import slecon.setting.setup.motion.SensorType;
import slecon.setting.setup.motion.SequenceSetting;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Error;
import comm.Parser_McsConfig;
import comm.Parser_McsNvram;
import comm.Parser_Misc;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




@SetupView(
    path      = "Setup::Motion::Misc",
    sortIndex = 0x215
)
public class AdvancedSetting extends SettingPanel<Advanced> implements Page, LiftDataChangedListener {
    private static final long           serialVersionUID = 7549749392929873482L;

    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.SettingPanel");

    /**
     * Logger.
     */
    private final Logger logger = LogManager.getLogger(AdvancedSetting.class);

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid solid = null;

    private final LiftConnectionBean connBean;

    private Parser_Error error;

    private Parser_McsConfig mcsconfig;
    
    private Parser_McsNvram nvram;
    
    private Parser_Misc     misc;
    
    private Parser_Status	status;
    
    private boolean IsVerify = false;
    
    private static int update_flag = 0x00;
    
    private static boolean first_into  = true;

    public AdvancedSetting ( LiftConnectionBean connBean ) {
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
            misc =  new Parser_Misc(connBean.getIp(), connBean.getPort());
            mcsconfig = new Parser_McsConfig(connBean.getIp(), connBean.getPort());
            status = new Parser_Status(connBean.getIp(), connBean.getPort());
            MON_MGR.addEventListener(this, connBean.getIp(), connBean.getPort(),
                    AgentMessage.MCS_CONFIG.getCode() 
                    | AgentMessage.MCS_NVRAM.getCode() 
                    | AgentMessage.ERROR.getCode()
                    | AgentMessage.STATUS.getCode());
            setHot( 0x03 );
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
                AgentMessage.MCS_CONFIG.getCode() 
                | AgentMessage.MCS_NVRAM.getCode() 
                | AgentMessage.ERROR.getCode()
                | AgentMessage.STATUS.getCode());
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MCS));
    }

    
    @Override
    public void onPause() throws Exception {
        setEnabled(false);
        first_into  = true;
        MON_MGR.removeEventListener(this);
    }

    
    @Override
    public void onStop() throws Exception {
    	first_into  = true;
        MON_MGR.removeEventListener(this);
    }

    
    @Override
    public void onOK(Advanced panel) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }

    
    @Override
    public void onReset(Advanced panel) {
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
        
        if(msg==AgentMessage.STATUS.getCode()) {
        	synchronized (mutex) {
                setHot( 0x01 );
            }
        }
        
        if(msg==AgentMessage.MCS_NVRAM.getCode()) {
        	synchronized (mutex) {
                setEnabled(false);
                if (solid != null) {
                	solid = null;
                    setHot( 0x02 );
                }
                setEnabled(true);
            }	
        }
        
        
    }

    
    @Override
    public void onConnLost() {
    	first_into = true;
        app.stop();
        setEnabled(true);
    }

    @Override
    public void onDestroy() throws Exception {
    }

    @Override
    protected String getPanelTitle () {
        return TEXT.getString( "Misc.title" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("Advanced"), this.getClass());
		return map;
	}

    //////////////////////////////////////////////////////////////////////////////////

    private static final class Solid {
        private final Advanced.PositionAdjustmentBean bean_PositionAdjustment;
        private final Advanced.AdvanceDoorOpenBean    bean_AdvanceDoorOpen;
        private final Advanced.ReLevelBean            bean_ReLevelBean;
        private final Advanced.OthersBean             bean_others;
        private final Advanced.BrakeBean			  bean_BrakeBean;
        private final Advanced.TemperatureBean		  bean_temp;
        private final Advanced.DynamicStatus		  bean_status;
        
        private Solid(PositionAdjustmentBean bean_PositionAdjustment, AdvanceDoorOpenBean bean_AdvanceDoorOpen,
        			  ReLevelBean bean_ReLevel, OthersBean bean_others, BrakeBean bean_brake, TemperatureBean bean_temp, DynamicStatus bean_status) {
            super();
            this.bean_PositionAdjustment = bean_PositionAdjustment;
            this.bean_AdvanceDoorOpen = bean_AdvanceDoorOpen;
            this.bean_ReLevelBean = bean_ReLevel;
            this.bean_others = bean_others;
            this.bean_BrakeBean = bean_brake;
            this.bean_temp = bean_temp;
            this.bean_status = bean_status;
        }
    }


    public void setHot( int flag ) {
        lastestTimeStamp = System.nanoTime();
        update_flag = flag;
        try {
            final Advanced.PositionAdjustmentBean bean_PositionAdjustment = new Advanced.PositionAdjustmentBean();
            final Advanced.AdvanceDoorOpenBean    bean_AdvanceDoorOpen    = new Advanced.AdvanceDoorOpenBean();
            final Advanced.ReLevelBean            bean_ReLevel            = new Advanced.ReLevelBean();
            final Advanced.OthersBean             bean_others       	  = new Advanced.OthersBean();
            final Advanced.BrakeBean			  bean_brake		      = new Advanced.BrakeBean();
            final Advanced.TemperatureBean		  bean_temp		    	  = new Advanced.TemperatureBean();
            final Advanced.DynamicStatus		  bean_status		      = new Advanced.DynamicStatus();
            
            /* Position Adjustment. */
            bean_PositionAdjustment.setEnable(( nvram.getUnsignedByte((short)0x1061).intValue()) == 1?true:false);   // NVADDR_POSITION_ADJUST_ENABLE uc
           	bean_PositionAdjustment.setHoldoffTime(nvram.getUnsignedShort((short)0x1064));							// NVADDR_POSITION_ADJUST_HOLDOFF 0x1064 ui
            bean_PositionAdjustment.setTolerance( (double) nvram.getFloat( ( short )0x1038 ) );    // NVADDR_POSITION_ADJUSTMENT_TOLERANCE (f)
            
            /* Advance Door Open. */
            bean_AdvanceDoorOpen.setEnable0(( nvram.getUnsignedByte((short)0x1062).intValue()) == 1?true:false);  // NVADDR_ADO_ENABLE	 0x1062	 uc
            bean_AdvanceDoorOpen.setActiveSpeedThreshold( (double) nvram.getFloat( ( short )0x1030 ) );    // NVADDR_ADO_SPEED (f)
            
            /* Re-Leveling. */
            bean_ReLevel.setEnable(( nvram.getUnsignedByte((short)0x1060).intValue()) == 1?true:false); // NVADDR_RELEVELING_ENABLE	(uc) 
            bean_ReLevel.setSensortype(nvram.getUnsignedShort( ( short )0x1066).intValue() == 1 ? SensorType.URL$LRL:SensorType.LPS01 ); // NVADDR_RELEVELING_SENSOR_TYPE	(ui)
            bean_ReLevel.setTriggerHoldOffTime( nvram.getUnsignedShort( ( short )0x104c ) );    // NVADDR_RELEVELING_TRIGGER_HOLDOFF (ui)
            bean_ReLevel.setTimeLimit( nvram.getUnsignedShort( ( short )0x1050 ) );             // NVADDR_RELEVELING_TIME_LIMIT (ui)
            bean_ReLevel.setRetryCount( nvram.getUnsignedByte( ( short )0x104E ) );     // NVADDR_RELEVELING_RETRY (uc)
            
            /* Others. */
            bean_others.setReverseRunPositionLimit( ( double )nvram.getFloat( ( short ) 0x1058  ) );   // NVADDR_REVERSE_RUN_DISP_LIMIT (f)
            
            /* Brake Monitor. */
            bean_brake.setBrake_open_retry_counts( nvram.getUnsignedShort((short)0x1072));
            bean_brake.setBrake_close_retry_counts( nvram.getUnsignedShort((short)0x1074));
            
            int brake_value = nvram.getUnsignedByte( ( short )0x105f).intValue();
            bean_brake.setOpen_brake_monitor(( brake_value & 0x03) == 0x02 ? OpenBrakeMonitor.OPEN_AT_ONCE_LOCKED : OpenBrakeMonitor.OPEN_COUNT_LOCKED);
            bean_brake.setRunning_brake_monitor((brake_value >> 2 & 0x03) == 0x02 ? RunningBrakeMonitor.EMERGENCY_STOP_LOCKED : RunningBrakeMonitor.NEAR_STOP_LOCKED);
            bean_brake.setStop_brake_monitor((brake_value >> 4 & 0x03) == 0x02 ? StopBrakeMonitor.STOP_AT_ONCE_LOCKED : StopBrakeMonitor.STOP_COUNT_LOCKED);
            bean_brake.setStandby_brake_monitor((brake_value >> 6 & 0x03) == 0x02 ? StandbyBrakeMonitor.STANDBY_COUNT_LOCKED : StandbyBrakeMonitor.STANDBY_AT_ONCE_LOCKED);
            
            Number temp = status.getCurrentTemp();
            bean_status.setTemperature( temp );
            bean_status.setInspect( status.getInspectMode() );
            
            bean_temp.setDelay( nvram.getUnsignedShort( (short)0x1968 ) );
            bean_temp.setStart_temp( nvram.getUnsignedByte( (short)0x196A ) );
            bean_temp.setStop_temp( nvram.getUnsignedByte( (short)0x196B ) );
            
            if(solid==null)
                solid = new Solid(bean_PositionAdjustment, bean_AdvanceDoorOpen, bean_ReLevel, bean_others, bean_brake, bean_temp, bean_status);
            if ( first_into == false ) {
	            SwingUtilities.invokeLater( new Runnable() {
	                @Override
	                public void run () {
	                	if((update_flag & 0x02) == 0x02) {
	                		app.setPositionAdjustmentBean( bean_PositionAdjustment );
	                        app.setAdvanceDoorOpenBean( bean_AdvanceDoorOpen );
	                        app.setReLevelBean(bean_ReLevel);
	                        app.setOthersBean(bean_others);
	                        app.setBrakeBean(bean_brake);
	                        app.SetCcfTemperature(bean_temp);
	                        app.setInpectionMode(bean_status);
	                	}
	                    
	                	if((update_flag & 0x01) == 0x01) {
	                		app.setDynamicStatus(bean_status);
	                	}
	                }
	            } );
            }else {
            	first_into = false;
            	app.setPositionAdjustmentBean( bean_PositionAdjustment );
                app.setAdvanceDoorOpenBean( bean_AdvanceDoorOpen );
                app.setReLevelBean(bean_ReLevel);
                app.setOthersBean(bean_others);
                app.setBrakeBean(bean_brake);
                app.SetCcfTemperature(bean_temp);
                app.setInpectionMode(bean_status);
        		app.setDynamicStatus(bean_status);
            }
        } catch (Exception e) {
            logger.catching(Level.FATAL, e);
        }
    }

    public boolean submit() {
    	if(IsVerify) {
	        try {
	            final PositionAdjustmentBean bean_PositionAdjustment = app.getPositionAdjustmentBean();
	            final AdvanceDoorOpenBean bean_AdvanceDoorOpen = app.getAdvanceDoorOpenBean();
	            final ReLevelBean bean_ReLevel = app.getReLevelBean();
	            final OthersBean bean_others = app.getOthersBean();
	            final BrakeBean bean_brake = app.getBrakeBean();
	            final TemperatureBean bean_temp = app.getCurrentTemperature();
	            final DynamicStatus bean_status = app.getDynamicStatus();
	
	            /* Position Adjustment. */
	            nvram.setByte((short)0x1061, (byte)(bean_PositionAdjustment.getEnable() ? 1 : 0 ));				// NVADDR_POSITION_ADJUST_ENABLE uc
	            nvram.setShort((short)0x1064, bean_PositionAdjustment.getHoldoffTime().shortValue());			// NVADDR_POSITION_ADJUST_HOLDOFF 0x1064 ui
	            nvram.setFloat( ( short )0x1038, bean_PositionAdjustment.getTolerance().floatValue() );         // NVADDR_POSITION_ADJUSTMENT_TOLERANCE (f)
	
	            /* Advance Door Open. */
	            nvram.setByte((short)0x1062,(byte)(bean_AdvanceDoorOpen.getEnable0() ? 1 : 0));					// NVADDR_ADO_ENABLE	 0x1062	 uc
	            nvram.setFloat( ( short )0x1030, bean_AdvanceDoorOpen.getActiveSpeedThreshold().floatValue() ); // NVADDR_ADO_SPEED (f)
	
	            /* Re-Leveling. */
	            nvram.setByte((short)0x1060,(byte)(bean_ReLevel.getEnable() ? 1 : 0));							// NVADDR_RELEVELING_ENABLE	(uc)
	            nvram.setShort((short)0x1066,(short)(bean_ReLevel.getSensortype().toString().equals("URL/LRL") ? 1 : 0 ));	// NVADDR_RELEVELING_SENSOR_TYPE	(ui)
	            nvram.setShort( ( short )0x104c, bean_ReLevel.getTriggerHoldOffTime().shortValue() );      		// NVADDR_RELEVELING_TRIGGER_HOLDOFF (ui)
	            nvram.setShort( ( short )0x1050, bean_ReLevel.getTimeLimit().shortValue() );                	// NVADDR_RELEVELING_TIME_LIMIT (ui)
	          	nvram.setByte( ( short )0x104E, bean_ReLevel.getRetryCount().byteValue() );                 	// NVADDR_RELEVELING_RETRY (uc)
	
	            /* Others. */
	            nvram.setFloat( ( short )0x1058, bean_others.getReverseRunPositionLimit().floatValue() );   // NVADDR_REVERSE_RUN_DISP_LIMIT (f)
	            
	            /* Brake. */
	            nvram.setShort((short)0x1072, bean_brake.getBrake_open_retry_counts().shortValue());
	            nvram.setShort((short)0x1074, bean_brake.getBrake_close_retry_counts().shortValue());
	            
	            int brake_value = 0;
	            brake_value |= ((OpenBrakeMonitor)bean_brake.getOpen_brake_monitor()).getCode();
	            brake_value |= ((RunningBrakeMonitor)bean_brake.getRunning_brake_monitor()).getCode();
	            brake_value |= ((StopBrakeMonitor)bean_brake.getStop_brake_monitor()).getCode();
	            brake_value |= ((StandbyBrakeMonitor)bean_brake.getStandby_brake_monitor()).getCode();
	            nvram.setByte((short)0x105f, (byte)brake_value );
	            
	            /* Temperature. */
	            nvram.setShort( (short)0x1968, bean_temp.getDelay().shortValue() );
	            nvram.setByte((short)0x196A, bean_temp.getStart_temp().byteValue());
	            nvram.setByte((short)0x196B, bean_temp.getStop_temp().byteValue() );
	            
	            nvram.commit();
	            
	            // Set Inspection Mode.
	            byte[] dat = new byte[4];
	            dat[0] = (byte)bean_status.getInspect();
	            misc.mcs((short)0x2ffe, dat);
	            
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
            			app.Visit_Enable = true;
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
	                        app.setPositionAdjustmentBean( solid.bean_PositionAdjustment );
	                        app.setAdvanceDoorOpenBean( solid.bean_AdvanceDoorOpen );
	                        app.setReLevelBean(solid.bean_ReLevelBean);
	                        app.setOthersBean(solid.bean_others);
	                        app.start();
	                    }
	                }
	            });
    	}
    }
    
    public void reloadDefaultNVRAM() {
    	misc.mcs((short)0x2819, new byte[0]); // [CMD] CMD_RELOAD_DEFAULT_NVRAM
    }
}
