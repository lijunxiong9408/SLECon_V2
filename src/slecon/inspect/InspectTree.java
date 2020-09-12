package slecon.inspect;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import logic.connection.LiftConnectionBean;
import logic.util.PageTreeExpression;
import logic.util.SimpleConfiguareExpression.GrammarException;
import logic.util.SimpleConfiguareExpression.LexException;
import slecon.StartUI;
import slecon.component.tree.PageTree;
import logic.util.SiteManagement;
import logic.util.Version;
import comm.constants.AuthLevel.Role;

import configure.PageLookup;
import configure.PageLookup.DisplayTreeItem;




public class InspectTree extends PageTree {
    private static final long                    serialVersionUID = 510722393153651849L;
    private LiftConnectionBean                   connectionBean;
    final Map<Class<? extends JPanel>, PageTreeExpression> pageConditionMap = new HashMap<>();




    public LiftConnectionBean getConnectionBean () {
        connectionBean = ( StartUI.getLiftSelector().getSelectedLift() == null )
                         ? connectionBean
                         : StartUI.getLiftSelector().getSelectedLift();
        return connectionBean;
    }


    protected synchronized PageTreeExpression getPageCondition(Class<? extends JPanel> panelClass) {
        return pageConditionMap.get(panelClass);
    }

    
    @Override
    protected synchronized void initTreeItem () {
        pageConditionMap.clear();
        
        if (getConnectionBean() != null) {
            for (DisplayTreeItem item : PageLookup.getInspectPageClass()) {
                pageConditionMap.put(item.panelClass, item.versionCondition);
                
                try {
                    Version version = SiteManagement.getVersion(getConnectionBean());
                    Role[]  roles   = SiteManagement.getRoles(getConnectionBean());
                    boolean cond    = item.versionCondition.evaluate(version, roles);
                    if (version!=null && cond) {
                        addPageItem(item.index, item.path, item.panelClass);
                    }
                } catch (LexException | GrammarException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
