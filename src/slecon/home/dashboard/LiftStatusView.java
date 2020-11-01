package slecon.home.dashboard;
import static logic.util.SiteManagement.MON_MGR;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.evlog.ErrorLog;
import logic.evlog.Level;
import logic.evlog.MCSErrorCode;
import logic.evlog.OCSErrorCode;
import logic.util.PageTreeExpression;
import logic.util.SiteManagement;
import logic.util.Version;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SequenceImage;
import slecon.component.Workspace;
import slecon.home.PosButton;
import slecon.inspect.InspectPanel;
import slecon.inspect.calls.FloorCallElement;
import slecon.interfaces.Page;
import slecon.setting.SetupPanel;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import comm.Parser_Deploy;
import comm.Parser_DoorEnable;
import comm.Parser_Error;
import comm.Parser_Log;
import comm.Parser_Log.Log;
import comm.Parser_McsConfig;
import comm.Parser_Misc;
import comm.Parser_Run;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.constants.CallDef;
import comm.constants.DisCallDef;
import comm.constants.DoorAction;
import comm.constants.OcsDirection;
import comm.constants.OcsModule;
import comm.event.LiftDataChangedListener;




public class LiftStatusView extends JPanel implements ActionListener, Page, LiftDataChangedListener {
    private static final long           serialVersionUID        = 7217079121042284743L;
    static final ResourceBundle TEXT                            = ToolBox.getResourceBundle( "home.LiftStatusView" );
    private static final ImageIcon      DOOR_OPENED_ICON        = ImageFactory.DOOR_OPENED_ICON.icon( 54, 54 );
    private static final ImageIcon      DOOR_OPENING_ICON       = ImageFactory.DOOR_OPENING_ICON.icon( 54, 54 );
    private static final ImageIcon      DOOR_CLOSING_ICON       = ImageFactory.DOOR_CLOSING_ICON.icon( 54, 54 );
    private static final ImageIcon      DOOR_CLOSED_ICON        = ImageFactory.DOOR_CLOSED_ICON.icon( 54, 54 );
    private static final ImageIcon      DOOR_CONNECTION_ICON       = ImageFactory.DOOR_ONLINE.icon( 54, 54 );
    private static final ImageIcon      DOOR_UNKNOWN_ICON       = ImageFactory.DOOR_OFFLINE.icon( 54, 54 );
    private static final ImageIcon      DOOR_SGS                = ImageFactory.DOOR_SGS.icon( 54, 54 );
    private static final ImageIcon      SETUP_ICON_NORMAL       = ImageFactory.SETUP_NORMAL.icon( 42, 42 );
    private static final ImageIcon      SETUP_ICON_ACTIVITY     = ImageFactory.SETUP_ACTIVITY.icon( 42, 42 );
    private static final ImageIcon      INSPECT_ICON_NORMAL     = ImageFactory.INSPECT_NORMAL.icon( 42, 42 );
    private static final ImageIcon      INSPECT_ICON_ACTIVITY   = ImageFactory.INSPECT_ACTIVITY.icon( 42, 42 );
    private static final ImageIcon      LED1                    = ImageFactory.LIGHT_BRIGHT_GREEN.icon(16, 16);
    private static final ImageIcon      LED2                    = ImageFactory.LIGHT_BRIGHT_ORANGE.icon(16, 16);
    private static final ImageIcon      LED3                    = ImageFactory.LIGHT_BRIGHT_RED.icon(16, 16);
    private static final ImageIcon      LED7                    = ImageFactory.LIGHT_BLACK.icon(16, 16);
    private static final ImageIcon[]    UP_ANIM_ICON            = ImageFactory.ARROW_ANIM_UP.icons( 20, 20 );
    private static final ImageIcon[]    DOWN_ANIM_ICON          = ImageFactory.ARROW_ANIM_DOWN.icons( 20, 20 );
    private static final ImageIcon[]    UP_ARROW_ICON           = { ImageFactory.ARROW_ANIM_UP.icon( 20, 20 ) };
    private static final ImageIcon[]    DOWN_ARROW_ICON         = { ImageFactory.ARROW_ANIM_DOWN.icon( 20, 20 ) };
    private static final ImageIcon[]    NONE_ARROW_ICON         = new ImageIcon[] { ImageFactory.NONE.icon( 20, 20 ) };
    private static final ImageIcon 		BUTTON_LOG 				= ImageFactory.BUTTON_LOG.icon(180,40);
    private static final ImageIcon 		BUTTON_CALL 			= ImageFactory.BUTTON_CALL.icon(180,40);
    private static final ImageIcon 		BUTTON_MOTION 			= ImageFactory.BUTTON_MOTION.icon(180,40);
    private static final ImageIcon 		BUTTON_OPERATION 		= ImageFactory.BUTTON_OPERATION.icon(180,40);
    static HashMap<String, String>      styles                  = new HashMap<>();
    static final PageTreeExpression     WRITE_HOME_EXPRESSION   = new PageTreeExpression( "write_home" );
    private final Map<Integer, String>  floorTexts              = new TreeMap<>();
    private final Map<Integer, Integer> doorzones               = new TreeMap<>();
    private JComponent[] context;
    
    static {
        styles.put( "statusLED", "20 6 16 16 c" );
        styles.put( "lblTitle", "40 5 196 20 l" );
        styles.put( "separator", "20 25 800 1 c");
        styles.put( "labelAttr1", "150 45 92 14 l" );
        styles.put( "labelAttr2", "150 65 92 14 l" );
        styles.put( "labelAttr3", "150 85 92 14 l" );
        styles.put( "lblLoginUsername", "220 45 92 14 l" );
        styles.put( "lblMode", "220 65 92 14 l" );
        styles.put( "lblUptime", "220 85 92 14 l" );
        styles.put( "lblDirection", "30 25 24 24 r" );
        styles.put( "lblPosition", "60 30 31 17 l" );
        styles.put( "lblFrontDoorStatus", "5 48 54 54 c" );
        styles.put( "lblRearDoorStatus", "65 48 54 54 c" );
        styles.put( "DoorStatus", "35 48 54 54 c" );
        styles.put( "btnSetupView", "700 45 42 42 c" );
        styles.put( "btnInspectView", "760 45 42 42 c" );
        styles.put( "shortcutPanel", "20 110 800 160 l" );
        styles.put( "btnLog", "30 275 180 40 c" );
        styles.put( "btnCall", "230 275 180 40 c" );
        styles.put( "btnMotion", "430 275 180 40 c" );
        styles.put( "btnShortCut", "630 275 180 40 c" );
        styles.put( "separator1", "20 280 1 30 c" );
        styles.put( "separator2", "220 280 1 30 c" );
        styles.put( "separator3", "420 280 1 30 c" );
        styles.put( "separator4", "620 280 1 30 c" );
        styles.put( "separator5", "820 280 1 30 c" );
    }


    private LiftCollectionPanel        sitePanel;
    private LiftStatusBean             liftStatus;
    private JLabel                     lblTitle;
    private JPanel				   	   separator;
    private JLabel                     lblLoginUsername;
    private JLabel                     lblUptime;
    private JLabel                     lblPosition;
    private SequenceImage              lblDirection;
    private JLabel                     lblFrontDoorStatus;
    private JLabel                     lblRearDoorStatus;
    private JLabel                     lblMode;
    private JLabel                     labelAttr1;
    private JLabel                     labelAttr3;
    private JLabel                     labelAttr2;
    private JLabel                     statusLED;
    private PosButton                  btnSetupView;
    private PosButton                  btnInspectView;
    private CallSubPanel               callPanel;
    private JList<ErrorLog>            errorLog;
    private DefaultListModel<ErrorLog> model;
    JPanel							   shortcutPanel;
    private JPanel				   	   separator1;
    private PosButton                  btnLog;
    private JPanel				   	   separator2;
    private PosButton                  btnCall;
    private JPanel				   	   separator3;
    private PosButton                  btnMotion;
    private JPanel				   	   separator4;
    private PosButton                  btnShortCut;
    private JPanel				   	   separator5;
    private ControlPanelData           controlPanelData;
    private MigLayout                  layout;
    private MotionSubPanel             motionSubPanel;
    private ControlSubPanel            controlSubPanel;

    //////////////////////////// Page interface ////////////////////////////
    private final LiftConnectionBean    connectionBean;
    private Parser_Status               status;
    private Parser_Deploy               deploy;
    private Parser_Run                  run;
    private Parser_McsConfig            mcsconfig;
    private Parser_Log                  log;
    private Parser_Error                error;
    Parser_Misc                         misc;
    private Parser_DoorEnable           doorEnable;
    
    private int speed_index = 0;
    private float [] speed_avg = new float[10];
    
    /**
     * Create the panel.
     */
    public LiftStatusView ( LiftConnectionBean bean, LiftCollectionPanel sitePanel ) {
    	this.connectionBean = bean;
    	this.sitePanel      = sitePanel;

    	initGUI();
        updateI18nGUI();
        
    }

    public LiftConnectionBean getConnectionBean () {
        return connectionBean;
    }
/*
    @Override
    public void paintComponent ( Graphics g ) {
        Image bgimage = ImageFactory.DASHBOARD_LIFTSTATUS.image();
        int   width   = getWidth();
        int   height  = getHeight();
        g.setColor( new Color( 0x4A708B ) );
        g.fillRect( 0, 0, width, height );
        g.drawImage( bgimage, 0, 0, null );
    }
*/
    /**
     * 	Gain Board Version.	
     * */
    private int getBoardVersion(LiftConnectionBean connBean) {
    	int ret = 0;
    	Version ver = SiteManagement.getVersion(connBean);
        if (ver!=null) {
            if ("A03".equalsIgnoreCase(ver.getMcsBoardVersion())) {
            	ret = 1;
            } else if ("A05".equalsIgnoreCase(ver.getMcsBoardVersion())) {
            	ret = 2;
            } else if ("A07".equalsIgnoreCase(ver.getMcsBoardVersion())) {
            	ret = 3;
            } else if ("C01".equalsIgnoreCase(ver.getMcsBoardVersion())) {
            	ret = 4;
            } else if ("A01".equalsIgnoreCase(ver.getMcsBoardVersion()) || "D01".equalsIgnoreCase(ver.getMcsBoardVersion())) {
            	ret = 5;
            }
        } else {
           // ToolBox.showErrorMessage( Dict.lookup( "ConnectionLost" ) );
        	ret = 5;
        }
        return ret;
    }
    
    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR, 2, true));
        layout = new MigLayout( "nogrid, w 840!, h 320!, gap 0", "[0!]", "[0!]" );
        setLayout( layout );
        {
            add( statusLED = new JLabel( LED1 ) );
            add( lblTitle = new JLabel( "" ) );
            add( separator = new JPanel());
            add( labelAttr1 = new JLabel( "Host Name:" ) );
            add( lblLoginUsername = new JLabel( "-" ) );
            add( labelAttr3 = new JLabel( "Mode:" ) );
            add( lblMode = new JLabel( "" ) );
            add( labelAttr2 = new JLabel( "Last Maint:" ) );
            add( lblUptime = new JLabel( "" ) );
            add( lblDirection = new SequenceImage() );
            add( lblPosition = new JLabel( "-" ) );
            add( lblFrontDoorStatus = new JLabel() );
            add( lblRearDoorStatus = new JLabel() );
            add( btnSetupView = new PosButton( SETUP_ICON_NORMAL, SETUP_ICON_ACTIVITY ) );
            add( btnInspectView = new PosButton( INSPECT_ICON_NORMAL, INSPECT_ICON_ACTIVITY ) );
            add( shortcutPanel = new JPanel());
            add( separator1 = new JPanel());
            add( btnLog = new PosButton(BUTTON_LOG) );
            add( separator2 = new JPanel());
            add( btnCall = new PosButton(BUTTON_CALL) );
            add( separator3 = new JPanel());
            add( btnMotion = new PosButton(BUTTON_MOTION) );
            add( separator4 = new JPanel());
            add( btnShortCut = new PosButton(BUTTON_OPERATION) );
            add( separator5 = new JPanel());
            lblTitle.setFont( FontFactory.FONT_16_PLAIN );
            lblTitle.setForeground(Color.WHITE);
            labelAttr1.setFont( FontFactory.FONT_12_BOLD );
            labelAttr1.setForeground(Color.WHITE);
            lblLoginUsername.setFont( FontFactory.FONT_12_PLAIN );
            lblLoginUsername.setForeground(Color.WHITE);
            labelAttr3.setFont( FontFactory.FONT_12_BOLD );
            labelAttr3.setForeground(Color.WHITE);
            lblMode.setFont( FontFactory.FONT_12_PLAIN );
            lblMode.setForeground(Color.WHITE);
            labelAttr2.setFont( FontFactory.FONT_12_BOLD );
            labelAttr2.setForeground(Color.WHITE);
            lblUptime.setFont( FontFactory.FONT_12_PLAIN );
            lblUptime.setForeground(Color.WHITE);
            lblDirection.setFont( getFont().deriveFont( 18.0f ) );
            lblDirection.setForeground(Color.WHITE);
            lblPosition.setFont( getFont().deriveFont( 18.0f ) );
            lblPosition.setForeground(Color.WHITE);
            
            shortcutPanel.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR, 1));
            shortcutPanel.setBounds(20, 110, 800, 160);
            shortcutPanel.setBackground(StartUI.SUB_BACKGROUND_COLOR);
            
            btnLog.setFont(FontFactory.FONT_12_BOLD);
            btnLog.setForeground(StartUI.LOG_Button_COLOR);
            
            btnCall.setFont(FontFactory.FONT_12_BOLD);
            
            btnMotion.setFont(FontFactory.FONT_12_BOLD);
            
            btnShortCut.setFont(FontFactory.FONT_12_BOLD);
            
            StartUI.setStyle(layout, styles, statusLED, "statusLED" );
            StartUI.setStyle(layout, styles, lblTitle, "lblTitle" );
            StartUI.setStyle(layout, styles, separator,"separator");
            separator.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR,1));
            StartUI.setStyle(layout, styles, labelAttr1, "labelAttr1" );
            StartUI.setStyle(layout, styles, labelAttr2, "labelAttr2" );
            StartUI.setStyle(layout, styles, labelAttr3, "labelAttr3" );
            StartUI.setStyle(layout, styles, lblLoginUsername, "lblLoginUsername" );
            StartUI.setStyle(layout, styles, lblMode, "lblMode" );
            StartUI.setStyle(layout, styles, lblUptime, "lblUptime" );
            StartUI.setStyle(layout, styles, lblDirection, "lblDirection" );
            StartUI.setStyle(layout, styles, lblPosition, "lblPosition" );
            StartUI.setStyle(layout, styles, lblFrontDoorStatus, "lblFrontDoorStatus" );
            StartUI.setStyle(layout, styles, lblRearDoorStatus, "lblRearDoorStatus" );
            StartUI.setStyle(layout, styles, btnSetupView, "btnSetupView" );
            StartUI.setStyle(layout, styles, btnInspectView, "btnInspectView" );
            StartUI.setStyle(layout, styles, shortcutPanel, "shortcutPanel" );
            StartUI.setStyle(layout, styles, btnLog, "btnLog" );
            StartUI.setStyle(layout, styles, btnCall, "btnCall" );
            StartUI.setStyle(layout, styles, btnMotion, "btnMotion" );
            StartUI.setStyle(layout, styles, btnShortCut, "btnShortCut" );
            StartUI.setStyle(layout, styles, separator1, "separator1");
            StartUI.setStyle(layout, styles, separator2, "separator2");
            StartUI.setStyle(layout, styles, separator3, "separator3");
            StartUI.setStyle(layout, styles, separator4, "separator4");
            StartUI.setStyle(layout, styles, separator5, "separator5");
            separator1.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR,1));
            separator2.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR,1));
            separator3.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR,1));
            separator4.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR,1));
            separator5.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR,1));
            updateUI();
        }
        {
            btnSetupView.addActionListener( this );
            btnInspectView.addActionListener( this );
            btnLog.addActionListener( this );
            btnCall.addActionListener( this );
            btnMotion.addActionListener( this );
            btnShortCut.addActionListener( this );
        }
        {
            model    = new DefaultListModel<ErrorLog>();
            errorLog = new LogSubView( model );
            errorLog.setFont( FontFactory.FONT_12_PLAIN );
            errorLog.setBackground(StartUI.SUB_BACKGROUND_COLOR);

            final MouseAdapter mouseAdapter = new MouseAdapter() {
                private Timer timer;
                private int   index = -1;
                {
                    timer = new Timer( 1600, new ActionListener() {
                        @Override
                        public void actionPerformed ( ActionEvent e ) {
                            doClickEvent();
                            timer.stop();
                        }
                    } );
                    timer.setRepeats( false );
                }
                @Override
                public void mouseMoved ( MouseEvent evt ) {
                    if ( index != errorLog.getSelectedIndex() && timer.isRunning() ) {
                        index = errorLog.getSelectedIndex();
                        timer.restart();
                    }
                }
                @Override
                public void mouseDragged ( MouseEvent evt ) {
                    if ( index != errorLog.getSelectedIndex() && timer.isRunning() ) {
                        index = errorLog.getSelectedIndex();
                        timer.restart();
                    }
                }
                @Override
                public void mousePressed ( MouseEvent evt ) {
                    if ( evt.getClickCount() == 1 ) {
                        index = errorLog.getSelectedIndex();
                        timer.start();
                    }
                }
                @Override
                public void mouseReleased ( MouseEvent evt ) {
                    cancelTimer();
                }
                private void cancelTimer () {
                    index = -1;
                    timer.stop();
                }
                @Override
                public void mouseEntered ( MouseEvent evt ) {
                    cancelTimer();
                }
                @Override
                public void mouseExited ( MouseEvent evt ) {
                    cancelTimer();
                }
                @Override
                public void mouseClicked ( MouseEvent evt ) {
                    if ( evt.getClickCount() == 2 )
                        doClickEvent();
                }
                private void doClickEvent () {
                    cancelTimer();
                    if (SiteManagement.isAlive(connectionBean)) {
                        final InspectPanel panelBinder = InspectPanel.build(connectionBean, slecon.inspect.logs.Main.class);
                        StartUI.getTopMain().push(panelBinder);
                        SwingUtilities.invokeLater( new Runnable() {

                            @Override
                            public void run () {
                                if (panelBinder.getWorkspace().getMainPanel() instanceof slecon.inspect.logs.Main) {
                                    ErrorLog value = errorLog.getSelectedValue();
                                    if(value != null) {
                                        ( ( slecon.inspect.logs.Main )panelBinder.getWorkspace().getMainPanel() ).setSelectedIndex( value.index );
                                    }
                                }
                            }
                            
                        });
                    }
                }
            };
            errorLog.addMouseListener( mouseAdapter );
            errorLog.addMouseMotionListener( mouseAdapter );
            
            JScrollPane errorLogScrollPanel = new JScrollPane( errorLog, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    									 		   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER ) {
			private static final long serialVersionUID = 3456960100419535223L;
			{
				this.setOpaque( false );
				this.getViewport().setOpaque( false );
				this.setViewportBorder( null );
				this.setBorder( null );
				this.setPreferredSize(new Dimension(780,150));
				this.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
				AdjustmentListener onScrollListener = new AdjustmentListener() {
				@Override
				public void adjustmentValueChanged ( AdjustmentEvent e ) {
					Point mousePoint = MouseInfo.getPointerInfo().getLocation();
					SwingUtilities.convertPointFromScreen( mousePoint, errorLog );
					errorLog.dispatchEvent( new MouseEvent( errorLog, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(),
					                                  MouseEvent.NOBUTTON, mousePoint.x, mousePoint.y, 0, false ) );
					}
				};
					getHorizontalScrollBar().addAdjustmentListener( onScrollListener );
					getVerticalScrollBar().addAdjustmentListener( onScrollListener );
			}};
			
			callPanel = new CallSubPanel( this, getBoardVersion(connectionBean));
			motionSubPanel = new MotionSubPanel();
            controlSubPanel = new ControlSubPanel(this);

			shortcutPanel.add(errorLogScrollPanel);
			JComponent[] context_temp = {errorLogScrollPanel, callPanel, motionSubPanel, controlSubPanel };
			context = context_temp;
			
        }
    }

    /* control panel */
    public void setControlPanelData ( ControlPanelData data ) {
        this.controlPanelData = data;
        if ( data != null ) {
            controlSubPanel.btnTF.setEnabled( true );
            controlSubPanel.btnBF.setEnabled( true );
            controlSubPanel.btnCHC.setEnabled( true );
            controlSubPanel.btnDDO.setEnabled( true );
            controlSubPanel.btnCHC.setSelected( controlPanelData.isChc() );
            controlSubPanel.btnDDO.setSelected( controlPanelData.isDdo() );
        } else {
            controlSubPanel.btnTF.setEnabled( false );
            controlSubPanel.btnBF.setEnabled( false );
            controlSubPanel.btnCHC.setEnabled( false );
            controlSubPanel.btnDDO.setEnabled( false );
        }
    }

    /* lift-status */
    public final LiftStatusBean getLiftStatus () {
        return liftStatus;
    }


    public final void setLiftStatus ( LiftStatusBean bean ) {
        this.liftStatus = bean;
        updateLiftStatus();
    }

    /* log panel */
    public void addErrorLog ( final ErrorLog errorLog ) {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                model.add( model.size(), errorLog );
            }
        } );
    }


    public void clearErrorLog () {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                model.clear();
            }
        } );
    }
    

    public void updateLiftStatus () {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                ImageIcon led = LED7;
                String loginUsername = "-";
                String uptime = "-";
                String mode = "-";
                String position = "-";
                ImageIcon[] direction = NONE_ARROW_ICON;
                ImageIcon frontdoorStatus = DOOR_UNKNOWN_ICON;
                ImageIcon reardoorStatus = DOOR_UNKNOWN_ICON;
                boolean tabEnabled = false;
                boolean setupEnabled = false;
                boolean inspectEnabled = false;

                final LiftStatusBean status = getLiftStatus();
                if ( status != null ) {
                    /* led */
                    if ( status.getModule() != null )
                        switch ( status.getModule() ) {
                        case ARO :
                        case NOR :
                            led = LED1;
                            break;
                        case ATT :
                        case CORR :
                        case ES :
                        case TDEO:
                        case FEO :
                        case FRO :
                        case INSP :
                        case ISC :
                        case PAK :
                        case RSCR :
                        case REL:
                        case EPB:
                        case UCMT:
                        case EQO:
                        case MCEX:
                            led = LED2;
                            break;
                        case DCS :
                        case DAF :
                        case EPO :
                        case FAIL :
                        case OLP :
                            led = LED3;
                            break;
                        default :
                            led = LED7;
                        }

                    /* login user name */
                    loginUsername = "admin";

                    /* lastMaint */
                    try {
                        uptime = status.getUptime();
                    } catch ( NullPointerException e ) {
                    }

                    /* module */
                    if ( status.getModule() != null )
                        mode = status.getModule().toString();

                    /* direction */
                    if ( status.getDirection() != null ) {
                        switch ( status.getDirection() ) {
                        case UP :
                            direction = status.isAnimation() ? UP_ANIM_ICON : UP_ARROW_ICON;
                            break;
                        case DOWN :
                            direction = status.isAnimation() ? DOWN_ANIM_ICON : DOWN_ARROW_ICON;
                            break;
                        case NO :
                            direction = NONE_ARROW_ICON;
                            break;
                        default :
                            direction = NONE_ARROW_ICON;
                        }
                    }

                    /* position */
                    if ( status.getPosition() != null )
                        position = status.getPosition();

                    /* front door */
                    if ( status.getFrontDoorAction() == DoorAction.FORCED_CLOSE ) {
                    	frontdoorStatus = DOOR_CLOSING_ICON;
                    }
                    else if ( status.getFrontDoorAction() == DoorAction.OPENED ) {
                    	frontdoorStatus = DOOR_OPENED_ICON;
                    } else if ( status.getFrontDoorAction() == DoorAction.OPENING ) {
                    	frontdoorStatus = DOOR_OPENING_ICON;
                    } else if ( status.getFrontDoorAction() == DoorAction.CLOSING ) {
                    	frontdoorStatus = DOOR_CLOSING_ICON;
                    } else if ( status.getFrontDoorAction() == DoorAction.CLOSED ) {
                    	frontdoorStatus = DOOR_CLOSED_ICON;
                    } else if ( status.getFrontDoorAction() == DoorAction.SGS ) {
                    	frontdoorStatus = DOOR_SGS;
                    } else {
                    	frontdoorStatus = DOOR_UNKNOWN_ICON;
                    }
                    
                    /* rear door */
                    if ( status.getRearDoorAction() == DoorAction.FORCED_CLOSE ) {
                    	reardoorStatus = DOOR_CLOSING_ICON;
                    } else if ( status.getRearDoorAction() == DoorAction.OPENED ) {
                    	reardoorStatus = DOOR_OPENED_ICON;
                    } else if ( status.getRearDoorAction() == DoorAction.OPENING ) {
                    	reardoorStatus = DOOR_OPENING_ICON;
                    } else if ( status.getRearDoorAction() == DoorAction.CLOSING ) {
                    	reardoorStatus = DOOR_CLOSING_ICON;
                    } else if ( status.getRearDoorAction() == DoorAction.CLOSED ) {
                    	reardoorStatus = DOOR_CLOSED_ICON;
                    } else if ( status.getRearDoorAction() == DoorAction.SGS ) {
                    	reardoorStatus = DOOR_SGS;
                    } else {
                    	reardoorStatus = DOOR_UNKNOWN_ICON;
                    }

                    tabEnabled = true;
                    setupEnabled = true;
                    inspectEnabled = true;
                }

                //tabbedPane.setEnabled( tabEnabled );
                errorLog.setEnabled( tabEnabled );
                callPanel.setEnabled( tabEnabled );
                motionSubPanel.setEnabled( tabEnabled );
                controlSubPanel.setEnabled( tabEnabled );
                btnLog.setEnabled(tabEnabled);
                btnCall.setEnabled(tabEnabled);
                btnMotion.setEnabled(tabEnabled);
                btnShortCut.setEnabled(tabEnabled);
                
                btnInspectView.setEnabled( setupEnabled );
                btnSetupView.setEnabled( inspectEnabled );
                statusLED.setIcon( led );
                lblLoginUsername.setText( loginUsername );
                lblUptime.setText( uptime );
                lblMode.setText( mode );
                lblDirection.setImages( direction );
                lblPosition.setText( position );
                lblFrontDoorStatus.setIcon( frontdoorStatus );
                lblRearDoorStatus.setIcon( reardoorStatus );
                sitePanel.updateOrder();
               
                
            }
        } );
    }


    protected static String toLoginUsernameString ( String hostName ) {
        return hostName == null
               ? "--"
               : hostName;
    }


    public void updateI18nGUI () {
        labelAttr1.setText( TEXT.getString( "Attr1.text" ) );
        labelAttr2.setText( TEXT.getString( "Attr2.text" ) );
        labelAttr3.setText( TEXT.getString( "Attr3.text" ) );
        
        btnLog.setText( TEXT.getString( "ErrorLog.text" ) );
        btnCall.setText( TEXT.getString( "Call.text" ) );
        btnMotion.setText( TEXT.getString( "Motion.text" ) );
        btnShortCut.setText( TEXT.getString( "ShortCut.text" ) );
    }
    
    private void BtnActivityColor(final ActionEvent e) {
    	int val = 0;
    	if( e.getSource() == btnLog ) {
    		val = 1;
    	}
    	if( e.getSource() == btnCall ) {
    		val = 2;
    	}
    	if( e.getSource() == btnMotion ) {
    		val = 3;
    	}
    	if( e.getSource() == btnShortCut ) {
    		val = 4;
    	}
    	
    	btnLog.setForeground(val==1?StartUI.LOG_Button_COLOR:Color.WHITE);
    	btnCall.setForeground(val==2?StartUI.CALL_Button_COLOR:Color.WHITE);
    	btnMotion.setForeground(val==3?StartUI.MOTION_Button_COLOR:Color.WHITE);
    	btnShortCut.setForeground(val==4?StartUI.SHUTCUT_Button_COLOR:Color.WHITE);
    	shortcutPanel.setBorder((val == 2 || val == 3 )?null:BorderFactory.createLineBorder(StartUI.BORDER_COLOR,1));
    }

    public void actionPerformed ( final ActionEvent e ) {
    	
    	BtnActivityColor(e);
    	
        if ( e.getSource() == btnInspectView ) {
            do_btnInspectView_actionPerformed( e );
        }
        if ( e.getSource() == btnSetupView ) {
            do_btnSetupView_actionPerformed( e );
        }
        if(e.getSource() == btnLog ) {
        	shortcutPanel.removeAll();
        	shortcutPanel.add(context[0]);
        	updateUI();
        }
        if(e.getSource() == btnCall ) {
        	shortcutPanel.removeAll();
        	shortcutPanel.add(context[1]);
        	updateUI();
        }
		if(e.getSource() == btnMotion ) {
			shortcutPanel.removeAll();
			shortcutPanel.add(context[2]);
			updateUI();
		}
		if(e.getSource() == btnShortCut ) {
			shortcutPanel.removeAll();
			shortcutPanel.add(context[3]);
			updateUI();
        }
    }


    protected void do_btnSetupView_actionPerformed ( final ActionEvent e ) {
        StartUI.getTopMain().push( SetupPanel.build( connectionBean, null ) );
    }


    protected void do_btnInspectView_actionPerformed ( final ActionEvent e ) {
        StartUI.getTopMain().push( InspectPanel.build( connectionBean, null ) );
    }


    /* call panel */
    public void setCallPanelData ( final Map<Integer, String> floorText, final TreeSet<FloorCallElement> calls ) {
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run () {
                callPanel.setData( floorText, calls );
            }
        } );
    }
    
    /* call status*/
    public void setCallStatusData(final TreeSet<FloorCallElement> calls) {
    	SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                callPanel.setCallStatus( calls, getBoardVersion(connectionBean));
            }
        } );
    }
    
    /* motion-chart panel */
    public void setChartRangeBound ( float highestDZ, float maxSpeed ) {
        motionSubPanel.setRangeBound( highestDZ, maxSpeed );
    }


    public void addLastestChartData( long ts, float position, float speed ) {
        motionSubPanel.addToMotionChartDataQueue( ts, position, speed );
        motionSubPanel.setChartPanelMessage( position, speed );
    }


    public static void hideToolTip ( JComponent comp ) {
        Action action = comp.getActionMap().get( "postTip" );
        if ( action != null ) {
            ActionEvent ae = new ActionEvent( comp, ActionEvent.ACTION_PERFORMED, "postTip", EventQueue.getMostRecentEventTime(), 0 );
            action.actionPerformed( ae );
        }
    }


    @Override
    public void onCreate ( Workspace pageBinder ) throws Exception {
    }


    @Override
    public void onStart () throws Exception {
        lblTitle.setText( connectionBean.getName() );
        
        try {
            status = new Parser_Status( connectionBean.getIp(), connectionBean.getPort() );
            deploy = new Parser_Deploy( connectionBean.getIp(), connectionBean.getPort() );
            mcsconfig = new Parser_McsConfig( connectionBean.getIp(), connectionBean.getPort() );
            run = new Parser_Run( connectionBean.getIp(), connectionBean.getPort() );
            misc = new Parser_Misc( connectionBean.getIp(), connectionBean.getPort() );
            error = new Parser_Error( connectionBean.getIp(), connectionBean.getPort() );
            log = new Parser_Log( connectionBean.getIp(), connectionBean.getPort() );
            doorEnable=new Parser_DoorEnable( connectionBean.getIp(), connectionBean.getPort() );
            
            MON_MGR.addEventListener( this, connectionBean.getIp(), connectionBean.getPort(),
                                      AgentMessage.LOG.getCode() |
                                      AgentMessage.STATUS.getCode() |
                                      AgentMessage.DEPLOYMENT.getCode() |
                                      AgentMessage.MCS_CONFIG.getCode() |
                                      AgentMessage.RUN.getCode() |
                                      AgentMessage.ERROR.getCode() |
                                      AgentMessage.DOOR_ENABLE.getCode());
            init();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.nanoTime();
        }
    }


    @Override
    public void onResume () throws Exception {
//        motionSubPanel.newChart();
//        MON_MGR.addEventListener( this, connectionBean.getIp(), connectionBean.getPort(),
//                                  AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() | AgentMessage.EVENT.getCode() );
    
    }


    @Override
    public void onPause () throws Exception {
//        MON_MGR.removeEventListener( this );
        hideToolTip( errorLog );
    }


    @Override
    public void onStop () throws Exception {
        MON_MGR.removeEventListener( this );
        hideToolTip( errorLog );
        removeAll();
    }


    @Override
    public void onDestroy () {
    }

    /////////////////////////////////////////////////////////
    
    @Override
    public void onConnCreate() {
        init();
    }


    @Override
    public void onDataChanged(long timestamp, int msg) {
        boolean firstTime = getLiftStatus()==null;
        motionSubPanel.setEnabled( true );
        
        if (firstTime) {
            initFloorText();
            updateLiftStatusPanel();
            updateMotionChartPanel();
            updateControlPanel();
            updateCallPanel();
            updateLogPanel();
            updateDoorEnable();
        } else {
            if (msg == AgentMessage.STATUS.getCode()) {
                updateLiftStatusPanel();
                updateMotionChartPanel();
                updateControlPanel();
            } else if (msg == AgentMessage.DEPLOYMENT.getCode()
                    || msg == AgentMessage.RUN.getCode()) {
                initFloorText();
                updateLiftStatusPanel();
                if(msg == AgentMessage.DEPLOYMENT.getCode()) 
                	updateCallPanel();
                
                if(msg == AgentMessage.RUN.getCode())
                	updateCallStatus();
                
            } else if (msg == AgentMessage.MCS_CONFIG.getCode()) {
                updateMotionChartPanel();
            } else if (msg == AgentMessage.LOG.getCode()) {
                updateLogPanel();
            } else if (msg == AgentMessage.DOOR_ENABLE.getCode()) {
                updateDoorEnable();
            } else {
                initFloorText();
                updateLiftStatusPanel();
                updateMotionChartPanel();
                updateControlPanel();
                updateLogPanel();
            }
        }
    }


    @Override
    public void onConnLost () {
        setLiftStatus( null );
        setErrorLogs( null );
        setCallPanelData( null, null );
        controlSubPanel.setEnabled( false );
    }

    public final static class ControlPanelData {
        private final boolean chc;
        private final boolean ddo;




        private ControlPanelData ( boolean chc, boolean ddo ) {
            super();
            this.chc = chc;
            this.ddo = ddo;
        }


        public final boolean isChc () {
            return chc;
        }


        public final boolean isDdo () {
            return ddo;
        }
        
    }
    

    protected String getFloorTextByFloor ( int floor ) {
        synchronized ( floorTexts ) {
            return floorTexts.get( floor );
        }
    }
    

    protected Integer getDoorzoneByFloor ( int floor ) {
        synchronized ( doorzones ) {
            return doorzones.get( floor );
        }
    }
    

    private void initFloorText () {
        Map<Integer, String> floorTexts = new TreeMap<>();
        Map<Integer, Integer> doorzones = new TreeMap<>();
        for ( int i = 0, count = deploy.getFloorCount(); i < count; i++ ) {
            int dz = deploy.getDoorZone( ( byte )i );
            if ( dz != - 1 ) {
                floorTexts.put( i, deploy.getFloorText( ( byte )i ) );
                doorzones.put( i, dz );
            }
        }
        synchronized ( this.floorTexts ) {
            this.floorTexts.clear();
            this.floorTexts.putAll( floorTexts );
        }
        synchronized ( this.doorzones ) {
            this.doorzones.clear();
            this.doorzones.putAll( doorzones );
        }
    }

    
    public void init () {
        setLiftStatus( null );
        setErrorLogs( null );
        setCallPanelData( null, null );
        controlSubPanel.setEnabled( false );
        updateDoorEnable();
    }


    private void updateLiftStatusPanel () {
        OcsModule module;
        String uptime = null;
        String position;
        OcsDirection direction;
        boolean animation = false;
        DoorAction frontdoorAction;
        DoorAction reardoorAction;

        // get floor position
        position = getFloorTextByFloor( status.getFloor() );

        // get Direction
        direction = status.getDirection();
        animation = status.isDirectionAnimation();

        // get the state of front door
        frontdoorAction = status.getDoorStatus( true );
        
        // get the state of rear door
        reardoorAction = status.getDoorStatus( false );
        
        module = status.getOCSModule();

        // Uptime.
        uptime = BaseFactory.getUptime( status.getTime() - mcsconfig.getBootupTime() );
        
        final LiftStatusBean statusBean = new LiftStatusBean( module, uptime, direction, animation, position, frontdoorAction, reardoorAction );
        setLiftStatus( statusBean );
    }
    

    private void updateCallPanel () {
        final Map<Integer, String> floorText = new HashMap<Integer, String>();
        final TreeSet<FloorCallElement> calls = new TreeSet<>();

        for ( int floor = 0; floor < deploy.getFloorCount(); floor++ ) {
            floorText.put( floor, getFloorTextByFloor( floor ) );
            boolean hasDoorZone = getDoorzoneByFloor( floor ) != null && (0 <= getDoorzoneByFloor(floor) && getDoorzoneByFloor(floor) < 128);
            boolean isFrontCarPresent = run.IsCallDef((short) floor, CallDef.FRONT_CAR);
            boolean isRearCarPresent  = run.IsCallDef((short) floor, CallDef.REAR_CAR);
            boolean isFrontUpPresent  = run.IsCallDef((short) floor, CallDef.FRONT_HALL_UP);
            boolean isFrontDownPresent  = run.IsCallDef((short) floor, CallDef.FRONT_HALL_DOWN);
            boolean isRearUpPresent  = run.IsCallDef((short) floor, CallDef.FRONT_HALL_UP);
            boolean isRearDownPresent  = run.IsCallDef((short) floor, CallDef.FRONT_HALL_DOWN);
            boolean isDisCarPresent  = run.IsDisCallDef((short) floor, DisCallDef.DISABLE_CAR);
            boolean isDisFrontUpPresent  = run.IsDisCallDef((short) floor, DisCallDef.DISABLE_FRONT_HALL_UP);
            boolean isDisFrontDownPresent  = run.IsDisCallDef((short) floor, DisCallDef.DISABLE_FRONT_HALL_DOWN);
            boolean isDisRearUpPresent  = run.IsDisCallDef((short) floor, DisCallDef.DISABLE_REAR_HALL_UP);
            boolean isDisRearDownPresent  = run.IsDisCallDef((short) floor, DisCallDef.DISABLE_REAR_HALL_DOWN);
            
            if ( hasDoorZone )
                calls.add( new FloorCallElement( floor, isFrontCarPresent, isRearCarPresent, isDisCarPresent, isFrontUpPresent, isFrontDownPresent,
                isRearUpPresent, isRearDownPresent, isDisFrontUpPresent, isDisFrontDownPresent, isDisRearUpPresent, isDisRearDownPresent) );
        }

        setCallPanelData( floorText, calls );
    }
    
    private void updateCallStatus () {
        final TreeSet<FloorCallElement> calls = new TreeSet<>();
        for ( int floor = 0; floor < deploy.getFloorCount(); floor++ ) {
            boolean hasDoorZone = getDoorzoneByFloor( floor ) != null && (0 <= getDoorzoneByFloor(floor) && getDoorzoneByFloor(floor) < 128);
            boolean isFrontCarPresent = run.IsCallDef((short) floor, CallDef.FRONT_CAR);
            boolean isRearCarPresent  = run.IsCallDef((short) floor, CallDef.REAR_CAR);
            boolean isFrontUpPresent  = run.IsCallDef((short) floor, CallDef.FRONT_HALL_UP);
            boolean isFrontDownPresent  = run.IsCallDef((short) floor, CallDef.FRONT_HALL_DOWN);
            boolean isRearUpPresent  = run.IsCallDef((short) floor, CallDef.REAR_HALL_UP);
            boolean isRearDownPresent  = run.IsCallDef((short) floor, CallDef.REAR_HALL_DOWN);
            boolean isDisCarPresent  = run.IsDisCallDef((short) floor, DisCallDef.DISABLE_CAR);
            boolean isDisFrontUpPresent  = run.IsDisCallDef((short) floor, DisCallDef.DISABLE_FRONT_HALL_UP);
            boolean isDisFrontDownPresent  = run.IsDisCallDef((short) floor, DisCallDef.DISABLE_FRONT_HALL_DOWN);
            boolean isDisRearUpPresent  = run.IsDisCallDef((short) floor, DisCallDef.DISABLE_REAR_HALL_UP);
            boolean isDisRearDownPresent  = run.IsDisCallDef((short) floor, DisCallDef.DISABLE_REAR_HALL_DOWN);
            
            if ( hasDoorZone )
                calls.add( new FloorCallElement( floor, isFrontCarPresent, isRearCarPresent, isDisCarPresent, isFrontUpPresent, isFrontDownPresent,
                isRearUpPresent, isRearDownPresent, isDisFrontUpPresent, isDisFrontDownPresent, isDisRearUpPresent, isDisRearDownPresent) );
        }

        setCallStatusData( calls );
    }
    
/*----------------------------------------------------------------------------------------*/
    /**type
		1 : Only front door.
		2 : only rear door.
		3 : both.	
    */
    private void updateDoorEnable() {
    	int doorEnableType = 0;	
    	byte door[] = doorEnable.getDoor_Enable_table();
    	for(int i = 0; i < deploy.getFloorCount(); i ++) {
    		if(door[i] == 0x03) {
    			doorEnableType = 3;
    			break;
    		}else if(door[i] == 0x01) {
    			if(doorEnableType == 0 || doorEnableType == 1) {
    				doorEnableType = 1;
    			}else {
    				doorEnableType = 3;
    				break;
    			}
    		}else if(door[i] == 0x02) {
    			if(doorEnableType == 0 || doorEnableType == 2 ) {
    				doorEnableType = 2;
    			}else {
    				doorEnableType = 3;
    				break;
    			}
    		}
    	}
    	if(doorEnableType == 1) {
    		SwingUtilities.invokeLater( new Runnable() {
        		@Override
				public void run() {
					// TODO Auto-generated method stub
        			lblFrontDoorStatus.setVisible(true);
            		lblRearDoorStatus.setVisible(false);
	        		StartUI.setStyle(layout, styles, lblFrontDoorStatus, "DoorStatus");
				}
    		});
    	}else if(doorEnableType == 2) {
    		SwingUtilities.invokeLater( new Runnable() {
    			@Override
				public void run() {
					// TODO Auto-generated method stub
	    			lblFrontDoorStatus.setVisible(false);
	        		lblRearDoorStatus.setVisible(true);
	        		StartUI.setStyle(layout, styles, lblRearDoorStatus, "DoorStatus");
				}
    		});
    	}else{
    		SwingUtilities.invokeLater( new Runnable() {
        		@Override
				public void run() {
					// TODO Auto-generated method stub
        			lblFrontDoorStatus.setVisible(true);
            		lblRearDoorStatus.setVisible(true);
	        		StartUI.setStyle(layout, styles, lblFrontDoorStatus, "lblFrontDoorStatus");
	        		StartUI.setStyle(layout, styles, lblRearDoorStatus, "lblRearDoorStatus");
				}
    		});
    	}
    }

    private void updateMotionChartPanel () {
    	float temp_speed = 0;
        final float mmratio = mcsconfig.getMMRatio();
        final float contractSpeed = mcsconfig.getContractSpeed();
        final float highestFloor = mcsconfig.getUpper( ( byte )( deploy.getFloorCount() - 1 ) ) * mmratio;
        final long chartTime = status.getTime();
        final float chartPosition = status.getPositionCount() * mmratio;

        if( speed_index >= speed_avg.length ) {
        	for( int i = 1; i < speed_avg.length; i++ ) {
        		speed_avg[ i - 1 ] =  speed_avg[ i ];       		
        	}
        }else
        	speed_index++;
        
        speed_avg[ speed_index - 1 ] = Math.abs( status.getSpeed()  * mmratio);
        
        for( float temp : speed_avg ) {
        	temp_speed += temp;  		
    	}

        final float chartVelocity = temp_speed / speed_index;
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                setChartRangeBound( highestFloor, contractSpeed );
                addLastestChartData( chartTime, chartPosition, chartVelocity );
            }

        } );
    }


    private void updateControlPanel () {
        boolean chc = status.isCHC();
        boolean ddo = status.isDDO();
        final ControlPanelData cpData = new ControlPanelData( chc, ddo );

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run () {
                setControlPanelData( cpData );
            }

        } );
    }
    
    
    private final List<ErrorLog> logs = new ArrayList<>();
    
    
    public void setErrorLogs ( List<ErrorLog> logs ) {
        List<Integer> codeLogPanel = new ArrayList<>();
        List<Integer> codeNewLog = new ArrayList<>();
        
        if (logs==null)
            logs = new ArrayList<>();

        for ( ErrorLog l : this.logs )
            codeLogPanel.add( ( int )l.rawLog.errcode );
        for ( ErrorLog l : logs )
            codeNewLog.add( ( int )l.rawLog.errcode );

        if ( ! codeLogPanel.equals( codeNewLog ) ) {
            clearErrorLog();
            for ( ErrorLog l : logs )
                addErrorLog( l );
            this.logs.clear();
            this.logs.addAll( logs );
        }
    }


    private void updateLogPanel() {
        List<ErrorLog> logs = new ArrayList<>();
        for ( int i = log.getCount() - 1; i >= 0 && logs.size() < 20; i-- ) {
            Log rawLog = log.getLog( i );
        	
            switch ( rawLog.type ) {
	            case 'O': {
	                OCSErrorCode l = OCSErrorCode.get( rawLog.errcode );
	                if ( l != null && l.getLevel() == Level.CRITICAL )
	                    logs.add( new ErrorLog( log.getCount() - i - 1, rawLog ) );
	                break;
	            }
	            case 'M': {
	                MCSErrorCode l = MCSErrorCode.get( rawLog.errcode );
	                if ( l != null && (l.getLevel() == Level.CRITICAL || l.getLevel() == Level.WARNING ) )
	                    logs.add( new ErrorLog( log.getCount() - i - 1, rawLog ) );
	                break;
	            }
            }
        }

        setErrorLogs( logs );
    }

}
