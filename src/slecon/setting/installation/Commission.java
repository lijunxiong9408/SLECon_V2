package slecon.setting.installation;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.home.HomePanel;
import slecon.home.PosButton;
import slecon.home.dashboard.DashboardPanel;
import slecon.interfaces.ConvertException;
import slecon.setting.installation.CommissionSetting.DoorCommand;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;

public class Commission extends JPanel implements ActionListener, ChangeListener {
    private static final long serialVersionUID = -1474652487505068145L;
    public static final ResourceBundle TEXT = ToolBox.getResourceBundle("setting.installation.Commission");
    private ImageIcon 	BUTTON_PAUSE_ICON  = null;
    private ImageIcon 	BUTTON_START_ICON  = null;
    private boolean started = false;
    
    private SettingPanel<Commission> settingPanel;
    private JLabel cpt_hardware;
    private ValueCheckBox ebd_installation_mode;
    private ValueCheckBox ebd_temporary_driver_activation;
    private JLabel lbl_calibrate_analog_output;
    private JTextField txt_calibrate_analog_output;
    private PosButton btn_set_calibrate_analog_output;
    private ValueCheckBox ebd_suspend_dcs_automation;
    private PosButton btn_open_front_door;
    private PosButton btn_close_front_door;
    private PosButton btn_open_rear_door;
    private PosButton btn_close_rear_door;

    /*----------------------------------------------------------------------------*/
    private JLabel cpt_diagnostic;
    private PosButton btn_hardware_self_test;
    private PosButton btn_verify_nvram;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel         		  cpt_security_test;
    private JLabel         		  lbl_mcs_reset;
    private PosButton			  btn_mcs_reset;
    private ValueCheckBox 		  ebd_mcs_independent_mode;
    private PosButton			  btn_mcs_emer_stop;
    private PosButton			  btn_mcs_pos_corr;
    private JLabel				  lbl_buffer_test;
    private MyComboBox			  cbo_buffer_test;
    private PosButton			  btn_buffer_test;
    private JLabel				  lbl_terminal_test;
    private MyComboBox			  cbo_terminal_test;
    private PosButton			  btn_terminal_test;
    private JLabel				  lbl_ucmp_test;
    private MyComboBox			  cbo_ucmp_test;
    private PosButton			  btn_ucmp_test;
    
    public Commission () {
        try {
            initGUI();
            ebd_installation_mode.addChangeListener( this );
            do_ebd_installation_mode_stateChanged( null );
            ebd_temporary_driver_activation.addChangeListener(this);
            do_ebd_temporary_driver_activation_stateChanged(null);
            ebd_mcs_independent_mode.addChangeListener(this);
            do_ebd_mcs_independent_mode_stateChanged(null);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


    public void setSettingPanel ( SettingPanel<Commission> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][200::200][150::150][]" ) );
        
        if(BaseFactory.getLocaleString().equals("en")) {
        	BUTTON_PAUSE_ICON  = ImageFactory.BUTTON_PAUSE.icon(120, 30);
        	BUTTON_START_ICON  = ImageFactory.BUTTON_START.icon(120, 30);
        }
        else {
        	BUTTON_PAUSE_ICON  = ImageFactory.BUTTON_PAUSE.icon(87, 30);
        	BUTTON_START_ICON  = ImageFactory.BUTTON_START.icon(87, 30);
        }
        cpt_hardware = new JLabel();
        ebd_installation_mode = new ValueCheckBox();
        ebd_installation_mode.addActionListener( this );
        ebd_temporary_driver_activation = new ValueCheckBox();
        ebd_temporary_driver_activation.addActionListener( this );
        lbl_calibrate_analog_output = new JLabel();
        txt_calibrate_analog_output = new JTextField();
        btn_set_calibrate_analog_output = new PosButton(BUTTON_PAUSE_ICON, BUTTON_START_ICON);
        btn_set_calibrate_analog_output.addActionListener( this );
        ebd_suspend_dcs_automation = new ValueCheckBox();
        ebd_suspend_dcs_automation.addActionListener( this );
        btn_open_front_door = new PosButton(BUTTON_PAUSE_ICON, BUTTON_START_ICON);
        btn_open_front_door.addActionListener( this );
        btn_close_front_door = new PosButton(BUTTON_PAUSE_ICON, BUTTON_START_ICON);
        btn_close_front_door.addActionListener( this );
        btn_open_rear_door = new PosButton(BUTTON_PAUSE_ICON, BUTTON_START_ICON);
        btn_open_rear_door.addActionListener( this );
        btn_close_rear_door = new PosButton(BUTTON_PAUSE_ICON, BUTTON_START_ICON);
        btn_close_rear_door.addActionListener( this );

        setCaptionStyle( cpt_hardware );
        // @CompoentSetting( ebd_installation_mode )

        // @CompoentSetting( ebd_temporary_driver_activation )

        ebd_temporary_driver_activation.setEnabled( false );
        lbl_calibrate_analog_output.setEnabled(false);
        txt_calibrate_analog_output.setEnabled(false);
        btn_set_calibrate_analog_output.setEnabled(false);
        
        // @CompoentSetting<Txt>( lbl_calibrate_analog_output , txt_calibrate_analog_output, btn_set_calibrate_analog_output )
        setTextLabelStyle( lbl_calibrate_analog_output );
        setTextValueStyle( txt_calibrate_analog_output );
        txt_calibrate_analog_output.setColumns( 5 );
        txt_calibrate_analog_output.setHorizontalAlignment( SwingConstants.RIGHT );
        txt_calibrate_analog_output.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        txt_calibrate_analog_output.setCaretColor(Color.WHITE);
        txt_calibrate_analog_output.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        txt_calibrate_analog_output.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        
        setButtonStyle( btn_set_calibrate_analog_output );
        // @CompoentSetting( ebd_suspend_dcs_automation )

        // @CompoentSetting( btn_open_front_door )
        setButtonStyle( btn_open_front_door );
        // @CompoentSetting( btn_close_front_door )
        setButtonStyle( btn_close_front_door );
        // @CompoentSetting( btn_open_rear_door )
        setButtonStyle( btn_open_rear_door );
        // @CompoentSetting( btn_close_rear_door )
        setButtonStyle( btn_close_rear_door );
        
        add( cpt_hardware, "gapbottom 18-12, span, top" );
        add( ebd_installation_mode, "skip 1, span" );
        add( ebd_temporary_driver_activation, "skip 1, span" );
        add( lbl_calibrate_analog_output, "skip 2, span, split, gapright 12" );
        add( txt_calibrate_analog_output, "gapright 12" );
        add( btn_set_calibrate_analog_output, "wrap" );
        add( ebd_suspend_dcs_automation, "skip 1, span" );
        add( btn_open_front_door, "skip 2, span" );
        add( btn_close_front_door, "skip 2, span" );
        add( btn_open_rear_door, "skip 2, span" );
        add( btn_close_rear_door, "skip 2, span, wrap 30, top" );

        /*----------------------------------------------------------------------------*/
        cpt_diagnostic = new JLabel();
        btn_hardware_self_test = new PosButton(BUTTON_PAUSE_ICON, BUTTON_START_ICON);
        btn_hardware_self_test.addActionListener( this );
        btn_verify_nvram = new PosButton(BUTTON_PAUSE_ICON, BUTTON_START_ICON);
        btn_verify_nvram.addActionListener( this );

        setCaptionStyle( cpt_diagnostic );
        // @CompoentSetting( btn_hardware_self_test )
        setButtonStyle( btn_hardware_self_test );
        // @CompoentSetting( btn_verify_nvram )
        setButtonStyle( btn_verify_nvram );

        add( cpt_diagnostic, "gapbottom 18-12, span, top" );
        add( btn_hardware_self_test, "skip 2, span" );
        add( btn_verify_nvram, "skip 2, span, wrap 30, top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_security_test = new JLabel();
        lbl_mcs_reset = new JLabel();
        btn_mcs_reset = new PosButton(ImageFactory.BUTTON_PAUSE.icon(72, 25), ImageFactory.BUTTON_START.icon(72, 25));
        ebd_mcs_independent_mode = new ValueCheckBox();
        ebd_mcs_independent_mode.addActionListener(this);
        btn_mcs_emer_stop = new PosButton(ImageFactory.BUTTON_PAUSE.icon(72, 25), ImageFactory.BUTTON_START.icon(72, 25));
        btn_mcs_pos_corr = new PosButton(ImageFactory.BUTTON_PAUSE.icon(72, 25), ImageFactory.BUTTON_START.icon(72, 25));
        lbl_buffer_test = new JLabel();
        cbo_buffer_test = new MyComboBox();
        btn_buffer_test = new PosButton(ImageFactory.BUTTON_PAUSE.icon(72, 25), ImageFactory.BUTTON_START.icon(72, 25));
        lbl_terminal_test = new JLabel();
        cbo_terminal_test = new MyComboBox();
        btn_terminal_test = new PosButton(ImageFactory.BUTTON_PAUSE.icon(72, 25), ImageFactory.BUTTON_START.icon(72, 25));
        lbl_ucmp_test = new JLabel();
        cbo_ucmp_test = new MyComboBox();
        btn_ucmp_test = new PosButton(ImageFactory.BUTTON_PAUSE.icon(72, 25), ImageFactory.BUTTON_START.icon(72, 25));
        
        setCaptionStyle( cpt_security_test );
        setTextLabelStyle( lbl_mcs_reset );
        setTextLabelStyle( lbl_buffer_test );
        setTextLabelStyle( lbl_terminal_test );
        setTextLabelStyle( lbl_ucmp_test );
        
        btn_mcs_reset.addActionListener(this);
        btn_mcs_emer_stop.addActionListener(this);
        btn_mcs_pos_corr.addActionListener(this);
        btn_buffer_test.addActionListener(this);
        btn_terminal_test.addActionListener(this);
        btn_ucmp_test.addActionListener(this);
        
        cbo_buffer_test.setModel( new DefaultComboBoxModel<RunDirect>(RunDirect.values()) );
        cbo_terminal_test.setModel( new DefaultComboBoxModel<RunDirect>(RunDirect.values()) );
        cbo_ucmp_test.setModel( new DefaultComboBoxModel<UcmpTest>(UcmpTest.values()) );
        
        Box vbox_title3 = Box.createVerticalBox();
        vbox_title3.add( lbl_mcs_reset);
        vbox_title3.add( Box.createVerticalStrut(15));
        vbox_title3.add( ebd_mcs_independent_mode);
        vbox_title3.add( Box.createVerticalStrut(15));
        vbox_title3.add( btn_mcs_emer_stop);
        vbox_title3.add( Box.createVerticalStrut(15));
        vbox_title3.add( lbl_buffer_test);
        vbox_title3.add( Box.createVerticalStrut(15));
        vbox_title3.add( lbl_terminal_test);
        vbox_title3.add( Box.createVerticalStrut(15));
        vbox_title3.add( lbl_ucmp_test);
        
        Box vbox_value3 = Box.createVerticalBox();
        vbox_value3.add( Box.createVerticalStrut(80));
        vbox_value3.add( btn_mcs_pos_corr );
        vbox_value3.add( Box.createVerticalStrut(10));
        vbox_value3.add( cbo_buffer_test );
        vbox_value3.add( Box.createVerticalStrut(10));
        vbox_value3.add( cbo_terminal_test );
        vbox_value3.add( Box.createVerticalStrut(10));
        vbox_value3.add( cbo_ucmp_test );
        
        Box vbox_oper3 = Box.createVerticalBox();
        vbox_oper3.add( btn_mcs_reset );
        vbox_oper3.add( Box.createVerticalStrut(90));
        vbox_oper3.add( btn_buffer_test );
        vbox_oper3.add( Box.createVerticalStrut(3));
        vbox_oper3.add( btn_terminal_test );
        vbox_oper3.add( Box.createVerticalStrut(3));
        vbox_oper3.add( btn_ucmp_test );
        
        add( cpt_security_test, "gapbottom 18-12, span, top" );
        add( vbox_title3, "skip 2, span 1, left, top" );
        add( vbox_value3, "span 1, left, top" );
        add( vbox_oper3, "span 1, wrap 30, left, top" );
        
        /*----------------------------------------------------------------------------*/
        bindGroup( "InstallationMode", ebd_installation_mode );
        bindGroup( "TemporaryDriverActivation", ebd_temporary_driver_activation );
        bindGroup( "CalibrateAnalogOutput", lbl_calibrate_analog_output, txt_calibrate_analog_output, btn_set_calibrate_analog_output );
        bindGroup( "SuspendDcsAutomation", ebd_suspend_dcs_automation );
        bindGroup( "OpenFrontDoor", btn_open_front_door );
        bindGroup( "CloseFrontDoor", btn_close_front_door );
        bindGroup( "OpenRearDoor", btn_open_rear_door );
        bindGroup( "CloseRearDoor", btn_close_rear_door );
        bindGroup( "HardwareSelfTest", btn_hardware_self_test );
        bindGroup( "VerifyNvram", btn_verify_nvram );
        
        //bindGroup( new AbstractButton[] { ebd_installation_mode }, ebd_temporary_driver_activation, lbl_calibrate_analog_output, txt_calibrate_analog_output, btn_set_calibrate_analog_output);
        bindGroup( new AbstractButton[] { ebd_suspend_dcs_automation }, btn_open_front_door, btn_close_front_door, btn_open_rear_door, btn_close_rear_door);
        
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_hardware.setText( getBundleText( "LBL_cpt_hardware", "Hardware" ) );
        ebd_installation_mode.setText( getBundleText( "LBL_ebd_installation_mode", "Installation mode" ) );
        ebd_temporary_driver_activation.setText( getBundleText( "LBL_ebd_temporary_driver_activation", "Temporary driver activation" ) );
        lbl_calibrate_analog_output.setText( getBundleText( "LBL_lbl_calibrate_analog_output", "Calibrate analog output" ) );
        btn_set_calibrate_analog_output.setText( getBundleText( "LBL_btn_set_calibrate_analog_output", "Set" ) );
        ebd_suspend_dcs_automation.setText( getBundleText( "LBL_ebd_suspend_dcs_automation", "Suspend DCS Automation" ) );
        btn_open_front_door.setText( getBundleText( "LBL_btn_open_front_door", "Open Front Door" ) );
        btn_close_front_door.setText( getBundleText( "LBL_btn_close_front_door", "Close Front Door" ) );
        btn_open_rear_door.setText( getBundleText( "LBL_btn_open_rear_door", "Open Rear Door" ) );
        btn_close_rear_door.setText( getBundleText( "LBL_btn_close_rear_door", "Close Rear Door" ) );

        /*----------------------------------------------------------------------------*/
        cpt_diagnostic.setText( getBundleText( "LBL_cpt_diagnostic", "Diagnostic" ) );
        btn_hardware_self_test.setText( getBundleText( "LBL_btn_hardware_self_test", "Hardware self test" ) );
        btn_verify_nvram.setText( getBundleText( "LBL_btn_verify_nvram", "Verify NVRAM" ) );
        
        /* ---------------------------------------------------------------------------- */
        cpt_security_test.setText( getBundleText( "LBL_cpt_security_test", "Security Test" ) );
        lbl_mcs_reset.setText( getBundleText( "LBL_lbl_mcs_reset", "MCS Reset" ) );
        btn_mcs_reset.setText( getBundleText( "LBL_send_command", "Send" ) );
        ebd_mcs_independent_mode.setText( getBundleText( "LBL_ebd_mcs_independent_mode", "Open MCEX" ) );
        btn_mcs_emer_stop.setText( getBundleText( "LBL_btn_mcs_emer_stop", "Emer Stop" ) );
        btn_mcs_pos_corr.setText( getBundleText( "LBL_btn_mcs_pos_corr", "Pos Corr" ) );
        lbl_buffer_test.setText( getBundleText( "LBL_lbl_buffer_test", "Buffer Test" ) );
        btn_buffer_test.setText( getBundleText( "LBL_send_command", "Send" ) );
        lbl_terminal_test.setText( getBundleText( "LBL_lbl_terminal_test", "Terminal Test" ) );
        btn_terminal_test.setText( getBundleText( "LBL_send_command", "Send" ) );
        lbl_ucmp_test.setText( getBundleText( "LBL_lbl_ucmp_test", "UCMP Test" ) );
        btn_ucmp_test.setText( getBundleText( "LBL_send_command", "Send" ) );
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setTextLabelStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setTextValueStyle ( JTextField c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setButtonStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }
    
    private void setComboBoxValueStyle ( JComboBox<?> c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }

    private void bindGroup ( final AbstractButton[] primary, final JComponent... list ) {
        for ( AbstractButton p : primary ) {
            p.addItemListener( new ItemListener() {
                public void itemStateChanged ( ItemEvent e ) {
                    boolean enabled = false;
                    for ( AbstractButton p : primary ) {
                        if ( p.isSelected() && p.isEnabled() ) {
                            enabled = true;
                            break;
                        }
                    }
                    for ( JComponent c : list )
                        c.setEnabled( enabled );
                }
            } );
            p.addPropertyChangeListener( "enabled", new PropertyChangeListener() {
                @Override
                public void propertyChange ( PropertyChangeEvent evt ) {
                    boolean enabled = false;
                    for ( AbstractButton p : primary ) {
                        if ( p.isSelected() && p.isEnabled() ) {
                            enabled = true;
                            break;
                        }
                    }
                    for ( JComponent c : list )
                        c.setEnabled( enabled );
                }
            } );
        }

        boolean enabled = false;
        for ( AbstractButton p : primary ) {
            if ( p.isSelected() && p.isEnabled() ) {
                enabled = true;
                break;
            }
        }
        for ( JComponent c : list )
            c.setEnabled( enabled );
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

    public static class HardwareBean {
        private Boolean installationMode;
        private Boolean temporaryDriverActivation;
        private Boolean suspendDcsAutomation;


        public Boolean getInstallationMode () {
            return this.installationMode;
        }


        public Boolean getTemporaryDriverActivation () {
            return this.temporaryDriverActivation;
        }


        public Boolean getSuspendDcsAutomation () {
            return this.suspendDcsAutomation;
        }


        public void setInstallationMode ( Boolean installationMode ) {
            this.installationMode = installationMode;
        }


        public void setTemporaryDriverActivation ( Boolean temporaryDriverActivation ) {
            this.temporaryDriverActivation = temporaryDriverActivation;
        }


        public void setSuspendDcsAutomation ( Boolean suspendDcsAutomation ) {
            this.suspendDcsAutomation = suspendDcsAutomation;
        }
    }


    public HardwareBean getHardwareBean () throws ConvertException {

        HardwareBean bean_hardware = new HardwareBean();
        bean_hardware.setInstallationMode( ebd_installation_mode.isSelected() );
        bean_hardware.setTemporaryDriverActivation( ebd_temporary_driver_activation.isSelected() );
        bean_hardware.setSuspendDcsAutomation( ebd_suspend_dcs_automation.isSelected() );
        return bean_hardware;
    }


    public void setHardwareBean ( HardwareBean bean_hardware ) {
        this.ebd_installation_mode.setOriginSelected( bean_hardware.getInstallationMode() != null && bean_hardware.getInstallationMode() == true );
        this.ebd_temporary_driver_activation.setOriginSelected( bean_hardware.getTemporaryDriverActivation() != null
                && bean_hardware.getTemporaryDriverActivation() == true );
       
        this.ebd_suspend_dcs_automation.setOriginSelected( bean_hardware.getSuspendDcsAutomation() != null
                && bean_hardware.getSuspendDcsAutomation() == true );
    }


    public static String getBundleText ( String key, String defaultValue ) {
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


    public static Commission createPanel ( SettingPanel<Commission> panel ) {
        Commission gui = new Commission();
        gui.setSettingPanel( panel );
        return gui;
    }


    public void actionPerformed ( final ActionEvent e ) {
        if ( e.getSource() == btn_close_rear_door ) {
            do_btn_close_rear_door_actionPerformed( e );
        }
        if ( e.getSource() == btn_open_rear_door ) {
            do_btn_open_rear_door_actionPerformed( e );
        }
        if ( e.getSource() == btn_close_front_door ) {
            do_btn_close_front_door_actionPerformed( e );
        }
        if ( e.getSource() == btn_open_front_door ) {
            do_btn_open_front_door_actionPerformed( e );
        }
        if ( e.getSource() == ebd_suspend_dcs_automation ) {
            do_ebd_suspend_dcs_automation_actionPerformed( e );
        }
        if ( e.getSource() == ebd_temporary_driver_activation ) {
            do_ebd_temporary_driver_activation_actionPerformed( e );
        }
        if ( e.getSource() == ebd_installation_mode ) {
            do_ebd_installation_mode_actionPerformed( e );
        }
        if ( e.getSource() == btn_set_calibrate_analog_output ) {
            do_btn_set_calibrate_analog_output_actionPerformed( e );
        }
        if ( e.getSource() == btn_verify_nvram ) {
            do_btn_verify_nvram_actionPerformed( e );
        }
        if ( e.getSource() == btn_hardware_self_test ) {
            do_btn_hardware_self_test_actionPerformed( e );
        }
        if( e.getSource() == btn_mcs_reset ) {
			if(  JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "Warning_desc" ), TEXT.getString( "Warning_title" ),
	                JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {
				if ( settingPanel instanceof CommissionSetting )
					( ( CommissionSetting )settingPanel ).reset_mcs();
				
				StartUI.getTopMain().push(HomePanel.build( DashboardPanel.class ));
			}
		}
		
		if( e.getSource() == ebd_mcs_independent_mode ) {
			if ( settingPanel instanceof CommissionSetting ) {
	        	final boolean selected = ebd_mcs_independent_mode.isSelected();
	            if(selected) {
	            	int ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "Mcex_enter_desc" ),
	                        TEXT.getString( "Mcex_title" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
	            	if ( ans == JOptionPane.YES_OPTION ) {
	                    ( ( CommissionSetting )settingPanel ).mcs_independent_mode( (byte)1 );
	            	}else {
	            		( ( CommissionSetting )settingPanel ).reset();
	            	}
	            	
	            }else {
	            	int ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "Mcex_exit_desc" ),
	                        TEXT.getString( "Mcex_title" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
	            	if ( ans == JOptionPane.YES_OPTION ) {
	            		( ( CommissionSetting )settingPanel ).mcs_independent_mode( (byte)0 );
	            	}else {
	            		( ( CommissionSetting )settingPanel ).reset();
	            	}
	            }
	        }
		}
		
		if( e.getSource() == btn_mcs_emer_stop ) {
			if ( settingPanel instanceof CommissionSetting ) {
				( ( CommissionSetting )settingPanel ).erem_stop();
			}
		}
		
		if( e.getSource() == btn_mcs_pos_corr ) {
			if ( settingPanel instanceof CommissionSetting ) {
				( ( CommissionSetting )settingPanel ).pos_corr();
			}
		}
		
		if( e.getSource() == btn_buffer_test ) {
			if ( settingPanel instanceof CommissionSetting ) {
				( ( CommissionSetting )settingPanel ).bumper_test( ((RunDirect)cbo_buffer_test.getSelectedItem()).getCode() );
			}
		}
		
		if( e.getSource() == btn_terminal_test ) {
			if ( settingPanel instanceof CommissionSetting ) {
				( ( CommissionSetting )settingPanel ).term_test( ((RunDirect)cbo_terminal_test.getSelectedItem()).getCode() );
			}
		}
		
		if( e.getSource() == btn_ucmp_test ) {
			if ( settingPanel instanceof CommissionSetting ) {
				( ( CommissionSetting )settingPanel ).ucmp_test( ((UcmpTest)cbo_ucmp_test.getSelectedItem()).getCode() );
			}
		}
    }


    protected void do_btn_hardware_self_test_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof CommissionSetting )
            ( ( CommissionSetting )settingPanel ).hardwareSelfTest();
    }


    protected void do_btn_verify_nvram_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof CommissionSetting )
            ( ( CommissionSetting )settingPanel ).verifyNVRAM();
    }


    protected void do_btn_set_calibrate_analog_output_actionPerformed ( final ActionEvent e ) {
        try {
            byte value = Byte.parseByte( txt_calibrate_analog_output.getText() );
            if ( settingPanel instanceof CommissionSetting )
                ( ( CommissionSetting )settingPanel ).setCalibrateAnalogOutput( value );
        } catch ( NumberFormatException ex ) {
            JOptionPane.showMessageDialog( StartUI.getFrame(), "number format!" );
        }
    }


    protected void do_ebd_installation_mode_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof CommissionSetting ) {
        	final boolean selected = ebd_installation_mode.isSelected();
            if(selected) {
            	int ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "Installation.CONTEXT" ),
                        TEXT.getString( "Installation.TITLE" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
            	if ( ans == JOptionPane.YES_OPTION ) {
                    ( ( CommissionSetting )settingPanel ).setInstallationMode(true);
            	}else {
            		( ( CommissionSetting )settingPanel ).reset();
            	}
            	
            }else {
            	int ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "UnInstallation.CONTEXT" ),
                        TEXT.getString( "Installation.TITLE" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
            	if ( ans == JOptionPane.YES_OPTION ) {
                    ( ( CommissionSetting )settingPanel ).setInstallationMode( false );
                    try {
						Thread.sleep(300);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                	( ( CommissionSetting )settingPanel ).setTemporaryDriverActivation( false );
            	}else {
            		( ( CommissionSetting )settingPanel ).reset();
            	}
            	
            }
        }
    }


    protected void do_ebd_temporary_driver_activation_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof CommissionSetting ) {
        	int ans = 0;
            final boolean selected = ebd_temporary_driver_activation.isSelected();
            if(selected) {
            	ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "TemporaryDriverActivation.CONTEXT" ),
                        TEXT.getString( "TemporaryDriverActivation.TITLE" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
            }else {
            	ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "UnTemporaryDriverActivation.CONTEXT" ),
                        TEXT.getString( "TemporaryDriverActivation.TITLE" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
            }
            
            if ( ans == JOptionPane.YES_OPTION ) {
                ( ( CommissionSetting )settingPanel ).setTemporaryDriverActivation( selected );
        	}else {
        		( ( CommissionSetting )settingPanel ).reset();
        	}

        }
    }


    protected void do_ebd_suspend_dcs_automation_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof CommissionSetting ) {
        	int ans = 0;
            final boolean selected = ebd_suspend_dcs_automation.isSelected();
            if(selected) {
            	ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "SuspendDCSAutomation.CONTEXT" ),
                        TEXT.getString( "TemporaryDriverActivation.TITLE" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
            }else {
            	ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "UnSuspendDCSAutomation.CONTEXT" ),
                        TEXT.getString( "TemporaryDriverActivation.TITLE" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
            }
            
            if ( ans == JOptionPane.YES_OPTION ) {
                ( ( CommissionSetting )settingPanel ).setSuspendDcsAutomation(selected);
        	}else {
        		( ( CommissionSetting )settingPanel ).reset();
        	}

        }
    }
    

    protected void do_btn_open_front_door_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof CommissionSetting ) {
            ( ( CommissionSetting )settingPanel ).sendDoorCommand( DoorCommand.OPEN_FRONT_DOOR );
        }
    }


    protected void do_btn_close_front_door_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof CommissionSetting ) {
            ( ( CommissionSetting )settingPanel ).sendDoorCommand( DoorCommand.CLOSE_FRONT_DOOR );
        }
    }


    protected void do_btn_open_rear_door_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof CommissionSetting ) {
            ( ( CommissionSetting )settingPanel ).sendDoorCommand( DoorCommand.OPEN_REAR_DOOR );
        }
    }


    protected void do_btn_close_rear_door_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof CommissionSetting ) {
            ( ( CommissionSetting )settingPanel ).sendDoorCommand( DoorCommand.CLOSE_REAR_DOOR );
        }
    }
    
    public void stateChanged ( final ChangeEvent e ) {
        if ( e.getSource() == ebd_installation_mode ) {
            do_ebd_installation_mode_stateChanged( e );
        }else if(e.getSource() == ebd_temporary_driver_activation ) {
        	do_ebd_temporary_driver_activation_stateChanged(e);
        }else if (e.getSource() == ebd_mcs_independent_mode ) {
			do_ebd_mcs_independent_mode_stateChanged(e);
		}
    }
    
    protected void do_ebd_mcs_independent_mode_stateChanged ( final ChangeEvent e ) {
        final boolean selected = ebd_mcs_independent_mode.isSelected();
        lbl_buffer_test.setEnabled( selected );
        cbo_buffer_test.setEnabled( selected );
        btn_buffer_test.setEnabled( selected );
        btn_mcs_emer_stop.setEnabled( selected );
        btn_mcs_pos_corr.setEnabled( selected );
        lbl_terminal_test.setEnabled( selected );
        cbo_terminal_test.setEnabled( selected );
        btn_terminal_test.setEnabled( selected );
        lbl_ucmp_test.setEnabled( selected );
        cbo_ucmp_test.setEnabled( selected );
        btn_ucmp_test.setEnabled( selected );
	}
    
    protected void do_ebd_installation_mode_stateChanged ( final ChangeEvent e ) {
        final boolean selected = ebd_installation_mode.isSelected();
        ebd_temporary_driver_activation.setEnabled( selected );
        if(!selected) {
        	lbl_calibrate_analog_output.setEnabled( selected );
            txt_calibrate_analog_output.setEnabled( selected );
            btn_set_calibrate_analog_output.setEnabled( selected );
        }
    }
    
    protected void do_ebd_temporary_driver_activation_stateChanged ( final ChangeEvent e ) {
        final boolean selected = ebd_temporary_driver_activation.isSelected();
        lbl_calibrate_analog_output.setEnabled( selected );
        txt_calibrate_analog_output.setEnabled( selected );
        btn_set_calibrate_analog_output.setEnabled( selected );
    }
    
    public void setMcexMode( DynamicStatus bean ) {
    	this.ebd_mcs_independent_mode.setOriginSelected(bean.getMcex() == 0x01);
    }
    
    public static class DynamicStatus{
    	private int  mcex;
    	
		public int getMcex() {
			return mcex;
		}
		public void setMcex(int mcex) {
			this.mcex = mcex;
		}
    }
    
    public enum RunDirect {
		DIRECT_UP((byte)1),
		DIRECT_DOWN((byte)0);
		
		private final byte code;
		
		private RunDirect(byte c){
			this.code = c;
		}
		
		public byte getCode () {
	        return this.code;
	    }
		
		public String toString () {
	        return TEXT.getString( name() );
	    }
	}
	
	public enum UcmpTest {
		BRAKING_FORCE_TEST((byte)0),
		ROPE_GRIPPER_TEST((byte)1);
		
		private final byte code;
		
		private UcmpTest(byte c){
			this.code = c;
		}
		
		public byte getCode () {
	        return this.code;
	    }
		
		public String toString () {
	        return TEXT.getString( name() );
	    }
	}
}