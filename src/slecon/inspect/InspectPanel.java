package slecon.inspect;
import java.awt.Color;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.Painter;
import javax.swing.ToolTipManager;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.PageTreeExpression;
import logic.util.SiteManagement;
import logic.util.Version;
import logic.util.VersionChangeListener;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.FlagPainter;
import slecon.component.NoPanel;
import slecon.component.NoPermissionPanel;
import slecon.component.PanelBinder;
import slecon.component.tree.MyTreeWrap;
import slecon.component.tree.PageTreeItem;
import slecon.component.tree.SelectedPanelClassChangeListener;
import slecon.home.HomePanel;
import slecon.home.dashboard.DashboardPanel;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;




public class InspectPanel extends PanelBinder implements VersionChangeListener {
    private static final long            serialVersionUID = -8719434492768209860L;
    private static final Painter<JPanel> painter          = new FlagPainter<>( Color.BLACK );
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "inspect.InspectPanel" );
    private InspectTree             tree;
    private Class<? extends JPanel> panelClass;
    private MyTreeWrap              leftTreePanel;




    private InspectPanel () {
        initGUI();

        /* if tree selection change, then update workspace. */
        tree.addEventSelectionChangeListener( new SelectedPanelClassChangeListener() {
            @Override
            public void panelClassChanged () {
                Class<? extends JPanel> pclass = tree.getSelectedPanelClass();
                if ( pclass != null && ( getMainPanel() == null || ! pclass.equals( getMainPanel().getClass() ) ) ) {
                    panelClass = pclass;
                    installMainClass( panelClass );
                }
                
                if ( tree.getSelectionItem() == null || tree.getSelectionItem().getPanelClass() != tree.getSelectedPanelClass() )
                    tree.scrollTo(tree.getSelectedPanelClass());
            }
        } );

        /* lookup title */
        addPropertyChangeListener( "workspace", new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                if ( getWorkspace() != null ) {
                    final Class<? extends JPanel> titlePanelClass = getWorkspace().getMainPanel().getClass();
                    if ( tree.getTreeItemTitle( titlePanelClass ) != null && tree.getTreeItemTitle( titlePanelClass ).length() != 0 )
                        getWorkspace().setTitle( String.format( "%s (%s)", TEXT.getString( "headerTitle.string" ),
                                                                tree.getTreeItemTitle( titlePanelClass ) ) );
                    else
                        getWorkspace().setTitle( TEXT.getString( "headerTitle.string" ) );
                }

                int x = leftTreePanel.getScrollPaneX();
                int y = leftTreePanel.getScrollPaneY();
                tree.selectTo( getWorkspace().getMainPanel().getClass() );
                leftTreePanel.setScrollPaneX( x );
                leftTreePanel.setScrollPaneY( y );
            }
        } );

        /* post ToolTip */
        addPropertyChangeListener( "workspace", new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                ToolTipManager.sharedInstance().setEnabled( false );
                ToolTipManager.sharedInstance().setEnabled( true );

                versionChanged(getConnection(), SiteManagement.getVersion(getConnection()));
            }
        } );

        /* if header->liftSelector changed, then re-new the workspace. */
        addPropertyChangeListener( "connection", new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                if ( getConnection() != null && tree.getSelectedPanelClass() != null) {
                    installMainClass( panelClass );
                    if ( getWorkspace() != null )
                        getWorkspace().getMainPanel().setEnabled(true);
                } else {
                    if ( getWorkspace() != null )
                        getWorkspace().getMainPanel().setEnabled(false);
                }
                versionChanged(getConnection(), SiteManagement.getVersion(getConnection()));
            }
        } );
    }


    public static synchronized InspectPanel build ( LiftConnectionBean connBean, Class<? extends JPanel> panelClass ) {
        InspectPanel result = new InspectPanel();
        result.panelClass = panelClass;
        result.setFlag( painter );
        result.setConnection( connBean );
        return result;
    }


    /**
     * initialize the GUI.
     */
    private void initGUI () {
        setLayout( new MigLayout( "gap 0, insets 0", "[220!,fill][grow,fill]", "[grow,fill]" ) );
        tree = new InspectTree();
        tree.setFont( FontFactory.FONT_12_PLAIN );
        leftTreePanel = new MyTreeWrap( tree );
        add( leftTreePanel, "cell 0 0" );
        add( getMainPanelContainer(), "cell 1 0" );
    }


    @Override
    public void onStart () {
    	if ( ! StartUI.getLiftSelector().setSelectedLift( getConnection() ) ) {
            return;
        }
    	
        tree.reloadPageTreeModel();
        if(tree.getRowCount()==0) {
            ToolBox.showLoginPanel(getConnection());
            tree.reloadPageTreeModel();
        }
        if (panelClass == null) {
            if (tree.getPageTreeModel().getRoot() instanceof PageTreeItem
                    && ( (PageTreeItem) tree.getPageTreeModel().getRoot() ).getFirstLeaf() instanceof PageTreeItem)
                panelClass = ( (PageTreeItem) ( (PageTreeItem) ( tree.getPageTreeModel().getRoot() ) ).getFirstLeaf() ).getPanelClass();
        }
        installMainClass( panelClass );
        SiteManagement.addVersionChangeListener(this, null);
        invokeStart();
        tree.scrollRectToVisible(new Rectangle(0,0,0,0));
    }


    @Override
    public void onResume () {
        LiftConnectionBean topMainConn = StartUI.getLiftSelector().getSelectedLift();
        if ( topMainConn != null )
            setConnection( topMainConn );

        invokeResume();
    }


    @Override
    public void onPause () {
        invokePause();
    }


    @Override
    public void onStop () {
        invokeStop();
        SiteManagement.removeVersionChangeListener(this);
    }
    
    
    @Override
    public void versionChanged(LiftConnectionBean connBean, Version newVersion) {
        if(getConnection()!=null && getConnection().equals(connBean)) {
            if(newVersion==null) {
            	System.out.println("Inspect Panel.");
                //showErrorMessage(Dict.lookup("ConnectionLost"));
                StartUI.getTopMain().push(HomePanel.build(DashboardPanel.class));
            }
        }
        tree.reloadPageTreeModel();
    }
    
    
    @Override
    public void installMainClass(Class<? extends JPanel> panelClass) {
        final PageTreeExpression expr = tree.getPageCondition(panelClass);
        if (expr != null) {
            if (expr.evaluate(SiteManagement.getVersion(getConnection()), ToolBox.getRoles(getConnection()))) {
                super.installMainClass(panelClass);
            } else
                super.installMainClass(NoPermissionPanel.class);
        } else {
            super.installMainClass(NoPanel.class);
        }
    }
    
}
