package comm;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;

import comm.agent.AgentPacket;
import comm.util.Endian;



public class Parser_Deploy {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * Deployment data.
     */
    private byte[] raw;
    
    /**
     * The size of floor data.
     */
    private static final int FLOOR_SIZE = 897;




    /**
     * Deployment data parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_Deploy ( String hostname, int port ) {
        this.agent = MON_MGR.getAgent( hostname, port );
        this.raw   = this.agent.deploy;
    }


    /**
     * Get the register bitmask of group.
     * @param floor     It specifies the floor number (It starts from 0).
     * @param ipindex   It specifies the IP index (e.g. 192.168.1.254 = 15, 192.168.1.253 = 14 and so on).
     * @return Returns the register bitmask of group (15th bit specifies 192.168.1.254, 14th bit specifies 192.168.1.253 and so on).
     */
    public int getGroupRegBitmask ( byte floor, byte ipindex ) {
        synchronized ( this.raw ) {
            return Endian.getShort( this.raw, FLOOR_SIZE + ipindex * 4 * 128 + floor * 4 ) & 0xFFFF;
        }
    }


    /**
     * Set the register bitmask of group.
     * @param floor     It specifies the floor number (It starts from 0).
     * @param ipindex   It specifies the IP index (e.g. 192.168.1.254 = 15, 192.168.1.253 = 14 and so on).
     * @param bitmask   It specifies the bitmask of register of group
     *                  (15th bit specifies 192.168.1.254, 14th bit specifies 192.168.1.253 and so on).
     */
    public void setGroupRegBitmask ( byte floor, byte ipindex, short bitmask ) {
        synchronized ( this.raw ) {
            System.arraycopy( Endian.getShortByteArray( bitmask ), 0, this.raw, FLOOR_SIZE + ipindex * 4 * 128 + floor * 4, 2 );
        }
    }


    /**
     * Get the clear bitmask of group.
     * @param floor     It specifies the floor number (It starts from 0).
     * @param ipindex   It specifies the IP index (e.g. 192.168.1.254 = 15, 192.168.1.253 = 14 and so on).
     * @return Returns the clear bitmask of group (15th bit specifies 192.168.1.254, 14th bit specifies 192.168.1.253 and so on).
     */
    public int getGroupClrBitmask ( byte floor, byte ipindex ) {
        synchronized ( this.raw ) {
            return Endian.getShort( this.raw, FLOOR_SIZE + ipindex * 4 * 128 + floor * 4 + 2 ) & 0xFFFF;
        }
    }


    /**
     * Set the clear bitmask of group.
     * @param floor     It specifies the floor number (It starts from 0).
     * @param ipindex   It specifies the IP index (e.g. 192.168.1.254 = 15, 192.168.1.253 = 14 and so on).
     * @param bitmask   It specifies the bitmask of clear of group
     *                  (15th bit specifies 192.168.1.254, 14th bit specifies 192.168.1.253 and so on).
     */
    public void setGroupClrBitmask ( byte floor, byte ipindex, short bitmask ) {
        System.arraycopy( Endian.getShortByteArray( bitmask ), 0, this.raw, FLOOR_SIZE + ipindex * 4 * 128 + floor * 4 + 2, 2 );
    }


    /**
     * Get the door zone status of specified floor.
     * @param floor It specifies the floor number (It starts from 0).
     * @return Returns {@code -1} once no door zone defined, otherwise, returns the corresponding door zone.
     */
    public int getDoorZone ( byte floor ) {
        synchronized ( this.raw ) {
            return this.raw[ 385 + floor ];
        }
    }


    /**
     * Set the door zone status of specified floor.
     * @param floor It specifies the floor number (It starts from 0).
     * @param dz    It specifies the door zone (-1 specifies no door zone in the {@code floor}).
     */
    public void setDoorZone ( byte floor, byte dz ) {
        synchronized ( this.raw ) {
            this.raw[ 385 + floor ] = dz;
        }
    }


    /**
     * Get the total number of floor in the environment.
     * @return Returns the number of floor configured in OCS.
     */
    public byte getFloorCount () {
        synchronized ( this.raw ) {
            return this.raw[ 0 ];
        }
    }


    /**
     * Set the total number of floor in the environment.
     * @param count It specifies the total number of floor.
     */
    public void setFloorCount ( byte count ) {
        synchronized ( this.raw ) {
            this.raw[ 0 ] = count;
        }
    }


    /**
     * Get the floor text of specified floor.
     * @param floor It specifies the floor number (It starts from 0).
     * @return Returns the floor text (maximum 3 characters) of specified floor.
     */
    public String getFloorText ( byte floor ) {
        synchronized ( this.raw ) {
            return new String( Arrays.copyOfRange( this.raw, 1 + floor * 3, 1 + floor * 3 + 3 ) ).trim();
        }
    }


    /**
     * Set the floor text of specified floor.
     * @param text  It specifies the floor text (maximum 3 characters).
     */
    public void setFloorText ( byte floor, String text ) {
        int pos = 1 + floor * 3;
        synchronized ( this.raw ) {
            this.raw[ pos ]     = text.length() <= 0 ? ( byte )' ' : ( byte )text.charAt( 0 );
            this.raw[ pos + 1 ] = text.length() <= 1 ? ( byte )' ' : ( byte )text.charAt( 1 );
            this.raw[ pos + 2 ] = text.length() <= 2 ? ( byte )' ' : ( byte )text.charAt( 2 );
        }
    }


    /**
     * Send local copy of Deployment data to OCS.
     */
    public void commit () {
        byte[] ret = new byte[ this.raw.length + 1 ];
        synchronized ( this.raw ) {
            System.arraycopy( this.raw, 0, ret, 1, this.raw.length );
        }
        ret[ 0 ] = AgentPacket.PACKET_DEPLOY;
        this.agent.send( ret );
    }
}
