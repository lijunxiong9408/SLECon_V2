package slecon.inspect.devices;
import comm.constants.DeviceType;




public class DeviceInfoBean {
    private Integer    deviceID;
    private DeviceType deviceType;
    private Integer    serail;
    private String     powermode;
    private String     status;
    private String     harewareVersion;
    private String     firmwareVersion;




    public DeviceInfoBean () {
        super();
    }


    public DeviceInfoBean ( DeviceType deviceType, String harewareVersion, String firmwareVersion ) {
        super();
        this.deviceID        = null;
        this.deviceType      = deviceType;
        this.serail          = null;
        this.powermode       = "not implement";
        this.status          = "not implement";
        this.harewareVersion = harewareVersion;
        this.firmwareVersion = firmwareVersion;
    }


    public Integer getDeviceID () {
        return deviceID;
    }


    public void setDeviceID ( Integer deviceID ) {
        this.deviceID = deviceID;
    }


    public DeviceType getDeviceType () {
        return deviceType;
    }


    public void setDeviceType ( DeviceType deviceType ) {
        this.deviceType = deviceType;
    }


    public Integer getSerail () {
        return serail;
    }


    public void setSerail ( Integer serail ) {
        this.serail = serail;
    }


    public String getPowermode () {
        return powermode;
    }


    public void setPowermode ( String powermode ) {
        this.powermode = powermode;
    }


    public String getStatus () {
        return status;
    }


    public void setStatus ( String status ) {
        this.status = status;
    }


    public String getHarewareVersion () {
        return harewareVersion;
    }


    public void setHarewareVersion ( String harewareVersion ) {
        this.harewareVersion = harewareVersion;
    }


    public String getFirmwareVersion () {
        return firmwareVersion;
    }


    public void setFirmwareVersion ( String firmwareVersion ) {
        this.firmwareVersion = firmwareVersion;
    }


    @Override
    public String toString () {
        return "DeviceInfoBean [deviceID=" + deviceID + ", deviceType=" + deviceType + ", serail=" + serail + ", powermode=" + powermode
               + ", status=" + status + ", harewareVersion=" + harewareVersion + ", firmwareVersion=" + firmwareVersion + "]";
    }
}
