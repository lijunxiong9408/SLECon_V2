package slecon.component;
import java.awt.*;   
import javax.swing.*;   
import javax.swing.border.*;   
  
public class SubtleSquareBorder extends AbstractBorder implements Border   
{   
	private static final long serialVersionUID = 8863151827637226471L;
	protected int m_w = 0;   
	protected int m_h = 0;   
	protected Color m_topColor;   
	protected Color m_bottomColor;  
	protected boolean roundc = false; // Do we want rounded corners on the border?   
	public SubtleSquareBorder(boolean round_corners, Color color)   
	{   
        roundc = round_corners;   
        m_topColor = color;
        m_bottomColor = color;
	}   
	public Insets getBorderInsets(Component c)   
	{   
		return new Insets(m_h, m_w, m_h, m_w);   
	}   
	public boolean isBorderOpaque()   
	{   
		return true;   
	}   
	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)   
	{   
		w = w - 3;   
		h = h - 3;   
		x ++;   
		y ++;   
		// Rounded corners   
		if(roundc)   
		{   
			g.setColor(m_topColor);
			g.drawLine(x, y + 3, x, y + h - 3);   
			g.drawLine(x + 3, y, x + w - 3, y);   
			g.drawLine(x, y + 3, x + 3, y); // Top left diagonal   
			g.drawLine(x, y + h - 3, x + 3, y + h); // Bottom left diagonal   
			g.setColor(m_bottomColor);   
			g.drawLine(x + w, y + 3, x + w, y + h - 3);   
			g.drawLine(x + 3, y + h, x + w -3, y + h);   
			g.drawLine(x + w - 3, y, x + w, y + 3); // Top right diagonal   
			g.drawLine(x + w, y + h - 3, x + w -3, y + h); // Bottom right diagonal   
		}   
		// Square corners   
		else  
		{   
			g.setColor(m_topColor);   
			g.drawLine(x, y, x, y + h);   
			g.drawLine(x, y, x + w, y);   
			g.setColor(m_bottomColor);   
			g.drawLine(x + w, y, x + w, y + h);   
			g.drawLine(x, y + h, x + w, y + h);   
		}   
	}   
} 