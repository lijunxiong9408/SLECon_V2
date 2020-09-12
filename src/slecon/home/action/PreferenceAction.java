package slecon.home.action;
import java.awt.event.ActionEvent;

import base.cfg.ImageFactory;
import logic.Dict;
import slecon.StartUI;
import slecon.home.PosAction;
import slecon.interfaces.HomeAction;




@HomeAction( sortIndex = 4 )
public class PreferenceAction extends PosAction {
    private static final long serialVersionUID = -7687325097046971402L;




    public PreferenceAction () {
        super( Dict.lookup( "Preference" ), ImageFactory.PREFRENCE_ICON.icons( 32, 32 ) );
    }


    @Override
    public void actionPerformed ( ActionEvent e ) {
        PreferenceDialog.showDialog();
    }
}
