package slecon.inspect.devices.subview;
import java.awt.Color;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.inspect.devices.DeviceInfoBean;
import base.cfg.FontFactory;




public class DeviceInfoView extends JPanel {
    private static final long           serialVersionUID = -6161128998745923204L;
    private static final ResourceBundle bundle           = ToolBox.getResourceBundle("inspect.DevicePanel");
    private DeviceInfoBean deviceInfo = null;
    private JLabel         labelDeviceID;
    private JLabel         labelDeviceType;
    private JLabel         labelSerialNumber;
    private JLabel         labelPowerMode;
    private JLabel         labelStatus;
    private JLabel         labelHarewareVersion;
    private JLabel         labelFirmwareVersion;
    private JLabel         lblDeviceID;
    private JLabel         lblDeviceType;
    private JLabel         lblSerial;
    private JLabel         lblPowerMode;
    private JLabel         lblStatus;
    private JLabel         lblHWVersion;
    private JLabel         lblFWVersion;




    /**
     * Create the panel.
     */
    public DeviceInfoView () {
        initGUI();
        updateI18nGUI();
    }


    private void updateI18nGUI () {
        labelDeviceID.setText( bundle.getString( "DeviceID.text" ) );
        labelDeviceType.setText( bundle.getString( "DeviceType.text" ) );
        labelSerialNumber.setText( bundle.getString( "SerialNumber.text" ) );
        labelPowerMode.setText( bundle.getString( "PowerMode.text" ) );
        labelStatus.setText( bundle.getString( "Status.text" ) );
        labelHarewareVersion.setText( bundle.getString( "HarewareVersion.text" ) );
        labelFirmwareVersion.setText( bundle.getString( "FirmwareVersion.text" ) );
    }


    private void initGUI () {
        setOpaque( false );
        setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        setLayout( new MigLayout( "ins 10 15 0 15", "[100!, left]40[100!, left]", "[][][][][][][]" ) );
        labelDeviceID = new JLabel( "Device ID" );
        labelDeviceID.setFont( FontFactory.FONT_12_BOLD );
        labelDeviceID.setForeground(Color.WHITE);
        add( labelDeviceID, "cell 0 0" );
        labelDeviceType = new JLabel( "Device Type" );
        labelDeviceType.setFont( FontFactory.FONT_12_BOLD );
        labelDeviceType.setForeground(Color.WHITE);
        add( labelDeviceType, "cell 0 1" );
        labelSerialNumber = new JLabel( "Serial." );
        labelSerialNumber.setFont( FontFactory.FONT_12_BOLD );
        labelSerialNumber.setForeground(Color.WHITE);
        add( labelSerialNumber, "cell 0 2" );
        labelPowerMode = new JLabel( "Power mode" );
        labelPowerMode.setFont( FontFactory.FONT_12_BOLD );
        labelPowerMode.setForeground(Color.WHITE);
        add( labelPowerMode, "cell 0 3" );
        labelStatus = new JLabel( "Status" );
        labelStatus.setFont( FontFactory.FONT_12_BOLD );
        labelStatus.setForeground(Color.WHITE);
        add( labelStatus, "cell 0 4" );
        labelHarewareVersion = new JLabel( "HW Version" );
        labelHarewareVersion.setFont( FontFactory.FONT_12_BOLD );
        labelHarewareVersion.setForeground(Color.WHITE);
        add( labelHarewareVersion, "cell 0 5" );
        labelFirmwareVersion = new JLabel( "FW Version" );
        labelFirmwareVersion.setFont( FontFactory.FONT_12_BOLD );
        labelFirmwareVersion.setForeground(Color.WHITE);
        add( labelFirmwareVersion, "cell 0 6" );
        lblDeviceID = new JLabel( "-" );
        lblDeviceID.setFont( FontFactory.FONT_12_PLAIN );
        lblDeviceID.setForeground(Color.WHITE);
        add( lblDeviceID, "cell 1 0" );
        lblDeviceType = new JLabel( "-" );
        lblDeviceType.setFont( FontFactory.FONT_12_PLAIN );
        lblDeviceType.setForeground(Color.WHITE);
        add( lblDeviceType, "cell 1 1" );
        lblSerial = new JLabel( "-" );
        lblSerial.setFont( FontFactory.FONT_12_PLAIN );
        lblSerial.setForeground(Color.WHITE);
        add( lblSerial, "cell 1 2" );
        lblPowerMode = new JLabel( "-" );
        lblPowerMode.setFont( FontFactory.FONT_12_PLAIN );
        lblPowerMode.setForeground(Color.WHITE);
        add( lblPowerMode, "cell 1 3" );
        lblStatus = new JLabel( "-" );
        lblStatus.setFont( FontFactory.FONT_12_PLAIN );
        lblStatus.setForeground(Color.WHITE);
        add( lblStatus, "cell 1 4" );
        lblHWVersion = new JLabel( "-" );
        lblHWVersion.setFont( FontFactory.FONT_12_PLAIN );
        lblHWVersion.setForeground(Color.WHITE);
        add( lblHWVersion, "cell 1 5" );
        lblFWVersion = new JLabel( "-" );
        lblFWVersion.setFont( FontFactory.FONT_12_PLAIN );
        lblFWVersion.setForeground(Color.WHITE);
        add( lblFWVersion, "cell 1 6" );
    }


    public void setDeviceInfo ( DeviceInfoBean info ) {
        this.deviceInfo = info;
        updateDeviceInfo();
    }


    public DeviceInfoBean getDeviceInfo () {
        return deviceInfo;
    }


    public void updateDeviceInfo () {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                String deviceID   = "-";
                String deviceType = "-";
                String serial     = "-";
                String powerMode  = "-";
                String status     = "-";
                String hwVersion  = "-";
                String fwVersion  = "-";
                if ( deviceInfo != null ) {
                    if ( deviceInfo.getDeviceID() != null )
                        deviceID = String.format("%d (0x%02x)", deviceInfo.getDeviceID(), deviceInfo.getDeviceID());
                    if ( deviceInfo.getDeviceType() != null )
                        deviceType = String.format( "%s", deviceInfo.getDeviceType() );
                    if ( deviceInfo.getSerail() != null )
                        serial = "not implements";
                    if ( deviceInfo.getPowermode() != null )
                        powerMode = "not implements";
                    if ( deviceInfo.getStatus() != null )
                        status = "not implements";
                    if ( deviceInfo.getHarewareVersion() != null )
                        hwVersion = deviceInfo.getHarewareVersion();
                    if ( deviceInfo.getFirmwareVersion() != null )
                        fwVersion = deviceInfo.getFirmwareVersion();
                }
                lblDeviceID.setText( deviceID );
                lblDeviceType.setText( deviceType );
                lblSerial.setText( serial );
                lblPowerMode.setText( powerMode );
                lblStatus.setText( status );
                lblHWVersion.setText( hwVersion );
                lblFWVersion.setText( fwVersion );
                updateUI();
            }
        } );
    }
}
