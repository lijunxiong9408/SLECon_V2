package comm.constants;
import java.util.TreeMap;



public enum DeviceType {
    GP32( "GP32", 32 ),
    DM3A( "DM3A", 32 ),
    GP4G( "GP4G", 4 ),
    DM2A( "DM2A", 4 ),
    GP4_X( "GP4_X", 4),
    GP32M( "GP32M", 32),
    GP32S( "GP32S", 32),
    UNKNOWN( "UNKNOWN", 32 ),
    NA( "N/A", 0);

    private final static TreeMap<String, DeviceType> deviceTypes = new TreeMap<>();




    static {
        for ( DeviceType type : DeviceType.values() )
            deviceTypes.put( type.name, type );
    }


    public String name;
    public int ioCount;




    DeviceType ( String name, int ioCount ) {
        this.name    = name;
        this.ioCount = ioCount;
    }


    public static DeviceType parse ( String name ) {
        return deviceTypes.get( name );
    }
}
