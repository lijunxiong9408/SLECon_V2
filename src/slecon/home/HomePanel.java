package slecon.home;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import base.cfg.ImageFactory;
import logic.Dict;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.FlagPainter;
import slecon.component.PanelBinder;
import slecon.component.Workspace;
import slecon.interfaces.HomeView;




public class HomePanel extends PanelBinder {
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "home.HomePanel" );
    private ShortcutPanel shortcut;

    private HomePanel () {
        initGUI();

        /* lookup the title */

        addPropertyChangeListener( "workspace", new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                Workspace workspace = getWorkspace();
                workspace.setTitle( TEXT.getString( "headerTitle.string" ) );
                if ( workspace != null ) {
                    if ( workspace.getMainPanel().getClass().isAnnotationPresent( HomeView.class ) ) {
                        String name = Dict.lookup( workspace.getMainPanel().getClass().getAnnotation( HomeView.class ).name() );
                        if ( name.length() != 0 )
                            workspace.setTitle( String.format( "%s (%s)", TEXT.getString( "headerTitle.string" ), name ) );
                    }
                }
            }
        } );

        /* post Tooltip */
        addPropertyChangeListener( "workspace", new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                ToolTipManager.sharedInstance().setEnabled( false );
                ToolTipManager.sharedInstance().setEnabled( true );
            }
        } );
    }


    public static HomePanel build ( Class<? extends JPanel> panelClass ) {
        HomePanel home = new HomePanel();
        home.installMainClass( panelClass );
        home.setFlag( new FlagPainter<JPanel>( Color.BLACK ));
        return home;
    }


    private void initGUI () {
        setLayout( new MigLayout( "gap 0, insets 0", "[220!,fill][grow,fill]", "[grow,fill]" ) );
        shortcut = new ShortcutPanel( this );
        add( shortcut, "cell 0 0" );
        add( getMainPanelContainer(), "cell 1 0" );
    }


    @Override
    public void onStart () {
        StartUI.getLiftSelector().setSelectedLift( null );
        invokeStart();
    }


    @Override
    public void onResume () {
        invokeResume();
        StartUI.getLiftSelector().setSelectedLift( null );
    }


    @Override
    public void onPause () {
        invokePause();
    }


    @Override
    public void onStop () {
        invokeStop();
    }
}
