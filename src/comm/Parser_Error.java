package comm;
import static logic.util.SiteManagement.MON_MGR;


public class Parser_Error {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * Error data.
     */
    private final byte error[];




    /**
     * Error parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_Error ( String hostname, int port ) {
        this.agent = MON_MGR.getAgent( hostname, port );
        this.error = this.agent.error;
    }


    /**
     * Check whether MCS NVRAM data has errors.
     * This flag will clear automatically once the problem is resolved.
     * This error occurs once unable to do low level operation on read/write data from/to MCS NVRAM.
     * @return Returns {@code true} once MCS NVRAM has error; otherwise, returns {@code false}.
     */
    public boolean getMcsNvramError () {
        if ( this.agent == null )
            return false;
        synchronized ( this.error ) {
            return ( this.error[ 0 ] & 1 ) != 0;
        }
    }


    /**
     * Check whether MCS data has errors.
     * This flag will clear automatically once the problem is resolved.
     * This error occurs once unable to do low level operation on read/write data from/to MCS command.
     * @return Returns {@code true} once MCS NVRAM has error; otherwise, returns {@code false}.
     */
    public boolean getMcsError () {
        if ( this.agent == null )
            return false;
        synchronized ( this.error ) {
            return ( this.error[ 0 ] & 2 ) != 0;
        }
    }


    /**
     * Check whether deployment data has errors.
     * This error occurs once unable to do low level write deployment data.
     * UI has to submit data again in order to clear this error; otherwise, this flag is never clear.
     * @return Returns {@code true} once MCS NVRAM has error; otherwise, returns {@code false}.
     */
    public boolean getDeploymentError () {
        if ( this.agent == null )
            return false;
        synchronized ( this.error ) {
            return ( this.error[ 0 ] & 4 ) != 0;
        }
    }


    /**
     * Check whether event data has errors.
     * This error occurs once unable to do low level write event data.
     * UI has to submit data again in order to clear this error; otherwise, this flag is never clear.
     * @return Returns {@code true} once MCS NVRAM has error; otherwise, returns {@code false}.
     */
    public boolean getEventError () {
        if ( this.agent == null )
            return false;
        synchronized ( this.error ) {
            return ( this.error[ 0 ] & 8 ) != 0;
        }
    }


    /**
     * Check whether module data has errors.
     * This error occurs once unable to do low level write module data.
     * UI has to submit data again in order to clear this error; otherwise, this flag is never clear.
     * @return Returns {@code true} once MCS NVRAM has error; otherwise, returns {@code false}.
     */
    public boolean getModuleError () {
        if ( this.agent == null )
            return false;
        synchronized ( this.error ) {
            return ( this.error[ 0 ] & 16 ) != 0;
        }
    }
    
    /**
     * Check whether door_enable data has errors.
     * This error occurs once unable to do low level write module data.
     * UI has to submit data again in order to clear this error; otherwise, this flag is never clear.
     * @return Returns {@code true} once MCS NVRAM has error; otherwise, returns {@code false}.
     */
    public boolean getDoorEnableError () {
        if ( this.agent == null )
            return false;
        synchronized ( this.error ) {
            return ( this.error[ 0 ] & 32 ) != 0;
        }
    }
    
    /**
     * Check whether update data has errors.
     * This error occurs once unable to do low level write module data.
     * UI has to submit data again in order to clear this error; otherwise, this flag is never clear.
     * @return Returns {@code true} once MCS NVRAM has error; otherwise, returns {@code false}.
     */
    public boolean getUpdateError () {
        if ( this.agent == null )
            return false;
        synchronized ( this.error ) {
            return ( this.error[ 0 ] & 64 ) != 0;
        }
    }
    
    /**
     * Check whether EPS data has errors.
     * This error occurs once unable to do low level write module data.
     * UI has to submit data again in order to clear this error; otherwise, this flag is never clear.
     * @return Returns {@code true} once MCS NVRAM has error; otherwise, returns {@code false}.
     */
    public boolean getEPSError () {
        if ( this.agent == null )
            return false;
        synchronized ( this.error ) {
            return ( this.error[ 0 ] & 128 ) != 0;
        }
    }
}
