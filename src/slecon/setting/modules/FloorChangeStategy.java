package slecon.setting.modules;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import slecon.ToolBox;
public class FloorChangeStategy {
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
	
	public enum Floor_Change_Stategy {
        
    	VTECH_MODE( (byte)0 ),
        
    	JES_MODE( (byte)1 );
    	
    	private final byte code;
    	
    	private Floor_Change_Stategy(byte c) {
    		this.code = c;
    	}
    	
    	public byte getCode () {
            return this.code;
        }
    	
    	private static final Map<Byte, Floor_Change_Stategy> LOOKUP = new HashMap<Byte, Floor_Change_Stategy>();
        static {
            for ( Floor_Change_Stategy s : EnumSet.allOf( Floor_Change_Stategy.class ) )
                LOOKUP.put( s.getCode(), s );
        }
    	
        public static Floor_Change_Stategy get ( byte code ) {
	        return LOOKUP.get( code );
	    }
      
        public String toString() {
            return getBundleText("LBL_"+name(),name());
        }
    }
}
