package comm;
import static logic.util.SiteManagement.MON_MGR;

import comm.constants.DoorAction;
import comm.constants.McsStatus;
import comm.constants.OcsDirection;
import comm.constants.OcsModule;
import comm.util.Endian;




public class Parser_Status {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * Status data.
     */
    private final byte[] status;




    /**
     * Status parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_Status ( String hostname, int port ) {
        this.agent  = MON_MGR.getAgent( hostname, port );
        this.status = this.agent.status;
    }


    /**
     * Get the status of Top Floor Switch.
     * @return Returns the status of Top Floor Switch.
     */
    public boolean isTF () {
        synchronized ( this.status ) {
            return ( this.status[ 0 ] & 1 ) != 0;
        }
    }


    /**
     * Get the status of Bottom Floor Switch.
     * @return Returns the status of Bottom Floor Switch.
     */
    public boolean isBF () {
        synchronized ( this.status ) {
            return ( this.status[ 0 ] & 2 ) != 0;
        }
    }


    /**
     * Get the status of Cut Hall Call Switch.
     * @return Returns the status of Cut Hall Call Switch.
     */
    public boolean isCHC () {
        synchronized ( this.status ) {
            return ( this.status[ 0 ] & 4 ) != 0;
        }
    }


    /**
     * Get the status of Disable Door Open Switch.
     * @return Returns the status of Disable Door Open Switch.
     */
    public boolean isDDO () {
        synchronized ( this.status ) {
            return ( this.status[ 0 ] & 8 ) != 0;
        }
    }


    /**
     * Returns the status of direction.
     * @return Returns {@code Direction}.
     */
    public OcsDirection getDirection () {
        synchronized ( this.status ) {
            return OcsDirection.get( ( byte )( this.status[ 0 ] >> 4 & 0x03 ) );
        }
    }


    /**
     * Check whether the elevator is running. Once the elevator is running, the direction plays animation.
     * @return Returns {@code true} once the direction is playing animation; otherwise, returns {@code false}.
     */
    public boolean isDirectionAnimation () {
        synchronized ( this.status ) {
            return ( this.status[ 0 ] & 64 ) != 0;
        }
    }


    /**
     * Get the floor (It starts from 0).
     * @return Return the current floor of elevator.
     */
    public byte getFloor () {
        synchronized ( this.status ) {
            return this.status[ 1 ];
        }
    }


    /**
     * Get the recent module of OCS.
     * @return Returns {@code OcsModule}.
     */
    public OcsModule getOCSModule () {
        synchronized ( this.status ) {
            return OcsModule.get( this.status[ 2 ] );
        }
    }


    /**
     * Get the count of position.
     * @return Returns the current position count of elevator.
     */
    public long getPositionCount () {
        synchronized ( this.status ) {
            return Endian.getInt( this.status, 3 );
        }
    }


    /**
     * Get the count of brake displacement.
     * @return Returns current brake displacement. It could be a negative integer.
     */
    public long getBrakeDisplacement () {
        synchronized ( this.status ) {
            return Endian.getInt( this.status, 7 );
        }
    }


    /**
     * Get the cabin load.
     * @return Returns the loading percentage of elevator.
     */
    public byte getCabinLoad () {
        synchronized ( this.status ) {
            return this.status[ 11 ];
        }
    }


    /**
     * Get the speed of elevator.
     * @return Returns the current speed of elevator.
     */
    public float getSpeed () {
        synchronized ( this.status ) {
            return Endian.getFloat( this.status, 12 );
        }
    }


    /**
     * MCS IO table.
     * @return Returns the latest status of MCS I/O.
     */
    public byte[] getMcsIO () {
        byte[] ret = new byte[ 16 ];
        synchronized ( this.status ) {
            System.arraycopy( this.status, 16, ret, 0, 16 );
        }
        return ret;
    }
    

    /**
     * MCS Status word table is private in parser.
     */
    public int getMcsStatusWordOffset () {
        return 32;
    }


    /**
     * Get the real time clock on the status packet sent from OCS.
     * This time stamp helpful to determine elapse time on status packets.
     * @return Returns the time-stamp on the latest status packet sent from OCS.
     */
    public long getTime () {
        synchronized ( this.status ) {
            return Endian.getLong( this.status, 46 );
        }
    }
    
    
    /**
     * Updata Data count.
     * @return Returns the latest status of Update Count.
     */
    public int getUpdateDataCount () {
        synchronized ( this.status ) {
           return Endian.getInt( this.status, 54 );
        }
    }
    
    /**
     * Updata Data MD5.
     * @return Returns the latest MD5 of Update Count.
     */
    public String getUpdateDataMD5 () {
    	byte [] ret = new byte[64];
        synchronized ( this.status ) {
           System.arraycopy( this.status, 58, ret, 0, 64 );
        }
        String str = new String(ret).trim();

        if( str.indexOf("None") != -1 ) {
        	str = "--";
        }

        return str;
    }
    
    /**
     * Get the status of door.
     * @param door  It specifies the position of door. {@code true}(Front door) and {@code false}(Rear door).
     * @return Returns {@code DoorAction}.
     * 
     * sgs 1 normal 0 fail
     * edp 1 normal 0 fail
     */
    public DoorAction getDoorStatus ( boolean door ) {
        synchronized ( this.status ) {
            int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + ( door ? 10 : 12 ) );
            DoorAction action = DoorAction.get( ( byte )( word >> 6 & 0x07 ) );
            // first FORCED_CLOSE
            if( action == DoorAction.FORCED_CLOSE ) {
            	return DoorAction.FORCED_CLOSE;
            }
            if ( (( word >> 3 ) & 0x01) == 0 || (( word >> 4 ) & 0x01) == 0 ) {
            	return DoorAction.SGS;
            }
            return action;
        }
    }


    /**
     * Get the status of MCS.
     * @return Returns {@code McsStatus}.
     */
    public McsStatus getMcsStatus () {
        synchronized ( this.status ) {
            int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + 0 );
            if ( ( word & 2 ) != 0 )
                return McsStatus.PWDN;
            if ( ( word & 4 ) != 0 )
                return McsStatus.PASV;
            if ( ( word & 8 ) != 0 )
                return McsStatus.IDLE;
            if ( ( word & 128 ) != 0 )
                return McsStatus.INST;
            if ( ( word & 256 ) != 0 )
                return McsStatus.SDA;
            if ( ( word & 512 ) != 0 )
                return McsStatus.TDA;
            return McsStatus.NORMAL;
        }
    }
    
    public boolean isInstallationMode () {
        synchronized ( this.status ) {
            int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + 0 );
            return ( word & 128 ) != 0;
        }
    }
    
    
    public boolean isSuspendDCSAutomation () {
        synchronized ( this.status ) {
            int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + 0 );
            return ( word & 256 ) != 0;
        } 
    }
    
    
    public boolean isTemporaryDriverActivation () {
        synchronized ( this.status ) {
            int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + 0 );
            return ( word & 512 ) != 0;
        } 
    }
    
    public boolean getLoadweightType () {
        synchronized ( this.status ) {
            int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + 0 );
            return ( word & 4096 ) != 0;
        } 
    }
    
    public int getInspectMode () {
        synchronized ( this.status ) {
            int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + 0 );
            return (word >> 13) & 0x07;
        } 
    }
    
    public boolean getMcexMode () {
        synchronized ( this.status ) {
            int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + 0 );
            return (word & 64) != 0 ;
        } 
    }
    
    public boolean getDOR ( boolean door ) {
        synchronized ( this.status ) {
        int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + ( door ? 10 : 12 ) );
        return ( word & 1 ) != 0;
        }
    }
    
    
    public boolean getDOL ( boolean door ) {
        synchronized ( this.status ) {
        int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + ( door ? 10 : 12 ) );
        return ( word & 2 ) != 0;
        }
    }
    
    
    public boolean getDCL ( boolean door ) {
        synchronized ( this.status ) {
        int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + ( door ? 10 : 12 ) );
        return ( word & 4 ) != 0;
        }
    }
    
    
    public boolean getSGS ( boolean door ) {
        synchronized ( this.status ) {
        int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + ( door ? 10 : 12 ) );
        return ( word & 8 ) != 0;
        }
    }
    
    
    public boolean getEDP ( boolean door ) {
        synchronized ( this.status ) {
        int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + ( door ? 10 : 12 ) );
        return ( word & 16 ) != 0;
        }
    }
    
    
    public boolean getLight ( boolean door ) {
        synchronized ( this.status ) {
        int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + ( door ? 10 : 12 ) );
        return ( word & 2048 ) != 0;
        }
    }
    
    
    /**
     * Get the fan status of door.
     * @param door  It specifies the position of door. {@code true}(Front door) and {@code false}(Rear door).
     * @return
     */
    public boolean getFan ( boolean door ) {
        synchronized ( this.status ) {
        int word = Endian.getShort( this.status, this.getMcsStatusWordOffset() + ( door ? 10 : 12 ) );
        return ( word & 4096 ) != 0;
        }
    }
    
    
    public byte[] getLoadSenorValue() {
    	byte[] ret = new byte[ 8 ];
        synchronized ( this.status ) {
            System.arraycopy( this.status, 122, ret, 0, 8 );
        }
        return ret;
    }
    
    public byte getCurrentTemp() {
        synchronized ( this.status ) {
            return this.status[ 130 ];
        }
    }
}
