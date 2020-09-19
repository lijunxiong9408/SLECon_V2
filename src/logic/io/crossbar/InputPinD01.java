package logic.io.crossbar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import slecon.ToolBox;
import slecon.ToolBox.AVersion;

public enum InputPinD01 {
    C1_7("1C.7", 0x2c, false, ""),
    C1_8("1C.8", 0x2d, false, ""),
    C1_9("1C.9", 0x2e, false, ""),
    C1_10("1C.10", 0x2f, false, ""),
    ERO_6("ERO.6", 0x29, false, ""),
    ERO_5("ERO.5", 0x2a, false, ""),
    ERO_4("ERO.4", 0x2b, false, ""),
    ERO_9("ERO.9", 0x28, false, ""),
    BR_2("BR.2", 0x39, false, ""),
    KM_4("KM.4", 0x3e, false, ""),
    M2_2("2M.2", 0x37, false, ""),
    M2_3("2M.3", 0x3c, false, ""),
    M2_4("2M.4", 0x3a, false, ""),
    M2_5("2M.5", 0x35, false, ""),
    M2_6("2M.6", 0x38, false, ""),
    M2_7("2M.7", 0x36, false, ""),
    M2_8("2M.8", 0x33, false, ""),
    M2_9("2M.9", 0x34, false, ""),
    M2_10("2M.10", 0x32, false, ""),
    VS_2("VS.2", 0x6D, false, ""),
    VS_3("VS.3", 0x6E, false, ""),
    VS_7("VS.7", 0x6F, false, ""),
    VS_9("VS.9", 0x31, false, ""),
    VS10("VS.10", 0x30, false, ""),
    H2_3("2H.3", 0x25, false, ""),
    H2_4("2H.4", 0x26, false, ""),
    C2_2("2C.2", 0x24, false, ""),
    C2_5("2C.5", 0x27, false, ""),
    C2_6("2C.6", 0x21, false, ""),
    H2_7("2H.7", 0x20, false, ""),
    M3_3("3M.3", 0x3b, false, ""),
    FDBP("FDBP", 0x3d, false, ""),
    RDBP("RDBP", 0x3f, false, ""),
    KM_1("KM.8", 0x68, false, ""),
    KM_8("KM.8", 0x66, false, ""),
	KM_9("KM.9", 0x6C, false, ""),
	KM_10("KM.10", 0x6B, false, ""),
	M1_12("1M.12", 0x67, false, ""),
	BR_4("BR.4", 0x69, false, ""),
    Always_0("Always 0", 0x70, false, ""),
    Always_1("Always 1", 0x71, false, ""),
    ENCF("ENCF", 0x23, false, ""),
    /*-------------------------------------*/
    DCS_REDP("DCS.REDP", 0x00, false, ""),
    DCS_EDP("DCS.EDP", 0x01, false, ""),
    DCS_RDOL("DCS.RDOL", 0x02, false, ""),
    DCS_RDCL("DCS.RDCL", 0x03, false, ""),
    DCS_RSGS("DCS.RSGS", 0x04, false, ""),
    DCS_INS("DCS.INS", 0x05, false, ""),
    DCS_DOL("DCS.DOL", 0x06, false, ""),
    DCS_DCL("DCS.DCL", 0x07, false, ""),
    DCS_SGS("DCS.SGS", 0x08, false, ""),
    DCS_NDZ("DCS.NDZ", 0x09, false, ""),
    DCS_INS_UP("DCS.INS_UP", 0x0a, false, ""),
    DCS_INS_DOWN("DCS.INS_DOWN", 0x0b, false, ""),
    DCS_CINS("DCS.CINS", 0x0c, false, ""),
    DCS_CINS_UP("DCS.CINS_UP", 0x0d, false, ""),
    DCS_CINS_DOWN("DCS.CINS_DOWN", 0x0e, false, ""),
    DCS_TSL("DCS.TSL", 0x0f, false, ""),
    DCS_FLDZ("DCS.FLDZ", 0x10, false, ""),
    DCS_FUDZ("DCS.FUDZ", 0x11, false, ""),
    DCS_RLDZ("DCS.RLDZ", 0x12, false, ""),
    DCS_RUDZ("DCS.RUDZ", 0x13, false, ""),
    DCS_DRL("DCS.DRL", 0x14, false, ""),
    DCS_URL("DCS.URL", 0x15, false, ""),
    DCS_LSL("DCS.LSL", 0x16, false, ""),
    DCS_USL("DCS.USL", 0x17, false, ""),
    DCS_DOR("DCS.DOR", 0x40, false, ""),
    DCS_DCR("DCS.DCR", 0x41, false, ""),
    DCS_FR("DCS.FR", 0x42, false, ""),
    DCS_LR("DCS.LR", 0x43, false, ""),
    DCS_NDGR("DCS.NDGR", 0x44, false, ""),
    DCS_RDOR("DCS.RDOR", 0x45, false, ""),
    DCS_RDCR("DCS.RDCR", 0x46, false, ""),
    DCS_RNDGR("DCS.RNDGR", 0x47, false, ""),
    DCS_TD1("DCS.TD1", 0x48, false, ""),
    DCS_TD2("DCS.TD2", 0x49, false, ""),
    DCS_TD3("DCS.TD3", 0x4a, false, ""),
    DCS_AR("DCS.AR", 0x4b, false, ""),
    /*---------------------------------*/
    EPS_RBR("EPS_RBR", 0x50, false, ""),
    EPS_MCK("EPS_MCK", 0x51, false, ""),
    EPS_DZI("EPS_DZI", 0x52, false, ""),
    EPS_ECF("EPS_ECF", 0x53, false, ""),
    EPS_EBK("EPS_EBK", 0x54, false, ""),
    EPS_IPS("EPS_IPS", 0x58, false, ""),
    EPS_MBR("EPS_MBR", 0x59, false, ""),
    EPS_MCR("EPS_MCR", 0x5a, false, ""),
    EPS_XMBR("EPS_XMBR", 0x5b, false, ""),
    EPS_BAT("EPS_BAT", 0x5c, false, ""),
    EPS_RCR("EPS_RCR", 0x5d, false, ""),
    
    DCS_NULL("DCS.NULL", 0xFF, false, "");
	

    private static final HashMap<Integer, InputPinD01> rawIOPosMap = new HashMap<Integer, InputPinD01>();
    private static final InputPinD01[] displayItem;



    static {
        List<InputPinD01> list = new ArrayList<>();
        for ( InputPinD01 is : values() ) {
            if(ToolBox.isDebugMode() || !is.debug) {
                rawIOPosMap.put( is.rawIOPos, is );
                list.add(is);
            }
        }
        displayItem = list.toArray(new InputPinD01[0]);
    }
    
    
    public final String text;
    public final int     rawIOPos;
    public final boolean debug;
    public final String  firmwareVersion;




    InputPinD01 ( String text, int rawIOPos, boolean debug, String firmwareVersion ) {
        this.text     = text;
        this.rawIOPos = rawIOPos;
        this.debug    = debug;
        this.firmwareVersion = firmwareVersion;
    }


    public static InputPinD01[] allDisplayItem() {
        return Arrays.copyOf(displayItem, displayItem.length);
    }

    
    public static InputPinD01[] allDisplayItem(String mcsFirmwareVersion) {
        List<InputPinD01> result = new ArrayList<>();
        AVersion v = ToolBox.parserVersion(mcsFirmwareVersion);
        for (InputPinD01 pin : displayItem) {
            if (v.compareTo(ToolBox.parserVersion(pin.firmwareVersion)) >= 0)
                result.add(pin);
        }
        return result.toArray(new InputPinD01[0]);
    }


    public String toString () {
        return String.format( "%s (0x%X)", text, rawIOPos );
    }


    public static InputPinD01 getByRawIOPosition ( int rawIOPos ) {
        return rawIOPosMap.get( rawIOPos );
    }
}
