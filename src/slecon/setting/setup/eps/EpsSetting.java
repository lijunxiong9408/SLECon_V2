package slecon.setting.setup.eps;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.connection.LiftConnectionBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import base.cfg.BaseFactory;
import base.cfg.INIFile;
import comm.Parser_Deploy;
import comm.Parser_DoorEnable;
import comm.Parser_Error;
import comm.Parser_Misc;
import comm.Parser_EPS;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;
import comm.util.Endian;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.setup.eps.Eps.SetupParamBean;


@SetupView(
    path      = "Setup::Eps Setup",
    sortIndex = 0x242,
    condition = ""
)
public class EpsSetting extends SettingPanel<Eps> implements Page,LiftDataChangedListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8077316991726658200L;

	private static final Logger logger           = LogManager.getLogger( EpsSetting.class );
    
    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.SettingPanel");
    
    private volatile long      lastestTimeStamp = -1;
    
    private final Object  mutex  = new Object(); 
    
    private volatile Solid solid = null;
    
    private LiftConnectionBean connBean;

    private Parser_Error  error;
    private Parser_EPS    eps;
    private Parser_Misc   misc;
    
    private boolean IsVerify = false;

    public EpsSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }

    //////////////////////////////// <Page interface> ////////////////////////////////
    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    	
    }

    @Override
    public void onStart () throws Exception {
    	 try {
             error  = new Parser_Error( connBean.getIp(), connBean.getPort() );
             eps    = new Parser_EPS( connBean.getIp(), connBean.getPort() ); 
             misc   = new Parser_Misc( connBean.getIp(), connBean.getPort() );
             MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
            		 					AgentMessage.EPS.getCode() |
                                        AgentMessage.ERROR.getCode()
                                       );
             byte[] Data = new byte[4];
             misc.can((short)0x1002, Data);
             
         	Runtime.getRuntime().addShutdownHook(new Thread() {
        		@Override
        		public void run() {
        			try {
        				byte[] Data = new byte[4];
        		        misc.can((short)0x1003, Data);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        		}
        	});
         	
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
					                AgentMessage.DOOR_ENABLE.getCode() |  
					                AgentMessage.ERROR.getCode() |
					                AgentMessage.DEPLOYMENT.getCode() );
					        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
    }

    @Override
    public void onPause () throws Exception {
    	setEnabled( false );
        MON_MGR.removeEventListener( this );
    }
    
    @Override
    public void onOK ( Eps panel ) {
    	synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( Eps panel ) {
    	reset();
    }
    
    @Override
    public void onStop () throws Exception {
    	byte[] Data = new byte[4];
        misc.can((short)0x1003, Data);
    	MON_MGR.removeEventListener( this );
    }

    @Override
    public void onDestroy () {
    	byte[] Data = new byte[4];
        misc.can((short)0x1003, Data);
    	MON_MGR.removeEventListener( this );
    }
    
    @Override
    protected String getPanelTitle () {
        return TEXT.getString( "EpsSetup.title" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("Eps_Setup"), this.getClass());
		return map;
	}

	@Override
	public void onConnCreate() {
		// TODO Auto-generated method stub
		app.start();
        setEnabled( false );
	}


	@Override
	public void onDataChanged(long timestamp, int msg) {
		// TODO Auto-generated method stub
		 if(msg==AgentMessage.EPS.getCode()) 
	            ToolBox.showRemoteErrorMessage(connBean, error);
		 synchronized (mutex) {
			 if(timestamp >= (lastestTimeStamp + 300*1000000 )) {
				 setHot();
			 }
	            
	     }
	}


	@Override
	public void onConnLost() {
		// TODO Auto-generated method stub
		app.stop();
        setEnabled( true );
	}
	  
	public void setHot () {
		try {
			lastestTimeStamp = System.nanoTime();
			
			final Eps.EPSBEAN bean = new Eps.EPSBEAN(); 
			final Eps.SetupParamBean setupParamBean = new Eps.SetupParamBean();
			bean.setHip(eps.getHIPStatus());
			bean.setVbus(eps.getVBUS());
			bean.setVbat(eps.getVBAT());
			bean.setCurr(eps.getCURR());
			bean.setSpeed(eps.getSpeed());
			
			setupParamBean.setDirect(Endian.getShort(eps.getDirection(), 0));
			setupParamBean.setPulse(Endian.getShort(eps.getEncoderPulse(), 0));
			setupParamBean.setTractor_wheel(Endian.getShort(eps.getTractorWheel(), 0));
			setupParamBean.setSuspend_count(Endian.getShort(eps.getSuspend(), 0));
			setupParamBean.setExit_time(Endian.getShort(eps.getExitTime(), 0));
			setupParamBean.setChange_time(Endian.getShort(eps.getChangeTime(), 0));
			setupParamBean.setNormal_voltage(Endian.getShort(eps.getNormalVol(), 0));
			setupParamBean.setLack_voltage(Endian.getShort(eps.getLackVol(), 0));
			setupParamBean.setFull_voltage(Endian.getShort(eps.getFullVol(), 0));
			setupParamBean.setLow_voltage(Endian.getShort(eps.getLowVol(), 0));
			setupParamBean.setProtect_voltage(Endian.getShort(eps.getProtectVol(), 0));
			setupParamBean.setCharger_current(Endian.getShort(eps.getChargerVol(), 0));
			setupParamBean.setOpen_voltage(Endian.getShort(eps.getOpenVol(), 0));
			setupParamBean.setReduced_voltage(Endian.getShort(eps.getReducedVol(), 0));
			setupParamBean.setNormal_power_hz(Endian.getShort(eps.getNormalPowVol(), 0));
			setupParamBean.setInverter_power_hz(Endian.getShort(eps.getInverterPowVol(), 0));
			setupParamBean.setBrake_open_time(Endian.getShort(eps.getBrakeOpenTime(), 0));
			
			app.setEPSBean(bean);
			if(solid == null) {
				solid = new Solid(setupParamBean);
				app.setSetupParam(setupParamBean);
			}
			
			if(!solid.getSetupParamBean().IsValueEquals(setupParamBean)) {
				app.setSetupParam(setupParamBean);
				solid = null;
			}
			
			
		}catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
	}
	
	private boolean submit ()  {
		if(IsVerify) { 
	        try {
	        	myThread setThread = new myThread();
	        	setThread.start();
	            return true;
	        } catch ( Exception e ) {
	            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
	            logger.catching( Level.FATAL, e );
	        }
		}
        return false;
    }
	
	private void reset () {
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
		    // Update returned data to visualization components.
		    if ( solid != null )
		        SwingUtilities.invokeLater( new Runnable() {
		            @Override
		            public void run () {
		                if ( solid != null ) {
		                    app.stop();
		                    app.start();
		                }
		            }
		        } );
    	}
	}
	 ///////////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        final Eps.SetupParamBean setupParamBean;
        private Solid (SetupParamBean setupParamBean) {
        	this.setupParamBean = setupParamBean;
        }
		public Eps.SetupParamBean getSetupParamBean() {
			return setupParamBean;
		}
        
    }
    
    public void SetSetupParam(short type, short param) {
    	byte[] data = new byte[4];
    	data[0] = (byte) (type & 0xFF);
    	data[1] = (byte) (type >> 8 & 0xFF);
    	data[2] = (byte) (param & 0xFF);
    	data[3] = (byte) (param >> 8 & 0xFF);
    	misc.can((short)0x1004, data);
    }
    
    class myThread extends Thread{
		@Override
		public void run() {
			try {
				final Eps.SetupParamBean setupParamBean = app.getSetupParamBean();
				if(setupParamBean.getDirect() != -1) {
					SetSetupParam((short)0,setupParamBean.getDirect());
	        		Thread.sleep(200);
				}
				if(setupParamBean.getPulse() != -1) {
					SetSetupParam((short)1,setupParamBean.getPulse());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getTractor_wheel() != -1) {
					SetSetupParam((short)2,setupParamBean.getTractor_wheel());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getSuspend_count() != -1) {
					SetSetupParam((short)3,setupParamBean.getSuspend_count());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getExit_time() != -1) {
					SetSetupParam((short)4,setupParamBean.getExit_time());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getChange_time() != -1) {
					SetSetupParam((short)5,setupParamBean.getChange_time());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getNormal_voltage() != -1) {
					SetSetupParam((short)6,setupParamBean.getNormal_voltage());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getLack_voltage() != -1) {
					SetSetupParam((short)7,setupParamBean.getLack_voltage());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getFull_voltage() != -1) {
					SetSetupParam((short)8,setupParamBean.getFull_voltage());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getLow_voltage() != -1) {
					SetSetupParam((short)9,setupParamBean.getLow_voltage());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getProtect_voltage() != -1) {
					SetSetupParam((short)10,setupParamBean.getProtect_voltage());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getCharger_current() != -1) {
					SetSetupParam((short)11,setupParamBean.getCharger_current());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getOpen_voltage() != -1) {
					SetSetupParam((short)12,setupParamBean.getOpen_voltage());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getReduced_voltage() != -1) {
					SetSetupParam((short)13,setupParamBean.getReduced_voltage());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getNormal_power_hz() != -1) {
					SetSetupParam((short)14,setupParamBean.getNormal_power_hz());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getInverter_power_hz() != -1) {
					SetSetupParam((short)15,setupParamBean.getInverter_power_hz());
		        	Thread.sleep(200);
				}
				if(setupParamBean.getBrake_open_time() != -1) {
					SetSetupParam((short)16,setupParamBean.getBrake_open_time());
		        	Thread.sleep(200);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
    	
    }
}
