package slecon.home;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import slecon.StartUI;




public class PosButton extends JButton {
    private static final long serialVersionUID = 5784188344638431556L;
    public PosButton ( Action action, int width, int height ) {
        super( action );
        init(width, height);
    }

    public PosButton ( String text, int width, int height ) {
        super( text );
        init(width, height);
    }
    
    public PosButton ( String text, Color normal, Color activity ) {
        super( text );
        dynamicColor(normal, activity);
    }
    
    public PosButton ( String text, ImageIcon icon ) {
        super( text, icon );
        normal();
    }
    
    public PosButton ( ImageIcon icon ) {
    	super( icon );
        normal();
    }
    
    public PosButton ( ImageIcon normal, ImageIcon activity ) {
    	dynamicImage(normal, activity);
    }
    
    public PosButton ( String text, ImageIcon normal, ImageIcon activity ) {
    	super(text);
    	dynamicImage(normal, activity);
    }
    
    private void common() {
    	setMargin( new Insets( 0, 0, 0, 0 ) );
        setOpaque( false );
        setContentAreaFilled(false);
        setForeground( Color.WHITE );
        setFocusPainted(false);
        setBorderPainted( false );
        setHorizontalAlignment( SwingConstants.CENTER );
        setVerticalTextPosition( SwingConstants.CENTER );
        setHorizontalTextPosition( SwingConstants.CENTER );
    }
    
    private void init (int width, int height) {
        setFont( FontFactory.FONT_14_BOLD );
        setPreferredSize( new Dimension( width, height ) );
        common();
        setCursor(Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ));
    }
    
    private void normal () {
    	common();
    	setCursor(Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ));
    }
    
    private void dynamicImage ( final ImageIcon normal, final ImageIcon activity ) {
    	setIcon(normal);
    	common();
        addMouseListener(new MouseListener() {
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			// TODO Auto-generated method stub
    			setIcon(normal);
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			// TODO Auto-generated method stub
    			setIcon(activity);
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			// TODO Auto-generated method stub
    			//setIcon(normal);
	            Cursor cursor = Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR );
	            setCursor( cursor );
    		}
    		
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			// TODO Auto-generated method stub
    			//setIcon(activity);
    			Cursor cursor = Cursor.getPredefinedCursor( Cursor.HAND_CURSOR );
	            setCursor( cursor );
    		}
    		
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			// TODO Auto-generated method stub
    			setIcon(normal);
    		}
    	});
    }
    
    private void dynamicColor ( final Color normal, final Color activity ) {
    	common();
    	addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				setForeground( normal );
	            Cursor cursor = Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR );
	            setCursor( cursor );
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				setForeground( activity );
	            Cursor cursor = Cursor.getPredefinedCursor( Cursor.HAND_CURSOR );
	            setCursor( cursor );
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    }
}
