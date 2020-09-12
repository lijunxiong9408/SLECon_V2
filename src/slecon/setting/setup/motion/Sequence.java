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
import slecon.component.ValueTextField;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;




/**
 * this is an auto-generated file. script:
 */
public class Sequence extends JPanel {
    private static final long     serialVersionUID = 2919139672152241181L;
    private static ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.motion.Sequence" );




    /* ---------------------------------------------------------------------------- */
    private boolean                started = false;
    private SettingPanel<Sequence> settingPanel;
    private JLabel                 cpt_pre_run_sequence;
    private JLabel                 lbl_pre_run_check_timeout_limit;
    private ValueTextField         fmt_pre_run_check_timeout_limit;
    private JLabel                 lbl_ur_dr_close_delay;
    private ValueTextField         fmt_ur_dr_close_delay;
    private JLabel                 lbl_rr_close_delay;
    private ValueTextField         fmt_rr_close_delay;
    private JLabel                 lbl_rr_close_feedback_timeout;
    private ValueTextField         fmt_rr_close_feedback_timeout;
    private JLabel                 lbl_pretorque_ok_timeout;
    private ValueTextField         fmt_pretorque_ok_timeout;
    private JLabel                 lbl_driver_brake_open_timeout;
    private ValueTextField         fmt_driver_brake_open_timeout;
    private JLabel                 lbl_brake_open_delay;
    private ValueTextField         fmt_brake_open_delay;
    private JLabel                 lbl_brake_open_feedback_timeout;
    private ValueTextField         fmt_brake_open_feedback_timeout;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_pre_stop_sequence;
    private JLabel         lbl_driver_brake_close_timeout;
    private ValueTextField fmt_driver_brake_close_timeout;
    private JLabel         lbl_brake_close_delay;
    private ValueTextField fmt_brake_close_delay;
    private JLabel         lbl_brake_close_feedback_timeout;
    private ValueTextField fmt_brake_close_feedback_timeout;
    private JLabel         lbl_driver_disable_delay;
    private ValueTextField fmt_driver_disable_delay;
    private JLabel         lbl_main_contacts_open_delay;
    private ValueTextField fmt_main_contacts_open_delay;
    private JLabel         lbl_main_contacts_open_feedback_delay;
    private ValueTextField fmt_main_contacts_open_feedback_delay;


    public void SetWidgetEnable(boolean enable) {
    	lbl_pre_run_check_timeout_limit.setEnabled(enable);
    	fmt_pre_run_check_timeout_limit.setEnabled(enable);
    	lbl_ur_dr_close_delay.setEnabled(enable);
    	fmt_ur_dr_close_delay.setEnabled(enable);
    	lbl_rr_close_delay.setEnabled(enable);
    	fmt_rr_close_delay.setEnabled(enable);
    	lbl_rr_close_feedback_timeout.setEnabled(enable);
    	fmt_rr_close_feedback_timeout.setEnabled(enable);
    	lbl_pretorque_ok_timeout.setEnabled(enable);
    	fmt_pretorque_ok_timeout.setEnabled(enable);
    	lbl_driver_brake_open_timeout.setEnabled(enable);
    	fmt_driver_brake_open_timeout.setEnabled(enable);
    	lbl_brake_open_delay.setEnabled(enable);
    	fmt_brake_open_delay.setEnabled(enable);
    	lbl_brake_open_feedback_timeout.setEnabled(enable);
    	fmt_brake_open_feedback_timeout.setEnabled(enable);
    	
    	lbl_driver_brake_close_timeout.setEnabled(enable);
    	fmt_driver_brake_close_timeout.setEnabled(enable);
    	lbl_brake_close_delay.setEnabled(enable);
    	fmt_brake_close_delay.setEnabled(enable);
    	lbl_brake_close_feedback_timeout.setEnabled(enable);
    	fmt_brake_close_feedback_timeout.setEnabled(enable);
    	lbl_driver_disable_delay.setEnabled(enable);
    	fmt_driver_disable_delay.setEnabled(enable);
    	lbl_main_contacts_open_delay.setEnabled(enable);
    	fmt_main_contacts_open_delay.setEnabled(enable);
    	lbl_main_contacts_open_feedback_delay.setEnabled(enable);
    	fmt_main_contacts_open_feedback_delay.setEnabled(enable);
    	
    }

    public Sequence () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<Sequence> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[40::40][20::20][32::32][]" ) );
        cpt_pre_run_sequence            = new JLabel();
        lbl_pre_run_check_timeout_limit = new JLabel();
        fmt_pre_run_check_timeout_limit = new ValueTextField();
        lbl_ur_dr_close_delay           = new JLabel();
        fmt_ur_dr_close_delay           = new ValueTextField();
        lbl_rr_close_delay              = new JLabel();
        fmt_rr_close_delay              = new ValueTextField();
        lbl_rr_close_feedback_timeout   = new JLabel();
        fmt_rr_close_feedback_timeout   = new ValueTextField();
        lbl_pretorque_ok_timeout        = new JLabel();
        fmt_pretorque_ok_timeout        = new ValueTextField();
        lbl_driver_brake_open_timeout   = new JLabel();
        fmt_driver_brake_open_timeout   = new ValueTextField();
        lbl_brake_open_delay            = new JLabel();
        fmt_brake_open_delay            = new ValueTextField();
        lbl_brake_open_feedback_timeout = new JLabel();
        fmt_brake_open_feedback_timeout = new ValueTextField();
        setCaptionStyle( cpt_pre_run_sequence );

        // @CompoentSetting<Fmt>( lbl_pre_run_check_timeout_limit , fmt_pre_run_check_timeout_limit )
        setTextLabelStyle( lbl_pre_run_check_timeout_limit );
        fmt_pre_run_check_timeout_limit.setColumns( 10 );
        fmt_pre_run_check_timeout_limit.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_pre_run_check_timeout_limit.setScope( Long.class, 0L, 60000L, false, true );
        fmt_pre_run_check_timeout_limit.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_ur_dr_close_delay , fmt_ur_dr_close_delay )
        setTextLabelStyle( lbl_ur_dr_close_delay );
        fmt_ur_dr_close_delay.setColumns( 10 );
        fmt_ur_dr_close_delay.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_ur_dr_close_delay.setScope( Long.class, 0L, 60000L, false, true );
        fmt_ur_dr_close_delay.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_rr_close_delay , fmt_rr_close_delay )
        setTextLabelStyle( lbl_rr_close_delay );
        fmt_rr_close_delay.setColumns( 10 );
        fmt_rr_close_delay.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_rr_close_delay.setScope( Long.class, 0L, 60000L, false, true );
        fmt_rr_close_delay.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_rr_close_feedback_timeout , fmt_rr_close_feedback_timeout )
        setTextLabelStyle( lbl_rr_close_feedback_timeout );
        fmt_rr_close_feedback_timeout.setColumns( 10 );
        fmt_rr_close_feedback_timeout.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_rr_close_feedback_timeout.setScope( Long.class, 0L, 60000L, false, true );
        fmt_rr_close_feedback_timeout.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_pretorque_ok_timeout , fmt_pretorque_ok_timeout )
        setTextLabelStyle( lbl_pretorque_ok_timeout );
        fmt_pretorque_ok_timeout.setColumns( 10 );
        fmt_pretorque_ok_timeout.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_pretorque_ok_timeout.setScope( Long.class, 0L, 60000L, false, true );
        fmt_pretorque_ok_timeout.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_driver_brake_open_timeout , fmt_driver_brake_open_timeout )
        setTextLabelStyle( lbl_driver_brake_open_timeout );
        fmt_driver_brake_open_timeout.setColumns( 10 );
        fmt_driver_brake_open_timeout.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_driver_brake_open_timeout.setScope( Long.class, 0L, 60000L, false, true );
        fmt_driver_brake_open_timeout.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_brake_open_delay , fmt_brake_open_delay )
        setTextLabelStyle( lbl_brake_open_delay );
        fmt_brake_open_delay.setColumns( 10 );
        fmt_brake_open_delay.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_brake_open_delay.setScope( Long.class, 0L, 60000L, false, true );
        fmt_brake_open_delay.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_brake_open_feedback_timeout , fmt_brake_open_feedback_timeout )
        setTextLabelStyle( lbl_brake_open_feedback_timeout );
        fmt_brake_open_feedback_timeout.setColumns( 10 );
        fmt_brake_open_feedback_timeout.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_brake_open_feedback_timeout.setScope( Long.class, 0L, 60000L, false, true );
        fmt_brake_open_feedback_timeout.setEmptyValue( 1000L );
        add( cpt_pre_run_sequence, "gapbottom 18-12, span, aligny center, top" );

        Box vbox_title = Box.createVerticalBox();
        vbox_title.add( lbl_pre_run_check_timeout_limit);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_ur_dr_close_delay);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_rr_close_delay);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_rr_close_feedback_timeout);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_pretorque_ok_timeout);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_driver_brake_open_timeout);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_brake_open_delay);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_brake_open_feedback_timeout);
        
        Box vbox_value = Box.createVerticalBox();
        vbox_value.add( fmt_pre_run_check_timeout_limit );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_ur_dr_close_delay );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_rr_close_delay );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_rr_close_feedback_timeout );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_pretorque_ok_timeout );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_driver_brake_open_timeout );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_brake_open_delay );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_brake_open_feedback_timeout );
        add(vbox_title, "skip 2, span, split, gapright 30, top");
        add(vbox_value, "right, wrap 30, top");

        /* ---------------------------------------------------------------------------- */
        cpt_pre_stop_sequence                 = new JLabel();
        lbl_driver_brake_close_timeout        = new JLabel();
        fmt_driver_brake_close_timeout        = new ValueTextField();
        lbl_brake_close_delay                 = new JLabel();
        fmt_brake_close_delay                 = new ValueTextField();
        lbl_brake_close_feedback_timeout      = new JLabel();
        fmt_brake_close_feedback_timeout      = new ValueTextField();
        lbl_driver_disable_delay              = new JLabel();
        fmt_driver_disable_delay              = new ValueTextField();
        lbl_main_contacts_open_delay          = new JLabel();
        fmt_main_contacts_open_delay          = new ValueTextField();
        lbl_main_contacts_open_feedback_delay = new JLabel();
        fmt_main_contacts_open_feedback_delay = new ValueTextField();
        setCaptionStyle( cpt_pre_stop_sequence );

        // @CompoentSetting<Fmt>( lbl_driver_brake_close_timeout , fmt_driver_brake_close_timeout )
        setTextLabelStyle( lbl_driver_brake_close_timeout );
        fmt_driver_brake_close_timeout.setColumns( 10 );
        fmt_driver_brake_close_timeout.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_driver_brake_close_timeout.setScope( Long.class, 0L, 60000L, false, true );
        fmt_driver_brake_close_timeout.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_brake_close_delay , fmt_brake_close_delay )
        setTextLabelStyle( lbl_brake_close_delay );
        fmt_brake_close_delay.setColumns( 10 );
        fmt_brake_close_delay.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_brake_close_delay.setScope( Long.class, 0L, 60000L, false, true );
        fmt_brake_close_delay.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_brake_close_feedback_timeout , fmt_brake_close_feedback_timeout )
        setTextLabelStyle( lbl_brake_close_feedback_timeout );
        fmt_brake_close_feedback_timeout.setColumns( 10 );
        fmt_brake_close_feedback_timeout.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_brake_close_feedback_timeout.setScope( Long.class, 0L, 60000L, false, true );
        fmt_brake_close_feedback_timeout.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_driver_disable_delay , fmt_driver_disable_delay )
        setTextLabelStyle( lbl_driver_disable_delay );
        fmt_driver_disable_delay.setColumns( 10 );
        fmt_driver_disable_delay.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_driver_disable_delay.setScope( Long.class, 0L, 60000L, false, true );
        fmt_driver_disable_delay.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_main_contacts_open_delay , fmt_main_contacts_open_delay )
        setTextLabelStyle( lbl_main_contacts_open_delay );
        fmt_main_contacts_open_delay.setColumns( 10 );
        fmt_main_contacts_open_delay.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_main_contacts_open_delay.setScope( Long.class, 0L, 60000L, false, true );
        fmt_main_contacts_open_delay.setEmptyValue( 1000L );

        // @CompoentSetting<Fmt>( lbl_main_contacts_open_feedback_delay , fmt_main_contacts_open_feedback_delay )
        setTextLabelStyle( lbl_main_contacts_open_feedback_delay );
        fmt_main_contacts_open_feedback_delay.setColumns( 10 );
        fmt_main_contacts_open_feedback_delay.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_main_contacts_open_feedback_delay.setScope( Long.class, 0L, 60000L, false, true );
        fmt_main_contacts_open_feedback_delay.setEmptyValue( 1000L );
        add( cpt_pre_stop_sequence, "gapbottom 18-12, span, top" );
        Box vbox_title1 = Box.createVerticalBox();
        vbox_title1.add( lbl_driver_brake_close_timeout);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_brake_close_delay);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_brake_close_feedback_timeout);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_driver_disable_delay);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_main_contacts_open_delay);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_main_contacts_open_feedback_delay);
        
        Box vbox_value2 = Box.createVerticalBox();
        vbox_value2.add( fmt_driver_brake_close_timeout );
        vbox_value2.add( Box.createVerticalStrut(13));
        vbox_value2.add( fmt_brake_close_delay );
        vbox_value2.add( Box.createVerticalStrut(13));
        vbox_value2.add( fmt_brake_close_feedback_timeout );
        vbox_value2.add( Box.createVerticalStrut(13));
        vbox_value2.add( fmt_driver_disable_delay );
        vbox_value2.add( Box.createVerticalStrut(13));
        vbox_value2.add( fmt_main_contacts_open_delay );
        vbox_value2.add( Box.createVerticalStrut(13));
        vbox_value2.add( fmt_main_contacts_open_feedback_delay );
        add(vbox_title1, "skip 2, span, split, gapright 30, top");
        add(vbox_value2, "right, wrap 30, top");


        /* ---------------------------------------------------------------------------- */
        bindGroup( "PreRunCheckTimeoutLimit", lbl_pre_run_check_timeout_limit, fmt_pre_run_check_timeout_limit );
        bindGroup( "UrDrCloseDelay", lbl_ur_dr_close_delay, fmt_ur_dr_close_delay );
        bindGroup( "RrCloseDelay", lbl_rr_close_delay, fmt_rr_close_delay );
        bindGroup( "RrCloseFeedbackTimeout", lbl_rr_close_feedback_timeout, fmt_rr_close_feedback_timeout );
        bindGroup( "PretorqueOkTimeout", lbl_pretorque_ok_timeout, fmt_pretorque_ok_timeout );
        bindGroup( "DriverBrakeOpenTimeout", lbl_driver_brake_open_timeout, fmt_driver_brake_open_timeout );
        bindGroup( "BrakeOpenDelay", lbl_brake_open_delay, fmt_brake_open_delay );
        bindGroup( "BrakeOpenFeedbackTimeout", lbl_brake_open_feedback_timeout, fmt_brake_open_feedback_timeout );
        bindGroup( "DriverBrakeCloseTimeout", lbl_driver_brake_close_timeout, fmt_driver_brake_close_timeout );
        bindGroup( "BrakeCloseDelay", lbl_brake_close_delay, fmt_brake_close_delay );
        bindGroup( "BrakeCloseFeedbackTimeout", lbl_brake_close_feedback_timeout, fmt_brake_close_feedback_timeout );
        bindGroup( "DriverDisableDelay", lbl_driver_disable_delay, fmt_driver_disable_delay );
        bindGroup( "MainContactsOpenDelay", lbl_main_contacts_open_delay, fmt_main_contacts_open_delay );
        bindGroup( "MainContactsOpenFeedbackDelay", lbl_main_contacts_open_feedback_delay, fmt_main_contacts_open_feedback_delay );
        loadI18N();
        SetWidgetEnable(false);
        revalidate();
    }


    private void loadI18N () {
        cpt_pre_run_sequence.setText( getBundleText( "LBL_cpt_pre_run_sequence", "Pre-run Sequence" ) );
        lbl_pre_run_check_timeout_limit.setText( getBundleText( "LBL_lbl_pre_run_check_timeout_limit", "Pre-run check timeout limit" ) );
        lbl_ur_dr_close_delay.setText( getBundleText( "LBL_lbl_ur_dr_close_delay", "UR/DR close delay" ) );
        lbl_rr_close_delay.setText( getBundleText( "LBL_lbl_rr_close_delay", "RR close delay" ) );
        lbl_rr_close_feedback_timeout.setText( getBundleText( "LBL_lbl_rr_close_feedback_timeout", "RR close feedback timeout" ) );
        lbl_pretorque_ok_timeout.setText( getBundleText( "LBL_lbl_pretorque_ok_timeout", "Pretorque OK timeout" ) );
        lbl_driver_brake_open_timeout.setText( getBundleText( "LBL_lbl_driver_brake_open_timeout", "Driver brake open timeout" ) );
        lbl_brake_open_delay.setText( getBundleText( "LBL_lbl_brake_open_delay", "Brake open delay" ) );
        lbl_brake_open_feedback_timeout.setText( getBundleText( "LBL_lbl_brake_open_feedback_timeout", "Brake open feedback timeout" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_pre_stop_sequence.setText( getBundleText( "LBL_cpt_pre_stop_sequence", "Pre-Stop Sequence" ) );
        lbl_driver_brake_close_timeout.setText( getBundleText( "LBL_lbl_driver_brake_close_timeout", "Driver brake close timeout" ) );
        lbl_brake_close_delay.setText( getBundleText( "LBL_lbl_brake_close_delay", "Brake close delay" ) );
        lbl_brake_close_feedback_timeout.setText( getBundleText( "LBL_lbl_brake_close_feedback_timeout", "Brake close feedback timeout" ) );
        lbl_driver_disable_delay.setText( getBundleText( "LBL_lbl_driver_disable_delay", "Driver disable delay" ) );
        lbl_main_contacts_open_delay.setText( getBundleText( "LBL_lbl_main_contacts_open_delay", "Main contacts open delay" ) );
        lbl_main_contacts_open_feedback_delay.setText( getBundleText( "LBL_lbl_main_contacts_open_feedback_delay",
                                                                      "Main contacts open feedback delay" ) );

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


    public PreRunSequenceBean getPreRunSequenceBean () throws ConvertException {
        if ( ! fmt_pre_run_check_timeout_limit.checkValue() )
            throw new ConvertException();
        if ( ! fmt_ur_dr_close_delay.checkValue() )
            throw new ConvertException();
        if ( ! fmt_rr_close_delay.checkValue() )
            throw new ConvertException();
        if ( ! fmt_rr_close_feedback_timeout.checkValue() )
            throw new ConvertException();
        if ( ! fmt_pretorque_ok_timeout.checkValue() )
            throw new ConvertException();
        if ( ! fmt_driver_brake_open_timeout.checkValue() )
            throw new ConvertException();
        if ( ! fmt_brake_open_delay.checkValue() )
            throw new ConvertException();
        if ( ! fmt_brake_open_feedback_timeout.checkValue() )
            throw new ConvertException();

        PreRunSequenceBean bean_preRunSequence = new PreRunSequenceBean();
        bean_preRunSequence.setPreRunCheckTimeoutLimit( ( Long )fmt_pre_run_check_timeout_limit.getValue() );
        bean_preRunSequence.setUrDrCloseDelay( ( Long )fmt_ur_dr_close_delay.getValue() );
        bean_preRunSequence.setRrCloseDelay( ( Long )fmt_rr_close_delay.getValue() );
        bean_preRunSequence.setRrCloseFeedbackTimeout( ( Long )fmt_rr_close_feedback_timeout.getValue() );
        bean_preRunSequence.setPretorqueOkTimeout( ( Long )fmt_pretorque_ok_timeout.getValue() );
        bean_preRunSequence.setDriverBrakeOpenTimeout( ( Long )fmt_driver_brake_open_timeout.getValue() );
        bean_preRunSequence.setBrakeOpenDelay( ( Long )fmt_brake_open_delay.getValue() );
        bean_preRunSequence.setBrakeOpenFeedbackTimeout( ( Long )fmt_brake_open_feedback_timeout.getValue() );
        return bean_preRunSequence;
    }


    public PreStopSequenceBean getPreStopSequenceBean () throws ConvertException {
        if ( ! fmt_driver_brake_close_timeout.checkValue() )
            throw new ConvertException();
        if ( ! fmt_brake_close_delay.checkValue() )
            throw new ConvertException();
        if ( ! fmt_brake_close_feedback_timeout.checkValue() )
            throw new ConvertException();
        if ( ! fmt_driver_disable_delay.checkValue() )
            throw new ConvertException();
        if ( ! fmt_main_contacts_open_delay.checkValue() )
            throw new ConvertException();
        if ( ! fmt_main_contacts_open_feedback_delay.checkValue() )
            throw new ConvertException();

        PreStopSequenceBean bean_preStopSequence = new PreStopSequenceBean();
        bean_preStopSequence.setDriverBrakeCloseTimeout( ( Long )fmt_driver_brake_close_timeout.getValue() );
        bean_preStopSequence.setBrakeCloseDelay( ( Long )fmt_brake_close_delay.getValue() );
        bean_preStopSequence.setBrakeCloseFeedbackTimeout( ( Long )fmt_brake_close_feedback_timeout.getValue() );
        bean_preStopSequence.setDriverDisableDelay( ( Long )fmt_driver_disable_delay.getValue() );
        bean_preStopSequence.setMainContactsOpenDelay( ( Long )fmt_main_contacts_open_delay.getValue() );
        bean_preStopSequence.setMainContactsOpenFeedbackDelay( ( Long )fmt_main_contacts_open_feedback_delay.getValue() );
        return bean_preStopSequence;
    }


    public void setPreRunSequenceBean ( PreRunSequenceBean bean_preRunSequence ) {
        this.fmt_pre_run_check_timeout_limit.setOriginValue( bean_preRunSequence.getPreRunCheckTimeoutLimit() );
        this.fmt_ur_dr_close_delay.setOriginValue( bean_preRunSequence.getUrDrCloseDelay() );
        this.fmt_rr_close_delay.setOriginValue( bean_preRunSequence.getRrCloseDelay() );
        this.fmt_rr_close_feedback_timeout.setOriginValue( bean_preRunSequence.getRrCloseFeedbackTimeout() );
        this.fmt_pretorque_ok_timeout.setOriginValue( bean_preRunSequence.getPretorqueOkTimeout() );
        this.fmt_driver_brake_open_timeout.setOriginValue( bean_preRunSequence.getDriverBrakeOpenTimeout() );
        this.fmt_brake_open_delay.setOriginValue( bean_preRunSequence.getBrakeOpenDelay() );
        this.fmt_brake_open_feedback_timeout.setOriginValue( bean_preRunSequence.getBrakeOpenFeedbackTimeout() );
    }


    public void setPreStopSequenceBean ( PreStopSequenceBean bean_preStopSequence ) {
        this.fmt_driver_brake_close_timeout.setOriginValue( bean_preStopSequence.getDriverBrakeCloseTimeout() );
        this.fmt_brake_close_delay.setOriginValue( bean_preStopSequence.getBrakeCloseDelay() );
        this.fmt_brake_close_feedback_timeout.setOriginValue( bean_preStopSequence.getBrakeCloseFeedbackTimeout() );
        this.fmt_driver_disable_delay.setOriginValue( bean_preStopSequence.getDriverDisableDelay() );
        this.fmt_main_contacts_open_delay.setOriginValue( bean_preStopSequence.getMainContactsOpenDelay() );
        this.fmt_main_contacts_open_feedback_delay.setOriginValue( bean_preStopSequence.getMainContactsOpenFeedbackDelay() );
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


    public static Sequence createPanel ( SettingPanel<Sequence> panel ) {
        Sequence gui = new Sequence();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class PreRunSequenceBean {
        private Long preRunCheckTimeoutLimit;
        private Long urDrCloseDelay;
        private Long rrCloseDelay;
        private Long rrCloseFeedbackTimeout;
        private Long pretorqueOkTimeout;
        private Long driverBrakeOpenTimeout;
        private Long brakeOpenDelay;
        private Long brakeOpenFeedbackTimeout;




        public Long getPreRunCheckTimeoutLimit () {
            return this.preRunCheckTimeoutLimit;
        }


        public Long getUrDrCloseDelay () {
            return this.urDrCloseDelay;
        }


        public Long getRrCloseDelay () {
            return this.rrCloseDelay;
        }


        public Long getRrCloseFeedbackTimeout () {
            return this.rrCloseFeedbackTimeout;
        }


        public Long getPretorqueOkTimeout () {
            return this.pretorqueOkTimeout;
        }


        public Long getDriverBrakeOpenTimeout () {
            return this.driverBrakeOpenTimeout;
        }


        public Long getBrakeOpenDelay () {
            return this.brakeOpenDelay;
        }


        public Long getBrakeOpenFeedbackTimeout () {
            return this.brakeOpenFeedbackTimeout;
        }


        public void setPreRunCheckTimeoutLimit ( Long preRunCheckTimeoutLimit ) {
            this.preRunCheckTimeoutLimit = preRunCheckTimeoutLimit;
        }


        public void setUrDrCloseDelay ( Long urDrCloseDelay ) {
            this.urDrCloseDelay = urDrCloseDelay;
        }


        public void setRrCloseDelay ( Long rrCloseDelay ) {
            this.rrCloseDelay = rrCloseDelay;
        }


        public void setRrCloseFeedbackTimeout ( Long rrCloseFeedbackTimeout ) {
            this.rrCloseFeedbackTimeout = rrCloseFeedbackTimeout;
        }


        public void setPretorqueOkTimeout ( Long pretorqueOkTimeout ) {
            this.pretorqueOkTimeout = pretorqueOkTimeout;
        }


        public void setDriverBrakeOpenTimeout ( Long driverBrakeOpenTimeout ) {
            this.driverBrakeOpenTimeout = driverBrakeOpenTimeout;
        }


        public void setBrakeOpenDelay ( Long brakeOpenDelay ) {
            this.brakeOpenDelay = brakeOpenDelay;
        }


        public void setBrakeOpenFeedbackTimeout ( Long brakeOpenFeedbackTimeout ) {
            this.brakeOpenFeedbackTimeout = brakeOpenFeedbackTimeout;
        }
    }




    public static class PreStopSequenceBean {
        private Long driverBrakeCloseTimeout;
        private Long brakeCloseDelay;
        private Long brakeCloseFeedbackTimeout;
        private Long driverDisableDelay;
        private Long mainContactsOpenDelay;
        private Long mainContactsOpenFeedbackDelay;




        public Long getDriverBrakeCloseTimeout () {
            return this.driverBrakeCloseTimeout;
        }


        public Long getBrakeCloseDelay () {
            return this.brakeCloseDelay;
        }


        public Long getBrakeCloseFeedbackTimeout () {
            return this.brakeCloseFeedbackTimeout;
        }


        public Long getDriverDisableDelay () {
            return this.driverDisableDelay;
        }


        public Long getMainContactsOpenDelay () {
            return this.mainContactsOpenDelay;
        }


        public Long getMainContactsOpenFeedbackDelay () {
            return this.mainContactsOpenFeedbackDelay;
        }


        public void setDriverBrakeCloseTimeout ( Long driverBrakeCloseTimeout ) {
            this.driverBrakeCloseTimeout = driverBrakeCloseTimeout;
        }


        public void setBrakeCloseDelay ( Long brakeCloseDelay ) {
            this.brakeCloseDelay = brakeCloseDelay;
        }


        public void setBrakeCloseFeedbackTimeout ( Long brakeCloseFeedbackTimeout ) {
            this.brakeCloseFeedbackTimeout = brakeCloseFeedbackTimeout;
        }


        public void setDriverDisableDelay ( Long driverDisableDelay ) {
            this.driverDisableDelay = driverDisableDelay;
        }


        public void setMainContactsOpenDelay ( Long mainContactsOpenDelay ) {
            this.mainContactsOpenDelay = mainContactsOpenDelay;
        }


        public void setMainContactsOpenFeedbackDelay ( Long mainContactsOpenFeedbackDelay ) {
            this.mainContactsOpenFeedbackDelay = mainContactsOpenFeedbackDelay;
        }
    }
}
