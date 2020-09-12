package slecon.home;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import logic.Dict;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import configure.PageLookup;
import configure.PageLookup.HomeItem;




public class ShortcutPanel extends JPanel {
    private static final long             serialVersionUID = -3375465177457858272L;
    private Color                         startColor       = StartUI.MAIN_BACKGROUND_COLOR;
    private Color                         endColor         = StartUI.MAIN_BACKGROUND_COLOR;
    private HomePanel                     homePanel;
    private static ImageIcon 	SHORTCUT_NORMAL = ImageFactory.SHORTCUT_NORMAL.icon(160,40);
    private static ImageIcon 	SHORTCUT_ACTIVITY = ImageFactory.SHORTCUT_ACTIVITY.icon(160,40);
    
    static PosButton    btn_OverView;
    static PosButton    btn_Install;
    static PosButton    btn_Review;
    static PosButton    btn_Setup;
    
    public ShortcutPanel ( HomePanel homePanel ) {
        super();
        this.homePanel = homePanel;
        initGUI();
    }

    public static final void btn_trigger(int index) {
    	StartUI.getTopMain().setEnabled(true);
    	btn_OverView.setIcon(index == 1 ? SHORTCUT_ACTIVITY : SHORTCUT_NORMAL);
        btn_Install.setIcon(index == 2 ? SHORTCUT_ACTIVITY : SHORTCUT_NORMAL);
		btn_Review.setIcon(index == 3 ? SHORTCUT_ACTIVITY : SHORTCUT_NORMAL);
		btn_Setup.setIcon(index == 4 ? SHORTCUT_ACTIVITY : SHORTCUT_NORMAL);
    }
    
    @Override
    public void paintComponent ( Graphics g ) {
        int width  = getWidth();
        int height = getHeight();

        // Create the gradient paint
        GradientPaint paint = new GradientPaint( 0, 0, startColor, 0, height, endColor, true );

        // we need to cast to Graphics2D for this operation
        Graphics2D g2d = ( Graphics2D )g;

        // save the old paint
        Paint oldPaint = g2d.getPaint();

        // set the paint to use for this operation
        g2d.setPaint( paint );

        // fill the background using the paint
        g2d.fillRect( 0, 0, width, height );

        // restore the original paint
        g2d.setPaint( oldPaint );
        super.paintComponent( g );
    }
    
    protected void initGUI () {
    	setOpaque( false );
        setLayout( new MigLayout( "wrap 0, gap 0, insets 30 0 30 0", "[grow,center]",
                                  "[center]20[center]20[center]20[center]" ) );
        
        /*OverView*/
        final HomeItem	overviewitem = PageLookup.getHomePageClass().get(0);
        String       text     = Dict.lookup( overviewitem.text );
        btn_OverView = new PosButton( text, 150, 40 );
        btn_OverView.setIcon(SHORTCUT_ACTIVITY);
        btn_OverView.setForeground(Color.WHITE);
        btn_OverView.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                if ( ! ( overviewitem.btnClass.equals( homePanel.getMainPanel().getClass() ) ) ) {
                    homePanel.installMainClass( ( Class<? extends JPanel> )overviewitem.btnClass );
                }
            }
        } );
        add( btn_OverView );
        
        /*Install*/
        final HomeItem	installitem = PageLookup.getHomePageClass().get(1);
        try {
	        Action    action = ( Action )installitem.btnClass.newInstance();
	        btn_Install	= new PosButton( action, 150, 40 );
	        btn_Install.setIcon(SHORTCUT_NORMAL);
	        btn_Install.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed ( ActionEvent e ) {
	            	btn_trigger(2);
	            	StartUI.getTopMain().setEnabled(false);
	            }
	        } );
	        add( btn_Install );
        } catch ( InstantiationException | IllegalAccessException e ) {
            e.printStackTrace();
        }
        
        /*Review*/
        final HomeItem reviewitem = PageLookup.getHomePageClass().get(2);
        try {
	        Action    action = ( Action )reviewitem.btnClass.newInstance();
	        btn_Review	= new PosButton( action, 150, 40 );
	        btn_Review.setIcon(SHORTCUT_NORMAL);
	        btn_Review.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed ( ActionEvent e ) {
	            	btn_trigger(3);
	            	StartUI.getTopMain().setEnabled(false);
	            }
	        } );
	        add( btn_Review );
        } catch ( InstantiationException | IllegalAccessException e ) {
            e.printStackTrace();
        }
        
        /*Setup*/
        final HomeItem setupitem = PageLookup.getHomePageClass().get(3);
        try {
	        Action    action = ( Action )setupitem.btnClass.newInstance();
	        btn_Setup	= new PosButton( action, 150, 40 );
	        btn_Setup.setIcon(SHORTCUT_NORMAL);
	        btn_Setup.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed ( ActionEvent e ) {
	            	btn_trigger(4);
	            	StartUI.getTopMain().setEnabled(false);
	            }
	        } );
	        add( btn_Setup );
        } catch ( InstantiationException | IllegalAccessException e ) {
            e.printStackTrace();
        }
        
    }
}
