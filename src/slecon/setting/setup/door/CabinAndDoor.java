package slecon.setting.setup.door;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyCheckBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueTextField;
import slecon.home.PosButton;
import slecon.setting.installation.CommissionSetting;
import slecon.setting.setup.door.CabinAndDoor.DoorEnableTableModel;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;

class headerCellRenderer implements TableCellRenderer{
	DoorEnableTableModel tableModel;
	JTableHeader tableHeader;
	MyCheckBox myCheckBox = null;
	
	public headerCellRenderer(JTable table) {
		// TODO Auto-generated constructor stub
		this.tableModel = (DoorEnableTableModel)table.getModel();
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

public class CabinAndDoor extends JPanel implements ActionListener {
    private static final long     serialVersionUID = 6584021027904307178L;
    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.door.CabinAndDoor");
    private ImageIcon 	BUTTON_PAUSE_ICON  = null;
    private ImageIcon 	BUTTON_START_ICON  = null;
    
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
    private boolean            started = false;
    private SettingPanel<CabinAndDoor> settingPanel;
    private JLabel             cpt_enable;
    private Door_Enable_Table	door_enable_table;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel			   cpt_weight_monitor;
    private JLabel			   lbl_weight_value;
    private ValueTextField	   val_weight_value;
    private ValueCheckBox	   ebd_digital_enable;
    private ValueCheckBox	   ebd_analog_enable;
    private PosButton		   btn_no_load_revise;
    private PosButton		   btn_full_load_revise;
    private JLabel			   lbl_sensor_status;
    private WeightSensorTable	weight_sensor_table;
    /* ---------------------------------------------------------------------------- */
    
    public CabinAndDoor () {
        initGUI();
    }

    public void setSettingPanel ( SettingPanel<CabinAndDoor> panel ) {
        this.settingPanel = panel;
    }

    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 24, gap 0 6", "[40::40][20::20][32::32][]" ) );
        if(BaseFactory.getLocaleString().equals("en")) {
        	BUTTON_PAUSE_ICON  = ImageFactory.BUTTON_PAUSE.icon(120, 30);
        	BUTTON_START_ICON  = ImageFactory.BUTTON_START.icon(120, 30);
        }
        else {
        	BUTTON_PAUSE_ICON  = ImageFactory.BUTTON_PAUSE.icon(87, 30);
        	BUTTON_START_ICON  = ImageFactory.BUTTON_START.icon(87, 30);
        }
        
        /* ---------------------------------------------------------------------------- */
        cpt_enable     = new JLabel();
        setCaptionStyle( cpt_enable );
        add( cpt_enable, "gapbottom 18-12, span, aligny center" );
        
        door_enable_table = new Door_Enable_Table();
        JPanel panel = new JPanel( new BorderLayout() );
        panel.setBorder( BorderFactory.createLineBorder( door_enable_table.getGridColor() ) );
        panel.add( door_enable_table.getTableHeader(), BorderLayout.NORTH );
        panel.add( door_enable_table);
        add( panel, "skip 2, span, wrap 30, top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_weight_monitor = new JLabel();
        lbl_weight_value = new JLabel();
        val_weight_value = new ValueTextField();
        val_weight_value.setColumns( 5 );
        val_weight_value.setHorizontalAlignment( SwingConstants.RIGHT );
        val_weight_value.setEmptyValue( 0 );
        val_weight_value.setEnabled(false);
        ebd_digital_enable = new ValueCheckBox();
        ebd_digital_enable.addActionListener(this);
        ebd_analog_enable = new ValueCheckBox();
        ebd_analog_enable.addActionListener(this);
        btn_no_load_revise = new PosButton(BUTTON_PAUSE_ICON, BUTTON_START_ICON);
        btn_no_load_revise.addActionListener(this);
        btn_full_load_revise = new PosButton(BUTTON_PAUSE_ICON, BUTTON_START_ICON);
        btn_full_load_revise.addActionListener(this);
        lbl_sensor_status = new JLabel();
        weight_sensor_table = new WeightSensorTable();
        
        setCaptionStyle( cpt_weight_monitor );
        setTextLabelStyle( lbl_weight_value );
        setTextLabelStyle( lbl_sensor_status );
        setButtonStyle( btn_no_load_revise );
        setButtonStyle( btn_full_load_revise );
        
        add(cpt_weight_monitor, "gapbottom 18-12, span, aligny center");
        add(lbl_weight_value, "skip 1, span, split, gapright 12");
        add(val_weight_value, "gapright 12, wrap");
        add(ebd_digital_enable, "skip 1, span");
        add(ebd_analog_enable, "skip 1, span");
        add(btn_no_load_revise, "skip 2, span");
        add(btn_full_load_revise, "skip 2, span");
        add(lbl_sensor_status, "skip 1, span");
        add(weight_sensor_table, "skip 2, span, wrap");
        
        bindGroup("cpt_enable",cpt_enable);
        bindGroup( new AbstractButton[] { ebd_analog_enable }, btn_no_load_revise, btn_full_load_revise, lbl_sensor_status, weight_sensor_table );
       
        SetTableEnable(false);
        loadI18N();
        revalidate();
    }
    
    public void SetTableEnable(boolean enable) {
    	door_enable_table.setEnabled(enable);
    	ebd_digital_enable.setEnabled(enable);
    	ebd_analog_enable.setEnabled(enable);
    	btn_no_load_revise.setEnabled(enable);
    	btn_full_load_revise.setEnabled(enable);
    	weight_sensor_table.SetTableEnable(enable);
    }
    
    private void loadI18N () {
        cpt_enable.setText( getBundleText( "LBL_cpt_title", "Door open strategy" ) );
        cpt_weight_monitor.setText( getBundleText( "LBL_cpt_weight_monitor", "Weight Monitor" ) );
        lbl_weight_value.setText( getBundleText( "LBL_lbl_weight_value", "Current Load Value :" ) );
        ebd_digital_enable.setText( getBundleText( "LBL_ebd_digital_enable", "Digital Sensor" ) );
        ebd_analog_enable.setText( getBundleText( "LBL_ebd_analog_enable", "Analog Sensor" ) );
        btn_no_load_revise.setText( getBundleText( "LBL_btn_no_load_revise", "No-load Correction" ) );
        btn_full_load_revise.setText( getBundleText( "LBL_btn_full_load_revise", "Full Correction" ) );
        lbl_sensor_status.setText( getBundleText( "LBL_lbl_sensor_status", "Sensor State" ) );
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_BOLD );
        c.setForeground(Color.WHITE);
    }
    
    private void setTextLabelStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }
    
    private void setButtonStyle ( JComponent c ) {
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
    
    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }

    
    public static CabinAndDoor createPanel ( SettingPanel<CabinAndDoor> panel ) {
        CabinAndDoor gui = new CabinAndDoor();
        gui.setSettingPanel( panel );
        return gui;
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
    
    public final static class Door_Enable_Table extends JTable {
		private static final long serialVersionUID = -502243421681857978L;
		private DoorEnableTableModel model;
		
		public Door_Enable_Table() {
			super();
			model = new DoorEnableTableModel();
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
		  
        public void refreshData (DoorEnableBean bean) {
        	model.ChangeData(bean);
        	model.fireTableDataChanged();
        	for(int i = 0; i < this.getColumnCount(); i++) {
            	setTableHeaderColor(this, i, StartUI.MAIN_BACKGROUND_COLOR);
            }
            JTableHeader head =  this.getTableHeader();
            head.setPreferredSize(new Dimension(1, 30));
        }
        
        public byte[] getData() {
        	return model.bean.getDoorEnableAction();
        }
        
    }
    
    public static class DoorEnableTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 5806086409046325795L;
		private String[] title	= {	TEXT.getString( "floor" ),
											TEXT.getString("floor_text"),
											TEXT.getString( "front_door" ),
											TEXT.getString( "rear_door" ), };
		private int floorCount;
		private DoorEnableBean bean;
		
		public void ChangeData(DoorEnableBean bean) {
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
                    return ( bean.getDoorEnableAction()[ rowIndex ] & 0x01 ) != 0;
                }else{
                	return ( (bean.getDoorEnableAction()[ rowIndex ] >> 1) & 0x01 ) != 0;                	
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
	                	bean.getDoorEnableAction()[ row ] = ( byte )( bean.getDoorEnableAction()[ row ] | ( 1 << bitOffset ) );
	                } else {
	                	bean.getDoorEnableAction()[ row ] = ( byte )( bean.getDoorEnableAction()[ row ] & ~ ( 1 << bitOffset ) );
	                }
	            }
        	}catch ( Exception e ) {
            }
        	fireTableCellUpdated( row, col );
        }
    }
    
    public DoorEnableBean getDoorEnableBean() {
    	DoorEnableBean bean = new DoorEnableBean();
    	bean.setFloorcount(door_enable_table.getRowCount());
    	bean.setDoorEnableAction(door_enable_table.getData());
    	return bean;
    }
    
    public void setDoorEnableBean(DoorEnableBean bean) {
    	door_enable_table.refreshData(bean.clone());
    }
    
    public static class DoorEnableBean {
    	int floorcount = 128;
		String[] FloorText;
    	byte[] DoorEnableAction;
    	
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

		public byte[] getDoorEnableAction() {
			return DoorEnableAction;
		}

		public void setDoorEnableAction(byte[] doorEnableAction) {
			DoorEnableAction = doorEnableAction;
		}

		@Override
		protected DoorEnableBean clone() {
			// TODO Auto-generated method stub
			DoorEnableBean bean = new DoorEnableBean();
			bean.setFloorcount(floorcount);
			bean.setFloorText(Arrays.copyOf(FloorText, FloorText.length));
			bean.setDoorEnableAction(Arrays.copyOf(DoorEnableAction, DoorEnableAction.length));
			return bean;
		}
		
    }
    
    public void setLoadWeightData ( LoadWeight load ) {
    	LoadWeight load_bean = load.clone();
    	this.val_weight_value.setText( String.format( "%d%%", load_bean.getLoad() ));
    	this.ebd_digital_enable.setOriginSelected( !load_bean.isDigitalSernsor() );
    	this.ebd_analog_enable.setOriginSelected( load_bean.isDigitalSernsor() );
    	
    	final Number[][] tableData = new Number[ 4 ][ 1 ];
        for ( int i = 0; i < 4; i++ ) {
            tableData[ i ][ 0 ] = (float)load_bean.getLoad_sensor()[i];
        }
        weight_sensor_table.setData( tableData );
    }
    
    
    public static class LoadWeight{
    	private int load;
    	private boolean isDigitalSernsor;
    	short[] load_sensor;
    	
		public int getLoad() {
			return load;
		}
		public void setLoad(int load) {
			this.load = load;
		}
		public boolean isDigitalSernsor() {
			return isDigitalSernsor;
		}
		public void setDigitalSernsor(boolean isDigitalSernsor) {
			this.isDigitalSernsor = isDigitalSernsor;
		}
		public short[] getLoad_sensor() {
			return load_sensor;
		}
		public void setLoad_sensor(short[] load_sensor) {
			this.load_sensor = load_sensor;
		}
		
		@Override
		protected LoadWeight clone() {
			// TODO Auto-generated method stub
			LoadWeight bean = new LoadWeight();
			bean.setLoad( load );
			bean.setDigitalSernsor( isDigitalSernsor );
			bean.setLoad_sensor( load_sensor );
			return bean;
		}
    }
    
    protected void do_ebd_digital_enable_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof CabinAndDoorSetting ) {
        	final boolean selected = ebd_digital_enable.isSelected();
            if(selected) {
            	int ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "Digital_LoadWeight_Enabled.CONTEXT" ),
                        TEXT.getString( "CabinLoadSetting.TITLE" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
            	if ( ans == JOptionPane.YES_OPTION ) {
            		if(ebd_analog_enable.isSelected()) {
            			ebd_analog_enable.setSelected( false );
            		}
                    ( ( CabinAndDoorSetting )settingPanel ).setLoadWeightType((byte)0);
            	}else {
            		ebd_digital_enable.setSelected( false );
            		( ( CabinAndDoorSetting )settingPanel ).reset();
            	}
            	
            }else {
            	int ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "Digital_LoadWeight_Disabled.CONTEXT" ),
                        TEXT.getString( "CabinLoadSetting.TITLE" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
            	if ( ans == JOptionPane.YES_OPTION ) {
            		if(!ebd_analog_enable.isSelected()) {
            			ebd_analog_enable.setSelected( false );
            		}
                    ( ( CabinAndDoorSetting )settingPanel ).setLoadWeightType((byte)1);
            	}else {
            		ebd_digital_enable.setSelected( true );
            		( ( CabinAndDoorSetting )settingPanel ).reset();
            	}
            	
            }
        }
    }
    
    protected void do_ebd_Analog_enable_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof CabinAndDoorSetting ) {
        	final boolean selected = ebd_analog_enable.isSelected();
            if(selected) {
            	int ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "Analog_LoadWeight_Enabled.CONTEXT" ),
                        TEXT.getString( "CabinLoadSetting.TITLE" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
            	if ( ans == JOptionPane.YES_OPTION ) {
            		if(ebd_digital_enable.isSelected()) {
            			ebd_digital_enable.setSelected( false );
            		}
                    ( ( CabinAndDoorSetting )settingPanel ).setLoadWeightType((byte)1);
            	}else {
            		ebd_digital_enable.setSelected( false );
            		( ( CabinAndDoorSetting )settingPanel ).reset();
            	}
            	
            }else {
            	int ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), TEXT.getString( "Analog_LoadWeight_Disabled.CONTEXT" ),
                        TEXT.getString( "CabinLoadSetting.TITLE" ), JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
            	if ( ans == JOptionPane.YES_OPTION ) {
            		if(!ebd_digital_enable.isSelected()) {
            			ebd_digital_enable.setSelected( true );
            		}
                    ( ( CabinAndDoorSetting )settingPanel ).setLoadWeightType((byte)0);
            	}else {
            		ebd_digital_enable.setSelected( true );
            		( ( CabinAndDoorSetting )settingPanel ).reset();
            	}
            	
            }
        }
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() == ebd_digital_enable ) {
			do_ebd_digital_enable_actionPerformed( e );
		}
		
		if ( e.getSource() == ebd_analog_enable ) {
			do_ebd_Analog_enable_actionPerformed( e );
		}
			
		if( e.getSource() == btn_full_load_revise ) {
			( ( CabinAndDoorSetting )settingPanel ).setFullLoadCorrection();
		}
		
		if( e.getSource() == btn_no_load_revise ) {
			( ( CabinAndDoorSetting )settingPanel ).setEmptyLoadCorrection();
		}
			
	}
}
