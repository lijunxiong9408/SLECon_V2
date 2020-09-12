package slecon.setting.setup.door;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.NumberFormatter;

import base.cfg.FontFactory;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;




public class WeightSensorTable extends JPanel {
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.door.CabinAndDoor" );
    private static final long serialVersionUID = 1981724333659324276L;
    private String            txt_tableTitle       = TEXT.getString( "LBL_title" );
    private String            txt_currVal       = TEXT.getString( "LBL_curr_val" );
    private String[]          hTitle           = { txt_tableTitle, txt_currVal };
    private String[]          vTitle           = { TEXT.getString( "LBL_sensor1" ), TEXT.getString( "LBL_sensor2" ),
                                                   TEXT.getString( "LBL_sensor3" ), TEXT.getString( "LBL_sensor4" ) };
    private Class[] types = { String.class, Float.class };
    private JTable  mainTable;

    public void SetTableEnable(boolean enable) {
    	mainTable.setEnabled(enable);
    }

    public WeightSensorTable () {
        setLayout( new BorderLayout() );
        setBorder( BorderFactory.createLineBorder( StartUI.BORDER_COLOR ) );
        
        mainTable = new JTable();
        mainTable.setModel( new DefaultTableModel( new Object[][]{
        }, hTitle ) );
        mainTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        mainTable.setColumnSelectionAllowed( true );
        
        add( mainTable.getTableHeader(), BorderLayout.NORTH);
        add( mainTable);
    }


    public void commitEdit () {
        int columnCount = mainTable.getColumnCount();
        for ( int i = 0; i < columnCount; i++ ) {
            TableCellEditor editor = mainTable.getColumnModel().getColumn( i ).getCellEditor();
            if ( editor != null ) {
                System.out.println( "commit" );
                editor.stopCellEditing();
            }
        }
    }


    public JTable getMainTable () {
        return mainTable;
    }


    public ListSelectionModel getSelectionModel () {
        return mainTable.getSelectionModel();
    }


    public int getSelectedRow () {
        return mainTable.getSelectedRow();
    }


    public int getSelectedColumn () {
        return mainTable.getSelectedColumn();
    }


    public void setData ( Object[][] tableData ) {
        if ( tableData.length != 4 )
            return;
        for ( int i = 0 ; i < tableData.length ; i++ ) {
            if ( tableData[ i ].length != 1 )
                return;
            for ( int j = 0 ; j < 1 ; j++ ) {
                if ( ! types[ j + 1 ].isInstance( tableData[ i ][ j ] ) )
                    return;
            }
        }
        Object[][] Data = new Object[4][2];
        for(int i = 0; i < 4; i++) {
        	for(int j = 0; j < 2; j++) {
        		if(j == 0) {
        			Data[i][j] = vTitle[i];
        		}else {
        			Data[i][j] = tableData[i][j - 1];
        		}
        	}
        }
        
        mainTable.setRowHeight(25);
        mainTable.setModel( new WeightSensorTableModel( Data ) );
        for(int i = 0; i < mainTable.getColumnCount(); i++) {
        	if(i > 0) {
        		mainTable.getColumnModel().getColumn( i ).setCellEditor( new NumberCellEditor() );
        	}
        	mainTable.getColumnModel().getColumn( i ).setCellRenderer(new RowNumberRenderer());
        	mainTable.getColumnModel().getColumn( i ).setPreferredWidth(90);
        }

        for(int i = 0; i < mainTable.getColumnCount(); i++) {
        	setTableHeaderColor(mainTable, i, StartUI.MAIN_BACKGROUND_COLOR);
        }
        JTableHeader head =  mainTable.getTableHeader();
        head.setPreferredSize(new Dimension(1, 30));
        
    }


    public Object[][] getData () {
        Object[][] result = new Object[ 4 ][ 1 ];
        TableModel model  = mainTable.getModel();
        for ( int i = 0 ; i < 4 ; i++ )
            for ( int j = 0 ; j < 1 ; j++ ) {
                result[ i ][ j ] = model.getValueAt( i, j + 1 );
            }
        return result;
    }


    private static class NumberCellEditor extends DefaultCellEditor {
        private static final long serialVersionUID = 7184125764155372913L;

        public NumberCellEditor () {
            super( new JFormattedTextField() );
        }


        @Override
        public Component getTableCellEditorComponent ( JTable table, Object value, boolean isSelected, int row, int column ) {
            JFormattedTextField editor = ( JFormattedTextField )super.getTableCellEditorComponent( table, value, isSelected, row, column );
            if ( value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long ) {
                Locale       myLocale      = Locale.getDefault();
                NumberFormat numberFormatB = NumberFormat.getInstance( myLocale );
                editor.setFormatterFactory( new javax.swing.text.DefaultFormatterFactory( new NumberFormatter( numberFormatB ) ) );
                editor.setHorizontalAlignment( SwingConstants.RIGHT );
                editor.setValue( value );
            } else if ( value instanceof Number ) {
                Locale       myLocale      = Locale.getDefault();
                NumberFormat numberFormatB = NumberFormat.getInstance( myLocale );
                numberFormatB.setMaximumFractionDigits( 2 );
                numberFormatB.setMinimumFractionDigits( 1 );
                numberFormatB.setMinimumIntegerDigits( 1 );
                editor.setFormatterFactory( new javax.swing.text.DefaultFormatterFactory( new NumberFormatter( numberFormatB ) ) );
                editor.setHorizontalAlignment( SwingConstants.RIGHT );
                editor.setValue( value );
            }
            editor.selectAll();
            return editor;
        }


        @Override
        public boolean stopCellEditing () {
            try {

                // try to get the value
                this.getCellEditorValue();
                return super.stopCellEditing();
            } catch ( Exception ex ) {
                return false;
            }
        }


        @Override
        public Object getCellEditorValue () {

            // get content of textField
            String str = ( String )super.getCellEditorValue();
            if ( str == null ) {
                return null;
            }
            if ( str.length() == 0 ) {
                return null;
            }

            // try to parse a number
            try {
                ParsePosition pos = new ParsePosition( 0 );
                Number        n   = NumberFormat.getInstance().parse( str, pos );
                if ( pos.getIndex() != str.length() ) {
                    throw new ParseException( "parsing incomplete", pos.getIndex() );
                }

                // return an instance of column class
                return n;
            } catch ( ParseException pex ) {
                throw new RuntimeException( pex );
            }
        }
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


    /*
     * Borrow the renderer from JDK1.4.2 table header
     */
    private class RowNumberRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = -7253863195459633978L;
        public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                int column ) {
			JLabel label = ( JLabel )super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
			label.setHorizontalAlignment( SwingConstants.RIGHT );
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
    }

    private class WeightSensorTableModel extends DefaultTableModel {
        Object data[][];

        public WeightSensorTableModel ( Object[][] data ) {
            super( data, hTitle );
            this.data = data;
        }
        
        public void setValueAt ( Object aValue, int row, int column ) {
        	if ( aValue instanceof Number && column > 0 ) {
                Number val = ( Number )aValue;
                if ( types[ column ].equals( Long.class ) ) {
                    data[ row ][ column ] = val.longValue();
                } else if ( types[ column ].equals( Integer.class ) ) {
                    data[ row ][ column ] = val.intValue();
                } else if ( types[ column ].equals( Float.class ) ) {
                    data[ row ][ column ] = val.floatValue();
                }
            }else if(aValue instanceof String) {
            	data[ row ][ column ] = vTitle[row];
            }
        }
        

        @Override
        public Class<?> getColumnClass ( int columnIndex ) {
            return types[ columnIndex ];
        }

        @Override
        public Object getValueAt ( int row, int column ) {
        	return data[ row ][ column ];
        }
    }
}
