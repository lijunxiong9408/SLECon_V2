package slecon.setting.modules;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.zip.DataFormatException;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import base.cfg.FontFactory;
import logic.EventID;
import logic.connection.LiftConnectionBean;
import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import ocsjava.remote.configuration.EventAggregator;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyCheckBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.iobar.IOBar;
import slecon.component.iobar.Type;
import slecon.component.iobar.IOBar.NewEvent;
import slecon.component.iobar.IOBar.NewEventListener;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.setting.modules.LockStrategy.Lock_Strategy;
import slecon.setting.modules.NonStopOperation.ControlPanleEnableTableModel;

class headerCellRenderer implements TableCellRenderer{
	ControlPanleEnableTableModel tableModel;
	JTableHeader tableHeader;
	MyCheckBox myCheckBox = null;
	
	public headerCellRenderer(JTable table) {
		// TODO Auto-generated constructor stub
		this.tableModel = (ControlPanleEnableTableModel)table.getModel();
		this.tableHeader = table.getTableHeader();
		myCheckBox = new MyCheckBox();
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		
		myCheckBox.setHorizontalAlignment(JLabel.CENTER);
		myCheckBox.setSelected((Boolean)value);
		
		JComponent component = myCheckBox;
        component.setBackground(StartUI.MAIN_BACKGROUND_COLOR);
        component.setFont(tableHeader.getFont());
        
		return component;
	}
	
}
public class NonStopOperation extends JPanel {
	private static final long serialVersionUID = 1601444337474040214L;
	public static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.module.NonstopOperation");
	private LiftConnectionBean           connBean;
	private boolean                    started = false;
	private	int				  current_floor_index = 0;
	private EventAggregator              eventAggregator;
	private byte[]	car_lock_strategy = new byte[128];
	private byte[]	hall_lock_strategy = new byte[128];
    private OpenIOEditorDialogListener handleCarCallLock;
    private OpenIOEditorDialogListener handleHallCallLock;
	
	
	private SettingPanel<NonStopOperation> settingPanel;
	private static TableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
        private static final long serialVersionUID = -3500230397712221379L;
        public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                         int column ) {
        	JComponent component = ( JLabel )super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
        	((JLabel)component).setHorizontalAlignment( SwingConstants.CENTER );
        	((JLabel)component).setBackground( StartUI.BORDER_COLOR );
        	((JLabel)component).setForeground(Color.WHITE);
        	((JLabel)component).setFont( FontFactory.FONT_12_BOLD );
        		
            return component;
        }
    };
    
    private static void setTableHeaderColor(JTable table, int columnIndex, Color c) {
        TableColumn column = table.getTableHeader().getColumnModel()
                .getColumn(columnIndex);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(c);
        cellRenderer.setHorizontalTextPosition(JLabel.CENTER);
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        cellRenderer.setForeground(Color.WHITE);
        column.setHeaderRenderer(cellRenderer);
    }    
    /* ---------------------------------------------------------------------------- */
    private JLabel				cpt_general;
    private ValueCheckBox       ebd_enable;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel				cpt_controlMode;
    private ValueCheckBox       ebd_controlMode;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel				cpt_controlpanel;
    private IOBar  				io_controlpanel_switch;
    private JLabel				lbl_controlpanel_strategy;
    private Control_Panel_Table	control_panle_table;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel            cpt_lock_setting;
    private JLabel            lbl_Select_floor;
    private MyComboBox		  cbo_Select_floor;
    private JLabel            lbl_car_lock;
    private IOBar             io_car_lock;
    private JLabel            lbl_car_lock_strategy;
    private MyComboBox		  cbo_car_lock_strategy;
    private JLabel            lbl_hall_lock;
    private IOBar             io_hall_lock;
    private JLabel            lbl_hall_lock_strategy;
    private MyComboBox		  cbo_hall_lock_strategy;
    
    public NonStopOperation(){
    	initGUI();
    }
    
    public void setSettingPanel ( SettingPanel<NonStopOperation> panel ) {
        this.settingPanel = panel;
    }
    
    public static NonStopOperation createPanel ( SettingPanel<NonStopOperation> panel ) {
    	NonStopOperation gui = new NonStopOperation();
        gui.setSettingPanel( panel );
        return gui;
    }
    
    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 24, gap 0 6", "[40::40][50::50][100::100][50::50][]" ) );
        
        /* ---------------------------------------------------------------------------- */
        cpt_general = new JLabel();
        ebd_enable = new ValueCheckBox();
        setCaptionStyle( cpt_general );
        add( cpt_general, "gapbottom 18-12, span, top" );
        add( ebd_enable, "skip 1, span, wrap 30,top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_controlMode = new JLabel();
        ebd_controlMode = new ValueCheckBox();
        setCaptionStyle( cpt_controlMode );
        add( cpt_controlMode, "gapbottom 18-12, span, top" );
        add( ebd_controlMode, "skip 1, span, wrap 30,top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_controlpanel = new JLabel();
        JLabel lbl_io_controlpanel_switch = new JLabel();
        io_controlpanel_switch = new IOBar(false);
        lbl_controlpanel_strategy = new JLabel();
        control_panle_table = new Control_Panel_Table();
        JPanel panel = new JPanel( new BorderLayout() );
        panel.setBorder( BorderFactory.createLineBorder( control_panle_table.getGridColor() ) );
        panel.add( control_panle_table.getTableHeader(), BorderLayout.NORTH );
        panel.add( control_panle_table);
        setCaptionStyle( cpt_controlpanel );
        setTextLabelStyle( lbl_io_controlpanel_switch );
        setTextLabelStyle( lbl_controlpanel_strategy );
        lbl_io_controlpanel_switch.setText( EventID.getString( EventID.NONSTOP_OPERATION_PANEL_SWITCH.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_controlpanel_switch, EventID.NONSTOP_OPERATION_PANEL_SWITCH.eventID );
        add( cpt_controlpanel, "gapbottom 18-12, span, aligny center" );
        add( lbl_io_controlpanel_switch, "skip 1, span, gapright 12" );
        add( io_controlpanel_switch, "skip 2, span, gapright 12" );
        add( lbl_controlpanel_strategy, "skip 1, span, gapright 12" );
        add( panel, "skip 2, span, wrap 30, top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_lock_setting = new JLabel();
        lbl_Select_floor = new JLabel();
        cbo_Select_floor = new MyComboBox();
        lbl_car_lock = new JLabel();
        io_car_lock = new IOBar(true);
        lbl_car_lock_strategy = new JLabel();
        cbo_car_lock_strategy = new MyComboBox( Lock_Strategy.values() );
        lbl_hall_lock = new JLabel();
        io_hall_lock = new IOBar(true);
        lbl_hall_lock_strategy = new JLabel();
        cbo_hall_lock_strategy = new MyComboBox(Lock_Strategy.values());
        setCaptionStyle(cpt_lock_setting);
        setTextLabelStyle(lbl_Select_floor);
        setTextLabelStyle(lbl_car_lock);
        setTextLabelStyle(lbl_car_lock_strategy);
        setTextLabelStyle(lbl_hall_lock);
        setTextLabelStyle(lbl_hall_lock_strategy);

        add( cpt_lock_setting, "gapbottom 18-12, span, aligny center" );
        add( lbl_Select_floor, "skip 1, span, gapright 12" );
        add( cbo_Select_floor, "skip 2, span, wrap 10, gapright 12" );
        add( lbl_car_lock, "skip 1, span, gapright 12" );
        add( io_car_lock, "skip 2, span, wrap 10, gapright 12" );
        add( lbl_car_lock_strategy, "skip 1, span, gapright 12" );
        add( cbo_car_lock_strategy, "skip 2, span, wrap 10, gapright 12" );
        add( lbl_hall_lock, "skip 1, span, gapright 12" );
        add( io_hall_lock, "skip 2, span, wrap 10, gapright 12" );
        add( lbl_hall_lock_strategy, "skip 1, span, gapright 12" );
        add( cbo_hall_lock_strategy, "skip 2, span, wrap 10, gapright 12" );
        cbo_Select_floor.setPreferredSize(new Dimension(100, 25));
        cbo_Select_floor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int floor = cbo_Select_floor.getSelectedIndex();
				current_floor_index = floor;
				IOBar_Listenner(floor);
				refresh();
			}
		});
        
        cbo_car_lock_strategy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int floor = cbo_Select_floor.getSelectedIndex();
				int index = cbo_car_lock_strategy.getSelectedIndex();
				car_lock_strategy[floor] = (byte)index;
			}
		});
        
        cbo_hall_lock_strategy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int floor = cbo_Select_floor.getSelectedIndex();
				int index = cbo_hall_lock_strategy.getSelectedIndex();
				hall_lock_strategy[floor] = (byte)index;
			}
		});
        
        bindGroup("controlMode", cpt_controlMode, ebd_controlMode);
        bindGroup(new AbstractButton[]{ ebd_enable }, ebd_controlMode, io_controlpanel_switch, control_panle_table,
        		cbo_Select_floor, io_car_lock, cbo_car_lock_strategy, io_hall_lock, cbo_hall_lock_strategy);
        
        IOBar_Listenner(0);
        loadI18N();
        revalidate();
    }
    
    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_BOLD );
        c.setForeground(Color.WHITE);
    }
    
    private void setTextLabelStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }
    
    public String getBundleText ( String key, String defaultValue ) {
        String result;
        try {
            result = TEXT.getString( key );
        } catch ( Exception e ) {
            result = defaultValue;
        }
        return result;
    }
    
    private void bindGroup ( final AbstractButton[] primary, final JComponent... list ) {
        for ( AbstractButton p : primary ) {
            p.addItemListener( new ItemListener() {
                public void itemStateChanged ( ItemEvent e ) {
                    boolean enabled = false;
                    for ( AbstractButton p : primary ) {
                        if ( p.isSelected() && p.isEnabled() ) {
                            enabled = true;
                            break;
                        }
                    }
                    for ( JComponent c : list )
                        c.setEnabled( enabled );
                }
            } );
            p.addPropertyChangeListener( "enabled", new PropertyChangeListener() {
                @Override
                public void propertyChange ( PropertyChangeEvent evt ) {
                    boolean enabled = false;
                    for ( AbstractButton p : primary ) {
                        if ( p.isSelected() && p.isEnabled() ) {
                            enabled = true;
                            break;
                        }
                    }
                    for ( JComponent c : list )
                        c.setEnabled( enabled );
                }
            } );
        }

        boolean enabled = false;
        for ( AbstractButton p : primary ) {
            if ( p.isSelected() && p.isEnabled() ) {
                enabled = true;
                break;
            }
        }
        for ( JComponent c : list )
            c.setEnabled( enabled );
    }
    
    private void bindGroup ( final String detailKey, final JComponent... list ) {
        for ( JComponent c : list ) {
            c.addMouseListener( new MouseAdapter() {
                @Override
                public synchronized void mouseEntered ( MouseEvent evt ) {
                    if ( settingPanel != null )
                        settingPanel.setDescription( TEXT.getString( detailKey + "_description" ) );
                }
                @Override
                public void mouseExited ( MouseEvent e ) {
                    if ( settingPanel != null )
                        settingPanel.setDescription( null );
                }
            } );
        }
    }
    
    private void loadI18N () {
    	cpt_general.setText(getBundleText("cpt_general", "General"));
    	ebd_enable.setText(getBundleText("ebd_enable", "Enable"));
    	
    	cpt_controlMode.setText(getBundleText("cpt_controlMode", "Control Mode Enabled"));
    	ebd_controlMode.setText(getBundleText("ebd_controlMode", "Enable"));
    	
    	cpt_controlpanel.setText(getBundleText("cpt_controlpanel", "Control Panle"));
    	lbl_controlpanel_strategy.setText(getBundleText("lbl_controlpanel_strategy", "Control Panle Strategy"));
    	
    	cpt_lock_setting.setText(getBundleText("cpt_lock_setting", "Lock Setting"));
    	lbl_Select_floor.setText(getBundleText("lbl_Select_floor", "Control Panle"));
    	lbl_car_lock.setText(getBundleText("lbl_car_lock", "Control Panle"));
    	lbl_car_lock_strategy.setText(getBundleText("lbl_car_lock_strategy", "Control Panle"));
    	lbl_hall_lock.setText(getBundleText("lbl_hall_lock", "Control Panle"));
    	lbl_hall_lock_strategy.setText(getBundleText("lbl_hall_lock_strategy", "Control Panle"));
    }
    
    public final static class Control_Panel_Table extends JTable {
		private static final long serialVersionUID = -502243421681857978L;
		private ControlPanleEnableTableModel model;
		
		public Control_Panel_Table() {
			super();
			model = new ControlPanleEnableTableModel();
			setModel(model);
			setRowSelectionAllowed( true );
            setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
            setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
            setGridColor(StartUI.SUB_BACKGROUND_COLOR);
            setShowGrid(true);
            setRowHeight(20);
            getColumnModel().getColumn( 0 ).setCellRenderer( headerRenderer );
            getColumnModel().getColumn( 1 ).setCellRenderer( headerRenderer );
            getColumnModel().getColumn( 2 ).setCellRenderer( new headerCellRenderer(this) );
            getColumnModel().getColumn( 3 ).setCellRenderer( new headerCellRenderer(this) );
        }
		
		@Override
	    public Component prepareEditor ( TableCellEditor editor, int row, int column ) {
	        Component c = super.prepareEditor( editor, row, column );
	        if ( c instanceof JTextComponent ) {
	            ( ( JTextComponent )c ).selectAll();
	        }
	        return c;
	    }
		  
        public void refreshData (NostopOperaPanelBean bean) {
        	model.ChangeData(bean);
        	model.fireTableDataChanged();
        	for(int i = 0; i < this.getColumnCount(); i++) {
            	setTableHeaderColor(this, i, StartUI.MAIN_BACKGROUND_COLOR);
            }
            JTableHeader head =  this.getTableHeader();
            head.setPreferredSize(new Dimension(1, 30));
        }
        
        public byte[] getData() {
        	return model.bean.getOperaPanelStrategy();
        }
        
    }
    
	public static class ControlPanleEnableTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -5412068488028185147L;
		private String[] title	= {	TEXT.getString( "floor" ),
											TEXT.getString("floor_text"),
											TEXT.getString( "car_operation" ),
											TEXT.getString( "hall_operation" ), };
		private int floorCount;
		private NostopOperaPanelBean bean;
		
		public void ChangeData(NostopOperaPanelBean bean) {
			this.floorCount = bean.getFloorcount();
			this.bean = bean;
		}
		
        @Override
        public int getRowCount () {
            return floorCount;
        }

        @Override
        public int getColumnCount () {
            return 4;
        }

        @Override
		public String getColumnName(int column) {
        	 return title[ column ];
		}

		@Override
        public Class<?> getColumnClass ( int col ) {
            if ( col <= 1 ) {
                return String.class;
            }
            return Boolean.class;
        }

        @Override
        public Object getValueAt ( int rowIndex, int columnIndex ) {
            try {
                if ( columnIndex == 0 ) {
                    return rowIndex;
                }else if ( columnIndex == 1 ) {
                    return bean.getFloorText()[ rowIndex ];
                }else if ( columnIndex == 2 ) {
                    return ( bean.getOperaPanelStrategy()[ rowIndex ] & 0x01 ) != 0;
                }else{
                	return ( (bean.getOperaPanelStrategy()[ rowIndex ] >> 1) & 0x01 ) != 0;                	
                }
                
            } catch ( Exception e ) {
                return false;
            }
        }

        @Override
        public boolean isCellEditable ( int row, int col ) {
            if ( col <= 1 )
                return false;
            return true;
        }
        
        @Override
        public void setValueAt ( Object aValue, int row, int col ) {
        	try {
	            if ( col >= 2 && col < 4 ) {
	                int bitOffset = col - 2;
	                if ( aValue == Boolean.TRUE ) {
	                	bean.getOperaPanelStrategy()[ row ] = ( byte )( bean.getOperaPanelStrategy()[ row ] | ( 1 << bitOffset ) );
	                } else {
	                	bean.getOperaPanelStrategy()[ row ] = ( byte )( bean.getOperaPanelStrategy()[ row ] & ~ ( 1 << bitOffset ) );
	                }
	            }
        	}catch ( Exception e ) {
            }
        	fireTableCellUpdated( row, col );
        }
    }
	
	private void IOBar_Listenner(int floor) {
		if( handleCarCallLock != null ) {
			io_car_lock.removeNewEventListener( handleCarCallLock );
			io_car_lock.removeActionListener( handleCarCallLock );
		}
		
		if( handleHallCallLock != null ) {
			io_hall_lock.removeNewEventListener( handleHallCallLock );
			io_hall_lock.removeActionListener( handleHallCallLock );
		}
		
		handleCarCallLock = new OpenIOEditorDialogListener( io_car_lock, EventID.getCarLockCallID( floor ) );
		handleHallCallLock = new OpenIOEditorDialogListener( io_hall_lock, EventID.getHallLockCallID( floor ) );
		
		io_car_lock.addNewEventListener( handleCarCallLock );
		io_car_lock.addActionListener( handleCarCallLock );
		
		io_hall_lock.addNewEventListener( handleHallCallLock );
		io_hall_lock.addActionListener( handleHallCallLock );
	}
	
    private void setCarLockCallEvent ( Event evt ) throws DataFormatException {
    	io_car_lock.setEvent( evt );
    }
    
    private void setCarLockCallStrategy( int index ) {
    	cbo_car_lock_strategy.setSelectedIndex( index );
    } 
    
    private void setHallLockCallEvent ( Event evt ) throws DataFormatException {
    	io_hall_lock.setEvent( evt );
    }
    
    private void setHallLockCallStrategy( int index ) {
    	cbo_hall_lock_strategy.setSelectedIndex( index );
    } 
    
	private synchronized void refresh () {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
            	int selectItem = cbo_Select_floor.getSelectedIndex();
            	 if ( eventAggregator != null ) {
                     Event evtCarLockCall       = eventAggregator.getEvent( EventID.getCarLockCallID(selectItem) );
                     Event evtHallLockCall        = eventAggregator.getEvent( EventID.getHallLockCallID( selectItem ) );
                     try {
                    	 setCarLockCallEvent( evtCarLockCall );
                    	 setHallLockCallEvent( evtHallLockCall );
                    	 setCarLockCallStrategy( car_lock_strategy[selectItem] );
                    	 setHallLockCallStrategy( hall_lock_strategy[selectItem] );
                     } catch ( DataFormatException e ) {
                         e.printStackTrace();
                     }
                 }
            }
        } );
    }
	
    public EventAggregator getEventAggregator () {
        return this.eventAggregator;
    }
    
	public NostopOperaPanelBean getNostopOperaPanelBean() {
		NostopOperaPanelBean bean = new NostopOperaPanelBean();
		bean.setEnable(ebd_enable.isSelected());
		bean.setContrlMode(ebd_controlMode.isSelected());
		bean.setOperaSwitch(io_controlpanel_switch.getEvent());
    	bean.setOperaPanelStrategy(control_panle_table.getData());
    	bean.setCarLockStrategy(this.car_lock_strategy);
    	bean.setHallLockStrategy(this.hall_lock_strategy);
    	return bean;
    }
    
    public void setNostopOperaPanelBean( LiftConnectionBean connBean, NostopOperaPanelBean bean, EventAggregator eventAggregator) {
    	ebd_enable.setOriginSelected(bean.getEnable());
    	ebd_controlMode.setOriginSelected(bean.getContrlMode());
    	io_controlpanel_switch.setEvent(bean.getOperaSwitch());
    	control_panle_table.refreshData(bean.clone());
    	this.connBean = connBean;
    	this.eventAggregator = eventAggregator;
    	System.arraycopy(bean.getCarLockStrategy(), 0, this.car_lock_strategy, 0, 128);
    	System.arraycopy(bean.getHallLockStrategy(), 0, this.hall_lock_strategy, 0, 128);
    	
    	cbo_Select_floor.setModel(new DefaultComboBoxModel<>(bean.getFloorText()));
        cbo_Select_floor.setSelectedIndex(current_floor_index);
        
        refresh();
    }
    
	public static class NostopOperaPanelBean {
		private Boolean enable;
		private Boolean contrlMode;
		private Event OperaSwitch;
		private int floorcount = 128;
		private String[] FloorText;
		private byte[] OperaPanelStrategy;
		private byte[] CarLockStrategy;
		private byte[] HallLockStrategy;
    	
    	public Boolean getEnable() {
			return enable;
		}

		public void setEnable(Boolean enable) {
			this.enable = enable;
		}

		public Boolean getContrlMode() {
			return contrlMode;
		}

		public void setContrlMode(Boolean contrlMode) {
			this.contrlMode = contrlMode;
		}
		
    	public Event getOperaSwitch() {
			return OperaSwitch;
		}

		public void setOperaSwitch(Event operaSwitch) {
			OperaSwitch = operaSwitch;
		}

		public int getFloorcount() {
			return floorcount;
		}

		public void setFloorcount(int floorcount) {
			this.floorcount = floorcount;
		}
		
    	public String[] getFloorText() {
			return FloorText;
		}

		public void setFloorText(String[] floorText) {
			FloorText = floorText;
		}

		public byte[] getOperaPanelStrategy() {
			return OperaPanelStrategy;
		}

		public void setOperaPanelStrategy(byte[] operaPanelStrategy) {
			OperaPanelStrategy = operaPanelStrategy;
		}
		
		public byte[] getCarLockStrategy() {
			return CarLockStrategy;
		}

		public void setCarLockStrategy(byte[] carLockStrategy) {
			CarLockStrategy = carLockStrategy;
		}

		public byte[] getHallLockStrategy() {
			return HallLockStrategy;
		}

		public void setHallLockStrategy(byte[] hallLockStrategy) {
			HallLockStrategy = hallLockStrategy;
		}

		@Override
		protected NostopOperaPanelBean clone() {
			// TODO Auto-generated method stub
			NostopOperaPanelBean bean = new NostopOperaPanelBean();
			bean.setFloorcount(floorcount);
			bean.setFloorText(Arrays.copyOf(FloorText, FloorText.length));
			bean.setOperaPanelStrategy(Arrays.copyOf(OperaPanelStrategy, OperaPanelStrategy.length));
			return bean;
		}
		
    }

	public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }
    
    public final class OpenIOEditorDialogListener implements NewEventListener, ActionListener {
        private final int eventID;
        private IOBar     iobar;

        OpenIOEditorDialogListener ( IOBar iobar, int evtID ) {
            this.eventID = evtID;
            this.iobar   = iobar;
        }


        @Override
        public void newEventOccurred ( NewEvent evt ) {
            Event e = null;
            if ( evt.getSource() == iobar ) {
                e = IOEditorDialog.showDialog( connBean, eventID, iobar.getEvent() );
            }
            
            if ( e != null && eventAggregator != null ) {
                eventAggregator.setEvent( eventID, e );
                refresh();
            }
        }


        @Override
        public void actionPerformed ( ActionEvent evt ) {
            Event e = null;
            if ( evt.getSource() == iobar && evt.getActionCommand().indexOf( "INPUT" ) >= 0 ){
                e = IOEditorDialog.showDialog( connBean, eventID, iobar.getEvent(), Type.INPUT, evt.getID() );
            }
            
            if ( evt.getSource() == iobar && evt.getActionCommand().indexOf( "OUTPUT" ) >= 0 ){
                e = IOEditorDialog.showDialog( connBean, eventID, iobar.getEvent(), Type.OUTPUT, evt.getID() );
            }
            
            if ( e != null && eventAggregator != null ) {
                eventAggregator.setEvent( eventID, e );
                refresh();
            }
        }
    }
}
