package slecon.setting.installation;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.ValueTextField;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;

public class Shaft extends JPanel {
    private static final long     serialVersionUID = 4203858361825615700L;

    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.installation.Shaft");


    /* ---------------------------------------------------------------------------- */
    private boolean             started = false;
    private SettingPanel<Shaft> settingPanel;
    private JLabel              cpt_door_zone_counts;
    private JLabel              lbl_building_door_zone_counts;
    private ValueTextField      fmt_building_door_zone_counts;
    private JLabel              lbl_door_zones_within_lsl;
    private ValueTextField      fmt_door_zones_within_lsl;
    private JLabel              lbl_door_zones_within_usl;
    private ValueTextField      fmt_door_zones_within_usl;

    /* ---------------------------------------------------------------------------- */
    private JLabel         cpt_door_zone_lengths;
    private JLabel         lbl_door_zone_height;
    private ValueTextField fmt_door_zone_height;
    private JLabel         lbl_ldz_udz_spacing;
    private ValueTextField fmt_ldz_udz_spacing;




    public Shaft() {
        initGUI();

        fmt_building_door_zone_counts.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (fmt_building_door_zone_counts.checkValue()) {
                    Long max = (Long) fmt_building_door_zone_counts.getValue();
                    fmt_door_zones_within_lsl.setScope(Long.class, 0L, max);
                    fmt_door_zones_within_usl.setScope(Long.class, 0L, max);
                }
            }
        });
        fmt_door_zone_height.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (fmt_building_door_zone_counts.checkValue()) {
                    Double max = (Double) fmt_door_zone_height.getValue();
                    fmt_ldz_udz_spacing.setScope(Double.class, 0D, max);
                }
            }
        });

    }


    public void setSettingPanel ( SettingPanel<Shaft> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_door_zone_counts          = new JLabel();
        lbl_building_door_zone_counts = new JLabel();
        fmt_building_door_zone_counts = new ValueTextField();
        lbl_door_zones_within_lsl     = new JLabel();
        fmt_door_zones_within_lsl     = new ValueTextField();
        lbl_door_zones_within_usl     = new JLabel();
        fmt_door_zones_within_usl     = new ValueTextField();
        setCaptionStyle( cpt_door_zone_counts );

        // @CompoentSetting<Fmt>( lbl_building_door_zone_counts , fmt_building_door_zone_counts )
        setTextLabelStyle( lbl_building_door_zone_counts );
        fmt_building_door_zone_counts.setColumns( 10 );
        fmt_building_door_zone_counts.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_building_door_zone_counts.setScope( Long.class, 0L, 127L, true, true );
        fmt_building_door_zone_counts.setEmptyValue( 0L );
        
        // @CompoentSetting<Fmt>( lbl_door_zones_within_lsl , fmt_door_zones_within_lsl )
        setTextLabelStyle( lbl_door_zones_within_lsl );
        fmt_door_zones_within_lsl.setColumns( 10 );
        fmt_door_zones_within_lsl.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_zones_within_lsl.setScope( Long.class, 0L, 127L, true, false );
        fmt_door_zones_within_lsl.setEmptyValue( 0L );

        // @CompoentSetting<Fmt>( lbl_door_zones_within_usl , fmt_door_zones_within_usl )
        setTextLabelStyle( lbl_door_zones_within_usl );
        fmt_door_zones_within_usl.setColumns( 10 );
        fmt_door_zones_within_usl.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_zones_within_usl.setScope( Long.class, 0L, 127L, true, false );
        fmt_door_zones_within_usl.setEmptyValue( 0L );
        add( cpt_door_zone_counts, "gapbottom 18-12, span, top" );
        
        Box vbox_title = Box.createVerticalBox();
        vbox_title.add( lbl_building_door_zone_counts);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_door_zones_within_lsl);
        vbox_title.add( Box.createVerticalStrut(15));
        vbox_title.add( lbl_door_zones_within_usl);
        
        Box vbox_value = Box.createVerticalBox();
        vbox_value.add( fmt_building_door_zone_counts );
        vbox_value.add( Box.createVerticalStrut(10));
        vbox_value.add( fmt_door_zones_within_lsl );
        vbox_value.add( Box.createVerticalStrut(10));
        vbox_value.add( fmt_door_zones_within_usl );
        
        add(vbox_title, "skip 2, span, split 2, left, top, gapright 30");
        add(vbox_value, "wrap 30");
       

        /* ---------------------------------------------------------------------------- */
        cpt_door_zone_lengths = new JLabel();
        lbl_door_zone_height  = new JLabel();
        fmt_door_zone_height  = new ValueTextField();
        lbl_ldz_udz_spacing   = new JLabel();
        fmt_ldz_udz_spacing   = new ValueTextField();
        setCaptionStyle( cpt_door_zone_lengths );

        // @CompoentSetting<Fmt>( lbl_door_zone_height , fmt_door_zone_height )
        setTextLabelStyle( lbl_door_zone_height );
        fmt_door_zone_height.setColumns( 10 );
        fmt_door_zone_height.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_door_zone_height.setScope( Double.class, 0D, null, false, false );
        fmt_door_zone_height.setEmptyValue( 1D );

        // @CompoentSetting<Fmt>( lbl_ldz_udz_spacing , fmt_ldz_udz_spacing )
        setTextLabelStyle( lbl_ldz_udz_spacing );
        fmt_ldz_udz_spacing.setColumns( 10 );
        fmt_ldz_udz_spacing.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_ldz_udz_spacing.setScope( Double.class, 0D, null, false, false );
        fmt_ldz_udz_spacing.setEmptyValue( 1D );
        add( cpt_door_zone_lengths, "gapbottom 18-12, span, top" );
        Box vbox_title1 = Box.createVerticalBox();
        vbox_title1.add( lbl_door_zone_height);
        vbox_title1.add( Box.createVerticalStrut(15));
        vbox_title1.add( lbl_ldz_udz_spacing);
        
        Box vbox_value2 = Box.createVerticalBox();
        vbox_value2.add( fmt_door_zone_height );
        vbox_value2.add( Box.createVerticalStrut(10));
        vbox_value2.add( fmt_ldz_udz_spacing );
        add(vbox_title1, "skip 2, span, split 2, left, top, gapright 30");
        add(vbox_value2, "wrap 30");

        /* ---------------------------------------------------------------------------- */
        bindGroup( "BuildingDoorZoneCounts", lbl_building_door_zone_counts, fmt_building_door_zone_counts );
        bindGroup( "DoorZonesWithinLsl", lbl_door_zones_within_lsl, fmt_door_zones_within_lsl );
        bindGroup( "DoorZonesWithinUsl", lbl_door_zones_within_usl, fmt_door_zones_within_usl );
        bindGroup( "DoorZoneHeight", lbl_door_zone_height, fmt_door_zone_height );
        bindGroup( "LdzUdzSpacing", lbl_ldz_udz_spacing, fmt_ldz_udz_spacing );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_door_zone_counts.setText( getBundleText( "LBL_cpt_door_zone_counts", "Door zone counts" ) );
        lbl_building_door_zone_counts.setText( getBundleText( "LBL_lbl_building_door_zone_counts", "Building door zone counts" ) );
        lbl_door_zones_within_lsl.setText( getBundleText( "LBL_lbl_door_zones_within_lsl", "Door zones within LSL" ) );
        lbl_door_zones_within_usl.setText( getBundleText( "LBL_lbl_door_zones_within_usl", "Door zones within USL" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_door_zone_lengths.setText( getBundleText( "LBL_cpt_door_zone_lengths", "Door zone lengths" ) );
        lbl_door_zone_height.setText( getBundleText( "LBL_lbl_door_zone_height", "Door zone height" ) );
        lbl_ldz_udz_spacing.setText( getBundleText( "LBL_lbl_ldz_udz_spacing", "LDZ-UDZ Spacing" ) );

        /* ---------------------------------------------------------------------------- */
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setTextLabelStyle ( JLabel c ) {
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


    public DoorZoneCountsBean getDoorZoneCountsBean () throws ConvertException {
        if ( ! fmt_building_door_zone_counts.checkValue() )
            throw new ConvertException();
        if ( ! fmt_door_zones_within_lsl.checkValue() )
            throw new ConvertException();
        if ( ! fmt_door_zones_within_usl.checkValue() )
            throw new ConvertException();

        DoorZoneCountsBean bean_doorZoneCounts = new DoorZoneCountsBean();
        bean_doorZoneCounts.setBuildingDoorZoneCounts( ( Long )fmt_building_door_zone_counts.getValue() );
        bean_doorZoneCounts.setDoorZonesWithinLsl( ( Long )fmt_door_zones_within_lsl.getValue() );
        bean_doorZoneCounts.setDoorZonesWithinUsl( ( Long )fmt_door_zones_within_usl.getValue() );
        return bean_doorZoneCounts;
    }


    public DoorZoneLengthsBean getDoorZoneLengthsBean () throws ConvertException {
        if ( ! fmt_door_zone_height.checkValue() )
            throw new ConvertException();
        if ( ! fmt_ldz_udz_spacing.checkValue() )
            throw new ConvertException();

        DoorZoneLengthsBean bean_doorZoneLengths = new DoorZoneLengthsBean();
        bean_doorZoneLengths.setDoorZoneHeight( ( Double )fmt_door_zone_height.getValue() );
        bean_doorZoneLengths.setLdzUdzSpacing( ( Double )fmt_ldz_udz_spacing.getValue() );
        return bean_doorZoneLengths;
    }


    public void setDoorZoneCountsBean ( DoorZoneCountsBean bean_doorZoneCounts ) {
        this.fmt_building_door_zone_counts.setOriginValue( bean_doorZoneCounts.getBuildingDoorZoneCounts() );
        this.fmt_door_zones_within_lsl.setOriginValue( bean_doorZoneCounts.getDoorZonesWithinLsl() );
        this.fmt_door_zones_within_usl.setOriginValue( bean_doorZoneCounts.getDoorZonesWithinUsl() );
    }


    public void setDoorZoneLengthsBean ( DoorZoneLengthsBean bean_doorZoneLengths ) {
        this.fmt_door_zone_height.setOriginValue( bean_doorZoneLengths.getDoorZoneHeight() );
        this.fmt_ldz_udz_spacing.setOriginValue( bean_doorZoneLengths.getLdzUdzSpacing() );
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


    public static Shaft createPanel ( SettingPanel<Shaft> panel ) {
        Shaft gui = new Shaft();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class DoorZoneCountsBean {
        private Long buildingDoorZoneCounts;
        private Long doorZonesWithinLsl;
        private Long doorZonesWithinUsl;




        public Long getBuildingDoorZoneCounts () {
            return this.buildingDoorZoneCounts;
        }


        public Long getDoorZonesWithinLsl () {
            return this.doorZonesWithinLsl;
        }


        public Long getDoorZonesWithinUsl () {
            return this.doorZonesWithinUsl;
        }


        public void setBuildingDoorZoneCounts ( Long buildingDoorZoneCounts ) {
            this.buildingDoorZoneCounts = buildingDoorZoneCounts;
        }


        public void setDoorZonesWithinLsl ( Long doorZonesWithinLsl ) {
            this.doorZonesWithinLsl = doorZonesWithinLsl;
        }


        public void setDoorZonesWithinUsl ( Long doorZonesWithinUsl ) {
            this.doorZonesWithinUsl = doorZonesWithinUsl;
        }
    }




    public static class DoorZoneLengthsBean {
        private Double doorZoneHeight;
        private Double ldzUdzSpacing;




        public Double getDoorZoneHeight () {
            return this.doorZoneHeight;
        }


        public Double getLdzUdzSpacing () {
            return this.ldzUdzSpacing;
        }


        public void setDoorZoneHeight ( Double doorZoneHeight ) {
            this.doorZoneHeight = doorZoneHeight;
        }


        public void setLdzUdzSpacing ( Double ldzUdzSpacing ) {
            this.ldzUdzSpacing = ldzUdzSpacing;
        }
    }
}
