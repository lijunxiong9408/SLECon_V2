package slecon.component.tree;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import logic.Dict;




/**
 * the item of the treeview(setup/inspect)
 * <p>
 * It inherited {@link DefaultMutableTreeNode}, and I add in 3 new property.
 * <ul>
 * <li>{@link #getSort} sortedKey</li>
 * <li>{@link #getItem} the text will be shown in the item. (lookup the Dict file)</li>
 * <li>{@link #getKey}  the path like this format Device::I/O::Hall</li>
 * </ul>
 * It support I18N. the item will lookup the ./data/Dict.properties file to show the text.
 * </p>
 * @author superb8
 *
 */
public class PageTreeItem extends DefaultMutableTreeNode implements Comparator<Object> {
    private static final long             serialVersionUID = -2081716955705149721L;
    private final String                  displayName;    // the text
    private final int                     sort;           // where the item display
    private final String                  key;            // eg. Devices::I/O::Hall
    private final Class<? extends JPanel> panelClass;




    public PageTreeItem ( int sort, String key, String term, Class<? extends JPanel> panelClass ) {
        this( sort, key, term, panelClass, true );
    }


    public PageTreeItem ( int sort, String key, String term, Class<? extends JPanel> panelClass, boolean b ) {
        super( term, b );
        this.key        = key;
        this.sort       = sort;
        this.panelClass = panelClass;
        
        String str = Dict.lookup( getBundleKey(term), false );
        this.displayName = ( str == null || str.trim().length() == 0 )
                      ? term
                      : str;
    }


    public String getKey () {
        return key;
    }


    public int getSort () {
        return sort;
    }


    public Class<? extends JPanel> getPanelClass () {
        return panelClass;
    }


    public String getBundleKey (String term) {
        StringBuffer sb  = new StringBuffer();
        String[]     arr = term.split( " " );
        for ( int i = 0 ; i < arr.length ; i++ ) {
            if ( arr[ i ] != null ) {
                String s = arr[ i ];
                s = s.replace( '(', '_' );
                s = s.replace( ')', '_' );
                s = s.replace( '*', '_' );
                s = s.replace( '-', '_' );
                s = s.replace( '.', '_' );
                s = s.replace( ',', '_' );
                s = s.replace( ':', '_' );
                s = s.replace( '\'', '_' );
                sb.append( s );
                if ( i + 1 < arr.length ) {
                    sb.append( "_" );
                }
            }
        }

        String key = sb.toString().replace( "__", "_" );
        if ( key.endsWith( "_" ) ) {
            return key.substring( 0, key.length() - 1 );
        }
        return key;
    }


    public String toString () {
        return displayName;
    }


    @Override
    public int compare ( Object o1, Object o2 ) {
        if ( o1 instanceof PageTreeItem && o2 instanceof PageTreeItem ) {
            PageTreeItem item1 = ( PageTreeItem )o1;
            PageTreeItem item2 = ( PageTreeItem )o2;
            return new Integer( item1.getSort() ).compareTo( item2.getSort() );
        }
        return 0;
    }
}
