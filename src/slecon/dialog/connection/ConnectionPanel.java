package slecon.dialog.connection;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import base.cfg.ImageFactory;
import logic.connection.LiftConnectionBean;
import logic.connection.LiftSiteBean;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.home.PosButton;




public class ConnectionPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 5053671987267016224L;
    private ImageIcon	BTN_IMG		=	ImageFactory.BUTTON_PAUSE.icon(87,30);
    // if showing JOPtionPane dialog, not show second dialog in "same" time. (but JOptionPane is modal)
    private static boolean     showingDialog = false;
    private SiteEditorTree     tree;
    private PosButton          btnRename;
    private PosButton          btnNewSite;
    private PosButton          btnNewLift;
    private PosButton          btnDelete;
    private JPanel             panel;
    private JLabel             lblHost;
    private JTextField         txtHost;
    private JLabel             lblPort;
    private JTextField         txtPort;
    private LiftConnectionBean selectedItem;
    private PosButton          btnUp;
    private PosButton          btnDown;
    private JLabel             lblSiteEntry;
    private JPanel             panel_1;




    public ConnectionPanel () {
        initGUI();
        setPanelEnable( false );
                
        tree.setEditable( true );
        tree.setSiteInfo( SiteInfo.fromSiteManagement() );
        tree.addTreeSelectionListener( new TreeSelectionListener() {
            @Override
            public void valueChanged ( TreeSelectionEvent e ) {
                if ( tree.getLastSelectedPathComponent() instanceof DefaultMutableTreeNode ) {
                    DefaultMutableTreeNode node = ( ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent() );
                    if ( selectedItem != null && ! new TreePath( node.getPath() ).equals( tree.getTreePath( selectedItem ) ) ) {
                        if ( ! commitData() )
                            return;
                    }
                    if ( node.getUserObject() instanceof LiftConnectionBean ) {
                        setSelectedItem( ( LiftConnectionBean )node.getUserObject() );
                    } else {
                        setSelectedItem( null );
                    }
                    btnRename.setEnabled( tree.isPathEditable( new TreePath( node.getPath() ) ) );
                    btnNewLift.setEnabled( node.getUserObject() instanceof LiftSiteBean
                                           || node.getUserObject() instanceof LiftConnectionBean );
                    btnUp.setEnabled( tree.hasPrev( ( ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent() ).getUserObject() )
                                      && ( node.getUserObject() instanceof LiftSiteBean
                                           || node.getUserObject() instanceof LiftConnectionBean ) );
                    btnDown.setEnabled( tree.hasNext( ( ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent() ).getUserObject() )
                                        && ( node.getUserObject() instanceof LiftSiteBean
                                             || node.getUserObject() instanceof LiftConnectionBean ) );
                }
            }
        } );
        tree.setSelectionRow( 0 );
    }


    public LiftConnectionBean getSelectedItem () {
        return selectedItem;
    }


    public void setSelectedItem ( LiftConnectionBean selectedItem ) {
        LiftConnectionBean old = this.selectedItem;
        if ( old == null || ! old.equals( selectedItem ) ) {
            this.selectedItem = selectedItem;
            setPanelEnable( getSelectedItem() != null );
            if ( getSelectedItem() != null ) {
                txtHost.setText( getSelectedItem().getIp() );
                txtPort.setText( Integer.toString( getSelectedItem().getPort() ) );
            }
        }
    }


    private void setPanelEnable ( boolean enabled ) {
        panel.setEnabled( enabled );
        for ( Component comp : panel.getComponents() ) {
            comp.setEnabled( enabled );
        }
    }


    public boolean commitData () {
        if ( selectedItem != null ) {
            String ip = txtHost.getText();
            if ( ip.trim().length() == 0 ) {
                if ( ! showingDialog ) {
                    synchronized ( ConnectionPanel.class ) {
                        showingDialog = true;
                    }
                    JOptionPane.showMessageDialog( ConnectionPanel.this, "Host name is invalid" );
                    synchronized ( ConnectionPanel.class ) {
                        showingDialog = false;
                    }
                }
                tree.setSelectionPath( tree.getTreePath( selectedItem ) );
                return false;
            }

            int port;
            try {
                port = Integer.parseInt( txtPort.getText() );
            } catch ( NumberFormatException ex ) {
                JOptionPane.showMessageDialog( ConnectionPanel.this, "Port is invalid" );
                tree.setSelectionPath( tree.getTreePath( selectedItem ) );
                return false;
            }
            selectedItem.setIp( ip );
            selectedItem.setPort( port );
        }
        return true;
    }


    private void initGUI () {
        setLayout( new MigLayout( "", "[grow][300,grow]", "[][250px,grow][]" ) );
        setBackground(StartUI.SUB_BACKGROUND_COLOR);
        lblSiteEntry = new JLabel( "Site Entry:" );
        lblSiteEntry.setForeground(Color.WHITE);
        lblSiteEntry.setDisplayedMnemonic( 't' );
        add( lblSiteEntry, "cell 0 0" );
        tree = new SiteEditorTree() {
            private static final long serialVersionUID = 5891069294899686969L;
            public void startEditingAtPath ( TreePath path ) {
                if ( commitData() )
                    super.startEditingAtPath( path );
            }
        };
        tree.setRowHeight( 25 );
        tree.setDragEnabled( true );
        lblSiteEntry.setLabelFor( tree );
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        scrollPane.setOpaque(false);
        add( scrollPane, "flowy,cell 0 1,grow" );
        
        panel = new JPanel();
        panel.setBorder( new TitledBorder( new LineBorder( StartUI.BORDER_COLOR, 1, false ), "Elevator", TitledBorder.LEADING,
                                           TitledBorder.TOP, null, Color.WHITE ) );
        add( panel, "cell 1 0 1 3,grow" );
        panel.setBackground( StartUI.SUB_BACKGROUND_COLOR);
        panel.setLayout( new MigLayout( "", "[][grow][][60!]", "[]" ) );
        lblHost = new JLabel( "Host:" );
        lblHost.setDisplayedMnemonic( 'h' );
        lblHost.setForeground(Color.WHITE);
        panel.add( lblHost, "cell 0 0,alignx trailing" );
        txtHost = new JTextField();
        txtHost.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        txtHost.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        txtHost.setForeground(Color.WHITE);
        txtHost.setCaretColor(Color.WHITE);
        lblHost.setLabelFor( txtHost );
        panel.add( txtHost, "cell 1 0,growx" );
        txtHost.setColumns( 10 );
        lblPort = new JLabel( "Port:" );
        lblPort.setDisplayedMnemonic( 'p' );
        lblPort.setForeground(Color.WHITE);
        panel.add( lblPort, "cell 2 0,alignx trailing" );
        txtPort = new JTextField();
        txtPort.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        txtPort.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        txtPort.setForeground(Color.WHITE);
        txtPort.setCaretColor(Color.WHITE);
        lblPort.setLabelFor( txtPort );
        panel.add( txtPort, "cell 3 0,growx" );
        txtPort.setColumns( 10 );
        panel_1 = new JPanel();
        add( panel_1, "cell 0 2,alignx center,growy" );
        panel_1.setLayout( new MigLayout( "ins 0, gap 5", "[110!,fill][110!,fill]", "[][][]" ) );
        panel_1.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        btnNewSite = new PosButton( "New Site", BTN_IMG );
        btnNewSite.setFocusable( false );
        btnNewSite.setMnemonic( 's' );
        panel_1.add( btnNewSite, "cell 0 0" );
        btnNewLift = new PosButton( "New Lift", BTN_IMG );
        btnNewLift.setFocusable( false );
        btnNewLift.setMnemonic( 'l' );
        panel_1.add( btnNewLift, "cell 1 0" );
        btnRename = new PosButton( "Rename", BTN_IMG );
        btnRename.setFocusable( false );
        btnRename.setMnemonic( 'r' );
        panel_1.add( btnRename, "cell 0 1" );
        btnDelete = new PosButton( "Delete", BTN_IMG );
        btnDelete.setFocusable( false );
        btnDelete.setMnemonic( 'e' );
        panel_1.add( btnDelete, "cell 1 1" );
        btnUp = new PosButton( "Move Up", BTN_IMG );
        btnUp.setFocusable( false );
        btnUp.setMnemonic( 'u' );
        panel_1.add( btnUp, "cell 0 2" );
        btnDown = new PosButton( "Move Down", BTN_IMG );
        btnDown.setFocusable( false );
        btnDown.setMnemonic( 'd' );
        panel_1.add( btnDown, "cell 1 2" );
        btnDown.addActionListener( this );
        btnUp.addActionListener( this );
        btnDelete.addActionListener( this );
        btnRename.addActionListener( this );
        btnNewLift.addActionListener( this );
        btnNewSite.addActionListener( this );
    }


//    /**
//     * @param args
//     * @throws ClassNotFoundException
//     * @throws UnsupportedLookAndFeelException
//     * @throws IllegalAccessException
//     * @throws InstantiationException
//     * @throws InterruptedException
//     */
//    public static void main ( String[] args )
//            throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException,
//                   InterruptedException {
//        Class.forName( XMLConfigureParser.class.getCanonicalName() );
//        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
//
//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//        f.getContentPane().add( new ConnectionPanel() );
//        f.pack();
//        f.setVisible( true );
//    }


    public void actionPerformed ( final ActionEvent e ) {
        if ( e.getSource() == btnDelete ) {
            do_btnDelete_actionPerformed( e );
        }
        if ( e.getSource() == btnDown ) {
            do_btnDown_actionPerformed( e );
        }
        if ( e.getSource() == btnUp ) {
            do_btnUp_actionPerformed( e );
        }
        if ( e.getSource() == btnNewLift ) {
            do_btnNewLift_actionPerformed( e );
        }
        if ( e.getSource() == btnNewSite ) {
            do_btnNewSite_actionPerformed( e );
        }
        if ( e.getSource() == btnRename ) {
            do_btnRename_actionPerformed( e );
        }
    }


    protected void do_btnRename_actionPerformed ( final ActionEvent e ) {
        if ( ! commitData() )
            return;
        tree.startEditingAtPath( tree.getSelectionPath() );
    }


    protected void do_btnNewSite_actionPerformed ( final ActionEvent e ) {
        if ( ! commitData() )
            return;

        int i;
        for ( i = 0 ; i < 10000 && tree.isSiteExist( "New Site " + i ) ; i++ );
        if ( i == 10000 )
            return;

        LiftSiteBean bean = new LiftSiteBean( "New Site " + i );
        tree.newSite( bean );
        tree.setSelectionPath( tree.getTreePath( bean ) );
    }


    protected void do_btnNewLift_actionPerformed ( final ActionEvent e ) {
        if ( ! commitData() )
            return;

        LiftSiteBean site = null;
        if ( tree.getLastSelectedPathComponent() instanceof DefaultMutableTreeNode ) {
            DefaultMutableTreeNode node = ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent();
            if ( node.getUserObject() instanceof LiftSiteBean )
                site = ( LiftSiteBean )node.getUserObject();
            else if ( node.getUserObject() instanceof LiftConnectionBean )
                site = ( ( LiftConnectionBean )node.getUserObject() ).getSite();
        }
        if ( site != null ) {
            LiftConnectionBean conn = new LiftConnectionBean( "Lift", "", 1235, site );
            tree.newLift( conn );
            tree.setSelectionPath( tree.getTreePath( conn ) );
        }
    }


    public SiteInfo getSiteInfo () {
        if ( ! commitData() )
            return null;
        return tree.getSiteInfo();
    }


    protected void do_btnUp_actionPerformed ( final ActionEvent e ) {
        if ( ! commitData() )
            return;

        Object                 obj;
        DefaultMutableTreeNode node = ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent();
        if ( ( ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent() ) != null ) {
            obj = node.getUserObject();
            if ( obj != null ) {
                tree.back( ( ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent() ).getUserObject() );
                tree.setSelectionPath( tree.getTreePath( obj ) );
            }
        }
    }


    protected void do_btnDown_actionPerformed ( final ActionEvent e ) {
        if ( ! commitData() )
            return;

        DefaultMutableTreeNode node = ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent();
        if ( node != null ) {
            Object obj = node.getUserObject();
            if ( obj != null ) {
                tree.forward( ( ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent() ).getUserObject() );
                tree.setSelectionPath( tree.getTreePath( obj ) );
            }
        }
    }


    protected void do_btnDelete_actionPerformed ( final ActionEvent e ) {
        DefaultMutableTreeNode node = ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent();
        if ( node != null ) {
            Object obj = node.getUserObject();
            if ( obj != null ) {
                tree.delete( obj );
            }
            setSelectedItem( null );
            tree.setSelectionPath( null );
        }
    }
}
