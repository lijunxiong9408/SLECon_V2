package slecon.inspect.iostatus;
public class InspectBean {
    private OnOffStatus ins;
    private OnOffStatus insup;
    private OnOffStatus insdown;
    private OnOffStatus install;
    private OnOffStatus installup;
    private OnOffStatus installdown;
    private OnOffStatus inslimitup;
    private OnOffStatus inslimitdown;


    public OnOffStatus getIns () {
        return ins;
    }


    public OnOffStatus getInsup () {
        return insup;
    }


    public OnOffStatus getInsdown () {
        return insdown;
    }
    

    public OnOffStatus getInstall() {
		return install;
	}


	public OnOffStatus getInstallup() {
		return installup;
	}


	public OnOffStatus getInstalldown() {
		return installdown;
	}


	public OnOffStatus getInslimitup() {
		return inslimitup;
	}


	public OnOffStatus getInslimitdown() {
		return inslimitdown;
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


	public void setInstall(OnOffStatus install) {
		this.install = install;
	}


	public void setInstallup(OnOffStatus installup) {
		this.installup = installup;
	}


	public void setInstalldown(OnOffStatus installdown) {
		this.installdown = installdown;
	}


	public void setInslimitup(OnOffStatus inslimitup) {
		this.inslimitup = inslimitup;
	}


	public void setInslimitdown(OnOffStatus inslimitdown) {
		this.inslimitdown = inslimitdown;
	}
    
}
