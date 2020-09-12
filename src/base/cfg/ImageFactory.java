package base.cfg;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.omg.CORBA.Current;




/**
 * define the key of the image and load the image.
 *
 * @author superb8
 *
 */
public enum ImageFactory {
    UP_ANIM( true, "images/uparrow4.png" ), 
    DOWN_ANIM( true, "images/downarrow4.png" ), 
    NONE( "images/none.png" ),
    MENU_SHADOW( "images/menu_shadow.png" ), 
    HOME_FLAG( "images/flag_home.png" ), 
    SETUP_FLAG( "images/flag_setup.png" ),
    INSPECT_FLAG( "images/flag_inspect.png" ), 
    TREE_EXPANDED( "images/expand_arrow.gif" ), 
    TREE_COLLAPSED( "images/collapse_arrow.png" ),
    SETUP_ICON( "images/setup_icon_32.png" ), 
    INSPECT_ICON( "images/inspect_icon_32.png" ), 
    DASHBOARD_ICON( "images/dash_icon_32.png" ),
    PREFRENCE_ICON( "images/pref_icon_32.png" ), 
    LOGIN_ICON( "images/login_icon_32.png" ), 
    HELP_ICON( "images/help_icon_32.png" ),
    DASHBOARD_LIFTSTATUS( "images/ui_dashboard_1mesh_bg.png" ), 
    DULL_YELLOW_BALL( "images/ball_light_green.png" ),
    BRIGHT_GREEN_BALL( "images/ball_light_yellow.png" ), 
    BRIGHT_RED_BALL( "images/ball_red.png" ),
    DULL_GREEN_BALL( "images/ball_green.png" ), 
    BRIGHT_YELLOW_BALL( "images/ball_yellow.png" ), 
    DULL_RED_BALL( "images/ball_brown.png" ),
    GRAY_BALL( "images/ball_gray.png" ), 
    CHECKED( "images/Self-Learning.png" ),
    S_BALL("S"),
    B_BALL("B"),
    F_BALL("F"),
    LIFTSELECTOR_DROPDOWN_ICON( "images/ui_dropdown_list_arrow.png" ),
    LIFTSELECTOR_BUILDING_SITE( "images/globe-green.png" ),
    LIFTSELECTOR_BUILDING_NORMAL( "images/building.png" ),
    LIFTSELECTOR_BUILDING_SELECTED( "images/building-old.png" ),
    LIFTSELECTOR_BUILDING_NOTALIVE( "images/building--exclamation.png" ),
    INSPECT_IO_BG( "images/ui_inspect_io_bg.png" ), 
    GRAPH_SUBSTITUTION( "images/ui_mcs_speed_profile.png" ),
    QUICK_PANEL_LOGS( "quick-panel1" ), 
    QUICK_PANEL_CONTROL( "quick-panel2" ), 
    QUICK_PANEL_MOTION( "quick-panel3" ),
    QUICK_PANEL_CALLS( "quick-panel4" ), 
    QUICK_UP_ON( "quick-panel5" ), 
    QUICK_UP_OFF( "quick-panel6" ), 
    QUICK_DN_ON( "quick-panel7" ),
    QUICK_DN_OFF( "quick-panel8" ), 
    DASHBOARD_INDICATOR_000( "dashbaord_indicator_000" ),
    DASHBOARD_INDICATOR_001( "dashbaord_indicator_001" ), 
    DASHBOARD_INDICATOR_010( "dashbaord_indicator_010" ),
    DASHBOARD_INDICATOR_011( "dashbaord_indicator_011" ), 
    DASHBOARD_INDICATOR_100( "dashbaord_indicator_100" ),
    DASHBOARD_INDICATOR_101( "dashbaord_indicator_101" ), 
    DASHBOARD_INDICATOR_110( "dashbaord_indicator_110" ),
    DASHBOARD_INDICATOR_111( "dashbaord_indicator_111" ), 
    IO_BTN_BACKGROUND1( "io-btn-background1" ),
    IO_BTN_BACKGROUND2( "io-btn-background2" ), 
    IO_BTN_BACKGROUND3( "io-btn-background3" ), 
    IO_BTN_BACKGROUND4( "io-btn-background4" ),
    IO_BTN_BACKGROUND5( "io-btn-background5" ), 
    IO_BTN_BACKGROUND6( "io-btn-background6" ), 
    DASHBOARD_COLLAPSE_ON( "dashboard-collapse-on" ),
    DASHBOARD_COLLAPSE_OFF( "dashboard-collapse-off" ), 
    DASHBOARD_COLLAPSE_ON_HOVER( "dashboard-collapse-on-hover" ),
    DASHBOARD_COLLAPSE_OFF_HOVER( "dashboard-collapse-off-hover" ), 
    DASHBOARD_LINK_DISABLED( "dashboard-link-disabled" ),
    DASHBOARD_LINK_DISABLED_HOVER( "dashboard-link-disabled-hover" ), 
    DASHBOARD_LINK_ENABLED( "dashboard-link-enabled" ),
    DASHBOARD_LINK_ENABLED_HOVER( "dashboard-link-enabled-hover" ), 
    INSPECT_LOG_ICON_CRITICAL( "ui_log_list_icon1" ),
    INSPECT_LOG_ICON_WARNING( "ui_log_list_icon2" ), 
    INSPECT_LOG_ICON_GENERAL( "ui_log_list_icon3" ), 
    LED_GREEN( "ui_led_0" ),
    LED_GREEN_DARK( "ui_led_1" ), 
    LED_RED( "ui_led_2" ), 
    LED_RED_DARK( "ui_led_3" ),
    LED_ORANGE( "ui_led_4" ), 
    LED_ORNAGE_DARK( "ui_led_5" ),
    LED_DIM_GRAY( "ui_led_6" ),
    LOGO_TITLE("images/logo.png"),
	
	//	New Icon
    ARROW_ANIM_UP( true, "images/Arrow_Anim_Up.png" ), 
    ARROW_ANIM_DOWN( true, "images/Arrow_Anim_Down.png" ),
	EVENT_OPERATION("images/Event_Operation.png"),
	EVENT_INPUT("images/Event_Input.png"),
	EVENT_OUTPUT("images/Event_Output.png"),
	SPEED_PROFILE("images/Speed_Profile.png"),
	ARROW_SCROLL_UP("images/Arrow_Scroll_Up.png"),
	ARROW_SCROLL_DOWN("images/Arrow_Scroll_Down.png"),
	ARROW_SCROLL_LEFT("images/Arrow_Scroll_Right.png"),
	ARROW_SCROLL_RIGHT("images/Arrow_Scroll_Left.png"),
	ARROW_NAVIGATION("images/Arrow_Navigation.png"),
	ARROW_TREE_CLOSE("images/Arrow_Tree_Close.png"),
	ARROW_TREE_OPEN("images/Arrow_Tree_Open.png"),
	ARROW_COMBBOX_UP("images/Arrow_CombBox_Down.png"),
	ARROW_COMBBOX_DOWN("images/Arrow_CombBox_Down.png"),
	SELECTOR( "images/Selected_Icon.png" ), 
	DISSELECTOR( "images/DisSelected_Icon.png" ), 
	IOSTATUS_BG( "images/IOStatus_bg.png" ), 
	DOOR_OPENED_ICON( "images/Door_Opened.png" ),
	DOOR_OPENING_ICON( "images/Door_Opening.png" ), 
	DOOR_CLOSING_ICON( "images/Door_Closing.png" ),
	DOOR_CLOSED_ICON( "images/Door_Closed.png" ), 
	DOOR_UNKNOWN_ICON( "images/Door_OffLine.png" ), 
	DOOR_ONLINE("images/Door_OnLine.png"),
	DOOR_OFFLINE("images/Door_OffLine.png"),
	DOOR_SGS( "images/Door_Sgs.png" ),
	DOOR_DDO( "images/Door_ddo.png" ), 
	BUTTON("images/button.png"),
	SHORTCUT_BUTTON_OFF("images/ShortCut_Button_Off.png"),
	SHORTCUT_BUTTON_ON("images/ShortCut_Button_On.png"),
	BUTTON_LIFTSELECTOR("images/Button_LiftSelector.png"),
	BUTTON_PAUSE("images/Button_Pause.png"),
	BUTTON_START("images/Button_Start.png"),
	BUTTON_CALL_ON("images/Button_Call_On.png"),
	BUTTON_CALL_OFF("images/Button_Call_Off.png"),
	BUTTON_CALL_UP("images/Button_Call_Up.png"),
	BUTTON_CALL_DOWN("images/Button_Call_Down.png"),
	BUTTON_CALL_UP_ON("images/Button_Call_Up_On.png"),
	BUTTON_CALL_DOWN_ON("images/Button_Call_Down_On.png"),	
	LIGHT_BLACK("images/Light_Black.png"),
	LIGHT_BRIGHT_GREEN("images/Light_Bright_Green.png"),
	LIGHT_DARK_GREEN("images/Light_Dark_Green.png"),
	LIGHT_BRIGHT_ORANGE("images/Light_Bright_Orange.png"),
	LIGHT_DARK_ORANGE("images/Light_Dark_Orange.png"),
	LIGHT_BRIGHT_RED("images/Light_Bright_Red.png"),
	LIGHT_DARK_RED("images/Light_Dark_Red.png"),
	SUBCALLBUTTON("images/subcallbutton.png"),
	SUBCALLTEXT("images/subcalltext.png"),
	SHORTCUT_NORMAL("images/ShortCut_Normal.png"),
	SHORTCUT_ACTIVITY("images/ShortCut_Activity.png"),
	SETUP_NORMAL("images/Setup_Normal.png"),
	SETUP_ACTIVITY("images/Setup_Activity.png"),
	INSPECT_NORMAL("images/Inspect_Normal.png"),
	INSPECT_ACTIVITY("images/Inspect_Activity.png"),
	BUTTON_LOG("images/Button_Log.png"),
	BUTTON_CALL("images/Button_Call.png"),
	BUTTON_MOTION("images/Button_Motion.png"),
	BUTTON_OPERATION("images/Button_Operation.png"),
	TRANSPARENT_BACKGROUND("images/Transparent.png");

    private static final String                        DASHBOARD_COLLAPSE  = "images/ui_icon_collapse.png";
    private static final String                        DASHBOARD_LINK      = "images/ui_icon_link.png";
    private static final String                        IO_BTN_BACKGROUND   = "images/ui_ocs_io.png";
    private static final String                        QUICK_PANEL         = "images/ui_raster_icons_16.png";
    private static final String                        UI_LED              = "images/ui_led.png";
    private static final String                        UI_LED2             = "images/ui_led2.png";
    private static final String                        DASHBOARD_INDICATOR = "images/ui_call_arrow.png";
    private static final String                        INSPECT_LOG_ICON    = "images/ui_log_list_icon.png";
    public static final Map<ImageFactory, Image>       images              = new HashMap<>();
    public static final Map<ImageFactory, ImageIcon[]> sequence            = new HashMap<ImageFactory, ImageIcon[]>();




    static {
        BufferedImage img;
        int           width, height;
        img                                 = getImageFromResource( QUICK_PANEL );
        width                               = img.getWidth( null ) / 8;
        height                              = img.getHeight( null );
        QUICK_PANEL_LOGS.image              = new BufferedImage[]{ img.getSubimage( width * 0, 0, width, height ) };
        QUICK_PANEL_CALLS.image             = new BufferedImage[]{ img.getSubimage( width * 1, 0, width, height ) };
        QUICK_PANEL_MOTION.image            = new BufferedImage[]{ img.getSubimage( width * 2, 0, width, height ) };
        QUICK_PANEL_CONTROL.image           = new BufferedImage[]{ img.getSubimage( width * 3, 0, width, height ) };
        QUICK_DN_ON.image                   = new BufferedImage[]{ img.getSubimage( width * 4, 0, width, height ) };
        QUICK_UP_ON.image                   = new BufferedImage[]{ img.getSubimage( width * 5, 0, width, height ) };
        QUICK_DN_OFF.image                  = new BufferedImage[]{ img.getSubimage( width * 6, 0, width, height ) };
        QUICK_UP_OFF.image                  = new BufferedImage[]{ img.getSubimage( width * 7, 0, width, height ) };
        img                                 = getImageFromResource( UI_LED );
        width                               = img.getWidth( null ) / 7;
        height                              = img.getHeight( null );
        LED_GREEN.image                     = new BufferedImage[]{ img.getSubimage( width * 0, 0, width, height ) };
        LED_GREEN_DARK.image                = new BufferedImage[]{ img.getSubimage( width * 1, 0, width, height ) };
        LED_RED.image                       = new BufferedImage[]{ img.getSubimage( width * 2, 0, width, height ) };
        LED_RED_DARK.image                  = new BufferedImage[]{ img.getSubimage( width * 3, 0, width, height ) };
        LED_ORANGE.image                    = new BufferedImage[]{ img.getSubimage( width * 4, 0, width, height ) };
        LED_ORNAGE_DARK.image               = new BufferedImage[]{ img.getSubimage( width * 5, 0, width, height ) };
        LED_DIM_GRAY.image                  = new BufferedImage[]{ img.getSubimage( width * 6, 0, width, height ) };
        img                                 = getImageFromResource( UI_LED2 );
        width                               = img.getWidth( null ) / 10;
        height                              = img.getHeight( null );
        BRIGHT_GREEN_BALL.image             = new BufferedImage[]{ img.getSubimage( width * 0, 0, width, height ) };
        DULL_GREEN_BALL.image               = new BufferedImage[]{ img.getSubimage( width * 1, 0, width, height ) };
        BRIGHT_RED_BALL.image               = new BufferedImage[]{ img.getSubimage( width * 2, 0, width, height ) };
        DULL_RED_BALL.image                 = new BufferedImage[]{ img.getSubimage( width * 3, 0, width, height ) };
        BRIGHT_YELLOW_BALL.image            = new BufferedImage[]{ img.getSubimage( width * 4, 0, width, height ) };
        DULL_YELLOW_BALL.image              = new BufferedImage[]{ img.getSubimage( width * 5, 0, width, height ) };
        GRAY_BALL.image                     = new BufferedImage[]{ img.getSubimage( width * 6, 0, width, height ) };
        S_BALL.image                        = new BufferedImage[]{ img.getSubimage( width * 7, 0, width, height ) };
        B_BALL.image                        = new BufferedImage[]{ img.getSubimage( width * 8, 0, width, height ) };
        F_BALL.image                        = new BufferedImage[]{ img.getSubimage( width * 9, 0, width, height ) };
        img                                 = getImageFromResource( IO_BTN_BACKGROUND );
        width                               = img.getWidth( null ) / 6;
        height                              = img.getHeight( null );
        IO_BTN_BACKGROUND1.image            = new BufferedImage[]{ img.getSubimage( width * 0, 0, width, height ) };
        IO_BTN_BACKGROUND2.image            = new BufferedImage[]{ img.getSubimage( width * 1, 0, width, height ) };
        IO_BTN_BACKGROUND3.image            = new BufferedImage[]{ img.getSubimage( width * 2, 0, width, height ) };
        IO_BTN_BACKGROUND4.image            = new BufferedImage[]{ img.getSubimage( width * 3, 0, width, height ) };
        IO_BTN_BACKGROUND5.image            = new BufferedImage[]{ img.getSubimage( width * 4, 0, width, height ) };
        IO_BTN_BACKGROUND6.image            = new BufferedImage[]{ img.getSubimage( width * 5, 0, width, height ) };
        img                                 = getImageFromResource( DASHBOARD_COLLAPSE );
        width                               = img.getWidth( null ) / 4;
        height                              = img.getHeight( null );
        DASHBOARD_COLLAPSE_ON.image         = new BufferedImage[]{ img.getSubimage( width * 0, 0, width, height ) };
        DASHBOARD_COLLAPSE_ON_HOVER.image   = new BufferedImage[]{ img.getSubimage( width * 1, 0, width, height ) };
        DASHBOARD_COLLAPSE_OFF.image        = new BufferedImage[]{ img.getSubimage( width * 2, 0, width, height ) };
        DASHBOARD_COLLAPSE_OFF_HOVER.image  = new BufferedImage[]{ img.getSubimage( width * 3, 0, width, height ) };
        img                                 = getImageFromResource( DASHBOARD_LINK );
        width                               = img.getWidth( null ) / 4;
        height                              = img.getHeight( null );
        DASHBOARD_LINK_DISABLED.image       = new BufferedImage[]{ img.getSubimage( width * 0, 0, width, height ) };
        DASHBOARD_LINK_DISABLED_HOVER.image = new BufferedImage[]{ img.getSubimage( width * 1, 0, width, height ) };
        DASHBOARD_LINK_ENABLED.image        = new BufferedImage[]{ img.getSubimage( width * 2, 0, width, height ) };
        DASHBOARD_LINK_ENABLED_HOVER.image  = new BufferedImage[]{ img.getSubimage( width * 3, 0, width, height ) };
        img                                 = getImageFromResource( DASHBOARD_INDICATOR );
        width                               = img.getWidth( null ) / 8;
        height                              = img.getHeight( null );
        DASHBOARD_INDICATOR_000.image       = new BufferedImage[]{ img.getSubimage( width * 0, 0, width, height ) };
        DASHBOARD_INDICATOR_001.image       = new BufferedImage[]{ img.getSubimage( width * 1, 0, width, height ) };
        DASHBOARD_INDICATOR_010.image       = new BufferedImage[]{ img.getSubimage( width * 2, 0, width, height ) };
        DASHBOARD_INDICATOR_011.image       = new BufferedImage[]{ img.getSubimage( width * 3, 0, width, height ) };
        DASHBOARD_INDICATOR_100.image       = new BufferedImage[]{ img.getSubimage( width * 4, 0, width, height ) };
        DASHBOARD_INDICATOR_101.image       = new BufferedImage[]{ img.getSubimage( width * 5, 0, width, height ) };
        DASHBOARD_INDICATOR_110.image       = new BufferedImage[]{ img.getSubimage( width * 6, 0, width, height ) };
        DASHBOARD_INDICATOR_111.image       = new BufferedImage[]{ img.getSubimage( width * 7, 0, width, height ) };
        img                                 = getImageFromResource( INSPECT_LOG_ICON );
        width                               = img.getWidth( null ) / 3;
        height                              = img.getHeight( null );
        INSPECT_LOG_ICON_CRITICAL.image     = new BufferedImage[]{ img.getSubimage( width * 0, 0, width, height ) };
        INSPECT_LOG_ICON_WARNING.image      = new BufferedImage[]{ img.getSubimage( width * 1, 0, width, height ) };
        INSPECT_LOG_ICON_GENERAL.image      = new BufferedImage[]{ img.getSubimage( width * 2, 0, width, height ) };
    }


    private final String    key;
    private boolean         isSequence;
    private BufferedImage[] image;




    ImageFactory ( String key ) {
        this( false, key );
    }


    ImageFactory ( boolean isSequence, String key ) {
        this.isSequence = isSequence;
        this.key        = key;
    }


    private final static BufferedImage getImageFromResource ( String key ) {
        InputStream inputStream = ImageFactory.class.getClassLoader().getResourceAsStream(key);
        try {
            return ImageIO.read( inputStream );
        } catch ( IOException e ) {
        }
        return null;
    }


    private void loadImage () {
        if ( isSequence ) {
            BufferedImage img         = getImageFromResource( key );
            int           totalImages = img.getWidth( null ) / img.getHeight( null );
            int           img_width   = img.getWidth( null ) / totalImages;
            int           img_height  = img.getHeight( null );
            image = new BufferedImage[ totalImages ];
            for ( int i = 0 ; i < totalImages ; i++ ) {
                image[ i ] = img.getSubimage( img_width * i, 0, img_width, img_height );
            }
        } else {
            image = new BufferedImage[]{ getImageFromResource( key ) };
        }
    }


    public Image image () {
        if ( image == null ) {
            loadImage();
        }
        return image[ 0 ];
    }


    public Image image ( int width, int height ) {
        return image().getScaledInstance( width, height, Image.SCALE_SMOOTH );
    }


    public ImageIcon icon () {
        return new ImageIcon( image() );
    }


    public ImageIcon icon ( int width, int height ) {
        return new ImageIcon( image( width, height ) );
    }


    public ImageIcon[] icons () {
        return icons( -1, -1 );
    }


    public ImageIcon[] icons ( int width, int height ) {
        if ( image == null )
            loadImage();
        if ( isSequence ) {
            ImageIcon[] images = new ImageIcon[ image.length ];
            for ( int i = 0 ; i < image.length ; i++ ) {
                ImageIcon icon = new ImageIcon( image[ i ].getScaledInstance( width, height, Image.SCALE_SMOOTH ) );
                if ( width >= 0 && height >= 0 )
                    images[ i ] = icon;
                else
                    images[ i ] = new ImageIcon( image[ i ] );
            }
            return images;
        } else {
            return new ImageIcon[]{ icon( width, height ) };
        }
    }


    /**
     * merge 2 image and resized. One on the left, and the other is on the right.
     *
     * @param key1      the image in the left hand side
     * @param key2      the image in the right hand side
     * @param width     the width of a image. The final width is 2*width;
     * @param height    the height of a image.
     * @return
     */
    public static ImageIcon merge ( ImageFactory key1, ImageFactory key2, int size ) {
        BufferedImage bimage = new BufferedImage( size * 2 + 5, size, BufferedImage.TYPE_4BYTE_ABGR_PRE );
        Graphics2D    g      = bimage.createGraphics();
        g.drawImage( key1.image( size, size ), 0, 0, null );
        g.drawImage( key2.image( size, size ), size + 5, 0, null );
        g.dispose();
        return new ImageIcon( bimage );
    }
}
