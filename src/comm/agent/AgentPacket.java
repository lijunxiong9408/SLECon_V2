package comm.agent;

public class AgentPacket {
	
	/**
     * OCS configuration data.
     */
    public static final byte PACKET_INIT = 101;

    /**
     * Error data.
     */
    public static final byte PACKET_ERROR = 102;

    /**
     * MCS configuration data.
     */
    public static final byte PACKET_MCSCONFIG = 103;

    /**
     * Status data.
     */
    public static final byte PACKET_STATUS = 104;

    /**
     * Log data.
     */
    public static final byte PACKET_LOG = 105;

    /**
     * Run data.
     */
    public static final byte PACKET_RUN = 106;

    /**
     * Deployment data.
     */
    public static final byte PACKET_DEPLOY = 107;

    /**
     * Event data.
     */
    public static final byte PACKET_EVENT = 108;

    /**
     * Module data.
     */
    public static final byte PACKET_MODULE = 109;

    /**
     * MCS NVRAM data.
     */
    public static final byte PACKET_MCSNVRAM = 110;

    /**
     * Device data.
     */
    public static final byte PACKET_DEVICE = 111;
    
    // TODO Not verified.
    public static final byte PACKET_REQ_CLEAR_LOG = 112;
    public static final byte PACKET_REQ_DEL_LOG = 113;
    public static final byte PACKET_REQ_CALL_REG = 114;
    public static final byte PACKET_REQ_BTN_REG = 115;
    public static final byte PACKET_REQ_MCS_WRITE = 116;
    public static final byte PACKET_REQ_AUTH = 117;
    public static final byte PACKET_REQ_WRITE_SYSTIME = 118;
    public static final byte PACKET_REQ_CLEAR_EXPIRE_DAY = 119;
    
    /**
     *  Door Enable data.
     * */
    public static final byte PACKET_DOOR_ENABLE = 120;
    
    public static final byte PACKET_REQ_CAN_WRITE = 121;
    
    public static final byte PACKET_UPDATE_OCS = 122;
    
    public static final byte PACKET_UPDATE_OCS_CONFIRM = 123;
    
    /**
     * 	EPS data
     * */
    public static final byte PACKET_UPDATE_EPS_DATA = 124;
    
    /**
     *  Update DCS Status.
     * */
    public static final byte PACKET_UPDATE_DCS_STATUS = 125;
    
    public static final byte PACKET_REQ_DISABLED_CALL_REG = 126;
    
    public static final byte PACKET_REQ_UPDATE_LOCAL_IP = 127;
    
    /** OCS backup. */
    public static final byte PACKET_REQ_OCS_BACKUP = 100;
    
    /** NVRAM backup. */
    public static final byte PACKET_REQ_NVRAM_BACKUP = 99;
    
    /** NVRAM Import. */
    public static final byte PACKET_REQ_NVRAM_IMPORT = 98;
    public static final byte PACKET_REQ_NVRAM 		 = 97;
}
