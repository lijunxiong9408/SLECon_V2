package slecon.setting.management;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueTextField;
import slecon.home.PosButton;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
/**
 * Input/export NVRAM Data file
 *
 */
public class NvBackup extends JPanel implements ActionListener {

    private static final long serialVersionUID = -2615861042431035396L;

    /**
     * Text resource.
     */
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.management.NvBackup" );
    
    private boolean                 started = false;
    private SettingPanel<NvBackup> 	settingPanel;
    private JLabel                  cpt_update;
    private JLabel					cpt_backup_type;
    static MyComboBox         		cbo_backup_type;
    private JLabel                  cpt_file;
    private static ValueTextField	fmt_file;
    private static PosButton		btn_file;
    
    private JLabel                  lbl_backup_status_title;
    private static JLabel           lbl_backup_status_value;
    private JLabel                  lbl_backup_size_title;
    private static JLabel           lbl_backup_size_value;
    private JLabel                  lbl_backup_md5_title;
    private static JLabel           lbl_backup_md5_value;
    private JLabel                  lbl_check_md5_title;
    private static JLabel           lbl_check_md5_value;
    
    static	int	backup_type_index = 0;
    static 	FTPClient 	ftpClient;
    static  String[] userParam = new String[4];
    
    public void setSettingPanel ( SettingPanel<NvBackup> panel ) {
        this.settingPanel = panel;
    }

    static void closeConnect() {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    static int login(String address, int port, String username, String password) {
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(address, port);
            ftpClient.login(username, password);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setBufferSize(1024 * 1024);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                closeConnect();
                return -1;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	return -1;
        }
        return 0;
    }
    
    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[40::40][20::20][32::32][]" ) );
        cpt_update = new JLabel();
        cpt_backup_type = new JLabel();
        cbo_backup_type = new MyComboBox();
        cbo_backup_type.setModel( new DefaultComboBoxModel<>( BackupType.values() ) );
        cbo_backup_type.addActionListener(this);
        cpt_file = new JLabel();
        fmt_file = new ValueTextField();
        fmt_file.setColumns( 30 );
        fmt_file.setHorizontalAlignment( SwingConstants.RIGHT );
        
        btn_file = new PosButton(ImageFactory.BUTTON_PAUSE.icon(87, 30),
        						 ImageFactory.BUTTON_START.icon(87, 30));
        btn_file.addActionListener( this );
        
        setCaptionStyle(cpt_update);
        setTextLabelStyle(cpt_file);
        setTextLabelStyle(cpt_backup_type);
        
        add( cpt_update, "gapbottom 18-12, span, aligny center" );
        add( cpt_backup_type, "skip 1, span, split, gapright 12" );
        add( cbo_backup_type, "wrap 10, top");
        add( cpt_file, "skip 1, span, split, gapright 12" );
        add( fmt_file, "gapright 12" );
        add( btn_file, "wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        lbl_backup_status_title = new JLabel(); 
        lbl_backup_status_value = new JLabel();
        lbl_backup_size_title = new JLabel();
        lbl_backup_size_value = new JLabel();
        lbl_backup_md5_title = new JLabel();
        lbl_backup_md5_value = new JLabel();
        lbl_check_md5_title = new JLabel();
        lbl_check_md5_value = new JLabel();
        
        setTextLabelStyle(lbl_backup_status_title);
        setTextLabelStyle(lbl_backup_status_value);
        setTextLabelStyle(lbl_backup_size_title);
        setTextLabelStyle(lbl_backup_size_value);
        setTextLabelStyle(lbl_backup_md5_title);
        setTextLabelStyle(lbl_backup_md5_value);
        setTextLabelStyle(lbl_check_md5_title);
        setTextLabelStyle(lbl_check_md5_value);
        
        add( lbl_backup_status_title, "skip 1, span, split, gapright 12" );
        add( lbl_backup_status_value, "wrap 10, top" );
        add( lbl_backup_size_title, "skip 1, span, split, gapright 12" );
        add( lbl_backup_size_value, "wrap 10, top" );
        add( lbl_backup_md5_title, "skip 1, span, split, gapright 12" );
        add( lbl_backup_md5_value, "wrap 10, top" );
        add( lbl_check_md5_title, "skip 1, span, split, gapright 12" );
        add( lbl_check_md5_value, "wrap 10, top" );
        
        SetWeightEnable(false);
        loadI18N();
        revalidate();
    }
    
    public void SetWeightEnable(boolean enable) {
    	cbo_backup_type.setEnabled(enable);
    	fmt_file.setEnabled(enable);
    	btn_file.setEnabled(enable);
    	lbl_backup_status_title.setEnabled(enable);
    	lbl_backup_status_value.setEnabled(enable);
    	lbl_backup_size_title.setEnabled(enable);
    	lbl_backup_size_value.setEnabled(enable);
    	lbl_backup_md5_title.setEnabled(enable);
    	lbl_backup_md5_value.setEnabled(enable);
    	lbl_check_md5_title.setEnabled(enable);
    	lbl_check_md5_value.setEnabled(enable);    	
    }

    private void loadI18N () {
    	cpt_backup_type.setText( getBundleText( "LBL_cpt_backup_type", "Backup Type" ) );
    	cpt_update.setText( getBundleText( "LBL_cpt_update", "Backup NVRAM" ) );
    	cpt_file.setText( getBundleText( "LBL_cpt_file", "file" ) );
    	btn_file.setText( getBundleText( "LBL_btn_file", "Backup NVRAM" ) );
    	
    	lbl_backup_status_title.setText(getBundleText( "LBL_lbl_backup_status_title", "Oper Status :" ));
    	lbl_backup_status_value.setText(getBundleText( "LBL_lbl_backup_status_value", "Idle" ));
    	lbl_backup_size_title.setText(getBundleText( "LBL_lbl_backup_size_title", "File Size :" ));
    	lbl_backup_size_value.setText(String.format("%d byte", 0));
    	lbl_backup_md5_title.setText(getBundleText( "LBL_lbl_backup_md5_title", "Backup File MD5 :" ));
    	lbl_backup_md5_value.setText("--");
    	lbl_check_md5_title.setText(getBundleText( "LBL_lbl_check_md5_title", "Gain packet MD5 :" ));
    	lbl_check_md5_value.setText("--");
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setTextLabelStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setLabelTitleStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
    }


    private void setLabelValueStyle ( JLabel c ) {
        c.setOpaque( true );
        c.setBackground( java.awt.Color.LIGHT_GRAY );
        c.setFont( FontFactory.FONT_12_PLAIN );
    }


    private void bindGroup ( final String detailKey, final JComponent... list ) {
        if ( detailKey != null && detailKey.trim().length() > 0 ) {
            for ( JComponent c : list ) {
                c.addMouseListener( new MouseAdapter() {
                    String detailText;
                    @Override
                    public synchronized void mouseEntered ( MouseEvent evt ) {
                        if ( settingPanel != null ) {
                            if ( detailText == null ) {
                                try {
                                    detailText = TEXT.getString( "Description_" + detailKey );
                                } catch ( Exception e ) {
                                    detailText = "No description here! Be careful.";
                                }
                            }
                            settingPanel.setDescription( detailText );
                        }
                    }
                    @Override
                    public void mouseExited ( MouseEvent e ) {
                        if ( settingPanel != null ) {
                            settingPanel.setDescription( null );
                        }
                    }
                } );
            }
        }
    }

    public String getBundleText ( String key, String defaultValue ) {
        String result;
        try {
            result = TEXT.getString( key );
        } catch ( Exception e ) {
            result = defaultValue;
        }
        return result;
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static NvBackup createPanel ( SettingPanel<NvBackup> panel ) {
        NvBackup gui = new NvBackup();
        gui.initGUI();
        gui.setSettingPanel( panel );
        return gui;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btn_file) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(".zip", "zip");
	    	chooser.setFileFilter(filter);
	    	chooser.setAcceptAllFileFilterUsed(false);
	    	
	    	chooser.setDialogType(JFileChooser.SAVE_DIALOG);
	    	int returnVal = chooser.showOpenDialog(new JPanel());
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	// get file name
		    	String path = chooser.getSelectedFile().getPath() + filter.getDescription();
		    	this.fmt_file.setValue(path);
	    	}
		}
		
		if( e.getSource() == cbo_backup_type ) {
			backup_type_index = cbo_backup_type.getSelectedIndex();
			if( backup_type_index == 0 ) {
				closeConnect();
				fmt_file.setText("");
				btn_file.setEnabled(true);
			}else if ( backup_type_index == 1 ) {
				btn_file.setEnabled(false);
				userParam = ToolBox.showFtpLoginPanel();
				if( !userParam[0].equals("") && !userParam[1].equals("") && !userParam[2].equals("") && !userParam[3].equals("") ) {
		            if(login( userParam[0], Integer.parseInt(userParam[1]), userParam[2], userParam[3] ) < 0) {
		            	closeConnect();
		            	ToolBox.showErrorMessage(getBundleText("ERRORMESSAGE", "Login Fail!"));
		            	cbo_backup_type.setSelectedIndex(0);
						btn_file.setEnabled(true);
		            }else{
		            	String backupName = ToolBox.InputInfoPanel();
		            	if( !backupName.equals("") ) {
		            		fmt_file.setText(backupName + ".zip");
		            	}else {		        
		            		this.cbo_backup_type.setSelectedIndex(0);
							btn_file.setEnabled(true);
		            	}
		            }
	            }else {
	            	ToolBox.showErrorMessage(getBundleText("ERRORMESSAGE", "Login Fail!"));
	            	this.cbo_backup_type.setSelectedIndex(0);
					btn_file.setEnabled(true);
	            }
			}
		}
	}
	
	public static void SetFileChooseEnable( boolean enable) {
		fmt_file.setEnabled(enable);
		btn_file.setEnabled(enable);
	}
    
	public static void SetFilePath ( String path ) {
		fmt_file.setText(path);
	}
	
	public static String GetFilePath() {
		return fmt_file.getText();
		
	}
	
	public static void SetDownLoadedAttribute(int step, int size, String md5, String check_md5) {
		lbl_backup_size_value.setText(String.format("%d byte", size));
		lbl_backup_md5_value.setText(md5);
		lbl_check_md5_value.setText(check_md5);
		switch(step) {
		    case 0:
		    	lbl_backup_status_value.setText( TEXT.getString( "LBL_lbl_backup_status_value" ) );
		    break;
			case 2 :
				lbl_backup_status_value.setText( TEXT.getString( "LBL_status_prepare" ) );
			break;
			case 4 :
				lbl_backup_status_value.setText( TEXT.getString( "LBL_status_upload" ) );
			break;
			case 5 :
				lbl_backup_status_value.setText( TEXT.getString( "LBL_status_finish" ) );
			break;
		}
	}
	
	public static String GetFileMD5() {
		return getMD5Three( GetFilePath() );
	}
	
	public static String getMD5Three(String path) {
		BigInteger bi = null;
		try {
			byte[] buffer = new byte[8192];
			int len = 0;
			MessageDigest md = MessageDigest.getInstance("MD5");
	        	File f = new File(path);
	        	FileInputStream fis = new FileInputStream(f);
				while ((len = fis.read(buffer)) != -1) {
					md.update(buffer, 0, len);
				}
				fis.close();
				byte[] b = md.digest();
				bi = new BigInteger(1, b);
	    } catch (NoSuchAlgorithmException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		String str = bi.toString(16);
		while(str.length() < 32) {
			StringBuffer sb = new StringBuffer();
			sb.append("0").append(str);
			str = sb.toString();
		}
	     return str;
	 }
	
	public static enum BackupType {
        LOCAL_BACKUP( ( byte )0 ), 
        REMOTE_BACKUP( ( byte )1 );

        private final static Map<Byte, BackupType> backupType_list = new HashMap<Byte, BackupType>();

        static {
            for ( BackupType lb : BackupType.values() )
            	backupType_list.put( lb.id, lb );
        }

        public final byte id;

        BackupType ( byte id ) {
            this.id = id;
        }

        @Override
        public String toString () {
            String bundleString = null;
            try {
                bundleString = TEXT.getString( name() );
            } catch ( Exception e ) {
            }
            return bundleString == null
                   ? name()
                   : bundleString;
        }

        public static BackupType get ( byte code ) {
            return backupType_list.get( code );
        }
    }
}
