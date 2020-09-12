package comm.constants;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum OcsDirection {
    /**
     * No direction.
     */
    NO( ( byte )0 ),

    /**
     * Up direction.
     */
    UP( ( byte )1 ),

    /**
     * Down direction.
     */
    DOWN( ( byte )2 );
    
    /**
     * Lookup table.
     */
    private static final Map<Byte, OcsDirection> LOOKUP = new HashMap<Byte, OcsDirection>();
    static {
        for ( OcsDirection s : EnumSet.allOf( OcsDirection.class ) )
            LOOKUP.put( s.getCode(), s );
    }


    /**
     * Enumeration value.
     */
    private final byte code;




    /**
     * Available OCS direction.
     * @param c Constant value of enumeration.
     */
    private OcsDirection ( byte c ) {
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
    public static OcsDirection get ( byte code ) {
        return LOOKUP.get( code );
    }
    
    
    public String toString () {
        return Message.getString( "OCSDirection." + name() );
    }
}
