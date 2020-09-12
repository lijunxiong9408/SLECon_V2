package slecon.inspect.calls;
import java.awt.Color;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import logic.connection.LiftConnectionBean;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.component.SequenceImage;
import comm.constants.DoorAction;
import comm.constants.OcsDirection;




public class LiftStatusView extends JPanel {
    private static final ImageIcon[] UP_ANIM_ICON      = ImageFactory.ARROW_ANIM_UP.icons( 24, 24 );
    private static final ImageIcon[] DOWN_ANIM_ICON    = ImageFactory.ARROW_ANIM_DOWN.icons( 24, 24 );
    private static final ImageIcon[] UP_ARROW_ICON     = { ImageFactory.ARROW_ANIM_UP.icon( 24, 24 ) };
    private static final ImageIcon[] DOWN_ARROW_ICON   = { ImageFactory.ARROW_ANIM_DOWN.icon( 24, 24 ) };
    private static final ImageIcon[] NONE_ARROW_ICON   = { ImageFactory.NONE.icon( 24, 24 ) };
    private static final long        serialVersionUID  = 8636374801455562132L;
    private static final ImageIcon   DOOR_OPENED_ICON  = ImageFactory.DOOR_OPENED_ICON.icon( 100, 100 );
    private static final ImageIcon   DOOR_OPENING_ICON = ImageFactory.DOOR_OPENING_ICON.icon( 100, 100 );
    private static final ImageIcon   DOOR_CLOSING_ICON = ImageFactory.DOOR_CLOSING_ICON.icon( 100, 100 );
    private static final ImageIcon   DOOR_CLOSED_ICON  = ImageFactory.DOOR_CLOSED_ICON.icon( 100, 100 );
    private static final ImageIcon   DOOR_UNKNOWN_ICON = ImageFactory.DOOR_UNKNOWN_ICON.icon( 100, 100 );
    private static final ImageIcon   DOOR_SGS_ICON     = ImageFactory.DOOR_SGS.icon( 100, 100 );
    static HashMap<String, String>      styles                  = new HashMap<>();
    private MigLayout layout;
    private final LiftConnectionBean connBean;
    private JLabel                   lblDescription;
    private JLabel                   lblFrontDoorIcon;
    private JLabel                   lblRearDoorIcon;
    private JLabel                   lblfloor;
    private SequenceImage            lblDirIcon;

    static {
        styles.put( "lblFrontDoorIcon", "20 60 100 100 c" );
        styles.put( "lblRearDoorIcon", "145 60 100 100 c");
        styles.put( "commDoorIcon", "75 60 100 100 c");
        styles.put( "lblfloor", "140 20 24 24 c" );
        styles.put( "lblDirIcon", "113 20 24 24 c" );
    }


    public LiftStatusView ( LiftConnectionBean bean ) {
        this.connBean = bean;
        initGUI();
    }


    private void initGUI () {
        setOpaque( false );
        layout = new MigLayout( "w 250!, h 220!" );
        setLayout( layout );
        lblDirIcon = new SequenceImage();
        add( lblDirIcon);
        StartUI.setStyle(layout, styles, lblDirIcon, "lblDirIcon");
        
        lblfloor = new JLabel();
        lblfloor.setFont(FontFactory.FONT_16_BOLD);
        lblfloor.setForeground(Color.WHITE);
        add( lblfloor);
        StartUI.setStyle(layout, styles, lblfloor, "lblfloor");
        
        lblFrontDoorIcon = new JLabel();
        add( lblFrontDoorIcon );
        StartUI.setStyle(layout, styles, lblFrontDoorIcon, "lblFrontDoorIcon");
        
        lblRearDoorIcon = new JLabel();
        add( lblRearDoorIcon );
        StartUI.setStyle(layout, styles, lblRearDoorIcon, "lblRearDoorIcon");
        
        lblDescription = new JLabel( "" );
        //add( lblDescription, "cell 0 2" );
    }


    public void setData ( LiftStatusBean liftStatus ) {
        String      floor = "??";
        ImageIcon[] dir   = NONE_ARROW_ICON;
        ImageIcon   front_door  = DOOR_UNKNOWN_ICON;
        ImageIcon   Rear_door  = DOOR_UNKNOWN_ICON;
        
        if ( liftStatus != null ) {

            if ( liftStatus.getDirection() != null ) {
                switch ( liftStatus.getDirection() ) {
                case UP :
                    dir = liftStatus.isAnimation() ? UP_ANIM_ICON : UP_ARROW_ICON;
                    break;
                case DOWN :
                    dir = liftStatus.isAnimation() ? DOWN_ANIM_ICON : DOWN_ARROW_ICON;
                    break;
                case NO :
                    dir = NONE_ARROW_ICON;
                    break;
                default :
                    dir = NONE_ARROW_ICON;
                }
            }

            switch ( liftStatus.getFrontdoorAction() ) {
            case NOP :
            	front_door = DOOR_UNKNOWN_ICON;
                break;
            case CLOSED :
            	front_door = DOOR_CLOSED_ICON;
                break;
            case CLOSING :
            	front_door = DOOR_CLOSING_ICON;
                break;
            case OPENED :
            	front_door = DOOR_OPENED_ICON;
                break;
            case OPENING :
            	front_door = DOOR_OPENING_ICON;
                break;
            case SGS :
            	front_door = DOOR_SGS_ICON;
                break;
            }
            
            switch ( liftStatus.getReardoorAction() ) {
            case NOP :
            	Rear_door = DOOR_UNKNOWN_ICON;
                break;
            case CLOSED :
            	Rear_door = DOOR_CLOSED_ICON;
                break;
            case CLOSING :
            	Rear_door = DOOR_CLOSING_ICON;
                break;
            case OPENED :
            	Rear_door = DOOR_OPENED_ICON;
                break;
            case OPENING :
            	Rear_door = DOOR_OPENING_ICON;
                break;
            case SGS :
            	Rear_door = DOOR_SGS_ICON;
                break;
            }
            floor = liftStatus.getFloorText();
        }
        
        lblDirIcon.setImages( dir );
        lblFrontDoorIcon.setIcon( front_door );
        lblRearDoorIcon.setIcon( Rear_door );
        lblfloor.setText( floor );
        
    }
    
    public void setDoorView(int type) {
    	if(type == 1) {
    		SwingUtilities.invokeLater( new Runnable() {
        		@Override
				public void run() {
					// TODO Auto-generated method stub
        			lblFrontDoorIcon.setVisible(true);
        			lblRearDoorIcon.setVisible(false);
	        		StartUI.setStyle(layout, styles, lblFrontDoorIcon, "commDoorIcon");
				}
    		});
    	}else if(type == 2) {
    		SwingUtilities.invokeLater( new Runnable() {
    			@Override
				public void run() {
					// TODO Auto-generated method stub
    				lblFrontDoorIcon.setVisible(false);
    				lblRearDoorIcon.setVisible(true);
	        		StartUI.setStyle(layout, styles, lblRearDoorIcon, "commDoorIcon");
				}
    		});
    	}else{
    		SwingUtilities.invokeLater( new Runnable() {
        		@Override
				public void run() {
					// TODO Auto-generated method stub
        			lblFrontDoorIcon.setVisible(true);
        			lblRearDoorIcon.setVisible(true);
	        		StartUI.setStyle(layout, styles, lblFrontDoorIcon, "lblFrontDoorIcon");
	        		StartUI.setStyle(layout, styles, lblRearDoorIcon, "lblRearDoorIcon");
				}
    		});
    	}
    }


    public final static class LiftStatusBean {
        private final DoorAction   frontdoorAction;
        private final DoorAction   reardoorAction;
        private final String       floorText;
        private final OcsDirection direction;
        private final boolean      animation;
        
        
        
        
        public LiftStatusBean(DoorAction frontdoorAction, DoorAction reardoorAction, String floorText, OcsDirection dir, boolean animation) {
            super();
            this.frontdoorAction = frontdoorAction;
            this.reardoorAction = reardoorAction;
            this.floorText = floorText;
            this.direction = dir;
            this.animation = animation;
        }

        public DoorAction getFrontdoorAction() {
			return frontdoorAction;
		}

		public DoorAction getReardoorAction() {
			return reardoorAction;
        }


        public final String getFloorText() {
            return floorText;
        }


        public final OcsDirection getDirection() {
            return direction;
        }


        public final boolean isAnimation() {
            return animation;
        }
    }
}
