package configure;
import java.util.ArrayList;

import javax.swing.JPanel;

import base.cfg.ImageFactory;
import logic.util.PageTreeExpression;




public final class PageLookup {
    private static ArrayList<HomeItem>        home;
    private static ArrayList<DisplayTreeItem> inspect;
    private static ArrayList<DisplayTreeItem> setup;

    
    

    public static class DisplayTreeItem {
        public final int index;
        public final String path;
        public final Class<? extends JPanel> panelClass;
        public final PageTreeExpression versionCondition;
        private DisplayTreeItem(int index, String path, Class<? extends JPanel> panelClass, String versionCondition) {
            super();
            this.index = index;
            this.path = path;
            this.panelClass = panelClass;
            this.versionCondition = new PageTreeExpression(versionCondition);
        }
    }
    
    
    
    public static class HomeItem {
        public final Class<?> btnClass;
        public final ImageFactory icon;
        public final String text;
        private HomeItem(Class<?> btnClass, ImageFactory icon, String text) {
            super();
            this.btnClass = btnClass;
            this.icon = icon;
            this.text = text;
        }
    }

    
    static {
        setup = new ArrayList<>();
        setup.add( new DisplayTreeItem( 0x0001, "Installation::Overview", slecon.setting.installation.OverviewSetting.class, "read_mcs && read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0110, "Installation::Motion", slecon.setting.installation.MotionSetting.class, "read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0120, "Installation::Shaft", slecon.setting.installation.ShaftSetting.class, "read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0130, "Installation::DoorZones", slecon.setting.installation.DoorZonesSetting.class, "read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0140, "Installation::Floor text and mapping", slecon.setting.installation.FloorTextAndMappingSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0150, "Installation::Group configuration", slecon.setting.installation.GroupConfigurationSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0160, "Installation::Commission", slecon.setting.installation.CommissionSetting.class, "read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0211, "Setup::Motion::Sequence", slecon.setting.setup.motion.SequenceSetting.class, "read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0212, "Setup::Motion::Speed Profile", slecon.setting.setup.motion.SpeedProfileSetting.class, "read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0213, "Setup::Motion::Timing", slecon.setting.setup.motion.TimingSetting.class, "read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0214, "Setup::Motion::SpeedRegulation", slecon.setting.setup.motion.SpeedRegulationSetting.class, "read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0220, "Setup::Event::Floor", slecon.setting.setup.event.FloorSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0221, "Setup::Event::Others", slecon.setting.setup.event.OthersSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0231, "Setup::I/O::Inputs", slecon.setting.setup.io.InputsA03Setting.class, "mcsBoardVersion[=A03] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0232, "Setup::I/O::Inputs", slecon.setting.setup.io.InputsA05Setting.class, "mcsBoardVersion[=A05] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0232, "Setup::I/O::Inputs", slecon.setting.setup.io.InputsA07Setting.class, "mcsBoardVersion[=A07] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0232, "Setup::I/O::Inputs", slecon.setting.setup.io.InputsC01Setting.class, "mcsBoardVersion[=C01] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0232, "Setup::I/O::Inputs", slecon.setting.setup.io.InputsA01Setting.class, "mcsBoardVersion[=A01] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0232, "Setup::I/O::Inputs", slecon.setting.setup.io.InputsD01Setting.class, "mcsBoardVersion[=D01] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0233, "Setup::I/O::Outputs", slecon.setting.setup.io.OutputsSettingA03.class, "mcsBoardVersion[=A03] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0233, "Setup::I/O::Outputs", slecon.setting.setup.io.OutputsSettingA05.class, "mcsBoardVersion[=A05] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0233, "Setup::I/O::Outputs", slecon.setting.setup.io.OutputsSettingA07.class, "mcsBoardVersion[=A07] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0233, "Setup::I/O::Outputs", slecon.setting.setup.io.OutputsSettingC01.class, "mcsBoardVersion[=C01] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0233, "Setup::I/O::Outputs", slecon.setting.setup.io.OutputsSettingA01.class, "mcsBoardVersion[=A01] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0233, "Setup::I/O::Outputs", slecon.setting.setup.io.OutputsSettingD01.class, "mcsBoardVersion[=D01] && read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0241, "Setup::CabinAndDoor", slecon.setting.setup.door.CabinAndDoorSetting.class, "read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0242, "Setup::Eps_Setup", slecon.setting.setup.eps.EpsSetting.class, "read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0243, "Setup::Advanced", slecon.setting.advanced.AdvancedSetting.class, "read_mcs" ) );
        setup.add( new DisplayTreeItem( 0x0301, "Modules::Message", slecon.setting.modules.MessageSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0302, "Modules::Door Timing", slecon.setting.modules.DoorTimingSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0303, "Modules::Auto Return", slecon.setting.modules.AutoReturnSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0304, "Modules::Earthquake Operation", slecon.setting.modules.EarthQuakeOperationSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0305, "Modules::Temperature Detector Emergency Operation", slecon.setting.modules.TemperatureDetectorEmergencyOperationSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0306, "Modules::Fire Emergency Return Operation", slecon.setting.modules.FireEmergencyReturnOperationSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0307, "Modules::Fireman's Emergency Operation", slecon.setting.modules.FiremansEmergencyOperationSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0308, "Modules::Emergency Battery Operation", slecon.setting.modules.EmergencyBatteryOperationSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0309, "Modules::Emergency Power Operation", slecon.setting.modules.EmergencyPowerOperationSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x030a, "Modules::Attendant Service Operation", slecon.setting.modules.AttendantServiceOperationSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x030b, "Modules::Independent Services", slecon.setting.modules.IndependentServiceSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x030c, "Modules::Parking", slecon.setting.modules.ParkingSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x030d, "Modules::FullLoad Operation", slecon.setting.modules.FullLoadOperationSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x030e, "Modules::Overload protection", slecon.setting.modules.OverloadProtectionSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x030f, "Modules::Fault State Group Suspension", slecon.setting.modules.FaultStateGroupSuspensionSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0310, "Modules::Access Control", slecon.setting.modules.AccessControlSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0311, "Modules::Car Call Option", slecon.setting.modules.CarCallOptionSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0312, "Modules::OCS Lock Up", slecon.setting.modules.OCSLockUpSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0313, "Modules::Disabled Person Operation", slecon.setting.modules.DisabledPersonOperationSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0314, "Modules::Prompt Tone", slecon.setting.modules.PromptToneSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0315, "Modules::Energy Saving", slecon.setting.modules.EnergySavingSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0316, "Modules::Random Run", slecon.setting.modules.RandomRunSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0317, "Modules::Nonstop Operation", slecon.setting.modules.NonStopOperationSetting.class, "read_ocs" ) );
        setup.add( new DisplayTreeItem( 0x0410, "Management::Utilities", slecon.setting.management.UtilitiesSetting.class, "read_manager" ) );
        setup.add( new DisplayTreeItem( 0x0411, "Management::Security", slecon.setting.notcomplete.T3.class, "debugMode" ) );
        setup.add( new DisplayTreeItem( 0x0412, "Management::Synchronization", slecon.setting.management.SynchronizationSetting.class, "read_manager" ) );
        setup.add( new DisplayTreeItem( 0x0413, "Management::Account manager", slecon.setting.notcomplete.T5.class, "debugMode" ) );
        setup.add( new DisplayTreeItem( 0x0414, "Management::Update", slecon.setting.management.UpdateSetting.class, "read_manager" ) );
        setup.add( new DisplayTreeItem( 0x0415, "Management::OcsBackup", slecon.setting.management.OcsBackupSetting.class, "read_manager" ) );
        setup.add( new DisplayTreeItem( 0x0416, "Management::NvImport", slecon.setting.management.NvImportSetting.class, "read_manager" ) );
        setup.add( new DisplayTreeItem( 0x0417, "Management::NvBackup", slecon.setting.management.NvBackupSetting.class, "read_manager" ) );

        inspect = new ArrayList<>();
        inspect.add( new DisplayTreeItem( 0x0100, "Inspect::MotionStatus", slecon.inspect.motion.MotionMain.class, "read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0200, "Inspect::Log", slecon.inspect.logs.Main.class, "read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0300, "Inspect::IOStatus", slecon.inspect.iostatus.IOStatusA03.class, "mcsBoardVersion[=A03] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0301, "Inspect::IOStatus", slecon.inspect.iostatus.IOStatusA05.class, "mcsBoardVersion[=A05] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0302, "Inspect::IOStatus", slecon.inspect.iostatus.IOStatusA07.class, "mcsBoardVersion[=A07] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0303, "Inspect::IOStatus", slecon.inspect.iostatus.IOStatusC01.class, "mcsBoardVersion[=C01] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0303, "Inspect::IOStatus", slecon.inspect.iostatus.IOStatusA01.class, "mcsBoardVersion[=A01] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0303, "Inspect::IOStatus", slecon.inspect.iostatus.IOStatusD01.class, "mcsBoardVersion[=D01] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0400, "Inspect::Door", slecon.inspect.door.DoorStatusA03.class, "mcsBoardVersion[=A03] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0400, "Inspect::Door", slecon.inspect.door.DoorStatusA05.class, "mcsBoardVersion[=A05] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0400, "Inspect::Door", slecon.inspect.door.DoorStatusA07.class, "mcsBoardVersion[=A07] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0400, "Inspect::Door", slecon.inspect.door.DoorStatusC01.class, "mcsBoardVersion[=C01] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0400, "Inspect::Door", slecon.inspect.door.DoorStatusA01.class, "mcsBoardVersion[=A01] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0400, "Inspect::Door", slecon.inspect.door.DoorStatusD01.class, "mcsBoardVersion[=D01] && read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0500, "Inspect::Devices::Hall", slecon.inspect.devices.HallLinkViewPanel.class, "read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0600, "Inspect::Devices::Car", slecon.inspect.devices.CarLinkViewPanel.class, "read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0700, "Inspect::Calls", slecon.inspect.calls.Main.class, "read_inspect" ) );
        inspect.add( new DisplayTreeItem( 0x0900, "Inspect::Group Control", slecon.inspect.logs.GroupControl.class, "debugMode" ) );
        

        home = new ArrayList<>();
        home.add(new HomeItem(slecon.home.dashboard.DashboardPanel.class, ImageFactory.DASHBOARD_ICON, "Dashboard"));
        home.add(new HomeItem(slecon.home.action.SetupAction.class, null, null));
        home.add(new HomeItem(slecon.home.action.InspectAction.class, null, null));
        home.add(new HomeItem(slecon.home.action.PreferenceAction.class, null, null));
    }
    
    
    public synchronized static ArrayList<DisplayTreeItem> getSetupPageClass () {
        return setup;
    }

    
    public synchronized static ArrayList<DisplayTreeItem> getInspectPageClass () {
        return inspect;
    }


    public synchronized static ArrayList<HomeItem> getHomePageClass () {
        return home;
    }
}
