package slecon.inspect.devices.subview;
import java.awt.Color;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.inspect.devices.IoStatusBean;




public class IOStatusView extends JPanel {
    private static final long           serialVersionUID = -6433591469453210211L;
    private static final ResourceBundle bundle           = ToolBox.getResourceBundle("inspect.DevicePanel");
    private IoStatusBean ioStatusBean;




    public IOStatusView () {
        setBorder( BorderFactory.createLineBorder(StartUI.BORDER_COLOR) );
        setOpaque( false );
    }


    public void setIoStatus ( IoStatusBean bean ) {
        this.ioStatusBean = bean;
        updateIoStatusBean();
    }


    public IoStatusBean getIoStatusBean () {
        return ioStatusBean;
    }


    public void setIoStatusBean ( IoStatusBean ioStatusBean ) {
        this.ioStatusBean = ioStatusBean;
    }


    public void updateIoStatusBean () {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                removeAll();
                revalidate();

                IoStatusBean ioData = ioStatusBean;
                if ( ioData != null ) {
                    MigLayout layout = new MigLayout( "ins 10 15 10 15, w 940!, h 250!","[left]","[20!][200!, top]" );
                    setLayout( layout );

                    int     cols        = (int) Math.ceil( ((float)ioData.ioCount) / 5);
                    Integer inputState  = ioData.inputPort;
                    Integer outputState = ioData.outputPort;
                    String  inputText   = String.format( bundle.getString( "IOStatusView.input" ) + "%0" + cols * 2 + "X",
                                                         inputState & ( ( ( ( long )1 ) << ( cols * 8 ) ) - 1 ) );
                    String outputText = String.format( bundle.getString( "IOStatusView.output" ) + "%0" + cols * 2 + "X",
                                                       outputState & ( ( ( ( long )1 ) << ( cols * 8 ) ) - 1 ) );
                    JLabel lblInputStatus = new JLabel( "--", SwingConstants.CENTER );
                    lblInputStatus.setText( inputText );
                    lblInputStatus.setFont(FontFactory.FONT_12_PLAIN);
                    lblInputStatus.setForeground(Color.WHITE);

                    JLabel lblOutputStatus = new JLabel( "--", SwingConstants.CENTER );
                    lblOutputStatus.setText( outputText );
                    lblOutputStatus.setFont(FontFactory.FONT_12_PLAIN);
                    lblOutputStatus.setForeground(Color.WHITE);

                    MigLayout statusLayout = new MigLayout();
                    JPanel    statusPanel  = new JPanel( statusLayout );
                    statusPanel.setOpaque( false );

                    add( lblInputStatus, "flowx, cell 0 0" );
                    add( lblOutputStatus, "gap 10, cell 0 0" );
                    add( statusPanel, "flowy, cell 0 1" );
           
                    for ( Integer index = 0 ; index < Math.ceil( ((float)ioData.ioCount) / 5 ) ; index++ ) {
                        JLabel inoutText1 = new JLabel( bundle.getString( "IOStatusView.inout" ), SwingConstants.RIGHT );
                        inoutText1.setForeground(Color.WHITE);
                        statusPanel.add( inoutText1, String.format( "cell %d 0", index ) );
                    }

                    StringBuffer sb = new StringBuffer();
                    for ( int i = 0 ; i < cols ; i++ )
                        sb.append( "[right]" );
                    statusLayout.setColumnConstraints( sb.toString() );
                    for ( Integer index = 0 ; index < ioData.ioCount ; index++ ) {
                        ImageFactory firstBall  = ImageFactory.LIGHT_DARK_RED;
                        ImageFactory secondBall = ImageFactory.LIGHT_DARK_RED;
                        if ( inputState != null ) {
                            if ( ( ( inputState >> index ) & 1 ) == 1 )
                                firstBall = ImageFactory.LIGHT_BRIGHT_GREEN;
                            else
                                firstBall = ImageFactory.LIGHT_DARK_GREEN;
                        }
                        if ( outputState != null ) {
                            if ( ( ( outputState >> index ) & 1 ) == 1 )
                                secondBall = ImageFactory.LIGHT_BRIGHT_ORANGE;
                            else
                                secondBall = ImageFactory.LIGHT_DARK_ORANGE;
                        }
                        if(((ioData.sync>>index)&1)==1) {
                            firstBall = ImageFactory.S_BALL;
                            secondBall = ImageFactory.S_BALL;
                        } else if(((ioData.blink>>index)&1)==1) {
                            firstBall = ImageFactory.B_BALL;
                            secondBall = ImageFactory.B_BALL;
                        } else if(((ioData.fast>>index)&1)==1) {
                            firstBall = ImageFactory.F_BALL;
                            secondBall = ImageFactory.F_BALL;
                        }

                        ImageIcon img = ImageFactory.merge( firstBall, secondBall, 16 );
                        JButton   btn = new JButton( String.format( "%02d", index ), img );
                        btn.setForeground(Color.WHITE);
                        btn.setIconTextGap(8);
                        btn.setVerticalTextPosition( SwingConstants.TOP );
                        btn.setHorizontalTextPosition( SwingConstants.LEFT );
                        btn.setHorizontalAlignment( SwingConstants.RIGHT );
                        btn.setFocusPainted( false );
                        btn.setMargin( new Insets( 5, 10, 5, 0 ) );
                        btn.setContentAreaFilled( false );
                        btn.setBorderPainted( false );
                        btn.setOpaque( false );
                        statusPanel.add( btn, String.format( "cell %d %d", ( index ) / 5, ( index ) % 5 + 1 ) );
                    }
                    setVisible( true );
                } else {
                    setVisible( false );
                }
                updateUI();
            }
        } );
    }


    public static void main ( String... args ) {
        JFrame f = new JFrame( "helloworld" );
        f.getContentPane().add( new IOStatusView() );
        f.pack();
        f.setVisible( true );
    }
}
