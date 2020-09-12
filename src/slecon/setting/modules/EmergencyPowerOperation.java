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
import logic.EventID;
import net.miginfocom.swing.MigLayout;
import base.cfg.FontFactory;
import comm.constants.DeviceMessage;




/**
 * Setup -> Module -> Emergency Power Operation.
 */
public class EmergencyPowerOperation extends JPanel {
    private static final long serialVersionUID = -9196811984218766150L;
    /**
     * Text resource.
     */
    public static final ResourceBundle            TEXT    = ToolBox.getResourceBundle( "setting.module.EmergencyPowerOperation" );
    private boolean                               started = false;
    private SettingPanel<EmergencyPowerOperation> settingPanel;
    private JLabel                                cpt_general;
    private ValueCheckBox                         ebd_enabled;
    private JLabel                                lbl_maximum_elevators_run;
    private ValueTextField                        fmt_maximum_elevators_run;
    private JLabel                                lbl_car_message;
    private MyComboBox				              cbo_car_message;
    private JLabel                                lbl_hall_message;
    private MyComboBox				              cbo_hall_message;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel                                cpt_io_setting;
    private JLabel                                lbl_io_epo;
    private IOBar                                 io_epo;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel               				  cpt_strategy;
    private ValueRadioButton    				  rd_recovery_has_priority;
    private JLabel               				  lbl_return_floor;
    private MyComboBox						      cbo_return_floor;
    private ValueRadioButton				      rd_operation_has_prioirty;




    public EmergencyPowerOperation () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<EmergencyPowerOperation> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_general               = new JLabel();
        ebd_enabled               = new ValueCheckBox();
        lbl_maximum_elevators_run = new JLabel();
        fmt_maximum_elevators_run = new ValueTextField();
        lbl_car_message           = new JLabel();
        cbo_car_message           = new MyComboBox( DeviceMessage.values() );
        lbl_hall_message          = new JLabel();
        cbo_hall_message          = new MyComboBox( DeviceMessage.values() );
        setCaptionStyle( cpt_general );

        // @CompoentSetting( ebd_enabled )

        // @CompoentSetting<Fmt>( lbl_maximum_elevators_run , fmt_maximum_elevators_run )
        setTextLabelStyle( lbl_maximum_elevators_run );
        fmt_maximum_elevators_run.setColumns( 10 );
        fmt_maximum_elevators_run.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_maximum_elevators_run.setScope( Long.class, 0L, null, false, false );
        fmt_maximum_elevators_run.setEmptyValue( 1L );

        // @CompoentSetting( lbl_car_message, cbo_car_message )
        setComboBoxLabelStyle( lbl_car_message );
        setComboBoxValueStyle( cbo_car_message );

        // @CompoentSetting( lbl_hall_message, cbo_hall_message )
        setComboBoxLabelStyle( lbl_hall_message );
        setComboBoxValueStyle( cbo_hall_message );
        add( cpt_general, "gapbottom 18-12, span, aligny center, top" );
        add( ebd_enabled, "skip 1, span, top" );
        add( lbl_maximum_elevators_run, "skip 2, span 1, left, top" );
        add( fmt_maximum_elevators_run, "span 1, left, wrap, top" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, left, wrap, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, left, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_io_setting = new JLabel();
        lbl_io_epo     = new JLabel();
        io_epo         = new IOBar( true );

        setCaptionStyle( cpt_io_setting );
        setTextLabelStyle( lbl_io_epo );
        lbl_io_epo.setText( EventID.getString( EventID.EPO_SWITCH.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_epo, EventID.EPO_SWITCH.eventID );
        add( cpt_io_setting, "gapbottom 18-12, span, top" );
        add( lbl_io_epo, "skip 2, span, gapright 12, top" );
        add( io_epo, "skip 2, span, wrap 30, top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_strategy               = new JLabel();
        rd_recovery_has_priority   = new ValueRadioButton();
        lbl_return_floor           = new JLabel();
        cbo_return_floor           = new MyComboBox();
        rd_operation_has_prioirty  = new ValueRadioButton();
        setCaptionStyle( cpt_strategy );
        
        rd_operation_has_prioirty.setEnabled( false );

        // @CompoentSetting( ebd_recovery_has_priority )

        // @CompoentSetting( lbl_return_floor, cbo_return_floor )
        setComboBoxLabelStyle( lbl_return_floor );
        setComboBoxValueStyle( cbo_return_floor );
        cbo_return_floor.setPreferredSize(new Dimension(50, 25));

        // @CompoentSetting( ebd_operation_has_prioirty )
        add( cpt_strategy, "gapbottom 18-12, span, aligny center, top" );
        add( rd_recovery_has_priority, "skip 1, span, top" );
        add( lbl_return_floor, "skip 2, span 1, left, top" );
        add( cbo_return_floor, "span 1, left, wrap, top" );
        add( rd_operation_has_prioirty, "skip 1, span, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "maximum_elevators_run", lbl_maximum_elevators_run, fmt_maximum_elevators_run );
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        bindGroup( "recovery_has_priority", rd_recovery_has_priority );
        bindGroup( "return_floor", lbl_return_floor, cbo_return_floor );
        bindGroup( "operation_has_prioirty", rd_operation_has_prioirty );

        ButtonGroup bg = new ButtonGroup();
        bg.add( rd_recovery_has_priority );
        bg.add( rd_operation_has_prioirty );
        bindGroup( new JToggleButton[]{ ebd_enabled }, lbl_maximum_elevators_run, fmt_maximum_elevators_run, lbl_car_message,
                   cbo_car_message, lbl_hall_message, cbo_hall_message, lbl_io_epo, io_epo, rd_recovery_has_priority /* , rd_operation_has_prioirty */ );
        bindGroup( new JToggleButton[]{ rd_recovery_has_priority }, lbl_return_floor, cbo_return_floor );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_maximum_elevators_run.setText( TEXT.getString( "maximum_elevators_run" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_io_setting.setText( TEXT.getString( "IO_settings" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_strategy.setText( TEXT.getString( "scheme" ) );
        rd_recovery_has_priority.setText( TEXT.getString( "recovery_has_priority" ) );
        lbl_return_floor.setText( TEXT.getString( "return_floor" ) );
        rd_operation_has_prioirty.setText( TEXT.getString( "operation_has_prioirty" ) );

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
        if ( ! fmt_maximum_elevators_run.checkValue() )
            throw new ConvertException();

        GeneralBean bean_general = new GeneralBean();
        bean_general.setEnabled( ebd_enabled.isSelected() );
        bean_general.setMaximumElevatorsRun( ( Long )fmt_maximum_elevators_run.getValue() );
        bean_general.setCarMessage( ( DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_general.setHallMessage( ( DeviceMessage )cbo_hall_message.getSelectedItem() );
        return bean_general;
    }
    
    
    public IOSettingsBean getIOSettingsBean () throws ConvertException {
        IOSettingsBean bean_iOSettings = new IOSettingsBean();
        bean_iOSettings.setEpoEvent( io_epo.getEvent() );
        return bean_iOSettings;
    }


    public StrategyBean getStrategyBean () throws ConvertException {
        StrategyBean bean_strategy = new StrategyBean();
        bean_strategy.setRecoveryHasPriority( rd_recovery_has_priority.isSelected() );
        bean_strategy.setReturnFloor( ( FloorText )cbo_return_floor.getSelectedItem() );
        bean_strategy.setOperationHasPrioirty( rd_operation_has_prioirty.isSelected() );
        return bean_strategy;
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.fmt_maximum_elevators_run.setOriginValue( bean_general.getMaximumElevatorsRun() );
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
    }
    
    
    public void setIOSettingsBean ( IOSettingsBean bean_iOSettings ) {
        this.io_epo.setEvent( bean_iOSettings.getEpoEvent() );
    }


    public void setStrategyBean ( StrategyBean bean_strategy ) {
        this.rd_recovery_has_priority.setOriginSelected( bean_strategy.getRecoveryHasPriority() != null
                                                    && bean_strategy.getRecoveryHasPriority() == true );
        this.cbo_return_floor.setSelectedItem( bean_strategy.getReturnFloor() );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static EmergencyPowerOperation createPanel ( SettingPanel<EmergencyPowerOperation> panel ) {
        EmergencyPowerOperation gui = new EmergencyPowerOperation();
        gui.setSettingPanel( panel );
        return gui;
    }


    public void setFloorText ( ArrayList<FloorText> list ) {
        cbo_return_floor.removeAllItems();
        for ( FloorText text : list )
            cbo_return_floor.addItem( text );
    }


    public static class GeneralBean {
        private Boolean       enabled;
        private Long          maximumElevatorsRun;
        private DeviceMessage carMessage;
        private DeviceMessage hallMessage;




        public Boolean getEnabled () {
            return this.enabled;
        }


        public Long getMaximumElevatorsRun () {
            return this.maximumElevatorsRun;
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


        public void setMaximumElevatorsRun ( Long maximumElevatorsRun ) {
            this.maximumElevatorsRun = maximumElevatorsRun;
        }


        public void setCarMessage ( DeviceMessage carMessage ) {
            this.carMessage = carMessage;
        }


        public void setHallMessage ( DeviceMessage hallMessage ) {
            this.hallMessage = hallMessage;
        }
    }
    
    
    
    public static class IOSettingsBean {
        private Event    epoEvent;

        
        
        public final Event getEpoEvent() {
            return epoEvent;
        }

        public final void setEpoEvent(Event epoEvent) {
            this.epoEvent = epoEvent;
        }
    }




    public static class StrategyBean {
        private Boolean   recoveryHasPriority;
        private FloorText returnFloor;
        private Boolean   operationHasPrioirty;




        public Boolean getRecoveryHasPriority () {
            return this.recoveryHasPriority;
        }


        public FloorText getReturnFloor () {
            return this.returnFloor;
        }


        public Boolean getOperationHasPrioirty () {
            return this.operationHasPrioirty;
        }


        public void setRecoveryHasPriority ( Boolean recoveryHasPriority ) {
            this.recoveryHasPriority = recoveryHasPriority;
        }


        public void setReturnFloor ( FloorText returnFloor ) {
            this.returnFloor = returnFloor;
        }


        public void setOperationHasPrioirty ( Boolean operationHasPrioirty ) {
            this.operationHasPrioirty = operationHasPrioirty;
        }
    }
}
