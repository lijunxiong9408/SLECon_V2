package comm.constants;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;




/**
 * Available disabled call definition.
 */
public enum DisCallDef {
    /**
     * Disable Car Call.
     */
    DISABLE_CAR( ( byte )1 ),

    /**
     * Disabled Front Hall Up Call.
     */
    DISABLE_FRONT_HALL_UP( ( byte )2 ),

    /**
     * Disabled Rear Hall Down Call.
     */
    DISABLE_FRONT_HALL_DOWN( ( byte )4 ),

    /**
     * Disabled Rear Hall Up Call.
     */
    DISABLE_REAR_HALL_UP( ( byte )8 ),
    
    /**
     * Disabled Rear Hall Down Call.
     */
    DISABLE_REAR_HALL_DOWN( ( byte )16 );

    /**
     * Lookup table.
     */
    private static final Map<Byte, DisCallDef> LOOKUP = new HashMap<Byte, DisCallDef>();
    static {
        for ( DisCallDef s : EnumSet.allOf( DisCallDef.class ) )
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
    private DisCallDef ( byte c ) {
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
    public static DisCallDef get ( byte code ) {
        return LOOKUP.get( code );
    }
}
