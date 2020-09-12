package slecon.inspect.logs;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.RowFilter;

import logic.evlog.Level;




public class LogTableFilter extends RowFilter<LogTableModel, Integer> {
    public static DateFormat DATE_FORMAT = new SimpleDateFormat( "yyyyMMdd" );
    public static DateFormat TIME_FORMAT = new SimpleDateFormat( "HHmmss" );
    private LogFilterBean    filterbean;




    public LogTableFilter ( LogFilterBean filterbean ) {
        super();
        this.filterbean = filterbean;
    }


    public void setLogFilterBean ( LogFilterBean filterbean ) {
        this.filterbean = filterbean;
    }


    public Long getDate ( Date date ) {
        return Long.parseLong( DATE_FORMAT.format( date ) );
    }


    public Long getTime ( Date date ) {
        return Long.parseLong( TIME_FORMAT.format( date ) );
    }


    @Override
    public boolean include ( Entry<? extends LogTableModel, ? extends Integer> entry ) {

        // maybe "Unknown"
        Level   level  = entry.getValue( 1 ) instanceof Level
                         ? ( Level )entry.getValue( 1 )
                         : null;
        int     code   = ( ( Number )entry.getValue( 2 ) ).intValue();
        long    date   = getDate( ( Date )entry.getValue( 3 ) );
        long    time   = getTime( ( Date )entry.getValue( 4 ) );
        boolean result = true;
        if ( filterbean != null ) {
            if ( filterbean.getType() != null && level != null && ! filterbean.getType().levels.contains( level ) ) {
                result = false;
            }
            if ( filterbean.getErrorCode() != null && filterbean.getErrorCode().intValue() != code ) {
                result = false;
            }
            if ( filterbean.getStartDate() != null && getDate( filterbean.getStartDate() ).compareTo( date ) > 0 ) {
                result = false;
            }
            if ( filterbean.getEndDate() != null && getDate( filterbean.getEndDate() ).compareTo( date ) < 0 ) {
                result = false;
            }
            if ( filterbean.getStartTime() != null && getTime( filterbean.getStartTime() ).compareTo( time ) > 0 ) {
                result = false;
            }
            if ( filterbean.getEndTime() != null && getTime( filterbean.getEndTime() ).compareTo( time ) < 0 ) {
                result = false;
            }
        }
        return result;
    }


    public static class LogFilterBean {
        private TypeOptions type;
        private Integer     errorCode;
        private Date        startDate;
        private Date        endDate;
        private Date        startTime;
        private Date        endTime;




        public LogFilterBean () {
            super();
        }


        public LogFilterBean ( TypeOptions type, Integer errorCode, Date startDate, Date endDate, Date startTime, Date endTime ) {
            super();
            this.type      = type;
            this.errorCode = errorCode;
            this.startDate = startDate;
            this.endDate   = endDate;
            this.startTime = startTime;
            this.endTime   = endTime;
        }


        @Override
        public int hashCode () {
            final int prime  = 31;
            int       result = 1;
            result = prime * result + ( ( endDate == null )
                                        ? 0
                                        : endDate.hashCode() );
            result = prime * result + ( ( endTime == null )
                                        ? 0
                                        : endTime.hashCode() );
            result = prime * result + ( ( errorCode == null )
                                        ? 0
                                        : errorCode.hashCode() );
            result = prime * result + ( ( startDate == null )
                                        ? 0
                                        : startDate.hashCode() );
            result = prime * result + ( ( startTime == null )
                                        ? 0
                                        : startTime.hashCode() );
            result = prime * result + ( ( type == null )
                                        ? 0
                                        : type.hashCode() );
            return result;
        }


        @Override
        public boolean equals ( Object obj ) {
            if ( this == obj ) {
                return true;
            }
            if ( obj == null ) {
                return false;
            }
            if ( ! ( obj instanceof LogFilterBean ) ) {
                return false;
            }

            LogFilterBean other = ( LogFilterBean )obj;
            if ( endDate == null ) {
                if ( other.endDate != null ) {
                    return false;
                }
            } else if ( ! endDate.equals( other.endDate ) ) {
                return false;
            }
            if ( endTime == null ) {
                if ( other.endTime != null ) {
                    return false;
                }
            } else if ( ! endTime.equals( other.endTime ) ) {
                return false;
            }
            if ( errorCode == null ) {
                if ( other.errorCode != null ) {
                    return false;
                }
            } else if ( ! errorCode.equals( other.errorCode ) ) {
                return false;
            }
            if ( startDate == null ) {
                if ( other.startDate != null ) {
                    return false;
                }
            } else if ( ! startDate.equals( other.startDate ) ) {
                return false;
            }
            if ( startTime == null ) {
                if ( other.startTime != null ) {
                    return false;
                }
            } else if ( ! startTime.equals( other.startTime ) ) {
                return false;
            }
            if ( type != other.type ) {
                return false;
            }
            return true;
        }


        public TypeOptions getType () {
            return type;
        }


        public Integer getErrorCode () {
            return errorCode;
        }


        public void setType ( TypeOptions type ) {
            this.type = type;
        }


        public void setErrorCode ( Integer errorCode ) {
            this.errorCode = errorCode;
        }


        public Date getStartDate () {
            return startDate;
        }


        public Date getEndDate () {
            return endDate;
        }


        public Date getStartTime () {
            return startTime;
        }


        public Date getEndTime () {
            return endTime;
        }


        public void setStartDate ( Date startDate ) {
            this.startDate = startDate;
        }


        public void setEndDate ( Date endDate ) {
            this.endDate = endDate;
        }


        public void setStartTime ( Date startTime ) {
            this.startTime = startTime;
        }


        public void setEndTime ( Date endTime ) {
            this.endTime = endTime;
        }
    }
}
