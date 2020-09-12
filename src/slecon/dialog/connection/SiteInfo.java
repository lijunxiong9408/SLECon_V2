package slecon.dialog.connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.connection.LiftConnectionBean;
import logic.connection.LiftSiteBean;
import logic.util.SiteManagement;




public final class SiteInfo {
    public final List<LiftSiteBean>                          sites   = new ArrayList<>();
    public final Map<LiftSiteBean, List<LiftConnectionBean>> siteMap = new HashMap<>();




    @Override
    public int hashCode () {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ( ( siteMap == null )
                                    ? 0
                                    : siteMap.hashCode() );
        result = prime * result + ( ( sites == null )
                                    ? 0
                                    : sites.hashCode() );
        return result;
    }


    @Override
    public boolean equals ( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( ! ( obj instanceof SiteInfo ) ) {
            return false;
        }

        SiteInfo other = ( SiteInfo )obj;
        if ( siteMap == null ) {
            if ( other.siteMap != null ) {
                return false;
            }
        } else if ( ! siteMap.equals( other.siteMap ) ) {
            return false;
        }
        if ( sites == null ) {
            if ( other.sites != null ) {
                return false;
            }
        } else if ( ! sites.equals( other.sites ) ) {
            return false;
        }
        return true;
    }


    public static final SiteInfo fromSiteManagement () {
        SiteInfo siteInfo = new SiteInfo();
        for ( LiftSiteBean site : SiteManagement.getAllSite() ) {
            siteInfo.sites.add( site.clone() );

            ArrayList<LiftConnectionBean> list = new ArrayList<>();
            for ( LiftConnectionBean conn : SiteManagement.getConnectionBySite( site ) ) {
                list.add( conn.clone() );
            }
            siteInfo.siteMap.put( site, list );
        }
        return siteInfo;
    }
}
