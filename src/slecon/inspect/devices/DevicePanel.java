package slecon.inspect.devices;
import javax.swing.JPanel;




public abstract class DevicePanel extends JPanel {
    private static final long serialVersionUID = 845616827002184397L;




    public abstract void setGong ( GongBean gone );


    public abstract void setDisplay ( DisplayBean display );


    public abstract void setDeviceInfo ( DeviceInfoBean info );


    public abstract void setIoStatus ( IoStatusBean bean );
}
