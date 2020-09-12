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
import slecon.setting.setup.motion.Sequence.PreRunSequenceBean;
import slecon.setting.setup.motion.Sequence.PreStopSequenceBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Error;
import comm.Parser_McsNvram;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;




@SetupView(
    path      = "Setup::Motion::Sequence",
    sortIndex = 0x211
)
public class SequenceSetting extends SettingPanel<Sequence> implements Page, comm.event.LiftDataChangedListener {
    private static final long           serialVersionUID = -7119975411025740204L;

    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.SettingPanel" );
    
    /**
     * Logger.
     */
    private final Logger logger = LogManager.getLogger(SequenceSetting.class);

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex            = new Object(); 
    private volatile Solid     solid            = null;

    private final LiftConnectionBean connBean;

    private Parser_Error error;

    private Parser_McsNvram nvram;

    private boolean IsVerify = false;


    public SequenceSetting ( LiftConnectionBean connBean ) {
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
    public void onOK(Sequence panel) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }

    
    @Override
    public void onReset(Sequence panel) {
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
        return TEXT.getString( "MotionSequence.title" );
    }

	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("Motion"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("Sequence"), this.getClass());
		return map;
	}
    //////////////////////////////////////////////////////////////////////////////////

    private static final class Solid {
        private final Sequence.PreRunSequenceBean  bean_PreRunSequence;
        private final Sequence.PreStopSequenceBean bean_PreStopSequence;
        
        private Solid(PreRunSequenceBean bean_PreRunSequence, PreStopSequenceBean bean_PreStopSequence) {
            super();
            this.bean_PreRunSequence = bean_PreRunSequence;
            this.bean_PreStopSequence = bean_PreStopSequence;
        }
    }

    
    public void setHot() {
        lastestTimeStamp = System.nanoTime();
        
        try {

            final Sequence.PreRunSequenceBean bean_PreRunSequence = new Sequence.PreRunSequenceBean();
            final Sequence.PreStopSequenceBean bean_PreStopSequence = new Sequence.PreStopSequenceBean();

            /* PreRunSequence */
            bean_PreRunSequence.setPreRunCheckTimeoutLimit(nvram.getUnsignedShort((short) 0x1014)); // NVADDR_PRERUN_CHECK_TIMEOUT_LIMIT (ui)
            bean_PreRunSequence.setUrDrCloseDelay(nvram.getUnsignedShort((short) 0x1020));          // NVADDR_UDR_CLOSE_DELAY (ui)
            bean_PreRunSequence.setRrCloseDelay(nvram.getUnsignedShort((short) 0x1022));            // NVADDR_RR_CLOSE_DELAY (ui)
            bean_PreRunSequence.setRrCloseFeedbackTimeout(nvram.getUnsignedShort((short) 0x1024));  // NVADDR_RR_CLOSE_TIMEOUT_LIMIT (ui)
            bean_PreRunSequence.setPretorqueOkTimeout(nvram.getUnsignedShort((short) 0x1016));      // NVADDR_PRETORQUE_TIMEOUT_LIMIT (ui)
            bean_PreRunSequence.setDriverBrakeOpenTimeout(nvram.getUnsignedShort((short) 0x1018));  // NVADDR_DRIVER_BRAKE_OPEN_TIMEOUT_LIMIT (ui)
            bean_PreRunSequence.setBrakeOpenDelay(nvram.getUnsignedShort((short) 0x1026));          // NVADDR_BRAKE_OPEN_DELAY (ui)
            bean_PreRunSequence.setBrakeOpenFeedbackTimeout(nvram.getUnsignedShort((short) 0x1028)); // NVADDR_BRAKE_OPEN_TIMEOUT_LIMIT (ui)

            /* PreStopSequence */
            bean_PreStopSequence.setDriverBrakeCloseTimeout(nvram.getUnsignedShort((short) 0x101a));   // NVADDR_DRIVER_BRAKE_CLOSE_TIMEOUT_LIMIT (ui)
            bean_PreStopSequence.setBrakeCloseDelay(nvram.getUnsignedShort((short) 0x102a));           // NVADDR_BRAKE_CLOSE_DELAY (ui)
            bean_PreStopSequence.setBrakeCloseFeedbackTimeout(nvram.getUnsignedShort((short) 0x102c)); // NVADDR_BRAKE_CLOSE_TIMEOUT_LIMIT (ui)
            bean_PreStopSequence.setDriverDisableDelay(nvram.getUnsignedShort((short) 0x101c));        // NVADDR_DRIVER_DISABLE_DELAY (ui)
            bean_PreStopSequence.setMainContactsOpenDelay(nvram.getUnsignedShort((short) 0x102e));     // NVADDR_UDRR_OPEN_DELAY (ui)
            bean_PreStopSequence.setMainContactsOpenFeedbackDelay(nvram.getUnsignedShort((short) 0x1042)); // NVADDR_DBDF_MON_DELAY (ui)

            if (solid == null)
                solid = new Solid(bean_PreRunSequence, bean_PreStopSequence);
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    app.stop();
                    app.setPreRunSequenceBean(bean_PreRunSequence);
                    app.setPreStopSequenceBean(bean_PreStopSequence);
                    app.start();
                }
            });
        } catch (Exception e) {
            logger.catching(Level.FATAL, e);
        }
    }

    public boolean submit() {
    	if(IsVerify) {
    		try {
	            final PreRunSequenceBean bean_PreRunSequence = app.getPreRunSequenceBean();
	            final PreStopSequenceBean bean_PreStopSequence = app.getPreStopSequenceBean();
	
	            /* Pre-run Sequence. */
	            nvram.setShort( ( short )0x1014, bean_PreRunSequence.getPreRunCheckTimeoutLimit().shortValue() );   // NVADDR_PRERUN_CHECK_TIMEOUT_LIMIT (ui)
	            nvram.setShort( ( short )0x1020, bean_PreRunSequence.getUrDrCloseDelay().shortValue() );            // NVADDR_UDR_CLOSE_DELAY (ui)
	            nvram.setShort( ( short )0x1022, bean_PreRunSequence.getRrCloseDelay().shortValue() );              // NVADDR_RR_CLOSE_DELAY (ui)
	            nvram.setShort( ( short )0x1024, bean_PreRunSequence.getRrCloseFeedbackTimeout().shortValue() );    // NVADDR_RR_CLOSE_TIMEOUT_LIMIT (ui)
	            nvram.setShort( ( short )0x1016, bean_PreRunSequence.getPretorqueOkTimeout().shortValue() );        // NVADDR_PRETORQUE_TIMEOUT_LIMIT (ui)
	            nvram.setShort( ( short )0x1018, bean_PreRunSequence.getDriverBrakeOpenTimeout().shortValue() );    // NVADDR_DRIVER_BRAKE_OPEN_TIMEOUT_LIMIT (ui)
	            nvram.setShort( ( short )0x1026, bean_PreRunSequence.getBrakeOpenDelay().shortValue() );            // NVADDR_BRAKE_OPEN_DELAY (ui)
	            nvram.setShort( ( short )0x1028, bean_PreRunSequence.getBrakeOpenFeedbackTimeout().shortValue() );  // NVADDR_BRAKE_OPEN_TIMEOUT_LIMIT (ui)
	
	            /* Pre-Stop Sequence. */
	            nvram.setShort( ( short )0x101a, bean_PreStopSequence.getDriverBrakeCloseTimeout().shortValue() );        // NVADDR_DRIVER_BRAKE_CLOSE_TIMEOUT_LIMIT (ui)
	            nvram.setShort( ( short )0x102a, bean_PreStopSequence.getBrakeCloseDelay().shortValue() );                // NVADDR_BRAKE_CLOSE_DELAY (ui)
	            nvram.setShort( ( short )0x102c, bean_PreStopSequence.getBrakeCloseFeedbackTimeout().shortValue() );      // NVADDR_BRAKE_CLOSE_TIMEOUT_LIMIT (ui)
	            nvram.setShort( ( short )0x101c, bean_PreStopSequence.getDriverDisableDelay().shortValue() );             // NVADDR_DRIVER_DISABLE_DELAY (ui)
	            nvram.setShort( ( short )0x102e, bean_PreStopSequence.getMainContactsOpenDelay().shortValue() );          // NVADDR_UDRR_OPEN_DELAY (ui)
	            nvram.setShort( ( short )0x1042, bean_PreStopSequence.getMainContactsOpenFeedbackDelay().shortValue() );  // NVADDR_DBDF_MON_DELAY (ui)
	            
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
	                        app.setPreRunSequenceBean( solid.bean_PreRunSequence );
	                        app.setPreStopSequenceBean( solid.bean_PreStopSequence );
	                        app.start();
	                    }
	                }
	            });
    	}	
    }

}
