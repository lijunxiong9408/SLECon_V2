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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Error;
import comm.Parser_McsNvram;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;



@SetupView(
    sortIndex = 0x212,
    path      = "Setup::Motion::Speed Profile"
)
public class SpeedProfileSetting extends SettingPanel<SpeedProfile> implements Page, LiftDataChangedListener {
    private static final long           serialVersionUID = 8014304319419260814L;

    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Logger.
     */
    private final Logger                logger           = LogManager.getLogger( SpeedProfileSetting.class );

    private volatile long               lastestTimeStamp = - 1;
    private final Object                mutex            = new Object();
    private volatile Solid              solid            = null;

    private final LiftConnectionBean    connBean;

    private Parser_Error                error;

    private Parser_McsNvram             nvram;

    private boolean IsVerify = false;


    public SpeedProfileSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }
    

    @Override
    public void onStart () throws Exception {
        try {
            error = new Parser_Error( connBean.getIp(), connBean.getPort() );
            nvram = new Parser_McsNvram( connBean.getIp(), connBean.getPort());
            MON_MGR.addEventListener(this, connBean.getIp(), connBean.getPort(),
                    AgentMessage.MCS_CONFIG.getCode() 
                    | AgentMessage.MCS_NVRAM.getCode() 
                    | AgentMessage.ERROR.getCode());
            setHot();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.nanoTime();
        }

        setOKButtonEnabled( Arrays.asList( ToolBox.getRoles( connBean ) ).contains( Role.WRITE_MCS ) );
        setResetButtonEnabled( Arrays.asList( ToolBox.getRoles( connBean ) ).contains( Role.WRITE_MCS ) );
    }
    

    @Override
    public void onResume () throws Exception {
        setEnabled( true );
        MON_MGR.addEventListener(this, connBean.getIp(), connBean.getPort(),
                AgentMessage.MCS_CONFIG.getCode() 
                | AgentMessage.MCS_NVRAM.getCode() 
                | AgentMessage.ERROR.getCode());

        setOKButtonEnabled( Arrays.asList( ToolBox.getRoles( connBean ) ).contains( Role.WRITE_MCS ) );
        setResetButtonEnabled( Arrays.asList( ToolBox.getRoles( connBean ) ).contains( Role.WRITE_MCS ) );
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
    public void onOK ( SpeedProfile panel ) {
        synchronized ( mutex ) {
            setEnabled( false );
            if ( submit() )
                solid = null;
            setEnabled( true );
        }
    }

    
    @Override
    public void onReset ( SpeedProfile panel ) {
    	synchronized ( mutex ) {
            setEnabled(false);
            reset();
            setEnabled(true);
        }
    }

    
    @Override
    public void onConnCreate () {
        app.start();
        setEnabled( false );
    }

    
    @Override
    public void onDataChanged ( long timestamp, int msg ) {
        if ( msg == AgentMessage.ERROR.getCode() )
            ToolBox.showRemoteErrorMessage( connBean, error );

        synchronized ( mutex ) {
            setEnabled( false );
            if ( solid != null && timestamp > lastestTimeStamp + 1500 * 1000000 ) {
                int result = JOptionPane.showConfirmDialog( StartUI.getFrame(), "The config of this lift has changed. Reload it?", "Update",
                        JOptionPane.YES_NO_OPTION );
                if ( result == JOptionPane.OK_OPTION ) {
                    solid = null;
                    setHot();
                }
            } else {
                setHot();
            }
            setEnabled( true );
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
        return TEXT.getString( "SpeedProfile.title" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("Motion"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("Speed_Profile"), this.getClass());
		return map;
	}

    //////////////////////////////////////////////////////////////////////////////////

    private static final class Solid {
        private final Number[][] tableData;


        private Solid ( Number[][] tableData ) {
            super();
            this.tableData = new Number[ tableData.length ][];
            for ( int i = 0; i < tableData.length; i++ ) {
                this.tableData[ i ] = Arrays.copyOf( tableData[ i ], tableData[ i ].length );
            }
        }
    }
    

    public void setHot () {
        lastestTimeStamp = System.nanoTime();

        final Number[][] tableData = new Number[ 6 ][ 8 ];
        for ( int i = 0; i < 6; i++ ) {
            short baseNVAddr = ( short )( 0x0a00 + i * 0x20 ); // NVADDR_TRAVEL_PROFILE_BASE
            tableData[ i ][ 0 ] = nvram.getFloat( ( short )( baseNVAddr + 0x00 ) );
            tableData[ i ][ 1 ] = nvram.getFloat( ( short )( baseNVAddr + 0x04 ) );
            tableData[ i ][ 2 ] = nvram.getFloat( ( short )( baseNVAddr + 0x08 ) );
            tableData[ i ][ 3 ] = nvram.getFloat( ( short )( baseNVAddr + 0x0C ) );
            tableData[ i ][ 4 ] = nvram.getFloat( ( short )( baseNVAddr + 0x10 ) );
            tableData[ i ][ 5 ] = nvram.getFloat( ( short )( baseNVAddr + 0x14 ) );
            tableData[ i ][ 6 ] = nvram.getFloat( ( short )( baseNVAddr + 0x18 ) );
            tableData[ i ][ 7 ] = nvram.getUnsignedInt( ( short )( baseNVAddr + 0x1C ) );
        }

        if ( solid == null )
            solid = new Solid( tableData );

        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                app.setData( tableData );
            }
        } );
        try {
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
    	if(IsVerify) { 
	        try {
	            Object[][] tableData = app.getData();
	
	            for ( int i = 0; i < 6; i++ ) {
	                short baseNVAddr = ( short )( 0x0a00 + i * 0x20 ); // NVADDR_TRAVEL_PROFILE_BASE
	                nvram.setFloat( ( short )( baseNVAddr + 0x00 ), ( float )tableData[ i ][ 0 ] );
	                nvram.setFloat( ( short )( baseNVAddr + 0x04 ), ( float )tableData[ i ][ 1 ] );
	                nvram.setFloat( ( short )( baseNVAddr + 0x08 ), ( float )tableData[ i ][ 2 ] );
	                nvram.setFloat( ( short )( baseNVAddr + 0x0C ), ( float )tableData[ i ][ 3 ] );
	                nvram.setFloat( ( short )( baseNVAddr + 0x10 ), ( float )tableData[ i ][ 4 ] );
	                nvram.setFloat( ( short )( baseNVAddr + 0x14 ), ( float )tableData[ i ][ 5 ] );
	                nvram.setFloat( ( short )( baseNVAddr + 0x18 ), ( float )tableData[ i ][ 6 ] );
	                if ( tableData[ i ][ 7 ] instanceof Number )
	                    nvram.setInt( ( short )( baseNVAddr + 0x1C ), ( ( Number )tableData[ i ][ 7 ] ).intValue() );
	
	            }
	            nvram.commit();
	
	            return true;
	        } catch ( Exception e ) {
	            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
	            logger.catching( Level.FATAL, e );
	        }
    	}
    	return false;
    }


    public void reset () {
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
	        if ( solid != null )
	            SwingUtilities.invokeLater( new Runnable() {
	                @Override
	                public void run () {
	                    if ( solid != null ) {
	                        app.stop();
	                        app.setData( solid.tableData );
	                        app.start();
	                    }
	                }
	            } );
    	}
    }

}
