package slecon.component;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Painter;




/**
 * this is for painting a component using a mono-color or a image.
 *
 * @see javax.swing.Painter
 */
public class FlagPainter<T> implements Painter<T> {
    private Image bgimage;
    private Color bgcolor;




    /**
     * the component is painted using the color.
     *
     * @param c
     */
    public FlagPainter ( Color c ) {
        this.bgcolor = c;
    }


    /**
     * the component is painted using the image.
     *
     * @param image
     */
    public FlagPainter ( Image image ) {
        this.bgimage = image;
    }


    /*
     * (non-Javadoc)
     *
     * @see javax.swing.Painter#paint(java.awt.Graphics2D, java.lang.Object, int, int)
     */
    public void paint ( Graphics2D g, T arg1, int width, int height ) {
        if ( bgcolor == null ) {
            g.setColor( Color.white );
            g.fillRect( 0, 0, width, height );

            int imwidth  = bgimage.getWidth( null );
            int imheight = bgimage.getHeight( null );
            int srcx     = ( imwidth - width ) > 0
                           ? imwidth - width - 1
                           : 0;
            int srcy     = ( imheight - height ) > 0
                           ? imheight - height - 1
                           : 0;
            g.drawImage( bgimage, 0, 0, width, imheight, srcx, srcy, imwidth, imheight, null );
        } else {
            g.setPaint( bgcolor );
            g.fillRect( 0, 0, width, height );
        }
    }
}
