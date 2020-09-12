package slecon.dialog.connection;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import logic.connection.LiftConnectionBean;
import logic.connection.LiftSiteBean;
import slecon.StartUI;
import slecon.component.tree.PageTreeCellRenderer;
import slecon.component.tree.PageTreeItem;




public class SiteEditorTree extends JTree {
    private static boolean         showingDialog    = false;
    final private SiteInfo         context          = new SiteInfo();
    HashMap<Object, TreePath>      pathMap          = new HashMap<>();
    private DefaultMutableTreeNode root;
    private DefaultTreeModel       model;
    private TreeCellEditor         editor;
    Object 	selectObject = null;

    public SiteEditorTree () {
        this( null );
    }


    public SiteEditorTree ( SiteInfo siteInfo ) {
        root  = new DefaultMutableTreeNode( "My Sites" );
        model = new DefaultTreeModel( root );
        
        setCellRenderer( new SiteTreeCellRenderer() );
        setRootVisible( false );
        setShowsRootHandles( false );
        //setLargeModel( true );
        setBackground(StartUI.SUB_BACKGROUND_COLOR);
        BasicTreeUI ui=(BasicTreeUI)(getUI());
        ui.setCollapsedIcon(null);
        ui.setExpandedIcon(null);
        this.putClientProperty("JTree.lineStyle","None");
        setModel( model );
        getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        
        addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	// TODO Auto-generated method stub
            	TreePath selectPath = getClosestPathForLocation( e.getX(), e.getY() );
                if ( selectPath != null ) {
                	selectObject = selectPath.getLastPathComponent();
                }
            }
        } );
        
        DefaultTreeCellRenderer renderer    = ( DefaultTreeCellRenderer )getCellRenderer();
        TreeCellEditor          comboEditor = new SiteLiftTreeEditor( new JTextField() );
        editor = new DefaultTreeCellEditor( this, renderer, comboEditor );
        setCellEditor( editor );
        setSiteInfo( siteInfo );
    }


    public boolean isPathEditable ( TreePath path ) {
        if ( ! super.isPathEditable( path ) )
            return false;
        if ( path.getLastPathComponent() instanceof DefaultMutableTreeNode ) {
            Object obj = ( ( DefaultMutableTreeNode )path.getLastPathComponent() ).getUserObject();
            if ( obj instanceof LiftSiteBean )
                return true;
            else if ( obj instanceof LiftConnectionBean )
                return true;
        }
        return false;
    }


    public synchronized void refresh () {

        // save the expand state, since we will remove all tree item.
        Map<LiftSiteBean, Boolean> expandMap = new HashMap<>();
        try {
            for ( LiftSiteBean site : context.siteMap.keySet() ) {
                expandMap.put( site, isExpanded( getTreePath( site ) ) );
            }
        } catch ( Exception e ) {
            expandMap = null;
        }
        editor.stopCellEditing();
        root.removeAllChildren();
        pathMap.clear();
        for ( LiftSiteBean site : context.sites ) {
            DefaultMutableTreeNode siteNode = new DefaultMutableTreeNode( site );
            root.add( siteNode );
            pathMap.put( site, new TreePath( siteNode.getPath() ) );
            if ( context.siteMap.get( site ) != null ) {
                for ( LiftConnectionBean conn : context.siteMap.get( site ) ) {
                    DefaultMutableTreeNode liftNode = new DefaultMutableTreeNode( conn );
                    siteNode.add( liftNode );
                    pathMap.put( conn, new TreePath( liftNode.getPath() ) );
                }
            }
        }
        model.reload();
        if ( expandMap != null )
            try {
                for ( Entry<LiftSiteBean, Boolean> ent : expandMap.entrySet() ) {
                    if ( ent.getValue() == Boolean.TRUE ) {
                        expandPath( getTreePath( ent.getKey() ) );
                    }
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
    }


    public synchronized boolean isSiteExist ( String site ) {
        return context.sites.contains( new LiftSiteBean( site ) );
    }


    public synchronized void newSite ( LiftSiteBean bean ) {
        context.sites.add( bean );
        context.siteMap.put( bean, new ArrayList<LiftConnectionBean>() );
        refresh();
    }


    public synchronized void newLift ( LiftConnectionBean bean ) {
        List<LiftConnectionBean> list = context.siteMap.get( bean.getSite() );
        if ( list != null )
            list.add( bean );
        refresh();
    }


    public TreePath getTreePath ( Object obj ) {
        return pathMap.get( obj );
    }


    public void setSiteInfo ( SiteInfo siteInfo ) {
        if ( siteInfo != null )
            if ( context == null || ! context.equals( siteInfo ) ) {
                context.sites.clear();
                context.siteMap.clear();
                context.sites.addAll( siteInfo.sites );
                context.siteMap.putAll( siteInfo.siteMap );
                refresh();
            }
        expandRow( 0 );
    }


    public SiteInfo getSiteInfo () {
        for ( LiftSiteBean site : context.sites ) {
            if ( site.getName().trim().length() == 0 )
                return null;
            for ( LiftConnectionBean conn : context.siteMap.get( site ) ) {
                if ( conn.getName().trim().length() == 0 ) {
                    return null;
                }
                if ( conn.getIp().trim().length() == 0 ) {
                    return null;
                }
                if ( ! conn.getSite().equals( site ) )
                    return null;
            }
        }

        SiteInfo siteInfo = new SiteInfo();
        for ( LiftSiteBean site : context.sites ) {
            LiftSiteBean siteBean = site.clone();
            siteInfo.sites.add( site );
            if ( context.siteMap.get( site ) != null ) {
                ArrayList<LiftConnectionBean> list = new ArrayList<>();
                for ( LiftConnectionBean conn : context.siteMap.get( site ) ) {
                    list.add( conn.clone() );
                }
                siteInfo.siteMap.put( siteBean, list );
            }
        }
        return siteInfo;
    }


    public boolean hasPrev ( Object obj ) {
        int index = indexOf( obj );
        return index > 0;
    }


    public boolean hasNext ( Object obj ) {
        int index = indexOf( obj );
        if ( obj instanceof LiftSiteBean ) {
            return index != -1 && index < context.sites.size() - 1;
        }
        if ( obj instanceof LiftConnectionBean )
            return index != -1 && index < context.siteMap.get( ( ( LiftConnectionBean )obj ).getSite() ).size() - 1;
        return false;
    }


    public int indexOf ( Object obj ) {
        if ( obj instanceof LiftSiteBean )
            return context.sites.indexOf( obj );
        if ( obj instanceof LiftConnectionBean )
            return context.siteMap.get( ( ( LiftConnectionBean )obj ).getSite() ).indexOf( obj );
        return -1;
    }


    public boolean back ( Object obj ) {
        if ( hasPrev( obj ) ) {
            int i = indexOf( obj );
            if ( obj instanceof LiftSiteBean ) {
                context.sites.remove( i );
                context.sites.add( i - 1, ( LiftSiteBean )obj );
                refresh();
                return true;
            } else if ( obj instanceof LiftConnectionBean ) {
                List<LiftConnectionBean> list = context.siteMap.get( ( ( LiftConnectionBean )obj ).getSite() );
                list.remove( i );
                list.add( i - 1, ( LiftConnectionBean )obj );
                refresh();
                return true;
            }
        }
        return false;
    }


    public boolean forward ( Object obj ) {
        if ( hasNext( obj ) ) {
            int i = indexOf( obj );
            if ( i == -1 )
                return false;
            if ( obj instanceof LiftSiteBean ) {
                context.sites.remove( i );
                context.sites.add( i + 1, ( LiftSiteBean )obj );
                refresh();
                return true;
            } else if ( obj instanceof LiftConnectionBean ) {
                List<LiftConnectionBean> list = context.siteMap.get( ( ( LiftConnectionBean )obj ).getSite() );
                list.remove( i );
                list.add( i + 1, ( LiftConnectionBean )obj );
                refresh();
                return true;
            }
        }
        return false;
    }


    public String convertValueToText ( Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
        if ( value != null ) {
            if ( value instanceof DefaultMutableTreeNode )
                value = ( ( DefaultMutableTreeNode )value ).getUserObject();
            if ( value instanceof LiftSiteBean ) {
                return ( ( LiftSiteBean )value ).getName();
            } else if ( value instanceof LiftConnectionBean )
                return ( ( LiftConnectionBean )value ).getName();
        }
        return super.convertValueToText( value, selected, expanded, leaf, row, hasFocus );
    }


    public boolean delete ( Object obj ) {
        boolean result = false;
        if ( obj instanceof LiftSiteBean ) {
            result = context.sites.remove( obj ) && context.siteMap.remove( obj ) != null;
        } else if ( obj instanceof LiftConnectionBean ) {
            LiftConnectionBean       conn = ( LiftConnectionBean )obj;
            List<LiftConnectionBean> list = context.siteMap.get( conn.getSite() );
            result = list.remove( obj );
        }
        if ( result ) {
            refresh();
        }
        return result;
    }


    public class SiteLiftTreeEditor extends DefaultCellEditor {
        private static final long serialVersionUID = 8080687467319386931L;




        public SiteLiftTreeEditor ( final JTextField textField ) {
            super( textField );
            setClickCountToStart( 1 );
            textField.removeActionListener( delegate );
            delegate = new EditorDelegate() {
                private static final long serialVersionUID = -2852771701124401164L;
                Object                    value;
                public void setValue ( Object value ) {
                    if ( value instanceof DefaultMutableTreeNode )
                        value = ( ( DefaultMutableTreeNode )value ).getUserObject();
                    this.value = value;
                    if ( value instanceof LiftSiteBean ) {
                        textField.setText( ( ( LiftSiteBean )value ).getName() );
                    } else if ( value instanceof LiftConnectionBean ) {
                        textField.setText( ( ( LiftConnectionBean )value ).getName() );
                    } else
                        textField.setText( ( value != null )
                                           ? value.toString()
                                           : "" );
                }
                public Object getCellEditorValue () {
                    String text = textField.getText();
                    if ( value instanceof LiftSiteBean && text != null ) {
                        LiftSiteBean site = ( LiftSiteBean )value;
                        if ( ! text.equals( site.getName() ) ) {
                            if ( ! context.sites.contains( new LiftSiteBean( textField.getText() ) ) ) {
                                synchronized ( SiteEditorTree.class ) {
                                    List<LiftConnectionBean> list = context.siteMap.remove( value );
                                    site.setName( textField.getText() );
                                    for ( LiftConnectionBean conn : list )
                                        conn.setSite( site );
                                    context.siteMap.put( site, list );
                                }
                            } else {
                                if ( ! showingDialog ) {
                                    synchronized ( SiteEditorTree.class ) {
                                        showingDialog = true;
                                    }
                                    JOptionPane.showMessageDialog( SiteEditorTree.this, "site name has duplicated." );
                                    synchronized ( SiteEditorTree.class ) {
                                        showingDialog = false;
                                    }
                                }
                            }
                        }
                    } else if ( value instanceof LiftConnectionBean ) {
                        ( ( LiftConnectionBean )value ).setName( textField.getText() );
                    } else
                        return textField.getText();
                    SwingUtilities.invokeLater( new Runnable() {
                        @Override
                        public void run () {
                            refresh();
                            SiteEditorTree.this.setSelectionPath( SiteEditorTree.this.getTreePath( value ) );
                        }
                    } );
                    return value;
                }
            };
            textField.addActionListener( delegate );
            textField.addFocusListener( new FocusAdapter() {
                @Override
                public void focusLost ( FocusEvent e ) {
                    if ( SiteEditorTree.this.isEditing() ) {
                        SiteEditorTree.this.stopEditing();

                        // SiteLiftTree.this.cancelEditing();
                    }
                }
            } );
        }


        public Component getTreeCellEditorComponent ( JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf,
                                                      int row ) {
            delegate.setValue( value );
            return editorComponent;
        }


        public Object getCellEditorValue () {
            return delegate.getCellEditorValue();
        }
    }
}
