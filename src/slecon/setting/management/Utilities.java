package slecon.setting.management;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.component.ValueTextField;
import slecon.home.PosButton;
import slecon.interfaces.ConvertException;
import base.cfg.BaseFactory;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;

public class Utilities extends JPanel implements ActionListener {

    private static final long serialVersionUID = -2615861042431035396L;

    /**
     * Text resource.
     */
    private static final ResourceBundle TEXT = ToolBox.getResourceBundle( "setting.management.Utilities" );

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    private boolean                 started = false;
    private SettingPanel<Utilities> settingPanel;
    private JLabel                  cpt_system_clock;
    private JLabel                  lbl_label_current_system_clock;
    private JLabel                  lbl_value_current_system_clock;
    private JLabel                  lbl_set_system_clock;
    private JLabel                  lbl_year;
    private JLabel                  lbl_month;
    private JLabel                  lbl_day;
    private JLabel                  lbl_hour;
    private JLabel                  lbl_minute;
    private JLabel                  lbl_second;
    private ValueTextField          fmt_year;
    private ValueTextField          fmt_month;
    private ValueTextField          fmt_day;
    private ValueTextField          fmt_hour;
    private ValueTextField          fmt_minute;
    private ValueTextField          fmt_second;
    private Timer                   timer;
    private PosButton               btn_useLocalTime;
    private DefaultFormatterFactory dateFormatFactory;

    private JLabel                  cpt_local_network;
    private JLabel                  lbl_local_ip;
    private JTextField          	txt_local_ip;
    private JLabel                  lbl_local_mask;
    private JTextField          	txt_local_mask;
    private JLabel                  lbl_local_gateway;
    private JTextField          	txt_local_gateway;

    public Utilities () {
        try {
        initGUI();
        } catch(Exception e) {
            e.printStackTrace();
        }
        timer = new Timer(1000, this);
        timer.setInitialDelay(0);
    }
    
    public void SetWeightEnable(boolean enable) {
    	cpt_system_clock.setEnabled(enable);
    	lbl_label_current_system_clock.setEnabled(enable);
    	lbl_value_current_system_clock.setEnabled(enable);
    	lbl_set_system_clock.setEnabled(enable);
    	lbl_year.setEnabled(enable);
    	lbl_month.setEnabled(enable);
    	lbl_day.setEnabled(enable);
    	lbl_hour.setEnabled(enable);
    	lbl_minute.setEnabled(enable);
    	lbl_second.setEnabled(enable);
    	fmt_year.setEnabled(enable);
    	fmt_month.setEnabled(enable);
    	fmt_day.setEnabled(enable);
    	fmt_hour.setEnabled(enable);
    	fmt_minute.setEnabled(enable);
    	fmt_second.setEnabled(enable);
    	btn_useLocalTime.setEnabled(enable);
    	cpt_local_network.setEnabled(enable);
    	lbl_local_ip.setEnabled(enable);
    	txt_local_ip.setEnabled(enable);
    	lbl_local_mask.setEnabled(enable);
    	txt_local_mask.setEnabled(enable);
    	lbl_local_gateway.setEnabled(enable);
    	txt_local_gateway.setEnabled(enable);
    }

    public void setSettingPanel ( SettingPanel<Utilities> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[40::40][20::20][32::32][]" ) );
        cpt_system_clock               = new JLabel();
        lbl_label_current_system_clock = new JLabel();
        lbl_value_current_system_clock = new JLabel();
        lbl_set_system_clock           = new JLabel();
        lbl_year                       = new JLabel();
        lbl_month                      = new JLabel();
        lbl_day                        = new JLabel();
        lbl_hour                       = new JLabel();
        lbl_minute                     = new JLabel();
        lbl_second                     = new JLabel();
        fmt_year                       = new ValueTextField();
        fmt_month                      = new ValueTextField();
        fmt_day                        = new ValueTextField();
        fmt_hour                       = new ValueTextField();
        fmt_minute                     = new ValueTextField();
        fmt_second                     = new ValueTextField();
        if(BaseFactory.getLocaleString().equals("en"))
        	btn_useLocalTime               = new PosButton(ImageFactory.BUTTON_PAUSE.icon(180, 30),
        												   ImageFactory.BUTTON_START.icon(180, 30));
        else
        	btn_useLocalTime               = new PosButton(ImageFactory.BUTTON_PAUSE.icon(150, 30),
        												   ImageFactory.BUTTON_START.icon(150, 30));
        
        setCaptionStyle( cpt_system_clock );
        setTextLabelStyle(lbl_year);
        setTextLabelStyle(lbl_month);
        setTextLabelStyle(lbl_day);
        setTextLabelStyle(lbl_hour);
        setTextLabelStyle(lbl_minute);
        setTextLabelStyle(lbl_second);
        
        fmt_year.setScope(Long.class, 1970L, 2100L);
        fmt_month.setScope(Long.class, 1L, 12L);
        fmt_day.setScope(Long.class, 1L, 31L);
        fmt_hour.setScope(Long.class, 0L, 23L);
        fmt_minute.setScope(Long.class, 0L, 59L);
        fmt_second.setScope(Long.class, 0L, 59L);
        fmt_year.setColumns(4);
        fmt_month.setColumns(2);
        fmt_day.setColumns(2);
        fmt_hour.setColumns(2);
        fmt_minute.setColumns(2);
        fmt_second.setColumns(2);
        fmt_year.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_month.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_day.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_hour.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_minute.setHorizontalAlignment(SwingConstants.RIGHT);
        fmt_second.setHorizontalAlignment(SwingConstants.RIGHT);
        
        DecimalFormat   df = new DecimalFormat( "####" );
        NumberFormatter nf = new NumberFormatter( df );
        dateFormatFactory = new DefaultFormatterFactory( nf );
        
        fmt_year.setFormatterFactory( dateFormatFactory );
        fmt_month.setFormatterFactory( dateFormatFactory );
        fmt_day.setFormatterFactory( dateFormatFactory );
        fmt_hour.setFormatterFactory( dateFormatFactory );
        fmt_minute.setFormatterFactory( dateFormatFactory );
        fmt_second.setFormatterFactory( dateFormatFactory );
        

        PropertyChangeListener dayMaximumListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if(fmt_year.getValue() instanceof Number && fmt_month.getValue() instanceof Number) {
                    Calendar cal = new GregorianCalendar(((Number)fmt_year.getValue()).intValue(), ((Number)fmt_month.getValue()).intValue() - 1, 1);
                    long daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    fmt_day.setScope(Long.class, 1L, daysInMonth);
                    fmt_day.setFormatterFactory(dateFormatFactory);
                }
            }
        };
        fmt_year.addPropertyChangeListener("value", dayMaximumListener);
        fmt_month.addPropertyChangeListener("value", dayMaximumListener);
        btn_useLocalTime.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Date now = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                
                fmt_year.setValue(year);
                fmt_month.setValue(month);
                fmt_day.setValue(day);
                fmt_hour.setValue(hour);
                fmt_minute.setValue(minute);
                fmt_second.setValue(second);
            }
        });

        // @CompoentSetting( lbl_label_current_system_clock, lbl_value_current_system_clock )
        setLabelTitleStyle( lbl_label_current_system_clock );
        setLabelValueStyle( lbl_value_current_system_clock );

        // @CompoentSetting<Txt>( lbl_set_system_clock , txt_set_system_clock )
        setTextLabelStyle( lbl_set_system_clock );
        fmt_year.setHorizontalAlignment( SwingConstants.RIGHT );
        add( cpt_system_clock, "gapbottom 18-12, span, aligny center" );
        add( lbl_label_current_system_clock, "skip 2, span, split, gapright 12" );
        add( lbl_value_current_system_clock, "wrap" );
        add( lbl_set_system_clock, "skip 2, span, split, gapright 12" );
        add( fmt_year, "right" );
        add( lbl_year, "right" );
        add( fmt_month, "right" );
        add( lbl_month, "right" );
        add( fmt_day, "right" );
        add( lbl_day, "right" );
        add( fmt_hour, "right" );
        add( lbl_hour, "right" );
        add( fmt_minute, "right" );
        add( lbl_minute, "right" );
        add( fmt_second, "right" );
        add( lbl_second, "right" );
        add( btn_useLocalTime, "right, wrap 30-12" );

        /* ---------------------------------------------------------------------------- */
        
        cpt_local_network = new JLabel();
        lbl_local_ip = new JLabel();
        txt_local_ip = new JTextField();
        lbl_local_mask = new JLabel();
        txt_local_mask = new JTextField();
        lbl_local_gateway = new JLabel();
        txt_local_gateway = new JTextField();
        
        setCaptionStyle( cpt_local_network );
        setTextLabelStyle(lbl_local_ip);
        setTextLabelStyle(lbl_local_mask);
        setTextLabelStyle(lbl_local_gateway);
        setTextFieldStyle(txt_local_ip);
        setTextFieldStyle(txt_local_mask);
        setTextFieldStyle(txt_local_gateway);
        
        Box vbox_title = Box.createVerticalBox();
        vbox_title.add( lbl_local_ip);
        vbox_title.add( Box.createVerticalStrut(17));
        vbox_title.add( lbl_local_mask);
        vbox_title.add( Box.createVerticalStrut(17));
        vbox_title.add( lbl_local_gateway);
        
        Box vbox_value = Box.createVerticalBox();
        vbox_value.add( txt_local_ip );
        vbox_value.add( Box.createVerticalStrut(15));
        vbox_value.add( txt_local_mask );
        vbox_value.add( Box.createVerticalStrut(15));
        vbox_value.add( txt_local_gateway );
        
        add( cpt_local_network, "gapbottom 18-12, span, aligny center" );
        add( vbox_title, "skip 2, span, split, gapright 12" );
        add( vbox_value, "wrap 30-12" );
        
        /* ---------------------------------------------------------------------------- */
        bindGroup( "CurrentSystemClock", lbl_label_current_system_clock, lbl_value_current_system_clock );
        bindGroup( "SetSystemClock", lbl_set_system_clock, fmt_year );
        SetWeightEnable(false);
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_system_clock.setText( getBundleText( "LBL_cpt_system_clock", "System clock" ) );
        lbl_label_current_system_clock.setText( getBundleText( "LBL_lbl_label_current_system_clock", "Current System clock" ) );
        lbl_set_system_clock.setText( getBundleText( "LBL_lbl_set_system_clock", "Set System clock" ) );
        lbl_year.setText( getBundleText( "LBL_lbl_year", "Year" ) );
        lbl_month.setText( getBundleText( "LBL_lbl_month", "Month" ) );
        lbl_day.setText( getBundleText( "LBL_lbl_day", "Day" ) );
        lbl_hour.setText( getBundleText( "LBL_lbl_hour", "hour" ) );
        lbl_minute.setText( getBundleText( "LBL_lbl_minute", "min" ) );
        lbl_second.setText( getBundleText( "LBL_lbl_second", "sec" ) );
        btn_useLocalTime.setText( getBundleText( "LBL_btn_useLocalTime", "use local time"));
        /* ---------------------------------------------------------------------------- */
        cpt_local_network.setText( getBundleText( "LBL_cpt_local_network", "Local Network" ) );
        lbl_local_ip.setText( getBundleText( "LBL_lbl_local_ip", "IP" ) );
        lbl_local_mask.setText( getBundleText( "LBL_lbl_local_mask", "Netmask" ) );
        lbl_local_gateway.setText( getBundleText( "LBL_lbl_local_gateway", "Gateway" ) );
        /* ---------------------------------------------------------------------------- */
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setTextLabelStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setLabelTitleStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setLabelValueStyle ( JLabel c ) {
        c.setOpaque( true );
        c.setBackground( StartUI.BORDER_COLOR );
        c.setFont( FontFactory.FONT_12_BOLD );
        c.setForeground(Color.WHITE);
    }
    
    private void setTextFieldStyle ( JTextField c ) {
    	c.setText( "N/A" );
        c.setColumns( 16 );
        c.setCaretColor(Color.WHITE);
        c.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        c.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        c.setForeground(Color.WHITE);
    }


    private void bindGroup ( final String detailKey, final JComponent... list ) {
        if ( detailKey != null && detailKey.trim().length() > 0 ) {
            for ( JComponent c : list ) {
                c.addMouseListener( new MouseAdapter() {
                    String detailText;
                    @Override
                    public synchronized void mouseEntered ( MouseEvent evt ) {
                        if ( settingPanel != null ) {
                            if ( detailText == null ) {
                                try {
                                    detailText = TEXT.getString( "Description_" + detailKey );
                                } catch ( Exception e ) {
                                    detailText = "No description here! Be careful.";
                                }
                            }
                            settingPanel.setDescription( detailText );
                        }
                    }
                    @Override
                    public void mouseExited ( MouseEvent e ) {
                        if ( settingPanel != null ) {
                            settingPanel.setDescription( null );
                        }
                    }
                } );
            }
        }
    }


    public SystemClockBean getSystemClockBean () throws ConvertException {
        SystemClockBean bean_systemClock = new SystemClockBean();

        if ( ! fmt_year.checkValue() )
            throw new ConvertException();
        if ( ! fmt_month.checkValue() )
            throw new ConvertException();
        if ( ! fmt_day.checkValue() )
            throw new ConvertException();
        if ( ! fmt_hour.checkValue() )
            throw new ConvertException();
        if ( ! fmt_minute.checkValue() )
            throw new ConvertException();
        if ( ! fmt_second.checkValue() )
            throw new ConvertException();
        
        try {
            Calendar calendar = Calendar.getInstance();

			 calendar.set(Calendar.YEAR, ((Number)fmt_year.getValue()).intValue());
			 calendar.set(Calendar.MONTH, ((Number)fmt_month.getValue()).intValue() - 1);
			 calendar.set(Calendar.DAY_OF_MONTH, ((Number)fmt_day.getValue()).intValue());
			 calendar.set(Calendar.HOUR_OF_DAY, ((Number)fmt_hour.getValue()).intValue());
			 calendar.set(Calendar.MINUTE, ((Number)fmt_minute.getValue()).intValue());
			 calendar.set(Calendar.SECOND, ((Number)fmt_second.getValue()).intValue());
            
            bean_systemClock.setSetSystemClock( calendar.getTime() );
        } catch (Exception e) {
            throw new ConvertException("time");
        }
        return bean_systemClock;
    }


    public void setSystemClockBean ( SystemClockBean bean_systemClock ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bean_systemClock.getSetSystemClock());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        
        this.fmt_year.setOriginValue(year);
        this.fmt_month.setOriginValue(month);
        this.fmt_day.setOriginValue(day);
        this.fmt_hour.setOriginValue(hour);
        this.fmt_minute.setOriginValue(minute);
        this.fmt_second.setOriginValue(second);
    }


    public String getBundleText ( String key, String defaultValue ) {
        String result;
        try {
            result = TEXT.getString( key );
        } catch ( Exception e ) {
            result = defaultValue;
        }
        return result;
    }


    public void start () {
        started = true;
        timer.start();
    }


    public void stop () {
        timer.stop();
        started = false;
    }


    public static Utilities createPanel ( SettingPanel<Utilities> panel ) {
        Utilities gui = new Utilities();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static class SystemClockBean {
        private Date setSystemClock;




        public Date getSetSystemClock () {
            return this.setSystemClock;
        }


        public void setSetSystemClock ( Date setSystemClock ) {
            this.setSystemClock = setSystemClock;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==timer) {
            lbl_value_current_system_clock.setText(sdf.format(new Date()));
        }
    }
    
    public void setNetWorkBean( NetworkBean bean ) {
    	this.txt_local_ip.setText( (new String(bean.getIp())).trim() );
    	this.txt_local_mask.setText( (new String(bean.getMask())).trim() );
    	this.txt_local_gateway.setText( (new String(bean.getGateway())).trim() );
    }
    
    public NetworkBean getNetworkBean() {
    	NetworkBean bean = new NetworkBean();
    	bean.setIp( this.txt_local_ip.getText().getBytes() );
    	bean.setMask( this.txt_local_mask.getText().getBytes() );
    	bean.setGateway( this.txt_local_gateway.getText().getBytes() );
    	return bean;
    }
    
    public static class NetworkBean{
    	private byte [] ip;
    	private byte [] mask;
    	private byte [] gateway;
    	
		public byte[] getIp() {
			return ip;
		}
		public void setIp(byte[] ip) {
			this.ip = ip;
		}
		public byte[] getMask() {
			return mask;
		}
		public void setMask(byte[] mask) {
			this.mask = mask;
		}
		public byte[] getGateway() {
			return gateway;
		}
		public void setGateway(byte[] gateway) {
			this.gateway = gateway;
		}
    }
}
