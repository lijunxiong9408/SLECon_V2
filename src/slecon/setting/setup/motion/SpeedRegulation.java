package slecon.setting.setup.motion;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;
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
import slecon.component.ValueTextField;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;





public class SpeedRegulation extends JPanel {
    private static final long     serialVersionUID = 1121115966128682981L;
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.motion.SpeedRegulation" );
    



    /* ---------------------------------------------------------------------------- */
    private boolean                       started = false;
    private SettingPanel<SpeedRegulation> settingPanel;
    private JLabel                        cpt_speed_checking;
    private JLabel                        lbl_over_speed_threshold;
    private ValueTextField                fmt_over_speed_threshold;
    private JLabel                        lbl_over_speed_time_limit;
    private ValueTextField                fmt_over_speed_time_limit;
    private JLabel                        lbl_speed_loss_threshold;
    private ValueTextField                fmt_speed_loss_threshold;
    private JLabel                        lbl_speed_loss_time_limit;
    private ValueTextField                fmt_speed_loss_time_limit;
    private JLabel                        lbl_acceleration_speed_loss_threshold;
    private ValueTextField                fmt_acceleration_speed_loss_threshold;
    private JLabel                        lbl_acceleration_speed_loss_time_limit;
    private ValueTextField                fmt_acceleration_speed_loss_time_limit;
    private JLabel                        lbl_usl_lsl_over_speed_threshold;
    private ValueTextField                fmt_usl_lsl_over_speed_threshold;
    private JLabel                        lbl_speed_check_upper_threshold;
    private ValueTextField                fmt_speed_check_upper_threshold;
    private JLabel                        lbl_speed_check_lower_threshold;
    private ValueTextField                fmt_speed_check_lower_threshold;
    private JLabel                        lbl_sc1_speed_check_upper_threshold;
    private ValueTextField                fmt_sc1_speed_check_upper_threshold;
    private JLabel                        lbl_sc1_speed_check_lower_threshold;
    private ValueTextField                fmt_sc1_speed_check_lower_threshold;
    private JLabel                        lbl_sc2_speed_check_upper_threshold;
    private ValueTextField                fmt_sc2_speed_check_upper_threshold;
    private JLabel                        lbl_sc2_speed_check_lower_threshold;
    private ValueTextField                fmt_sc2_speed_check_lower_threshold;
    private JLabel                        lbl_sc3_speed_check_upper_threshold;
    private ValueTextField                fmt_sc3_speed_check_upper_threshold;
    private JLabel                        lbl_sc3_speed_check_lower_threshold;
    private ValueTextField                fmt_sc3_speed_check_lower_threshold;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_speed_output;
    private JLabel         lbl_reference_speed_analog_output_offset;
    private ValueTextField fmt_reference_speed_analog_output_offset;
    private JLabel label;


    void SetWidgetEnable(boolean enable) {
    	lbl_over_speed_threshold.setEnabled(enable);
    	fmt_over_speed_threshold.setEnabled(enable);
    	lbl_over_speed_time_limit.setEnabled(enable);
    	fmt_over_speed_time_limit.setEnabled(enable);
    	lbl_speed_loss_threshold.setEnabled(enable);
    	fmt_speed_loss_threshold.setEnabled(enable);
    	lbl_speed_loss_time_limit.setEnabled(enable);
    	fmt_speed_loss_time_limit.setEnabled(enable);
    	lbl_acceleration_speed_loss_threshold.setEnabled(enable);
    	fmt_acceleration_speed_loss_threshold.setEnabled(enable);
    	lbl_acceleration_speed_loss_time_limit.setEnabled(enable);
    	fmt_acceleration_speed_loss_time_limit.setEnabled(enable);
    	lbl_usl_lsl_over_speed_threshold.setEnabled(enable);
    	fmt_usl_lsl_over_speed_threshold.setEnabled(enable);
    	lbl_speed_check_upper_threshold.setEnabled(enable);
    	fmt_speed_check_upper_threshold.setEnabled(enable);
    	lbl_speed_check_lower_threshold.setEnabled(enable);
    	fmt_speed_check_lower_threshold.setEnabled(enable);
    	lbl_sc1_speed_check_upper_threshold.setEnabled(enable);
    	fmt_sc1_speed_check_upper_threshold.setEnabled(enable);
    	lbl_sc1_speed_check_lower_threshold.setEnabled(enable);
    	fmt_sc1_speed_check_lower_threshold.setEnabled(enable);
    	lbl_sc2_speed_check_upper_threshold.setEnabled(enable);
    	fmt_sc2_speed_check_upper_threshold.setEnabled(enable);
    	lbl_sc2_speed_check_lower_threshold.setEnabled(enable);
    	fmt_sc2_speed_check_lower_threshold.setEnabled(enable);
    	lbl_sc3_speed_check_upper_threshold.setEnabled(enable);
    	fmt_sc3_speed_check_upper_threshold.setEnabled(enable);
    	lbl_sc3_speed_check_lower_threshold.setEnabled(enable);
    	fmt_sc3_speed_check_lower_threshold.setEnabled(enable);
    	
    	lbl_reference_speed_analog_output_offset.setEnabled(enable);
    	fmt_reference_speed_analog_output_offset.setEnabled(enable);
    	
    }

    public SpeedRegulation () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<SpeedRegulation> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][200::200][150::150][]" ) );
        cpt_speed_checking                     = new JLabel();
        lbl_over_speed_threshold               = new JLabel();
        fmt_over_speed_threshold               = new ValueTextField();
        lbl_over_speed_time_limit              = new JLabel();
        fmt_over_speed_time_limit              = new ValueTextField();
        lbl_speed_loss_threshold               = new JLabel();
        fmt_speed_loss_threshold               = new ValueTextField();
        lbl_speed_loss_time_limit              = new JLabel();
        fmt_speed_loss_time_limit              = new ValueTextField();
        lbl_acceleration_speed_loss_threshold  = new JLabel();
        fmt_acceleration_speed_loss_threshold  = new ValueTextField();
        lbl_acceleration_speed_loss_time_limit = new JLabel();
        fmt_acceleration_speed_loss_time_limit = new ValueTextField();
        lbl_usl_lsl_over_speed_threshold       = new JLabel();
        fmt_usl_lsl_over_speed_threshold       = new ValueTextField();
        lbl_speed_check_upper_threshold        = new JLabel();
        fmt_speed_check_upper_threshold        = new ValueTextField();
        lbl_speed_check_lower_threshold        = new JLabel();
        fmt_speed_check_lower_threshold        = new ValueTextField();
        lbl_sc1_speed_check_upper_threshold	   = new JLabel();
        fmt_sc1_speed_check_upper_threshold	   = new ValueTextField();
        lbl_sc1_speed_check_lower_threshold	   = new JLabel();
        fmt_sc1_speed_check_lower_threshold	   = new ValueTextField();
        lbl_sc2_speed_check_upper_threshold	   = new JLabel();
        fmt_sc2_speed_check_upper_threshold	   = new ValueTextField();
        lbl_sc2_speed_check_lower_threshold	   = new JLabel();
        fmt_sc2_speed_check_lower_threshold	   = new ValueTextField();
        lbl_sc3_speed_check_upper_threshold	   = new JLabel();
        fmt_sc3_speed_check_upper_threshold	   = new ValueTextField();
        lbl_sc3_speed_check_lower_threshold	   = new JLabel();
        fmt_sc3_speed_check_lower_threshold	   = new ValueTextField();
        setCaptionStyle( cpt_speed_checking );
        setTextLabelStyle( lbl_over_speed_threshold );
        fmt_over_speed_threshold.setColumns( 10 );
        fmt_over_speed_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_over_speed_threshold.setScope( Double.class, 0D, 11000D, false, true );
        fmt_over_speed_threshold.setEmptyValue( 100D );

        setTextLabelStyle( lbl_over_speed_time_limit );
        fmt_over_speed_time_limit.setColumns( 10 );
        fmt_over_speed_time_limit.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_over_speed_time_limit.setScope( Long.class, 0L, 60000L, false, true );
        fmt_over_speed_time_limit.setEmptyValue( 100L );

        setTextLabelStyle( lbl_speed_loss_threshold );
        fmt_speed_loss_threshold.setColumns( 10 );
        fmt_speed_loss_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_speed_loss_threshold.setScope( Double.class, 0D, 5000D, false, true );
        fmt_speed_loss_threshold.setEmptyValue( 100D );

        setTextLabelStyle( lbl_speed_loss_time_limit );
        fmt_speed_loss_time_limit.setColumns( 10 );
        fmt_speed_loss_time_limit.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_speed_loss_time_limit.setScope( Long.class, 0L, 60000L, false, true );
        fmt_speed_loss_time_limit.setEmptyValue( 100L );

        setTextLabelStyle( lbl_acceleration_speed_loss_threshold );
        fmt_acceleration_speed_loss_threshold.setColumns( 10 );
        fmt_acceleration_speed_loss_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_acceleration_speed_loss_threshold.setScope( Long.class, 0L, 5000L, false, true );
        fmt_acceleration_speed_loss_threshold.setEmptyValue( 100L );

        setTextLabelStyle( lbl_acceleration_speed_loss_time_limit );
        fmt_acceleration_speed_loss_time_limit.setColumns( 10 );
        fmt_acceleration_speed_loss_time_limit.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_acceleration_speed_loss_time_limit.setScope( Long.class, 0L, 60000L, false, true );
        fmt_acceleration_speed_loss_time_limit.setEmptyValue( 100L );

        setTextLabelStyle( lbl_usl_lsl_over_speed_threshold );
        fmt_usl_lsl_over_speed_threshold.setColumns( 10 );
        fmt_usl_lsl_over_speed_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_usl_lsl_over_speed_threshold.setScope( Double.class, 0D, 10000D, false, true );
        fmt_usl_lsl_over_speed_threshold.setEmptyValue( 100D );

        setTextLabelStyle( lbl_speed_check_upper_threshold );
        fmt_speed_check_upper_threshold.setColumns( 10 );
        fmt_speed_check_upper_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_speed_check_upper_threshold.setScope( Double.class, 0D, 10000D, false, true );
        fmt_speed_check_upper_threshold.setEmptyValue( 100D );

        setTextLabelStyle( lbl_speed_check_lower_threshold );
        fmt_speed_check_lower_threshold.setColumns( 10 );
        fmt_speed_check_lower_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_speed_check_lower_threshold.setScope( Double.class, 0D, 10000D, false, true );
        fmt_speed_check_lower_threshold.setEmptyValue( 100D );
        
        setTextLabelStyle( lbl_sc1_speed_check_upper_threshold );
        fmt_sc1_speed_check_upper_threshold.setColumns( 10 );
        fmt_sc1_speed_check_upper_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_sc1_speed_check_upper_threshold.setScope( Double.class, 0D, 10000D, false, true );
        fmt_sc1_speed_check_upper_threshold.setEmptyValue( 100D );
        
        setTextLabelStyle( lbl_sc1_speed_check_lower_threshold );
        fmt_sc1_speed_check_lower_threshold.setColumns( 10 );
        fmt_sc1_speed_check_lower_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_sc1_speed_check_lower_threshold.setScope( Double.class, 0D, 10000D, false, true );
        fmt_sc1_speed_check_lower_threshold.setEmptyValue( 100D );
        
        setTextLabelStyle( lbl_sc2_speed_check_upper_threshold );
        fmt_sc2_speed_check_upper_threshold.setColumns( 10 );
        fmt_sc2_speed_check_upper_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_sc2_speed_check_upper_threshold.setScope( Double.class, 0D, 10000D, false, true );
        fmt_sc2_speed_check_upper_threshold.setEmptyValue( 100D );
        
        setTextLabelStyle( lbl_sc2_speed_check_lower_threshold );
        fmt_sc2_speed_check_lower_threshold.setColumns( 10 );
        fmt_sc2_speed_check_lower_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_sc2_speed_check_lower_threshold.setScope( Double.class, 0D, 10000D, false, true );
        fmt_sc2_speed_check_lower_threshold.setEmptyValue( 100D );
        
        setTextLabelStyle( lbl_sc3_speed_check_upper_threshold );
        fmt_sc3_speed_check_upper_threshold.setColumns( 10 );
        fmt_sc3_speed_check_upper_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_sc3_speed_check_upper_threshold.setScope( Double.class, 0D, 10000D, false, true );
        fmt_sc3_speed_check_upper_threshold.setEmptyValue( 100D );
        
        setTextLabelStyle( lbl_sc3_speed_check_lower_threshold );
        fmt_sc3_speed_check_lower_threshold.setColumns( 10 );
        fmt_sc3_speed_check_lower_threshold.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_sc3_speed_check_lower_threshold.setScope( Double.class, 0D, 10000D, false, true );
        fmt_sc3_speed_check_lower_threshold.setEmptyValue( 100D );
        
        add( cpt_speed_checking, "gapbottom 18-12, span, top" );
        Box vbox_title = Box.createVerticalBox();
        vbox_title.add( lbl_over_speed_threshold);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_over_speed_time_limit);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_speed_loss_threshold);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_speed_loss_time_limit);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_acceleration_speed_loss_threshold);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_acceleration_speed_loss_time_limit);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_usl_lsl_over_speed_threshold);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_speed_check_upper_threshold);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_speed_check_lower_threshold);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_sc1_speed_check_upper_threshold);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_sc1_speed_check_lower_threshold);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_sc2_speed_check_upper_threshold);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_sc2_speed_check_lower_threshold);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_sc3_speed_check_upper_threshold);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_sc3_speed_check_lower_threshold);
        
        Box vbox_value = Box.createVerticalBox();
        vbox_value.add( fmt_over_speed_threshold );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_over_speed_time_limit );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_speed_loss_threshold );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_speed_loss_time_limit );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_acceleration_speed_loss_threshold );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_acceleration_speed_loss_time_limit );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_usl_lsl_over_speed_threshold );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_speed_check_upper_threshold );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_speed_check_lower_threshold );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_sc1_speed_check_upper_threshold);
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_sc1_speed_check_lower_threshold);
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_sc2_speed_check_upper_threshold);
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_sc2_speed_check_lower_threshold);
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_sc3_speed_check_upper_threshold);
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_sc3_speed_check_lower_threshold);
        
        add(vbox_title, "skip 2, span, split 2, gapright 50, left, top");
        add(vbox_value, "wrap 30");

        /* ---------------------------------------------------------------------------- */
        cpt_speed_output                         = new JLabel();
        lbl_reference_speed_analog_output_offset = new JLabel();
        fmt_reference_speed_analog_output_offset = new ValueTextField();
        setCaptionStyle( cpt_speed_output );

        // @CompoentSetting<Fmt>( lbl_reference_speed_analog_output_offset , fmt_reference_speed_analog_output_offset )
        setTextLabelStyle( lbl_reference_speed_analog_output_offset );
        fmt_reference_speed_analog_output_offset.setColumns( 10 );
        fmt_reference_speed_analog_output_offset.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_reference_speed_analog_output_offset.setScope( Double.class, 0D, 1D, true, false );
        fmt_reference_speed_analog_output_offset.setEmptyValue( 0.0D );
        add( cpt_speed_output, "gapbottom 18-12, span, top" );
        add( lbl_reference_speed_analog_output_offset, "skip 2, span, split 2, gapright 30, left, top" );
        add( fmt_reference_speed_analog_output_offset, "wrap" );
        

        label = new JLabel("");
        setTextLabelStyle(label);
        Map attributes = label.getFont().getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        label.setFont(label.getFont().deriveFont(attributes));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(label, "skip 2, left, wrap 30, top");
        
        label.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                ((SpeedRegulationSetting)settingPanel).gotoCommissionPanel();
            }
        });
        
        /* ---------------------------------------------------------------------------- */
        bindGroup( "OverSpeedThreshold", lbl_over_speed_threshold, fmt_over_speed_threshold );
        bindGroup( "OverSpeedTimeLimit", lbl_over_speed_time_limit, fmt_over_speed_time_limit );
        bindGroup( "SpeedLossThreshold", lbl_speed_loss_threshold, fmt_speed_loss_threshold );
        bindGroup( "SpeedLossTimeLimit", lbl_speed_loss_time_limit, fmt_speed_loss_time_limit );
        bindGroup( "AccelerationSpeedLossThreshold", lbl_acceleration_speed_loss_threshold, fmt_acceleration_speed_loss_threshold );
        bindGroup( "AccelerationSpeedLossTimeLimit", lbl_acceleration_speed_loss_time_limit, fmt_acceleration_speed_loss_time_limit );
        bindGroup( "UslLslOverSpeedThreshold", lbl_usl_lsl_over_speed_threshold, fmt_usl_lsl_over_speed_threshold );
        bindGroup( "SpeedCheckUpperThreshold", lbl_speed_check_upper_threshold, fmt_speed_check_upper_threshold );
        bindGroup( "SpeedCheckLowerThreshold", lbl_speed_check_lower_threshold, fmt_speed_check_lower_threshold );
        bindGroup( "ReferenceSpeedAnalogOutputOffset", lbl_reference_speed_analog_output_offset, fmt_reference_speed_analog_output_offset );
        loadI18N();
        SetWidgetEnable(false);
        revalidate();
    }


    private void loadI18N () {
        cpt_speed_checking.setText( getBundleText( "LBL_cpt_speed_checking", "Speed Checking" ) );
        lbl_over_speed_threshold.setText( getBundleText( "LBL_lbl_over_speed_threshold", "Over-speed threshold" ) );
        lbl_over_speed_time_limit.setText( getBundleText( "LBL_lbl_over_speed_time_limit", "Over-speed time limit" ) );
        lbl_speed_loss_threshold.setText( getBundleText( "LBL_lbl_speed_loss_threshold", "Speed-loss threshold" ) );
        lbl_speed_loss_time_limit.setText( getBundleText( "LBL_lbl_speed_loss_time_limit", "Speed-loss time limit" ) );
        lbl_acceleration_speed_loss_threshold.setText( getBundleText( "LBL_lbl_acceleration_speed_loss_threshold",
                                                                      "Acceleration Speed-loss threshold" ) );
        lbl_acceleration_speed_loss_time_limit.setText( getBundleText( "LBL_lbl_acceleration_speed_loss_time_limit",
                                                                       "Acceleration Speed-loss time limit" ) );
        lbl_usl_lsl_over_speed_threshold.setText( getBundleText( "LBL_lbl_usl_lsl_over_speed_threshold", "USL/LSL over-speed threshold" ) );
        lbl_speed_check_upper_threshold.setText( getBundleText( "LBL_lbl_speed_check_upper_threshold", "Speed check upper threshold" ) );
        lbl_speed_check_lower_threshold.setText( getBundleText( "LBL_lbl_speed_check_lower_threshold", "Speed check lower threshold" ) );

        lbl_sc1_speed_check_upper_threshold.setText( getBundleText( "LBL_lbl_sc1_speed_check_upper_threshold", "SC1 Upper Threshold" ) );
        lbl_sc1_speed_check_lower_threshold.setText( getBundleText( "LBL_lbl_sc1_speed_check_lower_threshold", "SC1 Down Threshold" ) );
        lbl_sc2_speed_check_upper_threshold.setText( getBundleText( "LBL_lbl_sc2_speed_check_upper_threshold", "SC2 Upper Threshold" ) );
        lbl_sc2_speed_check_lower_threshold.setText( getBundleText( "LBL_lbl_sc2_speed_check_lower_threshold", "SC2 Down Threshold" ) );
        lbl_sc3_speed_check_upper_threshold.setText( getBundleText( "LBL_lbl_sc3_speed_check_upper_threshold", "SC3 Upper Threshold" ) );
        lbl_sc3_speed_check_lower_threshold.setText( getBundleText( "LBL_lbl_sc3_speed_check_lower_threshold", "SC3 Down Threshold" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_speed_output.setText( getBundleText( "LBL_cpt_speed_output", "Speed output" ) );
        lbl_reference_speed_analog_output_offset.setText( getBundleText( "LBL_lbl_reference_speed_analog_output_offset",
                                                                         "Reference speed analog output offset" ) );

        /* ---------------------------------------------------------------------------- */
        label.setText(getBundleText( "LBL_label", "Calibrate analog output") );
        
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


    public SpeedCheckingBean getSpeedCheckingBean () throws ConvertException {
        if ( ! fmt_over_speed_threshold.checkValue() )
            throw new ConvertException();
        if ( ! fmt_over_speed_time_limit.checkValue() )
            throw new ConvertException();
        if ( ! fmt_speed_loss_threshold.checkValue() )
            throw new ConvertException();
        if ( ! fmt_speed_loss_time_limit.checkValue() )
            throw new ConvertException();
        if ( ! fmt_acceleration_speed_loss_threshold.checkValue() )
            throw new ConvertException();
        if ( ! fmt_acceleration_speed_loss_time_limit.checkValue() )
            throw new ConvertException();
        if ( ! fmt_usl_lsl_over_speed_threshold.checkValue() )
            throw new ConvertException();
        if ( ! fmt_speed_check_upper_threshold.checkValue() )
            throw new ConvertException();
        if ( ! fmt_speed_check_lower_threshold.checkValue() )
            throw new ConvertException();

        SpeedCheckingBean bean_speedChecking = new SpeedCheckingBean();
        bean_speedChecking.setOverSpeedThreshold( ( Double )fmt_over_speed_threshold.getValue() );
        bean_speedChecking.setOverSpeedTimeLimit( ( Long )fmt_over_speed_time_limit.getValue() );
        bean_speedChecking.setSpeedLossThreshold( ( Double )fmt_speed_loss_threshold.getValue() );
        bean_speedChecking.setSpeedLossTimeLimit( ( Long )fmt_speed_loss_time_limit.getValue() );
        bean_speedChecking.setAccelerationSpeedLossThreshold( ( Long )fmt_acceleration_speed_loss_threshold.getValue() );
        bean_speedChecking.setAccelerationSpeedLossTimeLimit( ( Long )fmt_acceleration_speed_loss_time_limit.getValue() );
        bean_speedChecking.setUslLslOverSpeedThreshold( ( Double )fmt_usl_lsl_over_speed_threshold.getValue() );
        bean_speedChecking.setSpeedCheckUpperThreshold( ( Double )fmt_speed_check_upper_threshold.getValue() );
        bean_speedChecking.setSpeedCheckLowerThreshold( ( Double )fmt_speed_check_lower_threshold.getValue() );
        bean_speedChecking.setSc1_speedCheckUpperThreshold( (Double )fmt_sc1_speed_check_upper_threshold.getValue() );
        bean_speedChecking.setSc1_speedCheckLowerThreshold( (Double )fmt_sc1_speed_check_lower_threshold.getValue() );
        bean_speedChecking.setSc2_speedCheckUpperThreshold( (Double )fmt_sc2_speed_check_upper_threshold.getValue() );
        bean_speedChecking.setSc2_speedCheckLowerThreshold( (Double )fmt_sc2_speed_check_lower_threshold.getValue() );
        bean_speedChecking.setSc3_speedCheckUpperThreshold( (Double )fmt_sc3_speed_check_upper_threshold.getValue() );
        bean_speedChecking.setSc3_speedCheckLowerThreshold( (Double )fmt_sc3_speed_check_lower_threshold.getValue() );
        return bean_speedChecking;
    }


    public SpeedOutputBean getSpeedOutputBean () throws ConvertException {
        if ( ! fmt_reference_speed_analog_output_offset.checkValue() )
            throw new ConvertException();

        SpeedOutputBean bean_speedOutput = new SpeedOutputBean();
        bean_speedOutput.setReferenceSpeedAnalogOutputOffset( ( Double )fmt_reference_speed_analog_output_offset.getValue() );
        return bean_speedOutput;
    }


    public void setSpeedCheckingBean ( SpeedCheckingBean bean_speedChecking ) {
        this.fmt_over_speed_threshold.setOriginValue( bean_speedChecking.getOverSpeedThreshold() );
        this.fmt_over_speed_time_limit.setOriginValue( bean_speedChecking.getOverSpeedTimeLimit() );
        this.fmt_speed_loss_threshold.setOriginValue( bean_speedChecking.getSpeedLossThreshold() );
        this.fmt_speed_loss_time_limit.setOriginValue( bean_speedChecking.getSpeedLossTimeLimit() );
        this.fmt_acceleration_speed_loss_threshold.setOriginValue( bean_speedChecking.getAccelerationSpeedLossThreshold() );
        this.fmt_acceleration_speed_loss_time_limit.setOriginValue( bean_speedChecking.getAccelerationSpeedLossTimeLimit() );
        this.fmt_usl_lsl_over_speed_threshold.setOriginValue( bean_speedChecking.getUslLslOverSpeedThreshold() );
        this.fmt_speed_check_upper_threshold.setOriginValue( bean_speedChecking.getSpeedCheckUpperThreshold() );
        this.fmt_speed_check_lower_threshold.setOriginValue( bean_speedChecking.getSpeedCheckLowerThreshold() );
        this.fmt_sc1_speed_check_upper_threshold.setOriginValue( bean_speedChecking.getSc1_speedCheckUpperThreshold() );
        this.fmt_sc1_speed_check_lower_threshold.setOriginValue( bean_speedChecking.getSc1_speedCheckLowerThreshold() );
        this.fmt_sc2_speed_check_upper_threshold.setOriginValue( bean_speedChecking.getSc2_speedCheckUpperThreshold() );
        this.fmt_sc2_speed_check_lower_threshold.setOriginValue( bean_speedChecking.getSc2_speedCheckLowerThreshold() );
        this.fmt_sc3_speed_check_upper_threshold.setOriginValue( bean_speedChecking.getSc3_speedCheckUpperThreshold() );
        this.fmt_sc3_speed_check_lower_threshold.setOriginValue( bean_speedChecking.getSc3_speedCheckLowerThreshold() );
    }


    public void setSpeedOutputBean ( SpeedOutputBean bean_speedOutput ) {
        this.fmt_reference_speed_analog_output_offset.setOriginValue( bean_speedOutput.getReferenceSpeedAnalogOutputOffset() );
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


    public static SpeedRegulation createPanel ( SettingPanel<SpeedRegulation> panel ) {
        SpeedRegulation gui = new SpeedRegulation();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class SpeedCheckingBean {
        private Double overSpeedThreshold;
        private Long overSpeedTimeLimit;
        private Double speedLossThreshold;
        private Long speedLossTimeLimit;
        private Long accelerationSpeedLossThreshold;
        private Long accelerationSpeedLossTimeLimit;
        private Double uslLslOverSpeedThreshold;
        private Double speedCheckUpperThreshold;
        private Double speedCheckLowerThreshold;
        private Double sc1_speedCheckUpperThreshold;
        private Double sc1_speedCheckLowerThreshold;
        private Double sc2_speedCheckUpperThreshold;
        private Double sc2_speedCheckLowerThreshold;
        private Double sc3_speedCheckUpperThreshold;
        private Double sc3_speedCheckLowerThreshold;

        public Double getOverSpeedThreshold () {
            return this.overSpeedThreshold;
        }

        public Long getOverSpeedTimeLimit () {
            return this.overSpeedTimeLimit;
        }


        public Double getSpeedLossThreshold () {
            return this.speedLossThreshold;
        }


        public Long getSpeedLossTimeLimit () {
            return this.speedLossTimeLimit;
        }


        public Long getAccelerationSpeedLossThreshold () {
            return this.accelerationSpeedLossThreshold;
        }


        public Long getAccelerationSpeedLossTimeLimit () {
            return this.accelerationSpeedLossTimeLimit;
        }


        public Double getUslLslOverSpeedThreshold () {
            return this.uslLslOverSpeedThreshold;
        }


        public Double getSpeedCheckUpperThreshold () {
            return this.speedCheckUpperThreshold;
        }


        public Double getSpeedCheckLowerThreshold () {
            return this.speedCheckLowerThreshold;
        }


        public void setOverSpeedThreshold ( Double overSpeedThreshold ) {
            this.overSpeedThreshold = overSpeedThreshold;
        }


        public void setOverSpeedTimeLimit ( Long overSpeedTimeLimit ) {
            this.overSpeedTimeLimit = overSpeedTimeLimit;
        }


        public void setSpeedLossThreshold ( Double speedLossThreshold ) {
            this.speedLossThreshold = speedLossThreshold;
        }


        public void setSpeedLossTimeLimit ( Long speedLossTimeLimit ) {
            this.speedLossTimeLimit = speedLossTimeLimit;
        }


        public void setAccelerationSpeedLossThreshold ( Long accelerationSpeedLossThreshold ) {
            this.accelerationSpeedLossThreshold = accelerationSpeedLossThreshold;
        }


        public void setAccelerationSpeedLossTimeLimit ( Long accelerationSpeedLossTimeLimit ) {
            this.accelerationSpeedLossTimeLimit = accelerationSpeedLossTimeLimit;
        }


        public void setUslLslOverSpeedThreshold ( Double uslLslOverSpeedThreshold ) {
            this.uslLslOverSpeedThreshold = uslLslOverSpeedThreshold;
        }


        public void setSpeedCheckUpperThreshold ( Double speedCheckUpperThreshold ) {
            this.speedCheckUpperThreshold = speedCheckUpperThreshold;
        }


        public void setSpeedCheckLowerThreshold ( Double speedCheckLowerThreshold ) {
            this.speedCheckLowerThreshold = speedCheckLowerThreshold;
        }

		public Double getSc1_speedCheckUpperThreshold() {
			return sc1_speedCheckUpperThreshold;
		}

		public void setSc1_speedCheckUpperThreshold(Double sc1_speedCheckUpperThreshold) {
			this.sc1_speedCheckUpperThreshold = sc1_speedCheckUpperThreshold;
		}

		public Double getSc1_speedCheckLowerThreshold() {
			return sc1_speedCheckLowerThreshold;
		}

		public void setSc1_speedCheckLowerThreshold(Double sc1_speedCheckLowerThreshold) {
			this.sc1_speedCheckLowerThreshold = sc1_speedCheckLowerThreshold;
		}

		public Double getSc2_speedCheckUpperThreshold() {
			return sc2_speedCheckUpperThreshold;
		}

		public void setSc2_speedCheckUpperThreshold(Double sc2_speedCheckUpperThreshold) {
			this.sc2_speedCheckUpperThreshold = sc2_speedCheckUpperThreshold;
		}

		public Double getSc2_speedCheckLowerThreshold() {
			return sc2_speedCheckLowerThreshold;
		}

		public void setSc2_speedCheckLowerThreshold(Double sc2_speedCheckLowerThreshold) {
			this.sc2_speedCheckLowerThreshold = sc2_speedCheckLowerThreshold;
		}

		public Double getSc3_speedCheckUpperThreshold() {
			return sc3_speedCheckUpperThreshold;
		}

		public void setSc3_speedCheckUpperThreshold(Double sc3_speedCheckUpperThreshold) {
			this.sc3_speedCheckUpperThreshold = sc3_speedCheckUpperThreshold;
		}

		public Double getSc3_speedCheckLowerThreshold() {
			return sc3_speedCheckLowerThreshold;
		}

		public void setSc3_speedCheckLowerThreshold(Double sc3_speedCheckLowerThreshold) {
			this.sc3_speedCheckLowerThreshold = sc3_speedCheckLowerThreshold;
		}
        
    }




    public static class SpeedOutputBean {
        private Double referenceSpeedAnalogOutputOffset;




        public Double getReferenceSpeedAnalogOutputOffset () {
            return this.referenceSpeedAnalogOutputOffset;
        }


        public void setReferenceSpeedAnalogOutputOffset ( Double referenceSpeedAnalogOutputOffset ) {
            this.referenceSpeedAnalogOutputOffset = referenceSpeedAnalogOutputOffset;
        }
    }
}
