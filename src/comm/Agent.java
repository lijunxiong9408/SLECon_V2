package comm;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import comm.Parser_Misc.OCSButton;
import comm.agent.AgentMessage;
import comm.agent.AgentPacket;
import comm.agent.Device;
import comm.constants.CallDef;
import comm.constants.DisCallDef;
import comm.util.Endian;


public class Agent extends Thread {
    /**
     * Logger.
     */
    private final Logger logger = LogManager.getLogger( Agent.class );
    
    /**
     * The size of packet header.
     */
    private static final byte HEADER_SIZE = 4;

    /**
     * The limit of interval time on retry..
     */
    private static final int RETRY_TIMEOUT = 3000;

    /**
     * The size of MCS NVRAM commit word.
     */
    public static final int MCS_NVRAM_ADDR_COMMIT_WORD = 23 * 4;

    /**
     * MCS NVRAM data.
     */
    public final byte[] mcs_nvram = new byte[ MCS_NVRAM_ADDR_COMMIT_WORD + 2567 ];

    /**
     * Status data.
     */
    public final byte[] status = new byte[ 132 ];

    /**
     * Error data.
     */
    public final byte[] error = new byte[ 1 ];

    /**
     * Run data.
     */
    public final byte[] run = new byte[ 260 ];

    /**
     * OCS configuration data.
     */
    public final byte[] ocs_config = new byte[ 5 ];

    /**
     * MCS configuration data.
     */
    public final byte[] mcs_config = new byte[ 1292 ];
    
    /**
     * DCS configuration data.
     */
    public final byte[] dcs_config = new byte[ 128 ];

    /**
     * Module data.
     */
    public final byte[] module = new byte[ 17445 ];

    /**
     * Deployment data.
     */
    public final byte[] deploy = new byte[ 9089 ];

    /**
     * Event data.
     */
    public final byte[] event = new byte[ 31500 ];
    
    /**
     * Log data.
     */
    public final byte[] log = new byte[ 150 * 100 ];
    public final byte[] log_count = { 0 };
    
    /**
     * Door Enable data.
     */
    public final byte[] doorenbale = new byte[ 128 ];
    
    /**
     * VT230 data.
     */
    public final byte[] eps = new byte[ 64 ];
    
    /**
     * Network data.
     */
    public final byte[] netWork = new byte[ 48 ];
    
    /**
     * OCS backup   
     **/
    public final byte[] ocs_backup = new byte[2117];
    
    /**
     * NVRAM backup   
     **/
    public final byte[] nvram_backup = new byte[197];
    
    /**
     * Device data.
     */
    public final Device device = new Device();

    /**
     * The host name.
     */
    final String hostname;

    /**
     * The host port.
     */
    final int port;

    /**
     * The instance of {@code MonitorMgr}.
     */
    private final MonitorMgr notifier;
    
    
    private static long  lastStatusTimer = 0;



    /**
     * A agent is the connection handler to a target host.
     * @param hostname  It specifies the host name.
     * @param port      It specifies the host port.
     * @param notifier  It specifies the instance of {@code MonitorMgr}.
     */
    public Agent ( String hostname, int port, MonitorMgr notifier ) {
        this.hostname = hostname;
        this.port     = port;
        this.notifier = notifier;
    }




    /**
     * Read data to buffer.
     * @param in        It specifies the input screen of socket.
     * @param total     It specifies total number of bytes will read.
     * @param buffer    It specifies the buffer to store return data.
     * @param off       It specifies the start offset in array {@code buffer} at which the data is written.
     * @throws IOException Once the return size doesn't match {@code total} or failure on socket connection, this exception is thrown.
     */
    private void read ( InputStream in, int total, byte buffer[], int off ) throws IOException{
    	int count = 0, len, read;
        do {
            len = total - count;
            if ( len > buffer.length )
                len = buffer.length;
            read = in.read( buffer, count + off, len );
            if ( read > 0 )
                count += read;
        } while ( read > 0 && read < total );

        if ( count != total ) {
        	throw new IOException();
        }

    }


    public void run () {
        byte        buffer[] = new byte[ 65536 ], message;
        int         total;
        Socket      client = null;
        InputStream in;
        int retryTimes = 0;
        while ( true ) {
            // Initialize socket.
            if ( client == null ) {
                try {
                    client = new Socket( this.hostname, this.port );
                    client.setSoTimeout( 5000 );
                    this.notifier.AgentEvent( this, MonitorMgr.AgentState.COMM_CONNECTED, AgentMessage.UNDEFINED );
                    retryTimes = 0;
                } catch ( IOException e ) {
                    try {
                        Thread.sleep( RETRY_TIMEOUT );
                    } catch ( InterruptedException ex ) {
                    }
                    continue;
                }
            }

            // Data processing.
            try {
                in = client.getInputStream();
                
                read( in, HEADER_SIZE, buffer, 0 );

                // Read data.
                total   = ( buffer[ 2 ] & 0xFF ) << 16 | ( buffer[ 1 ] & 0xFF ) << 8 | buffer[ 0 ] & 0xFF;
                message = buffer[ 3 ];
               
                // Parse what message taken.
                //System.out.print( "Data total=" + total + " TYPE=" );
                switch ( message ) {
                    case AgentPacket.PACKET_STATUS :
                    	//System.out.println( "PACKET_STATUS" );
                    	if((System.currentTimeMillis() - lastStatusTimer) > 5) {
                    		lastStatusTimer = System.currentTimeMillis();
                    		synchronized ( this.status ) {
                                read( in, total, this.status, 0 );
                            }
                            this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.STATUS );
                    	}else {
                    		read( in, total, buffer, 0 );
                    	}
                    break;
                    case AgentPacket.PACKET_RUN :
//                        System.out.println( "PACKET_RUN" );
                        synchronized ( this.run ) {
                            read( in, total, this.run, 0 );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.RUN );
                    break;
                    case AgentPacket.PACKET_DEVICE :
//                        System.out.println( "PACKET_DEVICE" );
                        read( in, total, buffer, 0 );
                        synchronized ( this.device ) {
                            this.device.parse( buffer, total );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.DEVICE );
                    break;
                    case AgentPacket.PACKET_MCSNVRAM :
//                        System.out.println( "PACKET_MCSNVRAM" );
                        synchronized ( this.mcs_nvram ) {
                            read( in, total, this.mcs_nvram, Agent.MCS_NVRAM_ADDR_COMMIT_WORD );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.MCS_NVRAM );
                    break;
                    case AgentPacket.PACKET_LOG :
//                        System.out.println( "PACKET_LOG" );
                        synchronized ( this.log ) {
                            read( in, total, this.log, 0 );
                        }
                        synchronized ( this.log_count ) {
                            this.log_count[ 0 ] = ( byte )( total / 150 );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.LOG );
                    break;
                    case AgentPacket.PACKET_ERROR :
 //                       System.out.println( "PACKET_ERROR" );
                        synchronized ( this.error ) {
                            read( in, total, this.error, 0 );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.ERROR );
                    break;
                    case AgentPacket.PACKET_INIT :
                        //System.out.println( "PACKET_INIT" );
                        synchronized ( this.ocs_config ) {
                            read( in, total, this.ocs_config, 0 );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.OCS_CONFIG );
                    break;
                    case AgentPacket.PACKET_MCSCONFIG :
//                        System.out.println( "PACKET_MCSCONFIG" );
                        synchronized ( this.mcs_config ) {
                            read( in, total, this.mcs_config, 0 );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.MCS_CONFIG );
                    break;
                    case AgentPacket.PACKET_DEPLOY :
//                        System.out.println( "PACKET_DEPLOY" );
                        synchronized ( this.deploy ) {
                            read( in, total, this.deploy, 0 );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.DEPLOYMENT );
                    break;
                    case AgentPacket.PACKET_EVENT :
//                        System.out.println( "PACKET_EVENT" );
                        synchronized ( this.event ) {
                            read( in, total, this.event, 0 );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.EVENT );
                    break;
                    case AgentPacket.PACKET_MODULE :
//                        System.out.println( "PACKET_MODULE" );
                        synchronized ( this.module ) {
                            read( in, total, this.module, 0 );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.MODULE );
                    break;
                    case AgentPacket.PACKET_DOOR_ENABLE :
//                      System.out.println( "PACKET_DOOR_ENABLE" );
                      synchronized ( this.doorenbale ) {
                          read( in, total, this.doorenbale, 0 );
                      }
                      this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.DOOR_ENABLE );
                    break;
                    
                    case AgentPacket.PACKET_UPDATE_EPS_DATA :
//                      System.out.println( "PACKET_UPDATE_EPS_DATA" );
                      synchronized ( this.eps ) {
                          read( in, total, this.eps, 0 );
                      }
                      this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.EPS );
                    break;
                    
                    case AgentPacket.PACKET_UPDATE_DCS_STATUS :
                      //System.out.println( "PACKET_UPDATE_DCS_STATUS" );
                      synchronized ( this.dcs_config ) {
                          read( in, total, this.dcs_config, 0 );
                      }
                      this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.DCS_CONFIG );
                    break;
                    
                    case AgentPacket.PACKET_REQ_UPDATE_LOCAL_IP :
                        //System.out.println( "PACKET_REQ_UPDATE_LOCAL_IP" );
                        synchronized ( this.netWork ) {
                            read( in, total, this.netWork, 0 );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.NETWORK );
                      break;
                      
                    case AgentPacket.PACKET_REQ_OCS_BACKUP :
                        synchronized ( this.ocs_backup ) {
                            read( in, total, this.ocs_backup, 0 );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.OCS_BACKUP );
                      break;  
                      
                    case AgentPacket.PACKET_REQ_NVRAM_BACKUP :
                        synchronized ( this.nvram_backup ) {
                            read( in, total, this.nvram_backup, 0 );
                        }
                        this.notifier.AgentEvent( this, MonitorMgr.AgentState.DATA_UPDATE, AgentMessage.NVRAM_BACKUP );
                      break;  
                     
                    default :
                        read( in, total, buffer, 0 );
                       // System.out.println( "UNKNOWN TYPE" );
                }
                
            } catch ( IOException e ) {
            	retryTimes += 1;
            	try {
					Thread.sleep( RETRY_TIMEOUT );
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
            	if(retryTimes > 3) {
                    try {
                    	notifier.AgentEvent( this, MonitorMgr.AgentState.COMM_LOST, AgentMessage.UNDEFINED );
                    	client.close();
                    } catch ( Exception io ) {
                    } finally {
                        client = null;
                    }
            	}
                
            }
        }
    }
    
    
    public void call ( CallDef type, byte floor ) {
        byte[] dat = { AgentPacket.PACKET_REQ_CALL_REG, type.getCode(), floor };
        this.send( dat );
    }
    
    public void disabledcall ( DisCallDef type, byte floor ) {
        byte[] dat = { AgentPacket.PACKET_REQ_DISABLED_CALL_REG, type.getCode(), floor };
        this.send( dat );
    }
    
    
    public void mcs ( short cmd, byte[] dat ) {
        byte[] ret = new byte[ 8 ];
        ret[ 0 ] = AgentPacket.PACKET_REQ_MCS_WRITE;
        System.arraycopy( Endian.getShortByteArray( cmd ), 0, ret, 1, 2 );
        ret[ 3 ] = ( byte )dat.length;
        System.arraycopy( dat, 0, ret, 4, dat.length );
        this.send( ret );
    }
    
    public void can ( short type, byte[] data ) {
    	byte[] ret = new byte[ 7 ];
    	ret[0] = AgentPacket.PACKET_REQ_CAN_WRITE;
    	System.arraycopy( Endian.getShortByteArray( type ), 0, ret, 1, 2 );
    	System.arraycopy( data, 0, ret, 3, data.length );
        this.send( ret );
    }
    
    public void update_Confirm (byte Type) {
    	byte[] ret = new byte[ 2 ];
    	ret[0] = AgentPacket.PACKET_UPDATE_OCS_CONFIRM;
    	ret[1] = Type;
        this.send( ret );
    }
    
    public void backup_Confirm (byte Type) {
    	byte[] ret = new byte[ 2 ];
    	ret[0] = AgentPacket.PACKET_REQ_OCS_BACKUP;
    	ret[1] = Type;
        this.send( ret );
    }
    
    public void nvImport_Confirm (byte Type) {
    	byte[] ret = new byte[ 2 ];
    	ret[0] = AgentPacket.PACKET_REQ_NVRAM_IMPORT;
    	ret[1] = Type;
        this.send( ret );
    }
    
    public void nvBackup_Confirm (byte Type) {
    	byte[] ret = new byte[ 2 ];
    	ret[0] = AgentPacket.PACKET_REQ_NVRAM_BACKUP;
    	ret[1] = Type;
        this.send( ret );
    }
    
    public void time ( long t ) {
        byte[] dat = new byte[ 9 ];
        dat[ 0 ] = AgentPacket.PACKET_REQ_WRITE_SYSTIME;
        System.arraycopy( Endian.getLongByteArray( t ), 0, dat, 1, 8 );
        this.send( dat );
    }
    
    
    public void press ( OCSButton btn, boolean status ) {
        byte[] dat = new byte[ 2 ];
        dat[ 0 ] = AgentPacket.PACKET_REQ_BTN_REG;
        dat[ 1 ] = ( byte )( btn.getCode() << 4 | ( status ? 1 : 0 ) );
        this.send( dat );
    }
    
    
    public void registerMaintenance () {
        byte[] dat = new byte[ 1 ];
        dat[ 0 ] = AgentPacket.PACKET_REQ_CLEAR_EXPIRE_DAY;
        this.send( dat );
    }
    
    public void sendUpdatePacket(byte [] data) {
    	byte [] ret = new byte[data.length + 1];
    	ret[0] = AgentPacket.PACKET_UPDATE_OCS;
    	System.arraycopy( data, 0, ret, 1, data.length );
    	this.send(ret);
    }
    
    public void sendImportNvramPacket(byte [] data) {
    	byte [] ret = new byte[data.length + 1];
    	ret[0] = AgentPacket.PACKET_REQ_NVRAM;
    	System.arraycopy( data, 0, ret, 1, data.length );
    	this.send(ret);
    }
    
    public void upDateLocalIP(byte [] data) {
    	byte [] ret = new byte[data.length + 1];
    	ret[0] = AgentPacket.PACKET_REQ_UPDATE_LOCAL_IP;
    	System.arraycopy( data, 0, ret, 1, data.length );
    	this.send(ret);
    }

    public void send ( byte dat[] ) {
        try {
            Socket client = new Socket( this.hostname, this.port - 1 );
            client.getOutputStream().write( dat );
            client.close();
        } catch ( IOException e ) {
            this.logger.info( "Unable to send data to <" + this.hostname + ", " + ( this.port - 1 ) + ">", e );
        }
    }
    
    
    public byte[] retrieve ( byte dat[] ) {
        byte[] ret = new byte[ 1 ];
        try {
            Socket client = new Socket( this.hostname, this.port - 1 );
            client.getOutputStream().write( dat );
            client.getInputStream().read( ret, 0, 1 );
            client.close();
        } catch ( IOException e ) {
            this.logger.info( "Unable to retrieve data from <" + this.hostname + ", " + ( this.port - 1 ) + ">", e );
        }
        return ret;
    }
}
