package slecon.setting.setup.io;




public enum NVAddressD01 {
    NVADDR_0( (short) 0x0000, NVAddressD01.UL), 
    NVADDR_INSTALLATION_MODE( (short) 0x0004, NVAddressD01.UC), 
    NVADDR_CUMULATIVE_CAR_RUN_COUNT( (short) 0x0008, NVAddressD01.UL), 
    NVADDR_CONTRACT_NO( (short) 0x000c, NVAddressD01.UL), 
    NVADDR_NV_FORMAT_VERSION( (short) 0x0010, NVAddressD01.UI), 
    NVADDR_BOOT_UP_COUNT( (short) 0x0014, NVAddressD01.UL), 
    NVADDR_RTC_YEAR( (short) 0x0018, NVAddressD01.UI), 
    NVADDR_RTC_MONTH( (short) 0x001A, NVAddressD01.UC), 
    NVADDR_RTC_DAY( (short) 0x001B, NVAddressD01.UC), 
    NVADDR_RTC_HOUR( (short) 0x001C, NVAddressD01.UC), 
    NVADDR_RTC_MINUTE( (short) 0x001D, NVAddressD01.UC), 
    NVADDR_RTC_SECOND( (short) 0x001E, NVAddressD01.UC), 
    NVADDR_SERIAL_NUMBER( (short) 0x0020, NVAddressD01.UL), 
    NVADDR_MCS_BASIC_INTERVAL( (short) 0x0024, NVAddressD01.UI), 
    NVADDR_DUMMY_SPACE( (short) 0x00cc, NVAddressD01.UL), 
    NVADDR_LSL( (short) 0x00f8, NVAddressD01.SL), 
    NVADDR_USL( (short) 0x00fc, NVAddressD01.SL), 
    NVADDR_LDZ_BASEADDR( (short) 0x0100, NVAddressD01.SL), 
    NVADDR_UDZ_BASEADDR( (short) 0x0300, NVAddressD01.SL), 
    NVADDR_UP_LANDING_TUNE_BASEADDR( (short) 0x0500, NVAddressD01.SL), 
    NVADDR_DOWN_LANDING_TUNE_BASEADDR( (short) 0x0700, NVAddressD01.SL), 
    NVADDR_ENCPOS( (short) 0x0900, NVAddressD01.SL), 
    NVADDR_DOORZONE_HEIGHT( (short) 0x0904, NVAddressD01.FLOAT), 
    NVADDR_LV_SPACING( (short) 0x0908, NVAddressD01.FLOAT), 
    NVADDR_MAX_SPEED( (short) 0x090C, NVAddressD01.FLOAT), 
    NVADDR_ANALOG_OUTPUT_ZERO_OFFSET( (short) 0x0910, NVAddressD01.FLOAT), 
    NVADDR_QEI_COUNT_PER_REV( (short) 0x0914, NVAddressD01.UL), 
    NVADDR_QEI_MAX_SPEED( (short) 0x0918, NVAddressD01.UL), 
    NVADDR_QEI_DIR( (short) 0x091C, NVAddressD01.UC), 
    NVADDR_MOTOR_RPM( (short) 0x0920, NVAddressD01.UL), 
    NVADDR_OVERSPEED_COUNT_LIMIT( (short) 0x0924, NVAddressD01.UL), 
    NVADDR_OVERSPEED_THRESHOLD( (short) 0x0928, NVAddressD01.FLOAT), 
    NVADDR_SPEEDLOSS_COUNT_LIMIT( (short) 0x092C, NVAddressD01.UL), 
    NVADDR_SPEEDLOSS_THRESHOLD( (short) 0x0930, NVAddressD01.FLOAT), 
    NVADDR_SPEED_CHECK_UPPER_THRESHOLD( (short) 0x0934, NVAddressD01.FLOAT), 
    NVADDR_SPEED_CHECK_LOWER_THRESHOLD( (short) 0x0938, NVAddressD01.FLOAT), 
    NVADDR_LRL_SPACING( (short) 0x093C, NVAddressD01.SL), 
    NVADDR_URL_SPACING( (short) 0x0940, NVAddressD01.SL), 
    NVADDR_TRAVEL_PROFILE_BASE( (short) 0x0a00, NVAddressD01.UL), 
    NVADDR_NORMAL_J1( (short) 0x0a00, NVAddressD01.FLOAT), 
    NVADDR_NORMAL_AAMAX( (short) 0x0a04, NVAddressD01.FLOAT), 
    NVADDR_NORMAL_J2( (short) 0x0a08, NVAddressD01.FLOAT), 
    NVADDR_NORMAL_VMAX( (short) 0x0a0c, NVAddressD01.FLOAT), 
    NVADDR_NORMAL_J3( (short) 0x0a10, NVAddressD01.FLOAT), 
    NVADDR_NORMAL_ADMAX( (short) 0x0a14, NVAddressD01.FLOAT), 
    NVADDR_NORMAL_J4( (short) 0x0a18, NVAddressD01.FLOAT), 
    NVADDR_NORMAL_J4OL( (short) 0x0a1c, NVAddressD01.UC), 
    NVADDR_FLOOR_POSITION( (short) 0x1000, NVAddressD01.UC), 
    NVADDR_BUILDING_DOORZONE_COUNT( (short) 0x1001, NVAddressD01.UC), 
    NVADDR_LOWER_SHAFT_LIMIT_DOORZONE_COUNT( (short) 0x1002, NVAddressD01.UC), 
    NVADDR_UPPER_SHAFT_LIMIT_DOORZONE_COUNT( (short) 0x1003, NVAddressD01.UC), 
    NVADDR_BK0_TIMEOUT_LIMIT( (short) 0x1008, NVAddressD01.UI), 
    NVADDR_BK1_TIMEOUT_LIMIT( (short) 0x100A, NVAddressD01.UI), 
    NVADDR_CAR_RUN_TIMEOUT_LIMIT( (short) 0x100C, NVAddressD01.UI), 
    NVADDR_LV_TOGGLE_TIMEOUT_LIMIT( (short) 0x100E, NVAddressD01.UI), 
    NVADDR_DCS_INSPECTION_EXIT_DELAY( (short) 0x1010, NVAddressD01.UI), 
    NVADDR_IDLE_STABLE_DELAY( (short) 0x1012, NVAddressD01.UI), 
    NVADDR_PRERUN_CHECK_TIMEOUT_LIMIT( (short) 0x1014, NVAddressD01.UI), 
    NVADDR_PRETORQUE_TIMEOUT_LIMIT( (short) 0x1016, NVAddressD01.UI), 
    NVADDR_DRIVER_BRAKE_OPEN_TIMEOUT_LIMIT( (short) 0x1018, NVAddressD01.UI), 
    NVADDR_DRIVER_BRAKE_CLOSE_TIMEOUT_LIMIT( (short) 0x101A, NVAddressD01.UI), 
    NVADDR_DRIVER_DISABLE_DELAY( (short) 0x101C, NVAddressD01.UI), 
    NVADDR_EMERGENCY_STOP_HOLDOFF( (short) 0x101E, NVAddressD01.UI), 
    NVADDR_UDR_CLOSE_DELAY( (short) 0x1020, NVAddressD01.UI), 
    NVADDR_RR_CLOSE_DELAY( (short) 0x1022, NVAddressD01.UI), 
    NVADDR_RR_CLOSE_TIMEOUT_LIMIT( (short) 0x1024, NVAddressD01.UI), 
    NVADDR_BRAKE_OPEN_DELAY( (short) 0x1026, NVAddressD01.UI), 
    NVADDR_BRAKE_OPEN_TIMEOUT_LIMIT( (short) 0x1028, NVAddressD01.UI), 
    NVADDR_BRAKE_CLOSE_DELAY( (short) 0x102A, NVAddressD01.UI), 
    NVADDR_BRAKE_CLOSE_TIMEOUT_LIMIT( (short) 0x102C, NVAddressD01.UI), 
    NVADDR_UDRR_OPEN_DELAY( (short) 0x102E, NVAddressD01.UI), 
    NVADDR_ADO_SPEED( (short) 0x1030, NVAddressD01.FLOAT), 
    NVADDR_SHAFT_LIMIT_OVER_SPEED_LIMIT( (short) 0x1034, NVAddressD01.FLOAT), 
    NVADDR_POSITION_ADJUSTMENT_TOLERANCE( (short) 0x1038, NVAddressD01.FLOAT), 
    NVADDR_BRAKE_RESISTOR( (short) 0x103C, NVAddressD01.UC), 
    NVADDR_DRVENF_FAULT_HOLDOFF( (short) 0x103E, NVAddressD01.UI), 
    NVADDR_BRAKE_JAM_TIME_RATIO( (short) 0x1040, NVAddressD01.UC), 
    NVADDR_DBDF_MON_DELAY( (short) 0x1042, NVAddressD01.UI), 
    NVADDR_SHAFT_LIMIT_OVER_SPEED_HOLDOFF( (short) 0x1044, NVAddressD01.UI), 
    NVADDR_CABIN_FAN_OFF_DELAY( (short) 0x1046, NVAddressD01.UI), 
    NVADDR_CABIN_FAN_ON_TEMP( (short) 0x1048, NVAddressD01.UC), 
    NVADDR_CABIN_FAN_OFF_TEMP( (short) 0x1049, NVAddressD01.UC), 
    NVADDR_BAD_SHAFT_LIMIT_HOLDOFF( (short) 0x104A, NVAddressD01.UI), 
    NVADDR_RELEVELING_TRIGGER_HOLDOFF( (short) 0x104C, NVAddressD01.UI), 
    NVADDR_RELEVELING_RETRY( (short) 0x104E, NVAddressD01.UC), 
    NVADDR_RELEVELING_TIME_LIMIT( (short) 0x1050, NVAddressD01.UI),
    
    NVADDR_IN_FIRST_BITPOS( (short) 0x1880, NVAddressD01.UC), 
    NVADDR_IN_LDZ_BITPOS( (short) 0x1880, NVAddressD01.UC), 
    NVADDR_IN_UDZ_BITPOS( (short) 0x1881, NVAddressD01.UC),
    NVADDR_IN_LVCFB_BITPOS( (short) 0x1882, NVAddressD01.UC),
    NVADDR_IN_LSL_BITPOS( (short) 0x1883, NVAddressD01.UC), 
    NVADDR_IN_USL_BITPOS( (short) 0x1884, NVAddressD01.UC), 
    NVADDR_IN_INS_BITPOS( (short) 0x1885, NVAddressD01.UC), 
    NVADDR_IN_INSUP_BITPOS( (short) 0x1886, NVAddressD01.UC), 
    NVADDR_IN_INSDOWN_BITPOS( (short) 0x1887, NVAddressD01.UC), 
    NVADDR_IN_LVC1FB_BITPOS( (short) 0x1888, NVAddressD01.UC),
    NVADDR_IN_DBDE_BITPOS( (short) 0x1889, NVAddressD01.UC),
    NVADDR_IN_DBDF_BITPOS( (short) 0x188a, NVAddressD01.UC),
    NVADDR_IN_BS_BITPOS( (short) 0x188b, NVAddressD01.UC),
    NVADDR_IN_THM_BITPOS( (short) 0x188c, NVAddressD01.UC), 
    NVADDR_IN_ST4_BITPOS( (short) 0x188d, NVAddressD01.UC),
    NVADDR_IN_ST3_BITPOS( (short) 0x188e, NVAddressD01.UC),
    NVADDR_IN_ST2_BITPOS( (short) 0x188f, NVAddressD01.UC), 
    NVADDR_IN_ST1_BITPOS( (short) 0x1890, NVAddressD01.UC), 
    NVADDR_IN_DRVBM_BITPOS( (short) 0x1891, NVAddressD01.UC), 
    NVADDR_IN_DRVOK_BITPOS( (short) 0x1892, NVAddressD01.UC), 
    NVADDR_IN_ST5_BITPOS( (short) 0x1893, NVAddressD01.UC),
    NVADDR_IN_ST6_BITPOS( (short) 0x1894, NVAddressD01.UC),
    NVADDR_IN_ENCF_BITPOS( (short) 0x1895, NVAddressD01.UC), 
    NVADDR_IN_DRVENF_BITPOS( (short) 0x1896, NVAddressD01.UC), 
    NVADDR_IN_LRL_BITPOS( (short) 0x1897, NVAddressD01.UC),
    NVADDR_IN_URL_BITPOS( (short) 0x1898, NVAddressD01.UC),
    NVADDR_IN_DLB_BITPOS( (short) 0x1899, NVAddressD01.UC), 
    NVADDR_IN_EPB_BITPOS( (short) 0x189a, NVAddressD01.UC),
    NVADDR_IN_BS1_BITPOS( (short) 0x189b, NVAddressD01.UC),
    NVADDR_IN_BS2_BITPOS( (short) 0x189c, NVAddressD01.UC), 
    NVADDR_IN_NDZ_BITPOS( (short) 0x189d, NVAddressD01.UC), 
    NVADDR_IN_UCMTS_BITPOS( (short) 0x189e, NVAddressD01.UC),
    NVADDR_IN_UCMTS2_BITPOS( (short) 0x189f, NVAddressD01.UC),
    NVADDR_IN_UCMTS3_BITPOS( (short) 0x18a0, NVAddressD01.UC),
    
    NVADDR_OUTBP_FIRST( (short) 0x18f0, NVAddressD01.UC), 
    NVADDR_OUTBP_K1( (short) 0x18f0, NVAddressD01.UC), 
    NVADDR_OUTBP_K2( (short) 0x18f1, NVAddressD01.UC), 
    NVADDR_OUTBP_GO( (short) 0x18f2, NVAddressD01.UC), 
    NVADDR_OUTBP_BR( (short) 0x18f3, NVAddressD01.UC), 
    NVADDR_OUTBP_ABR( (short) 0x18f4, NVAddressD01.UC), 
    NVADDR_OUTBP_SC_1( (short) 0x18f5, NVAddressD01.UC), 
    NVADDR_OUTBP_CCF( (short) 0x18f6, NVAddressD01.UC), 
    NVADDR_OUTBP_LVC( (short) 0x18f7, NVAddressD01.UC), 
    NVADDR_OUTBP_SC_2( (short) 0x18f8, NVAddressD01.UC), 
    NVADDR_OUTBP_FWD( (short) 0x18f9, NVAddressD01.UC), 
    NVADDR_OUTBP_REV( (short) 0x18fa, NVAddressD01.UC), 
    NVADDR_OUTBP_EPB( (short) 0x18fb, NVAddressD01.UC), 
    NVADDR_OUTBP_SPC( (short) 0x18fc, NVAddressD01.UC), 
    NVADDR_OUTBP_SC_3( (short) 0x18fd, NVAddressD01.UC), 
    NVADDR_OUTBP_D01( (short) 0x18fe, NVAddressD01.UC),
    NVADDR_OUTBP_LVC1( (short) 0x18ff, NVAddressD01.UC),
    
    NVADDR_DCS_REDP( (short) 0x1900, NVAddressD01.UC),     	// uc, REDP input bit position
    NVADDR_DCS_EDP( (short) 0x1901, NVAddressD01.UC),     	// uc, EDP input bit position
    NVADDR_DCS_RDOL( (short) 0x1902, NVAddressD01.UC),     	// uc, RDOL input bit position
    NVADDR_DCS_RDCL( (short) 0x1903, NVAddressD01.UC),     	// uc, RDCL input bit position
    NVADDR_DCS_RSGS( (short) 0x1904, NVAddressD01.UC),     	// uc, SGS input bit position
    NVADDR_DCS_INS( (short) 0x1905, NVAddressD01.UC),     	// uc, INS input bit positio
    NVADDR_DCS_DOL( (short) 0x1906, NVAddressD01.UC),     	// uc, DOL input bit position
    NVADDR_DCS_DCL( (short) 0x1907, NVAddressD01.UC),     	// uc, DCL input bit position
    NVADDR_DCS_SGS( (short) 0x1908, NVAddressD01.UC),     	// uc, SGS input bit position
    NVADDR_DCS_NDZ( (short) 0x1909, NVAddressD01.UC),     	// uc, NDZ1 input bit position
    NVADDR_DCS_INS_UP( (short) 0x190a, NVAddressD01.UC),    // uc, INS_UP2 input bit position
    NVADDR_DCS_INS_DOWN( (short) 0x190b, NVAddressD01.UC),  // uc, INS_DOWN input bit position
    NVADDR_DCS_CINS( (short) 0x190c, NVAddressD01.UC),      // uc, CINS input bit position
    NVADDR_DCS_CINS_UP( (short) 0x190d, NVAddressD01.UC),     // uc, CINS_UP input bit position
    NVADDR_DCS_CINS_DOWN( (short) 0x190e, NVAddressD01.UC),     // uc, CINS_DOWN input bit position
    NVADDR_DCS_TSL( (short) 0x190f, NVAddressD01.UC),      // uc, TSL input bit position
    NVADDR_DCS_FLDZ( (short) 0x1910, NVAddressD01.UC),  // uc, FLDZ input bit position
    NVADDR_DCS_FUDZ( (short) 0x1911, NVAddressD01.UC),  // uc, FUDZ input bit position
    NVADDR_DCS_RLDZ( (short) 0x1912, NVAddressD01.UC),  // uc, RLDZ input bit position
    NVADDR_DCS_RUDZ( (short) 0x1913, NVAddressD01.UC),  // uc, RUDZ input bit position
    NVADDR_DCS_DRL( (short) 0x1914, NVAddressD01.UC),     // uc, RDCR output bit position
    NVADDR_DCS_URL( (short) 0x1915, NVAddressD01.UC),     // uc, RNDR output bit position
    NVADDR_DCS_LSL( (short) 0x1916, NVAddressD01.UC),     // uc, RNDR output bit position
    NVADDR_DCS_USL( (short) 0x1917, NVAddressD01.UC),     // uc, RNDR output bit position
    NVADDR_DCS_DOR( (short) 0x1918, NVAddressD01.UC),     // uc, DOR output bit position
    NVADDR_DCS_DCR( (short) 0x1919, NVAddressD01.UC),     // uc, DCR output bit position       
    NVADDR_DCS_FR( (short) 0x191A, NVAddressD01.UC),     // uc, FR output bit position 
    NVADDR_DCS_LR( (short) 0x191B, NVAddressD01.UC),     // uc, LR output bit position        
    NVADDR_DCS_NDR( (short) 0x191C, NVAddressD01.UC),     // uc, NDR output bit position
    NVADDR_DCS_RDOR( (short) 0x191D, NVAddressD01.UC),     // uc, RDOR output bit position
    NVADDR_DCS_RDCR( (short) 0x191E, NVAddressD01.UC),     // uc, RDCR output bit position
    NVADDR_DCS_RNDR( (short) 0x191F, NVAddressD01.UC),     // uc, RNDR output bit position
    NVADDR_DCS_TD1( (short) 0x1920, NVAddressD01.UC),     // uc, TD1 output bit position 
    NVADDR_DCS_TD2( (short) 0x1921, NVAddressD01.UC),     // uc, TD2 output bit position        
    NVADDR_DCS_TD3( (short) 0x1922, NVAddressD01.UC),     // uc, TD3 output bit position
    NVADDR_DCS_AR( (short) 0x1923, NVAddressD01.UC),     // uc, AR output bit position
    
    NVADDR_EPS_RBR( (short) 0x1930, NVAddressD01.UC),     // uc, RBR output bit position
    NVADDR_EPS_MCK( (short) 0x1931, NVAddressD01.UC),     // uc, MCK output bit position
    NVADDR_EPS_DZI( (short) 0x1932, NVAddressD01.UC),     // uc, DZI output bit position
    NVADDR_EPS_ECF( (short) 0x1933, NVAddressD01.UC),     // uc, ECF output bit position
    NVADDR_EPS_EBK( (short) 0x1934, NVAddressD01.UC),     // uc, EBK output bit position
    NVADDR_EPS_IPS( (short) 0x1940, NVAddressD01.UC),     // uc,IPS output bit position
    NVADDR_EPS_MBR( (short) 0x1941, NVAddressD01.UC),     // uc,MBR output bit position
    NVADDR_EPS_MCR( (short) 0x1942, NVAddressD01.UC),     // uc, MCR output bit position
    NVADDR_EPS_XMBR( (short) 0x1943, NVAddressD01.UC),     // uc, XMBR output bit position
    NVADDR_EPS_BAT( (short) 0x1944, NVAddressD01.UC),     // uc, BAT output bit position
    NVADDR_EPS_RCR( (short) 0x1945, NVAddressD01.UC),     // uc, RCR output bit position
    NVADDR_OUTBP_LAST( (short) 0x194F, NVAddressD01.UC),    // uc, last output bit position
	
    NVADDR_IN_IM_BITPOS( (short) 0x1970, NVAddressD01.UC),	// uc, IM input bit position
    NVADDR_IN_IU_BITPOS( (short) 0x1971, NVAddressD01.UC),	// uc, IU input bit position
    NVADDR_IN_ID_BITPOS( (short) 0x1972, NVAddressD01.UC),	// uc, ID input bit position
    NVADDR_IN_ISU_BITPOS( (short) 0x1973, NVAddressD01.UC),	// uc, ISU input bit position
    NVADDR_IN_ISD_BITPOS( (short) 0x1974, NVAddressD01.UC);	// uc, ISD input bit position
    
    private static final int UL    = 0;
    private static final int UI    = 1;
    private static final int UC    = 2;
    private static final int SL    = 3;
    private static final int SI    = 4;
    private static final int SC    = 5;
    private static final int FLOAT = 6;
    public final short       address;
    public final int         type;




    NVAddressD01 ( short address, int type ) {
        this.address = address;
        this.type    = type;
    }


    public Integer getSize () {
        Integer size;
        switch ( type ) {
        case UL :
        case SL :
        case FLOAT :
            size = 4;
            break;
        case UI :
        case SI :
            size = 2;
            break;
        case UC :
        case SC :
            size = 1;
            break;
        default :
            size = null;
        }
        return size;
    }

}
