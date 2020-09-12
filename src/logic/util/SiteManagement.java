package logic.util;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import logic.connection.LiftConnectionBean;
import logic.connection.LiftSiteBean;

import comm.MonitorMgr;
import comm.Parser_Auth;
import comm.Parser_McsConfig;
import comm.Parser_OcsConfig;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




/**
 * [THREAD-SAFE]
 *
 * A Site without Connection is not exists.
 *
 * @author henry.tam
 *
 */
public class SiteManagement extends BaseSiteManagement {

    private final static ConcurrentHashMap<LiftConnectionBean, Parser_Auth> authMap         = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<LiftConnectionBean, Listener>    chgListenerMap  = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<LiftConnectionBean, Version>     versionMap      = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<LiftConnectionBean, Boolean>     aliveMap        = new ConcurrentHashMap<>();

    /* Listener */
    public static final Map<VersionChangeListener, LiftConnectionBean>      versionListener = new LinkedHashMap<>();

    /**
     * The instance of MonitorMgr.
     */
    public static final MonitorMgr                                          MON_MGR         = new MonitorMgr();


    public static void initMonitorMgr () {
        for ( LiftSiteBean site : getAllSite() ) {
            for ( LiftConnectionBean lift : getConnectionBySite( site ) ) {
                aliveMap.put( lift, false );
                MON_MGR.addAgent( lift.getIp(), lift.getPort() );
                final Listener listener = new Listener( lift );
                if ( MON_MGR.addEventListener( listener, lift.getIp(), lift.getPort(), 0xFFFFFFFF
                // AgentMessage.OCS_CONFIG.getCode() |
                // AgentMessage.MCS_CONFIG.getCode() |
                // AgentMessage.STATUS.getCode()
                        ) ) {

                    chgListenerMap.put( lift, listener );
                    authMap.put( lift, new Parser_Auth( lift.getIp(), lift.getPort() ) );

                    try {
                        Thread.sleep( 80 );
                    } catch ( InterruptedException e ) {
                    }
                    
                    listener.updateVersion();

                }
            }
        }

        addSiteChangeListener( new SiteChangeListener() {

            @Override
            public void siteRemove ( LiftConnectionBean conns ) {
                if ( chgListenerMap.containsKey( conns ) )
                    MON_MGR.removeEventListener( chgListenerMap.remove( conns ) );
                if ( authMap.containsKey( conns ) )
                    authMap.remove( conns );
            }


            @Override
            public void siteAdd ( LiftConnectionBean lift ) {
                aliveMap.put( lift, false );
                MON_MGR.addAgent( lift.getIp(), lift.getPort() );
                final Listener listener = new Listener( lift );
                if ( MON_MGR.addEventListener( listener, lift.getIp(), lift.getPort(), 0xFFFFFFFF
                // AgentMessage.OCS_CONFIG.getCode() |
                // AgentMessage.MCS_CONFIG.getCode() |
                // AgentMessage.STATUS.getCode()
                        ) ) {

                    chgListenerMap.put( lift, listener );
                    authMap.put( lift, new Parser_Auth( lift.getIp(), lift.getPort() ) );
                    listener.updateVersion();
                }
            }

        }, null );
    }
    
    private static class Listener implements LiftDataChangedListener {

        final LiftConnectionBean connBean;
        final Parser_McsConfig   mcsconfig;
        final Parser_OcsConfig   ocsconfig;


        private Listener ( LiftConnectionBean connBean ) {
            this.connBean = connBean;
            mcsconfig = new Parser_McsConfig( connBean.getIp(), connBean.getPort() );
            ocsconfig = new Parser_OcsConfig( connBean.getIp(), connBean.getPort() );
        }


        private void updateVersion () {
            String controlCoreVersion = ocsconfig.getVersion();
            String guiVersion = Integer.toString( ocsconfig.getProtocolVersion() );
            String boardVersion = mcsconfig.getBoardVersion();
            String contractNumber = mcsconfig.getContractNumber();
            String firmwareVersion = mcsconfig.getFirmwareVersion();
            String serialNumber = mcsconfig.getSerialNumber();

            final Version newVersion = new Version( controlCoreVersion, guiVersion, contractNumber, firmwareVersion, boardVersion, serialNumber );
            final Version oldVersion = versionMap.put( connBean, newVersion );

            if ( oldVersion == null && newVersion != null || ! oldVersion.equals( newVersion ) ) {
                fireVersionChange( connBean, newVersion );
            }
        }


        @Override
        public void onDataChanged ( long timestamp, int msg ) {
            aliveMap.put( connBean, true );
            if ( msg == AgentMessage.OCS_CONFIG.getCode() || msg == AgentMessage.MCS_CONFIG.getCode() )
                updateVersion();
        }


        @Override
        public void onConnLost () {
            aliveMap.put( connBean, false );
            versionMap.remove( connBean );
            fireVersionChange( connBean, null );
        }


        @Override
        public void onConnCreate () {
            aliveMap.put( connBean, true );
            updateVersion();
        }
    }


    public static boolean isAlive ( LiftConnectionBean lift ) {
        Boolean result = aliveMap.get( lift );
        return result != null ? result : false;
    }
    

    public static Version getVersion ( LiftConnectionBean lift ) {
        if ( lift != null ) {
            Version result = versionMap.get( lift );
            return result != null ? result : null;
        }
        return null;
    }
    

    protected static void fireVersionChange ( LiftConnectionBean connBean, Version newVersion ) {
        ArrayList<VersionChangeListener> listeners = new ArrayList<>();
        synchronized ( versionListener ) {
            for ( VersionChangeListener listener : versionListener.keySet() ) {
                if ( versionListener.get( listener ) == null || connBean.equals( versionListener.get( listener ) ) )
                    listeners.add( listener );
            }
        }
        for ( VersionChangeListener l : listeners )
            l.versionChanged( connBean, newVersion );
    }


    public static final void addVersionChangeListener ( VersionChangeListener listener, LiftConnectionBean lift ) {
        synchronized ( versionListener ) {
            versionListener.put( listener, lift );
        }
    }
    

    public static final void removeVersionChangeListener ( VersionChangeListener listener ) {
        synchronized ( versionListener ) {
            versionListener.remove( listener );
        }
    }
    

    public static final void auth ( LiftConnectionBean connBean, String username, String password ) {
        Parser_Auth auth = authMap.get( connBean );
        auth.auth( username, password );
    }
    

    public static final Role[] getRoles ( LiftConnectionBean connBean ) {
        final Parser_Auth auth = authMap.get( connBean );
        return auth == null ? new Role[] {} : auth.getRoles();
    }
    
}
