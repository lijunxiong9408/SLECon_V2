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
import logic.io.crossbar.InputPinA03;
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
    condition = "lift.version.mcsBoardVersion=='A03'"
)
public class InputsA03Setting extends SettingPanel<InputsA03> implements Page, LiftDataChangedListener {
    private static final long           serialVersionUID = 1061493484539801248L;

    private static final ResourceBundle TEXT           = ToolBox.getResourceBundle( "setting.SettingPanel" );
    
    /**
     * Logger.
     */
    private final Logger logger = LogManager.getLogger(InputsA03Setting.class);

    private final Object mutex = new Object();
    private volatile long      lastestTimeStamp = -1;
    
    private final LiftConnectionBean connBean;

    private Parser_Error error;

    private Parser_McsConfig mcsconfig;
    
    private Parser_McsNvram nvram;

    private Parser_Status status;

    private boolean IsVerify = false;
    

    public InputsA03Setting ( LiftConnectionBean connBean ) {
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
    public void onOK(InputsA03 panel) {
        synchronized ( mutex  ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }

    
    @Override
    public void onReset(InputsA03 panel) {
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
        return TEXT.getString( "Inputs.title" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("I/O"), slecon.setting.setup.io.InputsA03Setting.class);
		map.put(Dict.lookup("Inputs"), this.getClass());
		return map;
	}
	
    //////////////////////////////////////////////////////////////////////////////////

    private static final class Solid {
        private InputsA03.InputPinBean bean;

        private Solid(InputsA03.InputPinBean bean) {
            super();
            this.bean = bean;
        }
    }
    
    
    private volatile Solid solid = null;

    public void setHot() {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final InputsA03.InputPinBean bean = new InputsA03.InputPinBean();

            bean.setFirmwareVersion(mcsconfig.getFirmwareVersion());
            {
                // NVADDR_IN_USL_BITPOS 0x1884 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_USL_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUsl(InputPinA03.getByRawIOPosition(code));
                bean.setUslInverted(isInverted);
            }
            {
                // NVADDR_IN_LSL_BITPOS 0x1883
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_LSL_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setLsl(InputPinA03.getByRawIOPosition(code));
                bean.setLslInverted(isInverted);
            }
            {
                // NVADDR_IN_UDZ_BITPOS 0x1881
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_UDZ_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUdz(InputPinA03.getByRawIOPosition(code));
                bean.setUdzInverted(isInverted);
            }
            {
                // NVADDR_IN_LDZ_BITPOS 0x1880 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_LDZ_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setLdz(InputPinA03.getByRawIOPosition(code));
                bean.setLdzInverted(isInverted);
            }
            {
                // NVADDR_IN_INS_BITPOS 0x1885 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_INS_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setIns(InputPinA03.getByRawIOPosition(code));
                bean.setInsInverted(isInverted);
            }
            {
                // NVADDR_IN_INSUP_BITPOS 0x1886 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_INSUP_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setInsUp(InputPinA03.getByRawIOPosition(code));
                bean.setInsUpInverted(isInverted);
            }
            {
                // NVADDR_IN_INSDOWN_BITPOS 0x1887 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_INSDOWN_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setInsDown(InputPinA03.getByRawIOPosition(code));
                bean.setInsDownInverted(isInverted);
            }
            {
                // NVADDR_IN_DRVOK_BITPOS 0x1892 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_DRVOK_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setDrvOK(InputPinA03.getByRawIOPosition(code));
                bean.setDrvOKInverted(isInverted);
            }
            {
                // NVADDR_IN_DRVBM_BITPOS 0x1891 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_DRVBM_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setDrvBM(InputPinA03.getByRawIOPosition(code));
                bean.setDrvBMInverted(isInverted);
            }
            {
                // NVADDR_IN_DRVENF_BITPOS 0x1896 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_DRVENF_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setDrvENF(InputPinA03.getByRawIOPosition(code));
                bean.setDrvENFInverted(isInverted);
            }
            {
                // NVADDR_IN_ENABLE_BITPOS 0x1888 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_ENABLE_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setEnable(InputPinA03.getByRawIOPosition(code));
                bean.setEnableInverted(isInverted);
            }
            {
                // NVADDR_IN_DBDE_BITPOS 0x1889 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_DBDE_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setDbde(InputPinA03.getByRawIOPosition(code));
                bean.setDbdeInverted(isInverted);
            }
            {
                // NVADDR_IN_DBDF_BITPOS 0x188a (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_DBDF_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setDbdf(InputPinA03.getByRawIOPosition(code));
                bean.setDbdfInverted(isInverted);
            }
            {
                // NVADDR_IN_BS_BITPOS 0x188b (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_BS1_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setBs1(InputPinA03.getByRawIOPosition(code));
                bean.setBs1Inverted(isInverted);
            }
            {
                // NVADDR_IN_BS2_BITPOS 0x188c (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_BS2_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setBs2(InputPinA03.getByRawIOPosition(code));
                bean.setBs2Inverted(isInverted);
            }
            {
                // NVADDR_IN_DFC_BITPOS 0x188d (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_DFC_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setDfc(InputPinA03.getByRawIOPosition(code));
                bean.setDfcInverted(isInverted);
            }
            {
                // NVADDR_IN_DW_BITPOS 0x188e (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_DW_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setDw(InputPinA03.getByRawIOPosition(code));
                bean.setDwInverted(isInverted);
            }
            {
                // NVADDR_IN_HES_BITPOS 0x188f (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_HES_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setHes(InputPinA03.getByRawIOPosition(code));
                bean.setHesInverted(isInverted);
            }
            {
                // NVADDR_IN_CES_BITPOS 0x1890 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_CES_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setCes(InputPinA03.getByRawIOPosition(code));
                bean.setCesInverted(isInverted);
            }
            {
                // NVADDR_IN_LVCFB_BITPOS 0x1882 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_LVCFB_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setLvcfb(InputPinA03.getByRawIOPosition(code));
                bean.setLvcfbInverted(isInverted);
            }
            {
                // NVADDR_IN_THM_BITPOS 0x188c (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_THM_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setThm(InputPinA03.getByRawIOPosition(code));
                bean.setThmInverted(isInverted);
            }
            {
                // NVADDR_IN_ENCF_BITPOS 0x1895 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_ENCF_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setEncf(InputPinA03.getByRawIOPosition(code));
                bean.setEncfInverted(isInverted);
            }
            {
                // NVADDR_IN_URL_BITPOS 0x1895 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_URL_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUrl(InputPinA03.getByRawIOPosition(code));
                bean.setUrlInverted(isInverted);
            }
            {
                // NVADDR_IN_LRL_BITPOS 0x1895 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_LRL_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setLrl(InputPinA03.getByRawIOPosition(code));
                bean.setLrlInverted(isInverted);
            }
            {
                // NVADDR_IN_EPB_BITPOS 0x189a (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_EPB_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setEpb(InputPinA03.getByRawIOPosition(code));
                bean.setEpbInverted(isInverted);
            }
            
            {
                // NVADDR_IN_UCMTS_BITPOS 0x189e (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_UCMTS_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUcmts(InputPinA03.getByRawIOPosition(code));
                bean.setUcmtsInverted(isInverted);
            }
            
            {
                // NVADDR_IN_UCMTS2_BITPOS 0x189f (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_UCMTS2_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUcmts2(InputPinA03.getByRawIOPosition(code));
                bean.setUcmts2Inverted(isInverted);
            }
            
            {
                // NVADDR_IN_UCMTS3_BITPOS 0x18a0 (uc)
                int raw = nvram.getUnsignedByte(NVAddress.NVADDR_IN_UCMTS3_BITPOS.address).intValue();
                boolean isInverted = raw > 0x7f;
                int code = raw & 0x7f;
                bean.setUcmts3(InputPinA03.getByRawIOPosition(code));
                bean.setUcmts3Inverted(isInverted);
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
	            InputsA03.InputPinBean bean = app.getInputPinBean();

	            nvram.setByte( NVAddress.NVADDR_IN_USL_BITPOS.address, (byte) (bean.isUslInverted() ? ((int) bean.getUsl().rawIOPos)+128 : bean.getUsl().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_LSL_BITPOS.address, (byte) (bean.isLslInverted() ? ((int) bean.getLsl().rawIOPos)+128 : bean.getLsl().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_UDZ_BITPOS.address, (byte) (bean.isUdzInverted() ? ((int) bean.getUdz().rawIOPos)+128 : bean.getUdz().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_LDZ_BITPOS.address, (byte) (bean.isLdzInverted() ? ((int) bean.getLdz().rawIOPos)+128 : bean.getLdz().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_INS_BITPOS.address, (byte) (bean.isInsInverted() ? ((int) bean.getIns().rawIOPos)+128 : bean.getIns().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_INSUP_BITPOS.address, (byte) (bean.isInsUpInverted() ? ((int) bean.getInsUp().rawIOPos)+128 : bean.getInsUp().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_INSDOWN_BITPOS.address, (byte) (bean.isInsDownInverted() ? ((int) bean.getInsDown().rawIOPos)+128 : bean.getInsDown().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_DRVOK_BITPOS.address, (byte) (bean.isDrvOKInverted() ? ((int) bean.getDrvOK().rawIOPos)+128 : bean.getDrvOK().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_DRVBM_BITPOS.address, (byte) (bean.isDrvBMInverted() ? ((int) bean.getDrvBM().rawIOPos)+128 : bean.getDrvBM().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_DRVENF_BITPOS.address, (byte) (bean.isDrvENFInverted() ? ((int) bean.getDrvENF().rawIOPos)+128 : bean.getDrvENF().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_ENABLE_BITPOS.address, (byte) (bean.isEnableInverted() ? ((int) bean.getEnable().rawIOPos)+128 : bean.getEnable().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_DBDE_BITPOS.address, (byte) (bean.isDbdeInverted() ? ((int) bean.getDbde().rawIOPos)+128 : bean.getDbde().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_DBDF_BITPOS.address, (byte) (bean.isDbdfInverted() ? ((int) bean.getDbdf().rawIOPos)+128 : bean.getDbdf().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_BS1_BITPOS.address, (byte) (bean.isBs1Inverted() ? ((int) bean.getBs1().rawIOPos)+128 : bean.getBs1().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_BS2_BITPOS.address, (byte) (bean.isBs2Inverted() ? ((int) bean.getBs2().rawIOPos)+128 : bean.getBs2().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_DFC_BITPOS.address, (byte) (bean.isDfcInverted() ? ((int) bean.getDfc().rawIOPos)+128 : bean.getDfc().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_DW_BITPOS.address, (byte) (bean.isDwInverted() ? ((int) bean.getDw().rawIOPos)+128 : bean.getDw().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_HES_BITPOS.address, (byte) (bean.isHesInverted() ? ((int) bean.getHes().rawIOPos)+128 : bean.getHes().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_CES_BITPOS.address, (byte) (bean.isCesInverted() ? ((int) bean.getCes().rawIOPos)+128 : bean.getCes().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_LVCFB_BITPOS.address, (byte) (bean.isLvcfbInverted() ? ((int) bean.getLvcfb().rawIOPos)+128 : bean.getLvcfb().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_THM_BITPOS.address, (byte) (bean.isThmInverted() ? ((int) bean.getThm().rawIOPos)+128 : bean.getThm().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_ENCF_BITPOS.address, (byte) (bean.isEncfInverted() ? ((int) bean.getEncf().rawIOPos)+128 : bean.getEncf().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_URL_BITPOS.address, (byte) (bean.isUrlInverted() ? ((int) bean.getUrl().rawIOPos)+128 : bean.getUrl().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_LRL_BITPOS.address, (byte) (bean.isLrlInverted() ? ((int) bean.getLrl().rawIOPos)+128 : bean.getLrl().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_EPB_BITPOS.address, (byte) (bean.isEpbInverted() ? ((int) bean.getEpb().rawIOPos)+128 : bean.getEpb().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_UCMTS_BITPOS.address, (byte) (bean.isUcmtsInverted() ? ((int) bean.getUcmts().rawIOPos)+128 : bean.getUcmts().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_UCMTS2_BITPOS.address, (byte) (bean.isUcmts2Inverted() ? ((int) bean.getUcmts2().rawIOPos)+128 : bean.getUcmts2().rawIOPos));
	            nvram.setByte( NVAddress.NVADDR_IN_UCMTS3_BITPOS.address, (byte) (bean.isUcmts3Inverted() ? ((int) bean.getUcmts3().rawIOPos)+128 : bean.getUcmts3().rawIOPos));
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
        final HashMap<InputPinA03, Boolean> result   = new HashMap<>();
        CrossBar                            crossbar = new CrossBar( mcsconfig.getCrossbar() );
        byte[]                              rawIO    = status.getMcsIO();
        for ( InputPinA03 pin : InputPinA03.values() ) {
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
