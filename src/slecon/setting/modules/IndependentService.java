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
import slecon.setting.modules.FireEmergencyReturnOperation.StrategyBean;
import base.cfg.FontFactory;

import comm.constants.DeviceMessage;




/**
 * Setup -> Module -> Independent Service.
 */
public class IndependentService extends JPanel {
    private static final long serialVersionUID = 8723322077179135112L;
    /**
     * Text resource.
     */
    public static final ResourceBundle       TEXT    = ToolBox.getResourceBundle( "setting.module.IndependentService" );
    private boolean                          started = false;
    private SettingPanel<IndependentService> settingPanel;
    private JLabel                           cpt_general;
    private ValueCheckBox                    ebd_enabled;
    private JLabel                           lbl_car_message;
    private MyComboBox         				 cbo_car_message;
    private JLabel                           lbl_hall_message;
    private MyComboBox         				 cbo_hall_message;

    /* ---------------------------------------------------------------------------- */
    private JLabel cpt_i_o_settings;
    private IOBar  io_isc_switch;

    /* ---------------------------------------------------------------------------- */
    private JLabel       cpt_strategy;
    private ValueRadioButton ebd_enabled_stragety_a;
    private ValueRadioButton ebd_enabled_stragety_b;

    public IndependentService () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<IndependentService> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[40::40][20::20][32::32][]" ) );
        cpt_general      = new JLabel();
        ebd_enabled      = new ValueCheckBox();
        lbl_car_message  = new JLabel();
        cbo_car_message  = new MyComboBox( DeviceMessage.values() );
        lbl_hall_message = new JLabel();
        cbo_hall_message = new MyComboBox( DeviceMessage.values() );
        setCaptionStyle( cpt_general );

        // @CompoentSetting( ebd_enabled )

        // @CompoentSetting( lbl_car_message, cbo_car_message )
        setComboBoxLabelStyle( lbl_car_message );
        setComboBoxValueStyle( cbo_car_message );

        // @CompoentSetting( lbl_hall_message, cbo_hall_message )
        setComboBoxLabelStyle( lbl_hall_message );
        setComboBoxValueStyle( cbo_hall_message );
        add( cpt_general, "gapbottom 18-12, span, top" );
        add( ebd_enabled, "skip 1, span, top" );
        add( lbl_car_message, "skip 2, span, split, gapright 12, top" );
        add( cbo_car_message, "wrap, top" );
        add( lbl_hall_message, "skip 2, span, split, gapright 12, top" );
        add( cbo_hall_message, "right, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings = new JLabel();

        JLabel lbl_io_isc_switch = new JLabel();
        io_isc_switch = new IOBar( true );
        setCaptionStyle( cpt_i_o_settings );
        setTextLabelStyle( lbl_io_isc_switch );
        lbl_io_isc_switch.setText( EventID.getString( EventID.ISC_FRONT.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_isc_switch, EventID.ISC_FRONT.eventID );
        add( cpt_i_o_settings, "gapbottom 18-12, span, top" );
        add( lbl_io_isc_switch, "skip 2, span, gapright 12, top" );
        add( io_isc_switch, "skip 2, span, gapright 12, wrap 30, top" );
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
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        bindGroup( new ValueCheckBox[]{ ebd_enabled }, lbl_car_message, cbo_car_message, lbl_hall_message, 
        											   cbo_hall_message, io_isc_switch, cpt_strategy,
        											   ebd_enabled_stragety_a, ebd_enabled_stragety_b);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add( ebd_enabled_stragety_a );
        bg.add( ebd_enabled_stragety_b );
        
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings.setText( TEXT.getString( "event_setting" ) );

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
        bean_general.setCarMessage( ( DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_general.setHallMessage( ( DeviceMessage )cbo_hall_message.getSelectedItem() );
        return bean_general;
    }


    public IOSettingsBean getIOSettingsBean () throws ConvertException {
        IOSettingsBean bean_iOSettings = new IOSettingsBean();
        bean_iOSettings.setIscSwitchEvent( io_isc_switch.getEvent() );
        return bean_iOSettings;
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
    }


    public void setIOSettingsBean ( IOSettingsBean bean_iOSettings ) {
        io_isc_switch.setEvent( bean_iOSettings.getIscSwitchEvent() );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static IndependentService createPanel ( SettingPanel<IndependentService> panel ) {
        IndependentService gui = new IndependentService();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class GeneralBean {
        private Boolean       enabled;
        private DeviceMessage carMessage;
        private DeviceMessage hallMessage;




        public Boolean getEnabled () {
            return this.enabled;
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


        public void setCarMessage ( DeviceMessage carMessage ) {
            this.carMessage = carMessage;
        }


        public void setHallMessage ( DeviceMessage hallMessage ) {
            this.hallMessage = hallMessage;
        }
    }




    public static final class IOSettingsBean {
        private Event iscSwitchEvent;




        public final Event getIscSwitchEvent () {
            return iscSwitchEvent;
        }


        public final void setIscSwitchEvent ( Event iscSwitchEvent ) {
            this.iscSwitchEvent = iscSwitchEvent;
        }
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
