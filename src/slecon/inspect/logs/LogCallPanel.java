package slecon.inspect.logs;
import java.awt.Color;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import base.cfg.ImageFactory;
import net.miginfocom.swing.MigLayout;
import slecon.inspect.calls.FloorCallElement;




public class LogCallPanel extends JPanel {
    private static final long         serialVersionUID = -6476687784796491345L;
    private static final ImageIcon    DIRECTION_000    = ImageFactory.DASHBOARD_INDICATOR_000.icon();
    private static final ImageIcon    DIRECTION_001    = ImageFactory.DASHBOARD_INDICATOR_001.icon();
    private static final ImageIcon    DIRECTION_010    = ImageFactory.DASHBOARD_INDICATOR_010.icon();
    private static final ImageIcon    DIRECTION_011    = ImageFactory.DASHBOARD_INDICATOR_011.icon();
    private static final ImageIcon    DIRECTION_100    = ImageFactory.DASHBOARD_INDICATOR_100.icon();
    private static final ImageIcon    DIRECTION_101    = ImageFactory.DASHBOARD_INDICATOR_101.icon();
    private static final ImageIcon    DIRECTION_110    = ImageFactory.DASHBOARD_INDICATOR_110.icon();
    private static final ImageIcon    DIRECTION_111    = ImageFactory.DASHBOARD_INDICATOR_111.icon();
    private int                       ncol             = 12;
    private TreeSet<FloorCallElement> callSet;
    private Map<Integer, String>      floorText;




    public int getNcol () {
        return ncol;
    }


    public void setNcol ( int ncol ) {
        int old_ncol = this.ncol;
        this.ncol = ncol;
        if ( old_ncol != this.ncol )
            resetGUI();
    }

    private void resetGUI () {
        setOpaque( false );
        removeAll();
        revalidate();
        if ( callSet != null && floorText != null && ! callSet.isEmpty() && ! floorText.isEmpty() ) {
            this.invalidate();

            StringBuffer              colConstraints = new StringBuffer();
            StringBuffer              rowConstraints = new StringBuffer();
            TreeSet<FloorCallElement> callSet        = getCallSet();
            for ( int i = 0 ; i < callSet.size() ; i++ )
                colConstraints.append( "[center, fill, 20!]" );
            for ( int i = 0 ; i < new Double( Math.ceil( ( ( float )callSet.size() ) / ncol ) ).intValue() ; i++ )
                rowConstraints.append( "[center]" );

            MigLayout layout = new MigLayout( "ins 0, gap 0, wmax " + 20 * getNcol(), colConstraints.toString(), rowConstraints.toString() );
            layout.setRowConstraints( rowConstraints.toString() );
            setLayout( layout );
            
            this.validate();
        }
        repaint();
    }


    public TreeSet<FloorCallElement> getCallSet () {
        return callSet;
    }


    public Map<Integer, String> getFloorText () {
        return floorText;
    }


    public void setData ( Map<Integer, String> floorText, TreeSet<FloorCallElement> callSet ) {
        this.callSet   = callSet;
        this.floorText = floorText;
        this.resetGUI();
    }
}
