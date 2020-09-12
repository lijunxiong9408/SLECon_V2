package logic.io.crossbar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import slecon.ToolBox;
import slecon.ToolBox.AVersion;




public enum InputPinC01 {
    P1_1("1C.7", 0x30, false, ""),
    P1_2("1C.8", 0x2d, false, ""),
    P1_5("1C.10", 0x31, false, ""),
    P1_6("1C.9", 0x32, false, ""),
    P1_7("ERO.6", 0x33, false, ""),
    P1_9("ERO.5", 0x3c, false, ""),
    P1_10("ERO.4", 0x3d, false, ""),
    P2_1("K3M.2", 0x34, false, ""),
    P2_2("XBR.14", 0x35, false, ""),
    P2_4("K3M.4", 0x36, false, ""),
    P2_5("2M.2", 0x40, false, ""),
    P2_6("2M.3", 0x41, false, ""),
    P2_7("2M.4", 0x42, false, ""),
    P2_9("2M.5", 0x43, false, ""),
    P2_10("2M.6", 0x44, false, ""),
    P3_1("2C.5", 0x0f, false, ""),
    P3_2("2H.4", 0x38, false, ""),
    P3_3("2H.3", 0x39, false, ""),
    P3_4("2C.2", 0x3a, false, ""),
    P10_13("DRV.13", 0x1f, false, ""),
    P10_14("DRV.14", 0x3e, false, ""),
    P10_8("DRV.8", 0x1e, false, ""),
    RL1("K2M.6", 0x61, false, ""),
    RL2("K3M.6", 0x62, false, ""),
    RL3("GOR", 0x63, false, ""),
    RL4("BR", 0x67, false, ""),
    RL5("ABR", 0x66, false, ""),
    RL6("SCR", 0x65, false, ""),
    RL10("CCF.1", 0x64, false, ""),
    LVCR("LVCR", 0x60, false, ""),
    P10_1("DRV.1", 0x6f, false, ""),
    P10_2("DRV.2", 0x6b, false, ""),
    P10_3("DRV.3", 0x6a, false, ""),
    P10_4("DRV.4", 0x69, false, ""),
    P10_5("DRV.5", 0x68, false, ""),
    P10_6("DRV.6", 0x6d, false, ""),
    P10_7("K1", 0x6c, false, ""),
    Always_0("Always 0", 0x70, false, ""),
    Always_1("Always 1", 0x71, false, ""),
    LVCFB("LVCFB", 0x3f, false, ""),
    ENCF("ENCF", 0x49, false, ""),
    ADO("ADO", 0x71, false, ""),
    V24MON("V24MON", 0x2e, false, ""),
    IOWD("IOWD", 0x6e, false, ""),
    DCS_REDP("DCS.REDP", 0x00, false, ""),
    DCS_EDP("DCS.EDP", 0x01, false, ""),
    DCS_RDOL("DCS.RDOL", 0x02, false, ""),
    DCS_RDCL("DCS.RDCL", 0x03, false, ""),
    DCS_RSGS("DCS.RSGS", 0x04, false, ""),
    DCS_INS("DCS.INS", 0x05, false, ""),
    DCS_DOL("DCS.DOL", 0x06, false, ""),
    DCS_DCL("DCS.DCL", 0x07, false, ""),
    DCS_SGS("DCS.SGS", 0x08, false, ""),
    DCS_ADVBK("DCS.ADVBK", 0x09, false, ""),
    DCS_INS_UP("DCS.INS_UP", 0x0a, false, ""),
    DCS_INS_DOWN("DCS.INS_DOWN", 0x0b, false, ""),
    DCS_LDZ("DCS.LDZ", 0x0c, false, ""),
    DCS_UDZ("DCS.UDZ", 0x0d, false, ""),
    DCS_LRL("DCS.LRL", 0x0e, false, ""),
    DCS_URL("DCS.URL", 0x1C, false, ""),
    DCS_DOR("DCS.DOR", 0x10, false, ""),
    DCS_RDOR("DCS.RDOR", 0x11, false, ""),
    DCS_FR("DCS.FR", 0x12, false, ""),
    DCS_LR("DCS.LR", 0x13, false, "");


    private static final HashMap<Integer, InputPinC01> rawIOPosMap = new HashMap<Integer, InputPinC01>();
    private static final InputPinC01[] displayItem;



    static {
        List<InputPinC01> list = new ArrayList<>();
        for ( InputPinC01 is : values() ) {
            if(ToolBox.isDebugMode() || !is.debug) {
                rawIOPosMap.put( is.rawIOPos, is );
                list.add(is);
            }
        }
        displayItem = list.toArray(new InputPinC01[0]);
    }
    
    
    public final String text;
    public final int     rawIOPos;
    public final boolean debug;
    public final String  firmwareVersion;




    InputPinC01 ( String text, int rawIOPos, boolean debug, String firmwareVersion ) {
        this.text     = text;
        this.rawIOPos = rawIOPos;
        this.debug    = debug;
        this.firmwareVersion = firmwareVersion;
    }


    public static InputPinC01[] allDisplayItem() {
        return Arrays.copyOf(displayItem, displayItem.length);
    }

    
    public static InputPinC01[] allDisplayItem(String mcsFirmwareVersion) {
        List<InputPinC01> result = new ArrayList<>();
        AVersion v = ToolBox.parserVersion(mcsFirmwareVersion);
        for (InputPinC01 pin : displayItem) {
            if (v.compareTo(ToolBox.parserVersion(pin.firmwareVersion)) >= 0)
                result.add(pin);
        }
        return result.toArray(new InputPinC01[0]);
    }


    public String toString () {
        return String.format( "%s (0x%X)", text, rawIOPos );
    }


    public static InputPinC01 getByRawIOPosition ( int rawIOPos ) {
        return rawIOPosMap.get( rawIOPos );
    }
}
