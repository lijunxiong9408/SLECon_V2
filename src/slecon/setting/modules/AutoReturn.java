package slecon.setting.modules;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.color.ColorSpace;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ColorConvertOp;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.ValueTextField;
import slecon.interfaces.ConvertException;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import org.jdesktop.swingx.image.ColorTintFilter;

import base.cfg.FontFactory;




/**
 * Setup -> Module -> Auto Return.
 */
public class AutoReturn extends JPanel {
    private static final long serialVersionUID = -4702325303305313980L;
    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT    = ToolBox.getResourceBundle( "setting.module.AutoReturn" );
    private boolean                    started = false;
    private SettingPanel<AutoReturn>   settingPanel;
    private JLabel                     cpt_general;
    private ValueCheckBox              ebd_enabled;
    private JLabel                     lbl_activation_timer;
    private ValueTextField             fmt_activation_timer;
    private ValueCheckBox              ebd_enabled_group_auto_return;
    private JLabel                     lbl_return_floor;
    private MyComboBox			       cbo_return_floor;
    private JTable                     tbl_floor_list;
    private JLabel                     lbl_priority_list_size;
    private MyComboBox        		   cbo_priority_list_size;
    private MyComboBox 				   cbo_recommended_floor_list;




    public AutoReturn () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<AutoReturn> panel ) {
        this.settingPanel = panel;
    }
    
    DefaultTableCellRenderer normalRenderer = new DefaultTableCellRenderer() {
        private static final long serialVersionUID = 8202677196161198377L;
        public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                         int column ) {
            JLabel renderedLabel = ( JLabel )super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            renderedLabel.setHorizontalAlignment( SwingConstants.CENTER );
            renderedLabel.setForeground(Color.WHITE);
            if(isSelected) {
            	setBackground(StartUI.BORDER_COLOR);
            }else {
            	setBackground(Color.GRAY);
            }
            return renderedLabel;
        }
    };
    
    private static void setTableHeaderColor(JTable table, int columnIndex, Color c) {
        TableColumn column = table.getTableHeader().getColumnModel()
                .getColumn(columnIndex);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(c);
        cellRenderer.setFont(FontFactory.FONT_12_BOLD);
        cellRenderer.setHorizontalTextPosition(JLabel.CENTER);
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        cellRenderer.setForeground(Color.WHITE);
        column.setHeaderRenderer(cellRenderer);
    }

    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12, height 600!", "[30::30][30::30][100::100][150::150][]" ) );
        cpt_general                   = new JLabel();
        ebd_enabled                   = new ValueCheckBox();
        lbl_activation_timer          = new JLabel();
        fmt_activation_timer          = new ValueTextField();
        ebd_enabled_group_auto_return = new ValueCheckBox();
        lbl_return_floor              = new JLabel();
        cbo_return_floor              = new MyComboBox();
        lbl_priority_list_size        = new JLabel();
        cbo_priority_list_size        = new MyComboBox(new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});

        cbo_return_floor.setPreferredSize(new Dimension(100, 20));
        cbo_priority_list_size.setPreferredSize(new Dimension(100, 20));
        
        String[] column = {
            "P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8", "P9", "P10", "P11", "P12", "P13", "P14", "P15", "P16"
        };
        tbl_floor_list = new JTable( new DefaultTableModel( column, 1 ) );
        tbl_floor_list.setCellSelectionEnabled( true );
        tbl_floor_list.setRowSelectionAllowed( true );
        tbl_floor_list.setGridColor(StartUI.SUB_BACKGROUND_COLOR);
        tbl_floor_list.setShowGrid(true);
        tbl_floor_list.setRowHeight(20);
        for(int i = 0; i < tbl_floor_list.getColumnCount(); i++) {
        	setTableHeaderColor(tbl_floor_list, i, StartUI.BORDER_COLOR);
        }
        JTableHeader head =  tbl_floor_list.getTableHeader();
        head.setPreferredSize(new Dimension(1, 30));
        
        cbo_recommended_floor_list = new MyComboBox();
        for ( int i = 0 ; i < tbl_floor_list.getColumnCount() ; i++ ) {
            tbl_floor_list.getColumnModel().getColumn( i ).setCellEditor( new DefaultCellEditor( cbo_recommended_floor_list ) );
            tbl_floor_list.getColumnModel().getColumn( i ).setMaxWidth( 55 );
            tbl_floor_list.getColumnModel().getColumn( i ).setCellRenderer(normalRenderer);
        }
        setCaptionStyle( cpt_general );

        // @CompoentSetting( ebd_enabled )

        // @CompoentSetting<Fmt>( lbl_activation_timer , fmt_activation_timer )
        setTextLabelStyle( lbl_activation_timer );
        fmt_activation_timer.setColumns( 10 );
        fmt_activation_timer.setHorizontalAlignment( SwingConstants.RIGHT );
        fmt_activation_timer.setScope( Long.class, 0L, null, true, false );
        fmt_activation_timer.setEmptyValue( 1L );

        // @CompoentSetting( ebd_enabled_group_auto_return )

        setComboBoxLabelStyle( lbl_return_floor );
        setComboBoxValueStyle( cbo_return_floor );
        
        // @CompoentSetting( lbl_priority_list_size, cbo_priority_list_size )
        setComboBoxLabelStyle(lbl_priority_list_size);
        setComboBoxValueStyle( cbo_priority_list_size );
        
        add( cpt_general, "gapbottom 18-12, span, aligny center, top" );
        add( ebd_enabled, "skip 1, span, top" );
        add( lbl_activation_timer, "skip 2, span 1, left, top" );
        add( fmt_activation_timer, "span 1, left, wrap, top" );
        add( ebd_enabled_group_auto_return, "skip 1, span, top" );
        add( lbl_return_floor, "skip 2, span 1, left, top" );
        add( cbo_return_floor, "span 1, left, wrap, top" );

        JPanel panel_floorList = new JPanel( new BorderLayout() );
        panel_floorList.setBorder( BorderFactory.createLineBorder( StartUI.BORDER_COLOR ) );
        panel_floorList.add( tbl_floor_list.getTableHeader(), BorderLayout.NORTH );
        panel_floorList.add( tbl_floor_list );

        final LockableUI lockUI = new LockableUI();
        lockUI.setLockedCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );

        BufferedImageOpEffect effect1 = new BufferedImageOpEffect( new ColorConvertOp( ColorSpace.getInstance( ColorSpace.CS_GRAY ), null ) );
        BufferedImageOpEffect effect2 = new BufferedImageOpEffect( new ColorTintFilter( Color.GRAY, 0.2f ) );
        lockUI.setLockedEffects( effect1, effect2 );

        JXLayer<JComponent> layer = new JXLayer<JComponent>( panel_floorList, lockUI );
        add( layer, "skip 1, span, wrap, top" );
        
        add( lbl_priority_list_size, "skip 2, span 1, left, top" );
        add( cbo_priority_list_size, "span 1, left, wrap, top" );
        

        /* ---------------------------------------------------------------------------- */
        bindGroup( "auto_return_enabled", ebd_enabled );
        bindGroup( "activation_time", lbl_activation_timer, fmt_activation_timer );
        bindGroup( "enable_group_auto_return", ebd_enabled_group_auto_return );
        bindGroup( "return_floor", lbl_return_floor, cbo_return_floor );
        bindGroup( "return_floor_list", tbl_floor_list );
        bindGroup( "return_floor_list", tbl_floor_list.getTableHeader() );
        bindGroup( "priority_list_size", lbl_priority_list_size, cbo_priority_list_size);
        bindGroup( new AbstractButton[]{ ebd_enabled }, lbl_activation_timer, fmt_activation_timer, lbl_priority_list_size, ebd_enabled_group_auto_return );

        final Runnable group_auto_return_state_change = new Runnable() {
            @Override
            public void run () {
                if ( ebd_enabled_group_auto_return.isEnabled() ) {
                    if ( ebd_enabled_group_auto_return.isSelected() ) {
                        lbl_return_floor.setEnabled( false );
                        cbo_return_floor.setEnabled( false );
                        lockUI.setLocked( false );
                        lbl_priority_list_size.setEnabled( true );
                        cbo_priority_list_size.setEnabled( true );
                    } else {
                        lbl_return_floor.setEnabled( true );
                        cbo_return_floor.setEnabled( true );
                        lockUI.setLocked( true );
                        lbl_priority_list_size.setEnabled( false );
                        cbo_priority_list_size.setEnabled( false );
                    }
                } else {
                    cbo_return_floor.setEnabled( false );
                    lockUI.setLocked( true );
                    cbo_priority_list_size.setEnabled( false );
                }
            }
        };
        ebd_enabled_group_auto_return.addPropertyChangeListener( "enabled", new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                group_auto_return_state_change.run();
            }
        } );
        ebd_enabled_group_auto_return.addItemListener( new ItemListener() {
            @Override
            public void itemStateChanged ( ItemEvent e ) {
                group_auto_return_state_change.run();
            }
        } );
        group_auto_return_state_change.run();
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "auto_return_enabled" ) );
        lbl_activation_timer.setText( TEXT.getString( "activation_time" ) );
        ebd_enabled_group_auto_return.setText( TEXT.getString( "enable_group_auto_return" ) );
        lbl_return_floor.setText( TEXT.getString( "return_floor" ) );
        lbl_priority_list_size.setText( TEXT.getString( "priority_list_size" ) );
        
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

    private void setComboBoxLabelStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }

    private void setComboBoxValueStyle ( JComboBox<?> c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void bindGroup ( final AbstractButton[] primary, final JComponent... list ) {
        for ( AbstractButton p : primary ) {
            p.addItemListener( new ItemListener() {
                public void itemStateChanged ( ItemEvent e ) {
                    boolean enabled = false;
                    for ( AbstractButton p : primary ) {
                        if ( p.isSelected() && p.isEnabled() ) {
                            enabled = true;
                            break;
                        }
                    }
                    for ( JComponent c : list )
                        c.setEnabled( enabled );
                }
            } );
            p.addPropertyChangeListener( "enabled", new PropertyChangeListener() {
                @Override
                public void propertyChange ( PropertyChangeEvent evt ) {
                    boolean enabled = false;
                    for ( AbstractButton p : primary ) {
                        if ( p.isSelected() && p.isEnabled() ) {
                            enabled = true;
                            break;
                        }
                    }
                    for ( JComponent c : list )
                        c.setEnabled( enabled );
                }
            } );
        }

        boolean enabled = false;
        for ( AbstractButton p : primary ) {
            if ( p.isSelected() && p.isEnabled() ) {
                enabled = true;
                break;
            }
        }
        for ( JComponent c : list )
            c.setEnabled( enabled );
    }


    private void bindGroup ( final String detailKey, final JComponent... list ) {
        for ( JComponent c : list ) {
            c.addMouseListener( new MouseAdapter() {
                @Override
                public synchronized void mouseEntered ( MouseEvent evt ) {
                    if ( settingPanel != null )
                        settingPanel.setDescription( TEXT.getString( detailKey + "_description" ) );
                }
                @Override
                public void mouseExited ( MouseEvent e ) {
                    if ( settingPanel != null )
                        settingPanel.setDescription( null );
                }
            } );
        }
    }


    public GeneralBean getGeneralBean () throws ConvertException {
        if ( ! fmt_activation_timer.checkValue() )
            throw new ConvertException();

        GeneralBean bean_general = new GeneralBean();
        bean_general.setEnabled( ebd_enabled.isSelected() );
        bean_general.setActivationTimer( ( Long )fmt_activation_timer.getValue() );
        bean_general.setEnabledGroupAutoReturn( ebd_enabled_group_auto_return.isSelected() );
        bean_general.setReturnFloor( ( FloorText )cbo_return_floor.getSelectedItem() );
        bean_general.setPriorityListSize( (Integer) cbo_priority_list_size.getSelectedItem() );

        FloorText[] floorList = new FloorText[ 16 ];
        try {
            for ( int i = 0 ; i < 16 ; i++ )
                floorList[ i ] = ( FloorText )tbl_floor_list.getModel().getValueAt( 0, i );
            bean_general.setFloorList( floorList );
        } catch ( /* NullPointerException | ClassCastException */ Exception e ) {
            throw new ConvertException();
        }
        return bean_general;
    }




    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
        this.fmt_activation_timer.setOriginValue( bean_general.getActivationTimer() );
        this.ebd_enabled_group_auto_return.setOriginSelected( bean_general.getEnabledGroupAutoReturn() != null
                                                        && bean_general.getEnabledGroupAutoReturn() == true );
        this.cbo_return_floor.setSelectedItem( bean_general.getReturnFloor() );
        this.cbo_priority_list_size.setSelectedItem(bean_general.getPriorityListSize());
        for ( int i = 0 ; i < 16 ; i++ ) {
            tbl_floor_list.setValueAt( bean_general.getFloorList()[ i ], 0, i );
        }
    }




    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static AutoReturn createPanel ( SettingPanel<AutoReturn> panel ) {
        AutoReturn gui = new AutoReturn();
        gui.setSettingPanel( panel );
        return gui;
    }


    public void setFloorText ( ArrayList<FloorText> list ) {
        cbo_return_floor.removeAllItems();
        cbo_recommended_floor_list.removeAllItems();
        cbo_recommended_floor_list.addItem( null );
        for ( FloorText text : list ) {
            cbo_return_floor.addItem( text );
            cbo_recommended_floor_list.addItem( text );
        }
    }

    public static class GeneralBean {
        private Boolean     enabled;
        private Long        activationTimer;
        private Boolean     enabledGroupAutoReturn;
        private FloorText   returnFloor;
        private FloorText[] floorList;
        private Integer     PriorityListSize;




        public Boolean getEnabled () {
            return enabled;
        }


        public Long getActivationTimer () {
            return activationTimer;
        }


        public Boolean getEnabledGroupAutoReturn () {
            return enabledGroupAutoReturn;
        }


        public FloorText getReturnFloor () {
            return returnFloor;
        }


        public FloorText[] getFloorList () {
            return floorList;
        }


        public final Integer getPriorityListSize() {
            return PriorityListSize;
        }


        public void setEnabled ( Boolean enabled ) {
            this.enabled = enabled;
        }


        public void setActivationTimer ( Long activationTimer ) {
            this.activationTimer = activationTimer;
        }


        public void setEnabledGroupAutoReturn ( Boolean enabledGroupAutoReturn ) {
            this.enabledGroupAutoReturn = enabledGroupAutoReturn;
        }


        public void setReturnFloor ( FloorText returnFloor ) {
            this.returnFloor = returnFloor;
        }


        public void setFloorList ( FloorText[] floorList ) {
            this.floorList = floorList;
        }


        public final void setPriorityListSize(Integer priorityListSize) {
            PriorityListSize = priorityListSize;
        }
    }


}
