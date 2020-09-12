package logic.io.crossbar;
public enum OutputPinD01 {
	K1( "K1", 48, 11 ),
	K2( "K2", 49, 12 ),
	GO( "GO", 50, 6 ),
	BR( "BR", 51, 9 ),
	ABR( "ABR", 52, 8 ),
	SC1( "SC1", 53, 5 ),
	CCF( "CCF", 54, 7 ),
	LVC( "LVC", 55, 0 ),
	SC2( "SC2", 56, 4 ),
	FWD( "FWD", 57, 13 ),
	REV( "REV", 58, 14 ),
	EPB( "EPB", 59, 15 ),
	SPC( "SPC", 60, 10 ),
	SC3( "SC3", 61, 3 ),
	DO1( "DO1", 62, 2 ),
	LVC1( "LVC1", 63, 1 );


	
    private final String text;
    public final int     configureIndex;
    public final int     bitOrder;

    OutputPinD01 ( String text, int index, int bitOrder ) {
        this.text           = text;
        this.configureIndex = index;
        this.bitOrder       = bitOrder;
    }

    public String toString () {
        return text;
    }
}
