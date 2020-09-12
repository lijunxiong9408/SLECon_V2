package slecon.setting.setup.event;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.zip.DataFormatException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import logic.EventID;
import logic.connection.LiftConnectionBean;
import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import ocsjava.remote.configuration.EventAggregator;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.iobar.IOBar;
import slecon.component.iobar.Type;
import slecon.component.iobar.IOBar.NewEvent;
import slecon.component.iobar.IOBar.NewEventListener;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.inspect.logs.TypeOptions;
import slecon.setting.SetupPanel;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;




public class Floor extends JPanel {
    private static final long            serialVersionUID = 6575830564333543571L;

    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.event.Floor");
    
    private boolean                      started          = false;
    private SettingPanel<Floor>          settingPanel;
    private EventAggregator              eventAggregator;
    private LiftConnectionBean           connBean;
    private TreeMap<Integer, String>     floorText;
    private	int				  current_floor_index = 0;
    private JLabel            cpt_calls_io;
    private JLabel            lbl_Select_floor;
    private MyComboBox		  cbo_Select_floor;
    private JLabel            lbl_Front_car_call;
    private JLabel            lbl_Rear_car_call;
    private JLabel            lbl_Front_hall_up_call;
    private JLabel            lbl_Front_hall_down_call;
    private JLabel            lbl_Rear_hall_up_call;
    private JLabel            lbl_Rear_hall_down_call;
    private JLabel            lbl_Disabled_car_call;
    private JLabel            lbl_Disabled_Front_hall_up_call;
    private JLabel            lbl_Disabled_Front_hall_down_call;
    private JLabel            lbl_Disabled_Rear_hall_up_call;
    private JLabel            lbl_Disabled_Rear_hall_down_call;
    private IOBar             io_Front_car_call;
    private IOBar             io_Rear_car_call;
    private IOBar             io_Front_hall_up_call;
    private IOBar             io_Front_hall_down_call;
    private IOBar             io_Rear_hall_up_call;
    private IOBar             io_Rear_hall_down_call;
    private IOBar             io_Disabled_car_call;
	private IOBar             io_Disabled_Front_hall_up_call;
    private IOBar             io_Disabled_Front_hall_down_call;
    private IOBar             io_Disabled_Rear_hall_up_call;
    private IOBar             io_Disabled_Rear_hall_down_call;
    private JLabel            cpt_arrival_light_io;
    private JLabel            lbl_Front_halldown_arrival_light;
    private JLabel            lbl_Front_hall_up_arrival_light;
    private IOBar             io_Front_hall_down_arrival_light;
    private IOBar             io_Front_hall_up_arrival_light;
    private JLabel            lbl_Rear_halldown_arrival_light;
    private JLabel            lbl_Rear_hall_up_arrival_light;
    private IOBar             io_Rear_hall_down_arrival_light;
    private IOBar             io_Rear_hall_up_arrival_light;

    private OpenIOEditorDialogListener handleFrontCarCall;
    private OpenIOEditorDialogListener handleRearCarCall;
    private OpenIOEditorDialogListener handleDisabledCarCall;
    private OpenIOEditorDialogListener handleFrontHallUpCall;
    private OpenIOEditorDialogListener handleFrontHallDownCall;
    private OpenIOEditorDialogListener handleRearHallUpCall;
    private OpenIOEditorDialogListener handleRearHallDownCall;
    private OpenIOEditorDialogListener handleDisFrontHallUpCall;
    private OpenIOEditorDialogListener handleDisFrontHallDownCall;
    private OpenIOEditorDialogListener handleDisRearHallUpCall;
    private OpenIOEditorDialogListener handleDisRearHallDownCall;
    private OpenIOEditorDialogListener handleFrontHallUpLight;
    private OpenIOEditorDialogListener handleFrontHallDownLight;
    private OpenIOEditorDialogListener handleRearHallUpLight;
    private OpenIOEditorDialogListener handleRearHallDownLight;


    public Floor () {
        initGUI();
    }
    
    private void initGUI () {
    	setBackground(StartUI.SUB_BACKGROUND_COLOR);
        setLayout( new MigLayout( "fillx, ins 24, gap 0 6", "[40::40][20::20][32::32][]" ) );     //$NON-NLS-2$
        lbl_Select_floor   = new JLabel("Select Floor");
        cpt_calls_io       = new JLabel( "Calls I/O" );         
        lbl_Front_car_call = new JLabel( "Car Call" );          
        lbl_Rear_car_call  = new JLabel( "Car Call" );          
        lbl_Front_hall_up_call   = new JLabel( "Front Hall Up Call" );      
        lbl_Front_hall_down_call = new JLabel( "Front Hall Down Call" );    
        lbl_Rear_hall_up_call   = new JLabel( "Rear Hall Up Call" );      
        lbl_Rear_hall_down_call = new JLabel( "Rear Hall Down Call" );    
        lbl_Disabled_car_call = new JLabel( "Disabled Car Call" );
        lbl_Disabled_Front_hall_up_call = new JLabel( "Disable Front Hall Up Call" );
        lbl_Disabled_Front_hall_down_call = new JLabel( "Disable Front Hall down Call" );
        lbl_Disabled_Rear_hall_up_call = new JLabel( "Disable Rear Hall Up Call" );
        lbl_Disabled_Rear_hall_down_call = new JLabel( "Disable Rear Hall down Call" );
        
        cbo_Select_floor = new MyComboBox();
        io_Front_car_call  = new IOBar( true );
        io_Rear_car_call   = new IOBar( true );		
        io_Front_hall_up_call    = new IOBar( true );
        io_Front_hall_down_call  = new IOBar( true );
        io_Rear_hall_up_call    = new IOBar( true );
        io_Rear_hall_down_call  = new IOBar( true );
        io_Disabled_car_call = new IOBar(true);
        io_Disabled_Front_hall_up_call = new IOBar(true);
        io_Disabled_Front_hall_down_call = new IOBar(true);
        io_Disabled_Rear_hall_up_call = new IOBar(true);
        io_Disabled_Rear_hall_down_call = new IOBar(true);
        
        setCaptionStyle( lbl_Select_floor );
        setCaptionStyle( cpt_calls_io );
        setLabelTitleStyle( lbl_Front_car_call );
        setLabelTitleStyle( lbl_Rear_car_call );
        setLabelTitleStyle( lbl_Front_hall_up_call );
        setLabelTitleStyle( lbl_Front_hall_down_call );
        setLabelTitleStyle( lbl_Rear_hall_up_call );
        setLabelTitleStyle( lbl_Rear_hall_down_call );
        setLabelTitleStyle( lbl_Disabled_car_call );
        setLabelTitleStyle( lbl_Disabled_Front_hall_up_call );
        setLabelTitleStyle( lbl_Disabled_Front_hall_down_call );
        setLabelTitleStyle( lbl_Disabled_Rear_hall_up_call );
        setLabelTitleStyle( lbl_Disabled_Rear_hall_down_call );
        
        cbo_Select_floor.setPreferredSize(new Dimension(100, 25));
        cbo_Select_floor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int floor = cbo_Select_floor.getSelectedIndex();
				current_floor_index = floor;
				IOBar_Listenner(floor);
				refresh();
			}
		});
        
        add( lbl_Select_floor, "gapbottom 18-12, span, aligny center" );
        add( cbo_Select_floor, "skip 2, span, gapright 12, wrap 30" );
        add( cpt_calls_io, "gapbottom 18-12, span, aligny center" );
        add( lbl_Front_car_call, "skip 2, span, gapright 12" );
        add( io_Front_car_call, "skip 2, span, gapright 12, wrap 30-12" );
        add( lbl_Rear_car_call, "skip 2, span, gapright 12" );
        add( io_Rear_car_call, "skip 2, span, gapright 12, wrap 30-12" );
        add( lbl_Disabled_car_call, "skip 2, span, gapright 12" );
        add( io_Disabled_car_call, "skip 2, span, gapright 12, wrap 30-12" );
        add( lbl_Front_hall_up_call, "skip 2, span, gapright 12" );
        add( io_Front_hall_up_call, "skip 2, span, gapright 12, wrap 30-12" );      
        add( lbl_Front_hall_down_call, "skip 2, span, gapright 12" );               
        add( io_Front_hall_down_call, "skip 2, span, gapright 12, wrap 30-12" );    
        add( lbl_Rear_hall_up_call, "skip 2, span, gapright 12" );                 
        add( io_Rear_hall_up_call, "skip 2, span, gapright 12, wrap 30-12" );      
        add( lbl_Rear_hall_down_call, "skip 2, span, gapright 12" );               
        add( io_Rear_hall_down_call, "skip 2, span, gapright 12, wrap 30-12" );
        add( lbl_Disabled_Front_hall_up_call, "skip 2, span, gapright 12" );               
        add( io_Disabled_Front_hall_up_call, "skip 2, span, gapright 12, wrap 30-12" ); 
        add( lbl_Disabled_Front_hall_down_call, "skip 2, span, gapright 12" );               
        add( io_Disabled_Front_hall_down_call, "skip 2, span, gapright 12, wrap 30-12" );
        add( lbl_Disabled_Rear_hall_up_call, "skip 2, span, gapright 12" );               
        add( io_Disabled_Rear_hall_up_call, "skip 2, span, gapright 12, wrap 30-12" );
        add( lbl_Disabled_Rear_hall_down_call, "skip 2, span, gapright 12" );               
        add( io_Disabled_Rear_hall_down_call, "skip 2, span, gapright 12, wrap 30-12" );
        
        /* ---------------------------------------------------------------------------- */
        cpt_arrival_light_io       = new JLabel( "Arrival Light I/O" );          
        lbl_Front_hall_up_arrival_light  = new JLabel( "Hall Up Arrival Light" );      
        lbl_Front_halldown_arrival_light = new JLabel( "Hall Down Arrival Light" );    
        io_Front_hall_up_arrival_light   = new IOBar( true );
        io_Front_hall_down_arrival_light = new IOBar( true );
        lbl_Rear_hall_up_arrival_light  = new JLabel( "Hall Up Arrival Light" );      
        lbl_Rear_halldown_arrival_light = new JLabel( "Hall Down Arrival Light" );    
        io_Rear_hall_up_arrival_light   = new IOBar( true );
        io_Rear_hall_down_arrival_light = new IOBar( true );
        
        setCaptionStyle( cpt_arrival_light_io );
        setLabelTitleStyle( lbl_Front_hall_up_arrival_light );
        setLabelTitleStyle( lbl_Front_halldown_arrival_light );
        setLabelTitleStyle( lbl_Rear_hall_up_arrival_light );
        setLabelTitleStyle( lbl_Rear_halldown_arrival_light );
        add( cpt_arrival_light_io, "gapbottom 18-12, span, aligny center" );           
        add( lbl_Front_hall_up_arrival_light, "skip 2, span, gapright 12" );                 
        add( io_Front_hall_up_arrival_light, "skip 2, span, gapright 12, wrap 30-12" );      
        add( lbl_Front_halldown_arrival_light, "skip 2, span, gapright 12" );                
        add( io_Front_hall_down_arrival_light, "skip 2, span, gapright 12, wrap 30-12" );    
        
        add( lbl_Rear_hall_up_arrival_light, "skip 2, span, gapright 12" );                 
        add( io_Rear_hall_up_arrival_light, "skip 2, span, gapright 12, wrap 30-12" );      
        add( lbl_Rear_halldown_arrival_light, "skip 2, span, gapright 12" );                
        add( io_Rear_hall_down_arrival_light, "skip 2, span, gapright 12, wrap 30-12" );    

        /* ---------------------------------------------------------------------------- */
        IOBar_Listenner(0);
        loadI18N();
        SetWidgetEnable(false);
        revalidate();
    }
    
    private void loadI18N () {
        cpt_calls_io.setText( TEXT.getString( "Floor.CALL" ) );                                    
        lbl_Select_floor.setText(TEXT.getString("Floor.SELECT_FLOOR"));
        lbl_Front_car_call.setText( TEXT.getString( "Floor.CAR_FRONT" ) );                         
        lbl_Rear_car_call.setText( TEXT.getString( "Floor.CAR_REAR" ) );                         
        lbl_Front_hall_up_call.setText( TEXT.getString( "Floor.HALL_UP_FRONT" ) );                       
        lbl_Front_hall_down_call.setText( TEXT.getString( "Floor.HALL_DN_FRONT" ) );                     
        lbl_Rear_hall_up_call.setText( TEXT.getString( "Floor.HALL_UP_REAR" ) );                       
        lbl_Rear_hall_down_call.setText( TEXT.getString( "Floor.HALL_DN_REAR" ) ); 
        
        lbl_Disabled_car_call.setText( TEXT.getString( "Floor.DISABLED_CAR_REAR" ) );                         
        lbl_Disabled_Front_hall_up_call.setText( TEXT.getString( "Floor.DISABLED_HALL_UP_FRONT" ) );                       
        lbl_Disabled_Front_hall_down_call.setText( TEXT.getString( "Floor.DISABLED_HALL_DN_FRONT" ) );                     
        lbl_Disabled_Rear_hall_up_call.setText( TEXT.getString( "Floor.DISABLED_HALL_UP_REAR" ) );                       
        lbl_Disabled_Rear_hall_down_call.setText( TEXT.getString( "Floor.DISABLED_HALL_DN_REAR" ) ); 
        
        cpt_arrival_light_io.setText( TEXT.getString( "Floor.ARRIVAL_LIGHT" ) );                   
        lbl_Front_halldown_arrival_light.setText( TEXT.getString( "Floor.ARRIVAL_LIGHT_DN_FRONT" ) );    
        lbl_Front_hall_up_arrival_light.setText( TEXT.getString( "Floor.ARRIVAL_LIGHT_UP_FRONT" ) );     
        lbl_Rear_halldown_arrival_light.setText( TEXT.getString( "Floor.ARRIVAL_LIGHT_DN_REAR" ) );    
        lbl_Rear_hall_up_arrival_light.setText( TEXT.getString( "Floor.ARRIVAL_LIGHT_UP_REAR" ) );     
    }
    
    public void SetWidgetEnable(boolean enable) {
    	cbo_Select_floor.setEnabled(enable);
    	io_Front_car_call.setEnabled(enable);
    	io_Rear_car_call.setEnabled(enable);
    	io_Front_hall_up_call.setEnabled(enable);
    	io_Front_hall_down_call.setEnabled(enable);
    	io_Rear_hall_up_call.setEnabled(enable);
    	io_Rear_hall_down_call.setEnabled(enable);
    	io_Disabled_car_call.setEnabled(enable);
    	io_Disabled_Front_hall_up_call.setEnabled(enable);
        io_Disabled_Front_hall_down_call.setEnabled(enable);
        io_Disabled_Rear_hall_up_call.setEnabled(enable);
        io_Disabled_Rear_hall_down_call.setEnabled(enable);
    	io_Front_hall_down_arrival_light.setEnabled(enable);
    	io_Front_hall_up_arrival_light.setEnabled(enable);
    	io_Rear_hall_down_arrival_light.setEnabled(enable);
    	io_Rear_hall_up_arrival_light.setEnabled(enable);
    }
    
    private void IOBar_Listenner(int floor) {
    	if(handleFrontCarCall!= null) {
    		io_Front_car_call.removeNewEventListener(handleFrontCarCall);
    		io_Front_car_call.removeActionListener(handleFrontCarCall);
    	}
    	
    	if(handleRearCarCall!= null) {
    		io_Rear_car_call.removeNewEventListener(handleRearCarCall);
    		io_Rear_car_call.removeActionListener(handleRearCarCall);
    	}
    	
    	if(handleDisabledCarCall!= null) {
    		io_Disabled_car_call.removeNewEventListener(handleDisabledCarCall);
    		io_Disabled_car_call.removeActionListener(handleDisabledCarCall);
    	}
    	
    	if(handleFrontHallUpCall!= null) {
    		io_Front_hall_up_call.removeNewEventListener(handleFrontHallUpCall);
    		io_Front_hall_up_call.removeActionListener(handleFrontHallUpCall);
    	}
    	
    	if(handleFrontHallDownCall!= null) {
    		io_Front_hall_down_call.removeNewEventListener(handleFrontHallDownCall);
    		io_Front_hall_down_call.removeActionListener(handleFrontHallDownCall);
    	}
    	
    	if(handleRearHallUpCall!= null) {
    		io_Rear_hall_up_call.removeNewEventListener(handleRearHallUpCall);
    		io_Rear_hall_up_call.removeActionListener(handleRearHallUpCall);
    	}
    	
    	if(handleRearHallDownCall!= null) {
    		io_Rear_hall_down_call.removeNewEventListener(handleRearHallDownCall);
    		io_Rear_hall_down_call.removeActionListener(handleRearHallDownCall);
    	}
    	
    	if(handleDisFrontHallUpCall!= null) {
    		io_Disabled_Front_hall_up_call.removeNewEventListener(handleDisFrontHallUpCall);
    		io_Disabled_Front_hall_up_call.removeActionListener(handleDisFrontHallUpCall);
    	}
    	
    	if(handleDisFrontHallDownCall!= null) {
    		io_Disabled_Front_hall_down_call.removeNewEventListener(handleDisFrontHallDownCall);
    		io_Disabled_Front_hall_down_call.removeActionListener(handleDisFrontHallDownCall);
    	}
    	
    	if(handleDisRearHallUpCall!= null) {
    		io_Disabled_Rear_hall_up_call.removeNewEventListener(handleDisRearHallUpCall);
    		io_Disabled_Rear_hall_up_call.removeActionListener(handleDisRearHallUpCall);
    	}
    	
    	if(handleDisRearHallDownCall!= null) {
    		io_Disabled_Rear_hall_down_call.removeNewEventListener(handleDisRearHallDownCall);
    		io_Disabled_Rear_hall_down_call.removeActionListener(handleDisRearHallDownCall);
    	}
    	
    	if(handleFrontHallUpLight!= null) {
    		io_Front_hall_up_arrival_light.removeNewEventListener(handleFrontHallUpLight);
    		io_Front_hall_up_arrival_light.removeActionListener(handleFrontHallUpLight);
    	}
    	
    	if(handleFrontHallDownLight!= null) {
    		io_Front_hall_down_arrival_light.removeNewEventListener(handleFrontHallDownLight);
    		io_Front_hall_down_arrival_light.removeActionListener(handleFrontHallDownLight);
    	}
    	
    	if(handleRearHallUpLight!= null) {
    		io_Rear_hall_up_arrival_light.removeNewEventListener(handleRearHallUpLight);
    		io_Rear_hall_up_arrival_light.removeActionListener(handleRearHallUpLight);
    	}
    	
    	if(handleRearHallDownLight!= null) {
    		io_Rear_hall_down_arrival_light.removeNewEventListener(handleRearHallDownLight);
    		io_Rear_hall_down_arrival_light.removeActionListener(handleRearHallDownLight);
    	}
    	
    	handleFrontCarCall = new OpenIOEditorDialogListener( io_Front_car_call, EventID.getCarCallFrontID( floor ) );
    	handleRearCarCall = new OpenIOEditorDialogListener( io_Rear_car_call, EventID.getCarCallRearID( floor ) );
    	handleDisabledCarCall = new OpenIOEditorDialogListener( io_Disabled_car_call, EventID.getCarCallDisabledID( floor ) );
    	handleFrontHallUpCall = new OpenIOEditorDialogListener( io_Front_hall_up_call, EventID.getHallUpCallFrontID( floor ) );
    	handleFrontHallDownCall = new OpenIOEditorDialogListener( io_Front_hall_down_call, EventID.getHallDownCallFrontID( floor ) );
    	handleRearHallUpCall = new OpenIOEditorDialogListener( io_Rear_hall_up_call, EventID.getHallUpCallRearID( floor ) );
    	handleRearHallDownCall = new OpenIOEditorDialogListener( io_Rear_hall_down_call, EventID.getHallDownCallRearID( floor ) );
    	
    	handleDisFrontHallUpCall = new OpenIOEditorDialogListener( io_Disabled_Front_hall_up_call, EventID.getHallUpCallFrontDisabledID( floor ) );
    	handleDisFrontHallDownCall = new OpenIOEditorDialogListener( io_Disabled_Front_hall_down_call, EventID.getHallDownCallFrontDisabledID( floor ) );
    	handleDisRearHallUpCall = new OpenIOEditorDialogListener( io_Disabled_Rear_hall_up_call, EventID.getHallUpCallRearDisabledID( floor ) );
    	handleDisRearHallDownCall = new OpenIOEditorDialogListener( io_Disabled_Rear_hall_down_call, EventID.getHallDownCallRearDisabledID( floor ) );
    	
    	handleFrontHallUpLight = new OpenIOEditorDialogListener( io_Front_hall_up_arrival_light, EventID.getHallUpArrivalLightFrontID( floor ) );
    	handleFrontHallDownLight = new OpenIOEditorDialogListener( io_Front_hall_down_arrival_light, EventID.getHallDownArrivalLightFrontID( floor ) );
    	handleRearHallUpLight = new OpenIOEditorDialogListener( io_Rear_hall_up_arrival_light, EventID.getHallUpArrivalLightRearID( floor ) );
    	handleRearHallDownLight = new OpenIOEditorDialogListener( io_Rear_hall_down_arrival_light, EventID.getHallDownArrivalLightRearID( floor ) );
        
		io_Front_car_call.addNewEventListener( handleFrontCarCall );
		io_Rear_car_call.addNewEventListener(handleRearCarCall);
		io_Disabled_car_call.addNewEventListener(handleDisabledCarCall);
		
		io_Front_hall_up_call.addNewEventListener( handleFrontHallUpCall );
		io_Front_hall_down_call.addNewEventListener( handleFrontHallDownCall );
		
		io_Rear_hall_up_call.addNewEventListener( handleRearHallUpCall );
		io_Rear_hall_down_call.addNewEventListener( handleRearHallDownCall );
		
		io_Disabled_Front_hall_up_call.addNewEventListener( handleDisFrontHallUpCall );
		io_Disabled_Front_hall_down_call.addNewEventListener( handleDisFrontHallDownCall );
		
		io_Disabled_Rear_hall_up_call.addNewEventListener( handleDisRearHallUpCall );
		io_Disabled_Rear_hall_down_call.addNewEventListener( handleDisRearHallDownCall );
		
		io_Front_hall_up_arrival_light.addNewEventListener( handleFrontHallUpLight );
		io_Front_hall_down_arrival_light.addNewEventListener( handleFrontHallDownLight );
		
		io_Rear_hall_up_arrival_light.addNewEventListener( handleRearHallUpLight );
		io_Rear_hall_down_arrival_light.addNewEventListener( handleRearHallDownLight );
		
		io_Front_car_call.addActionListener( handleFrontCarCall );
		io_Rear_car_call.addActionListener(handleRearCarCall);
		io_Disabled_car_call.addActionListener(handleDisabledCarCall);
		
		io_Front_hall_up_call.addActionListener( handleFrontHallUpCall );
		io_Front_hall_down_call.addActionListener( handleFrontHallDownCall );
		
		io_Rear_hall_up_call.addActionListener( handleRearHallUpCall );
		io_Rear_hall_down_call.addActionListener( handleRearHallDownCall );
		
		io_Disabled_Front_hall_up_call.addActionListener( handleDisFrontHallUpCall );
		io_Disabled_Front_hall_down_call.addActionListener( handleDisFrontHallDownCall );
		
		io_Disabled_Rear_hall_up_call.addActionListener( handleDisRearHallUpCall );
		io_Disabled_Rear_hall_down_call.addActionListener( handleDisRearHallDownCall );
		
		io_Front_hall_up_arrival_light.addActionListener( handleFrontHallUpLight );
		io_Front_hall_down_arrival_light.addActionListener( handleFrontHallDownLight );
		
		io_Rear_hall_up_arrival_light.addActionListener( handleRearHallUpLight );
		io_Rear_hall_down_arrival_light.addActionListener( handleRearHallDownLight );
    	
    }
    
    public void setFrontCarCallEvent ( Event evt ) throws DataFormatException {
        io_Front_car_call.setEvent( evt );
    }
    
    public void setRearCarCallEvent ( Event evt ) throws DataFormatException {
        io_Rear_car_call.setEvent( evt );
    }
    
    public void setDisabledCarCallEvent ( Event evt ) throws DataFormatException {
        io_Disabled_car_call.setEvent( evt );
    }

    public void setFrontHallUpCallEvent ( Event evt ) throws DataFormatException {
        io_Front_hall_up_call.setEvent( evt );
    }

    public void setFrontHallDownCallEvent ( Event evt ) throws DataFormatException {
        io_Front_hall_down_call.setEvent( evt );
    }
    
    public void setRearHallUpCallEvent ( Event evt ) throws DataFormatException {
        io_Rear_hall_up_call.setEvent( evt );
    }


    public void setRearHallDownCallEvent ( Event evt ) throws DataFormatException {
        io_Rear_hall_down_call.setEvent( evt );
    }
    
    public void setDisabledFrontHallUpCallEvent ( Event evt ) throws DataFormatException {
        io_Disabled_Front_hall_up_call.setEvent( evt );
    }

    public void setDisabledFrontHallDownCallEvent ( Event evt ) throws DataFormatException {
        io_Disabled_Front_hall_down_call.setEvent( evt );
    }
    
    public void setDisabledRearHallUpCallEvent ( Event evt ) throws DataFormatException {
        io_Disabled_Rear_hall_up_call.setEvent( evt );
    }

    public void setDisabledRearHallDownCallEvent ( Event evt ) throws DataFormatException {
        io_Disabled_Rear_hall_down_call.setEvent( evt );
    }


    /* ---------------------------------------------------------------------------- */
    public void setFrontHallUpArrivalLightEvent ( Event evt ) throws DataFormatException {
        io_Front_hall_up_arrival_light.setEvent( evt );
    }


    public void setFrontHallDownArrivalLightEvent ( Event evt ) throws DataFormatException {
        io_Front_hall_down_arrival_light.setEvent( evt );
    }
    
    /* ---------------------------------------------------------------------------- */
    public void setRearHallUpArrivalLightEvent ( Event evt ) throws DataFormatException {
        io_Rear_hall_up_arrival_light.setEvent( evt );
    }


    public void setReartHallDownArrivalLightEvent ( Event evt ) throws DataFormatException {
        io_Rear_hall_down_arrival_light.setEvent( evt );
    }


    /* ---------------------------------------------------------------------------- */
    

    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_BOLD );
        c.setForeground(Color.WHITE);
    }


    private void setLabelTitleStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }
    

    public TreeMap<Integer, String> getFloorText () {
        return floorText;
    }


    public EventAggregator getEventAggregator () {
        return eventAggregator;
    }


    private synchronized void refresh () {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
            	int selectItem = cbo_Select_floor.getSelectedIndex();
            	 if ( eventAggregator != null ) {
                     Event evtCarFrontCall       = eventAggregator.getEvent( EventID.getCarCallFrontID(selectItem) );
                     Event evtCarRearCall        = eventAggregator.getEvent( EventID.getCarCallRearID( selectItem ) );
                     Event evtCarDisabledCall    = eventAggregator.getEvent( EventID.getCarCallDisabledID( selectItem ) );
                     Event evtHallUpFrontCall    = eventAggregator.getEvent( EventID.getHallUpCallFrontID( selectItem ) );
                     Event evtHallDownFrontCall  = eventAggregator.getEvent( EventID.getHallDownCallFrontID( selectItem ) );
                     Event evtHallUpRearCall    = eventAggregator.getEvent( EventID.getHallUpCallRearID( selectItem ) );
                     Event evtHallDownRearCall  = eventAggregator.getEvent( EventID.getHallDownCallRearID( selectItem ) );
                     Event evtHallUpFrontDisabledCall    = eventAggregator.getEvent( EventID.getHallUpCallFrontDisabledID( selectItem ) );
                     Event evtHallDownFrontDisabledCall  = eventAggregator.getEvent( EventID.getHallDownCallFrontDisabledID( selectItem ) );
                     Event evtHallUpRearDisabledCall    = eventAggregator.getEvent( EventID.getHallUpCallRearDisabledID( selectItem ) );
                     Event evtHallDownRearDisabledCall  = eventAggregator.getEvent( EventID.getHallDownCallRearDisabledID( selectItem ) );
                     Event evtHallUpFrontLight   = eventAggregator.getEvent( EventID.getHallUpArrivalLightFrontID( selectItem ) );
                     Event evtHallDownFrontLight = eventAggregator.getEvent( EventID.getHallDownArrivalLightFrontID( selectItem ) );
                     Event evtHallUpReartLight   = eventAggregator.getEvent( EventID.getHallUpArrivalLightRearID( selectItem ) );
                     Event evtHallDownRLight = eventAggregator.getEvent( EventID.getHallDownArrivalLightRearID( selectItem ) );

                     try {
                         setFrontCarCallEvent( evtCarFrontCall );
                         setRearCarCallEvent( evtCarRearCall );
                         setDisabledCarCallEvent(evtCarDisabledCall);
                         setFrontHallUpCallEvent( evtHallUpFrontCall );
                         setFrontHallDownCallEvent( evtHallDownFrontCall );
                         setRearHallUpCallEvent( evtHallUpRearCall );
                         setRearHallDownCallEvent( evtHallDownRearCall );
                         setDisabledFrontHallUpCallEvent(evtHallUpFrontDisabledCall);
                         setDisabledFrontHallDownCallEvent(evtHallDownFrontDisabledCall);
                         setDisabledRearHallUpCallEvent(evtHallUpRearDisabledCall);
                         setDisabledRearHallDownCallEvent(evtHallDownRearDisabledCall);
                         setFrontHallUpArrivalLightEvent( evtHallUpFrontLight );
                         setFrontHallDownArrivalLightEvent( evtHallDownFrontLight );
                         setRearHallUpArrivalLightEvent( evtHallUpReartLight );
                         setReartHallDownArrivalLightEvent( evtHallDownRLight );
                     } catch ( DataFormatException e ) {
                         e.printStackTrace();
                     }
                 }
            }
        } );
    }


    public static Floor createPanel ( SettingPanel<Floor> panel ) {
        Floor gui = new Floor();
        gui.setSettingPanel( panel );
        return gui;
    }


    private void setSettingPanel ( SettingPanel<Floor> panel ) {
        this.settingPanel = panel;
    }


    public void setData ( LiftConnectionBean connBean, TreeMap<Integer, String> floorMap, EventAggregator eventAggregator) {
        this.connBean        = connBean;
        this.floorText       = floorMap;
        this.eventAggregator = eventAggregator;

        String[] floor = new String[floorMap.size()];
        for ( Integer i : floorMap.keySet() ) {
        	floor[i] = floorMap.get(i);
        }
        
        cbo_Select_floor.setModel(new DefaultComboBoxModel<>(floor));
        cbo_Select_floor.setSelectedIndex(current_floor_index);
        
        refresh();
    }


    public final class OpenIOEditorDialogListener implements NewEventListener, ActionListener {
        private final int eventID;
        private IOBar     iobar;

        OpenIOEditorDialogListener ( IOBar iobar, int evtID ) {
            this.eventID = evtID;
            this.iobar   = iobar;
        }


        @Override
        public void newEventOccurred ( NewEvent evt ) {
            Event e = null;
            if ( evt.getSource() == iobar ) {
                e = IOEditorDialog.showDialog( connBean, eventID, iobar.getEvent() );
            }
            
            if ( e != null && eventAggregator != null ) {
                eventAggregator.setEvent( eventID, e );
                refresh();
            }
        }


        @Override
        public void actionPerformed ( ActionEvent evt ) {
            Event e = null;
            if ( evt.getSource() == iobar && evt.getActionCommand().indexOf( "INPUT" ) >= 0 ){
                e = IOEditorDialog.showDialog( connBean, eventID, iobar.getEvent(), Type.INPUT, evt.getID() );
            }
            
            if ( evt.getSource() == iobar && evt.getActionCommand().indexOf( "OUTPUT" ) >= 0 ){
                e = IOEditorDialog.showDialog( connBean, eventID, iobar.getEvent(), Type.OUTPUT, evt.getID() );
            }
            
            if ( e != null && eventAggregator != null ) {
                eventAggregator.setEvent( eventID, e );
                refresh();
            }
        }
    }
}
