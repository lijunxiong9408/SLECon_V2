package slecon.home.action;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.connection.LiftSiteBean;
import logic.evlog.ErrorLog;
import logic.util.SiteManagement;
import logic.util.Version;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SubtleSquareBorder;
import slecon.home.PosButton;
import slecon.home.ShortcutPanel;
import slecon.home.dashboard.VerticalSrcollBarUI;




abstract class SelectPopupDialog extends JDialog implements ActionListener, MouseListener {
    private static final long              serialVersionUID          = -8049239361635181900L;
    private static final ResourceBundle    TEXT                      = ToolBox.getResourceBundle( "logic.gui.SelectedLiftDialog" );
    private static final String            escapeStrokeActionCommand = "el1.dialog.SelectPopupDialog:WINDOW_CLOSING";
    private static String                  EMPTY_DESCRIPTION_TEXT    = "select a lift.";
    private ImageIcon	IMG_BUTTON	=	ImageFactory.BUTTON_PAUSE.icon(87,30);
    private PosButton                      cancelButton;
    private PosButton                      okButton;
    private JList<LiftSiteBean>            siteList;
    private DefaultListModel<LiftSiteBean> siteModel;
    private JList<LiftConnectionBean>            liftList;
    private DefaultListModel<LiftConnectionBean> liftModel;
    private JLabel                         lblDescription;
    private MigLayout                      descLayout;
    private JLabel 		lblTitle;
    private JSeparator	separator;
    private JLabel lblSites;
    private JLabel lblElevator;
    private JLabel lblOverview;
    private Timer timer;
    private JPanel	mainPanel;
	Point pressedPoint = null;
	
	private MigLayout         layout;
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("lblTitle", "20 10 100 20 l");
    	styles.put("separator", "10 35 680 2 c");
    	styles.put("lblSites", "20 40 100 20 l");
    	styles.put("panelSites", "20 60 270 150 c");
    	styles.put("lblElevator", "20 220 100 20 l");
    	styles.put("panelElevator", "20 240 270 150 c");
    	styles.put("lblOverview", "300 40 100 20 l");
    	styles.put("panelOverview", "300 60 270 330 c");
    	styles.put("okButton", "580 280 100 50 c");
    	styles.put("cancelButton", "580 340 100 50 c");
    	
    }

    /**
     * Create the dialog.
     */
    SelectPopupDialog () {
        super( StartUI.getFrame(), "", Dialog.ModalityType.DOCUMENT_MODAL );
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        setUndecorated(true);
        
        initGUI();

        loadSite();
        siteList.setSelectedIndex(0);
        timer = new Timer(1000, this);
        timer.setInitialDelay(0);
        timer.start();
        
        setLocationRelativeTo( StartUI.getFrame() );
        setResizable(false);
        getRootPane().registerKeyboardAction( this, escapeStrokeActionCommand, KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
                                              JComponent.WHEN_IN_FOCUSED_WINDOW );
         
    }


    public static LiftConnectionBean showDialog () {
        final LiftConnectionBean[] result = new LiftConnectionBean[ 1 ];
        SelectPopupDialog   dialog = new SelectPopupDialog() {
            private static final long serialVersionUID = - 322492892576768183L;

            @Override
            protected void setValue ( LiftConnectionBean selectedValue ) {
                result[ 0 ] = selectedValue;
            }
        };
        dialog.setVisible( true );
        
        dialog.timer.stop();
        dialog.dispose();
        if ( result[ 0 ] instanceof LiftConnectionBean )
            return result[ 0 ];
        return null;
    }


    protected String covertToListItemText ( Object obj ) {
        if ( obj instanceof LiftSiteBean ) {
            LiftSiteBean site = ( LiftSiteBean )obj;
            return site.getName();
        } else if ( obj instanceof LiftConnectionBean ) {
            return ( (LiftConnectionBean) obj ).getName();
        }
        return obj == null
               ? "All"
               : obj.toString();
    }


    private void initGUI () {
    	setBounds( 100, 100, 700, 400 );

    	mainPanel = new JPanel();
        layout = new MigLayout( "nogrid, w 700!, h 400!", "[0!]", "[0!]" );
        mainPanel.setLayout(layout);
        mainPanel.setBounds(0, 0, 700, 400);
        mainPanel.setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
        mainPanel.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        getContentPane().add(mainPanel);
        // Title
        lblTitle = new JLabel();
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(FontFactory.FONT_12_BOLD);
        mainPanel.add( lblTitle );
        StartUI.setStyle(layout, styles, lblTitle, "lblTitle");
        
        separator = new JSeparator();
        separator.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR, 2));
        mainPanel.add( separator );
        StartUI.setStyle(layout, styles, separator, "separator");
        
        // Site
        lblSites = new JLabel( "Sites" );
        lblSites.setForeground(Color.WHITE);
        lblSites.setFont(FontFactory.FONT_12_BOLD);
        mainPanel.add( lblSites, BorderLayout.NORTH );
        StartUI.setStyle(layout, styles, lblSites, "lblSites");
        
        JPanel panelSites = new JPanel(new MigLayout("ins 2 5 2 5, w 260!, h 146! ","[fill, center]","[fill, top]"));
        panelSites.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelSites.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR, 1));
        mainPanel.add( panelSites);
        StartUI.setStyle(layout, styles, panelSites, "panelSites");
        
        {
            siteModel = new DefaultListModel<>();
            siteList  = new JList<>( siteModel );
            siteList.setBackground(StartUI.SUB_BACKGROUND_COLOR);
            siteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            siteList.addListSelectionListener( new SiteListListSelectionListener() );
            siteList.setCellRenderer( new DefaultListCellRenderer() {
                private static final long serialVersionUID = 2487597000871839285L;
                public Component getListCellRendererComponent ( JList<?> list, Object value, int index, boolean isSelected,
                                                                boolean cellHasFocus ) {
                    JLabel label = ( JLabel )super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
                    label.setText( covertToListItemText( value ) );
                    label.setForeground( Color.WHITE );
                    if ( ! isSelected || ! cellHasFocus ) {
                    	label.setBackground( StartUI.SUB_BACKGROUND_COLOR );
                    }else {
                    	label.setBackground( StartUI.BORDER_COLOR );
                    }
                    return label;
                }
            } );

            JScrollPane scrollPane = new JScrollPane(siteList);
            scrollPane.setOpaque( false );
            scrollPane.getViewport().setOpaque( false );
            scrollPane.setViewportBorder( null );
            scrollPane.setBorder( null );
            scrollPane.setPreferredSize(new Dimension(260,146));
            scrollPane.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
            panelSites.add(scrollPane);
        }
        
        // Lift
        lblElevator = new JLabel( "Elevator" );
        lblElevator.setForeground(Color.WHITE);
        lblElevator.setFont(FontFactory.FONT_12_BOLD);
        mainPanel.add( lblElevator);
        StartUI.setStyle(layout, styles, lblElevator, "lblElevator");
        
        JPanel panelElevator = new JPanel(new MigLayout("ins 2 5 2 5, w 260!, h 146! ","[fill, center]","[fill, top]"));
        panelElevator.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelElevator.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR, 1));
        mainPanel.add( panelElevator);
        StartUI.setStyle(layout, styles, panelElevator, "panelElevator");
        {
            liftModel = new DefaultListModel<>();
            liftList  = new JList<>( liftModel );
            liftList.setBackground(StartUI.SUB_BACKGROUND_COLOR);
            liftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            liftList.addListSelectionListener( new LiftListListSelectionListener() );
            liftList.addMouseListener( this );
            liftList.setCellRenderer( new DefaultListCellRenderer() {
                private static final long serialVersionUID = 4546694766018316522L;
                public Component getListCellRendererComponent ( JList<?> list, Object value, int index, boolean isSelected,
                                                                boolean cellHasFocus ) {
                    JLabel label = ( JLabel )super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
                    label.setText( covertToListItemText( value ) );
                    label.setForeground( Color.WHITE );
                    if ( ! isSelected || ! cellHasFocus ) {
                    	label.setBackground( StartUI.SUB_BACKGROUND_COLOR );
                    }else {
                    	label.setBackground( StartUI.BORDER_COLOR );
                    }
                    return label;
                }
            } );

            JScrollPane scrollPane_2 = new JScrollPane( liftList );
            scrollPane_2.setOpaque( false );
            scrollPane_2.getViewport().setOpaque( false );
            scrollPane_2.setViewportBorder( null );
            scrollPane_2.setBorder( null );
            scrollPane_2.setPreferredSize(new Dimension(260,146));
            scrollPane_2.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
            panelElevator.add(scrollPane_2);
        }
        
        // Overview
        lblOverview = new JLabel( "Overview" );
        lblOverview.setForeground(Color.WHITE);
        lblOverview.setFont(FontFactory.FONT_12_BOLD);
        mainPanel.add( lblOverview);
        StartUI.setStyle(layout, styles, lblOverview, "lblOverview");
        
        JScrollPane panelOverview = new JScrollPane();
        panelOverview.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
        panelOverview.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR, 1));
        mainPanel.add( panelOverview);
        StartUI.setStyle(layout, styles, panelOverview, "panelOverview");
        {
        	lblDescription = new JLabel( EMPTY_DESCRIPTION_TEXT );
        	lblDescription.setHorizontalAlignment(SwingConstants.CENTER);
        	lblDescription.setFont(FontFactory.FONT_12_PLAIN);
        	lblDescription.setForeground(Color.WHITE);
        	lblDescription.setVerticalAlignment( SwingConstants.CENTER );
        	descLayout = new MigLayout( "fill, gap 0, ins 0" );
        	
        	JPanel wrapper = new JPanel( descLayout );
        	wrapper.setBackground( StartUI.SUB_BACKGROUND_COLOR );
        	wrapper.add( lblDescription, "left top,grow" );
        	descLayout.setComponentConstraints( lblDescription, "pos 0.5al 0.5al" );
        	panelOverview.setViewportView( wrapper );
        }
        
        // button.
        okButton = new PosButton( IMG_BUTTON );
        okButton.addActionListener( this );
        okButton.setActionCommand( "OK" );
        getRootPane().setDefaultButton( okButton );
        mainPanel.add( okButton);
        StartUI.setStyle(layout, styles, okButton, "okButton");
        
        cancelButton = new PosButton( IMG_BUTTON );
        cancelButton.addActionListener( this );
        cancelButton.setActionCommand( "Cancel" );
        mainPanel.add( cancelButton);
        StartUI.setStyle(layout, styles, cancelButton, "cancelButton");
        
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
       
        
        this.addWindowListener( new WindowAdapter() {
            public void windowClosed ( WindowEvent e ) {
                onClose();
            }
        } );
        
        loadI18n();
    }


    private void loadI18n() {
    	lblTitle.setText(TEXT.getString("SelectLiftDialog.TITLE")); 
    	lblSites.setText(TEXT.getString("SelectLiftDialog.SITES"));
    	lblElevator.setText(TEXT.getString("SelectLiftDialog.ELEVATOR"));
    	lblOverview.setText(TEXT.getString("SelectLiftDialog.OVERVIEW"));
    	EMPTY_DESCRIPTION_TEXT = TEXT.getString("SelectLiftDialog.EMPTY_DESCRIPTION_TEXT");
        lblDescription.setText(EMPTY_DESCRIPTION_TEXT);
        okButton.setText(TEXT.getString("SelectLiftDialog.OK"));
        cancelButton.setText(TEXT.getString("SelectLiftDialog.CANCEL"));
    }


    public synchronized void actionPerformed ( final ActionEvent e ) {
        if ( e.getSource() == cancelButton && SelectPopupDialog.this.isVisible() ) {
            onCancel();
        }
        if ( e.getActionCommand() == escapeStrokeActionCommand ) {
            onCancel();
        }
        if ( e.getSource() == okButton && SelectPopupDialog.this.isVisible() ) {
            onOK();
        }
        if ( e.getSource() == timer ) {
            onTimerEvent();
        }
    }


    private void onTimerEvent() {
        liftList.repaint();
        onLiftListSelectedChanged();
    }


    protected void onOK () {
        if ( liftList.getSelectedValue() != null )
            setValue( liftList.getSelectedValue() );
        dispatchEvent( new WindowEvent( this, WindowEvent.WINDOW_CLOSING ) );
    }


    protected void onCancel () {
        dispatchEvent( new WindowEvent( this, WindowEvent.WINDOW_CLOSING ) );
        ShortcutPanel.btn_trigger(1);
    }


    protected abstract void setValue ( LiftConnectionBean selectedValue );


    protected void onClose () {
    	ShortcutPanel.btn_trigger(1);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void mouseClicked ( final MouseEvent e ) {
        if ( e.getSource() == liftList ) {
            do_liftList_mouseClicked( e );
        }
    }


    public void mouseEntered ( final MouseEvent e ) {
    }


    public void mouseExited ( final MouseEvent e ) {
    }


    public void mousePressed ( final MouseEvent e ) {
    }


    public void mouseReleased ( final MouseEvent e ) {
    }


    protected void do_liftList_mouseClicked ( final MouseEvent e ) {
        if ( e.getClickCount() == 2 && SelectPopupDialog.this.isVisible() ) {
            onOK();
        }
    }


    private String toDescriptionHTML ( LiftConnectionBean lift ) {
        Version version = SiteManagement.getVersion(lift); 
        final String name       = lift.getName();
        final String isAlive    = SiteManagement.isAlive(lift) ? Dict.lookup("yes") : Dict.lookup("no");
        
        String       serial     = version!=null ? version.getMcsSerialNumber() : "-";
        String       fw         = version!=null ? version.getMcsFirmwareVersion() : "-";
        String       hw         = version!=null ? version.getMcsBoardVersion() : "-";
        String       contract   = version!=null ? version.getMcsContractVersion() : "-";
        String       ccVersion  = version!=null ? version.getControlCoreVersion() : "-";
        String       guiVersion = version!=null ? version.getGuiVersion() : "-";
        String       result     =
            String.format(
                    TEXT.getString("SelectLiftDialog.FMT_DESCRIPTION"), name, isAlive, serial, fw, hw, contract, ccVersion, guiVersion );
        return result;
    }


    private void loadSite () {
        List<LiftSiteBean> site = SiteManagement.getAllSite();
        siteModel.add( 0, null );
        for ( int i = 0 ; i < site.size() ; i++ )
            siteModel.add( i + 1, site.get( i ) );
    }


    private class LiftListListSelectionListener implements ListSelectionListener {
        public void valueChanged ( final ListSelectionEvent e ) {
            onLiftListSelectedChanged();
        }
    }


    private void onLiftListSelectedChanged() {
        LiftConnectionBean lift = liftList.getSelectedValue();
        if ( lift != null ) {
            lblDescription.setText( toDescriptionHTML( lift ) );
            descLayout.setComponentConstraints( lblDescription, "pos 0.5al 0.2al" );
        } else {
            lblDescription.setText( EMPTY_DESCRIPTION_TEXT );
            descLayout.setComponentConstraints( lblDescription, "pos 0.5al 0.5al" );
        }
    }



    private class SiteListListSelectionListener implements ListSelectionListener {
        public void valueChanged ( final ListSelectionEvent e ) {
            LiftSiteBean site = siteList.getSelectedValue();
            liftModel.removeAllElements();

            List<LiftConnectionBean> list = ( site == null )
                                            ? SiteManagement.getAllConnection()
                                            : SiteManagement.getConnectionBySite( site );
            for ( int i = 0 ; i < list.size() ; i++ ) {
                liftModel.add( i, list.get( i ) );
            }
            liftList.repaint();
        }
    }
    
}
