package slecon.inspect.iostatus;
public class SystemBean {
    private OnOffStatus ado;
    private OnOffStatus v24mon;
    private OnOffStatus encf;
    private OnOffStatus thm;
    private OnOffStatus ccf;
    private OnOffStatus dlb;
    private OnOffStatus ucmp;
    
    public OnOffStatus getAdo () {
        return ado;
    }

    public OnOffStatus getV24mon () {
        return v24mon;
    }


    public OnOffStatus getEncf () {
        return encf;
    }


    public OnOffStatus getThm () {
        return thm;
    }


    public OnOffStatus getCcf() {
		return ccf;
	}


	public OnOffStatus getDlb() {
		return dlb;
	}


	public OnOffStatus getUcmp() {
		return ucmp;
	}


	public void setAdo ( OnOffStatus ado ) {
        this.ado = ado;
    }


    public void setV24mon ( OnOffStatus v24mon ) {
        this.v24mon = v24mon;
    }


    public void setEncf ( OnOffStatus encf ) {
        this.encf = encf;
    }


    public void setThm ( OnOffStatus thm ) {
        this.thm = thm;
    }


	public void setCcf(OnOffStatus ccf) {
		this.ccf = ccf;
	}


	public void setDlb(OnOffStatus dlb) {
		this.dlb = dlb;
	}


	public void setUcmp(OnOffStatus ucmp) {
		this.ucmp = ucmp;
	}
    
}
