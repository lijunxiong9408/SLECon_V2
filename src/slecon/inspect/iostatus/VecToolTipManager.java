package slecon.inspect.iostatus;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class VecToolTipManager {
    private final JComponent parent;
    private final Timer      timer = new Timer( 500, new refreshTipAction() );
    private Popup            tooltipContainer;
    private JComponent       focusComponent;
    private JToolTip         tooltip;




    public VecToolTipManager () {
        this( null );
    }


    public VecToolTipManager ( JComponent parent ) {
        this.parent = parent;
    }


    public JToolTip createToolTip () {
        JToolTip tip = new JToolTip();
        tip.setComponent( parent );
        return tip;
    }


    public void hideToolTipPopup () {
        if ( tooltipContainer != null ) {
            tooltipContainer.hide();
            tooltipContainer = null;
            tooltip = null;
            if ( timer != null )
                timer.stop();
            focusComponent = null;
        }
    }


    public void registerToolTip ( JComponent comp ) {
        comp.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseExited ( MouseEvent e ) {
                hideToolTipPopup();
            }


            @Override
            public void mouseEntered ( MouseEvent e ) {
                hideToolTipPopup();
                if ( e.getComponent() instanceof JComponent ) {
                    checkTipTextChange( ( JComponent )e.getComponent(), e );
                    timer.setRepeats( false );
                    timer.start();
                }
            }
        } );
    }


    private void checkTipTextChange ( final JComponent comp, MouseEvent e ) {
        String text = comp.getToolTipText( e );
        if ( text != null && ( tooltip == null || ! text.equals( tooltip.getTipText() ) ) ) {
            hideToolTipPopup();

            focusComponent = comp;
            tooltip = createToolTip();
            tooltip.setTipText( text );

            PopupFactory popupFactory = PopupFactory.getSharedInstance();
            Point point = comp.getLocationOnScreen();
            tooltipContainer = popupFactory.getPopup( comp, tooltip, ( int )point.getX(), ( int )point.getY() + comp.getHeight() );
            tooltipContainer.show();
        }
    }

    private class refreshTipAction implements ActionListener {
        @Override
        public void actionPerformed ( ActionEvent e ) {
            if ( tooltip != null && focusComponent != null ) {
                Point p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen( p, focusComponent );
                if ( 0 <= p.x && p.x <= focusComponent.getWidth() && 0 <= p.y && p.y <= focusComponent.getHeight() ) {
                    checkTipTextChange( focusComponent, new MouseEvent( focusComponent, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(),
                            MouseEvent.NOBUTTON, p.x, p.y, 0, false ) );
                    timer.restart();
                }
            }
        }
    }
}