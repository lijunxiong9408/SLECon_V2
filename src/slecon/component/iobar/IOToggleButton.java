package slecon.component.iobar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.util.zip.DataFormatException;

import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import base.cfg.ImageFactory;
import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event.Item;




public class IOToggleButton extends JToggleButton {
    private static final long  serialVersionUID   = 5784188344638431556L;
    private static final Image BG_INPUT           = ImageFactory.EVENT_INPUT.image();
    private static final Image BG_OUTPUT          = ImageFactory.EVENT_OUTPUT.image();
    private final JLabel       labelBus           = new JLabel( "" );                                                 
    private final JLabel       labelType          = new JLabel( "" );                                                 
    private final JLabel       labelID            = new JLabel( Messages.getString( "IOToggleButton.LabelID" ) );     
    private final JLabel       labelBit           = new JLabel( Messages.getString( "IOToggleButton.LabelBit" ) );    
    private final JLabel       labelVal           = new JLabel( Messages.getString( "IOToggleButton.LabelVal" ) );    
    private final JLabel       lblID              = new JLabel( "23 (DM3A)" );                                        
    private final JLabel       lblBit             = new JLabel( "3" );                                                
    private final JLabel       lblVal             = new JLabel( "3" );                                                
    private Type               type;
    private Item               item;




    // class constructor
    public IOToggleButton ( Type type, Item info ) {
        initGUI();
        setType( type );
        setItem( info );
    }


    public Type getType () {
        return type;
    }


    public void setType ( Type type ) {
        this.type = type;
        if ( type == null )
            labelType.setText( Messages.getString( "IOToggleButton.EmptyType" ) );    
        else {
            labelType.setText( type.toString() );
            switch ( type ) {
            case INPUT :
                labelVal.setVisible( true );
                lblVal.setVisible( true );
                break;
            case OUTPUT :
                labelVal.setVisible( false );
                lblVal.setVisible( false );
                break;
            }
        }
    }


    public Item getItem () {
        return item;
    }


    public static int toUnsignedInt ( byte x ) {
        return ( ( int )x ) & 0xff;
    }


    public void setItem ( Item deviceData ) {
        this.item = deviceData;
        if ( item != null ) {
            lblID.setText( Integer.toString( toUnsignedInt( item.getId() ) ) );
            lblBit.setText( Integer.toString( toUnsignedInt( item.getBit() ) ) );
            lblVal.setText( Integer.toString( toUnsignedInt( item.getValue() ) ) );
            try {
                labelBus.setText( Messages.getString( item.getBus().toString() ) );
            } catch ( DataFormatException e ) {
            }
        } else {
            labelBus.setText( Messages.getString( "IOToggleButton.EmptyBus" ) );    
            lblID.setText( Messages.getString( "IOToggleButton.EmptyID" ) );        
            lblBit.setText( Messages.getString( "IOToggleButton.EmptyBit" ) );      
            lblVal.setText( Messages.getString( "IOToggleButton.EmptyVal" ) );      
        }
    }


    private void initGUI () {
        setMargin( new Insets( 0, 0, 0, 0 ) );
        setOpaque( false );
        setSelected( false );
        setFocusPainted( false );
        setContentAreaFilled( false );
        setBorderPainted( false );
        setPreferredSize( new Dimension( BG_INPUT.getWidth( this ), BG_INPUT.getHeight( this ) ) );
        setHorizontalAlignment( SwingConstants.LEFT );
        setVerticalTextPosition( SwingConstants.CENTER );
        setHorizontalTextPosition( SwingConstants.RIGHT );

        String layoutConstraints = String.format( "ins 0, gap 0, nogrid, w %d!, h %d!", BG_INPUT.getWidth( this ), BG_INPUT.getHeight( this ) );    
        setLayout( new MigLayout( layoutConstraints ) );
        labelType.setOpaque( false );
        labelType.setForeground( Color.white );
        labelBus.setOpaque( false );
        labelBus.setForeground( Color.white );
        labelID.setOpaque( false );
        labelID.setForeground( Color.white );
        labelBit.setOpaque( false );
        labelBit.setForeground( Color.white );
        labelVal.setOpaque( false );
        labelVal.setForeground( Color.white );
        lblID.setForeground(Color.WHITE);
        lblBit.setForeground(Color.WHITE);
        lblVal.setForeground(Color.WHITE);
        add( labelBus, "pos 10 12, hidemode 3" );           
        add( labelType, "pos 50 12, hidemode 3" );       
        add( labelID, "pos 15 32, hidemode 3" );           
        add( labelBit, "pos 15 52, hidemode 3" );       
        add( labelVal, "pos 15 72, hidemode 3" );    
        add( lblID, "pos 60 32" );                         
        add( lblBit, "pos 60 52, hidemode 3" );         
        add( lblVal, "pos 60 72, hidemode 3" );      
    }


    public void paintComponent ( Graphics g ) {
        if ( isSelected() ) {
            if ( Type.INPUT == getType() ) {
                g.drawImage( BG_INPUT, 0, 0, getWidth(), getHeight(), this );
            } else if ( Type.OUTPUT == getType() ) {
                g.drawImage( BG_OUTPUT, 0, 0, getWidth(), getHeight(), this );
            }
        } else {
            if ( Type.INPUT == getType() ) {
                g.drawImage( BG_INPUT, 0, 0, getWidth(), getHeight(), this );
            } else if ( Type.OUTPUT == getType() ) {
                g.drawImage( BG_OUTPUT, 0, 0, getWidth(), getHeight(), this );
            }
        }
    }
}
