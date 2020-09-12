package comm;

import static logic.util.SiteManagement.MON_MGR;

import comm.agent.AgentPacket;

public class Parser_NetWork {
	private final Agent agent;
	
	private byte[] netWork;
	
	public Parser_NetWork ( String hostname, int port ) {
		this.agent = MON_MGR.getAgent( hostname, port );
		this.netWork = this.agent.netWork;
	}
	
	public byte[] getLocalIP() {
		synchronized ( netWork ) {
			byte[] b = new byte[ 16 ];
            System.arraycopy( netWork, 0, b, 0, 16 );
            return b;
		}
	}
	
	public byte[] getLocalMask() {
		synchronized ( netWork ) {
			byte[] b = new byte[ 16 ];
            System.arraycopy( netWork, 16, b, 0, 16 );
            return b;
		}
	}
	
	public byte[] getLocalGateway() {
		synchronized ( netWork ) {
			byte[] b = new byte[ 16 ];
            System.arraycopy( netWork, 32, b, 0, 16 );
            return b;
		}
	}
	
	public void setLocalIP(byte[] b) {
		synchronized ( netWork ) {
			System.arraycopy( b, 0, netWork, 0, b.length );
		}
	}
	
	public void setLocalMask(byte[] b) {
		synchronized ( netWork ) {
			System.arraycopy( b, 0, netWork, 16, b.length );
		}
	}
	
	public void setLocalGateWay(byte[] b) {
		synchronized ( netWork ) {
			System.arraycopy( b, 0, netWork, 32, b.length );
		}
	}
	
	 /**
     * Send data to OCS.
     */
    public void commit () {
        byte[] ret = new byte[ this.netWork.length + 1 ];
        synchronized ( this.netWork ) {
            System.arraycopy( this.netWork, 0, ret, 1, this.netWork.length );
        }
        ret[ 0 ] = AgentPacket.PACKET_REQ_UPDATE_LOCAL_IP;
        this.agent.send( ret );
    }
}
