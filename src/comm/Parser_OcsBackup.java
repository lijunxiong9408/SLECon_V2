package comm;
import static logic.util.SiteManagement.MON_MGR;

import comm.util.Endian;


public class Parser_OcsBackup {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * OCS backup data.
     */
    private final byte[] ocs_backup;


    /**
     * OCS configuration parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_OcsBackup ( String hostname, int port ) {
        this.agent      = MON_MGR.getAgent( hostname, port );
        this.ocs_backup = this.agent.ocs_backup;
    }


    /**
     * Get operation step.
     */
    public int getOperateStep () {
        synchronized ( this.ocs_backup ) {
            return this.ocs_backup[ 0 ];
        }
    }


    /**
     * Get backup data md5.
     */
    public String getBackupDataMD5 () {
    	byte [] ret = new byte[64];
        synchronized ( this.ocs_backup ) {
           System.arraycopy( this.ocs_backup, 1, ret, 0, 64 );
        }
        String str = new String(ret).trim();

        return str;
    }
    
    /**
     * Backup data count.
     */
    public int getPacketDataCount () {
        synchronized ( this.ocs_backup ) {
           return Endian.getInt( this.ocs_backup, 65 );
        }
    }
    
    /**
     * Backup data.
     * */
    public byte[] getBackupData () {
    	byte[] dat = new byte[getPacketDataCount()];
    	synchronized ( this.ocs_backup ) {
            System.arraycopy( this.ocs_backup, 69, dat, 0, getPacketDataCount() );
        }
    	return dat;
    }
}
