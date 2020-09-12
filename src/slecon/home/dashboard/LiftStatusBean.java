package slecon.home.dashboard;
import comm.constants.DoorAction;
import comm.constants.OcsDirection;
import comm.constants.OcsModule;




public class LiftStatusBean {
    private final OcsModule    module;
    private final String       uptime;
    private final String       position;
    private final OcsDirection direction;
    private final boolean      animation;
    private final DoorAction   frontDoorAction;
	private final DoorAction   rearDoorAction;


    


	public LiftStatusBean ( OcsModule module, String uptime, OcsDirection direction, boolean animation, String position, DoorAction frontDoorAction, DoorAction rearDoorAction ) {
        super();
        this.module = module;
        this.uptime = uptime;
        this.position = position;
        this.direction = direction;
        this.animation = animation;
        this.frontDoorAction = frontDoorAction;
        this.rearDoorAction = rearDoorAction;
    }


    public String getPosition () {
        return position;
    }


    public String getUptime () {
        return uptime;
    }


    public OcsModule getModule () {
        return module;
    }


    public OcsDirection getDirection () {
        return direction;
    }


    public final boolean isAnimation () {
        return animation;
    }


    public DoorAction getFrontDoorAction() {
		return frontDoorAction;
	}
    
    public DoorAction getRearDoorAction() {
		return rearDoorAction;
	}
    
    @Override
    public String toString () {
        return "LiftStatusBean [status=" + module + ", lastMaint=" + uptime + ", position=" + position + ", direction=" + direction + ", frontdoorStatus="
                + frontDoorAction + ",reardoorStatus=" + rearDoorAction +"]";
    }
}
