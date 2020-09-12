package slecon.setting.modules;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.EventID;
import logic.connection.LiftConnectionBean;
import logic.util.PageTreeExpression;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.modules.OCSLockUp.GeneralBean;
import slecon.setting.modules.OCSLockUp.InformationBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.Parser_Event;
import comm.Parser_Misc;
import comm.Parser_Module;
import comm.Parser_OcsConfig;
import comm.agent.AgentMessage;
import comm.constants.DeviceMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




/**
 * Data of binding bean for Setup -> Module -> Access Control.
 */
@SetupView(
    path      = "Modules::OCS Lock Up",
    sortIndex = 0x30e
)
public class OCSLockUpSetting extends SettingPanel<OCSLockUp> implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = 6398741682602149481L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( OCSLockUpSetting.class );
    
    private static final PageTreeExpression WRITE_OCS_EXPRESSION = new PageTreeExpression("write_ocs");

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid     solid = null;

    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;

    // TODO
    private Parser_Error  error;
    
    private Parser_Module module;
    private Parser_OcsConfig ocsConfig;
    private Parser_Event  event;
    private Parser_Deploy deploy;
    private Parser_Misc misc;




    /**
     * The handler for Setup - Module - Access Control.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public OCSLockUpSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart () throws Exception {
        try {
            error     = new Parser_Error( connBean.getIp(), connBean.getPort() );
            module    = new Parser_Module( connBean.getIp(), connBean.getPort() );
            ocsConfig = new Parser_OcsConfig( connBean.getIp(), connBean.getPort() );
            misc      = new Parser_Misc( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.MODULE.getCode() | 
                                      AgentMessage.ERROR.getCode() |
                                      AgentMessage.OCS_CONFIG.getCode() |
                                      AgentMessage.MISC.getCode() );
            setHot();
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
                AgentMessage.MODULE.getCode() | 
                AgentMessage.ERROR.getCode() |
                AgentMessage.OCS_CONFIG.getCode() |
                AgentMessage.MISC.getCode() );
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
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
    public void onOK ( OCSLockUp panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( OCSLockUp panel ) {
        reset();
    }


    @Override
    public void onConnCreate () {
        app.start();
        setEnabled( false );
    }


    @Override
    public void onDataChanged ( long timestamp, int msg ) {
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
    public void onConnLost () {
        app.stop();
        setEnabled( true );
    }


    @Override
    public void onDestroy () throws Exception {
    }


    @Override
    protected String getPanelTitle () {
        return OCSLockUp.TEXT.getString( "ocs_lock_up" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("OCS_Lock_Up"), this.getClass());
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final OCSLockUp.GeneralBean     bean_general     = new OCSLockUp.GeneralBean();
            final OCSLockUp.InformationBean bean_Information = new OCSLockUp.InformationBean();

            // General
            bean_general.setEnabled( module.dcs.isOLUEnabled() );
            bean_general.setCarMessage( DeviceMessage.get( module.dcs.getOLUCar_message() ) );
            bean_general.setHallMessage( DeviceMessage.get( module.dcs.getOLUHall_message() ) );

            // Event Settings
            bean_Information.setLastMaintanence( (long) ocsConfig.getOCSExpireDaysCount() );
            
            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGeneralBean( bean_general );
                    app.setInformationBean( bean_Information );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    private boolean submit () {
        try {
            final OCSLockUp.GeneralBean    bean_general    = app.getGeneralBean();
            
            /* General */
            module.dcs.setOLUEnabled( bean_general.getEnabled() );
            module.dcs.setOLUCar_message( bean_general.getCarMessage().getCode() );
            module.dcs.setOLUHall_message( bean_general.getHallMessage().getCode() );


            module.commit();
            return true;
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
            logger.catching( Level.FATAL, e );
        }
        return false;
    }


    private void reset () {

        // Update returned data to visualization components.
        if ( solid != null )
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    if ( solid != null ) {
                        app.stop();
                        app.setGeneralBean( solid.bean_general );
                        app.setInformationBean( solid.bean_Information );
                        app.start();
                    }
                }
            } );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final OCSLockUp.GeneralBean    bean_general;
        private final OCSLockUp.InformationBean bean_Information;




        private Solid ( GeneralBean bean_general, InformationBean bean_iOSettings ) {
            super();
            this.bean_general    = bean_general;
            this.bean_Information = bean_iOSettings;
        }
    }

    
    public void registerMaintanence() {
        if(!ToolBox.requestRole(connBean, WRITE_OCS_EXPRESSION)) {
            ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
            return;
        }
        new Thread() {
            public void run() {
                misc.registerMaintenance();
                
                setEnabled(false);
                app.stop();
                try {
                    Thread.sleep( 1000 );
                } catch (InterruptedException e) {}
                
                app.start();
                setEnabled(true);
                
                setHot();
            }
        }.start();
    }

}
