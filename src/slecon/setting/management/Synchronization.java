package slecon.setting.management;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import logic.connection.LiftConnectionBean;
import logic.util.Version;
import logic.util.VersionChangeListener;
import net.miginfocom.swing.MigLayout;
import slecon.SiteInfoTree;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyCheckBox;
import slecon.component.SettingPanel;
import slecon.dialog.connection.SiteInfo;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;

public class Synchronization extends JPanel implements VersionChangeListener {
    /**
     * Text resource.
     */
    static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.management.Synchronization" );

    private boolean                       started = false;
    private SettingPanel<Synchronization> settingPanel;
    private JLabel                        cpt_configuration_synchronization;
    private MyCheckBox                    ebd_event_settings;
    private MyCheckBox                    ebd_module_settings;
    private MyCheckBox                    ebd_floor_settings;
    private JTextField                    txt_target;
    private SiteInfoTree                  tree;
    private LiftConnectionBean            target;




    public Synchronization () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<Synchronization> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[40::40][20::20][32::32][]" ) );
        cpt_configuration_synchronization = new JLabel();
        ebd_event_settings                = new MyCheckBox();
        ebd_module_settings               = new MyCheckBox();
        ebd_floor_settings                = new MyCheckBox();
        setCaptionStyle( cpt_configuration_synchronization );

        // @CompoentSetting( ebd_event_settings )
        setCheckBoxStyle( ebd_event_settings );

        // @CompoentSetting( ebd_module_settings )
        setCheckBoxStyle( ebd_module_settings );

        // @CompoentSetting( ebd_floor_settings )
        setCheckBoxStyle( ebd_floor_settings );
        add( cpt_configuration_synchronization, "gapbottom 18-12, span, aligny center" );
        add( ebd_event_settings, "skip 1, span" );
        add( ebd_module_settings, "skip 1, span" );
        add( ebd_floor_settings, "skip 1, span" );

        final JPanel treePanel = new JPanel( new BorderLayout() );
        treePanel.setBorder( BorderFactory.createLineBorder( StartUI.BORDER_COLOR));
        txt_target = new JTextField();
        txt_target.setColumns( 40 );
        txt_target.setEditable( false );
        txt_target.setCaretColor(Color.WHITE);
        txt_target.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        txt_target.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        txt_target.setForeground(Color.WHITE);
        
        tree = new SiteInfoTree();
        tree.setSiteInfo( SiteInfo.fromSiteManagement() );
        tree.setEditable( false );
        treePanel.add( tree );
        add( txt_target, "skip 2, span, w 300!" );
        add( treePanel, "skip 2, span, w 300!, h 180::, wrap 30-12" );
        tree.getSelectionModel().addTreeSelectionListener( new TreeSelectionListener() {
            @Override
            public void valueChanged ( TreeSelectionEvent e ) {
                if ( tree.getLastSelectedPathComponent() instanceof DefaultMutableTreeNode ) {
                    DefaultMutableTreeNode node = ( ( DefaultMutableTreeNode )tree.getLastSelectedPathComponent() );
                    if ( node.getUserObject() instanceof LiftConnectionBean ) {
                        setTarget( ( LiftConnectionBean )node.getUserObject() );
                    }
                }
            }
        } );

        /* ---------------------------------------------------------------------------- */
        bindGroup( "EventSettings", ebd_event_settings );
        bindGroup( "ModuleSettings", ebd_module_settings );
        bindGroup( "FloorSettings", ebd_floor_settings );
        bindGroup( "TargetElevator", txt_target );
        bindGroup( "TargetElevator", treePanel );
        bindGroup( "TargetElevator", tree );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_configuration_synchronization.setText( getBundleText( "LBL_cpt_configuration_synchronization",
                                                                  "Configuration synchronization" ) );
        ebd_event_settings.setText( getBundleText( "LBL_ebd_event_settings", "Event settings" ) );
        ebd_module_settings.setText( getBundleText( "LBL_ebd_module_settings", "Module settings" ) );
        ebd_floor_settings.setText( getBundleText( "LBL_ebd_floor_settings", "Floor settings" ) );

        /* ---------------------------------------------------------------------------- */
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setCheckBoxStyle ( JCheckBox c ) {
        c.setOpaque( false );
        c.setFont( FontFactory.FONT_12_PLAIN );
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


    public ConfigurationSynchronizationBean getConfigurationSynchronizationBean () throws ConvertException {
        if ( ! ( ebd_event_settings.isSelected() || ebd_floor_settings.isSelected() || ebd_module_settings.isSelected() ) )
            throw new ConvertException( "Please select items." );
        if ( getTarget() == null )
            throw new ConvertException( "Please select Target machine." );

        ConfigurationSynchronizationBean bean_configurationSynchronization = new ConfigurationSynchronizationBean();
        bean_configurationSynchronization.setEventSettings( ebd_event_settings.isSelected() );
        bean_configurationSynchronization.setModuleSettings( ebd_module_settings.isSelected() );
        bean_configurationSynchronization.setFloorSettings( ebd_floor_settings.isSelected() );
        bean_configurationSynchronization.setTargetElevator( getTarget() );
        return bean_configurationSynchronization;
    }


    public void setConfigurationSynchronizationBean ( ConfigurationSynchronizationBean bean_configurationSynchronization ) {
        this.ebd_event_settings.setSelected( bean_configurationSynchronization.getEventSettings() != null
                                             && bean_configurationSynchronization.getEventSettings() == true );
        this.ebd_module_settings.setSelected( bean_configurationSynchronization.getModuleSettings() != null
                                              && bean_configurationSynchronization.getModuleSettings() == true );
        this.ebd_floor_settings.setSelected( bean_configurationSynchronization.getFloorSettings() != null
                                             && bean_configurationSynchronization.getFloorSettings() == true );
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


    public static Synchronization createPanel ( SettingPanel<Synchronization> panel ) {
        Synchronization gui = new Synchronization();
        gui.setSettingPanel(panel);
        return gui;
    }


    public LiftConnectionBean getTarget () {
        return target;
    }


    public void setTarget ( LiftConnectionBean target ) {
        this.target = target;
        if ( target == null ) {
            txt_target.setText( "" );
        } else {
            txt_target.setText( String.format( "(%s) %s ", target.getSite().getName(), target.getName() ) );
        }
    }


    public static class ConfigurationSynchronizationBean {
        private Boolean            eventSettings;
        private Boolean            moduleSettings;
        private Boolean            floorSettings;
        private LiftConnectionBean targetElevator;




        public final Boolean getEventSettings () {
            return eventSettings;
        }


        public final Boolean getModuleSettings () {
            return moduleSettings;
        }


        public final Boolean getFloorSettings () {
            return floorSettings;
        }


        public final LiftConnectionBean getTargetElevator () {
            return targetElevator;
        }


        public final void setEventSettings ( Boolean eventSettings ) {
            this.eventSettings = eventSettings;
        }


        public final void setModuleSettings ( Boolean moduleSettings ) {
            this.moduleSettings = moduleSettings;
        }


        public final void setFloorSettings ( Boolean floorSettings ) {
            this.floorSettings = floorSettings;
        }


        public final void setTargetElevator ( LiftConnectionBean targetElevator ) {
            this.targetElevator = targetElevator;
        }
    }


    @Override
    public void versionChanged(LiftConnectionBean connBean, Version newVersion) {
        tree.repaint();
    }
}
