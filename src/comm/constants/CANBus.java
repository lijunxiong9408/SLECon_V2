package comm.constants;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;



public enum CANBus {
    /**
     * Car CAN.
     */
    CAR( ( short )0x0100 ),

    /**
     * Hall CAN.
     */
    HALL( ( short )0x0200 );

    /**
     * Lookup table.
     */
    private static final Map<Short, CANBus> LOOKUP = new HashMap<>();
    static {
        for ( CANBus s : EnumSet.allOf( CANBus.class ) )
            LOOKUP.put( s.getCode(), s );
    }

    /**
     * Enumeration value.
     */
    private short code;




    /**
     * Available CAN bus.
     * @param c Constant value of enumeration.
     */
    private CANBus ( short c ) {
        this.code = c;
    }


    /**
     * Get the constant value of enumeration.
     * @return Returns the constant value of enumeration.
     */
    public short getCode () {
        return this.code;
    }


    /**
     * Get an instance of enumeration by the constant value.
     * @param code  It specifies the constant value of enumeration.
     * @return Returns an instance of enumeration on success; otherwise, returns {@code null}.
     */
    public static CANBus get ( short code ) {
        return LOOKUP.get( code );
    }
}
