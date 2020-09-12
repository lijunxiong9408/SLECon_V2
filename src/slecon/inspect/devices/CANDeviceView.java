package slecon.inspect.devices;
import static logic.util.SiteManagement.MON_MGR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import logic.connection.LiftConnectionBean;
import net.miginfocom.swing.MigLayout;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import comm.Parser_Device;
import comm.agent.AgentMessage;
import comm.constants.CANBus;
import comm.constants.DeviceBehavior;
import comm.constants.DeviceType;
import comm.event.LiftDataChangedListener;




public abstract class CANDeviceView extends JPanel implements ActionListener, Page, LiftDataChangedListener {
    private static final long  serialVersionUID = 6525255555494160076L;
    private Integer            selectedDeviceId = null;

    private final LiftConnectionBean connBean;
    private JTable                   table;
    private DeviceView               devicePanel;
    private JLabel                   labelDeviceList;
    private DeviceTableModel         devicesModel;
    private JScrollPane              tableViewport;
    private Parser_Device            device;




    public CANDeviceView ( LiftConnectionBean bean ) {
        initGUI();
        updateI18nGUI();
        this.connBean = bean;
        setSelectedDeviceId( null );
    }


    abstract protected CANBus getLink ();


    abstract protected ResourceBundle getBundle ();


    private void initGUI () {
        setLayout( new MigLayout("fill, inset 20, gap 10", "[150.00:150.00:150.00,fill][575:575,grow,fill]", "[][grow,fill]") );
        labelDeviceList = new JLabel( "Hall Link Device List:" );
        add( labelDeviceList, "cell 0 0,gapy 10 10" );
        table = new JTable();
        table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        table.setFillsViewportHeight( true );
        table.setShowHorizontalLines( false );
        table.setShowGrid( false );
        devicesModel = new DeviceTableModel();
        table.setModel( devicesModel );
        table.getColumnModel().getColumn( 0 ).setPreferredWidth( 40 );
        table.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
            @Override
            public void valueChanged ( ListSelectionEvent e ) {
                if ( e.getSource() == table.getSelectionModel() && table.getRowSelectionAllowed() )
                    do_table_valueChanged( e );
            }
        } );

        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>( devicesModel );
        table.setRowSorter( sorter );

        List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add( new RowSorter.SortKey( 0, SortOrder.ASCENDING ) );
        sorter.setSortKeys( sortKeys );
        table.setBorder( null );
        tableViewport = new JScrollPane( table ) {
            private static final long serialVersionUID = -6857282948114353643L;
            {
                getViewport().setBackground( Color.white );
                setBackground( Color.white );
            }
        };
        add( tableViewport, "cell 0 1,grow" );
        devicePanel = new DeviceView();
        devicePanel.setBackground( Color.white );
        add( devicePanel, "cell 1 0 1 2,gapx 0,grow" );
        devicePanel.setLayout( new BorderLayout() );
    }


    public DeviceView getDevicePanel () {
        return devicePanel;
    }


    public Integer getSelectedDeviceId () {
        return selectedDeviceId;
    }


    public void setSelectedDeviceId ( Integer selectedDeviceId ) {
        getDevicePanel().setView( null );
        this.selectedDeviceId = selectedDeviceId;
        updateRightPanel();
    }


    @Override
    public void actionPerformed ( final ActionEvent e ) {
    }


    protected void do_table_valueChanged ( final ListSelectionEvent e ) {
        int row = table.getSelectionModel().getLeadSelectionIndex();
        if (row != -1 && row < table.getModel().getRowCount()) {
            int modelIndex = table.convertRowIndexToModel(row);
            if (devicesModel.getValueAt(modelIndex, 0) instanceof Integer) {
                int device = ( (Integer) devicesModel.getValueAt(modelIndex, 0) );
                if (selectedDeviceId == null || selectedDeviceId != device) {
                    setSelectedDeviceId(device);
                }
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setDeviceTable ( ArrayList<DeviceTableItem> items ) {
        final ArrayList<DeviceTableItem> list = (items == null) ? new ArrayList<DeviceTableItem>() : items;
        SwingUtilities.invokeLater( new Runnable() {
            public void run () {
                Integer deviceId = selectedDeviceId;
                devicesModel.setAllItem( list );
                
                table.updateUI();
                tableViewport.updateUI();
                
                if ( deviceId != null ) {
                    int index = devicesModel.getRowIndexByID( deviceId );
                    if (index != -1 && index < table.getModel().getRowCount()) {
                        int tableIndex = table.convertRowIndexToView(index);
                        if (tableIndex == -1) {
                            table.clearSelection();
                        } else if( tableIndex != table.getSelectionModel().getLeadSelectionIndex() )
                            table.setRowSelectionInterval(tableIndex, tableIndex);
                    }
                }
            }
        } );
    }


    public void updateI18nGUI () {
        labelDeviceList.setText( getBundle().getString( "DeviceTableHeader.text" ) );
    }


    @Override
    public void onCreate ( Workspace workspace ) {
    }


    @Override
    public void onStart () {
        try {
            device = new Parser_Device(connBean.getIp(), connBean.getPort());
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                    AgentMessage.DEVICE.getCode() | 
                    AgentMessage.ERROR.getCode() );
            updateDeviceTable();
            updateRightPanel();
        } catch(Exception e) {
            
        }
    }


    @Override
    public void onStop () {
        removeAll();
        MON_MGR.removeEventListener(this);
    }


    @Override
    public void onResume () {}


    @Override
    public void onPause () {}


    @Override
    public void onDestroy () {
    }
    
    
    @Override 
    public void onConnCreate () {
        updateDeviceTable();
        updateRightPanel();
    }


    @Override 
    public void onDataChanged ( long timestamp, int msg ) {
        updateDeviceTable();
        updateRightPanel();
    }
    

    @Override 
    public void onConnLost () {
        getDevicePanel().setVisible( false );
        setDeviceTable( null );
    }
    
    
    protected void updateDeviceTable() {
        if (device != null) {
            ArrayList<DeviceTableItem> deviceTableItems = new ArrayList<>();
            byte[] devices = device.getAvailableDevcies(getLink());
            if (devices != null) {
                for (Byte boardID : devices) {
                    String deviceType = device.getType(getLink(), boardID);
                    deviceTableItems.add(new DeviceTableItem(boardID.intValue(), deviceType));
                }
            }
            setDeviceTable(deviceTableItems);
        }
    }


    protected void updateRightPanel() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (device != null && getSelectedDeviceId()!=null) {
                    DeviceInfoBean info = new DeviceInfoBean();
                    info.setDeviceID(getSelectedDeviceId());
                    
                    DeviceType type = null;
                    try {
                        type = DeviceType.valueOf(device.getType(getLink(), getSelectedDeviceId().byteValue()));
                    } catch (IllegalArgumentException|NullPointerException e) {}
                    if (type == null)
                        type = DeviceType.UNKNOWN;
                    info.setDeviceType(type);
                    info.setHarewareVersion(device.getHardwareVersion(getLink(), getSelectedDeviceId().byteValue()));
                    info.setFirmwareVersion(device.getFirmwareVersion(getLink(), getSelectedDeviceId().byteValue()));
                    
                    DisplayBean display    = new DisplayBean();
                    display.setPosition(device.getText(getLink(), getSelectedDeviceId().byteValue()));
                    display.setArrow(device.getArrow(getLink(), getSelectedDeviceId().byteValue()));
                    display.setMsg(device.getMessage(getLink(), getSelectedDeviceId().byteValue()));

                    int in=0, out=0, sync=0, blink=0, fast=0;  
                    for (byte i = 0; i < info.getDeviceType().ioCount; i++) {
                        final DeviceBehavior behavior = device.getBehavior(getLink(), getSelectedDeviceId().byteValue(), i);
                        sync  |= behavior==DeviceBehavior.SYNC ? (1<<i) : 0;
                        blink |= behavior==DeviceBehavior.FLASH | behavior==DeviceBehavior.BLINK ? (1<<i) : 0;
                        fast  |= behavior==DeviceBehavior.FAST_FLASH | behavior==DeviceBehavior.FAST_BLINK ? (1<<i) : 0;
                        in    |= device.getInput(getLink(), getSelectedDeviceId().byteValue(), i) ? (1<<i) : 0;
                        out   |= device.getOutput(getLink(), getSelectedDeviceId().byteValue(), i) ? (1<<i) : 0;
                    }
                    IoStatusBean ioStatus = new IoStatusBean(info.getDeviceType().ioCount, in, out, sync, blink, fast);
                    switch (info.getDeviceType()) {
                    case DM3A:
                    case DM2A: {
                        getDevicePanel().setView(DeviceType.DM3A);
                        getDevicePanel().setIoStatus( ioStatus );
                        getDevicePanel().setDisplay( display );
                        getDevicePanel().setDeviceInfo(info);
                        getDevicePanel().setVisible( true );
                        break;
                    }
                    case GP4G : {
                        getDevicePanel().setView( DeviceType.GP4G );
                        getDevicePanel().setIoStatus( ioStatus );
                        getDevicePanel().setGong( null ); //TODO 
                        getDevicePanel().setDeviceInfo(info);
                        getDevicePanel().setVisible( true );
                        break;
                    }
                    case GP32 : {
                        getDevicePanel().setView( DeviceType.GP32 );
                        getDevicePanel().setIoStatus( ioStatus );
                        getDevicePanel().setDisplay( display );
                        getDevicePanel().setDeviceInfo( info );
                        getDevicePanel().setVisible( true );
                        break;
                    }
                    default :
                        getDevicePanel().setVisible( false );
                    }
                }
            }
            
        });
    }

}
