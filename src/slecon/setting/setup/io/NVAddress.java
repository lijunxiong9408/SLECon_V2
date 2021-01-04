package slecon.setting.setup.io;




public enum NVAddress {
    NVADDR_0( (short) 0x0000, NVAddress.UL), 
    NVADDR_INSTALLATION_MODE( (short) 0x0004, NVAddress.UC), 
    NVADDR_CUMULATIVE_CAR_RUN_COUNT( (short) 0x0008, NVAddress.UL), 
    NVADDR_CONTRACT_NO( (short) 0x000c, NVAddress.UL), 
    NVADDR_NV_FORMAT_VERSION( (short) 0x0010, NVAddress.UI), 
    NVADDR_BOOT_UP_COUNT( (short) 0x0014, NVAddress.UL), 
    NVADDR_RTC_YEAR( (short) 0x0018, NVAddress.UI), 
    NVADDR_RTC_MONTH( (short) 0x001A, NVAddress.UC), 
    NVADDR_RTC_DAY( (short) 0x001B, NVAddress.UC), 
    NVADDR_RTC_HOUR( (short) 0x001C, NVAddress.UC), 
    NVADDR_RTC_MINUTE( (short) 0x001D, NVAddress.UC), 
    NVADDR_RTC_SECOND( (short) 0x001E, NVAddress.UC), 
    NVADDR_SERIAL_NUMBER( (short) 0x0020, NVAddress.UL), 
    NVADDR_MCS_BASIC_INTERVAL( (short) 0x0024, NVAddress.UI), 
    NVADDR_DUMMY_SPACE( (short) 0x00cc, NVAddress.UL), 
    NVADDR_LSL( (short) 0x00f8, NVAddress.SL), 
    NVADDR_USL( (short) 0x00fc, NVAddress.SL), 
    NVADDR_LDZ_BASEADDR( (short) 0x0100, NVAddress.SL), 
    NVADDR_UDZ_BASEADDR( (short) 0x0300, NVAddress.SL), 
    NVADDR_UP_LANDING_TUNE_BASEADDR( (short) 0x0500, NVAddress.SL), 
    NVADDR_DOWN_LANDING_TUNE_BASEADDR( (short) 0x0700, NVAddress.SL), 
    NVADDR_ENCPOS( (short) 0x0900, NVAddress.SL), 
    NVADDR_DOORZONE_HEIGHT( (short) 0x0904, NVAddress.FLOAT), 
    NVADDR_LV_SPACING( (short) 0x0908, NVAddress.FLOAT), 
    NVADDR_MAX_SPEED( (short) 0x090C, NVAddress.FLOAT), 
    NVADDR_ANALOG_OUTPUT_ZERO_OFFSET( (short) 0x0910, NVAddress.FLOAT), 
    NVADDR_QEI_COUNT_PER_REV( (short) 0x0914, NVAddress.UL), 
    NVADDR_QEI_MAX_SPEED( (short) 0x0918, NVAddress.UL), 
    NVADDR_QEI_DIR( (short) 0x091C, NVAddress.UC), 
    NVADDR_MOTOR_RPM( (short) 0x0920, NVAddress.UL), 
    NVADDR_OVERSPEED_COUNT_LIMIT( (short) 0x0924, NVAddress.UL), 
    NVADDR_OVERSPEED_THRESHOLD( (short) 0x0928, NVAddress.FLOAT), 
    NVADDR_SPEEDLOSS_COUNT_LIMIT( (short) 0x092C, NVAddress.UL), 
    NVADDR_SPEEDLOSS_THRESHOLD( (short) 0x0930, NVAddress.FLOAT), 
    NVADDR_SPEED_CHECK_UPPER_THRESHOLD( (short) 0x0934, NVAddress.FLOAT), 
    NVADDR_SPEED_CHECK_LOWER_THRESHOLD( (short) 0x0938, NVAddress.FLOAT), 
    NVADDR_LRL_SPACING( (short) 0x093C, NVAddress.SL), 
    NVADDR_URL_SPACING( (short) 0x0940, NVAddress.SL), 
    NVADDR_TRAVEL_PROFILE_BASE( (short) 0x0a00, NVAddress.UL), 
    NVADDR_NORMAL_J1( (short) 0x0a00, NVAddress.FLOAT), 
    NVADDR_NORMAL_AAMAX( (short) 0x0a04, NVAddress.FLOAT), 
    NVADDR_NORMAL_J2( (short) 0x0a08, NVAddress.FLOAT), 
    NVADDR_NORMAL_VMAX( (short) 0x0a0c, NVAddress.FLOAT), 
    NVADDR_NORMAL_J3( (short) 0x0a10, NVAddress.FLOAT), 
    NVADDR_NORMAL_ADMAX( (short) 0x0a14, NVAddress.FLOAT), 
    NVADDR_NORMAL_J4( (short) 0x0a18, NVAddress.FLOAT), 
    NVADDR_NORMAL_J4OL( (short) 0x0a1c, NVAddress.UC), 
    NVADDR_FLOOR_POSITION( (short) 0x1000, NVAddress.UC), 
    NVADDR_BUILDING_DOORZONE_COUNT( (short) 0x1001, NVAddress.UC), 
    NVADDR_LOWER_SHAFT_LIMIT_DOORZONE_COUNT( (short) 0x1002, NVAddress.UC), 
    NVADDR_UPPER_SHAFT_LIMIT_DOORZONE_COUNT( (short) 0x1003, NVAddress.UC), 
    NVADDR_BK0_TIMEOUT_LIMIT( (short) 0x1008, NVAddress.UI), 
    NVADDR_BK1_TIMEOUT_LIMIT( (short) 0x100A, NVAddress.UI), 
    NVADDR_CAR_RUN_TIMEOUT_LIMIT( (short) 0x100C, NVAddress.UI), 
    NVADDR_LV_TOGGLE_TIMEOUT_LIMIT( (short) 0x100E, NVAddress.UI), 
    NVADDR_DCS_INSPECTION_EXIT_DELAY( (short) 0x1010, NVAddress.UI), 
    NVADDR_IDLE_STABLE_DELAY( (short) 0x1012, NVAddress.UI), 
    NVADDR_PRERUN_CHECK_TIMEOUT_LIMIT( (short) 0x1014, NVAddress.UI), 
    NVADDR_PRETORQUE_TIMEOUT_LIMIT( (short) 0x1016, NVAddress.UI), 
    NVADDR_DRIVER_BRAKE_OPEN_TIMEOUT_LIMIT( (short) 0x1018, NVAddress.UI), 
    NVADDR_DRIVER_BRAKE_CLOSE_TIMEOUT_LIMIT( (short) 0x101A, NVAddress.UI), 
    NVADDR_DRIVER_DISABLE_DELAY( (short) 0x101C, NVAddress.UI), 
    NVADDR_EMERGENCY_STOP_HOLDOFF( (short) 0x101E, NVAddress.UI), 
    NVADDR_UDR_CLOSE_DELAY( (short) 0x1020, NVAddress.UI), 
    NVADDR_RR_CLOSE_DELAY( (short) 0x1022, NVAddress.UI), 
    NVADDR_RR_CLOSE_TIMEOUT_LIMIT( (short) 0x1024, NVAddress.UI), 
    NVADDR_BRAKE_OPEN_DELAY( (short) 0x1026, NVAddress.UI), 
    NVADDR_BRAKE_OPEN_TIMEOUT_LIMIT( (short) 0x1028, NVAddress.UI), 
    NVADDR_BRAKE_CLOSE_DELAY( (short) 0x102A, NVAddress.UI), 
    NVADDR_BRAKE_CLOSE_TIMEOUT_LIMIT( (short) 0x102C, NVAddress.UI), 
    NVADDR_UDRR_OPEN_DELAY( (short) 0x102E, NVAddress.UI), 
    NVADDR_ADO_SPEED( (short) 0x1030, NVAddress.FLOAT), 
    NVADDR_SHAFT_LIMIT_OVER_SPEED_LIMIT( (short) 0x1034, NVAddress.FLOAT), 
    NVADDR_POSITION_ADJUSTMENT_TOLERANCE( (short) 0x1038, NVAddress.FLOAT), 
    NVADDR_BRAKE_RESISTOR( (short) 0x103C, NVAddress.UC), 
    NVADDR_DRVENF_FAULT_HOLDOFF( (short) 0x103E, NVAddress.UI), 
    NVADDR_BRAKE_JAM_TIME_RATIO( (short) 0x1040, NVAddress.UC), 
    NVADDR_DBDF_MON_DELAY( (short) 0x1042, NVAddress.UI), 
    NVADDR_SHAFT_LIMIT_OVER_SPEED_HOLDOFF( (short) 0x1044, NVAddress.UI), 
    NVADDR_CABIN_FAN_OFF_DELAY( (short) 0x1046, NVAddress.UI), 
    NVADDR_CABIN_FAN_ON_TEMP( (short) 0x1048, NVAddress.UC), 
    NVADDR_CABIN_FAN_OFF_TEMP( (short) 0x1049, NVAddress.UC), 
    NVADDR_BAD_SHAFT_LIMIT_HOLDOFF( (short) 0x104A, NVAddress.UI), 
    NVADDR_RELEVELING_TRIGGER_HOLDOFF( (short) 0x104C, NVAddress.UI), 
    NVADDR_RELEVELING_RETRY( (short) 0x104E, NVAddress.UC), 
    NVADDR_RELEVELING_TIME_LIMIT( (short) 0x1050, NVAddress.UI),
    NVADDR_IN_FIRST_BITPOS( (short) 0x1880, NVAddress.UC), 
    NVADDR_IN_LDZ_BITPOS( (short) 0x1880, NVAddress.UC), 
    NVADDR_IN_UDZ_BITPOS( (short) 0x1881, NVAddress.UC), 
    NVADDR_IN_LVCFB_BITPOS( (short) 0x1882, NVAddress.UC), 
    NVADDR_IN_LSL_BITPOS( (short) 0x1883, NVAddress.UC), 
    NVADDR_IN_USL_BITPOS( (short) 0x1884, NVAddress.UC), 
    NVADDR_IN_INS_BITPOS( (short) 0x1885, NVAddress.UC), 
    NVADDR_IN_INSUP_BITPOS( (short) 0x1886, NVAddress.UC), 
    NVADDR_IN_INSDOWN_BITPOS( (short) 0x1887, NVAddress.UC), 
    NVADDR_IN_ENABLE_BITPOS( (short) 0x1888, NVAddress.UC), 
    NVADDR_IN_DBDE_BITPOS( (short) 0x1889, NVAddress.UC), 
    NVADDR_IN_DBDF_BITPOS( (short) 0x188a, NVAddress.UC),
    NVADDR_IN_BS_BITPOS( (short) 0x188b, NVAddress.UC),
    NVADDR_IN_THM_BITPOS( (short) 0x188c, NVAddress.UC), 
    NVADDR_IN_DFC_BITPOS( (short) 0x188d, NVAddress.UC), 
    NVADDR_IN_DW_BITPOS( (short) 0x188e, NVAddress.UC), 
    NVADDR_IN_HES_BITPOS( (short) 0x188f, NVAddress.UC), 
    NVADDR_IN_CES_BITPOS( (short) 0x1890, NVAddress.UC), 
    NVADDR_IN_DRVBM_BITPOS( (short) 0x1891, NVAddress.UC), 
    NVADDR_IN_DRVOK_BITPOS( (short) 0x1892, NVAddress.UC), 
    NVADDR_IN_ADO_BITPOS( (short) 0x1893, NVAddress.UC), 
    NVADDR_IN_V24MON_BITPOS( (short) 0x1894, NVAddress.UC), 
    NVADDR_IN_ENCF_BITPOS( (short) 0x1895, NVAddress.UC), 
    NVADDR_IN_DRVENF_BITPOS( (short) 0x1896, NVAddress.UC), 
    NVADDR_IN_LRL_BITPOS( (short) 0x1897, NVAddress.UC), 
    NVADDR_IN_URL_BITPOS( (short) 0x1898, NVAddress.UC), 
    NVADDR_IN_DLB_BITPOS( (short) 0x1899, NVAddress.UC),
    NVADDR_IN_EPB_BITPOS( (short) 0x189a, NVAddress.UC),
    NVADDR_IN_BS1_BITPOS( (short) 0x189b, NVAddress.UC), 
    NVADDR_IN_BS2_BITPOS( (short) 0x189c, NVAddress.UC), 
    NVADDR_IN_NDZ_BITPOS( (short) 0x189d, NVAddress.UC),
    NVADDR_IN_UCMTS_BITPOS( (short) 0x189e, NVAddress.UC),
    NVADDR_IN_UCMTS2_BITPOS( (short) 0x189f, NVAddress.UC),
    NVADDR_IN_UCMTS3_BITPOS( (short) 0x18a0, NVAddress.UC),
    NVADDR_IN_LAST_BITPOS( (short) 0x18ef, NVAddress.UC), 
    NVADDR_OUTBP_FIRST( (short) 0x18f0, NVAddress.UC), 
    NVADDR_OUTBP_RL1( (short) 0x18f0, NVAddress.UC), 
    NVADDR_OUTBP_RL2( (short) 0x18f1, NVAddress.UC), 
    NVADDR_OUTBP_RL3( (short) 0x18f2, NVAddress.UC), 
    NVADDR_OUTBP_RL4( (short) 0x18f3, NVAddress.UC), 
    NVADDR_OUTBP_RL5( (short) 0x18f4, NVAddress.UC), 
    NVADDR_OUTBP_RL6( (short) 0x18f5, NVAddress.UC), 
    NVADDR_OUTBP_RL10( (short) 0x18f6, NVAddress.UC), 
    NVADDR_OUTBP_LVCR( (short) 0x18f7, NVAddress.UC), 
    NVADDR_OUTBP_P10_1( (short) 0x18f8, NVAddress.UC), 
    NVADDR_OUTBP_P10_2( (short) 0x18f9, NVAddress.UC), 
    NVADDR_OUTBP_P10_3( (short) 0x18fa, NVAddress.UC), 
    NVADDR_OUTBP_P10_4( (short) 0x18fb, NVAddress.UC), 
    NVADDR_OUTBP_P10_5( (short) 0x18fc, NVAddress.UC), 
    NVADDR_OUTBP_P10_6( (short) 0x18fd, NVAddress.UC), 
    NVADDR_OUTBP_P10_7( (short) 0x18fe, NVAddress.UC), 
    NVADDR_OUTBP_IOWD( (short) 0x18ff, NVAddress.UC), 
    NVADDR_OUTBP_LAST( (short) 0x18ff, NVAddress.UC), 
    NVADDR_BOARD_DESC( (short) 0x1f00, NVAddress.UC);
    
    
    
    
    private static final int UL    = 0;
    private static final int UI    = 1;
    private static final int UC    = 2;
    private static final int SL    = 3;
    private static final int SI    = 4;
    private static final int SC    = 5;
    private static final int FLOAT = 6;
    public final short       address;
    public final int         type;




    NVAddress ( short address, int type ) {
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
