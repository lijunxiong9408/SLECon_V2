package slecon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import comm.Parser_Status;
import comm.constants.OcsModule;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.SiteManagement;
import net.miginfocom.swing.MigLayout;
import slecon.component.IPanelBinder;
import slecon.component.SubtleSquareBorder;
import slecon.home.PosButton;
import slecon.home.dashboard.VerticalSrcollBarUI;

@SuppressWarnings("serial")
public class LiftSelectorPanel extends JPanel {
    private static final ImageIcon      LED1                    = ImageFactory.LIGHT_BRIGHT_GREEN.icon(16, 16);
    private static final ImageIcon      LED2                    = ImageFactory.LIGHT_BRIGHT_ORANGE.icon(16, 16);
    private static final ImageIcon      LED3                    = ImageFactory.LIGHT_BRIGHT_RED.icon(16, 16);
    private static final ImageIcon      LED7                    = ImageFactory.LIGHT_BLACK.icon(16, 16);
    public  IPanelBinder             hostedBinder;
	private LiftConnectionBean       selectedLift;
	private JLabel					 labLiftStatus;
	private JLabel					 labLiftName;
    private PosButton                btnLiftSelector;
 //   private SiteSelectorPanel        siteSelector;
    private LiftSelectorListPanel	 siteSelector;
    private JDialog                  popupWindow;
	private boolean					 hideEnable = false;
    private MigLayout         layout;
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("labLiftStatus", "20 10 16 16 c");
    	styles.put("labLiftName", "50 5 100 30 c");
    	styles.put("btnLiftSelector", "210 10 20 20 c");
    }
	
    private Parser_Status     status;
    
    final AbstractAction liftSelectorAction = new AbstractAction( "Action of LiftSelector" ) {
        private static final long serialVersionUID = - 882627567009039932L;


        public boolean isEnabled () {
            return btnLiftSelector == null ? true : btnLiftSelector.isVisible();
        }


        @Override
        public void actionPerformed ( ActionEvent e ) {
            if ( btnLiftSelector.isVisible() && StartUI.getFrame().isFocused() ){
            	if(!hideEnable) {
            		showLiftSelectorPopup();
            		hideEnable = true;
            	}else {
            		hideLiftSelectorPopup();
            		hideEnable = false;
            	}
            }
                
        }
    };
    
    public LiftSelectorPanel() {
    	init();
    }
    
    private void init() {
    	layout = new MigLayout("nogrid, w 250!, h 40!");
    	setLayout(layout);
    	setBackground(StartUI.SUB_BACKGROUND_COLOR);
    	setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
    	
    	labLiftStatus = new JLabel(LED7);
    	add(labLiftStatus);
    	
    	labLiftName = new JLabel();
    	labLiftName.setFont(FontFactory.FONT_14_BOLD);
    	labLiftName.setForeground(Color.WHITE);
    	add(labLiftName);
    	
    	btnLiftSelector = new PosButton( ImageFactory.ARROW_COMBBOX_DOWN.icon(11, 10) );
    	btnLiftSelector.addActionListener(liftSelectorAction);
        add(btnLiftSelector);
        
        StartUI.setStyle(layout, styles, labLiftStatus, "labLiftStatus");
        StartUI.setStyle(layout, styles, labLiftName, "labLiftName");
        StartUI.setStyle(layout, styles, btnLiftSelector, "btnLiftSelector");
        
        popupWindow = new JDialog( StartUI.getFrame() );
        ( ( JComponent )popupWindow.getContentPane() ).setBackground(StartUI.SUB_BACKGROUND_COLOR);
        popupWindow.setUndecorated( true );
        popupWindow.setAlwaysOnTop( true );
        popupWindow.getContentPane().setLayout( new MigLayout( "fill, ins 0 0 0 0" ) );
        popupWindow.addWindowFocusListener( new WindowFocusListener() {
            public void windowGainedFocus ( WindowEvent e ) {
            }

            public void windowLostFocus ( WindowEvent e ) {
                popupWindow.setVisible( false );
            }
        } );
        
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0, false );
        Action escapeAction = new AbstractAction() {
            private static final long serialVersionUID = - 4162704338565816547L;

            // close the frame when the user presses escape
            public void actionPerformed ( ActionEvent e ) {
                popupWindow.setVisible( false );
            }
        };
        popupWindow.getRootPane().getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( escapeKeyStroke, "ESCAPE" );
        popupWindow.getRootPane().getActionMap().put( "ESCAPE", escapeAction );
    }
    
    public boolean setSelectedLift ( LiftConnectionBean connBean ) {
        if ( connBean==null || SiteManagement.isAlive( connBean ) ) {
            LiftConnectionBean oldSite = this.selectedLift;
            if ( ( oldSite == null && connBean != null ) || ( oldSite != null && ! oldSite.equals( connBean ) ) ) {
                this.selectedLift = connBean;
                firePropertyChange( "selectedLift", oldSite, selectedLift );
                if ( hostedBinder != null )
                    hostedBinder.setConnection( connBean );
            }
            if(this.selectedLift != null) {
            	siteSelector = new LiftSelectorListPanel(selectedLift.getSite());
            	JScrollPane scrollPanel =  new JScrollPane( siteSelector );
            	scrollPanel.setBorder( null );
            	scrollPanel.setOpaque( false );
            	scrollPanel.getViewport().setOpaque( false );
            	scrollPanel.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
            	scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            	scrollPanel.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
            	popupWindow.getContentPane().removeAll();
            	popupWindow.repaint();
            	popupWindow.setPreferredSize(new Dimension(250, 150));
            	popupWindow.getContentPane().add( scrollPanel );
            }
            labLiftName.setText( toConnString( connBean ) );
            ChangeLiftWorkStatus(connBean);
            return true;
        } else {
            ToolBox.showErrorMessage( Dict.lookup( "SelectedLiftIsOffline" ) );
            return false;
        }
    }
	
    void showLiftSelectorPopup () {
        popupWindow.pack();
        int x = this.getLocationOnScreen().x;
        int y = this.getLocationOnScreen().y + this.getPreferredSize().height; 
        popupWindow.setLocation( x, y );
        popupWindow.setPreferredSize(new Dimension(250, 150));
        popupWindow.setBackground(StartUI.MAIN_BACKGROUND_COLOR);
        popupWindow.setVisible( true );
    }


    public void hideLiftSelectorPopup () {
        popupWindow.setVisible( false );
    }
    
    public LiftConnectionBean getSelectedLift () {
        return this.selectedLift;
    }


    private String toConnString ( LiftConnectionBean conn ) {
        if ( conn == null )
            return "";
        else
            return conn.getName() ;
    }

    private void ChangeLiftWorkStatus(LiftConnectionBean conn) {
    	if(conn != null) {
    		ImageIcon led ;
        	status = new Parser_Status( conn.getIp(), conn.getPort() );
        	OcsModule	ocsmodule	=	status.getOCSModule();
            switch ( ocsmodule ) {
    	        case ARO :
    	        case NOR :
    	            led = LED1;
    	            break;
    	        case ATT :
    	        case CORR :
    	        case ES :
    	        case TDEO:
    	        case FEO :
    	        case FRO :
    	        case INSP :
    	        case ISC :
    	        case PAK :
    	        case RSCR :
    	        case REL:
    	        case EPB:
    	        case UCMT:
    	        case EQO:
    	        case MCEX:
                case ACDO:
                case MOH:
    	            led = LED2;
    	            break;
    	        case DCS :
    	        case DAF :
    	        case EPO :
    	        case FAIL :
    	        case OLP :
    	            led = LED3;
    	            break;
    	        default :
    	            led = LED7;
            }
            labLiftStatus.setIcon(led);
    	}
    }
}
