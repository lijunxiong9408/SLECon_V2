package slecon.home.dashboard;
 
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

import base.cfg.ImageFactory;
import slecon.StartUI;
import slecon.home.PosButton;
 

public class VerticalSrcollBarUI extends BasicScrollBarUI {
	
    @Override
    protected void configureScrollBarColors() {

    	thumbColor = StartUI.BORDER_COLOR;
 
        trackColor = StartUI.SUB_BACKGROUND_COLOR;
 
        setThumbBounds(0, 0, 3, 10);
 
    }
 
    @Override
    public Dimension getPreferredSize(JComponent c) {
 
        // TODO Auto-generated method stub
 
        c.setPreferredSize(new Dimension(20, 0));
 
        return super.getPreferredSize(c);
 
    }
 
 
    public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
 
        Graphics2D g2 = (Graphics2D) g;
 
        GradientPaint gp = null;
 
        if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
 
            gp = new GradientPaint(0, 15, StartUI.SUB_BACKGROUND_COLOR,
 
                    trackBounds.width, 15, StartUI.SUB_BACKGROUND_COLOR );
 
        }
 
        if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
 
        	gp = new GradientPaint(0, 15, StartUI.SUB_BACKGROUND_COLOR,
        			 
                    trackBounds.width, 15, StartUI.SUB_BACKGROUND_COLOR );
 
        }
 
        g2.setPaint(gp);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width,
 
                trackBounds.height);
        
        
        // Border.
        g2.setColor(StartUI.BORDER_COLOR);
        g2.drawRect(trackBounds.x, trackBounds.y -1, trackBounds.width - 1,
                trackBounds.height + 1);

 
        if (trackHighlight == BasicScrollBarUI.DECREASE_HIGHLIGHT)
 
            this.paintDecreaseHighlight(g);
 
        if (trackHighlight == BasicScrollBarUI.INCREASE_HIGHLIGHT)
 
            this.paintIncreaseHighlight(g);
 
    }
 
 
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
 
        g.translate(thumbBounds.x, thumbBounds.y);
 
        g.setColor( StartUI.BORDER_COLOR );

        Graphics2D g2 = (Graphics2D) g;
 
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
 
                RenderingHints.VALUE_ANTIALIAS_ON);
 
        g2.addRenderingHints(rh);
 
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
 
                0.5f));
 
        g2.fillRoundRect(0, 0, 40, thumbBounds.height, 5, 5);
 
    }
 
    @Override
    protected JButton createIncreaseButton(int orientation) {
 
        PosButton	btn = new PosButton(ImageFactory.ARROW_SCROLL_DOWN.icon(20, 20));
        btn.setBorder(null);
        btn.setFocusable(false);
        
        return btn;
    }
 
    @Override
    protected JButton createDecreaseButton(int orientation) {
    	
    	 PosButton	btn = new PosButton(ImageFactory.ARROW_SCROLL_UP.icon(20, 20));
    	 btn.setBorder(null);
    	 btn.setFocusable(false);
 
    	 return	btn;
    }
 
}
