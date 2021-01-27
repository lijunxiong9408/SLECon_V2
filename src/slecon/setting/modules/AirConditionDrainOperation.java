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

/**
 * Setup -> Module -> Air Conditioner Drain Operation.
 */
public class AirConditionDrainOperation extends JPanel {
    private static final long serialVersionUID = -8054113534228418322L;
    /**
     * Text resource.
     */
    public static final ResourceBundle               TEXT    = ToolBox.getResourceBundle( "setting.module.AirConditionDrainOperation" );
    private boolean                                  started = false;
    private SettingPanel<AirConditionDrainOperation> settingPanel;
    private JLabel                                   cpt_general;
    private ValueCheckBox                            ebd_enabled;
    private JLabel                                   lbl_return_floor;
    private MyComboBox			                     cbo_return_floor;
    private JLabel                                   lbl_car_message;
    private MyComboBox								 cbo_car_message;
    private JLabel                                   lbl_hall_message;
    private MyComboBox 								 cbo_hall_message;

    /* ---------------------------------------------------------------------------- */
    private JLabel cpt_i_o_settings;
    private IOBar  io_acws_switch;
    private IOBar  io_acdu_switch;



    public AirConditionDrainOperation () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<AirConditionDrainOperation> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_general      = new JLabel();
        ebd_enabled      = new ValueCheckBox();
        lbl_return_floor = new JLabel();
        cbo_return_floor = new MyComboBox();
        lbl_car_message  = new JLabel();
        cbo_car_message  = new MyComboBox( comm.constants.DeviceMessage.values() );
        lbl_hall_message = new JLabel();
        cbo_hall_message = new MyComboBox( comm.constants.DeviceMessage.values() );
        setCaptionStyle( cpt_general );

        // @CompoentSetting( ebd_enabled )

        // @CompoentSetting( lbl_return_floor, cbo_return_floor )
        setComboBoxLabelStyle( lbl_return_floor );
        setComboBoxValueStyle( cbo_return_floor );
        cbo_return_floor.setPreferredSize(new Dimension(120, 25));
        
        // @CompoentSetting( lbl_car_message, cbo_car_message )
        setComboBoxLabelStyle( lbl_car_message );
        setComboBoxValueStyle( cbo_car_message );

        // @CompoentSetting( lbl_hall_message, cbo_hall_message )
        setComboBoxLabelStyle( lbl_hall_message );
        setComboBoxValueStyle( cbo_hall_message );
        add( cpt_general, "gapbottom 18-12, span, top" );
        add( ebd_enabled, "skip 1, span, top" );
        add( lbl_return_floor, "skip 2, span 1, left, top" );
        add( cbo_return_floor, "span 1, left, wrap, top" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, left, wrap, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, left, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings = new JLabel();

        JLabel lbl_io_acws_switch = new JLabel();
        JLabel lbl_io_acdu_switch = new JLabel();
        io_acws_switch = new IOBar( true );
        io_acdu_switch = new IOBar( true );
        setCaptionStyle( cpt_i_o_settings );
        setTextLabelStyle( lbl_io_acws_switch );
        setTextLabelStyle( lbl_io_acdu_switch );
        lbl_io_acws_switch.setText( EventID.getString( EventID.EVTID_ACWS.eventID, null ) );
        lbl_io_acdu_switch.setText( EventID.getString( EventID.EVTID_ACDU.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_acws_switch, EventID.EVTID_ACWS.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_acdu_switch, EventID.EVTID_ACDU.eventID );
        add( cpt_i_o_settings, "gapbottom 18-12, span, top" );
        add( lbl_io_acws_switch, "skip 2, span, gapright 12, top" );
        add( io_acws_switch, "skip 2, span, gapright 12, top" );
        add( lbl_io_acdu_switch, "skip 2, span, gapright 12, top" );
        add( io_acdu_switch, "skip 2, span, gapright 12, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "return_floor", lbl_return_floor, cbo_return_floor );
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );

        bindGroup( new AbstractButton[]{ ebd_enabled }, lbl_return_floor, cbo_return_floor, lbl_car_message, cbo_car_message, lbl_hall_message,
                   cbo_hall_message, io_acws_switch, io_acdu_switch );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_return_floor.setText( TEXT.getString( "return_floor" ) );
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
        bean_general.setReturnFloor( ( FloorText )cbo_return_floor.getSelectedItem() );
        bean_general.setCarMessage( ( comm.constants.DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_general.setHallMessage( ( comm.constants.DeviceMessage )cbo_hall_message.getSelectedItem() );
        return bean_general;
    }



    public IOSettingsBean getIOSettingsBean () throws ConvertException {
        IOSettingsBean bean_iOSettings = new IOSettingsBean();
        bean_iOSettings.setAcwsSwitchEvent( io_acws_switch.getEvent() );
        bean_iOSettings.setAcduSwitchEvent( io_acdu_switch.getEvent() );
        return bean_iOSettings;
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.cbo_return_floor.setSelectedItem( bean_general.getReturnFloor() );
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
    }



    public void setIOSettingsBean ( IOSettingsBean bean_iOSettings ) {
        this.io_acws_switch.setEvent( bean_iOSettings.getAcwsSwitchEvent() );
        this.io_acdu_switch.setEvent( bean_iOSettings.getAcduSwitchEvent() );
    }

    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static AirConditionDrainOperation createPanel ( SettingPanel<AirConditionDrainOperation> panel ) {
        AirConditionDrainOperation gui = new AirConditionDrainOperation();
        gui.setSettingPanel( panel );
        return gui;
    }


    public void setFloorText ( ArrayList<FloorText> list ) {
        cbo_return_floor.removeAllItems();
        for ( FloorText text : list )
            cbo_return_floor.addItem( text );
    }


    public static class GeneralBean {
        private Boolean                      enabled;
        private FloorText                    returnFloor;
        private comm.constants.DeviceMessage carMessage;
        private comm.constants.DeviceMessage hallMessage;

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
    }


    public static class IOSettingsBean {
        private Event acwsSwitchEvent;
        private Event acduSwitchEvent;
        
		public Event getAcwsSwitchEvent() {
			return acwsSwitchEvent;
		}
		public void setAcwsSwitchEvent(Event acwsSwitchEvent) {
			this.acwsSwitchEvent = acwsSwitchEvent;
		}
		
		public Event getAcduSwitchEvent() {
			return acduSwitchEvent;
		}
		public void setAcduSwitchEvent(Event acduSwitchEvent) {
			this.acduSwitchEvent = acduSwitchEvent;
		}
    }

}
