package slecon.inspect.motion;
import static logic.util.SiteManagement.MON_MGR;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.SiteManagement;
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
import comm.Parser_Deploy;
import comm.Parser_McsConfig;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.event.LiftDataChangedListener;


@InspectView(
    path      = "MotionStatus",
    sortIndex = 0x100
)
public class MotionMain extends JPanel implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = -552013171218549161L;
    static final ResourceBundle TEXT = ToolBox.getResourceBundle("inspect.Motion");
    private ImageIcon 	BUTTON_PAUSE = ImageFactory.BUTTON_PAUSE.icon(87,30);
    private final LiftConnectionBean connBean;
    private JPanel		  	  panelNavigation;
    private JPanel		  	  panelMain;
    private JPanel		  	  panelLiftSelector;
    private MotionChartPanel  chartPanel;
    private JLabel            lblPositionval;
    private JLabel            lblSpeedval;
    private JLabel            lblAccval;
    private JLabel            lblJerkval;
    private JLabel            lblDestval;
    private JLabel            lblBrakeval;
    private PosButton         btnStart;
    private MigLayout         layout;
    
    private Parser_McsConfig  mcsconfig;
    private Parser_Status     status;
    private Parser_Deploy     deploy;
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("panelLiftSelector", "60 20 250 60 c");
    	styles.put("chartPanel", "30 80 950 280 c");
    	styles.put("btnStart", "850 360 87 30 c");
    	styles.put("lblRealtimeStatus", "60 380 120 25 l");
    	styles.put("panelRealtimeStatus", "60 410 300 180 l");
    }
    
    private String[] navigationText = {Dict.lookup("Inspect"), Dict.lookup("MotionStatus")};
    ///////////////////// interface <Page> /////////////////////
    public MotionMain ( LiftConnectionBean bean ) {
    	setLayout(new BorderLayout());
        initGUI();
        this.connBean = bean;
        btnStart.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                if ( chartPanel.isRunning() )
                    chartPanel.stop();
                else
                    chartPanel.start();
                updateBtnText();
            }
        } );
        btnStart.doClick();
    }


    private void updateBtnText () {
        if ( chartPanel.isRunning() ) {
            btnStart.setText( TEXT.getString("Button.stop") );
        } else {
            btnStart.setText( TEXT.getString("Button.start") );
        }
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
        layout = new MigLayout( "nogrid, w 985!, h 600!, gap 0", "[left]", "[center]" );
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
        
        chartPanel = new MotionChartPanel();
        chartPanel.setBackground( StartUI.SUB_BACKGROUND_COLOR );
        panelMain.add( chartPanel);
        StartUI.setStyle(layout, styles, chartPanel, "chartPanel");
        
        btnStart = new PosButton( BUTTON_PAUSE );
        panelMain.add( btnStart);
        StartUI.setStyle(layout, styles, btnStart, "btnStart");
        
        JLabel lblRealtimeStatus = new JLabel( TEXT.getString("RealtimeStatus.text") );
        lblRealtimeStatus.setFont( FontFactory.FONT_12_BOLD );
        lblRealtimeStatus.setForeground(Color.WHITE);
        panelMain.add( lblRealtimeStatus );
        StartUI.setStyle(layout, styles, lblRealtimeStatus, "lblRealtimeStatus");
        
        JPanel panelRealtimeStatus = new JPanel();
        panelRealtimeStatus.setLayout(new MigLayout("fill","[][][]","[]"));
        panelRealtimeStatus.setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
        panelRealtimeStatus.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelMain.add( panelRealtimeStatus);
        StartUI.setStyle(layout, styles, panelRealtimeStatus, "panelRealtimeStatus");
        
        //Realtime Status.
        JLabel lblPosition = new JLabel( TEXT.getString("Position.text") );
        lblPositionval = new JLabel( "--" );
        JLabel lblMm = new JLabel( TEXT.getString("Unit.mm") );
        
        JLabel lblSpeed = new JLabel( TEXT.getString("Speed.text") );
        lblSpeedval = new JLabel( "--" );
        JLabel lblMms = new JLabel( TEXT.getString("Unit.mms") );
        
        JLabel lblAcceleration = new JLabel( TEXT.getString("Acceleration.text") );
        lblAccval = new JLabel( "--" );
        JLabel lblMms_1 = new JLabel( "<html>mm/s<sup>2</sup></html>" );
        
        JLabel lblJerk = new JLabel( TEXT.getString("Jerk.text") );
        lblJerkval = new JLabel( "--" );
        JLabel lblMms_2 = new JLabel( "<html>mm/s<sup>3</sup></html>" );
        
        JLabel lblDestination = new JLabel( TEXT.getString("Destination.text") );
        lblDestval = new JLabel( "--" );
        JLabel lblMm_1 = new JLabel( TEXT.getString("Unit.mm") );
        
        JLabel lblBrakeDisp = new JLabel( TEXT.getString("BrakeDisp.text") );
        lblBrakeval = new JLabel( "--" );
        JLabel lblMm_2 = new JLabel( TEXT.getString("Unit.mm") );
        
        setRealtimeStatusStyle(lblPosition, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblPositionval, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblMm, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblSpeed, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblSpeedval, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblMms, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblAcceleration, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblAccval, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblMms_1, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblJerk, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblJerkval, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblMms_2, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblDestination, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblDestval, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblMm_1, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblBrakeDisp, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblBrakeval, FontFactory.FONT_12_BOLD, Color.WHITE);
        setRealtimeStatusStyle(lblMm_2, FontFactory.FONT_12_BOLD, Color.WHITE);
        
        
        
        Box vboxRealtimeStatusLable = Box.createVerticalBox();
        vboxRealtimeStatusLable.add( lblPosition);
        vboxRealtimeStatusLable.add( Box.createVerticalStrut(10));
        vboxRealtimeStatusLable.add( lblSpeed);
        vboxRealtimeStatusLable.add( Box.createVerticalStrut(10));
        vboxRealtimeStatusLable.add( lblAcceleration);
        vboxRealtimeStatusLable.add( Box.createVerticalStrut(10));
        vboxRealtimeStatusLable.add( lblJerk);
        vboxRealtimeStatusLable.add( Box.createVerticalStrut(10));
        vboxRealtimeStatusLable.add( lblDestination);
        vboxRealtimeStatusLable.add( Box.createVerticalStrut(10));
        vboxRealtimeStatusLable.add( lblBrakeDisp);
        
        Box vboxRealtimeStatusValue = Box.createVerticalBox();
        vboxRealtimeStatusValue.add( lblPositionval);
        vboxRealtimeStatusValue.add( Box.createVerticalStrut(10));
        vboxRealtimeStatusValue.add( lblSpeedval);
        vboxRealtimeStatusValue.add( Box.createVerticalStrut(10));
        vboxRealtimeStatusValue.add( lblAccval);
        vboxRealtimeStatusValue.add( Box.createVerticalStrut(10));
        vboxRealtimeStatusValue.add( lblJerkval);
        vboxRealtimeStatusValue.add( Box.createVerticalStrut(10));
        vboxRealtimeStatusValue.add( lblDestval);
        vboxRealtimeStatusValue.add( Box.createVerticalStrut(10));
        vboxRealtimeStatusValue.add( lblBrakeval);
        
        Box vboxRealtimeStatusUnit = Box.createVerticalBox();
        vboxRealtimeStatusUnit.add( lblMm);
        vboxRealtimeStatusUnit.add( Box.createVerticalStrut(9));
        vboxRealtimeStatusUnit.add( lblMms);
        vboxRealtimeStatusUnit.add( Box.createVerticalStrut(9));
        vboxRealtimeStatusUnit.add( lblMms_1);
        vboxRealtimeStatusUnit.add( Box.createVerticalStrut(9));
        vboxRealtimeStatusUnit.add( lblMms_2);
        vboxRealtimeStatusUnit.add( Box.createVerticalStrut(9));
        vboxRealtimeStatusUnit.add( lblMm_1);
        vboxRealtimeStatusUnit.add( Box.createVerticalStrut(9));
        vboxRealtimeStatusUnit.add( lblMm_2);
        
        panelRealtimeStatus.add(vboxRealtimeStatusLable, "cell 0 0, top, gapright 10" );
        panelRealtimeStatus.add(vboxRealtimeStatusValue, "cell 1 0, top, gapright 10" );
        panelRealtimeStatus.add(vboxRealtimeStatusUnit, "cell 2 0, top, wrap" );
        
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
    
    private void setRealtimeStatusStyle(JComponent c, Font font, Color color ) {
    	if( c instanceof JLabel) {
    		( ( JLabel )c ).setFont(font);
    		( ( JLabel )c ).setForeground(color);
    	}
    }

    public void setRealtimeStatusBean ( RealtimeStatusBean realTimeStatus ) {
        lblPositionval.setText( realTimeStatus.getPositon() );
        lblSpeedval.setText( realTimeStatus.getSpeed() );
        lblAccval.setText( realTimeStatus.getAccelearation() );
        lblJerkval.setText( realTimeStatus.getJerk() );
        lblDestval.setText( realTimeStatus.getDestination() );
        lblBrakeval.setText( realTimeStatus.getBrakeDisplacement() );
    }


    public final void addToMotionChartDataQueue ( long ts, float position, float speed ) {
        chartPanel.addDataQueue( ts, speed, position );
    }


    void setChartRangeBound ( float highestDoorZone, float maxSpeed ) {
        chartPanel.setRangeBound( highestDoorZone, maxSpeed );
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart() throws Exception {
        status = new Parser_Status( connBean.getIp(), connBean.getPort() );
        mcsconfig = new Parser_McsConfig(connBean.getIp(), connBean.getPort());
        deploy = new Parser_Deploy( connBean.getIp(), connBean.getPort() );
        MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                AgentMessage.STATUS.getCode() |
                AgentMessage.MCS_CONFIG.getCode() |
                AgentMessage.RUN.getCode() |
                AgentMessage.DEPLOYMENT.getCode() |
                AgentMessage.ERROR.getCode() );
        setHot();
    }


    @Override
    public void onResume () throws Exception {}


    @Override
    public void onPause () throws Exception {}


    @Override
    public void onStop () throws Exception {
        chartPanel.stop();
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
        /*chart*/
        final float mmratio       = mcsconfig.getMMRatio();
        final float contractSpeed = mcsconfig.getContractSpeed();
        final float highestFloor = mcsconfig.getUpper( ( byte )( deploy.getFloorCount() - 1 ) ) * mmratio;
        final long chartTime = status.getTime();
        final float chartPosition = status.getPositionCount() * mmratio;
        final float chartVelocity = Math.abs( status.getSpeed()  * mmratio);

        /* realtime panel */
        String accelearation = "-";
        String position = "-";
        String speed = "-";
        String jerk = "-";
        String destination = "-";
        String brakeDisplacement = "-";
        if (SiteManagement.isAlive(connBean)) {
            //please check the unit in (mm).
           position = String.format("%.1f", status.getPositionCount() * mmratio);
           speed = String.format("%.1f", status.getSpeed() * mmratio);
           brakeDisplacement = String.format("%.1f", status.getBrakeDisplacement()*mmratio);
        }
        final RealtimeStatusBean realTimeStatus = new RealtimeStatusBean(position, speed, accelearation, jerk, destination, brakeDisplacement);
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                setChartRangeBound(highestFloor, contractSpeed);
                addToMotionChartDataQueue(chartTime, chartPosition, chartVelocity);
                setRealtimeStatusBean(realTimeStatus);
            }
            
        });
    }

}
