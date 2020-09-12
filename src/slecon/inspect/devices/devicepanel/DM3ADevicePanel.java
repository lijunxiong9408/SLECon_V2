package slecon.inspect.devices.devicepanel;
import java.awt.Color;
import java.util.ResourceBundle;

import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import slecon.ToolBox;
import slecon.inspect.devices.DeviceInfoBean;
import slecon.inspect.devices.DevicePanel;
import slecon.inspect.devices.DisplayBean;
import slecon.inspect.devices.GongBean;
import slecon.inspect.devices.IoStatusBean;
import slecon.inspect.devices.subview.DeviceInfoView;
import slecon.inspect.devices.subview.DisplayView;
import slecon.inspect.devices.subview.IOStatusView;




public class DM3ADevicePanel extends DevicePanel {
    private static final long           serialVersionUID = 1156341961743351195L;
    private static final ResourceBundle bundle           = ToolBox.getResourceBundle( "inspect.DevicePanel" );
    private DeviceInfoView deviceInfoView;
    private DisplayView    displayView;
    private IOStatusView   ioStatusView;




    /**
     * Create the panel.
     */
    public DM3ADevicePanel () {
        initGUI();
        setIoStatus( new IoStatusBean( 32, 0xF0F42318, 0x82471310, 0x1001, 0x2002, 0x4004 ) );
    }


    private void initGUI () {
        setLayout( new MigLayout( "nogrid, ins 20", "[][]", "[180!]16![fill]" ) );
        setBackground( Color.white );
        setBorder( new TitledBorder( null, bundle.getString( "DeviceView.title" ), TitledBorder.LEADING, TitledBorder.TOP, null, null ) );
        deviceInfoView = new DeviceInfoView();
        add( deviceInfoView, "span, split 2, wrap" );

//      {
//          JPanel controlPanel = new JPanel(new MigLayout("", "[fill]"));
//          controlPanel.setOpaque(false);
//          controlPanel.setBorder(new TitledBorder(null, "Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//          add(controlPanel, "span,alignx right, wrap");
//
//          JButton btnRefresh = new JButton("Refresh");
//          controlPanel.add(btnRefresh, "wrap");
//
//          JButton btnPowerDown = new JButton("Power Down");
//          controlPanel.add(btnPowerDown, "wrap");
//
//          JButton btnReset = new JButton("Reset");
//          controlPanel.add(btnReset, "wrap");
//      }
        displayView = new DisplayView();
        add( displayView, "span, split 2, hidemode 3" );
        ioStatusView = new IOStatusView();
        add( ioStatusView, "push, gapleft 24px, hidemode 3" );
    }


    @Override
    public void setDeviceInfo ( DeviceInfoBean info ) {
        deviceInfoView.setDeviceInfo( info );
    }


    @Override
    public void setDisplay ( DisplayBean display ) {
        displayView.setDisplay( display );
    }


    @Override
    public void setIoStatus ( IoStatusBean bean ) {
        ioStatusView.setIoStatus( bean );
    }


    @Override
    public void setGong ( GongBean gone ) {
    }
}
