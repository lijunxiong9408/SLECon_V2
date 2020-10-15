package slecon.setting.modules;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import slecon.ToolBox;
public class LockStrategy {
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
	
	public enum Lock_Strategy {
        
		CAR_LOCK( (byte)0 ),
        
		HALL_LOCK( (byte)1 ),
		
		ALL_LOCK( (byte)2 );
    	
    	private final byte code;
    	
    	private Lock_Strategy(byte c) {
    		this.code = c;
    	}
    	
    	public byte getCode () {
            return this.code;
        }
    	
    	private static final Map<Byte, Lock_Strategy> LOOKUP = new HashMap<Byte, Lock_Strategy>();
        static {
            for ( Lock_Strategy s : EnumSet.allOf( Lock_Strategy.class ) )
                LOOKUP.put( s.getCode(), s );
        }
    	
        public static Lock_Strategy get ( byte code ) {
	        return LOOKUP.get( code );
	    }
      
        public String toString() {
            return getBundleText(name(),name());
        }
    }
}
