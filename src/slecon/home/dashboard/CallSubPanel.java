package slecon.home.dashboard;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeSet;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import logic.Dict;
import logic.util.SiteManagement;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.home.PosButton;
import slecon.inspect.calls.FloorCallElement;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import comm.constants.CallDef;
import comm.constants.DisCallDef;




public class CallSubPanel extends JPanel {
    private static final long         serialVersionUID = - 6476687784796491345L;
    static final ResourceBundle TEXT                            = ToolBox.getResourceBundle( "home.LiftStatusView" );
    

    private static final ImageIcon    BGKBUTTON    	   = ImageFactory.BUTTON.icon(150,20);
    
    private MigLayout                  layout;
    static HashMap<String, String>      styles                  = new HashMap<>();
  
    private final PosButton			  front_hall_call;
    private final PosButton			  rear_hall_call;
    private final PosButton			  front_hall_disabled_call;
    private final PosButton			  rear_hall_disabled_call;
    private final PosButton			  car_call;
    private final JPanel              main             = new JPanel();
    private final int				  boardVersion;
    private int				  		  index = 0;
    private HashMap<Integer, ArrayList<JLabel>> CarCall = new HashMap<Integer, ArrayList<JLabel>>();
    private HashMap<Integer, ArrayList<PosButton>> HallCall = new HashMap<Integer, ArrayList<PosButton>>();

    static {
        styles.put( "car_call", "0 0 150 20 c" );
        styles.put( "front_hall_call", "156 0 150 20 c" );
        styles.put( "rear_hall_call", "312 0 150 20 c");
        styles.put( "front_hall_disabled_call", "468 0 150 20 c" );
        styles.put( "rear_hall_disabled_call", "624 0 150 20 c" );
        styles.put( "main", "0 25 800 130 c");
    }
    
    private TreeSet<FloorCallElement> callSet;
    private Map<Integer, String>      floorText;
    private LiftStatusView            liftStatusView;

    private void setCallStyle(int x, int y, int w, int h, JComponent c, ImageIcon icon, MigLayout layout) {
    	c.setPreferredSize(new Dimension( w, h ));
    	layout.setComponentConstraints( c, String.format( "x %d, y %d, w %d!, h %d!", x, y, w, h ));
    	
    	 if ( c instanceof JLabel ) {
    		 ( ( JLabel )c ).setIcon(icon);
    		 ( ( JLabel )c ).setForeground(Color.WHITE);
             ( ( JLabel )c ).setHorizontalTextPosition( SwingConstants.CENTER );
             ( ( JLabel )c ).setHorizontalAlignment( SwingConstants.CENTER );
         } 
    	
    }
    
    public void updateI18nGUI () {
    	front_hall_call.setText( TEXT.getString( "front_hall_call.text" ) );
    	rear_hall_call.setText( TEXT.getString( "rear_hall_call.text" ) );
    	front_hall_disabled_call.setText( TEXT.getString( "front_hall_disabled_call.text" ) );
    	rear_hall_disabled_call.setText( TEXT.getString( "rear_hall_disabled_call.text" ) );
    	car_call.setText( TEXT.getString( "car_call.text" ) );
    }

    /**
     * Create the panel.
     * @param liftStatusView
     */
    public CallSubPanel ( LiftStatusView liftStatusView, int ver ) {
        this.liftStatusView = liftStatusView;
        this.boardVersion = ver;
        layout = new MigLayout( "nogrid, w 800!, h 160!, gap 0", "[0!]", "[0!]" );
        setLayout(layout);
        setOpaque( false );
        {
        	add(car_call = new PosButton(BGKBUTTON));
        	add(front_hall_call = new PosButton(BGKBUTTON));
        	add(rear_hall_call = new PosButton(BGKBUTTON));
        	add(front_hall_disabled_call = new PosButton(BGKBUTTON));
        	add(rear_hall_disabled_call = new PosButton(BGKBUTTON));
        	add(main);
        	
        	StartUI.setStyle(layout, styles, front_hall_call,"front_hall_call");
        	StartUI.setStyle(layout, styles, rear_hall_call,"rear_hall_call");
        	StartUI.setStyle(layout, styles, front_hall_disabled_call,"front_hall_disabled_call");
        	StartUI.setStyle(layout, styles, rear_hall_disabled_call,"rear_hall_disabled_call");
        	StartUI.setStyle(layout, styles, car_call,"car_call");
        	StartUI.setStyle(layout, styles, main,"main");
        	
        	front_hall_call.setFont(FontFactory.FONT_12_PLAIN);
        	rear_hall_call.setFont(FontFactory.FONT_12_PLAIN);
        	front_hall_disabled_call.setFont(FontFactory.FONT_12_PLAIN);
        	rear_hall_disabled_call.setFont(FontFactory.FONT_12_PLAIN);
        	car_call.setFont(FontFactory.FONT_12_PLAIN);
        }
        
        updateI18nGUI();
    }
    
    private void resetGUI () {
        main.setBackground( StartUI.SUB_BACKGROUND_COLOR );
        main.removeAll();
        JPanel control = new JPanel();
        control.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        TreeSet<FloorCallElement> callSet = getCallSet();
        if ( callSet != null && floorText != null && ! callSet.isEmpty() && ! floorText.isEmpty() ) {
	        MigLayout layout = new MigLayout("nogrid, w 800!, h "+(16 * (callSet.size() + 2))+"!, gap 0", "[0!]", "[0!]" );
	        control.setLayout( layout );
	        control.invalidate();
	        int row = 0;
	        int x = 156, h = 15;
	        for(final FloorCallElement call : callSet) {
	        	String floortext = floorText.get( call.getFloor() );
	        	for(int i = 0; i < 5; i++) {
	        		index = i;
	        		if(i > 0) {
	        			final JLabel Lab_text = new JLabel(floortext);
		        		final PosButton Btn_up = new PosButton(ImageFactory.BUTTON_CALL_UP.icon(60,15));
		        		final PosButton Btn_down = new PosButton(ImageFactory.BUTTON_CALL_DOWN.icon(60,15));
		        		
		        		ArrayList<PosButton> btn_list = new ArrayList<PosButton>();
		        		btn_list.add(Btn_up);
		        		btn_list.add(Btn_down);
		        		HallCall.put((i << 8 | call.getFloor()), btn_list);
		        		
		        		control.add(Lab_text);
		        		control.add(HallCall.get((i<< 8 | call.getFloor())).get(0));
		        		control.add(HallCall.get((i<< 8 | call.getFloor())).get(1));
		        		
		        		setCallStyle(x * i, (h + 1) * row, 28, h, Lab_text, ImageFactory.SUBCALLTEXT.icon(28,15), layout);
		        		setCallStyle(x * i + 30, (h + 1) * row, 60, h, Btn_up, null, layout);
		        		setCallStyle(x * i + 90, (h + 1) * row, 60, h, Btn_down, null, layout);
		        		
		        		if(boardVersion < 5) {
		        			MouseListener hall_call = new MouseAdapter() {
		        				public void mousePressed(MouseEvent e) {
		        					CallDef call_type = null;
        							if(e.getComponent() == Btn_up) {
        								call_type = CallDef.FRONT_HALL_UP;
        							}else if(e.getComponent() == Btn_down) {
        								call_type = CallDef.FRONT_HALL_DOWN;	
        							}
        							liftStatusView.misc.call(call_type, ( byte )call.getFloor());
		        				}
		        			};
		        			Btn_up.addMouseListener(hall_call);
			        		Btn_down.addMouseListener(hall_call);
		        		}else if(boardVersion == 5) {
			        		switch(index) {
								case 1 :{
									MouseListener hall_call = new MouseAdapter() {
										@Override
										public void mousePressed(MouseEvent e) {
											CallDef call_type = null;
											if(e.getComponent() == Btn_up) {
												call_type = CallDef.FRONT_HALL_UP;
											}else if(e.getComponent() == Btn_down) {
												call_type = CallDef.FRONT_HALL_DOWN;	
											}
											liftStatusView.misc.call(call_type, ( byte )call.getFloor());
										}
									};
									Btn_up.addMouseListener(hall_call);
					        		Btn_down.addMouseListener(hall_call);
								}break;
								
								case 2 :{
									MouseListener hall_call = new MouseAdapter() {
										@Override
										public void mousePressed(MouseEvent e) {
											CallDef call_type = null;
											if(e.getComponent() == Btn_up) {
												call_type = CallDef.REAR_HALL_UP;
											}else if(e.getComponent() == Btn_down) {
												call_type = CallDef.REAR_HALL_DOWN;	
											}
											liftStatusView.misc.call(call_type, ( byte )call.getFloor());
										}
									};
									Btn_up.addMouseListener(hall_call);
					        		Btn_down.addMouseListener(hall_call);
								}break;
								
								case 3 :{
									MouseListener hall_call = new MouseAdapter() {
										@Override
										public void mousePressed(MouseEvent e) {
											DisCallDef call_type = null;
											if(e.getComponent() == Btn_up) {
												call_type = DisCallDef.DISABLE_FRONT_HALL_UP;
											}else if(e.getComponent() == Btn_down) {
												call_type = DisCallDef.DISABLE_FRONT_HALL_DOWN;	
											}
											liftStatusView.misc.disabledcall(call_type, ( byte )call.getFloor());
										}
									};
									Btn_up.addMouseListener(hall_call);
					        		Btn_down.addMouseListener(hall_call);
								}break;
								
								case 4 :{
									MouseListener hall_call = new MouseAdapter() {
										@Override
										public void mousePressed(MouseEvent e) {
											DisCallDef call_type = null;
											if(e.getComponent() == Btn_up) {
												call_type = DisCallDef.DISABLE_REAR_HALL_UP;
											}else if(e.getComponent() == Btn_down) {
												call_type = DisCallDef.DISABLE_REAR_HALL_DOWN;
											}
											liftStatusView.misc.disabledcall(call_type, ( byte )call.getFloor());
										}
									};
									Btn_up.addMouseListener(hall_call);
					        		Btn_down.addMouseListener(hall_call);
								}break;
							}
		        		}
	        		}else if(i == 0) {
	        			final JLabel Lab_text = new JLabel(floortext);
		        		final JLabel Lab_front = new JLabel();
		        		final JLabel Lab_rear = new JLabel();
		        		final JLabel Lab_disabled = new JLabel();
		        		
		        		ArrayList<JLabel> lab_list = new ArrayList<JLabel>();
		        		lab_list.add(Lab_front);
		        		lab_list.add(Lab_rear);
		        		lab_list.add(Lab_disabled);
		        		CarCall.put(call.getFloor(), lab_list);
		        		
		        		control.add(Lab_text);
		        		control.add(CarCall.get(call.getFloor()).get(0));
		        		control.add(CarCall.get(call.getFloor()).get(1));
		        		control.add(CarCall.get(call.getFloor()).get(2));
		        		
		        		ImageIcon Icon = ImageFactory.BUTTON_CALL_OFF.icon(29,15);
		        		if(row == 0) {
		        			final JLabel Lab_front_title = new JLabel("F");
			        		final JLabel Lab_rear_title = new JLabel("R");
			        		final JLabel Lab_disabled_title = new JLabel("Dis");
			        		control.add(Lab_front_title);
			        		control.add(Lab_rear_title);
			        		control.add(Lab_disabled_title);
			        		
		        			setCallStyle(30, 0, 40, h, Lab_front_title, ImageFactory.SUBCALLTEXT.icon(39,15), layout);
			        		setCallStyle(70, 0, 40, h, Lab_rear_title, ImageFactory.SUBCALLTEXT.icon(39,15), layout);
			        		setCallStyle(110, 0, 40, h, Lab_disabled_title, ImageFactory.SUBCALLTEXT.icon(39,15), layout);
			        		setCallStyle(0, (h + 1) * (row + 1), 28, h, Lab_text, ImageFactory.SUBCALLTEXT.icon(28,15), layout);
			        		setCallStyle(30, (h + 1) * (row + 1), 40, h, Lab_front, Icon, layout);
			        		setCallStyle(70, (h + 1) * (row + 1), 40, h, Lab_rear, Icon, layout);
			        		setCallStyle(110, (h + 1) * (row + 1), 40, h, Lab_disabled, Icon, layout);
		        			
		        		}else {
			        		setCallStyle(0, (h + 1) * (row + 1), 28, h, Lab_text, ImageFactory.SUBCALLTEXT.icon(28,15), layout);
			        		setCallStyle(30, (h + 1) * (row + 1), 40, h, Lab_front, Icon, layout);
			        		setCallStyle(70, (h + 1) * (row + 1), 40, h, Lab_rear, Icon, layout);
			        		setCallStyle(110, (h + 1) * (row + 1), 40, h, Lab_disabled, Icon, layout);
		        		}
		        		MouseListener car_call = new MouseAdapter() {
		        			
							@Override
							public void mouseEntered(MouseEvent e) {
								// TODO Auto-generated method stub
								setCursor(Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ));
							}

							@Override
							public void mousePressed(MouseEvent e) {
								/* Permission */
		                        if ( ! ToolBox.requestRole( liftStatusView.getConnectionBean(), LiftStatusView.WRITE_HOME_EXPRESSION ) ) {
		                            ToolBox.showErrorMessage( Dict.lookup( "NoPermission" ) );
		                            return;
		                        }
		                        
								if ( SiteManagement.isAlive( liftStatusView.getConnectionBean() ) ) {
									if(boardVersion < 5) {
										CallDef call_type = null;
										if(e.getComponent() == Lab_front) {
											call_type = CallDef.FRONT_CAR;
										}else if(e.getComponent() == Lab_rear) {
											call_type = CallDef.FRONT_CAR;
										}else if(e.getComponent() == Lab_disabled) {
											call_type = CallDef.FRONT_CAR;	
										}
										liftStatusView.misc.call( call_type, ( byte )call.getFloor() );
										
									}else if(boardVersion == 5) {
										if(e.getComponent() == Lab_front) {
											CallDef call_type = CallDef.FRONT_CAR;
											liftStatusView.misc.call( call_type, ( byte )call.getFloor() );
										}else if(e.getComponent() == Lab_rear) {
											CallDef call_type = CallDef.REAR_CAR;
											liftStatusView.misc.call( call_type, ( byte )call.getFloor() );
										}else if(e.getComponent() == Lab_disabled) {
											DisCallDef call_type = DisCallDef.DISABLE_CAR;
											liftStatusView.misc.disabledcall(call_type, ( byte )call.getFloor());
										}
									}
									/*
									CallDef call_type = null;
									if(e.getComponent() == Lab_front) {
										call_type = CallDef.FRONT_CAR;
									}else if(e.getComponent() == Lab_rear) {
										//call_type = CallDef.REAR_CAR;
										call_type = CallDef.FRONT_CAR;
									}else if(e.getComponent() == Lab_disabled) {
										call_type = CallDef.FRONT_CAR;	
									}
									liftStatusView.misc.call( call_type, ( byte )call.getFloor() );
									*/
								}
							}
		        		};
	        			Lab_front.addMouseListener(car_call);
	        			Lab_rear.addMouseListener(car_call);
	        			Lab_disabled.addMouseListener(car_call);
	        		}
	        	}
	        	row += 1;
	        }
        }
        
        JScrollPane CallScrollPanel = new JScrollPane( control, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER ) {
		private static final long serialVersionUID = 5179777323784811105L;
		{
			this.getViewport().setOpaque( false );
			this.setViewportBorder( null );
			this.setOpaque( false );
			this.setBorder( null );
			this.setPreferredSize(new Dimension(800,120));
			this.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
		}};
	        
		main.add(CallScrollPanel);
        main.revalidate();
        main.repaint();
    }
    
    
    public void paint ( Graphics g ) {
        super.paint( g );
        if ( ! ( callSet != null && floorText != null && ! callSet.isEmpty() && ! floorText.isEmpty() ) ) {
            Font f = g.getFont();
            Color c = g.getColor();
            g.setColor( Color.GRAY );
            g.setFont( FontFactory.FONT_20_PLAIN );

            String str = Dict.lookup( "Empty" );
            final FontMetrics fontMetrics = g.getFontMetrics();
            Rectangle2D stringBounds = fontMetrics.getStringBounds( str, g );
            int stringW = ( int )stringBounds.getWidth();
            int stringH = ( int )stringBounds.getHeight();

            g.drawString( str, ( getWidth() - stringW ) / 2, ( getHeight() - stringH ) / 2 + fontMetrics.getAscent() );

            g.setFont( f );
            g.setColor( c );
        }
    }


    public TreeSet<FloorCallElement> getCallSet () {
        return callSet;
    }


    public Map<Integer, String> getFloorText () {
        return floorText;
    }


    public void setData ( Map<Integer, String> floorText, TreeSet<FloorCallElement> callSet ) {
        this.callSet   = callSet;
        this.floorText = floorText;
        this.resetGUI();
    }
    
    public void setCallStatus(TreeSet<FloorCallElement> callSet, int ver) {
    	for(final FloorCallElement call : callSet) {
    		ImageIcon Front_Car_Icon = null;
    		ImageIcon Rear_Car_Icon = null;
    		ImageIcon Disabled_Car_Icon = null;
    		ImageIcon Front_Hall_Up_Icon = null;
    		ImageIcon Front_Hall_Down_Icon = null;
    		ImageIcon Rear_Hall_Up_Icon = null;
    		ImageIcon Rear_Hall_Down_Icon = null;
    		ImageIcon Dis_Front_Hall_Up_Icon = null;
    		ImageIcon Dis_Front_Hall_Down_Icon = null;
    		ImageIcon Dis_Rear_Hall_Up_Icon = null;
    		ImageIcon Dis_Rear_Hall_Down_Icon = null;
    		
    		
    		if(call.isFrontCarPresent()) {
    			Front_Car_Icon = ImageFactory.BUTTON_CALL_ON.icon(29,15);
    		}else {
    			Front_Car_Icon = ImageFactory.BUTTON_CALL_OFF.icon(29,15);
    		}
    		
    		if(call.isRearCarPresent()) {
    			Rear_Car_Icon = ImageFactory.BUTTON_CALL_ON.icon(29,15);
    		}else {
    			Rear_Car_Icon = ImageFactory.BUTTON_CALL_OFF.icon(29,15);
    		}
    		
    		if(call.isDisabledCarPresent()) {
    			Disabled_Car_Icon = ImageFactory.BUTTON_CALL_ON.icon(29,15);
    		}else {
    			Disabled_Car_Icon = ImageFactory.BUTTON_CALL_OFF.icon(29,15);
    		}
    		
    		if(call.isFrontUpPresent() ) {
    			Front_Hall_Up_Icon = ImageFactory.BUTTON_CALL_UP_ON.icon(60,15);
    		}else {
    			Front_Hall_Up_Icon = ImageFactory.BUTTON_CALL_UP.icon(60,15);
    		}
    		
    		if(call.isFrontDownPresent() ) {
    			Front_Hall_Down_Icon = ImageFactory.BUTTON_CALL_DOWN_ON.icon(60,15);
    		}else {
    			Front_Hall_Down_Icon = ImageFactory.BUTTON_CALL_DOWN.icon(60,15);
    		}
    		
    		if(call.isRearUpPresent() ) {
    			Rear_Hall_Up_Icon = ImageFactory.BUTTON_CALL_UP_ON.icon(60,15);
    		}else {
    			Rear_Hall_Up_Icon = ImageFactory.BUTTON_CALL_UP.icon(60,15);
    		}
    		
    		if(call.isRearDownPresent() ) {
    			Rear_Hall_Down_Icon = ImageFactory.BUTTON_CALL_DOWN_ON.icon(60,15);
    		}else {
    			Rear_Hall_Down_Icon = ImageFactory.BUTTON_CALL_DOWN.icon(60,15);
    		}
    		
    		if(call.isDisFrontUpPresent() ) {
    			Dis_Front_Hall_Up_Icon = ImageFactory.BUTTON_CALL_UP_ON.icon(60,15);
    		}else {
    			Dis_Front_Hall_Up_Icon = ImageFactory.BUTTON_CALL_UP.icon(60,15);
    		}
    		
    		if(call.isDisFrontDownPresent() ) {
    			Dis_Front_Hall_Down_Icon = ImageFactory.BUTTON_CALL_DOWN_ON.icon(60,15);
    		}else {
    			Dis_Front_Hall_Down_Icon = ImageFactory.BUTTON_CALL_DOWN.icon(60,15);
    		}
    		
    		if(call.isDisRearUpPresent() ) {
    			Dis_Rear_Hall_Up_Icon = ImageFactory.BUTTON_CALL_UP_ON.icon(60,15);
    		}else {
    			Dis_Rear_Hall_Up_Icon = ImageFactory.BUTTON_CALL_UP.icon(60,15);
    		}
    		
    		if(call.isDisRearDownPresent() ) {
    			Dis_Rear_Hall_Down_Icon = ImageFactory.BUTTON_CALL_DOWN_ON.icon(60,15);
    		}else {
    			Dis_Rear_Hall_Down_Icon = ImageFactory.BUTTON_CALL_DOWN.icon(60,15);
    		}
    		
    		if(ver < 5) {
    			ArrayList<JLabel> lab = CarCall.get(call.getFloor());
        		((JLabel)lab.get(0)).setIcon(Front_Car_Icon);
        		((JLabel)lab.get(1)).setIcon(Front_Car_Icon);
        		((JLabel)lab.get(2)).setIcon(Front_Car_Icon);
        		
        		for(int i = 1; i < 5; i ++ ) {
        			ArrayList<PosButton> btn = HallCall.get( (i << 8 | call.getFloor()));
        			((PosButton)btn.get(0)).setIcon(Front_Hall_Up_Icon);
        			((PosButton)btn.get(1)).setIcon(Front_Hall_Down_Icon);
        		}
    			
    		}else if(ver == 5) {
    			ArrayList<JLabel> lab = CarCall.get(call.getFloor());
        		((JLabel)lab.get(0)).setIcon(Front_Car_Icon);
        		((JLabel)lab.get(1)).setIcon(Rear_Car_Icon);
        		((JLabel)lab.get(2)).setIcon(Disabled_Car_Icon);
        		
        		ArrayList<PosButton> btn1 = HallCall.get( (1 << 8 | call.getFloor()));
    			((PosButton)btn1.get(0)).setIcon(Front_Hall_Up_Icon);
    			((PosButton)btn1.get(1)).setIcon(Front_Hall_Down_Icon);
    			
    			ArrayList<PosButton> btn2 = HallCall.get( (2 << 8 | call.getFloor()));
    			((PosButton)btn2.get(0)).setIcon(Rear_Hall_Up_Icon);
    			((PosButton)btn2.get(1)).setIcon(Rear_Hall_Down_Icon);
    			
    			ArrayList<PosButton> btn3 = HallCall.get( (3 << 8 | call.getFloor()));
    			((PosButton)btn3.get(0)).setIcon(Dis_Front_Hall_Up_Icon);
    			((PosButton)btn3.get(1)).setIcon(Dis_Front_Hall_Down_Icon);
    			
    			ArrayList<PosButton> btn4 = HallCall.get( (4 << 8 | call.getFloor()));
    			((PosButton)btn4.get(0)).setIcon(Dis_Rear_Hall_Up_Icon);
    			((PosButton)btn4.get(1)).setIcon(Dis_Rear_Hall_Down_Icon);
    		}
    		
    	}
    }
}
