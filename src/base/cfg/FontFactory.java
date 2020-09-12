package base.cfg;
import java.awt.Font;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FontFactory {
    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( FontFactory.class );

    /**
     * 10px, plain style.
     */
    public static final Font FONT_10_PLAIN;

    /**
     * 10px, bold style.
     */
    public static final Font FONT_10_BOLD;
    
    /**
     * 11px, plain style.
     */
    public static final Font FONT_11_PLAIN;

    /**
     * 11px, bold style.
     */
    public static final Font FONT_11_BOLD;

    /**
     * 12px, plain style.
     */
    public static final Font FONT_12_PLAIN;

    /**
     * 12px, bold style.
     */
    public static final Font FONT_12_BOLD;

    /**
     * 14px, plain style.
     */
    public static final Font FONT_14_PLAIN;

    /**
     * 14px, bold style.
     */
    public static final Font FONT_14_BOLD;

    /**
     * 16px, plain style.
     */
    public static final Font FONT_16_PLAIN;

    /**
     * 16px, bold style.
     */
    public static final Font FONT_16_BOLD;
    
    /**
     * 20px, plain style.
     */
    public static final Font FONT_20_PLAIN;
    
    /**
     * 20px, bold style.
     */
    public static final Font FONT_20_BOLD;
    
    /**
     * the font of {@link slecon.component.ValueTextField}
     */
    public static final Font VALUETEXT_TOOLTIP_FONT;
    
    static {
        Properties  p   = new Properties();
        INIFile     ini = null;

        // Load configuration of font form file.
        try {
            p.load( FontFactory.class.getClassLoader().getResourceAsStream( "config.properties" ) );
            ini      = new INIFile( p.getProperty( "basefile" ) );
        } catch ( Exception e ) {
            logger.error( "Unable to parse font settings", e );
        }

        // Configure fonts.
        FONT_10_PLAIN = getUIFont( ini, "FONT_10_PLAIN", new Font( "Tahoma", Font.PLAIN, 11 ) );
        FONT_10_BOLD  = getUIFont( ini, "FONT_10_BOLD", new Font( "Tahoma", Font.BOLD, 11 ) );
        FONT_11_PLAIN = getUIFont( ini, "FONT_11_PLAIN", new Font( "Tahoma", Font.PLAIN, 12 ) );
        FONT_11_BOLD  = getUIFont( ini, "FONT_11_BOLD", new Font( "Tahoma", Font.BOLD, 12 ) );
        FONT_12_PLAIN = getUIFont( ini, "FONT_12_PLAIN", new Font( "Tahoma", Font.PLAIN, 13 ) );
        FONT_12_BOLD  = getUIFont( ini, "FONT_12_BOLD", new Font( "Tahoma", Font.BOLD, 13 ) );
        FONT_14_PLAIN = getUIFont( ini, "FONT_14_PLAIN", new Font( "Tahoma", Font.PLAIN, 15 ) );
        FONT_14_BOLD  = getUIFont( ini, "FONT_14_BOLD", new Font( "Tahoma", Font.BOLD, 15 ) );
        FONT_16_PLAIN = getUIFont( ini, "FONT_16_PLAIN", new Font( "Tahoma", Font.PLAIN, 17 ) );
        FONT_16_BOLD  = getUIFont( ini, "FONT_16_BOLD", new Font( "Tahoma", Font.BOLD, 17 ) );
        FONT_20_PLAIN = getUIFont( ini, "FONT_20_PLAIN", new Font( "Tahoma", Font.PLAIN, 21 ) );
        FONT_20_BOLD  = getUIFont( ini, "FONT_20_BOLD", new Font( "Tahoma", Font.BOLD, 21 ) );
        VALUETEXT_TOOLTIP_FONT  = getUIFont( ini, "VALUETEXT_TOOLTIP_FONT", new Font( "Arial", Font.PLAIN, 13 ) );
    }


    /**
     * Get the font instance specified in the ini file. Once no definition in ini file, a default font is applied.
     * @param settings      It specifies the section of the ini file.
     * @param font          It specifies the property of the section of the ini file.
     * @param defaultFont   It specifies the default font once no definition in the ini file.
     * @return Returns the instance of font.
     */
    private static Font getUIFont ( INIFile ini, String font, Font defaultFont ) {
        try {
            String sectionName = BaseFactory.getLocaleString();
            String fontname = ini.getStringProperty(sectionName, font);
            return Font.decode( fontname );
        } catch ( NullPointerException e ) {
        }
        return defaultFont;
    }
}
