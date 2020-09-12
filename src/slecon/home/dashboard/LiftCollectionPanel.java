package slecon.home.dashboard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import base.cfg.BaseFactory;




public class LiftCollectionPanel extends JPanel implements Iterable<LiftStatusView> {
    private static final long          serialVersionUID = - 1197019065315796253L;
    private final List<LiftStatusView> panels           = new ArrayList<>();
    private MigLayout                  layout;


    LiftCollectionPanel () {
        super();
        setLayout( layout = new MigLayout( "gap 0 0, ins 0 0" ) );
    }


    public final void add ( LiftStatusView panel ) {
        panels.add( panel );
        super.add( panel );
        updateOrder();
    }


    public final void updateOrder () {
        List<LiftStatusView> panels2 = new ArrayList<>( panels );
        if ( BaseFactory.isPutOfflineElevatorAtTheBottom() ) {
            final HashMap<LiftStatusView, Integer> panelIndex = new HashMap<>();
            for(int i=0; i<panels2.size(); i++)
                panelIndex.put( panels2.get( i ), i );
            Collections.sort( panels2, new Comparator<LiftStatusView>() {
                @Override
                public int compare ( LiftStatusView panel1, LiftStatusView panel2 ) {
                    if ( panel1.getLiftStatus() == null || panel1.getLiftStatus().getModule() == null )
                        return 1;
                    if ( panel2.getLiftStatus() == null || panel2.getLiftStatus().getModule() == null )
                        return - 1;
                    // TODO 
                    return panelIndex.get( panel1 ).compareTo( panelIndex.get( panel2 ) );
                }
            } );
        }
        int index = 0;
        for ( LiftStatusView panel : panels2 ) {
            if ( BaseFactory.isHideOfflineElevator() )
                if ( panel.getLiftStatus() == null || panel.getLiftStatus().getModule() == null ) {
                    layout.setComponentConstraints( panel, String.format( "cell 0 %d, gapbottom 20, w 840!, hidemode 3", index++ ) );
                    panel.setVisible( false );
                    continue;
                }
            layout.setComponentConstraints( panel, String.format( "cell 0 %d, gapbottom 20, w 840!, hidemode 3", index++ ) );
            panel.setVisible( true );
        }
    }


    @Override
    public Iterator<LiftStatusView> iterator () {
        return panels.iterator();
    }
}
