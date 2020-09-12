package slecon.inspect.devices.subview;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import slecon.ToolBox;
import slecon.inspect.devices.GongBean;




public class GongView extends JPanel {
    private static final long           serialVersionUID = 6683997071932926599L;
    private static final ResourceBundle bundle           = ToolBox.getResourceBundle("inspect.DevicePanel");
    private GongBean gong;
    private JLabel   labelCode;
    private JLabel   labelFloorId;
    private JLabel   lblCode;
    private JLabel   lblFloorID;




    public GongView () {
        initGUI();
        updateI18nGUI();
    }


    private void updateI18nGUI () {
        labelCode.setText( bundle.getString( "Gong.Code.text" ) );
        labelFloorId.setText( bundle.getString( "Gong.FloorID.text" ) );
        setBorder( new TitledBorder( bundle.getString( "Gong.title" ) ) );
    }


    private void initGUI () {
        setOpaque( false );
        setLayout( new MigLayout( "inset 5, w 180!", "[50.00,right][grow,left,fill]", "[][][grow][]" ) );
        labelCode = new JLabel( "Gong Code" );
        add( labelCode, "cell 0 0" );
        labelFloorId = new JLabel( "Floor ID" );
        add( labelFloorId, "cell 0 1" );
        lblCode = new JLabel( "New label" );
        add( lblCode, "cell 1 0" );
        lblFloorID = new JLabel( "New label" );
        add( lblFloorID, "cell 1 1" );

        JButton btnNewButton = new JButton( "Set Gong" );
        add( btnNewButton, "cell 0 3 2 1, center, w 120!" );
    }


    public void setGong ( GongBean gong ) {
        this.gong = gong;
        updateGongBean();
    }


    public GongBean getGong () {
        return gong;
    }


    public synchronized void updateGongBean () {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                if ( gong != null ) {
                    lblCode.setText( gong.getCode() == null
                                     ? ""
                                     : gong.getCode().toString() );
                    lblFloorID.setText( gong.getFloorID() == null
                                        ? ""
                                        : gong.getFloorID().toString() );
                    setVisible( true );
                } else {
                    setVisible( false );
                }
                updateUI();
            }
        } );
    }
}
