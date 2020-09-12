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
import logic.io.crossbar.InputPinD01;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;

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
    path      = "Setup::I/O::Inputs",
    sortIndex = 0x231,
    condition = "lift.version.mcsBoardVersion=='D01'"
)
public class InputsD01Setting extends SettingPanel<InputsD01> implements Page, LiftDataChangedListener {
    private static final long           serialVersionUID = 1061493484539801248L;

    private static final ResourceBundle TEXT           = ToolBox.getResourceBundle( "setting.SettingPanel" );
    
    /**
     * Logger.
     */
    private final Logger logger = LogManager.getLogger(InputsD01Setting.class);

    private final Object mutex = new Object();
    private volatile long      lastestTimeStamp = -1;
    
    private final LiftConnectionBean connBean;

    private Parser_Error error;

    private Parser_McsConfig mcsconfig;
    
    private Parser_McsNvram nvram;

    private Parser_Status status;

    private boolean IsVerify = false;
    

    public InputsD01Setting ( LiftConnectionBean connBean ) {
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
                AgentMessage.MCS_CONFIG.getCode() 
                | AgentMessage.MCS_NVRAM.getCode() 
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
    public void onOK(InputsD01 panel) {
		synchronized ( mutex  ) {
		     setEnabled(false);
		     if(submit())
		    	 solid = null;
		     setEnabled(true);
		}
    }

    
    @Override
    public void onReset(InputsD01 panel) {
    	
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

        if(msg==AgentMessage.MCS_NVRAM.getCode()) {
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
        return TEXT.getString( "Inputs.title" );
    }

	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("I/O"), slecon.setting.setup.io.InputsD01Setting.class);
		map.put(Dict.lookup("Inputs"), this.getClass());
		return map;
	}
    //////////////////////////////////////////////////////////////////////////////////

    private static final class Solid {
        private InputsD01.InputPinBean bean;

        private Solid(InputsD01.InputPinBean bean) {
            super();
            this.bean = bean;
        }
    }
    
    
    private volatile Solid solid = null;

    public void setHot() {
    	lastestTimeStamp = System.nanoTime();
    	
        try {
            final InputsD01.InputPinBean bean = new InputsD01.InputPinBean();
            
            bean.setFirmwareVersion(mcsconfig.getFirmwareVersion());
            {
                // NVADDR_IN_USL_BITPOS 0x1884 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_USL_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUsl(InputPinD01.getByRawIOPosition(code));
                bean.setUslInverted(isInverted);
            }
            {
                // NVADDR_IN_LSL_BITPOS 0x1883 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_LSL_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setLsl(InputPinD01.getByRawIOPosition(code));
                bean.setLslInverted(isInverted);
            }
            {
                // NVADDR_IN_UDZ_BITPOS 0x1881 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_UDZ_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUdz(InputPinD01.getByRawIOPosition(code));
                bean.setUdzInverted(isInverted);
            }
            {
                // NVADDR_IN_LDZ_BITPOS 0x1880 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_LDZ_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setLdz(InputPinD01.getByRawIOPosition(code));
                bean.setLdzInverted(isInverted);
            }
            {
                // NVADDR_IN_INS_BITPOS 0x1885 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_INS_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setIns(InputPinD01.getByRawIOPosition(code));
                bean.setInsInverted(isInverted);
            }
            {
                // NVADDR_IN_INSUP_BITPOS 0x1886 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_INSUP_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setInsUp(InputPinD01.getByRawIOPosition(code));
                bean.setInsUpInverted(isInverted);
            }
            {
                // NVADDR_IN_INSDOWN_BITPOS 0x1887 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_INSDOWN_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setInsDown(InputPinD01.getByRawIOPosition(code));
                bean.setInsDownInverted(isInverted);
            }
            {
                // NVADDR_IN_DRVOK_BITPOS 0x1892 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_DRVOK_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setDrvOK(InputPinD01.getByRawIOPosition(code));
                bean.setDrvOKInverted(isInverted);
            }
            {
                // NVADDR_IN_DRVBM_BITPOS 0x1891 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_DRVBM_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setDrvBM(InputPinD01.getByRawIOPosition(code));
                bean.setDrvBMInverted(isInverted);
            }
            {
                // NVADDR_IN_DBDE_BITPOS 0x1889 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_DBDE_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setEnable(InputPinD01.getByRawIOPosition(code));
                bean.setEnableInverted(isInverted);
            }
            {
                // NVADDR_IN_DBDF_BITPOS 0x188a (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_DBDF_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setDbdf(InputPinD01.getByRawIOPosition(code));
                bean.setDbdfInverted(isInverted);
            }
            {
                // NVADDR_IN_BS1_BITPOS 0x189b (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_BS1_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setBs1(InputPinD01.getByRawIOPosition(code));
                bean.setBs1Inverted(isInverted);
            }
            {
                // NVADDR_IN_BS2_BITPOS 0x189c (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_BS2_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setBs2(InputPinD01.getByRawIOPosition(code));
                bean.setBs2Inverted(isInverted);
            }
            {
                // NVADDR_IN_ST1_BITPOS 0x1890 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_ST1_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setSt1(InputPinD01.getByRawIOPosition(code));
                bean.setSt1Inverted(isInverted);
            }
            {
                // NVADDR_IN_ST2_BITPOS 0x188f (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_ST2_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setSt2(InputPinD01.getByRawIOPosition(code));
                bean.setSt2Inverted(isInverted);
            }
            {
            	// NVADDR_IN_ST3_BITPOS 0x188e (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_ST3_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setSt3(InputPinD01.getByRawIOPosition(code));
                bean.setSt3Inverted(isInverted);
            }
            {
            	// NVADDR_IN_ST4_BITPOS 0x188d (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_ST4_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setSt4(InputPinD01.getByRawIOPosition(code));
                bean.setSt4Inverted(isInverted);
            }
            {
                // NVADDR_IN_ST5_BITPOS 0x1893 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_ST5_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setSt5(InputPinD01.getByRawIOPosition(code));
                bean.setSt5Inverted(isInverted);
            }
            {
                // NVADDR_IN_ST6_BITPOS 0x1894 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_ST6_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setSt6(InputPinD01.getByRawIOPosition(code));
                bean.setSt6Inverted(isInverted);
            }
            {
                // NVADDR_IN_LVCFB_BITPOS 0x1882 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_LVCFB_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setLvcfb(InputPinD01.getByRawIOPosition(code));
                bean.setLvcfbInverted(isInverted);
            }
            {
                // NVADDR_IN_LVC1FB_BITPOS 0x1888 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_LVC1FB_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setLvc1fb(InputPinD01.getByRawIOPosition(code));
                bean.setLvc1fbInverted(isInverted);
            }
            {
                // NVADDR_IN_THM_BITPOS 0x188c (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_THM_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setThm(InputPinD01.getByRawIOPosition(code));
                bean.setThmInverted(isInverted);
            }
            {
                // NVADDR_IN_ENCF_BITPOS 0x1895 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_ENCF_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setEncf(InputPinD01.getByRawIOPosition(code));
                bean.setEncfInverted(isInverted);
            }
            {
                // NVADDR_IN_URL_BITPOS 0x1895 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_URL_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUrl(InputPinD01.getByRawIOPosition(code));
                bean.setUrlInverted(isInverted);
            }
            {
                // NVADDR_IN_LRL_BITPOS 0x1895 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_LRL_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setLrl(InputPinD01.getByRawIOPosition(code));
                bean.setLrlInverted(isInverted);
            }
            
            {
                // NVADDR_IN_EPB_BITPOS 0x189a (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_EPB_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setEpb(InputPinD01.getByRawIOPosition(code));
                bean.setEpbInverted(isInverted);
            }
            
            {
                // NVADDR_IN_UCMTS_BITPOS 0x189e (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_UCMTS_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUcmts(InputPinD01.getByRawIOPosition(code));
                bean.setUcmtsInverted(isInverted);
            }
            
            {
                // NVADDR_IN_UCMTS2_BITPOS 0x189f (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_UCMTS2_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUcmts2(InputPinD01.getByRawIOPosition(code));
                bean.setUcmts2Inverted(isInverted);
            }
            
            {
                // NVADDR_IN_UCMTS3_BITPOS 0x18a0 (uc)
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_UCMTS3_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUcmts3(InputPinD01.getByRawIOPosition(code));
                bean.setUcmts3Inverted(isInverted);
            }
            {
                // NVADDR_IN_DLB_BITPOS
                int raw = nvram.getUnsignedByte(NVAddressD01.NVADDR_IN_DLB_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setDlb(InputPinD01.getByRawIOPosition(code));
                bean.setDlbInverted(isInverted);
            }
            
            if(solid==null)
                solid = new Solid(bean);
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    app.setInputPinBean(bean);
                }
            });
        } catch (Exception e) {
            logger.catching(Level.FATAL, e);
        }
    }
    
    
    public boolean submit() {
    	if(IsVerify) {
    		try {
	            InputsD01.InputPinBean bean = app.getInputPinBean();
	            nvram.setByte( NVAddressD01.NVADDR_IN_USL_BITPOS.address, (byte) (bean.isUslInverted() ? ((int) bean.getUsl().rawIOPos)+128 : bean.getUsl().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_LSL_BITPOS.address, (byte) (bean.isLslInverted() ? ((int) bean.getLsl().rawIOPos)+128 : bean.getLsl().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_UDZ_BITPOS.address, (byte) (bean.isUdzInverted() ? ((int) bean.getUdz().rawIOPos)+128 : bean.getUdz().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_LDZ_BITPOS.address, (byte) (bean.isLdzInverted() ? ((int) bean.getLdz().rawIOPos)+128 : bean.getLdz().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_INS_BITPOS.address, (byte) (bean.isInsInverted() ? ((int) bean.getIns().rawIOPos)+128 : bean.getIns().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_INSUP_BITPOS.address, (byte) (bean.isInsUpInverted() ? ((int) bean.getInsUp().rawIOPos)+128 : bean.getInsUp().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_INSDOWN_BITPOS.address, (byte) (bean.isInsDownInverted() ? ((int) bean.getInsDown().rawIOPos)+128 : bean.getInsDown().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_DRVOK_BITPOS.address, (byte) (bean.isDrvOKInverted() ? ((int) bean.getDrvOK().rawIOPos)+128 : bean.getDrvOK().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_DRVBM_BITPOS.address, (byte) (bean.isDrvBMInverted() ? ((int) bean.getDrvBM().rawIOPos)+128 : bean.getDrvBM().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_DBDE_BITPOS.address, (byte) (bean.isEnableInverted() ? ((int) bean.getEnable().rawIOPos)+128 : bean.getEnable().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_DBDF_BITPOS.address, (byte) (bean.isDbdfInverted() ? ((int) bean.getDbdf().rawIOPos)+128 : bean.getDbdf().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_BS1_BITPOS.address, (byte) (bean.isBs1Inverted() ? ((int) bean.getBs1().rawIOPos)+128 : bean.getBs1().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_BS2_BITPOS.address, (byte) (bean.isBs2Inverted() ? ((int) bean.getBs2().rawIOPos)+128 : bean.getBs2().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_ST1_BITPOS.address, (byte) (bean.isSt1Inverted() ? ((int) bean.getSt1().rawIOPos)+128 : bean.getSt1().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_ST2_BITPOS.address, (byte) (bean.isSt2Inverted() ? ((int) bean.getSt2().rawIOPos)+128 : bean.getSt2().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_ST3_BITPOS.address, (byte) (bean.isSt3Inverted() ? ((int) bean.getSt3().rawIOPos)+128 : bean.getSt3().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_ST4_BITPOS.address, (byte) (bean.isSt4Inverted() ? ((int) bean.getSt4().rawIOPos)+128 : bean.getSt4().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_ST5_BITPOS.address, (byte) (bean.isSt5Inverted() ? ((int) bean.getSt5().rawIOPos)+128 : bean.getSt5().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_ST6_BITPOS.address, (byte) (bean.isSt6Inverted() ? ((int) bean.getSt6().rawIOPos)+128 : bean.getSt6().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_LVCFB_BITPOS.address, (byte) (bean.isLvcfbInverted() ? ((int) bean.getLvcfb().rawIOPos)+128 : bean.getLvcfb().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_LVC1FB_BITPOS.address, (byte) (bean.isLvc1fbInverted() ? ((int) bean.getLvc1fb().rawIOPos)+128 : bean.getLvc1fb().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_THM_BITPOS.address, (byte) (bean.isThmInverted() ? ((int) bean.getThm().rawIOPos)+128 : bean.getThm().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_ENCF_BITPOS.address, (byte) (bean.isEncfInverted() ? ((int) bean.getEncf().rawIOPos)+128 : bean.getEncf().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_URL_BITPOS.address, (byte) (bean.isUrlInverted() ? ((int) bean.getUrl().rawIOPos)+128 : bean.getUrl().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_LRL_BITPOS.address, (byte) (bean.isLrlInverted() ? ((int) bean.getLrl().rawIOPos)+128 : bean.getLrl().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_EPB_BITPOS.address, (byte) (bean.isEpbInverted() ? ((int) bean.getEpb().rawIOPos)+128 : bean.getEpb().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_UCMTS_BITPOS.address, (byte) (bean.isUcmtsInverted() ? ((int) bean.getUcmts().rawIOPos)+128 : bean.getUcmts().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_UCMTS2_BITPOS.address, (byte) (bean.isUcmts2Inverted() ? ((int) bean.getUcmts2().rawIOPos)+128 : bean.getUcmts2().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_UCMTS3_BITPOS.address, (byte) (bean.isUcmts3Inverted() ? ((int) bean.getUcmts3().rawIOPos)+128 : bean.getUcmts3().rawIOPos));
	            nvram.setByte( NVAddressD01.NVADDR_IN_DLB_BITPOS.address, (byte) (bean.isDlbInverted() ? ((int) bean.getDlb().rawIOPos)+128 : bean.getDlb().rawIOPos));
	            
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
    		// Update returned data to visualization components.
            if (solid != null)
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (solid != null) {
                            app.stop();
                            app.setInputPinBean( solid.bean );
                            app.start();
                        }
                    }
                });
    	}
    }
    
    
    public void updateStatus () {
        final HashMap<InputPinD01, Boolean> result   = new HashMap<>();
        CrossBar                            crossbar = new CrossBar( mcsconfig.getCrossbar() );
        byte[]                              rawIO    = status.getMcsIO();
        for ( InputPinD01 pin : InputPinD01.values() ) {
            boolean val = crossbar.isInputPresent( rawIO, pin );
            result.put( pin, val );
        }
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                app.setInputPinState( result );
            }
        } );
    }

}
