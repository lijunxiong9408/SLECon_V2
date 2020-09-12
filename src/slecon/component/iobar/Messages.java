package slecon.component.iobar;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import slecon.ToolBox;




public class Messages {
    private static final String         BUNDLE_NAME     = "logic.gui.IOBar";    // $NON-NLS-1$
    private static final ResourceBundle RESOURCE_BUNDLE = ToolBox.getResourceBundle( BUNDLE_NAME );




    private Messages () {
    }


    public static String getString ( String key ) {
        try {
            return RESOURCE_BUNDLE.getString( key );
        } catch ( MissingResourceException e ) {
            return '!' + key + '!';
        }
    }
}
