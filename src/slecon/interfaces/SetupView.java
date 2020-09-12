package slecon.interfaces;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface SetupView {
    public int sortIndex ();
    public String path ();
    public String[] keywords () default {
    };
    public String condition ()  default "true";
}
