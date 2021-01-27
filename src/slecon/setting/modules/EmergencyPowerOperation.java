package slecon.setting.modules;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;

import ocsjava.remote.configuration.Event;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyCheckBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueRadioButton;
import slecon.component.ValueTextField;
import slecon.component.iobar.IOBar;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.interfaces.ConvertException;
import slecon.setting.installation.FloorTextAndMapping.Fmt_floor_countDocumentListener;
import slecon.setting.modules.EmergencyPowerOperation.LiftSeletorTableModel;
import slecon.setting.modules.LedBehavior.Led_Behavior;
import logic.EventID;
import net.miginfocom.swing.MigLayout;
import base.cfg.FontFactory;
import comm.constants.DeviceMessage;

/**
 * Setup -> Module -> Emergency Power Operation.
 */
public class EmergencyPowerOperation extends JPanel {
    private static final long serialVersionUID = -9196811984218766150L;
    /**
     * Text resource.
     */
    public static final ResourceBundle            TEXT    = ToolBox.getResourceBundle( "setting.module.EmergencyPowerOperation" );
    private boolean                               started = false;
    private SettingPanel<EmergencyPowerOperation> settingPanel;
    private JLabel                                cpt_general;
    private ValueCheckBox                         ebd_enabled;
    private JLabel                                lbl_led_behavior;
    private MyComboBox                        	  cbo_led_behavior;
    private ValueCheckBox  						  ebd_front_door;
    private ValueCheckBox  						  ebd_rear_door;
    private JLabel                                lbl_door_close_time;
    private ValueTextField                        fmt_door_close_time;
    private JLabel                                lbl_return_floor;
    private MyComboBox                 			  cbo_return_floor;
    private JLabel                                lbl_car_message;
    private MyComboBox				              cbo_car_message;
    private JLabel                                lbl_hall_message;
    private MyComboBox				              cbo_hall_message;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel                                cpt_io_setting;
    private JLabel                                lbl_io_epo;
    private IOBar                                 io_epo;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel               				  cpt_strategy;
    private ValueRadioButton    				  rd_auto_run;
    private LiftSeletorTable              		  tbl_lift_list;
    private ValueRadioButton				      rd_manual_run;
    private JLabel                                lbl_io_manual_run;
    private IOBar                                 io_manual_run;
    
    
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
    
    public EmergencyPowerOperation () {
        initGUI();
    }

    public void setSettingPanel ( SettingPanel<EmergencyPowerOperation> panel ) {
        this.settingPanel = panel;
    }
    
    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_general               = new JLabel();
        ebd_enabled               = new ValueCheckBox();
        lbl_led_behavior		  = new JLabel();
        cbo_led_behavior		  = new MyComboBox( Led_Behavior.values() );
        ebd_front_door			  = new ValueCheckBox();
        ebd_rear_door			  = new ValueCheckBox();
        lbl_door_close_time		  = new JLabel();
        fmt_door_close_time       = new ValueTextField();
        lbl_return_floor          = new JLabel();
        cbo_return_floor          = new MyComboBox();
        lbl_car_message           = new JLabel();
        cbo_car_message           = new MyComboBox( DeviceMessage.values() );
        lbl_hall_message          = new JLabel();
        cbo_hall_message          = new MyComboBox( DeviceMessage.values() );
        
        setCaptionStyle( cpt_general );
        setTextLabelStyle( lbl_led_behavior );
        cbo_led_behavior.setPreferredSize(new Dimension(120, 25));
        setTextLabelStyle( lbl_door_close_time );
        fmt_door_close_time.setColumns( 10 );
        fmt_door_close_time.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_close_time.setScope( Long.class, 0L, 255L, false, true );
        fmt_door_close_time.setEmptyValue( 1L );
        setComboBoxLabelStyle( lbl_return_floor );
        setComboBoxValueStyle( cbo_return_floor );
        cbo_return_floor.setPreferredSize(new Dimension(50, 25));
        setComboBoxLabelStyle( lbl_car_message );
        setComboBoxValueStyle( cbo_car_message );
        setComboBoxLabelStyle( lbl_hall_message );
        setComboBoxValueStyle( cbo_hall_message );
        
        add( cpt_general, "gapbottom 18-12, span, aligny center, top" );
        add( ebd_enabled, "skip 1, span, top" );
        add( lbl_return_floor, "skip 2, span 1, left, top" );
        add( cbo_return_floor, "span 1, left, wrap, top" );
        add( ebd_front_door, "skip 2, span 1, left, top" );
        add( ebd_rear_door, "span 1, left, wrap, top" );
        add( lbl_door_close_time, "skip 2, span 1, left, top" );
        add( fmt_door_close_time, "span 1, left, wrap, top" );
        add( lbl_led_behavior, "skip 2, span 1, left, top" );
        add( cbo_led_behavior, "span 1, left, wrap, top" );
        add( lbl_car_message, "skip 2, span 1, left, top" );
        add( cbo_car_message, "span 1, left, wrap, top" );
        add( lbl_hall_message, "skip 2, span 1, left, top" );
        add( cbo_hall_message, "span 1, left, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_io_setting = new JLabel();
        lbl_io_epo     = new JLabel();
        io_epo         = new IOBar( true );

        setCaptionStyle( cpt_io_setting );
        setTextLabelStyle( lbl_io_epo );
        lbl_io_epo.setText( EventID.getString( EventID.EPO_SWITCH.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_epo, EventID.EPO_SWITCH.eventID );
        add( cpt_io_setting, "gapbottom 18-12, span, top" );
        add( lbl_io_epo, "skip 2, span, gapright 12, top" );
        add( io_epo, "skip 2, span, wrap 30, top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_strategy   = new JLabel();
        rd_auto_run    = new ValueRadioButton();
        tbl_lift_list  = new LiftSeletorTable();
        JPanel panel = new JPanel( new BorderLayout() );
        panel.setBorder( BorderFactory.createLineBorder( StartUI.BORDER_COLOR ) );
        panel.add( tbl_lift_list.getTableHeader(), BorderLayout.NORTH );
        panel.add( tbl_lift_list);
        
        
        rd_manual_run  = new ValueRadioButton();
        lbl_io_manual_run = new JLabel();
        io_manual_run = new IOBar( true );
        		
        setCaptionStyle( cpt_strategy );
        setTextLabelStyle( lbl_io_manual_run );
        lbl_io_manual_run.setText( EventID.getString( EventID.EVTID_EPO_MANUAL.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_manual_run, EventID.EVTID_EPO_MANUAL.eventID );
        
        add( cpt_strategy, "gapbottom 18-12, span, aligny center, top" );
        add( rd_auto_run, "skip 1, span, wrap, top" );
        add( panel, "skip 1, span, wrap, top" );
        add( rd_manual_run, "skip 1, span, top" );
        add( lbl_io_manual_run, "skip 2, span, gapright 12, top" );
        add( io_manual_run, "skip 2, span, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "ebd_front_door", ebd_front_door );
        bindGroup( "ebd_rear_door", ebd_rear_door );
        bindGroup( "door_close_time", lbl_door_close_time, fmt_door_close_time );
        bindGroup( "return_floor", lbl_return_floor, cbo_return_floor );
        bindGroup( "car_message", lbl_car_message, cbo_car_message );
        bindGroup( "hall_message", lbl_hall_message, cbo_hall_message );
        bindGroup( "rd_auto_run", rd_auto_run );
        bindGroup( "rd_manual_run", rd_manual_run );
        

        ButtonGroup bg = new ButtonGroup();
        bg.add( rd_auto_run );
        bg.add( rd_manual_run );
        
        bindGroup( new JToggleButton[]{ ebd_enabled }, lbl_led_behavior, cbo_led_behavior, ebd_front_door, ebd_rear_door, 
        		lbl_door_close_time, fmt_door_close_time, lbl_return_floor, cbo_return_floor, lbl_car_message, cbo_car_message,
        		lbl_hall_message, cbo_hall_message, lbl_io_epo, io_epo, rd_auto_run, tbl_lift_list, rd_manual_run, lbl_io_manual_run, io_manual_run );
        bindGroup( new JToggleButton[]{ rd_auto_run }, tbl_lift_list );
        bindGroup( new JToggleButton[]{ rd_manual_run }, lbl_io_manual_run, io_manual_run );
        
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_led_behavior.setText( TEXT.getString( "lbl_led_behavior" ) );
        ebd_front_door.setText( TEXT.getString( "ebd_front_door" ) );
        ebd_rear_door.setText( TEXT.getString( "ebd_rear_door" ) );
        lbl_door_close_time.setText( TEXT.getString( "lbl_door_close_time" ) );
        lbl_return_floor.setText( TEXT.getString( "lbl_return_floor" ) );
        lbl_car_message.setText( TEXT.getString( "car_message" ) );
        lbl_hall_message.setText( TEXT.getString( "hall_message" ) );

        /* ---------------------------------------------------------------------------- */
        
        cpt_io_setting.setText( TEXT.getString( "IO_settings" ) );

        /* ---------------------------------------------------------------------------- */
        
        cpt_strategy.setText( TEXT.getString( "scheme" ) );
        rd_auto_run.setText( TEXT.getString( "rd_auto_run" ) );
        rd_manual_run.setText( TEXT.getString( "rd_manual_run" ) );
        
        /* ---------------------------------------------------------------------------- */
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setTextLabelStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setComboBoxLabelStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setComboBoxValueStyle ( JComboBox<?> c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
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


    public GeneralBean getGeneralBean () throws ConvertException {
        if ( ! fmt_door_close_time.checkValue() )
            throw new ConvertException();

        GeneralBean bean_general = new GeneralBean();
        bean_general.setEnabled( ebd_enabled.isSelected() );
        bean_general.setDoorOpenLed(( Led_Behavior )cbo_led_behavior.getSelectedItem() );
        bean_general.setDoorFrontOpen( ebd_front_door.isSelected() );
        bean_general.setDoorRearOpen( ebd_rear_door.isSelected() );
        bean_general.setCloseDoorTime( (Long)fmt_door_close_time.getValue() );
        bean_general.setReturnFloor( (FloorText)cbo_return_floor.getSelectedItem() );;
        bean_general.setCarMessage( ( DeviceMessage )cbo_car_message.getSelectedItem() );
        bean_general.setHallMessage( ( DeviceMessage )cbo_hall_message.getSelectedItem() );
        
        bean_general.setEpoEvent( io_epo.getEvent() );
        bean_general.setAutoRunSeletor( tbl_lift_list.getData() );
        
        bean_general.setStragetyAuto( rd_auto_run.isSelected() );
        bean_general.setStragetyManual( rd_manual_run.isSelected() );
        bean_general.setManualEvent( io_manual_run.getEvent() );
        return bean_general;
    }
    

    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.cbo_led_behavior.setSelectedItem( bean_general.getDoorOpenLed() );
        this.ebd_front_door.setOriginSelected( bean_general.getDoorFrontOpen() != null && bean_general.getDoorFrontOpen() == true );
        this.ebd_rear_door.setOriginSelected( bean_general.getDoorRearOpen() != null && bean_general.getDoorRearOpen() == true );
        this.fmt_door_close_time.setOriginValue( bean_general.getCloseDoorTime() );
        this.cbo_return_floor.setSelectedItem( bean_general.getReturnFloor() );
        this.cbo_car_message.setSelectedItem( bean_general.getCarMessage() );
        this.cbo_hall_message.setSelectedItem( bean_general.getHallMessage() );
        
        this.io_epo.setEvent( bean_general.getEpoEvent() );
        this.tbl_lift_list.refreshData( bean_general.clone().getAutoRunSeletor() );
        
        this.rd_auto_run.setOriginSelected( bean_general.getStragetyAuto() != null && bean_general.getStragetyAuto() == true );
        this.rd_manual_run.setOriginSelected( bean_general.getStragetyManual() != null && bean_general.getStragetyManual() == true );
        this.io_manual_run.setEvent(bean_general.getManualEvent() );
    }

    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static EmergencyPowerOperation createPanel ( SettingPanel<EmergencyPowerOperation> panel ) {
        EmergencyPowerOperation gui = new EmergencyPowerOperation();
        gui.setSettingPanel( panel );
        return gui;
    }


    public void setFloorText ( ArrayList<FloorText> list ) {
        cbo_return_floor.removeAllItems();
        for ( FloorText text : list )
            cbo_return_floor.addItem( text );
    }


    public static class GeneralBean {
        private Boolean       enabled;
        private Led_Behavior  doorOpenLed;
        private Boolean		  doorFrontOpen;
        private Boolean		  doorRearOpen;
        private byte		  runStategy;
        private FloorText	  returnFloor;
        private Long		  closeDoorTime;
        private DeviceMessage carMessage;
        private DeviceMessage hallMessage;
        private Boolean 	  StragetyAuto;
        private Boolean 	  StragetyManual;
        private int		  	  autoRunSeletor;
        private Event    	  epoEvent;
        private Event		  manualEvent;
        
		public Boolean getEnabled() {
			return enabled;
		}
		public Led_Behavior getDoorOpenLed() {
			return doorOpenLed;
		}
		public byte getRunStategy() {
			return runStategy;
		}
		public Long getCloseDoorTime() {
			return closeDoorTime;
		}
		public DeviceMessage getCarMessage() {
			return carMessage;
		}
		public DeviceMessage getHallMessage() {
			return hallMessage;
		}
		public int getAutoRunSeletor() {
			return autoRunSeletor;
		}
		public Event getEpoEvent() {
			return epoEvent;
		}
		public Event getManualEvent() {
			return manualEvent;
		}
		public Boolean getDoorFrontOpen() {
			return doorFrontOpen;
		}
		public Boolean getDoorRearOpen() {
			return doorRearOpen;
		}
		public FloorText getReturnFloor() {
			return returnFloor;
		}
		public Boolean getStragetyAuto() {
			return StragetyAuto;
		}
		public Boolean getStragetyManual() {
			return StragetyManual;
		}
		
		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}
		public void setDoorOpenLed(Led_Behavior doorOpenLed) {
			this.doorOpenLed = doorOpenLed;
		}
		public void setRunStategy(byte runStategy) {
			this.runStategy = runStategy;
		}
		public void setCloseDoorTime(Long closeDoorTime) {
			this.closeDoorTime = closeDoorTime;
		}
		public void setCarMessage(DeviceMessage carMessage) {
			this.carMessage = carMessage;
		}
		public void setHallMessage(DeviceMessage hallMessage) {
			this.hallMessage = hallMessage;
		}
		public void setAutoRunSeletor(int autoRunSeletor) {
			this.autoRunSeletor = autoRunSeletor;
		}
		public void setEpoEvent(Event epoEvent) {
			this.epoEvent = epoEvent;
		}
		public void setManualEvent(Event manualEvent) {
			this.manualEvent = manualEvent;
		}
		public void setDoorFrontOpen(Boolean doorFrontOpen) {
			this.doorFrontOpen = doorFrontOpen;
		}
		public void setDoorRearOpen(Boolean doorRearOpen) {
			this.doorRearOpen = doorRearOpen;
		}
		public void setReturnFloor(FloorText returnFloor) {
			this.returnFloor = returnFloor;
		}
		public void setStragetyAuto(Boolean stragetyAuto) {
			StragetyAuto = stragetyAuto;
		}
		public void setStragetyManual(Boolean stragetyManual) {
			StragetyManual = stragetyManual;
		}
		
		protected GeneralBean clone() {
			GeneralBean bean = new GeneralBean();
			bean.setAutoRunSeletor( autoRunSeletor );
			return bean;
		}
		
    }
    
    public static class LiftSeletorTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[] title	= {	"L1", "L2", "L3", "L4", "L5", "L6", "L7", "L8", "L9", "L10", "L11", "L12", "L13", "L14", "L15", "L16"};
		byte[] stategy = new byte[16];
		
		public void ChangeData( int enable ) {
			for( int i = 0; i < 16; i ++) {
				stategy[i] = (byte)(( enable & ( 1 << i ) ) >> i); 
			}
		}
		
        @Override
        public int getRowCount () {
            return 1;
        }

        @Override
        public int getColumnCount () {
            return 16;
        }

        @Override
		public String getColumnName(int column) {
        	 return title[ column ];
		}

		@Override
        public Class<?> getColumnClass ( int col ) {
            return Boolean.class;
        }

        @Override
        public Object getValueAt ( int rowIndex, int columnIndex ) {
            try {
            	return stategy[ columnIndex ] != 0;
            } catch ( Exception e ) {
                return false;
            }
        }

        @Override
        public boolean isCellEditable ( int row, int col ) {
            return true;
        }

        @Override
        public void setValueAt ( Object aValue, int row, int col ) {
        	try {
        		if ( aValue == Boolean.TRUE ) {
        			stategy[ col ] = 1 ;
                }else {
                	stategy[ col ] = 0 ;
                }
        	}catch ( Exception e ) {
            }
        	fireTableCellUpdated( row, col );
        }
    }
    
    public final static class LiftSeletorTable extends JTable {
		private static final long serialVersionUID = 3565969239581890690L;
		private LiftSeletorTableModel model;
		
		public LiftSeletorTable() {
			super();
			model = new LiftSeletorTableModel();
			setModel(model);
			setRowSelectionAllowed( true );
            setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
            setGridColor(StartUI.SUB_BACKGROUND_COLOR);
            setShowGrid(true);
            setRowHeight(25);
            
            for(int i = 0; i < this.getColumnCount(); i++) {
            	getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
            	getColumnModel().getColumn( i ).setPreferredWidth(50);
            	getColumnModel().getColumn( i ).setCellRenderer(new LiftSeletorCheckBoxCellRenderer(this));
            }
            
            getTableHeader().setPreferredSize(new Dimension(1, 30));
        }
		
		@Override
	    public Component prepareEditor ( TableCellEditor editor, int row, int column ) {
	        Component c = super.prepareEditor( editor, row, column );
	        if ( c instanceof JTextComponent ) {
	            ( ( JTextComponent )c ).selectAll();
	        }
	        return c;
	    }
		  
        public void refreshData ( int enable ) {
        	model.ChangeData(enable);
        	model.fireTableDataChanged();
        }
        
        public int getData() {
        	int data = 0;
        	for( int i = 0; i < 16; i ++ ) {
        		if( model.stategy[ i ] == 1 ) {
        			data |= 1 << i;
        		}
        	}
        	return data;
        }
        
    }

}

class LiftSeletorCheckBoxCellRenderer implements TableCellRenderer{
	LiftSeletorTableModel tableModel;
	JTableHeader tableHeader;
	MyCheckBox myCheckBox = null;
	
	public LiftSeletorCheckBoxCellRenderer(JTable table) {
		// TODO Auto-generated constructor stub
		this.tableModel = (LiftSeletorTableModel)table.getModel();
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
