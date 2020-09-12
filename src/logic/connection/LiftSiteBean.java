package logic.connection;
public class LiftSiteBean implements Comparable<LiftSiteBean>, Cloneable {
    private String name;




    public LiftSiteBean ( String name ) {
        super();
        if ( name == null )
            throw new RuntimeException( "The site name cannot not be null." );
        setName( name );
    }


    @Override
    public int hashCode () {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ( ( name == null )
                                    ? 0
                                    : name.hashCode() );
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
        if ( ! ( obj instanceof LiftSiteBean ) ) {
            return false;
        }

        LiftSiteBean other = ( LiftSiteBean )obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( ! name.equals( other.name ) ) {
            return false;
        }
        return true;
    }


    @Override
    public String toString () {
        return "Site [name=" + name + "]";
    }


    public String getName () {
        return name;
    }


    public void setName ( String name ) {
        this.name = name;
    }


    @Override
    public int compareTo ( LiftSiteBean o ) {
        if ( getName() == null )
            return -1;
        return getName().compareTo( o.getName() );
    }


    public LiftSiteBean clone () {
        try {
            return ( LiftSiteBean )super.clone();
        } catch ( CloneNotSupportedException e ) {
            return new LiftSiteBean( name );
        }
    }
}
