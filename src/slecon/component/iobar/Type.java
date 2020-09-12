package slecon.component.iobar;
public enum Type {
    INPUT, OUTPUT;

    public String toString () {
        return Messages.getString( "Type." + name() );
    }
}
