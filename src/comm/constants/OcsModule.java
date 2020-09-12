package comm.constants;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum OcsModule {
    /**
     * Drive failure.
     */
    DRV_FAIL( ( byte )0 ),

    /**
     * Inspection.
     */
    INSP( ( byte )1 ),

    /**
     * System failure.
     */
    FAIL( ( byte )2 ),

    /**
     * Emergency stop.
     */
    ES( ( byte )3 ),

    /**
     * Independent service.
     */
    ISC( ( byte )4 ),

    /**
     * Normal.
     */
    NOR( ( byte )5 ),

    /**
     * Auto return operation.
     */
    ARO( ( byte )6 ),

    /**
     * Attendant service.
     */
    ATT( ( byte )7 ),
    
	/**
     * Temperature Detector Emergency Operation.
     */
    TDEO( ( byte )8 ),
	
    /**
     * Fire emergency return operation.
     */
    FRO( ( byte )9 ),

    /**
     * Fireman's emergency operation.
     */
    FEO( ( byte )10 ),

    /**
     * Parking.
     */
    PAK( ( byte )11 ),

    /**
     * Overload protection.
     */
    OLP( ( byte )12 ),

    /**
     * Emergency power operation.
     */
    EPO( ( byte )13 ),

    /**
     * Fault state group suspension.
     */
    NGP( ( byte )14 ),

    /**
     * OCS Lock up.
     */
    OLU( ( byte )15 ),

    /**
     * Rescue run.
     */
    RSCR( ( byte )16 ),
    
    /**
     * Reserved.
     */
    RESERVED_2( ( byte )17 ),
    
    /**
     * Correction run.
     */
    CORR( ( byte )18 ),

    /**
     * DCS fault operation.
     */
    DCS( ( byte )19 ),
	
	/**
     * DAF fault operation.
     */
    DAF( ( byte )20 ),
	
	/**
     * REL Running.
     */
    REL( ( byte )21 ),
    
    /**
     * EPB Running.
     */
    EPB( ( byte )22 ),
    
    /**
     * UCMT Running.
     */
    UCMT( ( byte )23 ),
    
    /**
     * EQO Running.
     */
    EQO( ( byte )24 ),
	
	/**
     * MCEX Running.
     */
    MCEX( ( byte )25 );
    /**
     * Lookup table.
     */
    private static final Map<Byte, OcsModule> LOOKUP = new HashMap<Byte, OcsModule>();
    static {
        for ( OcsModule s : EnumSet.allOf( OcsModule.class ) )
            LOOKUP.put( s.getCode(), s );
    }


    /**
     * Enumeration value.
     */
    private final byte code;




    /**
     * Available OCS module.
     * @param c Constant value of enumeration.
     */
    private OcsModule ( byte c ) {
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
    public static OcsModule get ( byte code ) {
        return LOOKUP.get( code );
    }
    
    
    public String toString () {
        return Message.getString( "OCSModule." + name() );
    }
}
