package slecon.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class NoPanel extends JPanel {
    private static final long serialVersionUID = -8462998145878722784L;
    private static final String text = "Please select an item.";


    /**
     * Create the panel.
     */
    public NoPanel () {
        setBackground( Color.white );
    }


    public void paintComponent ( Graphics g ) {
        super.paintComponent( g );

        Graphics2D g2d;
        g2d = ( Graphics2D )g.create();

        g2d.setColor( Color.GRAY.darker() );

        Font font = getFont().deriveFont( Font.BOLD, 48 );
        g2d.setFont( font );
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth( text );
        g2d.drawString( text, ( int )( getWidth() - width ) / 2, ( int )( getHeight() ) / 2 );

        // done with g2d, dispose it
        g2d.dispose();
    }
}
