package slecon.component.tree;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.dialog.connection.SiteEditorTree;
import slecon.home.dashboard.VerticalSrcollBarUI;




/**
 * this is the left panel(setup/inspect) wrapping the treeview.
 *
 * @author henry.tam
 *
 */
public class MyTreeWrap extends JPanel {
    private static final long     serialVersionUID = -9199308412511638238L;
    private PageTree              tree;
    private TransparentScrollPane scrollpane;




    public MyTreeWrap ( PageTree tree ) {
        this.tree = tree;
        tree.setTreeWrap(this);
        initGUI();
    }
    
    @Override
    public void paintComponent ( Graphics g ) {
        int width  = getWidth();
        int height = getHeight();

        // Create the gradient paint
        GradientPaint paint = new GradientPaint( 0, 0, StartUI.MAIN_BACKGROUND_COLOR, 0, height, StartUI.MAIN_BACKGROUND_COLOR, true );

        // we need to cast to Graphics2D for this operation
        Graphics2D g2d = ( Graphics2D )g;

        // save the old paint
        Paint oldPaint = g2d.getPaint();

        // set the paint to use for this operation
        g2d.setPaint( paint );

        // fill the background using the paint
        g2d.fillRect( 0, 0, width, height );

        // restore the original paint
        g2d.setPaint( oldPaint );
        super.paintComponent( g );
    }


    private void initGUI () {
        setOpaque( false );
        setLayout( new MigLayout( "insets 0", "[240px,grow]", "[240px,grow]" ) );
        tree.setOpaque( false );
        tree.expandAll( true );

        JPanel panel = new JPanel( new BorderLayout() );
        panel.setOpaque( false );
        scrollpane = new TransparentScrollPane( tree );
        scrollpane.setViewportBorder(BorderFactory.createEmptyBorder(14, 15, 14, 15));
        scrollpane.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
        panel.add( scrollpane );
        add( panel, "cell 0 0,grow" );
    }


    public int getScrollPaneX () {
        return scrollpane.getHorizontalScrollBar().getValue();
    }


    public int getScrollPaneY () {
        return scrollpane.getVerticalScrollBar().getValue();
    }


    public void setScrollPaneX ( int x ) {
        scrollpane.getHorizontalScrollBar().setValue( x );
    }


    public void setScrollPaneY ( int y ) {
        scrollpane.getVerticalScrollBar().setValue( y );
    }


    public static final class TransparentScrollPane extends JScrollPane {
        private static final long serialVersionUID = -7202091723863488498L;
        {
            setBorder( null );
            getViewport().setBorder( null );
            setOpaque( false );
            getViewport().setOpaque( false );
        }


        public TransparentScrollPane ( Component view ) {
            super( view );
        }
    }
}
