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

import logic.EventID;
import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueRadioButton;
import slecon.component.iobar.IOBar;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;




/**
 * Setup -> Module -> Fireman's Emergency Operation.
 */
public class FiremansEmergencyOperation extends JPanel {
    private static final long serialVersionUID = -8054113534228418322L;
    /**
     * Text resource.
     */
    public static final ResourceBundle               TEXT    = ToolBox.getResourceBundle( "setting.module.FiremansEmergencyOperation" );
    private boolean                                  started = false;
    private SettingPanel<FiremansEmergencyOperation> settingPanel;
    private JLabel                                   cpt_general;
    private ValueCheckBox                            ebd_enabled;
    private JLabel                                   lbl_return_floor;
    private MyComboBox			                     cbo_return_floor;
    private JLabel                                   lbl_car_message;
    private MyComboBox								 cbo_car_message;
    private JLabel                                   lbl_hall_message;
    private MyComboBox 								 cbo_hall_message;
    private ValueCheckBox  ebd_enabled_edp;
    private ValueCheckBox  ebd_enabled_sgs;

    /* ---------------------------------------------------------------------------- */
    private JLabel cpt_i_o_settings;
    private IOBar  io_feo_switch;

    /* ---------------------------------------------------------------------------- */
    private JLabel       cpt_strategy;
    private ValueRadioButton ebd_enabled_stragety_a;
    private ValueRadioButton ebd_enabled_stragety_b;
    private ValueRadioButton ebd_enabled_stragety_c;




    public FiremansEmergencyOperation () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<FiremansEmergencyOperation> panel ) {
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
        ebd_enabled_edp  = new ValueCheckBox();
        ebd_enabled_sgs  = new ValueCheckBox();
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
        add( ebd_enabled_edp, "skip 2, span 1, left, top" );
        add( ebd_enabled_sgs, "span 1, left, wrap, top" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, left, wrap, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, left, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings = new JLabel();

        JLabel lbl_io_feo_switch = new JLabel();
        io_feo_switch = new IOBar( true );
        setCaptionStyle( cpt_i_o_settings );
        setTextLabelStyle( lbl_io_feo_switch );
        lbl_io_feo_switch.setText( EventID.getString( EventID.FEO_FRONT.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_feo_switch, EventID.FEO_FRONT.eventID );
        add( cpt_i_o_settings, "gapbottom 18-12, span, top" );
        add( lbl_io_feo_switch, "skip 2, span, gapright 12, top" );
        add( io_feo_switch, "skip 2, span, gapright 12, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_strategy           = new JLabel();
        ebd_enabled_stragety_a = new ValueRadioButton();
        ebd_enabled_stragety_b = new ValueRadioButton();
        ebd_enabled_stragety_c = new ValueRadioButton();
        setCaptionStyle( cpt_strategy );

        // @CompoentSetting( ebd_enabled_stragety_a )

        // @CompoentSetting( ebd_enabled_stragety_b )
        
        add( cpt_strategy, "gapbottom 18-12, span, top" );
        add( ebd_enabled_stragety_a, "skip 1, span, top" );
        add( ebd_enabled_stragety_b, "skip 1, span, top" );
        add( ebd_enabled_stragety_c, "skip 1, span, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "return_floor", lbl_return_floor, cbo_return_floor );
        bindGroup( "enabled_edp", ebd_enabled_edp);
        bindGroup( "enabled_sgs", ebd_enabled_sgs);
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        bindGroup( "strategy_1", ebd_enabled_stragety_a );
        bindGroup( "strategy_2", ebd_enabled_stragety_b );
        bindGroup( "strategy_3", ebd_enabled_stragety_c );

        ButtonGroup bg = new ButtonGroup();
        bg.add( ebd_enabled_stragety_a );
        bg.add( ebd_enabled_stragety_b );
        bg.add( ebd_enabled_stragety_c );
        bindGroup( new AbstractButton[]{ ebd_enabled }, lbl_return_floor, cbo_return_floor, ebd_enabled_edp, ebd_enabled_sgs, lbl_car_message, cbo_car_message, lbl_hall_message,
                   cbo_hall_message, io_feo_switch, ebd_enabled_stragety_a, ebd_enabled_stragety_b, ebd_enabled_stragety_c );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_return_floor.setText( TEXT.getString( "return_floor" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );
        ebd_enabled_edp.setText( TEXT.getString( "enabled_edp" ) );
        ebd_enabled_sgs.setText( TEXT.getString( "enabled_sgs" ) );
        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings.setText( TEXT.getString( "event_setting" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_strategy.setText( TEXT.getString( "strategy" ) );
        ebd_enabled_stragety_a.setText( TEXT.getString( "strategy_1" ) );
        ebd_enabled_stragety_b.setText( TEXT.getString( "strategy_2" ) );
        ebd_enabled_stragety_c.setText( TEXT.getString( "strategy_3" ) );

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
        bean_general.setCarMessage( ( comm.constants.DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_general.setHallMessage( ( comm.constants.DeviceMessage )cbo_hall_message.getSelectedItem() );
        bean_general.setEdp_enable( ebd_enabled_edp.isSelected() );
        bean_general.setSgs_enable( ebd_enabled_sgs.isSelected() );
        return bean_general;
    }


    public StrategyBean getStrategyBean () throws ConvertException {
        StrategyBean bean_strategy = new StrategyBean();
        bean_strategy.setEnabledStragetyA( ebd_enabled_stragety_a.isSelected() );
        bean_strategy.setEnabledStragetyB( ebd_enabled_stragety_b.isSelected() );
        bean_strategy.setEnabledStragetyC( ebd_enabled_stragety_c.isSelected() );
        return bean_strategy;
    }


    public IOSettingsBean getIOSettingsBean () throws ConvertException {
        IOSettingsBean bean_iOSettings = new IOSettingsBean();
        bean_iOSettings.setFeoSwitchEvent( io_feo_switch.getEvent() );
        return bean_iOSettings;
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.cbo_return_floor.setSelectedItem( bean_general.getReturnFloor() );
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
        this.ebd_enabled_edp.setOriginSelected(bean_general.getEdp_enable() != null && bean_general.getEdp_enable() == true);
        this.ebd_enabled_sgs.setOriginSelected(bean_general.getSgs_enable() != null && bean_general.getSgs_enable() == true);
    }


    public void setStrategyBean ( StrategyBean bean_strategy ) {
        this.ebd_enabled_stragety_a.setOriginSelected( bean_strategy.getEnabledStragetyA() != null && bean_strategy.getEnabledStragetyA() == true );
        this.ebd_enabled_stragety_b.setOriginSelected( bean_strategy.getEnabledStragetyB() != null && bean_strategy.getEnabledStragetyB() == true );
        this.ebd_enabled_stragety_c.setOriginSelected( bean_strategy.getEnabledStragetyC() != null && bean_strategy.getEnabledStragetyC() == true );
    }


    public void setIOSettingsBean ( IOSettingsBean bean_iOSettings ) {
        this.io_feo_switch.setEvent( bean_iOSettings.getFeoSwitchEvent() );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static FiremansEmergencyOperation createPanel ( SettingPanel<FiremansEmergencyOperation> panel ) {
        FiremansEmergencyOperation gui = new FiremansEmergencyOperation();
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
        private Boolean					  edp_enable;
        private Boolean					  sgs_enable;



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
    }




    public static class IOSettingsBean {
        private Event feoSwitchEvent;




        public final Event getFeoSwitchEvent () {
            return feoSwitchEvent;
        }


        public final void setFeoSwitchEvent ( Event feoSwitchEvent ) {
            this.feoSwitchEvent = feoSwitchEvent;
        }
    }




    public static class StrategyBean {
        private Boolean enabledStragetyA;
        private Boolean enabledStragetyB;
        private Boolean enabledStragetyC;




        public Boolean getEnabledStragetyA () {
            return this.enabledStragetyA;
        }


        public Boolean getEnabledStragetyB () {
            return this.enabledStragetyB;
        }


        public Boolean getEnabledStragetyC() {
			return enabledStragetyC;
		}


		public void setEnabledStragetyA ( Boolean enabledStragetyA ) {
            this.enabledStragetyA = enabledStragetyA;
        }


        public void setEnabledStragetyB ( Boolean enabledStragetyB ) {
            this.enabledStragetyB = enabledStragetyB;
        }


		public void setEnabledStragetyC(Boolean enabledStragetyC) {
			this.enabledStragetyC = enabledStragetyC;
		}
        
    }
}
