package slecon.interfaces;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface InspectView {
    public int sortIndex ();

    /* it would lookup the Dict.properties to build the parent text */
    public String path ();

    /* TODO no use */
    public String[] keywords () default {
    };
    public String condition ()  default "true";
}
