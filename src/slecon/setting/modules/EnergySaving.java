package slecon.setting.modules;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueTextField;
import slecon.interfaces.ConvertException;
import slecon.setting.modules.LedBehavior.Led_Behavior;
import base.cfg.FontFactory;




/**
 * Setup -> Module -> Energy Saving.
 */
public class EnergySaving extends JPanel {
    private static final long serialVersionUID = -4702325303305313980L;
    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT    = ToolBox.getResourceBundle( "setting.module.EnergySaving" );
    private boolean                    started = false;
    private SettingPanel<EnergySaving>   settingPanel;
    /* ---------------------------------------------------------------------------- */
    private JTabbedPane    tabPane;
    
    /* ---------------------------------------------------------------------------- */
    private ValueCheckBox  		ebd_enabled_dim_device_arrival;
    private ValueCheckBox  		ebd_enabled_dim_device_on_car;
    private ValueCheckBox  		ebd_enabled_dim_device_on_hall;
    private JLabel         		lbl_dim_timer;
    private ValueTextField 		fmt_dim_timer;
    private ValueCheckBox  		ebd_enabled_open_door_button_led;
    private JLabel         		lbl_activation_timer_0;
    private ValueTextField 		fmt_activation_timer_0;
    private JLabel              lbl_door_open_led_behavior;
    private MyComboBox			cbo_door_open_led_behavior;
    private ValueCheckBox  		ebd_disable_dcs_fan;
    private JLabel         		lbl_activation_timer_1;
    private ValueTextField 		fmt_activation_timer_1;
    private ValueCheckBox       ebd_disable_dcs_light;
    private JLabel              lbl_activation_timer_2;
    private ValueTextField      fmt_activation_timer_2;
    
    public EnergySaving () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<EnergySaving> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 10 25 25 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );        
        
        tabPane = new JTabbedPane();
        
        /* ---------------------------------------------------------------------------- */
        ebd_enabled_dim_device_arrival = new ValueCheckBox();
        ebd_enabled_dim_device_on_car  = new ValueCheckBox();
        ebd_enabled_dim_device_on_hall = new ValueCheckBox();
        lbl_dim_timer                  = new JLabel();
        fmt_dim_timer                  = new ValueTextField();

        // @CompoentSetting( ebd_enabled_dim_device_on_car )

        // @CompoentSetting( ebd_enabled_dim_device_on_hall )

        // @CompoentSetting<Fmt>( lbl_dim_timer , fmt_dim_timer )
        setTextLabelStyle( lbl_dim_timer );
        fmt_dim_timer.setColumns( 10 );
        fmt_dim_timer.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_dim_timer.setScope( Long.class, 0L, null, false, false );
        fmt_dim_timer.setEmptyValue( 1L );
        add( ebd_enabled_dim_device_arrival, "skip 1, span, wrap 10, top" );
        add( ebd_enabled_dim_device_on_car, "skip 1, span, wrap 10, top" );
        add( ebd_enabled_dim_device_on_hall, "skip 1, span, wrap 10, top" );
        add( lbl_dim_timer, "skip 2, span 1, left, top" );
        add( fmt_dim_timer, "span 1, wrap 10, left, top" );

        /* ---------------------------------------------------------------------------- */
        ebd_enabled_open_door_button_led = new ValueCheckBox();
        lbl_activation_timer_0           = new JLabel();
        fmt_activation_timer_0           = new ValueTextField();
        lbl_door_open_led_behavior		 = new JLabel();
        cbo_door_open_led_behavior 		 = new MyComboBox( Led_Behavior.values() ); 
        cbo_door_open_led_behavior.setPreferredSize(new Dimension(120, 25));

        setTextLabelStyle( lbl_activation_timer_0 );
        setTextLabelStyle( lbl_door_open_led_behavior );
        fmt_activation_timer_0.setColumns( 10 );
        fmt_activation_timer_0.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_activation_timer_0.setScope( Long.class, 0L, null, false, false );
        fmt_activation_timer_0.setEmptyValue( 1L );
        add( ebd_enabled_open_door_button_led, "skip 1, span, top" );
        add( lbl_activation_timer_0, "skip 2, span 1, left, top" );
        add( fmt_activation_timer_0, "span 1, left, wrap, top" );
        add( lbl_door_open_led_behavior, "skip 2, span 1, left, top" );
        add( cbo_door_open_led_behavior, "span 1, left, wrap 10, top" );

        /* ---------------------------------------------------------------------------- */
        ebd_disable_dcs_fan    = new ValueCheckBox();
        lbl_activation_timer_1 = new JLabel();
        fmt_activation_timer_1 = new ValueTextField();

        // @CompoentSetting( ebd_disable_dcs_fan )

        // @CompoentSetting<Fmt>( lbl_activation_timer_1 , fmt_activation_timer_1 )
        setTextLabelStyle( lbl_activation_timer_1 );
        fmt_activation_timer_1.setColumns( 10 );
        fmt_activation_timer_1.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_activation_timer_1.setScope( Long.class, 0L, null, false, false );
        fmt_activation_timer_1.setEmptyValue( 1L );
        add( ebd_disable_dcs_fan, "skip 1, span, top" );
        add( lbl_activation_timer_1, "skip 2, span 1, left, top" );
        add( fmt_activation_timer_1, "span 1, left, wrap 10, top" );

        /* ---------------------------------------------------------------------------- */
        ebd_disable_dcs_light  = new ValueCheckBox();
        lbl_activation_timer_2 = new JLabel();
        fmt_activation_timer_2 = new ValueTextField();

        // @CompoentSetting( ebd_disable_dcs_light )

        // @CompoentSetting<Fmt>( lbl_activation_timer_2 , fmt_activation_timer_2 )
        setTextLabelStyle( lbl_activation_timer_2 );
        fmt_activation_timer_2.setColumns( 10 );
        fmt_activation_timer_2.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_activation_timer_2.setScope( Long.class, 0L, null, false, false );
        fmt_activation_timer_2.setEmptyValue( 1L );
        add( ebd_disable_dcs_light, "skip 1, span, top" );
        add( lbl_activation_timer_2, "skip 2, span 1, left, top" );
        add( fmt_activation_timer_2, "span 1, left, wrap 10, top" );
        
        /* ---------------------------------------------------------------------------- */
        
        bindGroup( "enabled_dim_device_arrival", ebd_enabled_dim_device_arrival );
        bindGroup( "enable_energy_saving_on_car", ebd_enabled_dim_device_on_car );
        bindGroup( "enable_energy_saving_on_hall", ebd_enabled_dim_device_on_hall );
        bindGroup( "enable_energy_saving_activation_time", lbl_dim_timer, fmt_dim_timer );
        bindGroup( "enabled_open_door_button_led", ebd_enabled_open_door_button_led );
        bindGroup( "open_door_button_led_activation_time", lbl_activation_timer_0, fmt_activation_timer_0 );
        bindGroup( "disable_cabin_fan", ebd_disable_dcs_fan );
        bindGroup( "disable_cabin_fan_activation_time", lbl_activation_timer_1, fmt_activation_timer_1 );
        bindGroup( "disable_cabin_light", ebd_disable_dcs_light );
        bindGroup( "disable_cabin_light_activation_time", lbl_activation_timer_2, fmt_activation_timer_2 );
        bindGroup( new AbstractButton[]{ ebd_enabled_dim_device_on_car, ebd_enabled_dim_device_on_hall }, lbl_dim_timer, fmt_dim_timer );
        
        bindGroup( new AbstractButton[]{ ebd_enabled_open_door_button_led }, lbl_activation_timer_0, fmt_activation_timer_0,
        								 lbl_door_open_led_behavior, cbo_door_open_led_behavior);
        bindGroup( new AbstractButton[]{ ebd_disable_dcs_fan }, lbl_activation_timer_1, fmt_activation_timer_1 );
        bindGroup( new AbstractButton[]{ ebd_disable_dcs_light }, lbl_activation_timer_2, fmt_activation_timer_2 );
        loadI18N();
        revalidate();
    }

    private void loadI18N () {
        /* ---------------------------------------------------------------------------- */
    	ebd_enabled_dim_device_arrival.setText(TEXT.getString( "enabled_dim_device_arrival" ));
        ebd_enabled_dim_device_on_car.setText( TEXT.getString( "enable_energy_saving_on_car" ) );
        ebd_enabled_dim_device_on_hall.setText( TEXT.getString( "enable_energy_saving_on_hall" ) );
        lbl_dim_timer.setText( TEXT.getString( "enable_energy_saving_activation_time" ) );

        /* ---------------------------------------------------------------------------- */
        ebd_enabled_open_door_button_led.setText( TEXT.getString( "enabled_open_door_button_led" ) );
        lbl_activation_timer_0.setText( TEXT.getString( "open_door_button_led_activation_time" ) );
        lbl_door_open_led_behavior.setText( TEXT.getString("door_open_led_behavior") );

        /* ---------------------------------------------------------------------------- */
        ebd_disable_dcs_fan.setText( TEXT.getString( "disable_cabin_fan" ) );
        lbl_activation_timer_1.setText( TEXT.getString( "disable_cabin_fan_activation_time" ) );

        /* ---------------------------------------------------------------------------- */
        ebd_disable_dcs_light.setText( TEXT.getString( "disable_cabin_light" ) );
        lbl_activation_timer_2.setText( TEXT.getString( "disable_cabin_light_activation_time" ) );

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

    public DcsFanBean getDcsFanBean () throws ConvertException {
        if ( ! fmt_activation_timer_1.checkValue() )
            throw new ConvertException();

        DcsFanBean bean_dcsFan = new DcsFanBean();
        bean_dcsFan.setDisableDcsFan( ebd_disable_dcs_fan.isSelected() );
        bean_dcsFan.setActivationTimer1( ( Long )fmt_activation_timer_1.getValue() );
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


    public OpenDoorButtonBean getOpenDoorButtonBean () throws ConvertException {
        if ( ! fmt_activation_timer_0.checkValue() )
            throw new ConvertException();

        OpenDoorButtonBean bean_openDoorButton = new OpenDoorButtonBean();
        bean_openDoorButton.setEnabledOpenDoorButtonLed( ebd_enabled_open_door_button_led.isSelected() );
        bean_openDoorButton.setActivationTimer0( ( Long )fmt_activation_timer_0.getValue() );
        bean_openDoorButton.setLed_behavoir( (Led_Behavior)cbo_door_open_led_behavior.getSelectedItem() );
        return bean_openDoorButton;
    }


    public DcsLightBean getDcsLightBean () throws ConvertException {
        if ( ! fmt_activation_timer_2.checkValue() )
            throw new ConvertException();

        DcsLightBean bean_dcsLight = new DcsLightBean();
        bean_dcsLight.setDisableDcsLight( ebd_disable_dcs_light.isSelected() );
        bean_dcsLight.setActivationTimer2( ( Long )fmt_activation_timer_2.getValue() );
        return bean_dcsLight;
    }

    public void setDcsFanBean ( DcsFanBean bean_dcsFan ) {
        this.ebd_disable_dcs_fan.setOriginSelected( bean_dcsFan.getDisableDcsFan() != null && bean_dcsFan.getDisableDcsFan() == true );
        this.fmt_activation_timer_1.setOriginValue( bean_dcsFan.getActivationTimer1() );
    }


    public void setDimDevicesBean ( DimDevicesBean bean_dimDevices ) {
        this.ebd_enabled_dim_device_on_car.setOriginSelected( bean_dimDevices.getEnabledDimDeviceOnCar() != null
                                                        && bean_dimDevices.getEnabledDimDeviceOnCar() == true );
        this.ebd_enabled_dim_device_on_hall.setOriginSelected( bean_dimDevices.getEnabledDimDeviceOnHall() != null
                                                         && bean_dimDevices.getEnabledDimDeviceOnHall() == true );
        this.fmt_dim_timer.setOriginValue( bean_dimDevices.getDimTimer() );
    }


    public void setOpenDoorButtonBean ( OpenDoorButtonBean bean_openDoorButton ) {
        this.ebd_enabled_open_door_button_led.setOriginSelected( bean_openDoorButton.getEnabledOpenDoorButtonLed() != null
                                                           && bean_openDoorButton.getEnabledOpenDoorButtonLed() == true );
        this.fmt_activation_timer_0.setOriginValue( bean_openDoorButton.getActivationTimer0() );
        this.cbo_door_open_led_behavior.setSelectedItem(bean_openDoorButton.getLed_behavoir());
    }


    public void setDcsLightBean ( DcsLightBean bean_dcsLight ) {
        this.ebd_disable_dcs_light.setOriginSelected( bean_dcsLight.getDisableDcsLight() != null && bean_dcsLight.getDisableDcsLight() == true );
        this.fmt_activation_timer_2.setOriginValue( bean_dcsLight.getActivationTimer2() );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static EnergySaving createPanel ( SettingPanel<EnergySaving> panel ) {
        EnergySaving gui = new EnergySaving();
        gui.setSettingPanel( panel );
        return gui;
    }

    public static class DcsFanBean {
        private Boolean disableDcsFan;
        private Long    activationTimer1;




        public Boolean getDisableDcsFan () {
            return this.disableDcsFan;
        }


        public Long getActivationTimer1 () {
            return this.activationTimer1;
        }


        public void setDisableDcsFan ( Boolean disableDcsFan ) {
            this.disableDcsFan = disableDcsFan;
        }


        public void setActivationTimer1 ( Long activationTimer1 ) {
            this.activationTimer1 = activationTimer1;
        }
    }




    public static class DcsLightBean {
        private Boolean disableDcsLight;
        private Long    activationTimer2;




        public Boolean getDisableDcsLight () {
            return this.disableDcsLight;
        }


        public Long getActivationTimer2 () {
            return this.activationTimer2;
        }


        public void setDisableDcsLight ( Boolean disableDcsLight ) {
            this.disableDcsLight = disableDcsLight;
        }


        public void setActivationTimer2 ( Long activationTimer2 ) {
            this.activationTimer2 = activationTimer2;
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


    public static class OpenDoorButtonBean {
        private Boolean enabledOpenDoorButtonLed;
        private Long    activationTimer0;
        private Led_Behavior	led_behavoir;

        public Boolean getEnabledOpenDoorButtonLed () {
            return this.enabledOpenDoorButtonLed;
        }


        public Long getActivationTimer0 () {
            return this.activationTimer0;
        }


        public void setEnabledOpenDoorButtonLed ( Boolean enabledOpenDoorButtonLed ) {
            this.enabledOpenDoorButtonLed = enabledOpenDoorButtonLed;
        }


        public void setActivationTimer0 ( Long activationTimer0 ) {
            this.activationTimer0 = activationTimer0;
        }


		public Led_Behavior getLed_behavoir() {
			return led_behavoir;
		}


		public void setLed_behavoir(Led_Behavior led_Behavior) {
			this.led_behavoir = led_Behavior;
		}
        
    }
    
    public ArrivalEnergySaveBean getArrivalEnergySaveBean() throws ConvertException {
    	ArrivalEnergySaveBean bean_energy_save = new ArrivalEnergySaveBean();
    	bean_energy_save.setEnable(ebd_enabled_dim_device_arrival.isSelected());
    	return bean_energy_save;
    }
    
    public void setArrivalEnergySaveBean ( ArrivalEnergySaveBean bean_energy_save ) {
    	this.ebd_enabled_dim_device_arrival.setOriginSelected(bean_energy_save.getEnable() != null && bean_energy_save.getEnable() == true );
    }
    
    public static class ArrivalEnergySaveBean{
    	private Boolean enable;

		public Boolean getEnable() {
			return enable;
		}

		public void setEnable(Boolean enable) {
			this.enable = enable;
		}
    	
    }
}
