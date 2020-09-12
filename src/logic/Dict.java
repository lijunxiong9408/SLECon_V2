package logic;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import slecon.ToolBox;




public class Dict {
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "Dict" );



    
    public static String lookup ( String word ) {
        return lookup( word, true );
    }


    public static String lookup ( String word, boolean notnull ) {
        String value = null;
        try {
            value = TEXT.getString( word );
        } catch ( MissingResourceException e ) {
        }
        if ( notnull )
            return value == null
                   ? word
                   : value;
        else
            return value == null
                   ? null
                   : value;
    }
}
