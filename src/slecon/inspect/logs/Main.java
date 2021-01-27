package slecon.inspect.logs;
import static logic.util.SiteManagement.MON_MGR;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import logic.Dict;
import logic.connection.LiftConnectionBean;
import logic.evlog.ErrorLog;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SubPanelBinder;
import slecon.component.SubtleSquareBorder;
import slecon.component.Workspace;
import slecon.home.PosButton;
import slecon.home.dashboard.VerticalSrcollBarUI;
import slecon.inspect.InspectPanel;
import slecon.inspect.iostatus.VecToolTipManager;
import slecon.inspect.logs.LogTableFilter.LogFilterBean;
import slecon.interfaces.InspectView;
import slecon.interfaces.Page;
import comm.Parser_Deploy;
import comm.Parser_Log;
import comm.Parser_McsConfig;
import comm.Parser_Misc;
import comm.agent.AgentMessage;
import comm.event.LiftDataChangedListener;


@InspectView(
    path      = "MotionStatus",
    sortIndex = 0x100
)
public class Main extends JPanel implements Page, ActionListener, LiftDataChangedListener {
    private static final long serialVersionUID = -552013171218549161L;
    static final ResourceBundle TEXT = ToolBox.getResourceBundle("inspect.Log");
    public final static DateFormat      DATE_FORMAT            = new SimpleDateFormat( "yyyyMMdd" );
    public final static DateFormat      TIME_FORMAT            = new SimpleDateFormat( "HHmmss" );
    private ImageIcon	BUTTON_ICON		= ImageFactory.BUTTON_PAUSE.icon(87,30);
    final   LiftConnectionBean 		 connBean;
    private Workspace                workspace;
    private SubPanelBinder 			 subpanel;
    private JPanel		  	  panelNavigation;
    private JPanel		  	  panelMain;
    private JPanel		  	  panelLiftSelector;
    private JLabel			  labSearchTitle;
    private JPanel			  panelSearch;
    private JLabel            lblFoundValue;
    private PosButton         btnDelete;
    private PosButton         btnTruncate;
    private PosButton         btnExportLog;
    private JScrollPane 	  scrollPane;
    private	MigLayout 	      layout;
    static HashMap<String, String>      styles                  = new HashMap<>();
    {
    	styles.put("panelLiftSelector", "30 20 250 60 c");
    	styles.put("labSearchTitle", "20 80 100 30 l");
    	styles.put("panelSearch", "20 110 960 150 c" );
    	styles.put("lblFoundValue", "20 280 100 20 l");
    	styles.put("btnDelete", "690 280 87 30 c");
    	styles.put("btnTruncate", "790 280 87 30 c");
    	styles.put("btnExportLog", "890 280 87 30 c");
    	styles.put("scrollPane", "20 320 960 370 c");
    }
    
    private PosButton                   btnSearch;
    private PosButton                   btnClear;
    private MyComboBox           		cboCode;
    private MyComboBox      			cboType;
    private TooltipFormattedTextField   fmtStartDate;
    private TooltipFormattedTextField   fmtEndDate;
    private TooltipFormattedTextField   fmtStartTime;
    private TooltipFormattedTextField   fmtEndTime;
    private CodeComboBoxModel           codeModel;
    private Timer                       timer;
    private final VecToolTipManager     tooltipManager;
    private LogTable                    table;
    private LogTableModel               logModel;
    private Parser_Log                  log;
    private Parser_Misc                 misc;
    Parser_Deploy               		deploy;
    Parser_McsConfig           	 		mcsconfig;
    private final Object                deployMutex            = new Object();
    private final Map<Integer, String>  floorTexts             = new TreeMap<>();
    private final Map<Integer, Integer> doorzones              = new TreeMap<>();
    private String[] navigationText = {Dict.lookup("Inspect"), Dict.lookup("Log")};
    
    
    ///////////////////// interface <Page> /////////////////////
    public Main ( LiftConnectionBean bean ) {
    	setLayout(new BorderLayout());
        initGUI();
        this.connBean = bean;

        timer = new Timer( 1000, this );
        timer.setInitialDelay( 0 );
        timer.start();

        tooltipManager = new VecToolTipManager();
        tooltipManager.registerToolTip( fmtStartDate );
        tooltipManager.registerToolTip( fmtEndDate );
        tooltipManager.registerToolTip( fmtStartTime );
        tooltipManager.registerToolTip( fmtEndTime );

        btnSearch.doClick();
        
    }

    private void initGUI () {
    	removeAll();
        JPanel workspace = new JPanel( new MigLayout( "gap 0", "[left]", "[30!]10[700]" ) );
        workspace.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        
    /*------------------------------------------------------------------------------------------------------------*/   
        panelNavigation = new JPanel(new MigLayout( "nogrid, w 985!, h 30!, gap 0", "[left]", "[center]" ));
        //panelNavigation.setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
        panelNavigation.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        workspace.add(panelNavigation,"cell 0 0");
        int index = 0;
        for (String text : navigationText) {
        	if(index > 0) {
        		PosButton icon = new PosButton(ImageFactory.ARROW_NAVIGATION.icon(11,12));
        		panelNavigation.add(icon);
			}
        	
        	PosButton lab = new PosButton(text, StartUI.BORDER_COLOR, Color.WHITE);
        	lab.setForeground(StartUI.BORDER_COLOR);
        	lab.setFont(FontFactory.FONT_12_BOLD);
        	if(index == 0) {
        		lab.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						final InspectPanel panelBinder = InspectPanel.build(connBean, null);
                        StartUI.getTopMain().push(panelBinder);
					}
				});
        	}
			panelNavigation.add(lab);
			index += 1;
		}
        
    /*------------------------------------------------------------------------------------------------------------*/        
        layout = new MigLayout( "nogrid, w 1000!, h 700!, gap 0", "[left]", "[center]" );
        panelMain = new JPanel();
        panelMain.setBorder(new SubtleSquareBorder(true, StartUI.BORDER_COLOR));
        panelMain.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelMain.setLayout(layout);
        workspace.add(panelMain,"cell 0 1");
      
        panelLiftSelector = new JPanel();
        panelLiftSelector.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelLiftSelector.add(StartUI.getLiftSelector());
        panelMain.add(panelLiftSelector);
        StartUI.setStyle(layout, styles, panelLiftSelector, "panelLiftSelector");
        
    /*------------------------------------------------------------------------------------------------------------*/   
        labSearchTitle = new JLabel(TEXT.getString( "Main.Search" ));
        labSearchTitle.setFont(FontFactory.FONT_12_BOLD);
        labSearchTitle.setForeground(Color.WHITE);
        panelMain.add(labSearchTitle);
        StartUI.setStyle(layout, styles, labSearchTitle, "labSearchTitle");
        
        panelSearch = new JPanel();
        panelSearch.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        panelSearch.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        panelMain.add(panelSearch);
        StartUI.setStyle(layout, styles, panelSearch, "panelSearch");
        panelSearch.setLayout(new MigLayout( "ins 25, aligny center", "[200!]30![200!][30!][200!,grow]", "[bottom][center]15[bottom][center]") );
        
        // Type.
        JLabel lblType = new JLabel( TEXT.getString( "Main.Type" ) );
        lblType.setFont( FontFactory.FONT_12_BOLD );
        lblType.setForeground(Color.WHITE);
        panelSearch.add( lblType, "cell 0 0,alignx left" );
        cboType = new MyComboBox();
        cboType.setModel( new DefaultComboBoxModel<>( TypeOptions.values() ) );
        cboType.setSelectedItem( TypeOptions.All );
        panelSearch.add( cboType, "cell 0 1,growx" );
        
        // Error Code.
        JLabel lblErrorCode = new JLabel( TEXT.getString( "Main.ErrorCode" ) );
        lblErrorCode.setFont( FontFactory.FONT_12_BOLD );
        lblErrorCode.setForeground(Color.WHITE);
        panelSearch.add( lblErrorCode, "cell 0 2,alignx left" );
        cboCode = new MyComboBox();
        codeModel = new CodeComboBoxModel();
        cboCode.setEditable( false );
        cboCode.setModel( codeModel );
        cboCode.setRenderer( new CodeComboBoxRenderer() );
        panelSearch.add( cboCode, "cell 0 3,growx" );
        
        //Date Range
        JLabel lblDateRange = new JLabel( TEXT.getString( "Main.DateRange" ) );
        lblDateRange.setFont( FontFactory.FONT_12_BOLD );
        lblDateRange.setForeground(Color.WHITE);
        panelSearch.add( lblDateRange, "cell 1 0,alignx left" );
        DateFormatter editFormatter1 = new DateFormatter( new SimpleDateFormat( "yyyy-MM-dd" ) ) {
            public Object stringToValue ( String text ) throws ParseException {
                if ( text != null && text.trim().length() == 0 )
                    return null;
                return super.stringToValue( text );
            }
        };
        DefaultFormatterFactory dateFactory = new DefaultFormatterFactory( new DateFormatter( new SimpleDateFormat( "yyyy-MM-dd" ) ),
                new DateFormatter( DateFormat.getDateInstance( DateFormat.LONG, BaseFactory.getLocale() ) ), editFormatter1, new DateFormatter(
                        new SimpleDateFormat( "yyyy-MM-dd" ) ) );
        DateFormatter editFormatter2 = new DateFormatter( new SimpleDateFormat( "HH:mm:ss" ) ) {
            public Object stringToValue ( String text ) throws ParseException {
                if ( text != null && text.trim().length() == 0 )
                    return null;
                return super.stringToValue( text );
            }
        };
        DefaultFormatterFactory timeFactory = new DefaultFormatterFactory( new DateFormatter( new SimpleDateFormat( "HH:mm:ss" ) ),
                new DateFormatter( DateFormat.getTimeInstance( DateFormat.LONG, BaseFactory.getLocale() ) ), editFormatter2, new DateFormatter(
                        new SimpleDateFormat( "HH:mm:ss" ) ) );
        fmtStartDate = new TooltipFormattedTextField( dateFactory );
        fmtStartDate.addPropertyChangeListener( "value", new FmtStartDatePropertyChangeListener() );
        fmtStartDate.setValue( null );
        panelSearch.add( fmtStartDate, "cell 1 1,growx" );
        
        JLabel lblTo = new JLabel( TEXT.getString( "Main.to" ) );
        lblTo.setFont( FontFactory.FONT_12_BOLD );
        lblTo.setForeground(Color.WHITE);
        panelSearch.add( lblTo, "cell 2 1,alignx center" );
        
        fmtEndDate = new TooltipFormattedTextField( dateFactory );
        fmtEndDate.addPropertyChangeListener( "value", new FmtEndDatePropertyChangeListener() );
        fmtEndDate.setValue( null );
        panelSearch.add( fmtEndDate, "cell 3 1,growx" );
        
        // Time Range
        JLabel lblTimeRange = new JLabel( TEXT.getString( "Main.TimeRange" ) );
        lblTimeRange.setFont( FontFactory.FONT_12_BOLD );
        lblTimeRange.setForeground(Color.WHITE);
        panelSearch.add( lblTimeRange, "cell 1 2,alignx left" );
        
        fmtStartTime = new TooltipFormattedTextField( timeFactory );
        fmtStartTime.addPropertyChangeListener( "value", new FmtStartTimePropertyChangeListener() );
        fmtStartTime.setValue( null );
        panelSearch.add( fmtStartTime, "cell 1 3,growx" );
        
        JLabel lblTo_1 = new JLabel( TEXT.getString( "Main.to" ) );
        lblTo_1.setFont( FontFactory.FONT_12_BOLD );
        lblTo_1.setForeground(Color.WHITE);
        panelSearch.add( lblTo_1, "cell 2 3,alignx center" );
        
        fmtEndTime = new TooltipFormattedTextField( timeFactory );
        fmtEndTime.addPropertyChangeListener( "value", new FmtEndTimePropertyChangeListener() );
        fmtEndTime.setValue( null );
        panelSearch.add( fmtEndTime, "cell 3 3,growx" );
        
        // Clear & Search Buton.
        btnSearch = new PosButton( TEXT.getString( "Main.btnSearch" ), BUTTON_ICON );
        btnSearch.setFont(FontFactory.FONT_12_BOLD);
        btnSearch.addActionListener( this );
        panelSearch.add( btnSearch, "pos null null visual.x2-20 visual.y2+5,id btnsearch,cell 0 0,width 100!" );
        
        btnClear = new PosButton( TEXT.getString( "Main.btnClear" ), BUTTON_ICON );
        btnClear.setFont(FontFactory.FONT_12_BOLD);
        btnClear.addActionListener( this );
        panelSearch.add( btnClear, "pos null null btnsearch.x2 btnsearch.y-10,cell 0 0,width 100!" );
       
        /*------------------------------------------------------------------------------------------------------------*/ 
       
        lblFoundValue = new JLabel( "  " );
        lblFoundValue.setFont(FontFactory.FONT_12_PLAIN);
        lblFoundValue.setForeground(Color.WHITE);
        panelMain.add(lblFoundValue);
        StartUI.setStyle(layout, styles, lblFoundValue, "lblFoundValue");
        
        btnDelete = new PosButton( TEXT.getString( "Main.btnDelete" ), BUTTON_ICON );
        btnDelete.setFont(FontFactory.FONT_12_BOLD);
        btnDelete.addActionListener( this );
        panelMain.add(btnDelete);
        StartUI.setStyle(layout, styles, btnDelete, "btnDelete");

        btnTruncate = new PosButton( TEXT.getString( "Main.Truncate" ), BUTTON_ICON );
        btnTruncate.setFont(FontFactory.FONT_12_BOLD);
        btnTruncate.addActionListener( this );
        panelMain.add(btnTruncate);
        StartUI.setStyle(layout, styles, btnTruncate, "btnTruncate");
        
        btnExportLog = new PosButton( TEXT.getString( "Main.ExportLog" ), BUTTON_ICON );
        btnExportLog.setFont(FontFactory.FONT_12_BOLD);
        btnExportLog.addActionListener( this );
        panelMain.add(btnExportLog);
        StartUI.setStyle(layout, styles, btnExportLog, "btnExportLog");
        
        logModel = new LogTableModel();
        table = new LogTable( logModel );
        table.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        
        InputMap im = table.getInputMap(JTable.WHEN_FOCUSED);
        ActionMap am = table.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Delete");
        
        Action deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                do_btnDelete_actionPerformed( e );
            }

        };
        am.put("Delete", deleteAction);
        
        final TableMouseListener mouseAdapter = new TableMouseListener();
        table.addMouseListener( mouseAdapter );
        table.addMouseMotionListener( mouseAdapter );
        table.getRowSorter().addRowSorterListener( new RowSorterListener() {
            @Override
            public void sorterChanged ( RowSorterEvent e ) {
                if ( table.getRowSorter().getViewRowCount() > 0 ) {
                    lblFoundValue.setText( String.format( TEXT.getString( "Main.found.Normal" ), table.getRowSorter().getViewRowCount() ) );
                } else {
                    lblFoundValue.setText( String.format( TEXT.getString( "Main.found.NoData" ), table.getRowSorter().getViewRowCount() ) );
                }
                if ( new LogFilterBean().equals( table.getFilter() ) && table.getRowSorter().getViewRowCount() <= 0 ) {
                    btnClear.setEnabled( false );
                    btnSearch.setEnabled( false );
                } else {
                    btnClear.setEnabled( true );
                    btnSearch.setEnabled( true );
                }
            }
        } );
        
        scrollPane = new JScrollPane() {
            private static final long serialVersionUID = 1093186574754190090L;
            {
            	setBorder( BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
                setOpaque( false );
                getViewport().setOpaque( false );
                getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
            }
        };
        panelMain.add( scrollPane);
        StartUI.setStyle(layout, styles, scrollPane, "scrollPane");
        scrollPane.setViewportView( table );
        
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
    
    private class TableMouseListener extends MouseAdapter {
        private int   index = -1;
        private Timer timer = new Timer( 1600, new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                cancelTimer();
                doClickEvent();
            }
        } );
        


        
        @Override
        public void mousePressed ( MouseEvent evt ) {
            if ( evt.getClickCount() == 1 ) {
                index = table.rowAtPoint( evt.getPoint() );
                timer.start();
            }
            if ( evt.getClickCount() == 2 ) {
                cancelTimer();
                doClickEvent();
            }
        }


        @Override
        public void mouseReleased ( MouseEvent evt ) {
            cancelTimer();
        }


        @Override
        public void mouseMoved ( MouseEvent evt ) {
            if ( timer.isRunning() && index != table.rowAtPoint( evt.getPoint() ) ) {
                index = table.rowAtPoint( evt.getPoint() );
                timer.restart();
            }
        }


        @Override
        public void mouseDragged ( MouseEvent evt ) {
            if ( timer.isRunning() && index != table.rowAtPoint( evt.getPoint() ) ) {
                index = table.rowAtPoint( evt.getPoint() );
                timer.restart();
            }
        }


        @Override
        public void mouseEntered ( MouseEvent evt ) {
            cancelTimer();
        }


        @Override
        public void mouseExited ( MouseEvent evt ) {
            cancelTimer();
        }


        private void cancelTimer () {
            index = - 1;
            timer.stop();
        }


        private void doClickEvent () {
            cancelTimer();
            doClickLogTableEvent();
        }
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
    	this.workspace = workspace;
    }

    @Override
    public void onStart() throws Exception {
    	try {
            log = new Parser_Log( connBean.getIp(), connBean.getPort() );
            misc = new Parser_Misc( connBean.getIp(), connBean.getPort() );
            deploy = new Parser_Deploy( connBean.getIp(), connBean.getPort() );
            mcsconfig = new Parser_McsConfig(connBean.getIp(), connBean.getPort());

            MON_MGR.addEventListener( this, connBean.getIp(), connBean.getPort(), AgentMessage.DEPLOYMENT.getCode() | AgentMessage.LOG.getCode()
                    | AgentMessage.ERROR.getCode() | AgentMessage.MCS_CONFIG.getCode() );
            setHot();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.nanoTime();
        }
    }


    @Override
    public void onResume () throws Exception {}


    @Override
    public void onPause () throws Exception {}


    @Override
    public void onStop () throws Exception {
    	tooltipManager.hideToolTipPopup();
        timer.stop();
        removeAll();
        MON_MGR.removeEventListener( this );
    }

    @Override
    public void onDestroy () {}

    @Override
    public void onConnCreate() {
        setHot();
    }
    
    @Override
    public void onConnLost() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onDataChanged(long timestamp, int msg) {
        setHot();
    }
    
    protected TreeMap<Integer, String> getFloorText () {
        synchronized ( deployMutex ) {
            return new TreeMap<>( floorTexts );
        }
    }


    protected TreeMap<Integer, Integer> getDoorzones () {
        synchronized ( deployMutex ) {
            return new TreeMap<>( doorzones );
        }
    }
    
    private void initFloorText () {
        Map<Integer, String> floorTexts = new TreeMap<>();
        Map<Integer, Integer> doorzones = new TreeMap<>();
        for ( int i = 0, count = deploy.getFloorCount(); i < count; i++ ) {
            int dz = deploy.getDoorZone( ( byte )i );
            if ( dz != - 1 ) {
                floorTexts.put( i, deploy.getFloorText( ( byte )i ) );
                doorzones.put( i, dz );
            }
        }
        synchronized ( deployMutex ) {
            this.floorTexts.clear();
            this.floorTexts.putAll( floorTexts );
            this.doorzones.clear();
            this.doorzones.putAll( doorzones );
        }
    }
    
    public void addErrorLog ( final ErrorLog logEntry ) {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                logModel.addLog( logEntry );
                codeModel.setRecommendList( new ArrayList<Integer>( logModel.getAllErrorCodeID() ) );
            }
        } );
    }

    public void deleteLog ( final int index ) {
        new Thread() {
            public void run () {
                log.deleteLog( ( byte )index );
            }
        }.start();
    }
    
    public void clearLog () {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run () {
                logModel.clear();
            }
        } );
    }
    
    public void setHot() {
    	initFloorText();
        clearLog();
        for ( int i = log.getCount() - 1; i >= 0; i-- ) {
            addErrorLog( new ErrorLog( i, log.getLog( i ) ) );
        }
    }
    
    public void setSelectedIndex(int index) {
        index = table.convertRowIndexToView( index );
        if(index>=0) {
            table.setRowSelectionInterval( index, index);
            table.scrollRectToVisible(table.getCellRect(index, 1, true));
        } else 
            table.clearSelection();
    }
    
    private Date truncDate ( Date date ) {
        if ( date != null )
            try {
                return DATE_FORMAT.parse( DATE_FORMAT.format( date ) );
            } catch ( ParseException e ) {
            }
        return null;
    }


    private Date truncTime ( Date date ) {
        if ( date != null )
            try {
                return TIME_FORMAT.parse( TIME_FORMAT.format( date ) );
            } catch ( ParseException e ) {
            }
        return null;
    }


    private boolean dateBefore ( Date date1, Date date2 ) {
        date1 = truncDate( date1 );
        date2 = truncDate( date2 );

        Calendar c1 = Calendar.getInstance();
        c1.setTime( date1 );

        Calendar c2 = Calendar.getInstance();
        c2.setTime( date2 );
        return c1.before( c2 );
    }


    private boolean timeBefore ( Date date1, Date date2 ) {
        date1 = truncTime( date1 );
        date2 = truncTime( date2 );

        Calendar c1 = Calendar.getInstance();
        c1.setTime( date1 );

        Calendar c2 = Calendar.getInstance();
        c2.setTime( date2 );
        return c1.before( c2 );
    }
    
    private class FmtStartDatePropertyChangeListener implements PropertyChangeListener {
        public void propertyChange ( final PropertyChangeEvent evt ) {
            Date date1 = fmtStartDate == null ? null : ( Date )fmtStartDate.getValue();
            Date date2 = fmtEndDate == null ? null : ( Date )fmtEndDate.getValue();
            date1 = truncDate( date1 );
            date2 = truncDate( date2 );
            if ( date1 != null && ( date2 == null || ! dateBefore( date1, date2 ) ) ) {
                Calendar c = Calendar.getInstance();
                c.setTime( date1 );
                c.add( Calendar.DATE, 1 );
                fmtEndDate.setValue( c.getTime() );
            }
        }
    }

    private class FmtStartTimePropertyChangeListener implements PropertyChangeListener {
        public void propertyChange ( final PropertyChangeEvent evt ) {
            Date date1 = fmtStartTime == null ? null : ( Date )fmtStartTime.getValue();
            Date date2 = fmtEndTime == null ? null : ( Date )fmtEndTime.getValue();
            if ( date1 != null && ( date2 == null || ! timeBefore( date1, date2 ) ) ) {
                Calendar c = Calendar.getInstance();
                c.setTime( date1 );
                c.add( Calendar.HOUR, 1 );
                fmtEndTime.setValue( c.getTime() );
            }
        }
    }
    
    private class FmtEndDatePropertyChangeListener implements PropertyChangeListener {
        public void propertyChange ( final PropertyChangeEvent evt ) {
            Date date1 = ( Date )fmtStartDate.getValue();
            Date date2 = ( Date )fmtEndDate.getValue();
            date1 = truncDate( date1 );
            date2 = truncDate( date2 );
            if ( date2 != null && ( date1 == null || ! dateBefore( date1, date2 ) ) ) {
                Calendar c = Calendar.getInstance();
                c.setTime( date2 );
                c.add( Calendar.DATE, - 1 );
                fmtStartDate.setValue( c.getTime() );
            }
        }
    }

    private class FmtEndTimePropertyChangeListener implements PropertyChangeListener {
        public void propertyChange ( final PropertyChangeEvent evt ) {
            Date date1 = fmtStartTime == null ? null : ( Date )fmtStartTime.getValue();
            Date date2 = fmtEndTime == null ? null : ( Date )fmtEndTime.getValue();
            if ( date2 != null && ( date1 == null || ! timeBefore( date1, date2 ) ) ) {
                Calendar c = Calendar.getInstance();
                c.setTime( date2 );
                c.add( Calendar.HOUR, - 1 );
                fmtStartTime.setValue( c.getTime() );
            }
        }
    }
    
    private final class CodeComboBoxRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 3479118499394332129L;
        private DefaultListCellRenderer defaultCellRenderer = new DefaultListCellRenderer();

        public Component getListCellRendererComponent ( JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
        	JLabel renderer = (JLabel)defaultCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        	renderer.setForeground(Color.WHITE);
        	if(isSelected){
        		renderer.setBackground(StartUI.BORDER_COLOR);
        	}else{
        		renderer.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        	}
        	list.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
        	renderer.setFont(FontFactory.FONT_12_PLAIN);
        	renderer.setHorizontalAlignment(JLabel.CENTER);
		  
        	if (value != null && value instanceof String && !value.equals("")) {
        		int id = Integer.parseInt( ( String )value );
    			if ( cellHasFocus || isSelected )
            	  value = String.format( "%d (0x%04x)", id, id );
    			else
                  value = String.format( "<html>%d <font color=gray>(0x%04x)</font>", id, id );
        	}else {
        		value = "--";
        	}
        	renderer.setText(value.toString());
        	return renderer;
        }
    }
    
    protected static final class TooltipFormattedTextField extends JFormattedTextField {
        private static final long serialVersionUID = 8506024310666848461L;
        private String            tooltipText;
        
        @Override
        public void setCaretColor(Color c) {
        	// TODO Auto-generated method stub
        	super.setCaretColor(Color.WHITE);
        }
        
        @Override
        public void setBorder(Border border) {
        	// TODO Auto-generated method stub
        	super.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        }
        
        @Override
        public void setBackground(Color bg) {
        	// TODO Auto-generated method stub
        	super.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        }
        
        @Override
        public void setForeground(Color fg) {
        	// TODO Auto-generated method stub
        	super.setForeground(Color.WHITE);
        }
        
        public TooltipFormattedTextField ( AbstractFormatterFactory factory ) {
            super( factory );
        }


        @Override
        public String getToolTipText () {
            return tooltipText;
        }


        @Override
        public void setToolTipText ( String text ) {
            this.tooltipText = text;
        }
    }
    
    private final class CodeComboBoxModel extends DefaultComboBoxModel<String> {
        private static final long serialVersionUID = 1872921239166706987L;


        public void setRecommendList ( List<Integer> list ) {
            final List<String> list1 = new ArrayList<>();
            final List<String> list2 = new ArrayList<>();
            list1.add( "" );
            for ( int id : list )
                list1.add( Integer.toString( id ) );
            for ( int i = 0; i < codeModel.getSize(); i++ ) {
                list2.add( codeModel.getElementAt( i ) );
            }

            final List<String> add = new ArrayList<>();
            final List<String> del = new ArrayList<>();
            for ( String s : list1 ) {
                if ( ! list2.contains( s ) )
                    add.add( s );
            }
            for ( String s : list2 ) {
                if ( ! list1.contains( s ) )
                    del.add( s );
            }
            Collections.sort( add );
            Collections.sort( del );
            for ( String s : del )
                codeModel.removeElement( s );

            int i = 0;
            ADD_LOOP: while ( ! add.isEmpty() ) {
                final String s = add.remove( 0 );
                for ( ;; i++ ) {
                    if ( i >= codeModel.getSize() )
                        break;

                    int a = 0;
                    int b = 0;
                    try {
                        a = Integer.parseInt( s );
                        b = Integer.parseInt( codeModel.getElementAt( i ) );
                    } catch ( NumberFormatException e ) {
                        continue;
                    }
                    if ( Integer.compare( a, b ) < 0 ) {
                        codeModel.insertElementAt( s, i );
                        continue ADD_LOOP;
                    }
                }
                codeModel.addElement( s );
            }
        }
    }

    private void doClickLogTableEvent () {
        int row = table.getSelectedRow();
        if ( row != - 1 && table.getModel() instanceof LogTableModel ) {
            row = table.convertRowIndexToModel( row );
            
            LogTableModel model = ( LogTableModel )table.getModel();
            List<ErrorLog> logs = model.getAllLogs();
            ErrorLog log = model.getLogByIndex( row );
            if ( log != null ) {
                subpanel = new SubPanelBinder( workspace, null, new LogSubPanel(this.connBean, this, logs, row ) );
                StartUI.getTopMain().push( subpanel );
            }
        }
    }
    
    protected void do_btnSearch_actionPerformed ( ActionEvent e ) {
        TypeOptions type = null;
        Integer code = null;
        if ( cboType.getSelectedItem() instanceof TypeOptions )
            type = ( TypeOptions )cboType.getSelectedItem();
        if ( cboCode.getSelectedItem() instanceof String ) {
            try {
                String str = ( ( String )cboCode.getSelectedItem() ).trim();
                if ( str.startsWith( "0x" ) )
                    code = Integer.parseInt( str.substring( 2 ), 16 );
                else
                    code = Integer.parseInt( str );
            } catch ( NumberFormatException | NullPointerException ex ) {
            }
        }

        LogFilterBean logFilterBean = new LogFilterBean( type, code, ( Date )fmtStartDate.getValue(), ( Date )fmtEndDate.getValue(),
                ( Date )fmtStartTime.getValue(), ( Date )fmtEndTime.getValue() );
        table.setFilter( new LogTableFilter( logFilterBean ) );
    }


    protected void do_btnClear_actionPerformed ( ActionEvent e ) {
        cboType.setSelectedItem( TypeOptions.All );
        cboCode.setSelectedItem( null );
        fmtStartDate.setValue( null );
        fmtEndDate.setValue( null );
        fmtStartTime.setValue( null );
        fmtEndTime.setValue( null );

        TypeOptions type = null;
        Integer code = null;
        if ( cboType.getSelectedItem() instanceof TypeOptions )
            type = ( TypeOptions )cboType.getSelectedItem();
        if ( cboCode.getSelectedItem() instanceof String ) {
            try {
                String str = ( ( String )cboCode.getSelectedItem() ).trim();
                if ( str.startsWith( "0x" ) )
                    code = Integer.parseInt( str.substring( 2 ), 16 );
                else
                    code = Integer.parseInt( str );
            } catch ( NumberFormatException | NullPointerException ex ) {
            }
        }

        LogFilterBean logFilterBean = new LogFilterBean( type, code, ( Date )fmtStartDate.getValue(), ( Date )fmtEndDate.getValue(),
                ( Date )fmtStartTime.getValue(), ( Date )fmtEndTime.getValue() );
        table.setFilter( new LogTableFilter( logFilterBean ) );
    }

    protected void do_btnDelete_actionPerformed ( ActionEvent e ) {
        if ( table.getSelectedRows().length > 0 ) {
            String confirmText = "";
            if ( table.getSelectedRows().length == 1 ) {
                int modelIndex = table.convertRowIndexToModel( table.getSelectedRows()[ 0 ] );
                confirmText = String.format( TEXT.getString( "Dialog.DeleteOne.format" ), modelIndex + 1 );
            } else if ( table.getSelectedRows().length > 1 ) {
                confirmText = String.format( TEXT.getString( "Dialog.DeleteMulti.format" ), table.getSelectedRows().length );
            }

            if ( JOptionPane.showConfirmDialog( this, confirmText, TEXT.getString( "Dialog.title" ), JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {
                final ArrayList<Integer> deleteLogID = new ArrayList<>();
                for ( int row : table.getSelectedRows() ) {
                    int modelIndex = table.convertRowIndexToModel( row );
                    int index = ( ( LogTableModel )table.getModel() ).getLogByIndex( modelIndex ).index;
                    deleteLogID.add( index );
                }
                new Thread() {
                    public void run () {
                        for ( int index : deleteLogID )
                            log.deleteLog( ( byte )index );
                    }
                }.start();
            }
        }
    }


    protected void do_btnTruncate_actionPerformed ( ActionEvent e ) {
        if ( JOptionPane.showConfirmDialog( this, TEXT.getString( "Dialog.DeleteAll.format" ), TEXT.getString( "Dialog.title" ),
                JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {
            log.clearLog();
            misc.mcs( ( short )0x2815, new byte[] { 0 } );	//#define CMD_CLEAR_LOG 0x2815
        }
    }
    
    protected void do_btnExportLog_actionPerformed ( ActionEvent e ) {
        if ( JOptionPane.showConfirmDialog( this, TEXT.getString( "Dialog.ExportLog.format" ), TEXT.getString( "Dialog.title" ),
                JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {
        	try {
        		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
        		String filePath = "log_"+ date.format(new Date( System.currentTimeMillis() ))+ ".txt";
				ExportLogByJTable( table, new File( filePath.trim() ) );
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }


    private void do_timer_actionPerformed ( ActionEvent e ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        SimpleDateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss" );
        final Date now = new Date();
        String dateToolTip = String.format( TEXT.getString( "ToolTip.Date.format" ), dateFormat.format( now ) );
        String timeToolTip = String.format( TEXT.getString( "ToolTip.Time.format" ), timeFormat.format( now ) );
        fmtStartDate.setToolTipText( dateToolTip );
        fmtEndDate.setToolTipText( dateToolTip );
        fmtStartTime.setToolTipText( timeToolTip );
        fmtEndTime.setToolTipText( timeToolTip );
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() == btnExportLog ) {
			do_btnExportLog_actionPerformed( e );
		}
		if ( e.getSource() == btnTruncate ) {
            do_btnTruncate_actionPerformed( e );
        }
        if ( e.getSource() == btnDelete ) {
            do_btnDelete_actionPerformed( e );
        }
        if ( e.getSource() == btnSearch ) {
            do_btnSearch_actionPerformed( e );
        }
        if ( e.getSource() == btnClear ) {
            do_btnClear_actionPerformed( e );
        }
        if ( e.getSource() == timer ) {
            do_timer_actionPerformed( e );
        }
	}
	
	public void ExportLogByJTable( JTable table, File file ) throws IOException {
		TableModel model = table.getModel();
		FileWriter out = new FileWriter(file);
		
		for(int i=0; i < model.getColumnCount(); i++) {
			if( i != 4 ) {
				out.write( model.getColumnName(i) + "\t");
			} 
		}
		
		out.write("\n");
		
		for(int i=0; i< model.getRowCount(); i++) {
			for(int j=0; j < model.getColumnCount(); j++) {
				if( j == 2 ) {
					int code = Integer.parseInt( model.getValueAt(i,j).toString() );
					String codeStr = "0x"+Integer.toHexString(code);
					out.write( codeStr + "\t");
				}else if( j != 4 ) {
					out.write( model.getValueAt(i,j).toString() + "\t");
				}
			}
			out.write("\n");
		}
		out.close();
	}
}
