package slecon.setting.modules;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import logic.EventID;
import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueTextField;
import slecon.component.iobar.IOBar;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.interfaces.ConvertException;
import slecon.setting.modules.LedBehavior.Led_Behavior;
import base.cfg.FontFactory;

import comm.constants.DeviceMessage;




/**
 * Setup -> Module -> Parking.
 */
public class Parking extends JPanel {
    private static final long serialVersionUID = -7667454930494218772L;
    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT    = ToolBox.getResourceBundle( "setting.module.Parking" );
    private boolean                    started = false;
    private SettingPanel<Parking>      settingPanel;
    private JLabel                     cpt_general;
    private ValueCheckBox              ebd_enabled;
    private JLabel                     lbl_return_floor;
    private MyComboBox			       cbo_return_floor;
    private JLabel                     lbl_door_open_led_behavior;
    private MyComboBox				   cbo_door_open_led_behavior;
    private JLabel                     lbl_car_message;
    private MyComboBox				   cbo_car_message;
    private JLabel                     lbl_hall_message;
    private MyComboBox				   cbo_hall_message;
    private ValueCheckBox              ebd_open_door_before_go_return_floor;
    private ValueCheckBox              ebd_open_door_once_arrival_return_floor;
    private ValueCheckBox  			   ebd_enabled_edp;
    private ValueCheckBox 			   ebd_enabled_sgs;

    /* ---------------------------------------------------------------------------- */
    private JLabel cpt_i_o_settings;
    private IOBar  io_parking_switch;

    /* ---------------------------------------------------------------------------- */
    private JLabel    cpt_car_call_setting;
    private ValueCheckBox ebd_clear_car_calls;
    private ValueCheckBox ebd_reject_any_incomming_car_calls;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_dim_devices;
    private ValueCheckBox      ebd_enabled_energy_saving_on_car;
    private ValueCheckBox      ebd_enabled_energy_saving_on_hall;
    private JLabel         lbl_activation_timer;
    private ValueTextField fmt_activation_timer;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_open_door_button;
    private ValueCheckBox      ebd_enabled_open_door_button_led;
    private JLabel         lbl_activation_timer_1;
    private ValueTextField fmt_activation_timer_1;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_dcs_fan;
    private ValueCheckBox      ebd_disable_dcs_fan;
    private JLabel         lbl_activation_timer_2;
    private ValueTextField fmt_activation_timer_2;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_dcs_light;
    private ValueCheckBox      ebd_disable_dcs_light;
    private JLabel         lbl_activation_timer_3;
    private ValueTextField fmt_activation_timer_3;




    public Parking () {
        try {
            initGUI();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


    public void setSettingPanel ( SettingPanel<Parking> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_general                             = new JLabel();
        ebd_enabled                             = new ValueCheckBox();
        lbl_return_floor                        = new JLabel();
        cbo_return_floor                        = new MyComboBox();
        lbl_door_open_led_behavior 				= new JLabel();
        cbo_door_open_led_behavior 				= new MyComboBox( Led_Behavior.values() );
        lbl_car_message                         = new JLabel();
        cbo_car_message                         = new MyComboBox( DeviceMessage.values() );
        lbl_hall_message                        = new JLabel();
        cbo_hall_message                        = new MyComboBox( DeviceMessage.values() );
        ebd_open_door_before_go_return_floor    = new ValueCheckBox();
        ebd_open_door_once_arrival_return_floor = new ValueCheckBox();
        ebd_enabled_edp  = new ValueCheckBox();
        ebd_enabled_sgs  = new ValueCheckBox();
        setCaptionStyle( cpt_general );
        setComboBoxLabelStyle( lbl_return_floor );
        setComboBoxValueStyle( cbo_return_floor );
        cbo_return_floor.setPreferredSize(new Dimension(120, 25));
        setTextLabelStyle( lbl_door_open_led_behavior );
        cbo_door_open_led_behavior.setPreferredSize(new Dimension(120, 25));
        setComboBoxLabelStyle( lbl_car_message );
        setComboBoxValueStyle( cbo_car_message );
        setComboBoxLabelStyle( lbl_hall_message );
        setComboBoxValueStyle( cbo_hall_message );

        add( cpt_general, "gapbottom 18-12, span, top" );
        add( ebd_enabled, "skip 1, span, top" );
        add( lbl_return_floor, "skip 2, span 1, left, top" );
        add( cbo_return_floor, "span 1, left, wrap, top" );
        add( lbl_door_open_led_behavior, "skip 2, span 1, left, top" );
        add( cbo_door_open_led_behavior, "span 1, left, wrap, top" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, left, wrap, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, left, wrap, top" );
        add( ebd_enabled_edp, "skip 2, span 1, left, top" );
        add( ebd_enabled_sgs, "span 1, left, wrap, top" );
        add( ebd_open_door_before_go_return_floor, "skip 2, span 1, left, top" );
        add( ebd_open_door_once_arrival_return_floor, "span 1, left, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings = new JLabel();
        JLabel lbl_io_parking_switch = new JLabel();
        io_parking_switch = new IOBar( false );
        setCaptionStyle( cpt_i_o_settings );
        setTextLabelStyle( lbl_io_parking_switch );
        lbl_io_parking_switch.setText( EventID.getString( EventID.PARKING_SWITCH.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_parking_switch, EventID.PARKING_SWITCH.eventID );
        add( cpt_i_o_settings, "gapbottom 18-12, span, aligny center" );
        add( lbl_io_parking_switch, "skip 2, span, gapright 12" );
        add( io_parking_switch, "skip 2, span, gapright 12, wrap 30" );

        /* ---------------------------------------------------------------------------- */
        cpt_car_call_setting               = new JLabel();
        ebd_clear_car_calls                = new ValueCheckBox();
        ebd_reject_any_incomming_car_calls = new ValueCheckBox();
        setCaptionStyle( cpt_car_call_setting );
        add( cpt_car_call_setting, "gapbottom 18-12, span, aligny center" );
        add( ebd_clear_car_calls, "skip 1, span" );
        add( ebd_reject_any_incomming_car_calls, "skip 1, span, wrap 30" );

        /* ---------------------------------------------------------------------------- */
        cpt_dim_devices                   = new JLabel();
        ebd_enabled_energy_saving_on_car  = new ValueCheckBox();
        ebd_enabled_energy_saving_on_hall = new ValueCheckBox();
        lbl_activation_timer              = new JLabel();
        fmt_activation_timer              = new ValueTextField();
        setCaptionStyle( cpt_dim_devices );
        setTextLabelStyle( lbl_activation_timer );
        fmt_activation_timer.setColumns( 10 );
        fmt_activation_timer.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_activation_timer.setScope( Long.class, 0L, null, false, false );
        fmt_activation_timer.setEmptyValue( 0L );
        add( cpt_dim_devices, "gapbottom 18-12, span, aligny center" );
        add( ebd_enabled_energy_saving_on_car, "skip 1, span" );
        add( ebd_enabled_energy_saving_on_hall, "skip 1, span" );
        add( lbl_activation_timer, "skip 2, span 1, left, top" );
        add( fmt_activation_timer, "span 1, left, wrap 30" );

        /* ---------------------------------------------------------------------------- */
        cpt_open_door_button             = new JLabel();
        ebd_enabled_open_door_button_led = new ValueCheckBox();
        lbl_activation_timer_1           = new JLabel();
        fmt_activation_timer_1           = new ValueTextField();
        setCaptionStyle( cpt_open_door_button );
        setTextLabelStyle( lbl_activation_timer_1 );
        fmt_activation_timer_1.setColumns( 10 );
        fmt_activation_timer_1.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_activation_timer_1.setScope( Long.class, 0L, null, false, false );
        fmt_activation_timer_1.setEmptyValue( 0L );
        add( cpt_open_door_button, "gapbottom 18-12, span, aligny center" );
        add( ebd_enabled_open_door_button_led, "skip 1, span" );
        add( lbl_activation_timer_1, "skip 2, span 1, left, top" );
        add( fmt_activation_timer_1, "span 1, left, wrap 30" );

        /* ---------------------------------------------------------------------------- */
        cpt_dcs_fan            = new JLabel();
        ebd_disable_dcs_fan    = new ValueCheckBox();
        lbl_activation_timer_2 = new JLabel();
        fmt_activation_timer_2 = new ValueTextField();
        setCaptionStyle( cpt_dcs_fan );
        setTextLabelStyle( lbl_activation_timer_2 );
        fmt_activation_timer_2.setColumns( 10 );
        fmt_activation_timer_2.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_activation_timer_2.setScope( Long.class, 0L, null, false, false );
        fmt_activation_timer_2.setEmptyValue( 0L );
        add( cpt_dcs_fan, "gapbottom 18-12, span, aligny center" );
        add( ebd_disable_dcs_fan, "skip 1, span" );
        add( lbl_activation_timer_2, "skip 2, span 1, left, top" );
        add( fmt_activation_timer_2, "span 1, left, wrap 30" );

        /* ---------------------------------------------------------------------------- */
        cpt_dcs_light          = new JLabel();
        ebd_disable_dcs_light  = new ValueCheckBox();
        lbl_activation_timer_3 = new JLabel();
        fmt_activation_timer_3 = new ValueTextField();
        setCaptionStyle( cpt_dcs_light );
        setTextLabelStyle( lbl_activation_timer_3 );
        fmt_activation_timer_3.setColumns( 10 );
        fmt_activation_timer_3.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_activation_timer_3.setScope( Long.class, 0L, null, false, false );
        fmt_activation_timer_3.setEmptyValue( 0L );
        add( cpt_dcs_light, "gapbottom 18-12, span, aligny center" );
        add( ebd_disable_dcs_light, "skip 1, span" );
        add( lbl_activation_timer_3, "skip 2, span 1, left, top" );
        add( fmt_activation_timer_3, "span 1, left, wrap 30" );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "return_floor", lbl_return_floor, cbo_return_floor );
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        bindGroup( "enabled_edp", ebd_enabled_edp);
        bindGroup( "enabled_sgs", ebd_enabled_sgs);
        bindGroup( "open_door_before_go_to_return_floor", ebd_open_door_before_go_return_floor );
        bindGroup( "open_door_once_arrival_return_floor", ebd_open_door_once_arrival_return_floor );
        bindGroup( "clear_car_call", ebd_clear_car_calls );
        bindGroup( "reject_new_call", ebd_reject_any_incomming_car_calls );
        bindGroup( "enable_energy_saving_on_car", ebd_enabled_energy_saving_on_car );
        bindGroup( "enable_energy_saving_on_hall", ebd_enabled_energy_saving_on_hall );
        bindGroup( "enable_energy_saving_activation_time", lbl_activation_timer, fmt_activation_timer );
        bindGroup( "enabled_open_door_button_led", ebd_enabled_open_door_button_led );
        bindGroup( "open_door_button_led_activation_time", lbl_activation_timer_1, fmt_activation_timer_1 );
        bindGroup( "disable_cabin_fan", ebd_disable_dcs_fan );
        bindGroup( "disable_cabin_fan_activation_time", lbl_activation_timer_2, fmt_activation_timer_2 );
        bindGroup( "disable_cabin_light", ebd_disable_dcs_light );
        bindGroup( "disable_cabin_light_activation_time", lbl_activation_timer_3, fmt_activation_timer_3 );
        bindGroup( new AbstractButton[]{ ebd_enabled }, lbl_return_floor, cbo_return_floor, lbl_car_message, cbo_car_message, lbl_hall_message,
                   cbo_hall_message, ebd_enabled_edp, ebd_enabled_sgs, ebd_open_door_before_go_return_floor, ebd_open_door_once_arrival_return_floor,
                   lbl_io_parking_switch, io_parking_switch, ebd_clear_car_calls, ebd_reject_any_incomming_car_calls, ebd_enabled_energy_saving_on_car,
                   ebd_enabled_energy_saving_on_hall, ebd_enabled_open_door_button_led, ebd_disable_dcs_fan, ebd_disable_dcs_light,
                   lbl_door_open_led_behavior, cbo_door_open_led_behavior);
        bindGroup( new AbstractButton[]{ ebd_enabled_energy_saving_on_car, ebd_enabled_energy_saving_on_hall }, lbl_activation_timer,
                   fmt_activation_timer );
        bindGroup( new AbstractButton[]{ ebd_enabled_open_door_button_led }, lbl_activation_timer_1, fmt_activation_timer_1 );
        bindGroup( new AbstractButton[]{ ebd_disable_dcs_fan }, lbl_activation_timer_2, fmt_activation_timer_2 );
        bindGroup( new AbstractButton[]{ ebd_disable_dcs_light }, lbl_activation_timer_3, fmt_activation_timer_3 );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_return_floor.setText( TEXT.getString( "return_floor" ) );
        lbl_door_open_led_behavior.setText( TEXT.getString( "door_open_led_behavior" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );
        ebd_enabled_edp.setText( TEXT.getString( "enabled_edp" ) );
        ebd_enabled_sgs.setText( TEXT.getString( "enabled_sgs" ) );
        ebd_open_door_before_go_return_floor.setText( TEXT.getString( "open_door_before_go_to_return_floor" ) );
        ebd_open_door_once_arrival_return_floor.setText( TEXT.getString( "open_door_once_arrival_return_floor" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings.setText( TEXT.getString( "event_setting" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_car_call_setting.setText( TEXT.getString( "car_call_setting" ) );
        ebd_clear_car_calls.setText( TEXT.getString( "clear_car_call" ) );
        ebd_reject_any_incomming_car_calls.setText( TEXT.getString( "reject_new_call" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_dim_devices.setText( TEXT.getString( "energy_saving" ) );
        ebd_enabled_energy_saving_on_car.setText( TEXT.getString( "enable_energy_saving_on_car" ) );
        ebd_enabled_energy_saving_on_hall.setText( TEXT.getString( "enable_energy_saving_on_hall" ) );
        lbl_activation_timer.setText( TEXT.getString( "enable_energy_saving_activation_time" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_open_door_button.setText( TEXT.getString( "open_door_button_led" ) );
        ebd_enabled_open_door_button_led.setText( TEXT.getString( "enabled_open_door_button_led" ) );
        lbl_activation_timer_1.setText( TEXT.getString( "open_door_button_led_activation_time" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_dcs_fan.setText( TEXT.getString( "cabin_fan" ) );
        ebd_disable_dcs_fan.setText( TEXT.getString( "disable_cabin_fan" ) );
        lbl_activation_timer_2.setText( TEXT.getString( "disable_cabin_fan_activation_time" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_dcs_light.setText( TEXT.getString( "cabin_light" ) );
        ebd_disable_dcs_light.setText( TEXT.getString( "disable_cabin_light" ) );
        lbl_activation_timer_3.setText( TEXT.getString( "disable_cabin_light_activation_time" ) );

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


    private void setComboBoxLabelStyle ( JComponent c ) {
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


    public GeneralBean getGeneralBean () throws ConvertException {
        GeneralBean bean_general = new GeneralBean();
        bean_general.setEnabled( ebd_enabled.isSelected() );
        bean_general.setReturnFloor( ( FloorText )cbo_return_floor.getSelectedItem() );
        bean_general.setLedBehavior( (LedBehavior.Led_Behavior)cbo_door_open_led_behavior.getSelectedItem() );
        bean_general.setCarMessage( ( DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_general.setHallMessage( ( DeviceMessage )cbo_hall_message.getSelectedItem() );
        bean_general.setEnabled_edp(ebd_enabled_edp.isSelected());
        bean_general.setEnabled_sgs(ebd_enabled_sgs.isSelected());
        bean_general.setOpenDoorBeforeGoToReturnFloor( ebd_open_door_before_go_return_floor.isSelected() );
        bean_general.setOpenDoorOnceArrivalReturnFloor( ebd_open_door_once_arrival_return_floor.isSelected() );
        return bean_general;
    }


    public DcsFanBean getDcsFanBean () throws ConvertException {
        if ( ! fmt_activation_timer_2.checkValue() )
            throw new ConvertException();

        DcsFanBean bean_dcsFan = new DcsFanBean();
        bean_dcsFan.setDisableDcsFan( ebd_disable_dcs_fan.isSelected() );
        bean_dcsFan.setActivationTimer0( ( Long )fmt_activation_timer_2.getValue() );
        return bean_dcsFan;
    }


    public DimDevicesBean getDimDevicesBean () throws ConvertException {
        if ( ! fmt_activation_timer.checkValue() )
            throw new ConvertException();

        DimDevicesBean bean_dimDevices = new DimDevicesBean();
        bean_dimDevices.setEnabledDimDeviceOnCar( ebd_enabled_energy_saving_on_car.isSelected() );
        bean_dimDevices.setEnabledDimDeviceOnHall( ebd_enabled_energy_saving_on_hall.isSelected() );
        bean_dimDevices.setDimTimer( ( Long )fmt_activation_timer.getValue() );
        return bean_dimDevices;
    }


    public OpenDoorButtonBean getOpenDoorButtonBean () throws ConvertException {
        if ( ! fmt_activation_timer_1.checkValue() )
            throw new ConvertException();

        OpenDoorButtonBean bean_openDoorButton = new OpenDoorButtonBean();
        bean_openDoorButton.setEnabledOpenDoorButtonLed( ebd_enabled_open_door_button_led.isSelected() );
        bean_openDoorButton.setActivationTimer( ( Long )fmt_activation_timer_1.getValue() );
        return bean_openDoorButton;
    }


    public DcsLightBean getDcsLightBean () throws ConvertException {
        if ( ! fmt_activation_timer_3.checkValue() )
            throw new ConvertException();

        DcsLightBean bean_dcsLight = new DcsLightBean();
        bean_dcsLight.setDisableDcsLight( ebd_disable_dcs_light.isSelected() );
        bean_dcsLight.setActivationTimer1( ( Long )fmt_activation_timer_3.getValue() );
        return bean_dcsLight;
    }


    public CarCallSettingBean getCarCallSettingBean () throws ConvertException {
        CarCallSettingBean bean_carCallSetting = new CarCallSettingBean();
        bean_carCallSetting.setClearCarCalls( ebd_clear_car_calls.isSelected() );
        bean_carCallSetting.setRejectAnyIncommingCarCalls( ebd_reject_any_incomming_car_calls.isSelected() );
        return bean_carCallSetting;
    }


    public IOSettingsBean getIOSettingsBean () throws ConvertException {
        IOSettingsBean bean_iOSettings = new IOSettingsBean();
        bean_iOSettings.setParkingSwitchEvent( io_parking_switch.getEvent() );
        return bean_iOSettings;
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.cbo_return_floor.setSelectedItem( bean_general.getReturnFloor() );
        this.cbo_door_open_led_behavior.setSelectedItem( bean_general.getLedBehavior());
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
        this.ebd_enabled_edp.setOriginSelected(bean_general.getEnabled_edp());
        this.ebd_enabled_sgs.setOriginSelected(bean_general.getEnabled_sgs());
        this.ebd_open_door_before_go_return_floor.setOriginSelected( bean_general.getOpenDoorBeforeGoToReturnFloor() != null
                                                               && bean_general.getOpenDoorBeforeGoToReturnFloor() == true );
        this.ebd_open_door_once_arrival_return_floor.setOriginSelected( bean_general.getOpenDoorOnceArrivalReturnFloor() != null
                                                                  && bean_general.getOpenDoorOnceArrivalReturnFloor() == true );
    }


    public void setDcsFanBean ( DcsFanBean bean_dcsFan ) {
        this.ebd_disable_dcs_fan.setOriginSelected( bean_dcsFan.getDisableDcsFan() != null && bean_dcsFan.getDisableDcsFan() == true );
        this.fmt_activation_timer_2.setOriginValue( bean_dcsFan.getActivationTimer0() );
    }


    public void setDimDevicesBean ( DimDevicesBean bean_dimDevices ) {
        this.ebd_enabled_energy_saving_on_car.setOriginSelected( bean_dimDevices.getEnabledDimDeviceOnCar() != null
                                                           && bean_dimDevices.getEnabledDimDeviceOnCar() == true );
        this.ebd_enabled_energy_saving_on_hall.setOriginSelected( bean_dimDevices.getEnabledDimDeviceOnHall() != null
                                                            && bean_dimDevices.getEnabledDimDeviceOnHall() == true );
        this.fmt_activation_timer.setOriginValue( bean_dimDevices.getDimTimer() );
    }


    public void setOpenDoorButtonBean ( OpenDoorButtonBean bean_openDoorButton ) {
        this.ebd_enabled_open_door_button_led.setOriginSelected( bean_openDoorButton.getEnabledOpenDoorButtonLed() != null
                                                           && bean_openDoorButton.getEnabledOpenDoorButtonLed() == true );
        this.fmt_activation_timer_1.setOriginValue( bean_openDoorButton.getActivationTimer() );
    }


    public void setDcsLightBean ( DcsLightBean bean_dcsLight ) {
        this.ebd_disable_dcs_light.setOriginSelected( bean_dcsLight.getDisableDcsLight() != null && bean_dcsLight.getDisableDcsLight() == true );
        this.fmt_activation_timer_3.setOriginValue( bean_dcsLight.getActivationTimer1() );
    }


    public void setCarCallSettingBean ( CarCallSettingBean bean_carCallSetting ) {
        this.ebd_clear_car_calls.setOriginSelected( bean_carCallSetting.getClearCarCalls() != null
                                              && bean_carCallSetting.getClearCarCalls() == true );
        this.ebd_reject_any_incomming_car_calls.setOriginSelected( bean_carCallSetting.getRejectAnyIncommingCarCalls() != null
                                                             && bean_carCallSetting.getRejectAnyIncommingCarCalls() == true );
    }


    public void setIOSettingsBean ( IOSettingsBean bean_iOSettings ) {
        this.io_parking_switch.setEvent( bean_iOSettings.getParkingSwitchEvent() );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static Parking createPanel ( SettingPanel<Parking> panel ) {
        try {
            Parking gui = new Parking();
            gui.setSettingPanel( panel );
            return gui;
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }


    public void setFloorText ( ArrayList<FloorText> list ) {
        cbo_return_floor.removeAllItems();
        for ( FloorText text : list )
            cbo_return_floor.addItem( text );
    }


    public static class CarCallSettingBean {
        private Boolean clearCarCalls;
        private Boolean rejectAnyIncommingCarCalls;




        public Boolean getClearCarCalls () {
            return this.clearCarCalls;
        }


        public Boolean getRejectAnyIncommingCarCalls () {
            return this.rejectAnyIncommingCarCalls;
        }


        public void setClearCarCalls ( Boolean clearCarCalls ) {
            this.clearCarCalls = clearCarCalls;
        }


        public void setRejectAnyIncommingCarCalls ( Boolean rejectAnyIncommingCarCalls ) {
            this.rejectAnyIncommingCarCalls = rejectAnyIncommingCarCalls;
        }
    }




    public static class DcsFanBean {
        private Boolean disableDcsFan;
        private Long    activationTimer0;




        public Boolean getDisableDcsFan () {
            return this.disableDcsFan;
        }


        public Long getActivationTimer0 () {
            return this.activationTimer0;
        }


        public void setDisableDcsFan ( Boolean disableDcsFan ) {
            this.disableDcsFan = disableDcsFan;
        }


        public void setActivationTimer0 ( Long activationTimer0 ) {
            this.activationTimer0 = activationTimer0;
        }
    }




    public static class DcsLightBean {
        private Boolean disableDcsLight;
        private Long    activationTimer1;




        public Boolean getDisableDcsLight () {
            return this.disableDcsLight;
        }


        public Long getActivationTimer1 () {
            return this.activationTimer1;
        }


        public void setDisableDcsLight ( Boolean disableDcsLight ) {
            this.disableDcsLight = disableDcsLight;
        }


        public void setActivationTimer1 ( Long activationTimer1 ) {
            this.activationTimer1 = activationTimer1;
        }
    }




    public static class DimDevicesBean {
        private Boolean enabledDimDeviceOnCar;
        private Boolean enabledDimDeviceOnHall;
        private Long    dimTimer;




        public Boolean getEnabledDimDeviceOnCar () {
            return this.enabledDimDeviceOnCar;
        }


        public Boolean getEnabledDimDeviceOnHall () {
            return this.enabledDimDeviceOnHall;
        }


        public Long getDimTimer () {
            return this.dimTimer;
        }


        public void setEnabledDimDeviceOnCar ( Boolean enabledDimDeviceOnCar ) {
            this.enabledDimDeviceOnCar = enabledDimDeviceOnCar;
        }


        public void setEnabledDimDeviceOnHall ( Boolean enabledDimDeviceOnHall ) {
            this.enabledDimDeviceOnHall = enabledDimDeviceOnHall;
        }


        public void setDimTimer ( Long dimTimer ) {
            this.dimTimer = dimTimer;
        }
    }




    public static class GeneralBean {
        private Boolean       enabled;
        private FloorText     returnFloor;
        private Led_Behavior  ledBehavior; 
        private DeviceMessage carMessage;
        private DeviceMessage hallMessage;
        private Boolean		  enabled_edp;
        private Boolean		  enabled_sgs;			
        private Boolean       openDoorBeforeGoToReturnFloor;
        private Boolean       openDoorOnceArrivalReturnFloor;


        public Boolean getEnabled () {
            return enabled;
        }


        public FloorText getReturnFloor () {
            return returnFloor;
        }


        public DeviceMessage getCarMessage () {
            return carMessage;
        }


        public DeviceMessage getHallMessage () {
            return hallMessage;
        }
        

        public Boolean getEnabled_edp() {
			return enabled_edp;
		}


		public Boolean getEnabled_sgs() {
			return enabled_sgs;
		}


		public Boolean getOpenDoorBeforeGoToReturnFloor () {
            return openDoorBeforeGoToReturnFloor;
        }


        public Boolean getOpenDoorOnceArrivalReturnFloor () {
            return openDoorOnceArrivalReturnFloor;
        }


        public void setEnabled ( Boolean enabled ) {
            this.enabled = enabled;
        }


        public void setReturnFloor ( FloorText returnFloor ) {
            this.returnFloor = returnFloor;
        }


        public void setCarMessage ( DeviceMessage carMessage ) {
            this.carMessage = carMessage;
        }


        public void setHallMessage ( DeviceMessage hallMessage ) {
            this.hallMessage = hallMessage;
        }


        public void setEnabled_edp(Boolean enabled_edp) {
			this.enabled_edp = enabled_edp;
		}


		public void setEnabled_sgs(Boolean enabled_sgs) {
			this.enabled_sgs = enabled_sgs;
		}


		public void setOpenDoorBeforeGoToReturnFloor ( Boolean openDoorBeforeGoToReturnFloor ) {
            this.openDoorBeforeGoToReturnFloor = openDoorBeforeGoToReturnFloor;
        }


        public void setOpenDoorOnceArrivalReturnFloor ( Boolean openDoorOnceArrivalReturnFloor ) {
            this.openDoorOnceArrivalReturnFloor = openDoorOnceArrivalReturnFloor;
        }


		public Led_Behavior getLedBehavior() {
			return ledBehavior;
		}

		public void setLedBehavior(Led_Behavior ledBehavior) {
			this.ledBehavior = ledBehavior;
		}
    }




    public static class IOSettingsBean {
        private Event parkingSwitchEvent;




        public final Event getParkingSwitchEvent () {
            return parkingSwitchEvent;
        }


        public final void setParkingSwitchEvent ( Event parkingSwitchEvent ) {
            this.parkingSwitchEvent = parkingSwitchEvent;
        }
    }




    public static class OpenDoorButtonBean {
        private Boolean enabledOpenDoorButtonLed;
        private Long    activationTimer;




        public Boolean getEnabledOpenDoorButtonLed () {
            return this.enabledOpenDoorButtonLed;
        }


        public Long getActivationTimer () {
            return this.activationTimer;
        }


        public void setEnabledOpenDoorButtonLed ( Boolean enabledOpenDoorButtonLed ) {
            this.enabledOpenDoorButtonLed = enabledOpenDoorButtonLed;
        }


        public void setActivationTimer ( Long activationTimer ) {
            this.activationTimer = activationTimer;
        }
    }
}
