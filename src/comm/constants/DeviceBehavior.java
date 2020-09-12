package comm.constants;
import java.util.TreeMap;



public enum DeviceBehavior {
    NORMAL( ( byte )0 ),
    
    SYNC ( ( byte )1 ),
    
    FLASH( ( byte )2 ),
    
    FAST_FLASH( ( byte )3 ),
    
    BLINK( ( byte )4 ),
    
    FAST_BLINK( ( byte )5 );
    
    private final static TreeMap<Byte, DeviceBehavior> deviceTypes = new TreeMap<>();




    static {
        for ( DeviceBehavior type : DeviceBehavior.values() )
            deviceTypes.put( (byte) type.id, type );
    }


    public final int id;




    DeviceBehavior ( byte id ) {
        this.id      = id;
    }


    public static DeviceBehavior parse ( byte code ) {
        return deviceTypes.get( code );
    }
}
