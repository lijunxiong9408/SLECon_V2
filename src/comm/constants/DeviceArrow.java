package comm.constants;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum DeviceArrow {
    /**
     * No arrow.
     */
    NONE( ( byte )0 ),

    /**
     * Green up arrow.
     */
    GREEN_UP( ( byte )1 ),

    /**
     * Green up arrow with animation.
     */
    GREEN_UP_ANIMATION( ( byte )2 ),

    /**
     * Red down arrow.
     */
    RED_DOWN( ( byte )3 ),

    /**
     * Red down arrow with animation.
     */
    RED_DOWN_ANIMATION( ( byte )4 ),

    /**
     * No arrow and dim the screen.
     */
    NONE_DIM( ( byte )5 );

    /**
     * Lookup table.
     */
    private static final Map<Byte, DeviceArrow> LOOKUP = new HashMap<Byte, DeviceArrow>();
    static {
        for ( DeviceArrow s : EnumSet.allOf( DeviceArrow.class ) )
            LOOKUP.put( s.getCode(), s );
    }

    /**
     * Enumeration value.
     */
    private final byte code;




    /**
     * Available arrow in device.
     * @param c Constant value of enumeration.
     */
    private DeviceArrow ( byte c ) {
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
    public static DeviceArrow get ( byte code ) {
        return LOOKUP.get( code );
    }
    
    
    public String toString () {
        return Message.getString( "DeviceArrow." + name() );
    }
}
