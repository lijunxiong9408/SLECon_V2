package slecon.home.action;
import java.awt.event.ActionEvent;

import base.cfg.ImageFactory;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.SiteManagement;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.home.PosAction;
import slecon.inspect.InspectPanel;
import slecon.interfaces.HomeAction;




@HomeAction( sortIndex = 2 )
public class InspectAction extends PosAction {
    private static final long serialVersionUID = -7687325097046971402L;




    public InspectAction () {
        super( Dict.lookup( "Inspect" ), ImageFactory.INSPECT_ICON.icons( 32, 32 ) );
    }


    @Override
    public void actionPerformed ( ActionEvent e ) {
        LiftConnectionBean connBean = SelectPopupDialog.showDialog();
        if ( connBean != null ) {
            if ( SiteManagement.isAlive( connBean ) ) {
                try {
                    StartUI.getTopMain().push( InspectPanel.build( connBean, null ) );
                } catch ( Exception xe ) {
                    xe.printStackTrace();
                }
            } else {
                ToolBox.showErrorMessage( Dict.lookup( "SelectedLiftIsOffline" ) );
            }
        }
    }
}
