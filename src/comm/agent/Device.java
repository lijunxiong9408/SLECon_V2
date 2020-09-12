package comm.agent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import comm.constants.CANBus;




public class Device {
    /**
     * Timeout on device. A timeout device specifies the device has no response up to time limit.
     */
    private static final int DEVICE_TIMEOUT = 5000;

    /**
     * The size of all data of a device.
     */
    private static final int DEVICE_ITEM_SIZE = 36;

    /**
     * Data container.
     */
    private HashMap<String, DeviceItem> data = new HashMap<>( 512 );




    /**
     * Parse raw data received from remote host.
     * @param b         It specifies the raw data in byte array format.
     * @param count     It specifies the count of device data stored in the byte array.
     */
    public void parse ( byte[] b, int count ) {
        for ( int i = 0 ; i < count ; i += 36 ) {
            this.data.put( b[ i ] + "_" + b[ i + 1 ], new DeviceItem( b, i ) );
        }
    }


    /**
     * Get the raw data of a device.
     * This method returns a reference to the actually data stored. i.e. Changing the returned value will modify the original data directly.
     * @param bus   It specifies the CAN Bus of device.
     * @param id    It specifies the ID of device.
     * @return
     */
    public byte[] get ( CANBus bus, byte id ) {
        String     key  = ( bus == CANBus.CAR ? 1 : 2 ) + "_" + id;
        DeviceItem item = this.data.get( key );
        if ( item == null || item.timestamp + DEVICE_TIMEOUT < System.currentTimeMillis() ) {
            this.data.remove( key );
            return null;
        }
        return item.raw;
    }


    /**
     * Get all available devices.
     * @param bus       CAN bus.
     * @return Returns a list of device id that these devices are not timeout.
     */
    public byte[] getDeviceList ( CANBus bus ) {
        Pattern         pattern = Pattern.compile( "([0-9])\\_([0-9]+)" );
        Matcher         matcher;
        ArrayList<Byte> pre = new ArrayList<>( 256 );
        for ( String key : this.data.keySet() ) {
            matcher = pattern.matcher( key );
            if ( matcher.find() && matcher.group( 1 ).equals( bus == CANBus.CAR ? "1" : "2" ) )
                pre.add( Byte.parseByte( matcher.group( 2 ) ) );
        }
        byte[] ret = new byte[ pre.size() ];
        for ( int i = 0 ; i < ret.length ; i++ )
            ret[ i ] = pre.get( i );
        return ret;
    }


    private class DeviceItem {
        /**
         * The time stamp on raw data received.
         */
        private long timestamp;

        /**
         * The raw data of a device.
         */
        private byte[] raw;




        /**
         * The raw data of a device.
         * @param b     It specifies the raw data of device.
         * @param off   It specifies the data offset in {@code b}.
         */
        DeviceItem ( byte[] b, int off ) {
            this.raw       = Arrays.copyOfRange( b, off, off + DEVICE_ITEM_SIZE );
            this.timestamp = System.currentTimeMillis();
        }
    }
}
