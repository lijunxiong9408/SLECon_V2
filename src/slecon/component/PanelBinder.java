package slecon.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Painter;
import javax.swing.SwingUtilities;

import logic.connection.LiftConnectionBean;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.interfaces.Page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class PanelBinder extends JPanel implements IPanelBinder {
    private static final long       serialVersionUID = - 3057334404532783120L;
    private static final Logger     logger           = LogManager.getLogger( PanelBinder.class );

    /**
     * if panel is null, then use default page. otherwise use the specific class to be the workspace.
     */
    private Class<? extends JPanel> mainClass        = null;
    private Painter<JPanel>         flag;
    protected JPanel                container;
    protected JPanel                mainPanel;
    private Workspace               workspace;
    private LiftConnectionBean      conn;


    public PanelBinder () {
        super();
    }


    public PanelBinder ( boolean isDoubleBuffered ) {
        super( isDoubleBuffered );
    }


    public PanelBinder ( LayoutManager layout ) {
        super( layout );
    }


    public PanelBinder ( LayoutManager layout, boolean isDoubleBuffered ) {
        super( layout, isDoubleBuffered );
    }


    /*
     * (non-Javadoc)
     * 
     * @see slecon.component.IPanelBinder#isRelaceWorkspace()
     */
    @Override
    public Boolean isRelaceWorkspace () {
        return true;
    }


    /*
     * (non-Javadoc)
     * 
     * @see slecon.component.IPanelBinder#onStop()
     */
    @Override
    public abstract void onStop ();


    /*
     * (non-Javadoc)
     * 
     * @see slecon.component.IPanelBinder#onPause()
     */
    @Override
    public abstract void onPause ();


    /*
     * (non-Javadoc)
     * 
     * @see slecon.component.IPanelBinder#onResume()
     */
    @Override
    public abstract void onResume ();


    /*
     * (non-Javadoc)
     * 
     * @see slecon.component.IPanelBinder#onStart()
     */
    @Override
    public abstract void onStart ();


    protected JPanel getMainPanelContainer () {
        container = new JPanel();
        container.setLayout( new BorderLayout() );
        container.setBackground( StartUI.SUB_BACKGROUND_COLOR );
        return container;
    }


    public JPanel getMainPanel () {
        return mainPanel;
    }


    public Workspace getWorkspace () {
        return workspace;
    }


    protected void setWorkspace ( Workspace workspace ) {
        Workspace ws = getWorkspace();
        hosted( workspace );
        if ( ws != workspace ) {
            invokeStop();
            this.workspace = workspace;
            setMainPanel( getWorkspace().getMainPanel() );
            invokeStart();
            firePropertyChange( "workspace", ws, getWorkspace() );
        }
    }


    private void hosted ( final Workspace workspace ) {
        if ( workspace.getBinder() == null ) {
            workspace.hosted( this );

            WorkspacePropertyChangeListener wsListener = new WorkspacePropertyChangeListener();
            workspace.addPropertyChangeListener( "title", wsListener );
            workspace.addPropertyChangeListener( "mainPanel", wsListener );
            workspace.addRestartListener( new RestartListener() {
                @Override
                public void restartPerformed ( Workspace workspace ) {
                    if ( getMainClass() != null )
                        installMainClass( getMainClass() );
                }
            } );
        }
    }


    private final void setMainPanel ( JPanel panel ) {
        container.removeAll();
        container.add( panel, BorderLayout.CENTER );
        this.mainPanel = panel;
        container.updateUI();
    }


    /*
     * (non-Javadoc)
     * 
     * @see vecSpectra.component.IPanelBinder#getTitle()
     */
    @Override
    public String getTitle () {
        return getWorkspace() == null ? "XXX" : getWorkspace().getTitle();
    }


    /*
     * (non-Javadoc)
     * 
     * @see vecSpectra.component.IPanelBinder#getFlag()
     */
    @Override
    public Painter<JPanel> getFlag () {
        return flag;
    }


    /*
     * (non-Javadoc)
     * 
     * @see vecSpectra.component.IPanelBinder#setFlag(javax.swing.Painter)
     */
    @Override
    public void setFlag ( Painter<JPanel> flag ) {
        Painter<JPanel> oldFlag = this.flag;
        this.flag = flag;
        firePropertyChange( "flag", oldFlag, flag );
    }


    @Override
    public void setConnection ( LiftConnectionBean conn ) {
        LiftConnectionBean oldConn = this.conn;
        this.conn = conn;
        firePropertyChange( "connection", oldConn, conn );
    }


    @Override
    public LiftConnectionBean getConnection () {
        return conn;
    }


    protected final Class<? extends JPanel> getMainClass () {
        return mainClass;
    }


    private final void setMainClass ( Class<? extends JPanel> mainClass ) {
        Class<? extends JPanel> oldMainClass = this.mainClass;
        this.mainClass = mainClass;
        firePropertyChange( "mainClass", oldMainClass, mainClass );
    }


    public void installMainClass ( Class<? extends JPanel> panelClass ) {
        if ( panelClass != null ) {
            Workspace p = makeWorkspace( panelClass );
            // TODO check permission
            if ( p != null ) {
                setMainClass( panelClass );
                setWorkspace( p );
            } else {
                setMainClass( null );
                setWorkspace( Workspace.EMPTY_PAGE );
                logger.error( "newInstance failed [{}]", panelClass );
            }
        }
    }


    protected Workspace makeWorkspace ( Class<? extends JPanel> panelClass ) {
        if ( panelClass == null )
            return null;

        JPanel returnPanel = null;
        do {
            returnPanel = newPanelMethod1( panelClass );
            if ( returnPanel != null )
                break;
            returnPanel = newPanelMethod2( panelClass );
            if ( returnPanel != null )
                break;

            /* Add other constructor to build panel here */
        } while ( false );

        if ( returnPanel == null )
            return null;
        returnPanel.setOpaque( false );

        Workspace pagebinder = new Workspace( returnPanel, returnPanel instanceof Page ? ( Page )returnPanel : null );
        pagebinder.invokeCreate();
        return pagebinder;
    }


    /**
     * invoke <init>(LiftConnectiionBean)
     * 
     * @param panelClass
     * @return
     */
    protected JPanel newPanelMethod1 ( Class<? extends JPanel> panelClass ) {
        JPanel result = null;
        Constructor<? extends JPanel> constructor = null;
        try {
            constructor = panelClass.getConstructor( LiftConnectionBean.class );
            if ( constructor != null ) {
                final LiftConnectionBean params = getConnection() == null ? null : getConnection().clone();
                result = constructor.newInstance( params );
                logger.debug( String.format( "newPanel{%s(%s)}", panelClass, params ) );
            }
        } catch ( InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e1 ) {
        }
        return result;
    }


    protected JPanel newPanelMethod2 ( Class<? extends JPanel> panelClass ) {
        JPanel result = null;
        try {
            result = panelClass.newInstance();
        } catch ( InstantiationException | IllegalAccessException e ) {
            e.printStackTrace();
        }
        return result;
    }


    // //////////////////////// workspace::page /////////////////////////////////////
    protected final void invokeResume () {
        if ( workspace != null && workspace.getBinder() == this ) {
            workspace.invokeResume();
        }
    }


    protected final void invokeStart () {
        if ( workspace != null && workspace.getBinder() == this ) {
            workspace.invokeStart();
        }
    }


    protected final void invokeStop () {
        if ( workspace != null && workspace.getBinder() == this ) {
            workspace.invokeStop();
        }
    }


    protected final void invokePause () {
        if ( workspace != null && workspace.getBinder() == this ) {
            workspace.invokePause();
        }
    }

    private class WorkspacePropertyChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange ( PropertyChangeEvent evt ) {
            if ( "mainPanel".equals( evt.getPropertyName() ) )
                if ( getWorkspace() == workspace ) {
                    setMainPanel( getWorkspace().getMainPanel() );
                }
        }
    }

    static private long lastTimestampWhenConnLost;


    public static void showErrorMessage ( String text ) {
        if ( System.nanoTime() - lastTimestampWhenConnLost > 1000000000L ) {
            ToolBox.showErrorMessage( text );
            lastTimestampWhenConnLost = System.nanoTime();
        }
    }
}
