package slecon.setting.setup.motion;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;




public class SpeedProfile extends JPanel {
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.motion.SpeedProfile" );
    private static final long          serialVersionUID = 4715999776831590480L;
    private SpeedProfileTable          table;
    private SettingPanel<SpeedProfile> settingPanel;


    void SetWidgetEnable(boolean enable) {
    	table.SetTableEnable(enable);
    }

    public SpeedProfile () {
        initGUI();
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[20::20][20::20][32::32,grow][]", "[][][]" ) );

        JPanel panel = new JPanel() {
            protected void paintComponent ( Graphics g ) {
                if ( g instanceof Graphics2D ) {
                    final Image image = ImageFactory.SPEED_PROFILE.image();
                    g.drawImage(image, 0, 0, null);
                }
            }
        };
        add( panel, "skip 1, span,w 700!, h 250!, wrap 30" );

        JLabel cpt_speed_regulation = new JLabel( TEXT.getString( "cpt_speed_regulation" ) );
        cpt_speed_regulation.setFont(FontFactory.FONT_12_BOLD);
        cpt_speed_regulation.setForeground(Color.WHITE);
        add( cpt_speed_regulation, "gapbottom 18-12, span, aligny center" );
        table = new SpeedProfileTable();
        table.getMainTable().addKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed ( KeyEvent e ) {
                do_table_selectionChanged( e );
            }
        } );
        table.getMainTable().addMouseMotionListener( new MouseMotionAdapter() {
            @Override
            public void mouseMoved ( MouseEvent evt ) {
                int row = table.getMainTable().rowAtPoint( evt.getPoint() );
                int col = table.getMainTable().columnAtPoint( evt.getPoint() );
                if ( row >= 0 && col >= 1 ) {
                    settingPanel.setDescription( TEXT.getString( String.format( "column%d.description", col - 1 ) ) );
                }else {
                	settingPanel.setDescription("");
                }
            }
        } );
        table.getMainTable().addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked ( MouseEvent e ) {
                do_table_selectionChanged( e );
            }
        } );
        add( table, "skip 1, span, wrap 30" );
        
        SetWidgetEnable(false);
    }


    public void setData ( Object[][] tableData ) {
        table.setData( tableData );
    }


    public Object[][] getData () {
        table.commitEdit();
        return table.getData();
    }


    public void start () {
    }


    public void stop () {
    }


    public void setSettingPanel ( SettingPanel<SpeedProfile> panel ) {
        this.settingPanel = panel;
    }


    public static SpeedProfile createPanel ( SettingPanel<SpeedProfile> panel ) {
        SpeedProfile gui = new SpeedProfile();
        gui.setSettingPanel( panel );
        return gui;
    }


    protected void do_table_selectionChanged ( final InputEvent e ) {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                if ( table.getSelectedColumn() >= 1 ) {
                    settingPanel.setDescription( TEXT.getString( String.format( "column%d.description", table.getSelectedColumn() - 1 ) ) );
                } else {
                    settingPanel.setDescription( "" );
                }
            }
        } );
    }
}
