package logic.evlog;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import slecon.ToolBox;




public enum OCSErrorCode {
//    // OCS related.
//    OCS_BOOTUP( 0x01, Level.CRITICAL, "OCS_BOOTUP.text" ),

    // DCS related.
    DCS_UNKNOWN_FAIL( 0x0000, Level.CRITICAL, "DCS_UNKNOWN_FAIL.text" ), 
    DW_FAIL( 0x0001, Level.CRITICAL, "DW_FAIL.text" ), 
    DFC_FAIL( 0x0002, Level.CRITICAL, "DFC_FAIL.text" ),
    SAFETY_CHAIN_ORDER_FAIL( 0x0005, Level.CRITICAL, "SAFETY_CHAIN_ORDER_FAIL.text" ), 
    CES_FAIL( 0x0003, Level.CRITICAL, "CES_FAIL.text" ),
    HES_FAIL( 0x0004, Level.CRITICAL, "HES_FAIL.text" ),
    DCS_FAIL_OPEN( 0x0006, Level.CRITICAL, "DCS_FAIL_OPEN.text" ),
    DCS_FAIL_CLOSE( 0x0007, Level.CRITICAL, "DCS_FAIL_CLOSE.text" ),

    // Module related.
    OCS_MODULE_ATT_LOAD( 0x0107, Level.GENERAL, "OCS_MODULE_ATT_LOAD.text" ),
    OCS_MODULE_ATT_UNLOAD( 0x0207, Level.GENERAL, "OCS_MODULE_ATT_UNLOAD.text" ),
    OCS_MODULE_INSP_LOAD( 0x0101, Level.GENERAL, "OCS_MODULE_INSP_LOAD.text" ),
    OCS_MODULE_INSP_UNLOAD( 0x0201, Level.GENERAL, "OCS_MODULE_INSP_UNLOAD.text" ),
    OCS_MODULE_ISC_LOAD( 0x0104, Level.GENERAL, "OCS_MODULE_ISC_LOAD.text" ),
    OCS_MODULE_ISC_UNLOAD( 0x0204, Level.GENERAL, "OCS_MODULE_ISC_UNLOAD.text" ),
	OCS_MODULE_FEO_UNLOAD_FAIL( 0x020A, Level.GENERAL, "OCS_MODULE_FEO_UNLOAD_FAIL.text" );

    private static final Map<Integer, OCSErrorCode> codes = new TreeMap<>();
    
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "OCSErrorCode" );


    static {
        for ( OCSErrorCode code : OCSErrorCode.values() )
            codes.put( code.getCode(), code );
    }


    private final int    code;
    private final Level  level;
    private final String description;




    private OCSErrorCode ( Number code, Level level, String description ) {
        final ResourceBundle TEXT = ToolBox.getResourceBundle( "OCSErrorCode" );
        this.code        = code.intValue();
        this.level       = level;
        this.description = TEXT.getString( description );
    }


    public String getDescription () {
        return this.description;
    }


    public Level getLevel () {
        return this.level;
    }


    /**
     * Get the constant value of enumeration.
     * @return Returns the constant value of enumeration.
     */
    public int getCode () {
        return this.code;
    }


    public static OCSErrorCode get ( Number code ) {
        return codes.get( code.intValue() );
    }
}
