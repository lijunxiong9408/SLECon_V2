package logic;
import static logic.EventID.Direction.DOWN;
import static logic.EventID.Direction.NONE;
import static logic.EventID.Direction.UP;
import static logic.EventID.Door.FRONT;
import static logic.EventID.Door.REAR;
import static logic.EventID.Door.DISABLED;
import static logic.EventID.Type.CALL;
import static logic.EventID.Type.CONSTANT;
import static logic.EventID.Type.LIGHT;

import java.util.ResourceBundle;

import comm.constants.CANBus;
import slecon.ToolBox;




public enum EventID {
    CAR_FRONT_CALL( 0x1, 0x7F, CANBus.CAR, FRONT, NONE, CALL ), 
    CAR_REAR_CALL( 0x81, 0x7F, CANBus.CAR, REAR, NONE, CALL ),
    HALL_UP_FRONT_CALL( 0x101, 0x7F, CANBus.HALL, FRONT, UP, CALL ), 
    HALL_UP_FRONT_LIGHT( 0x181, 0x7F, CANBus.HALL, FRONT, UP, LIGHT ),
    HALL_UP_REAR_CALL( 0x201, 0x7F, CANBus.HALL, REAR, UP, CALL ), 
    HALL_UP_REAR_LIGHT( 0x281, 0x7F, CANBus.HALL, REAR, UP, LIGHT ),
    HALL_DOWN_FRONT_CALL( 0x301, 0x7F, CANBus.HALL, FRONT, DOWN, CALL ),
    HALL_DOWN_FRONT_LIGHT( 0x381, 0x7F, CANBus.HALL, FRONT, DOWN, LIGHT ),
    HALL_DOWN_REAR_CALL( 0x401, 0x7F, CANBus.HALL, REAR, DOWN, CALL ), 
    HALL_DOWN_REAR_LIGHT( 0x481, 0x7F, CANBus.HALL, REAR, DOWN, LIGHT ),
    FEO_FRONT( 1281, 0, null, FRONT, null, CONSTANT ),
    DOOR_OPEN_BUTTON_FRONT( 1283, 0, null, FRONT, null, CONSTANT ),
    DOOR_CLOSE_BUTTON_FRONT( 1285, 0, null, FRONT, null, CONSTANT ),
    BUZZER_FRONT( 1287, 0, null, FRONT, null, CONSTANT ), 
    ISC_FRONT( 1289, 0, null, FRONT, null, CONSTANT ),
    ATT_FRONT( 1291, 0, null, FRONT, null, CONSTANT ), 
    ATT_UP_FRONT( 1293, 0, null, FRONT, UP, CONSTANT ),
    ATT_DOWN_FRONT( 1295, 0, null, FRONT, DOWN, CONSTANT ),
    ATT_NONSTOP_FRONT( 1297, 0, null, FRONT, null, CONSTANT ), 
    FEO_REAR( 1282, 0, null, REAR, null, CONSTANT ),
    DOOR_OPEN_BUTTON_REAR( 1284, 0, null, REAR, null, CONSTANT ),
    DOOR_CLOSE_BUTTON_REAR( 1286, 0, null, REAR, null, CONSTANT ),
    BUZZER_REAR( 1288, 0, null, REAR, null, CONSTANT ), 
    ISC_REAR( 1290, 0, null, REAR, null, CONSTANT ),
    ATT_REAR( 1292, 0, null, REAR, null, CONSTANT ), 
    ATT_UP_REAR( 1294, 0, null, REAR, UP, CONSTANT ),
    ATT_DOWN_REAR( 1296, 0, null, REAR, DOWN, CONSTANT ),
    ATT_NONSTOP_REAR( 1298, 0, null, REAR, null, CONSTANT ), 
    ACCESS1( 1299, 0, null, null, null, CONSTANT ),
    ACCESS2( 1300, 0, null, null, null, CONSTANT ), 
    ACCESS3( 1301, 0, null, null, null, CONSTANT ),
    ACCESS4( 1302, 0, null, null, null, CONSTANT ), 
    FRO( 1303, 0, null, null, null, CONSTANT ),
    UP_INDICATOR( 1304, 0, null, null, null, CONSTANT ), 
    DOWN_INDICATOR( 1305, 0, null, null, null, CONSTANT ),
    DOOR_HOLD_BUTTON( 1306, 0, null, null, null, CONSTANT ), 
    PARKING_SWITCH( 1307, 0, null, null, null, CONSTANT ),
    EPO_SWITCH( 1308, 0, null, null, null, CONSTANT ),
    LIGHT_FLASH ( 1309, 0, null, null, null, CONSTANT ),
    LIGHT_FAST_FLASH ( 1310, 0, null, null, null, CONSTANT ),
    LIGHT_BLINK ( 1311, 0, null, null, null, CONSTANT ),
    LIGHT_FAST_BLINK ( 1312, 0, null, null, null, CONSTANT ),
    LIGHT_CUST1  ( 1313, 0, null, null, null, CONSTANT ),
    LIGHT_CUST2  ( 1314, 0, null, null, null, CONSTANT ),
	TDEO( 1315, 0, null, null, null, CONSTANT ),
	ACCESS5( 1316, 0, null, null, null, CONSTANT ), 
	ACCESS6( 1317, 0, null, null, null, CONSTANT ), 
	ACCESS7( 1318, 0, null, null, null, CONSTANT ),
	EQO_P_WARE( 1319, 0, null, null, null, CONSTANT ),
	EQO_LOW_WARE( 1320, 0, null, null, null, CONSTANT ),
	EQO_LOW_WARE_RESET( 1321, 0, null, null, null, CONSTANT ),
	EQO_LIGHT( 1322, 0, null, null, null, CONSTANT ),
	DISABLED_CAR_CALL( 1323, 0x7F, CANBus.CAR, DISABLED, NONE, CALL ),
	DISABLED_HALL_UP_FRONT_CALL( 1451, 0x7F, CANBus.HALL, DISABLED, UP, CALL ), 
	DISABLED_HALL_UP_REAR_CALL( 1579, 0x7F, CANBus.HALL, DISABLED, UP, CALL ), 
	DISABLED_HALL_DOWN_FRONT_CALL( 1707, 0x7F, CANBus.HALL, DISABLED, DOWN, CALL ),
	DISABLED_HALL_DOWN_REAR_CALL( 1835, 0x7F, CANBus.HALL, DISABLED, DOWN, CALL ),
	DOOR_OPEN_BUTTON_DISABLED( 1963, 0, null, DISABLED, null, CONSTANT ),
	DOOR_CLOSE_BUTTON_DISABLED( 1964, 0, null, DISABLED, null, CONSTANT ),
	ALL_STATION_PARKING_SWITCH( 1965, 0, null, null, null, CONSTANT );
	
	
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "logic.EventID" );
    public final int       eventID;
    public final int       size;
    public final CANBus       bus;
    public final Door      door;
    public final Direction direction;
    public final Type      type;




    enum Direction { NONE, UP, DOWN, }




    enum Door { FRONT, REAR, DISABLED; }




    enum Type { CALL, LIGHT, CONSTANT, }




    EventID ( int value, int size, CANBus bus, Door door, Direction direction, Type type ) {
        this.eventID   = value;
        this.size      = ( type == CONSTANT )
                         ? 0
                         : size;
        this.bus       = bus;
        this.door      = door;
        this.direction = direction;
        this.type      = type;
    }


    public static boolean isCar ( int id ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size && range.type != CONSTANT ) {
                return range.bus == CANBus.CAR;
            }
        }
        return false;
    }


    public static boolean isHall ( int id ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size && range.type != CONSTANT ) {
                return range.bus == CANBus.HALL;
            }
        }
        return false;
    }


    public static boolean isFront ( int id ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size && range.type != CONSTANT ) {
                return range.door == FRONT;
            }
        }
        return false;
    }


    public static boolean isRear ( int id ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size && range.type != CONSTANT ) {
                return range.door == REAR;
            }
        }
        return false;
    }


    public static boolean isUP ( int id ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size && range.type != CONSTANT ) {
                return range.direction == UP;
            }
        }
        return false;
    }


    public static boolean isDOWN ( int id ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size && range.type != CONSTANT ) {
                return range.direction == DOWN;
            }
        }
        return false;
    }


    public static boolean isCall ( int id ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size && range.type != CONSTANT ) {
                return range.type == CALL;
            }
        }
        return false;
    }


    public static boolean isLight ( int id ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size && range.type != CONSTANT ) {
                return range.type == LIGHT;
            }
        }
        return false;
    }


    public static Integer getOffset ( int id ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size && range.type == CONSTANT ) {
                return id - range.eventID;
            }
        }
        return null;
    }


    public static String getSimpleString ( int id ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size ) {
                int offset = id - range.eventID;
                return String.format( TEXT.getString( range.name() ), offset );
            }
        }
        return String.format( "Unknown ID (%d)", id );
    }

    
    public static int getFloor ( int id ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size ) {
                int offset = id - range.eventID;
                if ( range.type != CONSTANT && offset < 128 ) {
                    return offset;
                }
            }
        }
        return -1;
    }


    public static String getString ( int id, String[] floor ) {
        for ( EventID range : values() ) {
            if ( range.eventID <= id && id <= range.eventID + range.size ) {
                int offset = id - range.eventID;
                if ( range.type != CONSTANT && offset < floor.length && floor[ offset ] != null ) {
                    return String.format( TEXT.getString( range.name() ), floor[ offset ].trim() );
                } else {
                    return String.format( TEXT.getString( range.name() ), "XX" );
                }
            }
        }
        return String.format( "Unknown ID (%d)", id );
    }

    public static int getCarCallFrontID ( int floor ) {
        EventID ent = CAR_FRONT_CALL;
        return ent.eventID + floor;
    }


    public static int getCarCallRearID ( int floor ) {
        EventID ent = CAR_REAR_CALL;
        return ent.eventID + floor;
    }
    
    public static int getCarCallDisabledID ( int floor ) {
        EventID ent = DISABLED_CAR_CALL;
        return ent.eventID + floor;
    }


    public static int getHallUpCallFrontID ( int floor ) {
        EventID ent = HALL_UP_FRONT_CALL;
        return ent.eventID + floor;
    }


    public static int getHallDownCallFrontID ( int floor ) {
        EventID ent = HALL_DOWN_FRONT_CALL;
        return ent.eventID + floor;
    }


    public static int getHallUpCallRearID ( int floor ) {
        EventID ent = HALL_UP_REAR_CALL;
        return ent.eventID + floor;
    }


    public static int getHallDownCallRearID ( int floor ) {
        EventID ent = HALL_DOWN_REAR_CALL;
        return ent.eventID + floor;
    }
    
    public static int getHallUpCallFrontDisabledID ( int floor ) {
        EventID ent = DISABLED_HALL_UP_FRONT_CALL;
        return ent.eventID + floor;
    }


    public static int getHallDownCallFrontDisabledID ( int floor ) {
        EventID ent = DISABLED_HALL_DOWN_FRONT_CALL;
        return ent.eventID + floor;
    }


    public static int getHallUpCallRearDisabledID ( int floor ) {
        EventID ent = DISABLED_HALL_UP_REAR_CALL;
        return ent.eventID + floor;
    }


    public static int getHallDownCallRearDisabledID ( int floor ) {
        EventID ent = DISABLED_HALL_DOWN_REAR_CALL;
        return ent.eventID + floor;
    }


    public static int getHallUpArrivalLightFrontID ( int floor ) {
        EventID ent = HALL_UP_FRONT_LIGHT;
        return ent.eventID + floor;
    }


    public static int getHallDownArrivalLightFrontID ( int floor ) {
        EventID ent = HALL_DOWN_FRONT_LIGHT;
        return ent.eventID + floor;
    }


    public static int getHallUpArrivalLightRearID ( int floor ) {
        EventID ent = HALL_UP_REAR_LIGHT;
        return ent.eventID + floor;
    }


    public static int getHallDownArrivalLightRearID ( int floor ) {
        EventID ent = HALL_DOWN_REAR_LIGHT;
        return ent.eventID + floor;
    }
}
