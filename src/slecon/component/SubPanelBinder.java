package slecon.component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.Painter;
import javax.swing.SwingUtilities;

import logic.connection.LiftConnectionBean;
import slecon.StartUI;
import slecon.interfaces.Page;




/**
 * (there are no start and stop event).
 * when it start/resume, pause parent and this page resume.
 * when it pause/stop, this page pause, and resume parent.
 * @author superb8
 *
 */
public final class SubPanelBinder implements IPanelBinder {
    private boolean   focus = false;
    private Workspace parent;
    private Workspace workspace;
    private JPanel    orignalMainPanel;




    public SubPanelBinder ( final Workspace parent, final String title, final JPanel delegate ) {
        this.parent = parent;
        workspace   = new Workspace( delegate, ( delegate instanceof Page )
                                               ? ( Page )delegate
                                               : null );
        hosted();
        workspace.setTitle( title );
        workspace.addPropertyChangeListener( "title", new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
            }
        } );
        workspace.invokeCreate();
    }


    @Override
    public Boolean isRelaceWorkspace () {
        return false;
    }


    private void hosted () {
        workspace.hosted( this );
        workspace.addRestartListener( new RestartListener() {
            @Override
            public void restartPerformed ( Workspace workspace ) {
                parent.restart();
            }
        } );
    }


    public final Painter<JPanel> getFlag () {
        return parent.getBinder() == null
               ? null
               : parent.getBinder().getFlag();
    }


    @Override
    public void onStop () {
        unfocus();
    }


    @Override
    public void onPause () {
        unfocus();
    }


    public void focus () {
        if ( ! focus ) {
            focus = true;
            if ( orignalMainPanel == null ) {
                orignalMainPanel = parent.getMainPanel();
                parent.setMainPanel( workspace.getMainPanel() );
                workspace.invokeResume();
            }
        }
    }

    public void unfocus () {
        if ( focus ) {
            focus = false;
            if ( orignalMainPanel != null ) {
                workspace.invokePause();
                parent.setMainPanel( orignalMainPanel );
                orignalMainPanel = null;
            }
        }
    }


    @Override
    public void onResume () {
        focus();
    }


    @Override
    public void onStart () {
        focus();
    }


    @Override
    public final void setConnection ( LiftConnectionBean conn ) {
        parent.getBinder().setConnection( conn );
    }


    @Override
    public final LiftConnectionBean getConnection () {
        return parent.getBinder().getConnection();
    }


    @Override
    public final String getTitle () {
        return workspace.getTitle();
    }


    @Override
    public final void setFlag ( Painter<JPanel> flag ) {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
            }
        } );
    }
}
