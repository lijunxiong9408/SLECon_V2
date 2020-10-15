package slecon.setting.modules;
import static logic.util.SiteManagement.MON_MGR;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import logic.Dict;
import logic.EventID;
import logic.connection.LiftConnectionBean;
import ocsjava.remote.configuration.EventAggregator;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.modules.NonStopOperation.NostopOperaPanelBean;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.Parser_Event;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




@SetupView(
    path      = "Modules::NonstopOperation",
    sortIndex = 0x241,
    condition = ""
)
public class NonStopOperationSetting extends SettingPanel<NonStopOperation> implements Page,LiftDataChangedListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8077316991726658200L;

	private static final Logger logger           = LogManager.getLogger( NonStopOperationSetting.class );
    
    private volatile long      lastestTimeStamp = -1;
    
    private final Object  mutex  = new Object(); 
    
    private volatile Solid solid = null;
    
    private LiftConnectionBean connBean;
    private Parser_Error  	   error;
    private Parser_Deploy      deploy;
    private Parser_Module      module;
    private Parser_Event       event;
    private static boolean 	   first_into = true;
    
    public NonStopOperationSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }

    //////////////////////////////// <Page interface> ////////////////////////////////
    @Override
    public void onCreate ( Workspace workspace ) throws Exception { }

    @Override
    public void onStart () throws Exception {
    	 try {
             error  = new Parser_Error( connBean.getIp(), connBean.getPort() );
             deploy = new Parser_Deploy( connBean.getIp(), connBean.getPort() );
             module = new Parser_Module( connBean.getIp(), connBean.getPort() );
             event = new Parser_Event( connBean.getIp(), connBean.getPort() );
             MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                       AgentMessage.MODULE.getCode() | 
                                       AgentMessage.ERROR.getCode() |
                                       AgentMessage.DEPLOYMENT.getCode() |
                                       AgentMessage.EVENT.getCode() );
             
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
                                  AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() );
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
    }

    @Override
    public void onPause () throws Exception {
    	setEnabled( false );
    	first_into = true;
        MON_MGR.removeEventListener( this );
    }
    
    @Override
    public void onOK ( NonStopOperation panel ) {
    	synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }

    @Override
    public void onReset ( NonStopOperation panel ) {
    	reset();
    }
    
    @Override
    public void onStop () throws Exception {
    	first_into = true;
    	MON_MGR.removeEventListener( this );
    }

    @Override
    public void onDestroy () {
    }
    
    @Override
    protected String getPanelTitle () {
    	return NonStopOperation.TEXT.getString( "NonStopOperation" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Nonstop_Operation"), this.getClass());
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
		if(msg==AgentMessage.ERROR.getCode()) 
			ToolBox.showRemoteErrorMessage(connBean, error);
		
		synchronized (mutex) {
            setEnabled(false);
            if (solid != null && timestamp > lastestTimeStamp+1500*1000000) {
                int result = JOptionPane.showConfirmDialog(StartUI.getFrame(), "The config of this lift has changed. Reload it?", "Update",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    solid = null;
                    event  = new Parser_Event( connBean.getIp(), connBean.getPort() );
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
		first_into = true;
		app.stop();
        setEnabled( true );
	}
	 
	public void setHot () {
		try {
			lastestTimeStamp = System.nanoTime();
			String[] floorText = new String[deploy.getFloorCount()];
			for(byte i=0 ; i < deploy.getFloorCount(); i++) {
				floorText[ i ] = new String( deploy.getFloorText( i ) );
			}
			
			final NostopOperaPanelBean nonStopBean = new NostopOperaPanelBean();
			final EventAggregator ea = EventAggregator.toEventAggregator( event.getEvent(), this.connBean );
			
			nonStopBean.setEnable(module.nonstop.isEnabled());
			nonStopBean.setFloorcount(deploy.getFloorCount());
			nonStopBean.setFloorText(floorText);
			nonStopBean.setContrlMode(module.nonstop.isControlModeEnabled());
			nonStopBean.setOperaSwitch(ea.getEvent(EventID.NONSTOP_OPERATION_PANEL_SWITCH.eventID));
			nonStopBean.setOperaPanelStrategy(module.nonstop.getControlPaneltable());
			nonStopBean.setCarLockStrategy(module.nonstop.getCarLockStrategytable());
	        nonStopBean.setHallLockStrategy(module.nonstop.getHallLockStrategytable());
			
	        if ( solid == null )
	            solid = new Solid( this.connBean, nonStopBean, ea );
	        
	        // Update returned data to visualization components.
            app.setNostopOperaPanelBean( this.connBean, nonStopBean, ea );

		}catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
	}
	
	
	private boolean submit ()  {
		try {
			NostopOperaPanelBean nonStopBean = app.getNostopOperaPanelBean();
            EventAggregator ea = app.getEventAggregator();
            ea.setEvent(EventID.NONSTOP_OPERATION_PANEL_SWITCH.eventID, nonStopBean.getOperaSwitch());
            event.setEvent( ea.toByteArray( this.connBean ) );
            event.setInstalledDevices( ea.getInstalledDevices() );
            event.commit();
            
            module.nonstop.setEnabled(nonStopBean.getEnable());
            module.nonstop.setControlModeEnabled(nonStopBean.getContrlMode());
            module.nonstop.setControlPaneltable(nonStopBean.getOperaPanelStrategy());
            module.nonstop.setCarLockStrategytable(nonStopBean.getCarLockStrategy());
            module.nonstop.setHallLockStrategytable(nonStopBean.getHallLockStrategy());
            module.commit();
            
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(StartUI.getFrame(), "an error has come. " + e.getMessage());
            logger.catching(Level.FATAL, e);
        }
        return false;
    }
	
	void reset () {
	    // Update returned data to visualization components.
	    if ( solid != null )
	        SwingUtilities.invokeLater( new Runnable() {
	            @Override
	            public void run () {
	                if ( solid != null ) {
	                    app.stop();
	                    app.setNostopOperaPanelBean( solid.connBean, solid.nonStopBean, solid.ea );
	                    app.start();
	                }
	            }
	        } );
	}
	 ///////////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
    	private NostopOperaPanelBean nonStopBean;
    	private final LiftConnectionBean connBean;
    	private final EventAggregator ea;
    	
    	public Solid( LiftConnectionBean connBean, NostopOperaPanelBean nonStopBean, EventAggregator ea ) {
    		super();
    		this.connBean = connBean;
    		this.nonStopBean = nonStopBean;
    		this.ea = ea;
    	}

    }
}
