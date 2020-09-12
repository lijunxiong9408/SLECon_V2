package ocsjava.remote.configuration;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.DataFormatException;
import javax.swing.JOptionPane;
import ocsjava.remote.configuration.Event.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import comm.constants.CANBus;
import comm.util.Endian;



public class EventAggregator {
    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( EventAggregator.class );
    
    /**
     * Maximum devices are supported.
     */
    public static final int MAX_EVENT = 2000;

    /**
     * The size of a pointer.
     */
    public static final int PTR_SIZE = 4;

    /**
     * List of event instants.
     */
    protected Event events[] = new Event[ MAX_EVENT ];

    /**
     * Number of events defined.
     */
    private int eventCount = 0;




    /**
     * Convert a binary to an EventAggregator.
     * @param binary        Byte array.
     * @return Returns an EventAggregator on success, otherwise, returns null.
     * @throws DataFormatException Once fail to create an event element, this exception is thrown.
     */
    public static EventAggregator toEventAggregator ( byte[] binary ) throws DataFormatException {
        int             pos        = 0, ptr,
                        eventCount = 0;
        EventAggregator ret        = new EventAggregator();
        if ( binary.length >= MAX_EVENT * PTR_SIZE ) {
            while ( pos < MAX_EVENT * PTR_SIZE ) {
                byte tmp[] = new byte[ PTR_SIZE ];    // Read data pointers.
                tmp[ 0 ] = binary[ pos++ ];
                tmp[ 1 ] = binary[ pos++ ];
                tmp[ 2 ] = binary[ pos++ ];
                tmp[ 3 ] = binary[ pos++ ];
                if ( ( ptr = Endian.getInt( tmp, 0 ) ) != 0 )
                    ret.setEvent( eventCount, Event.toEvent( binary, ptr ) );
                eventCount++;
            }
            
            // XXX
            // Catch a debug that all events blemished on a unknown commit. 
            if ( binary.length <= MAX_EVENT * PTR_SIZE )
                JOptionPane.showMessageDialog( null, "R1", "Error", JOptionPane.ERROR_MESSAGE );
        } else
            logger.error( "Mismatch size of Event data!" );
        return ret;
    }


    /**
     * Set an event by event ID
     * @param id    Event ID.
     * @param event An event to be added.
     */
    public void setEvent ( int id, Event event ) {
        this.events[ id ] = event;
    }


    /**
     * Get the reference to an event by event ID.
     * @param id    Event ID.
     * @return Returns an event on success; otherwise, return null if the event doesn't exist.
     */
    public Event getEvent ( int id ) {
        return this.events[ id ];
    }


    /**
     * Get the reference to event list.
     * @return Returns the reference to event list.
     */
    public Event[] getEventList () {
        return this.events;
    }


    /**
     * Get the number of events.
     * @return Returns the number of events.
     */
    public int getSize () {
        return this.eventCount;
    }


    /**
     * Returns installed devices to a byte array. The installed devices includes all ID of device
     * that events has been defined.
     * @return Returns a byte array to installed devices.
     * @throws DataFormatException Once events contain invalid data format, this exception is thrown.
     */
    public byte[] getInstalledDevices () throws DataFormatException {
        HashMap<Byte, Byte>
            carID  = new HashMap<>(),
            hallID = new HashMap<>();
        Event event;
        for ( int i = 0, len = this.events.length ; i < len ; i++ ) {
            if ( ( event = this.events[ i ] ) == null )    // Process event by event.
                continue;
            for ( int j = 0, inputCount = event.getInputCount() ; j < inputCount ; j++ ) {
                Item item = event.getInput( j );           // Process input items.
                switch ( item.getBus() ) {
                case CAR :
                    carID.put( item.getId(), item.getId() );
                    break;
                case HALL :
                    hallID.put( item.getId(), item.getId() );
                    break;
                }
            }
            for ( int j = 0, outputCount = event.getOutputCount() ; j < outputCount ; j++ ) {
                Item item = event.getOutput( j );          // Process output items.
                switch ( item.getBus() ) {
                case CAR :
                    carID.put( item.getId(), item.getId() );
                    break;
                case HALL :
                    hallID.put( item.getId(), item.getId() );
                    break;
                }
            }
        }

        byte[] ret = new byte[ 1 ],
               tmp = new byte[ 1 ];
        ret[ 0 ] = ( byte )carID.size();    // Size of device ID in CAR.

        Iterator<Byte> it = carID.values().iterator();
        while ( it.hasNext() ) {
            tmp[ 0 ] = ( byte )it.next();    // Concat device ID one by one.
            ret      = concat( ret, tmp );
        }
        tmp[ 0 ] = ( byte )hallID.size();    // Size of device ID in HALL.
        ret      = concat( ret, tmp );
        it       = hallID.values().iterator();
        while ( it.hasNext() ) {
            tmp[ 0 ] = ( byte )it.next();    // Concat device ID one by one.
            ret      = concat( ret, tmp );
        }
        return ret;
    }


    /**
     * Convert all events include header to a byte array.
     * @return Returns a byte array which includes header and data blocks.
     * @throws DataFormatException Once event elements has invalid data format, this exception is thrown.
     */
    public byte[] toByteArray () throws DataFormatException {
        int   dataPtr  = MAX_EVENT * PTR_SIZE;
        byte  header[] = new byte[ dataPtr ];
        byte  data[]   = null;
        Event event;
        for ( int i = 0, len = this.events.length ; i < len ; i++ ) {
            if ( ( event = this.events[ i ] ) == null )
                continue;

            byte[] tmp = event.toByteArray();
            if ( data == null )
                data = tmp;
            else
                data = concat( data, tmp );                                                              // Append an event.
            System.arraycopy( Endian.getIntByteArray( dataPtr ), 0, header, i * PTR_SIZE, PTR_SIZE );    // Save data pointer.
            dataPtr += tmp.length;
        }
        return concat( header, data );
    }


    /**
     * Join two byte arrays.
     * @param A     First join array.
     * @param B     Second join array (Will be appended after <code>A</code>).
     * @return Returns a new array which join array <code>A</code> and <code>B</code>.
     */
    private static byte[] concat ( byte[] A, byte[] B ) {
        byte[] C = new byte[ A.length + B.length ];
        System.arraycopy( A, 0, C, 0, A.length );
        System.arraycopy( B, 0, C, A.length, B.length );
        return C;
    }


    /**
     * Demo section.
     * A DEMO TO OUTPUT FLOOR INFORMATION TO A BINARY FILE.
     * @param args  It specifies the arguments read from command line.
     */
    public static void main ( String args[] ) {
        Event           event;
        EventAggregator result = new EventAggregator();
        try {
            event = new Event();
            event.setOperator( Event.Operator.AND );
            event.addInput( new Item( CANBus.CAR, ( byte )2, ( byte )0, ( byte )1 ) );
            event.addInput( new Item( CANBus.HALL, ( byte )3, ( byte )31, ( byte )0 ) );
            event.addOutput( new Item( CANBus.HALL, ( byte )2, ( byte )0, ( byte )1 ) );
            event.addOutput( new Item( CANBus.CAR, ( byte )3, ( byte )31, ( byte )1 ) );
            result.setEvent( 0, event );
            event = new Event();
            event.setOperator( Event.Operator.OR );
            event.addInput( new Item( CANBus.HALL, ( byte )10, ( byte )20, ( byte )1 ) );
            event.addInput( new Item( CANBus.CAR, ( byte )11, ( byte )20, ( byte )1 ) );
            event.addInput( new Item( CANBus.HALL, ( byte )12, ( byte )20, ( byte )1 ) );
            event.addOutput( new Item( CANBus.HALL, ( byte )10, ( byte )19, ( byte )1 ) );
            event.addOutput( new Item( CANBus.HALL, ( byte )4, ( byte )0, ( byte )0 ) );
            result.setEvent( 1, event );
            event = new Event();
            event.setOperator( Event.Operator.NON );
            event.addInput( new Item( CANBus.HALL, ( byte )10, ( byte )20, ( byte )1 ) );
            result.setEvent( 2, event );
            event = new Event();
            event.setOperator( Event.Operator.NON );
            event.addOutput( new Item( CANBus.HALL, ( byte )4, ( byte )1, ( byte )1 ) );
            result.setEvent( 3, event );
            event = new Event();
            event.setOperator( Event.Operator.NON );
            result.setEvent( 4, event );

            byte[] output = result.toByteArray();

            // [DEBUG]
            // Binary array to EventAggregator.
            output = EventAggregator.toEventAggregator( output ).toByteArray();

            // [DEBUG]
            // Print binary (Event).
            System.out.println( "Total length: " + output.length );
            for ( int i = 1, len = output.length ; i <= len ; i++ ) {
                System.out.printf( "0x%02X ", output[ i - 1 ] );
                if ( ( i % 16 ) == 0 )
                    System.out.println( "" );
            }
            System.out.println( "" );

            // [DEBUG]
            // Save to a binary file (Event).
            try ( DataOutputStream out = new DataOutputStream( new FileOutputStream( new File( System.getProperty( "user.dir" )
                                                                                               + "\\build\\classes\\demo\\iomanager\\"
                                                                                               + "event.bin" ) ) ) ) {
                out.write( output );
                out.flush();
            } catch ( Exception e ) {
                System.err.println( "Unable to write event.bin!" );
            }

            // [DEBUG]
            // Print binary (Installed devices).
            output = result.getInstalledDevices();
            System.out.println( "Installed devices\n====================================================================" );
            for ( int i = 1, len = output.length ; i <= len ; i++ ) {
                System.out.printf( "0x%02X ", output[ i - 1 ] );
                if ( ( i % 16 ) == 0 )
                    System.out.println( "\n" );
            }
            System.out.println( "" );

            // [DEBUG]
            // Save to a binary file (Installed devices).
            try ( DataOutputStream out = new DataOutputStream( new FileOutputStream( new File( System.getProperty( "user.dir" )
                                                                                               + "\\build\\classes\\demo\\iomanager\\"
                                                                                               + "installed_devices.bin" ) ) ) ) {
                out.write( output );
                out.flush();
            } catch ( Exception e ) {
                System.err.println( "Unable to write installed_devices.bin!" );
            }
        } catch ( DataFormatException e ) {
            e.printStackTrace( System.err );
        }
    }

    
//  public static void main ( String args[] ) throws Exception {
//      String       HOSTNAME = "192.168.1.253";
//      int          PORT     = 2222;
//      Packet       packet   = new Packet( 2, 0, 1 );
//      Connection   conn     = new Connection( HOSTNAME, PORT, packet );
//      LibraryEvent event    = new LibraryEvent( conn );
//
//      // Read the floor configuraton from remote.
//      byte[] ret = event.getInstalledDevices();
//      System.out.println( "Length of installed devices received: " + ret.length );
//
//      // Write back installed devices to remote.
//      event.commitInstalledDevices( ret );
//
//      // Read the information on event.
//      ret = event.getEvent();
//      System.out.println( "Length of event informaiton received: " + ret.length );
//
//      // Using event aggregator to recover events by binary.
//      EventAggregator x = EventAggregator.toEventAggregator( ret );
//      ret = x.toByteArray();
//
//      // Write back event information to remote.
//      event.commitEvent( ret );
//
//      // Save to a binary file.
//      try ( DataOutputStream out = new DataOutputStream( new FileOutputStream( new File( System.getProperty( "user.dir" )
//                                                                                         + "\\event.bin" ) ) ) ) {
//          out.write( ret );
//          out.flush();
//      } catch ( Exception e ) {
//          System.err.println( "Unable to write event.bin!" );
//      }
//
////    // [DEBUG] Compare data.
////    DataInputStream in2 = new DataInputStream( new FileInputStream( new File( System.getProperty( "user.dir" ) + "\\event.bin.bak" ) ) );
////    int v1, v2, pos = 0;
////    do {
////        if ( ( v2 = in2.read() ) == -1 && pos >= ret.length )
////            break;
////        v1 = ret[ pos ];
////        v1 = v1 < 0 ? v1 + 256 : v1;
////        if ( ( v1 == -1 && v2 != -1 ) || ( v1 != -1 && v2 == -1 ) ) {
////            System.out.println( "Different length at position " + pos + " <" + v1 + ", " + v2 + ">" );
////            return;
////        }
////        if ( v1 != v2 ) {
////            System.out.println( "Different at position " + pos + " <" + v1 + ", " + v2 + ">" );
////            return;
////        }
////        pos++;
////    } while ( v1 != -1 && v2 != -1 );
////    System.out.println( "They are identical!" );
//  }
}
