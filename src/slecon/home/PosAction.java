package slecon.home;
import javax.swing.AbstractAction;
import javax.swing.Icon;




public abstract class PosAction extends AbstractAction {
    private static final long  serialVersionUID = 5471815220979092294L;
    public static final String ICONS            = "ICONS";




    public PosAction ( String text, Icon[] icons ) {
        super( text );
        putValue( ICONS, icons );
    }
}
