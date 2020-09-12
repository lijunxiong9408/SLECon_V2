package slecon.inspect.devices;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import slecon.ToolBox;




public class DeviceTableModel extends AbstractTableModel {
    private static final long           serialVersionUID = -2742626673180969170L;
    private static final ResourceBundle bundle           = ToolBox.getResourceBundle( "logic.gui.DeviceTable" );
    private static final Class<?>[]     colClasses       = new Class[]{ Integer.class, String.class };
    private static final String[]       colTitles        = new String[]{ bundle.getString( "ID.text" ), bundle.getString( "DeviceType.text" ) };
    public ArrayList<DeviceTableItem>   data             = new ArrayList<DeviceTableItem>();




    public void clear () {
        data.clear();
    }


    public void setAllItem ( List<DeviceTableItem> items ) {
        data = new ArrayList<>(items);
        fireTableDataChanged();
    }
    
    
    public void addItem ( DeviceTableItem item ) {
        for(int i=0; i<data.size();) {
            if(data.get(i).getId()==item.getId()) {
                data.remove(i);
                continue;
            }
            i++;
        }
        data.add( item );
    }


    public int getRowIndexByID ( Integer id ) {
        if ( id == null )
            return -1;

        int index = 0;
        for ( DeviceTableItem itm : data ) {
            if ( id == itm.getId() )
                return index;
            index++;
        }
        return -1;
    }


    @Override
    public int getRowCount () {
        return data.size();
    }


    @Override
    public int getColumnCount () {
        return colClasses.length;
    }


    @Override
    public Object getValueAt ( int rowIndex, int columnIndex ) {
        if ( rowIndex < data.size() ) {
            switch ( columnIndex ) {
            case 0 :
                return data.get( rowIndex ).getId();
            case 1 :
                return data.get( rowIndex ).getDeviceType();
            }
        }
        return null;
    }


    @Override
    public Class<?> getColumnClass ( int columnIndex ) {
        return columnIndex < colClasses.length
               ? colClasses[ columnIndex ]
               : null;
    }


    @Override
    public String getColumnName ( int column ) {
        return column < colTitles.length
               ? colTitles[ column ]
               : null;
    }


    @Override
    public boolean isCellEditable ( int rowIndex, int columnIndex ) {
        return false;
    }
}
