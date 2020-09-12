package logic.io.crossbar;
public enum InputSourceA07 {
	LDZ( "Lower Door Zone", 0 ), 
	UDZ( "Upper Door Zone", 1 ), 
	LVCFB( "LVC Feedback", 2 ), 
	LSL( "Lower Shaft Limit", 3 ),
	USL( "Upper Shaft Limit", 4 ), 
	INS( "Inspection", 5 ), 
	INSUP( "Inspection Up", 6 ), 
	INSDOWN( "Inspection Down", 7 ),
	ENABLE( "Main Contact Enabled", 8 ), 
	DBDE( "Brake Contact", 9 ), 
	DBDF( "Main Contact", 10 ), 
	BS( "Break Switch", 11 ),
	THM( "Thermal Sensor", 12 ), 
	DFC( "Door Fully Closed", 13 ), 
	DW( "Hall Door Safety Monitor", 14 ), 
	HES( "Hall Safety Monitor", 15 ),
	CES( "Car Safety Monitor", 16 ), 
	DRVBM( "Motor Driver Brake Open", 17 ), 
	DRVOK( "Motor Driver OK", 18 ), 
	ADO( "Advance Door Opening", 19 ),
	V24MON( "Power Monitoring", 20 ), 
	ENCF( "Encoder Fail", 21 ), 
	DRVENF( "Motor Driver Enabled Feedback", 22 ),
	LRL("Lower Re-Leveling",23),
	URL("Up Re-Leveling",24),
	ANO1FB("Analog Signal Return",25),
	EPB("Emergency Power Operation",26),
	BS1("Break Switch 1",27),
	BS2("Break Switch 2",28),
	NDZ("None Door Zone",29),
	UCMTS1("UCMP Test 1",30),
	UCMTS2("UCMP Test 2",31),
	UCMTS3("UCMP Test 3",32),
	DCS_REDP("DCS REDP",72),
	DCS_EDP("DCS EDP",73),
	DCS_RDOL("DCS RDOL",74),
	DCS_RDCL("DCS RDCL",75),
	DCS_RSGS("DCS RSGS",76),
	DCS_INS("DCS INS",77),
	DCS_DOL("DCS DOL",78),
	DCS_DCL("DCS DCL",79),
	DCS_FSGS("DCS FDCL",80),
	DCS_NDZ("DCS NDZ",81),
	DCS_INS_UP("DCS INS UP",82),
	DCS_INS_DOWN("DCS INS DOWN",83),
	DCS_LDZ("DCS LDZ",84),
	DCS_UDZ("DCS UDZ",85),
	DCS_LRL("DCS LRL",86),
	DCS_URL("DCS URL",87),
	DCS_FLDZ("DCS FLDZ",88),
	DCS_FUDZ("DCS FNDZ",89),
	DCS_RLDZ("DCS RLDZ",90),
	DCS_RUDZ("DCS RUDZ",91),
	DCS_DOR("DCS DOR",92),
	DCS_DCR("DCS DCR",93),
	DCS_FR("DCS FR",94),
	DCS_LR("DCS LR",95),
	DCS_NDR("DCS NDR",96),
	DCS_RDOR("DCS RDOR",97),
	DCS_RDCR("DCS RDCR",98),
	DCS_RNDR("DCS RDCR",99);
    public final int    configureIndex;
    public final String fullname;




    InputSourceA07 ( String text, int index ) {
        this.fullname       = text;
        this.configureIndex = index;
    }
}
