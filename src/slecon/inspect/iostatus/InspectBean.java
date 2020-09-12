package slecon.inspect.iostatus;
public class InspectBean {
    private OnOffStatus ins;
    private OnOffStatus insup;
    private OnOffStatus insdown;




    public OnOffStatus getIns () {
        return ins;
    }


    public OnOffStatus getInsup () {
        return insup;
    }


    public OnOffStatus getInsdown () {
        return insdown;
    }


    public void setIns ( OnOffStatus ins ) {
        this.ins = ins;
    }


    public void setInsup ( OnOffStatus insup ) {
        this.insup = insup;
    }


    public void setInsdown ( OnOffStatus insdown ) {
        this.insdown = insdown;
    }
}
