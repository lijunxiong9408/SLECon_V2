package comm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import comm.agent.AgentMessage;
import comm.event.LiftDataChangedListener;


public class MonitorMgr extends Thread {
    /**
     * Available {@code Agent}.
     */
    private final HashMap<String, Agent> AGENT = new LinkedHashMap<String, Agent>();

    /**
     * Available {@code LiftDataChangedListener}.
     */
    public final ConcurrentHashMap<LiftDataChangedListener, listenerNeed> listeners = new ConcurrentHashMap<LiftDataChangedListener,
                                                                                                             listenerNeed>();

    /**
     * Object for mutual exclusion lock.
     */
    private final Object mutex = new Object();

    /**
     * Total number of {@code AgentMessage} received.
     */
    private volatile long version = 0;

    /**
     * Pending {@code AgentMessage}.
     */
    private final ArrayList<AgentNoti> agentNoti = new ArrayList<AgentNoti>();

    /**
     * Available status of {@code Agent}.
     */
    public static enum AgentState { DATA_UPDATE, COMM_LOST, COMM_CONNECTED };
  
    
    public MonitorMgr () {
        setName( "Lift-UPDATE" );
        start();
    }


    /**
     * Callback method from {@code Agent}.
     * Once the {@code Agent} has any update, it call this method to notify {@code LiftDataChangedListener}.
     * @param agent     It specifies the instance of {@code Agent}.
     * @param state     It specifies a {@code AgentState}.
     * @param msg       It specifies a {@code AgentMessage}.
     */
    void AgentEvent ( Agent agent, AgentState state, AgentMessage msg ) {
        addNoti( new AgentNoti( agent, state, System.nanoTime(), msg ) );
        synchronized ( this.mutex ) {
            this.version += 1;
            this.mutex.notify();
        }
    }
    
    
    /**
     * Get the instance of {@code Agent}.
     * @param hostname  It specifies the host name.
     * @param port      It specifies the host port.
     * @return Returns the instance of {@code Agent} found; otherwise, it returns {@code null}.
     */
    Agent getAgent ( String hostname, int port ) {
        return this.AGENT.get( hostname + port );
    }


    /**
     * Add a {@code Agent} to Monitor Manager.
     * @param hostname  It specifies the host name.
     * @param port      It specifies the host port.
     */
    public void addAgent ( String hostname, int port ) {
        String key = hostname + port;
        if ( this.AGENT.containsKey( hostname + port ) == false ) {
            Agent agent = new Agent( hostname, port, this );
            this.AGENT.put( key, agent );
            agent.start();
        }
    }


    /**
     * Remove all {@code Agent}.
     * deprecated since agent dead is not support.
     */
    @Deprecated
    public void clear () {
        for ( Agent agent : this.AGENT.values() )
            agent.interrupt();
        this.AGENT.clear();
    }


    /**
     * Add an event listener on the {@code Agent}.
     * @param listener      It specifies the instance of {@code LiftDataChangedListener}
     * @param hostname      It specifies the host name.
     * @param port          It specifies the host port.
     * @param msg           It specifies the bit mask consists of {@code AgentMessage}.
     * @return Returns {@code true} on success; otherwise,
     *         returns {@code false} once duplicated {@code LiftDataChangedListener} added.
     */
    public boolean addEventListener ( LiftDataChangedListener listener, String hostname, int port, int msg ) {
        if ( this.listeners.containsKey( listener ) )
            return false;
        this.listeners.put( listener, new listenerNeed( hostname, port, msg ) );
        return true;
    }


    /**
     * Remove an event listener.
     * @param listener      It specifies the instance of {@code LiftDataChangedListener}
     */
    public void removeEventListener ( LiftDataChangedListener listener ) {
        this.listeners.remove( listener );
    }
    

    private void addNoti ( AgentNoti noti ) {
        synchronized ( agentNoti ) {
            if ( noti.state == AgentState.DATA_UPDATE ) {
                ArrayList<AgentNoti> removeNoti = new ArrayList<>();
                for ( int i = agentNoti.size() - 1; i >= 0; i-- ) {
                    AgentNoti n = agentNoti.get( i );
                    if ( n.agent == noti.agent ) {
                        if ( n.state == AgentState.COMM_CONNECTED || n.state == AgentState.COMM_LOST ) {
                            break;
                        }
                        if ( n.state == noti.state && n.msg == noti.msg )
                            removeNoti.add( n );
                         
                    }
                }
                agentNoti.removeAll( removeNoti );
            }
            this.agentNoti.add( noti );
        }
    }


    private boolean isNotiEmpty () {
        synchronized ( this.agentNoti ) {
            return this.agentNoti.isEmpty();
        }
    }
    

    private AgentNoti removeNoti () {
        synchronized ( this.agentNoti ) {
            return this.agentNoti.remove( 0 );
        }
    }


    public void run () {
        long currVersion = this.version;
        while ( true ) {
            synchronized ( this.mutex ) {
                try {
                    while ( currVersion >= this.version )
                        this.mutex.wait();
                } catch ( InterruptedException e ) {
                }
                currVersion = this.version;
            }
            while ( ! isNotiEmpty() ) {
                final AgentNoti message = removeNoti();
                for ( Entry<LiftDataChangedListener, listenerNeed> pair : this.listeners.entrySet() ) {
                    final LiftDataChangedListener listener = pair.getKey();
                    final listenerNeed subject = pair.getValue();
                    if ( subject.hostname.equals( message.agent.hostname ) && subject.port == message.agent.port ) {
                        switch ( message.state ) {
	                        case COMM_LOST :
	                            listener.onConnLost();
	                            break;
	                        case COMM_CONNECTED :
	                            listener.onConnCreate();
	                            break;
	                        case DATA_UPDATE :{
	                        	int code = message.msg.getCode();
	                            if ( ( subject.msg & code) != 0 ) {
	                            	listener.onDataChanged( message.timestamp, code );
	                            }
	                        }break;
                        }
                    }
                }
            }
        }
    }

    private static class AgentNoti {
        /**
         * The timestamp of notification. using {@System.nanoTime()};
         */
        final long timestamp;
        
        /**
         * The instance of {@code Agent}.
         */
        final Agent agent;

        /**
         * The instance of {@code STATE}.
         */
        final AgentState state;

        /**
         * It specifies a {@code AgentMessage}.
         */
        final AgentMessage msg;




        /**
         * This class specifies the notification object to an {@code Agent}.
         * @param agent It specifies the instance of {@code Agent}.
         * @param state It specifies the instance of {@code AgentState}.
         * @param msg   It specifies one of {@code AgentMessage}.
         */
        public AgentNoti ( Agent agent, AgentState state, long timestamp, AgentMessage msg ) {
            this.agent = agent;
            this.state = state;
            this.timestamp = timestamp;
            this.msg   = msg;
        }
    }



    public final static class listenerNeed {
        /**
         * The host name.
         */
        public final String hostname;

        /**
         * The host port.
         */
        public final int port;

        /**
         * The bit mask of {@code AgentMessage}.
         */
        public final int msg;




        /**
         * This class specifies {@code AgentMessage} a listener needed.
         * @param hostname  It specifies the host name.
         * @param port      It specifies the host port.
         * @param msg       It specifies the bit mask consists of {@code AgentMessage}.
         */
        public listenerNeed ( String hostname, int port, int msg ) {
            this.hostname = hostname;
            this.port     = port;
            this.msg      = msg;
        }
    }
}
