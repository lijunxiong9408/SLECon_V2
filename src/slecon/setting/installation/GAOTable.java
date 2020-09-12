package slecon.setting.installation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import base.cfg.FontFactory;
import slecon.StartUI;
import slecon.component.table.GridBagTable;
import slecon.setting.installation.GroupConfiguration.GAOTableModel;

public class GAOTable extends GridBagTable {
    private static final long serialVersionUID = -2024625964177633948L;
    private TableCellEditor   editor           = new DefaultCellEditor( new JTextField() ) {
        private static final long serialVersionUID = 5708141404804595300L;
        private Object            value;
        public Component getTableCellEditorComponent ( JTable table, Object value, boolean isSelected, int row, int column ) {
            this.value = value;
            ( ( JComponent )getComponent() ).setBorder( new LineBorder( Color.WHITE ) );

            JTextField editor = ( JTextField )super.getTableCellEditorComponent( table, value, isSelected, row, column );
            editor.setForeground( table.getForeground() );
            if ( column >= 2 ) {
                editor.setHorizontalAlignment( SwingConstants.RIGHT );
                if ( value instanceof Number )
                    editor.setText( Integer.toHexString( ( ( Number )value ).intValue() ).toUpperCase() );
                else
                    editor.setForeground( Color.red );
            }
            return editor;
        }
        
        public boolean stopCellEditing () {
            String s = ( String )super.getCellEditorValue();
            if ( ! "".equals( s ) ) {
                try {
                    if ( value instanceof Integer ) {
                        value = Integer.parseInt( s, 16 );
                    }
                } catch ( Exception e ) {
                    ( ( JComponent )getComponent() ).setBorder( new LineBorder( Color.red ) );
                    return false;
                }
            }
            return super.stopCellEditing();
        }
        
        @Override
        public Object getCellEditorValue () {
            return value;
        }
    };
    
    private static void setTableHeaderColor(JTable table, int columnIndex, Color c) {
        TableColumn column = table.getTableHeader().getColumnModel()
                .getColumn(columnIndex);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(c);
        cellRenderer.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
        cellRenderer.setFont(FontFactory.FONT_12_BOLD);
        cellRenderer.setHorizontalTextPosition(JLabel.RIGHT);
        cellRenderer.setHorizontalAlignment(JLabel.RIGHT);
        cellRenderer.setForeground(Color.WHITE);
        column.setHeaderRenderer(cellRenderer);
    }
    
    private TableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
        private static final long serialVersionUID = 5644897099872618782L;
        public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                         int column ) {
            JLabel label = ( JLabel )super.getTableCellRendererComponent( table, value, false, false, row, column );
            label.setHorizontalAlignment( SwingConstants.RIGHT );
            label.setForeground(Color.WHITE);
            if ( table.getSelectedRow() / 2 == row / 2 ) {
                label.setBackground( Color.gray );
                label.setFont( label.getFont().deriveFont( Font.BOLD ) );
            } else {
                label.setBackground( Color.lightGray );
                label.setFont( label.getFont().deriveFont( Font.PLAIN ) );
            }
            return label;
        }
    };
    
    private TableCellRenderer normalRenderer = new DefaultTableCellRenderer() {
        private static final long serialVersionUID = -3500230397712221379L;
        public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                         int column ) {
            JLabel label = ( JLabel )super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            label.setHorizontalAlignment( SwingConstants.RIGHT );
            if ( value instanceof Number ) {
                label.setText( Integer.toHexString( ( ( Number )value ).intValue() ).toUpperCase() );
            	label.setForeground(Color.WHITE);
            }else
                label.setForeground( Color.red );
            
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
    
    private final GAOTableModel model;

    public void createDefaultColumnsFromModel() {
        super.createDefaultColumnsFromModel();
        getColumnModel().getColumn(0).setResizable(false);
        getColumnModel().getColumn(1).setResizable(false);
        for(int i=2; i<getColumnCount(); i++) {
            getColumnModel().getColumn(i).setResizable(false);
            getColumnModel().getColumn(i).setMinWidth(98);
            getColumnModel().getColumn(i).setPreferredWidth(98);
        }
    }

    public GAOTable () {
        super( new GAOTableModel() );
        model = ( GAOTableModel )getModel();
        setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        setRowSelectionAllowed( true );
        setGridColor(StartUI.SUB_BACKGROUND_COLOR);
        setShowGrid(true);
        setRowHeight(20);
    }


    @Override
    public Component prepareEditor ( TableCellEditor editor, int row, int column ) {
        Component c = super.prepareEditor( editor, row, column );
        if ( c instanceof JTextComponent ) {
            ( ( JTextComponent )c ).selectAll();
        }
        return c;
    }


    @Override
    public TableCellEditor getCellEditor ( int row, int column ) {
        if ( column >= 2 )
            return editor;
        else
            return super.getCellEditor( row, column );
    }


    @Override
    public TableCellRenderer getCellRenderer ( int row, int column ) {
        if ( column >= 2 )
            return normalRenderer;
        else
            return headerRenderer;
    }
    
    public void setData ( String[] text, int[][][] data ) {
        model.setData( text, data );
        setDimension( text.length, 16 );
        for(int i = 0; i < this.getColumnCount(); i++) {
        	setTableHeaderColor(this, i, StartUI.MAIN_BACKGROUND_COLOR);
        }
        
        JTableHeader head =  this.getTableHeader();
        head.setPreferredSize(new Dimension(1, 30));
    }


    public void setDimension ( final int floorCount, int liftCount ) {
        model.setDimension( floorCount, liftCount );
        for ( int i = 0 ; i < floorCount ; i++ ) {
            mergeCells( i * 2, i * 2 + 1, 0, 0 );
            mergeCells( i * 2, i * 2 + 1, 1, 1 );
        }
    }


    public int[][][] getData () {
        return model.data;
    }

    public void update () {
        model.fireTableDataChanged();
    }
}