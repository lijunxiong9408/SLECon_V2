package comm;
import static logic.util.SiteManagement.MON_MGR;
import javax.swing.JOptionPane;
import ocsjava.remote.configuration.EventAggregator;
import comm.agent.AgentPacket;
import comm.util.Endian;



public class Parser_Event {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * The instance of installed devices.
     */
    private byte[] installed_devices;

    /**
     * The instance of event.
     */
    private byte[] event;

    /**
     * Event data.
     */
    private byte[] raw;

    private boolean	isUpdate = true;
    

    /**
     * Event data parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_Event ( String hostname, int port ) {
        this.agent = MON_MGR.getAgent( hostname, port );
        this.raw   = this.agent.event;
        synchronized ( this.raw ) {
            // Parse installed devices.
            byte tmp[] = new byte[ 2 ];
            System.arraycopy( this.raw, 0, tmp, 0, 2 );
            int installedDevicesLength = Endian.getShort( tmp, 0 );
            this.installed_devices = new byte[ installedDevicesLength ];
            System.arraycopy( this.raw, 2, this.installed_devices, 0, installedDevicesLength );

            // Parse event.
            System.arraycopy( this.raw, 2 + installedDevicesLength, tmp, 0, 2 );
            int eventLength = Endian.getShort( tmp, 0 );
            this.event = new byte[ eventLength ];
            System.arraycopy( this.raw, 2 + installedDevicesLength + 2, this.event, 0, eventLength );
        }
    }


    /**
     * Get installed devices.
     * @return Returns the installed devices in a byte array.
     */
    public byte[] getInstalledDevices () {
        return this.installed_devices.clone();
    }


    /**
     * Update installed devices.
     * @param b     It specifies the event as a byte array.
     */
    public void setInstalledDevices ( byte[] b ) {
        this.installed_devices = b.clone();
    }

    
    public void setUpdateFlag(boolean flag) {
    	this.isUpdate = flag;
    }
    /**
     * Get the event from OCS.
     * @return Returns the event in a byte array.
     */
    public byte[] getEvent () {
    	if(isUpdate) {
    		return this.event.clone();
    	}
    	return null;
    }


    /**
     * Update the configuration of event.
     * @param b     It specifies the event as a byte array.
     */
    public void setEvent ( byte[] b ) {
        this.event = b.clone();
    }


    /**
     * Get the Event data as byte array in Little Endian (OCS uses Little Endian).
     * @return Returns the Event data as byte array in Little Endian (OCS uses Little Endian).
     */
    public byte[] getRaw () {
        byte[] ret = new byte[ 2 + this.installed_devices.length + 2 + this.event.length ];

        // Installed devices.
        System.arraycopy( Endian.getShortByteArray( ( short )this.installed_devices.length ), 0, ret, 0, 2 );
        System.arraycopy( this.installed_devices, 0, ret, 2, this.installed_devices.length );

        // Event.
        System.arraycopy( Endian.getShortByteArray( ( short )this.event.length ), 0, ret, 2 + this.installed_devices.length, 2 );
        System.arraycopy( this.event, 0, ret, 2 + this.installed_devices.length + 2, this.event.length );
        return ret;
    }
    
    
    /**
     * Send local copy of Event data to OCS.
     */
    public void commit () {
        int offset = 0;
        byte[] ret = new byte[ this.raw.length + 1 ];
        
        // XXX
        // Catch a debug that all events blemished on a unknown commit. 
        if ( this.installed_devices == null || this.event == null 
            || this.installed_devices.length <= 0 ) {
            JOptionPane.showMessageDialog( null, "T1", "Error", JOptionPane.ERROR_MESSAGE );
            return;
        }
        
        synchronized ( this.raw ) {
            // Parse installed devices to byte array.
            System.arraycopy( Endian.getShortByteArray( ( short )this.installed_devices.length ), 0, this.raw, 0, 2 );
            System.arraycopy( this.installed_devices, 0, this.raw, 2, this.installed_devices.length );
            offset = 2 + this.installed_devices.length;

            // Parse event to byte array.
            System.arraycopy( Endian.getShortByteArray( ( short )this.event.length ), 0, this.raw, offset, 2 );
            offset += 2;
            System.arraycopy( this.event, 0, this.raw, offset, this.event.length );
            
            // Copy the raw array as the packet to be sent.
            System.arraycopy( this.raw, 0, ret, 1, this.raw.length );
        }
        ret[ 0 ] = AgentPacket.PACKET_EVENT;
        this.agent.send( ret );
    }
}
