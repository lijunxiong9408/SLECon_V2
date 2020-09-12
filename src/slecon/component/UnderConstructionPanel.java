package slecon.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class UnderConstructionPanel extends JPanel {
    private static final long   serialVersionUID = - 7164073222006514894L;
    private static final String text             = "Under Construction";


    public UnderConstructionPanel () {
        setBackground( Color.white );
    }


    public void paintComponent ( Graphics g ) {
        super.paintComponent( g );

        Graphics2D g2d;
        g2d = ( Graphics2D )g.create();

        // rotated 45 degrees around origin
        g2d.rotate( Math.toRadians( - 45 ), getWidth() / 2, getHeight() / 2 );
        g2d.setColor( Color.GRAY.darker() );

        Font font = getFont().deriveFont( Font.BOLD, 64 );
        g2d.setFont( font );

        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth( text );
        g2d.drawString( text, ( int )( getWidth() - width ) / 2, ( int )( getHeight() ) / 2 );

        // done with g2d, dispose it
        g2d.dispose();
    }
}
