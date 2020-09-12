package slecon.inspect.devices;

public class DeviceTableItem {
    private Integer    id;
    private String deviceType;




    public DeviceTableItem ( Integer id, String deviceType ) {
        super();
        this.id         = id;
        this.deviceType = deviceType;
    }


    public Integer getId () {
        return id;
    }


    public String getDeviceType () {
        return deviceType;
    }
}
