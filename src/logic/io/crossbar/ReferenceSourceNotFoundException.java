package logic.io.crossbar;
public class ReferenceSourceNotFoundException extends Exception {
    private static final long serialVersionUID = 4751453255411423161L;




    public ReferenceSourceNotFoundException () {
    }


    public ReferenceSourceNotFoundException ( String name ) {
        super( name );
    }
}
