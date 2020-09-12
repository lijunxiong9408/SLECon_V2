package slecon.setting.modules;
import static logic.util.SiteManagement.MON_MGR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.Workspace;
import slecon.interfaces.Page;
import slecon.interfaces.SetupView;
import slecon.setting.modules.AutoReturn.GeneralBean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import comm.Parser_Deploy;
import comm.Parser_Error;
import comm.Parser_Module;
import comm.agent.AgentMessage;
import comm.constants.AuthLevel.Role;
import comm.event.LiftDataChangedListener;




/**
 * Data of binding bean for Setup -> Module -> Auto Return.
 */
@SetupView(
    path      = "Modules::Auto Return",
    sortIndex = 0x303
)
public class AutoReturnSetting extends SettingPanel<AutoReturn> implements Page, LiftDataChangedListener {
    private static final long serialVersionUID = 9097571441531736737L;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger( AutoReturnSetting.class );

    private volatile long      lastestTimeStamp = -1;
    private final Object       mutex  = new Object(); 
    private volatile Solid      solid  = null;

    /**
     * Connectivity information.
     */
    private LiftConnectionBean connBean;
    
    // TODO
    private Parser_Error       error;
    
    private Parser_Module      module;
    private Parser_Deploy      deploy;

    /**
     * A floor v.s. floor text mapping table.
     */
    private ArrayList<FloorText> floorTexts;

    /**
     * The handler for Setup - Module - Auto Return.
     * @param connBean  It specifies the instance of Connectivity information.
     */
    public AutoReturnSetting ( LiftConnectionBean connBean ) {
        super(connBean);
        this.connBean = connBean;
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    }


    @Override
    public void onStart () throws Exception {
        try {
            deploy = new Parser_Deploy( connBean.getIp(), connBean.getPort() );
            error  = new Parser_Error( connBean.getIp(), connBean.getPort() );
            module = new Parser_Module( connBean.getIp(), connBean.getPort() );
            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(),
                                      AgentMessage.DEPLOYMENT.getCode() | AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode() );
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
                                  AgentMessage.DEPLOYMENT.getCode() | AgentMessage.MODULE.getCode() | AgentMessage.ERROR.getCode()
                                  | AgentMessage.EVENT.getCode() );
        
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
    public void onOK ( AutoReturn panel ) {
        synchronized ( mutex ) {
            setEnabled(false);
            if ( submit() )
                solid = null;
            setEnabled(true);
        }
    }


    @Override
    public void onReset ( AutoReturn panel ) {
        reset();
    }


    @Override
    public void onConnCreate () {
        app.start();
        setEnabled( false );
    }


    @Override
    public void onDataChanged(long timestamp, int msg) {
        if(msg==AgentMessage.ERROR.getCode()) 
            ToolBox.showRemoteErrorMessage(connBean, error);

        synchronized (mutex) {
            setEnabled(false);
            if (solid != null && timestamp > lastestTimeStamp+1500*1000000) {
                int result = JOptionPane.showConfirmDialog(StartUI.getFrame(), "The config of this lift has changed. Reload it?", "Update",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    solid = null;
                    setHot();
                }
            } else {
                setHot();
            }
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
        return AutoReturn.TEXT.getString( "auto_return" );
    }

	protected LinkedHashMap<String, Class<? extends JPanel>> getNavigation() {
		LinkedHashMap<String, Class<? extends JPanel>> map = new LinkedHashMap<String, Class<? extends JPanel>>();
		map.put(Dict.lookup("Modules"), slecon.setting.modules.MessageSetting.class);
		map.put(Dict.lookup("Auto_Return"), this.getClass());
		return map;
	}
	
    public void setHot () {
        lastestTimeStamp = System.nanoTime();
        
        try {
            final AutoReturn.GeneralBean        bean_general        = new AutoReturn.GeneralBean();

            // Initialize internal data.
            initFloorText();

            /* General */
            bean_general.setEnabled( module.aro.isEnabled() );
            bean_general.setActivationTimer( ( long )module.aro.getActivation_time() );
            bean_general.setEnabledGroupAutoReturn( module.aro.isEnable_group_auto_return() );
            bean_general.setReturnFloor( getFloorTextByFloor( module.aro.getReturn_floor() ) );

            FloorText[] ftList     = new FloorText[ 16 ];
            byte[]      floor_list = module.aro.getFloor_list();
            for ( int i = 0 ; i < 16 ; i++ ) {
                    ftList[ i ] = getFloorTextByFloor( floor_list[ i ] );
            }
            bean_general.setFloorList( ftList );
            bean_general.setPriorityListSize( ( int )module.aro.getPriority_len() );

            if ( solid == null )
                solid = new Solid( bean_general );

            // Update returned data to visualization components.
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    if ( solid != null ) {
                        app.stop();
                        app.setGeneralBean( bean_general );
                        app.start();
                    }
                }
            } );
        } catch ( Exception e ) {
            logger.catching( Level.FATAL, e );
        }
    }


    public boolean submit () {
        try {
            final AutoReturn.GeneralBean        bean_general        = app.getGeneralBean();

            /* General. */
            module.aro.setEnabled( bean_general.getEnabled() );
            module.aro.setActivation_time( bean_general.getActivationTimer().intValue() );
            module.aro.setEnable_group_auto_return( bean_general.getEnabledGroupAutoReturn() );
            module.aro.setReturn_floor( bean_general.getReturnFloor().getFloor() );

            final byte[] floorlist = new byte[ 16 ];
            for ( int i = 0 ; i < 16 ; i++ )
                floorlist[ i ] = bean_general.getFloorList()[ i ] == null
                                 ? -1
                                 : bean_general.getFloorList()[ i ].getFloor();
            module.aro.setFloor_list( floorlist );
            module.aro.setPriority_len( bean_general.getPriorityListSize().byteValue() );
            
            module.commit();
            return true;
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog( StartUI.getFrame(), "an error has come. " + e.getMessage() );
            logger.catching( Level.FATAL, e );
        }
        return false;
    }


    /**
     * Get the floor text by a floor.
     * @param floor     It specifies the floor number. It starts from 0.
     * @return Returns the floor text on success; otherwise, return null.
     */
    protected final FloorText getFloorTextByFloor ( int floor ) {
        if ( floorTexts != null )
            for ( int i = 0 ; i < floorTexts.size() ; i++ )
                if ( floorTexts.get( i ).index == floor )
                    return floorTexts.get( i );
        return null;
    }


    public void reset () {
        // Update returned data to visualization components.
        if ( solid != null )
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run () {
                    if ( solid != null ) {
                        app.stop();
                        app.setGeneralBean( solid.bean_general );
                        app.start();
                    }
                }
            } );
    }


    /**
     * Construct the internal table for floor v.s. floor text.
     */
    private void initFloorText () {
        floorTexts = new ArrayList<>( 128 );
        for ( byte i = 0, count = deploy.getFloorCount() ; i < count ; i++ ) {
            int dz = deploy.getDoorZone( i );
            if ( dz != -1 )
                floorTexts.add( new FloorText( i, dz, true, deploy.getFloorText( i ) ) );
        }
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                app.setFloorText( floorTexts );
            }
        } );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private final static class Solid {
        private final AutoReturn.GeneralBean        bean_general;

        private Solid ( GeneralBean bean_general ) {
            super();
            this.bean_general        = bean_general;
        }
    }
}
