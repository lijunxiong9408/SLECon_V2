package slecon.setting.management;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueTextField;
import slecon.home.PosButton;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import slecon.setting.installation.CommissionSetting;
import slecon.setting.management.Update.UpdateType;




/**
 * Import NVRAM file
 *
 */
public class NvImport extends JPanel implements ActionListener {

    private static final long serialVersionUID = -2615861042431035396L;

    /**
     * Text resource.
     */
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.management.NvImport" );
    
    private boolean                 started = false;
    private SettingPanel<NvImport> settingPanel;
    private JLabel                  cpt_update;
    private JLabel                  cpt_update_type;
    private MyComboBox         		cbo_update_type;
    private JLabel                  cpt_file;
    private static PosButton	 	btn_file;
    private static MyComboBox       cbo_remote_file;
    
    private JLabel                  cpt_update_size;
    private static JLabel                  cpt_update_size_value;
    private JLabel                  cpt_update_md5;
    private static JLabel           cpt_update_md5_value;
    
    private JLabel                  cpt_down_size;
    private static JLabel           cpt_down_size_value;
    private JLabel                  cpt_down_size_md5;
    private static JLabel           cpt_down_size_md5_value;
    private static PosButton		btn_Verify_MD5;
    
    static	int	update_type_index = 0;
    static	String	last_update_packet_name = "";
    static 	FTPClient 	ftpClient;
    
    public void setSettingPanel ( SettingPanel<NvImport> panel ) {
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
    
    private int login(String address, int port, String username, String password) {
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
        cpt_update_type = new JLabel();
        cbo_update_type = new MyComboBox();
        cbo_update_type.setModel( new DefaultComboBoxModel<>( UpdateType.values() ) );
        cbo_update_type.addActionListener(this);
        cpt_file = new JLabel();
        cbo_remote_file = new MyComboBox();
        cbo_remote_file.setPreferredSize( new Dimension(280, 25));
        cbo_remote_file.addActionListener(this);
        btn_file = new PosButton(ImageFactory.BUTTON_PAUSE.icon(87, 30),
        						 ImageFactory.BUTTON_START.icon(87, 30));
        btn_file.addActionListener( this );
        
        setCaptionStyle(cpt_update);
        setTextLabelStyle(cpt_update_type);
        setTextLabelStyle(cpt_file);
        
        
        add( cpt_update, "gapbottom 18-12, span, aligny center" );
        add( cpt_update_type, "skip 1, span, split, gapright 12" );
        add( cbo_update_type, "wrap 10, top");
        add( cpt_file, "skip 1, span, split, gapright 12" );
        add( cbo_remote_file, "gapright 12" );
        add( btn_file, "wrap 15, top");

        /* ---------------------------------------------------------------------------- */
        cpt_update_size = new JLabel(); 
        cpt_update_size_value = new JLabel();
        cpt_update_md5 = new JLabel();
        cpt_update_md5_value = new JLabel();
        
        cpt_down_size = new JLabel();
        cpt_down_size_value = new JLabel();
        cpt_down_size_md5 = new JLabel();
        cpt_down_size_md5_value = new JLabel();
        
        btn_Verify_MD5 = new PosButton(ImageFactory.BUTTON_PAUSE.icon(87, 30),
        							   ImageFactory.BUTTON_START.icon(87, 30));
        btn_Verify_MD5.addActionListener( this );
        
        setTextLabelStyle(cpt_update_size);
        setTextLabelStyle(cpt_update_size_value);
        setTextLabelStyle(cpt_update_md5);
        setTextLabelStyle(cpt_update_md5_value);
        setTextLabelStyle(cpt_down_size);
        setTextLabelStyle(cpt_down_size_value);
        setTextLabelStyle(cpt_down_size_md5);
        setTextLabelStyle(cpt_down_size_md5_value);
        
        add( cpt_update_size, "skip 1, span, split, gapright 12" );
        add( cpt_update_size_value, "wrap 10, top" );
        add( cpt_update_md5, "skip 1, span, split, gapright 12" );
        add( cpt_update_md5_value, "wrap 10, top" );
        add( cpt_down_size, "skip 1, span, split, gapright 12" );
        add( cpt_down_size_value, "wrap 10, top" );
        add( cpt_down_size_md5, "skip 1, span, split, gapright 12" );
        add( cpt_down_size_md5_value, "wrap 10, top" );
        add( btn_Verify_MD5,"skip 1, span");
        
        
        SetWeightEnable(false);
        SetVerifyButton(false);
        loadI18N();
        revalidate();
    }
    
    public void SetWeightEnable(boolean enable) {
    	cbo_update_type.setEnabled(enable);
    	cbo_remote_file.setEnabled(enable);
    	btn_file.setEnabled(enable);
    	cpt_update_size.setEnabled(enable);
    	cpt_update_size_value.setEnabled(enable);
    	cpt_update_md5.setEnabled(enable);
    	cpt_update_md5_value.setEnabled(enable);
    	cpt_down_size.setEnabled(enable);
    	cpt_down_size_value.setEnabled(enable);
    	cpt_down_size_md5.setEnabled(enable);
    	cpt_down_size_md5_value.setEnabled(enable);    	
    }
    
    public void SetVerifyButton(boolean enable) {
    	btn_Verify_MD5.setEnabled(enable);
    }

    private void loadI18N () {
    	cpt_update_type.setText( getBundleText( "LBL_cpt_update_type", "Update Way" ) );
    	cpt_update.setText( getBundleText( "LBL_cpt_update", "Update Ocs" ) );
    	cpt_file.setText( getBundleText( "LBL_cpt_file", "file" ) );
    	btn_file.setText( getBundleText( "LBL_btn_file", "Update Ocs" ) );
    	
    	cpt_update_size.setText(getBundleText( "LBL_update_size", "File Packet Size" ));
    	cpt_down_size.setText(getBundleText( "LBL_down_size", "Have Downloaded Size" ));
    	cpt_update_size_value.setText(String.format("%d byte", 0));
    	cpt_down_size_value.setText(String.format("%d byte", 0));
    	
    	cpt_update_md5.setText(getBundleText( "LBL_update_md5", "File Packet MD5" ));
    	cpt_update_md5_value.setText("--");
    	cpt_down_size_md5.setText(getBundleText( "LBL_down_size_md5", "Have Downloaded MD5" ));
    	cpt_down_size_md5_value.setText("--");
    	
    	btn_Verify_MD5.setText(getBundleText( "LBL_Verify_MD5", "Verify MD5" ));
        /* ---------------------------------------------------------------------------- */
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


    public static NvImport createPanel ( SettingPanel<NvImport> panel ) {
        NvImport gui = new NvImport();
        gui.initGUI();
        gui.setSettingPanel( panel );
        return gui;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btn_file) {
			InputStream in = null;
			JFileChooser chooser = new JFileChooser();
	    	FileNameExtensionFilter filter = new FileNameExtensionFilter(".zip", "zip");
	    	chooser.setFileFilter(filter);
	    	int returnVal = chooser.showOpenDialog(new JPanel());
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	// get file name
		    	String path = chooser.getSelectedFile().getPath();
		    	cbo_remote_file.removeAllItems();
		    	cbo_remote_file.addItem(path);
		    	
		    	//get file size 
		    	try {
					 in = new FileInputStream(path);
					this.cpt_update_size_value.setText(String.format("%d byte", in.available()));					
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
		    	
		    	//get file MD5
		    	cpt_update_md5_value.setText(getMD5Three(path));
	    	}
		}
		
		if(e.getSource() == btn_Verify_MD5) {
			if ( settingPanel instanceof NvImportSetting ) {
	            ( ( NvImportSetting )settingPanel ).sendImportCommand( (byte)1 );
	        }
		}
		
		if(e.getSource() == cbo_update_type) {
			update_type_index = this.cbo_update_type.getSelectedIndex();
			if( update_type_index == 0 ) {
				closeConnect();
				cbo_remote_file.removeAllItems();
				btn_file.setEnabled(true);
			}else if ( update_type_index == 1 ) {
				btn_file.setEnabled(false);
				String[] userParam = new String[4];
				userParam = ToolBox.showFtpLoginPanel();
				if( !userParam[0].equals("") && !userParam[1].equals("") && !userParam[2].equals("") && !userParam[3].equals("") ) {
		            if(login( userParam[0], Integer.parseInt(userParam[1]), userParam[2], userParam[3] ) < 0) {
		            	ToolBox.showErrorMessage(getBundleText("ERRORMESSAGE", "Login Fail!"));
		            	this.cbo_update_type.setSelectedIndex(0);
						btn_file.setEnabled(true);
		            }else{
		            	Runnable runnable = new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									cbo_remote_file.addItem(getBundleText("Remote_file_notify", "-- Choose File --"));
									ftpClient.changeWorkingDirectory("NVRAM");
				            		FTPFile[] ftpFiles = ftpClient.listFiles();
				            		for(FTPFile file : ftpFiles) {
				            			if(file.getName().equals(".") || file.getName().equals("..") ) {
				            				continue;
				            			}else {
				            				cbo_remote_file.addItem(file.getName());
				            			}
				            		}
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
		            	};
		            	runnable.run();
		            }
	            }else {
	            	ToolBox.showErrorMessage(getBundleText("ERRORMESSAGE", "Login Fail!"));
	            	this.cbo_update_type.setSelectedIndex(0);
					btn_file.setEnabled(true);
	            }
			}
		}
		
		if( e.getSource() == cbo_remote_file ) {
			if(cbo_remote_file.getSelectedIndex() != 0) {
				int confirm = JOptionPane.showConfirmDialog(null, getBundleText("Remote_file_confirm", "Confirm use the file?"), "Notify", JOptionPane.YES_NO_OPTION);
				if( confirm == JOptionPane.YES_OPTION ) {
					Runnable runnable = new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							DeleteUpdatePacket();
							try {
								FTPFile[] ftpFiles = ftpClient.listFiles();
								String fileName = cbo_remote_file.getSelectedItem().toString();
								for(FTPFile file : ftpFiles) {
									if( fileName.equalsIgnoreCase(file.getName()) ) {
				        				cpt_update_size_value.setText(String.format("%d byte", file.getSize()));
				        				File outFile = new File(fileName); 
										OutputStream is = new FileOutputStream(outFile);
										ftpClient.retrieveFile( fileName, is);
										is.close();
										cpt_update_md5_value.setText(getMD5Three(outFile.getAbsolutePath()));
										last_update_packet_name = fileName;
									}
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					};
					runnable.run();
				}
			}
		}
	}
	
	public static void SetFileChooseEnable( boolean enable) {
		btn_file.setEnabled(enable);
	}
    
	public static String GetFilePath() {
		String path;
		if( update_type_index == 0 ) {
			path = String.valueOf(cbo_remote_file.getSelectedItem());
		}else {
			path = System.getProperty("user.dir") + File.separator + String.valueOf(cbo_remote_file.getSelectedItem());
		}
		return path;
	}
	
	public static void SetDownLoadedAttribute(int size,String md5) {
		cpt_down_size_value.setText(String.format("%d byte", size));
		cpt_down_size_md5_value.setText(md5);
	}
	
	public static void ClearWidgetData() {
		closeConnect();
		cbo_remote_file.removeAllItems();
		cpt_update_size_value.setText(String.format("%d byte", 0));
    	cpt_down_size_value.setText(String.format("%d byte", 0));
    	cpt_update_md5_value.setText("--");
    	cpt_down_size_md5_value.setText("--");
	}
	
	public static String GetFileMD5() {
		return cpt_update_md5_value.getText();
	}
	
	public static void DeleteUpdatePacket() {
		if( !last_update_packet_name.equals("") ) {
			File file = new File(last_update_packet_name);
			file.delete();
		}
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
	
	public static enum UpdateType {
        LOCAL_UPFATE( ( byte )0 ), 
        REMOTE_UPDAYE( ( byte )1 );

        private final static Map<Byte, UpdateType> updateType_list = new HashMap<Byte, UpdateType>();

        static {
            for ( UpdateType lb : UpdateType.values() )
            	updateType_list.put( lb.id, lb );
        }

        public final byte id;

        UpdateType ( byte id ) {
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

        public static UpdateType get ( byte code ) {
            return updateType_list.get( code );
        }
    }
}
