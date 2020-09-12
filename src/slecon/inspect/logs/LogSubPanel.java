package slecon.inspect.logs;
import static logic.util.SiteManagement.MON_MGR;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.evlog.ErrorLog;
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
public class LogSubPanel extends JPanel implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = -552013171218549161L;
    static final ResourceBundle TEXT = ToolBox.getResourceBundle("inspect.Motion");
    private Main parent;
    private List<ErrorLog> logs;
    private int index;
    private final LiftConnectionBean connBean;
    private JPanel		  	  panelNavigation;
    private JPanel		  	  panelMain;
    private JPanel		  	  panelLiftSelector;
    private JPanel		  	  panelLogDetail;
    private MigLayout         layout;
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("panelLiftSelector", "30 20 250 60 c");
    	styles.put("panelLogDetail", "20 80 960 650 c");
    }
    
    private String[] navigationText = {Dict.lookup("Inspect"), Dict.lookup("Log"), Dict.lookup("SubLog")};
    ///////////////////// interface <Page> /////////////////////
    public LogSubPanel ( LiftConnectionBean bean, Main parent, List<ErrorLog> logs, int index ) {
    	this.parent = parent;
    	this.logs = logs;
    	this.index = index;
    	setLayout(new BorderLayout());
        initGUI();
        this.connBean = bean;
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
        int log_count = 0;
        for (String text : navigationText) {
        	if(log_count > 0) {
        		PosButton icon = new PosButton(ImageFactory.ARROW_NAVIGATION.icon(11,12));
        		panelNavigation.add(icon);
			}
        	
        	PosButton lab = new PosButton(text, StartUI.BORDER_COLOR, Color.WHITE);
        	lab.setForeground(StartUI.BORDER_COLOR);
        	lab.setFont(FontFactory.FONT_12_BOLD);
        	if(log_count == 0) {
        		lab.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						final InspectPanel panelBinder = InspectPanel.build(connBean, null);
                        StartUI.getTopMain().push(panelBinder);
					}
				});
        	}else if(log_count == 1) {
        		lab.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						final InspectPanel panelBinder = InspectPanel.build(connBean, slecon.inspect.logs.Main.class);
                        StartUI.getTopMain().push(panelBinder);
					}
				});
        	}
			panelNavigation.add(lab);
			log_count += 1;
		}
        
    /*------------------------------------------------------------------------------------------------------------*/        
        layout = new MigLayout( "nogrid, w 1000!, h 750!, gap 0", "[left]", "[center]" );
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
        
        panelLogDetail = new JPanel(new MigLayout("fill","[left]","[top]"));
        panelLogDetail.setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
        panelLogDetail.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelMain.add(panelLogDetail);
        StartUI.setStyle(layout, styles, panelLogDetail, "panelLogDetail");
        panelLogDetail.add(new LogSubDetailPanel(parent, logs, index));
        
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
    }

}
