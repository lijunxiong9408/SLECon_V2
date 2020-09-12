package slecon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import logic.connection.LiftConnectionBean;
import logic.connection.LiftSiteBean;
import logic.util.SiteManagement;
import slecon.dialog.connection.SiteInfo;
import slecon.dialog.connection.SiteTreeCellRenderer;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;




public final class SiteInfoTree extends JTree {
    private static final long      serialVersionUID = 3408071624465230281L;
    private String                 filterText;
    private DefaultMutableTreeNode root;
    private SiteInfo               siteInfo;




    public SiteInfoTree () {
        root = new DefaultMutableTreeNode( "My Sites" );

        final AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale( 1.5, 1.0 );
        setFont( FontFactory.FONT_12_PLAIN );

        TreeModel model = new DefaultTreeModel( root );
        setModel( model );
        getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        setSelectionPath( new TreePath( root ) );
        setBackground(StartUI.SUB_BACKGROUND_COLOR);
        setRootVisible( false );
        setLargeModel( true );
        setShowsRootHandles( false );
        BasicTreeUI ui=(BasicTreeUI)(getUI());
        ui.setCollapsedIcon(null);
        ui.setExpandedIcon(null);
        this.putClientProperty("JTree.lineStyle","None");
        setCellRenderer( new SiteTreeCellRenderer());
        
        addPropertyChangeListener( "siteInfo", new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                root.removeAllChildren();
                if ( getSiteInfo() != null ) {
                    for ( LiftSiteBean site : getSiteInfo().sites ) {
                        DefaultMutableTreeNode siteNode = new DefaultMutableTreeNode( site );
                        root.add( siteNode );
                        if ( getSiteInfo().siteMap.get( site ) != null ) {
                            for ( LiftConnectionBean conn : getSiteInfo().siteMap.get( site ) ) {
                                DefaultMutableTreeNode liftNode = new DefaultMutableTreeNode( conn );
                                siteNode.add( liftNode );
                            }
                        }
                    }
                }
                filterTree();
            }
        } );
        addPropertyChangeListener( "filterText", new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                filterTree();
            }
        } );
    }

    /////////////////////////////////////////////////////////////


    public final String getFilterText () {
        return filterText == null
               ? ""
               : filterText;
    }


    public final void setFilterText ( String filterText ) {
        String oldFilterText = this.filterText;
        this.filterText = filterText;
        firePropertyChange( "filterText", oldFilterText, filterText );
    }


    public final SiteInfo getSiteInfo () {
        return siteInfo;
    }


    public final void setSiteInfo ( SiteInfo siteInfo ) {
        SiteInfo oldSiteInfo = this.siteInfo;
        this.siteInfo = siteInfo;
        firePropertyChange( "siteInfo", oldSiteInfo, siteInfo );
    }


    private String toTreeItemText ( Object value ) {
        if ( value != null ) {
            if ( value instanceof DefaultMutableTreeNode )
                value = ( ( DefaultMutableTreeNode )value ).getUserObject();
            if ( value instanceof LiftSiteBean ) {
                return ( ( LiftSiteBean )value ).getName();
            } else if ( value instanceof LiftConnectionBean )
                return ( ( LiftConnectionBean )value ).getName();
        }
        return null;
    }


    @Override
    public String convertValueToText ( Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
        String text = toTreeItemText( value );
        if ( text != null )
            return text;
        return super.convertValueToText( value, selected, expanded, leaf, row, hasFocus );
    }


    private void filterTree () {

        // get a copy
        DefaultMutableTreeNode filteredRoot = copyNode( root );
        DefaultTreeModel       treeModel    = ( DefaultTreeModel )getModel();

        // remove un-matching item
        for ( int i = 0 ; i < filteredRoot.getChildCount() ; i++ ) {
            DefaultMutableTreeNode node = ( DefaultMutableTreeNode )filteredRoot.getChildAt( i );
            if ( ! matchesFilter( node ) && ! containsMatchingChild( node ) ) {
                filteredRoot.remove( i );
                i--;
            }
            for ( int j = 0 ; j < node.getChildCount() ; j++ ) {
                DefaultMutableTreeNode node1 = ( DefaultMutableTreeNode )node.getChildAt( j );
                if ( ! matchesFilter( node1 ) && ! containsMatchingChild( node1 ) ) {
                    node.remove( j );
                    j--;
                }
            }
        }
        treeModel.setRoot( filteredRoot );
        setModel( treeModel );
        updateUI();
        for ( int i = 0 ; i < getRowCount() ; i++ ) {
            expandRow( i );
        }
    }


    private DefaultMutableTreeNode copyNode ( DefaultMutableTreeNode orig ) {
        DefaultMutableTreeNode newOne = new DefaultMutableTreeNode();
        newOne.setUserObject( orig.getUserObject() );

        @SuppressWarnings( "unchecked" ) Enumeration<DefaultMutableTreeNode> enm = orig.children();
        while ( enm.hasMoreElements() ) {
            DefaultMutableTreeNode child = enm.nextElement();
            newOne.add( copyNode( child ) );
        }
        return newOne;
    }


    private boolean containsMatchingChild ( DefaultMutableTreeNode node ) {
        Enumeration<DefaultMutableTreeNode> e = node.breadthFirstEnumeration();
        while ( e.hasMoreElements() ) {
            final boolean match = matchesFilter( e.nextElement() );
            if ( match ) {
                return true;
            }
        }
        return false;
    }


    private boolean matchesFilter ( DefaultMutableTreeNode node ) {
        if ( node != null && node.getUserObject() != null ) {
            if ( node.getUserObject() instanceof LiftSiteBean ) {
                LiftSiteBean site = ( LiftSiteBean )node.getUserObject();
                return site.getName().toString().contains( getFilterText() );
            } else if ( node.getUserObject() instanceof LiftConnectionBean ) {
                LiftConnectionBean conn = ( LiftConnectionBean )node.getUserObject();
                if ( conn.getName().toString().contains( getFilterText() ) )
                    return true;
                if ( conn.getIp().toString().contains( getFilterText() ) )
                    return true;
                if ( Integer.toString( conn.getPort() ).toString().contains( getFilterText() ) )
                    return true;
            }
        }
        if ( toTreeItemText( node ) != null )
            toTreeItemText( node ).contains( getFilterText() );
        return false;
    }
}
