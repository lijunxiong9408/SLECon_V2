package slecon.setting.modules;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import slecon.ToolBox;
public class LedBehavior {
	private static ResourceBundle	TEXT = ToolBox.getResourceBundle( "setting.SettingPanel" );
	
	public static String getBundleText ( String key, String defaultValue ) {
        String result;
        try {
            result = TEXT.getString( key );
        } catch ( Exception e ) {
            result = defaultValue;
        }
        return result;
    }
	
	public enum Led_Behavior {
        
    	ALWAYS_ON( (byte)0 ),
        
        FLASH( (byte)1 );
    	
    	private final byte code;
    	
    	private Led_Behavior(byte c) {
    		this.code = c;
    	}
    	
    	public byte getCode () {
            return this.code;
        }
    	
    	private static final Map<Byte, Led_Behavior> LOOKUP = new HashMap<Byte, Led_Behavior>();
        static {
            for ( Led_Behavior s : EnumSet.allOf( Led_Behavior.class ) )
                LOOKUP.put( s.getCode(), s );
        }
    	
        public static Led_Behavior get ( byte code ) {
	        return LOOKUP.get( code );
	    }
      
        public String toString() {
            return getBundleText("LBL_"+name(),name());
        }
    }
}
