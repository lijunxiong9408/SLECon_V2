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
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;

import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyCheckBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueTextField;
import slecon.component.iobar.IOBar;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.interfaces.ConvertException;
import slecon.setting.modules.CarCallOption.Point2OperationTableModel;
import base.cfg.FontFactory;
import logic.EventID;


class Point2OperationCheckBoxCellRenderer implements TableCellRenderer{
	Point2OperationTableModel tableModel;
	JTableHeader tableHeader;
	MyCheckBox myCheckBox = null;
	
	public Point2OperationCheckBoxCellRenderer(JTable table) {
		// TODO Auto-generated constructor stub
		this.tableModel = (Point2OperationTableModel)table.getModel();
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

/**
 * Setup -> Module -> Car Call Option.
 */
public class CarCallOption extends JPanel {
    private static final long serialVersionUID = 3805831273620218180L;
    
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
            label.setBackground( StartUI.BORDER_COLOR );
            return label;
        }
    };
    
    /**
     * Text resource.
     */
    public static final ResourceBundle  TEXT    = ToolBox.getResourceBundle( "setting.module.CarCallOption" );
    private boolean                     started = false;
    private SettingPanel<CarCallOption> settingPanel;
    private JLabel                      cpt_anti_nuisance_car_call_operation;
    private ValueCheckBox               ebd_enabled;
    private JLabel                      lbl_percentage;
    private ValueTextField              fmt_percentage;
    private JLabel                      lbl_car_call_count;
    private ValueTextField              fmt_car_call_count;

    /* ---------------------------------------------------------------------------- */
    private JLabel    					cpt_calls_in_opposite_direction_auto_clear;
    private ValueCheckBox 				ebd_enabled_0;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel    				    cpt_calls_in_double_click_auto_clear;
    private ValueCheckBox 			    ebd_enabled_1;

    /* ---------------------------------------------------------------------------- */
    private JLabel                      cpt_near_stop;
    private ValueCheckBox               ebd_near_stop;
    private JLabel                      cpt_direct;
    private MyComboBox				    cbo_direct;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel                      cpt_all_station_parking;
    private ValueCheckBox               ebd_all_station_parking;
    private IOBar  						all_statin_parking_switch;
    /* ---------------------------------------------------------------------------- */
    private JLabel                      cpt_point_to_operation;
    private ValueCheckBox               ebd_point_to_operation;
    private Point_to_Operation_Table	point2OperationTable;
    
    /* ---------------------------------------------------------------------------- */
    public CarCallOption () {
        initGUI();
    }

    public void setSettingPanel ( SettingPanel<CarCallOption> panel ) {
        this.settingPanel = panel;
    }

    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_anti_nuisance_car_call_operation = new JLabel();
        ebd_enabled                          = new ValueCheckBox();
        lbl_percentage                       = new JLabel();
        fmt_percentage                       = new ValueTextField();
        lbl_car_call_count                   = new JLabel();
        fmt_car_call_count                   = new ValueTextField();
        setCaptionStyle( cpt_anti_nuisance_car_call_operation );

        // @CompoentSetting( ebd_enabled )

        // @CompoentSetting<Fmt>( lbl_percentage , fmt_percentage )
        setTextLabelStyle( lbl_percentage );
        fmt_percentage.setColumns( 10 );
        fmt_percentage.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_percentage.setScope( Long.class, 0L, null, false, false );
        fmt_percentage.setEmptyValue( 1L );

        // @CompoentSetting<Fmt>( lbl_car_call_count , fmt_car_call_count )
        setTextLabelStyle( lbl_car_call_count );
        fmt_car_call_count.setColumns( 10 );
        fmt_car_call_count.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_car_call_count.setScope( Long.class, 0L, null, false, false );
        fmt_car_call_count.setEmptyValue( 1L );
        add( cpt_anti_nuisance_car_call_operation, "gapbottom 18-12, span, top" );
        add( ebd_enabled, "skip 1, span, top" );
        Box vbox_title = Box.createVerticalBox();
        vbox_title.add( lbl_percentage);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_car_call_count);
        
        Box vbox_value = Box.createVerticalBox();
        vbox_value.add( fmt_percentage );
        vbox_value.add( Box.createVerticalStrut(10));
        vbox_value.add( fmt_car_call_count );
        add(vbox_title, "skip 2, span 1, left, top");
        add(vbox_value, "span 1, left, wrap 30, top");


        /* ---------------------------------------------------------------------------- */
        cpt_calls_in_opposite_direction_auto_clear = new JLabel();
        ebd_enabled_0                              = new ValueCheckBox();
        setCaptionStyle( cpt_calls_in_opposite_direction_auto_clear );

        // @CompoentSetting( ebd_enabled_0 )
        add( cpt_calls_in_opposite_direction_auto_clear, "gapbottom 18-12, span, top" );
        add( ebd_enabled_0, "skip 1, span, top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_calls_in_double_click_auto_clear = new JLabel();
        ebd_enabled_1                              = new ValueCheckBox();
        setCaptionStyle( cpt_calls_in_double_click_auto_clear );

        // @CompoentSetting( ebd_enabled_0 )
        add( cpt_calls_in_double_click_auto_clear, "gapbottom 18-12, span, top" );
        add( ebd_enabled_1, "skip 1, span, top" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_near_stop = new JLabel();
        ebd_near_stop = new ValueCheckBox();
        cpt_direct = new JLabel();
		cbo_direct = new MyComboBox();
		cbo_direct.setEnabled(false);
		setTextLabelStyle(cpt_direct);
		cbo_direct.setModel(new DefaultComboBoxModel<DirectType>(DirectType.values()));
        setComboBoxValueStyle(cbo_direct);
        if(cbo_direct.getItemCount()>0) cbo_direct.setSelectedIndex(0);
		
		setCaptionStyle( cpt_near_stop );
		
		add( cpt_near_stop, "gapbottom 18-12, span, top" );
        add( ebd_near_stop, "skip 1, span, top" );
        add( cpt_direct,"skip 2, span 1, left, top" );
		add( cbo_direct, "span 1, left, wrap, top");
		
        /* ---------------------------------------------------------------------------- */
		cpt_all_station_parking = new JLabel();
		ebd_all_station_parking = new ValueCheckBox();

        JLabel lbl_io_all_station_parking_switch = new JLabel();
        all_statin_parking_switch = new IOBar( false );
        setTextLabelStyle( lbl_io_all_station_parking_switch );
        lbl_io_all_station_parking_switch.setText( EventID.getString( EventID.ALL_STATION_PARKING_SWITCH.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), all_statin_parking_switch, EventID.ALL_STATION_PARKING_SWITCH.eventID );
		
        setCaptionStyle( cpt_all_station_parking );
		
		add( cpt_all_station_parking,"gapbottom 18-12, span, top" );
		add( ebd_all_station_parking, "skip 1, span, top");
		add( lbl_io_all_station_parking_switch, "skip 2, span, left, top" );
		add( all_statin_parking_switch, "skip 2, left, wrap, top");
		
		/* ---------------------------------------------------------------------------- */
		cpt_point_to_operation = new JLabel();
		ebd_point_to_operation = new ValueCheckBox();
		point2OperationTable = new Point_to_Operation_Table();
		
		setCaptionStyle( cpt_point_to_operation );
		
		add( cpt_point_to_operation,"gapbottom 18-12, span, top" );
		add( ebd_point_to_operation, "skip 1, span, top");
		
		JPanel panel = new JPanel( new BorderLayout() );
        panel.setBorder( BorderFactory.createLineBorder( StartUI.BORDER_COLOR ) );
        panel.add( point2OperationTable.getTableHeader(), BorderLayout.NORTH );
        panel.add( point2OperationTable);
        add( panel, "skip 2, span, top, wrap" );
        
		/* ---------------------------------------------------------------------------- */
        bindGroup( "anti_nuisance_car_call_operation_enabled", ebd_enabled );
        bindGroup( "percentage", lbl_percentage, fmt_percentage );
        bindGroup( "car_call_count", lbl_car_call_count, fmt_car_call_count );
        bindGroup( "calls_in_opposite_direction_auto_clear_enabled", ebd_enabled_0 );
        bindGroup( "calls_in_double_click_auto_clear", ebd_enabled_1 );
        bindGroup( new AbstractButton[]{ ebd_enabled }, lbl_percentage, fmt_percentage, lbl_car_call_count, fmt_car_call_count );
        bindGroup( new AbstractButton[]{ ebd_all_station_parking }, lbl_io_all_station_parking_switch, all_statin_parking_switch);
        bindGroup( new AbstractButton[]{ ebd_point_to_operation }, point2OperationTable);
        bindGroup("cpt_near_stop",cpt_near_stop);
        bindGroup("cpt_all_station_parking",cpt_all_station_parking, ebd_all_station_parking);
        bindGroup("cpt_point_to_operation",cpt_point_to_operation, ebd_point_to_operation);
        
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_anti_nuisance_car_call_operation.setText( TEXT.getString( "anti_nuisance_car_call_operation" ) );
        ebd_enabled.setText( TEXT.getString( "anti_nuisance_car_call_operation_enabled" ) );
        lbl_percentage.setText( TEXT.getString( "percentage" ) );
        lbl_car_call_count.setText( TEXT.getString( "car_call_count" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_calls_in_opposite_direction_auto_clear.setText( TEXT.getString( "calls_in_opposite_direction_auto_clear" ) );
        ebd_enabled_0.setText( TEXT.getString( "calls_in_opposite_direction_auto_clear_enabled" ) );
        
        /* ---------------------------------------------------------------------------- */
        cpt_calls_in_double_click_auto_clear.setText( TEXT.getString( "calls_in_double_click_auto_clear" ) );
        ebd_enabled_1.setText( TEXT.getString( "calls_in_double_click_auto_clear_enable" ) );
        
        /* ---------------------------------------------------------------------------- */
        cpt_near_stop.setText(TEXT.getString( "cpt_near_stop" ));
        ebd_near_stop.setText(TEXT.getString( "ebd_near_stop" ));
        cpt_direct.setText(TEXT.getString( "cpt_direct" ));
        
        /* ---------------------------------------------------------------------------- */
        cpt_all_station_parking.setText(TEXT.getString( "cpt_all_station_parking" ));
        ebd_all_station_parking.setText(TEXT.getString( "ebd_all_station_parking" ));
        
        /* ---------------------------------------------------------------------------- */
        cpt_point_to_operation.setText(TEXT.getString( "cpt_point_to_operation" ));
        ebd_point_to_operation.setText(TEXT.getString( "ebd_point_to_operation" ));
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setTextLabelStyle ( JLabel c ) {
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


    public AntiNuisanceCarCallOperationBean getAntiNuisanceCarCallOperationBean () throws ConvertException {
        if ( ! fmt_percentage.checkValue() )
            throw new ConvertException();
        if ( ! fmt_car_call_count.checkValue() )
            throw new ConvertException();

        AntiNuisanceCarCallOperationBean bean_antiNuisanceCarCallOperation = new AntiNuisanceCarCallOperationBean();
        bean_antiNuisanceCarCallOperation.setEnabled( ebd_enabled.isSelected() );
        bean_antiNuisanceCarCallOperation.setPercentage( ( Long )fmt_percentage.getValue() );
        bean_antiNuisanceCarCallOperation.setCarCallCount( ( Long )fmt_car_call_count.getValue() );
        return bean_antiNuisanceCarCallOperation;
    }


    public CallsInOppositeDirectionAutoClearBean getCallsInOppositeDirectionAutoClearBean () throws ConvertException {
        CallsInOppositeDirectionAutoClearBean bean_callsInOppositeDirectionAutoClear = new CallsInOppositeDirectionAutoClearBean();
        bean_callsInOppositeDirectionAutoClear.setEnabled0( ebd_enabled_0.isSelected() );
        return bean_callsInOppositeDirectionAutoClear;
    }
    
    public CallsInDoubleClickClearBean getCallsInDoubleClickAutoClearBean () throws ConvertException {
    	CallsInDoubleClickClearBean bean_callsInDoubleClickAutoClear = new CallsInDoubleClickClearBean();
    	bean_callsInDoubleClickAutoClear.setEnabled1( ebd_enabled_1.isSelected());
        return bean_callsInDoubleClickAutoClear;
    }


    public void setAntiNuisanceCarCallOperationBean ( AntiNuisanceCarCallOperationBean bean_antiNuisanceCarCallOperation ) {
        this.ebd_enabled.setOriginSelected( bean_antiNuisanceCarCallOperation.getEnabled() != null
                                      && bean_antiNuisanceCarCallOperation.getEnabled() == true );
        this.fmt_percentage.setOriginValue( bean_antiNuisanceCarCallOperation.getPercentage() );
        this.fmt_car_call_count.setOriginValue( bean_antiNuisanceCarCallOperation.getCarCallCount() );
    }


    public void setCallsInOppositeDirectionAutoClearBean ( CallsInOppositeDirectionAutoClearBean bean_callsInOppositeDirectionAutoClear ) {
        this.ebd_enabled_0.setOriginSelected( bean_callsInOppositeDirectionAutoClear.getEnabled0() != null
                                        && bean_callsInOppositeDirectionAutoClear.getEnabled0() == true );
    }
    
    public void setCallsInDoubleClickAutoClearBean ( CallsInDoubleClickClearBean bean_callsInDoubleClickAutoClear ) {
        this.ebd_enabled_1.setOriginSelected( bean_callsInDoubleClickAutoClear.getEnabled1() != null
                                        && bean_callsInDoubleClickAutoClear.getEnabled1() == true );
    }

    public void setNearStopBean( NearStopBean bean ) {
    	this.ebd_near_stop.setOriginSelected(bean.isEnable());
    	this.cbo_direct.setSelectedIndex(bean.getDirect());
    }
    
    public NearStopBean getNearStopBean() {
    	NearStopBean bean = new NearStopBean();
    	bean.setEnable(this.ebd_near_stop.isSelected());
    	bean.setDirect(this.cbo_direct.getSelectedIndex());
    	return bean;
    }
    
    public void setAspBean( ASPBEAN bean ) {
    	this.ebd_all_station_parking.setOriginSelected(bean.isEnable());
    	this.all_statin_parking_switch.setEvent(bean.getAspEvent());
    }
    
    public ASPBEAN getASPBean() {
    	ASPBEAN bean = new ASPBEAN();
    	bean.setEnable(this.ebd_all_station_parking.isSelected());
    	bean.setAspEvent(this.all_statin_parking_switch.getEvent());
    	return bean;
    }
    
    public Point2OperationBean getP2oBean() {
    	Point2OperationBean p2o = new Point2OperationBean();
    	p2o.setEnabled(this.ebd_point_to_operation.isSelected());
    	p2o.setDoorEnableAction(this.point2OperationTable.getData());
    	return p2o;
    }
    
    public void setP2oBean(Point2OperationBean p2oBean) {
    	this.ebd_point_to_operation.setSelected(p2oBean.getEnabled());
    	this.point2OperationTable.refreshData(p2oBean.clone());
    }
    
    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static CarCallOption createPanel ( SettingPanel<CarCallOption> panel ) {
        CarCallOption gui = new CarCallOption();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class AntiNuisanceCarCallOperationBean {
        private Boolean enabled;
        private Long    percentage;
        private Long    carCallCount;

        public Boolean getEnabled () {
            return this.enabled;
        }


        public Long getPercentage () {
            return this.percentage;
        }


        public Long getCarCallCount () {
            return this.carCallCount;
        }


        public void setEnabled ( Boolean enabled ) {
            this.enabled = enabled;
        }


        public void setPercentage ( Long percentage ) {
            this.percentage = percentage;
        }


        public void setCarCallCount ( Long carCallCount ) {
            this.carCallCount = carCallCount;
        }
    }




    public static class CallsInOppositeDirectionAutoClearBean {
        private Boolean enabled0;




        public Boolean getEnabled0 () {
            return this.enabled0;
        }


        public void setEnabled0 ( Boolean enabled0 ) {
            this.enabled0 = enabled0;
        }
    }
    
    public static class CallsInDoubleClickClearBean {
        private Boolean enabled1;

        public Boolean getEnabled1 () {
            return this.enabled1;
        }


        public void setEnabled1 ( Boolean enabled1 ) {
            this.enabled1 = enabled1;
        }
    }
    
    public static String getBundleText ( String key, String defaultValue ) {
        String result;
        try {
            result = TEXT.getString( key );
        } catch ( Exception e ) {
            result = defaultValue;
        }
        return result;
    }
    
    public enum DirectType {
        up,
        down;
        
        public String toString() {
            return getBundleText("LBL_"+name(),name());
        }
    } 
    
    public static class NearStopBean{
    	private boolean enable;
    	private int direct;
    	
		public boolean isEnable() {
			return enable;
		}
		public void setEnable(boolean enable) {
			this.enable = enable;
		}
		public int getDirect() {
			return direct;
		}
		public void setDirect(int direct) {
			this.direct = direct;
		}
    }
    
    public static class ASPBEAN{
    	private boolean enable;
    	private Event	aspEvent;

		public Event getAspEvent() {
			return aspEvent;
		}

		public void setAspEvent(Event aspEvent) {
			this.aspEvent = aspEvent;
		}

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}
    }
    
	public static class Point2OperationBean {
	    private Boolean   enabled;
	    int floorcount = 128;
		String[] FloorText;
		byte[] DoorEnableAction;
	
	    public Boolean getEnabled () {
	        return this.enabled;
	    }
	    
		public int getFloorcount() {
			return floorcount;
		}
	
		public String[] getFloorText() {
			return FloorText;
		}
	
		public byte[] getDoorEnableAction() {
			return DoorEnableAction;
		}
	
		public void setEnabled ( Boolean enabled ) {
	        this.enabled = enabled;
	    }
		
		public void setFloorcount(int floorcount) {
			this.floorcount = floorcount;
		}
	
		public void setFloorText(String[] floorText) {
			FloorText = floorText;
		}
	
		public void setDoorEnableAction(byte[] doorEnableAction) {
			DoorEnableAction = doorEnableAction;
		}
		
		@Override
		protected Point2OperationBean clone() {
			// TODO Auto-generated method stub
			Point2OperationBean bean = new Point2OperationBean();
			bean.setEnabled(enabled);
			bean.setFloorcount(floorcount);
			bean.setFloorText(Arrays.copyOf(FloorText, FloorText.length));
			bean.setDoorEnableAction(Arrays.copyOf(DoorEnableAction, DoorEnableAction.length));
			return bean;
		}
	}
	
    public static class Point2OperationTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[] title	= {	TEXT.getString( "LBL_floor" ),TEXT.getString("LBL_floor_text"),TEXT.getString("LBL_run")};
		private int floorCount;
		private Point2OperationBean bean;
		
		public void ChangeData(Point2OperationBean bean) {
			this.floorCount = bean.getFloorcount();
			this.bean = bean;
		}
		
        @Override
        public int getRowCount () {
            return floorCount;
        }

        @Override
        public int getColumnCount () {
            return 3;
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
                }else {
                    return ( bean.getDoorEnableAction()[ rowIndex ] & 0x01 ) != 0;
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
	            if ( col == 2 ) {
	                if ( aValue == Boolean.TRUE ) {
	                	bean.getDoorEnableAction()[ row ] = ( byte )0x01;
	                }else {
	                	bean.getDoorEnableAction()[ row ] = ( byte )0x00;	
	                }
	            }
        	}catch ( Exception e ) {
            }
        	fireTableCellUpdated( row, col );
        }
    }
    
    public final static class Point_to_Operation_Table extends JTable {
		private static final long serialVersionUID = 3565969239581890690L;
		private Point2OperationTableModel model;
		
		public Point_to_Operation_Table() {
			super();
			model = new Point2OperationTableModel();
			setModel(model);
			setRowSelectionAllowed( true );
            setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
            setGridColor(StartUI.SUB_BACKGROUND_COLOR);
            setShowGrid(true);
            setRowHeight(25);
            
            getColumnModel().getColumn( 0 ).setCellRenderer( normalRenderer );
            getColumnModel().getColumn( 0 ).setPreferredWidth(80);
            getColumnModel().getColumn( 1 ).setCellRenderer( normalRenderer );
            getColumnModel().getColumn( 1 ).setPreferredWidth(80);
            getColumnModel().getColumn( 2 ).setPreferredWidth(100);
            getColumnModel().getColumn( 2 ).setCellRenderer(new Point2OperationCheckBoxCellRenderer(this));
            for(int i = 0; i < this.getColumnCount(); i++) {
            	getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
            	
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
		  
        public void refreshData (Point2OperationBean bean) {
        	model.ChangeData(bean);
        	model.fireTableDataChanged();
        }
        
        public byte[] getData() {
        	return model.bean.getDoorEnableAction();
        }
        
    }
}
