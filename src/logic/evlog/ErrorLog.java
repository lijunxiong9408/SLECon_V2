package logic.evlog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import comm.Parser_Log.Log;
import slecon.ToolBox;

public class ErrorLog {
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "logic.ErrorLog" );
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM hh:mm:ss");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    public final int index;
    public final Log rawLog;
    
    public ErrorLog(int index, Log log) {
        this.index = index;
        this.rawLog = log;
    }


    public String toSimpleString () {
        String date = sdf1.format( new Date( rawLog.timestamp ) );
        String type = rawLog.type == 'O' ? "OCS" : rawLog.type == 'M' ? "MCS" : "XXX";
        String description = toAbbrString();
        String line = String.format( "[%s] [%s] %s", date, type, description );
        return line;
    }
    
    
    public String toAbbrString () {
        if ( rawLog.type == 'M' ) {
            if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_LSL.code )
                return String.format( MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_LSL.getDescription(), rawLog.current_floor );
            if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_USL.code )
                return String.format( MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_USL.getDescription(), rawLog.current_floor );
            if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_LSL_FOUND.code )
                return String.format( MCSErrorCode.EVLOG_SLDZ_LSL_FOUND.getDescription(), rawLog.position );
            if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_USL_FOUND.code )
                return String.format( MCSErrorCode.EVLOG_SLDZ_USL_FOUND.getDescription(), rawLog.position );
            if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_LDZ_FOUND.code )
                return String.format( MCSErrorCode.EVLOG_SLDZ_LDZ_FOUND.getDescription(), rawLog.current_floor, rawLog.position );
            if ( rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_UDZ_FOUND.code )
                return String.format( MCSErrorCode.EVLOG_SLDZ_UDZ_FOUND.getDescription(), rawLog.current_floor, rawLog.position );
        }
        String description = TEXT.getString( "Unknown.text" );
        if (rawLog.type == 'O') {
            OCSErrorCode l = OCSErrorCode.get(rawLog.errcode);
            description = l == null ? String.format( TEXT.getString( "Unknown.text" ) + " 0x%04x", rawLog.errcode ) : l.getDescription();
        } else if (rawLog.type == 'M') {
            MCSErrorCode l = MCSErrorCode.get(rawLog.errcode);
            description = l == null ? String.format( TEXT.getString( "Unknown.text" ) + " 0x%04x", rawLog.errcode ) : l.getDescription();
        }
        return description;
    }

    public String toDetailString() {
        StringBuffer sb = new StringBuffer();
        sb.append( TEXT.getString( "Time.text" ) + ": " + sdf2.format(new Date(rawLog.timestamp)) + "\n" );
        sb.append( TEXT.getString( "Type.text" ) + ": " + ( rawLog.type == 'O' ? "OCS" : rawLog.type == 'M' ? "MCS" : "N/A" ) + "\n" );
        sb.append( TEXT.getString( "ErrorCode.text" ) + ": " + String.format("0x%04x", rawLog.errcode) + "\n" );
        sb.append( TEXT.getString( "Description.text" ) + ": " + toAbbrString() );
        return sb.toString();
    }

}
