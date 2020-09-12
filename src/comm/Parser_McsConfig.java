package comm;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import comm.util.Endian;



public class Parser_McsConfig {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * MCS configuration data.
     */
    private final byte[] mcs_config;




    /**
     * MCS configuration parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_McsConfig ( String hostname, int port ) {
        this.agent      = MON_MGR.getAgent( hostname, port );
        this.mcs_config = this.agent.mcs_config;
    }


    /**
     * Get the MM ratio.
     * @return Returns MM ratio.
     */
    public float getMMRatio () {
        synchronized ( this.mcs_config ) {
            return Endian.getFloat( this.mcs_config, 0 );
        }
    }


    /**
     * Get the contract speed.
     * @return Returns the contract speed.
     */
    public int getContractSpeed () {
        synchronized ( this.mcs_config ) {
            return Endian.getInt( this.mcs_config, 4 );
        }
    }


    /**
     * Get the lower position of a floor.
     * @param floor     It specifies the floor (It starts from 0).
     * @return Returns the lower position of {@code floor}.
     */
    public int getLower ( byte floor ) {
        synchronized ( this.mcs_config ) {
            return Endian.getInt( this.mcs_config, 8 + floor * 4 );
        }
    }


    /**
     * Get the upper position of a floor.
     * @param floor     It specifies the floor (It starts from 0).
     * @return Returns the upper position of {@code floor}.
     */
    public int getUpper ( byte floor ) {
        synchronized ( this.mcs_config ) {
            return Endian.getInt( this.mcs_config, 520 + floor * 4 );
        }
    }


    /**
     * Get the raw byte array of crossbar.
     * @return Returns crossbar in a byte array.
     */
    public byte[] getCrossbar () {
        synchronized ( this.mcs_config ) {
            return Arrays.copyOfRange( this.mcs_config, 1032, 1032 + 116 );
        }
    }


    /**
     * Get the firmware version of MCS.
     * @return Returns the version of firmware of MCS.
     */
    public String getFirmwareVersion () {
        synchronized ( this.mcs_config ) {
            Matcher matcher = Pattern.compile( "([0-9.]+)\0?" ).matcher( new String( Arrays.copyOfRange( this.mcs_config, 1148,
            		1148 + 32 ) ) );
            if ( matcher.find() )
                return matcher.group( 1 );
            return "N/A";
        }
    }


    /**
     * Get the board version of MCS.
     * @return Returns the version of board of MCS.
     */
    public String getBoardVersion () {
        synchronized ( this.mcs_config ) {
            Matcher matcher = Pattern.compile( "([0-9A-Z.]+)\0?" ).matcher( new String( Arrays.copyOfRange( this.mcs_config, 1180,
            		1180 + 32 ) ) );
            if ( matcher.find() )
                return matcher.group( 1 );
            return "N/A";
        }
    }


    /**
     * Get the serial number of MCS.
     * @return Returns the serial number of MCS.
     */
    public String getSerialNumber () {
        synchronized ( this.mcs_config ) {
            Matcher matcher = Pattern.compile( "([0-9A-Za-z]+)\0?" ).matcher( new String( Arrays.copyOfRange( this.mcs_config, 1212,
            		1212 + 32 ) ) );
            if ( matcher.find() )
                return matcher.group( 1 );
            return "N/A";
        }
    }


    /**
     * Get the contract number of MCS.
     * @return Returns the contract number of MCS.
     */
    public String getContractNumber () {
        synchronized ( this.mcs_config ) {
            Matcher matcher = Pattern.compile( "([0-9A-Za-z]+)\0?" ).matcher( new String( Arrays.copyOfRange( this.mcs_config, 1244,
            		1244 + 32 ) ) );
            if ( matcher.find() )
                return matcher.group( 1 );
            return "N/A";
        }
    }


    /**
     * Get the system-wide real-time clock on last maintenance. The time-stamp is the accumulated million seconds since 1970-01-01 00:00:00 UTC.
     * @return Returns the time-stamp on last maintenance.
     */
    public long getLastMaintenance () {
        synchronized ( this.mcs_config ) {
            return Endian.getLong( this.mcs_config, 1276 );
        }
    }


    /**
     * Get the real-time clock on boot up.
     * @return Returns Returns the time-stamp on boot up.
     */
    public long getBootupTime () {
        synchronized ( this.mcs_config ) {
            return Endian.getLong( this.mcs_config, 1284 );
        }
    }
}
