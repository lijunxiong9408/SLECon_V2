package comm.constants;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;



public enum AuthLevel {
    
    /**
     * Default access role (Guest).
     * This level allows:
     * 1. Read operation in all pages of Home.
     * 2. Read operation in all pages of Inspect.
     * 3. Read operation in all pages of Setup (OCS).
     * 4. Read operation in all pages of Setup (MCS).
     * 5. Not allowed to access Setup (Setup->Management except Setup->Management->User management).
     * 6. Not allowed to access Setup (Setup->Management->User management).
     * 7. Not allowed to access developer functions.
     */
    LEVEL_0( 0, new Role[] {Role.READ_HOME, Role.READ_INSPECT}),

    /**
     * Level 1.
     * This level allows:
     * 1. Read/write operation in all pages of Home.
     * 2. Read/write operation in all pages of Inspect.
     */
    LEVEL_1( 1, new Role[] {Role.READ_HOME, Role.READ_INSPECT, Role.READ_OCS, Role.READ_MCS, 
            Role.WRITE_HOME, Role.WRITE_INSPECT} ),

    /**
     * Level 2.
     * This level allows:
     * 1. Read/write operation in all pages of Home.
     * 2. Read/write operation in all pages of Inspect.
     * 3. Read/write operation in all pages of Setup (OCS).
     */
    LEVEL_2( 2, new Role[] {Role.READ_HOME, Role.READ_INSPECT, Role.READ_OCS, Role.READ_MCS, 
            Role.WRITE_HOME, Role.WRITE_INSPECT, Role.WRITE_OCS} ),

    /**
     * Level 3.
     * This level allows:
     * 1. Read/write operation in all pages of Home.
     * 2. Read/write operation in all pages of Inspect.
     * 3. Read/write operation in all pages of Setup (OCS).
     * 4. Read/write operation in all pages of Setup (MCS).
     * 5. Read/write operation in all pages of Setup (Setup->Management->User management).
     * 6. Read/write operation in all pages of developer functions.
     */
    LEVEL_3( 3, new Role[] {Role.READ_HOME, Role.READ_INSPECT, Role.READ_OCS, Role.READ_MCS,
            Role.WRITE_HOME, Role.WRITE_INSPECT, Role.WRITE_OCS, Role.WRITE_MCS, 
            Role.READ_MANAGER, Role.WRITE_MANAGER} ),

    /**
     * Level 4.
     * This level allows:
     * 1. Read/write operation in all pages of Setup (Setup->Management except Setup->Management->User management).
     */
    LEVEL_4( 4, new Role[] {Role.READ_HOME, Role.READ_INSPECT, Role.READ_OCS, Role.READ_MCS,
            Role.WRITE_HOME, Role.WRITE_INSPECT, Role.WRITE_OCS, Role.WRITE_MCS, 
            Role.READ_MANAGER, Role.WRITE_MANAGER, Role.USER_MANAGER} ),

    /**
     * Level 5.
     * This level allows:
     * 1. Everything.
     */
    LEVEL_5( 5, Role.values() );

    /**
     * Lookup table.
     */
    private static final Map<Integer, AuthLevel> lookup = new HashMap<>( 6 );


    public static enum Role{
        READ_HOME, WRITE_HOME, 
        READ_INSPECT, WRITE_INSPECT, 
        READ_OCS, WRITE_OCS, 
        READ_MCS, WRITE_MCS, 
        READ_MANAGER, WRITE_MANAGER, 
        USER_MANAGER,
    }


    static {
        for ( AuthLevel s : EnumSet.allOf( AuthLevel.class ) )
            lookup.put( s.getCode(), s );
    }


    /**
     * The instance of enumeration value.
     */
    private final int code;
    
    
    public final Role[] roles;




    /**
     * Available roles in vecSystem.
     * @param c     It specifies the constant value of an enumeration.
     */
    private AuthLevel ( int c, Role[] roles ) {
        this.code = c;
        this.roles = roles;
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
