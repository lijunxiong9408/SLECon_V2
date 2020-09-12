package logic.util;

import java.util.EventListener;

import logic.connection.LiftConnectionBean;

public interface SiteChangeListener extends EventListener {

    public void siteAdd ( LiftConnectionBean conns );

    public void siteRemove ( LiftConnectionBean conns );
}