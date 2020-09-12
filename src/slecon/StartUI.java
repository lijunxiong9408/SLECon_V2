package slecon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;

import logic.connection.LiftConnectionBean;
import logic.util.SiteManagement;
import logic.util.Version;
import logic.util.VersionChangeListener;
import net.miginfocom.swing.MigLayout;
import slecon.home.HomePanel;
import slecon.home.dashboard.DashboardPanel;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.INIFile;
import base.cfg.ImageFactory;
import comm.MonitorMgr.listenerNeed;
import comm.event.LiftDataChangedListener;


public class StartUI extends JFrame {
	public static Color MAIN_BACKGROUND_COLOR = new Color(0x2D3A42);
	public static Color SUB_BACKGROUND_COLOR = new Color(0x334350);
	public static Color LIFTSITE_COLOR = new Color(0x00A099); 
	public static Color BORDER_COLOR = new Color(0x00A099);
	public static Color LOG_Button_COLOR = new Color(0xE9525C);
	public static Color CALL_Button_COLOR = new Color(0X0FBEE9);
	public static Color MOTION_Button_COLOR = new Color(0x4DA4FF);
	public static Color SHUTCUT_Button_COLOR = new Color(0xFFDD00);
	
    public StartUI() {
    }
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 6922097669093584271L;

    /** map containing all global actions */
    private static ConcurrentHashMap<KeyStroke, Action> actionMap = new ConcurrentHashMap<>( 16 );

    /**
     * The instantiated reference to this class.
     */
    private static StartUI self = null;

    /**
     * The major workspace of the application.
     */
    private TopMain topMain;
    private LiftSelectorPanel liftSelector;
    
    /**
     * 	Users.	
     * */
    private int UserCode = 0;	
    
    public synchronized static StartUI getFrame () {
        if ( self == null ) {
            self = new StartUI();
            self.initGUI();
        }
        return self;
    }


    private void initGUI () {
        this.setModalExclusionType( ModalExclusionType.APPLICATION_EXCLUDE );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        // Configure the main content pane.
        JPanel contentPane = ( JPanel )this.getContentPane();
        contentPane.setLayout( new BorderLayout() );
        
        liftSelector = new LiftSelectorPanel();
        
        // The major workspace for the application.
        topMain = new TopMain();
        topMain.push( HomePanel.build( DashboardPanel.class ) );
        contentPane.add( topMain, BorderLayout.CENTER );
    }

    public static TopMain getTopMain () {
        return getFrame().topMain;
    }

    public static LiftSelectorPanel getLiftSelector () {
        return getFrame().liftSelector;
    }

    /** call this somewhere in your GUI construction */
    public static void registerGlobalKey ( KeyStroke key1, Action action ) {
        actionMap.put( key1, action );
    }


    private static void setupKeyEventDispatcher () {
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addKeyEventDispatcher( new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent ( KeyEvent e ) {
                KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent( e );
                if ( actionMap.containsKey( keyStroke ) ) {
                    final Action a = actionMap.get( keyStroke );
                    if ( a.isEnabled() ) {
                        final ActionEvent ae = new ActionEvent( e.getSource(), e.getID(), null );
                        SwingUtilities.invokeLater( new Runnable() {
                            @Override
                            public void run () {
                                a.actionPerformed( ae );
                            }
                        } );
                        return true;
                    }
                }
                return false;
            }
        } );
    }


    /**
     * Halt the program.
     * Users have to restart the program manually.
     */
    public static int rebootRequest () {
        int ans = JOptionPane.showConfirmDialog( getFrame(), "You have to restart your program!", "Program restart", JOptionPane.ERROR_MESSAGE | JOptionPane.YES_NO_OPTION );
        if(ans == JOptionPane.YES_OPTION) {
            System.exit(-2);
            return 1;
        }
        return 0;
    }


    /**
     * Halt the program.
     * @param message   It specifies the message will show in the message dialog.
     * @param title     It specifies the title of the message dialog.
     */
    public static void crash ( String message, String title ) {
        JOptionPane.showMessageDialog( getFrame(), message, title, JOptionPane.ERROR_MESSAGE );
        System.exit( -1 );
    }


    /**
     * set default font of the swing UI (look and feel).
     */
    public static void setUIDefaultFont ( Font f ) {
        boolean             isSystemFont = false;
        GraphicsEnvironment ge           = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[]              fonts        = ge.getAllFonts();
        for ( Font sysFont : fonts ) {
            if ( sysFont.getFontName( Locale.forLanguageTag( BaseFactory.getLocaleString() ) ).equals(
                f.getName().trim() ) ) {
                isSystemFont = true;
                break;
            }
        }

        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while ( keys.hasMoreElements() ) {
            Object key = keys.nextElement();
            Object value = UIManager.get( key );

            if ( value != null && value instanceof javax.swing.plaf.FontUIResource ) {
                if ( isSystemFont )
                    value = new javax.swing.plaf.FontUIResource( f );
                else
                    value = new javax.swing.plaf.FontUIResource( new Font( ( ( javax.swing.plaf.FontUIResource )value ).getName(), f.getStyle(),
                            f.getSize() ) );

                UIManager.put( key, value );
            }
        }
    }


    /**
     * Launch the application.
     * @param args  It specifies the arguments read from command line.
     */
    public static void main ( String[] args ) {
        final long time = System.currentTimeMillis();
        // set Language
        Locale.setDefault( BaseFactory.getLocale() );
        
        // Enable monitor manager.
        SiteManagement.initMonitorMgr();

        // Enable default fonts.
        setUIDefaultFont( FontFactory.FONT_12_PLAIN );

        // Enable Anti-Alias.
        System.setProperty( "awt.useSystemAAFontSettings", "lcd" );
        System.setProperty( "swing.disablevistaanimation", "true" );
        
        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run () {

                // Setup look and feel.
                try {
                    UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
                } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e ) {
                    e.printStackTrace( System.err );
                }

                JOptionPane.setDefaultLocale( BaseFactory.getLocale() );
                ToolTipManager.sharedInstance().setInitialDelay( 300 );
                ToolTipManager.sharedInstance().setReshowDelay( 300 );
                ToolTipManager.sharedInstance().setDismissDelay( 3600000 );

                UIManager.put( "TabbedPane.tabInsets", new InsetsUIResource( 1, 4, 1, 4 ) );
                setupKeyEventDispatcher();
                
                StartUI frame = StartUI.getFrame();

                // Update the title of Main screen.
                String specTitle = StartUI.class.getPackage().getSpecificationTitle();
                String specVersion = StartUI.class.getPackage().getSpecificationVersion();
                String implementTitle = StartUI.class.getPackage().getImplementationTitle();
                String implementVersion = StartUI.class.getPackage().getImplementationVersion();
                if ( specVersion != null )
                    frame.setTitle( String.format( "%s %s-%s%s", specTitle, specVersion, implementTitle, implementVersion ) );
                else
                    frame.setTitle( "SLECon" );
                
                ImageIcon titleIcon = ImageFactory.LOGO_TITLE.icon(72, 64);
                frame.setIconImage(titleIcon.getImage());
                
                // Show the Main screen.
                frame.setMinimumSize( new Dimension( 1280, 720 ) );
                frame.setBounds( new Rectangle( 1280, 720 ) );
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );
            }
        } );

        if ( ToolBox.isDebugMode() ) {
            SiteManagement.addVersionChangeListener( new VersionChangeListener() {

                @Override
                public void versionChanged ( LiftConnectionBean connBean, Version newVersion ) {
                    System.out.printf( "IP:%s %s\n", connBean.getIp(), newVersion );
                }
            }, null );

            BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
            String str;
            try {
                while ( ( str = br.readLine() ) != null ) {
                    if ( "show".equals( str ) ) {
                        List<String> lines = new ArrayList<>();
                        for ( LiftDataChangedListener l : SiteManagement.MON_MGR.listeners.keySet() ) {
                            listenerNeed need = SiteManagement.MON_MGR.listeners.get( l );
                            String addr = need.hostname + ":" + need.port;
                            lines.add( l.getClass().getCanonicalName() + "\t " + addr );
                        }
                        Collections.sort( lines );
                        System.out.println( "MONMGR LISTENER:" );
                        for ( String l : lines ) {
                            System.out.print( "  " );
                            System.out.println( l );
                        }
                        System.out.printf("VersionListener: %d\n", SiteManagement.versionListener.size());
                    }
                }
            } catch ( IOException e ) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
        	@Override
    		public void run() {
    			try {
    				final Base64.Encoder encoder = Base64.getEncoder();
    				Properties p = new Properties();
    				p.load( BaseFactory.class.getClassLoader().getResourceAsStream( "config.properties" ) );
    				INIFile ini = new INIFile(p.getProperty( "basefile" ));
    				ini.setStringProperty( "settings", "last_date", encoder.encodeToString(String.valueOf(System.currentTimeMillis()/1000).getBytes("UTF-8")) , null);
    				ini.save();
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
        	
        } );
    }
    
    static class ParseString {
        static Pattern regex = Pattern.compile( "[^\\s\"']+|\"([^\"]*)\"|'([^']*)'" );

        public static synchronized String[] parse ( String str ) {
            Matcher      regexMatcher = regex.matcher( str );
            List<String> matchList    = new ArrayList<String>();
            while ( regexMatcher.find() ) {
                if ( regexMatcher.group( 1 ) != null ) {

                    // Add double-quoted string without the quotes
                    matchList.add( regexMatcher.group( 1 ) );
                } else if ( regexMatcher.group( 2 ) != null ) {

                    // Add single-quoted string without the quotes
                    matchList.add( regexMatcher.group( 2 ) );
                } else {

                    // Add unquoted word
                    String s = regexMatcher.group();
                    if ( s != null && s.length() > 0 )
                        matchList.add( regexMatcher.group() );
                }
            }
            return matchList.toArray( new String[ 0 ] );
        }
    }
    
    public static void setStyle (MigLayout layout, HashMap<String, String> styles, JComponent c, String name ) {
        String   s   = styles.get( name );
        String[] arr = ParseString.parse( s );
        try {
            int x = Integer.parseInt( arr[ 0 ] );
            int y = Integer.parseInt( arr[ 1 ] );
            int w = Integer.parseInt( arr[ 2 ] );
            int h = Integer.parseInt( arr[ 3 ] );
            c.setPreferredSize( new Dimension( w, h ) );
            layout.setComponentConstraints( c, String.format( "x %d, y %d, w %d!, h %d!", x, y, w, h ) );

            int horizontal = SwingUtilities.LEFT;
            if ( arr.length >= 5 && arr[ 4 ] != null ) {
                if ( arr[ 4 ].equals( "l" ) )
                    horizontal = SwingUtilities.LEFT;
                else if ( arr[ 4 ].equals( "c" ) )
                    horizontal = SwingUtilities.CENTER;
                else if ( arr[ 4 ].equals( "r" ) )
                    horizontal = SwingUtilities.RIGHT;
                if ( c instanceof JLabel ) {
                    ( ( JLabel )c ).setHorizontalTextPosition( horizontal );
                    ( ( JLabel )c ).setHorizontalAlignment( horizontal );
                } else if ( c instanceof AbstractButton ) {
                    ( ( AbstractButton )c ).setHorizontalAlignment( horizontal );
                    ( ( AbstractButton )c ).setHorizontalAlignment( horizontal );
                }
            }
        } catch ( Exception e ) {
            layout.setComponentConstraints( c, s );
        }
    }
}
