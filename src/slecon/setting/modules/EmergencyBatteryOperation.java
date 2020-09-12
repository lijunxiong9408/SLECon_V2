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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

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
import slecon.setting.modules.AccessControl.IOSettingsBean;
import slecon.setting.modules.LedBehavior.Led_Behavior;
import logic.EventID;
import net.miginfocom.swing.MigLayout;
import base.cfg.FontFactory;
import comm.constants.DeviceMessage;
import comm.constants.Message;




/**
 * Setup -> Module -> Emergency Battery Operation.
 */
public class EmergencyBatteryOperation extends JPanel {
    private static final long serialVersionUID = -9196811984218766150L;
    /**
     * Text resource.
     */
    public static final ResourceBundle            TEXT    = ToolBox.getResourceBundle( "setting.module.EmergergyBatteryOperation" );
    private boolean                               started = false;
    private SettingPanel<EmergencyBatteryOperation> settingPanel;
    private JLabel                                cpt_general;
    private ValueCheckBox                         ebd_enabled;
    private JLabel                                lbl_door_close_time;
    private ValueTextField                        fmt_door_close_time;
    private ValueCheckBox  						  ebd_enabled_front_buzzer;
    private ValueCheckBox  						  ebd_enabled_rear_buzzer;
    private JLabel                                lbl_door_open_led_behavior;
    private MyComboBox				              cbo_door_open_led_behavior;
    private JLabel                                lbl_car_message;
    private MyComboBox              			  cbo_car_message;
    private JLabel                                lbl_hall_message;
    private MyComboBox              			  cbo_hall_message;
    

    public EmergencyBatteryOperation () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<EmergencyBatteryOperation> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_general               = new JLabel();
        ebd_enabled               = new ValueCheckBox();
        lbl_door_close_time 	  = new JLabel();
        fmt_door_close_time 	  = new ValueTextField();
        ebd_enabled_front_buzzer  = new ValueCheckBox();
        ebd_enabled_rear_buzzer   = new ValueCheckBox();
        lbl_door_open_led_behavior = new JLabel();
        cbo_door_open_led_behavior = new MyComboBox( Led_Behavior.values() );
        lbl_car_message           = new JLabel();
        cbo_car_message           = new MyComboBox( DeviceMessage.values() );
        lbl_hall_message          = new JLabel();
        cbo_hall_message          = new MyComboBox( DeviceMessage.values() );
        setCaptionStyle( cpt_general );
        setTextLabelStyle( lbl_door_close_time );
        fmt_door_close_time.setColumns( 10 );
        fmt_door_close_time.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_close_time.setScope( Long.class, 0L, null, true, false );
        fmt_door_close_time.setEmptyValue( 1L );

        setTextLabelStyle( lbl_door_open_led_behavior );
        cbo_door_open_led_behavior.setPreferredSize(new Dimension(120, 25));
        setComboBoxLabelStyle( lbl_car_message );
        setComboBoxValueStyle( cbo_car_message );
        setComboBoxLabelStyle( lbl_hall_message );
        setComboBoxValueStyle( cbo_hall_message );
        add( cpt_general, "gapbottom 18-12, span, aligny center, top" );
        add( ebd_enabled, "skip 1, span, top" );
        add( lbl_door_close_time, "skip 2, span 1, left, top" );
        add( fmt_door_close_time, "span 1, left, wrap, top" );
        add( ebd_enabled_front_buzzer, "skip 2, span 1, left, top" );
        add( ebd_enabled_rear_buzzer, "span 1, left, wrap, top" );
        add( lbl_door_open_led_behavior, "skip 2, span 1, left, top" );
        add( cbo_door_open_led_behavior, "span 1, left, wrap, top" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, left, wrap, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, left, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "door_close_time", lbl_door_close_time, fmt_door_close_time );
        bindGroup( "front_buzzer", ebd_enabled_front_buzzer);
        bindGroup( "rear_buzzer", ebd_enabled_rear_buzzer);
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        bindGroup( new JToggleButton[]{ ebd_enabled }, lbl_door_close_time, fmt_door_close_time, lbl_door_open_led_behavior, cbo_door_open_led_behavior,
        		lbl_car_message, cbo_car_message, lbl_hall_message, cbo_hall_message );
        
        loadI18N();
        revalidate();
    }

    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_door_open_led_behavior.setText( TEXT.getString( "door_open_led_behavior" ) );
        lbl_door_close_time.setText( TEXT.getString( "door_close_time" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );
        ebd_enabled_front_buzzer.setText( TEXT.getString( "enabled_front_buzzer" ) );
        ebd_enabled_rear_buzzer.setText( TEXT.getString( "enabled_rear_buzzer" ) );
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
        if ( ! fmt_door_close_time.checkValue() )
            throw new ConvertException();

        GeneralBean bean_general = new GeneralBean();
        bean_general.setEnabled( ebd_enabled.isSelected() );
        bean_general.setBehavior( (Led_Behavior)cbo_door_open_led_behavior.getSelectedItem() );
        bean_general.setDoor_close_time( ( Long )fmt_door_close_time.getValue() );
        bean_general.setCarMessage( ( DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_general.setHallMessage( ( DeviceMessage )cbo_hall_message.getSelectedItem() );
        bean_general.setFront_buzzer_enable(this.ebd_enabled_front_buzzer.isSelected());
        bean_general.setRear_buzzer_enable(this.ebd_enabled_rear_buzzer.isSelected());
        return bean_general;
    }
    
    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.cbo_door_open_led_behavior.setSelectedItem( bean_general.getBehavior() );
        this.fmt_door_close_time.setOriginValue( bean_general.getDoor_close_time() );
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
        this.ebd_enabled_front_buzzer.setOriginSelected(bean_general.getFront_buzzer_enable() != null && bean_general.getFront_buzzer_enable() == true);
        this.ebd_enabled_rear_buzzer.setOriginSelected(bean_general.getRear_buzzer_enable() != null && bean_general.getRear_buzzer_enable() == true);
    }
    
    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static EmergencyBatteryOperation createPanel ( SettingPanel<EmergencyBatteryOperation> panel ) {
        EmergencyBatteryOperation gui = new EmergencyBatteryOperation();
        gui.setSettingPanel( panel );
        return gui;
    }

    public static class GeneralBean {
        private Boolean       enabled;
        private Led_Behavior  behavior;
        private Long          door_close_time;
        private DeviceMessage carMessage;
        private DeviceMessage hallMessage;
        private Boolean		  front_buzzer_enable;
        private Boolean		  rear_buzzer_enable;

        public Boolean getEnabled () {
            return this.enabled;
        }
        
        
        public Led_Behavior getBehavior() {
			return behavior;
		}


		public Long getDoor_close_time() {
			return door_close_time;
		}


		public DeviceMessage getCarMessage () {
            return this.carMessage;
        }


        public DeviceMessage getHallMessage () {
            return this.hallMessage;
        }


        public void setEnabled ( Boolean enabled ) {
            this.enabled = enabled;
        }
        
        
        public void setBehavior(Led_Behavior behavior) {
			this.behavior = behavior;
		}


		public void setDoor_close_time(Long door_close_time) {
			this.door_close_time = door_close_time;
		}


		public void setCarMessage ( DeviceMessage carMessage ) {
            this.carMessage = carMessage;
        }


        public void setHallMessage ( DeviceMessage hallMessage ) {
            this.hallMessage = hallMessage;
        }


		public Boolean getFront_buzzer_enable() {
			return front_buzzer_enable;
		}


		public void setFront_buzzer_enable(Boolean front_buzzer_enable) {
			this.front_buzzer_enable = front_buzzer_enable;
		}


		public Boolean getRear_buzzer_enable() {
			return rear_buzzer_enable;
		}


		public void setRear_buzzer_enable(Boolean rear_buzzer_enable) {
			this.rear_buzzer_enable = rear_buzzer_enable;
		}
        
    }
}
