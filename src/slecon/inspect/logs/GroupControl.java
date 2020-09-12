package slecon.inspect.logs;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import logic.connection.LiftConnectionBean;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.component.SubPanelBinder;
import slecon.component.Workspace;
import slecon.interfaces.InspectView;
import slecon.interfaces.Page;
import slecon.setting.SetupPanel;
import slecon.setting.management.UtilitiesSetting;




@InspectView(
    path      = "Group Control",
    sortIndex = 0x900,
    condition = "toolbox.debugMode"
)
public class GroupControl extends JPanel implements Page, ActionListener {
    private static final long serialVersionUID = 3522921229734038256L;
    private final String      title            = Long.toString( System.currentTimeMillis() );
    private final JButton     btnNewButton     = new JButton( "New button" );
    private int               count            = 0;
    private Workspace         parent;
    private final LiftConnectionBean connBean;
    private JButton gotoManagement;




    public GroupControl (LiftConnectionBean connBean) {
        initGUI();
        this.connBean = connBean;
        
        gotoManagement = new JButton("goto Setup->Utlities");
        gotoManagement.addActionListener(this);
        add(gotoManagement, "cell 1 0");
    }


    private void initGUI () {
        setLayout( new MigLayout( "", "[300!][]", "[10px,grow,fill]" ) );
        btnNewButton.addActionListener( new BtnNewButtonActionListener() );
        add( btnNewButton, "cell 0 0" );
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
        this.parent = workspace;
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
            final SubPanelBinder subpanel = new SubPanelBinder( parent, title, new LogSubPanel( title ) );
            StartUI.getTopMain().push( subpanel );
        }
    }




    public final class LogSubPanel extends JPanel implements Page {
        private static final long serialVersionUID = 8673269320886465536L;
        private String            context;




        LogSubPanel ( String context ) {
            this.context = context;
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
        }


        @Override
        public void onDestroy () {
            // TODO Auto-generated method stub
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
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == gotoManagement) {
            do_gotoManagement_actionPerformed(e);
        }
    }
    protected void do_gotoManagement_actionPerformed(final ActionEvent e) {
        StartUI.getTopMain().push( SetupPanel.build(connBean, UtilitiesSetting.class));
    }
}
