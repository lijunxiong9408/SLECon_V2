package comm;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import comm.constants.DoorAction;
import comm.util.Endian;

public class Parser_DcsConfig {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * MCS configuration data.
     */
    private final byte[] dcs_config;




    /**
     * MCS configuration parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_DcsConfig ( String hostname, int port ) {
        this.agent      = MON_MGR.getAgent( hostname, port );
        this.dcs_config = this.agent.dcs_config;
    }
    
    /**
     * Get the DCS firmware version of DCS.
     * @return Returns the version of firmware of DCS.
     */
    public String getDCSFirmwareVersion () {
    	synchronized ( this.dcs_config ) {
            Matcher matcher = Pattern.compile( "([0-9A-Z.]+)\0?" ).matcher( new String( Arrays.copyOfRange( this.dcs_config, 0, 32 ) ) );
            if ( matcher.find() )
                return matcher.group( 1 );
            return "N/A";
        }
    }
    
    /**
     * Get the DCS board version of DCS.
     * @return Returns the version of board of DCS.
     */
    public String getDCSBoardVersion () {
        synchronized ( this.dcs_config ) {
            Matcher matcher = Pattern.compile( "([0-9A-Z.]+)\0?" ).matcher( new String( Arrays.copyOfRange( this.dcs_config, 32, 64 ) ) );
            if ( matcher.find() )
                return matcher.group( 1 );
            return "N/A";
        }
    }
    
    
    /**
     * Get the EPS firmware version of DCS.
     * @return Returns the version of firmware of DCS.
     */
    public String getEPSFirmwareVersion () {
    	synchronized ( this.dcs_config ) {
            Matcher matcher = Pattern.compile( "([0-9A-Z.]+)\0?" ).matcher( new String( Arrays.copyOfRange( this.dcs_config, 64, 96 ) ) );
            if ( matcher.find() )
                return matcher.group( 1 );
            return "N/A";
        }
    }
    
    /**
     * Get the EPS board version of DCS.
     * @return Returns the version of board of DCS.
     */
    public String getEPSBoardVersion () {
        synchronized ( this.dcs_config ) {
            Matcher matcher = Pattern.compile( "([0-9A-Z.]+)\0?" ).matcher( new String( Arrays.copyOfRange( this.dcs_config, 96, 128 ) ) );
            if ( matcher.find() )
                return matcher.group( 1 );
            return "N/A";
        }
    }
    
}
