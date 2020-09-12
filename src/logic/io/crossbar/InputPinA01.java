package logic.io.crossbar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import slecon.ToolBox;
import slecon.ToolBox.AVersion;




public enum InputPinA01 {
    C1_7("1C.7", 0x17, false, ""),
    C1_8("1C.8", 0x14, false, ""),
    C1_9("1C.9", 0x13, false, ""),
    C1_10("1C.10", 0x10, false, ""),
    ERO_6("ERO.6", 0x15, false, ""),
    ERO_5("ERO.5", 0x12, false, ""),
    ERO_4("ERO.4", 0x11, false, ""),
    ERO_9("ERO.4", 0x16, false, ""),
    BR_2("BR.2", 0x0A, false, ""),
    KM_4("KM.4", 0x05, false, ""),
    M2_2("2M.2", 0x0C, false, ""),
    M2_3("2M.3", 0x0E, false, ""),
    M2_4("2M.4", 0x09, false, ""),
    M2_5("2M.5", 0x0B, false, ""),
    M2_6("2M.6", 0x0D, false, ""),
    M2_7("2M.7", 0x0F, false, ""),
    M2_8("2M.8", 0x04, false, ""),
    M2_9("2M.9", 0x02, false, ""),
    M2_10("2M.10", 0x00, false, ""),
    M2_11("2M.11", 0x63, false, ""),
    M2_12("2M.9", 0x62, false, ""),
    DRV_12("DRV.12", 0x01, false, ""),
    DRV_13("DRV.13", 0x03, false, ""),
    C2_2("2C.2", 0x18, false, ""),
    H2_3("2H.3", 0x19, false, ""),
    H2_4("2H.4", 0x1A, false, ""),
    C2_5("2C.5", 0x1B, false, ""),
    C2_6("2C.6", 0x1C, false, ""),
    H2_7("2H.7", 0x1D, false, ""),
    M3_3("3M.3", 0x08, false, ""),
    FDBP("FDBP", 0x06, false, ""),
    RDBP("RDBP", 0x07, false, ""),
    VS_1("VS.1", 0x68, false, ""),
    VS_2("VS.2", 0x64, false, ""),
    VS_3("VS.3", 0x65, false, ""),
    VS_4("VS.4", 0x66, false, ""),
    VS_5("VS.5", 0x67, false, ""),
    VS_6("VS.6", 0x60, false, ""),
    VS_7("VS.7", 0x61, false, ""),
    SC_3("SC.3", 0x6A, false, ""),
    SC_4("SC.4", 0x6B, false, ""),
    SC_5("SC.5", 0x6E, false, ""),
    Always_0("Always 0", 0x70, false, ""),
    Always_1("Always 1", 0x71, false, ""),
    //LVCFB("LVCFB", 0x3f, false, ""),
    //ENCF("ENCF", 0x49, false, ""),
    ADO("ADO", 0x71, false, ""),
    //V24MON("V24MON", 0x2e, false, ""),
    //IOWD("IOWD", 0x6e, false, ""),
    DCS_REDP("DCS.REDP", 0x20, false, ""),
    DCS_EDP("DCS.EDP", 0x21, false, ""),
    DCS_RDOL("DCS.RDOL", 0x22, false, ""),
    DCS_RDCL("DCS.RDCL", 0x23, false, ""),
    DCS_RSGS("DCS.RSGS", 0x24, false, ""),
    DCS_INS("DCS.INS", 0x25, false, ""),
    DCS_DOL("DCS.DOL", 0x26, false, ""),
    DCS_DCL("DCS.DCL", 0x27, false, ""),
    DCS_SGS("DCS.SGS", 0x28, false, ""),
    DCS_NDZ("DCS.NDZ", 0x29, false, ""),
    DCS_INS_UP("DCS.INS_UP", 0x2a, false, ""),
    DCS_INS_DOWN("DCS.INS_DOWN", 0x2b, false, ""),
    DCS_LDZ("DCS.LDZ", 0x2c, false, ""),
    DCS_UDZ("DCS.UDZ", 0x2d, false, ""),
    DCS_LRL("DCS.LRL", 0x2e, false, ""),
    DCS_URL("DCS.URL", 0x2F, false, ""),
    DCS_DOR("DCS.DOR", 0x30, false, ""),
    DCS_DCR("DCS.DCR", 0x31, false, ""),
    DCS_FR("DCS.FR", 0x32, false, ""),
    DCS_LR("DCS.LR", 0x33, false, ""),
	DCS_NDR("DCS.NDR", 0x34, false, ""),
	DCS_RDOR("DCS.RDOR", 0x35, false, ""),
	DCS_RDCR("DCS.RDCR", 0x36, false, ""),
	DCS_RNDR("DCS.RNDR", 0x37, false, ""),
	M1_12("1M.12", 0x69, false, ""),
	KM_8("KM.8", 0x50, false, ""),
	KM_9("KM.9", 0x51, false, ""),
	KM_10("KM.10", 0x52, false, ""),
	BR_3("BR.3", 0x54, false, ""),
	BR_5("BR.5", 0x55, false, "");
	

    private static final HashMap<Integer, InputPinA01> rawIOPosMap = new HashMap<Integer, InputPinA01>();
    private static final InputPinA01[] displayItem;



    static {
        List<InputPinA01> list = new ArrayList<>();
        for ( InputPinA01 is : values() ) {
            if(ToolBox.isDebugMode() || !is.debug) {
                rawIOPosMap.put( is.rawIOPos, is );
                list.add(is);
            }
        }
        displayItem = list.toArray(new InputPinA01[0]);
    }
    
    
    public final String text;
    public final int     rawIOPos;
    public final boolean debug;
    public final String  firmwareVersion;




    InputPinA01 ( String text, int rawIOPos, boolean debug, String firmwareVersion ) {
        this.text     = text;
        this.rawIOPos = rawIOPos;
        this.debug    = debug;
        this.firmwareVersion = firmwareVersion;
    }


    public static InputPinA01[] allDisplayItem() {
        return Arrays.copyOf(displayItem, displayItem.length);
    }

    
    public static InputPinA01[] allDisplayItem(String mcsFirmwareVersion) {
        List<InputPinA01> result = new ArrayList<>();
        AVersion v = ToolBox.parserVersion(mcsFirmwareVersion);
        for (InputPinA01 pin : displayItem) {
            if (v.compareTo(ToolBox.parserVersion(pin.firmwareVersion)) >= 0)
                result.add(pin);
        }
        return result.toArray(new InputPinA01[0]);
    }


    public String toString () {
        return String.format( "%s (0x%X)", text, rawIOPos );
    }


    public static InputPinA01 getByRawIOPosition ( int rawIOPos ) {
        return rawIOPosMap.get( rawIOPos );
    }
}
