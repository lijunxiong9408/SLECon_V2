package slecon.inspect.door;
import static logic.util.SiteManagement.MON_MGR;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.io.crossbar.CrossBar;
import logic.io.crossbar.InputSourceA05;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SubtleSquareBorder;
import slecon.component.ValueTextField;
import slecon.component.Workspace;
import slecon.home.PosButton;
import slecon.home.dashboard.VerticalSrcollBarUI;
import slecon.inspect.InspectPanel;
import slecon.interfaces.InspectView;
import slecon.interfaces.Page;
import comm.Parser_McsConfig;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.constants.DoorAction;
import comm.event.LiftDataChangedListener;


@InspectView(
    path      = "DoorStatus",
    sortIndex = 0x0400
)
public class DoorStatusA05 extends JPanel implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = -552013171218549161L;
    private static final ResourceBundle bundle = ToolBox.getResourceBundle( "inspect.Door" );
    private JPanel		  	  panelNavigation;
    private JPanel		  	  panelMain;
    private JPanel		  	  panelLiftSelector;
    private JPanel		  	  panelDoorSystem;
    private JPanel		  	  panelFrontDoor;
    private JPanel		  	  panelRearDoor;
    private MigLayout         layout;
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("panelLiftSelector", "20 20 250 60 c");
    	styles.put("labDoorSystem", "20 80 100 20 c");
    	styles.put("panelDoorSystem", "20 100 960 200 c");
    	styles.put("labFrontDoor", "20 305 100 20 l");
    	styles.put("labRearDoor", "350 305 100 20 l");
    	styles.put("panelFrontDoor", "20 325 270 270 c");
    	styles.put("panelRearDoor", "350 325 270 270 c");
    	
    }
    private String[] navigationText = {Dict.lookup("Inspect"), Dict.lookup("Door")};
    
    private static final ImageIcon      DOOR_OPENED_ICON     = ImageFactory.DOOR_OPENED_ICON.icon( 100, 100 );
    private static final ImageIcon      DOOR_OPENING_ICON    = ImageFactory.DOOR_OPENING_ICON.icon( 100, 100 );
    private static final ImageIcon      DOOR_CLOSING_ICON    = ImageFactory.DOOR_CLOSING_ICON.icon( 100, 100 );
    private static final ImageIcon      DOOR_CLOSED_ICON     = ImageFactory.DOOR_CLOSED_ICON.icon( 100, 100 );
    private static final ImageIcon      DOOR_UNKNOWN_ICON    = ImageFactory.DOOR_UNKNOWN_ICON.icon( 100, 100 );
    private static final ImageIcon      DOOR_SGS             = ImageFactory.DOOR_SGS.icon( 100, 100 );
    private static final ImageIcon      INPUT_ON_ICON        = ImageFactory.LIGHT_BRIGHT_GREEN.icon( 16, 16 );
    private static final ImageIcon      INPUT_OFF_ICON       = ImageFactory.LIGHT_DARK_GREEN.icon( 16, 16 );
    private static final ImageIcon      OUTPUT_ON_ICON       = ImageFactory.LIGHT_BRIGHT_ORANGE.icon( 16, 16 );
    private static final ImageIcon      OUTPUT_OFF_ICON      = ImageFactory.LIGHT_DARK_ORANGE.icon( 16, 16 );
    private static final ImageIcon      GRAY_ICON            = ImageFactory.LIGHT_BLACK.icon( 16, 16 );
    private JLabel                      lblFUdz;
    private JLabel                      lblFLdz;
    private JLabel                      lblRUdz;
    private JLabel                      lblRLdz;
    private JLabel                      lblNdz;
    private JLabel                      lblUrl;
    private JLabel                      lblLrl;
    private JLabel                      lblIns;
    private JLabel                      lblInsup;
    private JLabel                      lblInsdown;
    private JLabel                      lblFan;
    private JLabel                      lblLight;
    private ValueTextField				fmtLoad;
    private DCSStatusBean               dcsStatusBean;
    private DoorBean                    frontDoor;
    private JLabel                      lblFrontDol;
    private JLabel                      lblFrontDcl;
    private JLabel                      lblFrontSgs;
    private JLabel                      lblFrontEdp;
    private JLabel                      lblFrontDor;
    private JLabel                      lblFrontDcr;
    private JLabel                      lblFrontNdr;
    private JLabel                      lblFrontDoorImg;
    private JLabel                      titleFrontDoorStatus;
    private JLabel                      lblFrontDoorStatusText;
    private DoorBean                    rearDoor;
    private JLabel                      lblRearDol;
    private JLabel                      lblRearDcl;
    private JLabel                      lblRearSgs;
    private JLabel                      lblRearEdp;
    private JLabel                      lblRearDor;
    private JLabel                      lblRearDcr;
    private JLabel                      lblRearNdr;
    private JLabel                      lblRearDoorImg;
    private JLabel                      titleRearDoorStatus;
    private JLabel                      lblRearDoorStatusText;
    
    ///////////////////// interface <Page> /////////////////////
    private final LiftConnectionBean connBean;
    private Parser_McsConfig 		 mcsconfig;
    private Parser_Status 			 status;
    protected enum Type { INPUT, OUTPUT, }
    public static enum OnOffStatus {
        ON, OFF, DISABLED;

        private final String str;

        OnOffStatus () {
            str = bundle.getString("OnOffStatus" + "." + name());
        }

        public String toString () {
            return str;
        }
    }
    
    public DoorStatusA05 ( LiftConnectionBean bean) {
    	this.connBean = bean;
    	setLayout(new BorderLayout());
        initGUI();
        setDCSStatusBean( null );
        setFrontDoorBean( null );
        setRearDoorBean(null);
    }
    
    public void setDCSStatusBean ( DCSStatusBean bean ) {
        if ( bean == null )
            bean = new DCSStatusBean();
        this.dcsStatusBean = bean;
        updateDCSStatus();
    }


    public void setFrontDoorBean ( DoorBean bean ) {
        if ( bean == null )
            bean = new DoorBean();
        frontDoor = bean;
        updateFrontDoor();
    }
    
    public void setRearDoorBean ( DoorBean bean ) {
        if ( bean == null )
            bean = new DoorBean();
        rearDoor = bean;
        updateRearDoor();
    }
    
    public void setCabinLoadBean ( LoadBean bean ) {
        if ( bean == null )
            bean = new LoadBean();
        fmtLoad.setText( String.format( "%d%%", bean.getLoadValue() ));
    }
    
    private DoorAction parseDoorStatus ( DoorBean door ) {
        if ( door == null )
            return null;
        if ( door.getSgs() != OnOffStatus.ON || door.getEdp() != OnOffStatus.ON  )
            return DoorAction.SGS;
        if ( door.getDoorStatus() == null )
            return null;
        switch ( door.getDoorStatus() ) {
        case OPENED :
            return DoorAction.OPENED;
        case OPENING :
            return DoorAction.OPENING;
        case CLOSING :
            return DoorAction.CLOSING;
        case CLOSED :
            return DoorAction.CLOSED;
        case SGS :
            return DoorAction.SGS;
        default :
            return null;
        }
    }
    
    private ImageIcon doorstatusToIcon ( DoorBean door ) {
        DoorAction status = parseDoorStatus( door );
        if ( status == null )
            return DOOR_UNKNOWN_ICON;
        switch ( status ) {
        case OPENED :
            return DOOR_OPENED_ICON;
        case OPENING :
            return DOOR_OPENING_ICON;
        case CLOSING :
            return DOOR_CLOSING_ICON;
        case CLOSED :
            return DOOR_CLOSED_ICON;
        case SGS :
            return DOOR_SGS;
        default :
            return DOOR_UNKNOWN_ICON;
        }
    }
    
    private ImageIcon onoffstatusToIcon ( Type io, OnOffStatus status ) {
        if ( status == null || io == null )
            return GRAY_ICON;
        switch ( status ) {
        case ON :
            return io == Type.INPUT
                   ? INPUT_ON_ICON
                   : OUTPUT_ON_ICON;
        case OFF :
            return io == Type.INPUT
                   ? INPUT_OFF_ICON
                   : OUTPUT_OFF_ICON;
        case DISABLED :
        default :
            return GRAY_ICON;
        }
    }
    
    private void updateFrontDoor () {
        String status = parseDoorStatus( frontDoor ) == null
                        ? "-"
                        : parseDoorStatus( frontDoor ).toString();
        lblFrontDoorStatusText.setText( status );
        lblFrontDoorImg.setIcon( doorstatusToIcon( frontDoor ) );
        lblFrontDol.setIcon( onoffstatusToIcon( Type.INPUT, frontDoor.getDol() ) );
        lblFrontDcl.setIcon( onoffstatusToIcon( Type.INPUT, frontDoor.getDcl() ) );
        lblFrontSgs.setIcon( onoffstatusToIcon( Type.INPUT, frontDoor.getSgs() ) );
        lblFrontEdp.setIcon( onoffstatusToIcon( Type.INPUT, frontDoor.getEdp() ) );
        lblFrontDor.setIcon( onoffstatusToIcon( Type.OUTPUT, frontDoor.getDor() ) );
        lblFrontDcr.setIcon( onoffstatusToIcon( Type.OUTPUT, frontDoor.getDcr() ) );
        lblFrontNdr.setIcon( onoffstatusToIcon( Type.OUTPUT, frontDoor.getNdr() ) );
    }
    
    private void updateRearDoor () {
        String status = parseDoorStatus( rearDoor ) == null
                        ? "-"
                        : parseDoorStatus( rearDoor ).toString();
        lblRearDoorStatusText.setText( status );
        lblRearDoorImg.setIcon( doorstatusToIcon( rearDoor ) );
        lblRearDol.setIcon( onoffstatusToIcon( Type.INPUT, rearDoor.getDol() ) );
        lblRearDcl.setIcon( onoffstatusToIcon( Type.INPUT, rearDoor.getDcl() ) );
        lblRearSgs.setIcon( onoffstatusToIcon( Type.INPUT, rearDoor.getSgs() ) );
        lblRearEdp.setIcon( onoffstatusToIcon( Type.INPUT, rearDoor.getEdp() ) );
        lblRearDor.setIcon( onoffstatusToIcon( Type.OUTPUT, rearDoor.getDor() ) );
        lblRearDcr.setIcon( onoffstatusToIcon( Type.OUTPUT, rearDoor.getDcr() ) );
        lblRearNdr.setIcon( onoffstatusToIcon( Type.OUTPUT, rearDoor.getNdr() ) );
    }



    private void updateDCSStatus () {
        lblFUdz.setIcon( onoffstatusToIcon( Type.INPUT, dcsStatusBean.getFudz() ) );
        lblFLdz.setIcon( onoffstatusToIcon( Type.INPUT, dcsStatusBean.getFldz() ) );
        lblRUdz.setIcon( onoffstatusToIcon( Type.INPUT, dcsStatusBean.getRudz() ) );
        lblRLdz.setIcon( onoffstatusToIcon( Type.INPUT, dcsStatusBean.getRldz() ) );
        lblNdz.setIcon( onoffstatusToIcon( Type.INPUT, dcsStatusBean.getNdz() ) );
        lblUrl.setIcon( onoffstatusToIcon( Type.INPUT, dcsStatusBean.getUsl() ) );
        lblLrl.setIcon( onoffstatusToIcon( Type.INPUT, dcsStatusBean.getLsl() ) );
        lblIns.setIcon( onoffstatusToIcon( Type.INPUT, dcsStatusBean.getIns() ) );
        lblInsup.setIcon( onoffstatusToIcon( Type.INPUT, dcsStatusBean.getInsup() ) );
        lblInsdown.setIcon( onoffstatusToIcon( Type.INPUT, dcsStatusBean.getInsdown() ) );
        lblFan.setIcon( onoffstatusToIcon( Type.OUTPUT, dcsStatusBean.getFan() ) );
        lblLight.setIcon( onoffstatusToIcon( Type.OUTPUT, dcsStatusBean.getLight() ) );
    }

    private void initGUI () {
    	removeAll();
        JPanel workspace = new JPanel( new MigLayout( "gap 0", "[left]", "[30!]10[600]" ) );
        workspace.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        
    /*------------------------------------------------------------------------------------------------------------*/   
        panelNavigation = new JPanel(new MigLayout( "nogrid, w 985!, h 30!, gap 0", "[left]", "[center]" ));
        //panelNavigation.setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
        panelNavigation.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        workspace.add(panelNavigation,"cell 0 0");
        int index = 0;
        for (String text : navigationText) {
        	if(index > 0) {
        		PosButton icon = new PosButton(ImageFactory.ARROW_NAVIGATION.icon(11,12));
        		panelNavigation.add(icon);
			}
        	
        	PosButton lab = new PosButton(text, StartUI.BORDER_COLOR, Color.WHITE);
        	lab.setForeground(StartUI.BORDER_COLOR);
        	lab.setFont(FontFactory.FONT_12_BOLD);
        	if(index == 0) {
        		lab.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						final InspectPanel panelBinder = InspectPanel.build(connBean, null);
                        StartUI.getTopMain().push(panelBinder);
					}
				});
        	}
			panelNavigation.add(lab);
			index += 1;
		}
        
    /*------------------------------------------------------------------------------------------------------------*/        
        layout = new MigLayout( "nogrid, w 1000!, h 600!, gap 0", "[left]", "[center]" );
        panelMain = new JPanel();
        panelMain.setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
        panelMain.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelMain.setLayout(layout);
        workspace.add(panelMain,"cell 0 1");
      
        panelLiftSelector = new JPanel();
        panelLiftSelector.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelLiftSelector.add(StartUI.getLiftSelector());
        panelMain.add(panelLiftSelector);
        StartUI.setStyle(layout, styles, panelLiftSelector, "panelLiftSelector");
        
        // DCS
        JLabel labDoorSystem = new JLabel(bundle.getString( "DCS" ));
        labDoorSystem.setForeground(Color.WHITE);
        labDoorSystem.setFont(FontFactory.FONT_12_BOLD);
        panelMain.add(labDoorSystem);
        StartUI.setStyle(layout, styles, labDoorSystem, "labDoorSystem");
        
        panelDoorSystem = new JPanel(new MigLayout( "ins 20, w 640!, h 200!", "[110!]10[110!]10[110!]10[110!]10[110!]", "[][]" ));
        panelDoorSystem.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        panelDoorSystem.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelMain.add(panelDoorSystem);
        StartUI.setStyle(layout, styles, panelDoorSystem, "panelDoorSystem");
        
        JLabel lblIoStatus = new JLabel( bundle.getString( "IOStatus" ) );
        lblIoStatus.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        lblIoStatus.setForeground(Color.WHITE);
        panelDoorSystem.add( lblIoStatus, "cell 0 0 2 1" );
        JLabel lblCabinStatus = new JLabel( bundle.getString( "CabinStatus" ) );
        lblCabinStatus.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        lblCabinStatus.setForeground(Color.WHITE);
        panelDoorSystem.add( lblCabinStatus, "cell 2 0" );
        lblFUdz = new JLabel( "FUDZ" );
        lblFUdz.setForeground(Color.WHITE);
        panelDoorSystem.add( lblFUdz, "flowy,cell 0 1,growx,aligny top,gapy 5" );
        lblFLdz = new JLabel( "FLDZ" );
        lblFLdz.setForeground(Color.WHITE);
        panelDoorSystem.add( lblFLdz, "cell 0 1,growx,gapy 5" );
        lblRUdz = new JLabel( "RUDZ" );
        lblRUdz.setForeground(Color.WHITE);
        panelDoorSystem.add( lblRUdz, "flowy,cell 0 1,growx,aligny top,gapy 5" );
        lblRLdz = new JLabel( "RLDZ" );
        lblRLdz.setForeground(Color.WHITE);
        panelDoorSystem.add( lblRLdz, "cell 0 1,growx,gapy 5" );
        lblNdz = new JLabel("NDZ");
        lblNdz.setForeground(Color.WHITE);
        panelDoorSystem.add(lblNdz,"cell 0 1,growx,gapy 5");
        lblUrl = new JLabel( "URL" );
        lblUrl.setForeground(Color.WHITE);
        panelDoorSystem.add( lblUrl, "flowy,cell 1 1,growx,aligny top,gapy 5" );
        lblLrl = new JLabel( "LRL" );
        lblLrl.setForeground(Color.WHITE);
        panelDoorSystem.add( lblLrl, "cell 1 1,growx,gapy 5" );
        lblIns = new JLabel( "INS" );
        lblIns.setForeground(Color.WHITE);
        panelDoorSystem.add( lblIns, "flowy,cell 1 1,growx,aligny top,gapy 5" );
        lblInsup = new JLabel( "INSUP" );
        lblInsup.setForeground(Color.WHITE);
        panelDoorSystem.add( lblInsup, "cell 1 1,growx,gapy 5" );
        lblInsdown = new JLabel( "INSDOWN" );
        lblInsdown.setForeground(Color.WHITE);
        panelDoorSystem.add( lblInsdown, "cell 1 1,growx,gapy 5" );
        lblFan = new JLabel( bundle.getString( "Fan" ) );
        lblFan.setForeground(Color.WHITE);
        panelDoorSystem.add( lblFan, "flowy,cell 2 1,growx,aligny top,gapy 5" );
        lblLight = new JLabel( bundle.getString( "Light" ) );
        lblLight.setForeground(Color.WHITE);
        panelDoorSystem.add( lblLight, "flowy,cell 3 1,growx,aligny top,gapy 5" );
        JLabel lblCabinLoad = new JLabel( bundle.getString( "CabinLoad" ) );
        lblCabinLoad.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        lblCabinLoad.setForeground(Color.WHITE);
        panelDoorSystem.add( lblCabinLoad, "cell 4 0" );
        fmtLoad = new ValueTextField();
        fmtLoad.setColumns( 5 );
        fmtLoad.setHorizontalAlignment( SwingConstants.RIGHT );
        fmtLoad.setEmptyValue( 0 );
        fmtLoad.setEnabled(false);
        panelDoorSystem.add(fmtLoad, "flowy,cell 4 1,growx,aligny top,gapy 5");
        
        
        // front door
        JLabel labFrontDoor = new JLabel(bundle.getString( "FrontDoor" ));
        labFrontDoor.setForeground(Color.WHITE);
        labFrontDoor.setFont(FontFactory.FONT_12_BOLD);
        panelMain.add(labFrontDoor);
        StartUI.setStyle(layout, styles, labFrontDoor, "labFrontDoor");
        
        panelFrontDoor = new JPanel(new MigLayout( "ins 10", "[130!]20[100!]", "20[20!]20[150!]" )); 
        panelFrontDoor.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        panelFrontDoor.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelMain.add(panelFrontDoor);
        StartUI.setStyle(layout, styles, panelFrontDoor, "panelFrontDoor");
        
        titleFrontDoorStatus = new JLabel( bundle.getString( "titleFrontDoorStatus.text" ) );
        titleFrontDoorStatus.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        titleFrontDoorStatus.setForeground(Color.WHITE);
        panelFrontDoor.add( titleFrontDoorStatus, "flowx, cell 0 0, center" );
        lblFrontDoorStatusText = new JLabel( "" );
        lblFrontDoorStatusText.setForeground(Color.WHITE);
        panelFrontDoor.add( lblFrontDoorStatusText, "cell 0 0" );
        lblFrontDoorImg = new JLabel( "" );
        lblFrontDoorImg.setHorizontalAlignment( SwingConstants.CENTER );
        panelFrontDoor.add( lblFrontDoorImg, "cell 0 1, center" );
        
        JLabel lblIoStatus_1 = new JLabel( bundle.getString( "Door.IOStatus" ) );
        lblIoStatus_1.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        lblIoStatus_1.setForeground(Color.WHITE);
        panelFrontDoor.add( lblIoStatus_1, "cell 1 0" );
        lblFrontDol = new JLabel( "DOL" );
        lblFrontDol.setForeground(Color.WHITE);
        panelFrontDoor.add( lblFrontDol, "flowy,cell 1 1" );
        lblFrontDcl = new JLabel( "DCL" );
        lblFrontDcl.setForeground(Color.WHITE);
        panelFrontDoor.add( lblFrontDcl, "cell 1 1" );
        lblFrontSgs = new JLabel( "SGS" );
        lblFrontSgs.setForeground(Color.WHITE);
        panelFrontDoor.add( lblFrontSgs, "cell 1 1" );
        lblFrontEdp = new JLabel( "EDP" );
        lblFrontEdp.setForeground(Color.WHITE);
        panelFrontDoor.add( lblFrontEdp, "cell 1 1" );
        lblFrontDor = new JLabel( "DOR" );
        lblFrontDor.setForeground(Color.WHITE);
        panelFrontDoor.add( lblFrontDor, "cell 1 1" );
        lblFrontDcr = new JLabel( "DCR" );
        lblFrontDcr.setForeground(Color.WHITE);
        panelFrontDoor.add( lblFrontDcr, "cell 1 1" );
        lblFrontNdr = new JLabel( "NDR" );
        lblFrontNdr.setForeground(Color.WHITE);
        panelFrontDoor.add( lblFrontNdr, "cell 1 1" );
        
        // rear door
        JLabel labRearDoor = new JLabel(bundle.getString( "RearDoor" ));
        labRearDoor.setForeground(Color.WHITE);
        labRearDoor.setFont(FontFactory.FONT_12_BOLD);
        panelMain.add(labRearDoor);
        StartUI.setStyle(layout, styles, labRearDoor, "labRearDoor");
        
        panelRearDoor = new JPanel(new MigLayout( "ins 10", "[130!]20[100!]", "20[20!]20[150!]" )); 
        panelRearDoor.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        panelRearDoor.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelMain.add(panelRearDoor);
        StartUI.setStyle(layout, styles, panelRearDoor, "panelRearDoor");
        
        titleRearDoorStatus = new JLabel( bundle.getString( "titleFrontDoorStatus.text" ) );
        titleRearDoorStatus.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        titleRearDoorStatus.setForeground(Color.WHITE);
        panelRearDoor.add( titleRearDoorStatus, "flowx, cell 0 0, center" );
        lblRearDoorStatusText = new JLabel( "" );
        lblRearDoorStatusText.setForeground(Color.WHITE);
        panelRearDoor.add( lblRearDoorStatusText, "cell 0 0" );
        lblRearDoorImg = new JLabel( "" );
        lblRearDoorImg.setHorizontalAlignment( SwingConstants.CENTER );
        panelRearDoor.add( lblRearDoorImg, "flowx,cell 0 1, center " );
        
        JLabel lblIoStatus_2 = new JLabel( bundle.getString( "Door.IOStatus" ) );
        lblIoStatus_2.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        lblIoStatus_2.setForeground(Color.WHITE);
        panelRearDoor.add( lblIoStatus_2, "cell 1 0" );
        lblRearDol = new JLabel( "DOL" );
        lblRearDol.setForeground(Color.WHITE);
        panelRearDoor.add( lblRearDol, "flowy,cell 1 1" );
        lblRearDcl = new JLabel( "DCL" );
        lblRearDcl.setForeground(Color.WHITE);
        panelRearDoor.add( lblRearDcl, "cell 1 1" );
        lblRearSgs = new JLabel( "SGS" );
        lblRearSgs.setForeground(Color.WHITE);
        panelRearDoor.add( lblRearSgs, "cell 1 1" );
        lblRearEdp = new JLabel( "EDP" );
        lblRearEdp.setForeground(Color.WHITE);
        panelRearDoor.add( lblRearEdp, "cell 1 1" );
        lblRearDor = new JLabel( "DOR" );
        lblRearDor.setForeground(Color.WHITE);
        panelRearDoor.add( lblRearDor, "cell 1 1" );
        lblRearDcr = new JLabel( "DCR" );
        lblRearDcr.setForeground(Color.WHITE);
        panelRearDoor.add( lblRearDcr, "cell 1 1" );
        lblRearNdr = new JLabel( "NDR" );
        lblRearNdr.setForeground(Color.WHITE);
        panelRearDoor.add( lblRearNdr, "cell 1 1" );
        
        add( new JScrollPane( workspace ) {
            private static final long serialVersionUID = -5733767579374701576L;
            {
                setBorder( null );
                setOpaque( false );
                setPreferredSize(new Dimension(1000, 600));
                getViewport().setOpaque( false );
                getVerticalScrollBar().setUnitIncrement( 20 );
                this.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());

                setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
            }
        }, BorderLayout.CENTER );
        
        revalidate();
        repaint();
    }
    


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart() throws Exception {
    	mcsconfig = new Parser_McsConfig(connBean.getIp(), connBean.getPort());
        status = new Parser_Status(connBean.getIp(), connBean.getPort());

        MON_MGR.addEventListener(this, connBean.getIp(), connBean.getPort(),
                AgentMessage.MCS_CONFIG.getCode() |
                AgentMessage.STATUS.getCode() | 
                AgentMessage.ERROR.getCode());
        
        setHot();
    }


    @Override
    public void onResume () throws Exception {}


    @Override
    public void onPause () throws Exception {}


    @Override
    public void onStop () throws Exception {
        MON_MGR.removeEventListener(this);
    }

    @Override
    public void onDestroy () {}

    @Override
    public void onConnCreate() {
        setHot();
    }
    
    @Override
    public void onConnLost() {
        // TODO Auto-generated method stub
        
    }

    
    @Override
    public void onDataChanged(long timestamp, int msg) {
        setHot();
    }


    public void setHot() {
    	final HashMap<Object, OnOffStatus> map = new HashMap<>();
        final byte[] rawIO = status.getMcsIO();
        
        final CrossBar crossbar = new CrossBar(mcsconfig.getCrossbar());
        /*for(int i = 0; i < crossbar.configure.length; i++) {
        	System.out.print("["+crossbar.configure[i]+"]");
        }
        System.out.println("");*/
        
        final InputSourceA05[] updateList = {InputSourceA05.DCS_FUDZ,InputSourceA05.DCS_FLDZ,InputSourceA05.DCS_RUDZ,
        					  InputSourceA05.DCS_RLDZ,InputSourceA05.DCS_NDZ,InputSourceA05.DCS_URL,InputSourceA05.DCS_LRL,
        					  InputSourceA05.DCS_INS,InputSourceA05.DCS_INS_UP,InputSourceA05.DCS_INS_DOWN,
        					  
        					  InputSourceA05.DCS_FR,InputSourceA05.DCS_LR,InputSourceA05.DCS_DCR,InputSourceA05.DCS_NDR,
        					  InputSourceA05.DCS_RDCR,InputSourceA05.DCS_RNDR
        					};
        
        for(InputSourceA05 obj : updateList) {
    		boolean inputPresent = crossbar.isInputPresent(rawIO,(InputSourceA05)obj);
            map.put(obj, inputPresent ? OnOffStatus.ON : OnOffStatus.OFF);
        }
 
        
        
        final DoorStatusA05.DCSStatusBean dcsBean = new DoorStatusA05.DCSStatusBean();
        final DoorStatusA05.DoorBean      front   = new DoorStatusA05.DoorBean();
        final DoorStatusA05.DoorBean      rear    = new DoorStatusA05.DoorBean();
        final DoorStatusA05.LoadBean      load    = new DoorStatusA05.LoadBean();

        dcsBean.setFudz(map.get(InputSourceA05.DCS_FUDZ));
        dcsBean.setFldz(map.get(InputSourceA05.DCS_FLDZ));
        dcsBean.setRudz(map.get(InputSourceA05.DCS_RUDZ));
        dcsBean.setRldz(map.get(InputSourceA05.DCS_RLDZ));
        dcsBean.setNdz(map.get(InputSourceA05.DCS_NDZ));
        dcsBean.setUsl(map.get(InputSourceA05.DCS_URL));
        dcsBean.setLsl(map.get(InputSourceA05.DCS_LRL));
        dcsBean.setIns(map.get(InputSourceA05.DCS_INS));
        dcsBean.setInsup(map.get(InputSourceA05.DCS_INS_UP));
        dcsBean.setInsdown(map.get(InputSourceA05.DCS_INS_DOWN));
        // FAN and Light get state from DCS IO state, get invert.
        dcsBean.setFan( map.get(InputSourceA05.DCS_FR) == OnOffStatus.ON ? OnOffStatus.OFF : OnOffStatus.ON  );
        dcsBean.setLight( map.get(InputSourceA05.DCS_LR) == OnOffStatus.ON ? OnOffStatus.OFF : OnOffStatus.ON  );
        dcsBean.setDcr( map.get(InputSourceA05.DCS_DCR) );
        dcsBean.setNdr( map.get(InputSourceA05.DCS_NDR)  );
        
        front.setDoorStatus(status.getDoorStatus(true));
        front.setDcl( toOnOffStatus( status.getDCL( true ) ) );
        front.setDol( toOnOffStatus( status.getDOL( true ) ) );
        front.setDor( toOnOffStatus( status.getDOR( true ) ) );
        front.setSgs( toOnOffStatus( status.getSGS( true ) ) );
        front.setEdp( toOnOffStatus( status.getEDP( true ) ) );
        front.setDcr(map.get(InputSourceA05.DCS_DCR));
        front.setNdr(map.get(InputSourceA05.DCS_NDR));
        
        rear.setDoorStatus(status.getDoorStatus(false));
        rear.setDcl( toOnOffStatus( status.getDCL( false ) ) );
        rear.setDol( toOnOffStatus( status.getDOL( false ) ) );
        rear.setDor( toOnOffStatus( status.getDOR( false ) ) );
        rear.setSgs( toOnOffStatus( status.getSGS( false ) ) );
        rear.setEdp( toOnOffStatus( status.getEDP( false ) ) );
        rear.setDcr(map.get(InputSourceA05.DCS_RDCR));
        rear.setNdr(map.get(InputSourceA05.DCS_RNDR));
        
        load.setLoadValue(status.getCabinLoad());
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                setDCSStatusBean( dcsBean );
                setFrontDoorBean( front );
                setRearDoorBean(rear);
                setCabinLoadBean(load);
            }
        } );
    }
    
    public static class DCSStatusBean {
        private OnOffStatus fudz     = OnOffStatus.DISABLED;
        private OnOffStatus fldz     = OnOffStatus.DISABLED;
        private OnOffStatus rudz     = OnOffStatus.DISABLED;
        private OnOffStatus rldz     = OnOffStatus.DISABLED;
        private OnOffStatus ndz     = OnOffStatus.DISABLED;
        private OnOffStatus usl     = OnOffStatus.DISABLED;
        private OnOffStatus lsl     = OnOffStatus.DISABLED;
        private OnOffStatus ins     = OnOffStatus.DISABLED;
        private OnOffStatus insup   = OnOffStatus.DISABLED;
        private OnOffStatus insdown = OnOffStatus.DISABLED;
        private OnOffStatus fan     = OnOffStatus.DISABLED;
        private OnOffStatus light   = OnOffStatus.DISABLED;
        private OnOffStatus dcr   = OnOffStatus.DISABLED;
		private OnOffStatus ndr   = OnOffStatus.DISABLED;
		
        private Integer     loading;    // ignored?
        private Integer     cell1Load;
        private Integer     cell2Load;
        private Integer     cell3Load;
        private Integer     cell4Load;

        public DCSStatusBean () {
            super();
        }

        public DCSStatusBean ( OnOffStatus fudz, OnOffStatus fldz, OnOffStatus rudz, OnOffStatus rldz, OnOffStatus usl, OnOffStatus lsl,
        					   OnOffStatus ndz, OnOffStatus ins, OnOffStatus insup, OnOffStatus insdown, OnOffStatus fan, OnOffStatus light,
        					   OnOffStatus dcr, OnOffStatus ndr,Integer loading,Integer cell1Load, Integer cell2Load, Integer cell3Load, Integer cell4Load ) {
            super();
            this.fudz       = fudz;
            this.fldz       = fldz;
            this.rudz       = rudz;
            this.rldz       = rldz;
            this.ndz		= ndz;
            this.usl       = usl;
            this.lsl       = lsl;
            this.ins       = ins;
            this.insup     = insup;
            this.insdown   = insdown;
            this.fan       = fan;
            this.light     = light;
            this.dcr	   = dcr;
            this.ndr	   = ndr;
            this.loading   = loading;
            this.cell1Load = cell1Load;
            this.cell2Load = cell2Load;
            this.cell3Load = cell3Load;
            this.cell4Load = cell4Load;
        } 


        public OnOffStatus getDcr() {
			return dcr;
		}


		public void setDcr(OnOffStatus dcr) {
			this.dcr = dcr;
		}


		public OnOffStatus getNdr() {
			return ndr;
		}


		public void setNdr(OnOffStatus ndr) {
			this.ndr = ndr;
		}


		public OnOffStatus getFudz() {
			return fudz;
		}


		public void setFudz(OnOffStatus fudz) {
			this.fudz = fudz;
		}


		public OnOffStatus getFldz() {
			return fldz;
		}


		public void setFldz(OnOffStatus fldz) {
			this.fldz = fldz;
		}


		public OnOffStatus getRudz() {
			return rudz;
		}


		public void setRudz(OnOffStatus rudz) {
			this.rudz = rudz;
		}


		public OnOffStatus getRldz() {
			return rldz;
		}


		public void setRldz(OnOffStatus rldz) {
			this.rldz = rldz;
		}


		public OnOffStatus getNdz() {
			return ndz;
		}


		public void setNdz(OnOffStatus ndz) {
			this.ndz = ndz;
		}


		public OnOffStatus getUsl () {
            return usl;
        }


        public OnOffStatus getLsl () {
            return lsl;
        }


        public OnOffStatus getIns () {
            return ins;
        }


        public OnOffStatus getInsup () {
            return insup;
        }


        public OnOffStatus getInsdown () {
            return insdown;
        }


        public OnOffStatus getFan () {
            return fan;
        }


        public OnOffStatus getLight () {
            return light;
        }


        public Integer getLoading () {
            return loading;
        }


        public Integer getCell1Load () {
            return cell1Load;
        }


        public Integer getCell2Load () {
            return cell2Load;
        }


        public Integer getCell3Load () {
            return cell3Load;
        }


        public Integer getCell4Load () {
            return cell4Load;
        }


        public void setUsl ( OnOffStatus usl ) {
            this.usl = usl;
        }


        public void setLsl ( OnOffStatus lsl ) {
            this.lsl = lsl;
        }


        public void setIns ( OnOffStatus ins ) {
            this.ins = ins;
        }


        public void setInsup ( OnOffStatus insup ) {
            this.insup = insup;
        }


        public void setInsdown ( OnOffStatus insdown ) {
            this.insdown = insdown;
        }


        public void setFan ( OnOffStatus fan ) {
            this.fan = fan;
        }


        public void setLight ( OnOffStatus light ) {
            this.light = light;
        }


        @Deprecated
        public void setLoading ( Integer loading ) {
            this.loading = loading;
        }


        @Deprecated
        public void setCell1Load ( Integer cell1Load ) {
            this.cell1Load = cell1Load;
        }


        @Deprecated
        public void setCell2Load ( Integer cell2Load ) {
            this.cell2Load = cell2Load;
        }


        @Deprecated
        public void setCell3Load ( Integer cell3Load ) {
            this.cell3Load = cell3Load;
        }


        @Deprecated
        public void setCell4Load ( Integer cell4Load ) {
            this.cell4Load = cell4Load;
        }


        /*
         *  (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString () {
            StringBuilder builder = new StringBuilder();
            builder.append( "DCSStatusBean [fudz=" );
            builder.append( fudz );
            builder.append( ", fldz=" );
            builder.append( fldz );
            builder.append( ", rudz=" );
            builder.append( rudz );
            builder.append( ", rldz=" );
            builder.append( rldz );
            builder.append( ", usl=" );
            builder.append( usl );
            builder.append( ", lsl=" );
            builder.append( lsl );
            builder.append( ", ndz=" );
            builder.append( ndz );
            builder.append( ", ins=" );
            builder.append( ins );
            builder.append( ", insup=" );
            builder.append( insup );
            builder.append( ", insdown=" );
            builder.append( insdown );
            builder.append( ", fan=" );
            builder.append( fan );
            builder.append( ", light=" );
            builder.append( light );
            builder.append( ", loading=" );
            builder.append( loading );
            builder.append( ", cell1Load=" );
            builder.append( cell1Load );
            builder.append( ", cell2Load=" );
            builder.append( cell2Load );
            builder.append( ", cell3Load=" );
            builder.append( cell3Load );
            builder.append( ", cell4Load=" );
            builder.append( cell4Load );
            builder.append( "]" );
            return builder.toString();
        }
    }

    public static class DoorBean {
        private OnOffStatus       dol = OnOffStatus.DISABLED;
        private OnOffStatus       dcl = OnOffStatus.DISABLED;
        private OnOffStatus       sgs = OnOffStatus.DISABLED;
        private OnOffStatus       edp = OnOffStatus.DISABLED;
        private OnOffStatus       dor = OnOffStatus.DISABLED;
        private OnOffStatus       dcr = OnOffStatus.DISABLED;
		private OnOffStatus       ndr = OnOffStatus.DISABLED;
        private DoorAction doorStatus;



        public OnOffStatus getDcr() {
			return dcr;
		}


		public void setDcr(OnOffStatus dcr) {
			this.dcr = dcr;
		}


		public OnOffStatus getNdr() {
			return ndr;
		}


		public void setNdr(OnOffStatus ndr) {
			this.ndr = ndr;
		}
		
        public DoorAction getDoorStatus () {
            return doorStatus;
        }


        public void setDoorStatus ( DoorAction doorStatus ) {
            this.doorStatus = doorStatus;
        }


        public OnOffStatus getDol () {
            return dol;
        }


        public OnOffStatus getDcl () {
            return dcl;
        }


        public OnOffStatus getSgs () {
            return sgs;
        }


        public OnOffStatus getEdp () {
            return edp;
        }


        public OnOffStatus getDor () {
            return dor;
        }


        public void setDol ( OnOffStatus dol ) {
            this.dol = dol;
        }


        public void setDcl ( OnOffStatus dcl ) {
            this.dcl = dcl;
        }


        public void setSgs ( OnOffStatus sgs ) {
            this.sgs = sgs;
        }


        public void setEdp ( OnOffStatus edp ) {
            this.edp = edp;
        }


        public void setDor ( OnOffStatus dor ) {
            this.dor = dor;
        }


        @Override
        public String toString () {
            StringBuilder builder = new StringBuilder();
            builder.append( "DoorBean [dol=" );
            builder.append( dol );
            builder.append( ", dcl=" );
            builder.append( dcl );
            builder.append( ", sgs=" );
            builder.append( sgs );
            builder.append( ", edp=" );
            builder.append( edp );
            builder.append( ", dor=" );
            builder.append( dor );
            builder.append( "]" );
            return builder.toString();
        }
    }

    public static class LoadBean{
    	private int LoadValue;

		public int getLoadValue() {
			return LoadValue;
		}

		public void setLoadValue(int loadValue) {
			LoadValue = loadValue;
		}
    	
    }
    
    private OnOffStatus toOnOffStatus ( Boolean val ) {
        return val != null && val
               ? OnOffStatus.ON
               : OnOffStatus.OFF;
    }
}
