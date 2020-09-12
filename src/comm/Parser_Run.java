package comm;
import static logic.util.SiteManagement.MON_MGR;

import comm.constants.CallDef;
import comm.constants.DisCallDef;



public class Parser_Run {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * Run data.
     */
    private final byte run[];




    /**
     * Run parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_Run ( String hostname, int port ) {
        this.agent = MON_MGR.getAgent( hostname, port );
        this.run   = this.agent.run;
    }


    /**
     * Get the floor on last run.
     * @return Returns the floor which the elevator last run (It starts from 0).
     */
    public byte getLastRunFloor () {
        synchronized ( this.run ) {
            return this.run[ 0 ];
        }
    }


    /**
     * Get the lowest floor of the environment.
     * @return Returns the lowest floor the elevator able to run (It starts from 0).
     */
    public byte getLowerFloor () {
        synchronized ( this.run ) {
            return this.run[ 1 ];
        }
    }


    /**
     * Get the highest floor of the environment.
     * @return Returns the highest floor the elevator able to run (It starts from 0).
     */
    public byte getUpperFloor () {
        synchronized ( this.run ) {
            return this.run[ 2 ];
        }
    }


    /**
     * Whether the specified call definition is triggered.
     * @param floor     It specifies the floor.
     * @param type      It specifies the {@code CallDef}.
     * @return Returns {@code true} once the call definition is triggered; otherwise, returns {@code false}.
     */
    public boolean IsCallDef ( short floor, CallDef type ) {
        synchronized ( this.run ) {
            return ( this.run[ 3 + floor ] & type.getCode() ) != 0;
        }
    }


    /**
     * Whether the open front door command is sent on last run.
     * @return Returns {@code true} once the open front door command is sent on last run; otherwise, returns {@code false}.
     */
    public boolean isOpenFrontDoor () {
        synchronized ( this.run ) {
            return ( run[ 131 ] & 1 ) != 0;
        }
    }


    /**
     * Whether the open rear door command is sent on last run.
     * @return Returns {@code true} once the open rear door command is sent on last run; otherwise, returns {@code false}.
     */
    public boolean isOpenRearDoor () {
        synchronized ( this.run ) {
            return ( run[ 131 ] & 2 ) != 0;
        }
    }


    /**
     * Whether the run command is sent on last run.
     * @return Returns {@code true} once the run command is sent on last run; otherwise, returns {@code false}.
     */
    public boolean isRunCmd () {
        synchronized ( this.run ) {
            return ( run[ 131 ] & 4 ) != 0;
        }
    }
    
    /**
     * Whether the specified call definition is triggered.
     * @param floor     It specifies the floor.
     * @param type      It specifies the {@code CallDef}.
     * @return Returns {@code true} once the call definition is triggered; otherwise, returns {@code false}.
     */
    public boolean IsDisCallDef ( short floor, DisCallDef type ) {
        synchronized ( this.run ) {
            return ( this.run[ 132 + floor ] & type.getCode() ) != 0;
        }
    }
}
