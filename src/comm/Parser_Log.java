package comm;
import static logic.util.SiteManagement.MON_MGR;

import comm.agent.AgentPacket;
import comm.constants.CallDef;
import comm.constants.OcsModule;
import comm.util.Endian;



public class Parser_Log {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * Log data.
     */
    private final byte[] log;
    
    private final byte[] log_count;




    /**
     * Log data parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_Log ( String hostname, int port ) {
        this.agent     = MON_MGR.getAgent( hostname, port );
        this.log       = this.agent.log;
        this.log_count = this.agent.log_count;
    }


    public void clearLog () {
        byte[] dat = { AgentPacket.PACKET_REQ_CLEAR_LOG, AgentPacket.PACKET_REQ_CLEAR_LOG };
        this.agent.send( dat );
    }


    public void deleteLog ( byte idx ) {
        byte[] dat = { AgentPacket.PACKET_REQ_DEL_LOG, idx };
        this.agent.send( dat );
    }


    public int getCount () {
        synchronized ( this.log_count ) {
            return this.log_count[ 0 ];
        }
    }


    public Log getLog ( int idx ) {
        synchronized ( this.log ) {
            return new Log( this.log, idx * 150 );
        }
    }


    public static class Log {
        /**
         * The type of log. 'O' for OCS, 'M' for MCS.
         */
        public char type;
        public int errcode;
        public long  timestamp;
        
        // Extra data.
        public int position;
        public float speed;
        public byte current_floor;
        public byte last_run_floor;
        public OcsModule ocs_module;
        public byte calls[] = new byte[ 128 ]; // Please refer to {@code CallDef}.
                                               // E.g. Front Car is ON -> calls[ 0 ] & CallDef.FRONT_CAR.getCode() != 0.


        public Log ( byte[] dat, int off ) {
            this.type      = ( char )dat[ off + 0 ];
            this.errcode   = Endian.getShort( dat, off + 1 ) & 0xFFFF;
            this.timestamp = Endian.getLong( dat, off + 3 );
            
            // Extra data.
            this.position  = Endian.getInt( dat, off + 11 );
            this.speed     = Endian.getFloat( dat, off + 15 );
            this.current_floor = dat[ off + 19 ];
            this.last_run_floor = dat[ off + 20 ];
            this.ocs_module = OcsModule.get( dat[ off + 21 ] );
            System.arraycopy( dat, off + 22, calls, 0, 128 );
        }
        
        
        public Log () {

        }
        
        /**
         * Whether the specified call definition is triggered.
         * @param floor     It specifies the floor.
         * @param type      It specifies the {@code CallDef}.
         * @return Returns {@code true} once the call definition is triggered; otherwise, returns {@code false}.
         */
        public boolean IsCallDef ( short floor, CallDef type ) {
            return ( this.calls[ floor ] & type.getCode() ) != 0;
        }
    }
}
