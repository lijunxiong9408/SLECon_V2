package slecon.component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import slecon.interfaces.Page;




public class Workspace {
    private static final Logger   logger            = LogManager.getLogger( SubPanelBinder.class );
    public static final Workspace EMPTY_PAGE        = new Workspace( new JPanel(), null );
    private EventListenerList     eventListenerList = new EventListenerList();
    private PropertyChangeSupport listeners         = new PropertyChangeSupport( this );
    private IPanelBinder          binder;
    private JPanel                mainPanel;
    private String                title;
    private Page                  page;    // TODO final?
    private State                 state;




    private enum State { INIT, WORKING, PAUSE, DEAD, }




    public Workspace ( JPanel mainPanel, Page page ) {
        this.mainPanel = mainPanel;
        this.page      = page;
    }


    public void addRestartListener ( RestartListener l ) {
        eventListenerList.add( RestartListener.class, l );
    }


    public void removeRestartListener ( RestartListener l ) {
        eventListenerList.remove( RestartListener.class, l );
    }


    protected void fireRestartListener () {
        Object[] listeners = eventListenerList.getListenerList();
        for ( int i = listeners.length - 2 ; i >= 0 ; i -= 2 ) {
            if ( listeners[ i ] == RestartListener.class ) {
                ( ( RestartListener )listeners[ i + 1 ] ).restartPerformed( this );
            }
        }
    }


    public final JPanel getMainPanel () {
        return mainPanel;
    }


    public final String getTitle () {
        return title;
    }


    public final Page getPage () {
        return page;
    }


    public final IPanelBinder getBinder () {
        return binder;
    }


    public final void setMainPanel ( JPanel mainPanel ) {
        JPanel oldMainPanel = this.mainPanel;
        this.mainPanel = mainPanel;
        firePropertyChange( "mainPanel", oldMainPanel, mainPanel );
    }


    public final void setTitle ( String title ) {
        String oldTitle = this.title;
        this.title = title;
        firePropertyChange( "title", oldTitle, title );
    }


    public final void restart () {
        fireRestartListener();
    }


    @Deprecated
    public final void setPage ( Page page ) {
        Page oldPage = this.page;
        this.page = page;
        firePropertyChange( "page", oldPage, page );
    }


    protected final void hosted ( IPanelBinder binder ) {
        if ( state == State.INIT ) {
            this.binder = binder;
        }
    }


    protected final void invokeCreate () {
        if ( page != null ) {
            try {
                if ( state != State.DEAD ) {
                    page.onCreate( this );
                }
            } catch ( Exception e ) {
                logger.error( "Page Operation Exception", e );
            }
        }
        state = State.INIT;
    }


    protected final void invokeResume () {
        if ( page != null ) {
            try {
                if ( state != State.DEAD /* && state!=state.WORKING */ ) {
                    page.onResume();
                }
            } catch ( Exception e ) {
                logger.error( "Page Operation Exception", e );
            }
        }
        state = State.WORKING;
    }


    protected final void invokeStart () {
        if ( page != null ) {
            try {
                if ( state == State.INIT ) {
                    page.onStart();
                }
            } catch ( Exception e ) {
                logger.error( "Page Operation Exception", e );
            }
        }
        state = State.WORKING;
    }


    protected final void invokeStop () {
        if ( page != null ) {
            try {
                if ( state != State.DEAD ) {
                    page.onStop();
                }
            } catch ( Exception e ) {
                logger.error( "Page Operation Exception", e );
            }
        }
        state = State.DEAD;
        for ( PropertyChangeListener l : listeners.getPropertyChangeListeners() ) {
            removePropertyChangeListener( l );
        }
    }


    protected final void invokePause () {
        if ( page != null ) {
            try {
                if ( state == State.WORKING ) {
                    page.onPause();
                }
            } catch ( Exception e ) {
                logger.error( "Page Operation Exception", e );
            }
        }
        state = State.PAUSE;
    }


    public void addPropertyChangeListener ( PropertyChangeListener listener ) {
        listeners.addPropertyChangeListener( listener );
    }


    public void addPropertyChangeListener ( String name, PropertyChangeListener listener ) {
        listeners.addPropertyChangeListener( name, listener );
    }


    public void removePropertyChangeListener ( PropertyChangeListener listener ) {
        listeners.addPropertyChangeListener( listener );
    }


    public void removePropertyChangeListener ( String name, PropertyChangeListener listener ) {
        listeners.removePropertyChangeListener( name, listener );
    }


    protected void firePropertyChange ( String prop, Object oldValue, Object newValue ) {
        listeners.firePropertyChange( prop, oldValue, newValue );
    }
}
