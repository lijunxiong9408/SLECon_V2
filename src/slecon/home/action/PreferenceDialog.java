package slecon.home.action;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyCheckBox;
import slecon.component.MyComboBox;
import slecon.component.SubtleSquareBorder;
import slecon.dialog.connection.SiteLiftDialog;
import slecon.home.PosButton;
import slecon.home.ShortcutPanel;
import slecon.interfaces.ConvertException;

public class PreferenceDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -7280516820694990956L;
	static final ResourceBundle           TEXT                  = ToolBox.getResourceBundle( "logic.gui.Preference" );
	private static final String           escapeStrokeActionCommand = "el1.dialog.SelectPopupDialog:WINDOW_CLOSING";
	private final JLabel                  lblTitle           	= new JLabel( TEXT.getString( "Dialog.title" ) );
	private final JSeparator			  separator				= new JSeparator();
    private final JLabel                  lblLanguage           = new JLabel( TEXT.getString( "Label.language" ) );
    private final MyComboBox   			  cbLocale              = new MyComboBox();
    private final PosButton               btnConnection         = new PosButton( TEXT.getString( "Label.connection" ), ImageFactory.BUTTON_PAUSE.icon(87,30) );
    private final JTextField              txtProxyip            = new JTextField();
    private final JTextField              txtProxyport          = new JTextField();
    private final JLabel                  lblProxyip            = new JLabel( TEXT.getString( "Label.proxy_ip" ) );
    private final JLabel                  lblProxyPort          = new JLabel( TEXT.getString( "Label.proxy_port" ) );
    private final JLabel                  lblUptimeFormat       = new JLabel( TEXT.getString( "Label.uptime_format" ) );
    private final MyComboBox       		  cboUptimeFormat       = new MyComboBox();
    private final MyCheckBox              chkSorting            = new MyCheckBox( TEXT.getString( "Label.offline_elevators_aggregated_at_the_bottom" ) );
    private final MyCheckBox              chkHideUnavailability = new MyCheckBox( TEXT.getString( "Label.hide_elevators_offline_elevators" ) );
    private final JLabel                  lblDatePreview        = new JLabel("");
    private PosButton					  btnConfirm				= new PosButton(TEXT.getString("Button_Confirm"), ImageFactory.BUTTON_PAUSE.icon(87,30));
    private PosButton					  btnCancel				= new PosButton(TEXT.getString("Button_Cancel"), ImageFactory.BUTTON_PAUSE.icon(87,30));
    protected PreferenceBean    		  prefBean;
    
    private JPanel	panelMain;
    private	Timer 	timer;
    private static Point pressedPoint = null;
    
	public PreferenceDialog() {
		super( StartUI.getFrame(), "", Dialog.ModalityType.DOCUMENT_MODAL );
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        setUndecorated(true);
		
        separator.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        separator.setPreferredSize(new Dimension(560, 2));
        
        /*
        cbLocale.setOpaque(false);
        cbLocale.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        cbLocale.setRenderer(new ItemRenderer());
        */
        txtProxyip.setText( "Proxy IP" );
        txtProxyip.setColumns( 10 );
        txtProxyip.setCaretColor(Color.WHITE);
        txtProxyip.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        txtProxyip.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        txtProxyip.setForeground(Color.WHITE);
        
        txtProxyport.setText( "Proxy Port" );
        txtProxyport.setColumns( 10 );
        txtProxyport.setCaretColor(Color.WHITE);
        txtProxyport.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        txtProxyport.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        txtProxyport.setForeground(Color.WHITE);
        
        chkSorting.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        chkSorting.setFont(FontFactory.FONT_12_BOLD);
        chkSorting.setForeground(Color.WHITE);
        
        chkHideUnavailability.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        chkHideUnavailability.setFont(FontFactory.FONT_12_BOLD);
        chkHideUnavailability.setForeground(Color.WHITE);
        
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(FontFactory.FONT_12_BOLD);
        lblLanguage.setForeground(Color.WHITE);
        lblLanguage.setFont(FontFactory.FONT_12_BOLD);
        lblProxyip.setForeground(Color.WHITE);
        lblProxyip.setFont(FontFactory.FONT_12_BOLD);
        lblProxyPort.setForeground(Color.WHITE);
        lblProxyPort.setFont(FontFactory.FONT_12_BOLD);
        lblUptimeFormat.setForeground(Color.WHITE);
        lblUptimeFormat.setFont(FontFactory.FONT_12_BOLD);
        lblDatePreview.setForeground(Color.WHITE);
        lblDatePreview.setFont(FontFactory.FONT_12_BOLD);
        
        initGUI();
        
        PreferenceBean bean = new PreferenceBean();
        Locale locale = Locale.forLanguageTag( BaseFactory.getLocaleString() );
        bean.setLocale( locale );
        bean.setProxyIP( BaseFactory.getProxyHostname() );
        bean.setProxyPort( BaseFactory.getProxyPort() );
        bean.setUptimeFormat( BaseFactory.getUptimeFormat() );
        bean.setPutOfflineElevatorAtTheBottom( BaseFactory.isPutOfflineElevatorAtTheBottom() );
        bean.setHideOfflineLift( BaseFactory.isHideOfflineElevator() );
        setPreferenceBean(bean);
        prefBean = bean;
        
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                preview();
            }
            
            public void preview() {
            	long jvmUpTime = ManagementFactory.getRuntimeMXBean().getUptime();
                lblDatePreview.setText(BaseFactory.getUptime(jvmUpTime, cboUptimeFormat.getSelectedItem().toString()));
            }
        });
        
        timer.start();
        
        setLocationRelativeTo( StartUI.getFrame() );
        setResizable(false);
        getRootPane().registerKeyboardAction( this, escapeStrokeActionCommand, KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
                                              JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        // mouse listener.
        getContentPane().addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent e) {
				pressedPoint = e.getPoint();
			}
        });
        
        getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				Point point = e.getPoint();
				Point locationPoint = getLocation();
				int x = locationPoint.x + point.x - pressedPoint.x;
				int y = locationPoint.y + point.y - pressedPoint.y;
				setLocation(x, y);
			}
        });
	}
	
	class ItemRenderer extends BasicComboBoxRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if(isSelected) {
				setBackground(StartUI.BORDER_COLOR);
			}else {
				setBackground(StartUI.SUB_BACKGROUND_COLOR);
			}
			setForeground(Color.WHITE);
			return this;
		}
	}
	
	protected enum Language {
        EN( Locale.ENGLISH ),
        ZH_TW( Locale.TAIWAN ),
        ZH_CN( Locale.CHINA),
		JP( Locale.JAPAN );

        static HashMap<Locale, Language> map = new HashMap<>();

        static {
            for ( Language lang : values() ) {
                map.put( lang.locale, lang );
            }
        }

        private final Locale locale;

        Language ( Locale locale ) {
            this.locale = locale;
        }

        public String toString () {
        	String name = locale.getDisplayName();
        	return name;
        }

        public static Language lookup ( Locale locale ) {
            return map.get( locale );
        }
    }

    private void initGUI () {
    	setBounds( 100, 100, 450, 430 );
		getContentPane().setBackground(StartUI.SUB_BACKGROUND_COLOR);
		
    	panelMain = new JPanel();
    	panelMain.setLayout(new MigLayout("ins 20 20 0 20, gap 5 12, w 450!", "[150!]20![200!][]", "[][][][][][][]0![22!][][]20![]"));
    	panelMain.setBounds(0, 0, 450, 430);
    	panelMain.setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
    	panelMain.setBackground(StartUI.SUB_BACKGROUND_COLOR);
    	panelMain.setOpaque( false );
    	panelMain.setFocusable(true);
        getContentPane().add(panelMain);
        
        panelMain.add( lblTitle, "cell 0 0,alignx left" );
        panelMain.add( separator, "cell 0 1 3 1,alignx center" );
        panelMain.add( lblLanguage, "cell 0 2,alignx right" );
        cbLocale.setModel( new DefaultComboBoxModel<>( Language.values() ) );
        ItemChangedListener ItemListenner = new ItemChangedListener();
        cbLocale.addItemListener(ItemListenner);
        panelMain.add( cbLocale, "cell 1 2" );
        btnConnection.addActionListener( new BtnConnectionActionListener() );
        panelMain.add( btnConnection, "cell 1 3" );
        panelMain.add( lblProxyip, "cell 0 4,alignx trailing" );
        panelMain.add( txtProxyip, "cell 1 4,growx" );
        panelMain.add( lblProxyPort, "cell 0 5,alignx trailing" );
        panelMain.add( txtProxyport, "cell 1 5,growx" );
        panelMain.add(lblUptimeFormat, "cell 0 6,alignx trailing");
        panelMain.add( cboUptimeFormat, "cell 1 6,growx" );
        panelMain.add(lblDatePreview, "cell 1 7");
        panelMain.add( chkSorting, "cell 0 8 2 1, gapleft 100" );
        panelMain.add( chkHideUnavailability, "cell 0 9 2 1, gapleft 100" );
        panelMain.add(btnConfirm, "cell 0 10, alignx right");
        panelMain.add(btnCancel,"cell 1 10,alignx right");
        btnConfirm.addActionListener(this);
        btnCancel.addActionListener(this);
        
        cboUptimeFormat.setModel(new DefaultComboBoxModel<>(new String[] {
                TEXT.getString("Time_format_en"),
                TEXT.getString("Time_format_tw"),
                TEXT.getString("Time_format_cn"),
                TEXT.getString("Time_format_en")
        }));
    }
    
    public static void showDialog () {
        PreferenceDialog   dialog = new PreferenceDialog();
        dialog.setVisible( true );
        dialog.timer.stop();
        dialog.dispose();
    }
    
    private class BtnConnectionActionListener implements ActionListener {
        public void actionPerformed ( final ActionEvent e ) {
            SiteLiftDialog.showDialog();
        }
    }
    
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
    	if ( e.getActionCommand() == escapeStrokeActionCommand ) {
            onCancel();
        }
    	
    	if(e.getSource() == btnConfirm) {
    		 PreferenceBean newBean;
    	        try {
    	            newBean = getPreferenceBean();
    	        } catch ( ConvertException e1 ) {
    	            JOptionPane.showMessageDialog( StartUI.getFrame(), "Invalid input: " + e1.getMessage() );
    	            return;
    	        }

    	        boolean changed = false;
    	        boolean needRestart = false;
    	        if ( prefBean == null ) {
    	            changed = true;
    	        }
    	        if ( prefBean == null || prefBean.getLocale() == null || ! prefBean.getLocale().equals( newBean.getLocale() )
    	                || ! prefBean.getProxyIP().equals( newBean.getProxyIP() ) || ! prefBean.getProxyPort().equals( newBean.getProxyPort() ) ) {
    	            changed = true;
    	            needRestart = true;
    	        }
    	        if ( prefBean == null || Boolean.compare( prefBean.isPutOfflineElevatorAtTheBottom(), newBean.isPutOfflineElevatorAtTheBottom() ) != 0
    	                || Boolean.compare( prefBean.isHideOfflineLift(), newBean.isHideOfflineLift() ) != 0 
    	                || ! prefBean.getUptimeFormat().equals( newBean.getUptimeFormat() ) )
    	            changed = true;

    	        if ( changed ) {
    	            BaseFactory.setLocale( newBean.getLocale().toLanguageTag() );
    	            BaseFactory.setProxyHostname( newBean.getProxyIP() );
    	            BaseFactory.setProxyPort( newBean.getProxyPort() );
    	            BaseFactory.setUptimeFormat( newBean.getUptimeFormat() );
    	            BaseFactory.setPutOfflineElevatorAtTheBottom( newBean.isPutOfflineElevatorAtTheBottom() );
    	            BaseFactory.setHideOfflineElevator( newBean.isHideOfflineLift() );
    	            BaseFactory.save();
    	            prefBean = newBean;
    	            if ( needRestart ) {
    	                StartUI.rebootRequest();
    	            }
    	        }
    	        
	            this.setVisible( false );
	            this.dispose();
	            ShortcutPanel.btn_trigger(1);
    	}
    	
    	if(e.getSource() == btnCancel) {
    		onCancel();	
    	} 
    	
	}

    public class ItemChangedListener implements ActionListener, ItemListener {
        public void itemStateChanged ( ItemEvent e ) {
        	int i = cbLocale.getSelectedIndex();
        	cboUptimeFormat.setSelectedIndex(i);
        }

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int i = cbLocale.getSelectedIndex();
        	cboUptimeFormat.setSelectedIndex(i);
		}
    }
    
    protected void onCancel () {
    	this.timer.stop();
        dispatchEvent( new WindowEvent( this, WindowEvent.WINDOW_CLOSING ) );
        ShortcutPanel.btn_trigger(1);
    }
    
    public PreferenceBean getPreferenceBean () throws ConvertException {
        PreferenceBean result = new PreferenceBean();
        result.setLocale( ( ( Language )cbLocale.getSelectedItem() ).locale );
        result.setProxyIP( txtProxyip.getText() );
        result.setProxyPort( Integer.parseInt( txtProxyport.getText() ) );
        result.setUptimeFormat( cboUptimeFormat.getSelectedItem().toString() );
        result.setPutOfflineElevatorAtTheBottom( chkSorting.isSelected() );
        result.setHideOfflineLift( chkHideUnavailability.isSelected() );
        return result;
    }
    
    public void setPreferenceBean ( PreferenceBean bean ) {
        cbLocale.setSelectedItem( Language.lookup( bean.getLocale() ) );
        txtProxyip.setText( bean.getProxyIP() );
        txtProxyport.setText( bean.getProxyPort() == null ? "2222" : Integer.toString( bean.getProxyPort() ) );
        //cboUptimeFormat.getEditor().setItem( bean.getUptimeFormat() );
        cboUptimeFormat.setSelectedItem(bean.getUptimeFormat());
        chkSorting.setSelected( bean.isPutOfflineElevatorAtTheBottom() );
        chkHideUnavailability.setSelected( bean.isHideOfflineLift() );
    }
    
    public static final class PreferenceBean {
        private Locale  locale;
        private String  proxyIP;
        private Integer proxyPort;
        private String  uptimeFormat;
        private boolean sorted;
        private boolean hide;

        public Locale getLocale () {
            return locale;
        }


        public void setLocale ( Locale locale ) {
            this.locale = locale;
        }


        public final String getProxyIP () {
            return proxyIP;
        }


        public final Integer getProxyPort () {
            return proxyPort;
        }


        public final void setProxyIP ( String proxyIP ) {
            this.proxyIP = proxyIP;
        }


        public final void setProxyPort ( Integer proxyPort ) {
            this.proxyPort = proxyPort;
        }


        public String getUptimeFormat () {
            return uptimeFormat;
        }


        public void setUptimeFormat ( String uptimeFormat ) {
            this.uptimeFormat = uptimeFormat;
        }


        public final boolean isPutOfflineElevatorAtTheBottom () {
            return sorted;
        }


        public final void setPutOfflineElevatorAtTheBottom ( boolean sorted ) {
            this.sorted = sorted;
        }


        public final boolean isHideOfflineLift () {
            return hide;
        }


        public final void setHideOfflineLift ( boolean hide ) {
            this.hide = hide;
        }
    }

}
