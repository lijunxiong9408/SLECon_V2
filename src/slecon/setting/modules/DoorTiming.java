package slecon.setting.modules;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import logic.EventID;
import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueTextField;
import slecon.component.iobar.IOBar;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;




/**
 * Setup -> Module -> Independent Service.
 */
public class DoorTiming extends JPanel {
    private static final long serialVersionUID = -162992693475958210L;
    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT    = ToolBox.getResourceBundle( "setting.module.DoorTiming" );
    private boolean                    started = false;
    private SettingPanel<DoorTiming>   settingPanel;
    private JLabel                     cpt_general;
    private JLabel                     lbl_door_close_timer;
    private ValueTextField             fmt_door_close_timer;
    private JLabel                     lbl_disabled_door_close_timer;
    private ValueTextField             fmt_disabled_door_close_timer;
    private JLabel                     lbl_ddo_call_cool_time;
    private ValueTextField             fmt_ddo_call_cool_time;
    private ValueCheckBox  			   ebd_forced_close_door;
    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_timeout;
    private JLabel         lbl_door_opening_timeout;
    private ValueTextField fmt_door_opening_timeout;
    private JLabel         lbl_door_opened_timeout;
    private ValueTextField fmt_door_opened_timeout;
    private JLabel         lbl_door_closing_timeout;
    private ValueTextField fmt_door_closing_timeout;
    private JLabel         lbl_door_closed_timeout;
    private ValueTextField fmt_door_closed_timeout;
    private JLabel         lbl_door_forced_closed_timeout;
    private ValueTextField fmt_door_forced_closed_timeout;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_safety_chain_fail;
    private JLabel         lbl_activation_time;
    private ValueTextField fmt_activation_timer;
    private JLabel         lbl_retry_count;
    private ValueTextField fmt_retry_count;
    private JLabel		   lbl_change_station_retry_count;
    private ValueTextField fmt_change_station_retry_count;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_door_hold_button;
    private ValueCheckBox  ebd_enabled;
    private JLabel         lbl_holding_time;
    private ValueTextField fmt_holding_time;
    private IOBar          io_door_hold_button;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_independent_control_of_car_door_and_landing_door;
    private ValueCheckBox  ebd_enabled_0;
    private JLabel         lbl_front_door_car_calls;
    private ValueTextField fmt_front_door_car_calls;
    private JLabel         lbl_front_door_hall_calls;
    private ValueTextField fmt_front_door_hall_calls;
    private JLabel         lbl_rear_door_car_calls;
    private ValueTextField fmt_rear_door_car_calls;
    private JLabel         lbl_rear_door_hall_calls;
    private ValueTextField fmt_rear_door_hall_calls;




    public DoorTiming () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<DoorTiming> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][200::200][150::150][]" ) );
        cpt_general          = new JLabel();
        lbl_door_close_timer = new JLabel();
        fmt_door_close_timer = new ValueTextField();
        lbl_disabled_door_close_timer = new JLabel();
        fmt_disabled_door_close_timer = new ValueTextField();
        lbl_ddo_call_cool_time = new JLabel();
        fmt_ddo_call_cool_time = new ValueTextField();
        ebd_forced_close_door = new ValueCheckBox();
        
        setCaptionStyle( cpt_general );
        setTextLabelStyle( lbl_door_close_timer );
        fmt_door_close_timer.setColumns( 10 );
        fmt_door_close_timer.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_close_timer.setScope( Long.class, 0L, null, true, false );
        fmt_door_close_timer.setEmptyValue( 1L );
        setTextLabelStyle( lbl_disabled_door_close_timer );
        fmt_disabled_door_close_timer.setColumns( 10 );
        fmt_disabled_door_close_timer.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_disabled_door_close_timer.setScope( Long.class, 0L, null, true, false );
        fmt_disabled_door_close_timer.setEmptyValue( 1L );
        
        // @CompoentSetting<Fmt>( lbl_ddo_call_cool_time , fmt_ddo_call_cool_time )
        setTextLabelStyle( lbl_ddo_call_cool_time );
        fmt_ddo_call_cool_time.setColumns( 10 );
        fmt_ddo_call_cool_time.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_ddo_call_cool_time.setScope( Long.class, 0L, null, true, false );
        fmt_ddo_call_cool_time.setEmptyValue( 1L );
        add( cpt_general, "gapbottom 18-12, span, aligny center, top" );
        Box vbox_title = Box.createVerticalBox();
        vbox_title.add( lbl_door_close_timer);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_disabled_door_close_timer);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_ddo_call_cool_time);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( ebd_forced_close_door );
        
        Box vbox_value = Box.createVerticalBox();
        vbox_value.add( fmt_door_close_timer );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_disabled_door_close_timer );
        vbox_value.add( Box.createVerticalStrut(13));
        vbox_value.add( fmt_ddo_call_cool_time );
        add(vbox_title, "skip 2, span 1, left, top");
        add(vbox_value, "span 1, wrap 30, left, top");

        /* ---------------------------------------------------------------------------- */
        cpt_timeout              = new JLabel();
        lbl_door_opening_timeout = new JLabel();
        fmt_door_opening_timeout = new ValueTextField();
        lbl_door_opened_timeout  = new JLabel();
        fmt_door_opened_timeout  = new ValueTextField();
        lbl_door_closing_timeout = new JLabel();
        fmt_door_closing_timeout = new ValueTextField();
        lbl_door_closed_timeout  = new JLabel();
        fmt_door_closed_timeout  = new ValueTextField();
        lbl_door_forced_closed_timeout = new JLabel();
        fmt_door_forced_closed_timeout = new ValueTextField();
        setCaptionStyle( cpt_timeout );

        setTextLabelStyle( lbl_door_opening_timeout );
        fmt_door_opening_timeout.setColumns( 10 );
        fmt_door_opening_timeout.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_opening_timeout.setScope( Long.class, 0L, null, true, false );
        fmt_door_opening_timeout.setEmptyValue( 1L );

        setTextLabelStyle( lbl_door_opened_timeout );
        fmt_door_opened_timeout.setColumns( 10 );
        fmt_door_opened_timeout.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_opened_timeout.setScope( Long.class, 0L, null, true, false );
        fmt_door_opened_timeout.setEmptyValue( 1L );

        setTextLabelStyle( lbl_door_closing_timeout );
        fmt_door_closing_timeout.setColumns( 10 );
        fmt_door_closing_timeout.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_closing_timeout.setScope( Long.class, 0L, null, true, false );
        fmt_door_closing_timeout.setEmptyValue( 1L );

        setTextLabelStyle( lbl_door_closed_timeout );
        fmt_door_closed_timeout.setColumns( 10 );
        fmt_door_closed_timeout.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_closed_timeout.setScope( Long.class, 0L, null, true, false );
        fmt_door_closed_timeout.setEmptyValue( 1L );
        
        setTextLabelStyle( lbl_door_forced_closed_timeout );
        fmt_door_forced_closed_timeout.setColumns( 10 );
        fmt_door_forced_closed_timeout.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_forced_closed_timeout.setScope( Long.class, 0L, null, true, false );
        fmt_door_forced_closed_timeout.setEmptyValue( 1L );
        
        
        add( cpt_timeout, "gapbottom 18-12, span, aligny center, top" );
        Box vbox_title1 = Box.createVerticalBox();
        vbox_title1.add( lbl_door_opening_timeout);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_door_opened_timeout);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_door_closing_timeout);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_door_closed_timeout);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_door_forced_closed_timeout);
        
        Box vbox_value1 = Box.createVerticalBox();
        vbox_value1.add( fmt_door_opening_timeout );
        vbox_value1.add( Box.createVerticalStrut(13));
        vbox_value1.add( fmt_door_opened_timeout );
        vbox_value1.add( Box.createVerticalStrut(13));
        vbox_value1.add( fmt_door_closing_timeout );
        vbox_value1.add( Box.createVerticalStrut(13));
        vbox_value1.add( fmt_door_closed_timeout );
        vbox_value1.add( Box.createVerticalStrut(13));
        vbox_value1.add( fmt_door_forced_closed_timeout );
        add(vbox_title1, "skip 2, span 1, left, top");
        add(vbox_value1, "span 1, wrap 30, left, top");
        /* ---------------------------------------------------------------------------- */
        cpt_safety_chain_fail = new JLabel();
        lbl_activation_time   = new JLabel();
        fmt_activation_timer  = new ValueTextField();
        lbl_retry_count       = new JLabel();
        fmt_retry_count       = new ValueTextField();
        lbl_change_station_retry_count = new JLabel();
        fmt_change_station_retry_count = new ValueTextField();
        
        setCaptionStyle( cpt_safety_chain_fail );

        // @CompoentSetting<Fmt>( lbl_checking_timer_on_door_closed_failed , fmt_checking_timer_on_door_closed_failed )
        setTextLabelStyle( lbl_activation_time );
        fmt_activation_timer.setColumns( 10 );
        fmt_activation_timer.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_activation_timer.setScope( Long.class, 0L, null, true, false );
        fmt_activation_timer.setEmptyValue( 1L );

        // @CompoentSetting<Fmt>( lbl_retry_count , fmt_retry_count )
        setTextLabelStyle( lbl_retry_count );
        fmt_retry_count.setColumns( 10 );
        fmt_retry_count.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_retry_count.setScope( Long.class, 0L, null, true, false );
        fmt_retry_count.setEmptyValue( 1L );
        
        setTextLabelStyle( lbl_change_station_retry_count );
        fmt_change_station_retry_count.setColumns( 10 );
        fmt_change_station_retry_count.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_change_station_retry_count.setScope( Long.class, 0L, null, true, false );
        fmt_change_station_retry_count.setEmptyValue( 1L );
        
        add( cpt_safety_chain_fail, "gapbottom 18-12, span, aligny center, top" );
        Box vbox_title2 = Box.createVerticalBox();
        vbox_title2.add( lbl_activation_time);
        vbox_title2.add( Box.createVerticalStrut(15));
        vbox_title2.add( lbl_retry_count);
        vbox_title2.add( Box.createVerticalStrut(15));
        vbox_title2.add( lbl_change_station_retry_count);
        
        Box vbox_value2 = Box.createVerticalBox();
        vbox_value2.add( fmt_activation_timer );
        vbox_value2.add( Box.createVerticalStrut(13));
        vbox_value2.add( fmt_retry_count );
        vbox_value2.add( Box.createVerticalStrut(13));
        vbox_value2.add( fmt_change_station_retry_count );
        add(vbox_title2, "skip 2, span 1, left, top");
        add(vbox_value2, "span 1, wrap 30, left, top");

        /* ---------------------------------------------------------------------------- */
        cpt_door_hold_button = new JLabel();
        ebd_enabled          = new ValueCheckBox();
        lbl_holding_time     = new JLabel();
        fmt_holding_time     = new ValueTextField();
        setCaptionStyle( cpt_door_hold_button );

        // @CompoentSetting( ebd_enabled )

        // @CompoentSetting<Fmt>( lbl_holding_time , fmt_holding_time )
        setTextLabelStyle( lbl_holding_time );
        fmt_holding_time.setColumns( 10 );
        fmt_holding_time.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_holding_time.setScope( Long.class, 0L, null, true, false );
        fmt_holding_time.setEmptyValue( 1L );

        // @CompoentSetting( tbl_door_hold_button, panel_of_tbl_door_hold_button )
        io_door_hold_button = new IOBar( true );

        JLabel lbl_io_door_holding_button = new JLabel();
        setTextLabelStyle( lbl_io_door_holding_button );
        lbl_io_door_holding_button.setText( EventID.getString( EventID.DOOR_HOLD_BUTTON.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_door_hold_button, EventID.DOOR_HOLD_BUTTON.eventID );
        add( cpt_door_hold_button, "gapbottom 18-12, span, aligny center, top" );
        add( ebd_enabled, "skip 1, span, top" );
        
        add( lbl_holding_time, "skip 2, span 1, left, top" );
        add( fmt_holding_time, "span 1, left, wrap, top" );
        add( lbl_io_door_holding_button, "skip 2, span, left, top" );
        add( io_door_hold_button, "skip 2, span, left, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_independent_control_of_car_door_and_landing_door = new JLabel();
        ebd_enabled_0                                        = new ValueCheckBox();
        lbl_front_door_car_calls                             = new JLabel();
        fmt_front_door_car_calls                             = new ValueTextField();
        lbl_front_door_hall_calls                            = new JLabel();
        fmt_front_door_hall_calls                            = new ValueTextField();
        lbl_rear_door_car_calls                              = new JLabel();
        fmt_rear_door_car_calls                              = new ValueTextField();
        lbl_rear_door_hall_calls                             = new JLabel();
        fmt_rear_door_hall_calls                             = new ValueTextField();
        setCaptionStyle( cpt_independent_control_of_car_door_and_landing_door );

        // @CompoentSetting( ebd_enabled_0 )

        // @CompoentSetting<Fmt>( lbl_front_door_car_calls , fmt_front_door_car_calls )
        setTextLabelStyle( lbl_front_door_car_calls );
        fmt_front_door_car_calls.setColumns( 10 );
        fmt_front_door_car_calls.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_front_door_car_calls.setScope( Long.class, 0L, null, true, false );
        fmt_front_door_car_calls.setEmptyValue( 1L );

        // @CompoentSetting<Fmt>( lbl_front_door_hall_calls , fmt_front_door_hall_calls )
        setTextLabelStyle( lbl_front_door_hall_calls );
        fmt_front_door_hall_calls.setColumns( 10 );
        fmt_front_door_hall_calls.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_front_door_hall_calls.setScope( Long.class, 0L, null, true, false );
        fmt_front_door_hall_calls.setEmptyValue( 1L );

        // @CompoentSetting<Fmt>( lbl_rear_door_car_calls , fmt_rear_door_car_calls )
        setTextLabelStyle( lbl_rear_door_car_calls );
        fmt_rear_door_car_calls.setColumns( 10 );
        fmt_rear_door_car_calls.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_rear_door_car_calls.setScope( Long.class, 0L, null, true, false );
        fmt_rear_door_car_calls.setEmptyValue( 1L );

        // @CompoentSetting<Fmt>( lbl_rear_door_hall_calls , fmt_rear_door_hall_calls )
        setTextLabelStyle( lbl_rear_door_hall_calls );
        fmt_rear_door_hall_calls.setColumns( 10 );
        fmt_rear_door_hall_calls.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_rear_door_hall_calls.setScope( Long.class, 0L, null, true, false );
        fmt_rear_door_hall_calls.setEmptyValue( 1L );
        add( cpt_independent_control_of_car_door_and_landing_door, "gapbottom 18-12, span, aligny center, top" );
        add( ebd_enabled_0, "skip 1, span, top" );
        Box vbox_title3 = Box.createVerticalBox();
        vbox_title3.add( lbl_front_door_car_calls);
        vbox_title3.add( Box.createVerticalStrut(15));
        vbox_title3.add( lbl_front_door_hall_calls);
        vbox_title3.add( Box.createVerticalStrut(15));
        vbox_title3.add( lbl_rear_door_car_calls);
        vbox_title3.add( Box.createVerticalStrut(15));
        vbox_title3.add( lbl_rear_door_hall_calls);
        
        Box vbox_value3 = Box.createVerticalBox();
        vbox_value3.add( fmt_front_door_car_calls );
        vbox_value3.add( Box.createVerticalStrut(13));
        vbox_value3.add( fmt_front_door_hall_calls );
        vbox_value3.add( Box.createVerticalStrut(13));
        vbox_value3.add( fmt_rear_door_car_calls );
        vbox_value3.add( Box.createVerticalStrut(13));
        vbox_value3.add( fmt_rear_door_hall_calls );
        
        add(vbox_title3, "skip 2, span 1, left, top");
        add(vbox_value3, "span 1, left, top, wrap 30");

        /* ---------------------------------------------------------------------------- */
        bindGroup( "door_close_time", lbl_door_close_timer, fmt_door_close_timer );
        bindGroup( "disabled_door_close_time", lbl_disabled_door_close_timer, fmt_disabled_door_close_timer );
        bindGroup( "ddo_call_cool_time", lbl_ddo_call_cool_time, fmt_ddo_call_cool_time );
        bindGroup( "door_opening_timeout", lbl_door_opening_timeout, fmt_door_opening_timeout );
        bindGroup( "door_opened_timeout", lbl_door_opened_timeout, fmt_door_opened_timeout );
        bindGroup( "door_closing_timeout", lbl_door_closing_timeout, fmt_door_closing_timeout );
        bindGroup( "door_closed_timeout", lbl_door_closed_timeout, fmt_door_closed_timeout );
        bindGroup( "door_forced_closed_timeout", lbl_door_forced_closed_timeout, fmt_door_forced_closed_timeout );
        bindGroup( "activation_time", lbl_activation_time, fmt_activation_timer );
        bindGroup( "retry_count", lbl_retry_count, fmt_retry_count );
        bindGroup( "change_station_retry_count", lbl_change_station_retry_count, fmt_change_station_retry_count );
        bindGroup( "door_hold_button_enabled", ebd_enabled );
        bindGroup( "holding_time", lbl_holding_time, fmt_holding_time );
        bindGroup( "independent_control_of_car_door_and_landing_door_enabled", ebd_enabled_0 );
        bindGroup( "front_door_car_calls", lbl_front_door_car_calls, fmt_front_door_car_calls );
        bindGroup( "front_door_hall_calls", lbl_front_door_hall_calls, fmt_front_door_hall_calls );
        bindGroup( "rear_door_car_calls", lbl_rear_door_car_calls, fmt_rear_door_car_calls );
        bindGroup( "rear_door_hall_calls", lbl_rear_door_hall_calls, fmt_rear_door_hall_calls );
        bindGroup( new AbstractButton[]{ ebd_enabled }, lbl_holding_time, fmt_holding_time, lbl_io_door_holding_button, io_door_hold_button );
        bindGroup( new AbstractButton[]{ ebd_enabled_0 }, lbl_front_door_car_calls, fmt_front_door_car_calls, lbl_front_door_hall_calls,
                   fmt_front_door_hall_calls, lbl_rear_door_car_calls, fmt_rear_door_car_calls, lbl_rear_door_hall_calls,
                   fmt_rear_door_hall_calls );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        lbl_door_close_timer.setText( TEXT.getString( "door_close_time" ) );
        lbl_disabled_door_close_timer.setText( TEXT.getString( "disabled_door_close_time" ) );
        lbl_ddo_call_cool_time.setText( TEXT.getString( "ddo_call_cool_time" ) );
        ebd_forced_close_door.setText( TEXT.getString( "ebd_forced_close_door" ));
        
        /* ---------------------------------------------------------------------------- */
        cpt_timeout.setText( TEXT.getString( "timeout" ) );
        lbl_door_opening_timeout.setText( TEXT.getString( "door_opening_timeout" ) );
        lbl_door_opened_timeout.setText( TEXT.getString( "door_opened_timeout" ) );
        lbl_door_closing_timeout.setText( TEXT.getString( "door_closing_timeout" ) );
        lbl_door_closed_timeout.setText( TEXT.getString( "door_closed_timeout" ) );
        lbl_door_forced_closed_timeout.setText( TEXT.getString( "door_forced_closed_timeout" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_safety_chain_fail.setText( TEXT.getString( "safety_chain_fail" ) );
        lbl_activation_time.setText( TEXT.getString( "activation_time" ) );
        lbl_retry_count.setText( TEXT.getString( "retry_count" ) );
        lbl_change_station_retry_count.setText( TEXT.getString( "change_station_retry_count" ) );
        
        /* ---------------------------------------------------------------------------- */
        cpt_door_hold_button.setText( TEXT.getString( "door_hold_button" ) );
        ebd_enabled.setText( TEXT.getString( "door_hold_button_enabled" ) );
        lbl_holding_time.setText( TEXT.getString( "holding_time" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_independent_control_of_car_door_and_landing_door.setText( TEXT.getString( "independent_control_of_car_door_and_landing_door" ) );
        ebd_enabled_0.setText( TEXT.getString( "independent_control_of_car_door_and_landing_door_enabled" ) );
        lbl_front_door_car_calls.setText( TEXT.getString( "front_door_car_calls" ) );
        lbl_front_door_hall_calls.setText( TEXT.getString( "front_door_hall_calls" ) );
        lbl_rear_door_car_calls.setText( TEXT.getString( "rear_door_car_calls" ) );
        lbl_rear_door_hall_calls.setText( TEXT.getString( "rear_door_hall_calls" ) );

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


    public TimeoutBean getTimeoutBean () throws ConvertException {
        if ( ! fmt_door_opening_timeout.checkValue() )
            throw new ConvertException();
        if ( ! fmt_door_opened_timeout.checkValue() )
            throw new ConvertException();
        if ( ! fmt_door_closing_timeout.checkValue() )
            throw new ConvertException();
        if ( ! fmt_door_closed_timeout.checkValue() )
            throw new ConvertException();
        if ( ! fmt_door_forced_closed_timeout.checkValue() )
            throw new ConvertException();

        TimeoutBean bean_timeout = new TimeoutBean();
        bean_timeout.setDoorOpeningTimeout( ( Long )fmt_door_opening_timeout.getValue() );
        bean_timeout.setDoorOpenedTimeout( ( Long )fmt_door_opened_timeout.getValue() );
        bean_timeout.setDoorClosingTimeout( ( Long )fmt_door_closing_timeout.getValue() );
        bean_timeout.setDoorClosedTimeout( ( Long )fmt_door_closed_timeout.getValue() );
        bean_timeout.setDoorForcedClosedTimeout( ( Long )fmt_door_forced_closed_timeout.getValue() );
        return bean_timeout;
    }


    public GeneralBean getGeneralBean () throws ConvertException {
        if ( ! fmt_door_close_timer.checkValue() )
            throw new ConvertException();
        if ( ! fmt_ddo_call_cool_time.checkValue() )
            throw new ConvertException();

        GeneralBean bean_general = new GeneralBean();
        bean_general.setDoorCloseTimer( ( Long )fmt_door_close_timer.getValue() );
        bean_general.setDisabledDoorCloseTimer( ( Long )fmt_disabled_door_close_timer.getValue() );
        bean_general.setDdoCallCoolTime( ( Long )fmt_ddo_call_cool_time.getValue() );
        bean_general.setEnabel_fcd( ebd_forced_close_door.isSelected() );
        return bean_general;
    }


    public SafetyChainFailBean getSafetyChainFailBean () throws ConvertException {
        if ( ! fmt_activation_timer.checkValue() )
            throw new ConvertException();
        if ( ! fmt_retry_count.checkValue() )
            throw new ConvertException();
        if ( ! fmt_change_station_retry_count.checkValue() )
            throw new ConvertException();

        SafetyChainFailBean bean_doorCloseFailureRecovery = new SafetyChainFailBean();
        bean_doorCloseFailureRecovery.setActivationTime( ( Long )fmt_activation_timer.getValue() );
        bean_doorCloseFailureRecovery.setRetryCount( ( Long )fmt_retry_count.getValue() );
        bean_doorCloseFailureRecovery.setChangeStationRetryCount( (Long)fmt_change_station_retry_count.getValue() );
        return bean_doorCloseFailureRecovery;
    }


    public DoorHoldButtonBean getDoorHoldButtonBean () throws ConvertException {
        if ( ! fmt_holding_time.checkValue() )
            throw new ConvertException();

        DoorHoldButtonBean bean_doorHoldButton = new DoorHoldButtonBean();
        bean_doorHoldButton.setEnabled( ebd_enabled.isSelected() );
        bean_doorHoldButton.setHoldingTime( ( Long )fmt_holding_time.getValue() );
        bean_doorHoldButton.setDoorHoldButtonEvent( io_door_hold_button.getEvent() );
        return bean_doorHoldButton;
    }


    public IndependentControlOfCarDoorAndLandingDoorBean getIndependentControlOfCarDoorAndLandingDoorBean () throws ConvertException {
        if ( ! fmt_front_door_car_calls.checkValue() )
            throw new ConvertException();
        if ( ! fmt_front_door_hall_calls.checkValue() )
            throw new ConvertException();
        if ( ! fmt_rear_door_car_calls.checkValue() )
            throw new ConvertException();
        if ( ! fmt_rear_door_hall_calls.checkValue() )
            throw new ConvertException();

        IndependentControlOfCarDoorAndLandingDoorBean bean_independentControlOfCarDoorAndLandingDoor =
            new IndependentControlOfCarDoorAndLandingDoorBean();
        bean_independentControlOfCarDoorAndLandingDoor.setEnabled0( ebd_enabled_0.isSelected() );
        bean_independentControlOfCarDoorAndLandingDoor.setFrontDoorCarCalls( ( Long )fmt_front_door_car_calls.getValue() );
        bean_independentControlOfCarDoorAndLandingDoor.setFrontDoorHallCalls( ( Long )fmt_front_door_hall_calls.getValue() );
        bean_independentControlOfCarDoorAndLandingDoor.setRearDoorCarCalls( ( Long )fmt_rear_door_car_calls.getValue() );
        bean_independentControlOfCarDoorAndLandingDoor.setRearDoorHallCalls( ( Long )fmt_rear_door_hall_calls.getValue() );
        return bean_independentControlOfCarDoorAndLandingDoor;
    }


    public void setTimeoutBean ( TimeoutBean bean_timeout ) {
        this.fmt_door_opening_timeout.setOriginValue( bean_timeout.getDoorOpeningTimeout() );
        this.fmt_door_opened_timeout.setOriginValue( bean_timeout.getDoorOpenedTimeout() );
        this.fmt_door_closing_timeout.setOriginValue( bean_timeout.getDoorClosingTimeout() );
        this.fmt_door_closed_timeout.setOriginValue( bean_timeout.getDoorClosedTimeout() );
        this.fmt_door_forced_closed_timeout.setOriginValue(bean_timeout.getDoorForcedClosedTimeout() );
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
        this.fmt_door_close_timer.setOriginValue( bean_general.getDoorCloseTimer() );
        this.fmt_disabled_door_close_timer.setOriginValue( bean_general.getDisabledDoorCloseTimer() );
        this.fmt_ddo_call_cool_time.setOriginValue( bean_general.getDdoCallCoolTime() );
        this.ebd_forced_close_door.setOriginSelected( bean_general.getEnabel_fcd() );
    }


    public void setSafetyChainFailBean ( SafetyChainFailBean bean_safetyChainFailBean ) {
        this.fmt_activation_timer.setOriginValue( bean_safetyChainFailBean.getActivationTime() );
        this.fmt_retry_count.setOriginValue( bean_safetyChainFailBean.getRetryCount() );
        this.fmt_change_station_retry_count.setOriginValue( bean_safetyChainFailBean.getChangeStationRetryCount() );
    }


    public void setDoorHoldButtonBean ( DoorHoldButtonBean bean_doorHoldButton ) {
        this.ebd_enabled.setOriginSelected( bean_doorHoldButton.getEnabled() != null && bean_doorHoldButton.getEnabled() == true );
        this.fmt_holding_time.setOriginValue( bean_doorHoldButton.getHoldingTime() );
        this.io_door_hold_button.setEvent( bean_doorHoldButton.getDoorHoldButtonEvent() );
    }


    public void setIndependentControlOfCarDoorAndLandingDoorBean (
            IndependentControlOfCarDoorAndLandingDoorBean bean_independentControlOfCarDoorAndLandingDoor ) {
        this.ebd_enabled_0.setOriginSelected( bean_independentControlOfCarDoorAndLandingDoor.getEnabled0() != null
                                        && bean_independentControlOfCarDoorAndLandingDoor.getEnabled0() == true );
        this.fmt_front_door_car_calls.setOriginValue( bean_independentControlOfCarDoorAndLandingDoor.getFrontDoorCarCalls() );
        this.fmt_front_door_hall_calls.setOriginValue( bean_independentControlOfCarDoorAndLandingDoor.getFrontDoorHallCalls() );
        this.fmt_rear_door_car_calls.setOriginValue( bean_independentControlOfCarDoorAndLandingDoor.getRearDoorCarCalls() );
        this.fmt_rear_door_hall_calls.setOriginValue( bean_independentControlOfCarDoorAndLandingDoor.getRearDoorHallCalls() );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static DoorTiming createPanel ( SettingPanel<DoorTiming> panel ) {
        DoorTiming gui = new DoorTiming();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class DoorHoldButtonBean {
        private Boolean enabled;
        private Long    holdingTime;
        private Event   doorHoldButtonEvent;




        public final Boolean getEnabled () {
            return enabled;
        }


        public final Long getHoldingTime () {
            return holdingTime;
        }


        public final Event getDoorHoldButtonEvent () {
            return doorHoldButtonEvent;
        }


        public final void setEnabled ( Boolean enabled ) {
            this.enabled = enabled;
        }


        public final void setHoldingTime ( Long holdingTime ) {
            this.holdingTime = holdingTime;
        }


        public final void setDoorHoldButtonEvent ( Event doorHoldButtonEvent ) {
            this.doorHoldButtonEvent = doorHoldButtonEvent;
        }
    }




    public static class GeneralBean {
        private Long doorCloseTimer;
        private Long disabledDoorCloseTimer;
        private Long ddoCallCoolTime;
        private Boolean enabel_fcd;
        
        public Long getDoorCloseTimer () {
            return this.doorCloseTimer;
        }

        public Long getDdoCallCoolTime() {
            return ddoCallCoolTime;
        }

        public void setDoorCloseTimer ( Long doorCloseTimer ) {
            this.doorCloseTimer = doorCloseTimer;
        }
        
        public void setDdoCallCoolTime(Long ddoCallCoolTime) {
            this.ddoCallCoolTime = ddoCallCoolTime;
        }

		public Long getDisabledDoorCloseTimer() {
			return disabledDoorCloseTimer;
		}

		public void setDisabledDoorCloseTimer(Long disabledDoorCloseTimer) {
			this.disabledDoorCloseTimer = disabledDoorCloseTimer;
		}

		public Boolean getEnabel_fcd() {
			return enabel_fcd;
		}

		public void setEnabel_fcd(Boolean enabel_fcd) {
			this.enabel_fcd = enabel_fcd;
		}
    }




    public static class IndependentControlOfCarDoorAndLandingDoorBean {
        private Boolean enabled0;
        private Long    frontDoorCarCalls;
        private Long    frontDoorHallCalls;
        private Long    rearDoorCarCalls;
        private Long    rearDoorHallCalls;




        public Boolean getEnabled0 () {
            return this.enabled0;
        }


        public Long getFrontDoorCarCalls () {
            return this.frontDoorCarCalls;
        }


        public Long getFrontDoorHallCalls () {
            return this.frontDoorHallCalls;
        }


        public Long getRearDoorCarCalls () {
            return this.rearDoorCarCalls;
        }


        public Long getRearDoorHallCalls () {
            return this.rearDoorHallCalls;
        }


        public void setEnabled0 ( Boolean enabled0 ) {
            this.enabled0 = enabled0;
        }


        public void setFrontDoorCarCalls ( Long frontDoorCarCalls ) {
            this.frontDoorCarCalls = frontDoorCarCalls;
        }


        public void setFrontDoorHallCalls ( Long frontDoorHallCalls ) {
            this.frontDoorHallCalls = frontDoorHallCalls;
        }


        public void setRearDoorCarCalls ( Long rearDoorCarCalls ) {
            this.rearDoorCarCalls = rearDoorCarCalls;
        }


        public void setRearDoorHallCalls ( Long rearDoorHallCalls ) {
            this.rearDoorHallCalls = rearDoorHallCalls;
        }
    }




    public static class SafetyChainFailBean {
        private Long activationTime;
        private Long retryCount;
        private Long changeStationRetryCount;


        public Long getActivationTime () {
            return this.activationTime;
        }


        public Long getRetryCount () {
            return this.retryCount;
        }


        public void setActivationTime ( Long checkingTimerOnDoorClosedFailed ) {
            this.activationTime = checkingTimerOnDoorClosedFailed;
        }


        public void setRetryCount ( Long retryCount ) {
            this.retryCount = retryCount;
        }


		public Long getChangeStationRetryCount() {
			return changeStationRetryCount;
		}


		public void setChangeStationRetryCount(Long changeStationRetryCount) {
			this.changeStationRetryCount = changeStationRetryCount;
		}
        
    }




    public static class TimeoutBean {
        private Long doorOpeningTimeout;
        private Long doorOpenedTimeout;
        private Long doorClosingTimeout;
        private Long doorClosedTimeout;
        private Long doorForcedClosedTimeout;

        public Long getDoorOpeningTimeout () {
            return this.doorOpeningTimeout;
        }


        public Long getDoorOpenedTimeout () {
            return this.doorOpenedTimeout;
        }


        public Long getDoorClosingTimeout () {
            return this.doorClosingTimeout;
        }


        public Long getDoorClosedTimeout () {
            return this.doorClosedTimeout;
        }


        public Long getDoorForcedClosedTimeout() {
			return doorForcedClosedTimeout;
		}


		public void setDoorOpeningTimeout ( Long doorOpeningTimeout ) {
            this.doorOpeningTimeout = doorOpeningTimeout;
        }


        public void setDoorOpenedTimeout ( Long doorOpenedTimeout ) {
            this.doorOpenedTimeout = doorOpenedTimeout;
        }


        public void setDoorClosingTimeout ( Long doorClosingTimeout ) {
            this.doorClosingTimeout = doorClosingTimeout;
        }


        public void setDoorClosedTimeout ( Long doorClosedTimeout ) {
            this.doorClosedTimeout = doorClosedTimeout;
        }


		public void setDoorForcedClosedTimeout(Long doorForcedClosedTimeout) {
			this.doorForcedClosedTimeout = doorForcedClosedTimeout;
		}
        
    }
}
