package comm;
import static logic.util.SiteManagement.MON_MGR;

import comm.agent.Device;
import comm.constants.CANBus;
import comm.constants.DeviceArrow;
import comm.constants.DeviceBehavior;
import comm.constants.DeviceMessage;
import comm.util.Endian;



public class Parser_Device {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * Device data.
     */
    private final Device device;




    /**
     * Device data parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_Device ( String hostname, int port ) {
        this.agent     = MON_MGR.getAgent( hostname, port );
        this.device    = this.agent.device;
    }


    public String getType ( CANBus bus, byte id ) {
        byte[] b;
        synchronized ( this.device ) {
            b = this.device.get( bus, id );
            if ( b != null ) {
                switch ( b[ 2 ] ) {
                    case 1 :
                        return "GP32";
                    case 2 :
                        return "DM3A";
                    case 3 :
                        return "GP4G";
                    case 4 :
                        return "DM2A";
                    case 5 :
                    	return "GP4_X";
                    case 6 :
                    	return "GP32M";
                    case 7 :
                    	return "GP32S";
                }
            }
        }
        return "N/A";
    }


    public String getHardwareVersion ( CANBus bus, byte id ) {
        byte[] b;
        synchronized ( this.device ) {
            b = this.device.get( bus, id );
            if ( b != null )
                return String.format( "0x%02X", Endian.getShort( b, 3 ) );
        }
        return "N/A";
    }


    public String getFirmwareVersion ( CANBus bus, byte id ) {
        byte[] b;
        synchronized ( this.device ) {
            b = this.device.get( bus, id );
            if ( b != null )
                return String.format( "0x%02X", Endian.getShort( b, 5 ) );
        }
        return "N/A";
    }


    public boolean getInput ( CANBus bus, byte id, byte pos ) {
        byte[] b;
        synchronized ( this.device ) {
            b = this.device.get( bus, id );
            if ( b != null )
                return Endian.getBitsValue( Endian.getInt( b, 7 ), pos, pos ) != 0;
        }
        return false;
    }


    public boolean getOutput ( CANBus bus, byte id, byte pos ) {
        byte[] b;
        synchronized ( this.device ) {
            b = this.device.get( bus, id );
            if ( b != null )
                return Endian.getBitsValue( Endian.getInt( b, 11 ), pos, pos ) != 0;
        }
        return false;
    }


    public DeviceArrow getArrow ( CANBus bus, byte id ) {
        byte[] b;
        synchronized ( this.device ) {
            b = this.device.get( bus, id );
            if ( b != null )
                return DeviceArrow.get( b[ 15 ] );
        }
        return DeviceArrow.NONE;
    }


    public String getText ( CANBus bus, byte id ) {
        byte[] b;
        byte text[] = new byte[ 3 ];
        synchronized ( this.device ) {
            b = this.device.get( bus, id );
            if ( b != null ) {
                System.arraycopy( b, 16, text, 0, 3 );
                return new String( text ).trim();
            }
        }
        return "N/A";
    }


    public DeviceMessage getMessage ( CANBus bus, byte id ) {
        byte[] b;
        synchronized ( this.device ) {
            b = this.device.get( bus, id );
            if ( b != null )
                return DeviceMessage.get( b[ 19 ] );
        }
        return DeviceMessage.NONE;
    }


    public DeviceBehavior getBehavior ( CANBus bus, byte id, byte pos ) {
        byte[] b;
        synchronized ( this.device ) {
            b = this.device.get( bus, id );
            if ( b != null ) {
                return DeviceBehavior.parse( ( byte )Endian.getBitsValue( Endian.getInt( b, 20 + pos / 8 ),
                        pos * 4, pos * 4 + 3 ) );
            }
        }
        return DeviceBehavior.NORMAL;
    }


    /**
     * Get all available devices.
     * @param bus       CAN bus.
     * @return Returns a list of device id that these devices are not timeout.
     */
    public byte[] getAvailableDevcies ( CANBus bus ) {
        synchronized ( this.device ) {
            return this.device.getDeviceList( bus );
        }
    }
}
