package slecon.setting.modules;
public class FloorText {
    final byte    index;
    final String  displayText;
    final int     dzIndex;
    final boolean hasDZ;




    public FloorText ( byte index, int dzIndex, boolean hasDZ, String displayText ) {
        super();
        this.index       = index;
        this.dzIndex     = dzIndex;
        this.hasDZ       = hasDZ;
        this.displayText = displayText;
    }


    @Override
    public int hashCode () {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + index;
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
        if ( ! ( obj instanceof FloorText ) ) {
            return false;
        }

        FloorText other = ( FloorText )obj;
        if ( index != other.index ) {
            return false;
        }
        return true;
    }


    public byte getFloor () {
        return index;
    }


    public String getDisplayText () {
        return displayText;
    }


    @Override
    public String toString () {
        return displayText;
    }
}
