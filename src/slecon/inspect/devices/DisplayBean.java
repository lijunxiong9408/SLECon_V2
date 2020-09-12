package slecon.inspect.devices;
import comm.constants.DeviceArrow;
import comm.constants.DeviceMessage;
import comm.constants.OcsDirection;




public class DisplayBean {
    private OcsDirection direction;
    private String    position;
    private DeviceArrow arrow;
    private DeviceMessage   msg;




    public DisplayBean () {
        super();
    }


    public DisplayBean ( OcsDirection direction, String position, DeviceArrow arrow, DeviceMessage msg ) {
        super();
        this.direction = direction;
        this.position  = position;
        this.arrow     = arrow;
        this.msg       = msg;
    }


    public OcsDirection getDirection () {
        return direction;
    }


    public void setDirection ( OcsDirection direction ) {
        this.direction = direction;
    }


    public String getPosition () {
        return position;
    }


    public void setPosition ( String position ) {
        this.position = position;
    }


    public DeviceArrow getArrow () {
        return arrow;
    }


    public void setArrow ( DeviceArrow arrow ) {
        this.arrow = arrow;
    }


    public DeviceMessage getMsg () {
        return msg;
    }


    public void setMsg ( DeviceMessage msg ) {
        this.msg = msg;
    }
}
