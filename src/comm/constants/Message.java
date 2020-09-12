package comm.constants;

import java.util.ResourceBundle;

import slecon.ToolBox;

public class Message {
    public static ResourceBundle TEXT = ToolBox.getResourceBundle("logic.Constant");
    
    public static String getString(String key) {
        return TEXT.getString(key);
    }
} 
