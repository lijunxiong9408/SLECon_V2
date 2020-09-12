package slecon.setting.installation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.table.GridBagTable;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;




public class GroupConfiguration extends JPanel {
    private static final long     serialVersionUID = 4859692023012089140L;
    private static ResourceBundle TEXT = ToolBox.getResourceBundle("setting.installation.GroupConfiguration");
    private boolean                          started = false;
    private JLabel                           cpt_group_automatic_operation_setting;
    private GAOTable                         tbl_gao;
    private SettingPanel<GroupConfiguration> settingPanel;
    
    public GroupConfiguration () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<GroupConfiguration> panel ) {
        this.settingPanel = panel;
    }
    
    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[40::40][20::20][32::32][]" ) );
        cpt_group_automatic_operation_setting = new JLabel();
        tbl_gao = new GAOTable();

        /* ---------------------------------------------------------------------------- */
        setCaptionStyle( cpt_group_automatic_operation_setting );

        JPanel panel_of_tbl_gao = new JPanel( new BorderLayout() );
        panel_of_tbl_gao.setBorder( BorderFactory.createLineBorder( StartUI.BORDER_COLOR ) );
        panel_of_tbl_gao.add( tbl_gao.getTableHeader(), BorderLayout.NORTH );
        panel_of_tbl_gao.add( tbl_gao );
        add( cpt_group_automatic_operation_setting, "gapbottom 18-12, span, top" );
        add( panel_of_tbl_gao, "span, wrap 30, top" );
        loadI18N();
        updateUI();
    }


    private void loadI18N () {

        /* ---------------------------------------------------------------------------- */
        cpt_group_automatic_operation_setting.setText( getBundleText( "LBL_cpt_group_automatic_operation_setting",
                                                                      "Group Automatic Operation Setting" ) );

        /* ---------------------------------------------------------------------------- */
    }


    public GroupAutomaticOperationSettingBean getGroupAutomaticOperationSettingBean () throws ConvertException {
        TableCellEditor editor = tbl_gao.getCellEditor();
        if ( editor != null )
            editor.stopCellEditing();

        GroupAutomaticOperationSettingBean bean_groupAutomaticOperationSetting = new GroupAutomaticOperationSettingBean();
        bean_groupAutomaticOperationSetting.setGaoTableData( tbl_gao.getData() );
        return bean_groupAutomaticOperationSetting;
    }


    public void setGroupAutomaticOperationSettingBean ( GroupAutomaticOperationSettingBean bean_groupAutomaticOperationSetting ) {
        tbl_gao.setData( bean_groupAutomaticOperationSetting.getText(), bean_groupAutomaticOperationSetting.getGaoTableData() );
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


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static GroupConfiguration createPanel ( SettingPanel<GroupConfiguration> panel ) {
        GroupConfiguration gui = new GroupConfiguration();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class GroupAutomaticOperationSettingBean {
        int[][][]  gaoTableData;
        String[] text;




        public int[][][] getGaoTableData () {
            return gaoTableData;
        }


        public void setGaoTableData ( int[][][] gaoTable ) {
            this.gaoTableData = gaoTable;
        }


        public String[] getText () {
            return text;
        }


        public void setText ( String[] text ) {
            this.text = text;
        }
    }
    
    public final static class GAOTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 538470393895528256L;

        /* GridBagLayout must allocate row, col when table initial. */
        private int      floorCount = 128;
        private int      liftCount  = 16;
        public  final int[][][]  data = new int[128][16][2];
        private String[] text;

        public GAOTableModel() {}


        public void setData ( String[] text, int[][][] data ) {
            this.text = text;
            for (int i=0; i<128; i++)
                for(int j=0; j<16; j++) {
                    try {
                        this.data[i][j][0] = data!=null && i<data.length && j<data[i].length ? data[i][j][0] : 0;
                        this.data[i][j][1] = data!=null && i<data.length && j<data[i].length ? data[i][j][1] : 0;
                    } catch (Exception e) {
                        System.nanoTime();
                    }
                }
            fireTableDataChanged();
        }


        public static int toUnsignedInt ( byte b ) {
            return ( ( int )b ) & 0xFF;
        }

        public void setDimension ( int floorCount, int liftCount ) {
            this.floorCount = floorCount;
            this.liftCount  = liftCount;
            fireTableStructureChanged();
        }


        @Override
        public boolean isCellEditable ( int row, int col ) {
            return ( col < 2 )
                   ? false
                   : true;
        }


        @Override
        public int getRowCount () {
            return floorCount * 2;
        }


        @Override
        public int getColumnCount () {
            return liftCount + 2;
        }


        @Override
        public Object getValueAt ( int rowIndex, int columnIndex ) {
            if ( columnIndex == 0 ) {
                return rowIndex / 2;
            } else if ( columnIndex == 1 ) {
                try {
                    return text[ rowIndex / 2 ];
                } catch ( ArrayIndexOutOfBoundsException | NullPointerException e ) {
                    return String.format( "%d/F", rowIndex );
                }
            }
            int liftIndex = columnIndex - 2;
            int floor     = rowIndex / 2;
            try {
                if (rowIndex % 2 == 0) {
                    return data[floor][liftIndex][0];
                } else {
                    return data[floor][liftIndex][1];
                }
            } catch (Exception e) {
                System.nanoTime();
                return null;
            }
        }


        @Override
        public void setValueAt ( Object aValue, int rowIndex, int columnIndex ) {
            if ( columnIndex >= 2 && columnIndex < 18 ) {
                int tag       = Integer.parseInt( aValue.toString() );
                int liftIndex = columnIndex - 2;
                int floor     = rowIndex / 2;
                if ( rowIndex % 2 == 0 ) {
                    data[floor][liftIndex][0] = tag;
                } else if ( rowIndex % 2 == 1 ) {
                    data[floor][liftIndex][1] = tag;
                }
                fireTableCellUpdated( rowIndex, columnIndex );
            }
        }


        public String getColumnName ( int col ) {
            return TEXT.getString("Column"+col);
        }
    }
}
