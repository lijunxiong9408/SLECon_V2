package comm.constants;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;



public enum AccessRight {
    /**
     * Allow to read MCS settings in Setup.
     */
    SETUP_MCS_READ( 0 ),

    /**
     * Allow to read DCS settings in Setup.
     */
    SETUP_DCS_READ( 1 ),

    /**
     * Allow to read OCS settings in Setup.
     */
    SETUP_OCS_READ( 2 ),

    /**
     * Allow to read CAN settings in Setup.
     */
    SETUP_CAN_READ( 3 ),

    /**
     * Allow to write MCS settings in Setup.
     */
    SETUP_MCS_WRITE( 4 ),

    /**
     * Allow to write DCS settings in Setup.
     */
    SETUP_DCS_WRITE( 5 ),

    /**
     * Allow to write OCS settings in Setup.
     */
    SETUP_OCS_WRITE( 6 ),

    /**
     * Allow to write CAN settings in Setup.
     */
    SETUP_CAN_WRITE( 7 ),

    /**
     * Allow to access own user management in Setup.
     * E.g. 1. Change own password.
     *      2. View own role.
     *      3. NOT ALLOW to access other users' data.
     */
    SETUP_AUTH_ACCESS_OWN_INFO( 8 ),

    /**
     * Allow to grant all privilege of user management in Setup.
     * E.g. 1. Add/modify/delete users.
     *      2. Change role of all users.
     *      3. Retrieve all information of users in the remote host (PC104).
     */
    SETUP_AUTH_ALL_PRIVILEGE( 9 ),

    /**
     * Allow to read management settings in Setup.
     */
    SETUP_READ_MANAGEMENT( 10 ),

    /**
     * Allow to write management settings in Setup.
     */
    SETUP_WRITE_MANAGEMENT( 11 ),

    /**
     * Allow to read settings in Dashboard.
     */
    DASHBOARD_READ( 12 ),

    /**
     * Allow to write MCS/OCS/DCS/CAN settings in Dashboard.
     */
    DASHBOARD_WRITE( 13 ),

    /**
     * Allow to write MCS settings in Inspect.
     */
    INSPECT_MCS_WRITE( 14 ),

    /**
     * Allow to write DCS settings in Inspect.
     */
    INSPECT_DCS_WRITE( 15 ),

    /**
     * Allow to write OCS settings in Inspect.
     */
    INSPECT_OCS_WRITE( 16 ),

    /**
     * Allow to write CAN settings in Inspect.
     */
    INSPECT_CAN_WRITE( 17 ),

    /**
     * Allow to read MCS settings in Inspect.
     */
    INSPECT_MCS_READ( 18 ),

    /**
     * Allow to read DCS settings in Inspect.
     */
    INSPECT_DCS_READ( 19 ),

    /**
     * Allow to read OCS settings in Inspect.
     */
    INSPECT_OCS_READ( 20 ),

    /**
     * Allow to read CAN settings in Inspect.
     */
    INSPECT_CAN_READ( 21 );

    /**
     * Lookup table.
     */
    private static final Map<Integer, AuthLevel> lookup = new HashMap<>( 6 );




    static {
        for ( AuthLevel s : EnumSet.allOf( AuthLevel.class ) )
            lookup.put( s.getCode(), s );
    }


    /**
     * The instance of enumeration value.
     */
    private int code;




    /**
     * Available access right in vecSystem.
     * @param c     It specifies the constant value of an enumeration.
     */
    private AccessRight ( int c ) {
        this.code = c;
    }


    /**
     * Get the constant value of enumeration.
     * @return Returns the constant value of enumeration.
     */
    public int getCode () {
        return this.code;
    }


    /**
     * Get an instant of enumeration by the constant value <tt>code</tt>.
     * @param c     It specifies the constant value will search.
     * @return Returns an instant of enumeration on success; otherwise, returns <tt>null</tt>.
     */
    public static AuthLevel get ( int c ) {
        return lookup.get( c );
    }
}
