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
import javax.swing.ButtonGroup;
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
import slecon.component.ValueRadioButton;
import slecon.component.ValueTextField;
import slecon.component.iobar.IOBar;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.interfaces.ConvertException;
import slecon.setting.modules.FiremansEmergencyOperation.StrategyBean;
import slecon.setting.modules.LedBehavior.Led_Behavior;
import base.cfg.FontFactory;



/**
 * Setup -> Module -> Fire Emergency Return Operation.
 */
public class FireEmergencyReturnOperation extends JPanel {
    private static final long serialVersionUID = 6540945249043894216L;

    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.module.FireEmergencyReturnOperation" );

    private boolean                              started = false;
    private SettingPanel<FireEmergencyReturnOperation>    settingPanel;
    private JLabel                               cpt_general;
    private ValueCheckBox                        ebd_enabled;
    private JLabel                               lbl_return_floor;
    private MyComboBox                 			 cbo_return_floor;
    private JLabel                               lbl_door_open_led_behavior;
    private MyComboBox				             cbo_door_open_led_behavior;
    private JLabel         						 lbl_door_close_timer;
    private ValueTextField 						 fmt_door_close_timer;
    private JLabel                               lbl_car_message;
    private MyComboBox							 cbo_car_message;
    private JLabel                               lbl_hall_message;
    private MyComboBox							 cbo_hall_message;
    private ValueCheckBox  						 ebd_enabled_edp;
    private ValueCheckBox  						 ebd_enabled_sgs;
    private ValueCheckBox  						 ebd_enabled_front_buzzer;
    private ValueCheckBox  						 ebd_enabled_rear_buzzer;


    /* ---------------------------------------------------------------------------- */
    private JLabel cpt_i_o_settings;
    private IOBar  io_fro_switch;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_dim_devices;
    private ValueCheckBox  ebd_enabled_dim_device_on_car;
    private ValueCheckBox  ebd_enabled_dim_device_on_hall;
    private JLabel         lbl_dim_timer;
    private ValueTextField fmt_dim_timer;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_dcs_fan;
    private ValueCheckBox  ebd_disable_dcs_fan;
    private JLabel         lbl_activation_timer;
    private ValueTextField fmt_activation_timer;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_dcs_light;
    private ValueCheckBox  ebd_disable_dcs_light;
    private JLabel         lbl_activation_timer_0;
    private ValueTextField fmt_activation_timer_0;

    /* ---------------------------------------------------------------------------- */
    private JLabel       cpt_strategy;
    private ValueRadioButton ebd_enabled_stragety_a;
    private ValueRadioButton ebd_enabled_stragety_b;


    public FireEmergencyReturnOperation () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<FireEmergencyReturnOperation> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][200::200][150::150][]" ) );
        cpt_general      = new JLabel();
        ebd_enabled      = new ValueCheckBox();
        lbl_return_floor = new JLabel();
        cbo_return_floor = new MyComboBox();
        lbl_door_open_led_behavior = new JLabel();
        cbo_door_open_led_behavior = new MyComboBox( Led_Behavior.values() );
        lbl_door_close_timer       = new JLabel();
        fmt_door_close_timer       = new ValueTextField();
        lbl_car_message  = new JLabel();
        cbo_car_message  = new MyComboBox( comm.constants.DeviceMessage.values() );
        lbl_hall_message = new JLabel();
        cbo_hall_message = new MyComboBox( comm.constants.DeviceMessage.values() );
        ebd_enabled_edp  = new ValueCheckBox();
        ebd_enabled_sgs  = new ValueCheckBox();
        ebd_enabled_front_buzzer = new ValueCheckBox();
        ebd_enabled_rear_buzzer = new ValueCheckBox();
        setCaptionStyle( cpt_general );
        setComboBoxLabelStyle( lbl_return_floor );
        setComboBoxValueStyle( cbo_return_floor );
        setTextLabelStyle( lbl_door_open_led_behavior );
        setTextLabelStyle( lbl_door_close_timer );
        cbo_door_open_led_behavior.setPreferredSize(new Dimension(120, 25));
        cbo_return_floor.setPreferredSize(new Dimension(120, 25));
        fmt_door_close_timer.setColumns( 10 );
        fmt_door_close_timer.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_close_timer.setScope( Long.class, 0L, null, true, false );
        fmt_door_close_timer.setEmptyValue( 1L );
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
        add( lbl_door_close_timer, "skip 2, span 1, left, top" );
        add( fmt_door_close_timer, "span 1, left, wrap, top" );
        add( ebd_enabled_edp, "skip 2, span 1, left, top" );
        add( ebd_enabled_sgs, "span 1, left, wrap, top" );
        add( ebd_enabled_front_buzzer, "skip 2, span 1, left, top" );
        add( ebd_enabled_rear_buzzer, "span 1, left, wrap, top" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, left, wrap, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, left, wrap 30, top" );
        

        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings = new JLabel();

        JLabel lbl_io_fro_switch = new JLabel();
        io_fro_switch = new IOBar( true );
        setCaptionStyle( cpt_i_o_settings );
        setTextLabelStyle( lbl_io_fro_switch );
        lbl_io_fro_switch.setText( EventID.getString( EventID.FRO.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_fro_switch, EventID.FRO.eventID );
        add( cpt_i_o_settings, "gapbottom 18-12, span, top, top" );
        add( lbl_io_fro_switch, "skip 2, span, gapright 12, top" );
        add( io_fro_switch, "skip 2, span, gapright 12, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_dim_devices                = new JLabel();
        ebd_enabled_dim_device_on_car  = new ValueCheckBox();
        ebd_enabled_dim_device_on_hall = new ValueCheckBox();
        lbl_dim_timer                  = new JLabel();
        fmt_dim_timer                  = new ValueTextField();
        setCaptionStyle( cpt_dim_devices );

        // @CompoentSetting( ebd_enabled_dim_device_on_car )

        // @CompoentSetting( ebd_enabled_dim_device_on_hall )

        // @CompoentSetting<Fmt>( lbl_dim_timer , fmt_dim_timer )
        setTextLabelStyle( lbl_dim_timer );
        fmt_dim_timer.setColumns( 10 );
        fmt_dim_timer.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_dim_timer.setScope( Long.class, 0L, null, true, false );
        fmt_dim_timer.setEmptyValue( 1L );
        add( cpt_dim_devices, "gapbottom 18-12, span, top" );
        add( ebd_enabled_dim_device_on_car, "skip 1, span, top" );
        add( ebd_enabled_dim_device_on_hall, "skip 1, span, top" );
        add( lbl_dim_timer, "skip 2, span 1, left, top" );
        add( fmt_dim_timer, "span 1, left, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_dcs_fan          = new JLabel();
        ebd_disable_dcs_fan  = new ValueCheckBox();
        lbl_activation_timer = new JLabel();
        fmt_activation_timer = new ValueTextField();
        setCaptionStyle( cpt_dcs_fan );

        // @CompoentSetting( ebd_disable_dcs_fan )

        // @CompoentSetting<Fmt>( lbl_activation_timer , fmt_activation_timer )
        setTextLabelStyle( lbl_activation_timer );
        fmt_activation_timer.setColumns( 10 );
        fmt_activation_timer.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_activation_timer.setScope( Long.class, 0L, null, true, false );
        fmt_activation_timer.setEmptyValue( 1L );
        add( cpt_dcs_fan, "gapbottom 18-12, span, top" );
        add( ebd_disable_dcs_fan, "skip 1, span, top" );
        add( lbl_activation_timer, "skip 2, span 1, left, top" );
        add( fmt_activation_timer, "span 1, left, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_dcs_light          = new JLabel();
        ebd_disable_dcs_light  = new ValueCheckBox();
        lbl_activation_timer_0 = new JLabel();
        fmt_activation_timer_0 = new ValueTextField();
        setCaptionStyle( cpt_dcs_light );

        // @CompoentSetting( ebd_disable_dcs_light )

        // @CompoentSetting<Fmt>( lbl_activation_timer_0 , fmt_activation_timer_0 )
        setTextLabelStyle( lbl_activation_timer_0 );
        fmt_activation_timer_0.setColumns( 10 );
        fmt_activation_timer_0.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_activation_timer_0.setScope( Long.class, 0L, null, true, false );
        fmt_activation_timer_0.setEmptyValue( 1L );
        add( cpt_dcs_light, "gapbottom 18-12, span, top" );
        add( ebd_disable_dcs_light, "skip 1, span, top" );
        add( lbl_activation_timer_0, "skip 2, span 1, left, top" );
        add( fmt_activation_timer_0, "span 1, left, wrap 30, top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_strategy           = new JLabel();
        ebd_enabled_stragety_a = new ValueRadioButton();
        ebd_enabled_stragety_b = new ValueRadioButton();
        setCaptionStyle( cpt_strategy );

        add( cpt_strategy, "gapbottom 18-12, span, top" );
        add( ebd_enabled_stragety_a, "skip 1, span, top" );
        add( ebd_enabled_stragety_b, "skip 1, span, wrap 30, top" );
        
        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "return_floor", lbl_return_floor, cbo_return_floor );
        bindGroup( "enabled_edp", ebd_enabled_edp);
        bindGroup( "enabled_sgs", ebd_enabled_sgs);
        bindGroup( "front_buzzer", ebd_enabled_front_buzzer);
        bindGroup( "rear_buzzer", ebd_enabled_rear_buzzer);
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        bindGroup( "enable_energy_saving_on_car", ebd_enabled_dim_device_on_car );
        bindGroup( "enable_energy_saving_on_hall", ebd_enabled_dim_device_on_hall );
        bindGroup( "enable_energy_saving_activation_time", lbl_dim_timer, fmt_dim_timer );
        bindGroup( "disable_cabin_fan", ebd_disable_dcs_fan );
        bindGroup( "disable_cabin_fan_activation_time", lbl_activation_timer, fmt_activation_timer );
        bindGroup( "disable_cabin_light", ebd_disable_dcs_light );
        bindGroup( "disable_cabin_light_activation_time", lbl_activation_timer_0, fmt_activation_timer_0 );
        bindGroup( new AbstractButton[]{ ebd_enabled }, lbl_return_floor, cbo_return_floor, lbl_door_open_led_behavior, cbo_door_open_led_behavior,
        		   lbl_car_message, cbo_car_message, lbl_hall_message, cbo_hall_message, ebd_enabled_edp, ebd_enabled_sgs, 
        		   ebd_enabled_front_buzzer, ebd_enabled_rear_buzzer, io_fro_switch, ebd_enabled_dim_device_on_car, ebd_enabled_dim_device_on_hall,
        		   ebd_disable_dcs_fan, ebd_disable_dcs_light );
        bindGroup( new AbstractButton[]{ ebd_enabled_dim_device_on_car, ebd_enabled_dim_device_on_hall }, lbl_dim_timer, fmt_dim_timer );
        bindGroup( new AbstractButton[]{ ebd_disable_dcs_fan }, lbl_activation_timer, fmt_activation_timer );
        bindGroup( new AbstractButton[]{ ebd_disable_dcs_light }, lbl_activation_timer_0, fmt_activation_timer_0 );
        
        ButtonGroup bg = new ButtonGroup();
        bg.add( ebd_enabled_stragety_a );
        bg.add( ebd_enabled_stragety_b );
        
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_return_floor.setText( TEXT.getString( "return_floor" ) );
        lbl_door_open_led_behavior.setText( TEXT.getString( "door_open_led_behavior" ) );
        lbl_door_close_timer.setText( TEXT.getString( "door_close_timer" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );
        ebd_enabled_edp.setText( TEXT.getString( "enabled_edp" ) );
        ebd_enabled_sgs.setText( TEXT.getString( "enabled_sgs" ) );
        ebd_enabled_front_buzzer.setText( TEXT.getString( "enabled_front_buzzer" ) );
        ebd_enabled_rear_buzzer.setText( TEXT.getString( "enabled_rear_buzzer" ) );
        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings.setText( TEXT.getString( "event_setting" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_dim_devices.setText( TEXT.getString( "energy_saving" ) );
        ebd_enabled_dim_device_on_car.setText( TEXT.getString( "enable_energy_saving_on_car" ) );
        ebd_enabled_dim_device_on_hall.setText( TEXT.getString( "enable_energy_saving_on_hall" ) );
        lbl_dim_timer.setText( TEXT.getString( "enable_energy_saving_activation_time" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_dcs_fan.setText( TEXT.getString( "cabin_fan" ) );
        ebd_disable_dcs_fan.setText( TEXT.getString( "disable_cabin_fan" ) );
        lbl_activation_timer.setText( TEXT.getString( "disable_cabin_fan_activation_time" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_dcs_light.setText( TEXT.getString( "cabin_light" ) );
        ebd_disable_dcs_light.setText( TEXT.getString( "disable_cabin_light" ) );
        lbl_activation_timer_0.setText( TEXT.getString( "disable_cabin_light_activation_time" ) );

        /* ---------------------------------------------------------------------------- */
        
        cpt_strategy.setText( TEXT.getString( "strategy" ) );
        ebd_enabled_stragety_a.setText( TEXT.getString( "strategy_1" ) );
        ebd_enabled_stragety_b.setText( TEXT.getString( "strategy_2" ) );
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
        bean_general.setDoorCloseTimer( (Long)fmt_door_close_timer.getValue());
        bean_general.setCarMessage( ( comm.constants.DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_general.setHallMessage( ( comm.constants.DeviceMessage )cbo_hall_message.getSelectedItem() );
        bean_general.setEdp_enable( ebd_enabled_edp.isSelected() );
        bean_general.setSgs_enable( ebd_enabled_sgs.isSelected() );
        bean_general.setFront_buzzer_enable(ebd_enabled_front_buzzer.isSelected());
        bean_general.setRear_buzzer_enable(ebd_enabled_rear_buzzer.isSelected());
        return bean_general;
    }


    public DcsFanBean getDcsFanBean () throws ConvertException {
        if ( ! fmt_activation_timer.checkValue() )
            throw new ConvertException();

        DcsFanBean bean_dcsFan = new DcsFanBean();
        bean_dcsFan.setDisableDcsFan( ebd_disable_dcs_fan.isSelected() );
        bean_dcsFan.setActivationTimer( ( Long )fmt_activation_timer.getValue() );
        return bean_dcsFan;
    }


    public DimDevicesBean getDimDevicesBean () throws ConvertException {
        if ( ! fmt_dim_timer.checkValue() )
            throw new ConvertException();

        DimDevicesBean bean_dimDevices = new DimDevicesBean();
        bean_dimDevices.setEnabledDimDeviceOnCar( ebd_enabled_dim_device_on_car.isSelected() );
        bean_dimDevices.setEnabledDimDeviceOnHall( ebd_enabled_dim_device_on_hall.isSelected() );
        bean_dimDevices.setDimTimer( ( Long )fmt_dim_timer.getValue() );
        return bean_dimDevices;
    }


    public DcsLightBean getDcsLightBean () throws ConvertException {
        if ( ! fmt_activation_timer_0.checkValue() )
            throw new ConvertException();

        DcsLightBean bean_dcsLight = new DcsLightBean();
        bean_dcsLight.setDisableDcsLight( ebd_disable_dcs_light.isSelected() );
        bean_dcsLight.setActivationTimer0( ( Long )fmt_activation_timer_0.getValue() );
        return bean_dcsLight;
    }


    public IOSettingsBean getIOSettingsBean () throws ConvertException {
        IOSettingsBean bean_iOSettings = new IOSettingsBean();
        bean_iOSettings.setFroSwitchEvent( io_fro_switch.getEvent() );
        return bean_iOSettings;
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.cbo_return_floor.setSelectedItem( bean_general.getReturnFloor() );
        this.cbo_door_open_led_behavior.setSelectedItem( bean_general.getLedBehavior());
        this.fmt_door_close_timer.setValue( bean_general.getDoorCloseTimer() );
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
        this.ebd_enabled_edp.setOriginSelected(bean_general.getEdp_enable() != null && bean_general.getEdp_enable() == true);
        this.ebd_enabled_sgs.setOriginSelected(bean_general.getSgs_enable() != null && bean_general.getSgs_enable() == true);
        this.ebd_enabled_front_buzzer.setOriginSelected(bean_general.getFront_buzzer_enable() != null && bean_general.getFront_buzzer_enable() == true);
        this.ebd_enabled_rear_buzzer.setOriginSelected(bean_general.getRear_buzzer_enable() != null && bean_general.getRear_buzzer_enable() == true);
    }


    public void setDcsFanBean ( DcsFanBean bean_dcsFan ) {
        this.ebd_disable_dcs_fan.setOriginSelected( bean_dcsFan.getDisableDcsFan() != null && bean_dcsFan.getDisableDcsFan() == true );
        this.fmt_activation_timer.setOriginValue( bean_dcsFan.getActivationTimer() );
    }


    public void setDimDevicesBean ( DimDevicesBean bean_dimDevices ) {
        this.ebd_enabled_dim_device_on_car.setOriginSelected( bean_dimDevices.getEnabledDimDeviceOnCar() != null
                                                        && bean_dimDevices.getEnabledDimDeviceOnCar() == true );
        this.ebd_enabled_dim_device_on_hall.setOriginSelected( bean_dimDevices.getEnabledDimDeviceOnHall() != null
                                                         && bean_dimDevices.getEnabledDimDeviceOnHall() == true );
        this.fmt_dim_timer.setOriginValue( bean_dimDevices.getDimTimer() );
    }


    public void setDcsLightBean ( DcsLightBean bean_dcsLight ) {
        this.ebd_disable_dcs_light.setOriginSelected( bean_dcsLight.getDisableDcsLight() != null && bean_dcsLight.getDisableDcsLight() == true );
        this.fmt_activation_timer_0.setOriginValue( bean_dcsLight.getActivationTimer0() );
    }


    public void setIOSettingsBean ( IOSettingsBean bean_iOSettings ) {
        this.io_fro_switch.setEvent( bean_iOSettings.getFroSwitchEvent() );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static FireEmergencyReturnOperation createPanel ( SettingPanel<FireEmergencyReturnOperation> panel ) {
        FireEmergencyReturnOperation gui = new FireEmergencyReturnOperation();
        gui.setSettingPanel( panel );
        return gui;
    }


    public void setFloorText ( ArrayList<FloorText> list ) {
        cbo_return_floor.removeAllItems();
        for ( FloorText text : list )
            cbo_return_floor.addItem( text );
    }
    
    public void setStrategyBean ( StrategyBean bean_strategy ) {
        this.ebd_enabled_stragety_a.setOriginSelected( bean_strategy.getEnabledStragetyA() != null && bean_strategy.getEnabledStragetyA() == true );
        this.ebd_enabled_stragety_b.setOriginSelected( bean_strategy.getEnabledStragetyB() != null && bean_strategy.getEnabledStragetyB() == true );
    }
    
    public StrategyBean getStrategyBean () throws ConvertException {
        StrategyBean bean_strategy = new StrategyBean();
        bean_strategy.setEnabledStragetyA( ebd_enabled_stragety_a.isSelected() );
        bean_strategy.setEnabledStragetyB( ebd_enabled_stragety_b.isSelected() );
        return bean_strategy;
    }

    public static class DcsFanBean {
        private Boolean disableDcsFan;
        private Long    activationTimer;




        public Boolean getDisableDcsFan () {
            return this.disableDcsFan;
        }


        public Long getActivationTimer () {
            return this.activationTimer;
        }


        public void setDisableDcsFan ( Boolean disableDcsFan ) {
            this.disableDcsFan = disableDcsFan;
        }


        public void setActivationTimer ( Long activationTimer ) {
            this.activationTimer = activationTimer;
        }
    }




    public static class DcsLightBean {
        private Boolean disableDcsLight;
        private Long    activationTimer0;




        public Boolean getDisableDcsLight () {
            return this.disableDcsLight;
        }


        public Long getActivationTimer0 () {
            return this.activationTimer0;
        }


        public void setDisableDcsLight ( Boolean disableDcsLight ) {
            this.disableDcsLight = disableDcsLight;
        }


        public void setActivationTimer0 ( Long activationTimer0 ) {
            this.activationTimer0 = activationTimer0;
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
        private Boolean                   enabled;
        private FloorText                 returnFloor;
        private Led_Behavior			  ledBehavior;
        private Long					  doorCloseTimer;  
        private comm.constants.DeviceMessage carMessage;
        private comm.constants.DeviceMessage hallMessage;
        private Boolean					  edp_enable;
        private Boolean					  sgs_enable;
        private Boolean					  front_buzzer_enable;
        private Boolean					  rear_buzzer_enable;


        public Boolean getEnabled () {
            return this.enabled;
        }


        public FloorText getReturnFloor () {
            return this.returnFloor;
        }


        public comm.constants.DeviceMessage getCarMessage () {
            return this.carMessage;
        }


        public comm.constants.DeviceMessage getHallMessage () {
            return this.hallMessage;
        }
        

        public Boolean getEdp_enable() {
			return edp_enable;
		}


		public Boolean getSgs_enable() {
			return sgs_enable;
		}
		
		public Boolean getFront_buzzer_enable() {
			return front_buzzer_enable;
		}


		public Boolean getRear_buzzer_enable() {
			return rear_buzzer_enable;
		}

		public void setEnabled ( Boolean enabled ) {
            this.enabled = enabled;
        }


        public void setReturnFloor ( FloorText returnFloor ) {
            this.returnFloor = returnFloor;
        }


        public void setCarMessage ( comm.constants.DeviceMessage carMessage ) {
            this.carMessage = carMessage;
        }


        public void setHallMessage ( comm.constants.DeviceMessage hallMessage ) {
            this.hallMessage = hallMessage;
        }


		public void setEdp_enable(Boolean edp_enable) {
			this.edp_enable = edp_enable;
		}


		public void setSgs_enable(Boolean sgs_enable) {
			this.sgs_enable = sgs_enable;
		}
		
		public void setFront_buzzer_enable(Boolean front_buzzer_enable) {
			this.front_buzzer_enable = front_buzzer_enable;
		}


		public void setRear_buzzer_enable(Boolean rear_buzzer_enable) {
			this.rear_buzzer_enable = rear_buzzer_enable;
		}

		public Led_Behavior getLedBehavior() {
			return ledBehavior;
		}

		public void setLedBehavior(Led_Behavior led_Behavior) {
			this.ledBehavior = led_Behavior;
		}

		public Long getDoorCloseTimer() {
			return doorCloseTimer;
		}

		public void setDoorCloseTimer(Long doorCloseTimer) {
			this.doorCloseTimer = doorCloseTimer;
		}
    }




    public static class IOSettingsBean {
        private Event froSwitchEvent;




        public final Event getFroSwitchEvent () {
            return froSwitchEvent;
        }


        public final void setFroSwitchEvent ( Event froSwitchEvent ) {
            this.froSwitchEvent = froSwitchEvent;
        }
    }
    
    public static class StrategyBean {
        private Boolean enabledStragetyA;
        private Boolean enabledStragetyB;

        public Boolean getEnabledStragetyA () {
            return this.enabledStragetyA;
        }

        public Boolean getEnabledStragetyB () {
            return this.enabledStragetyB;
        }

		public void setEnabledStragetyA ( Boolean enabledStragetyA ) {
            this.enabledStragetyA = enabledStragetyA;
        }

        public void setEnabledStragetyB ( Boolean enabledStragetyB ) {
            this.enabledStragetyB = enabledStragetyB;
        }
        
    }
}
