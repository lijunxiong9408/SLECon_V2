package slecon.setting.installation;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueTextField;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;


public class Motion extends JPanel {
    private static final long     serialVersionUID = -204965001019606681L;
    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.installation.Motion");

    /* ---------------------------------------------------------------------------- */
    private boolean              started = false;
    private SettingPanel<Motion> settingPanel;
    private JLabel               cpt_motor_specification;
    private JLabel               lbl_revolution_per_minute_rpm;
    private ValueTextField       fmt_revolution_per_minute_rpm;
    private JLabel               lbl_maximum_linear_speed_mm_s;
    private ValueTextField       fmt_maximum_linear_speed_mm_s;

    /* ---------------------------------------------------------------------------- */
    private JLabel         		 cpt_encoder;
    private JLabel         		 lbl_encoder_count_per_revolution_cpr;
    private ValueTextField 		 fmt_encoder_count_per_revolution_cpr;
    private ValueCheckBox   	 ebd_invert_phase;

    /* ---------------------------------------------------------------------------- */
    private JLabel 				 cpt_information;
    private JLabel 				 lbl_label_encoder_cpr_at_maximum_speed;
    private JLabel 				 lbl_value_encoder_cpr_at_maximum_speed;
    private JLabel 				 lbl_label_count_to_mm_ratio;
    private JLabel 				 lbl_value_count_to_mm_ratio;
    private JLabel 				 lbl_label_recommended_shaft_limit_lsl_usl_length;
    private JLabel 				 lbl_value_recommended_shaft_limit_lsl_usl_length;
    private Fmt_maximum_linear_speed_mm_sDocumentListener maximum_linear_speed_mm_listener;

    private JLabel cpt_driver_type;
    private ValueCheckBox ebd_fuji_vf_driver_over_can_bus;
    



    public Motion () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<Motion> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_motor_specification       = new JLabel();
        lbl_revolution_per_minute_rpm = new JLabel();
        fmt_revolution_per_minute_rpm = new ValueTextField();
        lbl_maximum_linear_speed_mm_s = new JLabel();
        fmt_maximum_linear_speed_mm_s = new ValueTextField();
        maximum_linear_speed_mm_listener = new Fmt_maximum_linear_speed_mm_sDocumentListener( fmt_maximum_linear_speed_mm_s );
        fmt_maximum_linear_speed_mm_s.getDocument().addDocumentListener( maximum_linear_speed_mm_listener );
        setCaptionStyle( cpt_motor_specification );
        // @CompoentSetting<Fmt>( lbl_revolution_per_minute_rpm , fmt_revolution_per_minute_rpm )
        setTextLabelStyle( lbl_revolution_per_minute_rpm );
        
        fmt_revolution_per_minute_rpm.setColumns( 10 );
        fmt_revolution_per_minute_rpm.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_revolution_per_minute_rpm.setScope( Long.class, 0L, 10000L, false, true );
        fmt_revolution_per_minute_rpm.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_maximum_linear_speed_mm_s , fmt_maximum_linear_speed_mm_s )
        setTextLabelStyle( lbl_maximum_linear_speed_mm_s );
        fmt_maximum_linear_speed_mm_s.setColumns( 10 );
        fmt_maximum_linear_speed_mm_s.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_maximum_linear_speed_mm_s.setScope( Double.class, 0D, 10000D, false, true );
        fmt_maximum_linear_speed_mm_s.setEmptyValue( 1000D );
        
        add( cpt_motor_specification, "gapbottom 18-12, span, top" );
        Box vbox_title = Box.createVerticalBox();
        Box vbox_value = Box.createVerticalBox();
        vbox_title.add( lbl_revolution_per_minute_rpm);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_maximum_linear_speed_mm_s);
        
        vbox_value.add( fmt_revolution_per_minute_rpm );
        vbox_value.add( Box.createVerticalStrut(10));
        vbox_value.add( fmt_maximum_linear_speed_mm_s );
        add(vbox_title, "skip 2, span, split 2, left, top, gapright 30");
        add(vbox_value, "wrap 30");

        /* ---------------------------------------------------------------------------- */
        cpt_encoder                          = new JLabel();
        lbl_encoder_count_per_revolution_cpr = new JLabel();
        fmt_encoder_count_per_revolution_cpr = new ValueTextField();
        ebd_invert_phase                     = new ValueCheckBox();
        setCaptionStyle( cpt_encoder );

        // @CompoentSetting<Fmt>( lbl_encoder_count_per_revolution_cpr , fmt_encoder_count_per_revolution_cpr )
        setTextLabelStyle( lbl_encoder_count_per_revolution_cpr );
        fmt_encoder_count_per_revolution_cpr.setColumns( 10 );
        fmt_encoder_count_per_revolution_cpr.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_encoder_count_per_revolution_cpr.setScope( Long.class, 0L, null, false, true );
        fmt_encoder_count_per_revolution_cpr.setEmptyValue( 1000L );

        // @CompoentSetting( ebd_invert_phase )
        
        add( cpt_encoder, "gapbottom 18-12, span, top" );
        add( lbl_encoder_count_per_revolution_cpr, "skip 2, span, split 2, left, top, gapright 30" );
        add( fmt_encoder_count_per_revolution_cpr, "wrap" );
        add( ebd_invert_phase, "skip 1, span, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_information                                  = new JLabel();
        lbl_label_encoder_cpr_at_maximum_speed           = new JLabel();
        lbl_value_encoder_cpr_at_maximum_speed           = new JLabel();
        lbl_label_count_to_mm_ratio                      = new JLabel();
        lbl_value_count_to_mm_ratio                      = new JLabel();
        lbl_label_recommended_shaft_limit_lsl_usl_length = new JLabel();
        lbl_value_recommended_shaft_limit_lsl_usl_length = new JLabel();
        setCaptionStyle( cpt_information );
        setLabelTitleStyle( lbl_label_encoder_cpr_at_maximum_speed );
        setLabelValueStyle( lbl_value_encoder_cpr_at_maximum_speed );
        setLabelTitleStyle( lbl_label_count_to_mm_ratio );
        setLabelValueStyle( lbl_value_count_to_mm_ratio );
        setLabelTitleStyle( lbl_label_recommended_shaft_limit_lsl_usl_length );
        setLabelValueStyle( lbl_value_recommended_shaft_limit_lsl_usl_length );
        add( cpt_information, "gapbottom 18-12, span, aligny center" );
        /*
        add( lbl_label_encoder_cpr_at_maximum_speed, "skip 2, span 1, left, top" );
        add( lbl_value_encoder_cpr_at_maximum_speed, "span 1, wrap 30, left, top" );
        add( lbl_label_count_to_mm_ratio, "skip 2, span 1, left, top" );
        add( lbl_value_count_to_mm_ratio, "span 1, wrap 30, left, top" );
        add( lbl_label_recommended_shaft_limit_lsl_usl_length, "skip 2, span 1, left, top" );
        add( lbl_value_recommended_shaft_limit_lsl_usl_length, "span 1, wrap 30, left, top" );
		*/
        Box info_vbox_title = Box.createVerticalBox();
        Box info_vbox_value = Box.createVerticalBox();
        info_vbox_title.add( lbl_label_encoder_cpr_at_maximum_speed);
        info_vbox_title.add( Box.createVerticalStrut(15));
        info_vbox_title.add( lbl_label_count_to_mm_ratio);
        info_vbox_title.add( Box.createVerticalStrut(15));
        info_vbox_title.add( lbl_label_recommended_shaft_limit_lsl_usl_length);
        
        info_vbox_value.add( lbl_value_encoder_cpr_at_maximum_speed );
        info_vbox_value.add( Box.createVerticalStrut(15));
        info_vbox_value.add( lbl_value_count_to_mm_ratio );
        info_vbox_value.add( Box.createVerticalStrut(15));
        info_vbox_value.add( lbl_value_recommended_shaft_limit_lsl_usl_length );
        
        add(info_vbox_title, "skip 2, span, split 2, left, top, gapright 30");
        add(info_vbox_value, "wrap 30");
        
        /* ---------------------------------------------------------------------------- */
        cpt_driver_type                       = new JLabel();
        ebd_fuji_vf_driver_over_can_bus       = new ValueCheckBox();
        setCaptionStyle( cpt_driver_type );
        
        add( cpt_driver_type, "gapbottom 18-12, span, aligny center" );
        add( ebd_fuji_vf_driver_over_can_bus, "skip 1, span, wrap 30, top" );
        

        /* ---------------------------------------------------------------------------- */
        bindGroup( "RevolutionPerMinuteRpm", lbl_revolution_per_minute_rpm, fmt_revolution_per_minute_rpm );
        bindGroup( "MaximumLinearSpeedMmS", lbl_maximum_linear_speed_mm_s, fmt_maximum_linear_speed_mm_s );
        bindGroup( "EncoderCountPerRevolutionCpr", lbl_encoder_count_per_revolution_cpr, fmt_encoder_count_per_revolution_cpr );
        bindGroup( "InvertPhase", ebd_invert_phase );
        bindGroup( "EncoderCprAtMaximumSpeed", lbl_label_encoder_cpr_at_maximum_speed, lbl_value_encoder_cpr_at_maximum_speed );
        bindGroup( "CountToMmRatio", lbl_label_count_to_mm_ratio, lbl_value_count_to_mm_ratio );
        bindGroup( "RecommendedShaftLimitLslUslLength", lbl_label_recommended_shaft_limit_lsl_usl_length,
                   lbl_value_recommended_shaft_limit_lsl_usl_length );
        bindGroup( "FujiVFDriverOverCANBus", ebd_fuji_vf_driver_over_can_bus );


        final Fmt2RecommendValueListener focusListener = new Fmt2RecommendValueListener();
        fmt_maximum_linear_speed_mm_s.addFocusListener( focusListener );
        fmt_revolution_per_minute_rpm.addFocusListener( focusListener );
        fmt_encoder_count_per_revolution_cpr.addFocusListener( focusListener );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_motor_specification.setText( getBundleText( "LBL_cpt_motor_specification", "Motor Specification" ) );
        lbl_revolution_per_minute_rpm.setText( getBundleText( "LBL_lbl_revolution_per_minute_rpm", "Revolution Per Minute (RPM)" ) );
        lbl_maximum_linear_speed_mm_s.setText( getBundleText( "LBL_lbl_maximum_linear_speed_mm_s", "Maximum Linear Speed (mm/s)" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_encoder.setText( getBundleText( "LBL_cpt_encoder", "Encoder" ) );
        lbl_encoder_count_per_revolution_cpr.setText( getBundleText( "LBL_lbl_encoder_count_per_revolution_cpr",
                                                                     "Encoder Count Per Revolution (CPR)" ) );
        ebd_invert_phase.setText( getBundleText( "LBL_ebd_invert_phase", "Invert Phase" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_information.setText( getBundleText( "LBL_cpt_information", "Information" ) );
        lbl_label_encoder_cpr_at_maximum_speed.setText( getBundleText( "LBL_lbl_label_encoder_cpr_at_maximum_speed",
                                                                       "Encoder CPR at maximum speed" ) );
        lbl_label_count_to_mm_ratio.setText( getBundleText( "LBL_lbl_label_count_to_mm_ratio", "Count-to-mm ratio" ) );
        lbl_label_recommended_shaft_limit_lsl_usl_length.setText( getBundleText( "LBL_lbl_label_recommended_shaft_limit_lsl_usl_length",
                                                                                 "Recommended Shaft limit (LSL/USL) length" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_driver_type.setText( getBundleText( "LBL_cpt_driver_type", "Driver Type" ) );
        ebd_fuji_vf_driver_over_can_bus.setText( getBundleText( "LBL_ebd_fuji_vf_driver_over_can_bus", "Fuji VF Driver over CAN Bus" ) );
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setTextLabelStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }

    public void fillRecommendedShaftLimit () {
        Object obj = fmt_maximum_linear_speed_mm_s.getValue();
        if ( lbl_value_recommended_shaft_limit_lsl_usl_length != null )
            if ( fmt_maximum_linear_speed_mm_s.checkValue() && obj instanceof Number ) {
                Number num   = ( Number )obj;
                double value = 1000.0 * ( 0.4 * Math.pow( num.doubleValue() / 1000, 2.0 ) + 0.1 * ( num.floatValue() / 1000.0f ) + 1.0 );

                // float value = 0.4f*num.floatValue()*num.floatValue()+0.1f*num.floatValue()+1.0f;
                lbl_value_recommended_shaft_limit_lsl_usl_length.setText( String.format( "%.3f", value ) );
            }
    }


    public void fillEncoderCPRAtMaxSpeed () {
        Object obj1 = fmt_encoder_count_per_revolution_cpr.getValue();
        Object obj2 = fmt_revolution_per_minute_rpm.getValue();
        if ( lbl_value_encoder_cpr_at_maximum_speed != null )
            if ( fmt_maximum_linear_speed_mm_s.checkValue() && obj1 instanceof Number && fmt_revolution_per_minute_rpm.checkValue()
                && obj2 instanceof Number ) {
                double num1  = ( ( Number )obj1 ).doubleValue();
                double num2  = ( ( Number )obj2 ).doubleValue();
                double value = num1 * num2 / 60D;
                lbl_value_encoder_cpr_at_maximum_speed.setText( String.format( "%.3f", value ) );
            }
    }


    private void setLabelTitleStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setLabelValueStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
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


    public InformationBean getInformationBean () throws ConvertException {
        InformationBean bean_information = new InformationBean();
        return bean_information;
    }


    public MotorSpecificationBean getMotorSpecificationBean () throws ConvertException {
        if ( ! fmt_revolution_per_minute_rpm.checkValue() )
            throw new ConvertException();
        if ( ! fmt_maximum_linear_speed_mm_s.checkValue() )
            throw new ConvertException();

        MotorSpecificationBean bean_motorSpecification = new MotorSpecificationBean();
        bean_motorSpecification.setRevolutionPerMinuteRpm( ( Long )fmt_revolution_per_minute_rpm.getValue() );
        bean_motorSpecification.setMaximumLinearSpeedMmS( ( Double )fmt_maximum_linear_speed_mm_s.getValue() );
        return bean_motorSpecification;
    }


    public EncoderBean getEncoderBean () throws ConvertException {
        if ( ! fmt_encoder_count_per_revolution_cpr.checkValue() )
            throw new ConvertException();

        EncoderBean bean_encoder = new EncoderBean();
        bean_encoder.setEncoderCountPerRevolutionCpr( ( Long )fmt_encoder_count_per_revolution_cpr.getValue() );
        bean_encoder.setInvertPhase( ebd_invert_phase.isSelected() );
        return bean_encoder;
    }


    public void setInformationBean ( InformationBean bean_information ) {
        this.lbl_value_encoder_cpr_at_maximum_speed.setText( bean_information.getEncoderCprAtMaximumSpeed() );
        this.lbl_value_count_to_mm_ratio.setText( bean_information.getCountToMmRatio() );
        fillRecommendedShaftLimit();
        fillEncoderCPRAtMaxSpeed();
    }


    public void setMotorSpecificationBean ( MotorSpecificationBean bean_motorSpecification ) {
        this.fmt_revolution_per_minute_rpm.setOriginValue( bean_motorSpecification.getRevolutionPerMinuteRpm() );
        this.fmt_maximum_linear_speed_mm_s.setOriginValue( bean_motorSpecification.getMaximumLinearSpeedMmS() );
        fillRecommendedShaftLimit();
        fillEncoderCPRAtMaxSpeed();
    }


    public void setEncoderBean ( EncoderBean bean_encoder ) {
        this.fmt_encoder_count_per_revolution_cpr.setOriginValue( bean_encoder.getEncoderCountPerRevolutionCpr() );
        this.ebd_invert_phase.setOriginSelected( bean_encoder.getInvertPhase() != null && bean_encoder.getInvertPhase() == true );
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
        maximum_linear_speed_mm_listener.stopTimer();
    }


    public static Motion createPanel ( SettingPanel<Motion> panel ) {
        Motion gui = new Motion();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class EncoderBean {
        private Long    encoderCountPerRevolutionCpr;
        private Boolean invertPhase;




        public Long getEncoderCountPerRevolutionCpr () {
            return this.encoderCountPerRevolutionCpr;
        }


        public Boolean getInvertPhase () {
            return this.invertPhase;
        }


        public void setEncoderCountPerRevolutionCpr ( Long encoderCountPerRevolutionCpr ) {
            this.encoderCountPerRevolutionCpr = encoderCountPerRevolutionCpr;
        }


        public void setInvertPhase ( Boolean invertPhase ) {
            this.invertPhase = invertPhase;
        }
    }




    private class Fmt2RecommendValueListener extends FocusAdapter {
        @Override
        public void focusLost ( final FocusEvent e ) {
            fillRecommendedShaftLimit();
            fillEncoderCPRAtMaxSpeed();
        }
    }




    private class Fmt_maximum_linear_speed_mm_sDocumentListener implements DocumentListener, ActionListener {
        private Timer timer = new Timer( 800, this );




        private Fmt_maximum_linear_speed_mm_sDocumentListener ( JTextComponent text ) {}


        public void stopTimer () {
            timer.stop();
        }


        @Override
        public void actionPerformed ( ActionEvent e ) {
            fillRecommendedShaftLimit();
            fillEncoderCPRAtMaxSpeed();
            timer.stop();
        }


        @Override
        public void insertUpdate ( DocumentEvent e ) {
            timer.restart();
        }


        @Override
        public void removeUpdate ( DocumentEvent e ) {
            timer.restart();
        }


        @Override
        public void changedUpdate ( DocumentEvent e ) {
            timer.restart();
        }
    }




    public static class InformationBean {
        private String encoderCprAtMaximumSpeed;
        private String countToMmRatio;
        private String recommendedShaftLimitLslUslLength;




        public String getEncoderCprAtMaximumSpeed () {
            return this.encoderCprAtMaximumSpeed;
        }


        public String getCountToMmRatio () {
            return this.countToMmRatio;
        }


        public String getRecommendedShaftLimitLslUslLength () {
            return this.recommendedShaftLimitLslUslLength;
        }


        public void setEncoderCprAtMaximumSpeed ( String encoderCprAtMaximumSpeed ) {
            this.encoderCprAtMaximumSpeed = encoderCprAtMaximumSpeed;
        }


        public void setCountToMmRatio ( String countToMmRatio ) {
            this.countToMmRatio = countToMmRatio;
        }


        public void setRecommendedShaftLimitLslUslLength ( String recommendedShaftLimitLslUslLength ) {
            this.recommendedShaftLimitLslUslLength = recommendedShaftLimitLslUslLength;
        }
    }




    public static class MotorSpecificationBean {
        private Long revolutionPerMinuteRpm;
        private Double maximumLinearSpeedMmS;




        public Long getRevolutionPerMinuteRpm () {
            return this.revolutionPerMinuteRpm;
        }


        public Double getMaximumLinearSpeedMmS () {
            return this.maximumLinearSpeedMmS;
        }


        public void setRevolutionPerMinuteRpm ( Long revolutionPerMinuteRpm ) {
            this.revolutionPerMinuteRpm = revolutionPerMinuteRpm;
        }


        public void setMaximumLinearSpeedMmS ( Double maximumLinearSpeedMmS ) {
            this.maximumLinearSpeedMmS = maximumLinearSpeedMmS;
        }
    }
    
    
    public static class DriverTypeBean {
        private boolean fujiVFDriverOverCANBus_enabled;
        private Long fujiVFDriverOverCANBus;
        
        
        public boolean isFujiVFDriverOverCANBusEnabled() {
            return fujiVFDriverOverCANBus_enabled;
        }
        
        
        public void setFujiVFDriverOverCANBusEnabled(boolean fujiVFDriverOverCANBus_enabled) {
            this.fujiVFDriverOverCANBus_enabled = fujiVFDriverOverCANBus_enabled;
        }
    }

    
    public DriverTypeBean getDriverTypeBean() throws ConvertException {
        DriverTypeBean bean_driverType = new DriverTypeBean();
        bean_driverType.setFujiVFDriverOverCANBusEnabled( ebd_fuji_vf_driver_over_can_bus.isSelected() );
        return bean_driverType;
        
    }
    
    
    public void setDriverTypeBean(DriverTypeBean bean_driverType) {
        this.ebd_fuji_vf_driver_over_can_bus.setOriginSelected( bean_driverType.isFujiVFDriverOverCANBusEnabled() );
    }
    
}
