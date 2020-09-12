package slecon.setting.modules;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.ColorConvertOp;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import logic.EventID;
import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyCheckBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.component.iobar.IOBar;
import slecon.component.iobardialog.IOEditorDialog;
import slecon.interfaces.ConvertException;
import slecon.setting.modules.AccessControl.CallTableModel;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import org.jdesktop.swingx.image.ColorTintFilter;

import base.cfg.FontFactory;

class AccessCheckBoxCellRenderer implements TableCellRenderer{
	CallTableModel tableModel;
	JTableHeader tableHeader;
	MyCheckBox myCheckBox = null;
	
	public AccessCheckBoxCellRenderer(JTable table) {
		// TODO Auto-generated constructor stub
		this.tableModel = (CallTableModel)table.getModel();
		this.tableHeader = table.getTableHeader();
		myCheckBox = new MyCheckBox();
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		
		myCheckBox.setHorizontalAlignment(JLabel.CENTER);
		myCheckBox.setSelected((Boolean)value);
		
		JComponent component = myCheckBox;
        component.setBackground(StartUI.MAIN_BACKGROUND_COLOR);
        component.setFont(tableHeader.getFont());
        
		return component;
	}
	
}


/**
 * Setup -> Module -> Access Control.
 */
public class AccessControl extends JPanel {
    private static final long serialVersionUID = 4412644778595410536L;
    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT    = ToolBox.getResourceBundle( "setting.module.AccessControl" );
    private boolean                     started = false;
    private SettingPanel<AccessControl> settingPanel;
    private JLabel                      cpt_general;
    private ValueCheckBox               ebd_enabled;

    /* ---------------------------------------------------------------------------- */
    private JLabel cpt_i_o_settings;
    private IOBar  io_access_control_a_switch;
    private IOBar  io_access_control_b_switch;
    private IOBar  io_access_control_c_switch;
    private IOBar  io_access_control_d_switch;
    private IOBar  io_access_control_e_switch;
    private IOBar  io_access_control_f_switch;
    private IOBar  io_access_control_g_switch;

    /* ---------------------------------------------------------------------------- */
    private ValueCheckBox      ebd_enable_operation_set;
    private JLabel             lbl_i_o_operation_mode;
    private MyComboBox		   cbo_i_o_operation_mode;
    private CardLayout         callLayout;
    private CallTable[]        callTable;
    private JPanel             callPanel;
    protected String[]         text;
    protected byte[][]         calls;
    protected int 			   floorCounts;



    public AccessControl () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<AccessControl> panel ) {
        this.settingPanel = panel;
    }
    
    DefaultTableCellRenderer headRenderer = new DefaultTableCellRenderer() {
		private static final long serialVersionUID = -7289153737903649567L;

		public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                         int column ) {
            JLabel renderedLabel = ( JLabel )super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            renderedLabel.setHorizontalAlignment( SwingConstants.CENTER );
            renderedLabel.setForeground(Color.WHITE);
            setBackground(StartUI.MAIN_BACKGROUND_COLOR);
            return renderedLabel;
        }
    };
    
    DefaultTableCellRenderer normalRenderer = new DefaultTableCellRenderer() {
        private static final long serialVersionUID = 8202677196161198377L;
        public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                         int column ) {
            JLabel renderedLabel = ( JLabel )super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            renderedLabel.setHorizontalAlignment( SwingConstants.CENTER );
            renderedLabel.setForeground(Color.WHITE);
            setBackground(StartUI.BORDER_COLOR);
            return renderedLabel;
        }
    };

    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][30::30][150::150][150::150][]" ) );
        cpt_general = new JLabel();
        ebd_enabled = new ValueCheckBox();
        setCaptionStyle( cpt_general );

        // @CompoentSetting( ebd_enabled )
        add( cpt_general, "gapbottom 18-12, span, top" );
        add( ebd_enabled, "skip 1, span, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings = new JLabel();

        JLabel lbl_io_access_control_a = new JLabel();
        JLabel lbl_io_access_control_b = new JLabel();
        JLabel lbl_io_access_control_c = new JLabel();
        JLabel lbl_io_access_control_d = new JLabel();
        JLabel lbl_io_access_control_e = new JLabel();
        JLabel lbl_io_access_control_f = new JLabel();
        JLabel lbl_io_access_control_g = new JLabel();
        io_access_control_a_switch = new IOBar( true );
        io_access_control_b_switch = new IOBar( true );
        io_access_control_c_switch = new IOBar( true );
        io_access_control_d_switch = new IOBar( true );
        io_access_control_e_switch = new IOBar( true );
        io_access_control_f_switch = new IOBar( true );
        io_access_control_g_switch = new IOBar( true );
        
        setCaptionStyle( cpt_i_o_settings );
        setTextLabelStyle( lbl_io_access_control_a );
        setTextLabelStyle( lbl_io_access_control_b );
        setTextLabelStyle( lbl_io_access_control_c );
        setTextLabelStyle( lbl_io_access_control_d );
        setTextLabelStyle( lbl_io_access_control_e );
        setTextLabelStyle( lbl_io_access_control_f );
        setTextLabelStyle( lbl_io_access_control_g );
        lbl_io_access_control_a.setText( EventID.getString( EventID.ACCESS1.eventID, null ) );
        lbl_io_access_control_b.setText( EventID.getString( EventID.ACCESS2.eventID, null ) );
        lbl_io_access_control_c.setText( EventID.getString( EventID.ACCESS3.eventID, null ) );
        lbl_io_access_control_d.setText( EventID.getString( EventID.ACCESS4.eventID, null ) );
        lbl_io_access_control_e.setText( EventID.getString( EventID.ACCESS5.eventID, null ) );
        lbl_io_access_control_f.setText( EventID.getString( EventID.ACCESS6.eventID, null ) );
        lbl_io_access_control_g.setText( EventID.getString( EventID.ACCESS7.eventID, null ) );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_access_control_a_switch, EventID.ACCESS1.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_access_control_b_switch, EventID.ACCESS2.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_access_control_c_switch, EventID.ACCESS3.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_access_control_d_switch, EventID.ACCESS4.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_access_control_e_switch, EventID.ACCESS5.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_access_control_f_switch, EventID.ACCESS6.eventID );
        IOEditorDialog.assignTo( StartUI.getLiftSelector().getSelectedLift(), io_access_control_g_switch, EventID.ACCESS7.eventID );
        add( cpt_i_o_settings, "gapbottom 18-12, span, top" );
        add( lbl_io_access_control_a, "skip 2, span, gapright 12, top" );
        add( io_access_control_a_switch, "skip 2, span, wrap 12, top" );
        add( lbl_io_access_control_b, "skip 2, span, gapright 12, top" );
        add( io_access_control_b_switch, "skip 2, span, wrap 12, top" );
        add( lbl_io_access_control_c, "skip 2, span, gapright 12, top" );
        add( io_access_control_c_switch, "skip 2, span, wrap 12, top" );
        add( lbl_io_access_control_d, "skip 2, span, gapright 12, top" );
        add( io_access_control_d_switch, "skip 2, span, wrap 12, top" );
        add( lbl_io_access_control_e, "skip 2, span, gapright 12, top" );
        add( io_access_control_e_switch, "skip 2, span, wrap 12, top" );
        add( lbl_io_access_control_f, "skip 2, span, gapright 12, top" );
        add( io_access_control_f_switch, "skip 2, span, wrap 12, top" );
        add( lbl_io_access_control_g, "skip 2, span, gapright 12, top" );
        add( io_access_control_g_switch, "skip 2, span, wrap 30, top" );

        /* ---------------------------------------------------------------------------- */
        lbl_i_o_operation_mode = new JLabel();
        cbo_i_o_operation_mode = new MyComboBox();
        cbo_i_o_operation_mode.addActionListener( new Cbo_i_o_operation_modeActionListener() );
        cbo_i_o_operation_mode.setModel( new DefaultComboBoxModel<>( new Integer[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
            40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58,
            59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77,
            78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96,
            97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112,
            113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127
        } ) );
        ebd_enable_operation_set = new ValueCheckBox();

        // @CompoentSetting( lbl_i_o_operation_mode, cbo_i_o_operation_mode )
        setComboBoxLabelStyle( lbl_i_o_operation_mode );
        setComboBoxValueStyle( cbo_i_o_operation_mode );

        // @CompoentSetting( ebd_enable_operation_set )

        // @CompoentSetting<Fmt>( lbl_operation_set , fmt_operation_set )
        add( ebd_enable_operation_set, "skip 1, span" );
        add( lbl_i_o_operation_mode, "skip 2, span, split, gapright 12" );
        add( cbo_i_o_operation_mode, "wrap" );
        callLayout = new CardLayout();
        callTable  = new CallTable[ 128 ];
        callPanel  = new JPanel( callLayout );
        
        for ( Integer i = 0 ; i < 128 ; i++ ) {
            callTable[ i ] = new CallTable( i );

            JTableHeader head =  callTable[i].getTableHeader();
            head.setPreferredSize(new Dimension(1, 30));

            JPanel panel = new JPanel( new BorderLayout() );
            panel.setBorder( BorderFactory.createLineBorder( StartUI.BORDER_COLOR ) );
            panel.add( callTable[ i ].getTableHeader(), BorderLayout.NORTH );
            panel.add( callTable[ i ] );
            callPanel.add( i.toString(), panel );
        }

        final LockableUI lockUI = new LockableUI();
        lockUI.setLockedCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );

        BufferedImageOpEffect effect1 = new BufferedImageOpEffect( new ColorConvertOp( ColorSpace.getInstance( ColorSpace.CS_GRAY ), null ) );
        BufferedImageOpEffect effect2 = new BufferedImageOpEffect( new ColorTintFilter( Color.GRAY, 0.2f ) );
        lockUI.setLockedEffects( effect1, effect2 );
        
        JXLayer<JComponent> layer = new JXLayer<JComponent>( callPanel, lockUI );
        add( layer, "skip 2, span, wrap 30" );

        final MouseWheelListener mousewheelScrollListener = new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                getParent().dispatchEvent(e);
            }
        };
        layer.addMouseWheelListener(mousewheelScrollListener);

        /* ---------------------------------------------------------------------------- */
        bindGroup( "enabled", ebd_enabled );
        bindGroup( "force_operation_set", ebd_enable_operation_set );
        bindGroup( "operation_set", lbl_i_o_operation_mode, cbo_i_o_operation_mode );
        bindGroup( new AbstractButton[]{ ebd_enabled }, ebd_enable_operation_set, lbl_i_o_operation_mode, cbo_i_o_operation_mode,
                   lbl_io_access_control_a, io_access_control_a_switch, lbl_io_access_control_b, io_access_control_b_switch,
                   lbl_io_access_control_c, io_access_control_c_switch, lbl_io_access_control_d, io_access_control_d_switch,
                   lbl_io_access_control_e, io_access_control_e_switch,lbl_io_access_control_f, io_access_control_f_switch,
                   lbl_io_access_control_g, io_access_control_g_switch,callPanel );
        ebd_enabled.addItemListener( new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                lockUI.setLocked( ! ebd_enabled.isSelected() );
            }
        } );
        lockUI.setLocked( ! ebd_enabled.isSelected() );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_general.setText( TEXT.getString( "general" ) );
        ebd_enabled.setText( TEXT.getString( "enabled" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_i_o_settings.setText( TEXT.getString( "enabled_setting" ) );

        /* ---------------------------------------------------------------------------- */
        lbl_i_o_operation_mode.setText( TEXT.getString( "operation_set" ) );
        ebd_enable_operation_set.setText( TEXT.getString( "force_operation_set" ) );

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
        GeneralBean bean_general = new GeneralBean();
        bean_general.setEnabled( ebd_enabled.isSelected() );
        return bean_general;
    }


    public IOSettingsBean getIOSettingsBean () throws ConvertException {
        IOSettingsBean bean_iOSettings = new IOSettingsBean();
        bean_iOSettings.setSwitchAEvent( io_access_control_a_switch.getEvent() );
        bean_iOSettings.setSwitchBEvent( io_access_control_b_switch.getEvent() );
        bean_iOSettings.setSwitchCEvent( io_access_control_c_switch.getEvent() );
        bean_iOSettings.setSwitchDEvent( io_access_control_d_switch.getEvent() );
        bean_iOSettings.setSwitchEEvent( io_access_control_e_switch.getEvent() );
        bean_iOSettings.setSwitchFEvent( io_access_control_f_switch.getEvent() );
        bean_iOSettings.setSwitchGEvent( io_access_control_g_switch.getEvent() );
        bean_iOSettings.setIOOperationMode( ( Integer )cbo_i_o_operation_mode.getSelectedItem() );
        bean_iOSettings.setEnableOperationSet( ebd_enable_operation_set.isSelected() );
        bean_iOSettings.setFloorText( text );
        bean_iOSettings.setCallAvailability( calls );
        return bean_iOSettings;
    }


    public void setGeneralBean ( GeneralBean bean_general ) {
        this.ebd_enabled.setOriginSelected( bean_general.getEnabled() != null && bean_general.getEnabled() == true );
    }


    public void setIOSettingsBean ( IOSettingsBean bean_iOSettings ) {
        this.io_access_control_a_switch.setEvent( bean_iOSettings.getSwitchAEvent() );
        this.io_access_control_b_switch.setEvent( bean_iOSettings.getSwitchBEvent() );
        this.io_access_control_c_switch.setEvent( bean_iOSettings.getSwitchCEvent() );
        this.io_access_control_d_switch.setEvent( bean_iOSettings.getSwitchDEvent() );
        this.io_access_control_e_switch.setEvent( bean_iOSettings.getSwitchEEvent() );
        this.io_access_control_f_switch.setEvent( bean_iOSettings.getSwitchFEvent() );
        this.io_access_control_g_switch.setEvent( bean_iOSettings.getSwitchGEvent() );
        this.cbo_i_o_operation_mode.setSelectedItem( bean_iOSettings.getIOOperationMode() );
        this.ebd_enable_operation_set.setOriginSelected( bean_iOSettings.getEnableOperationSet() != null
                                                   && bean_iOSettings.getEnableOperationSet() == true );
        text  = bean_iOSettings.getFloorText();
        floorCounts = bean_iOSettings.getFloorCounts();
        calls = bean_iOSettings.getCallAvailability();
        for ( int i = 0 ; i < callTable.length ; i++ ) {
            callTable[ i ].refreshData();
        }
    }


    public void select ( Integer index ) {
        callLayout.show( callPanel, new Integer( index ).toString() );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static AccessControl createPanel ( SettingPanel<AccessControl> panel ) {
        AccessControl gui = new AccessControl();
        gui.setSettingPanel( panel );
        return gui;
    }


    public class CallTable extends JTable {
        private static final long serialVersionUID = -7497769793891380698L;

        public CallTable ( final int page ) {
            super( new CallTableModel( page ) );
            setGridColor(StartUI.SUB_BACKGROUND_COLOR);
            setShowGrid(true);
            setRowHeight(25);
            getColumnModel().getColumn( 0 ).setPreferredWidth( 50 );
            getColumnModel().getColumn( 0 ).setCellRenderer(normalRenderer);
            getTableHeader().getColumnModel().getColumn(0).setHeaderRenderer(headRenderer);
            for ( int i = 1 ; i < getColumnModel().getColumnCount() ; i++ ) {
                getColumnModel().getColumn( i ).setPreferredWidth( 120 );
                getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(headRenderer);
                getColumnModel().getColumn( i ).setCellRenderer( new AccessCheckBoxCellRenderer(this) );
            }
        }


        public void refreshData () {
            ( ( CallTableModel )getModel() ).fireTableDataChanged();
        }
    }




    public class CallTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 7666213648611456254L;
        protected String[]        title            = {
            TEXT.getString( "floor" ), TEXT.getString( "car_front_call" ), TEXT.getString( "car_rear_call" ),
            TEXT.getString( "hall_up_front_call" ), TEXT.getString( "hall_down_front_call" ), TEXT.getString( "hall_up_rear_call" ),
            TEXT.getString( "hall_down_rear_call" ),
        };
        private int page;




        public CallTableModel ( int page ) {
            this.page = page;
        }


        @Override
        public int getRowCount () {
            return floorCounts;
        }


        @Override
        public int getColumnCount () {
            return 7;
        }


        public String getColumnName ( int col ) {
            return title[ col ];
        }


        @Override
        public Class<?> getColumnClass ( int col ) {
            if ( col == 0 ) {
                return String.class;
            }
            return Boolean.class;
        }


        @Override
        public Object getValueAt ( int rowIndex, int columnIndex ) {
            try {
                if ( columnIndex == 0 ) {
                    return text[ rowIndex ];
                } else {
                    int bitOffset = columnIndex - 1;
                    return ( calls[ page ][ rowIndex ] & ( 1 << bitOffset ) ) != 0;
                }
            } catch ( Exception e ) {
                return false;
            }
        }


        @Override
        public boolean isCellEditable ( int row, int col ) {
            if ( col == 0 )
                return false;
            return true;
        }


        @Override
        public void setValueAt ( Object aValue, int row, int col ) {
            if ( col >= 1 && col < 7 ) {
                int bitOffset = col - 1;
                if ( aValue == Boolean.TRUE ) {
                    calls[ page ][ row ] = ( byte )( calls[ page ][ row ] | ( 1 << bitOffset ) );
                } else {
                    calls[ page ][ row ] = ( byte )( calls[ page ][ row ] & ~ ( 1 << bitOffset ) );
                }
                fireTableDataChanged();
            }
        }
    }




    private class Cbo_i_o_operation_modeActionListener implements ActionListener {
        public void actionPerformed ( final ActionEvent e ) {
            if ( cbo_i_o_operation_mode.getSelectedItem() != null )
                select( ( int )cbo_i_o_operation_mode.getSelectedItem() );
        }
    }


    public static class GeneralBean {
        private Boolean enabled;




        public Boolean getEnabled () {
            return this.enabled;
        }


        public void setEnabled ( Boolean enabled ) {
            this.enabled = enabled;
        }
    }




    public static class IOSettingsBean {
        private Event    switchAEvent;
        private Event    switchBEvent;
        private Event    switchCEvent;
        private Event    switchDEvent;
        private Event    switchEEvent;
        private Event    switchFEvent;
        private Event    switchGEvent;
        private Integer  iOOperationMode;
        private Boolean  enableOperationSet;
        private byte[][] callAvailability;
        private String[] floorText;
        private int 	 floorCounts;




        public int getFloorCounts() {
			return floorCounts;
		}


		public void setFloorCounts(int floorCounts) {
			this.floorCounts = floorCounts;
		}


		public final Event getSwitchAEvent () {
            return switchAEvent;
        }


        public final Event getSwitchBEvent () {
            return switchBEvent;
        }


        public final Event getSwitchCEvent () {
            return switchCEvent;
        }


        public final Event getSwitchDEvent () {
            return switchDEvent;
        }
        
        public final Event getSwitchEEvent () {
            return switchEEvent;
        }
        
        public final Event getSwitchFEvent () {
            return switchFEvent;
        }
        
        public final Event getSwitchGEvent () {
            return switchGEvent;
        }

        public final Integer getIOOperationMode () {
            return iOOperationMode;
        }


        public final Boolean getEnableOperationSet () {
            return enableOperationSet;
        }


        public final byte[][] getCallAvailability () {
            return callAvailability;
        }


        public final String[] getFloorText () {
            return floorText;
        }


        public final void setSwitchAEvent ( Event switchAEvent ) {
            this.switchAEvent = switchAEvent;
        }


        public final void setSwitchBEvent ( Event switchBEvent ) {
            this.switchBEvent = switchBEvent;
        }


        public final void setSwitchCEvent ( Event switchCEvent ) {
            this.switchCEvent = switchCEvent;
        }


        public final void setSwitchDEvent ( Event switchDEvent ) {
            this.switchDEvent = switchDEvent;
        }
        
        public final void setSwitchEEvent ( Event switchEEvent ) {
            this.switchEEvent = switchEEvent;
        }
        
        public final void setSwitchFEvent ( Event switchFEvent ) {
            this.switchFEvent = switchFEvent;
        }
        
        public final void setSwitchGEvent ( Event switchGEvent ) {
            this.switchGEvent = switchGEvent;
        }


        public final void setIOOperationMode ( Integer iOOperationMode ) {
            this.iOOperationMode = iOOperationMode;
        }


        public final void setEnableOperationSet ( Boolean enableOperationSet ) {
            this.enableOperationSet = enableOperationSet;
        }


        public final void setCallAvailability ( byte[][] callAvailability ) {
            this.callAvailability = callAvailability;
        }


        public final void setFloorText ( String[] floorText ) {
            this.floorText = floorText;
        }
    }
}
