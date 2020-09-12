package slecon.setting.advanced;

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
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueRadioButton;
import slecon.component.ValueTextField;
import slecon.home.HomePanel;
import slecon.home.PosButton;
import slecon.home.dashboard.DashboardPanel;
import slecon.interfaces.ConvertException;
import slecon.setting.installation.CommissionSetting;
import slecon.setting.setup.motion.SensorType;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import comm.constants.Message;





public class Advanced extends JPanel implements ActionListener, ChangeListener {
    	
	private static final long     serialVersionUID = -7758190488355480744L;

    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.advanced.Advanced" );
    
    public boolean			   Visit_Enable = false;

    /* ---------------------------------------------------------------------------- */
    private boolean            started = false;
    private SettingPanel<Advanced> settingPanel;
    private JLabel             cpt_position_adjustment;
    private ValueCheckBox      ebd_enable;
    private JLabel             lbl_holdoff_time;
    private ValueTextField     fmt_holdoff_time;
    private JLabel             lbl_tolerance;
    private ValueTextField     fmt_tolerance;

    /* ---------------------------------------------------------------------------- */
    private JLabel         	   cpt_advance_door_open;
    private ValueCheckBox      ebd_enable_0;
    private JLabel             lbl_active_speed_threshold;
    private ValueTextField     fmt_active_speed_threshold;

    /* ---------------------------------------------------------------------------- */
    
    private JLabel                cpt_re_leveling;
    private ValueCheckBox         ebd_releveling;
    private JLabel                lbl_sensor_type;
    private MyComboBox 			  cbo_sensor_type;
    private JLabel                lbl_trigger_hold_off_time;
    private ValueTextField        fmt_trigger_hold_off_time;
    private JLabel                lbl_time_limit;
    private ValueTextField        fmt_time_limit;
    private JLabel                lbl_retry_count;
    private ValueTextField        fmt_retry_count;
    
    /* ---------------------------------------------------------------------------- */
    
    private JLabel                cpt_brake_monitor;
    private JLabel                lbl_brake_retry_open_count;
    private ValueTextField        fmt_brake_retry_open_count;
    private JLabel                lbl_brake_retry_close_count;
    private ValueTextField        fmt_brake_retry_close_count;
    private JLabel                lbl_open_brake_monitor;
    private MyComboBox 			  cbo_open_brake_monitor;
    private JLabel                lbl_running_brake_monitor;
    private MyComboBox			  cbo_running_brake_monitor;
    private JLabel                lbl_stop_brake_monitor;
    private MyComboBox 			  cbo_stop_brake_monitor;
    private JLabel                lbl_standby_brake_monitor;
    private MyComboBox 			  cbo_standby_brake_monitor;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel       		  cpt_insprct_strategy;
    private ValueRadioButton 	  ebd_insprct_stragety_a;
    private ValueRadioButton 	  ebd_insprct_stragety_b;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel         		  cpt_temperature_control;
    private JLabel         		  lbl_current_temperature_value;
    private ValueTextField 		  fmt_current_temperature_value;
    private JLabel         		  lbl_ccf_stop_delay;
    private ValueTextField 		  fmt_ccf_stop_delay;
    private JLabel         		  lbl_temperature_start;
    private ValueTextField 		  fmt_temperature_start;
    private JLabel         		  lbl_temperature_stop;
    private ValueTextField 		  fmt_temperature_stop;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel                cpt_others;
    private JLabel                lbl_reverse_run_position_limit;
    private ValueTextField        fmt_reverse_run_position_limit;
    private PosButton			  btn_reload_nv_data_default;

    public Advanced () {
        initGUI();
        
        ebd_enable.addChangeListener(this);
        do_ebd_enable_stateChanged(null);
        
        ebd_enable_0.addChangeListener(this);
        do_ebd_enable_0_stateChanged(null);
        
        ebd_releveling.addChangeListener(this);
        do_ebd_releveling_stateChanged(null);
    }


    public void setSettingPanel ( SettingPanel<Advanced> panel ) {
        this.settingPanel = panel;
    }
    
    public void SetWidgetEnable(boolean enable) {
    	ebd_enable.setEnabled(enable);
    	lbl_holdoff_time.setEnabled(enable);
    	fmt_holdoff_time.setEnabled(enable);
    	lbl_tolerance.setEnabled(enable);
    	fmt_tolerance.setEnabled(enable);
    	
    	ebd_enable_0.setEnabled(enable);
    	lbl_active_speed_threshold.setEnabled(enable);
    	fmt_active_speed_threshold.setEnabled(enable);
    	
    	ebd_releveling.setEnabled(enable);
    	lbl_sensor_type.setEnabled(enable);
    	cbo_sensor_type.setEnabled(enable);
    	lbl_trigger_hold_off_time.setEnabled(enable);
    	fmt_trigger_hold_off_time.setEnabled(enable);
    	lbl_time_limit.setEnabled(enable);
    	fmt_time_limit.setEnabled(enable);
    	lbl_retry_count.setEnabled(enable);
    	fmt_retry_count.setEnabled(enable);
    	
    	lbl_reverse_run_position_limit.setEnabled(enable);
    	fmt_reverse_run_position_limit.setEnabled(enable);
    	btn_reload_nv_data_default.setEnabled(enable);
    	
    	cpt_brake_monitor.setEnabled(enable);
    	lbl_brake_retry_open_count.setEnabled(enable);
    	fmt_brake_retry_open_count.setEnabled(enable);
    	lbl_brake_retry_close_count.setEnabled(enable);
    	fmt_brake_retry_close_count.setEnabled(enable);
    	lbl_open_brake_monitor.setEnabled(enable);
    	cbo_open_brake_monitor.setEnabled(enable);
    	lbl_running_brake_monitor.setEnabled(enable);
    	cbo_running_brake_monitor.setEnabled(enable);
    	lbl_stop_brake_monitor.setEnabled(enable);
    	cbo_stop_brake_monitor.setEnabled(enable);
    	lbl_standby_brake_monitor.setEnabled(enable);
    	cbo_standby_brake_monitor.setEnabled(enable);
    	
    	ebd_insprct_stragety_a.setEnabled(enable);
        ebd_insprct_stragety_b.setEnabled(enable);
        
        lbl_current_temperature_value.setEnabled(enable);
        lbl_ccf_stop_delay.setEnabled(enable);
        fmt_ccf_stop_delay.setEnabled(enable);
        lbl_temperature_start.setEnabled(enable);
        fmt_temperature_start.setEnabled(enable);
        lbl_temperature_stop.setEnabled(enable);
        fmt_temperature_stop.setEnabled(enable);
    }

    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][200::200][150::150][]" ) );
        cpt_position_adjustment = new JLabel();
        ebd_enable              = new ValueCheckBox();
        
        lbl_holdoff_time        = new JLabel();
        fmt_holdoff_time        = new ValueTextField();
        lbl_tolerance           = new JLabel();
        fmt_tolerance           = new ValueTextField();
        setCaptionStyle( cpt_position_adjustment );

        // @CompoentSetting( ebd_enable )

        // @CompoentSetting<Fmt>( lbl_holdoff_time , fmt_holdoff_time )
        setTextLabelStyle( lbl_holdoff_time );
        fmt_holdoff_time.setColumns( 10 );
        fmt_holdoff_time.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_holdoff_time.setScope( Long.class, 0L, 60000L, true, true );
        fmt_holdoff_time.setEmptyValue( 50L );

        // @CompoentSetting<Fmt>( lbl_tolerance , fmt_tolerance )
        setTextLabelStyle( lbl_tolerance );
        fmt_tolerance.setColumns( 10 );
        fmt_tolerance.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_tolerance.setScope( Double.class, 0D, 10000D, true, true );
        fmt_tolerance.setEmptyValue( 500D );
        add( cpt_position_adjustment, "gapbottom 18-12, span, top" );
        add( ebd_enable, "skip 1, span, top" );
        
        Box vbox_title = Box.createVerticalBox();
        vbox_title.add( lbl_holdoff_time);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_tolerance);
        
        Box vbox_value = Box.createVerticalBox();
        vbox_value.add( fmt_holdoff_time );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_tolerance );

        add(vbox_title, "skip 2, span 1, left, top");
        add(vbox_value, "span 1, wrap 30, left, top");
        /* ---------------------------------------------------------------------------- */
        cpt_advance_door_open      = new JLabel();
        ebd_enable_0               = new ValueCheckBox();
        lbl_active_speed_threshold = new JLabel();
        fmt_active_speed_threshold = new ValueTextField();
        setCaptionStyle( cpt_advance_door_open );

        // @CompoentSetting( ebd_enable_0 )

        // @CompoentSetting<Fmt>( lbl_active_speed_threshold , fmt_active_speed_threshold )
        setTextLabelStyle( lbl_active_speed_threshold );
        fmt_active_speed_threshold.setColumns( 10 );
        fmt_active_speed_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_active_speed_threshold.setScope( Double.class, 0D, 5000D, true, true );
        fmt_active_speed_threshold.setEmptyValue( 750D );
        add( cpt_advance_door_open, "gapbottom 18-12, span, top" );
        add( ebd_enable_0, "skip 1, span, top" );
        add( lbl_active_speed_threshold, "skip 2, span 1, left, top" );
        add( fmt_active_speed_threshold, "span 1, wrap 30, left, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_re_leveling = new JLabel();
        ebd_releveling = new ValueCheckBox();
        lbl_sensor_type = new JLabel();
        cbo_sensor_type = new MyComboBox();
        lbl_trigger_hold_off_time = new JLabel();
        fmt_trigger_hold_off_time = new ValueTextField();
        lbl_time_limit = new JLabel();
        fmt_time_limit = new ValueTextField();
        lbl_retry_count = new JLabel();
        fmt_retry_count = new ValueTextField();

        cbo_sensor_type.setModel(new DefaultComboBoxModel<SensorType>(SensorType.values()));
        
        setCaptionStyle(cpt_re_leveling);
        // @CompoentSetting( ebd_enable )

        // @CompoentSetting( lbl_sensor_type, cbo_sensor_type )
        setComboBoxLabelStyle(lbl_sensor_type);
        setComboBoxValueStyle(cbo_sensor_type);
        if(cbo_sensor_type.getItemCount()>0) cbo_sensor_type.setSelectedIndex(0);
        
        // @CompoentSetting<Fmt>( lbl_trigger_hold_off_time , fmt_trigger_hold_off_time )
        setTextLabelStyle(lbl_trigger_hold_off_time);
        fmt_trigger_hold_off_time.setColumns(10);
        fmt_trigger_hold_off_time.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_trigger_hold_off_time.setScope(Long.class, 0L, 60000L, true, true);
        fmt_trigger_hold_off_time.setEmptyValue(0L);
        
        // @CompoentSetting<Fmt>( lbl_time_limit , fmt_time_limit )
        setTextLabelStyle(lbl_time_limit);
        fmt_time_limit.setColumns(10);
        fmt_time_limit.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_time_limit.setScope(Long.class, 0L, 60000L, true, true);
        fmt_time_limit.setEmptyValue(0L);
        
        // @CompoentSetting<Fmt>( lbl_retry_count , fmt_retry_count )
        setTextLabelStyle(lbl_retry_count);
        fmt_retry_count.setColumns(10);
        fmt_retry_count.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_retry_count.setScope(Long.class, 0L, 100L, true, true);
        fmt_retry_count.setEmptyValue(0L);

        add(cpt_re_leveling, "gapbottom 18-12, span, aligny center, top");
        add(ebd_releveling, "skip 1, span");
        
        Box vbox_title1 = Box.createVerticalBox();
        vbox_title1.add( lbl_sensor_type);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_trigger_hold_off_time);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_time_limit);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_retry_count);
        
        Box vbox_value1 = Box.createVerticalBox();
        vbox_value1.add( cbo_sensor_type );
        vbox_value1.add( Box.createVerticalStrut(10));
        vbox_value1.add( fmt_trigger_hold_off_time );
        vbox_value1.add( Box.createVerticalStrut(10));
        vbox_value1.add( fmt_time_limit );
        vbox_value1.add( Box.createVerticalStrut(10));
        vbox_value1.add( fmt_retry_count );

        add(vbox_title1, "skip 2, span 1, left, top");
        add(vbox_value1, "span 1, wrap 30, left, top");
        
        /* ---------------------------------------------------------------------------- */
        cpt_brake_monitor = new JLabel();
        lbl_brake_retry_open_count = new JLabel();
        fmt_brake_retry_open_count = new ValueTextField();
        lbl_brake_retry_close_count = new JLabel();
        fmt_brake_retry_close_count = new ValueTextField();
        lbl_open_brake_monitor = new JLabel();
        cbo_open_brake_monitor = new MyComboBox();
        lbl_running_brake_monitor = new JLabel();
        cbo_running_brake_monitor = new MyComboBox();
        lbl_stop_brake_monitor = new JLabel();
        cbo_stop_brake_monitor = new MyComboBox();
        lbl_standby_brake_monitor = new JLabel();
        cbo_standby_brake_monitor = new MyComboBox();
        
        cbo_open_brake_monitor.setModel(new DefaultComboBoxModel<OpenBrakeMonitor>(OpenBrakeMonitor.values()));
        cbo_running_brake_monitor.setModel(new DefaultComboBoxModel<RunningBrakeMonitor>(RunningBrakeMonitor.values()));
        cbo_stop_brake_monitor.setModel(new DefaultComboBoxModel<StopBrakeMonitor>(StopBrakeMonitor.values()));
        cbo_standby_brake_monitor.setModel(new DefaultComboBoxModel<StandbyBrakeMonitor>(StandbyBrakeMonitor.values()));
        cbo_open_brake_monitor.setSelectedIndex(0);
        cbo_running_brake_monitor.setSelectedIndex(0);
        cbo_stop_brake_monitor.setSelectedItem(0);
        cbo_standby_brake_monitor.setSelectedItem(0);
    
        setCaptionStyle(cpt_brake_monitor);
        setTextLabelStyle(lbl_brake_retry_open_count);
        setTextLabelStyle(lbl_brake_retry_close_count);
        setComboBoxLabelStyle(lbl_open_brake_monitor);
        setComboBoxValueStyle(cbo_open_brake_monitor);
        setComboBoxLabelStyle(lbl_running_brake_monitor);
        setComboBoxValueStyle(cbo_running_brake_monitor);
        setComboBoxLabelStyle(lbl_stop_brake_monitor);
        setComboBoxValueStyle(cbo_stop_brake_monitor);
        setComboBoxLabelStyle(lbl_standby_brake_monitor);
        setComboBoxValueStyle(cbo_standby_brake_monitor);
        
        fmt_brake_retry_open_count.setColumns(5);
        fmt_brake_retry_open_count.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_brake_retry_open_count.setScope(Long.class, 0L, 10L, true, true);
        fmt_brake_retry_open_count.setEmptyValue(0L);
        
        fmt_brake_retry_close_count.setColumns(5);
        fmt_brake_retry_close_count.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_brake_retry_close_count.setScope(Long.class, 0L, 10L, true, true);
        fmt_brake_retry_close_count.setEmptyValue(0L);
        
        add(cpt_brake_monitor, "gapbottom 18-12, span, aligny center, top");
        
        Box vbox_title2 = Box.createVerticalBox();
        vbox_title2.add( lbl_brake_retry_open_count);
        vbox_title2.add( Box.createVerticalStrut(15));
        vbox_title2.add( lbl_brake_retry_close_count);
        vbox_title2.add( Box.createVerticalStrut(15));
        vbox_title2.add( lbl_open_brake_monitor);
        vbox_title2.add( Box.createVerticalStrut(15));
        vbox_title2.add( lbl_running_brake_monitor);
        vbox_title2.add( Box.createVerticalStrut(15));
        vbox_title2.add( lbl_stop_brake_monitor);
        vbox_title2.add( Box.createVerticalStrut(15));
        vbox_title2.add( lbl_standby_brake_monitor);
        
        Box vbox_value2 = Box.createVerticalBox();
        vbox_value2.add( fmt_brake_retry_open_count );
        vbox_value2.add( Box.createVerticalStrut(10));
        vbox_value2.add( fmt_brake_retry_close_count );
        vbox_value2.add( Box.createVerticalStrut(10));
        vbox_value2.add( cbo_open_brake_monitor );
        vbox_value2.add( Box.createVerticalStrut(10));
        vbox_value2.add( cbo_running_brake_monitor );
        vbox_value2.add( Box.createVerticalStrut(10));
        vbox_value2.add( cbo_stop_brake_monitor );
        vbox_value2.add( Box.createVerticalStrut(10));
        vbox_value2.add( cbo_standby_brake_monitor );

        add(vbox_title2, "skip 2, span 1, left, top");
        add(vbox_value2, "span 1, wrap 30, left, top");
        
        /* ---------------------------------------------------------------------------- */
        
        cpt_insprct_strategy           = new JLabel();
        ebd_insprct_stragety_a = new ValueRadioButton();
        ebd_insprct_stragety_b = new ValueRadioButton();
        setCaptionStyle( cpt_insprct_strategy );
        ButtonGroup bg = new ButtonGroup();
        bg.add( ebd_insprct_stragety_a );
        bg.add( ebd_insprct_stragety_b );
        
        add( cpt_insprct_strategy, "gapbottom 18-12, span, top" );
        add( ebd_insprct_stragety_a, "skip 2, span, top" );
        add( ebd_insprct_stragety_b, "skip 2, span, wrap 30, top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_temperature_control      = new JLabel();
        lbl_current_temperature_value = new JLabel();
        fmt_current_temperature_value = new ValueTextField();
        lbl_ccf_stop_delay = new JLabel();
        fmt_ccf_stop_delay = new ValueTextField();
        lbl_temperature_start = new JLabel();
        fmt_temperature_start = new ValueTextField();
        lbl_temperature_stop = new JLabel();
        fmt_temperature_stop = new ValueTextField();
        
        setCaptionStyle( cpt_temperature_control );
        setTextLabelStyle( lbl_current_temperature_value );
        setTextLabelStyle( lbl_ccf_stop_delay );
        setTextLabelStyle( lbl_temperature_start );
        setTextLabelStyle( lbl_temperature_stop );
        
        fmt_current_temperature_value.setEnabled( false );
        fmt_current_temperature_value.setColumns( 10 );
        fmt_current_temperature_value.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_current_temperature_value.setEmptyValue( 0 );
        
        fmt_ccf_stop_delay.setColumns( 10 );
        fmt_ccf_stop_delay.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_ccf_stop_delay.setScope( Long.class, 0L, 50000L, true, true );
        fmt_ccf_stop_delay.setEmptyValue( 0 );
        
        fmt_temperature_start.setColumns( 10 );
        fmt_temperature_start.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_temperature_start.setScope( Long.class, -20L, 110L, true, true );
        fmt_temperature_start.setEmptyValue( 0 );
        
        fmt_temperature_stop.setColumns( 10 );
        fmt_temperature_stop.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_temperature_stop.setScope( Long.class, -20L, 110L, true, true );
        fmt_temperature_stop.setEmptyValue( 0 );
        
        add( cpt_temperature_control, "gapbottom 18-12, span, top" );
        add( lbl_current_temperature_value, "skip 2, span 1, left, top" );
        add( fmt_current_temperature_value, "span 1, wrap, left, top" );
        add( lbl_ccf_stop_delay, "skip 2, span 1, left, top" );
        add( fmt_ccf_stop_delay, "span 1, wrap, left, top" );
        add( lbl_temperature_start, "skip 2, span 1, left, top" );
        add( fmt_temperature_start, "span 1, wrap, left, top" );
        add( lbl_temperature_stop, "skip 2, span 1, left, top" );
        add( fmt_temperature_stop, "span 1, wrap 30, left, top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_others = new JLabel();
        lbl_reverse_run_position_limit = new JLabel();
        fmt_reverse_run_position_limit = new ValueTextField();
        btn_reload_nv_data_default = new PosButton(ImageFactory.BUTTON_PAUSE.icon(87, 30), ImageFactory.BUTTON_START.icon(87, 30));
        btn_reload_nv_data_default.addActionListener(this);
        setCaptionStyle(cpt_others);
        // @CompoentSetting<Fmt>( lbl_reverse_run_position_limit , fmt_reverse_run_position_limit )
        setTextLabelStyle(lbl_reverse_run_position_limit);
        fmt_reverse_run_position_limit.setColumns(10);
        fmt_reverse_run_position_limit.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_reverse_run_position_limit.setScope(Double.class, 0D, 1000D, false, false);
        fmt_reverse_run_position_limit.setEmptyValue(0D);
        
        setButtonStyle( btn_reload_nv_data_default );
        
        add(cpt_others, "gapbottom 18-12, span, aligny center, top");
        add(lbl_reverse_run_position_limit, "skip 2, span 1, left, top");
        add(fmt_reverse_run_position_limit, "span 1, wrap, left, top");
        add(btn_reload_nv_data_default, "skip 2, span, wrap 30, top");

        /* ---------------------------------------------------------------------------- */
        
        bindGroup( "Enable", ebd_enable );
        bindGroup( "HoldoffTime", lbl_holdoff_time, fmt_holdoff_time );
        bindGroup( "Tolerance", lbl_tolerance, fmt_tolerance );
        bindGroup( "Enable0", ebd_enable_0 );
        bindGroup( "ActiveSpeedThreshold", lbl_active_speed_threshold, fmt_active_speed_threshold );
        bindGroup( "Enable1", ebd_releveling );
        bindGroup( "SensorType", lbl_sensor_type, cbo_sensor_type );
        bindGroup( "TriggerHoldOffTime", lbl_trigger_hold_off_time, fmt_trigger_hold_off_time );
        bindGroup( "TimeLimit", lbl_time_limit, fmt_time_limit );
        bindGroup( "RetryCount", lbl_retry_count, fmt_retry_count );
        bindGroup( "ReverseRunPositionLimit", lbl_reverse_run_position_limit, fmt_reverse_run_position_limit );
        
        bindGroup( new AbstractButton[] { ebd_enable }, lbl_holdoff_time, fmt_holdoff_time, lbl_tolerance, fmt_tolerance);
        bindGroup( new AbstractButton[] { ebd_enable_0 }, lbl_active_speed_threshold, fmt_active_speed_threshold );
        bindGroup( new AbstractButton[] { ebd_releveling }, lbl_sensor_type , cbo_sensor_type, lbl_trigger_hold_off_time, fmt_trigger_hold_off_time,
                lbl_time_limit, fmt_time_limit, lbl_retry_count, fmt_retry_count);
        
        loadI18N();
        SetWidgetEnable(false);
        revalidate();
    }


    private void loadI18N () {
        cpt_position_adjustment.setText( getBundleText( "LBL_cpt_position_adjustment", "Position Adjustment" ) );
        ebd_enable.setText( getBundleText( "LBL_ebd_enable", "Enable" ) );
        lbl_holdoff_time.setText( getBundleText( "LBL_lbl_holdoff_time", "Holdoff time" ) );
        lbl_tolerance.setText( getBundleText( "LBL_lbl_tolerance", "Tolerance" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_advance_door_open.setText( getBundleText( "LBL_cpt_advance_door_open", "Advance Door Open" ) );
        ebd_enable_0.setText( getBundleText( "LBL_ebd_enable_0", "Enable" ) );
        lbl_active_speed_threshold.setText( getBundleText( "LBL_lbl_active_speed_threshold", "Active speed threshold" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_re_leveling.setText( getBundleText( "LBL_cpt_re_leveling", "Re-Leveling" ) );
        ebd_releveling.setText( getBundleText( "LBL_ebd_releveling", "Enable" ) );
        lbl_sensor_type.setText( getBundleText( "LBL_lbl_sensor_type", "Sensor Type" ) );
        lbl_trigger_hold_off_time.setText( getBundleText( "LBL_lbl_trigger_hold_off_time", "Trigger hold-off time" ) );
        lbl_time_limit.setText( getBundleText( "LBL_lbl_time_limit", "Time limit" ) );
        lbl_retry_count.setText( getBundleText( "LBL_lbl_retry_count", "Retry count" ) );
        
        /* ---------------------------------------------------------------------------- */
        cpt_brake_monitor.setText( getBundleText( "LBL_cpt_brake_monitor", "Brake Monitor" ) );
        lbl_brake_retry_open_count.setText( getBundleText( "LBL_lbl_brake_retry_open_count", "Brake retry count open fail" ) );
        lbl_brake_retry_close_count.setText( getBundleText( "LBL_lbl_brake_retry_close_count", "Brake retry count close fail" ) );
        lbl_open_brake_monitor.setText( getBundleText( "LBL_lbl_open_brake_monitor", "Open brake monitor" ) );
        lbl_running_brake_monitor.setText( getBundleText( "LBL_lbl_running_brake_monitor", "Running brake monitor" ) );
        lbl_stop_brake_monitor.setText( getBundleText( "LBL_lbl_stop_brake_monitor", "Stop brake monitor" ) );
        lbl_standby_brake_monitor.setText( getBundleText( "LBL_lbl_standby_brake_monitor", "Standby brake monitor" ) );
        
        /* ---------------------------------------------------------------------------- */
        cpt_insprct_strategy.setText( getBundleText( "LBL_cpt_insprct_strategy", "Inspect Strategy" ) );
        ebd_insprct_stragety_a.setText( getBundleText( "LBL_ebd_insprct_strategy_a", "China" ) );
        ebd_insprct_stragety_b.setText( getBundleText( "LBL_ebd_insprct_strategy_b", "Japen" ) );
        
        /* ---------------------------------------------------------------------------- */
        cpt_temperature_control.setText( getBundleText( "LBL_cpt_temperature_control", "Temperation Control" ) );
        lbl_current_temperature_value.setText( getBundleText( "LBL_lbl_current_temperature_value", "Current Temperation" ) );
        lbl_ccf_stop_delay.setText( getBundleText( "LBL_lbl_ccf_stop_delay", "Turn Off CCF" ) );
        lbl_temperature_start.setText( getBundleText( "LBL_lbl_temperature_start", "Turn On CCF Temperature" ) );
        lbl_temperature_stop.setText( getBundleText( "LBL_lbl_temperature_stop", "Turn Off CCF Temperature" ) );
        
        /* ---------------------------------------------------------------------------- */
        cpt_others.setText( getBundleText( "LBL_cpt_others", "Others" ) );
        lbl_reverse_run_position_limit.setText( getBundleText( "LBL_lbl_reverse_run_position_limit", "Reverse run position limit" ) );
        btn_reload_nv_data_default.setText(getBundleText( "LBL_btn_reload_nv_data_default", "Reload Default NV Data" ));
        
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }

    private void setTextLabelStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }

    private void setComboBoxLabelStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }

    private void setComboBoxValueStyle ( JComboBox<?> c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }
    
    private void setButtonStyle ( JComponent c ) {
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


    public PositionAdjustmentBean getPositionAdjustmentBean () throws ConvertException {
        if ( ! fmt_holdoff_time.checkValue() )
            throw new ConvertException();
        if ( ! fmt_tolerance.checkValue() )
            throw new ConvertException();

        PositionAdjustmentBean bean_positionAdjustment = new PositionAdjustmentBean();
        bean_positionAdjustment.setEnable( ebd_enable.isSelected() );
        bean_positionAdjustment.setHoldoffTime( ( Long )fmt_holdoff_time.getValue() );
        bean_positionAdjustment.setTolerance( ( Double )fmt_tolerance.getValue() );
        return bean_positionAdjustment;
    }


    public AdvanceDoorOpenBean getAdvanceDoorOpenBean () throws ConvertException {
        if ( ! fmt_active_speed_threshold.checkValue() )
            throw new ConvertException();

        AdvanceDoorOpenBean bean_advanceDoorOpen = new AdvanceDoorOpenBean();
        bean_advanceDoorOpen.setEnable0( ebd_enable_0.isSelected() );
        bean_advanceDoorOpen.setActiveSpeedThreshold( ( Double )fmt_active_speed_threshold.getValue() );
        return bean_advanceDoorOpen;
    }


    public void setPositionAdjustmentBean ( PositionAdjustmentBean bean_positionAdjustment ) {
        this.ebd_enable.setOriginSelected( bean_positionAdjustment.getEnable() != null && bean_positionAdjustment.getEnable() == true );
        this.fmt_holdoff_time.setOriginValue( bean_positionAdjustment.getHoldoffTime() );
        this.fmt_tolerance.setOriginValue( bean_positionAdjustment.getTolerance() );
    }


    public void setAdvanceDoorOpenBean ( AdvanceDoorOpenBean bean_advanceDoorOpen ) {
        this.ebd_enable_0.setOriginSelected( bean_advanceDoorOpen.getEnable0() != null && bean_advanceDoorOpen.getEnable0() == true );
        this.fmt_active_speed_threshold.setOriginValue( bean_advanceDoorOpen.getActiveSpeedThreshold() );
    }


    public BrakeBean getBrakeBean() throws ConvertException{
        if ( ! fmt_brake_retry_open_count.checkValue() )
            throw new ConvertException();
        if ( ! fmt_brake_retry_close_count.checkValue() )
            throw new ConvertException();
        
    	BrakeBean brakeBean = new BrakeBean();
    	brakeBean.setBrake_open_retry_counts((long)fmt_brake_retry_open_count.getValue());
    	brakeBean.setBrake_close_retry_counts((long)fmt_brake_retry_close_count.getValue());
    	brakeBean.setOpen_brake_monitor(cbo_open_brake_monitor.getSelectedItem());
    	brakeBean.setRunning_brake_monitor(cbo_running_brake_monitor.getSelectedItem());
    	brakeBean.setStop_brake_monitor(cbo_stop_brake_monitor.getSelectedItem());
    	brakeBean.setStandby_brake_monitor(cbo_standby_brake_monitor.getSelectedItem());
    	return brakeBean;
    }
    
    public void setBrakeBean(BrakeBean brakeBean) {
    	this.fmt_brake_retry_open_count.setOriginValue(brakeBean.getBrake_open_retry_counts());
    	this.fmt_brake_retry_close_count.setOriginValue(brakeBean.getBrake_close_retry_counts());
    	this.cbo_open_brake_monitor.setSelectedItem(brakeBean.getOpen_brake_monitor());
    	this.cbo_running_brake_monitor.setSelectedItem(brakeBean.getRunning_brake_monitor());
    	this.cbo_stop_brake_monitor.setSelectedItem(brakeBean.getStop_brake_monitor());
    	this.cbo_standby_brake_monitor.setSelectedItem(brakeBean.getStandby_brake_monitor());
    }
    
    public DynamicStatus getDynamicStatus() {
    	DynamicStatus status = new DynamicStatus();
    	int inspect_mode = this.ebd_insprct_stragety_a.isSelected() ? 0 : 1;
    	status.setInspect(inspect_mode);
    	return status;
    }
    
    public void setInpectionMode( DynamicStatus bean  ) {
    	this.ebd_insprct_stragety_a.setOriginSelected(bean.getInspect() == 0x00);
    	this.ebd_insprct_stragety_b.setOriginSelected(bean.getInspect() == 0x01);
    }
    
    public void setDynamicStatus( DynamicStatus bean ) {
    	this.fmt_current_temperature_value.setValue( bean.getTemperature() );
    }
    
    public TemperatureBean getCurrentTemperature() throws ConvertException {
    	if ( ! fmt_ccf_stop_delay.checkValue() )
            throw new ConvertException();
    	if ( ! fmt_temperature_start.checkValue() )
            throw new ConvertException();
        if ( ! fmt_temperature_stop.checkValue() )
            throw new ConvertException();
    	TemperatureBean bean = new TemperatureBean();
    	bean.setDelay( (long)this.fmt_ccf_stop_delay.getValue() );
    	bean.setStart_temp( (long)this.fmt_temperature_start.getValue() );
    	bean.setStop_temp( (long)this.fmt_temperature_stop.getValue() );
    	return bean;
    }
    
    public void SetCcfTemperature( TemperatureBean bean ) {
    	this.fmt_ccf_stop_delay.setOriginValue( bean.getDelay() );
    	this.fmt_temperature_start.setOriginValue( bean.getStart_temp() );
    	this.fmt_temperature_stop.setOriginValue( bean.getStop_temp() );
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


    public static Advanced createPanel ( SettingPanel<Advanced> panel ) {
        Advanced gui = new Advanced();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class AdvanceDoorOpenBean {
        private Boolean enable0;
        private Double  activeSpeedThreshold;




        public Boolean getEnable0 () {
            return this.enable0;
        }


        public Double getActiveSpeedThreshold () {
            return this.activeSpeedThreshold;
        }


        public void setEnable0 ( Boolean enable0 ) {
            this.enable0 = enable0;
        }


        public void setActiveSpeedThreshold ( Double activeSpeedThreshold ) {
            this.activeSpeedThreshold = activeSpeedThreshold;
        }
    }

    public static class PositionAdjustmentBean {
        private Boolean enable;
        private Long    holdoffTime;
        private Double  tolerance;




        public Boolean getEnable () {
            return this.enable;
        }


        public Long getHoldoffTime () {
            return this.holdoffTime;
        }


        public Double getTolerance () {
            return this.tolerance;
        }


        public void setEnable ( Boolean enable ) {
            this.enable = enable;
        }


        public void setHoldoffTime ( Long holdoffTime ) {
            this.holdoffTime = holdoffTime;
        }


        public void setTolerance ( Double tolerance ) {
            this.tolerance = tolerance;
        }
    }


    public static class ReLevelBean {
        private Boolean enable;
        private Object sensortype;
        private Long triggerHoldOffTime;
        private Long timeLimit;
        private Long retryCount;

        public Boolean getEnable() {
            return this.enable;
        }

        public Object getSensortype() {
            return this.sensortype;
        }

        public Long getTriggerHoldOffTime() {
            return this.triggerHoldOffTime;
        }

        public Long getTimeLimit() {
            return this.timeLimit;
        }

        public Long getRetryCount() {
            return this.retryCount;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public void setSensortype(Object sensortype) {
            this.sensortype = sensortype;
        }

        public void setTriggerHoldOffTime(Long triggerHoldOffTime) {
            this.triggerHoldOffTime = triggerHoldOffTime;
        }

        public void setTimeLimit(Long timeLimit) {
            this.timeLimit = timeLimit;
        }

        public void setRetryCount(Long retryCount) {
            this.retryCount = retryCount;
        }
    }
    
    public static class BrakeBean{
    	private Long brake_open_retry_counts;
    	private Long brake_close_retry_counts;
    	private Object	open_brake_monitor;
    	private Object	running_brake_monitor;
    	private Object	stop_brake_monitor;
    	private Object	standby_brake_monitor;
		public Long getBrake_open_retry_counts() {
			return brake_open_retry_counts;
		}
		public Long getBrake_close_retry_counts() {
			return brake_close_retry_counts;
		}
		public Object getOpen_brake_monitor() {
			return open_brake_monitor;
		}
		public Object getRunning_brake_monitor() {
			return running_brake_monitor;
		}
		public Object getStop_brake_monitor() {
			return stop_brake_monitor;
		}
		public Object getStandby_brake_monitor() {
			return standby_brake_monitor;
		}
		public void setBrake_open_retry_counts(Long brake_open_retry_counts) {
			this.brake_open_retry_counts = brake_open_retry_counts;
		}
		public void setBrake_close_retry_counts(Long brake_close_retry_counts) {
			this.brake_close_retry_counts = brake_close_retry_counts;
		}
		public void setOpen_brake_monitor(Object open_brake_monitor) {
			this.open_brake_monitor = open_brake_monitor;
		}
		public void setRunning_brake_monitor(Object running_brake_monitor) {
			this.running_brake_monitor = running_brake_monitor;
		}
		public void setStop_brake_monitor(Object stop_brake_monitor) {
			this.stop_brake_monitor = stop_brake_monitor;
		}
		public void setStandby_brake_monitor(Object standby_brake_monitor) {
			this.standby_brake_monitor = standby_brake_monitor;
		}
    	
    }
    
    public static class DynamicStatus{
    	private Number temperature;
    	private int	 inspect;
    	
		public Number getTemperature() {
			return temperature;
		}
		public void setTemperature(Number temperature) {
			this.temperature = temperature;
		}
		public int getInspect() {
			return inspect;
		}
		public void setInspect(int inspect) {
			this.inspect = inspect;
		}
    }
    
    public static class TemperatureBean{
    	private Long  delay;
    	private Long  start_temp;
    	private Long  stop_temp;

		public Long getDelay() {
			return delay;
		}
		public void setDelay(Long delay) {
			this.delay = delay;
		}
		public Long getStart_temp() {
			return start_temp;
		}
		public void setStart_temp(Long start_temp) {
			this.start_temp = start_temp;
		}
		public Long getStop_temp() {
			return stop_temp;
		}
		public void setStop_temp(Long stop_temp) {
			this.stop_temp = stop_temp;
		}
    }
    
    public static class OthersBean {
        private Double reverseRunPositionLimit;

        public Double getReverseRunPositionLimit() {
            return reverseRunPositionLimit;
        }

        public void setReverseRunPositionLimit(Double reverseRunPositionLimit) {
            this.reverseRunPositionLimit = reverseRunPositionLimit;
        }
    }
    
    public OthersBean getOthersBean() throws ConvertException {
        if(! fmt_reverse_run_position_limit.checkValue())
            throw new ConvertException();
        OthersBean bean_others = new OthersBean();
        bean_others.setReverseRunPositionLimit( (Double)fmt_reverse_run_position_limit.getValue() );
        return bean_others;
    }
    
    
    public void setOthersBean(OthersBean bean_others) {
        this.fmt_reverse_run_position_limit.setOriginValue( bean_others.getReverseRunPositionLimit() );
    }



    public ReLevelBean getReLevelBean() throws ConvertException {
        if(! fmt_trigger_hold_off_time.checkValue())
             throw new ConvertException();
        if(! fmt_time_limit.checkValue())
             throw new ConvertException();
        if(! fmt_retry_count.checkValue())
             throw new ConvertException();
    
        ReLevelBean bean_reLevel = new ReLevelBean();
        bean_reLevel.setEnable(ebd_releveling.isSelected());
        bean_reLevel.setSensortype((Object) cbo_sensor_type.getSelectedItem());
        bean_reLevel.setTriggerHoldOffTime((Long)fmt_trigger_hold_off_time.getValue());
        bean_reLevel.setTimeLimit((Long)fmt_time_limit.getValue());
        bean_reLevel.setRetryCount((Long)fmt_retry_count.getValue());
        return bean_reLevel;
    }


    public void setReLevelBean(ReLevelBean bean_reLevel) {
        this.ebd_releveling.setOriginSelected(bean_reLevel.getEnable() != null && bean_reLevel.getEnable() == true);
        this.cbo_sensor_type.setSelectedItem(bean_reLevel.getSensortype());
        this.fmt_trigger_hold_off_time.setOriginValue(bean_reLevel.getTriggerHoldOffTime());
        this.fmt_time_limit.setOriginValue(bean_reLevel.getTimeLimit());
        this.fmt_retry_count.setOriginValue(bean_reLevel.getRetryCount());
    }


	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == ebd_enable) {
			do_ebd_enable_stateChanged(e);
		}else if(e.getSource() == ebd_releveling) {
			do_ebd_releveling_stateChanged(e);
		}else if(e.getSource() == ebd_enable_0) {
			do_ebd_enable_0_stateChanged(e);
		}
	}
    
	protected void do_ebd_enable_stateChanged ( final ChangeEvent e ) {
        final boolean selected = ebd_enable.isSelected();
        lbl_holdoff_time.setEnabled( selected );
        fmt_holdoff_time.setEnabled( selected );
        lbl_tolerance.setEnabled( selected );
        fmt_tolerance.setEnabled( selected );
	}
	
	protected void do_ebd_enable_0_stateChanged ( final ChangeEvent e ) {
        final boolean selected = ebd_enable_0.isSelected();
        lbl_active_speed_threshold.setEnabled( selected );
        fmt_active_speed_threshold.setEnabled( selected );
	}
	
	protected void do_ebd_releveling_stateChanged ( final ChangeEvent e ) {
        final boolean selected = ebd_releveling.isSelected();
        lbl_sensor_type.setEnabled( selected );
        cbo_sensor_type.setEnabled( selected );
        lbl_trigger_hold_off_time.setEnabled( selected );
        fmt_trigger_hold_off_time.setEnabled( selected );
        lbl_time_limit.setEnabled( selected );
        fmt_time_limit.setEnabled( selected );
        lbl_retry_count.setEnabled( selected );
        fmt_retry_count.setEnabled( selected );
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btn_reload_nv_data_default) {
			if(  JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "Warning_desc" ), TEXT.getString( "Warning_title" ),
	                JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {
				if ( settingPanel instanceof AdvancedSetting )
					( ( AdvancedSetting )settingPanel ).reloadDefaultNVRAM();
				
				StartUI.getTopMain().push(HomePanel.build( DashboardPanel.class ));
			}
		}
	}
    
	public enum OpenBrakeMonitor {
		OPEN_COUNT_LOCKED((byte)1),
		OPEN_AT_ONCE_LOCKED((byte)2);
		
		private final byte code;
		
		private OpenBrakeMonitor(byte c){
			this.code = c;
		}
		
		public byte getCode () {
	        return this.code;
	    }
		
		public String toString () {
	        return TEXT.getString( name() );
	    }
	}
	
	public enum RunningBrakeMonitor {
		NEAR_STOP_LOCKED((byte)4),
		EMERGENCY_STOP_LOCKED((byte)8);
		
		private final byte code;
		
		private RunningBrakeMonitor(byte c){
			this.code = c;
		}
		
		public byte getCode () {
	        return this.code;
	    }
		
		public String toString () {
	        return TEXT.getString( name() );
	    }
	}
	
	public enum StopBrakeMonitor {
		STOP_COUNT_LOCKED((byte)16),
	    STOP_AT_ONCE_LOCKED((byte)32);
		
		private final byte code;
		
		private StopBrakeMonitor(byte c){
			this.code = c;
		}
		
		public byte getCode () {
	        return this.code;
	    }
		
		public String toString () {
	        return TEXT.getString( name() );
	    }
	}
	
	public enum StandbyBrakeMonitor {
		STANDBY_AT_ONCE_LOCKED((byte)64),
		STANDBY_COUNT_LOCKED((byte)128);
		
		private final byte code;
		
		private StandbyBrakeMonitor(byte c){
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
