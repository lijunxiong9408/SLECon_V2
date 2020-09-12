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
 * Setup -> Module -> OCS Lock Up.
 */
public class OCSLockUp extends JPanel implements ActionListener {
    private static final long serialVersionUID = 4412644778595410536L;
    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT    = ToolBox.getResourceBundle( "setting.module.OCSLockUp" );
    private boolean                     started = false;
    private SettingPanel<OCSLockUp>     settingPanel;

    /* ---------------------------------------------------------------------------- */
    private JLabel                     cpt_general;
    private ValueCheckBox              ebd_enabled;
    private JLabel                     lbl_car_message;
    private MyComboBox				   cbo_car_message;
    private JLabel                     lbl_hall_message;
    private MyComboBox				   cbo_hall_message;

    /* ---------------------------------------------------------------------------- */
    private JLabel                     cpt_information;
    private JLabel                     lbl_lastmaint;
    private JLabel                     val_lastmaint;
    private JLabel                     lbl_day;

    /* ---------------------------------------------------------------------------- */
    private JLabel                     cpt_operation;
    private JLabel                     lbl_reg_maint;
    private PosButton                  btn_reg_maint;

    /* ---------------------------------------------------------------------------- */
    
    public OCSLockUp () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<OCSLockUp> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_general      = new JLabel();
        ebd_enabled      = new ValueCheckBox();
        lbl_car_message  = new JLabel();
        cbo_car_message  = new MyComboBox();
        lbl_hall_message = new JLabel();
        cbo_hall_message = new MyComboBox();
        setCaptionStyle( cpt_general );
        cbo_car_message.setModel( new DefaultComboBoxModel<>( DeviceMessage.values() ) );
        cbo_hall_message.setModel( new DefaultComboBoxModel<>( DeviceMessage.values()) );
        setComboBoxLabelStyle( lbl_car_message );
        setComboBoxValueStyle( cbo_car_message );
        setComboBoxLabelStyle( lbl_hall_message );
        setComboBoxValueStyle( cbo_hall_message );

        // @CompoentSetting( ebd_enabled )
        add( cpt_general, "gapbottom 18-12, span, top" );
        add( ebd_enabled, "skip 1, span, top" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, left, wrap, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, left, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_information = new JLabel();
        lbl_lastmaint   = new JLabel();
        val_lastmaint   = new JLabel("--");
        lbl_day         = new JLabel(Dict.lookup( "DAY" ));
        
        setCaptionStyle( cpt_information );
        setTextLabelStyle( lbl_lastmaint );
        setComboBoxLabelStyle( val_lastmaint );
        setComboBoxLabelStyle( lbl_day );
        
        add( cpt_information, "gapbottom 18-12, span, top" );
        add( lbl_lastmaint, "skip 2, span 1, left, top" );
        add( val_lastmaint, "span 1, split, left, top" );
        add( lbl_day, "wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_operation = new JLabel();
        lbl_reg_maint = new JLabel();
        if(BaseFactory.getLocaleString().equals("en"))
        	btn_reg_maint = new PosButton(ImageFactory.BUTTON_PAUSE.icon(125, 30),
        								  ImageFactory.BUTTON_START.icon(125, 30));
        else
        	btn_reg_maint = new PosButton(ImageFactory.BUTTON_PAUSE.icon(87, 30),
        								  ImageFactory.BUTTON_START.icon(87, 30));
        btn_reg_maint.addActionListener(this);
        
        setCaptionStyle( cpt_operation );
        add( cpt_operation, "gapbottom 18-12, span, top" );
        setButtonStyle( btn_reg_maint );
        add( btn_reg_maint, "skip 2, span, wrap 30, top" );
        
        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        bindGroup( new AbstractButton[]{ ebd_enabled }, lbl_car_message, cbo_car_message, lbl_hall_message, cbo_hall_message, lbl_lastmaint, val_lastmaint, lbl_day, lbl_reg_maint, btn_reg_maint  );

        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_information.setText( TEXT.getString( "cpt_information" ) );
        lbl_lastmaint.setText( TEXT.getString( "lbl_lastmaint" ) );
        
        /* ---------------------------------------------------------------------------- */
        cpt_operation.setText( TEXT.getString( "cpt_operation" ) );
        btn_reg_maint.setText( TEXT.getString( "btn_reg_maint" ) );

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
        bean_general.setEnabled( ebd_enabled.isSelected() );
        bean_general.setCarMessage( ( DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_general.setHallMessage( ( DeviceMessage )cbo_hall_message.getSelectedItem() );
        return bean_general;
    }

    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
    }


    public void setInformationBean ( InformationBean bean_Information ) {
        val_lastmaint.setText( Long.toString( bean_Information.getLastMaintanence() ) );
    }

    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static OCSLockUp createPanel ( SettingPanel<OCSLockUp> panel ) {
        OCSLockUp gui = new OCSLockUp();
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




    public static class InformationBean {
        private Long lastMaintanence;

        public Long getLastMaintanence () {
            return this.lastMaintanence;
        }


        public void setLastMaintanence ( Long lastMaintanence ) {
            this.lastMaintanence = lastMaintanence;
        }
    }
    
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_reg_maint) {
            do_btn_reg_maint_actionPerformed(e);
        }
    }
    
    
    protected void do_btn_reg_maint_actionPerformed(ActionEvent e) {
        if (settingPanel instanceof OCSLockUpSetting)
            ((OCSLockUpSetting) settingPanel).registerMaintanence();
    }
}
