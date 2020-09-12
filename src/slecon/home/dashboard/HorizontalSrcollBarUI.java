package slecon.home.dashboard;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

import base.cfg.ImageFactory;
import slecon.StartUI;
import slecon.home.PosButton;


public class HorizontalSrcollBarUI extends BasicScrollBarUI {

    // hand shank width.
    private static final int thumbWidth = 20;

    // hand shank opaque. 
    private static final float opaque = 0.5f;
    
    // hand shank border color.
    private static final Color thumbColor = StartUI.BORDER_COLOR;

    // hand shank color.
    private static final Color thumbColorFrom = StartUI.BORDER_COLOR;
    private static final Color thumbColorTo = StartUI.BORDER_COLOR;

    // slide color.
    private static final Color backColorFrom = StartUI.SUB_BACKGROUND_COLOR;
    private static final Color backColorTo = StartUI.SUB_BACKGROUND_COLOR;

    @Override
    protected void configureScrollBarColors() {
    	trackColor = Color.black;
        setThumbBounds(0, 0, 0, 10);
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {

        // TODO Auto-generated method stub

        //        c.setPreferredSize(new Dimension(thumbWidth, 0));
        c.setPreferredSize(new Dimension(0, thumbWidth));

        return super.getPreferredSize(c);

    }

    // background color.
    public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {

        Graphics2D g2 = (Graphics2D) g;

        GradientPaint gp = null;

        if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            gp = new GradientPaint(0, 0, backColorFrom, 0, trackBounds.height, backColorTo);

        }

        if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
            gp = new GradientPaint(0, 0, backColorFrom, trackBounds.width, 0, backColorTo);
        }

        g2.setPaint(gp);

        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width , trackBounds.height);

         g2.setColor(StartUI.BORDER_COLOR);
         g2.drawRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);

        if (trackHighlight == BasicScrollBarUI.DECREASE_HIGHLIGHT)
            this.paintDecreaseHighlight(g);

        if (trackHighlight == BasicScrollBarUI.INCREASE_HIGHLIGHT)
            this.paintIncreaseHighlight(g);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {

        g.translate(thumbBounds.x, thumbBounds.y);

        g.setColor(thumbColor);

        Graphics2D g2 = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.addRenderingHints(rh);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opaque));

        g2.fillRoundRect(0, 0, thumbBounds.width, thumbBounds.height, 5, 5);

    }

    /**
     * Up Button.
     */
    @Override
    protected JButton createIncreaseButton(int orientation) {

    	PosButton	btn = new PosButton(ImageFactory.ARROW_SCROLL_LEFT.icon(20, 20));
    	btn.setBorder(null);
        btn.setFocusable(false);
        return btn;

    }

    /**
     *	Down Button.
     */
    @Override
    protected JButton createDecreaseButton(int orientation) {

		PosButton	btn = new PosButton(ImageFactory.ARROW_SCROLL_RIGHT.icon(20, 20));
		btn.setBorder(null);
        btn.setFocusable(false);
        return btn;

    }
}