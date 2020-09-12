package logic.util;

import java.util.EventListener;

import logic.connection.LiftConnectionBean;

public interface VersionChangeListener extends EventListener {
    
    
    /**
     * if newVersion is null, the connection will be lost; otherwise it is the new version the elevator return. 
     * @param conn
     * @param newVersion
     */
    public void versionChanged ( LiftConnectionBean connBean, Version newVersion );
}