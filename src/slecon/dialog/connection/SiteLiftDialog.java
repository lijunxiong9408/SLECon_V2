package slecon.dialog.connection;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import base.cfg.ImageFactory;
import logic.connection.LiftConnectionBean;
import logic.connection.LiftSiteBean;
import logic.util.SiteManagement;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.component.SubtleSquareBorder;
import slecon.home.PosButton;




public class SiteLiftDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -895492327055041623L;
	private static final String escapeStrokeActionCommand = "el1.dialog.connection.SiteLiftDialog:WINDOW_CLOSING";
    private PosButton           btnOk;
    private ConnectionPanel     panel;
    private PosButton           btnCancel;
    private SiteInfo            oriSiteInfo;
    private SiteInfo            newSiteInfo;
    private static Point pressedPoint = null;



    private SiteLiftDialog () {
        initGUI();
    }


    private SiteLiftDialog ( Frame owner, String title, boolean modalityType ) {
        super( owner, title, modalityType );
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        setUndecorated(true);
        
        initGUI();
        oriSiteInfo = panel.getSiteInfo();
        getRootPane().registerKeyboardAction( this, escapeStrokeActionCommand, KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
                                              JComponent.WHEN_IN_FOCUSED_WINDOW );
        // mouse listener.
        getContentPane().addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent e) {
				pressedPoint = e.getPoint();
			}
        });
        
        getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				Point point = e.getPoint();
				Point locationPoint = getLocation();
				int x = locationPoint.x + point.x - pressedPoint.x;
				int y = locationPoint.y + point.y - pressedPoint.y;
				setLocation(x, y);
			}
        });
    }


    public static void showDialog () {
        SiteLiftDialog dialog = new SiteLiftDialog( StartUI.getFrame(), "Site Manager", true );
        dialog.pack();
        dialog.setLocationRelativeTo( StartUI.getFrame() );
        dialog.setVisible( true );
        dialog.dispose();
        if ( dialog.newSiteInfo != null && dialog.oriSiteInfo != null ) {
            saveToSiteManagement( dialog.newSiteInfo );
            SiteManagement.exportFile();
//            if ( ! dialog.oriSiteInfo.equals( dialog.newSiteInfo ) )
//                StartUI.rebootRequest();
        }
    }


    private static void saveToSiteManagement ( SiteInfo siteInfo ) {
        if ( siteInfo == null || siteInfo.sites == null || siteInfo.siteMap == null )
            return;
        for(LiftSiteBean site : SiteManagement.getAllSite())
            for (LiftConnectionBean conn : SiteManagement.getConnectionBySite(site))
                SiteManagement.removeLift(conn);
        for ( LiftSiteBean site : siteInfo.sites ) {
            for (LiftConnectionBean connBean : siteInfo.siteMap.get( site ) ) {
                SiteManagement.addLift(connBean);
            }
        }
    }


    private static boolean equals ( SiteInfo info1, SiteInfo info2 ) {
        if ( info1 == null || info2 == null )
            return false;
        if ( info1.sites == null || info2.sites == null )
            return false;
        if ( info1.siteMap == null || info2.siteMap == null )
            return false;
        if ( info1.sites.size() != info2.sites.size() )
            return false;
        for ( int i = 0 ; i < info1.sites.size() ; i++ ) {
            LiftSiteBean             site1 = info1.sites.get( i );
            LiftSiteBean             site2 = info2.sites.get( i );
            List<LiftConnectionBean> sl1   = info1.siteMap.get( site1 );
            List<LiftConnectionBean> sl2   = info2.siteMap.get( site2 );
            if ( ! site1.equals( site2 ) )
                return false;
            if ( sl1.size() != sl2.size() )
                return false;
            for ( int j = 0 ; j < sl1.size() ; j++ ) {
                if ( ! sl1.get( j ).equals( sl2.get( j ) ) )
                    return false;
            }
        }
        return true;
    }


    private void initGUI () {
    	setBounds( 100, 100, 700, 600 );
        getContentPane().setBackground(StartUI.SUB_BACKGROUND_COLOR);
        
        JPanel mainpanel = new JPanel();
        mainpanel.setLayout(new MigLayout( "", "[grow]", "[grow]10px[]10px[]"));
        mainpanel.setBounds(0, 0, 700, 600);
        mainpanel.setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
        mainpanel.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        mainpanel.setOpaque( false );
        getContentPane().add(mainpanel);
        
        panel = new ConnectionPanel();
        mainpanel.add( panel, "cell 0 0,grow" );

        JSeparator separator = new JSeparator();
        separator.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        mainpanel.add( separator, "cell 0 1,growx" );
        btnOk = new PosButton( "OK", ImageFactory.BUTTON_PAUSE.icon(87,30));
        btnOk.addActionListener( this );
        mainpanel.add( btnOk, "flowx,cell 0 2,alignx center, w 120!" );
        btnCancel = new PosButton( "Cancel", ImageFactory.BUTTON_PAUSE.icon(87,30));
        btnCancel.addActionListener( this );
        mainpanel.add( btnCancel, "cell 0 2, w 120!" );
        setMinimumSize( new java.awt.Dimension( 500, 450 ) );
    }


//    /**
//     * @param args
//     * @throws ClassNotFoundException
//     * @throws UnsupportedLookAndFeelException
//     * @throws IllegalAccessException
//     * @throws InstantiationException
//     */
//    public static void main ( String[] args )
//            throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
//        Class.forName( XMLConfigureParser.class.getCanonicalName() );
//        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
//
//        SiteLiftDialog f = new SiteLiftDialog();
//        f.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
//        f.pack();
//        f.setVisible( true );
//    }


    public void actionPerformed ( final ActionEvent e ) {
        if ( e.getSource() == btnCancel ) {
            do_btnCancel_actionPerformed( e );
        }
        if ( e.getSource() == btnOk ) {
            do_btnOk_actionPerformed( e );
        }
        if ( e.getActionCommand() == escapeStrokeActionCommand ) {
            do_btnCancel_actionPerformed( e );
        }
    }


    protected void do_btnOk_actionPerformed ( final ActionEvent e ) {
        newSiteInfo = panel.getSiteInfo();
        dispose();
    }


    protected void do_btnCancel_actionPerformed ( final ActionEvent e ) {
        newSiteInfo = null;
        dispose();
    }
}
