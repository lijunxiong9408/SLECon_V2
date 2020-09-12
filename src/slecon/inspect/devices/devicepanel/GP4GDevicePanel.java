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
import slecon.inspect.devices.subview.GongView;
import slecon.inspect.devices.subview.IOStatusView;




public class GP4GDevicePanel extends DevicePanel {
    private static final long           serialVersionUID = -5824604854996100614L;
    private static final ResourceBundle bundle           = ToolBox.getResourceBundle( "inspect.DevicePanel" );
    private DeviceInfoView deviceInfoView;
    private GongView       gongView;
    private IOStatusView   ioStatusView;




    /**
     * Create the panel.
     */
    public GP4GDevicePanel () {
        initGUI();
        setGong( null );
    }


    private void initGUI () {
        setLayout( new MigLayout( "ins 20", "[]", "[180!]20![fill]" ) );
        setBackground( Color.white );
        setBorder( new TitledBorder( null, bundle.getString( "DeviceView.title" ), TitledBorder.LEADING, TitledBorder.TOP, null, null ) );
        deviceInfoView = new DeviceInfoView();
        add( deviceInfoView, "span, split, wrap" );

//      {
//          JPanel controlPanel = new JPanel(new MigLayout("", "[fill]"));
//          controlPanel.setOpaque(false);
//          controlPanel.setBorder(new TitledBorder(null, "Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//          add(controlPanel, "alignx right, wrap");
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
        gongView = new GongView();
        add( gongView, "hidemode 3" );
        ioStatusView = new IOStatusView();
        add( ioStatusView, "push" );
    }


    @Override
    public void setDeviceInfo ( DeviceInfoBean info ) {
        deviceInfoView.setDeviceInfo( info );
    }


    @Override
    public void setGong ( GongBean bean ) {
        gongView.setGong( bean );
    }


    @Override
    public void setIoStatus ( IoStatusBean bean ) {
        ioStatusView.setIoStatus( bean );
    }


    @Override
    public void setDisplay ( DisplayBean display ) {
    }


    public static void main ( String... args ) {
        JFrame f = new JFrame();
        JPanel p = new GP4GDevicePanel();
        f.getContentPane().add( p );
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.pack();
        f.setVisible( true );
    }
}
