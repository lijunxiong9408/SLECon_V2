package slecon.component.tree;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;




/**
 * <p>This is abstract class. The child class must inherit the method {@link #setupTreeItem} to tell how to describe the tree.
 *
 * @author superb8
 */
public abstract class PageTree extends JTree {
    private static final long       serialVersionUID = -3775718188675866276L;
    private static final String     PATH_SEPERATOR   = "::";
    private final PageTreeItem      root             = new PageTreeItem( 0, "root", "root", null );;
    private PageTreeModel           treeDemoModel;
    private Class<? extends JPanel> selectedPanelClass;
    private MyTreeWrap treeWrapper;

    public PageTree () {
        super();
        ToolTipManager.sharedInstance().registerComponent( this );    // we're using tool-tip.       
        setCellRenderer( new PageTreeCellRenderer() );
        setModel( getPageTreeModel() );
        setRootVisible( false );
        setShowsRootHandles( false );
        BasicTreeUI ui=(BasicTreeUI)(getUI());
        ui.setCollapsedIcon(null);
        ui.setExpandedIcon(null);
        this.putClientProperty("JTree.lineStyle","None");
        
        addKeyListener( new KeyAdapter() {
            public void keyPressed ( KeyEvent e ) {
                int key = e.getKeyCode();
                if ( key == KeyEvent.VK_ENTER ) {
                    PageTreeItem item = getSelectionItem();
                    if ( item != null ) {
                        selectTo( item.getPanelClass() );
                    }
                }
            }
        } );
        addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked ( MouseEvent e ) {
                TreePath path = getClosestPathForLocation( e.getX(), e.getY() );
                if ( path != null && path.getLastPathComponent() instanceof PageTreeItem ) {
                    PageTreeItem clickItem = ( PageTreeItem )path.getLastPathComponent();
                   
                    if ( ! clickItem.isLeaf() ) {
                    	clickItem = ( PageTreeItem )clickItem.getFirstLeaf();
                    }
                    
                    for ( int i = path.getPathCount() - 1 ; i >= 0 ; i-- ) {
                        if ( path.getPathComponent( i ) instanceof PageTreeItem ) {
                        	selectTo( clickItem.getPanelClass() );
                            break;
                        }
                    }
                }
            }
        } );
    }

    public void scrollTo(Class<? extends JPanel> selectedPanelClass) {
        PageTreeItem myItem = getPageTreeModel().findTreeItem(selectedPanelClass);
        if (myItem != null) {
            Rectangle rect = new Rectangle(0, 0, 0, 0);
            for (int i = 0; getPathForRow(i) != null; i++) {
                TreePath path = getPathForRow(i);
                if (path.getLastPathComponent() instanceof PageTreeItem) {
                    if (( (PageTreeItem) path.getLastPathComponent() ).getPanelClass() == myItem.getPanelClass()) {
                        rect = getPathBounds(path);
                        break;
                    }
                }
            }
            scrollRectToVisible(rect);
        }
        repaint();
    }


    public void reloadPageTreeModel () {
        int x = 0, y = 0;
        if ( treeWrapper != null) {
            x = treeWrapper.getScrollPaneX();
            y = treeWrapper.getScrollPaneY();
        }
        getPageTreeModel().removeAllTreeItem();
        initTreeItem();
        if ( treeWrapper != null) {
            treeWrapper.setScrollPaneX( x );
            treeWrapper.setScrollPaneY( y );
        }
        repaint();
    }


    public synchronized PageTreeModel getPageTreeModel () {
        if ( treeDemoModel == null ) {
            treeDemoModel = new PageTreeModel( root );
        }
        return treeDemoModel;
    }


    protected abstract void initTreeItem ();


    public String getTreeItemTitle ( Class<? extends JPanel> panelClass ) {
        if ( getPageTreeModel().findTreeItem( panelClass ) != null ) {
            return getPageTreeModel().findTreeItem( panelClass ).toString();
        }
        return null;
    }


    protected void addPageItem ( int sortKey, String key, Class<? extends JPanel> panelClass ) {
        PageTreeItem item = getPageTreeModel().addPageItem( sortKey, key, panelClass );
        scrollPathToVisible( new TreePath( item.getPath() ) );
    }


    /**
     * the item having the value panelClass will be selected.
     * @param panelClass
     */
    public void selectTo ( Class<? extends JPanel> panelClass ) {
        if ( panelClass == null || selectedPanelClass != panelClass ) {
            reloadPageTreeModel();
            PageTreeItem myItem = getPageTreeModel().findTreeItem( panelClass );
            if ( myItem != null ) {
                selectedPanelClass = myItem.getPanelClass();
                fireSelectionPanelClassChanged();
            }
        }
    }
    

    @Override
    public void scrollRectToVisible(Rectangle aRect) {
        aRect.y-=28; //border size
        aRect.y-=aRect.height;
        super.scrollRectToVisible( aRect );
        aRect.y+=aRect.height;
        aRect.y+=28;
    }


    public void addEventSelectionChangeListener ( SelectedPanelClassChangeListener listener ) {
        listenerList.add( SelectedPanelClassChangeListener.class, listener );
    }


    public void removeEventSelectionChangeListener ( SelectedPanelClassChangeListener listener ) {
        listenerList.remove( SelectedPanelClassChangeListener.class, listener );
    }


    protected void fireSelectionPanelClassChanged () {
        Object[] listeners = listenerList.getListenerList();
        for ( int i = 0 ; i < listeners.length ; i = i + 2 ) {
            if ( listeners[ i ] == SelectedPanelClassChangeListener.class ) {
                ( ( SelectedPanelClassChangeListener )listeners[ i + 1 ] ).panelClassChanged();
            }
        }
    }


    public Class<? extends JPanel> getSelectedPanelClass () {
        return selectedPanelClass;
    }


    public PageTreeItem getSelectionItem () {
        Object obj = getLastSelectedPathComponent();
        if ( obj != null && obj instanceof PageTreeItem && ( ( PageTreeItem )obj ).isLeaf() ) {
            return ( ( PageTreeItem )obj );
        }
        return null;
    }


    public void expandAll ( boolean expand ) {
        TreeNode root = ( TreeNode )this.getModel().getRoot();

        // Traverse tree from root
        expandAll( this, new TreePath( root ), expand );
    }


    private static void expandAll ( JTree tree, TreePath parent, boolean expand ) {

        // Traverse children
        TreeNode node = ( TreeNode )parent.getLastPathComponent();
        if ( node.getChildCount() >= 0 ) {
            for ( Enumeration<?> e = node.children() ; e.hasMoreElements() ; ) {
                TreeNode n    = ( TreeNode )e.nextElement();
                TreePath path = parent.pathByAddingChild( n );
                expandAll( tree, path, expand );
            }
        }

        // Expansion or collapse must be done bottom-up
        if ( expand ) {
            tree.expandPath( parent );
        } else {
            tree.collapsePath( parent );
        }
    }


    public static class PageTreeModel extends DefaultTreeModel {
        private static final long                                        serialVersionUID = -3835952553013510216L;
        private ConcurrentHashMap<Class<? extends JPanel>, PageTreeItem> classToItem      = new ConcurrentHashMap<>();




        public PageTreeModel ( PageTreeItem root ) {
            super( root, false );
        }


        public PageTreeItem findTreeItem ( Class<? extends JPanel> panelClass ) {
            if ( panelClass == null )
                return null;
            return classToItem.get( panelClass );
        }


        /**
         * if add to model successful, return new PageItemNode; otherwise return null;
         *
         * @param sortKey
         * @param key
         * @param panelClass
         * @return
         */
        protected PageTreeItem addPageItem ( int sortKey, String key, Class<? extends JPanel> panelClass ) {
            PageTreeItem      parent = ( PageTreeItem )getRoot();
            ArrayList<String> terms  = new ArrayList<>();
            for (String itm : key.split(PageTree.PATH_SEPERATOR)) {
                if (itm != null && itm.trim().length() != 0)
                    terms.add(itm);
            }

            for ( String term : terms ) {
                PageTreeItem node = null;
                for ( int i = 0 ; i < parent.getChildCount() ; i++ ) {
                    TreeNode n = parent.getChildAt( i );
                    if ( ( n instanceof PageTreeItem ) && ! ( ( PageTreeItem )n ).isLeaf()
                        && ( ( PageTreeItem )n ).getUserObject().equals( term ) ) {
                        node = ( PageTreeItem )n;
                        break;
                    }
                }
                if ( node == null ) {
                    int index = 0;
                    for ( ; index < parent.getChildCount() ; index++ ) {
                        TreeNode n = parent.getChildAt( index );
                        if ( ( n instanceof PageTreeItem ) ) {
                            if ( sortKey <= ( ( PageTreeItem )n ).getSort() )
                                break;
                        }
                    }
                    
                    if ( terms.indexOf( term ) == terms.size() - 1 )
                        node = new PageTreeItem( sortKey, key, term, panelClass );
                    else
                        node = new PageTreeItem( sortKey, key, term, null );
                    insertNodeInto( node, parent, index );
                }
                parent = node;
            }
            
            if ( panelClass != null )
                classToItem.put( panelClass, parent );
            return parent == getRoot()
                   ? null
                   : parent;
        }


        public void removeAllTreeItem () {
            classToItem.clear();
            ( ( PageTreeItem )getRoot() ).removeAllChildren();
            nodeStructureChanged( ( ( PageTreeItem )getRoot() ) );
        }
    }


    public void setTreeWrap(MyTreeWrap treeWrapper) {
        this.treeWrapper = treeWrapper;
    }
}
