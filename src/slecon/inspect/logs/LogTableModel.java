package slecon.inspect.logs;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import logic.evlog.ErrorLog;
import logic.evlog.Level;
import logic.evlog.MCSErrorCode;
import logic.evlog.OCSErrorCode;
import slecon.ToolBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Log.Log;




public final class LogTableModel extends AbstractTableModel {
    private static final ResourceBundle bundle   = ToolBox.getResourceBundle( "inspect.Log" );
    
    private static final Logger logger           = LogManager.getLogger( LogTableModel.class );
    
    private static final long   serialVersionUID = -8649422485721969429L;
    
    private String[]            titles           = {
        bundle.getString( "Table.Header.ID" ), bundle.getString( "Table.Header.Type" ), bundle.getString( "Table.Header.ErrorCode" ),
        bundle.getString( "Table.Header.Date" ), bundle.getString( "Table.Header.Time" ), bundle.getString( "Table.Header.Description" ),
    };
    @SuppressWarnings( "rawtypes" )
    private Class[]          classes = {
        Integer.class, Level.class, Integer.class, Timestamp.class, Timestamp.class, String.class
    };
    private Vector<ErrorLog> logs    = new Vector<>();




    public Set<Integer> getAllErrorCodeID () {
        TreeSet<Integer> result = new TreeSet<>();
        synchronized ( logs ) {
            for ( ErrorLog el : logs ) {
                Log rawLog = el.rawLog;
                result.add( (int) rawLog.errcode );
            }
        }
        return result;
    }


    public void addLog ( ErrorLog logEntry ) {
        synchronized ( logs ) {
            logs.add( logEntry );
        }
        fireTableRowsInserted( logs.size() - 1, logs.size() - 1 );
    }


    public void clear () {
        int upper = -1;
        synchronized ( logs ) {
            upper = logs.size() - 1;
            if ( upper >= 0 )
                logs.clear();
        }
        if ( upper >= 0 )
            fireTableRowsDeleted( 0, upper );
    }


    public ErrorLog getLogByIndex ( int index ) {
        synchronized ( logs ) {
            return logs.get( index );
        }
    }


    public ArrayList<ErrorLog> getAllLogs () {
        synchronized ( logs ) {
            return new ArrayList<>( logs );
        }
    }


    @Override
    public Class<?> getColumnClass ( int columnIndex ) {
        return classes[ columnIndex ];
    }


    @Override
    public String getColumnName ( int columnIndex ) {
        return titles[ columnIndex ];
    }


    @Override
    public int getColumnCount () {
        return titles.length;
    }


    @Override
    public boolean isCellEditable ( int row, int col ) {
        return false;
    }


    @Override
    public int getRowCount () {
        synchronized ( logs ) {
            return logs.size();
        }
    }


    @Override
    public Object getValueAt ( int rowIndex, int columnIndex ) {
        ErrorLog log;
        Log rawLog;
        synchronized ( logs ) {
            log = logs.get( rowIndex );
            rawLog = log.rawLog;
        }

        try {
            switch ( columnIndex ) {
            case 0 :
                return rowIndex+1;
            case 1 :    // level
                switch (rawLog.type) {
                case 'O': {
                    OCSErrorCode l = OCSErrorCode.get(rawLog.errcode);
                    if (l != null)
                        return l.getLevel();
                    break;
                }
                case 'M': {
                    MCSErrorCode l = MCSErrorCode.get(rawLog.errcode);
                    if (l != null)
                        return l.getLevel();
                    break;
                }
                }
                break;
            case 2 :
                return rawLog.errcode & 0xFFFF;
            case 3 :
            case 4 :
                return new Timestamp( rawLog.timestamp );
            case 5 :
                switch (rawLog.type) {
                case 'O': {
                    OCSErrorCode l = OCSErrorCode.get(rawLog.errcode);
                    if (l != null)
                        return l.getDescription();
                    break;
                }
                case 'M': {
                    MCSErrorCode l = MCSErrorCode.get(rawLog.errcode);
                    if (l != null) {
                    	if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_LSL.code )
                            return String.format( MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_LSL.getDescription(), rawLog.current_floor );
                    	else if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_USL.code )
                            return String.format( MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_USL.getDescription(), rawLog.current_floor );
                    	else if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_LSL_FOUND.code )
                            return String.format( MCSErrorCode.EVLOG_SLDZ_LSL_FOUND.getDescription(), rawLog.position );
                    	else if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_USL_FOUND.code )
                            return String.format( MCSErrorCode.EVLOG_SLDZ_USL_FOUND.getDescription(), rawLog.position );
                    	else if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_LDZ_FOUND.code )
                            return String.format( MCSErrorCode.EVLOG_SLDZ_LDZ_FOUND.getDescription(), rawLog.current_floor, rawLog.position );
                    	else if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_UDZ_FOUND.code )
                            return String.format( MCSErrorCode.EVLOG_SLDZ_UDZ_FOUND.getDescription(), rawLog.current_floor, rawLog.position );
                        else
                        	return l.getDescription();
                    }
                    break;
                }
                }
                break;
            }
        } catch ( Exception e ) {
            logger.error( "unknown log record" );
        }
        return Main.TEXT.getString("Table.UnknownCell");
    }
}
