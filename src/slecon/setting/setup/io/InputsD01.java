package slecon.setting.setup.io;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import logic.io.crossbar.InputPinD01;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyCheckBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;




public class InputsD01 extends JPanel {
    private static final long      serialVersionUID = 6592660277537767456L;
    
    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.io.InputsD01" );

    private static final ImageIcon INPUT_ON_ICON    = ImageFactory.LIGHT_BRIGHT_GREEN.icon( 16, 16 );
    private static final ImageIcon INPUT_OFF_ICON   = ImageFactory.LIGHT_DARK_GREEN.icon( 16, 16 );
    private static final ImageIcon GRAY_ICON        = ImageFactory.LIGHT_BLACK.icon( 16, 16 );
    


    private boolean                 started = false;
    private SettingPanel<InputsD01> settingPanel;
    
    private JLabel                        cpt_main_inputs;
    private JLabel                        lbl_title1;
    private JLabel                        lbl_title2;
    private JLabel                        lbl_title3;
    private JLabel                        lbl_title4;
    
    private JLabel                        lbl_line_usl;
    private MyComboBox				      cbo_line_usl;						
    private MyCheckBox                    chk_line_usl;
    private JLabel                        state_line_usl;
    
    private JLabel                        lbl_line_lsl;
    private MyComboBox				      cbo_line_lsl;

    private MyCheckBox                    chk_line_lsl;
    private JLabel                        state_line_lsl;
    
    private JLabel                        lbl_line_udz;
    private MyComboBox				      cbo_line_udz;
    private MyCheckBox                    chk_line_udz;
    private JLabel                        state_line_udz;
    
    private JLabel                        lbl_line_ldz;
    private MyComboBox				      cbo_line_ldz;
    private MyCheckBox                    chk_line_ldz;
    private JLabel                        state_line_ldz;
    
    private JLabel                        lbl_line_ins;
    private MyComboBox				      cbo_line_ins;
    private MyCheckBox                    chk_line_ins;
    private JLabel                        state_line_ins;
    
    private JLabel                        lbl_line_insup;
    private MyComboBox				      cbo_line_insup;
    private MyCheckBox                    chk_line_insup;
    private JLabel                        state_line_insup;
    
    private JLabel                        lbl_line_insdown;
    private MyComboBox				      cbo_line_insdown;
    MyCheckBox                            chk_line_insdown;
    JLabel                                state_line_insdown;
    
    private JLabel                        lbl_line_drvok;
    private MyComboBox			          cbo_line_drvok;
    MyCheckBox                            chk_line_drvok;
    JLabel                                state_line_drvok;
    
    private JLabel                        lbl_line_drvbm;
    private MyComboBox				      cbo_line_drvbm;
    MyCheckBox                            chk_line_drvbm;
    JLabel                                state_line_drvbm;
    
    private JLabel                        lbl_line_dbde;
    private MyComboBox				      cbo_line_dbde;
    private MyCheckBox                    chk_line_dbde;
    private JLabel                        state_line_dbde;
    
    private JLabel                        lbl_line_dbdf;
    private MyComboBox				      cbo_line_dbdf;
    private MyCheckBox                    chk_line_dbdf;
    private JLabel                        state_line_dbdf;
    
    private JLabel                        lbl_line_bs1;
    private MyComboBox				      cbo_line_bs1;
    private MyCheckBox                    chk_line_bs1;
    private JLabel                        state_line_bs1;
    
    private JLabel                        lbl_line_bs2;
    private MyComboBox				      cbo_line_bs2;
    private MyCheckBox                    chk_line_bs2;
    private JLabel                        state_line_bs2;
    
    private JLabel                        lbl_line_st1;
    private MyComboBox				      cbo_line_st1;
    private MyCheckBox                    chk_line_st1;
    private JLabel                        state_line_st1;

    private JLabel                        lbl_line_st2;
    private MyComboBox			          cbo_line_st2;
    private MyCheckBox                    chk_line_st2;
    private JLabel                        state_line_st2;
    
    private JLabel                        lbl_line_st3;
    private MyComboBox				      cbo_line_st3;
    private MyCheckBox                    chk_line_st3;
    private JLabel                        state_line_st3;

    private JLabel                        lbl_line_st4;
    private MyComboBox			          cbo_line_st4;
    private MyCheckBox                    chk_line_st4;
    private JLabel                        state_line_st4;

    private JLabel                        lbl_line_st5;
    private MyComboBox				      cbo_line_st5;
    private MyCheckBox                    chk_line_st5;
    private JLabel                        state_line_st5;

    private JLabel                        lbl_line_st6;
    private MyComboBox			          cbo_line_st6;
    private MyCheckBox                    chk_line_st6;
    private JLabel                        state_line_st6;
    
    private JLabel                        lbl_line_lvcfb;
    private MyComboBox			          cbo_line_lvcfb;
    private MyCheckBox                    chk_line_lvcfb;
    private JLabel                        state_line_lvcfb;
    
    private JLabel                        lbl_line_lvc1fb;
    private MyComboBox			          cbo_line_lvc1fb;
    private MyCheckBox                    chk_line_lvc1fb;
    private JLabel                        state_line_lvc1fb;
    
    private JLabel                        lbl_line_thm;
    private MyComboBox				      cbo_line_thm;
    private MyCheckBox                    chk_line_thm;
    private JLabel                        state_line_thm;
    
    private JLabel                        lbl_line_encf;
    private MyComboBox				      cbo_line_encf;
    private MyCheckBox                    chk_line_encf;
    private JLabel                        state_line_encf;
    
    private JLabel                        lbl_line_url;
    private MyComboBox				      cbo_line_url;
    private MyCheckBox                    chk_line_url;
    private JLabel                        state_line_url;
    
    private JLabel                        lbl_line_lrl;
    private MyComboBox			          cbo_line_lrl;
    private MyCheckBox                    chk_line_lrl;
    private JLabel                        state_line_lrl;
    
    private JLabel                        lbl_line_epb;
    private MyComboBox				      cbo_line_epb;
    private MyCheckBox                    chk_line_epb;
    private JLabel                        state_line_epb;
    
    private JLabel                        lbl_line_ucmts;
    private MyComboBox				      cbo_line_ucmts;
    private MyCheckBox                    chk_line_ucmts;
    private JLabel                        state_line_ucmts;
    
    private JLabel                        lbl_line_ucmts2;
    private MyComboBox        			  cbo_line_ucmts2;
    private MyCheckBox                     chk_line_ucmts2;
    private JLabel                        state_line_ucmts2;
    
    private JLabel                        lbl_line_ucmts3;
    private MyComboBox        		      cbo_line_ucmts3;
    private MyCheckBox                     chk_line_ucmts3;
    private JLabel                        state_line_ucmts3;
    
    private JLabel                        lbl_line_dlb;
    private MyComboBox        		      cbo_line_dlb;
    private MyCheckBox                    chk_line_dlb;
    private JLabel                        state_line_dlb;
    
    private JLabel                        lbl_line_insm;
    private MyComboBox        		      cbo_line_insm;
    private MyCheckBox                    chk_line_insm;    
    private JLabel                        state_line_insm;
    
    private JLabel                        lbl_line_iu;
    private MyComboBox        		      cbo_line_iu;
    private MyCheckBox                    chk_line_iu;
    private JLabel                        state_line_iu;
    
    private JLabel                        lbl_line_id;
    private MyComboBox        		      cbo_line_id;
    private MyCheckBox                    chk_line_id;
    private JLabel                        state_line_id;
    
    private JLabel                        lbl_line_isu;
    private MyComboBox        		      cbo_line_isu;
    private MyCheckBox                    chk_line_isu;
    private JLabel                        state_line_isu;
    
    private JLabel                        lbl_line_isd;
    private MyComboBox        		      cbo_line_isd;
    private MyCheckBox                    chk_line_isd;
    private JLabel                        state_line_isd;
    
    private HashMap<InputPinD01, Boolean> inputPinState;
    
    public InputsD01 () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<InputsD01> panel ) {
        this.settingPanel = panel;
    }

    
    public void SetJComboBoxEnable(boolean Enable) {
    	this.cbo_line_usl.setEnabled(Enable);
    	this.cbo_line_lsl.setEnabled(Enable);
    	this.cbo_line_udz.setEnabled(Enable);
    	this.cbo_line_ldz.setEnabled(Enable);
    	this.cbo_line_ins.setEnabled(Enable);
    	this.cbo_line_insup.setEnabled(Enable);
    	this.cbo_line_insdown.setEnabled(Enable);
    	this.cbo_line_drvok.setEnabled(Enable);
    	this.cbo_line_drvbm.setEnabled(Enable);
    	this.cbo_line_dbde.setEnabled(Enable);
    	this.cbo_line_dbdf.setEnabled(Enable);
    	this.cbo_line_bs1.setEnabled(Enable);
    	this.cbo_line_bs2.setEnabled(Enable);
    	this.cbo_line_st1.setEnabled(Enable);
    	this.cbo_line_st2.setEnabled(Enable);
    	this.cbo_line_st3.setEnabled(Enable);
    	this.cbo_line_st4.setEnabled(Enable);
    	this.cbo_line_st5.setEnabled(Enable);
    	this.cbo_line_st6.setEnabled(Enable);
    	this.cbo_line_lvcfb.setEnabled(Enable);
    	this.cbo_line_lvc1fb.setEnabled(Enable);
    	this.cbo_line_thm.setEnabled(Enable);
    	this.cbo_line_encf.setEnabled(Enable);
    	this.cbo_line_url.setEnabled(Enable);
    	this.cbo_line_lrl.setEnabled(Enable);
    	this.cbo_line_epb.setEnabled(Enable);
    	this.cbo_line_ucmts.setEnabled(Enable);
    	this.cbo_line_ucmts2.setEnabled(Enable);
    	this.cbo_line_ucmts3.setEnabled(Enable);
    	this.cbo_line_dlb.setEnabled(Enable);
    	this.cbo_line_insm.setEnabled(Enable);
    	this.cbo_line_iu.setEnabled(Enable);
    	this.cbo_line_id.setEnabled(Enable);
    	this.cbo_line_isu.setEnabled(Enable);
    	this.cbo_line_isd.setEnabled(Enable);
    	
    	this.chk_line_usl.setEnabled(Enable);
    	this.chk_line_lsl.setEnabled(Enable);
    	this.chk_line_udz.setEnabled(Enable);
    	this.chk_line_ldz.setEnabled(Enable);
    	this.chk_line_ins.setEnabled(Enable);
    	this.chk_line_insup.setEnabled(Enable);
    	this.chk_line_insdown.setEnabled(Enable);
    	this.chk_line_drvok.setEnabled(Enable);
    	this.chk_line_drvbm.setEnabled(Enable);
    	this.chk_line_dbde.setEnabled(Enable);
    	this.chk_line_dbdf.setEnabled(Enable);
    	this.chk_line_bs1.setEnabled(Enable);
    	this.chk_line_bs2.setEnabled(Enable);
    	this.chk_line_st1.setEnabled(Enable);
    	this.chk_line_st2.setEnabled(Enable);
    	this.chk_line_st3.setEnabled(Enable);
    	this.chk_line_st4.setEnabled(Enable);
    	this.chk_line_st5.setEnabled(Enable);
    	this.chk_line_st6.setEnabled(Enable);
    	this.chk_line_lvcfb.setEnabled(Enable);
    	this.chk_line_lvc1fb.setEnabled(Enable);
    	this.chk_line_thm.setEnabled(Enable);
    	this.chk_line_encf.setEnabled(Enable);
    	this.chk_line_url.setEnabled(Enable);
    	this.chk_line_lrl.setEnabled(Enable);
    	this.chk_line_epb.setEnabled(Enable);
    	this.chk_line_ucmts.setEnabled(Enable);
    	this.chk_line_ucmts2.setEnabled(Enable);
    	this.chk_line_ucmts3.setEnabled(Enable);
    	this.chk_line_dlb.setEnabled(Enable);
    	this.chk_line_insm.setEnabled(Enable);
    	this.chk_line_iu.setEnabled(Enable);
    	this.chk_line_id.setEnabled(Enable);
    	this.chk_line_isu.setEnabled(Enable);
    	this.chk_line_isd.setEnabled(Enable);
    }
    
    private void initGUI () {
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "ins 24, gap 10 6", "[30::30][20::20][100::100][200::200][][50::50, center]" ) );
        cpt_main_inputs = new JLabel();
        lbl_title1      = new JLabel();
        lbl_title2      = new JLabel();
        lbl_title3      = new JLabel();
        lbl_title4      = new JLabel();
        
        /**
         * Generated by Groovy script: list.each { c-> println "lbl_line_${c.toLowerCase()} = new JLabel();" println
         * "cbo_line_${c.toLowerCase()} = new JComboBox<InputSource>(InputSource.allDisplayItem());"; println "chk_line_${c.toLowerCase()} = new MyCheckBox();"
         * println "state_line_${c.toLowerCase()} = new JLabel();"; println "" }
         */
        lbl_line_usl = new JLabel();
        cbo_line_usl = new MyComboBox();
        cbo_line_usl.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_usl   = new MyCheckBox();
        state_line_usl = new JLabel( GRAY_ICON );
        lbl_line_lsl   = new JLabel();
        cbo_line_lsl   = new MyComboBox();
        cbo_line_lsl.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_lsl   = new MyCheckBox();
        state_line_lsl = new JLabel( GRAY_ICON );
        lbl_line_udz   = new JLabel();
        cbo_line_udz   = new MyComboBox();
        cbo_line_udz.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_udz   = new MyCheckBox();
        state_line_udz = new JLabel( GRAY_ICON );
        lbl_line_ldz   = new JLabel();
        cbo_line_ldz   = new MyComboBox();
        cbo_line_ldz.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_ldz   = new MyCheckBox();
        state_line_ldz = new JLabel( GRAY_ICON );
        lbl_line_ins   = new JLabel();
        cbo_line_ins   = new MyComboBox();
        cbo_line_ins.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_ins   = new MyCheckBox();
        state_line_ins = new JLabel( GRAY_ICON );
        lbl_line_insup = new JLabel();
        cbo_line_insup = new MyComboBox();
        cbo_line_insup.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_insup   = new MyCheckBox();
        state_line_insup = new JLabel( GRAY_ICON );
        lbl_line_insdown = new JLabel();
        cbo_line_insdown = new MyComboBox();
        cbo_line_insdown.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_insdown   = new MyCheckBox();
        state_line_insdown = new JLabel( GRAY_ICON );
        lbl_line_drvok     = new JLabel();
        cbo_line_drvok     = new MyComboBox();
        cbo_line_drvok.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_drvok   = new MyCheckBox();
        state_line_drvok = new JLabel( GRAY_ICON );
        lbl_line_drvbm   = new JLabel();
        cbo_line_drvbm   = new MyComboBox();
        cbo_line_drvbm.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_drvbm   = new MyCheckBox();
        state_line_drvbm = new JLabel( GRAY_ICON );
        lbl_line_dbde   = new JLabel();
        cbo_line_dbde   = new MyComboBox();
        cbo_line_dbde.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_dbde   = new MyCheckBox();
        state_line_dbde = new JLabel( GRAY_ICON );
        lbl_line_dbdf   = new JLabel();
        cbo_line_dbdf   = new MyComboBox();
        cbo_line_dbdf.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_dbdf   = new MyCheckBox();
        state_line_dbdf = new JLabel( GRAY_ICON );
        lbl_line_bs1     = new JLabel();
        cbo_line_bs1     = new MyComboBox();     
        cbo_line_bs1.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_bs1   = new MyCheckBox();
        state_line_bs1 = new JLabel( GRAY_ICON );
        lbl_line_bs2     = new JLabel();
        cbo_line_bs2     = new MyComboBox();
        cbo_line_bs2.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_bs2   = new MyCheckBox();
        state_line_bs2 = new JLabel( GRAY_ICON );
        lbl_line_st1   = new JLabel();
        cbo_line_st1   = new MyComboBox();
        cbo_line_st1.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_st1   = new MyCheckBox();
        state_line_st1 = new JLabel( GRAY_ICON );
        lbl_line_st2  = new JLabel();
        cbo_line_st2  = new MyComboBox();
        cbo_line_st2.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_st2   = new MyCheckBox();
        state_line_st2 = new JLabel( GRAY_ICON );
        lbl_line_st3  = new JLabel();
        cbo_line_st3  = new MyComboBox();
        cbo_line_st3.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_st3   = new MyCheckBox();
        state_line_st3 = new JLabel( GRAY_ICON );
        lbl_line_st4  = new JLabel();
        cbo_line_st4  = new MyComboBox();
        cbo_line_st4.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_st4   = new MyCheckBox();
        state_line_st4 = new JLabel( GRAY_ICON );
        lbl_line_st5  = new JLabel();
        cbo_line_st5  = new MyComboBox();
        cbo_line_st5.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_st5   = new MyCheckBox();
        state_line_st5 = new JLabel( GRAY_ICON );
        lbl_line_st6  = new JLabel();
        cbo_line_st6  = new MyComboBox();
        cbo_line_st6.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_st6   = new MyCheckBox();
        state_line_st6 = new JLabel( GRAY_ICON );
        lbl_line_lvcfb = new JLabel();
        cbo_line_lvcfb = new MyComboBox();
        cbo_line_lvcfb.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_lvcfb   = new MyCheckBox();
        state_line_lvcfb = new JLabel( GRAY_ICON );
        lbl_line_lvc1fb = new JLabel();
        cbo_line_lvc1fb = new MyComboBox();
        cbo_line_lvc1fb.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_lvc1fb   = new MyCheckBox();
        state_line_lvc1fb = new JLabel( GRAY_ICON );
        lbl_line_thm     = new JLabel();
        cbo_line_thm     = new MyComboBox();
        cbo_line_thm.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_thm   = new MyCheckBox();
        state_line_thm = new JLabel( GRAY_ICON );
        lbl_line_encf  = new JLabel();
        cbo_line_encf  = new MyComboBox();
        cbo_line_encf.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_encf   = new MyCheckBox();
        state_line_encf = new JLabel( GRAY_ICON );
        lbl_line_url  = new JLabel();
        cbo_line_url  = new MyComboBox();
        cbo_line_url.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_url   = new MyCheckBox();
        state_line_url = new JLabel( GRAY_ICON );
        lbl_line_lrl  = new JLabel();
        cbo_line_lrl  = new MyComboBox();
        cbo_line_lrl.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_lrl   = new MyCheckBox();
        state_line_lrl = new JLabel( GRAY_ICON );
        lbl_line_epb  = new JLabel();
        cbo_line_epb  = new MyComboBox();
        cbo_line_epb.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_epb   = new MyCheckBox();
        state_line_epb = new JLabel( GRAY_ICON );
        lbl_line_ucmts  = new JLabel();
        cbo_line_ucmts  = new MyComboBox();
        cbo_line_ucmts.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_ucmts   = new MyCheckBox();
        state_line_ucmts = new JLabel( GRAY_ICON );
        lbl_line_ucmts2  = new JLabel();
        cbo_line_ucmts2  = new MyComboBox();
        cbo_line_ucmts2.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_ucmts2   = new MyCheckBox();
        state_line_ucmts2 = new JLabel( GRAY_ICON );
        lbl_line_ucmts3  = new JLabel();
        cbo_line_ucmts3  = new MyComboBox();
        cbo_line_ucmts3.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_ucmts3   = new MyCheckBox();
        state_line_ucmts3 = new JLabel( GRAY_ICON );
        lbl_line_dlb = new JLabel();
        cbo_line_dlb = new MyComboBox();
        cbo_line_dlb.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_dlb = new MyCheckBox();
        state_line_dlb = new JLabel( GRAY_ICON );
        lbl_line_insm = new JLabel();
        cbo_line_insm = new MyComboBox();
        cbo_line_insm.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_insm = new MyCheckBox();
        state_line_insm = new JLabel( GRAY_ICON );
        lbl_line_iu = new JLabel();
        cbo_line_iu = new MyComboBox();
        cbo_line_iu.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_iu = new MyCheckBox();
        state_line_iu = new JLabel( GRAY_ICON );
        lbl_line_id = new JLabel();
        cbo_line_id = new MyComboBox();
        cbo_line_id.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_id = new MyCheckBox();
        state_line_id = new JLabel( GRAY_ICON );
        lbl_line_isu = new JLabel();
        cbo_line_isu = new MyComboBox();
        cbo_line_isu.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_isu = new MyCheckBox();
        state_line_isu = new JLabel( GRAY_ICON );
        lbl_line_isd = new JLabel();
        cbo_line_isd = new MyComboBox();
        cbo_line_isd.setModel( new DefaultComboBoxModel<>( InputPinD01.allDisplayItem() ) );
        chk_line_isd = new MyCheckBox();
        state_line_isd = new JLabel( GRAY_ICON );
        
        SetJComboBoxEnable(false);
        
        setCaptionStyle( cpt_main_inputs );
        setTitleStyle( lbl_title1 );
        setTitleStyle( lbl_title2 );
        setTitleStyle( lbl_title3 );
        setTitleStyle( lbl_title4 );

        /**
         * Generated by Groovy script: list.each { c-> println "setLabelTitleStyle(lbl_line_${c.toLowerCase()});" println
         * "setComboBoxValueStyle(cbo_line_${c.toLowerCase()});" println "setCheckBoxStyle(chk_line_${c.toLowerCase()});" }
         */
        setLabelTitleStyle( lbl_line_usl );
        setComboBoxValueStyle( cbo_line_usl );
        setCheckBoxStyle( chk_line_usl );
        setLabelTitleStyle( lbl_line_lsl );
        setComboBoxValueStyle( cbo_line_lsl );
        setCheckBoxStyle( chk_line_lsl );
        setLabelTitleStyle( lbl_line_udz );
        setComboBoxValueStyle( cbo_line_udz );
        setCheckBoxStyle( chk_line_udz );
        setLabelTitleStyle( lbl_line_ldz );
        setComboBoxValueStyle( cbo_line_ldz );
        setCheckBoxStyle( chk_line_ldz );
        setLabelTitleStyle( lbl_line_ins );
        setComboBoxValueStyle( cbo_line_ins );
        setCheckBoxStyle( chk_line_ins );
        setLabelTitleStyle( lbl_line_insup );
        setComboBoxValueStyle( cbo_line_insup );
        setCheckBoxStyle( chk_line_insup );
        setLabelTitleStyle( lbl_line_insdown );
        setComboBoxValueStyle( cbo_line_insdown );
        setCheckBoxStyle( chk_line_insdown );
        setLabelTitleStyle( lbl_line_drvok );
        setComboBoxValueStyle( cbo_line_drvok );
        setCheckBoxStyle( chk_line_drvok );
        setLabelTitleStyle( lbl_line_drvbm );
        setComboBoxValueStyle( cbo_line_drvbm );
        setCheckBoxStyle( chk_line_drvbm );
        setLabelTitleStyle( lbl_line_dbde );
        setComboBoxValueStyle( cbo_line_dbde );
        setCheckBoxStyle( chk_line_dbde );
        setLabelTitleStyle( lbl_line_dbdf );
        setComboBoxValueStyle( cbo_line_dbdf );
        setCheckBoxStyle( chk_line_dbdf );
        setLabelTitleStyle( lbl_line_bs1 );
        setComboBoxValueStyle( cbo_line_bs1 );
        setCheckBoxStyle( chk_line_bs1 );
        setLabelTitleStyle( lbl_line_bs2 );
        setComboBoxValueStyle( cbo_line_bs2 );
        setCheckBoxStyle( chk_line_bs2 );
        setLabelTitleStyle( lbl_line_st1 );
        setComboBoxValueStyle( cbo_line_st1 );
        setCheckBoxStyle( chk_line_st1 );
        setLabelTitleStyle( lbl_line_st2 );
        setComboBoxValueStyle( cbo_line_st2 );
        setCheckBoxStyle( chk_line_st2 );
        setLabelTitleStyle( lbl_line_st3 );
        setComboBoxValueStyle( cbo_line_st3 );
        setCheckBoxStyle( chk_line_st3 );
        setLabelTitleStyle( lbl_line_st4 );
        setComboBoxValueStyle( cbo_line_st4 );
        setCheckBoxStyle( chk_line_st4 );
        setLabelTitleStyle( lbl_line_st5 );
        setComboBoxValueStyle( cbo_line_st5 );
        setCheckBoxStyle( chk_line_st5 );
        setLabelTitleStyle( lbl_line_st6 );
        setComboBoxValueStyle( cbo_line_st6 );
        setCheckBoxStyle( chk_line_st6 );
        setLabelTitleStyle( lbl_line_lvcfb );
        setComboBoxValueStyle( cbo_line_lvcfb );
        setCheckBoxStyle( chk_line_lvcfb );
        setLabelTitleStyle( lbl_line_lvc1fb );
        setComboBoxValueStyle( cbo_line_lvc1fb );
        setCheckBoxStyle( chk_line_lvc1fb );
        setLabelTitleStyle( lbl_line_thm );
        setComboBoxValueStyle( cbo_line_thm );
        setCheckBoxStyle( chk_line_thm );
        setLabelTitleStyle( lbl_line_encf );
        setComboBoxValueStyle( cbo_line_encf );
        setCheckBoxStyle( chk_line_encf );
        setLabelTitleStyle( lbl_line_url );
        setComboBoxValueStyle( cbo_line_url );
        setCheckBoxStyle( chk_line_url );
        setLabelTitleStyle( lbl_line_lrl );
        setComboBoxValueStyle( cbo_line_lrl );
        setCheckBoxStyle( chk_line_lrl );
        setLabelTitleStyle( lbl_line_epb );
        setComboBoxValueStyle( cbo_line_epb );
        setCheckBoxStyle( chk_line_epb );
        setLabelTitleStyle( lbl_line_ucmts );
        setComboBoxValueStyle( cbo_line_ucmts );
        setCheckBoxStyle( chk_line_ucmts );
        setLabelTitleStyle( lbl_line_ucmts2 );
        setComboBoxValueStyle( cbo_line_ucmts2 );
        setCheckBoxStyle( chk_line_ucmts2 );
        setLabelTitleStyle( lbl_line_ucmts3 );
        setComboBoxValueStyle( cbo_line_ucmts3 );
        setCheckBoxStyle( chk_line_ucmts3 );
        setLabelTitleStyle( lbl_line_dlb );
        setComboBoxValueStyle( cbo_line_dlb );
        setCheckBoxStyle( chk_line_dlb );
        setLabelTitleStyle( lbl_line_insm );
        setComboBoxValueStyle( cbo_line_insm );
        setCheckBoxStyle( chk_line_insm );
        setLabelTitleStyle( lbl_line_iu );
        setComboBoxValueStyle( cbo_line_iu );
        setCheckBoxStyle( chk_line_iu );
        setLabelTitleStyle( lbl_line_id );
        setComboBoxValueStyle( cbo_line_id );
        setCheckBoxStyle( chk_line_id );
        setLabelTitleStyle( lbl_line_isu );
        setComboBoxValueStyle( cbo_line_isu );
        setCheckBoxStyle( chk_line_isu );
        setLabelTitleStyle( lbl_line_isd );
        setComboBoxValueStyle( cbo_line_isd );
        setCheckBoxStyle( chk_line_isd );
        
        add( cpt_main_inputs, "gapbottom 18-12, span, aligny center" );
        add( lbl_title1, "cell 2 1" );
        add( lbl_title2, "cell 3 1" );
        add( lbl_title3, "cell 4 1" );
        add( lbl_title4, "cell 5 1" );

        add( lbl_line_usl, "cell 2 2" );
        add( cbo_line_usl, "cell 3 2" );
        add( chk_line_usl, "center, cell 4 2" );
        add( state_line_usl, "center, cell 5 2" );
        add( lbl_line_lsl, "cell 2 3" );
        add( cbo_line_lsl, "cell 3 3" );
        add( chk_line_lsl, "center, cell 4 3" );
        add( state_line_lsl, "center, cell 5 3" );
        add( lbl_line_udz, "cell 2 4" );
        add( cbo_line_udz, "cell 3 4" );
        add( chk_line_udz, "center, cell 4 4" );
        add( state_line_udz, "center, cell 5 4" );
        add( lbl_line_ldz, "cell 2 5" );
        add( cbo_line_ldz, "cell 3 5" );
        add( chk_line_ldz, "center, cell 4 5" );
        add( state_line_ldz, "center, cell 5 5" );
        add( lbl_line_ins, "cell 2 6" );
        add( cbo_line_ins, "cell 3 6" );
        add( chk_line_ins, "center, cell 4 6" );
        add( state_line_ins, "center, cell 5 6" );
        add( lbl_line_insup, "cell 2 7" );
        add( cbo_line_insup, "cell 3 7" );
        add( chk_line_insup, "center, cell 4 7" );
        add( state_line_insup, "center, cell 5 7" );
        add( lbl_line_insdown, "cell 2 8" );
        add( cbo_line_insdown, "cell 3 8" );
        add( chk_line_insdown, "center, cell 4 8" );
        add( state_line_insdown, "center, cell 5 8" );
        add( lbl_line_drvok, "cell 2 9" );
        add( cbo_line_drvok, "cell 3 9" );
        add( chk_line_drvok, "center, cell 4 9" );
        add( state_line_drvok, "center, cell 5 9" );
        add( lbl_line_drvbm, "cell 2 10" );
        add( cbo_line_drvbm, "cell 3 10" );
        add( chk_line_drvbm, "center, cell 4 10" );
        add( state_line_drvbm, "center, cell 5 10" );
        add( lbl_line_dbde, "cell 2 11" );
        add( cbo_line_dbde, "cell 3 11" );
        add( chk_line_dbde, "center, cell 4 11" );
        add( state_line_dbde, "center, cell 5 11" );
        add( lbl_line_dbdf, "cell 2 12" );
        add( cbo_line_dbdf, "cell 3 12" );
        add( chk_line_dbdf, "center, cell 4 12" );
        add( state_line_dbdf, "center, cell 5 12" );
        add( lbl_line_bs1, "cell 2 13" );
        add( cbo_line_bs1, "cell 3 13" );
        add( chk_line_bs1, "center, cell 4 13" );
        add( state_line_bs1, "center, cell 5 13" );
        add( lbl_line_bs2, "cell 2 14" );
        add( cbo_line_bs2, "cell 3 14" );
        add( chk_line_bs2, "center, cell 4 14" );
        add( state_line_bs2, "center, cell 5 14" );
        add( lbl_line_st1, "cell 2 15" );
        add( cbo_line_st1, "cell 3 15" );
        add( chk_line_st1, "center, cell 4 15" );
        add( state_line_st1, "center, cell 5 15" );
        add( lbl_line_st2, "cell 2 16" );
        add( cbo_line_st2, "cell 3 16" );
        add( chk_line_st2, "center, cell 4 16" );
        add( state_line_st2, "center, cell 5 16" );
        add( lbl_line_st3, "cell 2 17" );
        add( cbo_line_st3, "cell 3 17" );
        add( chk_line_st3, "center, cell 4 17" );
        add( state_line_st3, "center, cell 5 17" );
        add( lbl_line_st4, "cell 2 18" );
        add( cbo_line_st4, "cell 3 18" );
        add( chk_line_st4, "center, cell 4 18" );
        add( state_line_st4, "center, cell 5 18" );
        add( lbl_line_st5, "cell 2 19" );
        add( cbo_line_st5, "cell 3 19" );
        add( chk_line_st5, "center, cell 4 19" );
        add( state_line_st5, "center, cell 5 19" );
        add( lbl_line_st6, "cell 2 20" );
        add( cbo_line_st6, "cell 3 20" );
        add( chk_line_st6, "center, cell 4 20" );
        add( state_line_st6, "center, cell 5 20" );        
        add( lbl_line_lvcfb, "cell 2 21" );
        add( cbo_line_lvcfb, "cell 3 21" );
        add( chk_line_lvcfb, "center, cell 4 21" );
        add( state_line_lvcfb, "center, cell 5 21" );
        add( lbl_line_lvc1fb, "cell 2 22" );
        add( cbo_line_lvc1fb, "cell 3 22" );
        add( chk_line_lvc1fb, "center, cell 4 22" );
        add( state_line_lvc1fb, "center, cell 5 22" );
        add( lbl_line_thm, "cell 2 23" );
        add( cbo_line_thm, "cell 3 23" );
        add( chk_line_thm, "center, cell 4 23" );
        add( state_line_thm, "center, cell 5 23" );
        add( lbl_line_encf, "cell 2 24" );
        add( cbo_line_encf, "cell 3 24" );
        add( chk_line_encf, "center, cell 4 24" );
        add( state_line_encf, "center, cell 5 24" );
        add( lbl_line_url, "cell 2 25" );
        add( cbo_line_url, "cell 3 25" );
        add( chk_line_url, "center, cell 4 25" );
        add( state_line_url, "center, cell 5 25" );
        add( lbl_line_lrl, "cell 2 26" );
        add( cbo_line_lrl, "cell 3 26" );
        add( chk_line_lrl, "center, cell 4 26" );
        add( state_line_lrl, "center, cell 5 26" );
        add( lbl_line_epb, "cell 2 27" );
        add( cbo_line_epb, "cell 3 27" );
        add( chk_line_epb, "center, cell 4 27" );
        add( state_line_epb, "center, cell 5 27" );
        add( lbl_line_ucmts, "cell 2 28" );
        add( cbo_line_ucmts, "cell 3 28" );
        add( chk_line_ucmts, "center, cell 4 28" );
        add( state_line_ucmts, "center, cell 5 28" );
        add( lbl_line_ucmts2, "cell 2 29" );
        add( cbo_line_ucmts2, "cell 3 29" );
        add( chk_line_ucmts2, "center, cell 4 29" );
        add( state_line_ucmts2, "center, cell 5 29" );
        add( lbl_line_ucmts3, "cell 2 30" );
        add( cbo_line_ucmts3, "cell 3 30" );
        add( chk_line_ucmts3, "center, cell 4 30" );
        add( state_line_ucmts3, "center, cell 5 30" );
        add( lbl_line_dlb, "cell 2 31" );
        add( cbo_line_dlb, "cell 3 31" );
        add( chk_line_dlb, "center, cell 4 31" );
        add( state_line_dlb, "center, cell 5 31" );
        add( lbl_line_insm, "cell 2 32" );
        add( cbo_line_insm, "cell 3 32" );
        add( chk_line_insm, "center, cell 4 32" );
        add( state_line_insm, "center, cell 5 32" );
        add( lbl_line_iu, "cell 2 33" );
        add( cbo_line_iu, "cell 3 33" );
        add( chk_line_iu, "center, cell 4 33" );
        add( state_line_iu, "center, cell 5 33" );
        add( lbl_line_id, "cell 2 34" );
        add( cbo_line_id, "cell 3 34" );
        add( chk_line_id, "center, cell 4 34" );
        add( state_line_id, "center, cell 5 34" );
        add( lbl_line_isu, "cell 2 35" );
        add( cbo_line_isu, "cell 3 35" );
        add( chk_line_isu, "center, cell 4 35" );
        add( state_line_isu, "center, cell 5 35" );
        add( lbl_line_isd, "cell 2 36" );
        add( cbo_line_isd, "cell 3 36" );
        add( chk_line_isd, "center, cell 4 36" );
        add( state_line_isd, "center, cell 5 36" );
        
        ItemChangedListener icl = new ItemChangedListener();
        cbo_line_usl.addItemListener( icl );
        cbo_line_lsl.addItemListener( icl );
        cbo_line_udz.addItemListener( icl );
        cbo_line_ldz.addItemListener( icl );
        cbo_line_ins.addItemListener( icl );
        cbo_line_insup.addItemListener( icl );
        cbo_line_insdown.addItemListener( icl );
        cbo_line_drvok.addItemListener( icl );
        cbo_line_drvbm.addItemListener( icl );
        cbo_line_dbde.addItemListener( icl );
        cbo_line_dbdf.addItemListener( icl );
        cbo_line_bs1.addItemListener( icl );
        cbo_line_bs2.addItemListener( icl );
        cbo_line_st1.addItemListener( icl );
        cbo_line_st2.addItemListener( icl );
        cbo_line_st3.addItemListener( icl );
        cbo_line_st4.addItemListener( icl );
        cbo_line_st5.addItemListener( icl );
        cbo_line_st6.addItemListener( icl );
        cbo_line_lvcfb.addItemListener( icl );
        cbo_line_lvc1fb.addItemListener( icl );
        cbo_line_thm.addItemListener( icl );
        cbo_line_encf.addItemListener( icl );
        cbo_line_url.addItemListener( icl );
        cbo_line_lrl.addItemListener( icl );
        cbo_line_epb.addItemListener(icl);
        cbo_line_ucmts.addItemListener(icl);
        cbo_line_ucmts2.addItemListener(icl);
        cbo_line_ucmts3.addItemListener(icl);
        cbo_line_dlb.addItemListener(icl);
        cbo_line_insm.addItemListener(icl);
        cbo_line_iu.addItemListener(icl);
        cbo_line_id.addItemListener(icl);
        cbo_line_isu.addItemListener(icl);
        cbo_line_isd.addItemListener(icl);
        
        chk_line_usl.addActionListener( icl );
        chk_line_lsl.addActionListener( icl );
        chk_line_udz.addActionListener( icl );
        chk_line_ldz.addActionListener( icl );
        chk_line_ins.addActionListener( icl );
        chk_line_insup.addActionListener( icl );
        chk_line_insdown.addActionListener( icl );
        chk_line_drvok.addActionListener( icl );
        chk_line_drvbm.addActionListener( icl );
        chk_line_dbde.addActionListener( icl );
        chk_line_dbdf.addActionListener( icl );
        chk_line_bs1.addActionListener( icl );
        chk_line_bs2.addActionListener( icl );
        chk_line_st1.addActionListener( icl );
        chk_line_st2.addActionListener( icl );
        chk_line_st3.addActionListener( icl );
        chk_line_st4.addActionListener( icl );
        chk_line_st5.addActionListener( icl );
        chk_line_st6.addActionListener( icl );
        chk_line_lvcfb.addActionListener( icl );
        chk_line_lvc1fb.addActionListener( icl );
        chk_line_thm.addActionListener( icl );
        chk_line_encf.addActionListener( icl );
        chk_line_url.addActionListener( icl );
        chk_line_lrl.addActionListener( icl );
        chk_line_epb.addActionListener( icl );
        chk_line_ucmts.addActionListener( icl );
        chk_line_ucmts2.addActionListener( icl );
        chk_line_ucmts3.addActionListener( icl );
        chk_line_dlb.addActionListener( icl );
        chk_line_insm.addActionListener( icl );
        chk_line_iu.addActionListener( icl );
        chk_line_id.addActionListener( icl );
        chk_line_isu.addActionListener( icl );
        chk_line_isd.addActionListener( icl );

        bindGroup( "USL", lbl_line_usl, cbo_line_usl, chk_line_usl, state_line_usl );
        bindGroup( "LSL", lbl_line_lsl, cbo_line_lsl, chk_line_lsl, state_line_lsl );
        bindGroup( "UDZ", lbl_line_udz, cbo_line_udz, chk_line_udz, state_line_udz );
        bindGroup( "LDZ", lbl_line_ldz, cbo_line_ldz, chk_line_ldz, state_line_ldz );
        bindGroup( "INS", lbl_line_ins, cbo_line_ins, chk_line_ins, state_line_ins );
        bindGroup( "INSUP", lbl_line_insup, cbo_line_insup, chk_line_insup, state_line_insup );
        bindGroup( "INSDOWN", lbl_line_insdown, cbo_line_insdown, chk_line_insdown, state_line_insdown );
        bindGroup( "DRVOK", lbl_line_drvok, cbo_line_drvok, chk_line_drvok, state_line_drvok );
        bindGroup( "DRVBM", lbl_line_drvbm, cbo_line_drvbm, chk_line_drvbm, state_line_drvbm );
        bindGroup( "DBDE", lbl_line_dbde, cbo_line_dbde, chk_line_dbde, state_line_dbde );
        bindGroup( "DBDF", lbl_line_dbdf, cbo_line_dbdf, chk_line_dbdf, state_line_dbdf );
        bindGroup( "BS1", lbl_line_bs1, cbo_line_bs1, chk_line_bs1, state_line_bs1 );
        bindGroup( "BS2", lbl_line_bs2, cbo_line_bs2, chk_line_bs2, state_line_bs2 );
        bindGroup( "ST1", lbl_line_st1, cbo_line_st1, chk_line_st1, state_line_st1 );
        bindGroup( "ST2", lbl_line_st2, cbo_line_st2, chk_line_st2, state_line_st2 );
        bindGroup( "ST3", lbl_line_st3, cbo_line_st3, chk_line_st3, state_line_st3 );
        bindGroup( "ST4", lbl_line_st4, cbo_line_st4, chk_line_st4, state_line_st4 );
        bindGroup( "ST5", lbl_line_st5, cbo_line_st5, chk_line_st5, state_line_st5 );
        bindGroup( "ST6", lbl_line_st6, cbo_line_st6, chk_line_st6, state_line_st6 );
        bindGroup( "LVCFB", lbl_line_lvcfb, cbo_line_lvcfb, chk_line_lvcfb, state_line_lvcfb );
        bindGroup( "LVC1FB", lbl_line_lvc1fb, cbo_line_lvc1fb, chk_line_lvc1fb, state_line_lvc1fb );
        bindGroup( "THM", lbl_line_thm, cbo_line_thm, chk_line_thm, state_line_thm );
        bindGroup( "ENCF", lbl_line_encf, cbo_line_encf, chk_line_encf, state_line_encf );
        bindGroup( "URL", lbl_line_url, cbo_line_url, chk_line_url, state_line_url );
        bindGroup( "LRL", lbl_line_lrl, cbo_line_lrl, chk_line_lrl, state_line_lrl );
        bindGroup( "EPB", lbl_line_epb, cbo_line_epb, chk_line_epb, state_line_epb );
        bindGroup( "UCMTS", lbl_line_ucmts, cbo_line_ucmts, chk_line_ucmts, state_line_ucmts );
        bindGroup( "UCMTS2", lbl_line_ucmts2, cbo_line_ucmts2, chk_line_ucmts2, state_line_ucmts2 );
        bindGroup( "UCMTS3", lbl_line_ucmts3, cbo_line_ucmts3, chk_line_ucmts3, state_line_ucmts3 );
        bindGroup( "DLB", lbl_line_dlb, cbo_line_dlb, chk_line_dlb, state_line_dlb );
        bindGroup( "INSM", lbl_line_insm, cbo_line_insm, chk_line_insm, state_line_insm );
        bindGroup( "IU", lbl_line_iu, cbo_line_iu, chk_line_iu, state_line_iu );
        bindGroup( "ID", lbl_line_id, cbo_line_id, chk_line_id, state_line_id );
        bindGroup( "ISU", lbl_line_isu, cbo_line_isu, chk_line_isu, state_line_isu );
        bindGroup( "ISD", lbl_line_isd, cbo_line_isd, chk_line_isd, state_line_isd );
        
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_main_inputs.setText( getBundleText( "LBL_cpt_main_inputs", "Main Inputs" ) );

        /**
         * Generated by Groovy script:
         *
         * for(int i=0; i<list.size(); i++) { println "lbl_line_${list[i].toLowerCase()}.setText(\"${list[i]}\");" }
         */
        lbl_title1.setText( getBundleText( "LBL_lbl_title1", "Input" ) );
        lbl_title2.setText( getBundleText( "LBL_lbl_title2", "Source" ) );
        lbl_title3.setText( getBundleText( "LBL_lbl_title3", "Invert" ) );
        lbl_title4.setText( getBundleText( "LBL_lbl_title4", "Status" ) );
        lbl_line_usl.setText( "USL" );
        lbl_line_lsl.setText( "LSL" );
        lbl_line_udz.setText( "UDZ" );
        lbl_line_ldz.setText( "LDZ" );
        lbl_line_ins.setText( "INS" );
        lbl_line_insup.setText( "INSUP" );
        lbl_line_insdown.setText( "INSDOWN" );
        lbl_line_drvok.setText( "DRVOK" );
        lbl_line_drvbm.setText( "DRVBM" );
        lbl_line_dbde.setText( "DBDE" );
        lbl_line_dbdf.setText( "DBDF" );
        lbl_line_bs1.setText( "BS1" );
        lbl_line_bs2.setText( "BS2" );
        lbl_line_st1.setText( "ST1" );
        lbl_line_st2.setText( "ST2" );
        lbl_line_st3.setText( "ST3" );
        lbl_line_st4.setText( "ST4" );
        lbl_line_st5.setText( "ST5" );
        lbl_line_st6.setText( "ST6" );
        lbl_line_lvcfb.setText( "LVCFB" );
        lbl_line_lvc1fb.setText( "LVC1FB" );
        lbl_line_thm.setText( "THM" );
        lbl_line_encf.setText( "ENCF" );
        lbl_line_url.setText( "URL" );
        lbl_line_lrl.setText( "LRL" );
        lbl_line_epb.setText( "EPB" );
        lbl_line_ucmts.setText( "UCMTS1" );
        lbl_line_ucmts2.setText( "UCMTS2" );
        lbl_line_ucmts3.setText( "UCMTS3" );
        lbl_line_dlb.setText( "DLB" );
        lbl_line_insm.setText( "IM" );
        lbl_line_iu.setText( "IU" );
        lbl_line_id.setText( "ID" );
        lbl_line_isu.setText( "ISU" );
        lbl_line_isd.setText( "ISD" );
    }


    private void setCheckBoxStyle ( final MyCheckBox c ) {
        c.setOpaque( false );
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged ( ChangeEvent e ) {
                ButtonModel buttonModel = c.getModel();
                boolean     pressed     = buttonModel.isPressed();
                if ( started && pressed ) {
                    c.setOpaque( true );
                    c.setBackground( Color.decode( "#99CCFF" ) );
                }
            }
        } );
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_BOLD );
        c.setForeground(Color.WHITE);
    }


    private void setComboBoxValueStyle ( JComboBox<?> c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setLabelTitleStyle ( JLabel c ) {
        c.setFont( FontFactory.FONT_12_BOLD );
        c.setForeground(Color.WHITE);
    }


    private void setTitleStyle ( JLabel c ) {
        c.setFont( c.getFont().deriveFont( Font.BOLD, 12 ) );
        c.setForeground(Color.WHITE);
    }


    private void bindGroup ( final String detailKey, final JComponent... list ) {
        if ( list.length >= 2 ) {
            if ( list[ 0 ] instanceof JLabel ) {
                ( ( JLabel )list[ 0 ] ).setLabelFor( list[ 1 ] );
            } else if ( list[ 0 ] instanceof JCheckBox ) {
                ( ( JCheckBox )list[ 0 ] ).addItemListener( new ItemListener() {
                    public void itemStateChanged ( ItemEvent e ) {
                        for ( int i = 1 ; i < list.length ; i++ ) {
                            list[ i ].setEnabled( ( ( JCheckBox )list[ 0 ] ).isSelected() );
                        }
                    }
                } );
                for ( int i = 1 ; i < list.length ; i++ ) {
                    list[ i ].setEnabled( ( ( JCheckBox )list[ 0 ] ).isSelected() );
                }
            }
        }
        if ( TEXT != null ) {
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


    /* $beanDefine$ */
    /* $beanGetters$ */
    public String getBundleText ( String key, String defaultValue ) {
        String result;
        try {
            result = TEXT.getString( key );
        } catch ( Exception e ) {
            result = defaultValue;
        }
        return result;
    }


    public InputPinBean getInputPinBean () throws ConvertException {
        InputPinBean bean_inputPin = new InputPinBean();
        
        bean_inputPin.setUsl( ( InputPinD01 )cbo_line_usl.getSelectedItem() );
        bean_inputPin.setLsl( ( InputPinD01 )cbo_line_lsl.getSelectedItem() );
        bean_inputPin.setUdz( ( InputPinD01 )cbo_line_udz.getSelectedItem() );
        bean_inputPin.setLdz( ( InputPinD01 )cbo_line_ldz.getSelectedItem() );
        bean_inputPin.setIns( ( InputPinD01 )cbo_line_ins.getSelectedItem() );
        bean_inputPin.setInsUp( ( InputPinD01 )cbo_line_insup.getSelectedItem() );
        bean_inputPin.setInsDown( ( InputPinD01 )cbo_line_insdown.getSelectedItem() );
        bean_inputPin.setDrvOK( ( InputPinD01 )cbo_line_drvok.getSelectedItem() );
        bean_inputPin.setDrvBM( ( InputPinD01 )cbo_line_drvbm.getSelectedItem() );
        bean_inputPin.setEnable( ( InputPinD01 )cbo_line_dbde.getSelectedItem() );
        bean_inputPin.setDbdf( ( InputPinD01 )cbo_line_dbdf.getSelectedItem() );
        bean_inputPin.setBs1( ( InputPinD01 )cbo_line_bs1.getSelectedItem() );
        bean_inputPin.setBs2( ( InputPinD01 )cbo_line_bs2.getSelectedItem() );
        bean_inputPin.setSt1( ( InputPinD01 )cbo_line_st1.getSelectedItem() );
        bean_inputPin.setSt2( ( InputPinD01 )cbo_line_st2.getSelectedItem() );
        bean_inputPin.setSt3( ( InputPinD01 )cbo_line_st3.getSelectedItem() );
        bean_inputPin.setSt4( ( InputPinD01 )cbo_line_st4.getSelectedItem() );
        bean_inputPin.setSt5( ( InputPinD01 )cbo_line_st5.getSelectedItem() );
        bean_inputPin.setSt6( ( InputPinD01 )cbo_line_st6.getSelectedItem() );
        bean_inputPin.setLvcfb( ( InputPinD01 )cbo_line_lvcfb.getSelectedItem() );
        bean_inputPin.setLvc1fb( ( InputPinD01 )cbo_line_lvc1fb.getSelectedItem() );
        bean_inputPin.setThm( ( InputPinD01 )cbo_line_thm.getSelectedItem() );
        bean_inputPin.setEncf( ( InputPinD01 )cbo_line_encf.getSelectedItem() );
        bean_inputPin.setUrl( ( InputPinD01 )cbo_line_url.getSelectedItem() );
        bean_inputPin.setLrl( ( InputPinD01 )cbo_line_lrl.getSelectedItem() );
        bean_inputPin.setEpb( ( InputPinD01 )cbo_line_epb.getSelectedItem() );
        bean_inputPin.setUcmts( ( InputPinD01 )cbo_line_ucmts.getSelectedItem() );
        bean_inputPin.setUcmts2( ( InputPinD01 )cbo_line_ucmts2.getSelectedItem() );
        bean_inputPin.setUcmts3( ( InputPinD01 )cbo_line_ucmts3.getSelectedItem() );
        bean_inputPin.setDlb( ( InputPinD01 )cbo_line_dlb.getSelectedItem() );
        bean_inputPin.setIm( ( InputPinD01 )cbo_line_insm.getSelectedItem() );
        bean_inputPin.setIu( ( InputPinD01 )cbo_line_iu.getSelectedItem() );
        bean_inputPin.setId( ( InputPinD01 )cbo_line_id.getSelectedItem() );
        bean_inputPin.setIsu( ( InputPinD01 )cbo_line_isu.getSelectedItem() );
        bean_inputPin.setIsd( ( InputPinD01 )cbo_line_isd.getSelectedItem() );
        
        bean_inputPin.setUslInverted( chk_line_usl.isSelected() );
        bean_inputPin.setLslInverted( chk_line_lsl.isSelected() );
        bean_inputPin.setUdzInverted( chk_line_udz.isSelected() );
        bean_inputPin.setLdzInverted( chk_line_ldz.isSelected() );
        bean_inputPin.setInsInverted( chk_line_ins.isSelected() );
        bean_inputPin.setInsUpInverted( chk_line_insup.isSelected() );
        bean_inputPin.setInsDownInverted( chk_line_insdown.isSelected() );
        bean_inputPin.setDrvOKInverted( chk_line_drvok.isSelected() );
        bean_inputPin.setDrvBMInverted( chk_line_drvbm.isSelected() );
        bean_inputPin.setEnableInverted( chk_line_dbde.isSelected() );
        bean_inputPin.setDbdfInverted( chk_line_dbdf.isSelected() );
        bean_inputPin.setBs1Inverted( chk_line_bs1.isSelected() );
        bean_inputPin.setBs2Inverted( chk_line_bs2.isSelected() );
        bean_inputPin.setSt1Inverted( chk_line_st1.isSelected() );
        bean_inputPin.setSt2Inverted( chk_line_st2.isSelected() );
        bean_inputPin.setSt3Inverted( chk_line_st3.isSelected() );
        bean_inputPin.setSt4Inverted( chk_line_st4.isSelected() );
        bean_inputPin.setSt5Inverted( chk_line_st5.isSelected() );
        bean_inputPin.setSt6Inverted( chk_line_st6.isSelected() );
        bean_inputPin.setLvcfbInverted( chk_line_lvcfb.isSelected() );
        bean_inputPin.setLvc1fbInverted( chk_line_lvc1fb.isSelected() );
        bean_inputPin.setThmInverted( chk_line_thm.isSelected() );
        bean_inputPin.setEncfInverted( chk_line_encf.isSelected() );
        bean_inputPin.setUrlInverted( chk_line_url.isSelected() );
        bean_inputPin.setLrlInverted( chk_line_lrl.isSelected() );
        bean_inputPin.setEpbInverted( chk_line_epb.isSelected() );
        bean_inputPin.setUcmtsInverted( chk_line_ucmts.isSelected() );
        bean_inputPin.setUcmts2Inverted( chk_line_ucmts2.isSelected() );
        bean_inputPin.setUcmts3Inverted( chk_line_ucmts3.isSelected() );
        bean_inputPin.setDlbInverted(chk_line_dlb.isSelected());
        bean_inputPin.setImInverted(chk_line_insm.isSelected());
        bean_inputPin.setIuInverted(chk_line_iu.isSelected());
        bean_inputPin.setIdInverted(chk_line_id.isSelected());
        bean_inputPin.setIsuInverted(chk_line_isu.isSelected());
        bean_inputPin.setIsdInverted(chk_line_isd.isSelected());
        
        return bean_inputPin;
    }


    public void setInputPinBean ( InputPinBean bean_inputPin ) {
        final InputPinD01[] items = InputPinD01.allDisplayItem(bean_inputPin.firmwareVersion);
        cbo_line_usl.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_lsl.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_udz.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_ldz.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_ins.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_insup.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_insdown.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_drvok.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_drvbm.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_dbde.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_dbdf.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_bs1.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_bs2.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_st1.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_st2.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_st3.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_st4.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_st5.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_st6.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_lvcfb.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_lvc1fb.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_thm.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_encf.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_url.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_lrl.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_epb.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_ucmts.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_ucmts2.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_ucmts3.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_dlb.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_insm.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_iu.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_id.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_isu.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_isd.setModel( new DefaultComboBoxModel<>( items ) );
        
        List<InputPinD01> list = Arrays.asList( items );
        
        this.cbo_line_usl.setSelectedItem( list.contains( bean_inputPin.getUsl() ) ? bean_inputPin.getUsl() : InputPinD01.Always_0 );
        this.cbo_line_lsl.setSelectedItem( list.contains( bean_inputPin.getLsl() ) ? bean_inputPin.getLsl() : InputPinD01.Always_0 );
        this.cbo_line_udz.setSelectedItem( list.contains( bean_inputPin.getUdz() ) ? bean_inputPin.getUdz() : InputPinD01.Always_0 );
        this.cbo_line_ldz.setSelectedItem( list.contains( bean_inputPin.getLdz() ) ? bean_inputPin.getLdz() : InputPinD01.Always_0 );
        this.cbo_line_ins.setSelectedItem( list.contains( bean_inputPin.getIns() ) ? bean_inputPin.getIns() : InputPinD01.Always_0 );
        this.cbo_line_insup.setSelectedItem( list.contains( bean_inputPin.getInsUp() ) ? bean_inputPin.getInsUp() : InputPinD01.Always_0 );
        this.cbo_line_insdown.setSelectedItem( list.contains( bean_inputPin.getInsDown() ) ? bean_inputPin.getInsDown() : InputPinD01.Always_0 );
        this.cbo_line_drvok.setSelectedItem( list.contains( bean_inputPin.getDrvOK() ) ? bean_inputPin.getDrvOK() : InputPinD01.Always_0 );
        this.cbo_line_drvbm.setSelectedItem( list.contains( bean_inputPin.getDrvBM() ) ? bean_inputPin.getDrvBM() : InputPinD01.Always_0 );
        this.cbo_line_dbde.setSelectedItem( list.contains( bean_inputPin.getEnable() ) ? bean_inputPin.getEnable() : InputPinD01.Always_0 );
        this.cbo_line_dbdf.setSelectedItem( list.contains( bean_inputPin.getDbdf() ) ? bean_inputPin.getDbdf() : InputPinD01.Always_0 );
        this.cbo_line_bs1.setSelectedItem( list.contains( bean_inputPin.getBs1() ) ? bean_inputPin.getBs1() : InputPinD01.Always_0 );
        this.cbo_line_bs2.setSelectedItem( list.contains( bean_inputPin.getBs2() ) ? bean_inputPin.getBs2() : InputPinD01.Always_0 );
        this.cbo_line_st1.setSelectedItem( list.contains( bean_inputPin.getSt1() ) ? bean_inputPin.getSt1() : InputPinD01.Always_0 );
        this.cbo_line_st2.setSelectedItem( list.contains( bean_inputPin.getSt2() ) ? bean_inputPin.getSt2() : InputPinD01.Always_0 );
        this.cbo_line_st3.setSelectedItem( list.contains( bean_inputPin.getSt3() ) ? bean_inputPin.getSt3() : InputPinD01.Always_0 );
        this.cbo_line_st4.setSelectedItem( list.contains( bean_inputPin.getSt4() ) ? bean_inputPin.getSt4() : InputPinD01.Always_0 );
        this.cbo_line_st5.setSelectedItem( list.contains( bean_inputPin.getSt5() ) ? bean_inputPin.getSt5() : InputPinD01.Always_0 );
        this.cbo_line_st6.setSelectedItem( list.contains( bean_inputPin.getSt6() ) ? bean_inputPin.getSt6() : InputPinD01.Always_0 );
        this.cbo_line_lvcfb.setSelectedItem( list.contains( bean_inputPin.getLvcfb() ) ? bean_inputPin.getLvcfb() : InputPinD01.Always_0 );
        this.cbo_line_lvc1fb.setSelectedItem( list.contains( bean_inputPin.getLvc1fb() ) ? bean_inputPin.getLvc1fb() : InputPinD01.Always_0 );
        this.cbo_line_thm.setSelectedItem( list.contains( bean_inputPin.getThm() ) ? bean_inputPin.getThm() : InputPinD01.Always_0 );
        this.cbo_line_encf.setSelectedItem( list.contains( bean_inputPin.getEncf() ) ? bean_inputPin.getEncf() : InputPinD01.Always_0 );
        this.cbo_line_url.setSelectedItem( list.contains( bean_inputPin.getUrl() ) ? bean_inputPin.getUrl() : InputPinD01.Always_0 );
        this.cbo_line_lrl.setSelectedItem( list.contains( bean_inputPin.getLrl() ) ? bean_inputPin.getLrl() : InputPinD01.Always_0 );
        this.cbo_line_epb.setSelectedItem( list.contains( bean_inputPin.getEpb() ) ? bean_inputPin.getEpb() : InputPinD01.Always_0 );
        this.cbo_line_ucmts.setSelectedItem( list.contains( bean_inputPin.getUcmts() ) ? bean_inputPin.getUcmts() : InputPinD01.Always_0 );
        this.cbo_line_ucmts2.setSelectedItem( list.contains( bean_inputPin.getUcmts2() ) ? bean_inputPin.getUcmts2() : InputPinD01.Always_0 );
        this.cbo_line_ucmts3.setSelectedItem( list.contains( bean_inputPin.getUcmts3() ) ? bean_inputPin.getUcmts3() : InputPinD01.Always_0 );
        this.cbo_line_dlb.setSelectedItem( list.contains( bean_inputPin.getDlb() ) ? bean_inputPin.getDlb() : InputPinD01.Always_0 );
        this.cbo_line_insm.setSelectedItem( list.contains( bean_inputPin.getIm() ) ? bean_inputPin.getIm() : InputPinD01.Always_0 );
        this.cbo_line_iu.setSelectedItem( list.contains( bean_inputPin.getIu() ) ? bean_inputPin.getIu() : InputPinD01.Always_0 );
        this.cbo_line_id.setSelectedItem( list.contains( bean_inputPin.getId() ) ? bean_inputPin.getId() : InputPinD01.Always_0 );
        this.cbo_line_isu.setSelectedItem( list.contains( bean_inputPin.getIsu() ) ? bean_inputPin.getIsu() : InputPinD01.Always_0 );
        this.cbo_line_isd.setSelectedItem( list.contains( bean_inputPin.getIsd() ) ? bean_inputPin.getIsd() : InputPinD01.Always_0 );
        
        this.chk_line_usl.setSelected( bean_inputPin.isUslInverted() );
        this.chk_line_lsl.setSelected( bean_inputPin.isLslInverted() );
        this.chk_line_udz.setSelected( bean_inputPin.isUdzInverted() );
        this.chk_line_ldz.setSelected( bean_inputPin.isLdzInverted() );
        this.chk_line_ins.setSelected( bean_inputPin.isInsInverted() );
        this.chk_line_insup.setSelected( bean_inputPin.isInsUpInverted() );
        this.chk_line_insdown.setSelected( bean_inputPin.isInsDownInverted() );
        this.chk_line_drvok.setSelected( bean_inputPin.isDrvOKInverted() );
        this.chk_line_drvbm.setSelected( bean_inputPin.isDrvBMInverted() );
        this.chk_line_dbde.setSelected( bean_inputPin.isEnableInverted() );
        this.chk_line_dbdf.setSelected( bean_inputPin.isDbdfInverted() );
        this.chk_line_bs1.setSelected( bean_inputPin.isBs1Inverted() );
        this.chk_line_bs2.setSelected( bean_inputPin.isBs2Inverted() );
        this.chk_line_st1.setSelected( bean_inputPin.isSt1Inverted() );
        this.chk_line_st2.setSelected( bean_inputPin.isSt2Inverted() );
        this.chk_line_st3.setSelected( bean_inputPin.isSt3Inverted() );
        this.chk_line_st4.setSelected( bean_inputPin.isSt4Inverted() );
        this.chk_line_st5.setSelected( bean_inputPin.isSt5Inverted() );
        this.chk_line_st6.setSelected( bean_inputPin.isSt6Inverted() );
        this.chk_line_lvcfb.setSelected( bean_inputPin.isLvcfbInverted() );
        this.chk_line_lvc1fb.setSelected( bean_inputPin.isLvc1fbInverted() );
        this.chk_line_thm.setSelected( bean_inputPin.isThmInverted() );
        this.chk_line_encf.setSelected( bean_inputPin.isEncfInverted() );
        this.chk_line_url.setSelected( bean_inputPin.isUrlInverted() );
        this.chk_line_lrl.setSelected( bean_inputPin.isLrlInverted() );
        this.chk_line_epb.setSelected( bean_inputPin.isEpbInverted() );
        this.chk_line_ucmts.setSelected( bean_inputPin.isUcmtsInverted() );
        this.chk_line_ucmts2.setSelected( bean_inputPin.isUcmts2Inverted() );
        this.chk_line_ucmts3.setSelected( bean_inputPin.isUcmts3Inverted() );
        this.chk_line_dlb.setSelected( bean_inputPin.isDlbInverted() );
        this.chk_line_insm.setSelected( bean_inputPin.isImInverted() );
        this.chk_line_iu.setSelected( bean_inputPin.isIuInverted() );
        this.chk_line_id.setSelected( bean_inputPin.isIdInverted() );
        this.chk_line_isu.setSelected( bean_inputPin.isIsuInverted() );
        this.chk_line_isd.setSelected( bean_inputPin.isIsdInverted() );
    }


    public static InputsD01 createPanel ( SettingPanel<InputsD01> panel ) {
        InputsD01 gui = new InputsD01();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static void main ( String... arg ) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.getContentPane().add( new InputsD01() );
        f.pack();
        f.setVisible( true );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    private ImageIcon toIcon ( Boolean state ) {
        if ( Boolean.TRUE.equals( state ) )
            return INPUT_ON_ICON;
        else if ( Boolean.FALSE.equals( state ) )
            return INPUT_OFF_ICON;
        return GRAY_ICON;
    }


    public void setInputPinState ( HashMap<InputPinD01, Boolean> inputPinState ) {
        this.inputPinState = inputPinState;
        updateInputPinState();
    }


    private static boolean logicalXOR ( boolean x, boolean y ) {
        return ( ( x || y ) && ! ( x && y ) );
    }


    private void updateInputPinState () {
        InputPinBean bean = null;
        if ( inputPinState != null )
            try {
                bean = this.getInputPinBean();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        if ( bean != null ) {
        	
            state_line_usl.setIcon( toIcon(bean != null && bean.getUsl() == null
                    						? null
											: logicalXOR( chk_line_usl.isSelected(), inputPinState.get( bean.getUsl()) ) ) );
            
            state_line_lsl.setIcon( toIcon( bean != null && bean.getLsl() == null
                                            ? null
                                            : logicalXOR( chk_line_lsl.isSelected(), inputPinState.get( bean.getLsl() ) ) ) );
            state_line_udz.setIcon( toIcon( bean != null && bean.getUdz() == null
                                            ? null
                                            : logicalXOR( chk_line_udz.isSelected(), inputPinState.get( bean.getUdz() ) ) ) );
            state_line_ldz.setIcon( toIcon( bean != null && bean.getLdz() == null
                                            ? null
                                            : logicalXOR( chk_line_ldz.isSelected(), inputPinState.get( bean.getLdz() ) ) ) );
            state_line_ins.setIcon( toIcon( bean != null && bean.getIns() == null
                                            ? null
                                            : logicalXOR( chk_line_ins.isSelected(), inputPinState.get( bean.getIns() ) ) ) );
            state_line_insup.setIcon( toIcon( bean != null && bean.getInsUp() == null
	                                          ? null
	                                          : logicalXOR( chk_line_insup.isSelected(), inputPinState.get( bean.getInsUp() ) ) ) );
            state_line_insdown.setIcon( toIcon( bean != null && bean.getInsDown() == null
                                                ? null
                                                : logicalXOR( chk_line_insdown.isSelected(), inputPinState.get( bean.getInsDown() ) ) ) );
            state_line_drvok.setIcon( toIcon( bean != null && bean.getDrvOK() == null
                                              ? null
                                              : logicalXOR( chk_line_drvok.isSelected(), inputPinState.get( bean.getDrvOK() ) ) ) );
            state_line_drvbm.setIcon( toIcon( bean != null && bean.getDrvBM() == null
                                              ? null
                                              : logicalXOR( chk_line_drvbm.isSelected(), inputPinState.get( bean.getDrvBM() ) ) ) );
            state_line_dbde.setIcon( toIcon( bean != null && bean.getEnable() == null
                                               ? null
                                               : logicalXOR( chk_line_dbde.isSelected(), inputPinState.get( bean.getEnable() ) ) ) );
            state_line_dbdf.setIcon( toIcon( bean != null && bean.getDbdf() == null
                                             ? null
                                             : logicalXOR( chk_line_dbdf.isSelected(), inputPinState.get( bean.getDbdf() ) ) ) );
            state_line_bs1.setIcon( toIcon( bean != null && bean.getBs1() == null
                                           ? null
                                           : logicalXOR( chk_line_bs1.isSelected(), inputPinState.get( bean.getBs1() ) ) ) );
            state_line_bs2.setIcon( toIcon( bean != null && bean.getBs2() == null
                    						? null
                    						: logicalXOR( chk_line_bs2.isSelected(), inputPinState.get( bean.getBs2() ) ) ) );
            state_line_st1.setIcon( toIcon( bean != null && bean.getSt1() == null
            								? null
            								: logicalXOR( chk_line_st1.isSelected(), inputPinState.get( bean.getSt1() ) ) ) );
            state_line_st2.setIcon( toIcon( bean != null && bean.getSt2() == null
                                            ? null
                                            : logicalXOR( chk_line_st2.isSelected(), inputPinState.get( bean.getSt2() ) ) ) );
            state_line_st3.setIcon( toIcon( bean != null && bean.getSt3() == null
						                    ? null
						                    : logicalXOR( chk_line_st3.isSelected(), inputPinState.get( bean.getSt3() ) ) ) );
            state_line_st4.setIcon( toIcon( bean != null && bean.getSt4() == null
						                    ? null
						                    : logicalXOR( chk_line_st4.isSelected(), inputPinState.get( bean.getSt4() ) ) ) );
            state_line_st5.setIcon( toIcon( bean != null && bean.getSt5() == null
						                    ? null
						                    : logicalXOR( chk_line_st5.isSelected(), inputPinState.get( bean.getSt5() ) ) ) );
            state_line_st6.setIcon( toIcon( bean != null && bean.getSt6() == null
						                    ? null
						                    : logicalXOR( chk_line_st6.isSelected(), inputPinState.get( bean.getSt6() ) ) ) );
            state_line_lvcfb.setIcon( toIcon( bean != null && bean.getLvcfb() == null
										    ? null
										    : logicalXOR( chk_line_lvcfb.isSelected(), inputPinState.get( bean.getLvcfb() ) ) ) );
            state_line_lvc1fb.setIcon( toIcon( bean != null && bean.getLvc1fb() == null
										    ? null
										    : logicalXOR( chk_line_lvc1fb.isSelected(), inputPinState.get( bean.getLvc1fb() ) ) ) );
            state_line_thm.setIcon( toIcon( bean != null && bean.getThm() == null
                                            ? null
                                            : logicalXOR( chk_line_thm.isSelected(), inputPinState.get( bean.getThm() ) ) ) );
            state_line_encf.setIcon( toIcon( bean != null && bean.getEncf() == null
                                             ? null
                                             : logicalXOR( chk_line_encf.isSelected(), inputPinState.get( bean.getEncf() ) ) ) );
            state_line_url.setIcon( toIcon( bean != null && bean.getUrl() == null
						                     ? null
						                     : logicalXOR( chk_line_url.isSelected(), inputPinState.get( bean.getUrl() ) ) ) );
            state_line_lrl.setIcon( toIcon( bean != null && bean.getLrl() == null
						                     ? null
						                     : logicalXOR( chk_line_lrl.isSelected(), inputPinState.get( bean.getLrl() ) ) ) );
            state_line_epb.setIcon( toIcon( bean != null && bean.getEpb() == null
						                    ? null
						                    : logicalXOR( chk_line_epb.isSelected(), inputPinState.get( bean.getEpb() ) ) ) );
            state_line_ucmts.setIcon( toIcon( bean != null && bean.getUcmts() == null
						                    ? null
						                    : logicalXOR( chk_line_ucmts.isSelected(), inputPinState.get( bean.getUcmts() ) ) ) );
            state_line_ucmts2.setIcon( toIcon( bean != null && bean.getUcmts2() == null
						                    ? null
						                    : logicalXOR( chk_line_ucmts2.isSelected(), inputPinState.get( bean.getUcmts2() ) ) ) );
            state_line_ucmts3.setIcon( toIcon( bean != null && bean.getUcmts3() == null
						                    ? null
						                    : logicalXOR( chk_line_ucmts3.isSelected(), inputPinState.get( bean.getUcmts3() ) ) ) );
            state_line_dlb.setIcon( toIcon( bean != null && bean.getDlb() == null
						                    ? null
						                    : logicalXOR( chk_line_dlb.isSelected(), inputPinState.get( bean.getDlb() ) ) ) );
            state_line_insm.setIcon( toIcon( bean != null && bean.getIm() == null
						                    ? null
						                    : logicalXOR( chk_line_insm.isSelected(), inputPinState.get( bean.getIm() ) ) ) );
            state_line_iu.setIcon( toIcon( bean != null && bean.getIu() == null
						                    ? null
						                    : logicalXOR( chk_line_iu.isSelected(), inputPinState.get( bean.getIu() ) ) ) );
            state_line_id.setIcon( toIcon( bean != null && bean.getId() == null
						                    ? null
						                    : logicalXOR( chk_line_id.isSelected(), inputPinState.get( bean.getId() ) ) ) );
            state_line_isu.setIcon( toIcon( bean != null && bean.getIsu() == null
						                    ? null
						                    : logicalXOR( chk_line_isu.isSelected(), inputPinState.get( bean.getIsu() ) ) ) );
            state_line_isd.setIcon( toIcon( bean != null && bean.getIsd() == null
						                    ? null
						                    : logicalXOR( chk_line_isd.isSelected(), inputPinState.get( bean.getIsd() ) ) ) );
        }
    }


    public static class InputPinBean {
        private String firmwareVersion;
        private InputPinD01 usl;
        private InputPinD01 lsl;
        private InputPinD01 udz;
        private InputPinD01 ldz;
        private InputPinD01 ins;
        private InputPinD01 insUp;
        private InputPinD01 insDown;
        private InputPinD01 drvOK;
        private InputPinD01 drvBM;
        private InputPinD01 enable;
        private InputPinD01 dbdf;
        private InputPinD01 bs1;
        private InputPinD01 bs2;
        private InputPinD01 st1;
        private InputPinD01 st2;
        private InputPinD01 st3;
        private InputPinD01 st4;
        private InputPinD01 st5;
        private InputPinD01 st6;
        private InputPinD01 lvcfb;
        private InputPinD01 lvc1fb;
        private InputPinD01 thm;
        private InputPinD01 encf;
        private InputPinD01 url;
        private InputPinD01 lrl;
        private InputPinD01 epb;
        private InputPinD01 ucmts;
        private InputPinD01 ucmts2;
        private InputPinD01 ucmts3;
        private InputPinD01 dlb;
        private InputPinD01 im;
        private InputPinD01 iu;
        private InputPinD01 id;
        private InputPinD01 isu;
        private InputPinD01 isd;
        
        private boolean     uslInverted;
        private boolean     lslInverted;
        private boolean     udzInverted;
        private boolean     ldzInverted;
        private boolean     insInverted;
        private boolean     insUpInverted;
        private boolean     insDownInverted;
        private boolean     drvOKInverted;
        private boolean     drvBMInverted;
        private boolean     enableInverted;
        private boolean     dbdfInverted;
        private boolean     bs1Inverted;
        private boolean     bs2Inverted;
        private boolean     st1Inverted;
        private boolean     st2Inverted;
        private boolean     st3Inverted;
        private boolean     st4Inverted;
        private boolean     st5Inverted;
        private boolean     st6Inverted;
        private boolean     lvcfbInverted;
        private boolean     lvc1fbInverted;
        private boolean     thmInverted;
        private boolean     encfInverted;
        private boolean     urlInverted;
        private boolean     lrlInverted;
        private boolean     epbInverted;
        private boolean     ucmtsInverted;
        private boolean     ucmts2Inverted;
        private boolean     ucmts3Inverted;
        private boolean     dlbInverted;
        private boolean     imInverted;
        private boolean     iuInverted;
        private boolean     idInverted;
        private boolean     isuInverted;
        private boolean    	isdInverted;
        

        public final String getFirmwareVersion() {
            return firmwareVersion;
        }


        public final void setFirmwareVersion(String firmwareVersion) {
            this.firmwareVersion = firmwareVersion;
        }


        public boolean isUslInverted () {
            return uslInverted;
        }


        public boolean isLslInverted () {
            return lslInverted;
        }


        public boolean isUdzInverted () {
            return udzInverted;
        }


        public boolean isLdzInverted () {
            return ldzInverted;
        }


        public boolean isInsInverted () {
            return insInverted;
        }


        public boolean isInsUpInverted () {
            return insUpInverted;
        }


        public boolean isInsDownInverted () {
            return insDownInverted;
        }


        public boolean isDrvOKInverted () {
            return drvOKInverted;
        }


        public boolean isDrvBMInverted () {
            return drvBMInverted;
        }


        public boolean isEnableInverted () {
            return enableInverted;
        }


        public boolean isDbdfInverted () {
            return dbdfInverted;
        }


        public boolean isBs1Inverted () {
            return bs1Inverted;
        }
        
        public boolean isBs2Inverted () {
            return bs2Inverted;
        }


        public boolean isSt1Inverted () {
        	return st1Inverted;
        }

        
        public boolean isSt2Inverted () {
            return st2Inverted;
        }
        

        public boolean isSt3Inverted() {
			return st3Inverted;
		}

        
		public boolean isSt4Inverted() {
			return st4Inverted;
		}

		
		public boolean isSt5Inverted() {
			return st5Inverted;
		}


		public boolean isSt6Inverted() {
			return st6Inverted;
		}


		public boolean isLvcfbInverted () {
            return lvcfbInverted;
        }
		

        public boolean isLvc1fbInverted() {
			return lvc1fbInverted;
		}


		public boolean isThmInverted () {
            return thmInverted;
        }


        public boolean isEncfInverted () {
            return encfInverted;
        }


        public final boolean isUrlInverted() {
            return urlInverted;
        }


        public final boolean isLrlInverted() {
            return lrlInverted;
        }
        
        public boolean isEpbInverted() {
			return epbInverted;
		}

		public boolean isUcmtsInverted() {
			return ucmtsInverted;
		}


		public boolean isUcmts2Inverted() {
			return ucmts2Inverted;
		}


		public boolean isUcmts3Inverted() {
			return ucmts3Inverted;
		}
		
		
		public boolean isDlbInverted() {
			return dlbInverted;
		}
		
		
		public boolean isImInverted() {
			return imInverted;
		}


		public boolean isIuInverted() {
			return iuInverted;
		}


		public boolean isIdInverted() {
			return idInverted;
		}


		public boolean isIsuInverted() {
			return isuInverted;
		}


		public boolean isIsdInverted() {
			return isdInverted;
		}


		//---------------------------------------------------------------------
		public void setUslInverted ( boolean uslInverted ) {
            this.uslInverted = uslInverted;
        }


        public void setLslInverted ( boolean lslInverted ) {
            this.lslInverted = lslInverted;
        }


        public void setUdzInverted ( boolean udzInverted ) {
            this.udzInverted = udzInverted;
        }


        public void setLdzInverted ( boolean ldzInverted ) {
            this.ldzInverted = ldzInverted;
        }


        public void setInsInverted ( boolean insInverted ) {
            this.insInverted = insInverted;
        }


        public void setInsUpInverted ( boolean insUpInverted ) {
            this.insUpInverted = insUpInverted;
        }


        public void setInsDownInverted ( boolean insDownInverted ) {
            this.insDownInverted = insDownInverted;
        }


        public void setDrvOKInverted ( boolean drvOKInverted ) {
            this.drvOKInverted = drvOKInverted;
        }


        public void setDrvBMInverted ( boolean drvBMInverted ) {
            this.drvBMInverted = drvBMInverted;
        }


        public void setEnableInverted ( boolean enableInverted ) {
            this.enableInverted = enableInverted;
        }


        public void setDbdfInverted ( boolean dbdfInverted ) {
            this.dbdfInverted = dbdfInverted;
        }


        public void setBs1Inverted ( boolean bsInverted ) {
            this.bs2Inverted = bsInverted;
        }
        
        public void setBs2Inverted ( boolean bsInverted ) {
            this.bs2Inverted = bsInverted;
        }

        public void setSt1Inverted ( boolean st1Inverted ) {
        	this.st1Inverted = st1Inverted;
        }

        public void setSt2Inverted ( boolean st2Inverted ) {
            this.st2Inverted = st2Inverted;
        }
        
        public void setSt3Inverted(boolean st3Inverted) {
			this.st3Inverted = st3Inverted;
		}

		public void setSt4Inverted(boolean st4Inverted) {
			this.st4Inverted = st4Inverted;
		}

		public void setSt5Inverted(boolean st5Inverted) {
			this.st5Inverted = st5Inverted;
		}

		public void setSt6Inverted(boolean st6Inverted) {
			this.st6Inverted = st6Inverted;
		}

		public void setLvcfbInverted ( boolean lvcfbInverted ) {
            this.lvcfbInverted = lvcfbInverted;
        }

        public void setLvc1fbInverted(boolean lvc1fbInverted) {
			this.lvc1fbInverted = lvc1fbInverted;
		}

		public void setThmInverted ( boolean thmInverted ) {
            this.thmInverted = thmInverted;
        }

        public void setEncfInverted ( boolean encfInverted ) {
            this.encfInverted = encfInverted;
        }
        
        public final void setUrlInverted(boolean urlInverted) {
            this.urlInverted = urlInverted;
        }

        public final void setLrlInverted(boolean lrlInverted) {
            this.lrlInverted = lrlInverted;
        }
        
        public void setEpbInverted(boolean epbInverted) {
			this.epbInverted = epbInverted;
		}

		public void setUcmtsInverted(boolean ucmtsInverted) {
			this.ucmtsInverted = ucmtsInverted;
		}

		public void setUcmts2Inverted(boolean ucmts2Inverted) {
			this.ucmts2Inverted = ucmts2Inverted;
		}

		public void setUcmts3Inverted(boolean ucmts3Inverted) {
			this.ucmts3Inverted = ucmts3Inverted;
		}
		
		public void setDlbInverted(boolean dlbInverted) {
			this.dlbInverted = dlbInverted;
		}
		
		public void setImInverted(boolean imInverted) {
			this.imInverted = imInverted;
		}

		public void setIuInverted(boolean iuInverted) {
			this.iuInverted = iuInverted;
		}

		public void setIdInverted(boolean idInverted) {
			this.idInverted = idInverted;
		}

		public void setIsuInverted(boolean isuInverted) {
			this.isuInverted = isuInverted;
		}

		public void setIsdInverted(boolean isdInverted) {
			this.isdInverted = isdInverted;
		}

		//---------------------------------------------------------------------
		public InputPinD01 getUsl () {
            return usl;
        }


        public InputPinD01 getLsl () {
            return lsl;
        }


        public InputPinD01 getUdz () {
            return udz;
        }


        public InputPinD01 getLdz () {
            return ldz;
        }


        public InputPinD01 getIns () {
            return ins;
        }


        public InputPinD01 getInsUp () {
            return insUp;
        }


        public InputPinD01 getInsDown () {
            return insDown;
        }


        public InputPinD01 getDrvOK () {
            return drvOK;
        }


        public InputPinD01 getDrvBM () {
            return drvBM;
        }


        public InputPinD01 getEnable () {
            return enable;
        }


        public InputPinD01 getDbdf () {
            return dbdf;
        }


        public InputPinD01 getBs1 () {
            return bs1;
        }
        
        public InputPinD01 getBs2 () {
            return bs2;
        }

        
        public InputPinD01 getSt1() {
			return st1;
		}


		public InputPinD01 getSt2() {
			return st2;
		}


		public InputPinD01 getSt3() {
			return st3;
		}


		public InputPinD01 getSt4() {
			return st4;
		}


		public InputPinD01 getSt5() {
			return st5;
		}


		public InputPinD01 getSt6() {
			return st6;
		}


		public InputPinD01 getLvcfb () {
            return lvcfb;
        }


        public InputPinD01 getLvc1fb() {
			return lvc1fb;
		}


		public InputPinD01 getThm () {
            return thm;
        }


        public InputPinD01 getEncf () {
            return encf;
        }


        public final InputPinD01 getUrl() {
            return url;
        }


        public final InputPinD01 getLrl() {
            return lrl;
        }
        
        
        public InputPinD01 getEpb() {
			return epb;
		}
        

		public InputPinD01 getUcmts() {
			return ucmts;
		}


		public InputPinD01 getUcmts2() {
			return ucmts2;
		}


		public InputPinD01 getUcmts3() {
			return ucmts3;
		}


		public InputPinD01 getDlb() {
			return dlb;
		}
		
		public InputPinD01 getIm() {
			return im;
		}


		public InputPinD01 getIu() {
			return iu;
		}


		public InputPinD01 getId() {
			return id;
		}


		public InputPinD01 getIsu() {
			return isu;
		}


		public InputPinD01 getIsd() {
			return isd;
		}


		public void setUsl ( InputPinD01 usl ) {
            this.usl = usl;
        }


        public void setLsl ( InputPinD01 lsl ) {
            this.lsl = lsl;
        }


        public void setUdz ( InputPinD01 udz ) {
            this.udz = udz;
        }


        public void setLdz ( InputPinD01 ldz ) {
            this.ldz = ldz;
        }


        public void setIns ( InputPinD01 ins ) {
            this.ins = ins;
        }


        public void setInsUp ( InputPinD01 insUp ) {
            this.insUp = insUp;
        }


        public void setInsDown ( InputPinD01 insDown ) {
            this.insDown = insDown;
        }


        public void setDrvOK ( InputPinD01 drvOK ) {
            this.drvOK = drvOK;
        }


        public void setDrvBM ( InputPinD01 drvBM ) {
            this.drvBM = drvBM;
        }


        public void setEnable ( InputPinD01 enable ) {
            this.enable = enable;
        }


        public void setDbdf ( InputPinD01 dbdf ) {
            this.dbdf = dbdf;
        }


        public void setBs1 ( InputPinD01 bs ) {
            this.bs1 = bs;
        }

        
        public void setBs2 ( InputPinD01 bs ) {
            this.bs2 = bs;
        }
        
        
        public void setSt1 ( InputPinD01 st1 ) {
        	this.st1 = st1;
        }
        
        
        public void setSt2 ( InputPinD01 st2 ) {
            this.st2 = st2;
        }

        
        public void setSt3(InputPinD01 st3) {
			this.st3 = st3;
		}


		public void setSt4(InputPinD01 st4) {
			this.st4 = st4;
		}


		public void setSt5(InputPinD01 st5) {
			this.st5 = st5;
		}


		public void setSt6(InputPinD01 st6) {
			this.st6 = st6;
		}


		public void setLvcfb ( InputPinD01 lvcfb ) {
            this.lvcfb = lvcfb;
        }

		
        public void setLvc1fb(InputPinD01 lvc1fb) {
			this.lvc1fb = lvc1fb;
		}


		public void setThm ( InputPinD01 thm ) {
            this.thm = thm;
        }


        public void setEncf ( InputPinD01 encf ) {
            this.encf = encf;
        }


        public final void setUrl(InputPinD01 url) {
            this.url = url;
        }


        public final void setLrl(InputPinD01 lrl) {
            this.lrl = lrl;
        }


		public void setEpb(InputPinD01 epb) {
			this.epb = epb;
		}

		public void setUcmts(InputPinD01 ucmts) {
			this.ucmts = ucmts;
		}

		public void setUcmts2(InputPinD01 ucmts2) {
			this.ucmts2 = ucmts2;
		}

		public void setUcmts3(InputPinD01 ucmts3) {
			this.ucmts3 = ucmts3;
		}

		public void setDlb(InputPinD01 dlb) {
			this.dlb = dlb;
		}

		public void setIm(InputPinD01 im) {
			this.im = im;
		}

		public void setIu(InputPinD01 iu) {
			this.iu = iu;
		}

		public void setId(InputPinD01 id) {
			this.id = id;
		}

		public void setIsu(InputPinD01 isu) {
			this.isu = isu;
		}

		public void setIsd(InputPinD01 isd) {
			this.isd = isd;
		}
		
    }




    public class ItemChangedListener implements ActionListener, ItemListener {
        public void itemStateChanged ( ItemEvent e ) {
            if ( settingPanel instanceof InputsD01Setting ) {
                ( ( InputsD01Setting )settingPanel ).updateStatus();
            }
        }


        @Override
        public void actionPerformed ( ActionEvent e ) {
            if ( settingPanel instanceof InputsD01Setting ) {
                ( ( InputsD01Setting )settingPanel ).updateStatus();
            }
        }
    }
}
