package slecon.home.dashboard;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import logic.Dict;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import base.cfg.FontFactory;




public class MotionSubPanel extends JPanel implements PropertyChangeListener {
    private static final long    serialVersionUID = 6655277775329579036L;
    private HomeChart chart;




    public MotionSubPanel () {
        initGUI();
        addPropertyChangeListener( "enabled", this);
    }
    
    
    @Override
    public void propertyChange ( PropertyChangeEvent evt ) {
        chart.setVisible( isEnabled() );
    }
    
    public void paint(Graphics g) {
        super.paint( g );
        if (!isEnabled()) {
            Font f = g.getFont();
            Color c = g.getColor();
            g.setColor( Color.WHITE );
            g.setFont( FontFactory.FONT_20_PLAIN );

            String str = Dict.lookup( "Empty" );
            final FontMetrics fontMetrics = g.getFontMetrics();
            Rectangle2D stringBounds = fontMetrics.getStringBounds( str, g );
            int stringW = ( int )stringBounds.getWidth();
            int stringH = ( int )stringBounds.getHeight();

            g.drawString( str, ( getWidth() - stringW ) / 2, ( getHeight() - stringH ) / 2 + fontMetrics.getAscent() );

            g.setFont( f );
            g.setColor( c );
        }
    }


    private void initGUI () {
        setOpaque( false );
        setLayout( new MigLayout( "fill, ins 0", "[center]", "[center]" ) );
        newChart();
    }


    public final void newChart () {
        stop();
        removeAll();
        chart = new HomeChart();
        add(chart);
    }
    
    public void stop() {
        if(chart!=null)
            chart.stop();
    }

    public final void addToMotionChartDataQueue ( long ts, float position, float speed ) {
        chart.addDataRecord(ts, new float[]{ position, speed } );
    }

    public void setRangeBound ( float upperFloor, float contractSpeed ) {
        chart.setCurveColor( new Color[]{ StartUI.SHUTCUT_Button_COLOR, StartUI.MOTION_Button_COLOR} );
        chart.setBoundValue( new float[] { upperFloor, contractSpeed } );
    }

    public void setChartPanelMessage ( float position, float speed ) {
        chart.setPositionLegendText( String.format( "Position: %.1f mm", position), String.format( "Speed: %.2f mm/s", speed) );
    }
    
}
