package slecon.interfaces;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import base.cfg.ImageFactory;




@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface HomeView {
    public int sortIndex ();

    /* lookup Dict.properties */
    public String name ();
    public ImageFactory icon () default ImageFactory.NONE;
    public String condition ()  default "true";
}
