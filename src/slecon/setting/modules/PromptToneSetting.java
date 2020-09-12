package slecon.setting.modules;
import static logic.util.SiteManagement.MON_MGR;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.PageTreeExpression;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.Parser_Misc;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;



@SetupView(
    path      = "Modules::Prompt Tone",
    sortIndex = 0x311
)
public class PromptToneSetting extends SettingPanel<PromptTone> implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = 6398741682602149481L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( PromptToneSetting.class );
    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.SettingPanel");
    private static final PageTreeExpression WRITE_OCS_EXPRESSION = new PageTreeExpression("write_ocs");

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid     solid = null;

    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;

    // TODO
    private Parser_Error  error;
    
    private Parser_Module module;
    private Parser_Deploy deploy;
    private Parser_Misc        misc;

    /**
     * The handler for Setup - Module - Access Control.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public PromptToneSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart () throws Exception {
        try {
            error     = new Parser_Error( connBean.getIp(), connBean.getPort() );
            module    = new Parser_Module( connBean.getIp(), connBean.getPort() );
            deploy 	  = new Parser_Deploy( connBean.getIp(), connBean.getPort() );  
            misc      = new Parser_Misc( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.MODULE.getCode() | 
                                      AgentMessage.ERROR.getCode() |
                                      AgentMessage.OCS_CONFIG.getCode() |
                                      AgentMessage.MISC.getCode() );
            setHot();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.nanoTime();
        }
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
    }


    @Override
    public void onResume () throws Exception {
        setEnabled( true );
        MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                AgentMessage.MODULE.getCode() | 
                AgentMessage.ERROR.getCode() |
                AgentMessage.OCS_CONFIG.getCode() |
                AgentMessage.MISC.getCode() );
        
        setOKButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
        setResetButtonEnabled(Arrays.asList(ToolBox.getRoles(connBean)).contains(Role.WRITE_OCS));
    }


    @Override
    public void onPause () throws Exception {
        setEnabled( false );
        MON_MGR.removeEventListener( this );
    }


    @Override
    public void onStop () throws Exception {
        MON_MGR.removeEventListener( this );
    }


    @Override
    public void onConnCreate () {
        app.start();
        setEnabled( false );
    }


    @Override
    public void onDataChanged ( long timestamp, int msg ) {
        if(msg==AgentMessage.ERROR.getCode()) 
            ToolBox.showRemoteErrorMessage(connBean, error);

        synchronized (mutex) {
            setEnabled(false);
            setHot();
            setEnabled(true);
        }
    }


    @Override
    public void onConnLost () {
        app.stop();
        setEnabled( true );
    }


    @Override
    public void onDestroy () throws Exception {
    }


    @Override
    protected String getPanelTitle () {
        return PromptTone.TEXT.getString( "prompt_tone" );
    }

    @Override
 	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
     	LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
 		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
 		map.put(Dict.lookup("Prompt_Tone"), this.getClass());
 		return map;
 	}
    
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final PromptTone.VoiceEnableBean bean_voice		= new PromptTone.VoiceEnableBean();
            
            bean_voice.setDoor_open_enable(module.ves.getDoorOpenEnable());
            bean_voice.setDoor_close_enable(module.ves.getDoorCloseEnable());
            bean_voice.setDirect_enable(module.ves.getDirectEnable());
            bean_voice.setMessage_enable(module.ves.getMessageEnable());
            bean_voice.setArrival_enable(module.ves.getArrivalEnable());
            bean_voice.setButton_enable(module.ves.getButtonEnable());
            bean_voice.setHall_up_voice(module.ves.getHallUpVoice());
            bean_voice.setHall_down_voice(module.ves.getHallDownVoice());
            bean_voice.setCar_up_voice(module.ves.getCarUpVoice());
            bean_voice.setCar_down_voice(module.ves.getCarDownVoice());
            bean_voice.setLevel_car_A(module.ves.getLevel_Car_A());
            bean_voice.setLevel_car_B(module.ves.getLevel_Car_B());
            bean_voice.setLevel_car_C(module.ves.getLevel_Car_C());
            bean_voice.setLevel_car_D(module.ves.getLevel_Car_D());
            bean_voice.setLevel_hall_A(module.ves.getLevel_Hall_A());
            bean_voice.setLevel_hall_B(module.ves.getLevel_Hall_B());
            bean_voice.setLevel_hall_C(module.ves.getLevel_Hall_C());
            bean_voice.setLevel_hall_D(module.ves.getLevel_Hall_D());
            
            if ( solid == null )
	            solid = new Solid( bean_voice );
            
            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    app.stop();
                    app.setVoiceEnableBean(bean_voice);
                    app.start();
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    private boolean submit () { 
    	try {
            final PromptTone.VoiceEnableBean bean_voice		= app.getVoiceEnableBean();
            
            // Check Language Level param.
            CheckLanguageLevelData(bean_voice.getLevel_car_A(), bean_voice.getLevel_car_B(),
            					   bean_voice.getLevel_car_C(), bean_voice.getLevel_car_D());
            
            CheckLanguageLevelData(bean_voice.getLevel_hall_A(), bean_voice.getLevel_hall_B(),
					   bean_voice.getLevel_hall_C(), bean_voice.getLevel_hall_D());
            
            module.ves.setDoorOpenEnable((byte)(bean_voice.isDoor_open_enable()?1:0));
            module.ves.setDoorCloseEnable((byte)(bean_voice.isDoor_close_enable()?1:0));
            module.ves.setDirectEnable((byte)(bean_voice.isDirect_enable()?1:0));
            module.ves.setMessageEnable((byte)(bean_voice.isMessage_enable() ?1:0));
            module.ves.setArrivalEnable((byte)(bean_voice.isArrival_enable()?1:0));
            module.ves.setButtonEnable((byte)(bean_voice.isButton_enable()?1:0));
            module.ves.setHallUpVoice((byte)bean_voice.getHall_up_voice());
            module.ves.setHallDownVoice((byte)bean_voice.getHall_down_voice());
            module.ves.setCarUpVoice((byte)bean_voice.getCar_up_voice());
            module.ves.setCarDownVoice((byte)bean_voice.getCar_down_voice());
            module.ves.setLevel_Car_A((byte)bean_voice.getLevel_car_A());
            module.ves.setLevel_Car_B((byte)bean_voice.getLevel_car_B());
            module.ves.setLevel_Car_C((byte)bean_voice.getLevel_car_C());
            module.ves.setLevel_Car_D((byte)bean_voice.getLevel_car_D());
            module.ves.setLevel_Hall_A((byte)bean_voice.getLevel_hall_A());
            module.ves.setLevel_Hall_B((byte)bean_voice.getLevel_hall_B());
            module.ves.setLevel_Hall_C((byte)bean_voice.getLevel_hall_C());
            module.ves.setLevel_Hall_D((byte)bean_voice.getLevel_hall_D());
            
            module.commit();
            return true;
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
            logger.catching( Level.FATAL, e );
        }
        return false;
    }


    private void reset () {
    	if ( solid != null )
	        SwingUtilities.invokeLater( new Runnable() {
	            @Override
	            public void run () {
	                if ( solid != null ) {
	                    app.stop();
	                    app.setVoiceEnableBean( solid.bean_voice );
	                    app.start();
	                }
	            }
	        } );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private static final class Solid {
        private final PromptTone.VoiceEnableBean bean_voice;
        private Solid ( PromptTone.VoiceEnableBean bean_voice ) {
            super();
            this.bean_voice = bean_voice;
        }
    }

	@Override
	public void onOK(PromptTone panel) {
		// TODO Auto-generated method stub
		synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
	}

	@Override
	public void onReset(PromptTone panel) {
		// TODO Auto-generated method stub
		reset();
	}
	
	/**
	 *  Check Lanaguage Level data.
	 * */
	private int CheckLanguageLevelData(short A, short B, short C, short D ) {
		 short[] level = new short[4];
         level[0] = A;
         level[1] = B;
         level[2] = C;
         level[3] = D;
         for(int i = 0; i < 4; i ++) {
         	int repect = 0;
         	for(int j = i; j < 4; j++ ) {
         		if((level[i] != 0) && level[i] == level[j]) {
         			repect += 1;
         		}
         		
         		//invalid data.
         		if((level[i] == 0 && level[j] != 0)) {
         			JOptionPane.showMessageDialog( StartUI.getFrame(), "Language Level Data Invalid !" );
         			return -1;
         		}
         		
         	}
         	if(repect > 1) {
         		JOptionPane.showMessageDialog( StartUI.getFrame(), "Language Level Data Repect !" );
     			return -1;
         	}
         }
         return 0;
	}
}
