package comm.constants;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;



public enum CallDef {
    /**
     * Front Car Call.
     */
    FRONT_CAR( ( byte )1 ),

    /**
     * Rear Car Call.
     */
    REAR_CAR( ( byte )2 ),

    /**
     * Front Hall Up Call.
     */
    FRONT_HALL_UP( ( byte )4 ),

    /**
     * Front Hall Down Call.
     */
    FRONT_HALL_DOWN( ( byte )8 ),

    /**
     * Rear Hall Up Call.
     */
    REAR_HALL_UP( ( byte )16 ),

    /**
     * Rear Hall Down Call.
     */
    REAR_HALL_DOWN( ( byte )32 ),

    /**
     * Up Direction Light.
     */
    LIGHT_UP( ( byte )64 ),

    /**
     * Down Direction Light.
     */
    LIGHT_DOWN( ( byte )128 );

    /**
     * Lookup table.
     */
    private static final Map<Byte, CallDef> LOOKUP = new HashMap<Byte, CallDef>();
    static {
        for ( CallDef s : EnumSet.allOf( CallDef.class ) )
            LOOKUP.put( s.getCode(), s );
    }

    /**
     * Enumeration value.
     */
    private final byte code;




    /**
     * Available call definition.
     * @param c Constant value of enumeration.
     */
    private CallDef ( byte c ) {
        this.code = c;
    }


    /**
     * Get the constant value of enumeration.
     * @return Returns the constant value of enumeration.
     */
    public byte getCode () {
        return this.code;
    }


    /**
     * Get an instance of enumeration by the constant value.
     * @param code  It specifies the constant value of enumeration.
     * @return Returns an instance of enumeration on success; otherwise, returns {@code null}.
     */
    public static CallDef get ( byte code ) {
        return LOOKUP.get( code );
    }
}
