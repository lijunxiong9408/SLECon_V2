package slecon.inspect.devices;
import static logic.util.SiteManagement.MON_MGR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import comm.Parser_Device;
import comm.agent.AgentMessage;
import comm.constants.CANBus;
import comm.constants.DeviceBehavior;
import comm.constants.DeviceType;
import comm.event.LiftDataChangedListener;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SubtleSquareBorder;
import slecon.component.Workspace;
import slecon.home.PosButton;
import slecon.home.dashboard.VerticalSrcollBarUI;
import slecon.inspect.InspectPanel;
import slecon.inspect.devices.subview.DeviceInfoView;
import slecon.inspect.devices.subview.DisplayView;
import slecon.inspect.devices.subview.IOStatusView;
import slecon.interfaces.InspectView;
import slecon.interfaces.Page;


@InspectView(
    sortIndex = 0x500,
    path      = "Devices::Hall"
)
public class HallLinkViewPanel extends JPanel implements Page, LiftDataChangedListener{
    private static final long serialVersionUID = -552013171218549161L;
    static final ResourceBundle TEXT = ToolBox.getResourceBundle("inspect.HallDevice");
    private final LiftConnectionBean connBean;
    private JPanel		  	  panelNavigation;
    private JPanel		  	  panelMain;
    private JPanel		  	  panelLiftSelector;
    // Device List Panel
    private JLabel			  	labDeviceList;
    private DeviceTable         table;
    private DeviceTableModel    devicesModel;
    private JScrollPane         tableViewport;
    private Integer             selectedDeviceId = null;
    // Device info Panel
    private JLabel			    labDeviceInfo;
    private DeviceInfoView	    deviceInfoView;
    // Display Panel
    private JLabel			    labDisplay;
    private DisplayView		    displayView;
    // IO Status
    private JLabel			    labIOStatus;
    private IOStatusView		ioStatusView;
    
    private Parser_Device            device;
    private MigLayout         layout;
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("panelLiftSelector", "30 20 250 60 c");
    	styles.put("labDeviceList", "30 100 100 20 l");
    	styles.put("tableViewport", "30 120 250 180 c");
    	styles.put("labDeviceInfo", "320 100 100 20 l");
    	styles.put("deviceInfoView", "320 120 250 180 c");
    	styles.put("labDisplay", "610 100 100 20 l");
    	styles.put("displayView", "610 120 250 180 c");
    	styles.put("labIOStatus", "30 320 100 20 l");
    	styles.put("ioStatusView", "30 340 940 250 c");
    }
    
    private String[] navigationText = {Dict.lookup("Inspect"), Dict.lookup("Devices"), Dict.lookup("Hall")};
    ///////////////////// interface <Page> /////////////////////
    public HallLinkViewPanel ( LiftConnectionBean bean) {
    	setLayout(new BorderLayout());
        initGUI();
        this.connBean = bean;
    }

    private void initGUI () {
    	removeAll();
        JPanel workspace = new JPanel( new MigLayout( "gap 0", "[left]", "[30!]10[600]" ) );
        workspace.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        
    /*------------------------------------------------------------------------------------------------------------*/   
        panelNavigation = new JPanel(new MigLayout( "nogrid, w 985!, h 30!, gap 0", "[left]", "[center]" ));
        //panelNavigation.setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
        panelNavigation.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        workspace.add(panelNavigation,"cell 0 0");
        int index = 0;
        for (String text : navigationText) {
        	if(index > 0) {
        		PosButton icon = new PosButton(ImageFactory.ARROW_NAVIGATION.icon(11,12));
        		panelNavigation.add(icon);
			}
        	
        	PosButton lab = new PosButton(text, StartUI.BORDER_COLOR, Color.WHITE);
        	lab.setForeground(StartUI.BORDER_COLOR);
        	lab.setFont(FontFactory.FONT_12_BOLD);
        	if(index == 0) {
        		lab.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						final InspectPanel panelBinder = InspectPanel.build(connBean, null);
                        StartUI.getTopMain().push(panelBinder);
					}
				});
        	}else if(index == 1) {
        		lab.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						final InspectPanel panelBinder = InspectPanel.build(connBean, slecon.inspect.devices.HallLinkViewPanel.class);
                        StartUI.getTopMain().push(panelBinder);
					}
				});
        	}
			panelNavigation.add(lab);
			index += 1;
		}
        
    /*------------------------------------------------------------------------------------------------------------*/        
        layout = new MigLayout( "nogrid, w 1000!, h 600!, gap 0", "[left]", "[center]" );
        panelMain = new JPanel();
        panelMain.setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
        panelMain.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelMain.setLayout(layout);
        workspace.add(panelMain,"cell 0 1");
      
        panelLiftSelector = new JPanel();
        panelLiftSelector.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelLiftSelector.add(StartUI.getLiftSelector());
        panelMain.add(panelLiftSelector);
        StartUI.setStyle(layout, styles, panelLiftSelector, "panelLiftSelector");
        
        //	Device List.
        labDeviceList = new JLabel(TEXT.getString("DeviceTableHeader.text"));
        labDeviceList.setFont(FontFactory.FONT_12_BOLD);
        labDeviceList.setForeground(Color.WHITE);
        panelMain.add(labDeviceList);
        StartUI.setStyle(layout, styles, labDeviceList, "labDeviceList");
        
        devicesModel = new DeviceTableModel();
        table = new DeviceTable(devicesModel);
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>( devicesModel );
        table.setRowSorter( sorter );
        table.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
            @Override
            public void valueChanged ( ListSelectionEvent e ) {
                if ( e.getSource() == table.getSelectionModel() && table.getRowSelectionAllowed() )
                    do_table_valueChanged( e );
            }
        } );

        tableViewport = new JScrollPane() {
            private static final long serialVersionUID = -6857282948114353643L;
            {
            	
                setOpaque( false );
                getViewport().setOpaque( false );
                setBorder( BorderFactory.createLineBorder(StartUI.BORDER_COLOR) );
                getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
            }
        };
        panelMain.add(tableViewport);
        StartUI.setStyle(layout, styles, tableViewport, "tableViewport");
        tableViewport.setViewportView( table );
        
        // Device info
        labDeviceInfo = new JLabel(TEXT.getString("DeviceView.title"));
        labDeviceInfo.setFont(FontFactory.FONT_12_BOLD);
        labDeviceInfo.setForeground(Color.WHITE);
        panelMain.add(labDeviceInfo);
        StartUI.setStyle(layout, styles, labDeviceInfo, "labDeviceInfo");
        
        deviceInfoView = new DeviceInfoView();
        panelMain.add(deviceInfoView);
        StartUI.setStyle(layout, styles, deviceInfoView, "deviceInfoView");
        
        // Display 
        labDisplay = new JLabel(TEXT.getString("Display.title"));
        labDisplay.setFont(FontFactory.FONT_12_BOLD);
        labDisplay.setForeground(Color.WHITE);
        panelMain.add(labDisplay);
        StartUI.setStyle(layout, styles, labDisplay, "labDisplay");
        
        displayView = new DisplayView();
        panelMain.add(displayView);
        StartUI.setStyle(layout, styles, displayView, "displayView");
        
        // IO Status.
        labIOStatus = new JLabel(TEXT.getString("IOStatusView.title"));
        labIOStatus.setFont(FontFactory.FONT_12_BOLD);
        labIOStatus.setForeground(Color.WHITE);
        panelMain.add(labIOStatus);
        StartUI.setStyle(layout, styles, labIOStatus, "labIOStatus");
        
        ioStatusView = new IOStatusView();
        panelMain.add(ioStatusView);
        StartUI.setStyle(layout, styles, ioStatusView, "ioStatusView");
        
        add( new JScrollPane( workspace ) {
            private static final long serialVersionUID = -5733767579374701576L;
            {
                setBorder( null );
                setOpaque( false );
                getViewport().setOpaque( false );
                getVerticalScrollBar().setUnitIncrement( 20 );
                this.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());

                setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
            }
        }, BorderLayout.CENTER );
        
        revalidate();
        repaint();
    }
    
    public Integer getSelectedDeviceId () {
        return selectedDeviceId;
    }
    
    public void setSelectedDeviceId ( Integer selectedDeviceId ) {
        this.selectedDeviceId = selectedDeviceId;
        updatePanel();
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

    public void setDeviceTable ( ArrayList<DeviceTableItem> items ) {
        final ArrayList<DeviceTableItem> list = (items == null) ? new ArrayList<DeviceTableItem>() : items;
        SwingUtilities.invokeLater( new Runnable() {
            public void run () {
                Integer deviceId = selectedDeviceId;
                devicesModel.setAllItem( list );
                
                table.initTable();
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
    
    protected void updateDeviceTable() {
        if (device != null) {
            ArrayList<DeviceTableItem> deviceTableItems = new ArrayList<>();
            byte[] devices = device.getAvailableDevcies(CANBus.HALL);
            if (devices != null) {
                for (Byte boardID : devices) {
                    String deviceType = device.getType(CANBus.HALL, boardID);
                    deviceTableItems.add(new DeviceTableItem(boardID.intValue(), deviceType));
                }
            }
            setDeviceTable(deviceTableItems);
        }
    }
    
    protected void updatePanel() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (device != null && getSelectedDeviceId()!=null) {
                    DeviceInfoBean info = new DeviceInfoBean();
                    info.setDeviceID(getSelectedDeviceId());
                    
                    DeviceType type = null;
                    try {
                        type = DeviceType.valueOf(device.getType(CANBus.HALL, getSelectedDeviceId().byteValue()));
                    } catch (IllegalArgumentException|NullPointerException e) {}
                    if (type == null)
                        type = DeviceType.UNKNOWN;
                    info.setDeviceType(type);
                    info.setHarewareVersion(device.getHardwareVersion(CANBus.HALL, getSelectedDeviceId().byteValue()));
                    info.setFirmwareVersion(device.getFirmwareVersion(CANBus.HALL, getSelectedDeviceId().byteValue()));
                    
                    DisplayBean display    = new DisplayBean();
                    display.setPosition(device.getText(CANBus.HALL, getSelectedDeviceId().byteValue()));
                    display.setArrow(device.getArrow(CANBus.HALL, getSelectedDeviceId().byteValue()));
                    display.setMsg(device.getMessage(CANBus.HALL, getSelectedDeviceId().byteValue()));

                    int in=0, out=0, sync=0, blink=0, fast=0;  
                    for (byte i = 0; i < info.getDeviceType().ioCount; i++) {
                        final DeviceBehavior behavior = device.getBehavior(CANBus.HALL, getSelectedDeviceId().byteValue(), i);
                        sync  |= behavior==DeviceBehavior.SYNC ? (1<<i) : 0;
                        blink |= behavior==DeviceBehavior.FLASH | behavior==DeviceBehavior.BLINK ? (1<<i) : 0;
                        fast  |= behavior==DeviceBehavior.FAST_FLASH | behavior==DeviceBehavior.FAST_BLINK ? (1<<i) : 0;
                        in    |= device.getInput(CANBus.HALL, getSelectedDeviceId().byteValue(), i) ? (1<<i) : 0;
                        out   |= device.getOutput(CANBus.HALL, getSelectedDeviceId().byteValue(), i) ? (1<<i) : 0;
                    }
                    IoStatusBean ioStatus = new IoStatusBean(info.getDeviceType().ioCount, in, out, sync, blink, fast);
                    
                    deviceInfoView.setDeviceInfo(info);
                    displayView.setDisplay(display);
                    ioStatusView.setIoStatus(ioStatus);
                }
            }
            
        });
    }
    
	@Override
	public void onCreate(Workspace pageBinder) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() throws Exception {
		// TODO Auto-generated method stub
		 try {
            device = new Parser_Device(connBean.getIp(), connBean.getPort());
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                    AgentMessage.DEVICE.getCode() | 
                    AgentMessage.ERROR.getCode() );
            updateDeviceTable();
            updatePanel();
        } catch(Exception e) {
            
        }
	}

	@Override
	public void onResume() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() throws Exception {
		// TODO Auto-generated method stub
		removeAll();
        MON_MGR.removeEventListener(this);
	}

	@Override
	public void onDestroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnCreate() {
		// TODO Auto-generated method stub
		updateDeviceTable();
		updatePanel();
	}

	@Override
	public void onDataChanged(long timestamp, int msg) {
		// TODO Auto-generated method stub
		updateDeviceTable();
		updatePanel();
	}

	@Override
	public void onConnLost() {
		// TODO Auto-generated method stub
		setDeviceTable( null );
	}
}
