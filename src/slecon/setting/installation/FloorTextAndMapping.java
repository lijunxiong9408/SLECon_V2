package slecon.setting.installation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
import slecon.component.SettingPanel;
import slecon.component.ValueTextField;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;

public class FloorTextAndMapping extends JPanel {
    private static final long        serialVersionUID = 1804562951275001152L;

    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.installation.FloorTextAndMapping");

    private static TableCellRenderer stringRenderer   = new DefaultTableCellRenderer() {
        private static final long serialVersionUID = -5345000415830605441L;
        public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                         int column ) {
            JLabel label = ( JLabel )super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            label.setHorizontalAlignment( JLabel.RIGHT );
            label.setHorizontalTextPosition( JLabel.RIGHT );
            label.setForeground(Color.WHITE);
            
            if (!isSelected) {
                if (row % 2 == 0) {
                    setBackground(StartUI.SUB_BACKGROUND_COLOR);
                } else {
                	setBackground(StartUI.MAIN_BACKGROUND_COLOR);
                }
            }else
            	setBackground(StartUI.BORDER_COLOR);
            return label;
        }
    };
    
    private static void setTableHeaderColor(JTable table, int columnIndex, Color c) {
        TableColumn column = table.getTableHeader().getColumnModel()
                .getColumn(columnIndex);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(c);
        cellRenderer.setFont(FontFactory.FONT_12_BOLD);
        cellRenderer.setHorizontalTextPosition(JLabel.RIGHT);
        cellRenderer.setHorizontalAlignment(JLabel.RIGHT);
        cellRenderer.setForeground(Color.WHITE);
        column.setHeaderRenderer(cellRenderer);
    }

    private boolean                           started = false;
    private long							  lastFloorCount = 0;
    private SettingPanel<FloorTextAndMapping> settingPanel;
    private JLabel                            cpt_general;
    private JLabel                            lbl_floor_count;
    private ValueTextField                    fmt_floor_count;

    /* ---------------------------------------------------------------------------- */
    private JLabel cpt_floor_setting;

    /* ---------------------------------------------------------------------------- */
    private FloorSettingTable tbl_floor_setting;




    public FloorTextAndMapping () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<FloorTextAndMapping> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[40::40][20::20][32::32][]" ) );
        cpt_general       = new JLabel();
        lbl_floor_count   = new JLabel();
        fmt_floor_count   = new ValueTextField();
        cpt_floor_setting = new JLabel();
        tbl_floor_setting = new FloorSettingTable();
        for(int i = 0; i < tbl_floor_setting.getColumnCount(); i++) {
        	setTableHeaderColor(tbl_floor_setting, i, StartUI.MAIN_BACKGROUND_COLOR);
        }
        JTableHeader head =  tbl_floor_setting.getTableHeader();
        head.setPreferredSize(new Dimension(1, 30));
        
        final Fmt_floor_countDocumentListener listener = new Fmt_floor_countDocumentListener();
        fmt_floor_count.getDocument().addDocumentListener( listener );
        fmt_floor_count.addActionListener( listener );
        setCaptionStyle( cpt_general );

        // @CompoentSetting<Fmt>( lbl_floor_count , fmt_floor_count )
        setTextLabelStyle( lbl_floor_count );
        fmt_floor_count.setColumns( 10 );
        fmt_floor_count.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_floor_count.setScope( Long.class, 0L, 127L, false, true );
        try {
        fmt_floor_count.setEmptyValue( 10L );
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        add( cpt_general, "gapbottom 18-12, span, aligny center, top" );
        add( lbl_floor_count, "skip 2, span, split, gapright 12, top" );
        add( fmt_floor_count, "wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        setCaptionStyle( cpt_floor_setting );

        JPanel panel_of_tbl_floor_setting = new JPanel( new BorderLayout() );
        panel_of_tbl_floor_setting.setBorder( BorderFactory.createLineBorder( StartUI.BORDER_COLOR ) );
        panel_of_tbl_floor_setting.add( tbl_floor_setting.getTableHeader(), BorderLayout.NORTH );
        panel_of_tbl_floor_setting.add( tbl_floor_setting );
        add( cpt_floor_setting, "gapbottom 18-12, span, aligny center, top" );
        add( panel_of_tbl_floor_setting, "skip 2, span, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "FloorCount", lbl_floor_count, fmt_floor_count );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( getBundleText( "LBL_cpt_general", "General" ) );
        lbl_floor_count.setText( getBundleText( "LBL_lbl_floor_count", "Floor Count" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_floor_setting.setText( getBundleText( "LBL_cpt_floor_setting", "Floor Setting" ) );

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


    private void bindGroup ( final String detailKey, final JComponent... list ) {
        if ( detailKey != null && detailKey.trim().length() > 0 ) {
            for ( JComponent c : list ) {
                c.addMouseListener( new MouseAdapter() {
                    String detailText;
                    @Override
                    public synchronized void mouseEntered ( MouseEvent evt ) {
                        if ( settingPanel != null ) {
                            if ( detailText == null ) {
                                try {
                                    detailText = TEXT.getString( "Description_" + detailKey );
                                } catch ( Exception e ) {
                                    detailText = "No description here! Be careful.";
                                }
                            }
                            settingPanel.setDescription( detailText );
                        }
                    }
                    @Override
                    public void mouseExited ( MouseEvent e ) {
                        if ( settingPanel != null ) {
                            settingPanel.setDescription( null );
                        }
                    }
                } );
            }
        }
    }


    public GeneralBean getGeneralBean () throws ConvertException {
        if ( ! fmt_floor_count.checkValue() )
            throw new ConvertException();

        GeneralBean bean_general = new GeneralBean();
        bean_general.setFloorCount( ( Long )fmt_floor_count.getValue() );
        return bean_general;
    }


    public FloorSettingBean getFloorSettingBean () throws ConvertException {
        TableCellEditor editor = tbl_floor_setting.getCellEditor();
        if ( editor != null )
            editor.stopCellEditing();

        FloorSettingBean bean_floorSetting = new FloorSettingBean();
        bean_floorSetting.setFloorText( tbl_floor_setting.getData().floorText );
        bean_floorSetting.setDoorzone( tbl_floor_setting.getData().doorzone );
        return bean_floorSetting;
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
    	lastFloorCount = bean_general.getFloorCount();
        this.fmt_floor_count.setOriginValue( lastFloorCount );
        doUpdateFloorCount();
    }


    public void setFloorSettingBean ( FloorSettingBean bean_floorSetting ) {
        int floorCount = 0;
        try {
            if ( fmt_floor_count.getValue() instanceof Number )
                floorCount = ( ( Number )fmt_floor_count.getValue() ).intValue();
        } catch ( NullPointerException e ) {
        }
        tbl_floor_setting.setData( floorCount, bean_floorSetting );
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


    public static FloorTextAndMapping createPanel ( SettingPanel<FloorTextAndMapping> panel ) {
        FloorTextAndMapping gui = new FloorTextAndMapping();
        gui.setSettingPanel( panel );
        return gui;
    }


    protected void doUpdateFloorCount() {
        GeneralBean bean;
        if (tbl_floor_setting != null)
            try {
                bean = getGeneralBean();
                if(tbl_floor_setting.getData() != null) {
                	FloorSettingBean floorSettingBean = tbl_floor_setting.getData().clone();
                	int floorCount = bean.getFloorCount().intValue();
                	if( lastFloorCount < floorCount) {
                		Integer[] doorzone = new Integer[128];
                		String[] floortext = new String[128];
                		System.arraycopy(floorSettingBean.getDoorzone(), 0, doorzone, 0, (int)lastFloorCount);
                		System.arraycopy(floorSettingBean.getFloorText(), 0, floortext, 0, (int)lastFloorCount);
                		for(int i = (int)lastFloorCount; i < floorCount; i++) {
                			doorzone[i] = doorzone[i - 1] + 1;
                			floortext[i] = Integer.parseInt(floortext[i - 1]) + 1 + "";
                		}
                		floorSettingBean.setDoorzone(doorzone);
                		floorSettingBean.setFloorText(floortext);
                		tbl_floor_setting.setData(bean.getFloorCount().intValue(), floorSettingBean);
                		return;
                	}
                }
                tbl_floor_setting.setData(bean.getFloorCount().intValue(), tbl_floor_setting.getData());
            } catch (ConvertException e1) {
            }
    }


    public static class FloorSettingBean {
        private String  floorText[];
        private Integer doorzone[];




        public String[] getFloorText () {
            return floorText;
        }


        public Integer[] getDoorzone () {
            return doorzone;
        }


        public void setFloorText ( String[] floorText ) {
            this.floorText = floorText;
        }


        public void setDoorzone ( Integer[] doorzone ) {
            this.doorzone = doorzone;
        }
        
        @Override
        public FloorSettingBean clone() {
            FloorSettingBean result = new FloorSettingBean();
            result.setDoorzone(Arrays.copyOf(doorzone, doorzone.length));
            result.setFloorText(Arrays.copyOf(floorText, floorText.length));
            return result;
        }
    }




    public final static class FloorSettingTable extends JTable {
        private static final long      serialVersionUID = -5892435662963090557L;
        private FloorSettingTableModel model;




        public FloorSettingTable () {
            super();
            model = new FloorSettingTableModel();
            setModel( model );
            setRowSelectionAllowed( true );
            setGridColor(StartUI.SUB_BACKGROUND_COLOR);
            setShowGrid(true);
            setRowHeight(20);
            setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
            getColumnModel().getColumn( 0 ).setCellRenderer( stringRenderer );
            getColumnModel().getColumn( 0 ).setPreferredWidth(50);
            getColumnModel().getColumn( 1 ).setCellRenderer( stringRenderer );
            getColumnModel().getColumn( 1 ).setPreferredWidth(150);
            getColumnModel().getColumn( 2 ).setCellRenderer( stringRenderer );
            getColumnModel().getColumn( 2 ).setPreferredWidth(150);
        }


        @Override
        public Component prepareEditor ( TableCellEditor editor, int row, int column ) {
            Component c = super.prepareEditor( editor, row, column );
            if ( c instanceof JTextComponent ) {
                ( ( JTextComponent )c ).selectAll();
            }
            return c;
        }


        public void setData ( int floorCount, FloorSettingBean bean ) {
            model.setData( floorCount, bean==null ? null : bean.clone() );
        }


        public FloorSettingBean getData () {
            return model.bean;
        }
    }




    public static class FloorSettingTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 664165366051951478L;
        private FloorSettingBean  bean;
        private int               floorCount;

        public void setData ( int floorCount, FloorSettingBean bean ) {
            this.floorCount = floorCount;
            this.bean       = bean;
            fireTableDataChanged();
        }


        @Override
        public boolean isCellEditable ( int row, int col ) {
            if ( col != 0 )
                return true;
            return false;
        }


        @Override
        public int getRowCount () {
            return floorCount;
        }


        @SuppressWarnings( { "rawtypes", "unchecked" } )
        @Override
        public Class getColumnClass ( int col ) {
            if ( col == 0 )
                return Integer.class;
            if ( col == 1 )
                return String.class;
            if ( col == 2 )
                return Byte.class;
            return Object.class;
        }


        public String getColumnName ( int col ) {
            return TEXT.getString("Column"+col);
        }


        @Override
        public int getColumnCount () {
            return 3;
        }


        @Override
        public Object getValueAt ( int rowIndex, int columnIndex ) {
            try {
                if ( bean != null ) {
                    if ( columnIndex == 0 )
                        return rowIndex;
                    if ( columnIndex == 1 ) {
                        return bean.getFloorText()[ rowIndex ];
                    }
                    if ( columnIndex == 2 )
                        return bean.getDoorzone()[ rowIndex ];
                }
            } catch ( ArrayIndexOutOfBoundsException e ) {
            }
            return null;
        }


        @Override
        public void setValueAt ( Object aValue, int rowIndex, int columnIndex ) {
            try {
                if ( bean != null ) {
                    if ( columnIndex == 1 && aValue instanceof String ) {
                        bean.getFloorText()[ rowIndex ] = ( String )aValue;
                    }
                    if ( columnIndex == 2 && aValue instanceof Number )
                        bean.getDoorzone()[ rowIndex ] = ( ( Number )aValue ).intValue();
                }
            } catch ( ArrayIndexOutOfBoundsException e ) {
            }
            fireTableCellUpdated( rowIndex, columnIndex );
        }
    }




    public class Fmt_floor_countDocumentListener implements ActionListener, DocumentListener {
        @Override
        public void insertUpdate ( DocumentEvent e ) {
            doUpdateFloorCount();
        }


        @Override
        public void removeUpdate ( DocumentEvent e ) {
            doUpdateFloorCount();
        }


        @Override
        public void changedUpdate ( DocumentEvent e ) {
            doUpdateFloorCount();
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            doUpdateFloorCount();
        }
    }




    public static class GeneralBean {
        private Long floorCount;




        public Long getFloorCount () {
            return this.floorCount;
        }


        public void setFloorCount ( Long floorCount ) {
            this.floorCount = floorCount;
        }
    }




}
