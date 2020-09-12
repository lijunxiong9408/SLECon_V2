package comm.constants;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;



public enum DeviceMessage {
    /**
     * No message.
     */
    NONE( ( byte )0 ),

    /**
     * Lift alarm.
     */
    LIFT_ALARM( ( byte )1 ),

    /**
     * Overload.
     */
    OVERLOAD( ( byte )2 ),

    /**
     * Fire.
     */
    FIRE( ( byte )3 ),

    /**
     * Fireman service.
     */
    FIREMAN_SERVICE( ( byte )4 ),

    /**
     * Out of service.
     */
    OUT_OF_SERVICE( ( byte )5 ),

    /**
     * Routine maintenance.
     */
    ROUTINE_MAINTENANCE( ( byte )6 ),

    /**
     * Park.
     */
    PARK( ( byte )7 ),

    /**
     * Cleaning.
     */
    CLEANING( ( byte )8 ),
    
    /**
     * Vip Special.
     */
    VIP_SPECIAL( ( byte )9 ),
	
	/**
     * Earthquake Occurs.
     */
    EARTHQUAKE_OCCURS_CAR( ( byte )10 ),
    
    /**
     * Outbreak of Fire .
     */
    OUTBREAK_FIRE_CAR( ( byte )11 ),
    
    /**
     * Power Outage.
     */
    POWER_OUTAGE_CAR( ( byte )12 ),
    
    /**
     * Pause.
     */
    PAUSE_CAR( ( byte )13 ),
    
    /**
     * OverLoad2.
     */
    OVERLOAD_CAR( ( byte )14 ),
    
	/**
     * Earthquake Occurs.
     */
    EARTHQUAKE_OCCURS_HALL( ( byte )15 ),
    
    /**
     * Outbreak of Fire .
     */
    OUTBREAK_FIRE_HALL( ( byte )16 ),
    
    /**
     * Power Outage.
     */
    POWER_OUTAGE_HALL( ( byte )17 ),
    
    /**
     * Routine maintenance.
     */
    ROUTINE_MAINTENANCE_HALL( ( byte )18 ),
    
    /**
     * Pause.
     */
    PAUSE_HALL( ( byte )19 ),
    
    /**
     * OverLoad2.
     */
    OVERLOAD_HALL( ( byte )20 ),
    
    /**
     * Vip Special.
     */
    VIP_SPECIAL_HALL( ( byte )21 );
	

    /**
     * Lookup table.
     */
    private static final Map<Byte, DeviceMessage> LOOKUP = new HashMap<Byte, DeviceMessage>();
    static {
        for ( DeviceMessage s : EnumSet.allOf( DeviceMessage.class ) )
            LOOKUP.put( s.getCode(), s );
    }

    /**
     * Enumeration value.
     */
    private final byte code;
    



    /**
     * Available message in device.
     * @param c Constant value of enumeration.
     */
    private DeviceMessage ( byte c ) {
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
    public static DeviceMessage get ( byte code ) {
        return LOOKUP.get( code );
    }
    
    
    public String toString () {
        return Message.getString( "DeviceMessage." + name() );
    }
}
