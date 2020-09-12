package slecon.inspect.devices;
import java.awt.BorderLayout;

import javax.swing.JPanel;

import comm.constants.DeviceType;
import slecon.inspect.devices.devicepanel.DM3ADevicePanel;
import slecon.inspect.devices.devicepanel.GP32DevicePanel;
import slecon.inspect.devices.devicepanel.GP4GDevicePanel;




public class DeviceView extends JPanel {
    private static final long serialVersionUID = -8749927472963219095L;
    private DevicePanel       dp;
    private DeviceType        board;
    private GongBean          gongbean;
    private DisplayBean       displaybean;
    private DeviceInfoBean    deviceinfobean;
    private IoStatusBean      iostatusbean;




    public DeviceView () {
        super( new BorderLayout() );
    }


    public void setGong ( GongBean gongbean ) {
        this.gongbean = gongbean;
        if ( dp != null )
            dp.setGong( gongbean );
    }


    public void setDisplay ( DisplayBean displaybean ) {
        this.displaybean = displaybean;
        if ( dp != null )
            dp.setDisplay( displaybean );
    }


    public void setDeviceInfo ( DeviceInfoBean deviceinfobean ) {
        this.deviceinfobean = deviceinfobean;
        if ( dp != null )
            dp.setDeviceInfo( deviceinfobean );
    }


    public void setIoStatus ( IoStatusBean iostatusbean ) {
        this.iostatusbean = iostatusbean;
        if ( dp != null )
            dp.setIoStatus( iostatusbean );
    }


    public void setView ( DeviceType board ) {
        if ( this.board == board ) {
            return;
        }
        this.board = board;
        if ( board == null ) {
            removeAll();
            repaint();
            return;
        }
        switch ( board ) {
        case DM3A :
        case DM2A : {
            dp = new DM3ADevicePanel();
            break;
        }
        case GP4G :
            dp = new GP4GDevicePanel();
            break;
        case GP32 :
            dp = new GP32DevicePanel();
            break;
        default :
            dp = null;
            return;
        }
        removeAll();
        if ( dp != null ) {
            add( dp );
            dp.setGong( gongbean );
            dp.setDisplay( displaybean );
            dp.setDeviceInfo( deviceinfobean );
            dp.setIoStatus( iostatusbean );
        }
    }
}
