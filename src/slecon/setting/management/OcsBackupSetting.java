package slecon.setting.management;

import static logic.util.SiteManagement.MON_MGR;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import comm.Parser_Misc;
import comm.Parser_OcsBackup;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;


@SetupView(
    path      = "Management::OcsBackup",
    sortIndex = 0x410
)
public class OcsBackupSetting extends SettingPanel<OcsBackup> implements Page, LiftDataChangedListener {
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
    
    private Parser_Misc		   misc;
    private Parser_OcsBackup   ocs_backup;
    
    private Thread thread;
    private boolean IsVerify = false;
    private int 	Oper_Status = 1;
    private int     BackupCount = 0;
    private boolean	IsFirstPacket = true;
    
    public OcsBackupSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart () throws Exception {
        try {
            misc = new Parser_Misc( connBean.getIp(), connBean.getPort() );
            ocs_backup = new Parser_OcsBackup( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(), AgentMessage.MISC.getCode() | AgentMessage.OCS_BACKUP.getCode() );
            //setHot();
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
        MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(), AgentMessage.MISC.getCode() | AgentMessage.OCS_BACKUP.getCode() );
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
        synchronized (mutex) {
        	if(msg == AgentMessage.OCS_BACKUP.getCode() ) {
        		setHot();
        	}
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
        return TEXT.getString( "OcsBackup.title" );
    }

	@Override
	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Management"), slecon.setting.management.UtilitiesSetting.class);
		map.put(Dict.lookup("OcsBackup"), null);
		return map;
	}
	
    public void setHot () {
    	int step = ocs_backup.getOperateStep();
    	switch( step ) {
			case 2:
				OcsBackup.SetDownLoadedAttribute( 2, 0, ocs_backup.getBackupDataMD5(), "--");
				sendBackupCommand((byte)3);
				Oper_Status = 3;
			break;
			case 4 :{
				BackupCount += ocs_backup.getPacketDataCount();
				OcsBackup.SetDownLoadedAttribute( 4, BackupCount, ocs_backup.getBackupDataMD5(), "--");
				File file = new File( OcsBackup.GetFilePath() );
				FileOutputStream fos = null;
	            try {
	            	
	            	if( file.exists() && IsFirstPacket ) {
	            		file.delete();
	            	}
	            	
	            	if(!file.exists()){
	            		file.createNewFile();
	            		fos = new FileOutputStream(file);
	            		IsFirstPacket = false;
		            }else{
		            	fos = new FileOutputStream(file,true);
		            }
	            	DataOutputStream out = new DataOutputStream(fos);
	            	out.write(ocs_backup.getBackupData());
	            	out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            Oper_Status = 4;
			}break;
			case 5 :
				OcsBackup.SetDownLoadedAttribute( 5, BackupCount, ocs_backup.getBackupDataMD5(), OcsBackup.GetFileMD5() );
				if(OcsBackup.GetFileMD5().equals(ocs_backup.getBackupDataMD5())) {
					Runnable run = new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(OcsBackup.backup_type_index == 1) {
								InputStream inputStream = null;
						         try{
						             inputStream = new FileInputStream(new File(OcsBackup.GetFilePath()));
						             OcsBackup.ftpClient.setFileType(OcsBackup.ftpClient.BINARY_FILE_TYPE);
						             OcsBackup.ftpClient.changeWorkingDirectory("OCS");
						             OcsBackup.ftpClient.storeFile(OcsBackup.GetFilePath(), inputStream);
						         }catch (Exception e) {
						             e.printStackTrace();
						         }finally{
						        	 OcsBackup.closeConnect();
						             if(null != inputStream){
						                 try {
						                     inputStream.close();
						                 } catch (IOException e) {
						                     e.printStackTrace();
						                 } 
						             } 
						             File file = new File(OcsBackup.GetFilePath());
						             file.delete();
						         }
							}
						}
					};
					run.run();
					
					int isDelete = JOptionPane.showConfirmDialog(null, "File Backup Successful !", "Notify", JOptionPane.YES_NO_OPTION);
					if(isDelete == JOptionPane.YES_OPTION){
						Oper_Status = 1;
						BackupCount = 0;
						IsFirstPacket = true;
						OcsBackup.cbo_backup_type.setSelectedIndex(0);
						OcsBackup.SetFileChooseEnable(true);
					}
	    		}
			break;
		}
    }

    private boolean submit () {
    	if(IsVerify) {
    		if( OcsBackup.GetFilePath() != null) {
    			OcsBackup.SetFileChooseEnable(false);
    			setOKButtonEnabled(false);
    			sendBackupCommand((byte)Oper_Status);
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
    		OcsBackup.SetDownLoadedAttribute( 0, 0, "---", "--");
			OcsBackup.SetFilePath("");
			OcsBackup.SetFileChooseEnable(true);
			setOKButtonEnabled(true);
    	}
    }


	@Override
	public void onOK(OcsBackup panel) {
		// TODO Auto-generated method stub
		setEnabled(false);
		submit();
        setEnabled(true);
	}

	@Override
	public void onReset(OcsBackup panel) {
		// TODO Auto-generated method stub
		reset();
	}
	
   public void sendBackupCommand(byte Type) {
        misc.backupConfirm(Type);
    }
}
