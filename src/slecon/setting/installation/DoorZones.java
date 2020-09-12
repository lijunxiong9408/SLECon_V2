package slecon.setting.installation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import logic.Dict;
import logic.util.PageTreeExpression;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.ValueTextField;
import slecon.home.PosButton;
import slecon.interfaces.ConvertException;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;

public class DoorZones extends JPanel implements ActionListener {
    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.installation.DoorZones");

    private final static PageTreeExpression WRITE_MCS_EXPRESSION = new PageTreeExpression("write_mcs");
    
    private static final long        serialVersionUID = -2830134753032728941L;
    
    /* ---------------------------------------------------------------------------- */
    private boolean                 started = false;
    private SettingPanel<DoorZones> settingPanel;
    private JLabel                  cpt_control;
    private JLabel                  lbl_label_dz_count;
    private JLabel                  lbl_value_dz_count;
    private PosButton 				btn_execute_self_learn_door_zone;

    /* ---------------------------------------------------------------------------- */
    private JLabel            cpt_shaft_positions;
    private JLabel            lbl_usl;
    private ValueTextField    fmt_usl;
    private JLabel            lbl_lsl;
    private ValueTextField    fmt_lsl;
    private JLabel            lbl_doorzones;
    private DefaultTableModel doorZoneModel;
    private JTable            table;

    public DoorZones () {
        try {
            initGUI();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void setSettingPanel ( SettingPanel<DoorZones> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_control        = new JLabel();
        lbl_label_dz_count = new JLabel();
        lbl_value_dz_count = new JLabel();
        setCaptionStyle( cpt_control );
        
        System.out.println("language : "+BaseFactory.getLocaleString());
        if(BaseFactory.getLocaleString().equals("en"))
        	btn_execute_self_learn_door_zone = new PosButton(ImageFactory.BUTTON_PAUSE.icon(180,30), 
        													 ImageFactory.BUTTON_START.icon(180,30));
        else
        	btn_execute_self_learn_door_zone = new PosButton(ImageFactory.BUTTON_PAUSE.icon(87,30),
        													 ImageFactory.BUTTON_START.icon(87,30));
        
        btn_execute_self_learn_door_zone.addActionListener( this );
        
        // @CompoentSetting( lbl_label_dz_count, lbl_value_dz_count )
        setLabelTitleStyle( lbl_label_dz_count );
        setLabelValueStyle( lbl_value_dz_count );
        add( cpt_control, "gapbottom 18-12, span, top" );
        add( lbl_label_dz_count, "skip 2, span, split, gapright 12, top" );
        add( lbl_value_dz_count, "wrap, top" );
        // @CompoentSetting( btn_execute_self_learn_door_zone )
        setButtonStyle( btn_execute_self_learn_door_zone );
        add( btn_execute_self_learn_door_zone, "skip 2, span, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_shaft_positions = new JLabel();
        lbl_usl             = new JLabel();
        fmt_usl             = new ValueTextField();
        lbl_lsl             = new JLabel();
        fmt_lsl             = new ValueTextField();
        lbl_doorzones       = new JLabel();
        setCaptionStyle( cpt_shaft_positions );

        // @CompoentSetting<Fmt>( lbl_usl , fmt_usl )
        setTextLabelStyle( lbl_usl );
        fmt_usl.setColumns( 10 );
        fmt_usl.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_usl.setScope( Double.class, null, null, false, false );
        fmt_usl.setEmptyValue( 0.0D );

        // @CompoentSetting<Fmt>( lbl_lsl , fmt_lsl )
        setTextLabelStyle( lbl_lsl );
        fmt_lsl.setColumns( 10 );
        fmt_lsl.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_lsl.setScope( Double.class, null, null, false, false );
        fmt_lsl.setEmptyValue( 0.0D );

        // @CompoentSetting( lbl_doorzones )
        setCaptionStyle( lbl_doorzones );
        add( cpt_shaft_positions, "gapbottom 18-12, span, aligny center" );
        Box vbox_title1 = Box.createVerticalBox();
        vbox_title1.add( lbl_usl);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_lsl);
        
        Box vbox_value2 = Box.createVerticalBox();
        vbox_value2.add( fmt_usl );
        vbox_value2.add( Box.createVerticalStrut(10));
        vbox_value2.add( fmt_lsl );
        add(vbox_title1, "skip 2, span, split 2, left, top, gapright 30");
        add(vbox_value2, "wrap 30");
        add( lbl_doorzones, "gapbottom 18-12, span, aligny center" );

        /** *************************************************************************** */
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 8202677196161198377L;
            public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                             int column ) {
                JLabel renderedLabel = ( JLabel )super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                renderedLabel.setHorizontalAlignment( SwingConstants.RIGHT );
                if ( value instanceof Number ) {
                    NumberFormat  nf = NumberFormat.getNumberInstance();
                    DecimalFormat df = ( DecimalFormat )nf;
                    df.applyPattern( "#,##0.00" );
                    if(column == 0) 
                    	renderedLabel.setText( value.toString() );
                    else
                    	renderedLabel.setText( df.format( value ) );
                    
                }
                renderedLabel.setForeground(Color.WHITE);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        setBackground(StartUI.SUB_BACKGROUND_COLOR);
                    } else {
                    	setBackground(StartUI.MAIN_BACKGROUND_COLOR);
                    }
                }else
                	setBackground(StartUI.BORDER_COLOR);
                
                return renderedLabel;
            }
        };
        doorZoneModel = new DefaultTableModel() {
            private static final long serialVersionUID = -2138212010292481218L;
            public boolean isCellEditable ( int row, int column ) {
                switch ( column ) {
                case 1 :
                case 2 :
                case 5 :
                case 6 :
                    return true;
                default :
                    return false;
                }
            }
            public Class<?> getColumnClass ( int columnIndex ) {
                if ( columnIndex == 0 )
                    return Integer.class;
                return Float.class;
            }
        };
        table = new JTable( doorZoneModel ) {
            private static final long serialVersionUID = -2466281171995793429L;
            @Override
            public Component prepareEditor ( TableCellEditor editor, int row, int column ) {
                setRowSelectionAllowed( false );

                Component c = super.prepareEditor( editor, row, column );
                if ( c instanceof JTextComponent ) {
                    ( ( JTextComponent )c ).selectAll();
                }
                return c;
            }
            public void removeEditor () {
                setRowSelectionAllowed( true );
                super.removeEditor();
            }
        };
        doorZoneModel.addColumn( getBundleText( "TABLE_COLUMN_DZ", "DZ" ) );
        doorZoneModel.addColumn( getBundleText( "TABLE_COLUMN_LDZ", "LDZ" ) );
        doorZoneModel.addColumn( getBundleText( "TABLE_COLUMN_UDZ", "UDZ" ) );
        doorZoneModel.addColumn( getBundleText( "TABLE_COLUMN_Dist", "Dist" ) );
        doorZoneModel.addColumn( getBundleText( "TABLE_COLUMN_Mid-point", "Mid-point" ) );
        doorZoneModel.addColumn( getBundleText( "TABLE_COLUMN_Fine-tune_Up", "Fine-tune Up" ) );
        doorZoneModel.addColumn( getBundleText( "TABLE_COLUMN_Fine-tune_Down", "Fine-tune Down" ) );
        table.setRowSelectionAllowed( true );
        table.setGridColor(StartUI.SUB_BACKGROUND_COLOR);
        table.setShowGrid(true);
        table.setRowHeight(20);
        table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        for(int i = 0; i < table.getColumnCount(); i++) {
        	setTableHeaderColor(table, i, StartUI.MAIN_BACKGROUND_COLOR);
        }
        
        JTableHeader head =  table.getTableHeader();
        head.setPreferredSize(new Dimension(1, 30));
        
        table.getColumnModel().getColumn( 0 ).setCellRenderer( rightRenderer );
        table.getColumnModel().getColumn( 0 ).setPreferredWidth(50);
        table.getColumnModel().getColumn( 1 ).setCellRenderer( rightRenderer );
        table.getColumnModel().getColumn( 2 ).setCellRenderer( rightRenderer );
        table.getColumnModel().getColumn( 3 ).setCellRenderer( rightRenderer );
        table.getColumnModel().getColumn( 4 ).setCellRenderer( rightRenderer );
        table.getColumnModel().getColumn( 5 ).setCellRenderer( rightRenderer );
        table.getColumnModel().getColumn( 6 ).setCellRenderer( rightRenderer );
        
        doorZoneModel.addTableModelListener(new TableModelListener() {
            
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE) {
                    switch(e.getColumn()) {
                    case 1:
                    case 2:
                        try {
                            final double ldz = Double.parseDouble( doorZoneModel.getValueAt( e.getFirstRow(), 1 ).toString() );
                            final double udz = Double.parseDouble( doorZoneModel.getValueAt( e.getFirstRow(), 2 ).toString() );
                            doorZoneModel.setValueAt( udz-ldz, e.getFirstRow(), 3 );
                            doorZoneModel.setValueAt( udz/2+ldz/2, e.getFirstRow(), 4 );
                        } catch(Exception ex) {
                            doorZoneModel.setValueAt( Float.NaN, e.getFirstRow(), 3 );
                            doorZoneModel.setValueAt( Float.NaN, e.getFirstRow(), 4 );
                        }
                        break;
                    }
                }
            }
        });

        JPanel tableWrapper = new JPanel( new BorderLayout() );
        tableWrapper.setBorder( BorderFactory.createLineBorder(StartUI.BORDER_COLOR) );
        tableWrapper.add( table.getTableHeader(), BorderLayout.NORTH );
        tableWrapper.add( table );
        add( tableWrapper, "span, grow, wrap 30-12" );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "DzCount", lbl_label_dz_count, lbl_value_dz_count );
        bindGroup( "ExecuteSelfLearnDoorZone", btn_execute_self_learn_door_zone );
        bindGroup( "Usl", lbl_usl, fmt_usl );
        bindGroup( "Lsl", lbl_lsl, fmt_lsl );
        bindGroup( "Doorzones", lbl_doorzones );
        loadI18N();
        revalidate();
        table.addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed ( KeyEvent e ) {
                if ( table.getSelectedColumn() >= 0 ) {
                    settingPanel.setDescription( TEXT.getString( String.format( "column%d.description", table.getSelectedColumn() ) ) );
                } else {
                    settingPanel.setDescription( "" );
                }
            }
        } );
        table.addMouseMotionListener( new MouseMotionAdapter() {
            @Override
            public void mouseMoved ( MouseEvent evt ) {
                int row = table.rowAtPoint( evt.getPoint() );
                int col = table.columnAtPoint( evt.getPoint() );
                if ( row >= 0 && col >= 0 ) {
                    settingPanel.setDescription( TEXT.getString( String.format( "column%d.description", col ) ) );
                }
            }
        } );
    }


    private void loadI18N () {
        cpt_control.setText( getBundleText( "LBL_cpt_control", "Control" ) );
        lbl_label_dz_count.setText( getBundleText( "LBL_lbl_label_dz_count", "DZ Count" ) );

        /* ---------------------------------------------------------------------------- */
        btn_execute_self_learn_door_zone.setText( getBundleText( "LBL_btn_execute_self_learn_door_zone", "Execute Self-learn Door zone" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_shaft_positions.setText( getBundleText( "LBL_cpt_shaft_positions", "Shaft Positions" ) );
        lbl_usl.setText( getBundleText( "LBL_lbl_usl", "USL" ) );
        lbl_lsl.setText( getBundleText( "LBL_lbl_lsl", "LSL" ) );
        lbl_doorzones.setText( getBundleText( "LBL_lbl_doorzones", "Doorzones" ) );

        /* ---------------------------------------------------------------------------- */
    }
    
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

    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
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


    private void setLabelTitleStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setLabelValueStyle ( JLabel c ) {
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


    public ShaftPositionsBean getShaftPositionsBean () throws ConvertException {
        if ( ! fmt_usl.checkValue() )
            throw new ConvertException();
        if ( ! fmt_lsl.checkValue() )
            throw new ConvertException();

        TableCellEditor editor = table.getCellEditor();
        if ( editor != null ) {
            editor.stopCellEditing();
        }

        ShaftPositionsBean bean_shaftPositions = new ShaftPositionsBean();
        bean_shaftPositions.setUsl( ( Double )fmt_usl.getValue() );
        bean_shaftPositions.setLsl( ( Double )fmt_lsl.getValue() );
        try {
            List<Map<String, Number>> details = new ArrayList<Map<String, Number>>();
            for ( int row = 0 ; row < doorZoneModel.getRowCount() ; row++ ) {
                Map<String, Number> map = new HashMap<String, Number>();
                map.put( "dz", ( Number )doorZoneModel.getValueAt( row, 0 ) );
                map.put( "ldz", ( Number )doorZoneModel.getValueAt( row, 1 ) );
                map.put( "udz", ( Number )doorZoneModel.getValueAt( row, 2 ) );
                map.put( "dist", ( Number )doorZoneModel.getValueAt( row, 3 ) );
                map.put( "mid-point", ( Number )doorZoneModel.getValueAt( row, 4 ) );
                map.put( "fine_tune_up", ( Number )doorZoneModel.getValueAt( row, 5 ) );
                map.put( "fine_tune_down", ( Number )doorZoneModel.getValueAt( row, 6 ) );
                details.add( map );
            }
            bean_shaftPositions.setDoorZoneDetails( details );
        } catch ( Exception e ) {
            throw new ConvertException( "Exception while door zone table convert." );
        }
        return bean_shaftPositions;
    }


    public ControlBean getControlBean () throws ConvertException {
        ControlBean bean_control = new ControlBean();
        return bean_control;
    }


    public void setShaftPositionsBean ( ShaftPositionsBean bean_shaftPositions ) {
        this.fmt_usl.setOriginValue( bean_shaftPositions.getUsl() );
        this.fmt_lsl.setOriginValue( bean_shaftPositions.getLsl() );
        doorZoneModel.setRowCount( 0 );
        if ( bean_shaftPositions.getDoorZoneDetails() != null ) {
            for ( int i = 0 ; i < bean_shaftPositions.getDoorZoneDetails().size() ; i++ ) {
                Map<String, Number> map            = bean_shaftPositions.getDoorZoneDetails().get( i );
                Number              dz             = map.get( "dz" );
                Number              ldz            = map.get( "ldz" );
                Number              udz            = map.get( "udz" );
                Number              dist           = map.get( "dist" );
                Number              mid_point      = map.get( "mid_point" );
                Number              fine_tune_up   = map.get( "fine_tune_up" );
                Number              fine_tune_down = map.get( "fine_tune_down" );
                doorZoneModel.addRow( new Object[] {
                    dz, ldz, udz, dist, mid_point, fine_tune_up, fine_tune_down
                } );
            }
        }
    }


    public void setControlBean ( ControlBean bean_control ) {
        this.lbl_value_dz_count.setText( bean_control.getDzCount() );
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


    public static DoorZones createPanel ( SettingPanel<DoorZones> panel ) {
        DoorZones gui = new DoorZones();
        gui.setSettingPanel( panel );
        return gui;
    }


    public void actionPerformed ( final ActionEvent e ) {
        if ( e.getSource() == btn_execute_self_learn_door_zone ) {
            do_btn_execute_self_learn_door_zone_actionPerformed( e );
        }
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    protected void do_btn_execute_self_learn_door_zone_actionPerformed ( final ActionEvent e ) {
        if(settingPanel instanceof DoorZonesSetting) {
            if(!ToolBox.requestRole((((DoorZonesSetting)settingPanel).connBean), WRITE_MCS_EXPRESSION)) {
                ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
                return;
            }
            try {
                ((DoorZonesSetting)settingPanel).onPause();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            
            SelfLearningDialog.doSelfLeraning((((DoorZonesSetting)settingPanel).connBean));
            
            try {
                ((DoorZonesSetting)settingPanel).onResume();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    public static class ControlBean {
        private String dzCount;




        public String getDzCount () {
            return this.dzCount;
        }


        public void setDzCount ( String dzCount ) {
            this.dzCount = dzCount;
        }
    }




    public static class ShaftPositionsBean {
        private Double                    usl;
        private Double                    lsl;
        private List<Map<String, Number>> doorZoneDetails;




        public Double getUsl () {
            return this.usl;
        }


        public Double getLsl () {
            return this.lsl;
        }


        public void setUsl ( Double usl ) {
            this.usl = usl;
        }


        public void setLsl ( Double lsl ) {
            this.lsl = lsl;
        }


        public List<Map<String, Number>> getDoorZoneDetails () {
            return doorZoneDetails;
        }


        public void setDoorZoneDetails ( List<Map<String, Number>> doorZoneDetails ) {
            this.doorZoneDetails = doorZoneDetails;
        }
    }
}
