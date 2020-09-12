package slecon.component;
import javax.swing.JPanel;
import javax.swing.Painter;

import logic.connection.LiftConnectionBean;




public interface IPanelBinder {
    public abstract Boolean isRelaceWorkspace ();


    public abstract void onStop ();


    public abstract void onPause ();


    public abstract void onResume ();


    public abstract void onStart ();


    public abstract String getTitle ();


    public abstract Painter<JPanel> getFlag ();


    public abstract void setFlag ( Painter<JPanel> flag );


    public abstract void setConnection ( LiftConnectionBean conn );


    public abstract LiftConnectionBean getConnection ();
}
