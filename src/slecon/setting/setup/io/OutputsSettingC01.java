package slecon.setting.setup.io;
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
import logic.io.crossbar.CrossBar;
import logic.io.crossbar.OutputPinC01;
import logic.io.crossbar.OutputSourceC01;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.setup.io.OutputsC01.DigitalOutputBean;
import slecon.setting.setup.io.OutputsC01.RelayOutputsBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Error;
import comm.Parser_McsConfig;
import comm.Parser_McsNvram;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




@SetupView(
    path      = "Setup::I/O::Outputs",
    sortIndex = 0x232
)
public class OutputsSettingC01 extends SettingPanel<OutputsC01> implements Page, LiftDataChangedListener {
    private static final long           serialVersionUID = -7606379969444434975L;

    private static final ResourceBundle TEXT           = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Logger.
     */
    private static final Logger         logger           = LogManager.getLogger( OutputsSettingC01.class );

    private final Object mutex = new Object();
    private volatile long      lastestTimeStamp = -1;
    
    private final LiftConnectionBean connBean;

    private Parser_Error error;

    private Parser_McsNvram nvram;
    
    private Parser_McsConfig mcsconfig;
    
    private Parser_Status status;

    private boolean IsVerify = false;


    public OutputsSettingC01 ( LiftConnectionBean connBean ) {
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
            status = new Parser_Status(connBean.getIp(), connBean.getPort());
            mcsconfig = new Parser_McsConfig(connBean.getIp(), connBean.getPort());
            
            MON_MGR.addEventListener(this, connBean.getIp(), connBean.getPort(),
            		AgentMessage.STATUS.getCode() 
                    | AgentMessage.MCS_CONFIG.getCode() 
                    | AgentMessage.MCS_NVRAM.getCode() 
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
    public void onOK(OutputsC01 panel) {
        synchronized ( mutex  ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }

    
    @Override
    public void onReset(OutputsC01 panel) {
    	synchronized ( mutex  ) {
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
        
        if (msg==AgentMessage.STATUS.getCode() || msg==AgentMessage.MCS_CONFIG.getCode())
            updateStatus();
        
        if( msg==AgentMessage.MCS_NVRAM.getCode() ) {
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
        return TEXT.getString( "Outputs.title" );
    }

	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("I/O"), slecon.setting.setup.io.InputsC01Setting.class);
		map.put(Dict.lookup("Outputs"), this.getClass());
		return map;
	}
    //////////////////////////////////////////////////////////////////////////////////

    private static final class Solid {
        private final RelayOutputsBean  bean_RelayOutputsBean;
        private final DigitalOutputBean bean_DigitalOutputBean;
        
        
        private Solid(RelayOutputsBean bean_RelayOutputsBean, DigitalOutputBean bean_DigitalOutputBean) {
            super();
            this.bean_RelayOutputsBean = bean_RelayOutputsBean;
            this.bean_DigitalOutputBean = bean_DigitalOutputBean;
        }
    }
    
    
    private volatile Solid solid = null;

    public void setHot() {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final OutputsC01.RelayOutputsBean bean_RelayOutputsBean = new OutputsC01.RelayOutputsBean();
            final OutputsC01.DigitalOutputBean bean_DigitalOutputBean = new OutputsC01.DigitalOutputBean();

            bean_RelayOutputsBean.setRl1(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_RL1.address).intValue()));
            bean_RelayOutputsBean.setRl2(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_RL2.address).intValue()));
            bean_RelayOutputsBean.setRl3(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_RL3.address).intValue()));
            bean_RelayOutputsBean.setRl4(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_RL4.address).intValue()));
            bean_RelayOutputsBean.setRl5(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_RL5.address).intValue()));
            bean_RelayOutputsBean.setRl6(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_RL6.address).intValue()));
            bean_RelayOutputsBean.setRl10(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_RL10.address).intValue()));
            
            bean_DigitalOutputBean.setP101(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_P10_1.address).intValue()));
            bean_DigitalOutputBean.setP102(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_P10_2.address).intValue()));
            bean_DigitalOutputBean.setP103(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_P10_3.address).intValue()));
            bean_DigitalOutputBean.setP104(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_P10_4.address).intValue()));
            bean_DigitalOutputBean.setP105(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_P10_5.address).intValue()));
            bean_DigitalOutputBean.setP106(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_P10_6.address).intValue()));
            bean_DigitalOutputBean.setP107(OutputSourceC01.getByID(nvram.getUnsignedByte(NVAddress.NVADDR_OUTBP_P10_7.address).intValue()));

            if (solid == null)
                solid = new Solid(bean_RelayOutputsBean, bean_DigitalOutputBean);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    app.setRelayOutputsBean(bean_RelayOutputsBean);
                    app.setDigitalOutputBean(bean_DigitalOutputBean);
                }
            });
        } catch (Exception e) {
            logger.catching(Level.FATAL, e);
        }
    }
    
    
    public boolean submit() {
    	if(IsVerify) {
	        try {
	            final RelayOutputsBean bean_RelayOutputsBean  = app.getRelayOutputsBean();
	            final DigitalOutputBean bean_DigitalOutputBean = app.getDigitalOutputBean();
	            
	            nvram.setByte(NVAddress.NVADDR_OUTBP_RL1.address, (byte) bean_RelayOutputsBean.getRl1().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_RL2.address, (byte) bean_RelayOutputsBean.getRl2().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_RL3.address, (byte) bean_RelayOutputsBean.getRl3().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_RL4.address, (byte) bean_RelayOutputsBean.getRl4().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_RL5.address, (byte) bean_RelayOutputsBean.getRl5().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_RL6.address, (byte) bean_RelayOutputsBean.getRl6().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_RL10.address, (byte) bean_RelayOutputsBean.getRl10().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_P10_1.address, (byte) bean_DigitalOutputBean.getP101().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_P10_2.address, (byte) bean_DigitalOutputBean.getP102().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_P10_3.address, (byte) bean_DigitalOutputBean.getP103().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_P10_4.address, (byte) bean_DigitalOutputBean.getP104().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_P10_5.address, (byte) bean_DigitalOutputBean.getP105().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_P10_6.address, (byte) bean_DigitalOutputBean.getP106().id );
	            nvram.setByte(NVAddress.NVADDR_OUTBP_P10_7.address, (byte) bean_DigitalOutputBean.getP107().id );
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
    	if(!IsVerify) {
    		try {
    			JPasswordField pwd = new JPasswordField();
    			Object[] message = {TEXT.getString("Password.text"), pwd};
    			int res = JOptionPane.showConfirmDialog(this, message, TEXT.getString("Password.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    			if(res == JOptionPane.OK_OPTION) {
    				if("282828".equals(new String(pwd.getPassword()))) {
    					IsVerify = true;
            			app.SetJComboBoxEnable(true);
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
                            app.setRelayOutputsBean( solid.bean_RelayOutputsBean );
                            app.setDigitalOutputBean( solid.bean_DigitalOutputBean );
                            app.start();
                        }
                    }
                });
    	}
    }
    
    public void updateStatus () {
        final HashMap<OutputPinC01, Boolean> result   = new HashMap<>();
        CrossBar                            crossbar = new CrossBar( mcsconfig.getCrossbar() );
        byte[]                              rawIO    = status.getMcsIO();
        for ( OutputPinC01 pin : OutputPinC01.values() ) {
            boolean val = crossbar.isOutputPresent( rawIO, pin );
            result.put( pin, val );
        }
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                app.setOutputSourceState( result );
            }
        } );
    }
}
