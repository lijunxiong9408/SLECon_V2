package logic.io.crossbar;
import java.util.HashMap;




public enum OutputSourceD01 {
    ALWAYS0( "Always 0", 0 ),
    ALWAYS1( "Always 1", 1 ),
    DRVEN( "Driver Enable", 2 ),
    DRVFWD( "Driver Forward Run", 3 ),
    DRVREV( "Driver Reverse Run", 4 ),
    DRVMLTSPD0( "Driver Multi Speed 0", 5 ),
    DRVMLTSPD1( "Driver Multi Speed 1", 6 ),
    DRVMLTSPD2( "Driver Multi Speed 2", 7 ),
    UR( "Up Run Relay", 8 ),
    DR( "Down Run Relay", 9 ),
    RR( "Main Run Relay", 10 ),
    BR( "Brake Relay", 11 ),
    LVCR( "(none)", 12 ),
    LR( "Car Lighting Relay", 13 ),
    RBR( "Relief Brake Relay", 14 ),
    IOWD( "(none)", 15 ),
    DOR( "Front Door Open Relay", 16 ),
    RDOR( "Rear Door Open Relay", 17 ),
    SIMLDZ( "(none)", 18 ),
    SIMUDZ( "(none)", 19 ),
    SIMLSL( "(none)", 20 ),
    SIMUSL( "(none)", 21 ),
    SPDCHK( "Speed Check", 22 ),
    CCF( "Cabin Cooling Fan", 23 ),
	REL( "Releveling", 24 ),
	GOR( "Go To Running", 25 ),
	UCMTS( "Ucmp Test", 26 ),
	UCMR( "Ucmp Relay", 27 ),
	EPB( "Emergency power Operation", 28 ),
	RR2( "Shorting phases contactor", 29 ),
	LVC1R( "(none)", 30 ),
	SC1( "(Speed Check 1)", 31 ),
	SC2( "(Speed Check 2)", 32 ),
	SC3( "(Speed Check 3)", 33 ),
	ARL( "(Alarm Sounding)", 34 );

    private static HashMap<Integer, OutputSourceD01> outputSources = new HashMap<>();




    static {
        for ( OutputSourceD01 s : values() )
            outputSources.put( s.id, s );
    }


    private final String fullname;
    public final int     id;




    OutputSourceD01 ( String text, int id ) {
        this.id       = id;
        this.fullname = String.format( "%s - %s", name(), text );
    }


    public static OutputSourceD01 getByID ( int id ) {
        return outputSources.get( id );
    }


    public String toString () {
        return fullname;
    }
}
