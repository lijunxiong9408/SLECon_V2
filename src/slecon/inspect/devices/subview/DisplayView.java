package slecon.inspect.devices.subview;
import java.awt.Color;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SequenceImage;
import slecon.inspect.devices.DisplayBean;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;




public class DisplayView extends JPanel {
    private static final long           serialVersionUID = -5597521744900271683L;
    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle("inspect.DevicePanel");
    
    private static final ImageIcon[] UP_ANIM_ICON    = ImageFactory.ARROW_ANIM_UP.icons( 24, 24 );
    private static final ImageIcon[] DOWN_ANIM_ICON  = ImageFactory.ARROW_ANIM_DOWN.icons( 24, 24 );
    private static final ImageIcon[] UP_ARROW_ICON   = { ImageFactory.ARROW_ANIM_UP.icon( 24, 24 ) };
    private static final ImageIcon[] DOWN_ARROW_ICON = { ImageFactory.ARROW_ANIM_DOWN.icon( 24, 24 ) };
    private static final ImageIcon[] NONE_ARROW_ICON = { ImageFactory.NONE.icon( 24, 24 ) };
    private final static ImageIcon[] NONE_IMAGEICON  = { ImageFactory.NONE.icon( 24, 24 ) };
    private static String            yes_text;
    private static String            no_text;
    private JLabel                   labelChar;
    private JLabel                   labelArrow;
    private JLabel                   labelMsg;
    private JLabel                   labelDim;
    private SequenceImage            indicator;
    private JLabel                   lblChar;
    private JLabel                   lblArrow;
    private JLabel                   lblMsg;
    private JLabel                   lblDim;
    private DisplayBean              display;




    /**
     * Create the panel.
     */
    public DisplayView () {
        initGUI();
        updateI18nGUI();
    }


    private void updateI18nGUI () {
        yes_text = TEXT.getString( "YES.text" );
        no_text  = TEXT.getString( "NO.text" );
        labelArrow.setText( TEXT.getString( "Display.Arrow.text" ) );
        labelChar.setText( TEXT.getString( "Display.Char.text" ) );
        labelDim.setText( TEXT.getString( "Display.Dim.text" ) );
        labelMsg.setText( TEXT.getString( "Display.Msg.text" ) );
    }


    private void initGUI () {
        setOpaque( false );
        setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        setLayout( new MigLayout( "inset 10 15 0 15", "[80!, left][120!, left]", "[][][][][]" ) );
        indicator = new SequenceImage( NONE_IMAGEICON, true );
        indicator.setFont( getFont().deriveFont( 24F ) );
        indicator.setForeground(Color.WHITE);
        add( indicator, "cell 0 0 2 1,alignx center" );
        labelChar = new JLabel( "Char:" );
        labelChar.setFont( FontFactory.FONT_12_BOLD );
        labelChar.setForeground(Color.WHITE);
        add( labelChar, "cell 0 1" );
        labelArrow = new JLabel( "Arrow:" );
        labelArrow.setFont( FontFactory.FONT_12_BOLD );
        labelArrow.setForeground(Color.WHITE);
        add( labelArrow, "cell 0 2" );
        labelMsg = new JLabel( "Msg:" );
        labelMsg.setFont( FontFactory.FONT_12_BOLD );
        labelMsg.setForeground(Color.WHITE);
        add( labelMsg, "cell 0 3" );
        labelDim = new JLabel( "Dim:" );
        labelDim.setFont( FontFactory.FONT_12_BOLD );
        labelDim.setForeground(Color.WHITE);
        add( labelDim, "cell 0 4,aligny center" );
        lblChar = new JLabel( "-" );
        lblChar.setFont( FontFactory.FONT_12_PLAIN );
        lblChar.setForeground(Color.WHITE);
        add( lblChar, "cell 1 1,width 70,alignx left" );
        lblArrow = new JLabel( "-" );
        lblArrow.setFont( FontFactory.FONT_12_PLAIN );
        lblArrow.setForeground(Color.WHITE);
        add( lblArrow, "cell 1 2,width 70,alignx left" );
        lblMsg = new JLabel( "-" );
        lblMsg.setFont( FontFactory.FONT_12_PLAIN );
        lblMsg.setForeground(Color.WHITE);
        add( lblMsg, "cell 1 3,width 70,alignx left" );
        lblDim = new JLabel( "-" );
        lblDim.setFont( FontFactory.FONT_12_PLAIN );
        lblDim.setForeground(Color.WHITE);
        add( lblDim, "cell 1 4,width 70,alignx left" );
    }


    public void setDisplay ( DisplayBean display ) {
        this.display = display;
        updateDisplayBean();
    }


    public DisplayBean getDisplay () {
        return display;
    }


    public void updateDisplayBean () {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                ImageIcon[] dir       = NONE_IMAGEICON;
                String      floor     = "--";
                String      character = "-";
                String      arrow     = "-";
                String      msg       = "-";
                String      dim       = no_text;
                if ( display != null ) {
                    if ( display.getArrow() != null ) {
                        switch ( display.getArrow() ) {
                        case GREEN_UP_ANIMATION :
                            dir = UP_ANIM_ICON;
                            break;
                        case RED_DOWN_ANIMATION :
                            dir = DOWN_ANIM_ICON;
                            break;
                        case GREEN_UP :
                            dir = UP_ARROW_ICON;
                            break;
                        case RED_DOWN :
                            dir = DOWN_ARROW_ICON;
                            break;
                        case NONE_DIM:
                            dim = yes_text;
                        case NONE :
                        default :
                            dir = NONE_ARROW_ICON;
                        }
                    }
                    if ( display.getPosition() != null ){
                        floor = display.getPosition();
                        character = display.getPosition();
                    }
                    if ( display.getArrow() != null )
                        arrow = String.format( "[0x%02x] %s", display.getArrow().getCode(), display.getArrow() );
                    if ( display.getMsg() != null )
                        msg = String.format( "[0x%02x] %s", display.getMsg().getCode(), display.getMsg().toString() );
                    setVisible( true );
                } else {
                    setVisible( false );
                }
                indicator.setText( floor );
                indicator.setImages( dir );
                lblChar.setText( character );
                lblChar.setToolTipText( character );
                lblArrow.setText( arrow );
                lblArrow.setToolTipText( arrow );
                lblMsg.setText( msg );
                lblMsg.setToolTipText( msg );
                lblDim.setText( dim );
            }
        } );
    }
}
