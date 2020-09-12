package comm;

import static logic.util.SiteManagement.MON_MGR;

import comm.agent.AgentPacket;

public class Parser_DoorEnable {
	private final Agent agent;
	
	private byte[] door_enable;
	
	public Parser_DoorEnable ( String hostname, int port ) {
		this.agent = MON_MGR.getAgent( hostname, port );
		this.door_enable = this.agent.doorenbale;
	}
	
	public byte[] getDoor_Enable_table() {
		synchronized ( door_enable ) {
			return door_enable;
		}
	}
	
	public void setDoor_Enable_table(byte[] table) {
		synchronized ( door_enable ) {
			this.door_enable = table;
		}
	}
	
	 /**
     * Send data to OCS.
     */
    public void commit () {
        byte[] ret = new byte[ this.door_enable.length + 1 ];
        synchronized ( this.door_enable ) {
            System.arraycopy( this.door_enable, 0, ret, 1, this.door_enable.length );
        }
        ret[ 0 ] = AgentPacket.PACKET_DOOR_ENABLE;
        this.agent.send( ret );
    }
}
