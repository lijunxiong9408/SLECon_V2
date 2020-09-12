package slecon.setting.setup.motion;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueTextField;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;





public class Timing extends JPanel {
    private static final long     serialVersionUID = -1538742280253939835L;
    private static ResourceBundle bundle = ToolBox.getResourceBundle( "setting.motion.Timing" );




    /* ---------------------------------------------------------------------------- */
    private boolean              started = false;
    private SettingPanel<Timing> settingPanel;
    private JLabel 				 cpt_fault_tripping;
    private JLabel 				 chk_car_run_over_time;
    private ValueTextField 		 fmt_car_run_over_time;
    private JLabel 				 chk_udz_ldz_toggle_timeout;
    private ValueTextField 		 fmt_udz_ldz_toggle_timeout;
    private JLabel 				 chk_driver_enable_fault_holdoff;
    private ValueTextField 		 fmt_driver_enable_fault_holdoff;
    private JLabel 				 chk_brake_jam_time_ratio;
    private ValueTextField 		 fmt_brake_jam_time_ratio;
    private JLabel 				 lbl_shaft_limit_over_speed_hold_off;
    private ValueTextField 		 fmt_shaft_limit_over_speed_hold_off;
    private JLabel 				 lbl_bad_usl_lsl_hold_off;
    private ValueTextField 		 fmt_bad_usl_lsl_hold_off;
    private JLabel 				 lbl_brake_jam_time_limit;
    private ValueTextField 		 fmt_brake_jam_time_limit;


    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_stablization_delay;
    private JLabel         lbl_idle_stable_delay;
    private ValueTextField fmt_idle_stable_delay;
    private JLabel         lbl_emergency_stop_holdoff;
    private ValueTextField fmt_emergency_stop_holdoff;
    private JLabel         lbl_exit_dcs_inspection_holdoff;
    private ValueTextField fmt_exit_dcs_inspection_holdoff;
    private JLabel         lbl_safety_chain_good_delay;
    private ValueTextField fmt_safety_chain_good_delay;

    


    public Timing () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<Timing> panel ) {
        this.settingPanel = panel;
    }

    
    void SetWidgetEnable(boolean enable) {
    	chk_car_run_over_time.setEnabled(enable);
    	fmt_car_run_over_time.setEnabled(enable);
    	chk_udz_ldz_toggle_timeout.setEnabled(enable);
    	fmt_udz_ldz_toggle_timeout.setEnabled(enable);
    	chk_driver_enable_fault_holdoff.setEnabled(enable);
    	fmt_driver_enable_fault_holdoff.setEnabled(enable);
    	chk_brake_jam_time_ratio.setEnabled(enable);
    	fmt_brake_jam_time_ratio.setEnabled(enable);
    	
    	lbl_shaft_limit_over_speed_hold_off.setEnabled(enable);
    	fmt_shaft_limit_over_speed_hold_off.setEnabled(enable);
    	lbl_bad_usl_lsl_hold_off.setEnabled(enable);
    	fmt_bad_usl_lsl_hold_off.setEnabled(enable);
    	lbl_brake_jam_time_limit.setEnabled(enable);
    	fmt_brake_jam_time_limit.setEnabled(enable);
    	
    	lbl_idle_stable_delay.setEnabled(enable);
    	fmt_idle_stable_delay.setEnabled(enable);
    	lbl_emergency_stop_holdoff.setEnabled(enable);
    	fmt_emergency_stop_holdoff.setEnabled(enable);
    	lbl_exit_dcs_inspection_holdoff.setEnabled(enable);
    	fmt_exit_dcs_inspection_holdoff.setEnabled(enable);
    	lbl_safety_chain_good_delay.setEnabled(enable);
    	fmt_safety_chain_good_delay.setEnabled(enable);
    	
    }
    
    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][200::200][150::150][]" ) );

        cpt_fault_tripping = new JLabel();
        chk_car_run_over_time = new JLabel();
        fmt_car_run_over_time = new ValueTextField();
        chk_udz_ldz_toggle_timeout = new JLabel();
        fmt_udz_ldz_toggle_timeout = new ValueTextField();
        chk_driver_enable_fault_holdoff = new JLabel();
        fmt_driver_enable_fault_holdoff = new ValueTextField();
        chk_brake_jam_time_ratio = new JLabel();
        fmt_brake_jam_time_ratio = new ValueTextField();
        lbl_shaft_limit_over_speed_hold_off = new JLabel();
        fmt_shaft_limit_over_speed_hold_off = new ValueTextField();
        lbl_bad_usl_lsl_hold_off = new JLabel();
        fmt_bad_usl_lsl_hold_off = new ValueTextField();
        lbl_brake_jam_time_limit = new JLabel();
        fmt_brake_jam_time_limit = new ValueTextField();

        setCaptionStyle(cpt_fault_tripping);
        // @CompoentSetting<Chk/Fmt>( chk_car_run_over_time , fmt_car_run_over_time )
        setTextLabelStyle(chk_car_run_over_time);
        fmt_car_run_over_time.setColumns(10);
        fmt_car_run_over_time.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_car_run_over_time.setScope(Long.class, 0L, 300L, true, true);
        fmt_car_run_over_time.setEmptyValue(100L);
        // @CompoentSetting<Chk/Fmt>( chk_udz_ldz_toggle_timeout , fmt_udz_ldz_toggle_timeout )
        setTextLabelStyle(chk_udz_ldz_toggle_timeout);
        fmt_udz_ldz_toggle_timeout.setColumns(10);
        fmt_udz_ldz_toggle_timeout.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_udz_ldz_toggle_timeout.setScope(Long.class, 0L, 60000L, true, true);
        fmt_udz_ldz_toggle_timeout.setEmptyValue(1000L);
        // @CompoentSetting<Chk/Fmt>( chk_driver_enable_fault_holdoff , fmt_driver_enable_fault_holdoff )
        setTextLabelStyle(chk_driver_enable_fault_holdoff);
        fmt_driver_enable_fault_holdoff.setColumns(10);
        fmt_driver_enable_fault_holdoff.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_driver_enable_fault_holdoff.setScope(Long.class, 0L, 60000L, true, true);
        fmt_driver_enable_fault_holdoff.setEmptyValue(1000L);
        // @CompoentSetting<Chk/Fmt>( chk_brake_jam_time_ratio , fmt_brake_jam_time_ratio )
        setTextLabelStyle(chk_brake_jam_time_ratio);
        fmt_brake_jam_time_ratio.setColumns(10);
        fmt_brake_jam_time_ratio.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_brake_jam_time_ratio.setScope(Long.class, 0L, 100L, true, true);
        fmt_brake_jam_time_ratio.setEmptyValue(50L);
        // @CompoentSetting<Fmt>( lbl_shaft_limit_over_speed_hold_off , fmt_shaft_limit_over_speed_hold_off )
        setTextLabelStyle(lbl_shaft_limit_over_speed_hold_off);
        fmt_shaft_limit_over_speed_hold_off.setColumns(10);
        fmt_shaft_limit_over_speed_hold_off.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_shaft_limit_over_speed_hold_off.setScope(Long.class, 20L, 100L, true, true);
        fmt_shaft_limit_over_speed_hold_off.setEmptyValue(1000L);
        // @CompoentSetting<Fmt>( lbl_bad_usl_lsl_hold_off , fmt_bad_usl_lsl_hold_off )
        setTextLabelStyle(lbl_bad_usl_lsl_hold_off);
        fmt_bad_usl_lsl_hold_off.setColumns(10);
        fmt_bad_usl_lsl_hold_off.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_bad_usl_lsl_hold_off.setScope(Long.class, 0L, 200L, true, true);
        fmt_bad_usl_lsl_hold_off.setEmptyValue(1000L);
        // @CompoentSetting<Fmt>( lbl_brake_jam_time_limit , fmt_brake_jam_time_limit )
        setTextLabelStyle(lbl_brake_jam_time_limit);
        fmt_brake_jam_time_limit.setColumns(10);
        fmt_brake_jam_time_limit.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_brake_jam_time_limit.setScope(Long.class, 0L, 60000L, true, true);
        fmt_brake_jam_time_limit.setEmptyValue(1000L);

        add(cpt_fault_tripping, "gapbottom 18-12, span, top");
        Box vbox_title = Box.createVerticalBox();
        vbox_title.add( chk_car_run_over_time);
        vbox_title.add( Box.createVerticalStrut(19));
        vbox_title.add( chk_udz_ldz_toggle_timeout);
        vbox_title.add( Box.createVerticalStrut(19));
        vbox_title.add( chk_driver_enable_fault_holdoff);
        vbox_title.add( Box.createVerticalStrut(19));
        vbox_title.add( chk_brake_jam_time_ratio);
        vbox_title.add( Box.createVerticalStrut(19));
        vbox_title.add( lbl_shaft_limit_over_speed_hold_off);
        vbox_title.add( Box.createVerticalStrut(19));
        vbox_title.add( lbl_bad_usl_lsl_hold_off);
        vbox_title.add( Box.createVerticalStrut(19));
        vbox_title.add( lbl_brake_jam_time_limit);
        
        Box vbox_value = Box.createVerticalBox();
        vbox_value.add( fmt_car_run_over_time );
        vbox_value.add( Box.createVerticalStrut(17));
        vbox_value.add( fmt_udz_ldz_toggle_timeout );
        vbox_value.add( Box.createVerticalStrut(17));
        vbox_value.add( fmt_driver_enable_fault_holdoff );
        vbox_value.add( Box.createVerticalStrut(17));
        vbox_value.add( fmt_brake_jam_time_ratio );
        vbox_value.add( Box.createVerticalStrut(17));
        vbox_value.add( fmt_shaft_limit_over_speed_hold_off );
        vbox_value.add( Box.createVerticalStrut(17));
        vbox_value.add( fmt_bad_usl_lsl_hold_off );
        vbox_value.add( Box.createVerticalStrut(17));
        vbox_value.add( fmt_brake_jam_time_limit );

        add(vbox_title, "skip 2, span 1, left, top");
        add(vbox_value, "span 1, wrap 30, left, top");

        /* ---------------------------------------------------------------------------- */
        cpt_stablization_delay          = new JLabel();
        lbl_idle_stable_delay           = new JLabel();
        fmt_idle_stable_delay           = new ValueTextField();
        lbl_emergency_stop_holdoff      = new JLabel();
        fmt_emergency_stop_holdoff      = new ValueTextField();
        lbl_exit_dcs_inspection_holdoff = new JLabel();
        fmt_exit_dcs_inspection_holdoff = new ValueTextField();
        lbl_safety_chain_good_delay     = new JLabel();
        fmt_safety_chain_good_delay     = new ValueTextField();
        fmt_safety_chain_good_delay.setEnabled(false);
        setCaptionStyle( cpt_stablization_delay );

        // @CompoentSetting<Fmt>( lbl_idle_stable_delay , fmt_idle_stable_delay )
        setTextLabelStyle( lbl_idle_stable_delay );
        fmt_idle_stable_delay.setColumns( 10 );
        fmt_idle_stable_delay.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_idle_stable_delay.setScope( Long.class, 0L, 60000L, false, false );
        fmt_idle_stable_delay.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_emergency_stop_holdoff , fmt_emergency_stop_holdoff )
        setTextLabelStyle( lbl_emergency_stop_holdoff );
        fmt_emergency_stop_holdoff.setColumns( 10 );
        fmt_emergency_stop_holdoff.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_emergency_stop_holdoff.setScope( Long.class, 0L, 60000L, false, false );
        fmt_emergency_stop_holdoff.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_exit_dcs_inspection_holdoff , fmt_exit_dcs_inspection_holdoff )
        setTextLabelStyle( lbl_exit_dcs_inspection_holdoff );
        fmt_exit_dcs_inspection_holdoff.setColumns( 10 );
        fmt_exit_dcs_inspection_holdoff.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_exit_dcs_inspection_holdoff.setScope( Long.class, 0L, 60000L, false, false );
        fmt_exit_dcs_inspection_holdoff.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_safety_chain_good_delay , fmt_safety_chain_good_delay )
        setTextLabelStyle( lbl_safety_chain_good_delay );
        fmt_safety_chain_good_delay.setColumns( 10 );
        fmt_safety_chain_good_delay.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_safety_chain_good_delay.setScope( Long.class, 0L, 60000L, false, false );
        fmt_safety_chain_good_delay.setEmptyValue( 1000L );
        add( cpt_stablization_delay, "gapbottom 18-12, span, aligny center, top" );
        Box vbox_title1 = Box.createVerticalBox();
        vbox_title1.add( lbl_idle_stable_delay);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_emergency_stop_holdoff);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_exit_dcs_inspection_holdoff);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_safety_chain_good_delay);
        
        Box vbox_value1 = Box.createVerticalBox();
        vbox_value1.add( fmt_idle_stable_delay );
        vbox_value1.add( Box.createVerticalStrut(13));
        vbox_value1.add( fmt_emergency_stop_holdoff );
        vbox_value1.add( Box.createVerticalStrut(13));
        vbox_value1.add( fmt_exit_dcs_inspection_holdoff );
        vbox_value1.add( Box.createVerticalStrut(13));
        vbox_value1.add( fmt_safety_chain_good_delay );
        add(vbox_title1, "skip 2, span 1, left, top");
        add(vbox_value1, "span 1, wrap 30, left, top");

        /* ---------------------------------------------------------------------------- */
        bindGroup("CarRunOverTime", chk_car_run_over_time, fmt_car_run_over_time);
        bindGroup("UdzLdzToggleTimeout", chk_udz_ldz_toggle_timeout, fmt_udz_ldz_toggle_timeout);
        bindGroup("DriverEnableFaultHoldoff", chk_driver_enable_fault_holdoff, fmt_driver_enable_fault_holdoff);
        bindGroup("BrakeJamTimeRatio", chk_brake_jam_time_ratio, fmt_brake_jam_time_ratio);
        bindGroup("ShaftLimitOverSpeedHoldOff", lbl_shaft_limit_over_speed_hold_off, fmt_shaft_limit_over_speed_hold_off);
        bindGroup("BadUslLslHoldOff", lbl_bad_usl_lsl_hold_off, fmt_bad_usl_lsl_hold_off);
        bindGroup("BrakeJamTimeLimit", lbl_brake_jam_time_limit, fmt_brake_jam_time_limit);

        bindGroup( "IdleStableDelay", lbl_idle_stable_delay, fmt_idle_stable_delay );
        bindGroup( "EmergencyStopHoldoff", lbl_emergency_stop_holdoff, fmt_emergency_stop_holdoff );
        bindGroup( "ExitDcsInspectionHoldoff", lbl_exit_dcs_inspection_holdoff, fmt_exit_dcs_inspection_holdoff );
        bindGroup( "SafetyChainGoodDelay", lbl_safety_chain_good_delay, fmt_safety_chain_good_delay );
        loadI18N();
        SetWidgetEnable(false);
        revalidate();
    }


    private void loadI18N () {
        cpt_fault_tripping.setText( getBundleText( "LBL_cpt_fault_tripping", "Fault tripping" ) );
        chk_car_run_over_time.setText(getBundleText("LBL_chk_car_run_over_time", "Car run over-time"));
        chk_udz_ldz_toggle_timeout.setText(getBundleText("LBL_chk_udz_ldz_toggle_timeout", "UDZ/LDZ toggle timeout"));
        chk_driver_enable_fault_holdoff.setText(getBundleText("LBL_chk_driver_enable_fault_holdoff", "Driver enable fault holdoff"));
        chk_brake_jam_time_ratio.setText(getBundleText("LBL_chk_brake_jam_time_ratio", "Brake jam time ratio"));
        lbl_shaft_limit_over_speed_hold_off.setText(getBundleText("LBL_lbl_shaft_limit_over_speed_hold_off", "Shaft-limit over speed hold-off"));
        lbl_bad_usl_lsl_hold_off.setText(getBundleText("LBL_lbl_bad_usl_lsl_hold_off", "Bad USL/LSL hold-off"));
        lbl_brake_jam_time_limit.setText(getBundleText("LBL_lbl_brake_jam_time_limit", "Brake jam time limit"));

        /* ---------------------------------------------------------------------------- */
        cpt_stablization_delay.setText( getBundleText( "LBL_cpt_stablization_delay", "Stablization delay" ) );
        lbl_idle_stable_delay.setText( getBundleText( "LBL_lbl_idle_stable_delay", "Idle stable delay" ) );
        lbl_emergency_stop_holdoff.setText( getBundleText( "LBL_lbl_emergency_stop_holdoff", "Emergency stop holdoff" ) );
        lbl_exit_dcs_inspection_holdoff.setText( getBundleText( "LBL_lbl_exit_dcs_inspection_holdoff", "Exit DCS inspection holdoff" ) );
        lbl_safety_chain_good_delay.setText( getBundleText( "LBL_lbl_safety_chain_good_delay", "Safety chain good delay" ) );

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
                                    detailText = bundle.getString( "Description_" + detailKey );
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


    public StablizationDelayBean getStablizationDelayBean () throws ConvertException {
        if ( ! fmt_idle_stable_delay.checkValue() )
            throw new ConvertException();
        if ( ! fmt_emergency_stop_holdoff.checkValue() )
            throw new ConvertException();
        if ( ! fmt_exit_dcs_inspection_holdoff.checkValue() )
            throw new ConvertException();
        if ( ! fmt_safety_chain_good_delay.checkValue() )
            throw new ConvertException();

        StablizationDelayBean bean_stablizationDelay = new StablizationDelayBean();
        bean_stablizationDelay.setIdleStableDelay( ( Long )fmt_idle_stable_delay.getValue() );
        bean_stablizationDelay.setEmergencyStopHoldoff( ( Long )fmt_emergency_stop_holdoff.getValue() );
        bean_stablizationDelay.setExitDcsInspectionHoldoff( ( Long )fmt_exit_dcs_inspection_holdoff.getValue() );
        bean_stablizationDelay.setSafetyChainGoodDelay( ( Long )fmt_safety_chain_good_delay.getValue() );
        return bean_stablizationDelay;
    }



    public FaultTrippingBean getFaultTrippingBean() throws ConvertException {
        if ( !fmt_car_run_over_time.checkValue())
            throw new ConvertException();
        if ( !fmt_udz_ldz_toggle_timeout.checkValue())
            throw new ConvertException();
        if ( !fmt_driver_enable_fault_holdoff.checkValue())
            throw new ConvertException();
        if ( !fmt_brake_jam_time_ratio.checkValue())
            throw new ConvertException();
        if (!fmt_shaft_limit_over_speed_hold_off.checkValue())
            throw new ConvertException();
        if (!fmt_bad_usl_lsl_hold_off.checkValue())
            throw new ConvertException();

        FaultTrippingBean bean_faultTripping = new FaultTrippingBean();
        bean_faultTripping.setCarRunOverTime((Long) fmt_car_run_over_time.getValue());
        bean_faultTripping.setUdzLdzToggleTimeout((Long) fmt_udz_ldz_toggle_timeout.getValue());
        bean_faultTripping.setDriverEnableFaultHoldoff((Long) fmt_driver_enable_fault_holdoff.getValue());
        bean_faultTripping.setBrakeJamTimeRatio((Long) fmt_brake_jam_time_ratio.getValue());
        bean_faultTripping.setShaftLimitOverSpeedHoldOff((Long) fmt_shaft_limit_over_speed_hold_off.getValue());
        bean_faultTripping.setBadUslLslHoldOff((Long) fmt_bad_usl_lsl_hold_off.getValue());
        bean_faultTripping.setBrakeJamTimeLimit((Long) fmt_brake_jam_time_limit.getValue());
        return bean_faultTripping;
    }


    public void setFaultTrippingBean(FaultTrippingBean bean_faultTripping) {
//      TODO  this.chk_car_run_over_time.setOriginSelected(bean_faultTripping.getCarRunOverTimeEnabled());
        this.fmt_car_run_over_time.setOriginValue(bean_faultTripping.getCarRunOverTime());
//      TODO  this.chk_udz_ldz_toggle_timeout.setOriginSelected(bean_faultTripping.getUdzLdzToggleTimeoutEnabled());
        this.fmt_udz_ldz_toggle_timeout.setOriginValue(bean_faultTripping.getUdzLdzToggleTimeout());
//      TODO  this.chk_driver_enable_fault_holdoff.setOriginSelected(bean_faultTripping.getDriverEnableFaultHoldoffEnabled());
        this.fmt_driver_enable_fault_holdoff.setOriginValue(bean_faultTripping.getDriverEnableFaultHoldoff());
//      TODO  this.chk_brake_jam_time_ratio.setOriginSelected(bean_faultTripping.getBrakeJamTimeRatioEnabled());
        this.fmt_brake_jam_time_ratio.setOriginValue(bean_faultTripping.getBrakeJamTimeRatio());
        this.fmt_shaft_limit_over_speed_hold_off.setOriginValue(bean_faultTripping.getShaftLimitOverSpeedHoldOff());
        this.fmt_bad_usl_lsl_hold_off.setOriginValue(bean_faultTripping.getBadUslLslHoldOff());
        this.fmt_brake_jam_time_limit.setOriginValue(bean_faultTripping.getBrakeJamTimeLimit());
    }


    public void setStablizationDelayBean ( StablizationDelayBean bean_stablizationDelay ) {
        this.fmt_idle_stable_delay.setOriginValue( bean_stablizationDelay.getIdleStableDelay() );
        this.fmt_emergency_stop_holdoff.setOriginValue( bean_stablizationDelay.getEmergencyStopHoldoff() );
        this.fmt_exit_dcs_inspection_holdoff.setOriginValue( bean_stablizationDelay.getExitDcsInspectionHoldoff() );
        this.fmt_safety_chain_good_delay.setOriginValue( bean_stablizationDelay.getSafetyChainGoodDelay() );
    }


    public String getBundleText ( String key, String defaultValue ) {
        String result;
        try {
            result = bundle.getString( key );
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


    public static Timing createPanel ( SettingPanel<Timing> panel ) {
        Timing gui = new Timing();
        gui.setSettingPanel( panel );
        return gui;
    }



    public static class FaultTrippingBean {
        private Long carRunOverTime;
        private boolean carRunOverTime_enabled;
        private Long udzLdzToggleTimeout;
        private boolean udzLdzToggleTimeout_enabled;
        private Long driverEnableFaultHoldoff;
        private boolean driverEnableFaultHoldoff_enabled;
        private Long brakeJamTimeRatio;
        private boolean brakeJamTimeRatio_enabled;
        private Long shaftLimitOverSpeedHoldOff;
        private Long badUslLslHoldOff;
        private Long brakeJamTimeLimit;

        
        
        
        public Long getCarRunOverTime() {
            return this.carRunOverTime;
        }

        public boolean getCarRunOverTimeEnabled() {
            return this.carRunOverTime_enabled;
        }

        public Long getUdzLdzToggleTimeout() {
            return this.udzLdzToggleTimeout;
        }

        public boolean getUdzLdzToggleTimeoutEnabled() {
            return this.udzLdzToggleTimeout_enabled;
        }

        public Long getDriverEnableFaultHoldoff() {
            return this.driverEnableFaultHoldoff;
        }

        public boolean getDriverEnableFaultHoldoffEnabled() {
            return this.driverEnableFaultHoldoff_enabled;
        }

        public Long getBrakeJamTimeRatio() {
            return this.brakeJamTimeRatio;
        }

        public boolean getBrakeJamTimeRatioEnabled() {
            return this.brakeJamTimeRatio_enabled;
        }

        public Long getShaftLimitOverSpeedHoldOff() {
            return this.shaftLimitOverSpeedHoldOff;
        }

        public Long getBadUslLslHoldOff() {
            return this.badUslLslHoldOff;
        }

        public Long getBrakeJamTimeLimit() {
            return brakeJamTimeLimit;
        }

        public void setCarRunOverTime(Long carRunOverTime) {
            this.carRunOverTime = carRunOverTime;
        }

        public void setCarRunOverTimeEnabled(boolean enabled) {
            this.carRunOverTime_enabled = enabled;
        }

        public void setUdzLdzToggleTimeout(Long udzLdzToggleTimeout) {
            this.udzLdzToggleTimeout = udzLdzToggleTimeout;
        }

        public void setUdzLdzToggleTimeoutEnabled(boolean enabled) {
            this.udzLdzToggleTimeout_enabled = enabled;
        }

        public void setDriverEnableFaultHoldoff(Long driverEnableFaultHoldoff) {
            this.driverEnableFaultHoldoff = driverEnableFaultHoldoff;
        }

        public void setDriverEnableFaultHoldoffEnabled(boolean enabled) {
            this.driverEnableFaultHoldoff_enabled = enabled;
        }

        public void setBrakeJamTimeRatio(Long brakeJamTimeRatio) {
            this.brakeJamTimeRatio = brakeJamTimeRatio;
        }

        public void setBrakeJamTimeRatioEnabled(boolean enabled) {
            this.brakeJamTimeRatio_enabled = enabled;
        }

        public void setShaftLimitOverSpeedHoldOff(Long shaftLimitOverSpeedHoldOff) {
            this.shaftLimitOverSpeedHoldOff = shaftLimitOverSpeedHoldOff;
        }

        public void setBadUslLslHoldOff(Long badUslLslHoldOff) {
            this.badUslLslHoldOff = badUslLslHoldOff;
        }

        public void setBrakeJamTimeLimit(Long brakeJamTimeLimit) {
            this.brakeJamTimeLimit = brakeJamTimeLimit;
        }
    }




    public static class StablizationDelayBean {
        private Long idleStableDelay;
        private Long emergencyStopHoldoff;
        private Long exitDcsInspectionHoldoff;
        private Long safetyChainGoodDelay;




        public Long getIdleStableDelay () {
            return this.idleStableDelay;
        }


        public Long getEmergencyStopHoldoff () {
            return this.emergencyStopHoldoff;
        }


        public Long getExitDcsInspectionHoldoff () {
            return this.exitDcsInspectionHoldoff;
        }


        public Long getSafetyChainGoodDelay () {
            return this.safetyChainGoodDelay;
        }


        public void setIdleStableDelay ( Long idleStableDelay ) {
            this.idleStableDelay = idleStableDelay;
        }


        public void setEmergencyStopHoldoff ( Long emergencyStopHoldoff ) {
            this.emergencyStopHoldoff = emergencyStopHoldoff;
        }


        public void setExitDcsInspectionHoldoff ( Long exitDcsInspectionHoldoff ) {
            this.exitDcsInspectionHoldoff = exitDcsInspectionHoldoff;
        }


        public void setSafetyChainGoodDelay ( Long safetyChainGoodDelay ) {
            this.safetyChainGoodDelay = safetyChainGoodDelay;
        }
    }
}
