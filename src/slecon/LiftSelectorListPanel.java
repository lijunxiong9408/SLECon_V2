package slecon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import comm.Parser_Status;
import comm.constants.OcsModule;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.connection.LiftSiteBean;
import logic.util.SiteManagement;
import net.miginfocom.swing.MigLayout;
import slecon.component.SubtleSquareBorder;
import slecon.home.PosButton;
import slecon.inspect.InspectPanel;

public class LiftSelectorListPanel extends JPanel {
	private static final long serialVersionUID = 438148267137775236L;
    private static final ImageIcon      LED1                    = ImageFactory.LIGHT_BRIGHT_GREEN.icon(16, 16);
    private static final ImageIcon      LED2                    = ImageFactory.LIGHT_BRIGHT_ORANGE.icon(16, 16);
    private static final ImageIcon      LED3                    = ImageFactory.LIGHT_BRIGHT_RED.icon(16, 16);
    private static final ImageIcon      LED7                    = ImageFactory.LIGHT_BLACK.icon(16, 16);
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("labLiftStatus", "10 10 16 16 c");
    	styles.put("labLiftName", "30 5 100 30 c");
    	styles.put("btnLiftSelector", "180 10 20 20 c");
    }
    
	LiftSiteBean site = null;
	
	public LiftSelectorListPanel(LiftSiteBean site) {
		// TODO Auto-generated constructor stub
		this.site = site;
		init();
	}
	
	
	private void init() {
		removeAll();
		MigLayout layout_main = new MigLayout("fill","[grow, center]","[center]");
		setLayout(layout_main);
		setPreferredSize(new Dimension(250, 200));
		setBackground(StartUI.SUB_BACKGROUND_COLOR);
	
		int index = 0;
		for(final LiftConnectionBean bean : SiteManagement.getConnectionBySite( site )) {
			final JPanel liftPanel = new JPanel();
			MigLayout layout = new MigLayout("nogrid, w 220!, h 40!");
			liftPanel.setLayout(layout);
			liftPanel.setBackground(StartUI.SUB_BACKGROUND_COLOR);
	    	
			final JLabel labLiftStatus = new JLabel(LiftWorkStatus(bean));
	    	liftPanel.add(labLiftStatus);
	    	
	    	final JLabel labLiftName = new JLabel(toConnString(bean));
	    	labLiftName.setFont(FontFactory.FONT_14_BOLD);
	    	labLiftName.setForeground(Color.WHITE);
	    	liftPanel.add(labLiftName);
	    	
	    	final PosButton btnLiftSelector = new PosButton(null);
	    	liftPanel.add(btnLiftSelector);
	        
	        StartUI.setStyle(layout, styles, labLiftStatus, "labLiftStatus");
	        StartUI.setStyle(layout, styles, labLiftName, "labLiftName");
	        StartUI.setStyle(layout, styles, btnLiftSelector, "btnLiftSelector");
	        
	        liftPanel.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					if(SiteManagement.isAlive( bean )) {
						StartUI.getLiftSelector().setSelectedLift( bean );
						StartUI.getLiftSelector().hideLiftSelectorPopup();
					}else {
			            ToolBox.showErrorMessage( Dict.lookup( "SelectedLiftIsOffline" ) );
			        }
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					btnLiftSelector.setIcon(null);
					liftPanel.setBackground(StartUI.SUB_BACKGROUND_COLOR);
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					btnLiftSelector.setIcon(ImageFactory.ARROW_COMBBOX_UP.icon(11, 10));
					liftPanel.setBackground(StartUI.BORDER_COLOR);
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
	        
	        add(liftPanel);
	        layout_main.setComponentConstraints( liftPanel, String.format( "x %d, y %d, w %d!, h %d!", 0, (index * 40), 220, 40 ));
	        index += 1;
		}
		revalidate();
		repaint();
		updateUI();
	}
	
	private String toConnString ( LiftConnectionBean conn ) {
        if ( conn == null )
            return "";
        else
            return conn.getName();
    }
	
	private ImageIcon LiftWorkStatus(LiftConnectionBean conn) {
		ImageIcon led = LED7 ;
    	if(conn != null && SiteManagement.isAlive(conn)) {
    		Parser_Status status = new Parser_Status( conn.getIp(), conn.getPort() );
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
    	}
    	return led;
    }
}
