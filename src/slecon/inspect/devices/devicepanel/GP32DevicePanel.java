package slecon.inspect.devices.devicepanel;
import java.awt.Color;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
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




public class GP32DevicePanel extends DevicePanel {
    private static final long           serialVersionUID = -3148803996836929636L;
    private static final ResourceBundle bundle           = ToolBox.getResourceBundle( "inspect.DevicePanel" );
    private DeviceInfoView deviceInfoView;
    private DisplayView    displayView;
    private IOStatusView   ioStatusView;




    /**
     * Create the panel.
     */
    public GP32DevicePanel () {
        initGUI();
    }


    private void initGUI () {
        setLayout( new MigLayout( "nogrid, ins 20", "[][]", "[180!]20![fill]" ) );
        setBackground( Color.white );
        setBorder( new TitledBorder( null, bundle.getString( "DeviceView.title" ), TitledBorder.LEADING, TitledBorder.TOP, null, null ) );
        deviceInfoView = new DeviceInfoView();
        add( deviceInfoView, "span, split 2, wrap" );

//      {
//          JPanel controlPanel = new JPanel(new MigLayout("", "[fill]"));
//          controlPanel.setOpaque(false);
//          controlPanel.setBorder(new TitledBorder(null, "Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//          add(controlPanel, "span, wrap,alignx right");
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
        ioStatusView = new IOStatusView();
        add( ioStatusView, "span, push, hidemode 3" );
        displayView = new DisplayView();
        add( displayView, "hidemode 3" );
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


    public static void main ( String... args ) {
        JFrame f = new JFrame();
        JPanel p = new GP32DevicePanel();
        f.getContentPane().add( p );
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.pack();
        f.setVisible( true );
    }
}
