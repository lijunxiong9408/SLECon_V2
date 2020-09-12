package slecon.inspect.motion;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;

import base.cfg.FontFactory;
import slecon.StartUI;



public class MotionChartPanel extends ChartPanel {
    private static final long serialVersionUID = 5370113851012281593L;

    /**
     * Number of samples a screen should show.
     * The sampling frequency is 100ms and 300 = 30s a screen.
     */
    private static final int    COUNT                   = 300;
    private final static int    POSITION_SERIES_DATASET = 1;
    private final static String POSITION_SERIES_TEXT    = MotionMain.TEXT.getString("Chart.Position.text");
    private final static int    SPEED_SERIES_DATASET    = 0;
    private final static String SPEED_SERIES_TEXT       = MotionMain.TEXT.getString("Chart.Speed.text");

    ////////////////////////////////////// <Demo: UI and Main> //////////////////////////////////////
    private static final String TITLE = "MCS Position v.s. Speed";

    /**
     * The timestamp as filter to discard old records.
     */
    private long timestamp = 0;

    /**
     * The speed data.
     */
    private ConcurrentLinkedQueue<Float> speedQueue = new ConcurrentLinkedQueue<>();

    /**
     * The position in MM data.
     */
    private ConcurrentLinkedQueue<Float> positionQueue         = new ConcurrentLinkedQueue<>();
    private boolean                      axisVisible           = true;
    protected int                        domainGridLineCount   = 20;
    protected int                        speedGridLineCount    = 11;
    protected int                        positionGridLineCount = 1;

    /**
     * Timer for refresh the chart.
     */
    private Timer timer;
    private float maxSpeed;
    private float upperDoorZone;




    /**
     * Times series chart for display MCS position and speed.
     * @param title     It specifies the name of chart.
     */
    public MotionChartPanel () {
        super( null );

        // Data set.
        setLayout( new BorderLayout() );

//      setMaximumDrawWidth( Toolkit.getDefaultToolkit().getScreenSize().width );
//      setMaximumDrawHeight( Toolkit.getDefaultToolkit().getScreenSize().height );

        final DynamicTimeSeriesCollection dataset1 = new DynamicTimeSeriesCollection( 1, COUNT, new Second() );
        dataset1.setTimeBase( new Second( 0, 0, 0, 1, 1, 2011 ) );
        dataset1.addSeries( initialPositionData(), 0, POSITION_SERIES_TEXT );

        final DynamicTimeSeriesCollection dataset2 = new DynamicTimeSeriesCollection( 1, COUNT, new Second() );
        dataset2.setTimeBase( new Second( 0, 0, 0, 1, 1, 2011 ) );
        dataset2.addSeries( initialSpeedData(), 0, SPEED_SERIES_TEXT );

        // Create the chart.
        JFreeChart chart = createChart( dataset1, dataset2 );
        setChart( chart );
        setRangeBound( 200000, 2500 );
        setMouseZoomable( false );

        // Timer (Refersh the chart).
        timer = new Timer( 10, new ActionListener() {

            /**
             * The speed previous plotted.
             */
            private float prevSpeed;

            /**
             * The position previous plotted.
             */
            private float prevPosition;
            @Override
            public void actionPerformed ( ActionEvent e ) {
                if ( speedQueue.isEmpty() == false && positionQueue.isEmpty() == false ) {
                    long time = System.currentTimeMillis();
                    prevSpeed    = speedQueue.poll();
                    prevPosition = positionQueue.poll();
                    dataset1.advanceTime();
                    dataset2.advanceTime();
                    dataset1.appendData( new float[]{ prevPosition } );
                    dataset2.appendData( new float[]{ prevSpeed } );

                    long elapsedTime = System.currentTimeMillis() - time;
                }

//              else {
                // Maintain previous record once no new record/enough record could show.
//                  dataset1.appendData( new float[]{ prevPosition } );
//                  dataset2.appendData( new float[]{ prevSpeed } );
//              }
            }
        } );
    }


    /**
     * Create a new JFree chart.
     * @param dataset1   It specifies the dataset to the chart.
     * @param dataset2
     * @return Returns the instance of chart.
     */
    private JFreeChart createChart ( final XYDataset dataset1, final XYDataset dataset2 ) {
        final JFreeChart     result    = ChartFactory.createTimeSeriesChart( null, "", "", dataset1, true, true, false );
        final XYPlot         plot      = result.getXYPlot();
        final XYItemRenderer renderer1 = new XYLineAndShapeRenderer( true, false );
        final XYItemRenderer renderer2 = new XYLineAndShapeRenderer( true, false );
        plot.setDomainPannable( true );
        plot.setRangePannable( true );
        plot.setDataset( SPEED_SERIES_DATASET, dataset2 );
        plot.mapDatasetToRangeAxis( SPEED_SERIES_DATASET, SPEED_SERIES_DATASET );
        plot.setRenderer( SPEED_SERIES_DATASET, renderer1 );
        plot.setDataset( POSITION_SERIES_DATASET, dataset1 );
        plot.mapDatasetToRangeAxis( POSITION_SERIES_DATASET, POSITION_SERIES_DATASET );
        plot.setRenderer( POSITION_SERIES_DATASET, renderer2 );
        plot.setBackgroundPaint( StartUI.MAIN_BACKGROUND_COLOR );
        plot.setDomainGridlinePaint( Color.decode("0x3b4246") );
        plot.setRangeGridlinePaint( Color.decode("0x3b4246") );
        plot.setRangeGridlineStroke( new BasicStroke( 0.8f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL ) );
        plot.setDomainGridlineStroke( new BasicStroke( 0.8f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL ) );
        renderer1.setSeriesPaint( 0, Color.decode( "#AAFF44" ) );
        renderer2.setSeriesPaint( 0, Color.decode( "#EEDD00" ) );
        result.setBackgroundPaint( null );
        result.removeLegend();
        return result;
    }


    /**
     * Get the initialize speed from the queue.
     * @return Returns an array of float that specifies the initialize values of speed.
     */
    private float[] initialSpeedData () {
        float[] ret = new float[ this.speedQueue.size() ];
        for ( int i = 0, len = ret.length ; i < len ; i++ )
            ret[ i ] = this.speedQueue.poll();
        return ret;
    }


    /**
     * Get the initialize position in MM from the queue.
     * @return Returns an array of float that specifies the initialize values of position.
     */
    private float[] initialPositionData () {
        float[] ret = new float[ this.positionQueue.size() ];
        for ( int i = 0, len = ret.length ; i < len ; i++ )
            ret[ i ] = this.positionQueue.poll();
        return ret;
    }


    ////////////////////////////////////// <public interface> //////////////////////////////////////
    public final void setAxisVisible ( boolean onoff ) {
        this.axisVisible = onoff;
        updateAxis();
    }


    private void updateAxis () {
        XYPlot plot = getChart().getXYPlot();
        plot.getDomainAxis( 0 ).setVisible( axisVisible );
        plot.getRangeAxis( 0 ).setVisible( axisVisible );
        plot.getRangeAxis( 1 ).setVisible( axisVisible );
    }


    public final void clearDataQueue () {
        positionQueue.clear();
        speedQueue.clear();
    }


    public void setRangeBound ( float upperDoorZone, float maxSpeed ) {
        this.upperDoorZone = upperDoorZone;
        this.maxSpeed      = maxSpeed;

        XYPlot plot = getChart().getXYPlot();
        plot.setDomainPannable( true );
        plot.setRangePannable( true );

        DateAxis domain = ( DateAxis )plot.getDomainAxis();
        domain.setTickMarksVisible( false );
        domain.setTickLabelsVisible( false );
        domain.setTickUnit( new DateTickUnit( DateTickUnitType.SECOND, COUNT / getDomainGridLineCount() ) );

        NumberAxis range1 = new NumberAxis( POSITION_SERIES_TEXT );
        range1.setLabelPaint(Color.WHITE);
        range1.setLabelFont(FontFactory.FONT_12_BOLD);
        range1.setTickLabelPaint(Color.WHITE);
        range1.setRange( 0, upperDoorZone * 1.1 );    // Please set range according to specification.
        range1.setTickUnit( new PositionTickUnit( upperDoorZone * 1.1 / getPositionGridLineCount() ) );
        range1.setAutoRange( false );
        plot.setRangeAxis( POSITION_SERIES_DATASET, range1 );

        NumberAxis range2 = new NumberAxis( SPEED_SERIES_TEXT );
        range2.setLabelPaint(Color.WHITE);
        range2.setLabelFont(FontFactory.FONT_12_BOLD);
        range2.setTickLabelPaint(Color.WHITE);
        range2.setRange( 0, maxSpeed * 1.1 );    // Please set range according to specification.
        range2.setTickUnit( new SpeedTickUnit( maxSpeed * 1.1 / getSpeedGridLineCount() ) );
        range2.setAutoRange( false );
        plot.setRangeAxis( SPEED_SERIES_DATASET, range2 );
        updateAxis();
    }


    public final void addDataQueue ( long ts, float speed, float position ) {
        if ( isRunning() && ( timestamp == 0 || ts >= timestamp ) ) {
            positionQueue.offer( position );
            speedQueue.offer( speed );
            timestamp = ts;
        }
    }


    public final void start () {
        timer.start();
    }


    public final boolean isRunning () {
        return timer.isRunning();
    }


    public final void stop () {
        timer.stop();
    }


    public final int getDomainGridLineCount () {
        return domainGridLineCount;
    }


    public final int getSpeedGridLineCount () {
        return speedGridLineCount;
    }


    public final int getPositionGridLineCount () {
        return positionGridLineCount;
    }


    public final void setDomainGridLineCount ( int domainGridLineCount ) {
        this.domainGridLineCount = domainGridLineCount;
        setRangeBound( upperDoorZone, maxSpeed );
    }


    public final void setSpeedGridLineCount ( int speedGridLineCount ) {
        this.speedGridLineCount = speedGridLineCount;
        setRangeBound( upperDoorZone, maxSpeed );
    }


    public final void setPositionGridLineCount ( int positionGridLineCount ) {
        this.positionGridLineCount = positionGridLineCount;
        setRangeBound( upperDoorZone, maxSpeed );
    }


    @SuppressWarnings( "serial" )
    public static final class PositionTickUnit extends NumberTickUnit {
        public PositionTickUnit ( double size ) {
            super( size );
        }


        @Override
        public String valueToString ( double value ) {
            return String.format( "%d", ( int )value );
        }
    }




    @SuppressWarnings( "serial" )
    public static final class SpeedTickUnit extends NumberTickUnit {
        public SpeedTickUnit ( double size ) {
            super( size );
        }


        @Override
        public String valueToString ( double value ) {
            return String.format( "%.2f", value / 1000 );
        }
    }
}
