/*--------------------------------------------------------------------------------------------------
|  vecSapphire NVRAM address map
|  
|  Written By: Chris Tou
|
|  Version: 1.00 (2010-10-21)
+--------------------------------------------------------------------------------------------------*/

#ifndef __NVADDR_H__
#define __NVADDR_H__

/*
 --- Remark on NVRAM address usage ---
 1. for all physical quantity related to time, such as speed, acceleration,
    use floating point format (FLOAT) to store natural value (e.g.: 100.0=100.0m/s)
	and use config loader to convert to program optimized data type
 2. for all position related quantity, use signed long integer (INT32) to store count value
 3. for all timer, use UINT16 type to state the millisecond (range 0-60000ms) or
	long second (0-60000 second)
 4. pad 8-bit data (UINT8, INT8) to become 16-bit data in order to preserve address
	alignment, except in known byte field (array) such as i/o crossbar
*/

/* NVRAM address map */
/* --- System --- */
#define NVADDR_0									0x0000	// ul, reserved
#define NVADDR_INSTALLATION_MODE					0x0004	// uc, installation mode
#define NVADDR_CUMULATIVE_CAR_RUN_COUNT				0x0008	// ul, cumulative car run count
#define NVADDR_CONTRACT_NO							0x000c	// ul, encrypted contract nuber
#define NVADDR_NV_FORMAT_VERSION					0x0010	// ui, format version code of NVRAM
#define NVADDR_BOOT_UP_COUNT						0x0014	// ul, counter of number of reboot
#define NVADDR_RTC_YEAR								0x0018	// ui, rtc year
#define NVADDR_RTC_MONTH							0x001A	// uc, rtc month
#define NVADDR_RTC_DAY								0x001B	// uc, rtc day
#define NVADDR_RTC_HOUR								0x001C	// uc, rtc hour
#define NVADDR_RTC_MINUTE							0x001D	// uc, rtc minute
#define NVADDR_RTC_SECOND							0x001E	// uc, rtc second
#define NVADDR_SERIAL_NUMBER						0x0020	// ul, encrypted serial number
#define NVADDR_MCS_BASIC_INTERVAL					0x0024	// ui, interval between MCS Basic push to OCS, unit in ms
#define NVADDR_OCS_WATCHDOG_EN						0x0026	// uc, OCS watchdog enable control
#define NVADDR_SHOW_DEBUG_LOG						0x0028	// uc, show debug log
#define NVADDR_METERED_RUN_COUNT					0x0030	// ul, metered run count 
#define NVADDR_METERED_RUN_COUNT_LIMIT				0x0034	// ul, metered run count limit (activate if >0)
#define NVADDR_MACHINE_TYPE							0x0038	// ui, machine and hardware type (MRL/gearless)
#define NVADDR_DUMMY_SPACE							0x00cc	// ul, dummy space, 4 byte

/* --- shaft position table --- */
#define NVADDR_LSL									0x00f8	// sl, LSL value
#define NVADDR_USL									0x00fc	// sl, USL value
#define NVADDR_LDZ_BASEADDR							0x0100	// sl, LDZ base address
#define NVADDR_UDZ_BASEADDR							0x0300	// sl, UDZ base address
#define NVADDR_UP_LANDING_TUNE_BASEADDR				0x0500	// sl, landing fine-tune for up direction car run
#define NVADDR_DOWN_LANDING_TUNE_BASEADDR			0x0700	// sl, landing fine-tune for down direction car run


/* --- motion related --- */
#define NVADDR_ENCPOS								0x0900	// sl, last encoder position
#define NVADDR_DOORZONE_HEIGHT						0x0904	// f, height/length of doorzone blade (unit in mm)
#define NVADDR_LV_SPACING							0x0908	// f, distance between LV sensor (LDZ and UDZ, unit in mm)
#define NVADDR_MAX_SPEED							0x090C	// f, contract max linear speed (unit in m/s)
#define NVADDR_ANALOG_OUTPUT_ZERO_OFFSET			0x0910	// f, zero-offset of analog output (0..1)
#define NVADDR_QEI_COUNT_PER_REV					0x0914	// ul, number of QEI counts per encoder rev.
#define NVADDR_QEI_MAX_SPEED						0x0918	// ul, qei count per second at max speed
#define NVADDR_QEI_DIR								0x091C	// uc, Encoder direction (QEI input swap)
#define NVADDR_MOTOR_RPM							0x0920	// ul, motor rev per minute at max speed
#define NVADDR_OVERSPEED_COUNT_LIMIT				0x0924	// ul, overspeed count limit (unit in motion TS)
#define NVADDR_OVERSPEED_THRESHOLD					0x0928	// f, overspeed threshold (unit in m/s)
#define NVADDR_SPEEDLOSS_COUNT_LIMIT				0x092C	// ul, speedloss count limit (unit in motion TS)
#define NVADDR_SPEEDLOSS_THRESHOLD					0x0930	// f, speedloss threshold (unit in m/s)
#define NVADDR_SPEED_CHECK_UPPER_THRESHOLD			0x0934	// f, speed check upper threshold (unit in m/s)
#define NVADDR_SPEED_CHECK_LOWER_THRESHOLD			0x0938	// f, speed check lower threshold
#define NVADDR_LRL_SPACING							0x093C	// sl, space between LRL and LDZ
#define NVADDR_URL_SPACING							0x0940	// sl, space between URL and UDZ

/* --- travel profile --- */
#define NVADDR_TRAVEL_PROFILE_BASE					0x0a00	// ul, base address for travel profile
#define NVADDR_TRAVEL_PROFILE_END					0x0aff	// ul, end address for travel profile

/* --- MCS related --- */
#define NVADDR_FLOOR_POSITION						0x1000	// uc, last floor position (floor number)
#define NVADDR_BUILDING_DOORZONE_COUNT				0x1001	// uc, total number of doorzone the building have
#define NVADDR_LOWER_SHAFT_LIMIT_DOORZONE_COUNT		0x1002	// uc, number of doorzone within lower shaft limit
#define NVADDR_UPPER_SHAFT_LIMIT_DOORZONE_COUNT		0x1003	// uc, number of doorzone within upper shaft limit
#define NVADDR_BK0_TIMEOUT_LIMIT					0x1008	// ui, bk0 monitor fail timeout limit
#define NVADDR_BK1_TIMEOUT_LIMIT					0x100A	// ui, bk1 monitor fail timeout limit
#define NVADDR_CAR_RUN_TIMEOUT_LIMIT				0x100C	// ui, car run overtime timeout limit
#define NVADDR_DZ_TOGGLE_TIMEOUT_LIMIT				0x100E	// ui, DZ signal toggle timeout limit
#define NVADDR_DCS_INSPECTION_EXIT_DELAY			0x1010	// ui, delay before exit from DCS inspection
#define NVADDR_IDLE_STABLE_DELAY					0x1012	// ui, idle stable delay
#define NVADDR_PRERUN_CHECK_TIMEOUT_LIMIT			0x1014	// ui, pre-run safety check fail timeout limit
#define NVADDR_PRETORQUE_TIMEOUT_LIMIT				0x1016	// ui, timeout for pretorque waiting
#define NVADDR_DRIVER_BRAKE_OPEN_TIMEOUT_LIMIT		0x1018	// ui, brake open signal from driver timeout limit
#define NVADDR_DRIVER_BRAKE_CLOSE_TIMEOUT_LIMIT		0x101A	// ui, brake close signal from driver timeout limit
#define NVADDR_DRIVER_DISABLE_DELAY					0x101C	// ui, delay before disabling driver
#define NVADDR_EMERGENCY_STOP_HOLDOFF				0x101E	// ui, hold off after emergency stop
#define NVADDR_UDR_CLOSE_DELAY						0x1020	// ui, delay after closing UR/DR (direction relay)
#define NVADDR_RR_CLOSE_DELAY						0x1022	// ui, delay after closing RR (run relay)
#define NVADDR_RR_CLOSE_TIMEOUT_LIMIT				0x1024	// ui, RR close timeout limit
#define NVADDR_BRAKE_OPEN_DELAY						0x1026	// ui, delay after opening brake
#define NVADDR_BRAKE_OPEN_TIMEOUT_LIMIT				0x1028	// ui, brake open fail timeout limit
#define NVADDR_BRAKE_CLOSE_DELAY					0x102A	// ui, delay after closing brake
#define NVADDR_BRAKE_CLOSE_TIMEOUT_LIMIT			0x102C	// ui, brake close fail timeout limit
#define NVADDR_UDRR_OPEN_DELAY						0x102E	// ui, delay after opening UR/DR and RR
#define NVADDR_ADO_SPEED							0x1030	// f, ADO active valid speed, mm/s
#define NVADDR_SHAFT_LIMIT_OVER_SPEED_LIMIT			0x1034	// f, shaft limit overspeed limit, mm/s
#define NVADDR_POSITION_ADJUST_TOLERANCE			0x1038	// f, position adjustment tolerance, mm
#define NVADDR_BRAKE_RESISTOR_TIME					0x103C	// ui, brake resistor enable time
#define NVADDR_DRVENF_FAULT_HOLDOFF					0x103E	// ui, DRVENF fault holdoff time
#define NVADDR_BRAKE_JAM_TIME_RATIO					0x1040	// uc, brake jam:brake ok time ratio
#define NVADDR_DBDF_MON_DELAY						0x1042	// ui, DBDF/ENABLE monitor delay
#define NVADDR_SHAFT_LIMIT_OVER_SPEED_HOLDOFF		0x1044	// ui, holdoff before shaft-limit overspeed triggered
#define NVADDR_CABIN_FAN_OFF_DELAY					0x1046	// ui, delay before cabin fan turn off, unit in second
#define NVADDR_CABIN_FAN_ON_TEMP					0x1048	// uc, temp to turn on cabin fan, UINT8 celcius 
#define NVADDR_CABIN_FAN_OFF_TEMP					0x1049	// uc, temp to turn off cabin fan, UINT8 celcius 
#define NVADDR_BAD_SHAFT_LIMIT_HOLDOFF				0x104A	// ui, holdoff time before bad shaft-limit fail triggered, unit in ms
#define NVADDR_RELEVELING_TRIGGER_HOLDOFF			0x104C	// ui, holdoff time before releveling triggered, unit in ms
#define NVADDR_RELEVELING_RETRY						0x104E	// uc, retry count for releveling
#define NVADDR_RELEVELING_TIME_LIMIT				0x1050	// ui, time limit for releveling, unit in ms
#define NVADDR_SPEED_REF_OUT_DELAY					0x1052	// ui, delay time between brake opened and speed ref output, unit in ms
#define NVADDR_FLOOR_POSITION_PHASE					0x1054	// uc, last floor number phase
#define NVADDR_ZERO_SPEED_STOP_HOLDOFF				0x1056	// ui, holdoff time at zero speed before close brake stop seq
#define NVADDR_REVERSE_RUN_DISP_LIMIT				0x1058	// f, reverse run displacement limit, unit in mm
#define NVADDR_CAR_RUN_TIMEOUT_ENABLE				0x105c	// uc, car run overtime monitor enable
#define NVADDR_DZ_TOGGLE_TIMEOUT_ENABLE				0x105d	// uc, DZ toggle timeout monitor enable
#define NVADDR_DRVENF_MONITOR_ENABLE				0x105e	// uc, driver feedback monitor enable
#define NVADDR_BRAKE_MONITOR_TYPE					0x105f	// uc, brake open/close/running monitor enable and type
#define NVADDR_RELEVELING_ENABLE					0x1060	// uc, releveling enable
#define NVADDR_POSITION_ADJUST_ENABLE				0x1061	// uc, position adjustment enable
#define NVADDR_ADO_ENABLE							0x1062	// uc, ADO enable
#define NVADDR_POSITION_ADJUST_HOLDOFF				0x1064	// ui, position adjustment holdoff time, unit in ms
#define NVADDR_RELEVELING_SENSOR_TYPE				0x1066	// ui, releveling sensor type
#define NVADDR_ANO1FB_ENABLE						0x1068	// uc, ANO1 feedback monitor enable
#define NVADDR_BRAKE_JAM_TIME_LIMIT					0x106a	// ui, brake jam time limit
#define NVADDR_ALERT_RESCUE_FAIL					0x1070	// ui, alert: rescue retry count
#define NVADDR_ALERT_BRAKE_OPEN_FAIL				0x1072	// ui, alert: brake open retry count
#define NVADDR_ALERT_BRAKE_CLOSE_FAIL				0x1074	// ui, alert: brake close retry count
#define NVADDR_ALERT_POWERUP_FAIL					0x1076	// ui, alert: power up fail
#define NVADDR_ALERT_EMERGENCY_STOP					0x1078	// ui, alert: emergency stop
#define NVADDR_ALERT_SPEED_LOSS						0x107a	// ui, alert: speed loss
#define NVADDR_ALERT_ENABLE_CLOSE_FAIL				0x107c	// ui, alert: enable close
#define NVADDR_ALERT_CHECK_RUN_SAFETY_FAIL			0x107e	// ui, alert: enable close
#define NVADDR_INSPECTION_OPTION_ERO				0x1080	// uc, inspection options, ERO
#define NVADDR_INSPECTION_OPTION_INS				0x1082	// uc, inspection options, INS
#define NVADDR_REGOPTION							0x1084	// ui, regulation options (for meeting local regulations)
#define NVADDR_EPB_DELAY							0x1806	// ui, EPB delay
#define NVADDR_VF_DRIVER_TYPE						0x1088	// uc, VF Driver type
#define NVADDR_RR2_CLOSE_OFFSET						0x108a	// si, RR2 close offset time, positive value implies a delay
#define NVADDR_RR2_RELEASE_OFFSET					0x108c	// si, RR2 close offset time, positive value implies a delay
#define NVADDR_EPB_LOADING_THRESHOLD				0x108e	// ui, EPB Loading threshold

#define NVADDR_IN_FIRST_BITPOS						0x1880	// uc, first input bit position
#define NVADDR_IN_LDZ_BITPOS						0x1880	// uc, LDZ input bit position
#define NVADDR_IN_UDZ_BITPOS						0x1881	// uc, UDZ input bit position
#define NVADDR_IN_LVCFB_BITPOS						0x1882	// uc, LVCFB input bit position
#define NVADDR_IN_LSL_BITPOS						0x1883	// uc, LSL input bit position
#define NVADDR_IN_USL_BITPOS						0x1884	// uc, USL input bit position
#define NVADDR_IN_INS_BITPOS						0x1885	// uc, INS input bit position
#define NVADDR_IN_INSUP_BITPOS						0x1886	// uc, INSUP input bit position
#define NVADDR_IN_INSDOWN_BITPOS					0x1887	// uc, INSDOWN input bit position
#define NVADDR_IN_ENABLE_BITPOS						0x1888	// uc, ENABLE input bit position
#define NVADDR_IN_DBDE_BITPOS						0x1889	// uc, DBDE input bit position
#define NVADDR_IN_DBDF_BITPOS						0x188a	// uc, DBDF input bit position
#define NVADDR_IN_BS_BITPOS							0x188b	// uc, BS input bit position
#define NVADDR_IN_THM_BITPOS						0x188c	// uc, THM input bit position
#define NVADDR_IN_DFC_BITPOS						0x188d	// uc, DFC input bit position
#define NVADDR_IN_DW_BITPOS							0x188e	// uc, DW input bit position
#define NVADDR_IN_HES_BITPOS						0x188f	// uc, HES input bit position
#define NVADDR_IN_CES_BITPOS						0x1890	// uc, CES input bit position
#define NVADDR_IN_DRVBM_BITPOS						0x1891	// uc, DRVBM input bit position
#define NVADDR_IN_DRVOK_BITPOS						0x1892	// uc, DRVOK input bit position
#define NVADDR_IN_ADO_BITPOS						0x1893	// uc, ADO input bit position
#define NVADDR_IN_V24MON_BITPOS						0x1894	// uc, V24MON input bit position
#define NVADDR_IN_ENCF_BITPOS						0x1895	// uc, ENCF input bit position
#define NVADDR_IN_DRVENF_BITPOS						0x1896	// uc, DRVEN input bit position
#define NVADDR_IN_LRL_BITPOS						0x1897	// uc, LRL input bit position
#define NVADDR_IN_URL_BITPOS						0x1898	// uc, URL input bit position
#define NVADDR_IN_ANO1FB_BITPOS						0x1899	// uc, ANO1FB input bit position
#define NVADDR_IN_EPB_BITPOS						0x189a	// uc, EPB input bit position
#define NVADDR_IN_BS1_BITPOS						0x189b	// uc, BS1 input bit position
#define NVADDR_IN_BS2_BITPOS						0x189c	// uc, BS2 input bit position
#define NVADDR_IN_NDZ_BITPOS						0x189d	// uc, NDZ input bit position
#define NVADDR_IN_UCMTS_BITPOS						0x189e	// uc, UCMTS input bit position (Routine Braking power test)
#define NVADDR_IN_UCMTS2_BITPOS						0x189f	// uc, UCMTS2 input bit position (Monitor Sub-System test)
#define NVADDR_IN_UCMTS3_BITPOS						0x18a0	// uc, UCMTS3 input bit position (Brake sub-system test)
#define NVADDR_IN_LAST_BITPOS						0x18ef	// uc, last input bit position

#define NVADDR_OUTBP_FIRST							0x18f0	// uc, first output bit position
#define NVADDR_OUTBP_RL1							0x18f0	// uc, RL1 output bit position
#define NVADDR_OUTBP_RL2							0x18f1	// uc, RL2 output bit position
#define NVADDR_OUTBP_RL3							0x18f2	// uc, RL3 output bit position
#define NVADDR_OUTBP_RL4							0x18f3	// uc, RL4 output bit position
#define NVADDR_OUTBP_RL5							0x18f4	// uc, RL5 output bit position
#define NVADDR_OUTBP_RL6							0x18f5	// uc, RL6 output bit position
#define NVADDR_OUTBP_RL10							0x18f6	// uc, RL10 output bit position
#define NVADDR_OUTBP_LVCR							0x18f7	// uc, LVCR output bit position
#define NVADDR_OUTBP_P10_1							0x18f8	// uc, P10.1 output bit position
#define NVADDR_OUTBP_P10_2							0x18f9	// uc, P10.2 output bit position
#define NVADDR_OUTBP_P10_3							0x18fa	// uc, P10.3 output bit position
#define NVADDR_OUTBP_P10_4							0x18fb	// uc, P10.4 output bit position
#define NVADDR_OUTBP_P10_5							0x18fc	// uc, P10.5 output bit position
#define NVADDR_OUTBP_P10_6							0x18fd	// uc, P10.6 output bit position
#define NVADDR_OUTBP_P10_7							0x18fe	// uc, P10.7 output bit position
#define NVADDR_OUTBP_IOWD							0x18ff	// uc, IO watchdog output bit position

/*dcs io*/
#define NVADDR_DCS_FIRST						0x1900	// uc, DCS first input bit position
#define NVADDR_DCS_REDP							0x1900	// uc, REDP input bit position
#define NVADDR_DCS_EDP							0x1901	// uc, EDP input bit position
#define NVADDR_DCS_RDOL							0x1902	// uc, RDOL input bit position
#define NVADDR_DCS_RDCL							0x1903	// uc, RDCL input bit position
#define NVADDR_DCS_RSGS							0x1904	// uc, SGS input bit position
#define NVADDR_DCS_INS							0x1905	// uc, INS input bit position
#define NVADDR_DCS_DOL							0x1906	// uc, DOL input bit position
#define NVADDR_DCS_DCL							0x1907	// uc, DCL input bit position
#define NVADDR_DCS_SGS							0x1908	// uc, SGS input bit position
#define NVADDR_DCS_NDZ							0x1909	// uc, NDZ1 input bit position
#define NVADDR_DCS_INS_UP						0x190a	// uc, INS_UP2 input bit position
#define NVADDR_DCS_INS_DOWN						0x190b	// uc, INS_DOWN input bit position
#define NVADDR_DCS_LDZ							0x190c	// uc, LDZ input bit position
#define NVADDR_DCS_UDZ							0x190d	// uc, UDZ input bit position
#define NVADDR_DCS_LRL							0x190e	// uc, LRL input bit position
#define NVADDR_DCS_URL						    0x190f	// uc, URL input bit position
#define NVADDR_DCS_FLDZ                         0x1910  // uc, FLDZ input bit position
#define NVADDR_DCS_FUDZ                         0x1911  // uc, FUDZ input bit position
#define NVADDR_DCS_RLDZ                         0x1912  // uc, RLDZ input bit position
#define NVADDR_DCS_RUDZ                         0x1913  // uc, RUDZ input bit position                       
#define NVADDR_DCS_DOR							0x1914	// uc, DOR output bit position 
#define NVADDR_DCS_DCR							0x1915	// uc, DCR output bit position        
#define NVADDR_DCS_FR							0x1916	// uc, FR output bit position  
#define NVADDR_DCS_LR							0x1917	// uc, LR output bit position         
#define NVADDR_DCS_NDR							0x1918	// uc, NDR output bit position 
#define NVADDR_DCS_RDOR							0x1919	// uc, RDOR output bit position
#define NVADDR_DCS_RDCR							0x191A	// uc, RDCR output bit position
#define NVADDR_DCS_RNDR							0x191B	// uc, RNDR output bit position
#define NVADDR_OUTBP_LAST					    0x191B	// uc, last output bit position


/* debug/soft info */
#define NVADDR_BOARD_DESC							0x1f00	// uc, board self-description

#endif

