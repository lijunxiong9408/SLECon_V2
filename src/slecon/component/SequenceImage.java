package slecon.component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;




public class SequenceImage extends JLabel implements ActionListener {
    private static final long serialVersionUID = 7768265517206211257L;
    private int               totalImages      = 0;
    private int               currentImage     = 0,
                              animationDelay   = 50;
    private boolean           autoStart        = true;
    private ImageIcon[]       images;
    private Timer             animationTimer;




    public SequenceImage () {
        super();

        AncestorListener ancestorListener = new AncestorListener() {
            public void ancestorAdded ( AncestorEvent ancestorEvent ) {
                if ( isAutoStart() && isShowing() )
                    startAnimation();
            }
            public void ancestorRemoved ( AncestorEvent ancestorEvent ) {
                if ( ! SequenceImage.this.isShowing() )
                    stopAnimation();
            }
            @Override
            public void ancestorMoved ( AncestorEvent event ) {
            }
        };
        this.addAncestorListener( ancestorListener );
    }


    public SequenceImage ( File file ) throws IOException {
        setImages( file );
    }


    public SequenceImage ( ImageIcon images[], boolean autoStart ) {
        this();
        setImages( images );
        setAutoStart( autoStart );
    }


    public SequenceImage ( File file, int width, int height, boolean autoStart ) throws IOException {
        setImages( file, -1, -1 );
        setAutoStart( autoStart );
    }


    public int getAnimationDelay () {
        return animationDelay;
    }


    public boolean isAutoStart () {
        return autoStart;
    }


    public void setAnimationDelay ( int animationDelay ) {
        this.animationDelay = animationDelay;
    }


    public void setAutoStart ( boolean autoStart ) {
        this.autoStart = autoStart;
    }


    public void setImages ( File file ) throws IOException {
        setImages( file );
    }


    public void setImages ( File file, int width, int height ) throws IOException {
        BufferedImage img         = ImageIO.read( file );
        int           totalImages = img.getWidth( null ) / img.getHeight( null );
        int           img_width   = img.getWidth( null ) / totalImages;
        int           img_height  = img.getHeight( null );
        ImageIcon[]   images      = new ImageIcon[ totalImages ];
        for ( int i = 0 ; i < totalImages ; i++ ) {
            BufferedImage subimage = img.getSubimage( img_width * i, 0, img_width, img_height );
            if ( width >= 0 && height >= 0 )
                images[ i ] = new ImageIcon( subimage.getScaledInstance( width, height, Image.SCALE_SMOOTH ) );
            else
                images[ i ] = new ImageIcon( subimage );
        }
        setImages( images );
    }


    public synchronized void setImages ( ImageIcon images[] ) {
        this.images = images;
        totalImages = images.length;
        if ( currentImage < images.length && images[ currentImage ] != null
            && images[ currentImage ].getImageLoadStatus() == MediaTracker.COMPLETE ) {
            setIcon( images[ currentImage ] );
        }
        if ( currentImage >= images.length || images[ currentImage ] == null ) {
            currentImage = 0;
        }
    }


    public void startAnimation () {
        if ( animationTimer == null ) {
            currentImage   = 0;
            animationTimer = new Timer( animationDelay, ( ActionListener )this );
            animationTimer.start();
        } else if ( ! animationTimer.isRunning() )
            animationTimer.restart();
    }


    public void stopAnimation () {
        animationTimer.stop();
    }


    public synchronized void actionPerformed ( ActionEvent e ) {
        if ( e.getSource().equals( animationTimer ) && totalImages != 0 ) {
            currentImage = ( currentImage + 1 ) % totalImages;
            if ( images != null && images[ currentImage ] != null && images[ currentImage ].getImageLoadStatus() == MediaTracker.COMPLETE )
                setIcon( images[ currentImage ] );
        }
    }
}
