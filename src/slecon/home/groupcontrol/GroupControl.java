package slecon.home.groupcontrol;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import base.cfg.ImageFactory;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.component.SubPanelBinder;
import slecon.component.Workspace;
import slecon.interfaces.HomeView;
import slecon.interfaces.Page;




@HomeView(
    icon      = ImageFactory.DASHBOARD_ICON,
    name      = "Group Control",
    sortIndex = 5,
    condition = "toolbox.debugMode"
)
public class GroupControl extends JPanel implements Page {
    private static final long serialVersionUID = -1550498911636241072L;
    private final String      title            = Long.toString( System.currentTimeMillis() );
    private final JPanel      lift1            = new JPanel();
    private final JButton     btnNewButton     = new JButton( "New button" );
    private int               count            = 0;
    private Workspace         panelbinder;




    public GroupControl () {
        initGUI();
    }


    private void initGUI () {
        setLayout( new MigLayout( "", "[300!][]", "[10px,grow,fill]" ) );
        add( lift1, "cell 0 0,alignx left,aligny top" );
        btnNewButton.addActionListener( new BtnNewButtonActionListener() );
        add( btnNewButton, "cell 1 0" );
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
        this.panelbinder = workspace;
    }


    @Override
    public void onStart () throws Exception {
        System.out.printf( "PARENT[%s] start\n", title );
    }


    @Override
    public void onResume () throws Exception {
        System.out.printf( "PARENT[%s] resume\n", title );
    }


    @Override
    public void onPause () throws Exception {
        System.out.printf( "PARENT[%s] pause\n", title );
    }


    @Override
    public void onStop () throws Exception {
        System.out.printf( "PARENT[%s] stop\n", title );
    }


    @Override
    public void onDestroy () {

        // TODO Auto-generated method stub
    }


    private class BtnNewButtonActionListener implements ActionListener {
        public void actionPerformed ( final ActionEvent e ) {
            final String         title    = "Log " + ( count++ );
            final SubPanelBinder subpanel = new SubPanelBinder( panelbinder, title, new LogSubPanel( title ) );
            StartUI.getTopMain().push( subpanel );
        }
    }




    public class LogSubPanel extends JPanel implements Page {
        private static final long serialVersionUID = -5658978225385031209L;
        private String            context;
        private Workspace         panelbinder;




        LogSubPanel ( String context ) {
            this.context = context;
            addMouseListener( new MouseAdapter() {
                int i = 0;
                public void mousePressed ( MouseEvent evt ) {
                    panelbinder.setTitle( "clickCount:" + i++ );
                }
            } );
        }


        @Override
        public void onStop () {
            System.out.printf( "group[%s] stop\n", this.context );
        }


        @Override
        public void onPause () {
            System.out.printf( "group[%s] pause\n", this.context );
        }


        @Override
        public void onResume () {
            System.out.printf( "group[%s] resume\n", this.context );
        }


        @Override
        public void onStart () {
            System.out.printf( "group[%s] start\n", this.context );
        }


        @Override
        public void onCreate ( Workspace workspace ) throws Exception {
            this.panelbinder = workspace;
            System.out.printf( "group[%s] create\n", this.context );
        }


        @Override
        public void onDestroy () {
        }


        @Override
        protected void paintComponent ( Graphics g ) {
            Graphics2D g2d;
            g2d = ( Graphics2D )g.create();

            // rotated 45 degrees around origin
            g2d.rotate( Math.toRadians( -45 ), getWidth() / 2, getHeight() / 2 );
            g2d.setColor( Color.GRAY.darker() );

            Font font = getFont().deriveFont( Font.BOLD, 64 );
            g2d.setFont( font );

            FontMetrics fm    = g2d.getFontMetrics();
            int         width = fm.stringWidth( context );
            g2d.drawString( context, ( int )( getWidth() - width ) / 2, ( int )( getHeight() ) / 2 );

            // done with g2d, dispose it
            g2d.dispose();
        }
    }
}
