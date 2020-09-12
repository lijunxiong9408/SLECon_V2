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
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueTextField;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;

import comm.constants.DeviceMessage;




/**
 * Setup -> Module -> FullLoad Operation.
 */
public class FullLoadOperation extends JPanel {
    private static final long serialVersionUID = 2157654635514381309L;
    /**
     * Text resource.
     */
    public static final ResourceBundle       TEXT    = ToolBox.getResourceBundle( "setting.module.FullLoadOperation" );
    private boolean                          started = false;
    private SettingPanel<FullLoadOperation> settingPanel;
    private JLabel                           cpt_general;
    private ValueCheckBox                    ebd_enabled;
    private JLabel                           lbl_percentage;
    private ValueTextField                   fmt_percentage;
    private JLabel                           lbl_car_message;
    private MyComboBox				         cbo_car_message;
    private JLabel                           lbl_hall_message;
    private MyComboBox				         cbo_hall_message;
    private ValueCheckBox                    ebd_enabled_front_buzzer;
    private ValueCheckBox                    ebd_enabled_rear_buzzer;

    public FullLoadOperation () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<FullLoadOperation> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_general               = new JLabel();
        ebd_enabled               = new ValueCheckBox();
        lbl_percentage            = new JLabel();
        fmt_percentage            = new ValueTextField();
        lbl_car_message           = new JLabel();
        cbo_car_message           = new MyComboBox( DeviceMessage.values() );
        lbl_hall_message          = new JLabel();
        cbo_hall_message          = new MyComboBox( DeviceMessage.values() );
        ebd_enabled_front_buzzer  = new ValueCheckBox();
        ebd_enabled_rear_buzzer   = new ValueCheckBox();
        
        setCaptionStyle( cpt_general );
        setTextLabelStyle( lbl_percentage );
        fmt_percentage.setColumns( 10 );
        fmt_percentage.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_percentage.setScope( Long.class, 0L, null, false, false );
        fmt_percentage.setEmptyValue( 1L );
        setComboBoxLabelStyle( lbl_car_message );
        setComboBoxValueStyle( cbo_car_message );
        setComboBoxLabelStyle( lbl_hall_message );
        setComboBoxValueStyle( cbo_hall_message );
        
        add( cpt_general, "gapbottom 18-12, span, top" );
        add( ebd_enabled, "skip 1, span, top" );
        add( lbl_percentage, "skip 2, span 1, left, top" );
        add( fmt_percentage, "span 1, left, wrap, top" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, left, wrap, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, left, wrap, top" );
        //add( ebd_enabled_front_buzzer, "skip 1, span, top" );
        //add( ebd_enabled_rear_buzzer, "skip 1, span, top" );
        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "percentage", lbl_percentage, fmt_percentage );
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        bindGroup( "enable_front_buzzer", ebd_enabled_front_buzzer );
        bindGroup( "enable_rear_buzzer", ebd_enabled_rear_buzzer );
        bindGroup( new AbstractButton[]{ ebd_enabled }, lbl_percentage, fmt_percentage, lbl_car_message, cbo_car_message, lbl_hall_message,
                   cbo_hall_message, ebd_enabled_front_buzzer, ebd_enabled_rear_buzzer );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_percentage.setText( TEXT.getString( "percentage" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );
        ebd_enabled_front_buzzer.setText( TEXT.getString( "enable_front_buzzer" ) );
        ebd_enabled_rear_buzzer.setText( TEXT.getString( "enable_rear_buzzer" ) );

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
        if ( detailKey != null && detailKey.trim().length() > 0 ) {
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
    }


    public GeneralBean getGeneralBean () throws ConvertException {
        if ( ! fmt_percentage.checkValue() )
            throw new ConvertException();

        GeneralBean bean_general = new GeneralBean();
        bean_general.setEnabled( ebd_enabled.isSelected() );
        bean_general.setPercentage( ( Long )fmt_percentage.getValue() );
        bean_general.setCarMessage( ( DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_general.setHallMessage( ( DeviceMessage )cbo_hall_message.getSelectedItem() );
        bean_general.setEnabledFrontBuzzer( ebd_enabled_front_buzzer.isSelected() );
        bean_general.setEnabledRearBuzzer( ebd_enabled_rear_buzzer.isSelected() );
        return bean_general;
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.fmt_percentage.setOriginValue( bean_general.getPercentage() );
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
        this.ebd_enabled_front_buzzer.setOriginSelected( bean_general.getEnabledFrontBuzzer() != null
                                                   && bean_general.getEnabledFrontBuzzer() == true );
        this.ebd_enabled_rear_buzzer.setOriginSelected( bean_general.getEnabledRearBuzzer() != null
                                                  && bean_general.getEnabledRearBuzzer() == true );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static FullLoadOperation createPanel ( SettingPanel<FullLoadOperation> panel ) {
        FullLoadOperation gui = new FullLoadOperation();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class GeneralBean {
        private Boolean       enabled;
        private Long          percentage;
        private DeviceMessage carMessage;
        private DeviceMessage hallMessage;
        private Boolean       enabledFrontBuzzer;
        private Boolean       enabledRearBuzzer;

        public Boolean getEnabled () {
            return this.enabled;
        }


        public Long getPercentage () {
            return this.percentage;
        }


        public DeviceMessage getCarMessage () {
            return this.carMessage;
        }


        public DeviceMessage getHallMessage () {
            return this.hallMessage;
        }


        public Boolean getEnabledFrontBuzzer () {
            return this.enabledFrontBuzzer;
        }


        public Boolean getEnabledRearBuzzer () {
            return this.enabledRearBuzzer;
        }


        public void setEnabled ( Boolean enabled ) {
            this.enabled = enabled;
        }


        public void setPercentage ( Long percentage ) {
            this.percentage = percentage;
        }


        public void setCarMessage ( DeviceMessage carMessage ) {
            this.carMessage = carMessage;
        }


        public void setHallMessage ( DeviceMessage hallMessage ) {
            this.hallMessage = hallMessage;
        }


        public void setEnabledFrontBuzzer ( Boolean enabledFrontBuzzer ) {
            this.enabledFrontBuzzer = enabledFrontBuzzer;
        }


        public void setEnabledRearBuzzer ( Boolean enabledRearBuzzer ) {
            this.enabledRearBuzzer = enabledRearBuzzer;
        }
    }
}
