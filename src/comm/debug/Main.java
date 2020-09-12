package comm.debug;
import static logic.util.SiteManagement.MON_MGR;
import java.util.zip.DataFormatException;
import ocsjava.remote.configuration.Event;
import ocsjava.remote.configuration.EventAggregator;
import base.cfg.SiteFactory;
import base.cfg.SiteFactory.Elevator;
import comm.Parser_Auth;
import comm.Parser_Deploy;
import comm.Parser_Device;
import comm.Parser_Error;
import comm.Parser_Event;
import comm.Parser_Log;
import comm.Parser_Log.Log;
import comm.Parser_McsConfig;
import comm.Parser_McsNvram;
import comm.Parser_Misc;
import comm.Parser_Misc.OCSButton;
import comm.Parser_Module;
import comm.Parser_OcsConfig;
import comm.Parser_Run;
import comm.Parser_Status;
import comm.agent.AgentMessage;
import comm.constants.CANBus;
import comm.constants.CallDef;
import comm.event.LiftDataChangedListener;




@SuppressWarnings("unused")
public class Main implements LiftDataChangedListener {
    private static final String HOSTNAME = "192.168.1.251";
    private static final int    PORT     = 1235;
    private Parser_Status       status;
    private Parser_Run          run;
    private Parser_OcsConfig    ocs_config;
    private Parser_Module       module;
    private Parser_Misc         misc;
    private Parser_McsNvram     mcs_nvram;
    private Parser_McsConfig    mcs_config;
    private Parser_Log          log;
    private Parser_Event        event;
    private Parser_Error        error;
    private Parser_Device       device;
    private Parser_Deploy       deploy;
    private Parser_Auth         auth;




    public Main () {
        // Test Misc.
//        misc       = new Parser_Misc( HOSTNAME, PORT );
//        misc.call( CallDef.FRONT_CAR, ( byte )10 );
//        misc.press( OCSButton.BS_TF, true );
//        misc.press( OCSButton.BS_BF, true );
//        misc.press( OCSButton.BS_CHC, true );
//        misc.press( OCSButton.BS_DDO, true );
//        misc.time( System.currentTimeMillis() );
//        misc.mcs( ( short )0x1004, new byte[ 0 ]  );
//        misc.registerMaintenance();
        
//        // Test Run.
//        run        = new Parser_Run( HOSTNAME, PORT );
//        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.RUN.getCode() );

//        // Test Log.
//        log        = new Parser_Log( HOSTNAME, PORT );
//        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.LOG.getCode() );
//        testLog();

//        // Test Event.
//        event      = new Parser_Event( HOSTNAME, PORT );
//        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.EVENT.getCode() );
        
//        // Test Error.
//        error      = new Parser_Error( HOSTNAME, PORT );
//        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.ERROR.getCode() );

//        // Test Module.
//        module     = new Parser_Module( HOSTNAME, PORT );
//        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.MODULE.getCode() );
        
//        // Test MCS configuration.
//        mcs_config = new Parser_McsConfig( HOSTNAME, PORT );
//        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.MCS_CONFIG.getCode() );
        
//        // Test MCS NVRAM.
//        mcs_nvram  = new Parser_McsNvram( HOSTNAME, PORT );
//        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.MCS_NVRAM.getCode() );
        
        // Test OCS configuration.
        ocs_config = new Parser_OcsConfig( HOSTNAME, PORT );
        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.OCS_CONFIG.getCode() );
        
//        // Test Status.
//        status     = new Parser_Status( HOSTNAME, PORT );
//        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.STATUS.getCode() );
        
//        // Test Authentication.
//        auth       = new Parser_Auth( HOSTNAME, PORT, "admin", "admin" );
//        this.testAuth();
//        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.AUTH.getCode() );
        
//        // Test Deployment.
//        deploy     = new Parser_Deploy( HOSTNAME, PORT );
//        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.DEPLOYMENT.getCode() );
//        testDeployment();
        
//        // Test Device.
//        device     = new Parser_Device( HOSTNAME, PORT );
//        MON_MGR.addEventListener( this, HOSTNAME, PORT, AgentMessage.DEVICE.getCode() );
//        testDevice();
    }


    private static void initMonitorMgr () {
        SiteFactory siteFactory = new SiteFactory();
        siteFactory.load();
        String[]    siteName    = siteFactory.getSiteName();
        Elevator[]  elevator;
        for ( String siteName1 : siteName ) {
            elevator = siteFactory.getElevator( siteName1 );
            for ( Elevator elevator1 : elevator ) {
                try {
                    MON_MGR.addAgent( elevator1.host, Integer.parseInt( elevator1.port ) );
                } catch ( NumberFormatException e ) {
                    e.printStackTrace( System.err );
                }
            }
        }
    }
    
    
    public void testAuth () {
        auth.auth();
        System.out.println( "Role: " + auth.getRoles() );
    }
    
    
    private void testDeployment () {
        // Test read.
        System.out.println( "Group configuration: " );
        for ( int i = 0 ; i < 16 ; i++ ) {
            System.out.println( "Elevator 192.168.1.2" + ( 39 + i ) );
            System.out.print( "Register:" );
            for ( int j = 0 ; j < 128 ; j++ ) {
                System.out.printf( "[0x%X] ", deploy.getGroupRegBitmask( ( byte )j, ( byte )i ) );
            }
            System.out.println();
            System.out.print( "Clear:   " );
            for ( int j = 0 ; j < 128 ; j++ ) {
                System.out.printf( "[0x%X] ", deploy.getGroupClrBitmask( ( byte )j, ( byte )i ) );
            }
            System.out.println();
        }
        System.out.println( "Floor count: " + deploy.getFloorCount() );
        System.out.print( "Door zone: " );
        for ( byte i = 0 ; i < deploy.getFloorCount() ; i++ )
            System.out.print( deploy.getDoorZone( i ) + " " );
        System.out.println();
        System.out.print( "Floor text: " );
        for ( byte i = 0 ; i < deploy.getFloorCount() ; i++ )
            System.out.print( deploy.getFloorText( i ) + " " );
        System.out.println();
        System.out.println();
        
//        // Test write.
//        deploy.setDoorZone( ( byte )0, ( byte )100 );
//        deploy.setFloorCount( ( byte )111 );
//        deploy.setFloorText( ( byte )0 , "xyz" );
//        deploy.setGroupRegBitmask( ( byte )0, ( byte )15, ( short )0xFFFF );
//        deploy.setGroupClrBitmask( ( byte )0, ( byte )15, ( short )0xFFFF );
//        deploy.commit();
    }


    private void testDevice () {
        byte i, j;
        System.out.println( "Available device in CAR: " );
        byte ret[] = device.getAvailableDevcies( CANBus.CAR );
        for ( i = 0 ; i < ret.length ; i++ ) {
            System.out.println( "ID: " + ret[ i ] );
            System.out.println( "Type: " + device.getType( CANBus.CAR, ret[ i ] ) );
            System.out.println( "Hardware version: " + device.getHardwareVersion( CANBus.CAR, ret[ i ] ) );
            System.out.println( "Firmware version: " + device.getFirmwareVersion( CANBus.CAR, ret[ i ] ) );
            System.out.print( "Input: " );
            for ( j = 0 ; j < 32 ; j++ )
                System.out.print( device.getInput( CANBus.CAR, ret[ i ], j ) + " " );
            System.out.println();
            System.out.print( "Output: " );
            for ( j = 0 ; j < 32 ; j++ )
                System.out.print( device.getOutput( CANBus.CAR, ret[ i ], j ) + " " );
            System.out.println();
            System.out.println( "Arrow: " + device.getArrow( CANBus.CAR, ret[ i ] ) );
            System.out.println( "Text: " + device.getText( CANBus.CAR, ret[ i ] ) );
            System.out.println( "Message: " + device.getMessage( CANBus.CAR, ret[ i ] ) );
            System.out.print( "Behavior: " );
            for ( j = 0 ; j < 32 ; j++ )
                System.out.print( device.getBehavior( CANBus.CAR, ret[ i ], j ) + " " );
            System.out.println();
            System.out.println();
        }
        System.out.println();
        System.out.println( "Available device in HALL: " );
        ret = device.getAvailableDevcies( CANBus.HALL );
        for ( i = 0 ; i < ret.length ; i++ ) {
            System.out.println( "ID: " + ret[ i ] );
            System.out.println( "Type: " + device.getType( CANBus.HALL, ret[ i ] ) );
            System.out.println( "Hardware version: " + device.getHardwareVersion( CANBus.HALL, ret[ i ] ) );
            System.out.println( "Firmware version: " + device.getFirmwareVersion( CANBus.HALL, ret[ i ] ) );
            System.out.print( "Input: " );
            for ( j = 0 ; j < 32 ; j++ )
                System.out.print( device.getInput( CANBus.HALL, ret[ i ], j ) + " " );
            System.out.println();
            System.out.print( "Output: " );
            for ( j = 0 ; j < 32 ; j++ )
                System.out.print( device.getOutput( CANBus.HALL, ret[ i ], j ) + " " );
            System.out.println();
            System.out.println( "Arrow: " + device.getArrow( CANBus.HALL, ret[ i ] ) );
            System.out.println( "Text: " + device.getText( CANBus.HALL, ret[ i ] ) );
            System.out.println( "Message: " + device.getMessage( CANBus.HALL, ret[ i ] ) );
            System.out.print( "Behavior: " );
            for ( j = 0 ; j < 32 ; j++ )
                System.out.print( device.getBehavior( CANBus.HALL, ret[ i ], j ) + " " );
            System.out.println();
            System.out.println();
        }
        System.out.println();
    }


    private void testError () {
        System.out.println( "Deployment error: " + error.getDeploymentError() );
        System.out.println( "MCSNVRAM error: " + error.getMcsNvramError() );
        System.out.println( "MCS error: " + error.getMcsError() );
        System.out.println( "Event error: " + error.getEventError() );
        System.out.println( "Module error: " + error.getModuleError() );
        System.out.println();
    }


    private void testEvent () {
        // Read the floor configuration from remote.
        byte[] ret = event.getInstalledDevices();
        System.out.println( "Length of installed devices received: " + ret.length );

        // Read the information on event.
        ret = event.getEvent();
        System.out.println( "Length of event informaiton received: " + ret.length );

        // Using event aggregator to recover events by binary.
        try {
            EventAggregator x = EventAggregator.toEventAggregator( ret );
            Event           e = x.getEvent( 0 );
            System.out.println( "Input count: " + e.getInputCount() );
            System.out.println( "Output count: " + e.getOutputCount() );
        } catch ( DataFormatException e ) {
            e.printStackTrace();
        }
        System.out.println();
    }


    private void testLog () {
        // Test read.
        System.out.println( "Log count: " + log.getCount() );
        System.out.println();
        for ( int i = 0 ; i < log.getCount() ; i++ ) {
            Log alog = log.getLog( ( byte )i );
            
            // Primary data.
            System.out.println( "Type: " + alog.type );
            System.out.printf( "Error code: 0x%X\n", alog.errcode );
            System.out.println( "Timestamp: " + alog.timestamp );
            
            // Extra data.
            System.out.println( "Position: " + alog.position );
            System.out.println( "Speed: " + alog.speed );
            System.out.println( "Current Floor: " + alog.current_floor );
            System.out.println( "Last run floor: " + alog.last_run_floor );
            System.out.println( "OCS module: " + alog.ocs_module );
            for ( int x = 0 ; x < 128 ; x++ ) {
                System.out.print( "[" );
                System.out.print( ( ( CallDef.FRONT_CAR.getCode() & alog.calls[ x ] ) != 0 ) ? "C" : " " );
                System.out.print( ( ( CallDef.FRONT_HALL_UP.getCode() & alog.calls[ x ] ) != 0 ) ? "U" : " " );
                System.out.print( ( ( CallDef.FRONT_HALL_DOWN.getCode() & alog.calls[ x ] ) != 0 ) ? "D" : " " );
                System.out.print( ( ( CallDef.LIGHT_UP.getCode() & alog.calls[ x ] ) != 0 ) ? "u" : " " );
                System.out.print( ( ( CallDef.LIGHT_DOWN.getCode() & alog.calls[ x ] ) != 0 ) ? "d" : " " );
                System.out.print( "]" );
            }
            System.out.println();
        }
        System.out.println();

//        // Test write.
//        log.deleteLog( ( byte )0 );
//        log.clearLog();
    }


    private void testMCSConfig () {
        System.out.println( "MM-ratio: " + mcs_config.getMMRatio() );
        System.out.println( "Contract speed: " + mcs_config.getContractSpeed() );
        for ( int i = 0 ; i < 128 ; i++ ) {
            System.out.print( "Lower position: " + mcs_config.getLower( ( byte )i ) );
            System.out.print( "\t" );
            System.out.println( "Upper position: " + mcs_config.getUpper( ( byte )i ) );
        }
        System.out.print( "Cross-bar: " );
        for ( byte i = 0, ret[] = mcs_config.getCrossbar() ; i < ret.length ; i++ )
            System.out.printf( "0x%02X ", ret[ i ] );
        System.out.println();
        System.out.println( "Firmware version: " + mcs_config.getFirmwareVersion() );
        System.out.println( "Board version: " + mcs_config.getBoardVersion() );
        System.out.println( "Serial number: " + mcs_config.getSerialNumber() );
        System.out.println( "Contract number: " + mcs_config.getContractNumber() );
        System.out.println( "Last maintenace: " + mcs_config.getLastMaintenance() );
        System.out.println( "Bootup time: " + mcs_config.getBootupTime() );
        System.out.println();
    }


    private void testMCSNVRAM () {
        // Test read.
        System.out.println( "ADDR 8 -> " + mcs_nvram.getUnsignedInt( ( short )8 ) );
        System.out.println();

//      // Test write.
//      mcs_nvram.setInt( ( short )8, 1234 );
//      mcs_nvram.commit();
    }


    private void testModule () {
        module.commit();
    }


    private void testOCSConfig () {
        System.out.println( "Protocol version: " + ocs_config.getProtocolVersion() );
        System.out.println( "OCS version: " + ocs_config.getVersion() );
        System.out.println( "OCS expire days: " + ocs_config.getOCSExpireDaysCount() );
    }


    private void testRun () {
        System.out.println( "Open front door on last run: " + run.isOpenFrontDoor() );
        System.out.println( "Open rear door on last run: " + run.isOpenRearDoor() );
        System.out.println( "Sent run command on last run: " + run.isRunCmd() );
        System.out.println( "Last run floor: " + run.getLastRunFloor() );
        System.out.println( "Lowest floor: " + run.getLowerFloor() );
        System.out.println( "Top floor: " + run.getUpperFloor() );
        for ( int i = 0 ; i < 128 ; i++ ) {
            System.out.print( "[" );
            System.out.print( run.IsCallDef( ( byte )i, CallDef.FRONT_CAR ) ? "C" : " " );
            System.out.print( run.IsCallDef( ( byte )i, CallDef.FRONT_HALL_UP ) ? "U" : " " );
            System.out.print( run.IsCallDef( ( byte )i, CallDef.FRONT_HALL_DOWN ) ? "D" : " " );
            System.out.print( run.IsCallDef( ( byte )i, CallDef.LIGHT_UP ) ? "u" : " " );
            System.out.print( run.IsCallDef( ( byte )i, CallDef.LIGHT_DOWN ) ? "d" : " " );
            System.out.print( "]" );
        }
        System.out.println();
        System.out.println();
    }


    private void testStatus () {
        System.out.println( "BF: " + status.isBF() );
        System.out.println( "TF: " + status.isTF() );
        System.out.println( "DDO: " + status.isDDO() );
        System.out.println( "CHC: " + status.isCHC() );
        System.out.println( "Direction: " + status.getDirection() );
        System.out.println( "Direction animation: " + status.isDirectionAnimation() );
        System.out.println( "Floor: " + status.getFloor() );
        System.out.println( "Pos. cnt: " + status.getPositionCount() );
        System.out.println( "Brake disp.: " + status.getBrakeDisplacement() );
        System.out.println( "Cabin load: " + status.getCabinLoad() );
        System.out.println( "Speed: " + status.getSpeed() );
        System.out.println( "Time: " + status.getTime() );
        System.out.println( "OCS module: " + status.getOCSModule() );
        System.out.println( "DCS status: " + status.getDoorStatus( true ) );
        System.out.println( "MCS status: " + status.getMcsStatus() );
        System.out.println( "DOR: " + status.getDOR( true ) );
        System.out.println( "DOL: " + status.getDOL( true ) );
        System.out.println( "DCL: " + status.getDCL( true ) );
        System.out.println( "SGS: " + status.getSGS( true ) );
        System.out.println( "EDP: " + status.getEDP( true ) );
        System.out.println( "Light: " + status.getLight( true ) );
        System.out.println( "Fan: " + status.getFan( true ) );
        System.out.println();
    }


    public static void main ( String args[] ) throws InterruptedException {
        initMonitorMgr();
        Thread.sleep( 1000 );
        Main main = new Main();
    }


    @Override
    public void onConnCreate () {
        System.out.println( "Connection established!" );
    }


//    private long t = System.currentTimeMillis();
    @Override
    public void onDataChanged ( long timestamp, int msg ) {
//        testStatus();
        testOCSConfig();
//        testMCSNVRAM();
//        testMCSConfig();
//        testError();
//        testModule();
//        testDeployment();
//        testEvent();
//        testLog();
//        testRun();
//        testDevice();
    }


    @Override
    public void onConnLost () {
        System.out.println( "Connection lost!" );
    }
}
