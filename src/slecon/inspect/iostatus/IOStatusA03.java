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
import logic.io.crossbar.InputSourceA03;
import logic.io.crossbar.OutputPinA03;
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
public class IOStatusA03 extends JPanel implements Page, LiftDataChangedListener, IIOStatus {
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
    	styles.put("cabinPanel", "680 515 190 100 l");
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
    private JLabel            lblSystem;
    private InputSourceLabel  lblInAdo;
    private InputSourceLabel  lblInV24mon;
    private InputSourceLabel  lblInEncf;
    private InputSourceLabel  lblInThm;
    private OutputSourceLabel lblOutFr;

    /* Control */
    private JLabel            lblControl;
    private InputSourceLabel  lblInEnable;
    private InputSourceLabel  lblInBs;
    private OutputSourceLabel lblOutUr;
    private OutputSourceLabel lblOutBr;
    private InputSourceLabel  lblInDbde;
    private InputSourceLabel  lblInLvcfb;
    private OutputSourceLabel lblOutDr;
    private OutputSourceLabel lblOutLvcr;
    private InputSourceLabel  lblInDbdf;
    private OutputSourceLabel lblOutRr;

    /* Driver */
    private JLabel            lblDriver;
    private OutputSourceLabel lblOutDrven;
    private InputSourceLabel  lblInDrvenf;
    private OutputSourceLabel lblOutDrvfwd;
    private InputSourceLabel  lblInDrvbm;
    private OutputSourceLabel lblOutDrvrev;
    private InputSourceLabel  lblInDrvok;
    private OutputSourceLabel lblOutDrvmltsp0;
    private OutputSourceLabel lblOutDrvmltsp1;
    private OutputSourceLabel lblOutDrvmltsp2;

    /* Safety */
    private JLabel           lblSafety;
    private InputSourceLabel lblInDfc;
    private InputSourceLabel lblInDw;
    private InputSourceLabel lblInHes;
    private InputSourceLabel lblInCes;

    /* Inspection */
    private JLabel           lblInspect;
    private InputSourceLabel lblInIns;
    private InputSourceLabel lblInInsup;
    private InputSourceLabel lblInInsdown;

    /* Cabin */
    private JLabel            lblCabin;
    private InputSourceLabel  lblInUdz;
    private InputSourceLabel  lblInLdz;
    private InputSourceLabel  lblInUsl;
    private InputSourceLabel  lblInLsl;
    private OutputSourceLabel lblOutLr;
    
    private SystemBean        system;
    private ControlBean       control;
    private InspectBean       inspect;
    private DriverBean        driver;
    private SafetyBean        safety;
    private CabinBean         cabin;
    private CrossBar          crossbar;
    private VecToolTipManager tooltipManager;
    
    private String[] navigationText = {Dict.lookup("Inspect"), Dict.lookup("IOStatus")};
    ///////////////////// interface <Page> /////////////////////
    private Workspace                workspace;
    private Parser_McsConfig mcsconfig;
    private Parser_Status status;
    
    protected enum Type { INPUT, OUTPUT, }
    
    public IOStatusA03 ( LiftConnectionBean bean) {
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
        
        sysPanel.setLayout( new MigLayout( "gap 5 3, ins 5", "[75!]5[75!]", "[]5[]5[]" ) );
        lblInAdo = new InputSourceLabel( "ADO", InputSourceA03.ADO );
        sysPanel.add( lblInAdo, "cell 0 0" );
        lblInV24mon = new InputSourceLabel( "V24MON", InputSourceA03.V24MON );
        sysPanel.add( lblInV24mon, "cell 0 1" );
        lblInEncf = new InputSourceLabel( "ENCF", InputSourceA03.ENCF );
        sysPanel.add( lblInEncf, "cell 0 2" );
        lblInThm = new InputSourceLabel( "THM", InputSourceA03.THM );
        sysPanel.add( lblInThm, "cell 1 0" );
        lblOutFr = new OutputSourceLabel( "CCF", OutputSourceD01.CCF );
        sysPanel.add( lblOutFr, "cell 1 1" );
        
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
        
        ctrlPanel.setLayout( new MigLayout( "gap 5 3, ins 5", "[23%:n:23%][23%:n:23%][23%:n:23%][23%:n:23%]", "[][][]" ) );
        lblInEnable = new InputSourceLabel( "Enable", InputSourceA03.ENABLE );
        ctrlPanel.add( lblInEnable, "cell 0 0" );
        lblInDbde = new InputSourceLabel( "DBDE", InputSourceA03.DBDE );
        ctrlPanel.add( lblInDbde, "cell 0 1" );
        lblInDbdf = new InputSourceLabel( "DBDF", InputSourceA03.DBDF );
        ctrlPanel.add( lblInDbdf, "cell 0 2" );
        lblInBs = new InputSourceLabel( "BS", InputSourceA03.BS );
        ctrlPanel.add( lblInBs, "cell 1 0" );
        lblInLvcfb = new InputSourceLabel( "LVCFB", InputSourceA03.LVCFB );
        ctrlPanel.add( lblInLvcfb, "cell 1 1" );
        lblOutUr = new OutputSourceLabel( "UR", OutputSourceD01.UR );
        ctrlPanel.add( lblOutUr, "cell 2 0" );
        lblOutDr = new OutputSourceLabel( "DR", OutputSourceD01.DR );
        ctrlPanel.add( lblOutDr, "cell 2 1" );
        lblOutBr = new OutputSourceLabel( "BR", OutputSourceD01.BR );
        ctrlPanel.add( lblOutBr, "cell 3 0" );
        lblOutLvcr = new OutputSourceLabel( "LVCR", OutputSourceD01.LVCR );
        ctrlPanel.add( lblOutLvcr, "cell 3 1" );
        lblOutRr = new OutputSourceLabel( "RR", OutputSourceD01.RR );
        ctrlPanel.add( lblOutRr, "cell 3 2" );
        
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
        lblInIns = new InputSourceLabel( "INS", InputSourceA03.INS );
        insectPanel.add( lblInIns, "cell 0 0" );
        lblInInsup = new InputSourceLabel( "INSUP", InputSourceA03.INSUP );
        insectPanel.add( lblInInsup, "cell 0 1" );
        lblInInsdown = new InputSourceLabel( "INSDOWN", InputSourceA03.INSDOWN );
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
        driverPanel.setLayout( new MigLayout( "ins 5", "[30%:n:30%][32%:n:32%][32%:n:32%]", "[][][]" ) );
       
        lblOutDrven = new OutputSourceLabel( "DRVEN", OutputSourceD01.DRVEN );
        driverPanel.add( lblOutDrven, "cell 0 0" );
        lblOutDrvfwd = new OutputSourceLabel( "DRVFWD", OutputSourceD01.DRVFWD );
        driverPanel.add( lblOutDrvfwd, "cell 0 1" );
        lblOutDrvrev = new OutputSourceLabel( "DRVREV", OutputSourceD01.DRVREV );
        driverPanel.add( lblOutDrvrev, "cell 0 2" );
        lblOutDrvmltsp0 = new OutputSourceLabel( "DRVMLTSP0", OutputSourceD01.DRVMLTSPD0 );
        driverPanel.add( lblOutDrvmltsp0, "cell 1 0" );
        lblOutDrvmltsp1 = new OutputSourceLabel( "DRVMLTSP1", OutputSourceD01.DRVMLTSPD1 );
        driverPanel.add( lblOutDrvmltsp1, "cell 1 1" );
        lblOutDrvmltsp2 = new OutputSourceLabel( "DRVMLTSP2", OutputSourceD01.DRVMLTSPD2 );
        driverPanel.add( lblOutDrvmltsp2, "cell 1 2" );
        lblInDrvenf = new InputSourceLabel( "DRVENF", InputSourceA03.DRVENF );
        driverPanel.add( lblInDrvenf, "cell 2 0" );
        lblInDrvbm = new InputSourceLabel( "DRVBM", InputSourceA03.DRVBM );
        driverPanel.add( lblInDrvbm, "cell 2 1" );
        lblInDrvok = new InputSourceLabel( "DRVOK", InputSourceA03.DRVOK );
        driverPanel.add( lblInDrvok, "cell 2 2" );
      

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
        safePanel.setLayout( new MigLayout( "gap 5 3, ins 5", "[75!]5[75!]", "[][][]" ) );
        lblInDfc = new InputSourceLabel( "DFC", InputSourceA03.DFC );
        safePanel.add( lblInDfc, "cell 0 0" );
        lblInDw = new InputSourceLabel( "DW", InputSourceA03.DW );
        safePanel.add( lblInDw, "cell 0 1" );
        lblInHes = new InputSourceLabel( "HES", InputSourceA03.HES );
        safePanel.add( lblInHes, "cell 0 2" );
        lblInCes = new InputSourceLabel( "CES", InputSourceA03.CES );
        safePanel.add( lblInCes, "cell 1 0" );
        lblInUsl = new InputSourceLabel( "USL", InputSourceA03.USL );
        safePanel.add( lblInUsl, "cell 1 1" );
        lblInLsl = new InputSourceLabel( "LSL", InputSourceA03.LSL );
        safePanel.add( lblInLsl, "cell 1 2" );
        
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
        
        cabinPanel.setLayout( new MigLayout( "gap 5 3, ins 5", "[75!]5[75!]", "[][][]" ) );
        lblInUdz = new InputSourceLabel( "UDZ", InputSourceA03.DCS_UDZ );
        cabinPanel.add( lblInUdz, "cell 0 0" );
        lblInLdz = new InputSourceLabel( "LDZ", InputSourceA03.DCS_LDZ );
        cabinPanel.add( lblInLdz, "cell 0 1" );
        lblInUsl = new InputSourceLabel( "URL", InputSourceA03.DCS_URL );
        cabinPanel.add( lblInUsl, "cell 0 2" );
        lblInLsl = new InputSourceLabel( "LRL", InputSourceA03.DCS_LRL );
        cabinPanel.add( lblInLsl, "cell 1 0" );
        lblOutLr = new OutputSourceLabel( "LR", OutputSourceD01.LR );
        cabinPanel.add( lblOutLr, "cell 1 1" );
        
        PosButton btnConfigIO = new PosButton(TEXT.getString( "IOStatus.TEXT_BUTTON" ), ImageFactory.BUTTON_PAUSE.icon(87,30));
        btnConfigIO.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				StartUI.getTopMain().push(SetupPanel.build(connBean, slecon.setting.setup.io.InputsA03Setting.class));
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
        tooltipManager.registerToolTip( lblInAdo );
        tooltipManager.registerToolTip( lblInBs );
        tooltipManager.registerToolTip( lblInCes );
        tooltipManager.registerToolTip( lblInDbde );
        tooltipManager.registerToolTip( lblInDbdf );
        tooltipManager.registerToolTip( lblInDfc );
        tooltipManager.registerToolTip( lblInDrvbm );
        tooltipManager.registerToolTip( lblInDrvenf );
        tooltipManager.registerToolTip( lblInDrvok );
        tooltipManager.registerToolTip( lblInDw );
        tooltipManager.registerToolTip( lblInEnable );
        tooltipManager.registerToolTip( lblInEncf );
        tooltipManager.registerToolTip( lblInHes );
        tooltipManager.registerToolTip( lblInUsl );
        tooltipManager.registerToolTip( lblInLsl );
        tooltipManager.registerToolTip( lblInIns );
        tooltipManager.registerToolTip( lblInInsdown );
        tooltipManager.registerToolTip( lblInInsup );
        tooltipManager.registerToolTip( lblInLdz );
        tooltipManager.registerToolTip( lblInLsl );
        tooltipManager.registerToolTip( lblInLvcfb );
        tooltipManager.registerToolTip( lblInThm );
        tooltipManager.registerToolTip( lblInUdz );
        tooltipManager.registerToolTip( lblInUsl );
        tooltipManager.registerToolTip( lblInV24mon );
        tooltipManager.registerToolTip( lblOutBr );
        tooltipManager.registerToolTip( lblOutDr );
        tooltipManager.registerToolTip( lblOutDrven );
        tooltipManager.registerToolTip( lblOutDrvfwd );
        tooltipManager.registerToolTip( lblOutDrvmltsp0 );
        tooltipManager.registerToolTip( lblOutDrvmltsp1 );
        tooltipManager.registerToolTip( lblOutDrvmltsp2 );
        tooltipManager.registerToolTip( lblOutDrvrev );
        tooltipManager.registerToolTip( lblOutFr );
        tooltipManager.registerToolTip( lblOutLr );
        tooltipManager.registerToolTip( lblOutLvcr );
        tooltipManager.registerToolTip( lblOutRr );
        tooltipManager.registerToolTip( lblOutUr );
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
        lblInAdo.setStatus( system.getAdo() );
        lblInEncf.setStatus( system.getEncf() );
        lblInThm.setStatus( system.getThm() );
        lblInV24mon.setStatus( system.getV24mon() );
        lblOutFr.setStatus( system.getCcf() );
    }


    private void updateControl () {
        lblInEnable.setStatus( control.getEnable() );
        lblInDbde.setStatus( control.getDbde() );
        lblInDbdf.setStatus( control.getDbdf() );
        lblInBs.setStatus( control.getBs() );
        lblInLvcfb.setStatus( control.getLvcfb() );
        lblOutUr.setStatus( control.getUr() );
        lblOutDr.setStatus( control.getDr() );
        lblOutBr.setStatus( control.getBr() );
        lblOutLvcr.setStatus( control.getLvcr() );
        lblOutRr.setStatus(control.getRbr());
    }


    private void updateInspect () {
        lblInIns.setStatus( inspect.getIns() );
        lblInInsup.setStatus( inspect.getInsup() );
        lblInInsdown.setStatus( inspect.getInsdown() );
    }


    private void updateDriver () {
        lblOutDrven.setStatus( driver.getDrven() );
        lblOutDrvfwd.setStatus( driver.getDrvfwd() );
        lblOutDrvrev.setStatus( driver.getDrvrev() );
        lblOutDrvmltsp0.setStatus( driver.getDrvmltsp0() );
        lblOutDrvmltsp1.setStatus( driver.getDrvmltsp1() );
        lblOutDrvmltsp2.setStatus( driver.getDrvmltsp2() );
        lblInDrvenf.setStatus( driver.getDrvenf() );
        lblInDrvbm.setStatus( driver.getDrvbm() );
        lblInDrvok.setStatus( driver.getDrvok() );
    }


    private void updateSafety () {
        lblInDfc.setStatus( safety.getDfc() );
        lblInDw.setStatus( safety.getDw() );
        lblInHes.setStatus( safety.getHes() );
        lblInCes.setStatus( safety.getCes() );
        lblInUsl.setStatus( safety.getUsl() );
        lblInLsl.setStatus( safety.getLsl() );
    }


    private void updateCabin () {
    	lblInUdz.setStatus( cabin.getUdz() );
        lblInLdz.setStatus( cabin.getLdz() );
        lblInLsl.setStatus( cabin.getLsl() );
        lblInUsl.setStatus( cabin.getUsl() );
        lblOutLr.setStatus( cabin.getLr() );
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

    private static final Object[] updateList = { InputSourceA03.ADO, InputSourceA03.V24MON, InputSourceA03.ENCF, InputSourceA03.THM, OutputSourceD01.CCF,
    		InputSourceA03.ENABLE, InputSourceA03.DBDE, InputSourceA03.DBDF, InputSourceA03.BS, InputSourceA03.LVCFB, OutputSourceD01.UR, OutputSourceD01.DR,
            OutputSourceD01.RR, OutputSourceD01.BR, OutputSourceD01.LVCR, OutputSourceD01.DRVEN, OutputSourceD01.DRVFWD, OutputSourceD01.DRVREV,
            OutputSourceD01.DRVMLTSPD0, OutputSourceD01.DRVMLTSPD1, OutputSourceD01.DRVMLTSPD2, InputSourceA03.DRVBM, InputSourceA03.DRVOK, InputSourceA03.DRVENF,
            InputSourceA03.DFC, InputSourceA03.DW, InputSourceA03.HES, InputSourceA03.CES, InputSourceA03.UDZ, InputSourceA03.LDZ, InputSourceA03.USL, InputSourceA03.LSL,
            OutputSourceD01.LR, InputSourceA03.INS, InputSourceA03.INSUP, InputSourceA03.INSDOWN, };

    
    public void setHot () {
        final HashMap<Object, OnOffStatus> map = new HashMap<>();
        final byte[] rawIO = status.getMcsIO();
        final CrossBar crossbar = new CrossBar( mcsconfig.getCrossbar() );

        setToolTip( crossbar );
        for ( Object obj : updateList ) {
            if ( obj instanceof InputSourceA03 ) {
                boolean inputPresent = crossbar.isInputPresent( rawIO, ( InputSourceA03 )obj );
                map.put( obj, inputPresent ? OnOffStatus.ON : OnOffStatus.OFF );
            } else if ( obj instanceof OutputSourceD01 ) {
                boolean outputPresent;
                try {
                    outputPresent = crossbar.isOutputPresent( rawIO, ( OutputSourceD01 )obj );
                    map.put( obj, outputPresent ? OnOffStatus.ON : OnOffStatus.OFF );
                } catch ( ReferenceSourceNotFoundException ex ) {
                    map.put( obj, OnOffStatus.DISABLED );
                }
            }
        }
        final SystemBean sb = new SystemBean();
        final ControlBean ctrlb = new ControlBean();
        final DriverBean db = new DriverBean();
        final SafetyBean stb = new SafetyBean();
        final CabinBean cbb = new CabinBean();
        final InspectBean ib = new InspectBean();
        sb.setAdo( map.get( InputSourceA03.ADO ) );
        sb.setV24mon( map.get( InputSourceA03.V24MON ) );
        sb.setEncf( map.get( InputSourceA03.ENCF ) );
        sb.setThm( map.get( InputSourceA03.THM ) );
        sb.setCcf( map.get( OutputSourceD01.CCF ) );
        ctrlb.setEnable( map.get( InputSourceA03.ENABLE ) );
        ctrlb.setDbde( map.get( InputSourceA03.DBDE ) );
        ctrlb.setDbdf( map.get( InputSourceA03.DBDF ) );
        ctrlb.setBs( map.get( InputSourceA03.BS ) );
        ctrlb.setLvcfb( map.get( InputSourceA03.LVCFB ) );
        ctrlb.setUr( map.get( OutputSourceD01.UR ) );
        ctrlb.setDr( map.get( OutputSourceD01.DR ) );
        ctrlb.setRr( map.get( OutputSourceD01.RR ) );
        ctrlb.setBr( map.get( OutputSourceD01.BR ) );
        ctrlb.setLvcr( map.get( OutputSourceD01.LVCR ) );
        db.setDrven( map.get( OutputSourceD01.DRVEN ) );
        db.setDrvfwd( map.get( OutputSourceD01.DRVFWD ) );
        db.setDrvrev( map.get( OutputSourceD01.DRVREV ) );
        db.setDrvmltsp0( map.get( OutputSourceD01.DRVMLTSPD0 ) );
        db.setDrvmltsp1( map.get( OutputSourceD01.DRVMLTSPD1 ) );
        db.setDrvmltsp2( map.get( OutputSourceD01.DRVMLTSPD2 ) );
        db.setDrvenf( map.get( InputSourceA03.DRVENF ) );
        db.setDrvbm( map.get( InputSourceA03.DRVBM ) );
        db.setDrvok( map.get( InputSourceA03.DRVOK ) );
        stb.setDfc( map.get( InputSourceA03.DFC ) );
        stb.setDw( map.get( InputSourceA03.DW ) );
        stb.setHes( map.get( InputSourceA03.HES ) );
        stb.setCes( map.get( InputSourceA03.CES ) );
        cbb.setUdz( map.get( InputSourceA03.UDZ ) );
        cbb.setLdz( map.get( InputSourceA03.LDZ ) );
        cbb.setUsl( map.get( InputSourceA03.USL ) );
        cbb.setLsl( map.get( InputSourceA03.LSL ) );
        cbb.setLr( map.get( OutputSourceD01.LR ) );
        ib.setIns( map.get( InputSourceA03.INS ) );
        ib.setInsup( map.get( InputSourceA03.INSUP ) );
        ib.setInsdown( map.get( InputSourceA03.INSDOWN ) );
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
        private InputSourceA03 inputSource;
        private OnOffStatus status;

        @Override
        public void setForeground(Color fg) {
        	// TODO Auto-generated method stub
        	super.setForeground(Color.WHITE);
        }


        public InputSourceLabel ( String caption, InputSourceA03 is ) {
            super( caption );
            this.inputSource = is;
        }


        public void setStatus ( OnOffStatus onoff ) {
            this.status = onoff;
            update();
        }

        
        public void update () {
            setIcon( statusToIcon( Type.INPUT, status ) );
        }


        public String getToolTipText () {
            return String.format( TEXT.getString( "IOStatus.TEXT_TOOLTIP_INPUT" ),
                                  String.format( "%s - %s", inputSource, inputSource.fullname ),    // input name
                                  crossbar == null
                                  ? TEXT.getString( "IOStatus.TEXT_UNASSIGNED" )
                                  : crossbar.getInputPinA03(inputSource),
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
            OutputPinA03 pin = null;
            for ( OutputPinA03 p : OutputPinA03.values() ) {
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
}
