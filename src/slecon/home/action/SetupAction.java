package slecon.home.action;
import java.awt.event.ActionEvent;

import base.cfg.ImageFactory;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.SiteManagement;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.home.PosAction;
import slecon.interfaces.HomeAction;
import slecon.setting.SetupPanel;




@HomeAction( sortIndex = 1 )
public class SetupAction extends PosAction {
    private static final long serialVersionUID = -7687325097046971402L;




    public SetupAction () {
        super( Dict.lookup( "Setup" ), ImageFactory.SETUP_ICON.icons( 32, 32 ) );
    }


    @Override
    public void actionPerformed ( ActionEvent e ) {
        LiftConnectionBean connBean = SelectPopupDialog.showDialog();
        if ( connBean != null ) {
            if ( SiteManagement.isAlive( connBean ) ) {
                try {
                    StartUI.getTopMain().push( SetupPanel.build( connBean, null ) );
                } catch ( Exception xe ) {
                    xe.printStackTrace();
                }
            } else {
                ToolBox.showErrorMessage( Dict.lookup( "SelectedLiftIsOffline" ) );
            }
        }
    }
}
