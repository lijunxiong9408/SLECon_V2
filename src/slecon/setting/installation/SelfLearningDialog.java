package slecon.setting.installation;

import static logic.util.SiteManagement.MON_MGR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ScrollBarUI;

import base.cfg.ImageFactory;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.evlog.ErrorLog;
import logic.evlog.MCSErrorCode;
import logic.util.PageTreeExpression;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.PanelBinder;
import slecon.home.PosButton;
import slecon.home.dashboard.VerticalSrcollBarUI;
import comm.Parser_Log;
import comm.Parser_Misc;
import comm.agent.AgentMessage;
import comm.event.LiftDataChangedListener;

public class SelfLearningDialog extends JDialog implements ActionListener, LiftDataChangedListener {

    private final static PageTreeExpression WRITE_MCS_EXPRESSION = new PageTreeExpression("write_mcs");
    
    private static final Icon                     critIcon                  = ImageFactory.INSPECT_LOG_ICON_CRITICAL.icon();
    private static final Icon                     normIcon                  = ImageFactory.INSPECT_LOG_ICON_GENERAL.icon();
    private static final Icon                     noneIcon                  = ImageFactory.NONE.icon( 14, 14 );
    private static final Icon                     checkIcon                 = ImageFactory.CHECKED.icon( 14, 14 );

    private static final ResourceBundle           TEXT                      = ToolBox.getResourceBundle( "logic.gui.SelfLearningDialog" );
    private static final HashMap<Integer, String> textMap;
    private static final HashMap<Integer, Icon>   iconMap;
    private static final long                     serialVersionUID          = 8671267956827185361L;

    private static final String                   escapeStrokeActionCommand = "selfLearningDialog:WINDOW_CLOSING";
    private final JPanel                          contentPanel              = new JPanel();
    private LiftConnectionBean                    connBean;
    private PosButton                             btnStartStop;
    private PosButton                             btnCancel;

    private boolean                               running                   = false;

    private Parser_Misc                           misc;

    private Parser_Log                            log;
    private DefaultListModel<ErrorLog>            listModel;
    private JLabel                                iconExitFromLowerShaftLimit;
    private JLabel                                iconGotoLowestDoorzone;
    private JLabel                                iconStartLearning;
    private JLabel                                iconParkToHighestDoorzone;
    private JLabel                                iconFinish;
    private int                                   lastLogIndex;

    
    public static void main ( String... strings ) {
        MCSErrorCode[] codes = {
            MCSErrorCode.EVLOG_SLDZ_EXIT_LSL,
            MCSErrorCode.EVLOG_SLDZ_EXIT_LSL_ERROR,
            MCSErrorCode.EVLOG_SLDZ_GOTO_LOWEST_DZ,
            MCSErrorCode.EVLOG_SLDZ_GOTO_LOWEST_DZ_ERROR,
            MCSErrorCode.EVLOG_SLDZ_GOTO_HIGHEST_DZ,
            MCSErrorCode.EVLOG_SLDZ_GOTO_HIGHEST_DZ_ERROR,
            MCSErrorCode.EVLOG_SLDZ_NEW_DATA_SAVED,
            MCSErrorCode.EVLOG_SLDZ_PARK_TO_HIGHEST_DZ,
            MCSErrorCode.EVLOG_SLDZ_PARK_TO_HIGHEST_DZ_ERROR,
            MCSErrorCode.EVLOG_SLDZ_FINISH,
            MCSErrorCode.EVLOG_SLDZ_LSL_EXITED,
            MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_LSL,
            MCSErrorCode.EVLOG_SLDZ_LOWEST_DZ_ARRIVED,
            MCSErrorCode.EVLOG_SLDZ_LSL_FOUND,
            MCSErrorCode.EVLOG_SLDZ_USL_FOUND,
            MCSErrorCode.EVLOG_SLDZ_LDZ_FOUND,
            MCSErrorCode.EVLOG_SLDZ_UDZ_FOUND,
            MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_USL,
            MCSErrorCode.EVLOG_SLDZ_TOO_MUCH_DZ_FOUND,
            MCSErrorCode.EVLOG_SLDZ_ALL_DZ_FOUND,
        }; 
        
        for ( MCSErrorCode c : codes ) {
            String s1 = c.getDescription();
            String s2 = TEXT.getString( textMap.get( c.code ) );
            if ( ! s1.equals( s2 ) ) {
                System.out.println( c );
                System.out.printf( "  %s\n  %s\n", s1, s2 );
            }
        }
    }
    
    /**
     * Create the dialog.
     */
    private SelfLearningDialog () {
        super( StartUI.getFrame(), Dialog.ModalityType.DOCUMENT_MODAL );
        initGUI();

        setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        setLocationRelativeTo( StartUI.getFrame() );
        getRootPane().registerKeyboardAction( this, escapeStrokeActionCommand, KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),
                JComponent.WHEN_IN_FOCUSED_WINDOW );
    }
    
    public static void doSelfLeraning(LiftConnectionBean connBean) {
        SelfLearningDialog dialog = new SelfLearningDialog();
        dialog.connBean = connBean;
        //dialog.setTitle("Self Learning - " + connBean.getSite().getName() + ":" + connBean.getName());
        dialog.setVisible(true);
    }


    private void initGUI () {
        setTitle( TEXT.getString( "Dialog.title" ) );

        setBounds( 100, 100, 800, 600 );
        getContentPane().setLayout( new BorderLayout() );
        contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
        contentPanel.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        getContentPane().add( contentPanel, BorderLayout.CENTER );
        contentPanel.setLayout( new MigLayout( "ins 24, gap 12", "[][30,fill][][grow]", "[][][][][]24![grow]" ) );
        {
            iconExitFromLowerShaftLimit = new JLabel( "" );
            contentPanel.add( iconExitFromLowerShaftLimit, "cell 1 0" );
        }
        {
            JLabel lblExitFromLowerShaft = new JLabel( TEXT.getString( "lblExitFromLowerShaft.text" ) );
            lblExitFromLowerShaft.setForeground(Color.WHITE);
            contentPanel.add( lblExitFromLowerShaft, "cell 2 0" );
        }
        {
            iconGotoLowestDoorzone = new JLabel( "" );
            iconGotoLowestDoorzone.setForeground(Color.WHITE);
            contentPanel.add( iconGotoLowestDoorzone, "cell 1 1" );
        }
        {
            JLabel lblGotoLowestDoorzone = new JLabel( TEXT.getString("lblGotoLowestDoorzone.text") );
            lblGotoLowestDoorzone.setForeground(Color.WHITE);
            contentPanel.add( lblGotoLowestDoorzone, "cell 2 1" );
        }
        {
            iconStartLearning = new JLabel( "" );
            iconStartLearning.setForeground(Color.WHITE);
            contentPanel.add( iconStartLearning, "cell 1 2" );
        }
        {
            JLabel lblStartLearning = new JLabel( TEXT.getString("lblStartLearning.text") );
            lblStartLearning.setForeground(Color.WHITE);
            contentPanel.add( lblStartLearning, "cell 2 2" );
        }
        {
            iconParkToHighestDoorzone = new JLabel( "" );
            iconParkToHighestDoorzone.setForeground(Color.WHITE);
            contentPanel.add( iconParkToHighestDoorzone, "cell 1 3" );
        }
        {
            JLabel lblParkToHighestDoorzone = new JLabel( TEXT.getString("lblParkToHighestDoorzone.text") );
            lblParkToHighestDoorzone.setForeground(Color.WHITE);
            contentPanel.add( lblParkToHighestDoorzone, "cell 2 3" );
        }
        {
            iconFinish = new JLabel( "" );
            iconFinish.setForeground(Color.WHITE);
            contentPanel.add( iconFinish, "cell 1 4" );
        }
        {
            JLabel lblFinish = new JLabel( TEXT.getString("lblFinish.text") );
            lblFinish.setForeground(Color.WHITE);
            contentPanel.add( lblFinish, "cell 2 4" );
        }
        {
            JScrollPane scrollPane = new JScrollPane();
            contentPanel.add( scrollPane, "cell 0 5 4 1,grow" );
            {
                listModel = new DefaultListModel<>();
                JList<ErrorLog> list = new JList<>( listModel );
                list.setBackground(StartUI.SUB_BACKGROUND_COLOR);

                scrollPane.setViewportView( list );
                scrollPane.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
                scrollPane.getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
                
                list.setCellRenderer( new DefaultListCellRenderer() {

                    private static final long serialVersionUID = 5592084984703817968L;


                    public Component getListCellRendererComponent ( JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
                        super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
                        if ( value instanceof ErrorLog ) {
                            ErrorLog eLog = ( ErrorLog )value;
                            setText( toText( eLog ) );
                            setForeground(Color.WHITE);
                            setIcon( toIcon( eLog ) );
                        }
                        return this;
                    }
                } );
            }
        }
        {
            JPanel panel = new JPanel();
            panel.setBackground(StartUI.SUB_BACKGROUND_COLOR);
            contentPanel.add( panel, "cell 3 0 1 5,grow" );
            panel.setLayout( new MigLayout( "fill", "[grow, right]", "[]" ) );
            {
                btnStartStop = new PosButton( TEXT.getString( "Button.start" ), ImageFactory.BUTTON_PAUSE.icon(87, 30) );
                btnStartStop.addActionListener( this );
                panel.add( btnStartStop, "w 160!, cell 0 0, flowy, gapbottom 20" );
            }
            {
                btnCancel = new PosButton( TEXT.getString( "Button.cancel" ), ImageFactory.BUTTON_PAUSE.icon(87, 30) );
                btnCancel.addActionListener( this );
                panel.add( btnCancel, "w 160!, cell 0 0" );
            }
        }
    }
    
//    ParserMisc misc = new ParserMisc();

    
    public void actionPerformed ( final ActionEvent e ) {
        if ( e.getSource() == btnCancel || e.getActionCommand() == escapeStrokeActionCommand ) {
            do_btnCancel_actionPerformed( e );
        }
        if ( e.getSource() == btnStartStop ) {
            do_btnStartStop_actionPerformed( e );
        }
    }
    
    
    public String toText ( ErrorLog aLog ) {
        if ( aLog.rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_LSL.code )
            return String.format( TEXT.getString( textMap.get( ( int )aLog.rawLog.errcode ) ), aLog.rawLog.current_floor );
        if ( aLog.rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_USL.code )
            return String.format( TEXT.getString( textMap.get( ( int )aLog.rawLog.errcode ) ), aLog.rawLog.current_floor );
        if ( aLog.rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_LSL_FOUND.code )
            return String.format( TEXT.getString( textMap.get( ( int )aLog.rawLog.errcode ) ), aLog.rawLog.position );
        if ( aLog.rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_USL_FOUND.code )
            return String.format( TEXT.getString( textMap.get( ( int )aLog.rawLog.errcode ) ), aLog.rawLog.position );
        if ( aLog.rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_LDZ_FOUND.code )
            return String.format( TEXT.getString( textMap.get( ( int )aLog.rawLog.errcode ) ), aLog.rawLog.current_floor, aLog.rawLog.position );
        if ( aLog.rawLog.errcode == MCSErrorCode.EVLOG_SLDZ_UDZ_FOUND.code )
            return String.format( TEXT.getString( textMap.get( ( int )aLog.rawLog.errcode ) ), aLog.rawLog.current_floor, aLog.rawLog.position );

        return TEXT.getString( textMap.get( ( int )aLog.rawLog.errcode ) );
    }
    

    public Icon toIcon ( ErrorLog aLog ) {
        final Icon icon = iconMap.get( ( int )aLog.rawLog.errcode );
        return icon != null ? icon : noneIcon;
    }
    
    static {
        textMap = new HashMap<>();
        textMap.put( MCSErrorCode.EVLOG_SLDZ_EXIT_LSL.code, "EVLOG_SLDZ_EXIT_LSL.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_EXIT_LSL_ERROR.code, "EVLOG_SLDZ_EXIT_LSL_ERROR.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_GOTO_LOWEST_DZ.code, "EVLOG_SLDZ_GOTO_LOWEST_DZ.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_GOTO_LOWEST_DZ_ERROR.code, "EVLOG_SLDZ_GOTO_LOWEST_DZ_ERROR.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_GOTO_HIGHEST_DZ.code, "EVLOG_SLDZ_GOTO_HIGHEST_DZ.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_GOTO_HIGHEST_DZ_ERROR.code, "EVLOG_SLDZ_GOTO_HIGHEST_DZ_ERROR.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_NEW_DATA_SAVED.code, "EVLOG_SLDZ_NEW_DATA_SAVED.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_PARK_TO_HIGHEST_DZ.code, "EVLOG_SLDZ_PARK_TO_HIGHEST_DZ.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_PARK_TO_HIGHEST_DZ_ERROR.code, "EVLOG_SLDZ_PARK_TO_HIGHEST_DZ_ERROR.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_FINISH.code, "EVLOG_SLDZ_FINISH.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_LSL_EXITED.code, "EVLOG_SLDZ_LSL_EXITED.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_LSL.code, "EVLOG_SLDZ_DZ_FOUND_IN_LSL.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_LOWEST_DZ_ARRIVED.code, "EVLOG_SLDZ_LOWEST_DZ_ARRIVED.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_LSL_FOUND.code, "EVLOG_SLDZ_LSL_FOUND.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_USL_FOUND.code, "EVLOG_SLDZ_USL_FOUND.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_LDZ_FOUND.code, "EVLOG_SLDZ_LDZ_FOUND.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_UDZ_FOUND.code, "EVLOG_SLDZ_UDZ_FOUND.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_USL.code, "EVLOG_SLDZ_DZ_FOUND_IN_USL.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_TOO_MUCH_DZ_FOUND.code, "EVLOG_SLDZ_TOO_MUCH_DZ_FOUND.text" );
        textMap.put( MCSErrorCode.EVLOG_SLDZ_ALL_DZ_FOUND.code, "EVLOG_SLDZ_ALL_DZ_FOUND.text" );

        iconMap = new HashMap<>();
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_EXIT_LSL_ERROR.code, critIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_GOTO_LOWEST_DZ_ERROR.code, critIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_GOTO_HIGHEST_DZ_ERROR.code, critIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_PARK_TO_HIGHEST_DZ_ERROR.code, critIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_TOO_MUCH_DZ_FOUND.code, critIcon );

        iconMap.put( MCSErrorCode.EVLOG_SLDZ_EXIT_LSL.code, normIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_GOTO_LOWEST_DZ.code, normIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_GOTO_HIGHEST_DZ.code, normIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_NEW_DATA_SAVED.code, normIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_PARK_TO_HIGHEST_DZ.code, normIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_FINISH.code, normIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_LSL_EXITED.code, normIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_LOWEST_DZ_ARRIVED.code, normIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_ALL_DZ_FOUND.code, normIcon );

        iconMap.put( MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_LSL.code, noneIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_LSL_FOUND.code, noneIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_USL_FOUND.code, noneIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_LDZ_FOUND.code, noneIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_UDZ_FOUND.code, noneIcon );
        iconMap.put( MCSErrorCode.EVLOG_SLDZ_DZ_FOUND_IN_USL.code, noneIcon );
    }
    

    public void analyseAErrorLog ( final ErrorLog aLog ) {
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run () {
                if ( textMap.containsKey( ( int )aLog.rawLog.errcode ) ) {
                    listModel.addElement( aLog );
                    switch(aLog.rawLog.errcode){
                    	case 0x004D : //MCSErrorCode.EVLOG_SLDZ_LSL_EXITED.code :
                    	case 0x0044 : //MCSErrorCode.EVLOG_SLDZ_EXIT_LSL_ERROR.code :
                    	case 0x0045 : //EVLOG_SLDZ_GOTO_LOWEST_DZ.code :
                    		iconExitFromLowerShaftLimit.setIcon( checkIcon );
                    	break;
                    	
                    	case 0x004F: //MCSErrorCode.EVLOG_SLDZ_LOWEST_DZ_ARRIVED.code :
                    	case 0x0046: //MCSErrorCode.EVLOG_SLDZ_GOTO_LOWEST_DZ_ERROR.code :
                    		iconGotoLowestDoorzone.setIcon( checkIcon );
                    	break;
                    	
                    	case 0x0056: //MCSErrorCode.EVLOG_SLDZ_ALL_DZ_FOUND.code :
                    	case 0x0047: //MCSErrorCode.EVLOG_SLDZ_GOTO_HIGHEST_DZ.code :
                    	case 0x0048: //MCSErrorCode.EVLOG_SLDZ_GOTO_HIGHEST_DZ_ERROR.code :
                    	case 0x0055: //MCSErrorCode.EVLOG_SLDZ_TOO_MUCH_DZ_FOUND.code :
                    		iconStartLearning.setIcon( checkIcon );
                    	break;
                    	
                    	case 0x0049 : //EVLOG_SLDZ_NEW_DATA_SAVED :
                    	case 0x004A: //MCSErrorCode.EVLOG_SLDZ_PARK_TO_HIGHEST_DZ.code :
                    	case 0x004B: //MCSErrorCode.EVLOG_SLDZ_PARK_TO_HIGHEST_DZ_ERROR.code :
                    		iconParkToHighestDoorzone.setIcon( checkIcon );
                    	break;
                    	
                    	case 0x004C: //MCSErrorCode.EVLOG_SLDZ_FINISH.code :
                    		iconFinish.setIcon( checkIcon );
                    		stop();
                    	break;
                    }
                }
            }
        } );
    }
    

    public void start () {
        if ( ! running ) {
            if ( JOptionPane.showConfirmDialog( this, TEXT.getString( "Confirm.start.text" ), TEXT.getString( "Confirm.title" ),
                    JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION
                    && JOptionPane.showConfirmDialog( this, TEXT.getString( "Confirm.clearLog.text" ), TEXT.getString( "Confirm.title" ),
                            JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {
                setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
                btnStartStop.setText( TEXT.getString( "Button.abort" ) );
                btnCancel.setEnabled( false );
                running = true;
                misc = new Parser_Misc( connBean.getIp(), connBean.getPort() );
                log = new Parser_Log( connBean.getIp(), connBean.getPort() );

                MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(), AgentMessage.MISC.getCode() | AgentMessage.LOG.getCode()
                        | AgentMessage.ERROR.getCode() );

                log.clearLog();
                listModel.clear();
                misc.mcs( ( short )0x1803, new byte[] { 1 } ); // CMD_SELF_LEARN_DOORZONE
                lastLogIndex = 0;
            }
        }
    }

    
    public void stop () {
        if ( running ) {
            setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
            btnStartStop.setText( TEXT.getString( "Button.start" ) );
            btnCancel.setEnabled( true );
            running = false;

            MON_MGR.removeEventListener( this );
        }
    }
    
    
    public void abort () {
        if ( running ) {
            if ( JOptionPane.showConfirmDialog( this, TEXT.getString( "Confirm.abort.text" ), TEXT.getString( "Confirm.title" ),
                    JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {
                setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
                btnStartStop.setText( TEXT.getString( "Button.start" ) );
                btnCancel.setEnabled( true );
                running = false;

                MON_MGR.removeEventListener( this );
                misc.mcs( ( short )0x1803, new byte[] { 0 } ); // CMD_SELF_LEARN_DOORZONE
            }
        }
    }
    
    
    protected void do_btnStartStop_actionPerformed ( final ActionEvent e ) {
        if(!ToolBox.requestRole(connBean, WRITE_MCS_EXPRESSION)) {
            ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
            return;
        }
        if ( ! running )
            start();
        else
            abort();
    }
    
    
    protected void do_btnCancel_actionPerformed ( final ActionEvent e ) {
        if(!ToolBox.requestRole(connBean, WRITE_MCS_EXPRESSION)) {
            ToolBox.showErrorMessage(Dict.lookup("NoPermission"));
            return;
        }
        if ( ! running )
            dispose();
    }


    @Override
    public void onConnCreate () {
    }


    @Override
    public void onDataChanged ( long timestamp, int msg ) {
        if ( msg == AgentMessage.LOG.getCode() ) {
        	if(lastLogIndex == 100) {
        		lastLogIndex -= 1;
        	}
            for ( ; lastLogIndex < log.getCount(); lastLogIndex++ ) {
                //System.out.println( "ErrorLog current_floor : [" + log.getLog( lastLogIndex ).current_floor +"], pos : ["+log.getLog( lastLogIndex ).position+"]." );
                analyseAErrorLog( new ErrorLog( lastLogIndex, log.getLog( lastLogIndex ) ) );
            }
        }
    }


    @Override
    public void onConnLost () {
        PanelBinder.showErrorMessage( "ConnectionLost" );
        if ( running )
            MON_MGR.removeEventListener( this );
        dispose();
    }
}
