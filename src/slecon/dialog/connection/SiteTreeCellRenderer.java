package slecon.dialog.connection;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import base.cfg.ImageFactory;
import logic.connection.LiftConnectionBean;
import slecon.StartUI;
import slecon.component.tree.PageTree;
import slecon.component.tree.PageTreeItem;




/**
 * the target is
 * <ol>
 * <li>if the size of tree-item is over the tree size, it will shown the scrollbar. the first target is the size is over 120px, the item will show '...'</li>
 * <li>tooltip</li>
 * </ol>
 * @author superb8
 *
 */
public final class SiteTreeCellRenderer extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = 560734417332582511L;
    private static final ImageIcon      TRIANGLE_RIGHT      = ImageFactory.ARROW_TREE_CLOSE.icon(8, 8);
    private static final ImageIcon      TRIANGLE_DOWN 		= ImageFactory.ARROW_TREE_OPEN.icon(8, 8);



    public SiteTreeCellRenderer () {
        setBackgroundNonSelectionColor( null );
        setBackgroundSelectionColor( null );
        // setBorderSelectionColor(null);
        setLeafIcon(TRIANGLE_RIGHT);
        setOpenIcon(TRIANGLE_DOWN);
        setClosedIcon(TRIANGLE_RIGHT);
    }


    public Component getTreeCellRendererComponent ( JTree tree, Object value, boolean selection, boolean expanded, boolean leaf, int row,
                                                    boolean hasFocus ) {
        Component c = super.getTreeCellRendererComponent( tree, value, selection, expanded, leaf, row, hasFocus );
        DefaultMutableTreeNode node = ( DefaultMutableTreeNode )value;
        setPreferredSize( new Dimension(120, 25) );
        tree.setRowHeight(25);
        setForeground( Color.WHITE );
        
        
        return this;
    }
}
