package slecon.inspect.iostatus;
public class SafetyBean {
    private OnOffStatus dfc;
    private OnOffStatus dw;
    private OnOffStatus rdfc;
    private OnOffStatus rdw;
    private OnOffStatus hes;
    private OnOffStatus ces;
    private OnOffStatus usl;
    private OnOffStatus lsl;
    
    public OnOffStatus getUsl() {
		return usl;
	}


	public void setUsl(OnOffStatus usl) {
		this.usl = usl;
	}


	public OnOffStatus getLsl() {
		return lsl;
	}


	public void setLsl(OnOffStatus lsl) {
		this.lsl = lsl;
	}

    public OnOffStatus getDfc () {
        return dfc;
    }


    public OnOffStatus getDw () {
        return dw;
    }


    public OnOffStatus getHes () {
        return hes;
    }


    public OnOffStatus getCes () {
        return ces;
    }


    public void setDfc ( OnOffStatus dfc ) {
        this.dfc = dfc;
    }


    public void setDw ( OnOffStatus dw ) {
        this.dw = dw;
    }


    public void setHes ( OnOffStatus hes ) {
        this.hes = hes;
    }


    public void setCes ( OnOffStatus ces ) {
        this.ces = ces;
    }


	public OnOffStatus getRdfc() {
		return rdfc;
	}


	public void setRdfc(OnOffStatus rdfc) {
		this.rdfc = rdfc;
	}


	public OnOffStatus getRdw() {
		return rdw;
	}


	public void setRdw(OnOffStatus rdw) {
		this.rdw = rdw;
	}
    
}
