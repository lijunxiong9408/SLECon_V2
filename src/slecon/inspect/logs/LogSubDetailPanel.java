package slecon.inspect.logs;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import logic.Dict;
import logic.evlog.ErrorLog;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.Workspace;
import slecon.home.PosButton;
import slecon.inspect.calls.FloorCallElement;
import slecon.interfaces.Page;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import comm.Parser_Log.Log;
import comm.constants.CallDef;




public class LogSubDetailPanel extends JPanel implements Page, ActionListener {
    private static final ResourceBundle bundle               = ToolBox.getResourceBundle( "inspect.Log" );
    private static final long           serialVersionUID     = - 3330780413254999916L;
    private ImageIcon					BUTTON_ICON			 = ImageFactory.BUTTON_PAUSE.icon(87,30);
    private final JLabel                lblLogID             = new JLabel( "LogID" );
    private final JLabel                lblLogdescription    = new JLabel( "LogDescription" );
    private final PosButton             btnDelete            = new PosButton( bundle.getString( "LogSubPanel.button.Delete" ), BUTTON_ICON );
    private final PosButton             btnSearch            = new PosButton( bundle.getString( "LogSubPanel.button.Search" ), BUTTON_ICON );
    private final PosButton             btnPrev              = new PosButton( bundle.getString( "LogSubPanel.button.Previous" ), BUTTON_ICON );
    private final PosButton             btnNext              = new PosButton( bundle.getString( "LogSubPanel.button.Next" ), BUTTON_ICON );
    private final JSeparator            separator            = new JSeparator();
    private final JLabel                lblBasicInformation  = new JLabel( bundle.getString( "LogSubPanel.title.BasicInformation" ) );
    private final JLabel                lblType              = new JLabel( bundle.getString( "LogSubPanel.title.Type" ) );
    private final JLabel                lblDate              = new JLabel( bundle.getString( "LogSubPanel.title.Date" ) );
    private final JLabel                lblTime              = new JLabel( bundle.getString( "LogSubPanel.title.Time" ) );
    private final JLabel                lblErrorCode         = new JLabel( bundle.getString( "LogSubPanel.title.ErrorCode" ) );
    private final JLabel                lblDescription       = new JLabel( bundle.getString( "LogSubPanel.title.Description" ) );
    private final JLabel                lblTypevalue         = new JLabel( "typeValue" );
    private final JLabel                lblDatevalue         = new JLabel( "dateValue" );
    private final JLabel                lblTimevalue         = new JLabel( "timeValue" );
    private final JLabel                lblErrorcodevalue    = new JLabel( "errorCodeValue" );
    private final JLabel                lblDescriptionvalue  = new JLabel( "descriptionValue" );
    private int                         index                = - 1;
    private final JSeparator            separator_1          = new JSeparator();
    private final Main                  parent;
    private final List<ErrorLog>        logs;
    private ErrorLog                    selectedLog;
    private Workspace                   workspace;
    private String                      title;
    private final JSeparator            separator_2          = new JSeparator();
    private final JLabel                lblExtraInformation  = new JLabel( bundle.getString( "LogSubPanel.title.ExtraInformation" ) );
    private final JLabel                lblCount             = new JLabel( bundle.getString( "LogSubPanel.title.Count" ) );
    private final JLabel                lblLastHappen        = new JLabel( bundle.getString( "LogSubPanel.title.LastHappen" ) );
    private final JLabel                lblCountvalue        = new JLabel( "countValue" );
    private final JLabel                lblLasthappenvalue   = new JLabel( "lastHappenValue" );

    private final JPanel                panel                = new JPanel();
    private final JLabel                lblExtraData         = new JLabel( bundle.getString( "LogSubPanel.title.ExtraData" ) );
    private final JLabel                lblPosition          = new JLabel( bundle.getString( "LogSubPanel.title.Position" ) );
    private final JLabel                lblSpeed             = new JLabel( bundle.getString( "LogSubPanel.title.Speed" ) );
    private final JLabel                lblCurrentFloor      = new JLabel( bundle.getString( "LogSubPanel.title.CurrentFloor" ) );
    private final JLabel                lblLastRunFloor      = new JLabel( bundle.getString( "LogSubPanel.title.LastRunFloor" ) );
    private final JLabel                lblOcsModule         = new JLabel( bundle.getString( "LogSubPanel.title.OCSModule" ) );
    private final JLabel                lblPositionvalue     = new JLabel( "positionValue" );
    private final JLabel                lblSpeedvalue        = new JLabel( "speedValue" );
    private final JLabel                lblCurrfloorvalue    = new JLabel( "currentfloorValue" );
    private final JLabel                lblLastrunfloorvalue = new JLabel( "lastrunfloorValue" );
    private final JLabel                lblOcsmodulevalue    = new JLabel( "ocsModuleValue" );

    public LogSubDetailPanel ( Main parent, List<ErrorLog> logs, int index ) {
        this.parent = parent;
        this.logs = logs;
        btnPrev.addActionListener( new BtnPrevActionListener() );
        initGUI();
        setLayout( new MigLayout("fill, w 940!, h 640!", "[200!][200!]200![320!,grow]", "[25px]5px[2px][36px][2px][17px][17px][17px][17px][17px][17px][2px][][][][][125px:n]") );
        btnDelete.addActionListener( this );
        btnSearch.addActionListener( this );
        btnNext.addActionListener( new BtnNextActionListener() );
        // first row.
        add( lblLogID, "cell 0 0,alignx left,aligny top, id logID" );
        add( btnNext, "id next, pos null logID.y  visual.x2 null" );
        add( btnPrev, "id prev, pos null logID.y next.x-5 null" );
        add( btnSearch, "id search, pos null logID.y prev.x-5 null" );
        add( btnDelete, "id delete, pos null logID.y search.x-5 null" );
        add( separator, "cell 0 1 4 1,growx,aligny top" );
        separator.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        
        // second row.
        add( lblLogdescription, "cell 0 2 2 1,alignx left,aligny center, id logDescription" );
        add( separator_1, "cell 0 3 2 1,growx,aligny top" );
        separator_1.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        
        // third row.
        add( lblTypevalue, "cell 1 5,alignx left,aligny top" );
        add( lblDatevalue, "cell 1 6,alignx left,aligny top" );
        add( lblTimevalue, "cell 1 7,alignx left,aligny top" );
        add( lblErrorcodevalue, "cell 1 8,alignx left,aligny top" );
        add( lblDescription, "cell 0 9,alignx left,aligny top" );
        add( lblErrorCode, "cell 0 8,alignx left,aligny top" );
        add( lblTime, "cell 0 7,alignx left,aligny top" );
        add( lblBasicInformation, "cell 0 4 2 1,alignx left,aligny top" );
        add( lblType, "cell 0 5,alignx left,aligny top" );
        add( lblDate, "cell 0 6,alignx left,aligny top" );
        add( lblDescriptionvalue, "cell 1 9,alignx left,aligny top" );
        add( separator_2, "cell 0 10 2 1,growx" );
        separator_2.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        
        // fourth row.
        add( lblExtraInformation, "cell 0 11 2 1" );
        add( lblCount, "cell 0 12" );
        add( lblCountvalue, "cell 1 12" );
        add( lblLastHappen, "cell 0 13" );
        add( lblLasthappenvalue, "cell 1 13" );
        add( lblExtraData, "cell 0 14" );
        panel.setOpaque( false );
        add( panel, "cell 0 15 2 1, grow, gapleft 30" );
        panel.setLayout( new MigLayout( "ins 0, gap 12", "[]", "[][][][][]" ) );
        panel.add( lblPosition, "flowx,cell 0 0" );
        panel.add( lblPositionvalue, "cell 0 0" );
        panel.add( lblSpeed, "flowx,cell 0 1" );
        panel.add( lblSpeedvalue, "cell 0 1" );
        panel.add( lblCurrentFloor, "flowx,cell 0 2" );
        panel.add( lblCurrfloorvalue, "cell 0 2" );
        panel.add( lblLastRunFloor, "flowx,cell 0 3" );
        panel.add( lblLastrunfloorvalue, "cell 0 3" );
        panel.add( lblOcsModule, "flowx,cell 0 4" );
        panel.add( lblOcsmodulevalue, "cell 0 4" );
        
        setComponentZOrder(lblLogdescription, 20);
        setIndex( index );
    }


    public final int getIndex () {
        return index;
    }


    public final void setIndex ( int index ) {
        if ( 0 <= index && index < logs.size() ) {
            if ( this.index != index ) {
                this.index = index;
                setSelectedLog( logs.get( index ) );
                parent.setSelectedIndex( index );
            }
        }
        btnPrev.setEnabled( index - 1 >= 0 );
        btnNext.setEnabled( index + 1 < logs.size() );
    }


    private void setSelectedLog ( ErrorLog errorLog ) {
        if ( this.selectedLog != errorLog ) {
            this.selectedLog = errorLog;
            updateSelectedLog();
        }
    }


    private void updateSelectedLog () {
        DateFormat dateFormat = DateFormat.getDateInstance( DateFormat.LONG, BaseFactory.getLocale() );
        DateFormat timeFormat = DateFormat.getTimeInstance( DateFormat.LONG, BaseFactory.getLocale() );
        
        final Log rawLog = selectedLog.rawLog;
        final String type = rawLog.type == 'O' ? "OCS" : rawLog.type == 'M' ? "MCS" : "XXX";
        String description = selectedLog.toAbbrString();
        
        lblLogID.setText( String.format( "Log #%d", logs.size() - selectedLog.index ) );
        lblLogdescription.setText( String.format( "[0x%04X] %s", rawLog.errcode, description ) );
        lblTypevalue.setText( type );
        lblDatevalue.setText( dateFormat.format( new Date( rawLog.timestamp ) ) );
        lblTimevalue.setText( timeFormat.format( new Date( rawLog.timestamp ) ) );
        lblErrorcodevalue.setText( String.format( "0x%04X", rawLog.errcode ) );
        lblDescriptionvalue.setText( selectedLog.toAbbrString() );
        
        int count = 0;
        long lastTimeLog = 0;
        for ( ErrorLog eLog : logs ) {
            if ( eLog.rawLog.errcode == rawLog.errcode ) {
                count += 1;
                if ( lastTimeLog < eLog.rawLog.timestamp && rawLog.timestamp > eLog.rawLog.timestamp )
                    lastTimeLog = eLog.rawLog.timestamp;
            }
        }
        lblCountvalue.setText( count + "" );
        lblLasthappenvalue.setText( lastTimeLog ==0 ? "-" : new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format( new Date( lastTimeLog) ) );

        float mmratio = this.parent.mcsconfig.getMMRatio();
        lblPositionvalue.setText( String.format( "%.2f mm", rawLog.position * mmratio ) );
        lblSpeedvalue.setText( String.format( "%.2f mm/s", rawLog.speed * mmratio ) );
        lblCurrfloorvalue.setText( this.parent.deploy.getFloorText( rawLog.current_floor ) );
        lblLastrunfloorvalue.setText( this.parent.deploy.getFloorText( rawLog.last_run_floor ) );
        lblOcsmodulevalue.setText( rawLog.ocs_module == null ? "-" : rawLog.ocs_module.toString() );
        
        setTitle( String.format( "Log %d", logs.size()-selectedLog.index ) );
    }


    private void initGUI () {
        setBackground(StartUI.SUB_BACKGROUND_COLOR);
        lblLogID.setFont( FontFactory.FONT_20_PLAIN );
        lblLogID.setForeground(Color.WHITE);
        lblLogdescription.setFont( FontFactory.FONT_16_BOLD );
        lblLogdescription.setForeground(Color.WHITE);
        lblBasicInformation.setFont( FontFactory.FONT_14_BOLD );
        lblBasicInformation.setForeground(Color.WHITE);
        lblType.setFont( FontFactory.FONT_14_PLAIN );
        lblType.setForeground(Color.WHITE);
        lblDate.setFont( FontFactory.FONT_14_PLAIN );
        lblDate.setForeground(Color.WHITE);
        lblTime.setFont( FontFactory.FONT_14_PLAIN );
        lblTime.setForeground(Color.WHITE);
        lblErrorCode.setFont( FontFactory.FONT_14_PLAIN );
        lblErrorCode.setForeground(Color.WHITE);
        lblDescription.setFont( FontFactory.FONT_14_PLAIN );
        lblDescription.setForeground(Color.WHITE);
        lblTypevalue.setFont( FontFactory.FONT_14_PLAIN );
        lblTypevalue.setForeground(Color.WHITE);
        lblDatevalue.setFont( FontFactory.FONT_14_PLAIN );
        lblDatevalue.setForeground(Color.WHITE);
        lblTimevalue.setFont( FontFactory.FONT_14_PLAIN );
        lblTimevalue.setForeground(Color.WHITE);
        lblErrorcodevalue.setFont( FontFactory.FONT_14_PLAIN );
        lblErrorcodevalue.setForeground(Color.WHITE);
        lblDescriptionvalue.setFont( FontFactory.FONT_14_PLAIN );
        lblDescriptionvalue.setForeground(Color.WHITE);
        lblExtraInformation.setFont( FontFactory.FONT_14_BOLD );
        lblExtraInformation.setForeground(Color.WHITE);
        lblCount.setFont( FontFactory.FONT_14_PLAIN );
        lblCount.setForeground(Color.WHITE);
        lblCountvalue.setFont( FontFactory.FONT_14_PLAIN );
        lblCountvalue.setForeground(Color.WHITE);
        lblLastHappen.setFont( FontFactory.FONT_14_PLAIN );
        lblLastHappen.setForeground(Color.WHITE);
        lblLasthappenvalue.setFont( FontFactory.FONT_14_PLAIN );
        lblLasthappenvalue.setForeground(Color.WHITE);
        lblExtraData.setFont( FontFactory.FONT_14_PLAIN );
        lblExtraData.setForeground(Color.WHITE);
        lblPosition.setFont( FontFactory.FONT_12_PLAIN );
        lblPosition.setForeground(Color.WHITE);
        lblSpeed.setFont( FontFactory.FONT_12_PLAIN );
        lblSpeed.setForeground(Color.WHITE);
        lblCurrentFloor.setFont( FontFactory.FONT_12_PLAIN );
        lblCurrentFloor.setForeground(Color.WHITE);
        lblLastRunFloor.setFont( FontFactory.FONT_12_PLAIN );
        lblLastRunFloor.setForeground(Color.WHITE);
        lblOcsModule.setFont( FontFactory.FONT_12_PLAIN );
        lblOcsModule.setForeground(Color.WHITE);
        lblPositionvalue.setFont( FontFactory.FONT_12_PLAIN );
        lblPositionvalue.setForeground(Color.WHITE);
        lblSpeedvalue.setFont( FontFactory.FONT_12_PLAIN );
        lblSpeedvalue.setForeground(Color.WHITE);
        lblCurrfloorvalue.setFont( FontFactory.FONT_12_PLAIN );
        lblCurrfloorvalue.setForeground(Color.WHITE);
        lblLastrunfloorvalue.setFont( FontFactory.FONT_12_PLAIN );
        lblLastrunfloorvalue.setForeground(Color.WHITE);
        lblOcsmodulevalue.setFont( FontFactory.FONT_12_PLAIN );
        lblOcsmodulevalue.setForeground(Color.WHITE);
    }


    @Override
    public void onCreate ( Workspace workspace ) throws Exception {
        this.workspace = workspace;
    }


    @Override
    public void onStart () throws Exception {
    }


    @Override
    public void onResume () throws Exception {
        workspace.setTitle( getTitle() );
    }


    @Override
    public void onPause () throws Exception {
        // TODO Auto-generated method stub
    }


    @Override
    public void onStop () throws Exception {
    }


    @Override
    public void onDestroy () {
    }


    public String getTitle () {
        return title;
    }


    public void setTitle ( String title ) {
        this.title = title;
        if ( workspace != null )
            workspace.setTitle( title );
    }


    private class BtnNextActionListener implements ActionListener {
        public void actionPerformed ( final ActionEvent e ) {
            setIndex( index + 1 );
        }
    }




    private class BtnPrevActionListener implements ActionListener {
        public void actionPerformed ( final ActionEvent e ) {
            setIndex( index - 1 );
        }
    }
    
    
    
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == btnSearch) {
            do_btnSearch_actionPerformed(e);
        }
        if (e.getSource() == btnDelete) {
            do_btnDelete_actionPerformed(e);
        }
    }
    
    
    protected void do_btnDelete_actionPerformed(final ActionEvent e) {
        String confirmText = String.format(bundle.getString("Dialog.DeleteOne.format"), logs.size()-selectedLog.index);

        if (JOptionPane.showConfirmDialog(this, confirmText, bundle.getString("Dialog.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            parent.deleteLog(logs.get( index ).index);
        }
    }
    
    
    protected void do_btnSearch_actionPerformed(final ActionEvent e) {
    }
}
