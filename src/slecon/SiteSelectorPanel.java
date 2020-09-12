package slecon;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import logic.connection.LiftConnectionBean;
import logic.util.SiteChangeListener;
import logic.util.SiteManagement;
import logic.util.Version;
import logic.util.VersionChangeListener;
import net.miginfocom.swing.MigLayout;
import slecon.component.FocusTraversalOnArray;
import slecon.dialog.connection.SiteInfo;




public class SiteSelectorPanel extends JPanel implements SiteChangeListener, VersionChangeListener {
    private static final long      serialVersionUID = 111311144053188898L;
    private SiteInfoTree           tree             = new SiteInfoTree();
    private SiteInfo               siteInfo;


    /**
     * Create the panel.
     */
    public SiteSelectorPanel () {
        setFocusTraversalPolicyProvider( true );
        initGUI();
        tree.requestFocusInWindow();
        setSiteInfo( SiteInfo.fromSiteManagement() );
        SiteManagement.addSiteChangeListener( this, null );
        SiteManagement.addVersionChangeListener( this, null );
    }


    private void initGUI () {
        setLayout( new MigLayout( "ins 5, gap 0", "[fill,grow]", "[fill,grow]" ) );

        JScrollPane scrollPane = new JScrollPane();
        add( scrollPane);
        tree = new SiteInfoTree();
        tree.addMouseListener( new TreeMouseListener() );
        tree.addKeyListener( new TreeKeyListener() );
        scrollPane.setViewportView( tree );
    }


    public final SiteInfo getSiteInfo () {
        return siteInfo;
    }


    public final void setSiteInfo ( SiteInfo siteInfo ) {
        tree.setSiteInfo( siteInfo );
    }


    private class TreeKeyListener extends KeyAdapter {
        @Override
        public void keyPressed ( KeyEvent e ) {
            if ( e.getKeyCode() == 10 ) {
                TreePath selPath = tree.getSelectionPath();
                if ( selPath.getLastPathComponent() instanceof DefaultMutableTreeNode ) {
                    DefaultMutableTreeNode node = ( DefaultMutableTreeNode )selPath.getLastPathComponent();
                    if ( node.getUserObject() instanceof LiftConnectionBean ) {
                        StartUI.getLiftSelector().setSelectedLift( ( LiftConnectionBean )node.getUserObject() );
                        StartUI.getLiftSelector().hideLiftSelectorPopup();
                    }
                }
                tree.repaint();
            }
        }
    }


    private class TreeMouseListener extends MouseAdapter {
        @Override
        public void mousePressed ( final MouseEvent e ) {
            int      selRow  = tree.getRowForLocation( e.getX(), e.getY() );
            TreePath selPath = tree.getPathForLocation( e.getX(), e.getY() );
            if ( selRow != -1 && e.getClickCount() == 2 ) {
                if ( selPath.getLastPathComponent() instanceof DefaultMutableTreeNode ) {
                    DefaultMutableTreeNode node = ( DefaultMutableTreeNode )selPath.getLastPathComponent();
                    if ( node.getUserObject() instanceof LiftConnectionBean ) {
                    	StartUI.getLiftSelector().setSelectedLift( ( LiftConnectionBean )node.getUserObject() );
                        StartUI.getLiftSelector().hideLiftSelectorPopup();
                    }
                }
            }
            tree.repaint();
        }
    }


    @Override
    public void siteAdd ( LiftConnectionBean conns ) {
        tree.setSiteInfo( SiteInfo.fromSiteManagement() );
    }


    @Override
    public void siteRemove ( LiftConnectionBean conns ) {
        tree.setSiteInfo( SiteInfo.fromSiteManagement() );
    }
    

    @Override
    public void versionChanged ( LiftConnectionBean connBean, Version newVersion ) {
        tree.repaint();
    }


    public void resetSelection () {
        tree.setSelectionInterval( -1, -1 );
    }
}
