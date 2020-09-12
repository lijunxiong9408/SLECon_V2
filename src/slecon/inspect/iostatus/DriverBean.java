package slecon.inspect.iostatus;
public class DriverBean {
    private OnOffStatus drven;
    private OnOffStatus drvfwd;
    private OnOffStatus drvrev;
    private OnOffStatus drvmltsp0;
    private OnOffStatus drvmltsp1;
    private OnOffStatus drvmltsp2;
    private OnOffStatus drvenf;
    private OnOffStatus drvbm;
    private OnOffStatus drvok;
    private OnOffStatus drvepb;

    public OnOffStatus getDrven () {
        return drven;
    }


    public OnOffStatus getDrvfwd () {
        return drvfwd;
    }


    public OnOffStatus getDrvrev () {
        return drvrev;
    }


    public OnOffStatus getDrvmltsp0 () {
        return drvmltsp0;
    }


    public OnOffStatus getDrvmltsp1 () {
        return drvmltsp1;
    }


    public OnOffStatus getDrvmltsp2 () {
        return drvmltsp2;
    }


    public OnOffStatus getDrvenf () {
        return drvenf;
    }


    public OnOffStatus getDrvbm () {
        return drvbm;
    }


    public OnOffStatus getDrvok () {
        return drvok;
    }


    public OnOffStatus getDrvepb() {
		return drvepb;
	}


	public void setDrven ( OnOffStatus drven ) {
        this.drven = drven;
    }


    public void setDrvfwd ( OnOffStatus drvfwd ) {
        this.drvfwd = drvfwd;
    }


    public void setDrvrev ( OnOffStatus drvrev ) {
        this.drvrev = drvrev;
    }


    public void setDrvmltsp0 ( OnOffStatus drvmltsp0 ) {
        this.drvmltsp0 = drvmltsp0;
    }


    public void setDrvmltsp1 ( OnOffStatus drvmltsp1 ) {
        this.drvmltsp1 = drvmltsp1;
    }


    public void setDrvmltsp2 ( OnOffStatus drvmltsp2 ) {
        this.drvmltsp2 = drvmltsp2;
    }


    public void setDrvenf ( OnOffStatus drvenf ) {
        this.drvenf = drvenf;
    }


    public void setDrvbm ( OnOffStatus drvbm ) {
        this.drvbm = drvbm;
    }


    public void setDrvok ( OnOffStatus drvok ) {
        this.drvok = drvok;
    }


	public void setDrvepb(OnOffStatus drvepb) {
		this.drvepb = drvepb;
	}
    
}
