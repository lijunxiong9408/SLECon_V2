package comm;
import static logic.util.SiteManagement.MON_MGR;

import comm.agent.AgentPacket;
import comm.constants.AuthLevel;
import comm.constants.AuthLevel.Role;



public class Parser_Auth {
    /**
     * The role.
     */
    private AuthLevel level = AuthLevel.LEVEL_5;

    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;
    private String      hash;




    public Parser_Auth ( String hostname, int port ) {
        this.agent = MON_MGR.getAgent( hostname, port );
    }


    /**
     * Authentication data parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     * @param username  It specifies the username, it allows [a-zA-Z0-9] only.
     * @param password  It specifies the password, it allows [a-zA-Z0-9] only.
     */
    public Parser_Auth ( String hostname, int port, String username, String password ) {
        this.hash  = this.MD5( username + password );
        this.agent = MON_MGR.getAgent( hostname, port );
    }


    /**
     * Do an authentication with server, and the role will update also.
     */
    public boolean auth () {
        byte[] ret = new byte[ 33 ];
        ret[ 0 ] = AgentPacket.PACKET_REQ_AUTH;
        System.arraycopy( hash.getBytes(), 0, ret, 1, 32 );
        this.level = AuthLevel.get( this.agent.retrieve( ret )[ 0 ] );
//        System.out.println( "Level: " + this.level );
        return true;
    }


    public boolean auth ( String username, String password ) {
        byte[] ret = new byte[ 33 ];
        ret[ 0 ]  = AgentPacket.PACKET_REQ_AUTH;
        this.hash = this.MD5( username + password );
        System.arraycopy( hash.getBytes(), 0, ret, 1, 32 );
        this.level = AuthLevel.get( this.agent.retrieve( ret )[ 0 ] );
//        System.out.println( "Level: " + this.level );
        return true;
    }


    /**
     * Get the role.
     * @return Returns the role once success; however, it returns <tt>LEVEL_0</tt> (as Guest) if the authentication failed.
     */
    public AuthLevel getLevel () {
        return this.level;
    }
    
    
    /**
     * Get the role.
     * @return Returns the role once success; however, it returns <tt>LEVEL_0</tt> (as Guest) if the authentication failed.
     */
    public Role[] getRoles () {
        return this.level.roles;
    }


    private String MD5 ( String md5 ) {
        try {
            java.security.MessageDigest md    = java.security.MessageDigest.getInstance( "MD5" );
            byte[]                      array = md.digest( md5.getBytes() );
            StringBuffer                sb    = new StringBuffer();
            for ( int i = 0 ; i < array.length ; ++i ) {
                sb.append( Integer.toHexString( ( array[ i ] & 0xFF ) | 0x100 ).substring( 1, 3 ) );
            }
            return sb.toString();
        } catch ( java.security.NoSuchAlgorithmException e ) {
        }
        return null;
    }
}
