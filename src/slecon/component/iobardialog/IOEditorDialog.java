package slecon.component.iobardialog;
import static logic.util.SiteManagement.MON_MGR;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.DataFormatException;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import logic.EventID;
import logic.connection.LiftConnectionBean;
import logic.util.SiteManagement;
import logic.util.Version;
import logic.util.VersionChangeListener;
import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import ocsjava.remote.configuration.Event.Item;
import ocsjava.remote.configuration.Event.Operator;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyCheckBox;
import slecon.component.MyComboBox;
import slecon.component.iobar.IOBar;
import slecon.component.iobar.IOBar.EventSelectionChangeEvent;
import slecon.component.iobar.IOBar.EventSelectionChangeListener;
import slecon.component.iobar.IOBar.NewEvent;
import slecon.component.iobar.IOBar.NewEventListener;
import slecon.component.iobar.IOBar.OperatorChangedEvent;
import slecon.component.iobar.IOBar.OperatorChangedListener;
import slecon.component.iobar.IOBar.RemoveEvent;
import slecon.component.iobar.IOBar.RemoveEventListener;
import slecon.home.PosButton;
import slecon.home.dashboard.HorizontalSrcollBarUI;
import slecon.home.dashboard.VerticalSrcollBarUI;
import slecon.inspect.devices.DeviceTableItem;
import slecon.inspect.devices.DeviceTableModel;
import slecon.setting.SetupPanel;
import slecon.setting.setup.event.FloorSetting;
import ocsjava.remote.configuration.EventAggregator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import comm.Parser_Deploy;
import comm.Parser_Device;
import comm.Parser_Event;
import comm.agent.AgentMessage;
import comm.constants.CANBus;
import comm.constants.DeviceType;
import comm.event.LiftDataChangedListener;




public class IOEditorDialog extends JDialog
        implements OperatorChangedListener, EventSelectionChangeListener, NewEventListener, RemoveEventListener, ActionListener,
                   ItemListener, ListSelectionListener {
    
    private static final Logger                        logger                    = LogManager.getLogger( IOEditorDialog.class );

    /**
     * Text resource.
     */
    private static final ResourceBundle                TEXT                      = ToolBox.getResourceBundle( "logic.gui.IOEditorDialog" );


    private static final long                          serialVersionUID          = 4380637260291816226L;
    private final Frame                                ownerFrame;
    private final LiftConnectionBean                   connBean;
    private final Parser_Device device;
    private final Parser_Deploy deploy;
    private final Parser_Event event;
    
    private static final String                        escapeStrokeActionCommand = "el1.component.iobardialog.IOEditorDialog:WINDOW_CLOSING";    
    static int                                         count                     = 0;
    private slecon.component.iobar.Type            	   selectedType              = null;
    private int                                        selectedIndex             = -1;
    private String[]                                   floorText;
    private boolean									   other_event				 = false;
    private QueryEventsFromItem                        query;
    private JTextField                                 txtIOName;
    private IOBar                                      iobar;
    private MyComboBox                          	   cbBus;
    private MyComboBox								   cbType;
    private DeviceTableModel                           devicesModel;
    private JTable                                     deviceTable;
    private JList<Integer>                             bitList;
    private DefaultListModel<Integer>                  bitModel;
    private int                                        eventID;
    private Event                                      returnValue;
    private JTextField                                 txtDeviceID;
    private JTextField                                 txtBit;
    private MyComboBox		  						   cbo_Select_floor;
    private MyCheckBox                    			   chk_id;
    private MyCheckBox                    			   chk_bit;
    private MyCheckBox                    			   chkValue;
    private PosButton                                  btnDeviceIDOk;
    private PosButton                                  btnBitOK;
    private RemoteDataChangedListener                  listener;
    private PosButton                                  btnOK;
    private PosButton                                  btnCancel;
    private JScrollPane scrollPane;
    private static Point pressedPoint = null;

    enum CommitType { TYPE, BUS, DEVICEID, BIT, VALUE }
    
    private static TableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
        private static final long serialVersionUID = -3500230397712221379L;
        public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                         int column ) {
            JLabel label = ( JLabel )super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            label.setHorizontalAlignment( SwingConstants.CENTER );
            label.setBackground( StartUI.MAIN_BACKGROUND_COLOR );
            label.setForeground(Color.WHITE);
            return label;
        }
    };
    
    private static TableCellRenderer normalRenderer = new DefaultTableCellRenderer() {
        private static final long serialVersionUID = -3500230397712221379L;
        public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                         int column ) {
            JLabel label = ( JLabel )super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            label.setHorizontalAlignment( SwingConstants.CENTER );
            label.setForeground(Color.WHITE);
            if ( table.getSelectedRow() == row ) {
                label.setBackground( StartUI.BORDER_COLOR );
            } else {
                label.setBackground( StartUI.MAIN_BACKGROUND_COLOR );
            }
            return label;
        }
    };

    private IOEditorDialog ( Frame owner, LiftConnectionBean connBean, int id, Event evt ) throws DataFormatException {
        super( owner );
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        setUndecorated(true);
        
        this.ownerFrame = owner;
        this.connBean = connBean;
        initGUI();
        setEvent( id, evt );
        pack();
        setLocationRelativeTo( ownerFrame );

        listener = new RemoteDataChangedListener();
        deploy = new Parser_Deploy(connBean.getIp(), connBean.getPort());
        device = new Parser_Device(connBean.getIp(), connBean.getPort());
        event = new Parser_Event(connBean.getIp(), connBean.getPort());
        MON_MGR.addEventListener( listener, connBean.getIp(), connBean.getPort(),
                                  AgentMessage.DEVICE.getCode() | 
                                  AgentMessage.DEPLOYMENT.getCode() | 
                                  AgentMessage.EVENT.getCode() | 
                                  AgentMessage.ERROR.getCode() );

        onIOBarSelected();
        
        if(SiteManagement.isAlive(connBean))
            listener.updateAll();
        
        if(floorText != null) {
        	int curr_floor = EventID.getFloor( eventID );
        	if(curr_floor != -1) {
        		int valid_floor = floorText.length - curr_floor;
            	String[] sel_floor = new String[valid_floor];
            	System.arraycopy(floorText, curr_floor, sel_floor, 0, valid_floor);
            	cbo_Select_floor.setModel(new DefaultComboBoxModel<>(sel_floor));
        	}else {
        		other_event = true;
        	}
        	
        }
        
     // mouse listener.
        getContentPane().addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent e) {
				pressedPoint = e.getPoint();
			}
        });
        
        getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				Point point = e.getPoint();
				Point locationPoint = getLocation();
				int x = locationPoint.x + point.x - pressedPoint.x;
				int y = locationPoint.y + point.y - pressedPoint.y;
				setLocation(x, y);
			}
        });
        
    }


    public QueryEventsFromItem getQuery () {
        return query;
    }


    public void setQuery ( QueryEventsFromItem query ) {
        this.query = query;
        bitList.updateUI();
    }


    Item getIOBarItem ( slecon.component.iobar.Type type, int index ) {
        if ( type != null && iobar != null && iobar.getEvent() != null ) {
            try {
                switch ( type ) {
                case INPUT :
                    return iobar.getEvent().getInput( index );
                case OUTPUT :
                    return iobar.getEvent().getOutput( index );
                }
            } catch ( IndexOutOfBoundsException e ) {
            }
        }
        return null;
    }


    public static Event showDialog ( LiftConnectionBean connBean, int eventID, Event evt ) {
        return showDialog( connBean, eventID, evt, null, -1 );
    }


    public static Event showDialog ( LiftConnectionBean connBean, int eventID, Event evt, slecon.component.iobar.Type sel_inout,
                                     int sel_index ) {
    	StartUI.getTopMain().setEnabled(false);
        try {
            StartUI owner = StartUI.getFrame();
            final IOEditorDialog dialog = new IOEditorDialog( owner, connBean, eventID, evt );
            //dialog.setTitle( TEXT.getString( "TITLE" ) );    
            dialog.setModal( true );
            dialog.setIOBarSelection( sel_inout, sel_index );
            
            VersionChangeListener listener = new VersionChangeListener() {
                @Override
                public void versionChanged ( LiftConnectionBean connBean, Version newVersion ) {
                    if (newVersion==null)
                        dialog.dispose();
                }
            };
            SiteManagement.addVersionChangeListener( listener, connBean );
            dialog.setVisible( true );
            SiteManagement.removeVersionChangeListener( listener );
            dialog.dispose();
            
            MON_MGR.removeEventListener(dialog.listener);
            return dialog.returnValue;
        } catch ( DataFormatException e ) {
            e.printStackTrace();
        }
        return null;
    }

    /* lookup */
    protected synchronized void setDeviceTable ( final ArrayList<DeviceTableItem> deviceTableItems ) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run () {
                devicesModel.clear();
                if ( deviceTableItems != null ) {
                    for ( DeviceTableItem itm : deviceTableItems ) {
                        devicesModel.addItem( itm );
                    }
                }
                deviceTable.updateUI();

                Item item  = getIOBarItem( selectedType, selectedIndex );
                int  index = ( item == null )
                             ? -1
                             : devicesModel.getRowIndexByID( ( int )item.getId() );
                if ( index == -1 ) {
                    if ( item != null ) {
                        devicesModel.addItem( new DeviceTableItem( ( int )item.getId(), "UNKNOWN" ) );
                        index = devicesModel.getRowIndexByID( ( int )item.getId() );
                        deviceTable.setRowSelectionInterval( index, index );
                    }
                } else if ( index != deviceTable.getSelectionModel().getLeadSelectionIndex() ) {
                    deviceTable.setRowSelectionInterval( index, index );
                    txtDeviceID.setText( Integer.toString( item.getId() ) );
                }
            }
        } );
    }


    public synchronized void setEvent ( int id, Event evt ) {
        this.eventID = id;
        iobar.setEvent( evt );
        refresh();
        updateFromRemote();
    }


    private void refresh () {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                if ( floorText != null )
                    txtIOName.setText( EventID.getString( eventID, floorText ) );
                else
                    txtIOName.setText( EventID.getSimpleString( eventID ) );
            }
        } );
    }


    public void setIOBarSelection ( slecon.component.iobar.Type inout, int index ) {
        if ( inout != selectedType || index != selectedIndex ) {
            selectedType  = inout;
            selectedIndex = index;
            onIOBarSelected();
        }
        iobar.setSelection( inout, index );
    }


    private void initGUI () {
        setMinimumSize(new Dimension(600, 450));
        
        JPanel panel_3 = new JPanel();
        setContentPane( panel_3 );
        getContentPane().setLayout( new MigLayout("w 600, h 450", "[120!][grow]", "[25!][][fill][][grow][][]") );
        
        panel_3.setBackground(StartUI.MAIN_BACKGROUND_COLOR);
        panel_3.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        
        JLabel titleBar = new JLabel( TEXT.getString( "TITLE" ) );
        setCaptionStyle(titleBar);
        getContentPane().add( titleBar, "cell 0 0, span, left, top" );    
        
        JSeparator separator = new JSeparator();
        separator.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        getContentPane().add( separator, "cell 0 1 2 1,growx" );    // $NON-NLS-1$
        
        JLabel lblIoName = new JLabel( TEXT.getString( "IONAME" ) );    
        getContentPane().add( lblIoName, "cell 0 2, left, top" );    
        setLabelTitleStyle(lblIoName);
        
        txtIOName = new JTextField();
        txtIOName.setEnabled( false );
        getContentPane().add( txtIOName, "cell 1 2,growx" );    
        txtIOName.setColumns( 10 );
        txtIOName.setCaretColor(Color.WHITE);
        txtIOName.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        txtIOName.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        txtIOName.setForeground(Color.WHITE);
        
        scrollPane = new JScrollPane();
        scrollPane.setBackground(StartUI.MAIN_BACKGROUND_COLOR);
        scrollPane.setOpaque( false );
        scrollPane.getViewport().setOpaque( false );
        scrollPane.setViewportBorder( null );
        scrollPane.setBorder( null );
        panel_3.add(scrollPane, "cell 0 3 2 1, grow");
        iobar = new IOBar( false );
        iobar.setBackground(StartUI.MAIN_BACKGROUND_COLOR);
        scrollPane.setViewportView(iobar);
        scrollPane.setWheelScrollingEnabled(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.getHorizontalScrollBar().setUI(new HorizontalSrcollBarUI());
        
        final MouseAdapter mousewheelScrollListener = new MouseAdapter()
        {
            public void mouseWheelMoved(MouseWheelEvent evt) {
                JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
                if (evt.getWheelRotation() == 1) {
                    int iScrollAmount = evt.getScrollAmount();
                    int iNewValue = horizontalScrollBar.getValue() + horizontalScrollBar.getBlockIncrement() * iScrollAmount;
                    horizontalScrollBar.setValue(iNewValue <= horizontalScrollBar.getMaximum() ? iNewValue : horizontalScrollBar.getMaximum());
                } else if (evt.getWheelRotation() == -1) {
                    int iScrollAmount = evt.getScrollAmount();
                    int iNewValue = horizontalScrollBar.getValue() - horizontalScrollBar.getBlockIncrement() * iScrollAmount;
                    horizontalScrollBar.setValue(iNewValue >= 0 ? iNewValue : 0);
                }
            }
        };
        scrollPane.addMouseWheelListener(mousewheelScrollListener);

        iobar.addNewEventListener( this );
        iobar.addEventSelectionChangeListener( this );
        iobar.addRemoveEventListener( this );
        iobar.addOperatorChangedListener( this );

        JPanel panel = new JPanel();
        panel.setBackground(StartUI.MAIN_BACKGROUND_COLOR);
        getContentPane().add( panel, "cell 0 4 2 1,grow" );                            
        panel.setLayout( new MigLayout( "ins 0", "[grow][grow]", "[10px,grow]" ) );     //$NON-NLS-2$ //$NON-NLS-3$

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(StartUI.MAIN_BACKGROUND_COLOR);
        panel.add( panel_1, "cell 0 0,grow" );                                            
        panel_1.setLayout( new MigLayout( "", "[80!][grow]", "[19px][fill][grow]" ) );     //$NON-NLS-2$ //$NON-NLS-3$

        JLabel lblBus = new JLabel( TEXT.getString( "BUS" ) );    
        setLabelTitleStyle(lblBus);
        panel_1.add( lblBus, "cell 0 0,alignx trailing,aligny top" );    
        cbBus = new MyComboBox();
        cbBus.addItemListener( this );
        cbBus.setModel( new DefaultComboBoxModel<>( CANBus.values() ) );
        panel_1.add( cbBus, "cell 1 0,growx" );    

        JLabel lblDeviceId = new JLabel( TEXT.getString( "DEVICEID" ) );    
        setLabelTitleStyle(lblDeviceId);
        panel_1.add( lblDeviceId, "cell 0 1,alignx right,aligny center" );    
        
        txtDeviceID = new JTextField();
        txtDeviceID.setCaretColor(Color.WHITE);
        txtDeviceID.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        txtDeviceID.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        txtDeviceID.setForeground(Color.WHITE);
        txtDeviceID.addActionListener( this );
        panel_1.add( txtDeviceID, "flowx,cell 1 1,growx" );    
        txtDeviceID.setColumns( 10 );

        JScrollPane deviceIDScrollPane = new JScrollPane();
        deviceIDScrollPane.setOpaque( false );
        deviceIDScrollPane.getViewport().setOpaque( false );
        deviceIDScrollPane.setViewportBorder( null );
        deviceIDScrollPane.setBorder( BorderFactory.createLineBorder(StartUI.BORDER_COLOR) );
        panel_1.add( deviceIDScrollPane, "cell 1 2,width 50%,grow" );
        deviceTable = new JTable();
        deviceTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        deviceTable.setShowGrid( true );
        deviceTable.setGridColor(StartUI.SUB_BACKGROUND_COLOR);
        deviceTable.setRowHeight(20);
        devicesModel = new DeviceTableModel();
        deviceTable.setModel( devicesModel );
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>( devicesModel );
        deviceTable.setRowSorter( sorter );
        deviceTable.getColumnModel().getColumn( 0 ).setPreferredWidth( 40 );
        for(int i = 0; i < deviceTable.getColumnCount(); i++) {
        	deviceTable.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        	deviceTable.getColumnModel().getColumn(i).setCellRenderer(normalRenderer);
        }
        
        deviceIDScrollPane.setViewportView( deviceTable );
        deviceIDScrollPane.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
        btnDeviceIDOk = new PosButton( TEXT.getString( "DEVICEID_OK" ), ImageFactory.BUTTON_PAUSE.icon(40, 20) );
        btnDeviceIDOk.addActionListener( this );
        panel_1.add( btnDeviceIDOk, "cell 1 1" );
        deviceTable.getSelectionModel().addListSelectionListener( this );

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(StartUI.MAIN_BACKGROUND_COLOR);
        panel.add( panel_2, "cell 1 0,grow" );                                       
        panel_2.setLayout( new MigLayout( "", "[80!][grow]", "[][center][grow]" ) ); 
        
        JLabel lbl_ToFloor	= new JLabel( TEXT.getString("TOFLOOR"));
        setLabelTitleStyle(lbl_ToFloor);
        panel_2.add( lbl_ToFloor, "cell 0 0,alignx trailing,aligny center" );
        cbo_Select_floor = new MyComboBox();
        cbo_Select_floor.setPreferredSize(new Dimension(30, 25));
        panel_2.add( cbo_Select_floor, "flowx,growx" );
        chk_id = new MyCheckBox();
        setCheckBoxStyle(chk_id);
        chk_id.setText("ID");
        panel_2.add( chk_id, "flowx, growx" );
        chk_bit = new MyCheckBox();
        setCheckBoxStyle(chk_bit);
        chk_bit.setText("Bit");
        panel_2.add( chk_bit, "flowx, growx" );
        
        JLabel lblReversed = new JLabel( TEXT.getString( "REVERSED" ) );
        setLabelTitleStyle(lblReversed);
        panel_2.add( lblReversed, "cell 0 1,alignx trailing,aligny center" );
        chkValue = new MyCheckBox();
        chkValue.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				do_chkvalue_actionPerformed();
			}
		});
        setCheckBoxStyle(chkValue);
        panel_2.add( chkValue, "flowx, cell 1 1,growx" );
        
        JLabel lblInOut = new JLabel( TEXT.getString( "IOTYPE" ) );
        setLabelTitleStyle(lblInOut);
        panel_2.add( lblInOut, "cell 0 2,alignx trailing,aligny center" );
        cbType = new MyComboBox();
        cbType.addItemListener( this );
        cbType.setModel( new DefaultComboBoxModel<>( slecon.component.iobar.Type.values() ) );
        panel_2.add( cbType, "flowx,cell 1 2,growx" );

        JLabel lblBit = new JLabel( TEXT.getString( "BIT" ) );
        setLabelTitleStyle(lblBit);
        panel_2.add( lblBit, "cell 0 3,alignx trailing,aligny center" );    
        txtBit = new JTextField();
        panel_2.add( txtBit, "flowx,cell 1 3,growx" );    
        txtBit.setColumns( 10 );
        txtBit.setCaretColor(Color.WHITE);
        txtBit.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        txtBit.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        txtBit.setForeground(Color.WHITE);

        JScrollPane bitScrollPane = new JScrollPane();
        bitScrollPane.setOpaque( false );
        bitScrollPane.getViewport().setOpaque( false );
        bitScrollPane.setViewportBorder( null );
        bitScrollPane.setBorder( BorderFactory.createLineBorder(StartUI.BORDER_COLOR) );
        panel_2.add( bitScrollPane, "cell 1 4,width 50%,grow" );    
        bitModel = new DefaultListModel<Integer>();
        bitList  = new JList<>( bitModel );
        bitList.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        bitList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        bitList.addListSelectionListener( this );
        bitList.setCellRenderer( new BitListCellRenderer() );
        bitScrollPane.setViewportView( bitList );
        bitScrollPane.getHorizontalScrollBar().setUI(new HorizontalSrcollBarUI());
        bitScrollPane.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
        btnBitOK = new PosButton( TEXT.getString( "BIT_OK" ), ImageFactory.BUTTON_PAUSE.icon(40, 20) );    
        btnBitOK.addActionListener( this );
        panel_2.add( btnBitOK, "cell 1 3" );    

        JSeparator separator1 = new JSeparator();
        separator1.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        getContentPane().add( separator1, "cell 0 5 2 1,growx" );    
        
        btnOK = new PosButton( TEXT.getString( "OK" ), ImageFactory.BUTTON_PAUSE.icon(87, 30) );
        btnOK.addActionListener( this );
        getContentPane().add( btnOK, "flowx,cell 0 6 2 1,tag ok, w 100!" );    
        btnCancel = new PosButton( TEXT.getString( "CANCEL" ),  ImageFactory.BUTTON_PAUSE.icon(87, 30) );    
        btnCancel.addActionListener( this );
        getContentPane().add( btnCancel, "tag cancel,cell 0 6 2 1,width 100!" );    
        getRootPane().registerKeyboardAction( this, escapeStrokeActionCommand, KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
                                              JComponent.WHEN_IN_FOCUSED_WINDOW );
    }
    
    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_BOLD );
        c.setForeground(Color.WHITE);
    }
    
    private void setLabelTitleStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }
    
    private void setCheckBoxStyle ( final MyCheckBox c ) {
        c.setOpaque( false );
        c.setForeground(Color.WHITE);
        c.setFont( FontFactory.FONT_12_PLAIN );
    }
    
    @Override
    public void valueChanged ( ListSelectionEvent e ) {
        if ( e.getSource() == bitList ) {
            do_bitList_valueChanged( e );
        }
        if ( e.getSource() == deviceTable.getSelectionModel() ) {
            do_devicetable_valueChanged();
        }
    }


    @Override
    public void selectionChanged ( final EventSelectionChangeEvent evt ) {
        if ( evt.getSource() == iobar ) {
            do_iobar_selectionChanged( evt );
        }
    }


    @Override
    public void newEventOccurred ( final NewEvent evt ) {
        if ( evt.getSource() == iobar ) {
            do_iobar_newEventOccurred( evt );
        }
    }


    @Override
    public void removeEventOccurred ( RemoveEvent evt ) {
        if ( evt.getSource() == iobar ) {
            do_iobar_removeEventOccurred( evt );
        }
    }


    @Override
    public void operatorChangedOccurred ( OperatorChangedEvent evt ) {
        if ( evt.getSource() == iobar ) {
            do_iobar_opeartorChangedOccurred( evt );
        }
    }


    private void do_iobar_opeartorChangedOccurred ( OperatorChangedEvent evt ) {
        Event event = evt.getEvent();
        if ( event != null ) {
            try {
                if ( event.getInputCount() <= 1 )
                    event.setOperator( Operator.NON );
                else if ( evt.getOperator() == null )
                    event.setOperator( Operator.OR );
                else
                    event.setOperator( evt.getOperator() );
                setEvent( eventID, event );
            } catch ( DataFormatException e ) {
            }
        }
    }


    private void do_iobar_removeEventOccurred ( RemoveEvent evt ) {
        slecon.component.iobar.Type type  = evt.getType();
        Event                           event = evt.getEvent();
        int                             index = evt.getIndex();
        if ( event != null ) {
            if ( type == slecon.component.iobar.Type.INPUT && index != -1 && index < event.getInputCount() ) {
                event.removeInput( index );
            }
            if ( type == slecon.component.iobar.Type.OUTPUT && index != -1 && index < event.getOutputCount() ) {
                event.removeOutput( index );
            }
            setEvent( eventID, event );
        }
    }


    protected synchronized void do_iobar_newEventOccurred ( final NewEvent evt ) {
        byte id    = 0;
        CANBus  bus   = CANBus.CAR;
        byte bit   = 0;
        byte value = 1;
        Item item  = null;
        try {
            if ( cbBus.getSelectedItem() == CANBus.CAR )
                bus = CANBus.CAR;
            else if ( cbBus.getSelectedItem() == CANBus.HALL )
                bus = CANBus.HALL;
            item = new Item( bus, id, bit, value );
        } catch ( DataFormatException e ) {
        }
        if ( item != null ) {
            slecon.component.iobar.Type inout = ( slecon.component.iobar.Type )cbType.getSelectedItem();
            if ( inout == null )
                inout = slecon.component.iobar.Type.INPUT;
            if ( inout == slecon.component.iobar.Type.INPUT ) {
                Event e = iobar.getEvent();
                try {
                    e.addInput( item );
                } catch ( DataFormatException e1 ) {
                    e1.printStackTrace();
                }
                setEvent( eventID, e );
                setIOBarSelection( slecon.component.iobar.Type.INPUT, e.getInputCount() - 1 );
            } else if ( inout == slecon.component.iobar.Type.OUTPUT ) {
                Event e = iobar.getEvent();
                try {
                    e.addOutput( item );
                } catch ( DataFormatException e1 ) {
                    e1.printStackTrace();
                }
                setEvent( eventID, e );
                setIOBarSelection( slecon.component.iobar.Type.OUTPUT, e.getOutputCount() - 1 );
            }
        }
    }


    protected void do_iobar_selectionChanged ( final EventSelectionChangeEvent evt ) {
        setIOBarSelection( evt.getType(), evt.getIndex() );
        final Item ioBarItem = getIOBarItem( selectedType, selectedIndex );
        txtDeviceID.setText(Integer.toString(ioBarItem.getId()));
        txtBit.setText(Integer.toString(ioBarItem.getBit()));
        chkValue.setSelected( ioBarItem.getValue() == 1? false : true );
        updateFromRemote();
    }


    /**
     * add item from the input panel to iobar, then select the new item.
     */
    private synchronized void writeToIOBar ( Set<CommitType> types ) {
        final Item ioBarItem = getIOBarItem( selectedType, selectedIndex );
        final Item panelItem = getPanelItem( ioBarItem, types );
        if ( ! ioBarItem.equals( panelItem ) ) {
            Event evt = iobar.getEvent();
            
            boolean removed = false;
            if (selectedType == slecon.component.iobar.Type.INPUT && cbType.getSelectedItem()!=slecon.component.iobar.Type.INPUT 
                    && selectedIndex < evt.getInputCount()) {
                evt.removeInput(selectedIndex);
                removed = true;
            } else if (selectedType == slecon.component.iobar.Type.OUTPUT && cbType.getSelectedItem()!=slecon.component.iobar.Type.OUTPUT 
                    && selectedIndex < evt.getOutputCount()) {
                evt.removeOutput(selectedIndex);
                removed = true;
            }

            slecon.component.iobar.Type ioType = slecon.component.iobar.Type.INPUT;
            ioType = ( slecon.component.iobar.Type )cbType.getSelectedItem();
            if ( ioType == slecon.component.iobar.Type.INPUT ) {
                try {
                    if(removed)
                        evt.addInput( panelItem );
                    else 
                        evt.setInputItem(selectedIndex, panelItem);
                } catch ( DataFormatException e1 ) {
                    e1.printStackTrace();
                }
            } else if ( ioType == slecon.component.iobar.Type.OUTPUT ) {
                try {
                    if(removed)
                        evt.addOutput( panelItem );
                    else 
                        evt.setOutputItem(selectedIndex, panelItem);
                } catch ( DataFormatException e1 ) {
                    e1.printStackTrace();
                }
            }
            setEvent( eventID, evt );

            Event event = iobar.getEvent();
            try {
                if ( panelItem != null && ioType == slecon.component.iobar.Type.INPUT ) {
                    for ( int i = 0 ; i < event.getInputCount() ; i++ ) {
                        Item itm = event.getInput( i );
                        if ( itm.getBus() == panelItem.getBus() && itm.getId() == panelItem.getId() && itm.getBit() == panelItem.getBit()
                            && itm.getValue() == panelItem.getValue() ) {
                            setIOBarSelection( slecon.component.iobar.Type.INPUT, i );
                            break;
                        }
                    }
                } else if ( panelItem != null && ioType == slecon.component.iobar.Type.OUTPUT ) {
                    for ( int i = 0 ; i < event.getOutputCount() ; i++ ) {
                        Item itm = event.getOutput( i );
                        if ( itm.getBus() == panelItem.getBus() && itm.getId() == panelItem.getId() && itm.getBit() == panelItem.getBit()
                            && itm.getValue() == panelItem.getValue() ) {
                            setIOBarSelection( slecon.component.iobar.Type.OUTPUT, i );
                            break;
                        }
                    }
                }
            } catch ( DataFormatException e ) {
            }
            txtDeviceID.setText( Integer.toString( panelItem.getId() ) );
            txtBit.setText( Integer.toString( panelItem.getBit() ) );
            chkValue.setSelected( panelItem.getValue() == 1 ? false : true );
            bitList.setSelectedValue( panelItem.getBit(), true );
            
            
            int row = -1;
            for ( int i = 0 ; i < devicesModel.getRowCount() ; i++ ) {
                if ( panelItem.getId() == ( int )devicesModel.getValueAt( i, 0 ) )
                    row = i;
            }
            if ( row != -1 )
                deviceTable.setRowSelectionInterval( row, row );
        }
    }


    protected final void do_txtDeviceID_actionPerformed ( ActionEvent e ) {
        commitDeviceID();
    }


    private void onIOBarSelected () {
        Item item = getIOBarItem( selectedType, selectedIndex );
        if ( item != null ) {
            try {
                if ( item.getBus() == CANBus.CAR )
                    cbBus.setSelectedItem( CANBus.CAR );
                else if ( item.getBus() == CANBus.HALL )
                    cbBus.setSelectedItem( CANBus.HALL );
            } catch ( DataFormatException e ) {
            }
        } else {
            cbBus.setSelectedItem( null );
        }
        cbType.setSelectedItem( selectedType );
    }


    @Override
    public void actionPerformed ( ActionEvent e ) {
        if ( e.getSource() == txtDeviceID ) {
            do_txtDeviceID_actionPerformed( e );
        }
        if ( e.getSource() == btnBitOK ) {
            do_btnBtnbitok_actionPerformed( e );
        }
        if ( e.getSource() == btnDeviceIDOk ) {
            do_btnDeviceIDOk_actionPerformed( e );
        }
        if ( e.getSource() == btnCancel ) {
            do_btnCancel_actionPerformed( e );
            StartUI.getTopMain().setEnabled(true);
        }
        if ( e.getSource() == btnOK ) {
            do_btnOK_actionPerformed( e );
        }
        if ( e.getActionCommand() == escapeStrokeActionCommand ) {
            do_btnCancel_actionPerformed( e );
        }
    }


    public void itemStateChanged ( final ItemEvent e ) {
        if ( e.getSource() == cbType && e.getStateChange() == ItemEvent.SELECTED ) {
            do_cbType_newItemSelected( e );
        }
        if ( e.getSource() == cbBus && e.getStateChange() == ItemEvent.SELECTED ) {
            do_cbBus_newItemSelected( e );
        }
    }


    protected void do_cbBus_newItemSelected ( final ItemEvent e ) {
        boolean    change = true;
        final Item item   = getIOBarItem( selectedType, selectedIndex );
        try {
            CANBus bus = null;
            if ( cbBus.getSelectedItem() == CANBus.CAR )
                bus = CANBus.CAR;
            else if ( cbBus.getSelectedItem() == CANBus.HALL )
                bus = CANBus.HALL;
            if ( ( item == null ) || ( item != null && item.getBus() == bus ) )
                change = false;
        } catch ( DataFormatException e1 ) {
        }
        if ( change )
            writeToIOBar( new TreeSet<CommitType>( Arrays.asList( CommitType.BUS ) ) );
    }


    protected void do_cbType_newItemSelected ( final ItemEvent e ) {
        boolean                         change = true;
        Item                            item   = getIOBarItem( selectedType, selectedIndex );
        slecon.component.iobar.Type inout  = ( slecon.component.iobar.Type )cbType.getSelectedItem();
        if ( item == null || inout == null || selectedType == inout ) {
            change = false;
        }
        if ( change ) {
            writeToIOBar( new TreeSet<CommitType>( Arrays.asList( CommitType.TYPE ) ) );
        }
    }


    protected void do_bitList_valueChanged ( ListSelectionEvent e ) {
        if ( bitList.getSelectedValue() != null ) {
            txtBit.setText( Integer.toString( bitList.getSelectedValue() ) );
            commitBit();
        }
    }

    
    protected void do_chkvalue_actionPerformed () {
    	commitValue();
    }
    
    protected void do_btnBtnbitok_actionPerformed ( ActionEvent e ) {
        commitBit();
    }
    
    protected void do_btnDeviceIDOk_actionPerformed ( ActionEvent e ) {
        commitDeviceID();
    }
    
    
    private void commitValue () {
        final Item item = getIOBarItem( selectedType, selectedIndex );
        try {
            final int value = chkValue.isSelected() ? 0 : 1;
            boolean change = true;
            if ( item == null || new Integer( item.getValue() ).compareTo( value ) == 0 )
                change = false;
            if ( change )
                writeToIOBar( new TreeSet<CommitType>( Arrays.asList( CommitType.VALUE ) ) );
        } catch ( NumberFormatException ex ) {
        }
    }
    
    private void commitBit () {
        final Item item = getIOBarItem( selectedType, selectedIndex );
        try {
            final int evtID = Integer.parseInt( txtBit.getText() );
            if ( evtID < 0 || evtID > 31 )
                throw new NumberFormatException();

            boolean change = true;
            if ( item == null || new Integer( item.getBit() ).compareTo( evtID ) == 0 )
                change = false;
            if ( change )
                writeToIOBar( new TreeSet<CommitType>( Arrays.asList( CommitType.BIT ) ) );
        } catch ( NumberFormatException ex ) {
            ToolBox.showErrorMessage( TEXT.getString( "INVALIDATE_BIT" ) );    
        }
    }


    private void commitDeviceID () {
        final Item item = getIOBarItem( selectedType, selectedIndex );
        try {
            final int evtID = Integer.parseInt( txtDeviceID.getText() );
            if ( evtID < 0 || evtID > 255 )
                throw new NumberFormatException();

            int row = -1;
            for ( int i = 0 ; i < devicesModel.getRowCount() ; i++ ) {
                if ( evtID == ( int )devicesModel.getValueAt( i, 0 ) )
                    row = i;
            }
            if ( row != -1 ) {
                deviceTable.setRowSelectionInterval( row, row );
                DeviceType       deviceType = DeviceType.parse((String) devicesModel.getValueAt( row, 1 ));
                TreeSet<Integer> oldset     = new TreeSet<>();
                TreeSet<Integer> newset     = new TreeSet<>();
                for ( int i = 0 ; i < bitModel.getSize() ; i++ )
                    oldset.add( bitModel.getElementAt( i ) );
                if ( deviceType != null ) {
                    for ( int i = 0 ; i < deviceType.ioCount ; i++ )
                        newset.add( i );
                } else {
                    if ( item != null )
                        newset.add( new Integer( item.getBit() ) );
                }

                TreeSet<Integer> add = new TreeSet<>( newset );
                add.removeAll( oldset );

                TreeSet<Integer> remove = new TreeSet<>( oldset );
                remove.removeAll( newset );
                for ( Integer bit : remove )
                    bitModel.removeElement( bit );
                for ( Integer bit : add ) {
                    if ( bit < bitModel.size() )
                        bitModel.add( bit, bit );
                    else
                        bitModel.add( bitModel.getSize(), bit );
                }
                if ( item != null
                    && ! ( bitList.getSelectedValue() instanceof Integer
                           && bitList.getSelectedValue().compareTo( new Integer( item.getBit() ) ) == 0 ) )
                    bitList.setSelectedValue( new Integer( item.getBit() ), true );
            }

            boolean change = true;
            if ( item == null || new Integer( item.getId() ).compareTo( evtID ) == 0 ) {
                change = false;
            }
            if ( change )
                writeToIOBar( new TreeSet<CommitType>( Arrays.asList( CommitType.DEVICEID ) ) );
        } catch ( NumberFormatException ex ) {
            ToolBox.showErrorMessage( TEXT.getString( "INVALIDATE_DEVICEID" ) );    
        }
    }


    protected void do_devicetable_valueChanged () {
        int index = deviceTable.getSelectionModel().getLeadSelectionIndex();
        int row   = deviceTable.convertRowIndexToModel( index );
        if ( row != -1 ) {
            int id = ( int )devicesModel.getValueAt( row, 0 );
            txtDeviceID.setText( Integer.toString( id ) );
            commitDeviceID();
        }
    }


    public Item getPanelItem ( Item defaultItem, Set<CommitType> types ) {

        /* default value */
        CANBus bus = CANBus.CAR;
        try {
            if ( defaultItem != null )
                bus = defaultItem.getBus();
        } catch ( DataFormatException e1 ) {
            e1.printStackTrace();
        }

        byte id  = defaultItem == null
                   ? 0
                   : defaultItem.getId();
        byte bit = ( defaultItem == null
                     ? 0
                     : defaultItem.getBit() );
        
        byte value = ( defaultItem == null
		                ? 0
		                : defaultItem.getValue() );
        /* bus */
        if ( types != null ) {
            if ( types.contains( CommitType.BUS ) )
                if ( cbBus.getSelectedItem() == CANBus.CAR )
                    bus = CANBus.CAR;
                else if ( cbBus.getSelectedItem() == CANBus.HALL )
                    bus = CANBus.HALL;

            /* device id */
            if ( types.contains( CommitType.DEVICEID ) )
                try {
                    id = Byte.parseByte( txtDeviceID.getText() );
                } catch ( NumberFormatException e ) {
                }

            /* bit */
            if ( types.contains( CommitType.BIT ) )
                try {
                    bit = Byte.parseByte( txtBit.getText() );
                } catch ( NumberFormatException e ) {
                }
            
            /* value */
            if ( types.contains( CommitType.VALUE ) )
                try {
                	value = (byte)(chkValue.isSelected() ? 0 : 1);
                } catch ( NumberFormatException e ) {
                }
        }
        try {
            defaultItem = new Item( bus, id, bit, value );
        } catch ( DataFormatException e ) {
        }
        return defaultItem;
    }


    protected void do_btnOK_actionPerformed ( ActionEvent e ) {
    	StartUI.getTopMain().setEnabled(true);
        returnValue = iobar.getEvent();
        if ( returnValue.getInputCount() < 1 && !chk_id.isSelected() && !chk_bit.isSelected() ) {
            try {
                returnValue.setOperator( Operator.NON );
            } catch ( DataFormatException e1 ) {
                e1.printStackTrace();
            }
        }
        
        if(!other_event) {
        	int input_count = returnValue.getInputCount();
            int output_count = returnValue.getOutputCount();
            int cur_floor = EventID.getFloor( eventID );
        	int sel_floor = cur_floor + cbo_Select_floor.getSelectedIndex();
        	try {
        		EventAggregator ea = EventAggregator.toEventAggregator( event.getEvent() );
        		if( input_count > 0 || output_count > 0 ) {
            		event.setUpdateFlag(false);
            		for(int i = 0; i <= (sel_floor - cur_floor); i++) {
            			Event event = new Event();
            			Item input_item = null;
            			Item output_item = null;
            			if(input_count > 0) { 
            				input_item = returnValue.getInput(0);
            			}
            			if(output_count > 0) {
            				output_item = returnValue.getOutput(0);        				
            			}
            			event.setOperator(returnValue.getOperator());
            			if(chk_id.isSelected()) {
            				if(input_item != null) {
            					event.addInput( new Item( input_item.getBus(), ( byte )(input_item.getId() + i), ( byte )input_item.getBit(), ( byte )input_item.getValue() ) );        					
            				}
            				if(output_item != null) {
            					event.addOutput( new Item( output_item.getBus(), ( byte )(output_item.getId() + i), ( byte )output_item.getBit(), ( byte )output_item.getValue() ) );					        					
            				}
            				ea.setEvent( eventID + i , event );
            			}else if(chk_bit.isSelected()) {
            				if(input_item != null) {
            					event.addInput( new Item( input_item.getBus(), ( byte )input_item.getId(), ( byte )(input_item.getBit() + i), ( byte )input_item.getValue() ) );
            				}
            				if(output_item != null) {
            					event.addOutput( new Item( output_item.getBus(), ( byte )output_item.getId(), ( byte )(output_item.getBit() + i), ( byte )output_item.getValue() ) );					
            				}
            				ea.setEvent( eventID + i , event );
            			}else {
            				ea.setEvent( eventID, returnValue );
            				break;
            			}
            		}        
            		event.setEvent( ea.toByteArray() );
            		event.setInstalledDevices( ea.getInstalledDevices() );
            		event.commit();
            		event.setUpdateFlag(true);
        		}else if( input_count == 0 && output_count == 0 ) {
        			if( chk_id.isSelected() && chk_bit.isSelected() ) {
	        			event.setUpdateFlag(false);
	            		for(int i = 0; i <= (sel_floor - cur_floor); i++) {
	            			Event event = iobar.getEvent();
	            			event.setOperator(Operator.NON);
	        				ea.setEvent( eventID + i , event );
	            		}
	            		event.setEvent( ea.toByteArray() );
	            		event.setInstalledDevices( ea.getInstalledDevices() );
	            		event.commit();
	            		event.setUpdateFlag(true);
        			}
        		}
        	} catch (DataFormatException e1) {
           		e1.printStackTrace();
           	}
            	//StartUI.getTopMain().push(SetupPanel.build(connBean, FloorSetting.class));
         
        }
        
        dispatchEvent( new WindowEvent( this, WindowEvent.WINDOW_CLOSING ) );
    }


    protected void do_btnCancel_actionPerformed ( ActionEvent e ) {
    	StartUI.getTopMain().setEnabled(true);
        dispatchEvent( new WindowEvent( this, WindowEvent.WINDOW_CLOSING ) );
    }


    public static final void assignTo ( LiftConnectionBean connBean, IOBar iobar, int evtID ) {
        OpenIOEditorDialogListener listener = new OpenIOEditorDialogListener( connBean, iobar, evtID );
        iobar.addNewEventListener( listener );
        iobar.addActionListener( listener );
    }


    /**
     * Bit List Renderer
     *
     * @author superb8
     *
     */
    @SuppressWarnings( "serial" )
    private class BitListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent ( JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
            super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
            if ( value instanceof Number ) {
                CANBus bus = null;
                if ( cbBus.getSelectedItem() == CANBus.CAR )
                    bus = CANBus.CAR;
                else if ( cbBus.getSelectedItem() == CANBus.HALL )
                    bus = CANBus.HALL;

                Byte id = null;
                try {
                    id = Byte.parseByte( txtDeviceID.getText() );
                } catch ( NumberFormatException e ) {
                    int row = deviceTable.getSelectionModel().getLeadSelectionIndex();
                    if ( row != -1 ) {
                        Number num = ( ( Number )devicesModel.getValueAt( row, 0 ) );
                        if ( num != null )
                            id = num.byteValue();
                    }
                }

                Byte bit  = ( ( Number )value ).byteValue();
                Item item = null;
                try {
                    if ( bus != null && id != null && bit != null )
                        item = new Item( bus, id, bit, ( byte )1 );
                } catch ( DataFormatException e ) {
                }
                if ( item != null ) {
                    QueryEventsFromItem q = getQuery();
                    if ( q != null ) {
                        Set<Integer> set = q.getEvents( item );
                        if ( set != null ) {
                            StringBuffer sb = new StringBuffer();
                            for ( int i : set ) {
                                if ( sb.length() != 0 )
                                    sb.append( "; " ); 
                                /* some floor has floorText but is smaller than maxFloorCount */
                                final int floor = EventID.getFloor( i );
                                if ( floor == - 1 || floor < floorText.length)
                                    sb.append( EventID.getString( i, floorText ) );
                            }
                            setText( String.format( "<html><font color=%s>%d&nbsp;&nbsp;&nbsp;&nbsp;%s</font></html>", "white", value, sb ) );    
                        }else {
                        	setText( String.format( "<html><font color=%s>%d</font></html>", "white", value ) );
                        }
                        
                        if(isSelected) {
                        	setBackground(StartUI.BORDER_COLOR);
                        }else {
                        	setBackground(StartUI.SUB_BACKGROUND_COLOR);
                        }
                        
                    }
                }
            }
            return this;
        }
    }




    /////////////////////////////////////////////////////////////////////////////////////////
    private static final class OpenIOEditorDialogListener implements NewEventListener, ActionListener {
        private final int          eventID;
        private IOBar              iobar;
        private LiftConnectionBean connBean;




        OpenIOEditorDialogListener ( LiftConnectionBean connBean, IOBar iobar, int evtID ) {
            this.connBean = connBean;
            this.eventID  = evtID;
            this.iobar    = iobar;
        }


        @Override
        public void newEventOccurred ( NewEvent evt ) {
            Event e = null;
            if ( evt.getSource() == iobar )
                e = IOEditorDialog.showDialog( connBean, eventID, iobar.getEvent() );
            if ( e != null )
                iobar.setEvent( e );
        }


        @Override
        public void actionPerformed ( ActionEvent evt ) {
            Event e = null;
            if ( evt.getSource() == iobar && evt.getActionCommand().indexOf( "INPUT" ) >= 0 )    
                e = IOEditorDialog.showDialog( connBean, eventID, iobar.getEvent(), slecon.component.iobar.Type.INPUT, evt.getID() );
            if ( evt.getSource() == iobar && evt.getActionCommand().indexOf( "OUTPUT" ) >= 0 )    
                e = IOEditorDialog.showDialog( connBean, eventID, iobar.getEvent(), slecon.component.iobar.Type.OUTPUT, evt.getID() );
            if ( e != null )
                iobar.setEvent( e );
        }
    }

    protected void updateFromRemote() {
        if(listener!=null)
            listener.updateAll();
    }

    public class RemoteDataChangedListener implements LiftDataChangedListener {

        @Override
        public void onConnCreate() {
            updateAll();
        }

        @Override
        public void onDataChanged(long timestamp, int msg) {
        		updateAll();        		
        }

        public void updateAll() {
            if (SiteManagement.isAlive(connBean)) {
                updateDevice();
                updateFloor();
                updateItem();
            } else {
                setDeviceTable(null);
                setQuery(new QueryEventsFromItem());   // bitList.updateUI(); [swing]
            }
        }

        @Override
        public void onConnLost() {
            updateAll();
        }
        
        private void updateDevice() {
            final Item item = getIOBarItem(selectedType, selectedIndex);
            CANBus bus = null;
            /* init bus */
            if (item != null) {
                try {
                    if (item.getBus() == CANBus.CAR)
                        bus = CANBus.CAR;
                    else if (item.getBus() == CANBus.HALL)
                        bus = CANBus.HALL;
                } catch (DataFormatException e) {}
            }
            if (bus == null && cbBus.getSelectedItem() instanceof CANBus)
                bus = (CANBus) cbBus.getSelectedItem();

            ArrayList<DeviceTableItem> deviceTableItems = new ArrayList<>();
            if (bus == null) {
                setDeviceTable(null);
            } else {
                byte[] devices = device.getAvailableDevcies(bus);
                if(devices!=null) {
                    for (Byte boardID : devices) {
                        String deviceType = device.getType(bus, boardID);
                        deviceTableItems.add(new DeviceTableItem(boardID.intValue(), deviceType));
                    }
                }
                setDeviceTable(deviceTableItems);
            }
        }

        public void updateFloor() {
            floorText = new String[deploy.getFloorCount()];
            for (Integer i = 0; i < deploy.getFloorCount(); i++)
                floorText[i] = new String(deploy.getFloorText(i.byteValue()));
            refresh();
        }
        
        public void updateItem() {
            final QueryEventsFromItem query = new QueryEventsFromItem();
            try {
                EventAggregator ea = EventAggregator.toEventAggregator(event.getEvent());
                Event[] events = ea.getEventList();
                for (int id = 0, len = events.length; id < len; id++) {
                    Event event;
                    if (( event = events[id] ) == null)
                        continue;
                    for (int j = 0, inputCount = event.getInputCount(); j < inputCount; j++) {
                        Item itm = event.getInput(j);
                        query.add(itm, id);
                    }
                    for (int j = 0, outputCount = event.getOutputCount(); j < outputCount; j++) {
                        Item itm = event.getOutput(j);
                        query.add(itm, id);
                    }
                }
            } catch (DataFormatException e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setQuery(query);
                }
            });
        }
    }
    
}
