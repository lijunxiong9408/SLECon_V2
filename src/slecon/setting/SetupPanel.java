package slecon.setting;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Painter;
import javax.swing.ToolTipManager;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.connection.LiftSiteBean;
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
import slecon.component.tree.PageTree;
import slecon.component.tree.PageTreeItem;
import slecon.component.tree.SelectedPanelClassChangeListener;
import slecon.home.HomePanel;
import slecon.home.dashboard.DashboardPanel;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;




public class SetupPanel extends PanelBinder implements VersionChangeListener {
    private static final long            serialVersionUID = -8719434492768209860L;
    private final static Painter<JPanel> painter          = new FlagPainter<>( Color.BLACK );
    private final static ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.SettingPanel" );
    private SetupTree               tree;
    private Class<? extends JPanel> panelClass;
    private MyTreeWrap              leftTreePanel;
    private Version                 version;




    private SetupPanel () {
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
                    if (getWorkspace()!=null)
                        getWorkspace().getMainPanel().setEnabled(true);
                } else {
                    if (getWorkspace()!=null)
                        getWorkspace().getMainPanel().setEnabled(false);
                }
                versionChanged(getConnection(), SiteManagement.getVersion(getConnection()));
            }
        } );
    }


    public static synchronized SetupPanel build ( LiftConnectionBean connBean, Class<? extends JPanel> panelClass ) {
        SetupPanel result = new SetupPanel();
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
        tree = new SetupTree();
        tree.setOpaque( false );
        tree.setFont( FontFactory.FONT_12_PLAIN );
        leftTreePanel = new MyTreeWrap( tree );
        add( leftTreePanel, "cell 0 0" );
        add( getMainPanelContainer(), "cell 1 0" );
        setOpaque( true );
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
            	System.out.println("Setup Panel.");
                //showErrorMessage(Dict.lookup("ConnectionLost"));
                StartUI.getTopMain().push(HomePanel.build(DashboardPanel.class));
            } else if (version != newVersion) {
                version = newVersion;
                tree.reloadPageTreeModel();
            }
        }
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
    
    
    
    public static void main(String...args) {
        SiteManagement.initMonitorMgr();
        StartUI.getLiftSelector().setSelectedLift(new LiftConnectionBean("#Lab 254",  "192.168.1.254", 1235, new LiftSiteBean("本地網絡")));
        
        PageTree tree          = new SetupTree();
        tree.reloadPageTreeModel();
        final JPanel leftTreePanel = new MyTreeWrap( tree );
        
        final JPanel panelA = new JPanel();
        final JPanel panelB = new JPanel();
        
        final ActionListener btn1Listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panelA.remove(leftTreePanel);
                panelB.remove(leftTreePanel);
                panelA.add(leftTreePanel, "cell 0 0");
                panelA.revalidate();
                panelB.revalidate();
                panelA.repaint();
                panelB.repaint();
            }
            
        };
        final ActionListener btn2Listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panelA.remove(leftTreePanel);
                panelB.remove(leftTreePanel);
                panelB.add(leftTreePanel, "cell 0 0");
                panelA.revalidate();
                panelB.revalidate();
                panelA.repaint();
                panelB.repaint();
                panelA.repaint();
                panelB.repaint();
            }
            
        };
        {
            JPanel contentpane = new JPanel();

            final JButton btn1 = new JButton("move to panelA");
            final JButton btn2 = new JButton("move to panelB");
            
            contentpane.add(btn1);
            contentpane.add(btn2);
            
            btn1.addActionListener(btn1Listener);
            btn2.addActionListener(btn2Listener);
            

            panelA.setLayout(new MigLayout("w 600, h 500, gap 0, insets 0", "[220!,fill][grow,fill]", "[grow,fill]"));

            panelA.add(leftTreePanel, "cell 0 0");
            panelA.add(contentpane, "cell 1 0");
            panelA.setOpaque(true);

            JFrame frame = new JFrame("panelA");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panelA);
            frame.pack();
            frame.setVisible(true);
        }

        {
            JPanel contentpane = new JPanel();

            final JButton btn1 = new JButton("move to panelA");
            final JButton btn2 = new JButton("move to panelB");
            
            contentpane.add(btn1);
            contentpane.add(btn2);

            btn1.addActionListener(btn1Listener);
            btn2.addActionListener(btn2Listener);
            
            panelB.setLayout(new MigLayout("w 600, h 500, gap 0, insets 0", "[220!,fill][grow,fill]", "[grow,fill]"));
            panelB.add(contentpane, "cell 1 0");
            panelB.setOpaque(true);

            JFrame frame = new JFrame("panelB");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panelB);
            frame.pack();
            frame.setVisible(true);
        }
    }
}
