package slecon.setting.modules;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.EventID;
import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.iobar.IOBar;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;
import comm.constants.DeviceMessage;




/**
 * Setup -> Module -> Attendant Service Operation.
 */
public class AttendantServiceOperation extends JPanel {
    private static final long serialVersionUID = 8692007247520068954L;
    /**
     * Text resource.
     */
    public static final ResourceBundle              TEXT    = ToolBox.getResourceBundle( "setting.module.AttendantServiceOperation" );
    private boolean                                 started = false;
    private SettingPanel<AttendantServiceOperation> settingPanel;
    private JLabel                                  cpt_general;
    private ValueCheckBox                           ebd_enabled;
    private JLabel                                  lbl_car_led_behavior;
    private MyComboBox				                cbo_car_led_behavior;
    private ValueCheckBox                           ebd_enabled_front_buzzer_on_hall_call;
    private ValueCheckBox                           ebd_enabled_rear_buzzer_on_hall_call;
    private JLabel                     				lbl_car_message;
    private MyComboBox   			   				cbo_car_message;
    private JLabel                     				lbl_hall_message;
    private MyComboBox				   				cbo_hall_message;

    /* ---------------------------------------------------------------------------- */
    private JLabel cpt_i_o_settings;
    private IOBar  io_att_switch;
    private IOBar  io_attup_switch;
    private IOBar  io_attdown_switch;
    private IOBar  io_attns_switch;

    public AttendantServiceOperation () {
        initGUI();
    }


    public static enum LEDBehavior {
        FLASH( ( byte )0 ), FAST_FLASH( ( byte )1 ), BLINK( ( byte )2 ), FAST_BLINK( ( byte )3 );

        private final static Map<Byte, LEDBehavior> ledBehaviors = new HashMap<Byte, LEDBehavior>();




        static {
            for ( LEDBehavior lb : LEDBehavior.values() )
                ledBehaviors.put( lb.id, lb );
        }


        public final byte id;




        LEDBehavior ( byte id ) {
            this.id = id;
        }


        @Override
        public String toString () {
            String bundleString = null;
            try {
                bundleString = TEXT.getString( name() );
            } catch ( Exception e ) {
            }
            return bundleString == null
                   ? name()
                   : bundleString;
        }


        /**
         * Get an instance of enumeration by the constant value.
         * @param code  It specifies the constant value of enumeration.
         * @return Returns an instance of enumeration on success; otherwise, returns {@code null}.
         */
        public static LEDBehavior get ( byte code ) {
            return ledBehaviors.get( code );
        }
    }




    public void setSettingPanel ( SettingPanel<AttendantServiceOperation> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[40::40][20::20][150::150][]" ) );
        cpt_general          = new JLabel();
        ebd_enabled          = new ValueCheckBox();
        lbl_car_led_behavior = new JLabel();
        cbo_car_led_behavior = new MyComboBox();
        cbo_car_led_behavior.setModel( new DefaultComboBoxModel<>( LEDBehavior.values() ) );
        ebd_enabled_front_buzzer_on_hall_call = new ValueCheckBox();
        ebd_enabled_rear_buzzer_on_hall_call  = new ValueCheckBox();
        lbl_car_message         = new JLabel();
        cbo_car_message         = new MyComboBox( DeviceMessage.values() );
        lbl_hall_message        = new JLabel();
        cbo_hall_message        = new MyComboBox( DeviceMessage.values() );
        
        setCaptionStyle( cpt_general );
        setComboBoxLabelStyle( lbl_car_led_behavior );
        setComboBoxValueStyle( cbo_car_led_behavior );
        setTextLabelStyle( lbl_car_message );
        setTextLabelStyle( lbl_hall_message );
        add( cpt_general, "gapbottom 18-12, span, aligny center" );
        add( ebd_enabled, "skip 1, span" );
        add( lbl_car_led_behavior, "skip 2, span, split, gapright 12" );
        add( cbo_car_led_behavior, "wrap" );
        add( ebd_enabled_front_buzzer_on_hall_call, "skip 1, span" );
        add( ebd_enabled_rear_buzzer_on_hall_call, "skip 1, span" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, wrap, left, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, wrap 30, left, top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings = new JLabel();

        JLabel lbl_io_att_switch   = new JLabel();
        JLabel lbl_io_attup_switch = new JLabel();
        JLabel lbl_io_attdn_switch = new JLabel();
        JLabel lbl_io_attns_switch = new JLabel();
        io_att_switch     = new IOBar( true );
        io_attup_switch   = new IOBar( true );
        io_attdown_switch = new IOBar( true );
        io_attns_switch   = new IOBar( true );
        setTextLabelStyle( lbl_io_att_switch );
        setTextLabelStyle( lbl_io_attup_switch );
        setTextLabelStyle( lbl_io_attdn_switch );
        setTextLabelStyle( lbl_io_attns_switch );
        lbl_io_att_switch.setText( EventID.getString( EventID.ATT_FRONT.eventID, null ) );
        lbl_io_attup_switch.setText( EventID.getString( EventID.ATT_UP_FRONT.eventID, null ) );
        lbl_io_attdn_switch.setText( EventID.getString( EventID.ATT_DOWN_FRONT.eventID, null ) );
        lbl_io_attns_switch.setText( EventID.getString( EventID.ATT_NONSTOP_FRONT.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_att_switch, EventID.ATT_FRONT.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_attup_switch, EventID.ATT_UP_FRONT.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_attdown_switch, EventID.ATT_DOWN_FRONT.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_attns_switch, EventID.ATT_NONSTOP_FRONT.eventID );
        setCaptionStyle( cpt_i_o_settings );
        add( cpt_i_o_settings, "gapbottom 18-12, span, aligny center" );
        add( lbl_io_att_switch, "skip 2, span, gapright 12" );
        add( io_att_switch, "skip 2, span, wrap 12" );
        add( lbl_io_attup_switch, "skip 2, span, gapright 12" );
        add( io_attup_switch, "skip 2, span, wrap 12" );
        add( lbl_io_attdn_switch, "skip 2, span, gapright 12" );
        add( io_attdown_switch, "skip 2, span, wrap 12" );
        add( lbl_io_attns_switch, "skip 2, span, gapright 12" );
        add( io_attns_switch, "skip 2, span, wrap 12" );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "car_led_behavior", lbl_car_led_behavior, cbo_car_led_behavior );
        bindGroup( "enabled_front_buzzer_on_hall_call", ebd_enabled_front_buzzer_on_hall_call );
        bindGroup( "enabled_rear_buzzer_on_hall_call", ebd_enabled_rear_buzzer_on_hall_call );
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        bindGroup( new AbstractButton[]{ ebd_enabled }, lbl_car_led_behavior, cbo_car_led_behavior, ebd_enabled_front_buzzer_on_hall_call,
                   ebd_enabled_rear_buzzer_on_hall_call, lbl_car_message, cbo_car_message, lbl_hall_message, cbo_hall_message,
                   lbl_io_att_switch, io_att_switch, lbl_io_attup_switch, io_attup_switch,
                   lbl_io_attdn_switch, io_attdown_switch, lbl_io_attns_switch, io_attns_switch );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_car_led_behavior.setText( TEXT.getString( "car_led_behavior" ) );
        ebd_enabled_front_buzzer_on_hall_call.setText( TEXT.getString( "enabled_front_buzzer_on_hall_call" ) );
        ebd_enabled_rear_buzzer_on_hall_call.setText( TEXT.getString( "enabled_rear_buzzer_on_hall_call" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );
        
        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings.setText( TEXT.getString( "event_setting" ) );
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
        bean_general.setCarLedBehavior( ( LEDBehavior )cbo_car_led_behavior.getSelectedItem() );
        bean_general.setEnabledFrontBuzzerOnHallCall( ebd_enabled_front_buzzer_on_hall_call.isSelected() );
        bean_general.setEnabledRearBuzzerOnHallCall( ebd_enabled_rear_buzzer_on_hall_call.isSelected() );
        bean_general.setCarMessage( (DeviceMessage)cbo_car_message.getSelectedItem());
        bean_general.setHallMessage( (DeviceMessage)cbo_hall_message.getSelectedItem());
        return bean_general;
    }


    public IOSettingsBean getIOSettingsBean () throws ConvertException {
        IOSettingsBean bean_iOSettings = new IOSettingsBean();
        bean_iOSettings.setAttSwitch( io_att_switch.getEvent() );
        bean_iOSettings.setUpSwitch( io_attup_switch.getEvent() );
        bean_iOSettings.setDownSwitch( io_attdown_switch.getEvent() );
        bean_iOSettings.setNonStopSwitch( io_attns_switch.getEvent() );
        return bean_iOSettings;
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.cbo_car_led_behavior.setSelectedItem( bean_general.getCarLedBehavior() );
        this.ebd_enabled_front_buzzer_on_hall_call.setOriginSelected( bean_general.getEnabledFrontBuzzerOnHallCall() != null
                                                                && bean_general.getEnabledFrontBuzzerOnHallCall() == true );
        this.ebd_enabled_rear_buzzer_on_hall_call.setOriginSelected( bean_general.getEnabledRearBuzzerOnHallCall() != null
                                                               && bean_general.getEnabledRearBuzzerOnHallCall() == true );
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
    }


    public void setIOSettingsBean ( IOSettingsBean bean_iOSettings ) {
        io_att_switch.setEvent( bean_iOSettings.getAttSwitch() );
        io_attup_switch.setEvent( bean_iOSettings.getUpSwitch() );
        io_attdown_switch.setEvent( bean_iOSettings.getDownSwitch() );
        io_attns_switch.setEvent( bean_iOSettings.getNonStopSwitch() );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static AttendantServiceOperation createPanel ( SettingPanel<AttendantServiceOperation> panel ) {
        AttendantServiceOperation gui = new AttendantServiceOperation();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class GeneralBean {
        private Boolean     enabled;
        private LEDBehavior carLedBehavior;
        private Boolean     enabledFrontBuzzerOnHallCall;
        private Boolean     enabledRearBuzzerOnHallCall;
        private DeviceMessage carMessage;
        private DeviceMessage hallMessage;



        public Boolean getEnabled () {
            return this.enabled;
        }


        public LEDBehavior getCarLedBehavior () {
            return this.carLedBehavior;
        }


        public Boolean getEnabledFrontBuzzerOnHallCall () {
            return this.enabledFrontBuzzerOnHallCall;
        }


        public Boolean getEnabledRearBuzzerOnHallCall () {
            return this.enabledRearBuzzerOnHallCall;
        }


        public void setEnabled ( Boolean enabled ) {
            this.enabled = enabled;
        }


        public void setCarLedBehavior ( LEDBehavior carLedBehavior ) {
            this.carLedBehavior = carLedBehavior;
        }


        public void setEnabledFrontBuzzerOnHallCall ( Boolean enabledFrontBuzzerOnHallCall ) {
            this.enabledFrontBuzzerOnHallCall = enabledFrontBuzzerOnHallCall;
        }


        public void setEnabledRearBuzzerOnHallCall ( Boolean enabledRearBuzzerOnHallCall ) {
            this.enabledRearBuzzerOnHallCall = enabledRearBuzzerOnHallCall;
        }


		public DeviceMessage getCarMessage() {
			return carMessage;
		}


		public void setCarMessage(DeviceMessage carMessage) {
			this.carMessage = carMessage;
		}


		public DeviceMessage getHallMessage() {
			return hallMessage;
		}


		public void setHallMessage(DeviceMessage hallMessage) {
			this.hallMessage = hallMessage;
		}
        
        
    }




    public static class IOSettingsBean {
        private Event attSwitch;
        private Event upSwitch;
        private Event downSwitch;
        private Event nonStopSwitch;




        public final Event getAttSwitch () {
            return attSwitch;
        }


        public final Event getUpSwitch () {
            return upSwitch;
        }


        public final Event getDownSwitch () {
            return downSwitch;
        }


        public final Event getNonStopSwitch () {
            return nonStopSwitch;
        }


        public final void setAttSwitch ( Event attSwitch ) {
            this.attSwitch = attSwitch;
        }


        public final void setUpSwitch ( Event upSwitch ) {
            this.upSwitch = upSwitch;
        }


        public final void setDownSwitch ( Event downSwitch ) {
            this.downSwitch = downSwitch;
        }


        public final void setNonStopSwitch ( Event nonStopSwitch ) {
            this.nonStopSwitch = nonStopSwitch;
        }
    }
}
