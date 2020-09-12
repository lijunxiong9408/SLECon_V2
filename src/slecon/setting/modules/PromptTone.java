package slecon.setting.modules;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.component.ValueCheckBox;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;

public class PromptTone extends JPanel{
    private static final long serialVersionUID = 4412644778595410536L;
    /**
     * Text resource.
     */
    public static final ResourceBundle TEXT    = ToolBox.getResourceBundle( "setting.module.PromptTone" );
    private boolean                     started = false;
    private SettingPanel<PromptTone>     settingPanel;

    /*----------------------------------------------------------------------------*/
    private JLabel 			cpt_Voice_Enable;
    private ValueCheckBox 	ebd_Voice_Door_Open_Enable;
    private ValueCheckBox 	ebd_Voice_Door_Close_Enable;
    private ValueCheckBox 	ebd_Voice_Direc_Enable;
    private ValueCheckBox 	ebd_Voice_Message_Enable;
    private ValueCheckBox 	ebd_Voice_Floor_Enable;
    private ValueCheckBox 	ebd_Voice_Button_Enable;
    
    /*----------------------------------------------------------------------------*/
    private JLabel 			cpt_Run_Direct_Voice;
    private JLabel 			lbl_Hall_Up;
    private MyComboBox 		cbo_Hall_Up_Type;
    private JLabel 			lbl_Hall_Down;
    private MyComboBox		cbo_Hall_Down_Type;
    private JLabel 			lbl_Car_Up;
    private MyComboBox 		cbo_Car_Up_Type;
    private JLabel 			lbl_Car_Down;
    private MyComboBox		cbo_Car_Down_Type;
    
    /*----------------------------------------------------------------------------*/
    private JLabel cpt_Language_Level;
    private JLabel cpt_CAR;
    private JLabel lbl_Car_Level_A;
    private MyComboBox cbo_Car_Level_A;
    private JLabel lbl_Car_Level_B;
    private MyComboBox cbo_Car_Level_B;
    private JLabel lbl_Car_Level_C;
    private MyComboBox cbo_Car_Level_C;
    private JLabel lbl_Car_Level_D;
    private MyComboBox cbo_Car_Level_D;
    
    private JLabel cpt_HALL;
    private JLabel lbl_Hall_Level_A;
    private MyComboBox cbo_Hall_Level_A;
    private JLabel lbl_Hall_Level_B;
    private MyComboBox cbo_Hall_Level_B;
    private JLabel lbl_Hall_Level_C;
    private MyComboBox cbo_Hall_Level_C;
    private JLabel lbl_Hall_Level_D;
    private MyComboBox cbo_Hall_Level_D;
    
    
    public PromptTone () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<PromptTone> panel ) {
        this.settingPanel = panel;
    }


    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "fillx, ins 25, gap 0 12", "[30::30][150::150][150::150][]" ) );

        /*----------------------------------------------------------------------------*/
        cpt_Voice_Enable = new JLabel();
        ebd_Voice_Door_Open_Enable = new ValueCheckBox();
        ebd_Voice_Door_Close_Enable = new ValueCheckBox();
        ebd_Voice_Direc_Enable = new ValueCheckBox();
        ebd_Voice_Message_Enable = new ValueCheckBox();
        ebd_Voice_Floor_Enable = new ValueCheckBox();
        ebd_Voice_Button_Enable = new ValueCheckBox();
        setCaptionStyle( cpt_Voice_Enable );
        
        add(cpt_Voice_Enable,"gapbottom 18-12, span, top");
        add(ebd_Voice_Door_Open_Enable,"skip 1, span");
        add(ebd_Voice_Door_Close_Enable,"skip 1, span");
        add(ebd_Voice_Direc_Enable,"skip 1, span");
        add(ebd_Voice_Message_Enable,"skip 1, span");
        add(ebd_Voice_Floor_Enable,"skip 1, span");
        add(ebd_Voice_Button_Enable,"skip 1, wrap 30, span");
        
        /*----------------------------------------------------------------------------*/
        cpt_Run_Direct_Voice = new JLabel(); 
        lbl_Hall_Up = new JLabel();
        cbo_Hall_Up_Type = new MyComboBox();
        lbl_Hall_Down = new JLabel();
        cbo_Hall_Down_Type = new MyComboBox();
        lbl_Car_Up = new JLabel();
        cbo_Car_Up_Type = new MyComboBox();
        lbl_Car_Down = new JLabel();
        cbo_Car_Down_Type = new MyComboBox();
        
        setCaptionStyle( cpt_Run_Direct_Voice );
        setLabelStyle(lbl_Hall_Up);
        setLabelStyle(lbl_Hall_Down);
        setLabelStyle(lbl_Car_Up);
        setLabelStyle(lbl_Car_Down);
        
        cbo_Hall_Up_Type.setPreferredSize(new Dimension(100, 25));
        cbo_Hall_Down_Type.setPreferredSize(new Dimension(100, 25));
        cbo_Car_Up_Type.setPreferredSize(new Dimension(100, 25));
        cbo_Car_Down_Type.setPreferredSize(new Dimension(100, 25));
        
        cbo_Hall_Up_Type.setModel(new DefaultComboBoxModel<VoiceType>(VoiceType.values()));
        setComboBoxValueStyle(cbo_Hall_Up_Type);
        if(cbo_Hall_Up_Type.getItemCount()>0) cbo_Hall_Up_Type.setSelectedIndex(0);
        
        cbo_Hall_Down_Type.setModel(new DefaultComboBoxModel<VoiceType>(VoiceType.values()));
        setComboBoxValueStyle(cbo_Hall_Down_Type);
        if(cbo_Hall_Down_Type.getItemCount()>0) cbo_Hall_Down_Type.setSelectedIndex(0);
        
        cbo_Car_Up_Type.setModel(new DefaultComboBoxModel<VoiceType>(VoiceType.values()));
        setComboBoxValueStyle(cbo_Car_Up_Type);
        if(cbo_Car_Up_Type.getItemCount()>0) cbo_Car_Up_Type.setSelectedIndex(0);
        
        cbo_Car_Down_Type.setModel(new DefaultComboBoxModel<VoiceType>(VoiceType.values()));
        setComboBoxValueStyle(cbo_Car_Down_Type);
        if(cbo_Car_Down_Type.getItemCount()>0) cbo_Car_Down_Type.setSelectedIndex(0);
        
        
        add(cpt_Run_Direct_Voice,"gapbottom 18-12, span, top");
        add(lbl_Hall_Up,"skip 1, span 1, left, top");
        add(cbo_Hall_Up_Type,"span 1, left, wrap, top");
        add(lbl_Hall_Down,"skip 1, span 1, left, top");
        add(cbo_Hall_Down_Type,"span 1, left, wrap, top");
        add(lbl_Car_Up,"skip 1, span 1, left, top");
        add(cbo_Car_Up_Type,"span 1, left, wrap, top");
        add(lbl_Car_Down,"skip 1, span 1, left, top");
        add(cbo_Car_Down_Type,"span 1, left, wrap 30, top");
        
        /*----------------------------------------------------------------------------*/
        cpt_Language_Level = new JLabel();
        cpt_CAR = new JLabel();
        lbl_Car_Level_A = new JLabel();
        cbo_Car_Level_A = new MyComboBox();
        lbl_Car_Level_B = new JLabel();
        cbo_Car_Level_B = new MyComboBox();
        lbl_Car_Level_C = new JLabel();
        cbo_Car_Level_C = new MyComboBox();
        lbl_Car_Level_D = new JLabel();
        cbo_Car_Level_D = new MyComboBox();
        
        cpt_HALL = new JLabel();
        lbl_Hall_Level_A = new JLabel();
        cbo_Hall_Level_A = new MyComboBox();
        lbl_Hall_Level_B = new JLabel();
        cbo_Hall_Level_B = new MyComboBox();
        lbl_Hall_Level_C = new JLabel();
        cbo_Hall_Level_C = new MyComboBox();
        lbl_Hall_Level_D = new JLabel();
        cbo_Hall_Level_D = new MyComboBox();
        
        setCaptionStyle(cpt_Language_Level);
        setCaption2Style(cpt_CAR);
        setLabelStyle(lbl_Car_Level_A);
        setLabelStyle(lbl_Car_Level_B);
        setLabelStyle(lbl_Car_Level_C);
        setLabelStyle(lbl_Car_Level_D);
        setComboBoxValueStyle(cbo_Car_Level_A);
        setComboBoxValueStyle(cbo_Car_Level_B);
        setComboBoxValueStyle(cbo_Car_Level_C);
        setComboBoxValueStyle(cbo_Car_Level_D);
        
        setCaption2Style(cpt_HALL);
        setLabelStyle(lbl_Hall_Level_A);
        setLabelStyle(lbl_Hall_Level_B);
        setLabelStyle(lbl_Hall_Level_C);
        setLabelStyle(lbl_Hall_Level_D);
        setComboBoxValueStyle(cbo_Hall_Level_A);
        setComboBoxValueStyle(cbo_Hall_Level_B);
        setComboBoxValueStyle(cbo_Hall_Level_C);
        setComboBoxValueStyle(cbo_Hall_Level_D);
               
        cbo_Car_Level_A.setModel(new DefaultComboBoxModel<Language>(Language.values()));
        cbo_Car_Level_B.setModel(new DefaultComboBoxModel<Language>(Language.values()));
        cbo_Car_Level_C.setModel(new DefaultComboBoxModel<Language>(Language.values()));
        cbo_Car_Level_D.setModel(new DefaultComboBoxModel<Language>(Language.values()));
        
        cbo_Hall_Level_A.setModel(new DefaultComboBoxModel<Language>(Language.values()));
        cbo_Hall_Level_B.setModel(new DefaultComboBoxModel<Language>(Language.values()));
        cbo_Hall_Level_C.setModel(new DefaultComboBoxModel<Language>(Language.values()));
        cbo_Hall_Level_D.setModel(new DefaultComboBoxModel<Language>(Language.values()));
        
        add(cpt_Language_Level,"gapbottom 18-12, span, top");
        add(cpt_CAR, "skip 1, span, left, top, wrap");
        add(lbl_Car_Level_A,"skip 1, span 1, left, top");
        add(cbo_Car_Level_A,"span 1, left, top, wrap");
        add(lbl_Car_Level_B,"skip 1, span 1, left, top");
        add(cbo_Car_Level_B,"span 1, left, top, wrap");
        add(lbl_Car_Level_C,"skip 1, span 1, left, top");
        add(cbo_Car_Level_C,"span 1, left, top, wrap");
        add(lbl_Car_Level_D,"skip 1, span 1, left, top");
        add(cbo_Car_Level_D,"span 1, left, top, wrap 30");
        
        add(cpt_HALL, "skip 1, span, left, top, wrap");
        add(lbl_Hall_Level_A,"skip 1, span 1, left, top");
        add(cbo_Hall_Level_A,"span 1, left, top, wrap");
        add(lbl_Hall_Level_B,"skip 1, span 1, left, top");
        add(cbo_Hall_Level_B,"span 1, left, top, wrap");
        add(lbl_Hall_Level_C,"skip 1, span 1, left, top");
        add(cbo_Hall_Level_C,"span 1, left, top, wrap");
        add(lbl_Hall_Level_D,"skip 1, span 1, left, top");
        add(cbo_Hall_Level_D,"span 1, left, top, wrap 30");
		        
        loadI18N();
        revalidate();
    }

    private void loadI18N () {
    	
        /*----------------------------------------------------------------------------*/
        cpt_Voice_Enable.setText(TEXT.getString("LBL_cpt_Voice_Enable"));
        ebd_Voice_Door_Open_Enable.setText(TEXT.getString("LBL_ebd_Voice_Door_Open_Enable"));
        ebd_Voice_Door_Close_Enable.setText(TEXT.getString("LBL_ebd_Voice_Door_Close_Enable"));
        ebd_Voice_Direc_Enable.setText(TEXT.getString("LBL_ebd_Voice_Direc_Enable"));
        ebd_Voice_Message_Enable.setText(TEXT.getString("LBL_ebd_Voice_Message_Enable"));
        ebd_Voice_Floor_Enable.setText(TEXT.getString("LBL_ebd_Voice_Floor_Enable"));
        ebd_Voice_Button_Enable.setText(TEXT.getString("LBL_ebd_Voice_Button_Enable"));
        
        /*----------------------------------------------------------------------------*/
        cpt_Run_Direct_Voice.setText(TEXT.getString("LBL_cpt_Hall_Voice"));
        lbl_Hall_Up.setText(TEXT.getString("LBL_lbl_Hall_Up"));
        lbl_Hall_Down.setText(TEXT.getString("LBL_lbl_Hall_Down"));
        lbl_Car_Up.setText(TEXT.getString("LBL_lbl_Car_Up"));
        lbl_Car_Down.setText(TEXT.getString("LBL_lbl_Car_Down"));
        
        /*----------------------------------------------------------------------------*/
        cpt_Language_Level.setText(TEXT.getString("LBL_Language_Level"));
        cpt_CAR.setText(TEXT.getString("LBL_CAR"));
        lbl_Car_Level_A.setText(TEXT.getString("LBL_LEVEL_A"));
        lbl_Car_Level_B.setText(TEXT.getString("LBL_LEVEL_B"));
        lbl_Car_Level_C.setText(TEXT.getString("LBL_LEVEL_C"));
        lbl_Car_Level_D.setText(TEXT.getString("LBL_LEVEL_D"));
        cpt_HALL.setText(TEXT.getString("LBL_HALL"));
        lbl_Hall_Level_A.setText(TEXT.getString("LBL_LEVEL_A"));
        lbl_Hall_Level_B.setText(TEXT.getString("LBL_LEVEL_B"));
        lbl_Hall_Level_C.setText(TEXT.getString("LBL_LEVEL_C"));
        lbl_Hall_Level_D.setText(TEXT.getString("LBL_LEVEL_D"));
        
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }
    
    private void setCaption2Style ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_BOLD );
        c.setForeground(Color.WHITE);
    }
    
    private void setLabelStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }
    
    private void setComboBoxValueStyle ( JComboBox<?> c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }

    private void bindGroup ( final AbstractButton[] primary, final JComponent... list ) {
        for ( AbstractButton p : primary ) {
            p.addItemListener( new ItemListener() {
                public void itemStateChanged ( ItemEvent e ) {
                    boolean enabled = false;
                    for ( AbstractButton p : primary ) {
                        if ( p.isSelected() && p.isEnabled() ) {
                            enabled = true;
                            break;
                        }
                    }
                    for ( JComponent c : list )
                        c.setEnabled( enabled );
                }
            } );
            p.addPropertyChangeListener( "enabled", new PropertyChangeListener() {
                @Override
                public void propertyChange ( PropertyChangeEvent evt ) {
                    boolean enabled = false;
                    for ( AbstractButton p : primary ) {
                        if ( p.isSelected() && p.isEnabled() ) {
                            enabled = true;
                            break;
                        }
                    }
                    for ( JComponent c : list )
                        c.setEnabled( enabled );
                }
            } );
        }

        boolean enabled = false;
        for ( AbstractButton p : primary ) {
            if ( p.isSelected() && p.isEnabled() ) {
                enabled = true;
                break;
            }
        }
        for ( JComponent c : list )
            c.setEnabled( enabled );
    }


    private void bindGroup ( final String detailKey, final JComponent... list ) {
        for ( JComponent c : list ) {
            c.addMouseListener( new MouseAdapter() {
                @Override
                public synchronized void mouseEntered ( MouseEvent evt ) {
                    if ( settingPanel != null )
                        settingPanel.setDescription( TEXT.getString( detailKey + "_description" ) );
                }
                @Override
                public void mouseExited ( MouseEvent e ) {
                    if ( settingPanel != null )
                        settingPanel.setDescription( null );
                }
            } );
        }
    }

    public VoiceEnableBean getVoiceEnableBean () throws ConvertException {
    	VoiceEnableBean bean = new VoiceEnableBean();
    	bean.setDoor_open_enable(this.ebd_Voice_Door_Open_Enable.isSelected());
    	bean.setDoor_close_enable(this.ebd_Voice_Door_Close_Enable.isSelected());
    	bean.setDirect_enable(this.ebd_Voice_Direc_Enable.isSelected());
    	bean.setMessage_enable(this.ebd_Voice_Message_Enable.isSelected());
    	bean.setArrival_enable(this.ebd_Voice_Floor_Enable.isSelected());
    	bean.setButton_enable(this.ebd_Voice_Button_Enable.isSelected());
    	bean.setHall_up_voice((short)this.cbo_Hall_Up_Type.getSelectedIndex());
    	bean.setHall_down_voice((short)this.cbo_Hall_Down_Type.getSelectedIndex());
    	bean.setCar_up_voice((short)this.cbo_Car_Up_Type.getSelectedIndex());
    	bean.setCar_down_voice((short)this.cbo_Car_Down_Type.getSelectedIndex());
    	bean.setLevel_car_A((short)this.cbo_Car_Level_A.getSelectedIndex());
    	bean.setLevel_car_B((short)this.cbo_Car_Level_B.getSelectedIndex());
    	bean.setLevel_car_C((short)this.cbo_Car_Level_C.getSelectedIndex());
    	bean.setLevel_car_D((short)this.cbo_Car_Level_D.getSelectedIndex());
    	bean.setLevel_hall_A((short)this.cbo_Hall_Level_A.getSelectedIndex());
    	bean.setLevel_hall_B((short)this.cbo_Hall_Level_B.getSelectedIndex());
    	bean.setLevel_hall_C((short)this.cbo_Hall_Level_C.getSelectedIndex());
    	bean.setLevel_hall_D((short)this.cbo_Hall_Level_D.getSelectedIndex());
        return bean;
    }

    public void setVoiceEnableBean ( VoiceEnableBean bean ) {
        this.ebd_Voice_Door_Open_Enable.setOriginSelected( bean.isDoor_open_enable());
        this.ebd_Voice_Door_Close_Enable.setOriginSelected( bean.isDoor_close_enable());
        this.ebd_Voice_Direc_Enable.setOriginSelected( bean.isDirect_enable());
        this.ebd_Voice_Message_Enable.setOriginSelected( bean.isMessage_enable());
        this.ebd_Voice_Floor_Enable.setOriginSelected( bean.isArrival_enable());
        this.ebd_Voice_Button_Enable.setOriginSelected( bean.isButton_enable());
        this.cbo_Hall_Up_Type.setSelectedIndex(bean.getHall_up_voice());
        this.cbo_Hall_Down_Type.setSelectedIndex(bean.getHall_down_voice());
        this.cbo_Car_Up_Type.setSelectedIndex(bean.getCar_up_voice());
        this.cbo_Car_Down_Type.setSelectedIndex(bean.getCar_down_voice());
        this.cbo_Car_Level_A.setSelectedIndex(bean.getLevel_car_A());
        this.cbo_Car_Level_B.setSelectedIndex(bean.getLevel_car_B());
        this.cbo_Car_Level_C.setSelectedIndex(bean.getLevel_car_C());
        this.cbo_Car_Level_D.setSelectedIndex(bean.getLevel_car_D());
        this.cbo_Hall_Level_A.setSelectedIndex(bean.getLevel_hall_A());
        this.cbo_Hall_Level_B.setSelectedIndex(bean.getLevel_hall_B());
        this.cbo_Hall_Level_C.setSelectedIndex(bean.getLevel_hall_C());
        this.cbo_Hall_Level_D.setSelectedIndex(bean.getLevel_hall_D());
    }
    
    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static PromptTone createPanel ( SettingPanel<PromptTone> panel ) {
        PromptTone gui = new PromptTone();
        gui.setSettingPanel( panel );
        return gui;
    }
    
    public static String getBundleText ( String key, String defaultValue ) {
        String result;
        try {
            result = TEXT.getString( key );
        } catch ( Exception e ) {
            result = defaultValue;
        }
        return result;
    }
    
    public enum VoiceType {
        LIFT_UP,
        LIFT_DOWN,
        DING,
        DINGDANG,
        DONG,
        DONGDONG;
        
        public String toString() {
            return getBundleText("LBL_"+name(),name());
        }
    }
    
    public enum Language {
    	UNUSABLE,
    	CHINESE,
    	CANTONESE,
    	ENGLISH,
    	PORTUGUESE;
    	
        public String toString() {
            return getBundleText("LBL_"+name(),name());
        }
    }
    
    public static class VoiceEnableBean{
    	boolean	 door_open_enable;
    	boolean	 door_close_enable;
    	boolean	 direct_enable;
    	boolean	 message_enable;
    	boolean	 arrival_enable;
    	boolean	 button_enable;
    	short	 hall_up_voice;
    	short	 hall_down_voice;
    	short	 car_up_voice;
    	short	 car_down_voice;
    	short	 level_car_A;
    	short	 level_car_B;
    	short	 level_car_C;
    	short	 level_car_D;
    	short	 level_hall_A;
    	short	 level_hall_B;
    	short	 level_hall_C;
    	short	 level_hall_D;
    	
    	public boolean isDoor_open_enable() {
			return door_open_enable;
		}
		public void setDoor_open_enable(boolean door_open_enable) {
			this.door_open_enable = door_open_enable;
		}
		public boolean isDoor_close_enable() {
			return door_close_enable;
		}
		public void setDoor_close_enable(boolean door_close_enable) {
			this.door_close_enable = door_close_enable;
		}
		public boolean isDirect_enable() {
			return direct_enable;
		}
		public void setDirect_enable(boolean direct_enable) {
			this.direct_enable = direct_enable;
		}
		public boolean isMessage_enable() {
			return message_enable;
		}
		public void setMessage_enable(boolean message_enable) {
			this.message_enable = message_enable;
		}
		public boolean isArrival_enable() {
			return arrival_enable;
		}
		public void setArrival_enable(boolean arrival_enable) {
			this.arrival_enable = arrival_enable;
		}
		public boolean isButton_enable() {
			return button_enable;
		}
		public void setButton_enable(boolean button_enable) {
			this.button_enable = button_enable;
		}
		public short getHall_up_voice() {
			return hall_up_voice;
		}
		public void setHall_up_voice(short hall_up_voice) {
			this.hall_up_voice = hall_up_voice;
		}
		public short getHall_down_voice() {
			return hall_down_voice;
		}
		public void setHall_down_voice(short hall_down_voice) {
			this.hall_down_voice = hall_down_voice;
		}
		public short getCar_up_voice() {
			return car_up_voice;
		}
		public void setCar_up_voice(short car_up_voice) {
			this.car_up_voice = car_up_voice;
		}
		public short getCar_down_voice() {
			return car_down_voice;
		}
		public void setCar_down_voice(short car_down_voice) {
			this.car_down_voice = car_down_voice;
		}
		public short getLevel_car_A() {
			return level_car_A;
		}
		public void setLevel_car_A(short level_car_A) {
			this.level_car_A = level_car_A;
		}
		public short getLevel_car_B() {
			return level_car_B;
		}
		public void setLevel_car_B(short level_car_B) {
			this.level_car_B = level_car_B;
		}
		public short getLevel_car_C() {
			return level_car_C;
		}
		public void setLevel_car_C(short level_car_C) {
			this.level_car_C = level_car_C;
		}
		public short getLevel_car_D() {
			return level_car_D;
		}
		public void setLevel_car_D(short level_car_D) {
			this.level_car_D = level_car_D;
		}
		public short getLevel_hall_A() {
			return level_hall_A;
		}
		public void setLevel_hall_A(short level_hall_A) {
			this.level_hall_A = level_hall_A;
		}
		public short getLevel_hall_B() {
			return level_hall_B;
		}
		public void setLevel_hall_B(short level_hall_B) {
			this.level_hall_B = level_hall_B;
		}
		public short getLevel_hall_C() {
			return level_hall_C;
		}
		public void setLevel_hall_C(short level_hall_C) {
			this.level_hall_C = level_hall_C;
		}
		public short getLevel_hall_D() {
			return level_hall_D;
		}
		public void setLevel_hall_D(short level_hall_D) {
			this.level_hall_D = level_hall_D;
		}
    }
	
}
