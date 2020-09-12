package slecon.interfaces;
import slecon.component.Workspace;




/**
 * @author superb8
 *
 * this interface is binding to PanelBinder.
 * <blockquote>{@link slecon.component.PanelBinder};
 * </blockquote>
 */
public interface Page {

    /**
     * when the bean is created, the method would be called.<br/>
     */
    public void onCreate ( Workspace pageBinder ) throws Exception;


    public void onStart () throws Exception;


    public void onResume () throws Exception;


    public void onPause () throws Exception;


    public void onStop () throws Exception;
    
    public void onDestroy() throws Exception;

}
