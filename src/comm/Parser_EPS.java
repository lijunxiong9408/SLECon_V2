package comm;

import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;

import comm.agent.AgentPacket;
import comm.util.Endian;

public class Parser_EPS {
	private final Agent agent;
	
	private byte[] eps;
	
	public Parser_EPS ( String hostname, int port ) {
		this.agent = MON_MGR.getAgent( hostname, port );
		this.eps = this.agent.eps;
	}
	
	public byte[] getHIPStatus() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 0, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getVBUS() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 4 ];
            System.arraycopy( this.eps, 2, b, 0, 4 );
            return b;
        }
	}
	
	public byte[] getVBAT() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 4 ];
            System.arraycopy( this.eps, 6, b, 0, 4 );
            return b;
        }
	}
	
	public byte[] getCURR() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 4 ];
            System.arraycopy( this.eps, 10, b, 0, 4 );
            return b;
        }
	}
	
	public byte[] getSpeed() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 4 ];
            System.arraycopy( this.eps, 14, b, 0, 4 );
            return b;
        }
	}
	
	public byte[] getDirection() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 18, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getEncoderPulse() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 20, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getTractorWheel() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 22, b, 0, 2 );
            return b;
        }
	}	
	
	public byte[] getSuspend() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 24, b, 0, 2 );
            return b;
        }
	}	
	
	public byte[] getExitTime() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 26, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getChangeTime() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 28, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getNormalVol() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 30, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getLackVol() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 32, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getFullVol() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 34, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getLowVol() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 36, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getProtectVol() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 38, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getChargerVol() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 40, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getOpenVol() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 42, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getReducedVol() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 44, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getNormalPowVol() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 46, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getInverterPowVol() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 48, b, 0, 2 );
            return b;
        }
	}
	
	public byte[] getBrakeOpenTime() {
		synchronized ( this.eps ) {
			byte[] b = new byte[ 2 ];
            System.arraycopy( this.eps, 50, b, 0, 2 );
            return b;
        }
	}
}
