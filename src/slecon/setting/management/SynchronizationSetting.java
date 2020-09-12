package slecon.setting.management;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.SiteManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.ConvertException;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;

import comm.Parser_Deploy;
import comm.Parser_Event;
import comm.Parser_Module;
import comm.constants.AuthLevel.Role;




@SetupView(
    path      = "Management::Synchronization",
    sortIndex = 0x412
)
public class SynchronizationSetting extends SettingPanel<Synchronization> implements Page {
    private static final long   serialVersionUID = 1373042810433207687L;

    /**
     * Text resource.
     */
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Logger.
     */
    private final Logger   logger = LogManager.getLogger( SynchronizationSetting.class );
    
    private LiftConnectionBean connBean;




    public SynchronizationSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
        setResetButtonEnabled( false );
    }


    @Override
    public void onStart () throws Exception {
        setEnabled( true );
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MANAGER));
        
        SiteManagement.addVersionChangeListener(app, null);
    }


    @Override
    public void onResume () throws Exception {
        setEnabled( true );
        
        SiteManagement.addVersionChangeListener(app, null);
    }


    @Override
    public void onPause () throws Exception {
        setEnabled( false );
        
        SiteManagement.removeVersionChangeListener(app);
    }


    @Override
    public void onStop () throws Exception {
        SiteManagement.removeVersionChangeListener(app);
    }


    @Override
    public void onOK ( Synchronization panel ) {
        submit();
    }

    @Override
    public void onReset ( Synchronization panel ) {}


    @Override
    public void onDestroy () throws Exception {
    }


    @Override
    protected String getPanelTitle () {
        return TEXT.getString( "Synchronization.title" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Management"), slecon.setting.management.UtilitiesSetting.class);
		map.put(Dict.lookup("Synchronization"), null);
		return map;
	}

    //////////////////////////////////////////////////////////////////////////////////

    private void submit() {
        try {
            Synchronization.ConfigurationSynchronizationBean bean = app.getConfigurationSynchronizationBean();
            if (bean.getEventSettings() || bean.getFloorSettings() || bean.getModuleSettings()) {

                int ans = JOptionPane.showConfirmDialog(StartUI.getFrame(), Synchronization.TEXT.getString("Confirm.text"), Synchronization.TEXT.getString("Confirm.title"), JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    if (bean.getEventSettings()) {
                        Parser_Event event1 = new Parser_Event(connBean.getIp(), connBean.getPort());
                        Parser_Event event2 = new Parser_Event(bean.getTargetElevator().getIp(), bean.getTargetElevator().getPort());

                        event2.setEvent(event1.getEvent());
                        event2.setInstalledDevices(event1.getEvent());
                        event2.commit();
                    }

                    if (bean.getFloorSettings()) {
                        Parser_Deploy deploy1 = new Parser_Deploy(connBean.getIp(), connBean.getPort());
                        Parser_Deploy deploy2 = new Parser_Deploy(bean.getTargetElevator().getIp(), bean.getTargetElevator().getPort());

                        deploy2.setFloorCount(deploy1.getFloorCount());
                        for (int floor = 0; floor < deploy2.getFloorCount(); floor++) {
                            deploy2.setFloorText((byte) floor, deploy1.getFloorText((byte) floor));
                            deploy2.setDoorZone((byte) floor, (byte) deploy1.getDoorZone((byte) floor));
                            for (byte ip = 0; ip < 16; ip++) {
                                deploy2.setGroupClrBitmask((byte) floor, ip, ( short )deploy1.getGroupClrBitmask((byte) floor, ip));
                                deploy2.setGroupRegBitmask((byte) floor, ip, ( short )deploy1.getGroupRegBitmask((byte) floor, ip));
                            }
                        }
                        deploy2.commit();
                    }

                    if (bean.getModuleSettings()) {
                        Parser_Module module1 = new Parser_Module(connBean.getIp(), connBean.getPort());
                        Parser_Module module2 = new Parser_Module(bean.getTargetElevator().getIp(), bean.getTargetElevator().getPort());
                        module2.setByteArray(module1.getByteArray());
                        module2.commit();
                    }
                }
            }
        } catch (ConvertException e) {
            e.printStackTrace();
        }
        
    }
}
