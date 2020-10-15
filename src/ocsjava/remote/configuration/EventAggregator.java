package ocsjava.remote.configuration;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.DataFormatException;
import javax.swing.JOptionPane;
import javax.xml.bind.Marshaller.Listener;

import ocsjava.remote.configuration.Event.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import comm.constants.CANBus;
import comm.util.Endian;
import logic.connection.LiftConnectionBean;
import logic.util.SiteManagement;
import logic.util.Version;
import logic.util.VersionChangeListener;



public class EventAggregator {
    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( EventAggregator.class );
    
    /**
     * Maximum devices are supported.
     */
    public static final int MAX_EVENT = 2500;

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
    public static EventAggregator toEventAggregator ( byte[] binary, LiftConnectionBean connBean ) throws DataFormatException {
        int             pos        = 0, ptr,
                        eventCount = 0, max_eventCounts = 0;
        EventAggregator ret        = new EventAggregator();
        Version version = SiteManagement.getVersion(connBean);
        String[] str_ver = (version.getControlCoreVersion()).split("\\.");
        int ver = Integer.parseInt(str_ver[0])*100  + 
        		  Integer.parseInt(str_ver[1])*10 +
        		  Integer.parseInt(str_ver[2]);
        
        if( ver <=  223 )	// 223 -> ocs ver "2.2.3"
        	max_eventCounts = 2000;
        else 
        	max_eventCounts = MAX_EVENT;
        if ( binary.length >= max_eventCounts * PTR_SIZE ) {
            while ( pos < max_eventCounts * PTR_SIZE ) {
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
            if ( binary.length <= max_eventCounts * PTR_SIZE )
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
    public byte[] toByteArray ( LiftConnectionBean connBean ) throws DataFormatException {
    	int max_eventCounts = 0;
    	Version version = SiteManagement.getVersion(connBean);
        String[] str_ver = (version.getControlCoreVersion()).split("\\.");
        int ver = Integer.parseInt(str_ver[0])*100  + 
        		  Integer.parseInt(str_ver[1])*10 +
        		  Integer.parseInt(str_ver[2]);
        
        if( ver <=  223 )	// 223 -> ocs ver "2.2.3"
        	max_eventCounts = 2000;
        else 
        	max_eventCounts = MAX_EVENT;
        
        //----------------------------------------------------------------
        
        int   dataPtr  = max_eventCounts * PTR_SIZE;
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
}
