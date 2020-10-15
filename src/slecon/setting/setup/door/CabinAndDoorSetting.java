package slecon.setting.setup.door;
import static logic.util.SiteManagement.MON_MGR;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
import slecon.setting.setup.door.CabinAndDoor.DoorEnableBean;
import slecon.setting.setup.door.CabinAndDoor.LoadWeight;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Deploy;
import comm.Parser_DoorEnable;
import comm.Parser_Error;
import comm.Parser_Misc;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




@SetupView(
    path      = "Setup::CabinAndDoor",
    sortIndex = 0x241,
    condition = ""
)
public class CabinAndDoorSetting extends SettingPanel<CabinAndDoor> implements Page,LiftDataChangedListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8077316991726658200L;

	private static final Logger logger           = LogManager.getLogger( CabinAndDoorSetting.class );
    
    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.SettingPanel");
    
    private volatile long      lastestTimeStamp = -1;
    
    private final Object  mutex  = new Object(); 
    
    private volatile Solid solid = null;
    
    private LiftConnectionBean connBean;

    private Parser_Error  error;
    private Parser_Deploy      deploy;
    private Parser_DoorEnable door_enable;
    private Parser_Status status;
    private Parser_Misc misc;
    private boolean IsVerify = false;
    private static int update_flag = 0x00;
    private static boolean first_into  = true;
    
    public CabinAndDoorSetting ( LiftConnectionBean connBean ) {
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
             door_enable = new Parser_DoorEnable( connBean.getIp(), connBean.getPort() );
             status = new Parser_Status( connBean.getIp(), connBean.getPort() );
             misc = new Parser_Misc( connBean.getIp(), connBean.getPort() );
             
             MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
            		 					AgentMessage.DOOR_ENABLE.getCode() |
                                        AgentMessage.ERROR.getCode() |
                                        AgentMessage.DEPLOYMENT.getCode() |
                                        AgentMessage.STATUS.getCode()
                                       );
             setHot( 0x03 );
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
					                AgentMessage.DOOR_ENABLE.getCode() |  
					                AgentMessage.ERROR.getCode() |
					                AgentMessage.DEPLOYMENT.getCode() |
					                AgentMessage.STATUS.getCode());
					        
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
    public void onOK ( CabinAndDoor panel ) {
    	synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( CabinAndDoor panel ) {
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
        return TEXT.getString( "CabinAndDoor.title" );
    }
    
	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Setup"), slecon.setting.setup.motion.SequenceSetting.class);
		map.put(Dict.lookup("CabinAndDoor"), this.getClass());
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
		
		if( msg == AgentMessage.STATUS.getCode()) {
			synchronized (mutex) {
			    if (solid != null ) {
			        setHot( 0x02 );
			    }
			}
		}
		
		if( msg == AgentMessage.DOOR_ENABLE.getCode()) {
			synchronized (mutex) {
				setEnabled(false);
			    if (solid != null ) {
			        setHot( 0x01 );
			    }
			    setEnabled(true);
			}
		}
	}


	@Override
	public void onConnLost() {
		first_into = true;
		app.stop();
        setEnabled( true );
	}
	  
	public void setHot ( int flag ) {
		try {
			update_flag = flag;
			
			lastestTimeStamp = System.nanoTime();
			String[] floorText = new String[deploy.getFloorCount()];
			for(byte i=0 ; i < deploy.getFloorCount(); i++) {
				floorText[ i ] = new String( deploy.getFloorText( i ) );
			}
			
			final CabinAndDoor.DoorEnableBean bean = new CabinAndDoor.DoorEnableBean();
			final CabinAndDoor.LoadWeight load = new CabinAndDoor.LoadWeight();
			
			bean.setFloorcount(deploy.getFloorCount());
			bean.setFloorText(floorText);
			bean.setDoorEnableAction(door_enable.getDoor_Enable_table());
			
	        load.setLoad( status.getCabinLoad() );
	        load.setDigitalSernsor( status.getLoadweightType() );
	        short[] sa = new short[4];
	        sa = toShortArray( status.getLoadSenorValue() );
	        load.setLoad_sensor( sa );
	        
	        if ( solid == null )
	            solid = new Solid( bean, load );
	        if( first_into == false ) {
	        	 // Update returned data to visualization components.
		        SwingUtilities.invokeLater( new Runnable() {
		            @Override
		            public void run () {
		                app.stop();
		                if( (update_flag & 0x01) == 0x01 ) {
		                	app.setDoorEnableBean( bean );
		                }
		                if( (update_flag & 0x02) == 0x02 ) {
		                	app.setLoadWeightData( load );
		                }
		                app.start();
		            }
		        } );
	        }else {
	        	first_into = false;
	        	app.stop();
                if( (update_flag & 0x01) == 0x01 ) {
                	app.setDoorEnableBean( bean );
                }
                if( (update_flag & 0x02) == 0x02 ) {
                	app.setLoadWeightData( load );
                }
                app.start();
	        }
	       
		}catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
	}
	
	private boolean submit ()  {
		if(IsVerify) { 
	        try {
	            final CabinAndDoor.DoorEnableBean    bean   = app.getDoorEnableBean();
	            byte[] Data = bean.getDoorEnableAction();
	            door_enable.setDoor_Enable_table(Data);
	            door_enable.commit();
	            return true;
	        } catch ( Exception e ) {
	            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
	            logger.catching( Level.FATAL, e );
	        }
		}
        return false;
    }
	
	void reset () {
		if(!IsVerify) {
    		try {
    			JPasswordField pwd = new JPasswordField();
    			Object[] message = {TEXT.getString("Password.text"), pwd};
    			int res = JOptionPane.showConfirmDialog(this, message, TEXT.getString("Password.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    			if(res == JOptionPane.OK_OPTION) {
    				if("282828".equals(new String(pwd.getPassword()))) {
    					IsVerify = true;
            			app.SetTableEnable(true);
    				}else {
    					JOptionPane.showMessageDialog(null,TEXT.getString("Password.error"));
    				}
    				
    			}
    		}catch(Exception e) {
    			JOptionPane.showMessageDialog(null,TEXT.getString("Password.error"));
    		}
    		
    	}else { 
		    // Update returned data to visualization components.
		    if ( solid != null )
		        SwingUtilities.invokeLater( new Runnable() {
		            @Override
		            public void run () {
		                if ( solid != null ) {
		                    app.stop();
		                    app.setDoorEnableBean( solid.bean );
		                    app.start();
		                }
		            }
		        } );
    	}
	}
	 ///////////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final CabinAndDoor.DoorEnableBean	bean;
        private final CabinAndDoor.LoadWeight	load;
        
        private Solid ( DoorEnableBean bean_Settings, LoadWeight load ) {
            super();
            this.bean = bean_Settings;
            this.load = load;
        }
    }
    
    public void setLoadWeightType( byte type ) {
        misc.mcs((short) 0x2408, new byte[] { type }); 
    }
    
    public void setEmptyLoadCorrection() {
        misc.mcs((short) 0x2408, new byte[] { 2 }); 
    }
    
    public void setFullLoadCorrection() {
        misc.mcs((short) 0x2408, new byte[] { 3 }); 
    }
    
    public static short[] toShortArray(byte[] src) {
        int count = src.length >> 1;
        short[] dest = new short[count];
        for (int i = 0; i < count; i++) {
        	dest[i] = (short) ((src[i * 2] & 0xff) | (src[2 * i + 1] << 8) );
        }
        return dest;
    }
}
