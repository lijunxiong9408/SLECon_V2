package slecon.inspect.iostatus;
import logic.io.crossbar.CrossBar;
import slecon.component.Workspace;




public interface IIOStatus {
    public abstract SystemBean getSystem ();


    public abstract ControlBean getControl ();
  

    public abstract InspectBean getInspect ();


    public abstract DriverBean getDriver ();


    public abstract SafetyBean getSafety ();


    public abstract CabinBean getCabin ();


    public abstract void setSystem ( SystemBean system );


    public abstract void setControl ( ControlBean control );


    public abstract void setInspect ( InspectBean inspect );


    public abstract void setDriver ( DriverBean driver );


    public abstract void setSafety ( SafetyBean safety );


    public abstract void setCabin ( CabinBean cabin );


    public abstract void setToolTip ( CrossBar crossbar );


    public abstract Workspace getWorkspace ();
}
