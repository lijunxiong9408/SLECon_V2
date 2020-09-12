package slecon.inspect.calls;
import static logic.util.SiteManagement.MON_MGR;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.PageTreeExpression;
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
import slecon.inspect.calls.LiftStatusView.LiftStatusBean;
import slecon.interfaces.InspectView;
import slecon.interfaces.Page;
import comm.Parser_Deploy;
import comm.Parser_DoorEnable;
import comm.Parser_Misc;
import comm.Parser_Run;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.constants.CallDef;
import comm.constants.DisCallDef;
import comm.constants.DoorAction;
import comm.constants.OcsDirection;
import comm.event.LiftDataChangedListener;


@InspectView(
    path      = "Calls",
    sortIndex = 0x700
)
public class Main extends JPanel implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = -552013171218549161L;
    static final ResourceBundle TEXT = ToolBox.getResourceBundle("inspect.Call");
    private ImageIcon 	BTN_IMAGE		= 	ImageFactory.BUTTON.icon();
    private LiftStatusView    liftView;
    private AllCallPanel	  allCallPanel;
    final LiftConnectionBean  connBean;
    private Parser_Run    	  run;
    private Parser_Misc   	  misc;
    private Parser_Status 	  status;
    private Parser_Deploy     deploy;
    private Parser_DoorEnable doorEnable;
    static final PageTreeExpression writeInspectExpression = new PageTreeExpression("write_inspect");
    
    private JPanel		  	  panelNavigation;
    private JPanel		  	  panelMain;
    private JPanel		  	  panelLiftSelector;
    private PosButton		  btnFloorText;
    private PosButton		  btnCarCall;
    private PosButton		  btnHallFrontCall;
    private PosButton		  btnHallRearCall;
    private PosButton		  btnHallFrontDisabledCall;
    private PosButton		  btnHallRearDisabledCall;
    private JSeparator		  separator;
    private MigLayout         layout;
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("panelLiftSelector", "30 20 250 60 c");
    	styles.put("btnFloorText", "20 80 50 20 c");
    	styles.put("btnCarCall", "75 80 120 20 c");
    	styles.put("btnHallFrontCall", "200 80 120 20 c");
    	styles.put("btnHallRearCall", "325 80 120 20 c");
    	styles.put("btnHallFrontDisabledCall", "450 80 120 20 c");
    	styles.put("btnHallRearDisabledCall", "575 80 120 20 c");
    	styles.put("separator", "20 105 960 2 c");
    	styles.put("allCallPanel", "15 120 720 450 c");
    	styles.put("liftView", "750 120 250 220 c");
    }
    
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
            } else if ("A01".equalsIgnoreCase(ver.getMcsBoardVersion()) || "D01".equalsIgnoreCase(ver.getMcsBoardVersion()) ) {
            	ret = 5;
            }
        } else {
            ToolBox.showErrorMessage( Dict.lookup( "ConnectionLost" ) );
        }
        return ret;
    }
    
    private String[] navigationText = {Dict.lookup("Inspect"), Dict.lookup("Calls")};
    ///////////////////// interface <Page> /////////////////////
    public Main ( LiftConnectionBean bean) {
    	this.connBean = bean;
    	setLayout(new BorderLayout());
        initGUI();
        
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
    
    /*------------------------------------------------------------------------------------------------------------*/  
        btnFloorText = new PosButton(TEXT.getString("Floor.text"), BTN_IMAGE);
        panelMain.add(btnFloorText);
        StartUI.setStyle(layout, styles, btnFloorText, "btnFloorText");
        
        btnCarCall = new PosButton(TEXT.getString("CarCall.text"), BTN_IMAGE);
        panelMain.add(btnCarCall);
        StartUI.setStyle(layout, styles, btnCarCall, "btnCarCall");
        
        btnHallFrontCall = new PosButton(TEXT.getString("HallFrontCall.text"), BTN_IMAGE);
        panelMain.add(btnHallFrontCall);
        StartUI.setStyle(layout, styles, btnHallFrontCall, "btnHallFrontCall");
        
        btnHallRearCall = new PosButton(TEXT.getString("HallRearCall.text"), BTN_IMAGE);
        panelMain.add(btnHallRearCall);
        StartUI.setStyle(layout, styles, btnHallRearCall, "btnHallRearCall");
        
        btnHallFrontDisabledCall = new PosButton(TEXT.getString("HallFrontDisabledCall.text"), BTN_IMAGE);
        panelMain.add(btnHallFrontDisabledCall);
        StartUI.setStyle(layout, styles, btnHallFrontDisabledCall, "btnHallFrontDisabledCall");
        
        btnHallRearDisabledCall = new PosButton(TEXT.getString("HallRearDisabledCall.text"), BTN_IMAGE);
        panelMain.add(btnHallRearDisabledCall);
        StartUI.setStyle(layout, styles, btnHallRearDisabledCall, "btnHallRearDisabledCall");
        
        separator = new JSeparator();
        separator.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        panelMain.add(separator);
        StartUI.setStyle(layout, styles, separator, "separator");	
        
        allCallPanel = new AllCallPanel(this, getBoardVersion(connBean) );
        panelMain.add(allCallPanel);
        StartUI.setStyle(layout, styles, allCallPanel, "allCallPanel");
        
        liftView = new LiftStatusView(this.connBean);
        panelMain.add(liftView);
        StartUI.setStyle(layout, styles, liftView, "liftView");
         
         
        
        
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
        
        revalidate();
        repaint();
    }
    
    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }

    @Override
    public void onStart() throws Exception {
    	deploy = new Parser_Deploy(connBean.getIp(), connBean.getPort());
        run = new Parser_Run(connBean.getIp(), connBean.getPort());
        misc = new Parser_Misc(connBean.getIp(), connBean.getPort());
        status = new Parser_Status(connBean.getIp(), connBean.getPort());
        doorEnable = new Parser_DoorEnable(connBean.getIp(), connBean.getPort());
        
        MON_MGR.addEventListener(this, connBean.getIp(), connBean.getPort(),
                AgentMessage.DEPLOYMENT.getCode() | 
                AgentMessage.STATUS.getCode() | 
                AgentMessage.RUN.getCode() | 
                AgentMessage.ERROR.getCode() | 
                AgentMessage.DOOR_ENABLE.getCode());
        
        setCallPanelHot();
        setCallStatusHot();
        setLiftViewHot();
        setLiftDoorView();
    }


    @Override
    public void onResume () throws Exception {}


    @Override
    public void onPause () throws Exception {}


    @Override
    public void onStop () throws Exception {
    	removeAll();
        MON_MGR.removeEventListener(this);
    }

    @Override
    public void onDestroy () {}

    @Override
    public void onConnCreate() {
    }
    
    @Override
    public void onConnLost() {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void onDataChanged(long timestamp, int msg) {
    	if(msg == AgentMessage.DEPLOYMENT.getCode()) {
			setCallPanelHot();
    	}
    	
    	if(msg == AgentMessage.RUN.getCode()) {
    		setCallStatusHot();
    	}
    	
    	if(msg == AgentMessage.STATUS.getCode()) {
    		setLiftViewHot();
    	}
    	
    	if(msg == AgentMessage.DOOR_ENABLE.getCode()) {
    		setLiftDoorView();
    	}
    	
    }

    private final Map<Integer, String> floorTexts = new TreeMap<>();
    private final Map<Integer, Integer> doorzones = new TreeMap<>();

    
    protected String getFloorTextByFloor ( int floor ) {
        synchronized (floorTexts) {
            return floorTexts.get(floor);
        }
    }
    
    protected Integer getDoorzoneByFloor ( int floor ) {
        synchronized (doorzones) {
            return doorzones.get(floor);
        }
    }
    
    private void initFloorText () {
        Map<Integer, String> floorTexts = new TreeMap<>();
        Map<Integer, Integer> doorzones = new TreeMap<>();
        for ( int i = 0, count = deploy.getFloorCount() ; i < count ; i++ ) {
            int dz = deploy.getDoorZone( (byte) i );
            if ( dz != -1 ) {
                floorTexts.put( i, deploy.getFloorText( (byte) i ) );
                doorzones.put( i, dz );
            }
        }
        synchronized (this.floorTexts) {
            this.floorTexts.clear();
            this.floorTexts.putAll(floorTexts);
        }
        synchronized (this.doorzones) {
            this.doorzones.clear();
            this.doorzones.putAll(doorzones);
        }
    }
    
    private void setCallPanelHot() {
        initFloorText ();
         
        final HashMap<Integer, String>  floorText = new HashMap<Integer, String>();
        
        final TreeSet<FloorCallElement> calls     = new TreeSet<>();
        for ( int floor = 0 ; floor < deploy.getFloorCount() ; floor++ ) {
            floorText.put( floor, getFloorTextByFloor( (byte) floor ) );
            
            boolean hasDoorZone  = getDoorzoneByFloor(floor) != null && (0 <= getDoorzoneByFloor(floor) && getDoorzoneByFloor(floor) < 128);
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

        allCallPanel.setData(floorText, calls);
    }
    
    private void setCallStatusHot() {
        final TreeSet<FloorCallElement> calls     = new TreeSet<>();
        for ( int floor = 0 ; floor < deploy.getFloorCount() ; floor++ ) {
            boolean hasDoorZone  = getDoorzoneByFloor(floor) != null && (0 <= getDoorzoneByFloor(floor) && getDoorzoneByFloor(floor) < 128);

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

        allCallPanel.setCallStatus(calls, getBoardVersion(connBean));
    }
    
    private void setLiftViewHot() {
    	/* status */
        String                        text       = getFloorTextByFloor(status.getFloor());
        DoorAction frontdoorAction = status.getDoorStatus(true);
        DoorAction reardoorAction = status.getDoorStatus(false);
        OcsDirection direction = status.getDirection();
        boolean animation = status.isDirectionAnimation();
        final LiftStatusBean statusBean = new LiftStatusBean(frontdoorAction, reardoorAction, text, direction, animation);
        synchronized(liftView) {
        	liftView.setData(statusBean);
        }
    }
    
    private void setLiftDoorView() {
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
    	synchronized(liftView) {
        	liftView.setDoorView(doorEnableType);
        }
    }
    
    public void addCall(CallDef call, int floor) {
        misc.call(call, (byte) floor);
    }
    
    public void addDisabledCall(DisCallDef call, int floor) {
        misc.disabledcall(call, (byte) floor);
    }
}
