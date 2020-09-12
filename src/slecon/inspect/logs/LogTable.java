package slecon.inspect.logs;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import logic.evlog.Level;
import logic.evlog.MCSErrorCode;
import slecon.StartUI;
import slecon.ToolBox;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import comm.Parser_Log.Log;




public class LogTable extends JTable implements PropertyChangeListener {
    private static final long             serialVersionUID = -4793573201077600328L;
    private static final Icon             critIcon         = ImageFactory.INSPECT_LOG_ICON_CRITICAL.icon();
    private static final Icon             warnIcon         = ImageFactory.INSPECT_LOG_ICON_WARNING.icon();
    private static final Icon             normIcon         = ImageFactory.INSPECT_LOG_ICON_GENERAL.icon();
    private static final ResourceBundle   TEXT             = ToolBox.getResourceBundle( "inspect.Log" );
    private TableRowSorter<LogTableModel> sorter;
    private LogTableFilter                filter;




    public LogTable ( LogTableModel model ) {
        super();
        setShowGrid( false );
        addPropertyChangeListener( this );
        setModel( model == null
                  ? new LogTableModel()
                  : model );
        setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    }
    
    private static void setTableHeaderColor(JTable table, int columnIndex, Color c) {
        TableColumn column = table.getTableHeader().getColumnModel()
                .getColumn(columnIndex);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            /** serialVersionUID */
            private static final long serialVersionUID = 43279841267L;

            @Override
            public Component getTableCellRendererComponent(JTable table, 
                    Object value, boolean isSelected,boolean hasFocus,
                    int row, int column) {

                ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                        .setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
                
                ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                		.setFont(FontFactory.FONT_12_BOLD);

                return super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
            }
        };
        cellRenderer.setBackground(c);
        cellRenderer.setForeground(Color.WHITE);
        column.setHeaderRenderer(cellRenderer);
    }

    private void initColumnModel () {

        // "ID", "Type", "Error Code", "Date", "Time", "Description"
        getColumnModel().getColumn( 0 ).setPreferredWidth( 25 );
        getColumnModel().getColumn( 1 ).setPreferredWidth( 25 );
        getColumnModel().getColumn( 2 ).setPreferredWidth( 25 );
        getColumnModel().getColumn( 3 ).setPreferredWidth( 40 );
        getColumnModel().getColumn( 4 ).setPreferredWidth( 40 );
        getColumnModel().getColumn( 5 ).setPreferredWidth( 120 );
       
        for(int i = 0; i < this.getColumnCount(); i++) {
        	setTableHeaderColor(this, i, StartUI.MAIN_BACKGROUND_COLOR);
        }
        
        JTableHeader head =  this.getTableHeader();
        head.setPreferredSize(new Dimension(1, 30));
        
        this.setShowGrid(true);
        this.setGridColor(StartUI.SUB_BACKGROUND_COLOR);
        this.setRowHeight(20);
        
        getColumnModel().getColumn( 0 ).setCellRenderer( new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 249379973271800160L;
            public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                             int col ) {
                Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col );
                this.setHorizontalAlignment( JLabel.LEFT );
                this.setForeground(Color.WHITE);
                this.setFont(FontFactory.FONT_11_BOLD);
                if (table.getModel() instanceof LogTableModel) {
                    LogTableModel model = (LogTableModel) table.getModel();

                    Level l = null;
                    final Object obj = model.getValueAt(table.convertRowIndexToModel(row), 1);
                    if (obj instanceof Level)
                        l = (Level) obj;

                    if (!isSelected) {
                        if (row % 2 == 0) {
                            setBackground(StartUI.SUB_BACKGROUND_COLOR);
                        } else {
                        	setBackground(StartUI.MAIN_BACKGROUND_COLOR);
                        }
                    }else
                    	setBackground(StartUI.BORDER_COLOR);
                    
                    if (c instanceof JLabel) {
                        ( (JLabel) c ).setIconTextGap(8);
                        if (l == Level.CRITICAL)
                            ( (JLabel) c ).setIcon(critIcon);
                        else if (l == Level.WARNING)
                            ( (JLabel) c ).setIcon(warnIcon);
                        else
                            ( (JLabel) c ).setIcon(normIcon);
                    }
                }
                return c;
            }
        } );
        getColumnModel().getColumn( 1 ).setCellRenderer( new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 249379973271800160L;
            public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                             int col ) {
                Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col );
                this.setForeground(Color.WHITE);
                this.setFont(FontFactory.FONT_11_BOLD);
                if ( value instanceof Level ) {
                    Level item = ( Level )value;
                    try {
                        setText( TEXT.getString( "Table." + item.name() ) );
                    } catch ( MissingResourceException e ) {
                    }
                }
                if (table.getModel() instanceof LogTableModel) {
                    if (!isSelected)
                    	if (row % 2 == 0) {
                            setBackground(StartUI.SUB_BACKGROUND_COLOR);
                        } else {
                        	setBackground(StartUI.MAIN_BACKGROUND_COLOR);
                        }
                    else
                    	setBackground(StartUI.BORDER_COLOR); 
                   
                }
                return c;
            }
        } );
        getColumnModel().getColumn( 2 ).setCellRenderer( new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 249379973271800160L;
            public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                             int col ) {
                Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col );
                this.setForeground(Color.WHITE);
                this.setFont(FontFactory.FONT_11_BOLD);
                if ( c instanceof JLabel && value instanceof Number ) {
                    ( ( JLabel )c ).setText( String.format( "0x%04X", ( ( Number )value ).intValue() ) );
                }
                if (table.getModel() instanceof LogTableModel) {
                    if (!isSelected)
                    	if (row % 2 == 0) {
                            setBackground(StartUI.SUB_BACKGROUND_COLOR);
                        } else {
                        	setBackground(StartUI.MAIN_BACKGROUND_COLOR);
                        }
                    else
                    	setBackground(StartUI.BORDER_COLOR); 
                }
                return c;
            }
        } );
        getColumnModel().getColumn( 3 ).setCellRenderer( new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 249379973271800160L;
            public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                             int col ) {
                Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col );
                this.setForeground(Color.WHITE);
                this.setFont(FontFactory.FONT_11_BOLD);
                if ( c instanceof JLabel && value instanceof Date ) {
                    DateFormat dateFormat = DateFormat.getDateInstance( DateFormat.LONG, BaseFactory.getLocale() );
                    ( ( JLabel )c ).setText( dateFormat.format( value ) );
                }
                if (table.getModel() instanceof LogTableModel) {
                    if (!isSelected)
                    	if (row % 2 == 0) {
                            setBackground(StartUI.SUB_BACKGROUND_COLOR);
                        } else {
                        	setBackground(StartUI.MAIN_BACKGROUND_COLOR);
                        }
                    else
                    	setBackground(StartUI.BORDER_COLOR); 
                }
                return c;
            }
        } );
        getColumnModel().getColumn( 4 ).setCellRenderer( new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 249379973271800160L;
            public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                             int col ) {
                Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col );
                this.setForeground(Color.WHITE);
                this.setFont(FontFactory.FONT_11_BOLD);
                if ( c instanceof JLabel && value instanceof Date ) {
                    DateFormat dateFormat = DateFormat.getTimeInstance( DateFormat.LONG, BaseFactory.getLocale() );
                    ( ( JLabel )c ).setText( dateFormat.format( value ) );
                }
                if (table.getModel() instanceof LogTableModel) {
                    if (!isSelected)
                    	if (row % 2 == 0) {
                            setBackground(StartUI.SUB_BACKGROUND_COLOR);
                        } else {
                        	setBackground(StartUI.MAIN_BACKGROUND_COLOR);
                        }
                    else
                    	setBackground(StartUI.BORDER_COLOR); 
                }
                return c;
            }
        } );
        getColumnModel().getColumn( 5 ).setCellRenderer( new DefaultTableCellRenderer() {
            private static final long serialVersionUID = - 3226069666274526797L;

            public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                             int col ) {
                Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col );
                this.setForeground(Color.WHITE);
                this.setFont(FontFactory.FONT_11_BOLD);
                if (table.getModel() instanceof LogTableModel) {
                    if (!isSelected)
                    	if (row % 2 == 0) {
                            setBackground(StartUI.SUB_BACKGROUND_COLOR);
                        } else {
                        	setBackground(StartUI.MAIN_BACKGROUND_COLOR);
                        }
                    else
                    	setBackground(StartUI.BORDER_COLOR); 
                }
                return c;
            }
        } );
    }


    public void propertyChange ( final PropertyChangeEvent evt ) {
        if ( evt.getSource() == this ) {
            do_this_propertyChange( evt );
        }
    }


    protected void do_this_propertyChange ( final PropertyChangeEvent evt ) {
        if ( "model".equals( evt.getPropertyName() ) ) {
            if ( evt.getNewValue() instanceof LogTableModel ) {
                initColumnModel();
                sorter = new TableRowSorter<LogTableModel>( ( LogTableModel )evt.getNewValue() );
                if ( filter != null )
                    sorter.setRowFilter( filter );
                setRowSorter( sorter );
            }
        }
    }


    public final LogTableFilter getFilter () {
        return filter;
    }


    public final void setFilter ( LogTableFilter filter ) {
        LogTableFilter oldValue = this.filter;
        if ( oldValue != filter ) {
            this.filter = filter;
            if ( sorter != null ) {
                sorter.setRowFilter( filter );
                sorter.setSortKeys( null );
            }
            firePropertyChange( "filter", oldValue, this.filter );
        }
    }

/*
	@Override
	public String getToolTipText() {
		StringBuffer sb   = new StringBuffer();
		int index = ( ( LogTableModel )this.getModel() ).getLogByIndex( this.getSelectedRow()).rawLog.errcode;
		for( MCSErrorCode code : MCSErrorCode.values() ) {
			if(index == code.code) {
				sb.append(code.getDescription());
				break;
			}
		}
		return sb.toString();
	}
 */  
    
}
