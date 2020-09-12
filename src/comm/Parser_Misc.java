package comm;
import static logic.util.SiteManagement.MON_MGR;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import comm.constants.CallDef;
import comm.constants.DisCallDef;



public class Parser_Misc {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;




    /**
     * Misc parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_Misc ( String hostname, int port ) {
        this.agent = MON_MGR.getAgent( hostname, port );
    }

    
    /**
     * Trigger a call to OCS.
     * @param btn       It specifies the type of call.
     * @param status    It specifies the floor.
     */
    public void call ( CallDef type, byte floor ) {
        synchronized ( this ) {
            this.agent.call( type , floor );
        }
    }
    
    /**
     * Trigger a diabeld call to OCS.
     * @param btn       It specifies the type of call.
     * @param status    It specifies the floor.
     */
    public void disabledcall ( DisCallDef type, byte floor ) {
        synchronized ( this ) {
            this.agent.disabledcall( type , floor );
        }
    }


    /**
     * Send a MCS command to OCS.
     * @param cmd   It specifies the command in Big Endian.
     * @param dat   It specifies the data of MCS (It MUST in Little Endian).
     *              The length of {@code dat} MUST has exactly length of data. 
     *              E.g. if the data of the MCS command is 3 bytes, {@code dat} MUST be a @{code byte[3]}.
     */
    public void mcs ( short cmd, byte[] dat ) {
        synchronized ( this ) {
            this.agent.mcs( cmd, dat );
        }
    }

    /**
     * Send a CAN command to OCS.
     */
    public void can ( short cmd, byte[] dat ) {
        synchronized ( this ) {
            this.agent.can( cmd, dat );
        }
    }
    
    /**
     * Send a Update Confirm command to OCS.
     */
    public void updateConfirm (byte Type) {
        synchronized ( this ) {
            this.agent.update_Confirm(Type);
        }
    }
    
    
    
    /**
     * Send a Backup Confirm command to OCS.
     */
    public void backupConfirm (byte Type) {
        synchronized ( this ) {
            this.agent.backup_Confirm(Type);
        }
    }
    
    
    /**
     * Send a NVRAM Import Confirm command to OCS.
     */
    public void nvImportConfirm (byte Type) {
        synchronized ( this ) {
            this.agent.nvImport_Confirm(Type);
        }
    }
    
    /**
     * Send a NVRAM Backup Confirm command to OCS.
     */
    public void nvBackupConfirm (byte Type) {
        synchronized ( this ) {
            this.agent.nvBackup_Confirm(Type);
        }
    }

    /**
     * Set the system time.
     * Please use Parser_Status.getTime() to retrieve latest clock.
     * @param t
     */
    public void time ( long t ) {
        synchronized ( this ) {
            this.agent.time( t );
        }
    }
    
    
    /**
     * Register maintenance.
     */
    public void registerMaintenance () {
        synchronized ( this ) {
            this.agent.registerMaintenance();
        }
    }


    /**
     * TF/BF/CHC/DDO.
     * @param btn
     * @param status
     */
    public void press ( OCSButton btn, boolean status ) {
        synchronized ( this ) {
            this.agent.press( btn, status );
        }
    }
    
    public void update(byte [] data) {
    	synchronized ( this ) {
            this.agent.sendUpdatePacket( data );
        }
    }
    
    public void importNvram(byte [] data) {
    	synchronized ( this ) {
            this.agent.sendImportNvramPacket( data );
        }
    }
    
    public static enum OCSButton {
        /** Disable door open. */
        BS_DDO ( ( byte )0 ),

        /** Cut Hall call. */
        BS_CHC ( ( byte )1 ),
        
        /** Top floor. */
        BS_TF ( ( byte )2 ),

        /** Bottom floor. */
        BS_BF ( ( byte )3 );

        /**
         * Lookup table.
         */
        private static final Map<Byte, OCSButton> LOOKUP = new HashMap<Byte, OCSButton>();
        static {
            for ( OCSButton s : EnumSet.allOf( OCSButton.class ) )
                LOOKUP.put( s.getCode(), s );
        }

        /**
         * Enumeration value.
         */
        private final byte code;




        private OCSButton ( byte c ) {
            this.code = c;
        }


        /**
         * Get the constant value of enumeration.
         * @return Returns the constant value of enumeration.
         */
        public byte getCode () {
            return this.code;
        }


        /**
         * Get an instance of enumeration by the constant value.
         * @param code  It specifies the constant value of enumeration.
         * @return Returns an instance of enumeration on success; otherwise, returns {@code null}.
         */
        public static OCSButton get ( byte code ) {
            return LOOKUP.get( code );
        }
    } 
}
