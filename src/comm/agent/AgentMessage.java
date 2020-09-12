package comm.agent;


public enum AgentMessage {
    /**
     * Undefined data type (default value).
     */
    UNDEFINED( 0 ),

    /**
     * Status data.
     */
    STATUS( 1 ),

    /**
     * MCS configuration data.
     */
    MCS_CONFIG( 2 ),

    /**
     * MCS NVRAM data.
     */
    MCS_NVRAM( 4 ),

    /**
     * Device data.
     */
    DEVICE( 8 ),

    /**
     * Log data.
     */
    LOG( 16 ),

    /**
     * Module data.
     */
    MODULE( 32 ),

    /**
     * Event data.
     */
    EVENT( 64 ),

    /**
     * Deployment data.
     */
    DEPLOYMENT( 128 ),

    /**
     * Run data.
     */
    RUN( 256 ),

    /**
     * Error data.
     */
    ERROR( 512 ),

    /**
     * OCS configuration data.
     */
    OCS_CONFIG( 1024 ),

    /**
     * Authentication data.
     */
    AUTH( 2048 ),

    /**
     * Miscellaneous data.
     */
    MISC( 4096 ),
	
	/**
     * DOOR_ENABLE data.
     */
    DOOR_ENABLE( 8192 ),
	
	/**
     * EPS data.
     */
    EPS( 16384 ),
	
	/**
     * DCS_CONFIG data.
     */
    DCS_CONFIG( 32768 ),
	
	/**
     * NETWORK data.
     */
    NETWORK( 65536 ),
	
	/**
     * OCS backup.
     */
    OCS_BACKUP( 131072 ),
	
	/**
     * NVRAM backup.
     */
    NVRAM_BACKUP( 262144 );
    /**
     * The instance of enumeration value.
     */
    private final int code;




    /**
     * Available message of agent.
     * @param c It specifies the constant value of an enumeration.
     */
    private AgentMessage ( int c ) {
        this.code = c;
    }


    /**
     * Get the constant value of enumeration.
     * @return Returns the constant value of enumeration.
     */
    public int getCode () {
        return this.code;
    }
}
