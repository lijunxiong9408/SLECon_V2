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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueRadioButton;
import slecon.component.ValueTextField;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;



/**
 * Setup -> Module -> Fault State Group Suspension.
 */
public class FaultStateGroupSuspension extends JPanel {
    private static final long serialVersionUID = -1987106447811948754L;

    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.module.FaultStateGroupSuspension" );

    private boolean                                 started = false;
    private SettingPanel<FaultStateGroupSuspension> settingPanel;
    private JLabel                                  cpt_general;
    private ValueCheckBox                               ebd_enabled;
    private ValueCheckBox                               ebd_enabled_front_buzzer;
    private ValueCheckBox                               ebd_enabled_rear_buzzer;
    private JLabel                                  lbl_timer_to_activate_module;
    private ValueTextField                          fmt_timer_to_activate_module;

    /* ---------------------------------------------------------------------------- */
    private JLabel       cpt_strategy;
    private ValueRadioButton ebd_schedule_next_call;
    private ValueRadioButton ebd_go_to_nearest_floor;




    public FaultStateGroupSuspension () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<FaultStateGroupSuspension> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_general                  = new JLabel();
        ebd_enabled                  = new ValueCheckBox();
        ebd_enabled_front_buzzer     = new ValueCheckBox();
        ebd_enabled_rear_buzzer      = new ValueCheckBox();
        lbl_timer_to_activate_module = new JLabel();
        fmt_timer_to_activate_module = new ValueTextField();
        setCaptionStyle( cpt_general );

        // @CompoentSetting( ebd_enabled )

        // @CompoentSetting( ebd_enabled_front_buzzer )

        // @CompoentSetting( ebd_enabled_rear_buzzer )

        // @CompoentSetting<Fmt>( lbl_timer_to_activate_module , fmt_timer_to_activate_module )
        setTextLabelStyle( lbl_timer_to_activate_module );
        fmt_timer_to_activate_module.setColumns( 10 );
        fmt_timer_to_activate_module.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_timer_to_activate_module.setScope( Long.class, 0L, null, false, false );
        fmt_timer_to_activate_module.setEmptyValue( 1L );
        add( cpt_general, "gapbottom 18-12, span, top" );
        add( ebd_enabled, "skip 1, span, top" );
        add( lbl_timer_to_activate_module, "skip 2, span 1, left, top" );
        add( fmt_timer_to_activate_module, "span 1, left, wrap, top" );
        add( ebd_enabled_front_buzzer, "skip 1, span, top" );
        add( ebd_enabled_rear_buzzer, "skip 1, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_strategy            = new JLabel();
        ebd_schedule_next_call  = new ValueRadioButton();
        ebd_go_to_nearest_floor = new ValueRadioButton();
        setCaptionStyle( cpt_strategy );

        // @CompoentSetting( ebd_schedule_next_call )

        // @CompoentSetting( ebd_go_to_nearest_floor )
        
        add( cpt_strategy, "gapbottom 18-12, span, top" );
        add( ebd_schedule_next_call, "skip 1, span, top" );
        add( ebd_go_to_nearest_floor, "skip 1, span, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "enable_front_buzzer", ebd_enabled_front_buzzer );
        bindGroup( "enable_rear_buzzer", ebd_enabled_rear_buzzer );
        bindGroup( "activation_time", lbl_timer_to_activate_module, fmt_timer_to_activate_module );
        bindGroup( "schedule_next_call", ebd_schedule_next_call );
        bindGroup( "go_to_nearest_floor", ebd_go_to_nearest_floor );

        ButtonGroup bg = new ButtonGroup();
        bg.add( ebd_schedule_next_call );
        bg.add( ebd_go_to_nearest_floor );
        bindGroup( new AbstractButton[]{ ebd_enabled }, ebd_enabled_front_buzzer, ebd_enabled_rear_buzzer, lbl_timer_to_activate_module,
                   fmt_timer_to_activate_module, ebd_schedule_next_call, ebd_go_to_nearest_floor );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        ebd_enabled_front_buzzer.setText( TEXT.getString( "enable_front_buzzer" ) );
        ebd_enabled_rear_buzzer.setText( TEXT.getString( "enable_rear_buzzer" ) );
        lbl_timer_to_activate_module.setText( TEXT.getString( "activation_time" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_strategy.setText( TEXT.getString( "scheme" ) );
        ebd_schedule_next_call.setText( TEXT.getString( "schedule_next_call" ) );
        ebd_go_to_nearest_floor.setText( TEXT.getString( "go_to_nearest_floor" ) );

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


    public GeneralBean getGeneralBean () throws ConvertException {
        if ( ! fmt_timer_to_activate_module.checkValue() )
            throw new ConvertException();

        GeneralBean bean_general = new GeneralBean();
        bean_general.setEnabled( ebd_enabled.isSelected() );
        bean_general.setEnabledFrontBuzzer( ebd_enabled_front_buzzer.isSelected() );
        bean_general.setEnabledRearBuzzer( ebd_enabled_rear_buzzer.isSelected() );
        bean_general.setTimerToActivateModule( ( Long )fmt_timer_to_activate_module.getValue() );
        return bean_general;
    }


    public StrategyBean getStrategyBean () throws ConvertException {
        StrategyBean bean_strategy = new StrategyBean();
        bean_strategy.setScheduleNextCall( ebd_schedule_next_call.isSelected() );
        bean_strategy.setGoToNearestFloor( ebd_go_to_nearest_floor.isSelected() );
        return bean_strategy;
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.ebd_enabled_front_buzzer.setOriginSelected( bean_general.getEnabledFrontBuzzer() != null
                                                   && bean_general.getEnabledFrontBuzzer() == true );
        this.ebd_enabled_rear_buzzer.setOriginSelected( bean_general.getEnabledRearBuzzer() != null
                                                  && bean_general.getEnabledRearBuzzer() == true );
        this.fmt_timer_to_activate_module.setOriginValue( bean_general.getTimerToActivateModule() );
    }


    public void setStrategyBean ( StrategyBean bean_strategy ) {
        this.ebd_schedule_next_call.setOriginSelected( bean_strategy.getScheduleNextCall() != null && bean_strategy.getScheduleNextCall() == true );
        this.ebd_go_to_nearest_floor.setOriginSelected( bean_strategy.getGoToNearestFloor() != null
                                                  && bean_strategy.getGoToNearestFloor() == true );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static FaultStateGroupSuspension createPanel ( SettingPanel<FaultStateGroupSuspension> panel ) {
        FaultStateGroupSuspension gui = new FaultStateGroupSuspension();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class GeneralBean {
        private Boolean enabled;
        private Boolean enabledFrontBuzzer;
        private Boolean enabledRearBuzzer;
        private Long    timerToActivateModule;




        public Boolean getEnabled () {
            return this.enabled;
        }


        public Boolean getEnabledFrontBuzzer () {
            return this.enabledFrontBuzzer;
        }


        public Boolean getEnabledRearBuzzer () {
            return this.enabledRearBuzzer;
        }


        public Long getTimerToActivateModule () {
            return this.timerToActivateModule;
        }


        public void setEnabled ( Boolean enabled ) {
            this.enabled = enabled;
        }


        public void setEnabledFrontBuzzer ( Boolean enabledFrontBuzzer ) {
            this.enabledFrontBuzzer = enabledFrontBuzzer;
        }


        public void setEnabledRearBuzzer ( Boolean enabledRearBuzzer ) {
            this.enabledRearBuzzer = enabledRearBuzzer;
        }


        public void setTimerToActivateModule ( Long timerToActivateModule ) {
            this.timerToActivateModule = timerToActivateModule;
        }
    }




    public static class StrategyBean {
        private Boolean scheduleNextCall;
        private Boolean goToNearestFloor;




        public Boolean getScheduleNextCall () {
            return this.scheduleNextCall;
        }


        public Boolean getGoToNearestFloor () {
            return this.goToNearestFloor;
        }


        public void setScheduleNextCall ( Boolean scheduleNextCall ) {
            this.scheduleNextCall = scheduleNextCall;
        }


        public void setGoToNearestFloor ( Boolean goToNearestFloor ) {
            this.goToNearestFloor = goToNearestFloor;
        }
    }
}
