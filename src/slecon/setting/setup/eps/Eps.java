package slecon.setting.setup.eps;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.ValueTextField;
import slecon.home.PosButton;
import slecon.interfaces.ConvertException;
import slecon.setting.advanced.AdvancedSetting;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import comm.util.Endian;



public class Eps extends JPanel implements ActionListener {
    private static final long     serialVersionUID = 6584021027904307178L;
    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.eps.eps");
    private static final ImageIcon      INPUT_ON_ICON        = ImageFactory.BRIGHT_GREEN_BALL.icon( 16, 16 );
    private static final ImageIcon      INPUT_OFF_ICON       = ImageFactory.DULL_GREEN_BALL.icon( 16, 16 );
    private static final ImageIcon      INPUT_DISABLED_ICON  = ImageFactory.DULL_RED_BALL.icon( 16, 16 );
    private static final ImageIcon      OUTPUT_ON_ICON       = ImageFactory.BRIGHT_YELLOW_BALL.icon( 16, 16 );
    private static final ImageIcon      OUTPUT_OFF_ICON      = ImageFactory.DULL_YELLOW_BALL.icon( 16, 16 );
    private static final ImageIcon      OUTPUT_DISABLED_ICON = ImageFactory.DULL_RED_BALL.icon( 16, 16 );
    private static final ImageIcon      GRAY_ICON            = ImageFactory.GRAY_BALL.icon( 16, 16 );
    private static final ImageIcon      ON_ICON              = ImageFactory.BRIGHT_YELLOW_BALL.icon( 16, 16 );
    private static final ImageIcon      OFF_ICON             = ImageFactory.BRIGHT_RED_BALL.icon( 16, 16 );
    private static final ImageIcon      DISABLED_ICON        = ImageFactory.DULL_RED_BALL.icon( 16, 16 );
    
    /* ---------------------------------------------------------------------------- */
    private boolean            started = false;
    private SettingPanel<Eps> settingPanel;
    
    
    private JLabel	cpt_status;
    private JLabel	lbl_vbus_title;
    private JLabel	lbl_vbus_value;
    private JLabel	lbl_vbat_title;
    private JLabel	lbl_vbat_value;
    private JLabel	lbl_curr_title;
    private JLabel	lbl_curr_value;
    private JLabel	lbl_speed_title;
    private JLabel	lbl_speed_value;
    
    private HIPStatusBean hipStatusBean;
    private JPanel panel;
    private JLabel	lblup;
    private JLabel	lbldown;
    private JLabel	lblqeifa;
    private JLabel	lblvmax;
    private JLabel	lblbatlo;
    private JLabel	lbldzi;
    private JLabel	lblrbr;
    private JLabel	lblmck;
    private JLabel	lblmbi;
    private JLabel	lblmcr;
    private JLabel	lblrcr;
    private JLabel	lblmbr;
    private JLabel	lbln2r;
    private JLabel	lblips;
    private JLabel	lblbat;
    
    private JLabel	cpt_setup_param;
    private JLabel  lbl_encoder_direction;
    private ValueTextField  fmt_encoder_direction;
    private JLabel  lbl_encoder_pulse;
    private ValueTextField  fmt_encoder_pulse;
    private JLabel  lbl_tractor_wheel;
    private ValueTextField  fmt_tractor_wheel;
    private JLabel  lbl_suspend_count;
    private ValueTextField  fmt_suspend_count;
    private JLabel  lbl_exit_time;
    private ValueTextField  fmt_exit_time;
    private JLabel  lbl_change_time;
    private ValueTextField  fmt_change_time;
    private JLabel  lbl_normal_voltage;
    private ValueTextField  fmt_normal_voltage;
    private JLabel  lbl_lack_voltage;
    private ValueTextField  fmt_lack_voltage;
    private JLabel  lbl_full_voltage;
    private ValueTextField  fmt_full_voltage;
    private JLabel  lbl_low_voltage;
    private ValueTextField  fmt_low_voltage;
    private JLabel  lbl_protect_voltage;
    private ValueTextField  fmt_protect_voltage;
    private JLabel  lbl_charger_current;
    private ValueTextField  fmt_charger_current;
    private JLabel  lbl_open_voltage;
    private ValueTextField  fmt_open_voltage;
    private JLabel  lbl_reduced_voltage;
    private ValueTextField  fmt_reduced_voltage;
    private JLabel  lbl_normal_power_hz;
    private ValueTextField  fmt_normal_power_hz;
    private JLabel  lbl_inverter_power_hz;
    private ValueTextField  fmt_inverter_power_hz;
    private JLabel  lbl_brake_open_time;
    private ValueTextField  fmt_brake_open_time;
    private PosButton  btn_default_param;
    
    protected enum Type { INPUT, OUTPUT, }
    
    public static enum OnOffStatus {
        ON, OFF, DISABLED;
    }
    
    private ImageIcon onoffstatusToIcon ( Type io, OnOffStatus status ) {
        if ( status == null || io == null )
            return GRAY_ICON;
        switch ( status ) {
        case ON :
            return io == Type.INPUT
                   ? INPUT_ON_ICON
                   : OUTPUT_ON_ICON;
        case OFF :
            return io == Type.INPUT
                   ? INPUT_OFF_ICON
                   : OUTPUT_OFF_ICON;
        case DISABLED :
        default :
            return GRAY_ICON;
        }
    }
    
    public void setHipStatusBean ( HIPStatusBean bean ) {
        if ( bean == null )
            bean = new HIPStatusBean();
        hipStatusBean = bean;
        updateHIPStatus();
    }
    
    private void updateHIPStatus () {
    	lblup.setIcon( onoffstatusToIcon( Type.INPUT, hipStatusBean.getUp() ) );
    	lbldown.setIcon( onoffstatusToIcon( Type.INPUT, hipStatusBean.getDown() ) );
    	lblqeifa.setIcon( onoffstatusToIcon( Type.INPUT, hipStatusBean.getQeifa() ) );
    	lblvmax.setIcon( onoffstatusToIcon( Type.INPUT, hipStatusBean.getVmax() ) );
    	lblbatlo.setIcon( onoffstatusToIcon( Type.INPUT, hipStatusBean.getBatlo() ) );
    	lbldzi.setIcon( onoffstatusToIcon( Type.INPUT, hipStatusBean.getDzi() ) );
    	lblrbr.setIcon( onoffstatusToIcon( Type.INPUT, hipStatusBean.getRbr() ) );
    	lblmck.setIcon( onoffstatusToIcon( Type.INPUT, hipStatusBean.getMck() ) );
    	lblmbi.setIcon( onoffstatusToIcon( Type.INPUT, hipStatusBean.getMbi() ) );
    	lblmcr.setIcon( onoffstatusToIcon( Type.OUTPUT, hipStatusBean.getMcr() ) );
    	lblrcr.setIcon( onoffstatusToIcon( Type.OUTPUT, hipStatusBean.getRcr() ) );
    	lblmbr.setIcon( onoffstatusToIcon( Type.OUTPUT, hipStatusBean.getMbr() ) );
    	lbln2r.setIcon( onoffstatusToIcon( Type.OUTPUT, hipStatusBean.getN2r() ) );
    	lblips.setIcon( onoffstatusToIcon( Type.OUTPUT, hipStatusBean.getIps() ) );
    	lblbat.setIcon( onoffstatusToIcon( Type.OUTPUT, hipStatusBean.getBat() ) );
    }
    public Eps () {
        initGUI();
    }

    public void setSettingPanel ( SettingPanel<Eps> panel ) {
        this.settingPanel = panel;
    }

    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 24, gap 0 6", "[30::30][30::30][150::150][150::150][]" ) );
        
        cpt_status     = new JLabel();
        lbl_vbus_title = new JLabel();
        lbl_vbus_value = new JLabel();
        lbl_vbat_title = new JLabel();
        lbl_vbat_value = new JLabel();
        lbl_curr_title = new JLabel();
        lbl_curr_value = new JLabel();
        lbl_speed_title = new JLabel();
        lbl_speed_value = new JLabel();
        
        setCaptionStyle( cpt_status );
        setTextLabelStyle(lbl_vbus_title);
        setTextLabelStyle(lbl_vbus_value);
        setTextLabelStyle(lbl_vbat_title);
        setTextLabelStyle(lbl_vbat_value);
        setTextLabelStyle(lbl_curr_title);
        setTextLabelStyle(lbl_curr_value);
        setTextLabelStyle(lbl_speed_title);
        setTextLabelStyle(lbl_speed_value);
        
        add( cpt_status, "gapbottom 18-12, span, aligny center" );
        add( lbl_vbus_title, "skip 1, span, split, gapright 12");
        add( lbl_vbus_value, "wrap, top");
        add( lbl_vbat_title, "skip 1, span, split, gapright 12");
        add( lbl_vbat_value, "wrap, top");
        add( lbl_curr_title, "skip 1, span, split, gapright 12");
        add( lbl_curr_value, "wrap, top");
        add( lbl_speed_title, "skip 1, span, split, gapright 12");
        add( lbl_speed_value, "right, wrap 30, top");
        
        /*----------------------------------------------------------------------------------------*/
        panel = new JPanel();
        panel.setBackground( StartUI.SUB_BACKGROUND_COLOR );
        panel.setBorder( new TitledBorder( new LineBorder( StartUI.BORDER_COLOR ), getBundleText( "LBL_cpt_hip_status", "HIP Status" ), TitledBorder.LEADING,
                                           TitledBorder.TOP, null, Color.white ) );
        add( panel, "gapbottom 18-12, span, aligny center" );
        panel.setLayout( new MigLayout( "ins 10", "[10px][]", "[10px,grow]" ) );

        JPanel hip_status_panel = new JPanel();
        hip_status_panel.setBackground( StartUI.SUB_BACKGROUND_COLOR );
        panel.add( hip_status_panel, "cell 0 0,alignx left,aligny top" );
        hip_status_panel.setLayout( new MigLayout( "", "[100px:100px:100px][100px:100px:100px][100px:100px:100px][100px:100px:100px][]",
                                                        "[][]" ) );
        lblup = new JLabel( "UP" );
        lblup.setForeground(Color.WHITE);
        hip_status_panel.add( lblup, "flowy,cell 0 1,growx,aligny top,gapy 5" );
        lbldown = new JLabel( "DOWN" );
        lbldown.setForeground(Color.WHITE);
        hip_status_panel.add( lbldown, "cell 0 1,growx,gapy 5" );
        lblqeifa = new JLabel( "QEIFA" );
        lblqeifa.setForeground(Color.WHITE);
        hip_status_panel.add( lblqeifa, "flowy,cell 0 1,growx,aligny top,gapy 5" );
        lblvmax = new JLabel( "VMAX" );
        lblvmax.setForeground(Color.WHITE);
        hip_status_panel.add( lblvmax, "cell 0 1,growx,gapy 5" );
        
        lblbatlo = new JLabel("BATLO");
        lblbatlo.setForeground(Color.WHITE);
        hip_status_panel.add(lblbatlo,"flowy,cell 1 1,growx,aligny top,gapy 5");
        lbldzi = new JLabel( "DZI" );
        lbldzi.setForeground(Color.WHITE);
        hip_status_panel.add( lbldzi, "cell 1 1,growx,gapy 5" );
        lblrbr = new JLabel( "RBR" );
        lblrbr.setForeground(Color.WHITE);
        hip_status_panel.add( lblrbr, "flowy,cell 1 1,growx,aligny top,gapy 5" );
        lblmck = new JLabel( "MCK" );
        lblmck.setForeground(Color.WHITE);
        hip_status_panel.add( lblmck, "cell 1 1,growx,gapy 5" );
        
        lblmbi = new JLabel("MBI");
        lblmbi.setForeground(Color.WHITE);
        hip_status_panel.add(lblmbi,"flowy,cell 2 1,growx,aligny top,gapy 5");
        lblmcr = new JLabel( "MCR" );
        lblmcr.setForeground(Color.WHITE);
        hip_status_panel.add( lblmcr, "cell 2 1,growx,gapy 5" );
        lblrcr = new JLabel( "RCR" );
        lblrcr.setForeground(Color.WHITE);
        hip_status_panel.add( lblrcr, "flowy,cell 2 1,growx,aligny top,gapy 5" );
        lblmbr = new JLabel( "MBR" );
        lblmbr.setForeground(Color.WHITE);
        hip_status_panel.add( lblmbr, "cell 2 1,growx,gapy 5" );
        
        lbln2r = new JLabel("N2R");
        lbln2r.setForeground(Color.WHITE);
        hip_status_panel.add(lbln2r,"flowy,cell 3 1,growx,aligny top,gapy 5");
        lblips = new JLabel( "IPS" );
        lblips.setForeground(Color.WHITE);
        hip_status_panel.add( lblips, "cell 3 1,growx,gapy 5" );
        lblbat = new JLabel( "BAT" );
        lblbat.setForeground(Color.WHITE);
        hip_status_panel.add( lblbat, "cell 3 1,growx,gapy 5" );
        
        
        /*----------------------------------------------------------------------------------------*/
        cpt_setup_param     = new JLabel();
        lbl_encoder_direction = new JLabel();
        fmt_encoder_direction = new ValueTextField();
        lbl_encoder_pulse = new JLabel();
        fmt_encoder_pulse = new ValueTextField();
        lbl_tractor_wheel = new JLabel();
        fmt_tractor_wheel = new ValueTextField();
        lbl_suspend_count = new JLabel();
        fmt_suspend_count = new ValueTextField();
        lbl_exit_time = new JLabel();
        fmt_exit_time = new ValueTextField();
        lbl_change_time = new JLabel();
        fmt_change_time = new ValueTextField();
        lbl_normal_voltage = new JLabel();
        fmt_normal_voltage = new ValueTextField();
        lbl_lack_voltage = new JLabel();
        fmt_lack_voltage = new ValueTextField();
        lbl_full_voltage = new JLabel();
        fmt_full_voltage = new ValueTextField();
        lbl_low_voltage = new JLabel();
        fmt_low_voltage = new ValueTextField();
        lbl_protect_voltage = new JLabel();
        fmt_protect_voltage = new ValueTextField();
        lbl_charger_current = new JLabel();
        fmt_charger_current = new ValueTextField();
        lbl_open_voltage = new JLabel();
        fmt_open_voltage = new ValueTextField();
        lbl_reduced_voltage = new JLabel();
        fmt_reduced_voltage = new ValueTextField();
        lbl_normal_power_hz = new JLabel();
        fmt_normal_power_hz = new ValueTextField();
        lbl_inverter_power_hz = new JLabel();
        fmt_inverter_power_hz = new ValueTextField();
        lbl_brake_open_time = new JLabel();
        fmt_brake_open_time = new ValueTextField();
        
        if(BaseFactory.getLocaleString().equals("en"))
        	btn_default_param = new PosButton(ImageFactory.BUTTON_PAUSE.icon(120, 30),
        									  ImageFactory.BUTTON_START.icon(120, 30));
        else
        	btn_default_param = new PosButton(ImageFactory.BUTTON_PAUSE.icon(87, 30),
        									  ImageFactory.BUTTON_START.icon(87, 30));
        btn_default_param.addActionListener(this);
        
        
        fmt_encoder_direction.setColumns(5);
        fmt_encoder_direction.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_encoder_direction.setScope(Integer.class, 0, 1, true, true);
        fmt_encoder_direction.setEmptyValue(0);
        
        fmt_encoder_pulse.setColumns(5);
        fmt_encoder_pulse.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_encoder_pulse.setScope(Integer.class, 0, 4096, true, true);
        fmt_encoder_pulse.setEmptyValue(0);
        
        fmt_tractor_wheel.setColumns(5);
        fmt_tractor_wheel.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_tractor_wheel.setScope(Integer.class, 320, 1000, true, true);
        fmt_tractor_wheel.setEmptyValue(0);
        
        fmt_suspend_count.setColumns(5);
        fmt_suspend_count.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_suspend_count.setScope(Integer.class, 1, 4, true, true);
        fmt_suspend_count.setEmptyValue(0);
        
        fmt_exit_time.setColumns(5);
        fmt_exit_time.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_exit_time.setScope(Integer.class, 10, 1800, true, true);
        fmt_exit_time.setEmptyValue(0);
        
        fmt_change_time.setColumns(5);
        fmt_change_time.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_change_time.setScope(Integer.class, 3, 60, true, true);
        fmt_change_time.setEmptyValue(0);
        
        fmt_normal_voltage.setColumns(5);
        fmt_normal_voltage.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_normal_voltage.setScope(Integer.class, 10, 40, true, true);
        fmt_normal_voltage.setEmptyValue(0);
        
        fmt_lack_voltage.setColumns(5);
        fmt_lack_voltage.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_lack_voltage.setScope(Integer.class, 0, 15, true, true);
        fmt_lack_voltage.setEmptyValue(0);
        
        fmt_full_voltage.setColumns(5);
        fmt_full_voltage.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_full_voltage.setScope(Integer.class, 0, 30, true, true);
        fmt_full_voltage.setEmptyValue(0);
        
        fmt_low_voltage.setColumns(5);
        fmt_low_voltage.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_low_voltage.setScope(Integer.class, 0, 26, true, true);
        fmt_low_voltage.setEmptyValue(0);
        
        fmt_protect_voltage.setColumns(5);
        fmt_protect_voltage.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_protect_voltage.setScope(Integer.class, 18, 24, true, true);
        fmt_protect_voltage.setEmptyValue(0);
        
        fmt_charger_current.setColumns(5);
        fmt_charger_current.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_charger_current.setScope(Integer.class, 0, 1500, true, true);
        fmt_charger_current.setEmptyValue(0);
        
        fmt_open_voltage.setColumns(5);
        fmt_open_voltage.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_open_voltage.setScope(Integer.class, 0, 95, true, true);
        fmt_open_voltage.setEmptyValue(0);
        
        fmt_reduced_voltage.setColumns(5);
        fmt_reduced_voltage.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_reduced_voltage.setScope(Integer.class, 0, 95, true, true);
        fmt_reduced_voltage.setEmptyValue(0);
        
        fmt_normal_power_hz.setColumns(5);
        fmt_normal_power_hz.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_normal_power_hz.setScope(Integer.class, 50, 60, true, true);
        fmt_normal_power_hz.setEmptyValue(0);
        
        fmt_inverter_power_hz.setColumns(5);
        fmt_inverter_power_hz.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_inverter_power_hz.setScope(Integer.class, 50, 60, true, true);
        fmt_inverter_power_hz.setEmptyValue(0);
        
        fmt_brake_open_time.setColumns(5);
        fmt_brake_open_time.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_brake_open_time.setScope(Integer.class, 1, 5, true, true);
        fmt_brake_open_time.setEmptyValue(0);
        
        setCaptionStyle( cpt_setup_param );
        setTextLabelStyle( lbl_encoder_direction );
        setTextLabelStyle( lbl_encoder_pulse );
        setTextLabelStyle( lbl_tractor_wheel );
        setTextLabelStyle( lbl_suspend_count );
        setTextLabelStyle( lbl_exit_time );
        setTextLabelStyle( lbl_change_time );
        setTextLabelStyle( lbl_normal_voltage );
        setTextLabelStyle( lbl_lack_voltage );
        setTextLabelStyle( lbl_full_voltage );
        setTextLabelStyle( lbl_low_voltage );
        setTextLabelStyle( lbl_protect_voltage );
        setTextLabelStyle( lbl_charger_current );
        setTextLabelStyle( lbl_open_voltage );
        setTextLabelStyle( lbl_reduced_voltage );
        setTextLabelStyle( lbl_normal_power_hz );
        setTextLabelStyle( lbl_inverter_power_hz );
        setTextLabelStyle( lbl_brake_open_time );
        setButtonStyle( btn_default_param );
        
        add( cpt_setup_param, "gapbottom 30-20, span, aligny center" );

        Box vbox_title = Box.createVerticalBox();
        vbox_title.add( lbl_encoder_direction);
        vbox_title.add( Box.createVerticalStrut(17));
        vbox_title.add( lbl_tractor_wheel);
        vbox_title.add( Box.createVerticalStrut(17));
        vbox_title.add( lbl_exit_time);
        vbox_title.add( Box.createVerticalStrut(17));
        vbox_title.add( lbl_normal_voltage);
        vbox_title.add( Box.createVerticalStrut(17));
        vbox_title.add( lbl_full_voltage);
        vbox_title.add( Box.createVerticalStrut(17));
        vbox_title.add( lbl_protect_voltage);
        vbox_title.add( Box.createVerticalStrut(17));
        vbox_title.add( lbl_open_voltage);
        vbox_title.add( Box.createVerticalStrut(17));
        vbox_title.add( lbl_normal_power_hz);
        vbox_title.add( Box.createVerticalStrut(17));
        vbox_title.add( lbl_brake_open_time);
        
        Box vbox_value = Box.createVerticalBox();
        vbox_value.add( fmt_encoder_direction );
        vbox_value.add( Box.createVerticalStrut(15));
        vbox_value.add( fmt_tractor_wheel );
        vbox_value.add( Box.createVerticalStrut(15));
        vbox_value.add( fmt_exit_time );
        vbox_value.add( Box.createVerticalStrut(15));
        vbox_value.add( fmt_normal_voltage );
        vbox_value.add( Box.createVerticalStrut(15));
        vbox_value.add( fmt_full_voltage );
        vbox_value.add( Box.createVerticalStrut(15));
        vbox_value.add( fmt_protect_voltage );
        vbox_value.add( Box.createVerticalStrut(15));
        vbox_value.add( fmt_open_voltage );
        vbox_value.add( Box.createVerticalStrut(15));
        vbox_value.add( fmt_normal_power_hz );
        vbox_value.add( Box.createVerticalStrut(15));
        vbox_value.add( fmt_brake_open_time );
        
        Box vbox_title1 = Box.createVerticalBox();
        vbox_title1.add( lbl_encoder_pulse);
        vbox_title1.add( Box.createVerticalStrut(17));
        vbox_title1.add( lbl_suspend_count);
        vbox_title1.add( Box.createVerticalStrut(17));
        vbox_title1.add( lbl_change_time);
        vbox_title1.add( Box.createVerticalStrut(17));
        vbox_title1.add( lbl_lack_voltage);
        vbox_title1.add( Box.createVerticalStrut(17));
        vbox_title1.add( lbl_low_voltage);
        vbox_title1.add( Box.createVerticalStrut(17));
        vbox_title1.add( lbl_charger_current);
        vbox_title1.add( Box.createVerticalStrut(17));
        vbox_title1.add( lbl_reduced_voltage);
        vbox_title1.add( Box.createVerticalStrut(17));
        vbox_title1.add( lbl_inverter_power_hz);
        vbox_title1.add( Box.createVerticalStrut(12));
        vbox_title1.add( btn_default_param);
        
        Box vbox_value2 = Box.createVerticalBox();
        vbox_value2.add( fmt_encoder_pulse );
        vbox_value2.add( Box.createVerticalStrut(15));
        vbox_value2.add( fmt_suspend_count );
        vbox_value2.add( Box.createVerticalStrut(15));
        vbox_value2.add( fmt_change_time );
        vbox_value2.add( Box.createVerticalStrut(15));
        vbox_value2.add( fmt_lack_voltage );
        vbox_value2.add( Box.createVerticalStrut(15));
        vbox_value2.add( fmt_low_voltage );
        vbox_value2.add( Box.createVerticalStrut(15));
        vbox_value2.add( fmt_charger_current );
        vbox_value2.add( Box.createVerticalStrut(15));
        vbox_value2.add( fmt_reduced_voltage );
        vbox_value2.add( Box.createVerticalStrut(15));
        vbox_value2.add( fmt_inverter_power_hz );

        add(vbox_title, "skip 2, span, split, gapright 30, top");
        add(vbox_value, "split, gapright 70, top");
        add(vbox_title1, "split, gapright 30, top");
        add(vbox_value2, "wrap 30, top");
        
        bindGroup("lbl_encoder_direction",lbl_encoder_direction,fmt_encoder_direction);
        bindGroup("lbl_encoder_pulse",lbl_encoder_pulse,fmt_encoder_pulse);
        bindGroup("lbl_tractor_wheel",lbl_tractor_wheel,fmt_tractor_wheel);
        bindGroup("lbl_exit_time",lbl_exit_time,fmt_exit_time);
        bindGroup("lbl_change_time",lbl_change_time,fmt_change_time);
        bindGroup("lbl_normal_voltage",lbl_normal_voltage,fmt_normal_voltage);
        bindGroup("lbl_lack_voltage",lbl_lack_voltage,fmt_lack_voltage);
        bindGroup("lbl_full_voltage",lbl_full_voltage,fmt_full_voltage);
        bindGroup("lbl_low_voltage",lbl_low_voltage,fmt_low_voltage);
        bindGroup("lbl_protect_voltage",lbl_protect_voltage,fmt_protect_voltage);
        bindGroup("lbl_charger_current",lbl_charger_current,fmt_charger_current);
        bindGroup("lbl_open_voltage",lbl_open_voltage,fmt_open_voltage);
        bindGroup("lbl_reduced_voltage",lbl_reduced_voltage,fmt_reduced_voltage);
        bindGroup("lbl_normal_power_hz",lbl_normal_power_hz,fmt_normal_power_hz);
        bindGroup("lbl_inverter_power_hz",lbl_inverter_power_hz,fmt_inverter_power_hz);
        bindGroup("lbl_brake_open_time",lbl_brake_open_time,fmt_brake_open_time);
        
        SetWidgetEnable(false);
        loadI18N();
        revalidate();
    }
    
    private void loadI18N () {
    	cpt_status.setText(getBundleText( "LBL_cpt_status", "Inspection Status" ));
    	lbl_vbus_title.setText(getBundleText( "LBL_lbl_vbus_title", "VBUS Voltage" ));
    	lbl_vbus_value.setText("0 V");
    	lbl_vbat_title.setText(getBundleText( "LBL_lbl_vbat_title", "VBAT Voltage" ));
    	lbl_vbat_value.setText("0 V");
    	lbl_curr_title.setText(getBundleText( "LBL_lbl_curr_title", "VBUS Voltage" ));
    	lbl_curr_value.setText("0 V");
    	lbl_speed_title.setText(getBundleText( "LBL_lbl_speed_title", "VBUS Voltage" ));
    	lbl_speed_value.setText("0 m/s");
    	
    	cpt_setup_param.setText( getBundleText( "LBL_cpt_setup_param", "Setup Param" ) );
    	lbl_encoder_direction.setText( getBundleText( "LBL_lbl_encoder_direction", "Encoder Direction" ) );
    	lbl_encoder_pulse.setText( getBundleText( "LBL_lbl_encoder_pulse", "Encoder pulse" ) );
    	lbl_tractor_wheel.setText( getBundleText( "LBL_lbl_tractor_wheel", "Tractor Wheel Dia" ) );
    	lbl_suspend_count.setText( getBundleText( "LBL_lbl_suspend_count", "Suspend Count" ) );
    	lbl_exit_time.setText( getBundleText( "LBL_lbl_exit_time", "Exit Time" ) );
    	lbl_change_time.setText( getBundleText( "LBL_lbl_change_time", "Power Change Time" ) );
    	lbl_normal_voltage.setText( getBundleText( "LBL_lbl_normal_voltage", "Normal Voltage" ) );
    	lbl_lack_voltage.setText( getBundleText( "LBL_lbl_lack_voltage", "Lack Voltage" ) );
    	lbl_full_voltage.setText( getBundleText( "LBL_lbl_full_voltage", "Full Battery Voltage" ) );
    	lbl_low_voltage.setText( getBundleText( "LBL_lbl_low_voltage", "Low Battery Voltage" ) );
    	lbl_protect_voltage.setText( getBundleText( "LBL_lbl_protect_voltage", "Protect Voltage" ) );
    	lbl_charger_current.setText( getBundleText( "LBL_lbl_charger_current", "Charger Current" ) );
    	lbl_open_voltage.setText( getBundleText( "LBL_lbl_open_voltage", "Open Voltage" ) );
    	lbl_reduced_voltage.setText( getBundleText( "LBL_lbl_reduced_voltage", "Reduced Voltage" ) );
    	lbl_normal_power_hz.setText( getBundleText( "LBL_lbl_normal_power_hz", "Normal Power HZ" ) );
    	lbl_inverter_power_hz.setText( getBundleText( "LBL_lbl_inverter_power_hz", "Inverter Power HZ" ) );
    	lbl_brake_open_time.setText( getBundleText( "LBL_lbl_brake_open_time", "Inverter Power HZ" ) );
    	btn_default_param.setText( getBundleText( "LBL_btn_default_param", "Default Param" ) );
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }
    
    private void setTextLabelStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }
    
    private void setButtonStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
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
    
    public void SetWidgetEnable(boolean enable) {
    	cpt_status.setEnabled(enable);
    	lbl_vbus_title.setEnabled(enable);
    	lbl_vbus_value.setEnabled(enable);
        lbl_vbat_title.setEnabled(enable);
        lbl_vbat_value.setEnabled(enable);
        lbl_curr_title.setEnabled(enable);
        lbl_curr_value.setEnabled(enable);
        lbl_speed_title.setEnabled(enable);
        lbl_speed_value.setEnabled(enable);
        panel.setEnabled(enable);
        cpt_setup_param.setEnabled(enable);
        lbl_encoder_direction.setEnabled(enable);
        fmt_encoder_direction.setEnabled(enable);
        lbl_encoder_pulse.setEnabled(enable);
        fmt_encoder_pulse.setEnabled(enable);
        lbl_tractor_wheel.setEnabled(enable);
        fmt_tractor_wheel.setEnabled(enable);
        lbl_suspend_count.setEnabled(enable);
        fmt_suspend_count.setEnabled(enable);
        lbl_exit_time.setEnabled(enable);
        fmt_exit_time.setEnabled(enable);
        lbl_change_time.setEnabled(enable);
        fmt_change_time.setEnabled(enable);
        lbl_normal_voltage.setEnabled(enable);
        fmt_normal_voltage.setEnabled(enable);
        lbl_lack_voltage.setEnabled(enable);
        fmt_lack_voltage.setEnabled(enable);
        lbl_full_voltage.setEnabled(enable);
        fmt_full_voltage.setEnabled(enable);
        lbl_low_voltage.setEnabled(enable);
        fmt_low_voltage.setEnabled(enable);
        lbl_protect_voltage.setEnabled(enable);
        fmt_protect_voltage.setEnabled(enable);
        lbl_charger_current.setEnabled(enable);
        fmt_charger_current.setEnabled(enable);
        lbl_open_voltage.setEnabled(enable);
        fmt_open_voltage.setEnabled(enable);
        lbl_reduced_voltage.setEnabled(enable);
        fmt_reduced_voltage.setEnabled(enable);
        lbl_normal_power_hz.setEnabled(enable);
        fmt_normal_power_hz.setEnabled(enable);
        lbl_inverter_power_hz.setEnabled(enable);
        fmt_inverter_power_hz.setEnabled(enable);
        lbl_brake_open_time.setEnabled(enable);
        fmt_brake_open_time.setEnabled(enable);
        btn_default_param.setEnabled(enable);
    }
    
    public static Eps createPanel ( SettingPanel<Eps> panel ) {
        Eps gui = new Eps();
        gui.setSettingPanel( panel );
        return gui;
    }
    
    private void bindGroup ( final String detailKey, final JComponent... list ) {
        for ( JComponent c : list ) {
            c.addMouseListener( new MouseAdapter() {
                @Override
                public synchronized void mouseEntered ( MouseEvent evt ) {
                    if ( settingPanel != null )
                        settingPanel.setDescription( TEXT.getString( detailKey + "_description" ) );
                }
                @Override
                public void mouseExited ( MouseEvent e ) {
                    if ( settingPanel != null )
                        settingPanel.setDescription( null );
                }
            } );
        }
    }
    
    
    public class HIPStatusBean{
    	private OnOffStatus       up = OnOffStatus.DISABLED;
    	private OnOffStatus       down = OnOffStatus.DISABLED;
    	private OnOffStatus       qeifa = OnOffStatus.DISABLED;
    	private OnOffStatus       vmax = OnOffStatus.DISABLED;
    	private OnOffStatus       batlo = OnOffStatus.DISABLED;
    	private OnOffStatus       dzi = OnOffStatus.DISABLED;
    	private OnOffStatus       rbr = OnOffStatus.DISABLED;
    	private OnOffStatus       mck = OnOffStatus.DISABLED;
    	private OnOffStatus       mbi = OnOffStatus.DISABLED;
    	private OnOffStatus       mcr = OnOffStatus.DISABLED;
    	private OnOffStatus       rcr = OnOffStatus.DISABLED;
    	private OnOffStatus       mbr = OnOffStatus.DISABLED;
    	private OnOffStatus       n2r = OnOffStatus.DISABLED;
    	private OnOffStatus       ips = OnOffStatus.DISABLED;
    	private OnOffStatus       bat = OnOffStatus.DISABLED;
    	
		public OnOffStatus getUp() {
			return up;
		}
		public void setUp(OnOffStatus up) {
			this.up = up;
		}
		public OnOffStatus getDown() {
			return down;
		}
		public void setDown(OnOffStatus down) {
			this.down = down;
		}
		public OnOffStatus getQeifa() {
			return qeifa;
		}
		public void setQeifa(OnOffStatus qeifa) {
			this.qeifa = qeifa;
		}
		public OnOffStatus getVmax() {
			return vmax;
		}
		public void setVmax(OnOffStatus vmax) {
			this.vmax = vmax;
		}
		public OnOffStatus getBatlo() {
			return batlo;
		}
		public void setBatlo(OnOffStatus batlo) {
			this.batlo = batlo;
		}
		public OnOffStatus getDzi() {
			return dzi;
		}
		public void setDzi(OnOffStatus dzi) {
			this.dzi = dzi;
		}
		public OnOffStatus getRbr() {
			return rbr;
		}
		public void setRbr(OnOffStatus rbr) {
			this.rbr = rbr;
		}
		public OnOffStatus getMck() {
			return mck;
		}
		public void setMck(OnOffStatus mck) {
			this.mck = mck;
		}
		public OnOffStatus getMbi() {
			return mbi;
		}
		public void setMbi(OnOffStatus mbi) {
			this.mbi = mbi;
		}
		public OnOffStatus getMcr() {
			return mcr;
		}
		public void setMcr(OnOffStatus mcr) {
			this.mcr = mcr;
		}
		public OnOffStatus getRcr() {
			return rcr;
		}
		public void setRcr(OnOffStatus rcr) {
			this.rcr = rcr;
		}
		public OnOffStatus getMbr() {
			return mbr;
		}
		public void setMbr(OnOffStatus mbr) {
			this.mbr = mbr;
		}
		public OnOffStatus getN2r() {
			return n2r;
		}
		public void setN2r(OnOffStatus n2r) {
			this.n2r = n2r;
		}
		public OnOffStatus getIps() {
			return ips;
		}
		public void setIps(OnOffStatus ips) {
			this.ips = ips;
		}
		public OnOffStatus getBat() {
			return bat;
		}
		public void setBat(OnOffStatus bat) {
			this.bat = bat;
		}
    }
    
    public void setEPSBean(EPSBEAN bean){
    	HIPStatusBean hipBean = new HIPStatusBean();
    	StatusBean statusBean = new StatusBean();
    	EPSBEAN epsBean;
		try {
			epsBean = bean.clone();
			statusBean.setVbus( Endian.getFloat( epsBean.getVbus(), 0 ));
	    	statusBean.setVbat( Endian.getFloat( epsBean.getVbat(), 0 ));
	    	statusBean.setCurr( Endian.getFloat( epsBean.getCurr(), 0 ));
	    	statusBean.setSpeed( Endian.getFloat( epsBean.getSpeed(), 0 ));
	    	
			hipBean.setUp((epsBean.getHip()[0] & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setDown((epsBean.getHip()[0] >> 1 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setQeifa((epsBean.getHip()[0] >> 2 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setVmax((epsBean.getHip()[0] >> 3 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setBatlo((epsBean.getHip()[0] >> 4 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setDzi((epsBean.getHip()[0] >> 5 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setRbr((epsBean.getHip()[0] >> 6 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setMck((epsBean.getHip()[0] >> 7 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setMbi((epsBean.getHip()[1] & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setMcr((epsBean.getHip()[1] >> 1 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setRcr((epsBean.getHip()[1] >> 2 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setMbr((epsBean.getHip()[1] >> 3 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setN2r((epsBean.getHip()[1] >> 4 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setIps((epsBean.getHip()[1] >> 5 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	hipBean.setBat((epsBean.getHip()[1] >> 6 & 0x01) == 1 ? OnOffStatus.ON:OnOffStatus.OFF );
	    	
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	setHipStatusBean(hipBean);
    	setStatusBean(statusBean);
    }
    
    public static class EPSBEAN{
    	private byte[] hip;
    	private byte[] vbus;
    	private byte[] vbat;
    	private byte[] curr;
    	private byte[] speed;

    	public byte[] getHip() {
			return hip;
		}
		public void setHip(byte[] hip) {
			this.hip = hip;
		}
		public byte[] getVbus() {
			return vbus;
		}
		public void setVbus(byte[] vbus) {
			this.vbus = vbus;
		}
		public byte[] getVbat() {
			return vbat;
		}
		public void setVbat(byte[] vbat) {
			this.vbat = vbat;
		}
		public byte[] getCurr() {
			return curr;
		}
		public void setCurr(byte[] curr) {
			this.curr = curr;
		}
		public byte[] getSpeed() {
			return speed;
		}
		public void setSpeed(byte[] speed) {
			this.speed = speed;
		}

		@Override
		protected EPSBEAN clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			EPSBEAN bean = new EPSBEAN();
			bean.setHip(Arrays.copyOf(hip, hip.length));
			bean.setVbus(Arrays.copyOf(vbus, vbus.length));
			bean.setVbat(Arrays.copyOf(vbat, vbat.length));
			bean.setCurr(Arrays.copyOf(curr, curr.length));
			bean.setSpeed(Arrays.copyOf(speed, speed.length));
			return bean;
		}
    }
    
    public void setStatusBean(StatusBean statusBean) {
    	this.lbl_vbus_value.setText(String.format("%.2f V",statusBean.vbus));
    	this.lbl_vbat_value.setText(String.format("%.2f V",statusBean.vbat));
    	this.lbl_curr_value.setText(String.format("%.2f mA",statusBean.curr));
    	this.lbl_speed_value.setText(String.format("%.2f m/s",statusBean.speed));
    }
    
    public void setSetupParam(SetupParamBean setupParamBean) {
    	this.fmt_encoder_direction.setOriginValue(setupParamBean.getDirect());
    	this.fmt_encoder_pulse.setOriginValue(setupParamBean.getPulse());
    	this.fmt_tractor_wheel.setOriginValue(setupParamBean.getTractor_wheel());
    	this.fmt_suspend_count.setOriginValue(setupParamBean.getSuspend_count());
    	this.fmt_exit_time.setOriginValue(setupParamBean.getExit_time());
    	this.fmt_change_time.setOriginValue(setupParamBean.getChange_time());
    	this.fmt_normal_voltage.setOriginValue(setupParamBean.getNormal_voltage());
    	this.fmt_lack_voltage.setOriginValue(setupParamBean.getLack_voltage());
    	this.fmt_full_voltage.setOriginValue(setupParamBean.getFull_voltage());
    	this.fmt_low_voltage.setOriginValue(setupParamBean.getLow_voltage());
    	this.fmt_protect_voltage.setOriginValue(setupParamBean.getProtect_voltage());
    	this.fmt_charger_current.setOriginValue(setupParamBean.getCharger_current());
    	this.fmt_open_voltage.setOriginValue(setupParamBean.getOpen_voltage());
    	this.fmt_reduced_voltage.setOriginValue(setupParamBean.getReduced_voltage());
    	this.fmt_normal_power_hz.setOriginValue(setupParamBean.getNormal_power_hz());
    	this.fmt_inverter_power_hz.setOriginValue(setupParamBean.getInverter_power_hz());
    	this.fmt_brake_open_time.setOriginValue(setupParamBean.getBrake_open_time());
    }
    
    public SetupParamBean getSetupParamBean() throws ConvertException{
        
    	SetupParamBean setupParamBean = new SetupParamBean();
    	if ( !fmt_encoder_direction.getValue().equals(fmt_encoder_direction.getOriginValue()) )
    		setupParamBean.setDirect(Short.valueOf(fmt_encoder_direction.getValue().toString()) );
    	else
    		setupParamBean.setDirect((short)0xFFFF );
    	
    	if ( !fmt_encoder_pulse.getValue().equals(fmt_encoder_pulse.getOriginValue()) )
    		setupParamBean.setPulse(Short.valueOf(fmt_encoder_pulse.getValue().toString()) );
    	else
    		setupParamBean.setPulse((short)0xFFFF );
    	
    	if ( !fmt_tractor_wheel.getValue().equals(fmt_tractor_wheel.getOriginValue()) )
    		setupParamBean.setTractor_wheel(Short.valueOf(fmt_tractor_wheel.getValue().toString()) );
    	else
    		setupParamBean.setTractor_wheel((short)0xFFFF );
    	
    	if ( !fmt_suspend_count.getValue().equals(fmt_suspend_count.getOriginValue()) )
    		setupParamBean.setSuspend_count(Short.valueOf(fmt_suspend_count.getValue().toString()) );
    	else
    		setupParamBean.setSuspend_count((short)0xFFFF );
    	
    	if ( !fmt_exit_time.getValue().equals(fmt_exit_time.getOriginValue()) )
    		setupParamBean.setExit_time(Short.valueOf(fmt_exit_time.getValue().toString()) );
    	else
    		setupParamBean.setExit_time((short)0xFFFF );
    	
    	if ( !fmt_change_time.getValue().equals(fmt_change_time.getOriginValue()) )
    		setupParamBean.setChange_time(Short.valueOf(fmt_change_time.getValue().toString()) );
    	else
    		setupParamBean.setChange_time((short)0xFFFF );
    	
    	if ( !fmt_normal_voltage.getValue().equals(fmt_normal_voltage.getOriginValue()) )
    		setupParamBean.setNormal_voltage(Short.valueOf(fmt_normal_voltage.getValue().toString()) );
    	else
    		setupParamBean.setNormal_voltage((short)0xFFFF );
    	
    	if ( !fmt_lack_voltage.getValue().equals(fmt_lack_voltage.getOriginValue()) )
    		setupParamBean.setLack_voltage(Short.valueOf(fmt_lack_voltage.getValue().toString()) );
    	else
    		setupParamBean.setLack_voltage((short)0xFFFF );
    	
    	if ( !fmt_full_voltage.getValue().equals(fmt_full_voltage.getOriginValue()) )
    		setupParamBean.setFull_voltage(Short.valueOf(fmt_full_voltage.getValue().toString()) );
    	else
    		setupParamBean.setFull_voltage((short)0xFFFF );
    	
    	if ( !fmt_low_voltage.getValue().equals(fmt_low_voltage.getOriginValue()) )
    		setupParamBean.setLow_voltage(Short.valueOf(fmt_low_voltage.getValue().toString()) );
    	else
    		setupParamBean.setLow_voltage((short)0xFFFF );
    	
    	if ( !fmt_protect_voltage.getValue().equals(fmt_protect_voltage.getOriginValue()) )
    		setupParamBean.setProtect_voltage(Short.valueOf(fmt_protect_voltage.getValue().toString()) );
    	else
    		setupParamBean.setProtect_voltage((short)0xFFFF );
    	
     	if ( !fmt_charger_current.getValue().equals(fmt_charger_current.getOriginValue()) )
    		setupParamBean.setCharger_current(Short.valueOf(fmt_charger_current.getValue().toString()) );
    	else
    		setupParamBean.setCharger_current((short)0xFFFF );
     	
     	if ( !fmt_open_voltage.getValue().equals(fmt_open_voltage.getOriginValue()) )
    		setupParamBean.setOpen_voltage(Short.valueOf(fmt_open_voltage.getValue().toString()) );
    	else
    		setupParamBean.setOpen_voltage((short)0xFFFF );
     	
     	if ( !fmt_reduced_voltage.getValue().equals(fmt_reduced_voltage.getOriginValue()) )
    		setupParamBean.setReduced_voltage(Short.valueOf(fmt_reduced_voltage.getValue().toString()) );
    	else
    		setupParamBean.setReduced_voltage((short)0xFFFF );
     	
     	if ( !fmt_normal_power_hz.getValue().equals(fmt_normal_power_hz.getOriginValue()) )
    		setupParamBean.setNormal_power_hz(Short.valueOf(fmt_normal_power_hz.getValue().toString()) );
    	else
    		setupParamBean.setNormal_power_hz((short)0xFFFF );
     	
     	if ( !fmt_inverter_power_hz.getValue().equals(fmt_inverter_power_hz.getOriginValue()) )
    		setupParamBean.setInverter_power_hz(Short.valueOf(fmt_inverter_power_hz.getValue().toString()) );
    	else
    		setupParamBean.setInverter_power_hz((short)0xFFFF );
     	
     	if ( !fmt_brake_open_time.getValue().equals(fmt_brake_open_time.getOriginValue()) )
    		setupParamBean.setBrake_open_time(Short.valueOf(fmt_brake_open_time.getValue().toString()) );
    	else
    		setupParamBean.setBrake_open_time((short)0xFFFF );
     	
    	return setupParamBean;
    }
    
    public static class StatusBean{
    	private float vbus;
    	private float vbat;
    	private float curr;
    	private float speed;
    	
		public float getVbus() {
			return vbus;
		}
		public void setVbus(float vbus) {
			this.vbus = vbus;
		}
		public float getVbat() {
			return vbat;
		}
		public void setVbat(float vbat) {
			this.vbat = vbat;
		}
		public float getCurr() {
			return curr;
		}
		public void setCurr(float curr) {
			this.curr = curr;
		}
		public float getSpeed() {
			return speed;
		}
		public void setSpeed(float speed) {
			this.speed = speed;
		}
    }
    
    public static class SetupParamBean{
    	private short direct;
    	private short pulse;
    	private short tractor_wheel;
    	private short suspend_count;
    	private short exit_time;
    	private short change_time;
    	private short normal_voltage;
    	private short lack_voltage;
    	private short full_voltage;
    	private short low_voltage;
    	private short protect_voltage;
    	private short charger_current;
    	private short open_voltage;
    	private short reduced_voltage;
    	private short normal_power_hz;
    	private short inverter_power_hz;
    	private short brake_open_time;
    	
		public short getDirect() {
			return direct;
		}
		public short getPulse() {
			return pulse;
		}
		public short getTractor_wheel() {
			return tractor_wheel;
		}
		public short getSuspend_count() {
			return suspend_count;
		}
		public short getExit_time() {
			return exit_time;
		}
		public short getChange_time() {
			return change_time;
		}
		public short getNormal_voltage() {
			return normal_voltage;
		}
		public short getLack_voltage() {
			return lack_voltage;
		}
		public short getFull_voltage() {
			return full_voltage;
		}
		public short getLow_voltage() {
			return low_voltage;
		}
		public short getProtect_voltage() {
			return protect_voltage;
		}
		public short getCharger_current() {
			return charger_current;
		}
		public short getOpen_voltage() {
			return open_voltage;
		}
		public short getReduced_voltage() {
			return reduced_voltage;
		}
		public short getNormal_power_hz() {
			return normal_power_hz;
		}
		public short getInverter_power_hz() {
			return inverter_power_hz;
		}
		public short getBrake_open_time() {
			return brake_open_time;
		}
		public void setDirect(short direct) {
			this.direct = direct;
		}
		public void setPulse(short pulse) {
			this.pulse = pulse;
		}
		public void setTractor_wheel(short tractor_wheel) {
			this.tractor_wheel = tractor_wheel;
		}
		public void setSuspend_count(short suspend_count) {
			this.suspend_count = suspend_count;
		}
		public void setExit_time(short exit_time) {
			this.exit_time = exit_time;
		}
		public void setChange_time(short change_time) {
			this.change_time = change_time;
		}
		public void setNormal_voltage(short normal_voltage) {
			this.normal_voltage = normal_voltage;
		}
		public void setLack_voltage(short lack_voltage) {
			this.lack_voltage = lack_voltage;
		}
		public void setFull_voltage(short full_voltage) {
			this.full_voltage = full_voltage;
		}
		public void setLow_voltage(short low_voltage) {
			this.low_voltage = low_voltage;
		}
		public void setProtect_voltage(short protect_voltage) {
			this.protect_voltage = protect_voltage;
		}
		public void setCharger_current(short charger_current) {
			this.charger_current = charger_current;
		}
		public void setOpen_voltage(short open_voltage) {
			this.open_voltage = open_voltage;
		}
		public void setReduced_voltage(short reduced_voltage) {
			this.reduced_voltage = reduced_voltage;
		}
		public void setNormal_power_hz(short normal_power_hz) {
			this.normal_power_hz = normal_power_hz;
		}
		public void setInverter_power_hz(short inverter_power_hz) {
			this.inverter_power_hz = inverter_power_hz;
		}
		public void setBrake_open_time(short brake_open_time) {
			this.brake_open_time = brake_open_time;
		}
		
		public boolean IsValueEquals(SetupParamBean bean) {
			if(this.direct != bean.getDirect()) {
				return false;
			}
			if(this.pulse != bean.getPulse()) {
				return false;
			}
			if(this.tractor_wheel != bean.getTractor_wheel()) {
				return false;
			}
			if(this.suspend_count != bean.getSuspend_count()) {
				return false;
			}
			if(this.exit_time != bean.getExit_time()) {
				return false;
			}
			if(this.change_time != bean.getChange_time()) {
				return false;
			}
			if(this.normal_voltage != bean.getNormal_voltage()) {
				return false;
			}
			if(this.lack_voltage != bean.getLack_voltage()) {
				return false;
			}
			if(this.full_voltage != bean.getFull_voltage()) {
				return false;
			}
			if(this.low_voltage != bean.getLow_voltage()) {
				return false;
			}
			if(this.protect_voltage != bean.getProtect_voltage()) {
				return false;
			}
			if(this.charger_current != bean.getCharger_current()) {
				return false;
			}
			if(this.open_voltage != bean.getOpen_voltage()) {
				return false;
			}
			if(this.reduced_voltage != bean.getReduced_voltage()) {
				return false;
			}
			if(this.normal_power_hz != bean.getNormal_power_hz()) {
				return false;
			}
			if(this.inverter_power_hz != bean.getInverter_power_hz()) {
				return false;
			}
			if(this.brake_open_time != bean.getBrake_open_time()) {
				return false;
			}
			return true;
		}
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btn_default_param) {
			if ( settingPanel instanceof EpsSetting )
				( ( EpsSetting )settingPanel ).SetSetupParam((short)17,(short)1);
		}
	}
    
}
