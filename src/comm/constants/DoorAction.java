package comm.constants;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum DoorAction {
    /**
     * Undefined (No) operation.
     */
    NOP( ( byte )0 ),

    /**
     * Door is opening.
     */
    OPENING( ( byte )1 ),

    /**
     * Door is opened.
     */
    OPENED( ( byte )2 ),

    /**
     * Door is closing.
     */
    CLOSING( ( byte )3 ),

    /**
     * Door is closed.
     */
    CLOSED( ( byte )4 ),

    /**
     * SGS or EDP is triggered.
     * This action is translated by slecon rather than raw status of DCS.
     */
    SGS( ( byte )5 ),
	
	/**
	 *  Forced close door.
	 * */
	FORCED_CLOSE( (byte)6 ),
	
	/**
	 * Door system passive.
	 */
	PASSIVE( (byte)7 );

    /**
     * Lookup table.
     */
    private static final Map<Byte, DoorAction> LOOKUP = new HashMap<Byte, DoorAction>();
    static {
        for ( DoorAction s : EnumSet.allOf( DoorAction.class ) )
            LOOKUP.put( s.getCode(), s );
    }

    /**
     * Enumeration value.
     */
    private final byte code;




    /**
     * Available door action.
     * @param c Constant value of enumeration.
     */
    private DoorAction ( byte c ) {
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
    public static DoorAction get ( byte code ) {
        return LOOKUP.get( code );
    }
    
    
    public String toString () {
        return Message.getString( "DoorAction." + name() );
    }
}
