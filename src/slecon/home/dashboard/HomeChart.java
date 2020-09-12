package slecon.home.dashboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.Timer;

import slecon.StartUI;


public class HomeChart extends JPanel implements ActionListener {
    
    private static final long serialVersionUID = 2295589989654186502L;
    
    Paint                         background   = new GradientPaint( 0, 0, Color.decode( "#181818" ), 0, 80, Color.decode( "#000000" ) );
    Paint                         gridColor    = Color.decode( "#444444" );
    Stroke                        gridStroke   = new BasicStroke( 1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f );
    Paint                         curveColor[] = new Paint[]{ StartUI.SHUTCUT_Button_COLOR, StartUI.MOTION_Button_COLOR };
    Paint                         positionlegendColor = StartUI.SHUTCUT_Button_COLOR;
    Paint                         speedlegendColor = StartUI.MOTION_Button_COLOR;
    private volatile boolean      needRepaint  = false;
    private LinkedList<DataPoint> dataset      = new LinkedList<>();
    private Timer                 repaintTimer;
    private float[]               boundValue;
    private String                positionlegendText;
    private String                speedlegendText;




    public HomeChart () {
        initGUI();
        repaintTimer.start();
    }


    public Paint[] getCurveColor () {
        return curveColor;
    }


    public void setCurveColor ( Paint[] curveColor ) {
        this.curveColor = curveColor;
    }
    
    @Override
    public Dimension getSize() {
        return new Dimension( 780, 150 );
    }


    private void initGUI () {
        setPreferredSize( new Dimension( 780, 150 ) );
        repaintTimer = new Timer( 100, this );
    }


    protected void drawBackground ( Graphics2D g ) {
        g.setPaint( background );
        g.fillRect( 0, 0, getWidth(), getHeight() );
    }


    protected void drawGrid ( Graphics2D g ) {
        g.setStroke( gridStroke );
        g.setPaint( gridColor );

        // draw horizontal line
        float minSpanY = getHeight() / 8.0f;
        float y        = minSpanY;
        while ( y <= getHeight() ) {
            g.drawLine( 0, ( int )y, getWidth(), ( int )y );
            y += minSpanY;
        }

        // draw vertical line
        float minSpanX = 20;
        float x        = minSpanX;
        while ( x <= getWidth() ) {
            g.drawLine( ( int )x, 0, ( int )x, getHeight() );
            x += minSpanX;
        }
    }


    protected void drawLegend ( Graphics2D g ) {
        String positon = getPositionLegendText();
        if ( positon != null && positon.trim().length() > 0 ) {
            g.setFont( getFont() );
            g.setPaint( positionlegendColor );

            int height = g.getFontMetrics().getHeight();
            drawString( g, positon, 5, 5 + height / 2 );
        }
        
        String speed = getSpeedLegendText();
        if ( speed != null && speed.trim().length() > 0 ) {
            g.setFont( getFont() );
            g.setPaint( speedlegendColor );

            int height = g.getFontMetrics().getHeight();
            drawString( g, speed, 5, 10 + height );
        }
        
    }


    private void drawString ( Graphics g, String text, int x, int y ) {
        for ( String line : text.split( "\n" ) ) {
            g.drawString( line, x, y );
            y += g.getFontMetrics().getHeight();
        }
    }


    protected void drawCurve ( Graphics2D g ) {
        int  y1      = 0;
        long startTs = 0;
        synchronized ( dataset ) {
            if ( boundValue != null )
                for ( int i = 0 ; i < boundValue.length ; i++ ) {
                    Paint paint = ( curveColor != null && curveColor.length > i )
                                  ? curveColor[ i ]
                                  : Color.getHSBColor( ( float )Math.random(), ( float )( Math.random() * 0.2 + 0.2 ), 0.9f );
                    g.setPaint( paint );

                    int cur = 0;
                    for ( int x = 0 ; x < 780 ; x++ ) {
                        if ( x == 0 && cur < dataset.size() ) {
                            DataPoint point = dataset.get( cur );
                            y1      = ( int )( getHeight() * ( 1.0 - ( point.data[ i ] / boundValue[ i ] ) * ( 7f / 8f ) ) );
                            startTs = point.ts;
                            continue;
                        }

                        long ts = startTs + 180 * x;
                        for ( ; cur < dataset.size() ; cur++ ) {
                            if ( dataset.get( cur ).ts >= ts )
                                break;
                        }
                        if ( cur >= dataset.size() )
                            break;

                        DataPoint point = dataset.get( cur );
                        int       y2    = y1;
                        if ( ! Float.isNaN( point.data[ i ] ) )
                            y2 = ( int )( getHeight() * ( 1.0 - ( point.data[ i ] / boundValue[ i ] ) * ( 7f / 8f ) ) );
                        g.drawLine( x - 1, y1, x, y2 );
                        y1 = y2;
                    }
                }
        }
    }


    public void paintComponent ( Graphics g ) {
        super.paintComponent( g );

        ( ( Graphics2D )g ).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        drawBackground( ( Graphics2D )g );
        drawGrid( ( Graphics2D )g );

        if ( isEnabled() ) {
            drawCurve( ( Graphics2D )g );
            drawLegend( ( Graphics2D )g );
        }
    }


    public String getPositionLegendText () {
        return positionlegendText;
    }

    public String getSpeedLegendText () {
        return speedlegendText;
    }

    public void setPositionLegendText ( String position, String speed ) {
        this.positionlegendText = position;
        this.speedlegendText = speed;
        needRepaint = true;
    }


    public float[] getBoundValue () {
        return boundValue;
    }


    public void setBoundValue ( float[] data ) {
        this.boundValue = data;
        needRepaint     = true;
    }


    public void addDataRecord ( long ts, float[] data ) {
        synchronized ( dataset ) {
            dataset.addLast( new DataPoint( ts, data ) );
            for ( int i = 0 ; i < dataset.size() ; i++ )
                if ( dataset.get( i ).ts < ts - 78000 ) {
                    dataset.remove( i );
                    i -= 1;
                }
        }
        needRepaint = true;
    }
    
    
    public void stop() {
        repaintTimer.stop();
    }


    @Override
    public void actionPerformed ( ActionEvent e ) {
        if ( e.getSource() == repaintTimer ) {
            if ( needRepaint ) {
                repaint();
                needRepaint = false;
            }
        }
    }


    static final class DataPoint {
        final long    ts;
        final float[] data;




        public DataPoint ( long ts, float[] data ) {
            this.ts   = ts;
            this.data = data;
        }
    }
}
