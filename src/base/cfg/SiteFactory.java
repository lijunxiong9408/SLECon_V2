package base.cfg;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import base.cfg.INIFile.INIProperty;


public class SiteFactory {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private final Logger logger = LogManager.getLogger( SiteFactory.class );

    /**
     * The instance for INI parser.
     */
    private INIFile ini;

    /**
     * The path to the configuration file of site.
     */
    private String file;




    /**
     * Access the configuration file of site.
     */
    public SiteFactory () {
        try {
            Properties p = new Properties();
            p.load( getClass().getClassLoader().getResourceAsStream( "config.properties" ) );
            this.file = p.getProperty( "sitefile" );
            this.ini = new INIFile( this.file, false );
        } catch ( IOException e ) {
        }
    }


    public void load () {
        if ( this.file != null )
            ini = new INIFile( this.file );
    }


    /**
     * Get total number of site.
     * @return Returns the total number of site in the configuration file.
     */
    public int getSiteCount () {
        return this.ini.getAllSectionNames().length;
    }


    /**
     * Get name of all available sites.
     * @return Return the name of site. Return an array with length zero if no site exists.
     */
    public String[] getSiteName () {
        return this.ini.getAllSectionNames();
    }


    /**
     * Get information of elevators of a site.
     * @param s It specifies the name of site.
     * @return Return the information of elevator. Return an array with length zero if no elevator exists.
     */
    public Elevator[] getElevator ( String s ) {
        int        count = 0;
        Map<String, INIProperty>    site  = this.ini.getProperties( s );
        Elevator[] ret   = new Elevator[ site.size() ];
        List<String> keys = new ArrayList<>(site.keySet());
        Collections.sort(keys);
        for ( String elevator : keys )
            ret[ count++ ] = new Elevator( site.get( elevator ).getPropValue() );
        return ret;
    }


    /**
     * Add an elevator to a site.
     * @param s     It specifies the name of site.
     *     @param label It specifies the human readable text.
     *     @param host  It specifies the hostname of elevator.
     *     @param port  It specifies the port of elevator.
     */
    public void addElevator ( String s, String label, String host, String port ) {
        addElevator( s, new Elevator( label.replaceAll( ",", ",," ), host.replaceAll( ",", ",," ), port.replaceAll( ",", ",," ) ) );
    }


    /**
     * Add an elevator to a site.
     * @param s It specifies the the name of site.
     * @param e It specifies the instance of an elevator.
     */
    public void addElevator ( String s, Elevator e ) {
        Map<String, INIProperty> properties = this.ini.getProperties( s );
        Integer key = (properties == null ? 0 : properties.size()) + 1;
        this.ini.setStringProperty(s, key.toString(), e.toString(), null);
    }


    /**
     * Save the ini file to storage.
     * @return Returns <tt>true</tt> on success; otherwise, returns <tt>false</tt>.
     */
    public boolean save () {
        return this.ini.save();
    }

    public static final class Elevator {
        /**
         * The human readable text.
         */
        public String label;

        /**
         * The hostname of elevator.
         */
        public String host;

        /**
         * The port of elevator.
         */
        public String port;




        /**
         * The data structure of an elevator.
         * @param e It specifies the content read from the ini file.
         */
        public Elevator ( String e ) {
            String[] item;
            e          = e.replaceAll( ",,", "\0" );
            item       = e.split( "," );
            this.label = item[ 0 ].replaceAll( "\0", "," ).trim();
            this.host  = item[ 1 ].trim();
            this.port  = item[ 2 ].trim();
        }


        /**
         * The data structure of an elevator.
         * @param label It specifies the human readable text.
         * @param host  It specifies the hostname of elevator.
         * @param port  It specifies the port of elevator.
         */
        public Elevator ( String label, String host, String port ) {
            this.label = label;
            this.host  = host;
            this.port  = port;
        }


        @Override
        public String toString () {
            return this.label + ", " + this.host + ", " + this.port;
        }
    }
}
