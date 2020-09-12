package logic.io.crossbar;
public enum OutputPinA01 {
    RL1( "K2M", 100, 1 ),
    RL2( "K3M", 101, 2 ),
    RL3( "GOR", 102, 3 ),
    RL4( "BR", 103, 7 ),
    RL5( "ABR", 104, 6 ),
    RL6( "SCR", 105, 5 ),
    RL10( "CCFR", 106, 4 ),
    LVCR( "LVCR", 107, 0 ),
    P10_1( "ENHW", 108, 15 ),
    P10_2( "FWD", 109, 11 ),
    P10_3( "REV", 110, 10 ),
    P10_4( "EPB", 111, 9 ),
    P10_5( "REL", 112, 8 ),
    P10_6( "UCMTS", 113, 13 ),
    P10_7( "UCMR", 114, 12 ),
    IOWD( "IOWD", 115, 14 );

	
    private final String text;
    public final int     configureIndex;
    public final int     bitOrder;




    OutputPinA01 ( String text, int index, int bitOrder ) {
        this.text           = text;
        this.configureIndex = index;
        this.bitOrder       = bitOrder;
    }


    public String toString () {
        return text;
    }
}
