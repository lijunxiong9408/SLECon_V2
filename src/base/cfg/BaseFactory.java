package base.cfg;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




/**
 * Access the fundamental configuration file of slecon.
 */
public class BaseFactory {
    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( BaseFactory.class );

    /**
     * The instance for INI parser.
     */
    private static INIFile ini;

    /**
     * The locale specifies the language of slecon.
     */
    private static String locale = "en";

    /**
     * The hostname of proxy.
     */
    private static String proxyHostname = "127.0.0.1";

    /**
     * The port of proxy.
     */
    private static int proxyPort = 7777;

    /**
     * Hide offline elevators in home screen.
     */
    private static boolean hideOfflineElevator = false;

    /**
     * Put offline elevator at the bottom of site list in home screen.
     */
    private static boolean putOfflineElevatorAtTheBottom = true;

    private static String uptimeFormat = "[DAY] day [HOUR] hr";

    // Variables for uptime.
    private static final long           MILLISECONDS_PER_WEEK   = 24 * 60 * 60 * 1000L;
    private static final long           MILLISECONDS_PER_DAY    = 24 * 60 * 60 * 1000L;
    private static final long           MILLISECONDS_PER_HOUR   = 60 * 60 * 1000L;
    private static final long           MILLISECONDS_PER_MINUTE = 60 * 1000L;
    private static final long           MILLISECONDS_PER_SECOND = 1000L;

    /**
     * The path to the configuration file of site.
     */
    private static String file;
    
    private static PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(BaseFactory.class);

    static {
        Properties p = new Properties();
        try {
            p.load( BaseFactory.class.getClassLoader().getResourceAsStream( "config.properties" ) );
            file = p.getProperty( "basefile" );
            ini = new INIFile(file);
            
            // Read fundamental configuration from file.
            locale                        = ini.getStringProperty( "settings", "locale" );
            proxyHostname                 = ini.getStringProperty( "settings", "proxy_hostname" );
            proxyPort                     = ini.getIntegerProperty( "settings", "proxy_port" );
            uptimeFormat                  = ini.getStringProperty( "settings", "uptime_format" );
            hideOfflineElevator           = ini.getBooleanProperty( "settings", "hide_offline_elevator" );
            putOfflineElevatorAtTheBottom = ini.getBooleanProperty( "settings", "put_offline_elevator_at_the_bottom" );
            
        } catch ( Exception e ) {
            logger.error( "Unable to parse basefile", e );
        }
    }


    /**
     * Get the locale of vecSpectra. The locale specifies the language of vecSpectra.
     * @return Returns the locale of vecSpectra.
     */
    public static String getLocaleString () {
        return locale;
    }
    

    public static Locale getLocale () {
        return Locale.forLanguageTag(locale);
    }
    
    
    /**
     * Set the locale of vecSpectra. The locale specifies the language of vecSpectra.
     * @param l It specifies the new locale of vecSpectra.
     */
    public static void setLocale ( String locale ) {
        String oldValue = BaseFactory.locale;
        BaseFactory.locale = locale;
        firePropertyChange("locale", oldValue, locale);
    }


    /**
     * Get the hostname of proxy.
     * @return Returns the hostname of proxy.
     */
    public static String getProxyHostname () {
        return proxyHostname;
    }


    /**
     * Set the hostname of proxy.
     * @param h It specifies the new hostname of proxy.
     */
    public static void setProxyHostname ( String proxyHostname ) {
        String oldValue = BaseFactory.proxyHostname;
        BaseFactory.proxyHostname = proxyHostname;
        firePropertyChange("proxyHostname", oldValue, proxyHostname);
    }


    /**
     * Get the port of proxy.
     * @return Returns the port of proxy.
     */
    public static int getProxyPort () {
        return proxyPort;
    }


    /**
     * Set the port of proxy.
     * @param proxyPort It specifies the new port of proxy.
     */
    public static void setProxyPort ( int proxyPort ) {
        int oldValue = BaseFactory.proxyPort;
        BaseFactory.proxyPort = proxyPort;
        firePropertyChange("proxyPort", oldValue, proxyPort);
    }


    public static String getUptimeFormat () {
        return uptimeFormat;
    }
    
    public static String getUptime(long millisec) {
        return getUptime(millisec, getUptimeFormat());
    }
    
    public static String getUptime(long millisec, String format) {
        long millisecond = millisec;
        long second = ( millisecond / MILLISECONDS_PER_SECOND );
        long minute = ( millisecond / MILLISECONDS_PER_MINUTE );
        long hour = ( millisecond / MILLISECONDS_PER_HOUR );
        long day = ( millisecond / MILLISECONDS_PER_DAY );
        long week = ( millisecond / MILLISECONDS_PER_WEEK );

        Map<String,String> fmtMap = new HashMap<String , String>();
        fmtMap.put("\\[WEEK\\]", Long.toString( week ));
        fmtMap.put("\\[DAY\\]", Long.toString( day ));
        fmtMap.put("\\[HOUR\\]", Long.toString( hour ));
        fmtMap.put("\\[MINUTE\\]", Long.toString( minute ));
        fmtMap.put("\\[SECOND\\]", Long.toString( second ));
        
        fmtMap.put("\\[7DAY\\]", Long.toString( day % 7 ));
        fmtMap.put("\\[24HOUR\\]", Long.toString( hour % 24 ));
        fmtMap.put("\\[60MINUTE\\]", Long.toString( minute % 60 ));
        fmtMap.put("\\[SECOND\\]", Long.toString( second % 60 ));
        
        String uptime = format;
        for (Map.Entry<String, String> entry: fmtMap.entrySet()) {
            uptime = uptime.replaceAll(entry.getKey().toString(), entry.getValue().toString());
        }
        return uptime;
    }


    public static void setUptimeFormat ( String uptimeFormat ) {
        String oldValue = BaseFactory.uptimeFormat;
        BaseFactory.uptimeFormat = uptimeFormat;
        firePropertyChange("uptimeFormat", oldValue, uptimeFormat);
    }


    /**
     * Get the status of hiding offline elevators in home screen.
     * @return Returns <tt>true</tt> once this option is enabled; otherwise, returns <tt>false</tt>.
     */
    public static boolean isHideOfflineElevator () {
        return hideOfflineElevator;
    }


    /**
     * Set the status of hiding offline elevators in home screen.
     * @param hideOfflineElevator It specifies the new status.
     */
    public static void setHideOfflineElevator ( boolean hideOfflineElevator ) {
        boolean oldValue = BaseFactory.hideOfflineElevator;
        BaseFactory.hideOfflineElevator = hideOfflineElevator;
        firePropertyChange("hideOfflineElevator", oldValue, hideOfflineElevator);
    }


    /**
     * Get the status of putting offline elevator at the bottom of site list in home screen.
     * @return Returns <tt>true</tt> once this option is enabled; otherwise, returns <tt>false</tt>.
     */
    public static boolean isPutOfflineElevatorAtTheBottom () {
        return putOfflineElevatorAtTheBottom;
    }


    /**
     * Set the status of putting offline elevator at the bottom of site list in home screen.
     * @param s It specifies the new status.
     */
    public static void setPutOfflineElevatorAtTheBottom ( boolean putOfflineElevatorAtTheBottom ) {
        boolean oldValue = BaseFactory.putOfflineElevatorAtTheBottom;
        BaseFactory.putOfflineElevatorAtTheBottom = putOfflineElevatorAtTheBottom;
        firePropertyChange("putOfflineElevatorAtTheBottom", oldValue, putOfflineElevatorAtTheBottom);
    }


    /**
     * Save the ini file to storage.
     * @return Returns <tt>true</tt> on success; otherwise, returns <tt>false</tt>.
     */
    public static boolean save () {
        ini.setStringProperty( "settings", "locale", locale, null);
        ini.setStringProperty( "settings", "proxy_hostname", proxyHostname, null);
        ini.setIntegerProperty( "settings", "proxy_port", proxyPort, null);
        ini.setStringProperty( "settings", "uptime_format", uptimeFormat, null);
        ini.setBooleanProperty( "settings", "hide_offline_elevator", hideOfflineElevator, null);
        ini.setBooleanProperty( "settings", "put_offline_elevator_at_the_bottom", putOfflineElevatorAtTheBottom, null);
        return ini.save();
    }
    
    
    public static void addPropertyChangeListener ( PropertyChangeListener listener ) {
        propertyChangeSupport.addPropertyChangeListener( listener );
    }


    public static void addPropertyChangeListener ( String name, PropertyChangeListener listener ) {
        propertyChangeSupport.addPropertyChangeListener( name, listener );
    }


    public static void removePropertyChangeListener ( PropertyChangeListener listener ) {
        propertyChangeSupport.addPropertyChangeListener( listener );
    }


    public static void removePropertyChangeListener ( String name, PropertyChangeListener listener ) {
        propertyChangeSupport.removePropertyChangeListener( name, listener );
    }


    protected static void firePropertyChange ( String prop, Object oldValue, Object newValue ) {
        propertyChangeSupport.firePropertyChange( prop, oldValue, newValue );
    }
}
