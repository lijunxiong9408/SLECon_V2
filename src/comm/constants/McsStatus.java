package comm.constants;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum McsStatus {
    /**
     * System Passive.
     */
    PASV( ( byte )0 ),

    /**
     * Installation Mode.
     */
    INST( ( byte )1 ),

    /**
     * Power Down.
     */
    PWDN( ( byte )2 ),

    /**
     * Idle.
     */
    IDLE( ( byte )3 ),

    /**
     * Temporary driver activation mode.
     */
    TDA( ( byte )4 ),

    /**
     * Suspend Door Automation mode.
     */
    SDA( ( byte )5 ),

    /**
     * Normal.
     */
    NORMAL( ( byte )6 );

    /**
     * Lookup table.
     */
    private static final Map<Byte, McsStatus> LOOKUP = new HashMap<Byte, McsStatus>();
    static {
        for ( McsStatus s : EnumSet.allOf( McsStatus.class ) )
            LOOKUP.put( s.getCode(), s );
    }

    /**
     * Enumeration value.
     */
    private final byte code;




    /**
     * Available MCS status.
     * @param c Constant value of enumeration.
     */
    private McsStatus ( byte c ) {
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
    public static McsStatus get ( byte code ) {
        return LOOKUP.get( code );
    }
    
    
    public String toString () {
        return Message.getString( "MCSStatus." + name() );
    }
}
