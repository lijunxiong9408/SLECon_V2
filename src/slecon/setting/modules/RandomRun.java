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
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyCheckBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueTextField;
import slecon.home.PosButton;
import slecon.interfaces.ConvertException;
import slecon.setting.modules.RandomRun.RandomRunTableModel;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;


class RandomCheckBoxCellRenderer implements TableCellRenderer{
	RandomRunTableModel tableModel;
	JTableHeader tableHeader;
	MyCheckBox myCheckBox = null;
	
	public RandomCheckBoxCellRenderer(JTable table) {
		// TODO Auto-generated constructor stub
		this.tableModel = (RandomRunTableModel)table.getModel();
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

public class RandomRun extends JPanel implements ActionListener{
    private static final long serialVersionUID = 4412644778595410536L;
    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT    = ToolBox.getResourceBundle( "setting.module.RandomRun" );
    private boolean                     started = false;
    private SettingPanel<RandomRun>     settingPanel;
    
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

    /* ---------------------------------------------------------------------------- */
    private JLabel                     cpt_general;
    private ValueCheckBox              ebd_enabled;
    private JLabel                     lbl_interval_time;
    private ValueTextField			   fmt_interval_time;
    private JLabel                     lbl_run_times;
    private ValueTextField			   fmt_run_times;
    private Random_Run_Table		   random_run_table;
    
    /*----------------------------------------------------------------------------*/
    private JLabel 					   cpt_Voice_Debug;
    private ValueCheckBox 			   ebd_Voice_Debug;
    private JLabel					   lbl_CanID;
    private ValueTextField			   txt_CanID;
    private MyComboBox				   cbo_CanType;
    private JLabel 					   lbl_Voice;
    private MyComboBox				   cbo_VoiceType;
    private PosButton 				   btn_Voice_Debug;
    
    public RandomRun () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<RandomRun> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][50::50][100::100][100::100][]" ) );
        /* ---------------------------------------------------------------------------- */
        cpt_general      = new JLabel();
        ebd_enabled      = new ValueCheckBox();
        lbl_interval_time = new JLabel();
        fmt_interval_time = new ValueTextField();
        lbl_run_times = new JLabel();
        fmt_run_times = new ValueTextField();
        random_run_table = new Random_Run_Table();
        
        setCaptionStyle( cpt_general );
        setTextLabelStyle(lbl_interval_time);
        fmt_interval_time.setColumns( 10 );
        fmt_interval_time.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_interval_time.setScope( Long.class, 0L, null, false, false );
        fmt_interval_time.setEmptyValue( 0L );
        setTextLabelStyle(lbl_run_times);
        fmt_run_times.setColumns( 10 );
        fmt_run_times.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_run_times.setScope( Long.class, 0L, null, true, false );
        fmt_run_times.setEmptyValue( 0L );
        
        // @CompoentSetting( ebd_enabled )
        add( cpt_general, "gapbottom 18-12, span, aligny center, top" );
        add( ebd_enabled, "skip 1, span, top" );
        Box vbox_title = Box.createVerticalBox();
        vbox_title.add( lbl_interval_time);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_run_times);
        
        Box vbox_value = Box.createVerticalBox();
        vbox_value.add( fmt_interval_time );
        vbox_value.add( Box.createVerticalStrut(10));
        vbox_value.add( fmt_run_times );
        add(vbox_title, "skip 2, span 1, left, top, gapright 50");
        add(vbox_value, "skip 1, span 1, left, wrap 30, top");
        
        JPanel panel = new JPanel( new BorderLayout() );
        panel.setBorder( BorderFactory.createLineBorder( StartUI.BORDER_COLOR ) );
        panel.add( random_run_table.getTableHeader(), BorderLayout.NORTH );
        panel.add( random_run_table);
        add( panel, "skip 2, span, top" );

        /*----------------------------------------------------------------------------*/
        cpt_Voice_Debug = new JLabel();
        ebd_Voice_Debug = new ValueCheckBox();
        lbl_CanID = new JLabel();
        txt_CanID = new ValueTextField();
        cbo_CanType = new MyComboBox();	
        lbl_Voice = new JLabel();
        cbo_VoiceType = new MyComboBox();
        btn_Voice_Debug = new PosButton(ImageFactory.BUTTON_PAUSE.icon(80, 25),
        								ImageFactory.BUTTON_START.icon(80, 25));
        btn_Voice_Debug.addActionListener( this );
        
        setCaptionStyle( cpt_Voice_Debug );
        setTextLabelStyle(lbl_CanID);
        
        setTextValueStyle( txt_CanID );

        txt_CanID.setColumns( 8 );
        txt_CanID.setHorizontalAlignment( SwingConstants.RIGHT );
        txt_CanID.setScope( Integer.class, 0, 250, false, true );
        txt_CanID.setEmptyValue( 0x8 );
        
        cbo_CanType.setModel(new DefaultComboBoxModel<CanType>(CanType.values()));
        setComboBoxValueStyle(cbo_CanType);
        if(cbo_CanType.getItemCount()>0) cbo_CanType.setSelectedIndex(0);
        cbo_CanType.setPreferredSize(new Dimension(80, 25));
        
        setTextLabelStyle(lbl_Voice);
        
        cbo_VoiceType.setModel(new DefaultComboBoxModel<VoiceType>(VoiceType.values()));
        setComboBoxValueStyle(cbo_VoiceType);
        if(cbo_VoiceType.getItemCount()>0) cbo_VoiceType.setSelectedIndex(0);
        cbo_VoiceType.setPreferredSize(new Dimension(80, 25));
        
        setButtonStyle(btn_Voice_Debug);
        
        add(cpt_Voice_Debug,"gapbottom 18-12, span, top");
        add(ebd_Voice_Debug,"skip 1, span");
        add(lbl_CanID,"skip 2, span 1, left, top, gapright 30");
        add(txt_CanID,"span 1, left, top, gapright 20");
        add(cbo_CanType,"span 1, left, top, wrap");
        add(lbl_Voice,"skip 2, span 1, left, top, gapright 30");
        add(cbo_VoiceType,"span 1, left, top, gapright 20");
        add(btn_Voice_Debug,"span 1, left, top, wrap 30");
        
        bindGroup( "enabled", ebd_enabled );
        bindGroup("fmt_interval_time",fmt_interval_time);
        bindGroup("fmt_run_times",fmt_run_times);
        bindGroup( new AbstractButton[]{ ebd_enabled }, lbl_interval_time, fmt_interval_time,lbl_run_times,fmt_run_times,random_run_table);
        bindGroup( new AbstractButton[] { ebd_Voice_Debug }, lbl_CanID, txt_CanID, cbo_CanType, lbl_Voice,cbo_VoiceType,btn_Voice_Debug);
        
        loadI18N();
        revalidate();
    }

    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );
        lbl_interval_time.setText(TEXT.getString("interval_time"));
        lbl_run_times.setText(TEXT.getString("run_times"));
        
        /*----------------------------------------------------------------------------*/
        cpt_Voice_Debug.setText(TEXT.getString("LBL_cpt_Voice_Debug"));
        ebd_Voice_Debug.setText(TEXT.getString("LBL_open"));
        lbl_CanID.setText(TEXT.getString("LBL_CanID"));
        lbl_Voice.setText(TEXT.getString("LBL_Voice"));
        btn_Voice_Debug.setText(TEXT.getString("LBL_btn_Voice_Debug"));
        
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
    
    private void setTextValueStyle ( JTextField c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }

    private void setComboBoxValueStyle ( JComboBox<?> c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setButtonStyle ( JComponent c ) {
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
    
    public final static class Random_Run_Table extends JTable {
		private static final long serialVersionUID = 3565969239581890690L;
		private RandomRunTableModel model;
		
		public Random_Run_Table() {
			super();
			model = new RandomRunTableModel();
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
            getColumnModel().getColumn( 2 ).setCellRenderer(new RandomCheckBoxCellRenderer(this));
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
		  
        public void refreshData (GeneralBean bean) {
        	model.ChangeData(bean);
        	model.fireTableDataChanged();
        }
        
        public byte[] getData() {
        	return model.bean.getDoorEnableAction();
        }
        
    }

    public GeneralBean getGeneralBean () throws ConvertException {
        GeneralBean bean_general = new GeneralBean();
        bean_general.setEnabled( ebd_enabled.isSelected() );
        bean_general.setInterval_time((Long)fmt_interval_time.getValue());
        bean_general.setRun_times(Integer.parseInt(fmt_run_times.getValue().toString()));
        bean_general.setDoorEnableAction(random_run_table.getData());
        return bean_general;
    }

    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.fmt_interval_time.setOriginValue(bean_general.getInterval_time());
        this.fmt_run_times.setOriginValue(bean_general.getRun_times());
        this.random_run_table.refreshData(bean_general.clone());
    }
    
    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static RandomRun createPanel ( SettingPanel<RandomRun> panel ) {
        RandomRun gui = new RandomRun();
        gui.setSettingPanel( panel );
        return gui;
    }

    public static class RandomRunTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 409083449310946326L;
		private String[] title	= {	TEXT.getString( "LBL_floor" ),TEXT.getString("LBL_floor_text"),TEXT.getString("LBL_run")};
		private int floorCount;
		private GeneralBean bean;
		
		public void ChangeData(GeneralBean bean) {
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
    
    public static class GeneralBean {
        private Boolean   enabled;
        private Long   	  interval_time;
        private int 	  run_times;
        int floorcount = 128;
		String[] FloorText;
    	byte[] DoorEnableAction;

        public Boolean getEnabled () {
            return this.enabled;
        }
        
        public Long getInterval_time() {
			return interval_time;
		}

		public int getRun_times() {
			return run_times;
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
		
		public void setInterval_time(Long interval_time) {
			this.interval_time = interval_time;
		}
		
		public void setRun_times(int run_times) {
			this.run_times = run_times;
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
		protected GeneralBean clone() {
			// TODO Auto-generated method stub
			GeneralBean bean = new GeneralBean();
			bean.setEnabled(enabled);
			bean.setFloorcount(floorcount);
			bean.setFloorText(Arrays.copyOf(FloorText, FloorText.length));
			bean.setDoorEnableAction(Arrays.copyOf(DoorEnableAction, DoorEnableAction.length));
			return bean;
		}
    }

    
    protected void do_btn_Voice_Debug_actionPerformed ( final ActionEvent e ) {
        if ( settingPanel instanceof RandomRunSetting ) {
        	if(txt_CanID.getText().trim().equals("")) {
        		JOptionPane.showMessageDialog(StartUI.getFrame(), " Error : ID Is NULL ");
        		return;
        	}
        	CanVoiceBean bean = new CanVoiceBean();
        	bean.setID(Integer.parseInt(txt_CanID.getText()));
        	bean.setBUS(cbo_CanType.getSelectedIndex());
        	bean.setVoiceType(cbo_VoiceType.getSelectedIndex());
        	( ( RandomRunSetting )settingPanel ).sendCanData(bean);
        }
    }


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		 if( e.getSource() == btn_Voice_Debug ) {
	    	do_btn_Voice_Debug_actionPerformed(e);
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
	
	public enum CanType {
        CAR,
        HALL;
        
        public String toString() {
            return getBundleText("LBL_"+name(),name());
        }
    }
    
    public enum VoiceType {
        ARRIVAL,
        DIRECTION,
        DOORACTION,
        MESSAGE,
        BUTTON,
    	OTHER;
        
        public String toString() {
            return getBundleText("LBL_"+name(),name());
        }
    }
    
    public static class CanVoiceBean{
    	private int ID;
		private int BUS;
    	private int VoiceType;
    	
    	public int getID() {
			return ID;
		}
		public void setID(int iD) {
			ID = iD;
		}
		public int getBUS() {
			return BUS;
		}
		public void setBUS(int bUS) {
			BUS = bUS;
		}
		public int getVoiceType() {
			return VoiceType;
		}
		public void setVoiceType(int voiceType) {
			VoiceType = voiceType;
		}
    	
    }
    
}
