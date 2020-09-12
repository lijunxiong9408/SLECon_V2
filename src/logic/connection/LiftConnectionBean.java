package logic.connection;
public class LiftConnectionBean implements Comparable<LiftConnectionBean>, Cloneable {
    private String       name;
    private String       ip;
    private int          port;
    private LiftSiteBean site;




    public LiftConnectionBean ( String name, String ip, int port, LiftSiteBean site ) {
        super();
        this.name = name;
        this.ip   = ip;
        this.port = port;
        this.site = site;
    }


    public String getName () {
        return name;
    }


    public void setName ( String name ) {
        this.name = name;
    }


    public String getIp () {
        return ip;
    }


    public void setIp ( String ip ) {
        this.ip = ip;
    }


    public int getPort () {
        return port;
    }


    public void setPort ( int port ) {
        this.port = port;
    }


    public LiftSiteBean getSite () {
        return site;
    }


    public void setSite ( LiftSiteBean site ) {
        this.site = site;
    }


    @Override
    public String toString () {
        return "LiftConnectionBean [site=" + site + ", name=" + name + ", ip=" + ip + ", port=" + port + "]";
    }


    @Override
    public int hashCode () {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ( ( ip == null )
                                    ? 0
                                    : ip.hashCode() );
        result = prime * result + port;
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
        if ( ! ( obj instanceof LiftConnectionBean ) ) {
            return false;
        }

        LiftConnectionBean other = ( LiftConnectionBean )obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( ! name.equals( other.name ) ) {
            return false;
        }
        if ( ip == null ) {
            if ( other.ip != null ) {
                return false;
            }
        } else if ( ! ip.equals( other.ip ) ) {
            return false;
        }
        if ( port != other.port ) {
            return false;
        }
        return true;
    }


    @Override
    public int compareTo ( LiftConnectionBean o ) {
        if ( getSite() != null ) {
            int result = getSite().compareTo( o.getSite() );
            if ( result != 0 )
                return result;
        }
        if ( getName() != null ) {
            int result = getName().compareTo( o.getName() );
            if ( result != 0 )
                return result;
        }
        if ( getIp() != null ) {
            int result = new Integer( getPort() ).compareTo( o.getPort() );
            if ( result != 0 )
                return result;
            return getIp().compareTo( o.getIp() );
        }
        return -1;
    }


    public LiftConnectionBean clone () {
        try {
            return ( LiftConnectionBean )super.clone();
        } catch ( CloneNotSupportedException e ) {
            return new LiftConnectionBean( name, ip, port, site );
        }
    }


    public boolean isLocal () {
        return isLocal( this );
    }


    public static boolean isLocal ( LiftConnectionBean bean ) {
        try {
            return bean.getIp().trim().startsWith( "192.168." );
        } catch ( Exception e ) {
            return false;
        }
    }
}
