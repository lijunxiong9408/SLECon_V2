package slecon.inspect.iostatus;
import static logic.util.SiteManagement.MON_MGR;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.evlog.ErrorLog;
import logic.io.crossbar.CrossBar;
import logic.io.crossbar.InputPinD01;
import logic.io.crossbar.InputSourceD01;
import logic.io.crossbar.OutputPinD01;
import logic.io.crossbar.OutputSourceD01;
import logic.io.crossbar.ReferenceSourceNotFoundException;
import logic.util.SiteManagement;
import logic.util.Version;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SubtleSquareBorder;
import slecon.component.Workspace;
import slecon.home.PosButton;
import slecon.home.dashboard.VerticalSrcollBarUI;
import slecon.inspect.InspectPanel;
import slecon.interfaces.InspectView;
import slecon.interfaces.Page;
import slecon.setting.SetupPanel;
import comm.Parser_Deploy;
import comm.Parser_McsConfig;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.event.LiftDataChangedListener;


@InspectView(
    path      = "IOStatus",
    sortIndex = 0x303
)
public class IOStatusD01 extends JPanel implements Page, LiftDataChangedListener, IIOStatus {
    private static final long serialVersionUID = -552013171218549161L;
    static final ResourceBundle        TEXT                 = ToolBox.getResourceBundle( "inspect.IOStatus" );
    private static final ImageIcon     INPUT_ON_ICON        = ImageFactory.LIGHT_BRIGHT_GREEN.icon( 16, 16 );
    private static final ImageIcon     INPUT_OFF_ICON       = ImageFactory.LIGHT_DARK_GREEN.icon( 16, 16 );
    private static final ImageIcon     INPUT_DISABLED_ICON  = ImageFactory.LIGHT_BLACK.icon( 16, 16 );
    private static final ImageIcon     OUTPUT_ON_ICON       = ImageFactory.LIGHT_BRIGHT_ORANGE.icon( 16, 16 );
    private static final ImageIcon     OUTPUT_OFF_ICON      = ImageFactory.LIGHT_DARK_ORANGE.icon( 16, 16 );
    private static final ImageIcon     OUTPUT_DISABLED_ICON = ImageFactory.LIGHT_BLACK.icon( 16, 16 );
    private final LiftConnectionBean connBean;
    private JPanel		  	  panelNavigation;
    private JPanel		  	  panelMain;
    private JPanel		  	  panelLiftSelector;
    private MigLayout         layout;
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("panelLiftSelector", "30 20 250 60 c");
    	styles.put("lblIOStatus", "30 80 200 20 l");
    	styles.put("lblSystem", "95 125 100 20 l");
    	styles.put("sysPanel", "95 145 190 118 l");
    	styles.put("lblControl", "325 125 100 20 l");
    	styles.put("ctrlPanel", "325 145 350 118 l");
    	styles.put("lblInspect", "770 125 100 20 l");
    	styles.put("insectPanel", "770 145 142 118 l");
    	styles.put("lblDriver", "140 495 100 20 l");
    	styles.put("driverPanel", "140 515 300 100 l");
    	styles.put("lblSafety", "455 495 100 20 l");
    	styles.put("safePanel", "455 515 190 100 l");
    	styles.put("lblCabin", "680 495 100 20 l");
    	styles.put("cabinPanel", "680 515 350 100 l");
    }
    
    private static final BufferedImage bgImage;
    static {
        Image img      = ImageFactory.IOSTATUS_BG.image();
        int   imwidth  = img.getWidth( null );
        int   imheight = img.getHeight( null );
        bgImage = new BufferedImage( imwidth, imheight, BufferedImage.TYPE_4BYTE_ABGR );
        
        Graphics2D bg = ( Graphics2D )bgImage.getGraphics();
        bg.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        bg.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        bg.drawImage( img, 0, 0, imwidth, imheight, null );
        bg.dispose();
    }
    
    /* System */
    private JLabel                lblSystem;
    private InputSourceLabel  	  lblInEncf;
    private InputSourceLabel  	  lblInThm;
    private InputSourceLabel  	  lblDlb;
    private InputSourceLabel  	  lblUcmp;
    private OutputSourceLabel  	  lblOutCcf;

    /* Control */
    private JLabel                lblControl;
    private InputSourceLabel      lblInDbde;
    private InputSourceLabel      lblInDbdf;
    private InputSourceLabel      lblInBs1;
    private InputSourceLabel      lblInBs2;
    private InputSourceLabel      lblInLvcfb;
    private InputSourceLabel      lblInLvc1fb;
    private InputSourceLabel      lblInUdz;
    private InputSourceLabel      lblInLdz;
    
    private OutputSourceLabel      lblOutUr;
    private OutputSourceLabel      lblOutDr;
    private OutputSourceLabel      lblOutK1;
    private OutputSourceLabel      lblOutK2;
    private OutputSourceLabel      lblOutBr;
    private OutputSourceLabel      lblOutRbr;
    private OutputSourceLabel      lblOutLvcr;
    private OutputSourceLabel      lblOutLvc1r;

    /* Driver */
    private JLabel                 lblDriver;
    private OutputPinD01Label      lblOutDrvfwd;
    private OutputPinD01Label      lblOutDrvrev;
    private OutputPinD01Label      lblOutDrvepb;
    private InputSourceLabel  	   lblInDrvok;
    private InputSourceLabel  	   lblInDrvbm;

    /* Safety */
    private JLabel           	  lblSafety;
    private InputSourceLabel 	  lblInFDfc;
    private InputSourceLabel      lblInFDw;
    private InputSourceLabel  	  lblInHes;
    private InputSourceLabel 	  lblInCes;
    private InputSourceLabel 	  lblInRDfc;
    private InputSourceLabel 	  lblInRDw;
    private InputSourceLabel 	  lblInUsl;
    private InputSourceLabel 	  lblInLsl;

    /* Inspection */
    private JLabel           	  lblInspect;
    private InputSourceLabel 	  lblInIns;
    private InputSourceLabel	  lblInInsup;
    private InputSourceLabel 	  lblInInsdown;

    /* Cabin */
    private JLabel            	  lblCabin;
    private InputSourceLabel  	  lblInFUdz;
    private InputSourceLabel  	  lblInFLdz;
    private InputSourceLabel  	  lblInRUdz;
    private InputSourceLabel  	  lblInRLdz;
    private InputSourceLabel  	  lblInUrl;
    private InputSourceLabel  	  lblInLrl;
    private InputSourceLabel  	  lblInDUsl;
    private InputSourceLabel  	  lblInDLsl;
    private InputSourceLabel  	  lblInTsl;
    private InputSourceLabel  	  lblInLr;
    private InputSourceLabel  	  lblInFr;
    private InputSourceLabel  	  lblInAr;
    private InputSourceLabel  	  lblInTd1;
    private InputSourceLabel  	  lblInTd2;
    private InputSourceLabel  	  lblInTd3;
    
    private SystemBean        	  system;
    private ControlBean       	  control;
    private InspectBean       	  inspect;
    private DriverBean        	  driver;
    private SafetyBean        	  safety;
    private CabinBean         	  cabin;
    private CrossBar          	  crossbar;
    private VecToolTipManager tooltipManager;
    
    private String[] navigationText = {Dict.lookup("Inspect"), Dict.lookup("IOStatus")};
    ///////////////////// interface <Page> /////////////////////
    private Workspace                workspace;
    private Parser_McsConfig mcsconfig;
    private Parser_Status status;
    
    protected enum Type { INPUT, OUTPUT, }
    
    public IOStatusD01 ( LiftConnectionBean bean) {
    	this.connBean = bean;
    	setLayout(new BorderLayout());
        initGUI();
        setSystem( new SystemBean() );
        setControl( new ControlBean() );
        setInspect( new InspectBean() );
        setDriver( new DriverBean() );
        setSafety( new SafetyBean() );
        setCabin( new CabinBean() );
    }

    private void initGUI () {
        JPanel workspace = new JPanel( new MigLayout( "gap 0, w 1000!, gap 0", "[left]", "[30!]10[650]10[50]" ) );
        workspace.setBackground(StartUI.SUB_BACKGROUND_COLOR);
    /*------------------------------------------------------------------------------------------------------------*/   
        panelNavigation = new JPanel(new MigLayout( "nogrid, w 985!, h 30!, gap 0", "[left]", "[center]" ));
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
        layout = new MigLayout( "nogrid, w 1000!, h 639!, gap 0", "[left]", "[center]" );
        panelMain = new JPanel() {
        	private static final long serialVersionUID = 5921623163675275469L;
            {
                setOpaque( false );
            }
            protected void paintComponent ( Graphics g ) {
                Graphics2D g2d = ( Graphics2D )g;
                g2d.setPaint( new TexturePaint( bgImage, new Rectangle2D.Float( 0, 0, getWidth(), getHeight() ) ) );
                g2d.fillRect( 0, 0, getWidth(), getHeight() );
                super.paintComponent( g2d );
            }
        };
        panelMain.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelMain.setLayout(layout);
        workspace.add(panelMain,"cell 0 1");

        panelLiftSelector = new JPanel();
        panelLiftSelector.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelLiftSelector.add(StartUI.getLiftSelector());
        panelMain.add(panelLiftSelector);
        StartUI.setStyle(layout, styles, panelLiftSelector, "panelLiftSelector");
        
        JLabel lblIOStatus = new JLabel( TEXT.getString( "IOStatus.TEXT_LABEL_TITLE" ) );
        lblIOStatus.setForeground(Color.WHITE);
        lblIOStatus.setFont(FontFactory.FONT_12_BOLD);
        panelMain.add( lblIOStatus);
        StartUI.setStyle(layout, styles, lblIOStatus, "lblIOStatus");
        
        /** ****************************** System Panel ****************************** */
        lblSystem = new JLabel( TEXT.getString( "IOStatus.TEXT_LABEL_SYSTEM" ) );
        lblSystem.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        lblSystem.setForeground(Color.WHITE);
        panelMain.add( lblSystem);
        StartUI.setStyle(layout, styles, lblSystem, "lblSystem");
        
        JPanel sysPanel = new JPanel();
        sysPanel.setOpaque( false );
        sysPanel.setBackground( Color.green );
        panelMain.add( sysPanel);
        StartUI.setStyle(layout, styles, sysPanel, "sysPanel");
        
        sysPanel.setLayout( new MigLayout( "gap 5 3, ins 5", "[75!]5[75!]", "[]5[]" ) );
        lblInEncf = new InputSourceLabel( "ENCF", InputSourceD01.ENCF, Type.INPUT );
        sysPanel.add( lblInEncf, "cell 0 0" );
        lblInThm = new InputSourceLabel( "THM", InputSourceD01.THM, Type.INPUT );
        sysPanel.add( lblInThm, "cell 0 1" );
        lblOutCcf = new OutputSourceLabel( "CCF", OutputSourceD01.CCF );
        sysPanel.add( lblOutCcf, "cell 0 2" );
        lblDlb = new InputSourceLabel( "DLB", InputSourceD01.DLB, Type.OUTPUT );
        sysPanel.add( lblDlb, "cell 1 0" );
        lblUcmp = new InputSourceLabel( "UCMP", null, Type.INPUT);
        sysPanel.add( lblUcmp, "cell 1 1" );
        
        /** ****************************** Control Panel ****************************** */
        lblControl = new JLabel( TEXT.getString( "IOStatus.TEXT_LABEL_CONTROL" ) );
        lblControl.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        lblControl.setForeground(Color.WHITE);
        panelMain.add( lblControl);
        StartUI.setStyle(layout, styles, lblControl, "lblControl");
        
        JPanel ctrlPanel = new JPanel();
        ctrlPanel.setOpaque( false );
        panelMain.add( ctrlPanel);
        StartUI.setStyle(layout, styles, ctrlPanel, "ctrlPanel");
        
        ctrlPanel.setLayout( new MigLayout( "gap 5 3, ins 5", "[23%:n:23%][23%:n:23%][23%:n:23%][23%:n:23%]", "[][][][]" ) );
        lblInDbde = new InputSourceLabel( "DBDE", InputSourceD01.DBDE, Type.INPUT );
        ctrlPanel.add( lblInDbde, "cell 0 0" );
        lblInDbdf = new InputSourceLabel( "DBDF", InputSourceD01.DBDF, Type.INPUT );
        ctrlPanel.add( lblInDbdf, "cell 0 1" );
        lblInBs1 = new InputSourceLabel( "BS1", InputSourceD01.BS1, Type.INPUT );
        ctrlPanel.add( lblInBs1, "cell 0 2" );
        lblInBs2 = new InputSourceLabel( "BS2", InputSourceD01.BS2, Type.INPUT );
        ctrlPanel.add( lblInBs2, "cell 0 3" );
        lblInLvcfb = new InputSourceLabel( "LVCFB", InputSourceD01.LVCFB, Type.INPUT );
        ctrlPanel.add( lblInLvcfb, "cell 1 0" );
        lblInLvc1fb = new InputSourceLabel( "LVC1FB", InputSourceD01.LVC1FB, Type.INPUT );
        ctrlPanel.add( lblInLvc1fb, "cell 1 1" );
        lblInUdz = new InputSourceLabel( "UDZ", InputSourceD01.UDZ, Type.INPUT );
        ctrlPanel.add( lblInUdz, "cell 1 2" );
        lblInLdz = new InputSourceLabel( "LDZ", InputSourceD01.LDZ, Type.INPUT );
        ctrlPanel.add( lblInLdz, "cell 1 3" );
        
        lblOutUr = new OutputSourceLabel( "UR", OutputSourceD01.UR );
        ctrlPanel.add( lblOutUr, "cell 2 0" );
        lblOutDr = new OutputSourceLabel( "DR", OutputSourceD01.DR );
        ctrlPanel.add( lblOutDr, "cell 2 1" );
        lblOutK1 = new OutputSourceLabel( "K1", OutputSourceD01.RR2 );
        ctrlPanel.add( lblOutK1, "cell 2 2" );
        lblOutK2 = new OutputSourceLabel( "K2", OutputSourceD01.RR );
        ctrlPanel.add( lblOutK2, "cell 2 3" );
        lblOutBr = new OutputSourceLabel( "BR", OutputSourceD01.BR );
        ctrlPanel.add( lblOutBr, "cell 3 0" );
        lblOutRbr = new OutputSourceLabel( "RBR", OutputSourceD01.RBR );
        ctrlPanel.add( lblOutRbr, "cell 3 1" );
        lblOutLvcr = new OutputSourceLabel( "LVCR", OutputSourceD01.LVCR );
        ctrlPanel.add( lblOutLvcr, "cell 3 2" );
        lblOutLvc1r = new OutputSourceLabel( "LVC1R", OutputSourceD01.LVC1R );
        ctrlPanel.add( lblOutLvc1r, "cell 3 3" );
        
        /** ****************************** Inspect Panel ****************************** */
        lblInspect = new JLabel( TEXT.getString( "IOStatus.TEXT_LABEL_INSPECT" ) );
        lblInspect.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        lblInspect.setForeground(Color.WHITE);
        panelMain.add( lblInspect );
        StartUI.setStyle(layout, styles, lblInspect, "lblInspect");
        
        JPanel insectPanel = new JPanel();
        insectPanel.setOpaque( false );
        panelMain.add( insectPanel );
        StartUI.setStyle(layout, styles, insectPanel, "insectPanel");
        insectPanel.setLayout( new MigLayout( "gap 5 3, ins 5", "[grow]", "[][][]" ) );
        lblInIns = new InputSourceLabel( "INS", InputSourceD01.INS, Type.INPUT );
        insectPanel.add( lblInIns, "cell 0 0" );
        lblInInsup = new InputSourceLabel( "INSUP", InputSourceD01.INSUP, Type.INPUT );
        insectPanel.add( lblInInsup, "cell 0 1" );
        lblInInsdown = new InputSourceLabel( "INSDOWN", InputSourceD01.INSDOWN, Type.INPUT );
        insectPanel.add( lblInInsdown, "cell 0 2" );
        
        /** ****************************** Driver Panel ****************************** */
        lblDriver = new JLabel( TEXT.getString( "IOStatus.TEXT_LABEL_DRIVER" ) );
        lblDriver.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        lblDriver.setForeground(Color.WHITE);
        panelMain.add( lblDriver);
        StartUI.setStyle(layout, styles, lblDriver, "lblDriver");
        
        JPanel driverPanel = new JPanel();
        driverPanel.setOpaque( false );
        panelMain.add( driverPanel);
        StartUI.setStyle(layout, styles, driverPanel, "driverPanel");
        driverPanel.setLayout( new MigLayout( "ins 5", "[30%:n:30%][32%:n:32%]", "[][][]" ) );
       
        lblOutDrvfwd = new OutputPinD01Label( "DRVFWD", OutputPinD01.FWD );
        driverPanel.add( lblOutDrvfwd, "cell 0 0" );
        lblOutDrvrev = new OutputPinD01Label( "DRVREV", OutputPinD01.REV );
        driverPanel.add( lblOutDrvrev, "cell 0 1" );
        lblOutDrvepb = new OutputPinD01Label( "DRVEPB", OutputPinD01.EPB );
        driverPanel.add( lblOutDrvepb, "cell 0 2" );
        
        lblInDrvbm = new InputSourceLabel( "DRVBM", InputSourceD01.DRVBM, Type.INPUT );
        driverPanel.add( lblInDrvbm, "cell 1 0" );
        lblInDrvok = new InputSourceLabel( "DRVOK", InputSourceD01.DRVOK, Type.INPUT );
        driverPanel.add( lblInDrvok, "cell 1 1" );
        
        /** ****************************** Safety Panel ****************************** */
        lblSafety = new JLabel( TEXT.getString( "IOStatus.TEXT_LABEL_SAFETY" ) );
        lblSafety.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        lblSafety.setForeground(Color.WHITE);
        panelMain.add( lblSafety);
        StartUI.setStyle(layout, styles, lblSafety, "lblSafety");
        
        JPanel safePanel = new JPanel();
        safePanel.setOpaque( false );
        panelMain.add( safePanel);
        StartUI.setStyle(layout, styles, safePanel, "safePanel");
        safePanel.setLayout( new MigLayout( "gap 5 3, ins 5", "[75!]5[75!]", "[][][][]" ) );
        lblInFDfc = new InputSourceLabel( "FDFC", InputSourceD01.DFC, Type.INPUT );
        safePanel.add( lblInFDfc, "cell 0 0" );
        lblInFDw = new InputSourceLabel( "FDW", InputSourceD01.DW, Type.INPUT );
        safePanel.add( lblInFDw, "cell 0 1" );
        lblInHes = new InputSourceLabel( "HES", InputSourceD01.HES, Type.INPUT );
        safePanel.add( lblInHes, "cell 0 2" );
        lblInCes = new InputSourceLabel( "CES", InputSourceD01.CES, Type.INPUT );
        safePanel.add( lblInCes, "cell 0 3" );
        lblInRDfc = new InputSourceLabel( "RDFC", InputSourceD01.RDFC, Type.INPUT );
        safePanel.add( lblInRDfc, "cell 1 0" );
        lblInRDw = new InputSourceLabel( "RDW", InputSourceD01.RDW, Type.INPUT );
        safePanel.add( lblInRDw, "cell 1 1" );
        lblInUsl = new InputSourceLabel( "USL", InputSourceD01.USL, Type.INPUT );
        safePanel.add( lblInUsl, "cell 1 2" );
        lblInLsl = new InputSourceLabel( "LSL", InputSourceD01.LSL, Type.INPUT );
        safePanel.add( lblInLsl, "cell 1 3" );
        
        /** ****************************** Cabin Panel ****************************** */
        lblCabin = new JLabel( TEXT.getString( "IOStatus.TEXT_LABEL_CARBIN" ) );
        lblCabin.setFont( getFont().deriveFont( Font.BOLD, 12 ) );
        lblCabin.setForeground(Color.WHITE);
        panelMain.add( lblCabin );
        StartUI.setStyle(layout, styles, lblCabin, "lblCabin");
        
        JPanel cabinPanel = new JPanel();
        cabinPanel.setOpaque( false );
        panelMain.add( cabinPanel);
        StartUI.setStyle(layout, styles, cabinPanel, "cabinPanel");
        
        cabinPanel.setLayout( new MigLayout( "gap 5 3, ins 5", "[75!]5[75!]5[75]5[75]", "[][][][]" ) );
        lblInFUdz = new InputSourceLabel( "FUDZ", InputSourceD01.DCS_FUDZ, Type.INPUT );
        cabinPanel.add( lblInFUdz, "cell 0 0" );
        lblInFLdz = new InputSourceLabel( "FLDZ", InputSourceD01.DCS_FLDZ, Type.INPUT );
        cabinPanel.add( lblInFLdz, "cell 0 1" );
        lblInRUdz = new InputSourceLabel( "RUDZ", InputSourceD01.DCS_RUDZ, Type.INPUT );
        cabinPanel.add( lblInRUdz, "cell 0 2" );
        lblInRLdz = new InputSourceLabel( "RLDZ", InputSourceD01.DCS_RLDZ, Type.INPUT );
        cabinPanel.add( lblInRLdz, "cell 0 3" );
        lblInUrl = new InputSourceLabel( "URL", InputSourceD01.DCS_URL, Type.INPUT );
        cabinPanel.add( lblInUrl, "cell 1 0" );
        lblInLrl = new InputSourceLabel( "LRL", InputSourceD01.DCS_DRL, Type.INPUT );
        cabinPanel.add( lblInLrl, "cell 1 1" );
        lblInDUsl = new InputSourceLabel( "USL", InputSourceD01.DCS_USL, Type.INPUT );
        cabinPanel.add( lblInDUsl, "cell 1 2" );
        lblInDLsl = new InputSourceLabel( "LSL", InputSourceD01.DCS_LSL, Type.INPUT );
        cabinPanel.add( lblInDLsl, "cell 1 3" );
        lblInTsl = new InputSourceLabel( "TSL", InputSourceD01.DCS_TSL, Type.INPUT );
        cabinPanel.add( lblInTsl, "cell 2 0" );
        lblInLr = new InputSourceLabel( "LR", InputSourceD01.DCS_LR, Type.OUTPUT );
        cabinPanel.add( lblInLr, "cell 2 1" );
        lblInFr = new InputSourceLabel( "FR", InputSourceD01.DCS_FR, Type.OUTPUT );
        cabinPanel.add( lblInFr, "cell 2 2" );
        lblInAr = new InputSourceLabel( "AR", InputSourceD01.DCS_AR, Type.OUTPUT );
        cabinPanel.add( lblInAr, "cell 2 3" );
        lblInTd1 = new InputSourceLabel( "TD1", InputSourceD01.DCS_TD1, Type.OUTPUT );
        cabinPanel.add( lblInTd1, "cell 3 0" );
        lblInTd2 = new InputSourceLabel( "TD2", InputSourceD01.DCS_TD2, Type.OUTPUT );
        cabinPanel.add( lblInTd2, "cell 3 1" );
        lblInTd3 = new InputSourceLabel( "TD3", InputSourceD01.DCS_TD3, Type.OUTPUT );
        cabinPanel.add( lblInTd3, "cell 3 2" );
        
        
        PosButton btnConfigIO = new PosButton(TEXT.getString( "IOStatus.TEXT_BUTTON" ), ImageFactory.BUTTON_PAUSE.icon(87,30));
        btnConfigIO.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				StartUI.getTopMain().push(SetupPanel.build(connBean, slecon.setting.setup.io.InputsD01Setting.class));
			}
		} );
        workspace.add(btnConfigIO, "cell 0 2, right");
        
        add( new JScrollPane( workspace ) {
            private static final long serialVersionUID = -5733767579374701576L;
            {
                setBorder( null );
                setOpaque( false );
                getViewport().setOpaque( false );
                getVerticalScrollBar().setUnitIncrement( 20 );
                this.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
                setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
            }
        }, BorderLayout.CENTER );
        
        tooltipManager = new VecToolTipManager( this );
        tooltipManager.registerToolTip( lblInEncf );
        tooltipManager.registerToolTip( lblInThm );
        tooltipManager.registerToolTip( lblDlb );
        tooltipManager.registerToolTip( lblUcmp );
        tooltipManager.registerToolTip( lblOutCcf );

        tooltipManager.registerToolTip( lblInDbde );
        tooltipManager.registerToolTip( lblInDbdf );
        tooltipManager.registerToolTip( lblInBs1 );
        tooltipManager.registerToolTip( lblInBs2 );
        tooltipManager.registerToolTip( lblInLvcfb );
        tooltipManager.registerToolTip( lblInLvc1fb );
        tooltipManager.registerToolTip( lblInUdz );
        tooltipManager.registerToolTip( lblInLdz );
        
        tooltipManager.registerToolTip( lblOutUr );
        tooltipManager.registerToolTip( lblOutDr );
        tooltipManager.registerToolTip( lblOutK1);
        tooltipManager.registerToolTip( lblOutK2 );
        tooltipManager.registerToolTip( lblOutBr );
        tooltipManager.registerToolTip( lblOutRbr );
        tooltipManager.registerToolTip( lblOutLvcr );
        tooltipManager.registerToolTip( lblOutLvc1r );
        
        tooltipManager.registerToolTip( lblOutDrvfwd );
        tooltipManager.registerToolTip( lblOutDrvrev );
        tooltipManager.registerToolTip( lblOutDrvepb );
        tooltipManager.registerToolTip( lblInDrvok );
        tooltipManager.registerToolTip( lblInDrvbm );
        
        tooltipManager.registerToolTip( lblInFDfc );
        tooltipManager.registerToolTip( lblInFDw );
        tooltipManager.registerToolTip( lblInHes );
        tooltipManager.registerToolTip( lblInCes );
        tooltipManager.registerToolTip( lblInRDfc );
        tooltipManager.registerToolTip( lblInRDw );
        tooltipManager.registerToolTip( lblInUsl );
        tooltipManager.registerToolTip( lblInLsl );
        
        tooltipManager.registerToolTip( lblInIns );
        tooltipManager.registerToolTip( lblInInsup );
        tooltipManager.registerToolTip( lblInInsdown );
        
        tooltipManager.registerToolTip( lblInFUdz );
        tooltipManager.registerToolTip( lblInFLdz );
        tooltipManager.registerToolTip( lblInRUdz );
        tooltipManager.registerToolTip( lblInRLdz );
        tooltipManager.registerToolTip( lblInUrl );
        tooltipManager.registerToolTip( lblInLrl );
        tooltipManager.registerToolTip( lblInDUsl );
        tooltipManager.registerToolTip( lblInDLsl );
        tooltipManager.registerToolTip( lblInTsl );
        tooltipManager.registerToolTip( lblInLr );
        tooltipManager.registerToolTip( lblInFr );
        tooltipManager.registerToolTip( lblInAr );
        tooltipManager.registerToolTip( lblInTd1 );
        tooltipManager.registerToolTip( lblInTd2 );
        tooltipManager.registerToolTip( lblInTd3 );
    }
    
    private ImageIcon statusToIcon ( Type io, OnOffStatus status ) {
        if ( status == null )
            return null;
        switch ( status ) {
        case ON :
            return io == Type.INPUT ? INPUT_ON_ICON : OUTPUT_ON_ICON;
        case OFF :
            return io == Type.INPUT ? INPUT_OFF_ICON : OUTPUT_OFF_ICON;
        case DISABLED :
            return io == Type.INPUT ? INPUT_DISABLED_ICON : OUTPUT_DISABLED_ICON;
        default :
            return null;
        }
    }


    private void updateSystem () {
    	lblInEncf.setStatus( system.getEncf() );
    	lblInThm.setStatus( system.getThm() );
    	lblDlb.setStatus(system.getDlb() );
    	lblUcmp.setStatus( system.getUcmp() );
    	lblOutCcf.setStatus( system.getCcf() );
    }

    private void updateControl () {
        lblInDbde.setStatus( control.getDbde() );
        lblInDbdf.setStatus( control.getDbdf() );
        lblInBs1.setStatus( control.getBs1() );
        lblInBs2.setStatus( control.getBs2() );
        lblInLvcfb.setStatus( control.getLvcfb() );
        lblInLvc1fb.setStatus( control.getLvc1fb() );
        lblInUdz.setStatus( control.getUdz() );
        lblInLdz.setStatus( control.getLdz() );
        
        lblOutUr.setStatus( control.getUr() );
        lblOutDr.setStatus( control.getDr() );
        lblOutK1.setStatus( control.getK1() );
        lblOutK2.setStatus( control.getK2() );
        lblOutBr.setStatus( control.getBr() );
        lblOutRbr.setStatus( control.getRbr() );
        lblOutLvcr.setStatus( control.getLvcr() );
        lblOutLvc1r.setStatus( control.getLvcr1() );
    }


    private void updateInspect () {
        lblInIns.setStatus( inspect.getIns() );
        lblInInsup.setStatus( inspect.getInsup() );
        lblInInsdown.setStatus( inspect.getInsdown() );
    }


    private void updateDriver () {
        lblOutDrvfwd.setStatus( driver.getDrvfwd() );
        lblOutDrvrev.setStatus( driver.getDrvrev() );
        lblOutDrvepb.setStatus( driver.getDrvepb() );
        lblInDrvok.setStatus( driver.getDrvok() );
        lblInDrvbm.setStatus( driver.getDrvbm() );
    }


    private void updateSafety () {
        lblInFDfc.setStatus( safety.getDfc() );
        lblInFDw.setStatus( safety.getDw() );
        lblInHes.setStatus( safety.getHes() );
        lblInCes.setStatus( safety.getCes() );
        lblInRDfc.setStatus( safety.getRdfc());
        lblInRDw.setStatus( safety.getRdw() );
        lblInUsl.setStatus( safety.getUsl() );
        lblInLsl.setStatus( safety.getLsl() );
    }


    private void updateCabin () {
    	lblInFUdz.setStatus( cabin.getUdz() );
        lblInFLdz.setStatus( cabin.getLdz() );
        lblInRUdz.setStatus( cabin.getRudz() );
        lblInRLdz.setStatus( cabin.getRldz() );
        lblInUrl.setStatus( cabin.getUrl() );
        lblInLrl.setStatus( cabin.getLrl() );
        lblInDUsl.setStatus( cabin.getUsl() );
        lblInDLsl.setStatus( cabin.getLsl() );
        lblInTsl.setStatus( cabin.getTsl() );
        lblInLr.setStatus( cabin.getLr() );
        lblInFr.setStatus( cabin.getFr() );
        lblInAr.setStatus( cabin.getAr() );
        lblInTd1.setStatus( cabin.getTd1() );
        lblInTd2.setStatus( cabin.getTd2() );
        lblInTd3.setStatus( cabin.getTd3() );
    }
    
    @Override
	public SystemBean getSystem() {
		// TODO Auto-generated method stub
		return system;
	}

	@Override
	public ControlBean getControl() {
		// TODO Auto-generated method stub
		return control;
	}

	@Override
	public InspectBean getInspect() {
		// TODO Auto-generated method stub
		return inspect;
	}

	@Override
	public DriverBean getDriver() {
		// TODO Auto-generated method stub
		return driver;
	}

	@Override
	public SafetyBean getSafety() {
		// TODO Auto-generated method stub
		return safety;
	}

	@Override
	public CabinBean getCabin() {
		// TODO Auto-generated method stub
		return cabin;
	}

	@Override
	public void setSystem(SystemBean system) {
		// TODO Auto-generated method stub
		this.system = system;
        updateSystem();
	}

	@Override
	public void setControl(ControlBean control) {
		// TODO Auto-generated method stub
		this.control = control;
        updateControl();
	}

	@Override
	public void setInspect(InspectBean inspect) {
		// TODO Auto-generated method stub
		this.inspect = inspect;
        updateInspect();
	}

	@Override
	public void setDriver(DriverBean driver) {
		// TODO Auto-generated method stub
		this.driver = driver;
        updateDriver();
	}

	@Override
	public void setSafety(SafetyBean safety) {
		// TODO Auto-generated method stub
		this.safety = safety;
        updateSafety();
	}

	@Override
	public void setCabin(CabinBean cabin) {
		// TODO Auto-generated method stub
		this.cabin = cabin;
        updateCabin();
	}

	@Override
	public void setToolTip(CrossBar crossbar) {
		// TODO Auto-generated method stub
		this.crossbar = ( CrossBar )crossbar;
	}

	@Override
	public Workspace getWorkspace() {
		// TODO Auto-generated method stub
		return workspace;
	}
	
    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    	this.workspace = workspace;
    }


    @Override
    public void onStart() throws Exception {
    	mcsconfig = new Parser_McsConfig( connBean.getIp(), connBean.getPort() );
        status = new Parser_Status( connBean.getIp(), connBean.getPort() );

        MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                AgentMessage.MCS_CONFIG.getCode() |
                AgentMessage.STATUS.getCode() | 
                AgentMessage.ERROR.getCode());

        setHot();
    }


    @Override
    public void onResume () throws Exception {
    	MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                AgentMessage.MCS_CONFIG.getCode() |
                AgentMessage.STATUS.getCode() | 
                AgentMessage.ERROR.getCode());
    }


    @Override
    public void onPause () throws Exception {
    	MON_MGR.removeEventListener( this );
    }


    @Override
    public void onStop () throws Exception {
    	tooltipManager.hideToolTipPopup();
        removeAll();
        MON_MGR.removeEventListener( this );
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

    private static final Object[] updateList = { InputSourceD01.ENCF, InputSourceD01.THM, InputSourceD01.DLB, 
    		InputSourceD01.DBDE, InputSourceD01.DBDF, InputSourceD01.BS1,InputSourceD01.BS2, InputSourceD01.LVCFB,
    		InputSourceD01.LVC1FB, InputSourceD01.UDZ, InputSourceD01.LDZ, InputSourceD01.DRVBM,
    		InputSourceD01.DRVOK, InputSourceD01.DRVENF,OutputPinD01.K1, OutputPinD01.K2, OutputPinD01.BR, 
    		OutputPinD01.EPB, InputSourceD01.DFC, InputSourceD01.DW,	InputSourceD01.HES, InputSourceD01.CES,
    		InputSourceD01.RDFC, InputSourceD01.RDW, InputSourceD01.USL, InputSourceD01.LSL, 
    		InputSourceD01.DCS_USL, InputSourceD01.DCS_LSL, InputSourceD01.INS, 
    		InputSourceD01.INSUP, InputSourceD01.INSDOWN, InputSourceD01.DCS_FUDZ, InputSourceD01.DCS_FLDZ, InputSourceD01.DCS_RUDZ,
    		InputSourceD01.DCS_RUDZ, InputSourceD01.DCS_RLDZ, InputSourceD01.DCS_URL, InputSourceD01.DCS_DRL,
    		InputSourceD01.DCS_TSL, InputSourceD01.DCS_LR, InputSourceD01.DCS_FR, InputSourceD01.DCS_AR,
    		InputSourceD01.DCS_TD1, InputSourceD01.DCS_TD2, InputSourceD01.DCS_TD3,
    		
    		OutputSourceD01.CCF, OutputSourceD01.UR, OutputSourceD01.DR, OutputSourceD01.RR,OutputSourceD01.RR2, OutputSourceD01.LVCR,
    		OutputSourceD01.LVC1R, OutputSourceD01.RBR, OutputSourceD01.DRVEN, OutputSourceD01.DRVFWD, OutputSourceD01.DRVREV,
    		OutputSourceD01.DRVMLTSPD0, OutputSourceD01.DRVMLTSPD1, OutputSourceD01.DRVMLTSPD2, 
    };
    
    public void setHot () {
        final HashMap<Object, OnOffStatus> map = new HashMap<>();
        final byte[] rawIO = status.getMcsIO();
       
        final CrossBar crossbar = new CrossBar( mcsconfig.getCrossbar() );

        setToolTip( crossbar );
        for ( Object obj : updateList ) {
            if ( obj instanceof InputSourceD01 ) {
                boolean inputPresent = crossbar.isInputPresent( rawIO, ( InputSourceD01 )obj );
                map.put( obj, inputPresent ? OnOffStatus.ON : OnOffStatus.OFF );
            } else if ( obj instanceof OutputSourceD01 ) {
                boolean outputPresent;
                try {
                    outputPresent = crossbar.isOutputPresent( rawIO, ( OutputSourceD01 )obj );
                    map.put( obj, outputPresent ? OnOffStatus.ON : OnOffStatus.OFF );
                } catch ( ReferenceSourceNotFoundException ex ) {
                    map.put( obj, OnOffStatus.DISABLED );
                }
            }else if(obj instanceof InputPinD01) {
            	 boolean inputPresent = crossbar.isInputPresent( rawIO, ( InputPinD01 )obj );
                 map.put( obj, inputPresent ? OnOffStatus.ON : OnOffStatus.OFF );
            }else if(obj instanceof OutputPinD01) {
           	 	boolean outputPresent = crossbar.isOutputPresent( rawIO, ( OutputPinD01 )obj );
                map.put( obj, outputPresent ? OnOffStatus.ON : OnOffStatus.OFF );
           }
        }
        final SystemBean sb = new SystemBean();
        final ControlBean ctrlb = new ControlBean();
        final DriverBean db = new DriverBean();
        final SafetyBean stb = new SafetyBean();
        final CabinBean cbb = new CabinBean();
        final InspectBean ib = new InspectBean();
        
        sb.setEncf( map.get( InputSourceD01.ENCF ) );
        sb.setThm( map.get( InputSourceD01.THM ) );
        sb.setDlb( map.get( InputSourceD01.DLB ) );
        sb.setUcmp( map.get( null ) );
        sb.setCcf( map.get( OutputSourceD01.CCF ) );
        
        ctrlb.setDbde( map.get( InputSourceD01.DBDE ) );
        ctrlb.setDbdf( map.get( InputSourceD01.DBDF ) );
        ctrlb.setBs1( map.get( InputSourceD01.BS1 ) );
        ctrlb.setBs2( map.get( InputSourceD01.BS2 ) );
        ctrlb.setLvcfb( map.get( InputSourceD01.LVCFB ) );
        ctrlb.setLvc1fb( map.get( InputSourceD01.LVC1FB ) );
        ctrlb.setUdz( map.get( InputSourceD01.UDZ ) );
        ctrlb.setLdz( map.get( InputSourceD01.LDZ ) );
        
        ctrlb.setUr( map.get( OutputSourceD01.UR ) );
        ctrlb.setDr( map.get( OutputSourceD01.DR ) );
        ctrlb.setK1( map.get( OutputPinD01.K1 ) );
        ctrlb.setK2( map.get( OutputPinD01.K2 ) );
        ctrlb.setBr( map.get( OutputPinD01.BR ) );
        ctrlb.setRbr(map.get( OutputSourceD01.RBR ) );
        ctrlb.setLvcr( map.get( OutputSourceD01.LVCR ) );
        ctrlb.setLvcr1( map.get( OutputSourceD01.LVC1R ) );
        
        db.setDrvfwd( map.get( OutputSourceD01.DRVFWD ) );
        db.setDrvrev( map.get( OutputSourceD01.DRVREV ) );
        db.setDrvbm( map.get( InputSourceD01.DRVBM ) );
        db.setDrvok( map.get( InputSourceD01.DRVOK ) );
        db.setDrvepb( map.get( OutputPinD01.EPB ) );
        
        stb.setDfc( map.get( InputSourceD01.DFC ) );
        stb.setDw( map.get( InputSourceD01.DW ) );
        stb.setHes( map.get( InputSourceD01.HES ) );
        stb.setCes( map.get( InputSourceD01.CES ) );
        stb.setRdfc( map.get( InputSourceD01.RDFC ) );
        stb.setRdw( map.get( InputSourceD01.RDW ) );
        stb.setUsl( map.get( InputSourceD01.USL ) );
        stb.setLsl( map.get( InputSourceD01.LSL ) );
        
        cbb.setUdz( map.get( InputSourceD01.DCS_FUDZ ) );
        cbb.setLdz( map.get( InputSourceD01.DCS_FLDZ ) );
        cbb.setRudz( map.get( InputSourceD01.DCS_RUDZ ) );
        cbb.setRldz( map.get( InputSourceD01.DCS_RLDZ ) );
        cbb.setUrl( map.get( InputSourceD01.DCS_URL ) );
        cbb.setLrl( map.get( InputSourceD01.DCS_DRL ) );
        cbb.setUsl( map.get( InputSourceD01.DCS_USL ) );
        cbb.setLsl( map.get( InputSourceD01.DCS_LSL ) );
        cbb.setTsl( map.get( InputSourceD01.DCS_TSL ) );
        cbb.setLr( map.get( InputSourceD01.DCS_LR) == OnOffStatus.ON ? OnOffStatus.OFF : OnOffStatus.ON );
        cbb.setFr( map.get( InputSourceD01.DCS_FR) == OnOffStatus.ON ? OnOffStatus.OFF : OnOffStatus.ON );
        cbb.setAr( map.get( InputSourceD01.DCS_AR ) );
        cbb.setTd1( map.get( InputSourceD01.DCS_TD1 ) );
        cbb.setTd2( map.get( InputSourceD01.DCS_TD2 ) );
        cbb.setTd3( map.get( InputSourceD01.DCS_TD3 ) );
        
        ib.setIns( map.get( InputSourceD01.INS ) );
        ib.setInsup( map.get( InputSourceD01.INSUP ) );
        ib.setInsdown( map.get( InputSourceD01.INSDOWN ) );
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                setSystem( sb );
                setControl( ctrlb );
                setDriver( db );
                setSafety( stb );
                setCabin( cbb );
                setInspect( ib );
            }
        } );
    }
    
    @SuppressWarnings( "serial" )
    class InputSourceLabel extends JLabel {
        private InputSourceD01 inputSource;
        private OnOffStatus status;
        private Type io;

        @Override
        public void setForeground(Color fg) {
        	// TODO Auto-generated method stub
        	super.setForeground(Color.WHITE);
        }

        public InputSourceLabel ( String caption, InputSourceD01 is, Type io ) {
            super( caption );
            this.inputSource = is;
            this.io = io;
        }

        public void setStatus ( OnOffStatus onoff ) {
            this.status = onoff;
            update();
        }

        public void update () {
            setIcon( statusToIcon( this.io, status ) );
        }

        public String getToolTipText () {
            return String.format( io == Type.INPUT ? TEXT.getString( "IOStatus.TEXT_TOOLTIP_INPUT" )
            					  : TEXT.getString( "IOStatus.TEXT_TOOLTIP_OUTPUT" ),
                                  String.format( "%s - %s", inputSource, inputSource.fullname ),    // input name
                                  crossbar == null
                                  ? TEXT.getString( "IOStatus.TEXT_UNASSIGNED" )
                                  : crossbar.getInputPinD01(inputSource),
                                  crossbar == null
                                  ? TEXT.getString( "IOStatus.TEXT_UNASSIGNED_PIN" )
                                  : crossbar.isInverted( inputSource )
                                    ? TEXT.getString( "IOStatus.TEXT_YES" )
                                    : TEXT.getString( "IOStatus.TEXT_NO" ),    // inverted
                                    status == null
                                    ? OnOffStatus.DISABLED
                                    : status );                                  // status
        }
    }
    
    @SuppressWarnings( "serial" )
    class OutputSourceLabel extends JLabel {
        private OutputSourceD01 outputSource;
        private OnOffStatus  status;

        @Override
        public void setForeground(Color fg) {
        	// TODO Auto-generated method stub
        	super.setForeground(Color.WHITE);
        }


        public OutputSourceLabel ( String caption, OutputSourceD01 os ) {
            super( caption );
            this.outputSource = os;
        }


        public void setStatus ( OnOffStatus onoff ) {
            this.status = onoff;
            setIcon( statusToIcon( Type.OUTPUT, status ) );
        }


        public String getToolTipText () {
            OutputPinD01 pin = null;
            for ( OutputPinD01 p : OutputPinD01.values() ) {
                if ( crossbar!=null && outputSource == crossbar.getOutputSource( p ) )
                    pin = p;
            }
            return String.format( TEXT.getString( "IOStatus.TEXT_TOOLTIP_OUTPUT" ), outputSource,    // output name
                                  pin == null
                                  ? TEXT.getString( "IOStatus.TEXT_UNASSIGNED" )
                                  : pin,                                                               // output pin
                                  pin == null
                                  ? TEXT.getString( "IOStatus.TEXT_UNASSIGNED_PIN" )
                                  : crossbar.isInverted( pin )
                                    ? TEXT.getString( "IOStatus.TEXT_YES" )
                                    : TEXT.getString( "IOStatus.TEXT_NO" ),                          // inverted
                                    status == null
                                    ? OnOffStatus.DISABLED
                                    : status );
        }
    }
    
    @SuppressWarnings( "serial" )
    class OutputPinD01Label extends JLabel {
        private OutputPinD01 outputPin;
        private OnOffStatus  status;

        @Override
        public void setForeground(Color fg) {
        	// TODO Auto-generated method stub
        	super.setForeground(Color.WHITE);
        }


        public OutputPinD01Label ( String caption, OutputPinD01 pin ) {
            super( caption );
            this.outputPin = pin;
        }


        public void setStatus ( OnOffStatus onoff ) {
            this.status = onoff;
            setIcon( statusToIcon( Type.OUTPUT, status ) );
        }


        public String getToolTipText () {
            return String.format( TEXT.getString( "IOStatus.TEXT_TOOLTIP_OUTPUT" ), outputPin,    // output name
            					  outputPin == null
                                  ? TEXT.getString( "IOStatus.TEXT_UNASSIGNED" )
                                  : outputPin,                                                               // output pin
                                  outputPin == null
                                  ? TEXT.getString( "IOStatus.TEXT_UNASSIGNED_PIN" )
                                  : crossbar.isInverted( outputPin )
                                    ? TEXT.getString( "IOStatus.TEXT_YES" )
                                    : TEXT.getString( "IOStatus.TEXT_NO" ),                          // inverted
                                    status == null
                                    ? OnOffStatus.DISABLED
                                    : status );
        }
    }
}
