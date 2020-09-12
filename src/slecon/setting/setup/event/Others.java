package slecon.setting.setup.event;
import java.awt.Color;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.EventID;
import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.iobar.IOBar;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;




public class Others extends JPanel {
    private static final long     serialVersionUID = -7783322137054727249L;
    
    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.event.Others");

    /* ---------------------------------------------------------------------------- */
    private boolean              started = false;
    private SettingPanel<Others> settingPanel;
    private JLabel               cpt_Front_open_door;
    private JLabel               cpt_Front_close_door;
    private JLabel               cpt_Rear_open_door;
    private JLabel               cpt_Rear_close_door;
    private JLabel               cpt_Disabled_open_door;
    private JLabel               cpt_Disabled_close_door;
    private JLabel               cpt_Front_buzzer;
    private JLabel               cpt_Rear_buzzer;
    private JLabel               cpt_up_indicator;
    private JLabel               cpt_dn_indicator;
    private JLabel               cpt_light_flash;
    private JLabel               cpt_light_fast_flash;
    private JLabel               cpt_light_blink;
    private JLabel               cpt_light_fast_blink;
    private JLabel               cpt_light_cust1;
    private JLabel               cpt_light_cust2;
    private IOBar                io_Front_open_door;
    private IOBar                io_Front_close_door;
    private IOBar                io_Rear_open_door;
    private IOBar                io_Rear_close_door;
    private IOBar                io_Disabled_open_door;
    private IOBar                io_Disabled_close_door;
    private IOBar                io_Front_buzzer;
    private IOBar                io_Rear_buzzer;
    private IOBar                io_up_indicator;
    private IOBar                io_dn_indicator;
    private IOBar                io_light_flash;
    private IOBar                io_light_fast_flash;
    private IOBar                io_light_blink;
    private IOBar                io_light_fast_blink;
    private IOBar                io_light_cust1;
    private IOBar                io_light_cust2;

    public void SetWidgetEnable(boolean enable) {
    	io_Front_open_door.setEnabled(enable);
    	io_Front_close_door.setEnabled(enable);
    	io_Rear_open_door.setEnabled(enable);
    	io_Rear_close_door.setEnabled(enable);
    	io_Disabled_open_door.setEnabled(enable);
    	io_Disabled_close_door.setEnabled(enable);
    	io_Front_buzzer.setEnabled(enable);
    	io_Rear_buzzer.setEnabled(enable);
    	io_up_indicator.setEnabled(enable);
    	io_dn_indicator.setEnabled(enable);
    	io_light_flash.setEnabled(enable);
    	io_light_fast_flash.setEnabled(enable);
    	io_light_blink.setEnabled(enable);
    	io_light_fast_blink.setEnabled(enable);
    	io_light_cust1.setEnabled(enable);
    	io_light_cust2.setEnabled(enable);
    }

    
    /**
     * Create the panel.
     */
    public Others () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<Others> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 24, gap 0 6", "[40::40][20::20][32::32][]" ) );
        cpt_Front_open_door    = new JLabel();
        cpt_Front_close_door   = new JLabel();
        cpt_Rear_open_door    = new JLabel();
        cpt_Rear_close_door   = new JLabel();
        cpt_Disabled_open_door = new JLabel();
        cpt_Disabled_close_door = new JLabel();
        cpt_Front_buzzer       = new JLabel();
        cpt_Rear_buzzer       = new JLabel();
        cpt_up_indicator = new JLabel();
        cpt_dn_indicator = new JLabel();
        cpt_light_flash  = new JLabel();
        cpt_light_fast_flash  = new JLabel();
        cpt_light_blink  = new JLabel();
        cpt_light_fast_blink  = new JLabel();
        cpt_light_cust1  = new JLabel();
        cpt_light_cust2  = new JLabel();
        io_Front_open_door     = new IOBar( true );
        io_Front_close_door    = new IOBar( true );
        io_Rear_open_door     = new IOBar( true );
        io_Rear_close_door    = new IOBar( true );
        io_Disabled_open_door = new IOBar(true);
        io_Disabled_close_door = new IOBar(true);
        io_Front_buzzer        = new IOBar( true );
        io_Rear_buzzer        = new IOBar( true );
        io_up_indicator  = new IOBar( true );
        io_dn_indicator  = new IOBar( true );
        io_light_flash  = new IOBar( true );
        io_light_fast_flash  = new IOBar( true );
        io_light_blink  = new IOBar( true );
        io_light_fast_blink  = new IOBar( true );
        io_light_cust1  = new IOBar( true );
        io_light_cust2  = new IOBar( true );
        setCaptionStyle( cpt_Front_open_door );
        setCaptionStyle( cpt_Front_close_door );
        setCaptionStyle( cpt_Rear_open_door );
        setCaptionStyle( cpt_Rear_close_door );
        setCaptionStyle( cpt_Disabled_open_door );
        setCaptionStyle( cpt_Disabled_close_door );
        setCaptionStyle( cpt_Front_buzzer );
        setCaptionStyle( cpt_Rear_buzzer );
        setCaptionStyle( cpt_up_indicator );
        setCaptionStyle( cpt_dn_indicator );
        setCaptionStyle( cpt_light_flash );
        setCaptionStyle( cpt_light_fast_flash );
        setCaptionStyle( cpt_light_blink );
        setCaptionStyle( cpt_light_fast_blink );
        setCaptionStyle( cpt_light_cust1 );
        setCaptionStyle( cpt_light_cust2 );
        cpt_Front_open_door.setText( EventID.getString( EventID.DOOR_OPEN_BUTTON_FRONT.eventID, null ) );
        cpt_Front_close_door.setText( EventID.getString( EventID.DOOR_CLOSE_BUTTON_FRONT.eventID, null ) );
        cpt_Rear_open_door.setText( EventID.getString( EventID.DOOR_OPEN_BUTTON_REAR.eventID, null ) );
        cpt_Rear_close_door.setText( EventID.getString( EventID.DOOR_CLOSE_BUTTON_REAR.eventID, null ) );
        cpt_Disabled_open_door.setText( EventID.getString( EventID.DOOR_OPEN_BUTTON_DISABLED.eventID, null ) );
        cpt_Disabled_close_door.setText( EventID.getString( EventID.DOOR_CLOSE_BUTTON_DISABLED.eventID, null ) );
        cpt_Front_buzzer.setText( EventID.getString( EventID.BUZZER_FRONT.eventID, null ) );
        cpt_Rear_buzzer.setText( EventID.getString( EventID.BUZZER_REAR.eventID, null ) );
        cpt_up_indicator.setText( EventID.getString( EventID.UP_INDICATOR.eventID, null ) );
        cpt_dn_indicator.setText( EventID.getString( EventID.DOWN_INDICATOR.eventID, null ) );
        cpt_light_flash.setText( EventID.getString( EventID.LIGHT_FLASH.eventID, null ) );
        cpt_light_fast_flash.setText( EventID.getString( EventID.LIGHT_FAST_FLASH.eventID, null ) );
        cpt_light_blink.setText( EventID.getString( EventID.LIGHT_BLINK.eventID, null ) );
        cpt_light_fast_blink.setText( EventID.getString( EventID.LIGHT_FAST_BLINK.eventID, null ) );
        cpt_light_cust1.setText( EventID.getString( EventID.LIGHT_CUST1.eventID, null ) );
        cpt_light_cust2.setText( EventID.getString( EventID.LIGHT_CUST2.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_Front_open_door, EventID.DOOR_OPEN_BUTTON_FRONT.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_Front_close_door, EventID.DOOR_CLOSE_BUTTON_FRONT.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_Rear_open_door, EventID.DOOR_OPEN_BUTTON_REAR.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_Rear_close_door, EventID.DOOR_CLOSE_BUTTON_REAR.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_Disabled_open_door, EventID.DOOR_OPEN_BUTTON_DISABLED.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_Disabled_close_door, EventID.DOOR_CLOSE_BUTTON_DISABLED.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_Front_buzzer, EventID.BUZZER_FRONT.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_Rear_buzzer, EventID.BUZZER_REAR.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_up_indicator, EventID.UP_INDICATOR.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_dn_indicator, EventID.DOWN_INDICATOR.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_light_flash, EventID.LIGHT_FLASH.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_light_fast_flash, EventID.LIGHT_FAST_FLASH.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_light_blink, EventID.LIGHT_BLINK.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_light_fast_blink, EventID.LIGHT_FAST_BLINK.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_light_cust1, EventID.LIGHT_CUST1.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_light_cust2, EventID.LIGHT_CUST2.eventID );
        add( cpt_Front_open_door, "gapbottom 18-12, span, aligny center, top" );
        add( io_Front_open_door, "skip 2, span, gapright 12, wrap 30-12, top" );
        add( cpt_Front_close_door, "gapbottom 18-12, span, aligny center, top" );
        add( io_Front_close_door, "skip 2, span, gapright 12, wrap 30-12, top" );
        add( cpt_Rear_open_door, "gapbottom 18-12, span, aligny center, top" );
        add( io_Rear_open_door, "skip 2, span, gapright 12, wrap 30-12, top" );
        add( cpt_Rear_close_door, "gapbottom 18-12, span, aligny center, top" );
        add( io_Rear_close_door, "skip 2, span, gapright 12, wrap 30-12, top" );
        add( cpt_Disabled_open_door, "gapbottom 18-12, span, aligny center, top" );
        add( io_Disabled_open_door, "skip 2, span, gapright 12, wrap 30-12, top" );
        add( cpt_Disabled_close_door, "gapbottom 18-12, span, aligny center, top" );
        add( io_Disabled_close_door, "skip 2, span, gapright 12, wrap 30-12, top" );
        add( cpt_Front_buzzer, "gapbottom 18-12, span, aligny center, top" );
        add( io_Front_buzzer, "skip 2, span, gapright 12, wrap 30-12, top" );
        add( cpt_Rear_buzzer, "gapbottom 18-12, span, aligny center, top" );
        add( io_Rear_buzzer, "skip 2, span, gapright 12, wrap 30-12, top" );
        add( cpt_up_indicator, "gapbottom 18-12, span, aligny center, top" );
        add( io_up_indicator, "skip 2, span, gapright 12, wrap 30-12, top" );
        add( cpt_dn_indicator, "gapbottom 18-12, span, aligny center, top" );
        add( io_dn_indicator, "skip 2, span, gapright 12, wrap 30-12, top" );
        add( cpt_light_flash, "gapbottom 18-12, span, aligny center, top" );
        add( io_light_flash, "skip 2, span, gapright 12, wrap 30" );
        add( cpt_light_fast_flash, "gapbottom 18-12, span, aligny center, top" );
        add( io_light_fast_flash, "skip 2, span, gapright 12, wrap 30" );
        add( cpt_light_blink, "gapbottom 18-12, span, aligny center, top" );
        add( io_light_blink, "skip 2, span, gapright 12, wrap 30" );
        add( cpt_light_fast_blink, "gapbottom 18-12, span, aligny center, top" );
        add( io_light_fast_blink, "skip 2, span, gapright 12, wrap 30" );
        add( cpt_light_cust1, "gapbottom 18-12, span, aligny center, top" );
        add( io_light_cust1, "skip 2, span, gapright 12, wrap 30" );
        add( cpt_light_cust2, "gapbottom 18-12, span, aligny center, top" );
        add( io_light_cust2, "skip 2, span, gapright 12, wrap 30" );
        SetWidgetEnable(false);
        revalidate();
    }


    public String getBundleText ( String key, String defaultValue ) {
        String result;
        try {
            result = TEXT.getString( key );
        } catch ( Exception e ) {
            result = defaultValue;
        }
        return result;
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_BOLD );
        c.setForeground(Color.WHITE);
    }


    public IOSettingsBean getIOSettingsBean () throws ConvertException {
        IOSettingsBean bean_iOSettings = new IOSettingsBean();
        bean_iOSettings.setFrontbuzzerEvent( io_Front_buzzer.getEvent() );
        bean_iOSettings.setRearbuzzerEvent( io_Rear_buzzer.getEvent() );
        bean_iOSettings.setOpenFrontDoorEvent( io_Front_open_door.getEvent() );
        bean_iOSettings.setCloseFrontDoorEvent( io_Front_close_door.getEvent() );
        bean_iOSettings.setOpenRearDoorEvent( io_Rear_open_door.getEvent() );
        bean_iOSettings.setCloseRearDoorEvent( io_Rear_close_door.getEvent() );
        bean_iOSettings.setOpenDisabledDoorEvent(io_Disabled_open_door.getEvent());
        bean_iOSettings.setCloseDisabledDoorEvent(io_Disabled_close_door.getEvent());
        bean_iOSettings.setUpIndicatorEvent( io_up_indicator.getEvent() );
        bean_iOSettings.setDnIndicatorEvent( io_dn_indicator.getEvent() );
        bean_iOSettings.setLightFlashEvent( io_light_flash.getEvent() );
        bean_iOSettings.setLightFastFlashEvent( io_light_fast_flash.getEvent() );
        bean_iOSettings.setLightBlinkEvent( io_light_blink.getEvent() );
        bean_iOSettings.setLightFastBlinkEvent( io_light_fast_blink.getEvent() );
        bean_iOSettings.setLightCust1Event( io_light_cust1.getEvent() );
        bean_iOSettings.setLightCust2Event( io_light_cust2.getEvent() );
        return bean_iOSettings;
    }


    public void setIOSettingsBean ( IOSettingsBean bean_iOSettings ) {
        this.io_Front_buzzer.setEvent( bean_iOSettings.getFrontbuzzerEvent() );
        this.io_Rear_buzzer.setEvent( bean_iOSettings.getRearbuzzerEvent() );
        this.io_Front_open_door.setEvent( bean_iOSettings.getOpenFrontDoorEvent() );
        this.io_Front_close_door.setEvent( bean_iOSettings.getCloseFrontDoorEvent() );
        this.io_Rear_open_door.setEvent( bean_iOSettings.getOpenRearDoorEvent() );
        this.io_Rear_close_door.setEvent( bean_iOSettings.getCloseRearDoorEvent() );
        this.io_Disabled_open_door.setEvent(bean_iOSettings.getOpenDisabledDoorEvent());
        this.io_Disabled_close_door.setEvent(bean_iOSettings.getCloseDisabledDoorEvent());
        this.io_up_indicator.setEvent( bean_iOSettings.getUpIndicatorEvent() );
        this.io_dn_indicator.setEvent( bean_iOSettings.getDnIndicatorEvent() );
        this.io_light_flash.setEvent( bean_iOSettings.getLightFlashEvent() );
        this.io_light_fast_flash.setEvent( bean_iOSettings.getLightFastFlashEvent() );
        this.io_light_blink.setEvent( bean_iOSettings.getLightBlinkEvent() );
        this.io_light_fast_blink.setEvent( bean_iOSettings.getLightFastBlinkEvent() );
        this.io_light_cust1.setEvent( bean_iOSettings.getLightCust1Event() );
        this.io_light_cust2.setEvent( bean_iOSettings.getLightCust2Event() );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static Others createPanel ( SettingPanel<Others> panel ) {
        Others gui = new Others();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class IOSettingsBean {
        private Event openFrontDoorEvent;
        private Event closeFrontDoorEvent;
        private Event openRearDoorEvent;
        private Event closeRearDoorEvent;
        private Event openDisabledDoorEvent;
        private Event closeDisabledDoorEvent;
        private Event frontbuzzerEvent;
        private Event rearbuzzerEvent;
        private Event upIndicatorEvent;
        private Event dnIndicatorEvent;
        private Event lightFlashEvent;
        private Event lightFastFlashEvent;
        private Event lightBlinkEvent;
        private Event lightFastBlinkEvent;
        private Event lightCust1Event;
        private Event lightCust2Event;
        


        public Event getOpenFrontDoorEvent() {
			return openFrontDoorEvent;
		}


		public Event getCloseFrontDoorEvent() {
			return closeFrontDoorEvent;
		}


		public Event getOpenRearDoorEvent() {
			return openRearDoorEvent;
		}


		public Event getCloseRearDoorEvent() {
			return closeRearDoorEvent;
		}

        public Event getFrontbuzzerEvent() {
			return frontbuzzerEvent;
		}


		public Event getRearbuzzerEvent() {
			return rearbuzzerEvent;
		}


		public final Event getUpIndicatorEvent () {
            return upIndicatorEvent;
        }


        public final Event getDnIndicatorEvent () {
            return dnIndicatorEvent;
        }


        public Event getLightFlashEvent() {
            return lightFlashEvent;
        }


        public Event getLightFastFlashEvent() {
            return lightFastFlashEvent;
        }


        public Event getLightBlinkEvent() {
            return lightBlinkEvent;
        }


        public Event getLightFastBlinkEvent() {
            return lightFastBlinkEvent;
        }


        public Event getLightCust1Event() {
            return lightCust1Event;
        }


        public Event getLightCust2Event() {
            return lightCust2Event;
        }
        
        
        public Event getOpenDisabledDoorEvent() {
			return openDisabledDoorEvent;
		}


		public Event getCloseDisabledDoorEvent() {
			return closeDisabledDoorEvent;
		}


		public void setOpenFrontDoorEvent(Event openFrontDoorEvent) {
			this.openFrontDoorEvent = openFrontDoorEvent;
		}


		public void setCloseFrontDoorEvent(Event closeFrontDoorEvent) {
			this.closeFrontDoorEvent = closeFrontDoorEvent;
		}


		public void setOpenRearDoorEvent(Event openRearDoorEvent) {
			this.openRearDoorEvent = openRearDoorEvent;
		}


		public void setCloseRearDoorEvent(Event closeRearDoorEvent) {
			this.closeRearDoorEvent = closeRearDoorEvent;
		}

		
        public void setFrontbuzzerEvent(Event frontbuzzerEvent) {
			this.frontbuzzerEvent = frontbuzzerEvent;
		}


		public void setRearbuzzerEvent(Event rearbuzzerEvent) {
			this.rearbuzzerEvent = rearbuzzerEvent;
		}


		public final void setUpIndicatorEvent ( Event upIndicatorEvent ) {
            this.upIndicatorEvent = upIndicatorEvent;
        }


        public final void setDnIndicatorEvent ( Event dnIndicatorEvent ) {
            this.dnIndicatorEvent = dnIndicatorEvent;
        }


        public void setLightFlashEvent(Event flashLightEvent) {
            this.lightFlashEvent = flashLightEvent;
        }


        public void setLightFastFlashEvent(Event lightFastFlashEvent) {
            this.lightFastFlashEvent = lightFastFlashEvent;
        }


        public void setLightBlinkEvent(Event lightBlinkEvent) {
            this.lightBlinkEvent = lightBlinkEvent;
        }


        public void setLightFastBlinkEvent(Event lightFastBlinkEvent) {
            this.lightFastBlinkEvent = lightFastBlinkEvent;
        }


        public void setLightCust1Event(Event lightCust1Event) {
            this.lightCust1Event = lightCust1Event;
        }


        public void setLightCust2Event(Event lightCust2Event) {
            this.lightCust2Event = lightCust2Event;
        }


		public void setOpenDisabledDoorEvent(Event openDisabledDoorEvent) {
			this.openDisabledDoorEvent = openDisabledDoorEvent;
		}


		public void setCloseDisabledDoorEvent(Event closeDisabledDoorEvent) {
			this.closeDisabledDoorEvent = closeDisabledDoorEvent;
		}
        
    }
}
