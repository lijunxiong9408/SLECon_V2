package logic.util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import logic.connection.LiftConnectionBean;
import logic.connection.LiftSiteBean;
import base.cfg.SiteFactory;
import base.cfg.SiteFactory.Elevator;




/**
 * [THREAD-SAFE]
 *
 * A Site without Connection is not exists.
 *
 * @author henry.tam
 *
 */
class BaseSiteManagement {

    /* Listener */
    private static final Map<SiteChangeListener, LiftConnectionBean> listenerConnection = new LinkedHashMap<>();
    
    /* Data */
    private static List<LiftSiteBean>                sites   = new ArrayList<>();
    private static Map<LiftSiteBean, ConnectionList> connMap = new ConcurrentHashMap<>();

    static {
        importFile();
    }
    
    public synchronized static void importFile() {
        final SiteFactory siteFactory = new SiteFactory();
        siteFactory.load();
        for ( String siteName : siteFactory.getSiteName() ) {
            LiftSiteBean site = new LiftSiteBean(siteName);
            
            if(!sites.contains(site))
                sites.add(site);
            
            for ( Elevator elevator : siteFactory.getElevator( siteName ) ) {
                LiftConnectionBean lift = new LiftConnectionBean(elevator.label, elevator.host, Integer.parseInt(elevator.port), site);
                addLift(lift);
            }
        }
    }
    
    public synchronized static void exportFile() {
        final SiteFactory siteFactory = new SiteFactory();
        for(LiftSiteBean site : getAllSite()) {
            for(LiftConnectionBean lift : getConnectionBySite(site)) {
                siteFactory.addElevator(lift.getSite().getName(), 
                        new Elevator(lift.getName(), lift.getIp(), Integer.toString(lift.getPort())));
            }
        }
        siteFactory.save();
    }
    
    public static synchronized void addLift(LiftConnectionBean lift) {
        LiftSiteBean site = lift.getSite();
        if (!sites.contains(site))
            sites.add(site);

        ConnectionList list = connMap.get( site );
        if(list==null)
            connMap.put(site, list=new ConnectionList());
        
        if (!connMap.containsKey(lift)) {
            list.add(lift);
            fireAddEvent(lift);
        }
    }
    

    private static void fireRemoveEvent ( LiftConnectionBean lift ) {
        ArrayList<SiteChangeListener> listeners = new ArrayList<>();
        synchronized ( listenerConnection ) {
            for ( SiteChangeListener listener : listenerConnection.keySet() ) {
                if ( listenerConnection.get( listener ) == null || lift.equals( listenerConnection.get( listener ) ) )
                    listeners.add( listener );
            }
        }
        for ( SiteChangeListener l : listeners )
            l.siteRemove( lift );
    }

    public static synchronized boolean removeLift(LiftConnectionBean lift) {
        LiftSiteBean site = lift.getSite();
        if (sites.contains(site)) {
            ConnectionList list = connMap.get( site );
            if(list!=null) {
                list.remove(lift);
                fireRemoveEvent(lift);
                
                if(list.size()==0) {
                    sites.remove(site);
                    connMap.remove(site);
                }
                return true;
            }
        }
        return false;
    }
    

    private static void fireAddEvent ( LiftConnectionBean lift ) {
        ArrayList<SiteChangeListener> listeners = new ArrayList<>();
        synchronized ( listenerConnection ) {
            for ( SiteChangeListener listener : listenerConnection.keySet() ) {
                if ( listenerConnection.get( listener ) == null || lift.equals( listenerConnection.get( listener ) ) )
                    listeners.add( listener );
            }
        }
        for ( SiteChangeListener l : listeners )
            l.siteAdd( lift );
    }
    
    public static final void addSiteChangeListener( SiteChangeListener listener, LiftConnectionBean lift ) {
        synchronized ( listenerConnection ) {
            listenerConnection.put(listener, lift);
        }
    }
    
    public static final void removeSiteChangeListener( SiteChangeListener listener ) {
        synchronized ( listenerConnection ) {
            listenerConnection.remove(listener);
        }
    }
    


    /**
     * Get all site.
     * <p>
     * the order is followed by the configure file.
     *
     * @param site
     * @return
     */
    public static List<LiftSiteBean> getAllSite () {
        ArrayList<LiftSiteBean> result = new ArrayList<LiftSiteBean>();
        for ( LiftSiteBean conn : sites )
            result.add( conn.clone() );
        return result;
    }


    /**
     * Get the connection which the site is given.
     * <p>
     * the order is followed by the configure file.
     *
     * @param site
     * @return
     */
    public static final List<LiftConnectionBean> getConnectionBySite ( LiftSiteBean site ) {
        ArrayList<LiftConnectionBean> result = new ArrayList<LiftConnectionBean>();
        ConnectionList                list   = connMap.get( site );
        if ( list != null ) {
            synchronized ( list.mutex ) {
                for ( LiftConnectionBean conn : list.ctx )
                    result.add( conn.clone() );
            }
        }
        return result;
    }


    /**
     * Get all connection, no same lift.
     * <p>
     * order by site, name, ip, port.
     *
     * @param site
     * @return
     */
    public synchronized static final List<LiftConnectionBean> getAllConnection () {
        List<LiftConnectionBean> result = new ArrayList<>();
        for ( LiftSiteBean s : sites ) {
            ConnectionList list = connMap.get( s );
            if ( list != null )
                synchronized ( list.mutex ) {
                    for ( LiftConnectionBean c : list.ctx ) {
                        result.add( c.clone() );
                    }
                }
        }
        return result;
    }


    private static class ConnectionList implements Iterable<LiftConnectionBean> {
        private final Object                   mutex = new Object();
        private final List<LiftConnectionBean> ctx   = new CopyOnWriteArrayList<>();
        
        public int size() {
            return ctx.size();
        }

        public Iterator<LiftConnectionBean> iterator() {
            synchronized (mutex) {
                return ctx.iterator();
            }
        }
        
        public void add(LiftConnectionBean lift) {
            synchronized (mutex) {
                ctx.add(lift);
            }
        }
        
        public boolean remove(LiftConnectionBean lift) {
            synchronized (mutex) {
                return ctx.remove(lift);
            }
        }
    }
}
