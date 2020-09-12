package slecon.setting.installation;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.installation.GroupConfiguration.GroupAutomaticOperationSettingBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




@SetupView(
    path      = "Installation::Group configuration",
    sortIndex = 0x150
)
public class GroupConfigurationSetting extends SettingPanel<GroupConfiguration> implements Page, LiftDataChangedListener {
    private static final long     serialVersionUID = -3428363594554717108L;
    private static ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Logger.
     */
    private final Logger       logger = LogManager.getLogger( GroupConfigurationSetting.class );
    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid     solid  = null;
    private LiftConnectionBean connBean;
    private Parser_Error       error;
    private Parser_Deploy      deploy;
    private boolean			   isFirst = true;



    public GroupConfigurationSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart () throws Exception {
        try {
            deploy = new Parser_Deploy( connBean.getIp(), connBean.getPort() );
            error  = new Parser_Error( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                    AgentMessage.DEPLOYMENT.getCode() | AgentMessage.ERROR.getCode() );
            setHot();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.nanoTime();
        }
        
        if(isFirst) {
        	setResetText(TEXT.getString("Reset.default"));	
        }
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
    }


    @Override
    public void onResume () throws Exception {
        setEnabled( true );
        MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                AgentMessage.DEPLOYMENT.getCode() | AgentMessage.ERROR.getCode() );

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
    public void onOK ( GroupConfiguration panel ) {
        synchronized ( mutex  ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( GroupConfiguration panel ) {
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
        return TEXT.getString( "GroupConfiguration.title" );
    }

    @Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
    	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Installation"), slecon.setting.installation.OverviewSetting.class);
		map.put(Dict.lookup("Group_configuration"), null);
		return map;
	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final GroupConfiguration.GroupAutomaticOperationSettingBean bean_groupAutomaticOperationSetting =
                new GroupConfiguration.GroupAutomaticOperationSettingBean();

            /* GroupAutomaticOperationSetting */
            int       dzCount  = deploy.getFloorCount();
            String[]  ftext    = new String[ dzCount ];
            int[][][] gaoTable = new int[ dzCount ][ 16 ][ 2 ];
            for ( int floor = 0 ; floor < dzCount ; floor++ ) {
                ftext[ floor ] = deploy.getFloorText( ( byte )floor );
                for ( int ip = 0 ; ip < 16 ; ip++ ) {
                    gaoTable[ floor ][ 16 - ip - 1 ][ 0 ] = deploy.getGroupRegBitmask( ( byte )floor, ( byte )ip );
                    gaoTable[ floor ][ 16 - ip - 1 ][ 1 ] = deploy.getGroupClrBitmask( ( byte )floor, ( byte )ip );
                }
            }
            bean_groupAutomaticOperationSetting.setGaoTableData( gaoTable );
            bean_groupAutomaticOperationSetting.setText( ftext );
            if ( solid == null ) {
                solid = new Solid( bean_groupAutomaticOperationSetting );
            }
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setGroupAutomaticOperationSettingBean( bean_groupAutomaticOperationSetting );
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            GroupConfiguration.GroupAutomaticOperationSettingBean bean_groupAutomaticOperationSetting =
                app.getGroupAutomaticOperationSettingBean();

            /* GroupAutomaticOperationSetting */
            int[][][] gaoTable = bean_groupAutomaticOperationSetting.getGaoTableData();
            for ( int floor = 0 ; floor < gaoTable.length ; floor++ ) {
                for ( int ip = 0 ; ip < 16 ; ip++ ) {
                    deploy.setGroupRegBitmask( ( byte )floor, ( byte )ip, ( short )gaoTable[ floor ][ 16 - ip - 1 ][ 0 ] );
                    deploy.setGroupClrBitmask( ( byte )floor, ( byte )ip, ( short )gaoTable[ floor ][ 16 - ip - 1 ][ 1 ] );
                }
            }
            deploy.commit();
            return true;
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
            logger.catching( Level.FATAL, e );
        }
        return false;
    }


    public void reset () {

        // Update returned data to visualization components.
        if ( solid != null )
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    if ( solid != null ) {
                        app.stop();
                        if(isFirst) {
                        	GroupAutomaticOperationSettingBean bean = new GroupAutomaticOperationSettingBean();
                        	int       dzCount  = deploy.getFloorCount();
                        	int[][][] gaoTable = new int[ dzCount ][ 16 ][ 2 ];
                            for ( int floor = 0 ; floor < dzCount ; floor++ ) {
                                for ( int ip = 0 ; ip < 16 ; ip++ ) {
                                	if(ip > 11) {
                                		gaoTable[ floor ][ 16 - ip - 1 ][ 0 ] = 0xF000;
                                		gaoTable[ floor ][ 16 - ip - 1 ][ 1 ] = 0xF000;                                		
                                	}else {
                                		gaoTable[ floor ][ 16 - ip - 1 ][ 0 ] = 0;
                                		gaoTable[ floor ][ 16 - ip - 1 ][ 1 ] = 0;
                                	}
                                }
                            }
                            bean.setText(solid.bean_groupAutomaticOperationSetting.getText());
                            bean.setGaoTableData(gaoTable);
                            app.setGroupAutomaticOperationSettingBean( bean );
                            isFirst = false;
                            setResetText(TEXT.getString("Reset.text"));
                        }else {
                        	app.setGroupAutomaticOperationSettingBean( solid.bean_groupAutomaticOperationSetting );
                        }
                        app.start();
                    }
                }
            } );
    }


    //////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final GroupConfiguration.GroupAutomaticOperationSettingBean bean_groupAutomaticOperationSetting;




        private Solid ( GroupAutomaticOperationSettingBean bean_groupAutomaticOperationSetting ) {
            super();
            this.bean_groupAutomaticOperationSetting = bean_groupAutomaticOperationSetting;
        }
    }
}
