package slecon.setting.management;

import static logic.util.SiteManagement.MON_MGR;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import comm.Parser_Error;
import comm.Parser_Misc;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;
import comm.util.Endian;



@SetupView(
    path      = "Management::Update",
    sortIndex = 0x410
)
public class UpdateSetting extends SettingPanel<Update> implements Page, LiftDataChangedListener {
    private static final long           serialVersionUID = -3944112770351037862L;
    
    /**
     * Text resource.
     */
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.SettingPanel" );

    /**
     * Connectivity information.
     */
    private final LiftConnectionBean connBean;
    
    private final Object       mutex  = new Object(); 
    
    private Parser_Error       error;
    private Parser_Status      status;
    private Parser_Misc		   misc;
    
    private Thread thread;
    private boolean IsVerify = false;
    private boolean DownFileAttributeReset = true;

    
    public UpdateSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart () throws Exception {
        try {
            error  = new Parser_Error( connBean.getIp(), connBean.getPort() );
            status = new Parser_Status( connBean.getIp(), connBean.getPort() );
            misc = new Parser_Misc( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.ERROR.getCode() | AgentMessage.MCS_CONFIG.getCode() | AgentMessage.MISC.getCode() );
            setHot();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.nanoTime();
        }
        
        app.start();
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MANAGER));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_MANAGER));
    }


    @Override
    public void onResume () throws Exception {
        app.start();
        setEnabled( true );
        MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                AgentMessage.ERROR.getCode() | AgentMessage.MCS_CONFIG.getCode() | AgentMessage.MISC.getCode() );
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
    }


    @Override
    public void onPause () throws Exception {
        app.stop();
        setEnabled( false );
        MON_MGR.removeEventListener( this );
    }


    @Override
    public void onStop () throws Exception {
    	app.DeleteUpdatePacket();
    	app.closeConnect();
        app.stop();
        MON_MGR.removeEventListener( this );
    }

    @Override
    public void onConnCreate () {
        app.start();
        setEnabled( false );
    }


    @Override
    public void onDataChanged ( long timestamp, int msg ) {
    	if (msg==AgentMessage.ERROR.getCode()) {
            ToolBox.showRemoteErrorMessage(connBean, error);            
        }
        synchronized (mutex) {
        	DownFileAttributeReset = false;
        	setHot();
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
        return TEXT.getString( "Update.title" );
    }

	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Management"), slecon.setting.management.UtilitiesSetting.class);
		map.put(Dict.lookup("Update"), null);
		return map;
	}
	
    public void setHot () {
    	if(DownFileAttributeReset) {
    		Update.SetDownLoadedAttribute(0,"--");
    	}else {
    		Update.SetDownLoadedAttribute(status.getUpdateDataCount(),status.getUpdateDataMD5());
    		
    		if(Update.GetFileMD5().equals(status.getUpdateDataMD5())) {
				int isDelete = JOptionPane.showConfirmDialog(null, "File Download Successful !", "Notify", JOptionPane.YES_NO_OPTION);
				if(isDelete == JOptionPane.YES_OPTION){
					sendUpdateCommand((byte)0);
				}
    		}
    	}
    	
    }


    private boolean submit () {
    	if(IsVerify) {
	        try {
	        	setOKButtonEnabled(false);
	        	Update.SetFileChooseEnable(false);
	        	thread = new Thread(new MyThread());
	        	thread.start();
	        	return true;
	        } catch ( Exception e ) {
	            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
	        }
    	}
        return false;
    }


    private void reset () {
    	if(!IsVerify) {
    		try {
    			JPasswordField pwd = new JPasswordField();
    			Object[] message = {TEXT.getString("Password.text"), pwd};
    			int res = JOptionPane.showConfirmDialog(this, message, TEXT.getString("Password.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    			if(res == JOptionPane.OK_OPTION) {
    				if("282828".equals(new String(pwd.getPassword()))) {
    					IsVerify = true;
            			app.SetWeightEnable(true);
    				}else {
    					JOptionPane.showMessageDialog(null,TEXT.getString("Password.error"));
    				}
    			}
    		}catch(Exception e) {
    			JOptionPane.showMessageDialog(null,TEXT.getString("Password.error"));
    		}
    	}else {
    		DownFileAttributeReset = true;
    		app.ClearWidgetData();
    	}
    }


	@Override
	public void onOK(Update panel) {
		// TODO Auto-generated method stub
		setEnabled(false);
		submit();
        setEnabled(true);
	}


	@Override
	public void onReset(Update panel) {
		// TODO Auto-generated method stub
		reset();
	}
	
	class MyThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			final String Path = Update.GetFilePath();
			InputStream in = null;  
            try {  
                byte[] tempbytes = new byte[2048];  
                int packetID = 0;
                int byteread = 0; 
                int FileSize = 0;
                int bytesend = 0;
                in = new FileInputStream(Path); 
                FileSize = in.available();
                while ((byteread = in.read(tempbytes)) != -1) {
                	packetID ++;
                	bytesend += byteread;
                	byte [] ret = new byte[byteread + 8];
                	System.arraycopy( Endian.getIntByteArray(packetID) , 0, ret, 0, 4 );
                	System.arraycopy( Endian.getIntByteArray(FileSize) , 0, ret, 4, 4 );
                	System.arraycopy(tempbytes, 0, ret, 8, byteread);
                	misc.update(ret);
                	Thread.sleep(500);
                } 
                if(FileSize == bytesend) {
                	app.SetVerifyButton(true);
                	setOKButtonEnabled(true);
    	        	Update.SetFileChooseEnable(true);
                }
            } catch (Exception e1) {  
                e1.printStackTrace();  
            } finally {  
                if (in != null) {  
                    try {  
                        in.close();  
                    } catch (IOException e1) {  
                    }  
                }  
            } 
		}
	}
	
   public void sendUpdateCommand(byte Type) {
        misc.updateConfirm(Type);
    }
}
