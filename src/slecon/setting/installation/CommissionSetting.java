package slecon.setting.installation;

import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.PageTreeExpression;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Agent;
import comm.Parser_Error;
import comm.Parser_McsNvram;
import comm.Parser_Misc;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;

@SetupView(path = "Installation::Commission", sortIndex = 0x160)
public class CommissionSetting extends SettingPanel<Commission> implements Page, LiftDataChangedListener {
    
    private static ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger(CommissionSetting.class);

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    volatile Solid     solid  = null;

    private final LiftConnectionBean connBean;
    
    private Parser_Error       error;
    
    private Parser_Status      status;
    
    private Parser_Misc        misc;
    
    private Parser_McsNvram    nvram;

    
    

    public CommissionSetting (LiftConnectionBean connBean) {
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
            error     = new Parser_Error( connBean.getIp(), connBean.getPort() );
            status    = new Parser_Status( connBean.getIp(), connBean.getPort() );
            misc      = new Parser_Misc( connBean.getIp(), connBean.getPort() );
            nvram 	  = new Parser_McsNvram( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                    AgentMessage.MISC.getCode() | AgentMessage.STATUS.getCode() | AgentMessage.ERROR.getCode() | AgentMessage.MCS_NVRAM.getCode() );
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
                AgentMessage.MISC.getCode() | AgentMessage.STATUS.getCode() | AgentMessage.ERROR.getCode() );
        
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
    public void onOK ( Commission panel ) {
    }


    @Override
    public void onReset ( Commission panel ) {
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
            if (solid != null /* && timestamp > lastestTimeStamp+1500*1000000 */) {
//                int result = JOptionPane.showConfirmDialog(StartUI.getFrame(), "The config of this lift has changed. Reload it?", "Update",
//                        JOptionPane.YES_NO_OPTION);
//                if (result == JOptionPane.OK_OPTION) {
//                    solid = null;
//                    setHot();
//                }
//            } else {
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
    protected String getPanelTitle() {
        return TEXT.getString( "Commission.title" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Installation"), slecon.setting.installation.OverviewSetting.class);
		map.put(Dict.lookup("Commission"), slecon.setting.installation.CommissionSetting.class);
		return map;
	}
	
    //////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final Commission.HardwareBean bean_hardware;
        private final Commission.DynamicStatus bean_status;
        
        private Solid(Commission.HardwareBean bean_hardware, Commission.DynamicStatus bean_status) {
            super();
            this.bean_hardware = bean_hardware;
            this.bean_status = bean_status;
        }
    }


    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final Commission.HardwareBean bean_hardware = new Commission.HardwareBean();
            bean_hardware.setInstallationMode(status.isInstallationMode());
            bean_hardware.setTemporaryDriverActivation(status.isTemporaryDriverActivation());
            bean_hardware.setSuspendDcsAutomation(status.isSuspendDCSAutomation());
            bean_hardware.setMcsDebug( nvram.getUnsignedByte( ( short )0x1975 ) == 1 );
            
            final Commission.DynamicStatus bean_status = new Commission.DynamicStatus();
            bean_status.setMcex(status.getMcexMode()? 1 : 0);
            
            if (solid == null)
                solid = new Solid(bean_hardware, bean_status);
            
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.setHardwareBean(bean_hardware);
                    app.setMcexMode(bean_status);
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }
    
    public void hardwareSelfTest() {
        if(!ToolBox.requestRole(connBean, WRITE_MCS_EXPRESSION)) {
            ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
            return;
        }
        misc.mcs((short)0x8016, new byte[0]); // [CMD] CMD_HARDWARE_SELF_TEST
    }
    
    public void verifyNVRAM() {
        if(!ToolBox.requestRole(connBean, WRITE_MCS_EXPRESSION)) {
            ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
            return;
        }
        misc.mcs((short)0x8017, new byte[0]); // [CMD] CMD_VERIFY_NVRAM
    }
    
    public void setCalibrateAnalogOutput(byte value) {
        if(!ToolBox.requestRole(connBean, WRITE_MCS_EXPRESSION)) {
            ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
            return;
        }
        misc.mcs((short) 0x3003, new byte[] { value }); // [CMD] CMD_SET_SPEED_REF
    }
    
    
    private final static PageTreeExpression WRITE_MCS_EXPRESSION = new PageTreeExpression("write_mcs");
    public void setInstallationMode(boolean onoff) {
        if(!ToolBox.requestRole(connBean, WRITE_MCS_EXPRESSION)) {
            ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
            return;
        }
        misc.mcs((short) 0x2812, new byte[]{ (byte) ( onoff ? 1 : 0 ) });  // [CMD] CMD_SET_INSTALLATION_MODE
    }
    
    
    public void setTemporaryDriverActivation(boolean onoff) {
        if(!ToolBox.requestRole(connBean, WRITE_MCS_EXPRESSION)) {
            ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
            return;
        }
        misc.mcs((short) 0x2826, new byte[]{ (byte) (onoff ? 1 : 0) });  // [CMD] CMD_TEMPORARY_DRIVER_ACTIVATION
    }
    
    
    public void setSuspendDcsAutomation(boolean onoff) {
        if(!ToolBox.requestRole(connBean, WRITE_MCS_EXPRESSION)) {
            ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
            return;
        }
        misc.mcs((short) 0x2406, new byte[]{ (byte) ( onoff ? 1 : 0 ) });  // [CMD] CMD_SUSPEND_DCS_AUTOMATION
    }
    
    public void reset () {
        setHot();
    }
    
    public void sendDoorCommand(DoorCommand cmd) {
        if(!ToolBox.requestRole(connBean, WRITE_MCS_EXPRESSION)) {
            ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
            return;
        }
        misc.mcs(cmd.cmdID, new byte[]{});
    }
    
    static enum DoorCommand {
        OPEN_FRONT_DOOR(0x1004), CLOSE_FRONT_DOOR(0x1005), OPEN_REAR_DOOR(0x1006), CLOSE_REAR_DOOR(0x1007);
        
        private final short cmdID;

        DoorCommand(int cmdID) {
            this.cmdID = (short) cmdID;
        }
    }
    
    public void reset_mcs() {
    	misc.mcs((short)0x01800, new byte[]{ (byte)0 });
    }
    
    public void mcs_independent_mode( byte flag ) {
    	misc.mcs((short)0x02ffa, new byte[]{ flag });
    }
    
    public void erem_stop() {
    	misc.mcs((short)0x100a, new byte[]{});
    }
    
    public void pos_corr() {
    	misc.mcs((short)0x100d, new byte[]{});
    }
    
    public void ucmp_test( byte flag ) {
    	misc.mcs((short)0x2ffb, new byte[]{ flag });
    }
    
    public void term_test( byte flag ) {
    	misc.mcs((short)0x2ffc, new byte[]{ flag });
    }
    
    public void bumper_test( byte flag ) {
    	misc.mcs((short)0x2ffd, new byte[]{ flag });
    }
    
    public void mcs_debug( byte enable) {
    	nvram.setByte( ( short )0x1975, enable );
    	nvram.commit();
    }
    
    
}