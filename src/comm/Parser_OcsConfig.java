package comm;
import static logic.util.SiteManagement.MON_MGR;


public class Parser_OcsConfig {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * OCS configuration data.
     */
    private final byte[] ocs_config;




    /**
     * OCS configuration parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_OcsConfig ( String hostname, int port ) {
        this.agent      = MON_MGR.getAgent( hostname, port );
        this.ocs_config = this.agent.ocs_config;
    }


    /**
     * Get the version of communication protocol.
     * @return Returns the version of communication protocol.
     */
    public int getProtocolVersion () {
        synchronized ( this.ocs_config ) {
            return this.ocs_config[ 0 ];
        }
    }


    /**
     * Get the version of OCS.
     * @return Returns the version of OCS.
     */
    public String getVersion () {
        synchronized ( this.ocs_config ) {
            return String.format( "%d.%d.%d", this.ocs_config[ 1 ], this.ocs_config[ 2 ], this.ocs_config[ 3 ] );
        }
    }
    
    
    /**
     * Get number of days accumulated without clear expire days (Reserved for OCS Lock Up module).
     * @return
     */
    public int getOCSExpireDaysCount () {
        synchronized ( this.ocs_config ) {
            return this.ocs_config[ 4 ];
        }
    }
}
