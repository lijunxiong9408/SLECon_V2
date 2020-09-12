package slecon.setting.modules;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.Dict;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.home.PosButton;
import slecon.interfaces.ConvertException;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import comm.constants.DeviceMessage;




/**
 * Setup -> Module -> Disabled Person Operation.
 */
public class DisabledPersonOperation extends JPanel {
    private static final long serialVersionUID = 4412644778595410536L;
    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT    = ToolBox.getResourceBundle( "setting.module.DisPersonOper" );
    private boolean                     started = false;
    private SettingPanel<DisabledPersonOperation>     settingPanel;

    /* ---------------------------------------------------------------------------- */
    private JLabel                     cpt_general;
    private JLabel                     lbl_car_message;
    private MyComboBox				   cbo_car_message;
    private JLabel                     lbl_hall_message;
    private MyComboBox				   cbo_hall_message;
    private JLabel                     lbl_hall_strategy;
    private MyComboBox				   cbo_hall_strategy;

    /* ---------------------------------------------------------------------------- */
    
    public DisabledPersonOperation () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<DisabledPersonOperation> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_general      = new JLabel();
        lbl_car_message  = new JLabel();
        cbo_car_message  = new MyComboBox();
        lbl_hall_message = new JLabel();
        cbo_hall_message = new MyComboBox();
        lbl_hall_strategy = new JLabel();
        cbo_hall_strategy = new MyComboBox();
        
        setCaptionStyle( cpt_general );
        cbo_car_message.setModel( new DefaultComboBoxModel<>( DeviceMessage.values() ) );
        cbo_hall_message.setModel( new DefaultComboBoxModel<>( DeviceMessage.values()) );
        cbo_hall_strategy.setModel( new DefaultComboBoxModel<>( HallCallStrategy.values()));
        
        setComboBoxLabelStyle( lbl_car_message );
        setComboBoxValueStyle( cbo_car_message );
        setComboBoxLabelStyle( lbl_hall_message );
        setComboBoxValueStyle( cbo_hall_message );
        setComboBoxLabelStyle( lbl_hall_strategy );
        setComboBoxValueStyle( cbo_hall_strategy );

        // @CompoentSetting( ebd_enabled )
        add( cpt_general, "gapbottom 18-12, span, top" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, left, wrap, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, left, wrap, top" );
        add( lbl_hall_strategy, "skip 2, span 1, left, top" );
        add( cbo_hall_strategy, "span 1, left, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );
        lbl_hall_strategy.setText( TEXT.getString( "hall_strategy" ) );
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


    private void setButtonStyle ( JComponent c ) {
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
        bean_general.setCarMessage( ( DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_general.setHallMessage( ( DeviceMessage )cbo_hall_message.getSelectedItem() );
        bean_general.setStrategy( (HallCallStrategy)cbo_hall_strategy.getSelectedItem() );
        return bean_general;
    }

    public void setGeneralBean ( GeneralBean bean_general ) {
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
        this.cbo_hall_strategy.setSelectedItem( bean_general.getStrategy() );
    }

    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static DisabledPersonOperation createPanel ( SettingPanel<DisabledPersonOperation> panel ) {
        DisabledPersonOperation gui = new DisabledPersonOperation();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class GeneralBean {
        private DeviceMessage carMessage;
        private DeviceMessage hallMessage;
        private HallCallStrategy strategy;

        public DeviceMessage getCarMessage () {
            return this.carMessage;
        }

        public DeviceMessage getHallMessage () {
            return this.hallMessage;
        }

        public void setCarMessage ( DeviceMessage carMessage ) {
            this.carMessage = carMessage;
        }

        public void setHallMessage ( DeviceMessage hallMessage ) {
            this.hallMessage = hallMessage;
        }

		public HallCallStrategy getStrategy() {
			return strategy;
		}

		public void setStrategy(HallCallStrategy strategy) {
			this.strategy = strategy;
		}
    }
    
    public static String getBundleText ( String key, String defaultValue ) {
        String result;
        try {
            result = TEXT.getString( key );
        } catch ( Exception e ) {
            result = defaultValue;
        }
        return result;
    }
    
    public enum HallCallStrategy {
        HALL_VALID( (byte)0 ),
        HALL_INVALID( (byte)1 );
    	
    	 /**
         * Lookup table.
         */
        private static final Map<Byte, HallCallStrategy> LOOKUP = new HashMap<Byte, HallCallStrategy>();
        static {
            for ( HallCallStrategy s : EnumSet.allOf( HallCallStrategy.class ) )
                LOOKUP.put( s.getCode(), s );
        }

        /**
         * Enumeration value.
         */
        private final byte code;
        
        /**
         * Get the constant value of enumeration.
         * @return Returns the constant value of enumeration.
         */
        public byte getCode () {
            return this.code;
        }
        
        /**
         * Available message in device.
         * @param c Constant value of enumeration.
         */
        private HallCallStrategy ( byte c ) {
            this.code = c;
        }
        
        /**
         * Get an instance of enumeration by the constant value.
         * @param code  It specifies the constant value of enumeration.
         * @return Returns an instance of enumeration on success; otherwise, returns {@code null}.
         */
        public static HallCallStrategy get ( byte code ) {
            return LOOKUP.get( code );
        }
        
        public String toString() {
            return getBundleText("LBL_"+name(),name());
        }
    }
}
