package slecon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Painter;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import logic.Dict;
import net.miginfocom.swing.MigLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slecon.component.IPanelBinder;
import slecon.home.HomePanel;
import slecon.home.PosButton;
import slecon.home.dashboard.DashboardPanel;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.INIFile;
import base.cfg.ImageFactory;

public class TopMain extends JPanel {
    private static final long        serialVersionUID = - 689147670042197125L;
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "home.HomePanel" );
    private static Logger            logger           = LogManager.getLogger( TopMain.class );
    public  JPanel					 body;
    private JLabel                   lblVersion;
    private JLabel 					 lblMemoryUsage;
    private JLabel 					 lblValidity;
    private static INIFile ini;
    final Base64.Decoder decoder = Base64.getDecoder();
    
    String  file;
    long 	last_date = 0;
    long 	release_date = 0;
    long 	invalid_date = 0;
    
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("Logo", "60 0 72 64 c");
    }

    /**
     * Create the panel.
     */
    public TopMain () {
        initGUI();
    }
    
    @SuppressWarnings("serial")
    private void initGUI () {
        setBorder( null );
        MigLayout layout = new MigLayout( "gap 0,insets 0", "[220,fill,grow][220:220:n,fill]", "[64!][grow, fill][22!]" );
        setLayout(layout);
        
        PosButton Logo = new PosButton(ImageFactory.LOGO_TITLE.icon(72, 64));
        add(Logo);
        StartUI.setStyle(layout, styles, Logo, "Logo");
        
        PosButton Index = new PosButton(TEXT.getString("headerTitle.string"), Color.WHITE, StartUI.BORDER_COLOR);
        Index.setFont(FontFactory.FONT_12_BOLD);
        Index.setVerticalAlignment(SwingConstants.BOTTOM);
        Index.setVerticalTextPosition(SwingConstants.BOTTOM);
        Index.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				push(HomePanel.build( DashboardPanel.class ));
			}
		});
        add(Index, "Cell 1 0, grow");
        
        // Status Bar.
        JPanel statusbarPanel = new StatusBarX();
        statusbarPanel.setBorder( new LineBorder( Color.gray ) {
            public void paintBorder ( Component c, Graphics g, int x, int y, int width, int height ) {
                super.paintBorder( c, g, x, y, width, height );
                if ( ( this.thickness > 0 ) && ( g instanceof Graphics2D ) ) {
                	g.setColor( this.lineColor );
                    g.drawLine( 0, 0, width, 0 );
                }
            }
        } );
        statusbarPanel.setBackground( Color.BLACK );
        add( statusbarPanel, "cell 0 2 2 1,grow" );

        
        statusbarPanel.setLayout( new MigLayout( "ins 0 0 0 12, gap 0, fill", "24![left][center][grow]", "[fill]" ) );
        lblVersion = new JLabel();
        lblVersion.setHorizontalAlignment( SwingConstants.RIGHT );
        lblVersion.setVerticalAlignment( SwingConstants.CENTER );
        lblVersion.setText("Version : 2.1.6");
        
        lblMemoryUsage = new JLabel( "" );
        lblValidity = new JLabel();
        lblValidity.setHorizontalAlignment( SwingConstants.RIGHT );
        lblValidity.setVerticalAlignment( SwingConstants.CENTER );
        
        lblMemoryUsage.setForeground(StartUI.LIFTSITE_COLOR);
        lblValidity.setForeground(StartUI.LIFTSITE_COLOR);
        lblVersion.setForeground(StartUI.LIFTSITE_COLOR);
        statusbarPanel.add( lblMemoryUsage, "cell 1 0,gapleft" );
        statusbarPanel.add( lblValidity, "cell 2 0,growx" );
        statusbarPanel.add( lblVersion, "cell 3 0, grow" );
        
        
        Timer timer = new Timer(1000, new ActionListener() {
            
            String toString(long value) {
                value/=1000;
                if (value>1000) {
                    return (value/1000) + "M";
                } else
                    return value + "K";
            }
            
            @Override
            public void actionPerformed ( ActionEvent e ) {                
                Runtime runtime = Runtime.getRuntime();
                long maxMemory = runtime.maxMemory();
                long freeMemory = runtime.freeMemory();
                long totalMemory = runtime.totalMemory();
                
                lblMemoryUsage.setText(String.format(Dict.lookup( "MemoryUsage" ), toString(totalMemory-freeMemory), toString(totalMemory), toString(maxMemory)));
            }
        });
        timer.start();

        body = new JPanel();
        add( body, "cell 0 1 2 1" );
        body.setLayout( new BorderLayout( 0, 0 ) );

        Properties p = new Properties();
        try {
			p.load( BaseFactory.class.getClassLoader().getResourceAsStream( "config.properties" ) );
			file = p.getProperty( "basefile" );
			ini = new INIFile(file);
			release_date = Long.parseLong(new String(decoder.decode(ini.getStringProperty("settings","release_date")),"UTF-8")) ;
			last_date = Long.parseLong(new String(decoder.decode(ini.getStringProperty("settings","last_date")),"UTF-8"));
			invalid_date = release_date + 15552000;
			Timer timer2 = new Timer(3000, new ActionListener() {
				@Override
				public void actionPerformed ( ActionEvent e ) { 
					try {
						lblValidity.setText(String.format(Dict.lookup( "Validity_Date" ),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date( invalid_date * 1000)) ));
						if(System.currentTimeMillis()/1000 <= release_date || System.currentTimeMillis()/1000 <= last_date || System.currentTimeMillis()/1000 >= invalid_date ) {
							System.exit(0);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.exit(0);
					}
				}
			});
			timer2.start();
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}

    }
    
    public static long getTime(String time_url) {
    	long ld = 0;
    	try {
    		URL url = new URL(time_url);
    		URLConnection uc = url.openConnection();
    		uc.connect();
    		ld = uc.getDate();
    		System.out.println("");
    		/*
    		Date date = new Date(ld);
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    		return sdf.format(date);
			*/
    	} catch (Exception e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
    	}
    	return ld; 
    }

    public class StatusBarX extends JPanel implements MouseListener, MouseMotionListener {

        private static final long serialVersionUID = 1L;
        private Polygon           resizeCorner     = new Polygon();
        private int               offsetX;
        private int               offsetY;
        private Dimension         offsetSize;
        private Cursor            resizeCursor     = new Cursor( Cursor.SE_RESIZE_CURSOR );
        private Cursor            defaultCursor    = new Cursor( Cursor.DEFAULT_CURSOR );


        public StatusBarX () {
            super();
            this.addMouseListener( this );
            this.addMouseMotionListener( this );
        }


        private void createResizeHandle () {
            resizeCorner.reset();
            int size = 12;
            resizeCorner.addPoint( getWidth(), getHeight() );
            resizeCorner.addPoint( getWidth() - size, getHeight() );
            resizeCorner.addPoint( getWidth(), getHeight() - size );
        }


        @Override
        public void paint ( Graphics g ) {
            super.paint( g );
            Graphics2D g2;
            g2 = ( Graphics2D )g;
            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            g2.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
            createResizeHandle();
            g2.setColor( Color.gray );
            g2.fillPolygon( resizeCorner );
        }


        @Override
        public void mouseDragged ( MouseEvent e ) {
            if ( ( e.getModifiers() & InputEvent.BUTTON1_MASK ) != 0 ) {
                if ( offsetSize != null ) {
                    int width = ( int )( this.offsetSize.getWidth() - this.offsetX + e.getXOnScreen() );
                    int height = ( int )( this.offsetSize.getHeight() - this.offsetY + e.getYOnScreen() );
                    this.getRootPane().getParent().setSize( width, height );
                    createResizeHandle();
                }
            }
        }


        @Override
        public void mouseMoved ( MouseEvent e ) {
            if ( resizeCorner.contains( e.getX(), e.getY() ) ) {
                setCursor( resizeCursor );
            } else {
                setCursor( defaultCursor );
            }
        }


        @Override
        public void mouseClicked ( MouseEvent arg0 ) {
        }


        @Override
        public void mouseEntered ( MouseEvent arg0 ) {
        }


        @Override
        public void mouseExited ( MouseEvent arg0 ) {
        }


        @Override
        public void mousePressed ( MouseEvent e ) {
            if ( resizeCorner.contains( e.getX(), e.getY() ) ) {
                this.offsetX = e.getXOnScreen();
                this.offsetY = e.getYOnScreen();
                this.offsetSize = this.getRootPane().getParent().getSize();
            }
        }


        @Override
        public void mouseReleased ( MouseEvent arg0 ) {
            this.offsetSize = null;
        }
    }


    /**
     * put IPanelBinder into workspace.
     * 
     * @param panel
     */
    public void push ( IPanelBinder panel ) {
        if ( panel != null ) {
            IPanelBinder oldMainPanel = StartUI.getLiftSelector().hostedBinder;
            if ( oldMainPanel != null ) {
                Throwable thrown = null;
                try {
                    oldMainPanel.onStop();
                } catch ( RuntimeException x ) {
                    thrown = x;
                } catch ( Error x ) {
                    thrown = x;
                    throw x;
                } catch ( Throwable x ) {
                    thrown = x;
                    throw new Error( x );
                } finally {
                    if ( thrown != null )
                        logger.error( "uncheck exception:[onPause]", thrown );
                }
            }
            StartUI.getLiftSelector().hostedBinder = panel;
            if ( StartUI.getLiftSelector().hostedBinder instanceof Component && StartUI.getLiftSelector().hostedBinder.isRelaceWorkspace() ) {
                body.removeAll();
                body.add( ( Component )StartUI.getLiftSelector().hostedBinder );
            }

            Throwable thrown = null;
            try {
            	StartUI.getLiftSelector().hostedBinder.onStart();
            } catch ( RuntimeException x ) {
                thrown = x;
            } catch ( Error x ) {
                thrown = x;
                throw x;
            } catch ( Throwable x ) {
                thrown = x;
                throw new Error( x );
            } finally {
                if ( thrown != null )
                    logger.error( "uncheck exception:[onStart]", thrown );
            }
        }
    }

    public void paintComponent ( Graphics g ) {
        Painter<JPanel> painter = null;
        if ( StartUI.getLiftSelector().hostedBinder != null )
            painter = StartUI.getLiftSelector().hostedBinder.getFlag();
        if ( painter != null && g instanceof Graphics2D ) {
            int width = getWidth();
            int height = getHeight();
            painter.paint( ( Graphics2D )g, this, width, height );
        } else {
            super.paintComponent( g );
        }
    }
    
    @Override
    public void paint(Graphics g) {
    	super.paint(g);
		if(!isEnabled()) {
			g.drawImage(ImageFactory.TRANSPARENT_BACKGROUND.image(), getX(), getY(), getSize().width, getSize().height, null);
		}else {
			g.drawImage(null, getX(), getY(), getSize().width, getSize().height, null);
		}
    }    
}
