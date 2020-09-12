package vecspectra3;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sf.extcos.ComponentQuery;
import net.sf.extcos.ComponentScanner;
import slecon.interfaces.HomeAction;
import slecon.interfaces.HomeView;
import slecon.interfaces.InspectView;
import slecon.interfaces.SetupView;

public class PageLoaderConfigure {
    public static String buildInspectXML () {
        StringBuffer sb = new StringBuffer();
        ComponentScanner scanner = new ComponentScanner();
        Set<Class<?>> classSet = scanner.getClasses( new ComponentQuery() {
            @Override
            protected void query () {
                select().from( "slecon.inspect" ).returning( allBeing( and( subclassOf( JPanel.class ), annotatedWith( InspectView.class ) ) ) );
            }
        } );
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>( classSet );
        Collections.sort( classes, new Comparator<Class<?>>() {
            @Override
            public int compare ( Class<?> o1, Class<?> o2 ) {
                int result = 0;
                if ( o1 == null || o2 == null )
                    return 0;
                if ( o1.isAnnotationPresent( InspectView.class ) ) {
                    Integer a = o1.getAnnotation( InspectView.class ).sortIndex();
                    Integer b = o2.getAnnotation( InspectView.class ).sortIndex();
                    result = Integer.compare( a, b );
                    if ( result != 0 )
                        return result;

                    String c = o1.getAnnotation( InspectView.class ).path();
                    String d = o2.getAnnotation( InspectView.class ).path();
                    result = c.compareTo( d );
                    if ( result != 0 )
                        return result;
                }
                return o1.getCanonicalName().compareTo( o2.getCanonicalName() );
            }
        } );
        sb.append( "<InspectTree>\n" );
        for ( Class<?> c : classes ) {
            InspectView annotation = c.getAnnotation( InspectView.class );
            sb.append( String.format( "\t<Node sortIndex=\"%x\" path=\"%s\" class=\"%s\"><![CDATA[%s]]></Node>\n", annotation.sortIndex(),
                    annotation.path(), c.getCanonicalName(), annotation.condition() ) );
        }
        sb.append( "</InspectTree>\n" );
        return sb.toString();
    }


    public static String buildSetupXML () {
        StringBuffer sb = new StringBuffer();
        ComponentScanner scanner = new ComponentScanner();
        Set<Class<?>> classSet = scanner.getClasses( new ComponentQuery() {
            @Override
            protected void query () {
                select().from( "slecon.setting" ).returning( allBeing( and( subclassOf( JPanel.class ), annotatedWith( SetupView.class ) ) ) );
            }
        } );
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>( classSet );
        Collections.sort( classes, new Comparator<Class<?>>() {
            @Override
            public int compare ( Class<?> o1, Class<?> o2 ) {
                int result = 0;
                if ( o1 == null || o2 == null )
                    return 0;
                if ( o1.isAnnotationPresent( SetupView.class ) ) {
                    Integer a = o1.getAnnotation( SetupView.class ).sortIndex();
                    Integer b = o2.getAnnotation( SetupView.class ).sortIndex();
                    result = Integer.compare( a, b );
                    if ( result != 0 )
                        return result;

                    String c = o1.getAnnotation( SetupView.class ).path();
                    String d = o2.getAnnotation( SetupView.class ).path();
                    result = c.compareTo( d );
                    if ( result != 0 )
                        return result;
                }
                return o1.getCanonicalName().compareTo( o2.getCanonicalName() );
            }
        } );
        sb.append( "<SetupTree>\n" );
        for ( Class<?> c : classes ) {
            SetupView annotation = c.getAnnotation( SetupView.class );
            sb.append( String.format( "\t<Node sortIndex=\"%x\" path=\"%s\" class=\"%s\"><![CDATA[%s]]></Node>\n", annotation.sortIndex(),
                    annotation.path(), c.getCanonicalName(), annotation.condition() ) );
        }
        sb.append( "</SetupTree>\n" );
        return sb.toString();
    }


    private static String buildHomeXML () {
        StringBuffer sb = new StringBuffer();
        ComponentScanner scanner = new ComponentScanner();
        Set<Class<?>> classSet1 = scanner.getClasses( new ComponentQuery() {
            @Override
            protected void query () {
                select().from( "slecon.home" ).returning( allBeing( and( subclassOf( JPanel.class ), annotatedWith( HomeView.class ) ) ) );
            }
        } );
        Set<Class<?>> classSet2 = scanner.getClasses( new ComponentQuery() {
            @Override
            protected void query () {
                select().from( "slecon.home" ).returning( allBeing( and( implementorOf( Action.class ), annotatedWith( HomeAction.class ) ) ) );
            }
        } );
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>( classSet1 );
        classes.addAll( classSet2 );
        Collections.sort( classes, new Comparator<Class<?>>() {
            @Override
            public int compare ( Class<?> o1, Class<?> o2 ) {
                int result = 0;
                if ( o1 == null || o2 == null )
                    return 0;
                if ( o1.isAnnotationPresent( HomeView.class ) && o2.isAnnotationPresent( HomeView.class ) ) {
                    Integer a = o1.getAnnotation( HomeView.class ).sortIndex();
                    Integer b = o2.getAnnotation( HomeView.class ).sortIndex();
                    result = Integer.compare( a, b );
                    if ( result != 0 )
                        return result;

                    String c = o1.getAnnotation( HomeView.class ).name();
                    String d = o2.getAnnotation( HomeView.class ).name();
                    result = c.compareTo( d );
                    if ( result != 0 )
                        return result;
                } else if ( o1.isAnnotationPresent( HomeView.class ) && o2.isAnnotationPresent( HomeAction.class ) ) {
                    Integer a = o1.getAnnotation( HomeView.class ).sortIndex();
                    Integer b = o2.getAnnotation( HomeAction.class ).sortIndex();
                    result = Integer.compare( a, b );
                    if ( result != 0 )
                        return result;
                } else if ( o1.isAnnotationPresent( HomeAction.class ) && o2.isAnnotationPresent( HomeView.class ) ) {
                    Integer a = o1.getAnnotation( HomeAction.class ).sortIndex();
                    Integer b = o2.getAnnotation( HomeView.class ).sortIndex();
                    result = Integer.compare( a, b );
                    if ( result != 0 )
                        return result;
                } else if ( o1.isAnnotationPresent( HomeAction.class ) && o2.isAnnotationPresent( HomeAction.class ) ) {
                    Integer a = o1.getAnnotation( HomeAction.class ).sortIndex();
                    Integer b = o2.getAnnotation( HomeAction.class ).sortIndex();
                    result = Integer.compare( a, b );
                    if ( result != 0 )
                        return result;
                }
                return o1.getCanonicalName().compareTo( o2.getCanonicalName() );
            }
        } );
        sb.append( "<HomeView>\n" );
        for ( Class<?> panelClass : classes ) {
            String condition = "";
            if ( panelClass.getAnnotation( HomeView.class ) != null ) {
                condition = panelClass.getAnnotation( HomeView.class ).condition();
            }
            if ( panelClass.getAnnotation( HomeAction.class ) != null ) {
                condition = panelClass.getAnnotation( HomeAction.class ).condition();
            }
            if ( condition.trim().length() == 0 )
                sb.append( String.format( "\t<Node class=\"%s\"/>\n", panelClass.getCanonicalName() ) );
            else
                sb.append( String.format( "\t<Node class=\"%s\"><![CDATA[%s]]></Node>\n", panelClass.getCanonicalName(), condition ) );
        }
        sb.append( "</HomeView>\n" );
        return sb.toString();
    }


    public static String buildXML () {
        StringBuffer sb = new StringBuffer();
        Scanner scanner;
        sb.append( "<vecSpectra>\n" );
        scanner = new Scanner( buildHomeXML() );
        while ( scanner.hasNextLine() ) {
            String line = scanner.nextLine();
            sb.append( '\t' );
            sb.append( line );
            sb.append( '\n' );
        }
        scanner.close();
        scanner = new Scanner( buildSetupXML() );
        while ( scanner.hasNextLine() ) {
            String line = scanner.nextLine();
            sb.append( '\t' );
            sb.append( line );
            sb.append( '\n' );
        }
        scanner.close();
        scanner = new Scanner( buildInspectXML() );
        while ( scanner.hasNextLine() ) {
            String line = scanner.nextLine();
            sb.append( '\t' );
            sb.append( line );
            sb.append( '\n' );
        }
        scanner.close();
        sb.append( "</vecSpectra>\n" );
        return sb.toString();
    }


    public static void main ( String... strings ) {
        final JFrame frame = new JFrame();
        final JTextArea ta = new JTextArea();
        final JButton btn = new JButton( "Save" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.add( new JScrollPane( ta ) );
        frame.add( btn, BorderLayout.NORTH );
        ta.setText( buildXML() );
        frame.setSize( 800, 600 );
        frame.setVisible( true );
        btn.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                final JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory( new File( System.getProperty( "user.dir" ) ) );
                fc.setSelectedFile( new File( "PageLoader.xml" ) );
                if ( JFileChooser.APPROVE_OPTION == fc.showSaveDialog( frame ) ) {
                    File file = fc.getSelectedFile();
                    if ( ! file.exists()
                            || file.exists()
                            && JOptionPane.showConfirmDialog( frame, "Replaced File", "The file exist, replaced it?", JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {
                        saveFile( file );
                    }
                }
            }


            private void saveFile ( File file ) {
                try {
                    FileWriter writer = new FileWriter( file );
                    writer.write( ta.getText() );
                    writer.close();
                } catch ( IOException e ) {
                    JOptionPane.showMessageDialog( frame, "Problem on saving file" );
                    e.printStackTrace();
                }
            }
        } );
    }
}
