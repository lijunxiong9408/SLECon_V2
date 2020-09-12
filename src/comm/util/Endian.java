package comm.util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;



public class Endian {
    /**
     * Get the bit value of a {@code byte}.
     * @param num   It specifies the number.
     * @param bit   It specifies the position of bit (It starts from zero).
     * @return Returns either {@code 1} or {@code 0}.
     */
    public static int getBit ( byte num, int bit ) {
        return ( num & ( 1 << bit ) ) >> bit;
    }


    /**
     * Get the value of {@code short}.
     * @param arr       It specifies an Little Endian byte array.
     * @param offset    It specifies the starting position in array.
     * @return Returns a 2-byte integer.
     */
    public static short getShort ( byte[] arr, int offset ) {
        return ( short )( ( arr[ offset + 1 ] & 0xFF ) << 8 | ( arr[ offset ] & 0xFF ) );
    }


    /**
     * Get the value of {@code int}.
     * @param arr       It specifies an Little Endian byte array.
     * @param offset    It specifies the starting position in array.
     * @return Returns a 4-byte integer.
     */
    public static int getInt ( byte[] arr, int offset ) {
        return ( arr[ offset + 3 ] & 0xFF ) << 24 | ( arr[ offset + 2 ] & 0xFF ) << 16 | ( arr[ offset + 1 ] & 0xFF ) << 8
               | ( arr[ offset ] & 0xFF );
    }


    /**
     * Get the value of {@code long}.
     * @param arr       It specifies an Little Endian byte array.
     * @param offset    It specifies the starting position in array.
     * @return Returns a 8-byte integer.
     */
    public static long getLong ( byte[] arr, int offset ) {
        return ( ( long )arr[ offset + 7 ] & 0xFF ) << 56 | ( ( long )arr[ offset + 6 ] & 0xFF ) << 48
               | ( ( long )arr[ offset + 5 ] & 0xFF ) << 40 | ( ( long )arr[ offset + 4 ] & 0xFF ) << 32
               | ( ( long )arr[ offset + 3 ] & 0xFF ) << 24 | ( ( long )arr[ offset + 2 ] & 0xFF ) << 16 
               | ( ( long )arr[ offset + 1 ] & 0xFF ) << 8
               | ( ( long )arr[ offset ] & 0xFF );
    }


    /**
     * Get the value of {@code float}.
     * @param num   It specifies an Little Endian byte array.
     * @param bit   It specifies the starting position in array.
     * @return Returns a floating number.
     */
    public static float getFloat ( byte[] arr, int offset ) {
        return Float.intBitsToFloat( getInt( arr, offset ) );
    }


    /**
     * Get the value of {@code double}.
     * @param num   It specifies an Little Endian byte array.
     * @param bit   It specifies the starting position in array.
     * @return Returns a floating number.
     */
    public static double getDouble ( byte[] arr, int offset ) {
        return Double.longBitsToDouble( getLong( arr, offset ) );
    }


    /**
     * Get the value of {@code short} as Little Endian in a byte array.
     * @param num   It specifies the number to be converted to Little Endian.
     * @return Return Little Endian of {@code short} in a byte array.
     */
    public static byte[] getShortByteArray ( short num ) {
        ByteBuffer buf = ByteBuffer.allocate( 2 );
        buf.order( ByteOrder.LITTLE_ENDIAN );
        buf.putShort( num );
        return buf.array();
    }


    /**
     * Get the value of {@code int} as Little Endian in a byte array.
     * @param num   It specifies the number to be converted to Little Endian.
     * @return Return Little Endian of {@code int} in a byte array.
     */
    public static byte[] getIntByteArray ( int num ) {
        ByteBuffer buf = ByteBuffer.allocate( 4 );
        buf.order( ByteOrder.LITTLE_ENDIAN );
        buf.putInt( num );
        return buf.array();
    }


    /**
     * Get the value of {@code long} as Little Endian in a byte array.
     * @param num   It specifies the number to be converted to Little Endian.
     * @return Return Little Endian of {@code long} in a byte array.
     */
    public static byte[] getLongByteArray ( long num ) {
        ByteBuffer buf = ByteBuffer.allocate( 8 );
        buf.order( ByteOrder.LITTLE_ENDIAN );
        buf.putLong( num );
        return buf.array();
    }


    /**
     * Get the value of {@code float} as Little Endian in a byte array.
     * @param num   It specifies the number to be converted to Little Endian.
     * @return Return Little Endian of {@code float} in a byte array.
     */
    public static byte[] getFloatByteArray ( float num ) {
        ByteBuffer buf = ByteBuffer.allocate( 4 );
        buf.order( ByteOrder.LITTLE_ENDIAN );
        buf.putFloat( num );
        return buf.array();
    }


    /**
     * Get a range of bits in the integer.
     * @param num  It specifies the input number. It cannot exceed {@code Long.MAX_VALUE}.
     * @param s    It specifies the starting offset of bit position (0-63) (inclusive).
     * @param e    It specifies the end offset of bits position (0-63) (inclusive).
     * @return Returns value extracted.
     */
    public static long getBitsValue ( long num, int s, int e ) {
        return ( num >>> s ) & ( ( 1L << ( e - s + 1 ) ) - 1L );
    }


    /**
     * Set a range of bits in the integer.
     * @param num   It specifies the integer to be overwritten. It cannot exceed {@code Long.MAX_VALUE}.
     * @param v     It specifies the value to overwrite {@code num}.
     * @param s     It specifies the starting offset of bit position (0-63) (inclusive).
     * @param e     It specifies the end offset of bits position (0-63) (inclusive).
     * @return Returns value set.
     */
    public static long setBitsValue ( long num, int v, int s, int e ) {
        return ( num & ~ ( ( ( 1 << ( e - s + 1 ) ) - 1 ) << s ) ) | ( v << s );
    }
}
