package slecon.inspect.devices;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import base.cfg.FontFactory;
import slecon.StartUI;

public class DeviceTable extends JTable {
	private static final long serialVersionUID = 5926115539804263554L;

	public DeviceTable ( DeviceTableModel model ) {
        super();
        setModel( model );
        setShowGrid( true );
        setGridColor(StartUI.SUB_BACKGROUND_COLOR);
        setRowHeight(20);
        setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    }
	
	void initTable() {
		getColumnModel().getColumn( 0 ).setPreferredWidth( 150 );
        getColumnModel().getColumn( 1 ).setPreferredWidth( 100 );
        
        for(int i = 0; i < getColumnCount(); i++) {
        	setTableHeaderColor(this, i, StartUI.MAIN_BACKGROUND_COLOR);
        }
        
        JTableHeader head =  this.getTableHeader();
        head.setPreferredSize(new Dimension(1, 30));
        
        getColumnModel().getColumn( 0 ).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = -9201863710276563371L;
			public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                    int col ) {
					Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col );
					this.setHorizontalAlignment( JLabel.CENTER );
	                this.setForeground(Color.WHITE);
	                this.setFont(FontFactory.FONT_11_BOLD);
		            if(!isSelected)    
		            	if (row % 2 == 0) {
	                        setBackground(StartUI.SUB_BACKGROUND_COLOR);
	                    } else {
	                    	setBackground(StartUI.MAIN_BACKGROUND_COLOR);
	                    }
	                else
                    	setBackground(StartUI.BORDER_COLOR); 
					return c;
			}
        });
        
        getColumnModel().getColumn( 1 ).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = -9201863710276563371L;
			public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                    int col ) {
					Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col );
					this.setHorizontalAlignment( JLabel.CENTER );
	                this.setForeground(Color.WHITE);
	                this.setFont(FontFactory.FONT_11_BOLD);
	                if(!isSelected) 
	                	if (row % 2 == 0) {
	                        setBackground(StartUI.SUB_BACKGROUND_COLOR);
	                    } else {
	                    	setBackground(StartUI.MAIN_BACKGROUND_COLOR);
	                    }
	                else
                    	setBackground(StartUI.BORDER_COLOR);
					return c;
			}
        });
	}

	private static void setTableHeaderColor(JTable table, int columnIndex, Color c) {
		TableColumn column = table.getTableHeader().getColumnModel().getColumn(columnIndex);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(c);
        cellRenderer.setForeground(Color.WHITE);
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        cellRenderer.setHorizontalTextPosition(JLabel.CENTER);;
        column.setHeaderRenderer(cellRenderer);
    }
}
