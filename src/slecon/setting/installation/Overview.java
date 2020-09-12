package slecon.setting.installation;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;

import comm.constants.DoorAction;
import comm.constants.McsStatus;
import comm.constants.OcsModule;


public class Overview extends JPanel {
    private static final long     serialVersionUID = 6676455212934115560L;
    private static final ResourceBundle TEXT     = ToolBox.getResourceBundle( "setting.installation.Overview" );

    /* ---------------------------------------------------------------------------- */
    private boolean                started = false;
    private SettingPanel<Overview> settingPanel;

    /* ---------------------------------------------------------------------------- */
    private JLabel cpt_status;
    private JLabel lbl_mcs_status;
    private JLabel lbl_front_dcs_status;
    private JLabel lbl_rear_dcs_status;
    private JLabel lbl_ocs_status;
    private JLabel label_mcs_status;
    private JLabel label_front_dcs_status;
    private JLabel label_rear_dcs_status;
    private JLabel label_ocs_status;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel cpt_identification;
    private JLabel lbl_contract_number;
    private JLabel lbl_serial_number;
    private JLabel lbl_mcs_hardware_version;
    private JLabel lbl_mcs_firmware_version;
    private JLabel lbl_ocs_version;
    private JLabel lbl_dcs_hardware_version;
    private JLabel lbl_dcs_firmware_version;
    private JLabel lbl_eps_hardware_version;
    private JLabel lbl_eps_firmware_version;
    
    private JLabel label_contract_number;
    private JLabel label_serial_number;
    private JLabel label_mcs_hardware_version;
    private JLabel label_mcs_firmware_version;
    private JLabel label_ocs_version;
    private JLabel label_dcs_hardware_version;
    private JLabel label_dcs_firmware_version;
    private JLabel label_eps_hardware_version;
    private JLabel label_eps_firmware_version;
    /* ---------------------------------------------------------------------------- */
    private JLabel             cpt_statistic;
    private JLabel             lbl_run_count;
    private JLabel             label_run_count;
    private JLabel             lbl_up_time;
    private JLabel             label_up_time;
    private JLabel             lbl_last_maintanance;
    private JLabel             label_last_maintanance;
    private IdentificationBean identificationBean;

    private MigLayout         layout;
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("cpt_status", "20 15 100 20 l");
    	styles.put("panelStatus", "20 40 250 120 c");
    	styles.put("cpt_identification", "20 180 100 20 l");
    	styles.put("panelIdentification", "20 205 250 220 c");
    	styles.put("cpt_statistic", "20 450 100 20 l");
    	styles.put("panelStatistic", "20 475 280 100 c");
    }

    public Overview () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<Overview> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        layout = new MigLayout( "nogrid, w 800!, h 650!, gap 0","[left]", "[center]" );
        setLayout( layout );
        
        /* ---------------------------------------------------------------------------- */
        cpt_status       = new JLabel();
        setCaptionStyle( cpt_status );
        add(cpt_status);
        StartUI.setStyle(layout, styles, cpt_status, "cpt_status");
        
        JPanel panelStatus = new JPanel(new MigLayout( "ins 10 15 0 15", "[100!, left]20[100!, left]", "[]5[]5[]5[]" ));
        panelStatus.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelStatus.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR, 1));
        add(panelStatus);
        StartUI.setStyle(layout, styles, panelStatus, "panelStatus");
        
        lbl_mcs_status   = new JLabel();
        lbl_front_dcs_status   = new JLabel();
        lbl_rear_dcs_status   = new JLabel();
        lbl_ocs_status   = new JLabel();
        label_mcs_status = new JLabel();
        label_front_dcs_status = new JLabel();
        label_rear_dcs_status = new JLabel();
        label_ocs_status = new JLabel();
        setLabelTitleStyle( lbl_mcs_status );
        setLabelTitleStyle( lbl_front_dcs_status );
        setLabelTitleStyle( lbl_rear_dcs_status );
        setLabelTitleStyle( lbl_ocs_status );
        setLabelValueStyle( label_mcs_status );
        setLabelValueStyle( label_front_dcs_status );
        setLabelValueStyle( label_rear_dcs_status );
        setLabelValueStyle( label_ocs_status );
        panelStatus.add( lbl_ocs_status, "cell 0 0" );
        panelStatus.add( label_ocs_status, "cell 1 0" );
        panelStatus.add( lbl_mcs_status, "cell 0 1" );
        panelStatus.add( label_mcs_status, "cell 1 1" );
        panelStatus.add( lbl_front_dcs_status, "cell 0 2" );
        panelStatus.add( label_front_dcs_status, "cell 1 2" );
        panelStatus.add( lbl_rear_dcs_status, "cell 0 3" );
        panelStatus.add( label_rear_dcs_status, "cell 1 3" );
        

        /* ---------------------------------------------------------------------------- */
        cpt_identification     = new JLabel();
        setCaptionStyle( cpt_identification );
        add(cpt_identification);
        StartUI.setStyle(layout, styles, cpt_identification, "cpt_identification");
        
        JPanel panelIdentification = new JPanel(new MigLayout( "ins 10 15 0 15", "[100!, left]20[100!, left]", "[]5[]5[]5[]5[]5[]5[]5[]" ));
        panelIdentification.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelIdentification.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR, 1));
        add(panelIdentification);
        StartUI.setStyle(layout, styles, panelIdentification, "panelIdentification");
        lbl_contract_number    	   = new JLabel();
        lbl_serial_number      	   = new JLabel();
        lbl_mcs_hardware_version   = new JLabel();
        lbl_mcs_firmware_version   = new JLabel();
        lbl_ocs_version        	   = new JLabel();
        lbl_dcs_hardware_version   = new JLabel();
        lbl_dcs_firmware_version   = new JLabel();
        lbl_eps_hardware_version   = new JLabel();
        lbl_eps_firmware_version   = new JLabel();
        
        label_contract_number  	   = new JLabel();
        label_serial_number    	   = new JLabel();
        label_mcs_hardware_version = new JLabel();
        label_mcs_firmware_version = new JLabel();
        label_ocs_version      	   = new JLabel();
        label_dcs_hardware_version = new JLabel();
        label_dcs_firmware_version = new JLabel();
        label_eps_hardware_version = new JLabel();
        label_eps_firmware_version = new JLabel();
        
        setLabelTitleStyle( lbl_contract_number );
        setLabelTitleStyle( lbl_serial_number );
        setLabelTitleStyle( lbl_mcs_hardware_version );
        setLabelTitleStyle( lbl_mcs_firmware_version );
        setLabelTitleStyle( lbl_ocs_version );
        setLabelTitleStyle( lbl_dcs_hardware_version );
        setLabelTitleStyle( lbl_dcs_firmware_version );
        setLabelTitleStyle( lbl_eps_hardware_version );
        setLabelTitleStyle( lbl_eps_firmware_version );
        
        setLabelValueStyle( label_contract_number );
        setLabelValueStyle( label_serial_number );
        setLabelValueStyle( label_mcs_hardware_version );
        setLabelValueStyle( label_mcs_firmware_version );
        setLabelValueStyle( label_ocs_version );
        setLabelValueStyle( label_dcs_hardware_version );
        setLabelValueStyle( label_dcs_firmware_version );
        setLabelValueStyle( label_eps_hardware_version );
        setLabelValueStyle( label_eps_firmware_version );
        
        
        panelIdentification.add(lbl_contract_number, "cell 0 0");
        panelIdentification.add(lbl_serial_number, "cell 0 1");
        panelIdentification.add(lbl_ocs_version, "cell 0 2");
        panelIdentification.add(lbl_mcs_hardware_version, "cell 0 3");
        panelIdentification.add(lbl_mcs_firmware_version, "cell 0 4");
        panelIdentification.add(lbl_dcs_hardware_version, "cell 0 5");
        panelIdentification.add(lbl_dcs_firmware_version, "cell 0 6");
        panelIdentification.add(lbl_eps_hardware_version, "cell 0 7");
        panelIdentification.add(lbl_eps_firmware_version, "cell 0 8");
        
        panelIdentification.add(label_contract_number, "cell 1 0");
        panelIdentification.add(label_serial_number, "cell 1 1");
        panelIdentification.add(label_ocs_version, "cell 1 2");
        panelIdentification.add(label_mcs_hardware_version, "cell 1 3");
        panelIdentification.add(label_mcs_firmware_version, "cell 1 4");
        panelIdentification.add(label_dcs_hardware_version, "cell 1 5");
        panelIdentification.add(label_dcs_firmware_version, "cell 1 6");
        panelIdentification.add(label_eps_hardware_version, "cell 1 7");
        panelIdentification.add(label_eps_firmware_version, "cell 1 8");
        
        /* ---------------------------------------------------------------------------- */
        cpt_statistic        = new JLabel();
        setCaptionStyle( cpt_statistic );
        add(cpt_statistic);
        StartUI.setStyle(layout, styles, cpt_statistic, "cpt_statistic");
        
        JPanel panelStatistic = new JPanel(new MigLayout( "ins 10 15 0 15", "[100!, left]20[100!, left]", "[]5[]5[]" ));
        panelStatistic.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelStatistic.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR, 1));
        add(panelStatistic);
        StartUI.setStyle(layout, styles, panelStatistic, "panelStatistic");
        lbl_run_count        = new JLabel();
        label_run_count = new JLabel();
        lbl_up_time          = new JLabel();
        label_up_time = new JLabel();
        lbl_last_maintanance = new JLabel();
        label_last_maintanance = new JLabel();

        setLabelTitleStyle( lbl_run_count );
        setLabelTitleStyle( lbl_up_time );
        setLabelTitleStyle( lbl_last_maintanance );
        setLabelValueStyle( label_run_count );
        setLabelValueStyle( label_up_time );
        setLabelValueStyle( label_last_maintanance );
        panelStatistic.add(lbl_run_count, "cell 0 0");
        panelStatistic.add(lbl_up_time, "cell 0 1");
        panelStatistic.add(lbl_last_maintanance, "cell 0 2");
        panelStatistic.add(label_run_count, "cell 1 0");
        panelStatistic.add(label_up_time, "cell 1 1");
        panelStatistic.add(label_last_maintanance, "cell 1 2");
        
        /* ---------------------------------------------------------------------------- */
        bindGroup( "McsStatus", lbl_mcs_status );
        bindGroup( "DcsStatus", lbl_front_dcs_status );
        bindGroup( "DcsStatus", lbl_rear_dcs_status );
        bindGroup( "OcsStatus", lbl_ocs_status );
        bindGroup( "ContractNumber", lbl_contract_number );
        bindGroup( "SerialNumber", lbl_serial_number );
        bindGroup( "HardwareVersion", lbl_mcs_hardware_version );
        bindGroup( "McsVersion", lbl_mcs_firmware_version );
        bindGroup( "OcsVersion", lbl_ocs_version );
        bindGroup( "RunCount", lbl_run_count );
        bindGroup( "UpTime", lbl_up_time );
        bindGroup( "LastMaintanance", lbl_last_maintanance );
 
        loadI18N();
        revalidate();
        
    }


    private void loadI18N () {
        /* ---------------------------------------------------------------------------- */
        cpt_status.setText( getBundleText( "LBL_cpt_status", "Status" ) );
        lbl_mcs_status.setText( getBundleText( "LBL_lbl_mcs_status", "MCS Status" ) );
        lbl_front_dcs_status.setText( getBundleText( "LBL_lbl_front_dcs_status", "Front DCS Status" ) );
        lbl_rear_dcs_status.setText( getBundleText( "LBL_lbl_rear_dcs_status", "Rear DCS Status" ) );
        lbl_ocs_status.setText( getBundleText( "LBL_lbl_ocs_status", "OCS Status" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_identification.setText( getBundleText( "LBL_cpt_identification", "Identification" ) );
        lbl_contract_number.setText( getBundleText( "LBL_lbl_contract_number", "Contract Number" ) );
        lbl_serial_number.setText( getBundleText( "LBL_lbl_serial_number", "Serial Number" ) );
        lbl_mcs_hardware_version.setText( getBundleText( "LBL_lbl_mcs_hardware_version", "MCS Hardware Version" ) );
        lbl_mcs_firmware_version.setText( getBundleText( "LBL_lbl_mcs_firmware_version", "MCS Firmware Version" ) );
        lbl_ocs_version.setText( getBundleText( "LBL_lbl_ocs_version", "OCS Version" ) );
        lbl_dcs_hardware_version.setText( getBundleText( "LBL_lbl_dcs_hardware_version", "DCS Hardware Version" ) );
        lbl_dcs_firmware_version.setText( getBundleText( "LBL_lbl_dcs_firmware_version", "DCS Firmware Version" ) );
        lbl_eps_hardware_version.setText( getBundleText( "LBL_lbl_eps_hardware_version", "EPS Hardware Version" ) );
        lbl_eps_firmware_version.setText( getBundleText( "LBL_lbl_eps_firmware_version", "EPS Firmware Version" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_statistic.setText( getBundleText( "LBL_cpt_statistic", "Statistic" ) );
        lbl_run_count.setText( getBundleText( "LBL_lbl_run_count", "Run Count" ) );
        lbl_up_time.setText( getBundleText( "LBL_lbl_up_time", "Up Time" ) );
        lbl_last_maintanance.setText( getBundleText( "LBL_lbl_last_maintanance", "Last Maintanance" ) );

        /* ---------------------------------------------------------------------------- */
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_11_BOLD );
        c.setForeground(Color.WHITE);
    }


    private void setLabelTitleStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_11_BOLD );
        c.setForeground(Color.WHITE);
    }
    
    private void setLabelValueStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_11_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void bindGroup ( final String detailKey, final JComponent... list ) {
        if ( detailKey != null && detailKey.trim().length() > 0 ) {
            for ( JComponent c : list ) {
                c.addMouseListener( new MouseAdapter() {
                    String detailText;
                    @Override
                    public synchronized void mouseEntered ( MouseEvent evt ) {
                        if ( settingPanel != null ) {
                            if ( detailText == null ) {
                                try {
                                    detailText = TEXT.getString( "Description_" + detailKey );
                                } catch ( Exception e ) {
                                    detailText = "No description here! Be careful.";
                                }
                            }
                            settingPanel.setDescription( detailText );
                        }
                    }
                    @Override
                    public void mouseExited ( MouseEvent e ) {
                        if ( settingPanel != null ) {
                            settingPanel.setDescription( null );
                        }
                    }
                } );
            }
        }
    }


    public StatusBean getStatusBean () throws ConvertException {
        StatusBean bean_status = new StatusBean();
        return bean_status;
    }


    public StatisticBean getStatisticBean () throws ConvertException {
        StatisticBean bean_statistic = new StatisticBean();
        return bean_statistic;
    }


    public IdentificationBean getIdentificationBean () throws ConvertException {
        IdentificationBean bean_identification = new IdentificationBean();
        return bean_identification;
    }


    public void setStatusBean ( StatusBean bean_status ) {
        label_mcs_status.setText( bean_status.getMcsStatus() == null
                                  ? "---"
                                  : bean_status.getMcsStatus().toString() );
        label_ocs_status.setText( bean_status.getOcsStatus() == null
                                  ? "---"
                                  : bean_status.getOcsStatus().toString() );
        label_front_dcs_status.setText( bean_status.getFrontdoorAction() == null
                                  ? "---"
                                  : bean_status.getFrontdoorAction().toString() );
        label_rear_dcs_status.setText( bean_status.getReardoorAction() == null
                ? "---"
                : bean_status.getReardoorAction().toString() );
    }


    public void setStatisticBean ( StatisticBean bean_statistic ) {
        label_run_count.setText( bean_statistic.getRunCount() );
        label_up_time.setText( bean_statistic.getUpTime());
        label_last_maintanance.setText( bean_statistic.getLastMaintenance() );
    }


    public void setIdentificationBean ( IdentificationBean bean_identification ) {
    	label_contract_number.setText( bean_identification == null || bean_identification.getMcsContractVersion() == null
                ? "---"
                : bean_identification.getMcsContractVersion() );
		label_serial_number.setText( bean_identification == null || bean_identification.getMcsSerialNumber() == null
		              ? "---"
		              : bean_identification.getMcsSerialNumber() );
    	label_mcs_hardware_version.setText( bean_identification == null || bean_identification.getMcsHardwareVersion() == null
                ? "---"
                : bean_identification.getMcsHardwareVersion() );
    	label_mcs_firmware_version.setText( bean_identification == null || bean_identification.getMcsFirmwareVersion() == null
                                   ? "---"
                                   : bean_identification.getMcsFirmwareVersion() );
        label_ocs_version.setText( bean_identification == null || bean_identification.getOcsVersion() == null
                                   ? "---"
                                   : bean_identification.getOcsVersion() );
        label_dcs_hardware_version.setText( bean_identification == null || bean_identification.getDcsHardwareVersion() == null
                ? "---"
                : bean_identification.getDcsHardwareVersion() );
        label_dcs_firmware_version.setText( bean_identification == null || bean_identification.getDcsFirmwareVersion() == null
                ? "---"
                : bean_identification.getDcsFirmwareVersion() );
        label_eps_hardware_version.setText( bean_identification == null || bean_identification.getEpsHardwareVersion() == null
                ? "---"
                : bean_identification.getEpsHardwareVersion() );
        label_eps_firmware_version.setText( bean_identification == null || bean_identification.getEpsFirmwareVersion() == null
                ? "---"
                : bean_identification.getEpsFirmwareVersion() );
    }


    public String getBundleText ( String key, String defaultValue ) {
        String result;
        try {
            result = TEXT.getString( key );
        } catch ( Exception e ) {
            result = defaultValue;
        }
        return result;
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static Overview createPanel ( SettingPanel<Overview> panel ) {
        Overview gui = new Overview();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class IdentificationBean {
        private String ocsVersion;
        private String mcsContractVersion;
        private String mcsFirmwareVersion;
        private String mcsHardwareVersion;
        private String mcsSerialNumber;
        private String dcsFirmwareVersion;
        private String dcsHardwareVersion;
        private String epsFirmwareVersion;
        private String epsHardwareVersion;
		public String getOcsVersion() {
			return ocsVersion;
		}
		public void setOcsVersion(String ocsVersion) {
			this.ocsVersion = ocsVersion;
		}
		public String getMcsContractVersion() {
			return mcsContractVersion;
		}
		public void setMcsContractVersion(String mcsContractVersion) {
			this.mcsContractVersion = mcsContractVersion;
		}
		public String getMcsFirmwareVersion() {
			return mcsFirmwareVersion;
		}
		public void setMcsFirmwareVersion(String mcsFirmwareVersion) {
			this.mcsFirmwareVersion = mcsFirmwareVersion;
		}
		public String getMcsHardwareVersion() {
			return mcsHardwareVersion;
		}
		public void setMcsHardwareVersion(String mcsHardwareVersion) {
			this.mcsHardwareVersion = mcsHardwareVersion;
		}
		public String getMcsSerialNumber() {
			return mcsSerialNumber;
		}
		public void setMcsSerialNumber(String mcsSerialNumber) {
			this.mcsSerialNumber = mcsSerialNumber;
		}
		public String getDcsFirmwareVersion() {
			return dcsFirmwareVersion;
		}
		public void setDcsFirmwareVersion(String dcsFirmwareVersion) {
			this.dcsFirmwareVersion = dcsFirmwareVersion;
		}
		public String getDcsHardwareVersion() {
			return dcsHardwareVersion;
		}
		public void setDcsHardwareVersion(String dcsHardwareVersion) {
			this.dcsHardwareVersion = dcsHardwareVersion;
		}
		public String getEpsFirmwareVersion() {
			return epsFirmwareVersion;
		}
		public void setEpsFirmwareVersion(String epsFirmwareVersion) {
			this.epsFirmwareVersion = epsFirmwareVersion;
		}
		public String getEpsHardwareVersion() {
			return epsHardwareVersion;
		}
		public void setEpsHardwareVersion(String epsHardwareVersion) {
			this.epsHardwareVersion = epsHardwareVersion;
		}
        
    }




    public static class StatisticBean {
        private String runCount;
        private String upTime;
        private String lastMaintenance;

        
        
        
        public final String getRunCount() {
            return runCount;
        }

        
        public final String getUpTime() {
            return upTime;
        }

        
        public final String getLastMaintenance() {
            return lastMaintenance;
        }

        
        public final void setRunCount(String runCount) {
            this.runCount = runCount;
        }
        

        public final void setUpTime(String upTime) {
            this.upTime = upTime;
        }

        
        public final void setLastMaintenance(String lastMaintenance) {
            this.lastMaintenance = lastMaintenance;
        }
    }




    public static class StatusBean {
        DoorAction  frontdoorAction;
		DoorAction  reardoorAction;
        McsStatus   mcsStatus;
        OcsModule   ocsStatus;
        
        
        public final McsStatus getMcsStatus() {
            return mcsStatus;
        }
        public final OcsModule getOcsStatus() {
            return ocsStatus;
        }

        public final void setMcsStatus(McsStatus mcsStatus) {
            this.mcsStatus = mcsStatus;
        }
        public final void setOcsStatus(OcsModule ocsStatus) {
            this.ocsStatus = ocsStatus;
        }
        public DoorAction getFrontdoorAction() {
 			return frontdoorAction;
 		}
 		public void setFrontdoorAction(DoorAction frontdoorAction) {
 			this.frontdoorAction = frontdoorAction;
 		}
 		public DoorAction getReardoorAction() {
 			return reardoorAction;
 		}
 		public void setReardoorAction(DoorAction reardoorAction) {
 			this.reardoorAction = reardoorAction;
 		}
    }
}
