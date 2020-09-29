package slecon.setting.modules;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueTextField;
import slecon.component.iobar.IOBar;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;

import comm.constants.DeviceMessage;
import logic.EventID;




/**
 * Setup -> Module -> Inspection.
 */
public class Message extends JPanel {
    private static final long serialVersionUID = 5732111397133393810L;
    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT    = ToolBox.getResourceBundle( "setting.module.Message" );
    private boolean                    started = false;
    private SettingPanel<Message>   settingPanel;
    private JLabel                     cpt_inspection_settings;
    private JLabel                     lbl_car_message;
    private MyComboBox   			   cbo_car_message;
    private JLabel                     lbl_hall_message;
    private MyComboBox				   cbo_hall_message;
    
    private JLabel                     cpt_fault_inspection_settings;
    private JLabel                     lbl_fault_car_message;
    private MyComboBox				   cbo_fault_car_message;
    private JLabel                     lbl_fault_hall_message;
    private MyComboBox				   cbo_fault_hall_message;
    
    private JLabel                     cpt_fault_sign_keep_timer;
    private JLabel                     lbl_fault_sign_keep_timer;
    private ValueTextField 			   fmt_fault_sign_keep_timer;
    private JLabel                     lbl_io_fault_sign;
    private IOBar          			   io_fault_sign;
    

    public Message () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<Message> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_inspection_settings = new JLabel();
        lbl_car_message         = new JLabel();
        cbo_car_message         = new MyComboBox( DeviceMessage.values() );
        lbl_hall_message        = new JLabel();
        cbo_hall_message        = new MyComboBox( DeviceMessage.values() );
        setCaptionStyle( cpt_inspection_settings );
        setComboBoxLabelStyle( lbl_car_message );
        setComboBoxValueStyle( cbo_car_message );
        setComboBoxLabelStyle( lbl_hall_message );
        setComboBoxValueStyle( cbo_hall_message );
        add( cpt_inspection_settings, "gapbottom 18-12, span, top" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, wrap, left, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, wrap 30, left, top" );
        /* ---------------------------------------------------------------------------- */

        cpt_fault_inspection_settings = new JLabel();
        lbl_fault_car_message = new JLabel();
        cbo_fault_car_message = new MyComboBox( DeviceMessage.values() );
        lbl_fault_hall_message = new JLabel();
        cbo_fault_hall_message = new MyComboBox( DeviceMessage.values() );

        setCaptionStyle( cpt_fault_inspection_settings );

        // @CompoentSetting( lbl_fault_car_message, cbo_fault_car_message )
        setComboBoxLabelStyle( lbl_fault_car_message );
        setComboBoxValueStyle( cbo_fault_car_message );

        // @CompoentSetting( lbl_hall_message, cbo_fault_hall_message )
        setComboBoxLabelStyle( lbl_fault_hall_message );
        setComboBoxValueStyle( cbo_fault_hall_message );
        add( cpt_fault_inspection_settings, "gapbottom 18-12, span, top" );
        add( lbl_fault_car_message, "skip 2, span 1, left, top" );
        add( cbo_fault_car_message, "span 1, wrap, left, top" );
        add( lbl_fault_hall_message, "skip 2, span 1, left, top" );
        add( cbo_fault_hall_message, "span 1, wrap 30, left, top" );
        /* ---------------------------------------------------------------------------- */
        cpt_fault_sign_keep_timer = new JLabel();
        lbl_fault_sign_keep_timer = new JLabel();
        fmt_fault_sign_keep_timer = new ValueTextField();
        io_fault_sign = new IOBar(true);
        
        setCaptionStyle( cpt_fault_sign_keep_timer );
        setComboBoxLabelStyle( lbl_fault_sign_keep_timer );
        fmt_fault_sign_keep_timer.setColumns( 5 );
        fmt_fault_sign_keep_timer.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_fault_sign_keep_timer.setScope( Long.class, 0L, 120L, true, true );
        fmt_fault_sign_keep_timer.setEmptyValue( 0L );
        
        lbl_io_fault_sign = new JLabel();
        setComboBoxLabelStyle( lbl_io_fault_sign );
        lbl_io_fault_sign.setText( EventID.getString( EventID.EVTID_SYSTEM_FAULT_WARNING.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_fault_sign, EventID.EVTID_SYSTEM_FAULT_WARNING.eventID );
        
        add( cpt_fault_sign_keep_timer, "gapbottom 18-12, span, top" );
        add( lbl_fault_sign_keep_timer, "skip 2, span 1, left, top" );
        add( fmt_fault_sign_keep_timer, "span 1, wrap, left, top" );
        add( lbl_io_fault_sign, "skip 2, span, left, top" );
        add( io_fault_sign, "skip 2, wrap 30, left, top" );
        /* ---------------------------------------------------------------------------- */
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        bindGroup( "fault_car_message", lbl_fault_car_message, cbo_fault_car_message );
        bindGroup( "fault_hall_message", lbl_fault_hall_message, cbo_fault_hall_message );
        /* ---------------------------------------------------------------------------- */
        bindGroup("system_fault_waring", cpt_fault_sign_keep_timer, lbl_io_fault_sign, io_fault_sign);
        bindGroup("system_fault_waring_keep_timer", lbl_fault_sign_keep_timer, fmt_fault_sign_keep_timer);
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_inspection_settings.setText( TEXT.getString( "inspect_settings" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );
        
        cpt_fault_inspection_settings.setText( TEXT.getString( "fault_settings" ) );
        lbl_fault_car_message.setText( TEXT.getString( "fault_car_message" ) );
        lbl_fault_hall_message.setText( TEXT.getString( "fault_hall_message" ) );
        
        cpt_fault_sign_keep_timer.setText( TEXT.getString( "system_fault_waring" ) );
        lbl_fault_sign_keep_timer.setText( TEXT.getString( "system_fault_waring_keep_timer" ) );
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
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
        GeneralBean bean_inspectionSettings = new GeneralBean();
        bean_inspectionSettings.setCarMessage( ( DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_inspectionSettings.setHallMessage( ( DeviceMessage )cbo_hall_message.getSelectedItem() );
        return bean_inspectionSettings;
    }
    

    public FaultInspectionSettingsBean getFaultInspectionSettingsBean () throws ConvertException {
        FaultInspectionSettingsBean bean_inspectionSettings = new FaultInspectionSettingsBean();
        bean_inspectionSettings.setCarMessage( ( DeviceMessage )cbo_fault_car_message.getSelectedItem() );
        bean_inspectionSettings.setHallMessage( ( DeviceMessage )cbo_fault_hall_message.getSelectedItem() );
        return bean_inspectionSettings;
    }
    
    public FaultWarningSignBean getFaultWarningSignBean() throws ConvertException {
    	if ( ! fmt_fault_sign_keep_timer.checkValue() )
            throw new ConvertException();
    	
    	FaultWarningSignBean bean_faltWarningSign = new FaultWarningSignBean();
    	bean_faltWarningSign.setWarning_keep_timer( Short.parseShort(fmt_fault_sign_keep_timer.getValue().toString()) );
    	bean_faltWarningSign.setWarning_event( io_fault_sign.getEvent() );
		return bean_faltWarningSign;
    }
    
    public void setGeneralBean ( GeneralBean bean_inspectionSettings ) {
        this.cbo_car_message.setSelectedItem( bean_inspectionSettings.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_inspectionSettings.getHallMessage() );
    }

    
    public void setFaultInspectionSettingsBean ( FaultInspectionSettingsBean bean_inspectionSettings ) {
        this.cbo_fault_car_message.setSelectedItem( bean_inspectionSettings.getCarMessage() );
        this.cbo_fault_hall_message.setSelectedItem( bean_inspectionSettings.getHallMessage() );
    }
    
    public void setFaultWarningSignBean( FaultWarningSignBean bean_faltWarningSign ) {
    	this.fmt_fault_sign_keep_timer.setOriginValue( bean_faltWarningSign.getWarning_keep_timer() );
    	this.io_fault_sign.setEvent( bean_faltWarningSign.getWarning_event() );
    }
    
    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static Message createPanel ( SettingPanel<Message> panel ) {
        Message gui = new Message();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class GeneralBean {
        private DeviceMessage carMessage;
        private DeviceMessage hallMessage;




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
    }
    
    public static class FaultInspectionSettingsBean {
        private DeviceMessage carMessage;
        private DeviceMessage hallMessage;




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
    }
    
    public static class FaultWarningSignBean{
    	private short warning_keep_timer;
    	private Event warning_event;
		public short getWarning_keep_timer() {
			return warning_keep_timer;
		}
		public void setWarning_keep_timer(short warning_keep_timer) {
			this.warning_keep_timer = warning_keep_timer;
		}
		public Event getWarning_event() {
			return warning_event;
		}
		public void setWarning_event(Event warning_event) {
			this.warning_event = warning_event;
		}
    }
}
