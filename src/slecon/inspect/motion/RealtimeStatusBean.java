package slecon.inspect.motion;
public class RealtimeStatusBean {
    private final String positon;
    private final String speed;
    private final String accelearation;
    private final String jerk;
    private final String destination;
    private final String brakeDisplacement;




    public RealtimeStatusBean(String positon, String speed, String accelearation, String jerk, String destination, String brakeDisplacement) {
        super();
        this.positon = positon;
        this.speed = speed;
        this.accelearation = accelearation;
        this.jerk = jerk;
        this.destination = destination;
        this.brakeDisplacement = brakeDisplacement;
    }


    public String getPositon () {
        return positon;
    }


    public String getSpeed () {
        return speed;
    }


    public String getAccelearation () {
        return accelearation;
    }


    public String getJerk () {
        return jerk;
    }


    public String getDestination () {
        return destination;
    }


    public String getBrakeDisplacement () {
        return brakeDisplacement;
    }

}
