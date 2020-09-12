package slecon;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.util.PageTreeExpression;
import logic.util.SiteManagement;
import net.miginfocom.swing.MigLayout;
import base.cfg.BaseFactory;

import comm.Parser_Error;
import comm.constants.AuthLevel.Role;

public final class ToolBox {
    public final static ResourceBundle getResourceBundle ( String key ) {
        try {
            return ResourceBundle.getBundle( key + "_" + BaseFactory.getLocaleString().toLowerCase() );
        } catch ( MissingResourceException e ) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean requestRole ( LiftConnectionBean connBean, PageTreeExpression expr ) {
        boolean hasRole = expr.evaluate( SiteManagement.getVersion( connBean ), ToolBox.getRoles( connBean ) );
        if ( ! hasRole ) {
            ToolBox.showLoginPanel( connBean );
            hasRole = expr.evaluate( SiteManagement.getVersion( connBean ), ToolBox.getRoles( connBean ) );
        }
        return hasRole;
    }


    public static Role[] getRoles ( LiftConnectionBean connBean ) {
        return SiteManagement.getRoles( connBean );
    }


    public static boolean showLoginPanel ( final LiftConnectionBean connBean ) {
        final boolean[] result = new boolean[] { false };
        Runnable runnable = new Runnable() {
            @Override
            public void run () {
                final JLabel lblUsername = new JLabel( Dict.lookup( "Username" ) );
                final JLabel lblPassword = new JLabel( Dict.lookup( "Password" ) );
                final JTextField txtUsername = new JTextField( 15 );
                final JPasswordField txtPassword = new JPasswordField( 15 );

                lblUsername.setLabelFor( txtUsername );
                lblPassword.setLabelFor( txtPassword );
                lblUsername.setDisplayedMnemonic( 'U' );
                lblPassword.setDisplayedMnemonic( 'P' );

                JPanel customPanel = new JPanel( new MigLayout( "fill", "[right]12[grow]", "[]10[]" ) );
                customPanel.add( lblUsername, "cell 0 0" );
                customPanel.add( txtUsername, "cell 1 0" );
                customPanel.add( lblPassword, "cell 0 1" );
                customPanel.add( txtPassword, "cell 1 1" );
                customPanel.addComponentListener( new ComponentAdapter() {
                    public void componentShown ( ComponentEvent ce ) {
                        txtUsername.requestFocus( true );
                    }
                } );

                int ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), customPanel, Dict.lookup( "Login" ), JOptionPane.OK_CANCEL_OPTION );
                if ( ans == JOptionPane.OK_OPTION ) {
                    String username = txtUsername.getText();
                    String password = String.copyValueOf( txtPassword.getPassword() );

                    SiteManagement.auth( connBean, username, password );
                    result[ 0 ] = true;
                    return;
                }
            }
        };
        if ( SwingUtilities.isEventDispatchThread() )
            runnable.run();
        else {
            try {
                SwingUtilities.invokeAndWait( runnable );
            } catch ( InvocationTargetException | InterruptedException e ) {
                e.printStackTrace();
            }
        }
        // TODO sleep a while if fail.
        return result[ 0 ];
    }
    
    
    public static String[] showFtpLoginPanel () {
        final String[] UserParam = new String[] {"","","",""};
        
        final JLabel lblHost = new JLabel( Dict.lookup( "Host" ) );
        final JLabel lblPort = new JLabel( Dict.lookup( "Port" ) );
        final JLabel lblUsername = new JLabel( Dict.lookup( "Username" ) );
        final JLabel lblPassword = new JLabel( Dict.lookup( "Password" ) );
        final JTextField txtHost = new JTextField( 20 );
        final JTextField txtPort = new JTextField( 5 );
        final JTextField txtUsername = new JTextField( 20 );
        final JPasswordField txtPassword = new JPasswordField( 20 );
        
        lblHost.setLabelFor( txtHost );
        lblPort.setLabelFor( txtPort );
        lblUsername.setLabelFor( txtUsername );
        lblPassword.setLabelFor( txtPassword );
        
        JPanel customPanel = new JPanel( new MigLayout( "fill", "[right]12[grow]", "[]10[]10[]" ) );
        customPanel.add( lblHost, "cell 0 0" );
        customPanel.add( txtHost, "cell 1 0" );
        customPanel.add( lblPort, "cell 0 1" );
        customPanel.add( txtPort, "cell 1 1" );
        customPanel.add( lblUsername, "cell 0 2" );
        customPanel.add( txtUsername, "cell 1 2" );
        customPanel.add( lblPassword, "cell 0 3" );
        customPanel.add( txtPassword, "cell 1 3" );
        
        txtHost.setText("192.168.124.188");
        txtPort.setText("7474");
        
        int ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), customPanel, Dict.lookup( "Login" ), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE );
        
        if ( ans == JOptionPane.OK_OPTION ) {
        	String host = txtHost.getText();
        	String port = txtPort.getText();
            String username = txtUsername.getText();
            String password = String.copyValueOf( txtPassword.getPassword() );
            
            UserParam[ 0 ] = host;
            UserParam[ 1 ] = port;
            UserParam[ 2 ] = username;
            UserParam[ 3 ] = password;
        }
        
        // TODO sleep a while if fail.
        return UserParam;
    }
    
    public static String InputInfoPanel () {
        String FileName = "";
        final JLabel lblName = new JLabel( Dict.lookup( "BackupName" ) );
        final JTextField txtName = new JTextField( 20 );
        
        lblName.setLabelFor( txtName );
        
        JPanel customPanel = new JPanel( new MigLayout( "fill", "[center]12[center]", "[]" ) );
        customPanel.add( lblName, "cell 0 0" );
        customPanel.add( txtName, "cell 0 1" );
        
        int ans = JOptionPane.showConfirmDialog( StartUI.getFrame(), customPanel, Dict.lookup( "NOTIFY" ), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE );
        
        if ( ans == JOptionPane.OK_OPTION ) {
        	FileName = txtName.getText();
        }
        return FileName;
    }
    

    private static final boolean debugMode = false; //StartUI.class.getPackage().getSpecificationTitle() == null;


    public final static boolean isDebugMode () {
        return debugMode;
    }


    public static void showErrorMessage ( final String text ) {
        Runnable runnable = new Runnable() {
            @Override
            public void run () {
                JOptionPane.showMessageDialog( StartUI.getFrame(), text, Dict.lookup( "ERROR" ), JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE );
            }
        };
        if ( SwingUtilities.isEventDispatchThread() )
            runnable.run();
        else {
            try {
                SwingUtilities.invokeAndWait( runnable );
            } catch ( InvocationTargetException | InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }

    public static final class AVersion implements Comparable<AVersion> {
        public final long major;
        public final long minor;


        public AVersion ( long major, long minor ) {
            super();
            this.major = major;
            this.minor = minor;
        }


        @Override
        public String toString () {
            return "AVersion [major=" + major + ", minor=" + minor + "]";
        }


        @Override
        public int compareTo ( AVersion o ) {
            int a1 = Long.compare( major, o.major );
            int a2 = Long.compare( minor, o.minor );
            return a1 != 0 ? a1 : a2;
        }
    }


    public static void main ( String... args ) {
        final AVersion v = parserVersion( "2.1.17" );
        System.out.println( v );
        System.out.println( v.compareTo( new AVersion( 3, 1 ) ) );
    }


    public static AVersion parserVersion ( String versionString ) {
        ArrayList<Long> terms = new ArrayList<>();
        if ( versionString != null )
            try {
                for ( int i = 0; i < versionString.length(); i++ ) {
                    char c = versionString.charAt( i );
                    if ( Character.isAlphabetic( c ) ) {
                        terms.add( ( long )( Character.toUpperCase( c ) - 'A' ) );
                    } else if ( Character.isDigit( c ) ) {
                        String str = "";
                        int j;
                        for ( j = i; j < versionString.length(); j++ ) {
                            char c1 = versionString.charAt( j );
                            if ( Character.isDigit( c1 ) ) {
                                str += c1;
                                continue;
                            }
                            break;
                        }
                        if ( str.isEmpty() )
                            throw new NumberFormatException();
                        terms.add( Long.parseLong( str ) );
                        i = j;
                    } else if ( c == '.' ) {
                        continue;
                    }
                }
            } catch ( NumberFormatException ex ) {
            }
        long major = 0, minor = 0;
        if ( terms.size() >= 2 ) {
            major = terms.get( 0 );
            minor = terms.get( 1 );
        }
        AVersion result = new AVersion( major, minor );
        return result;
    }


    public static final void showRemoteErrorMessage ( LiftConnectionBean connBean, Parser_Error error ) {
        if ( error.getDeploymentError() || error.getEventError() || error.getMcsError() || error.getMcsNvramError() || error.getModuleError()
        	 || error.getDoorEnableError() || error.getUpdateError() || error.getEPSError() ) {
            final ResourceBundle bundle = getResourceBundle( "ParserErrorText" );
            StringBuffer sb = new StringBuffer();
            if ( error.getDeploymentError() ) {
                sb.append( bundle.getString( "Deployment.text" ) );
                sb.append( System.getProperty( "line.seperator" ) );
                sb.append( System.getProperty( "line.seperator" ) );
            }
            if ( error.getEventError() ) {
                sb.append( bundle.getString( "Event.text" ) );
                sb.append( System.getProperty( "line.seperator" ) );
                sb.append( System.getProperty( "line.seperator" ) );
            }
            if ( error.getMcsError() ) {
                sb.append( bundle.getString( "MCS.text" ) );
                sb.append( System.getProperty( "line.seperator" ) );
                sb.append( System.getProperty( "line.seperator" ) );
            }
            if ( error.getMcsNvramError() ) {
                sb.append( bundle.getString( "NVRAM.text" ) );
                sb.append( System.getProperty( "line.seperator" ) );
                sb.append( System.getProperty( "line.seperator" ) );
            }
            if ( error.getModuleError() ) {
                sb.append( bundle.getString( "Module.text" ) );
                sb.append( System.getProperty( "line.seperator" ) );
                sb.append( System.getProperty( "line.seperator" ) );
            }
            
            if ( error.getDoorEnableError() ) {
                sb.append( bundle.getString( "Door_Enable.text" ) );
                sb.append( System.getProperty( "line.seperator" ) );
                sb.append( System.getProperty( "line.seperator" ) );
            }
            
            if ( error.getUpdateError() ) {
                sb.append( bundle.getString( "Update.text" ) );
                sb.append( System.getProperty( "line.seperator" ) );
                sb.append( System.getProperty( "line.seperator" ) );
            }

            final String title = connBean.getName();
            final String text = sb.toString();

            Runnable runnable = new Runnable() {
                @Override
                public void run () {
                    JOptionPane.showMessageDialog( StartUI.getFrame(), text, Dict.lookup( "ERROR" ) + " " + title, JOptionPane.OK_OPTION
                            | JOptionPane.ERROR_MESSAGE );
                }
            };
            if ( SwingUtilities.isEventDispatchThread() )
                runnable.run();
            else {
                try {
                    SwingUtilities.invokeAndWait( runnable );
                } catch ( InvocationTargetException | InterruptedException e ) {
                    e.printStackTrace();
                }
            }
        }
    }
}
