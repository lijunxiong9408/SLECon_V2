package slecon.home.dashboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import logic.connection.LiftConnectionBean;
import logic.connection.LiftSiteBean;
import logic.util.SiteChangeListener;
import logic.util.SiteManagement;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.component.Workspace;
import slecon.interfaces.HomeView;
import slecon.interfaces.Page;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXPanel;

import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;




@HomeView(
    icon      = ImageFactory.DASHBOARD_ICON,
    name      = "Dashboard",
    sortIndex = 3
)
public class DashboardPanel extends JPanel implements Page, SiteChangeListener {
    private static final long                       serialVersionUID = -5641196993353629510L;
    private final Map<LiftStatusView, LiftConnectionBean> map1             = new HashMap<>();




    public DashboardPanel () {
        setLayout( new BorderLayout() );
        SiteManagement.addSiteChangeListener(this, null);
        initGUI();
        
        resetTimer.setInitialDelay( 100 );
        resetTimer.setRepeats( false );
        BaseFactory.addPropertyChangeListener( new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                resetGui();
            }
        } );
    }


    private void initGUI() {
        removeAll();
        
        JPanel workspace = new JPanel( new MigLayout( "gap 0", "[grow, center]", "[fill]" ) );
        workspace.setOpaque( false );
        for ( LiftSiteBean site : SiteManagement.getAllSite() ) {
            JXPanel                 sitePanel   = new JXPanel( new MigLayout( "gap 0 0, ins 0 0" ) );
            final JLabel            title       = new JLabel( site.getName() );
            final VECCollpaseButton btnCollapse = new VECCollpaseButton();
            title.setFont( FontFactory.FONT_16_BOLD );
            title.setForeground(StartUI.LIFTSITE_COLOR);
            

            final LiftCollectionPanel liftsPanel = new LiftCollectionPanel();
            for ( LiftConnectionBean connection : SiteManagement.getConnectionBySite( site ) ) {
                LiftStatusView panel = new LiftStatusView( connection, liftsPanel );
                map1.put( panel, connection );
                liftsPanel.add( panel );
            }

            JXPanel                 titleBar      = new JXPanel( new MigLayout( "gap 0 0, ins 0 0" ) );
            final JXCollapsiblePane collpasedPane = new JXCollapsiblePane();
            collpasedPane.setAnimated( false );
            collpasedPane.setContentPane( liftsPanel );
            setTransparent( collpasedPane );
            setTransparent( sitePanel );
            setTransparent( titleBar );
            setTransparent( liftsPanel );
            titleBar.add( title );
            titleBar.add( btnCollapse, "push, right" );
            sitePanel.add( titleBar, "gapleft 20, gaptop 12, wrap, w 840!" );
            
            JSeparator separator = new JSeparator();
            separator.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
            sitePanel.add( separator, "gapleft 20, gapbottom 10, wrap, w 840!" );
            
            sitePanel.add( collpasedPane, "gapleft 20, gapbottom 20, w 840!, left" );
            setHandCursorWhenMouseOver( btnCollapse );
            workspace.add( sitePanel, "left, wrap" );
            btnCollapse.setCollapsed( collpasedPane.isCollapsed() );
            btnCollapse.addMouseListener( new MouseAdapter() {
                @Override
                public void mousePressed ( MouseEvent e ) {
                    collpasedPane.setCollapsed( ! collpasedPane.isCollapsed() );
                    btnCollapse.setCollapsed( collpasedPane.isCollapsed() );
                }
            } );
        }
        add( new JScrollPane( workspace ) {
            private static final long serialVersionUID = -5733767579374701576L;
            {
                setBorder( null );
                setOpaque( false );
                getViewport().setOpaque( false );
                getVerticalScrollBar().setUnitIncrement( 20 );
                this.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());

                setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
            }
        }, BorderLayout.CENTER );
        
        revalidate();
        repaint();
    }


    public void setTransparent ( JComponent comp ) {
        comp.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        comp.setOpaque( false );
        comp.setBackground( StartUI.SUB_BACKGROUND_COLOR );
    }


    private static void setHandCursorWhenMouseOver ( final JComponent c ) {
        c.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseEntered ( MouseEvent e ) {
                Cursor cursor = Cursor.getPredefinedCursor( Cursor.HAND_CURSOR );
                c.setCursor( cursor );
            }
            @Override
            public void mouseExited ( MouseEvent e ) {
                Cursor cursor = Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR );
                c.setCursor( cursor );
            }
        } );
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
        for ( LiftStatusView panel : map1.keySet() ) {
            panel.onCreate( workspace );
        }
    }


    @Override
    public void onStart () throws Exception {
        for ( LiftStatusView panel : map1.keySet() ) {
            panel.onStart();
        }
        
    }


    @Override
    public void onResume () throws Exception {
        for ( LiftStatusView panel : map1.keySet() ) {
            panel.onResume();
        }
    }


    @Override
    public void onPause () throws Exception {
        for ( LiftStatusView panel : map1.keySet() ) {
            panel.onPause();
        }
    }


    @Override
    public void onStop () throws Exception {
        for ( LiftStatusView panel : map1.keySet() ) {
            panel.onStop();
        }
        map1.clear();
    }


    @Override
    public void onDestroy () {
    }


    private final class VECCollpaseButton extends JLabel {
        private static final long serialVersionUID = -6839674049139929056L;
        private boolean           collapsed;
        private boolean           hovered;




        public VECCollpaseButton () {
            addMouseListener( new MouseAdapter() {
                @Override
                public void mouseEntered ( MouseEvent e ) {
                    hovered = true;
                    refreshIcon();
                }
                @Override
                public void mouseExited ( MouseEvent e ) {
                    hovered = false;
                    refreshIcon();
                }
            } );
            refreshIcon();
        }


        public boolean isCollapsed () {
            return collapsed;
        }


        public void setCollapsed ( boolean collapse ) {
            this.collapsed = collapse;
            refreshIcon();
        }


        private void refreshIcon () {
            if ( isCollapsed() && hovered ) {
                setIcon( ImageFactory.DASHBOARD_COLLAPSE_ON_HOVER.icon() );
            } else if ( ! isCollapsed() && hovered ) {
                setIcon( ImageFactory.DASHBOARD_COLLAPSE_OFF_HOVER.icon() );
            } else if ( isCollapsed() && ! hovered ) {
                setIcon( ImageFactory.DASHBOARD_COLLAPSE_ON.icon() );
            } else if ( ! isCollapsed() && ! hovered ) {
                setIcon( ImageFactory.DASHBOARD_COLLAPSE_OFF.icon() );
            }
        }
    }




    private final class VECLinkButton extends JLabel {
        private static final long serialVersionUID = 6231063583341534630L;
        private boolean           linked;
        private boolean           hovered;




        public VECLinkButton () {
            addMouseListener( new MouseAdapter() {
                @Override
                public void mouseEntered ( MouseEvent e ) {
                    hovered = true;
                    refreshIcon();
                }
                @Override
                public void mouseExited ( MouseEvent e ) {
                    hovered = false;
                    refreshIcon();
                }
            } );
            refreshIcon();
        }


        public boolean isLinked () {
            return linked;
        }


        public void setLinked ( boolean linked ) {
            this.linked = linked;
            refreshIcon();
        }


        private void refreshIcon () {
            if ( isLinked() && hovered ) {
                setIcon( ImageFactory.DASHBOARD_LINK_ENABLED_HOVER.icon() );
            } else if ( ! isLinked() && hovered ) {
                setIcon( ImageFactory.DASHBOARD_LINK_DISABLED_HOVER.icon() );
            } else if ( isLinked() && ! hovered ) {
                setIcon( ImageFactory.DASHBOARD_LINK_ENABLED.icon() );
            } else if ( ! isLinked() && ! hovered ) {
                setIcon( ImageFactory.DASHBOARD_LINK_DISABLED.icon() );
            }
        }
    }

    Timer resetTimer = new Timer( 500, new ActionListener() {
                         @Override
                         public void actionPerformed ( ActionEvent e ) {
                             System.out.println("reset");
                             try {
                                 onStop();
                                 initGUI();
                                 onStart();
                             } catch ( Exception e1 ) {
                             }
                         }
                     } );
    
    
    public void resetGui () {
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run () {
                resetTimer.restart();
            }
            
        });
    }


    @Override
    public void siteAdd ( LiftConnectionBean conns ) {
        resetGui();
    }


    @Override
    public void siteRemove ( LiftConnectionBean conns ) {
        resetGui();
    }
}
