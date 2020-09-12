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

import logic.io.crossbar.InputPinA07;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyCheckBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;




public class InputsA07 extends JPanel {
    private static final long      serialVersionUID = 6592660277537767456L;
    
    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.io.InputsA07" );

    private static final ImageIcon INPUT_ON_ICON    = ImageFactory.LIGHT_BRIGHT_GREEN.icon( 16, 16 );
    private static final ImageIcon INPUT_OFF_ICON   = ImageFactory.LIGHT_DARK_GREEN.icon( 16, 16 );
    private static final ImageIcon GRAY_ICON        = ImageFactory.LIGHT_BLACK.icon( 16, 16 );
    
    private boolean                 started = false;
    private SettingPanel<InputsA07> settingPanel;
    
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
    
    private JLabel                        lbl_line_drvenf;
    private MyComboBox			          cbo_line_drvenf;
    private MyCheckBox                    chk_line_drvenf;
    private JLabel                        state_line_drvenf;
    
    private JLabel                        lbl_line_enable;
    private MyComboBox				      cbo_line_enable;
    private MyCheckBox                    chk_line_enable;
    private JLabel                        state_line_enable;
    
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
    
    private JLabel                        lbl_line_dfc;
    private MyComboBox				      cbo_line_dfc;
    private MyCheckBox                    chk_line_dfc;
    private JLabel                        state_line_dfc;
    
    private JLabel                        lbl_line_dw;
    private MyComboBox				      cbo_line_dw;
    MyCheckBox                            chk_line_dw;
    JLabel                                state_line_dw;
    
    private JLabel                        lbl_line_hes;
    private MyComboBox			          cbo_line_hes;
    private MyCheckBox                    chk_line_hes;
    private JLabel                        state_line_hes;
    
    private JLabel                        lbl_line_ces;
    private MyComboBox				      cbo_line_ces;
    private MyCheckBox                    chk_line_ces;
    private JLabel                        state_line_ces;
    
    private JLabel                        lbl_line_lvcfb;
    private MyComboBox			          cbo_line_lvcfb;
    private MyCheckBox                    chk_line_lvcfb;
    private JLabel                        state_line_lvcfb;
    
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
    
    private HashMap<InputPinA07, Boolean> inputPinState;
    




    public InputsA07 () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<InputsA07> panel ) {
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
    	this.cbo_line_drvenf.setEnabled(Enable);
    	this.cbo_line_enable.setEnabled(Enable);
    	this.cbo_line_dbde.setEnabled(Enable);
    	this.cbo_line_dbdf.setEnabled(Enable);
    	this.cbo_line_bs1.setEnabled(Enable);
    	this.cbo_line_bs2.setEnabled(Enable);
    	this.cbo_line_dfc.setEnabled(Enable);
    	this.cbo_line_dw.setEnabled(Enable);
    	this.cbo_line_hes.setEnabled(Enable);
    	this.cbo_line_ces.setEnabled(Enable);
    	this.cbo_line_lvcfb.setEnabled(Enable);
    	this.cbo_line_thm.setEnabled(Enable);
    	this.cbo_line_encf.setEnabled(Enable);
    	this.cbo_line_url.setEnabled(Enable);
    	this.cbo_line_lrl.setEnabled(Enable);
    	this.cbo_line_epb.setEnabled(Enable);
    	this.cbo_line_ucmts.setEnabled(Enable);
    	this.cbo_line_ucmts2.setEnabled(Enable);
    	this.cbo_line_ucmts3.setEnabled(Enable);
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
         * "cbo_line_${c.toLowerCase()} = new JComboBox<InputSource>(InputSource.allDisplayItem());"; println "chk_line_${c.toLowerCase()} = new JCheckBox();"
         * println "state_line_${c.toLowerCase()} = new JLabel();"; println "" }
         */
        lbl_line_usl = new JLabel();
        cbo_line_usl = new MyComboBox();
        cbo_line_usl.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_usl   = new MyCheckBox();
        state_line_usl = new JLabel( GRAY_ICON );
        lbl_line_lsl   = new JLabel();
        cbo_line_lsl   = new MyComboBox();
        cbo_line_lsl.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_lsl   = new MyCheckBox();
        state_line_lsl = new JLabel( GRAY_ICON );
        lbl_line_udz   = new JLabel();
        cbo_line_udz   = new MyComboBox();
        cbo_line_udz.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_udz   = new MyCheckBox();
        state_line_udz = new JLabel( GRAY_ICON );
        lbl_line_ldz   = new JLabel();
        cbo_line_ldz   = new MyComboBox();
        cbo_line_ldz.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_ldz   = new MyCheckBox();
        state_line_ldz = new JLabel( GRAY_ICON );
        lbl_line_ins   = new JLabel();
        cbo_line_ins   = new MyComboBox();
        cbo_line_ins.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_ins   = new MyCheckBox();
        state_line_ins = new JLabel( GRAY_ICON );
        lbl_line_insup = new JLabel();
        cbo_line_insup = new MyComboBox();
        cbo_line_insup.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_insup   = new MyCheckBox();
        state_line_insup = new JLabel( GRAY_ICON );
        lbl_line_insdown = new JLabel();
        cbo_line_insdown = new MyComboBox();
        cbo_line_insdown.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_insdown   = new MyCheckBox();
        state_line_insdown = new JLabel( GRAY_ICON );
        lbl_line_drvok     = new JLabel();
        cbo_line_drvok     = new MyComboBox();
        cbo_line_drvok.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_drvok   = new MyCheckBox();
        state_line_drvok = new JLabel( GRAY_ICON );
        lbl_line_drvbm   = new JLabel();
        cbo_line_drvbm   = new MyComboBox();
        cbo_line_drvbm.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_drvbm   = new MyCheckBox();
        state_line_drvbm = new JLabel( GRAY_ICON );
        lbl_line_drvenf  = new JLabel();
        cbo_line_drvenf  = new MyComboBox();
        cbo_line_drvenf.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_drvenf   = new MyCheckBox();
        state_line_drvenf = new JLabel( GRAY_ICON );
        lbl_line_enable   = new JLabel();
        cbo_line_enable   = new MyComboBox();
        cbo_line_enable.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_enable   = new MyCheckBox();
        state_line_enable = new JLabel( GRAY_ICON );
        lbl_line_dbde     = new JLabel();
        cbo_line_dbde     = new MyComboBox();
        cbo_line_dbde.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_dbde   = new MyCheckBox();
        state_line_dbde = new JLabel( GRAY_ICON );
        lbl_line_dbdf   = new JLabel();
        cbo_line_dbdf   = new MyComboBox();
        cbo_line_dbdf.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_dbdf   = new MyCheckBox();
        state_line_dbdf = new JLabel( GRAY_ICON );
        lbl_line_bs1     = new JLabel();
        cbo_line_bs1     = new MyComboBox();     
        cbo_line_bs1.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_bs1   = new MyCheckBox();
        state_line_bs1 = new JLabel( GRAY_ICON );
        lbl_line_bs2     = new JLabel();
        cbo_line_bs2     = new MyComboBox();
        cbo_line_bs2.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_bs2   = new MyCheckBox();
        state_line_bs2 = new JLabel( GRAY_ICON );
        lbl_line_dfc  = new JLabel();
        cbo_line_dfc  = new MyComboBox();
        cbo_line_dfc.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_dfc   = new MyCheckBox();
        state_line_dfc = new JLabel( GRAY_ICON );
        lbl_line_dw    = new JLabel();
        cbo_line_dw    = new MyComboBox();
        cbo_line_dw.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_dw   = new MyCheckBox();
        state_line_dw = new JLabel( GRAY_ICON );
        lbl_line_hes  = new JLabel();
        cbo_line_hes  = new MyComboBox();
        cbo_line_hes.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_hes   = new MyCheckBox();
        state_line_hes = new JLabel( GRAY_ICON );
        lbl_line_ces   = new JLabel();
        cbo_line_ces   = new MyComboBox();
        cbo_line_ces.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_ces   = new MyCheckBox();
        state_line_ces = new JLabel( GRAY_ICON );
        lbl_line_lvcfb = new JLabel();
        cbo_line_lvcfb = new MyComboBox();
        cbo_line_lvcfb.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_lvcfb   = new MyCheckBox();
        state_line_lvcfb = new JLabel( GRAY_ICON );
        lbl_line_thm     = new JLabel();
        cbo_line_thm     = new MyComboBox();
        cbo_line_thm.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_thm   = new MyCheckBox();
        state_line_thm = new JLabel( GRAY_ICON );
        lbl_line_encf  = new JLabel();
        cbo_line_encf  = new MyComboBox();
        cbo_line_encf.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_encf   = new MyCheckBox();
        state_line_encf = new JLabel( GRAY_ICON );
        lbl_line_url  = new JLabel();
        cbo_line_url  = new MyComboBox();
        cbo_line_url.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_url   = new MyCheckBox();
        state_line_url = new JLabel( GRAY_ICON );
        lbl_line_lrl  = new JLabel();
        cbo_line_lrl  = new MyComboBox();
        cbo_line_lrl.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_lrl   = new MyCheckBox();
        state_line_lrl = new JLabel( GRAY_ICON );
        lbl_line_epb  = new JLabel();
        cbo_line_epb  = new MyComboBox();
        cbo_line_epb.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_epb   = new MyCheckBox();
        state_line_epb = new JLabel( GRAY_ICON );
        lbl_line_ucmts  = new JLabel();
        cbo_line_ucmts  = new MyComboBox();
        cbo_line_ucmts.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_ucmts   = new MyCheckBox();
        state_line_ucmts = new JLabel( GRAY_ICON );
        lbl_line_ucmts2  = new JLabel();
        cbo_line_ucmts2  = new MyComboBox();
        cbo_line_ucmts2.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_ucmts2   = new MyCheckBox();
        state_line_ucmts2 = new JLabel( GRAY_ICON );
        lbl_line_ucmts3  = new JLabel();
        cbo_line_ucmts3  = new MyComboBox();
        cbo_line_ucmts3.setModel( new DefaultComboBoxModel<>( InputPinA07.allDisplayItem() ) );
        chk_line_ucmts3   = new MyCheckBox();
        state_line_ucmts3 = new JLabel( GRAY_ICON );
        
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
        setLabelTitleStyle( lbl_line_drvenf );
        setComboBoxValueStyle( cbo_line_drvenf );
        setCheckBoxStyle( chk_line_drvenf );
        setLabelTitleStyle( lbl_line_enable );
        setComboBoxValueStyle( cbo_line_enable );
        setCheckBoxStyle( chk_line_enable );
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
        setLabelTitleStyle( lbl_line_dfc );
        setComboBoxValueStyle( cbo_line_dfc );
        setCheckBoxStyle( chk_line_dfc );
        setLabelTitleStyle( lbl_line_dw );
        setComboBoxValueStyle( cbo_line_dw );
        setCheckBoxStyle( chk_line_dw );
        setLabelTitleStyle( lbl_line_hes );
        setComboBoxValueStyle( cbo_line_hes );
        setCheckBoxStyle( chk_line_hes );
        setLabelTitleStyle( lbl_line_ces );
        setComboBoxValueStyle( cbo_line_ces );
        setCheckBoxStyle( chk_line_ces );
        setLabelTitleStyle( lbl_line_lvcfb );
        setComboBoxValueStyle( cbo_line_lvcfb );
        setCheckBoxStyle( chk_line_lvcfb );
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
        add( cpt_main_inputs, "gapbottom 18-12, span, aligny center" );
        add( lbl_title1, "cell 2 1" );
        add( lbl_title2, "cell 3 1" );
        add( lbl_title3, "cell 4 1" );
        add( lbl_title4, "cell 5 1" );

        /**
         * Generated by Groovy script:
         *
         * for(int i=0; i<list.size(); i++) { println "add(lbl_line_${list[i].toLowerCase()}, \"cell 2 ${i+2}\");" println
         * "add(cbo_line_${list[i].toLowerCase()}, \"cell 3 ${i+2}\");" println "add(chk_line_${list[i].toLowerCase()}, \"cell 4 ${i+2}\");" println
         * "add(state_line_${list[i].toLowerCase()}, \"cell 5 ${i+2}\");" }
         */
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
        add( lbl_line_drvenf, "cell 2 11" );
        add( cbo_line_drvenf, "cell 3 11" );
        add( chk_line_drvenf, "center, cell 4 11" );
        add( state_line_drvenf, "center, cell 5 11" );
        add( lbl_line_enable, "cell 2 12" );
        add( cbo_line_enable, "cell 3 12" );
        add( chk_line_enable, "center, cell 4 12" );
        add( state_line_enable, "center, cell 5 12" );
        add( lbl_line_dbde, "cell 2 13" );
        add( cbo_line_dbde, "cell 3 13" );
        add( chk_line_dbde, "center, cell 4 13" );
        add( state_line_dbde, "center, cell 5 13" );
        add( lbl_line_dbdf, "cell 2 14" );
        add( cbo_line_dbdf, "cell 3 14" );
        add( chk_line_dbdf, "center, cell 4 14" );
        add( state_line_dbdf, "center, cell 5 14" );
        add( lbl_line_bs1, "cell 2 15" );
        add( cbo_line_bs1, "cell 3 15" );
        add( chk_line_bs1, "center, cell 4 15" );
        add( state_line_bs1, "center, cell 5 15" );
        add( lbl_line_bs2, "cell 2 16" );
        add( cbo_line_bs2, "cell 3 16" );
        add( chk_line_bs2, "center, cell 4 16" );
        add( state_line_bs2, "center, cell 5 16" );
        add( lbl_line_dfc, "cell 2 17" );
        add( cbo_line_dfc, "cell 3 17" );
        add( chk_line_dfc, "center, cell 4 17" );
        add( state_line_dfc, "center, cell 5 17" );
        add( lbl_line_dw, "cell 2 18" );
        add( cbo_line_dw, "cell 3 18" );
        add( chk_line_dw, "center, cell 4 18" );
        add( state_line_dw, "center, cell 5 18" );
        add( lbl_line_hes, "cell 2 19" );
        add( cbo_line_hes, "cell 3 19" );
        add( chk_line_hes, "center, cell 4 19" );
        add( state_line_hes, "center, cell 5 19" );
        add( lbl_line_ces, "cell 2 20" );
        add( cbo_line_ces, "cell 3 20" );
        add( chk_line_ces, "center, cell 4 20" );
        add( state_line_ces, "center, cell 5 20" );
        add( lbl_line_lvcfb, "cell 2 21" );
        add( cbo_line_lvcfb, "cell 3 21" );
        add( chk_line_lvcfb, "center, cell 4 21" );
        add( state_line_lvcfb, "center, cell 5 21" );
        add( lbl_line_thm, "cell 2 22" );
        add( cbo_line_thm, "cell 3 22" );
        add( chk_line_thm, "center, cell 4 22" );
        add( state_line_thm, "center, cell 5 22" );
        add( lbl_line_encf, "cell 2 23" );
        add( cbo_line_encf, "cell 3 23" );
        add( chk_line_encf, "center, cell 4 23" );
        add( state_line_encf, "center, cell 5 23" );
        add( lbl_line_url, "cell 2 24" );
        add( cbo_line_url, "cell 3 24" );
        add( chk_line_url, "center, cell 4 24" );
        add( state_line_url, "center, cell 5 24" );
        add( lbl_line_lrl, "cell 2 25" );
        add( cbo_line_lrl, "cell 3 25" );
        add( chk_line_lrl, "center, cell 4 25" );
        add( state_line_lrl, "center, cell 5 25" );
        add( lbl_line_epb, "cell 2 26" );
        add( cbo_line_epb, "cell 3 26" );
        add( chk_line_epb, "center, cell 4 26" );
        add( state_line_epb, "center, cell 5 26" );
        add( lbl_line_ucmts, "cell 2 27" );
        add( cbo_line_ucmts, "cell 3 27" );
        add( chk_line_ucmts, "center, cell 4 27" );
        add( state_line_ucmts, "center, cell 5 27" );
        add( lbl_line_ucmts2, "cell 2 28" );
        add( cbo_line_ucmts2, "cell 3 28" );
        add( chk_line_ucmts2, "center, cell 4 28" );
        add( state_line_ucmts2, "center, cell 5 28" );
        add( lbl_line_ucmts3, "cell 2 29" );
        add( cbo_line_ucmts3, "cell 3 29" );
        add( chk_line_ucmts3, "center, cell 4 29" );
        add( state_line_ucmts3, "center, cell 5 29" );
        
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
        cbo_line_drvenf.addItemListener( icl );
        cbo_line_enable.addItemListener( icl );
        cbo_line_dbde.addItemListener( icl );
        cbo_line_dbdf.addItemListener( icl );
        cbo_line_bs1.addItemListener( icl );
        cbo_line_bs2.addItemListener( icl );
        cbo_line_dfc.addItemListener( icl );
        cbo_line_dw.addItemListener( icl );
        cbo_line_hes.addItemListener( icl );
        cbo_line_ces.addItemListener( icl );
        cbo_line_lvcfb.addItemListener( icl );
        cbo_line_thm.addItemListener( icl );
        cbo_line_encf.addItemListener( icl );
        cbo_line_url.addItemListener( icl );
        cbo_line_lrl.addItemListener( icl );
        cbo_line_epb.addItemListener(icl);
        cbo_line_ucmts.addItemListener(icl);
        cbo_line_ucmts2.addItemListener(icl);
        cbo_line_ucmts3.addItemListener(icl);
        
        chk_line_usl.addActionListener( icl );
        chk_line_lsl.addActionListener( icl );
        chk_line_udz.addActionListener( icl );
        chk_line_ldz.addActionListener( icl );
        chk_line_ins.addActionListener( icl );
        chk_line_insup.addActionListener( icl );
        chk_line_insdown.addActionListener( icl );
        chk_line_drvok.addActionListener( icl );
        chk_line_drvbm.addActionListener( icl );
        chk_line_drvenf.addActionListener( icl );
        chk_line_enable.addActionListener( icl );
        chk_line_dbde.addActionListener( icl );
        chk_line_dbdf.addActionListener( icl );
        chk_line_bs1.addActionListener( icl );
        chk_line_bs2.addActionListener( icl );
        chk_line_dfc.addActionListener( icl );
        chk_line_dw.addActionListener( icl );
        chk_line_hes.addActionListener( icl );
        chk_line_ces.addActionListener( icl );
        chk_line_lvcfb.addActionListener( icl );
        chk_line_thm.addActionListener( icl );
        chk_line_encf.addActionListener( icl );
        chk_line_url.addActionListener( icl );
        chk_line_lrl.addActionListener( icl );
        chk_line_epb.addActionListener( icl );
        chk_line_ucmts.addActionListener( icl );
        chk_line_ucmts2.addActionListener( icl );
        chk_line_ucmts3.addActionListener( icl );

        /**
         * for(int i=0; i<list.size(); i++) { println
         * "bindGroup(\"${list[i]}\", lbl_line_${list[i].toLowerCase()}, cbo_line_${list[i].toLowerCase()}, chk_line_${list[i].toLowerCase()}, state_line_${list[i].toLowerCase()});"
         * }
         */
        bindGroup( "USL", lbl_line_usl, cbo_line_usl, chk_line_usl, state_line_usl );
        bindGroup( "LSL", lbl_line_lsl, cbo_line_lsl, chk_line_lsl, state_line_lsl );
        bindGroup( "UDZ", lbl_line_udz, cbo_line_udz, chk_line_udz, state_line_udz );
        bindGroup( "LDZ", lbl_line_ldz, cbo_line_ldz, chk_line_ldz, state_line_ldz );
        bindGroup( "INS", lbl_line_ins, cbo_line_ins, chk_line_ins, state_line_ins );
        bindGroup( "INSUP", lbl_line_insup, cbo_line_insup, chk_line_insup, state_line_insup );
        bindGroup( "INSDOWN", lbl_line_insdown, cbo_line_insdown, chk_line_insdown, state_line_insdown );
        bindGroup( "DRVOK", lbl_line_drvok, cbo_line_drvok, chk_line_drvok, state_line_drvok );
        bindGroup( "DRVBM", lbl_line_drvbm, cbo_line_drvbm, chk_line_drvbm, state_line_drvbm );
        bindGroup( "DRVENF", lbl_line_drvenf, cbo_line_drvenf, chk_line_drvenf, state_line_drvenf );
        bindGroup( "ENABLE", lbl_line_enable, cbo_line_enable, chk_line_enable, state_line_enable );
        bindGroup( "DBDE", lbl_line_dbde, cbo_line_dbde, chk_line_dbde, state_line_dbde );
        bindGroup( "DBDF", lbl_line_dbdf, cbo_line_dbdf, chk_line_dbdf, state_line_dbdf );
        bindGroup( "BS1", lbl_line_bs1, cbo_line_bs1, chk_line_bs1, state_line_bs1 );
        bindGroup( "BS2", lbl_line_bs2, cbo_line_bs2, chk_line_bs2, state_line_bs2 );
        bindGroup( "DFC", lbl_line_dfc, cbo_line_dfc, chk_line_dfc, state_line_dfc );
        bindGroup( "DW", lbl_line_dw, cbo_line_dw, chk_line_dw, state_line_dw );
        bindGroup( "HES", lbl_line_hes, cbo_line_hes, chk_line_hes, state_line_hes );
        bindGroup( "CES", lbl_line_ces, cbo_line_ces, chk_line_ces, state_line_ces );
        bindGroup( "LVCFB", lbl_line_lvcfb, cbo_line_lvcfb, chk_line_lvcfb, state_line_lvcfb );
        bindGroup( "THM", lbl_line_thm, cbo_line_thm, chk_line_thm, state_line_thm );
        bindGroup( "ENCF", lbl_line_encf, cbo_line_encf, chk_line_encf, state_line_encf );
        bindGroup( "URL", lbl_line_url, cbo_line_url, chk_line_url, state_line_url );
        bindGroup( "LRL", lbl_line_lrl, cbo_line_lrl, chk_line_lrl, state_line_lrl );
        bindGroup( "EPB", lbl_line_epb, cbo_line_epb, chk_line_epb, state_line_epb );
        bindGroup( "UCMTS", lbl_line_ucmts, cbo_line_ucmts, chk_line_ucmts, state_line_ucmts );
        bindGroup( "UCMTS2", lbl_line_ucmts2, cbo_line_ucmts2, chk_line_ucmts2, state_line_ucmts2 );
        bindGroup( "UCMTS3", lbl_line_ucmts3, cbo_line_ucmts3, chk_line_ucmts3, state_line_ucmts3 );
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
        lbl_line_drvenf.setText( "DRVENF" );
        lbl_line_enable.setText( "ENABLE" );
        lbl_line_dbde.setText( "DBDE" );
        lbl_line_dbdf.setText( "DBDF" );
        lbl_line_bs1.setText( "BS1" );
        lbl_line_bs2.setText( "BS2" );
        lbl_line_dfc.setText( "DFC" );
        lbl_line_dw.setText( "DW" );
        lbl_line_hes.setText( "HES" );
        lbl_line_ces.setText( "CES" );
        lbl_line_lvcfb.setText( "LVCFB" );
        lbl_line_thm.setText( "THM" );
        lbl_line_encf.setText( "ENCF" );
        lbl_line_url.setText( "URL" );
        lbl_line_lrl.setText( "LRL" );
        lbl_line_epb.setText( "EPB" );
        lbl_line_ucmts.setText( "UCMTS" );
        lbl_line_ucmts2.setText( "UCMTS2" );
        lbl_line_ucmts3.setText( "UCMTS3" );
    }


    private void setCheckBoxStyle ( final JCheckBox c ) {
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
        
        bean_inputPin.setUsl( ( InputPinA07 )cbo_line_usl.getSelectedItem() );
        bean_inputPin.setLsl( ( InputPinA07 )cbo_line_lsl.getSelectedItem() );
        bean_inputPin.setUdz( ( InputPinA07 )cbo_line_udz.getSelectedItem() );
        bean_inputPin.setLdz( ( InputPinA07 )cbo_line_ldz.getSelectedItem() );
        bean_inputPin.setIns( ( InputPinA07 )cbo_line_ins.getSelectedItem() );
        bean_inputPin.setInsUp( ( InputPinA07 )cbo_line_insup.getSelectedItem() );
        bean_inputPin.setInsDown( ( InputPinA07 )cbo_line_insdown.getSelectedItem() );
        bean_inputPin.setDrvOK( ( InputPinA07 )cbo_line_drvok.getSelectedItem() );
        bean_inputPin.setDrvBM( ( InputPinA07 )cbo_line_drvbm.getSelectedItem() );
        bean_inputPin.setDrvENF( ( InputPinA07 )cbo_line_drvenf.getSelectedItem() );
        bean_inputPin.setEnable( ( InputPinA07 )cbo_line_enable.getSelectedItem() );
        bean_inputPin.setDbde( ( InputPinA07 )cbo_line_dbde.getSelectedItem() );
        bean_inputPin.setDbdf( ( InputPinA07 )cbo_line_dbdf.getSelectedItem() );
        bean_inputPin.setBs1( ( InputPinA07 )cbo_line_bs1.getSelectedItem() );
        bean_inputPin.setBs2( ( InputPinA07 )cbo_line_bs2.getSelectedItem() );
        bean_inputPin.setDfc( ( InputPinA07 )cbo_line_dfc.getSelectedItem() );
        bean_inputPin.setDw( ( InputPinA07 )cbo_line_dw.getSelectedItem() );
        bean_inputPin.setHes( ( InputPinA07 )cbo_line_hes.getSelectedItem() );
        bean_inputPin.setCes( ( InputPinA07 )cbo_line_ces.getSelectedItem() );
        bean_inputPin.setLvcfb( ( InputPinA07 )cbo_line_lvcfb.getSelectedItem() );
        bean_inputPin.setThm( ( InputPinA07 )cbo_line_thm.getSelectedItem() );
        bean_inputPin.setEncf( ( InputPinA07 )cbo_line_encf.getSelectedItem() );
        bean_inputPin.setUrl( ( InputPinA07 )cbo_line_url.getSelectedItem() );
        bean_inputPin.setLrl( ( InputPinA07 )cbo_line_lrl.getSelectedItem() );
        bean_inputPin.setEpb( ( InputPinA07 )cbo_line_epb.getSelectedItem() );
        bean_inputPin.setUcmts( ( InputPinA07 )cbo_line_ucmts.getSelectedItem() );
        bean_inputPin.setUcmts2( ( InputPinA07 )cbo_line_ucmts2.getSelectedItem() );
        bean_inputPin.setUcmts3( ( InputPinA07 )cbo_line_ucmts3.getSelectedItem() );
        
        bean_inputPin.setUslInverted( chk_line_usl.isSelected() );
        bean_inputPin.setLslInverted( chk_line_lsl.isSelected() );
        bean_inputPin.setUdzInverted( chk_line_udz.isSelected() );
        bean_inputPin.setLdzInverted( chk_line_ldz.isSelected() );
        bean_inputPin.setInsInverted( chk_line_ins.isSelected() );
        bean_inputPin.setInsUpInverted( chk_line_insup.isSelected() );
        bean_inputPin.setInsDownInverted( chk_line_insdown.isSelected() );
        bean_inputPin.setDrvOKInverted( chk_line_drvok.isSelected() );
        bean_inputPin.setDrvBMInverted( chk_line_drvbm.isSelected() );
        bean_inputPin.setDrvENFInverted( chk_line_drvenf.isSelected() );
        bean_inputPin.setEnableInverted( chk_line_enable.isSelected() );
        bean_inputPin.setDbdeInverted( chk_line_dbde.isSelected() );
        bean_inputPin.setDbdfInverted( chk_line_dbdf.isSelected() );
        bean_inputPin.setBs1Inverted( chk_line_bs1.isSelected() );
        bean_inputPin.setBs2Inverted( chk_line_bs2.isSelected() );
        bean_inputPin.setDfcInverted( chk_line_dfc.isSelected() );
        bean_inputPin.setDwInverted( chk_line_dw.isSelected() );
        bean_inputPin.setHesInverted( chk_line_hes.isSelected() );
        bean_inputPin.setCesInverted( chk_line_ces.isSelected() );
        bean_inputPin.setLvcfbInverted( chk_line_lvcfb.isSelected() );
        bean_inputPin.setThmInverted( chk_line_thm.isSelected() );
        bean_inputPin.setEncfInverted( chk_line_encf.isSelected() );
        bean_inputPin.setUrlInverted( chk_line_url.isSelected() );
        bean_inputPin.setLrlInverted( chk_line_lrl.isSelected() );
        bean_inputPin.setEpbInverted( chk_line_epb.isSelected() );
        bean_inputPin.setUcmtsInverted( chk_line_ucmts.isSelected() );
        bean_inputPin.setUcmts2Inverted( chk_line_ucmts2.isSelected() );
        bean_inputPin.setUcmts3Inverted( chk_line_ucmts3.isSelected() );
        return bean_inputPin;
    }


    public void setInputPinBean ( InputPinBean bean_inputPin ) {
        final InputPinA07[] items = InputPinA07.allDisplayItem(bean_inputPin.firmwareVersion);
        cbo_line_usl.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_lsl.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_udz.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_ldz.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_ins.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_insup.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_insdown.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_drvok.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_drvbm.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_drvenf.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_enable.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_dbde.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_dbdf.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_bs1.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_bs2.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_dfc.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_dw.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_hes.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_ces.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_lvcfb.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_thm.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_encf.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_url.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_lrl.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_epb.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_ucmts.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_ucmts2.setModel( new DefaultComboBoxModel<>( items ) );
        cbo_line_ucmts3.setModel( new DefaultComboBoxModel<>( items ) );
        
        List<InputPinA07> list = Arrays.asList( items );
        
        this.cbo_line_usl.setSelectedItem( list.contains( bean_inputPin.getUsl() ) ? bean_inputPin.getUsl() : null );
        this.cbo_line_lsl.setSelectedItem( list.contains( bean_inputPin.getLsl() ) ? bean_inputPin.getLsl() : null );
        this.cbo_line_udz.setSelectedItem( list.contains( bean_inputPin.getUdz() ) ? bean_inputPin.getUdz() : null );
        this.cbo_line_ldz.setSelectedItem( list.contains( bean_inputPin.getLdz() ) ? bean_inputPin.getLdz() : null );
        this.cbo_line_ins.setSelectedItem( list.contains( bean_inputPin.getIns() ) ? bean_inputPin.getIns() : null );
        this.cbo_line_insup.setSelectedItem( list.contains( bean_inputPin.getInsUp() ) ? bean_inputPin.getInsUp() : null );
        this.cbo_line_insdown.setSelectedItem( list.contains( bean_inputPin.getInsDown() ) ? bean_inputPin.getInsDown() : null );
        this.cbo_line_drvok.setSelectedItem( list.contains( bean_inputPin.getDrvOK() ) ? bean_inputPin.getDrvOK() : null );
        this.cbo_line_drvbm.setSelectedItem( list.contains( bean_inputPin.getDrvBM() ) ? bean_inputPin.getDrvBM() : null );
        this.cbo_line_drvenf.setSelectedItem( list.contains( bean_inputPin.getDrvENF() ) ? bean_inputPin.getDrvENF() : null );
        this.cbo_line_enable.setSelectedItem( list.contains( bean_inputPin.getEnable() ) ? bean_inputPin.getEnable() : null );
        this.cbo_line_dbde.setSelectedItem( list.contains( bean_inputPin.getDbde() ) ? bean_inputPin.getDbde() : null );
        this.cbo_line_dbdf.setSelectedItem( list.contains( bean_inputPin.getDbdf() ) ? bean_inputPin.getDbdf() : null );
        this.cbo_line_bs1.setSelectedItem( list.contains( bean_inputPin.getBs1() ) ? bean_inputPin.getBs1() : null );
        this.cbo_line_bs2.setSelectedItem( list.contains( bean_inputPin.getBs2() ) ? bean_inputPin.getBs2() : null );
        this.cbo_line_dfc.setSelectedItem( list.contains( bean_inputPin.getDfc() ) ? bean_inputPin.getDfc() : null );
        this.cbo_line_dw.setSelectedItem( list.contains( bean_inputPin.getDw() ) ? bean_inputPin.getDw() : null );
        this.cbo_line_hes.setSelectedItem( list.contains( bean_inputPin.getHes() ) ? bean_inputPin.getHes() : null );
        this.cbo_line_ces.setSelectedItem( list.contains( bean_inputPin.getCes() ) ? bean_inputPin.getCes() : null );
        this.cbo_line_lvcfb.setSelectedItem( list.contains( bean_inputPin.getLvcfb() ) ? bean_inputPin.getLvcfb() : null );
        this.cbo_line_thm.setSelectedItem( list.contains( bean_inputPin.getThm() ) ? bean_inputPin.getThm() : null );
        this.cbo_line_encf.setSelectedItem( list.contains( bean_inputPin.getEncf() ) ? bean_inputPin.getEncf() : null );
        this.cbo_line_url.setSelectedItem( list.contains( bean_inputPin.getUrl() ) ? bean_inputPin.getUrl() : null );
        this.cbo_line_lrl.setSelectedItem( list.contains( bean_inputPin.getLrl() ) ? bean_inputPin.getLrl() : null );
        this.cbo_line_epb.setSelectedItem( list.contains( bean_inputPin.getEpb() ) ? bean_inputPin.getEpb() : null );
        this.cbo_line_ucmts.setSelectedItem( list.contains( bean_inputPin.getUcmts() ) ? bean_inputPin.getUcmts() : null );
        this.cbo_line_ucmts2.setSelectedItem( list.contains( bean_inputPin.getUcmts2() ) ? bean_inputPin.getUcmts2() : null );
        this.cbo_line_ucmts3.setSelectedItem( list.contains( bean_inputPin.getUcmts3() ) ? bean_inputPin.getUcmts3() : null );
        
        this.chk_line_usl.setSelected( bean_inputPin.isUslInverted() );
        this.chk_line_lsl.setSelected( bean_inputPin.isLslInverted() );
        this.chk_line_udz.setSelected( bean_inputPin.isUdzInverted() );
        this.chk_line_ldz.setSelected( bean_inputPin.isLdzInverted() );
        this.chk_line_ins.setSelected( bean_inputPin.isInsInverted() );
        this.chk_line_insup.setSelected( bean_inputPin.isInsUpInverted() );
        this.chk_line_insdown.setSelected( bean_inputPin.isInsDownInverted() );
        this.chk_line_drvok.setSelected( bean_inputPin.isDrvOKInverted() );
        this.chk_line_drvbm.setSelected( bean_inputPin.isDrvBMInverted() );
        this.chk_line_drvenf.setSelected( bean_inputPin.isDrvENFInverted() );
        this.chk_line_enable.setSelected( bean_inputPin.isEnableInverted() );
        this.chk_line_dbde.setSelected( bean_inputPin.isDbdeInverted() );
        this.chk_line_dbdf.setSelected( bean_inputPin.isDbdfInverted() );
        this.chk_line_bs1.setSelected( bean_inputPin.isBs1Inverted() );
        this.chk_line_bs2.setSelected( bean_inputPin.isBs2Inverted() );
        this.chk_line_dfc.setSelected( bean_inputPin.isDfcInverted() );
        this.chk_line_dw.setSelected( bean_inputPin.isDwInverted() );
        this.chk_line_hes.setSelected( bean_inputPin.isHesInverted() );
        this.chk_line_ces.setSelected( bean_inputPin.isCesInverted() );
        this.chk_line_lvcfb.setSelected( bean_inputPin.isLvcfbInverted() );
        this.chk_line_thm.setSelected( bean_inputPin.isThmInverted() );
        this.chk_line_encf.setSelected( bean_inputPin.isEncfInverted() );
        this.chk_line_url.setSelected( bean_inputPin.isUrlInverted() );
        this.chk_line_lrl.setSelected( bean_inputPin.isLrlInverted() );
        this.chk_line_epb.setSelected( bean_inputPin.isEpbInverted() );
        this.chk_line_ucmts.setSelected( bean_inputPin.isUcmtsInverted() );
        this.chk_line_ucmts2.setSelected( bean_inputPin.isUcmts2Inverted() );
        this.chk_line_ucmts3.setSelected( bean_inputPin.isUcmts3Inverted() );
    }


    public static InputsA07 createPanel ( SettingPanel<InputsA07> panel ) {
        InputsA07 gui = new InputsA07();
        gui.setSettingPanel( panel );
        return gui;
    }


    public static void main ( String... arg ) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.getContentPane().add( new InputsA07() );
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


    public void setInputPinState ( HashMap<InputPinA07, Boolean> inputPinState ) {
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
            state_line_drvenf.setIcon( toIcon( bean != null && bean.getDrvENF() == null
                                               ? null
                                               : logicalXOR( chk_line_drvenf.isSelected(), inputPinState.get( bean.getDrvENF() ) ) ) );
            state_line_enable.setIcon( toIcon( bean != null && bean.getEnable() == null
                                               ? null
                                               : logicalXOR( chk_line_enable.isSelected(), inputPinState.get( bean.getEnable() ) ) ) );
            state_line_dbde.setIcon( toIcon( bean != null && bean.getDbde() == null
                                             ? null
                                             : logicalXOR( chk_line_dbde.isSelected(), inputPinState.get( bean.getDbde() ) ) ) );
            state_line_dbdf.setIcon( toIcon( bean != null && bean.getDbdf() == null
                                             ? null
                                             : logicalXOR( chk_line_dbdf.isSelected(), inputPinState.get( bean.getDbdf() ) ) ) );
            state_line_bs1.setIcon( toIcon( bean != null && bean.getBs1() == null
                                           ? null
                                           : logicalXOR( chk_line_bs1.isSelected(), inputPinState.get( bean.getBs1() ) ) ) );
            state_line_bs2.setIcon( toIcon( bean != null && bean.getBs2() == null
                    						? null
                    						: logicalXOR( chk_line_bs2.isSelected(), inputPinState.get( bean.getBs2() ) ) ) );
            state_line_dfc.setIcon( toIcon( bean != null && bean.getDfc() == null
                                            ? null
                                            : logicalXOR( chk_line_dfc.isSelected(), inputPinState.get( bean.getDfc() ) ) ) );
            state_line_dw.setIcon( toIcon( bean != null && bean.getDw() == null
                                           ? null
                                           : logicalXOR( chk_line_dw.isSelected(), inputPinState.get( bean.getDw() ) ) ) );
            state_line_hes.setIcon( toIcon( bean != null && bean.getHes() == null
                                            ? null
                                            : logicalXOR( chk_line_hes.isSelected(), inputPinState.get( bean.getHes() ) ) ) );
            state_line_ces.setIcon( toIcon( bean != null && bean.getCes() == null
                                            ? null
                                            : logicalXOR( chk_line_ces.isSelected(), inputPinState.get( bean.getCes() ) ) ) );
            state_line_lvcfb.setIcon( toIcon( bean != null && bean.getLvcfb() == null
                                              ? null
                                              : logicalXOR( chk_line_lvcfb.isSelected(), inputPinState.get( bean.getLvcfb() ) ) ) );
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
        }
    }


    public static class InputPinBean {
        private String firmwareVersion;
        private InputPinA07 usl;
        private InputPinA07 lsl;
        private InputPinA07 udz;
        private InputPinA07 ldz;
        private InputPinA07 ins;
        private InputPinA07 insUp;
        private InputPinA07 insDown;
        private InputPinA07 drvOK;
        private InputPinA07 drvBM;
        private InputPinA07 drvENF;
        private InputPinA07 enable;
        private InputPinA07 dbde;
        private InputPinA07 dbdf;
        private InputPinA07 bs1;
        private InputPinA07 bs2;
        private InputPinA07 dfc;
        private InputPinA07 dw;
        private InputPinA07 hes;
        private InputPinA07 ces;
        private InputPinA07 lvcfb;
        private InputPinA07 thm;
        private InputPinA07 encf;
        private InputPinA07 url;
        private InputPinA07 lrl;
        private InputPinA07 epb;
        private InputPinA07 ucmts;
        private InputPinA07 ucmts2;
        private InputPinA07 ucmts3;
        
        private boolean     uslInverted;
        private boolean     lslInverted;
        private boolean     udzInverted;
        private boolean     ldzInverted;
        private boolean     insInverted;
        private boolean     insUpInverted;
        private boolean     insDownInverted;
        private boolean     drvOKInverted;
        private boolean     drvBMInverted;
        private boolean     drvENFInverted;
        private boolean     enableInverted;
        private boolean     dbdeInverted;
        private boolean     dbdfInverted;
        private boolean     bs1Inverted;
        private boolean     bs2Inverted;
        private boolean     dfcInverted;
        private boolean     dwInverted;
        private boolean     hesInverted;
        private boolean     cesInverted;
        private boolean     lvcfbInverted;
        private boolean     thmInverted;
        private boolean     encfInverted;
        private boolean     urlInverted;
        private boolean     lrlInverted;
        private boolean     epbInverted;
        private boolean     ucmtsInverted;
        private boolean     ucmts2Inverted;
        private boolean     ucmts3Inverted;



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


        public boolean isDrvENFInverted () {
            return drvENFInverted;
        }


        public boolean isEnableInverted () {
            return enableInverted;
        }


        public boolean isDbdeInverted () {
            return dbdeInverted;
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

        public boolean isDfcInverted () {
            return dfcInverted;
        }


        public boolean isDwInverted () {
            return dwInverted;
        }


        public boolean isHesInverted () {
            return hesInverted;
        }


        public boolean isCesInverted () {
            return cesInverted;
        }


        public boolean isLvcfbInverted () {
            return lvcfbInverted;
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


        public void setDrvENFInverted ( boolean drvENFInverted ) {
            this.drvENFInverted = drvENFInverted;
        }


        public void setEnableInverted ( boolean enableInverted ) {
            this.enableInverted = enableInverted;
        }


        public void setDbdeInverted ( boolean dbdeInverted ) {
            this.dbdeInverted = dbdeInverted;
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

        public void setDfcInverted ( boolean dfcInverted ) {
            this.dfcInverted = dfcInverted;
        }


        public void setDwInverted ( boolean dwInverted ) {
            this.dwInverted = dwInverted;
        }


        public void setHesInverted ( boolean hesInverted ) {
            this.hesInverted = hesInverted;
        }


        public void setCesInverted ( boolean cesInverted ) {
            this.cesInverted = cesInverted;
        }


        public void setLvcfbInverted ( boolean lvcfbInverted ) {
            this.lvcfbInverted = lvcfbInverted;
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

        public InputPinA07 getUsl () {
            return usl;
        }


        public InputPinA07 getLsl () {
            return lsl;
        }


        public InputPinA07 getUdz () {
            return udz;
        }


        public InputPinA07 getLdz () {
            return ldz;
        }


        public InputPinA07 getIns () {
            return ins;
        }


        public InputPinA07 getInsUp () {
            return insUp;
        }


        public InputPinA07 getInsDown () {
            return insDown;
        }


        public InputPinA07 getDrvOK () {
            return drvOK;
        }


        public InputPinA07 getDrvBM () {
            return drvBM;
        }


        public InputPinA07 getDrvENF () {
            return drvENF;
        }


        public InputPinA07 getEnable () {
            return enable;
        }


        public InputPinA07 getDbde () {
            return dbde;
        }


        public InputPinA07 getDbdf () {
            return dbdf;
        }


        public InputPinA07 getBs1 () {
            return bs1;
        }
        public InputPinA07 getBs2 () {
            return bs2;
        }

        public InputPinA07 getDfc () {
            return dfc;
        }


        public InputPinA07 getDw () {
            return dw;
        }


        public InputPinA07 getHes () {
            return hes;
        }


        public InputPinA07 getCes () {
            return ces;
        }


        public InputPinA07 getLvcfb () {
            return lvcfb;
        }


        public InputPinA07 getThm () {
            return thm;
        }


        public InputPinA07 getEncf () {
            return encf;
        }


        public final InputPinA07 getUrl() {
            return url;
        }


        public final InputPinA07 getLrl() {
            return lrl;
        }
        
        public InputPinA07 getEpb() {
			return epb;
		}
        

		public InputPinA07 getUcmts() {
			return ucmts;
		}


		public InputPinA07 getUcmts2() {
			return ucmts2;
		}


		public InputPinA07 getUcmts3() {
			return ucmts3;
		}

        public void setUsl ( InputPinA07 usl ) {
            this.usl = usl;
        }


        public void setLsl ( InputPinA07 lsl ) {
            this.lsl = lsl;
        }


        public void setUdz ( InputPinA07 udz ) {
            this.udz = udz;
        }


        public void setLdz ( InputPinA07 ldz ) {
            this.ldz = ldz;
        }


        public void setIns ( InputPinA07 ins ) {
            this.ins = ins;
        }


        public void setInsUp ( InputPinA07 insUp ) {
            this.insUp = insUp;
        }


        public void setInsDown ( InputPinA07 insDown ) {
            this.insDown = insDown;
        }


        public void setDrvOK ( InputPinA07 drvOK ) {
            this.drvOK = drvOK;
        }


        public void setDrvBM ( InputPinA07 drvBM ) {
            this.drvBM = drvBM;
        }


        public void setDrvENF ( InputPinA07 drvENF ) {
            this.drvENF = drvENF;
        }


        public void setEnable ( InputPinA07 enable ) {
            this.enable = enable;
        }


        public void setDbde ( InputPinA07 dbde ) {
            this.dbde = dbde;
        }


        public void setDbdf ( InputPinA07 dbdf ) {
            this.dbdf = dbdf;
        }


        public void setBs1 ( InputPinA07 bs ) {
            this.bs1 = bs;
        }
        public void setBs2 ( InputPinA07 bs ) {
            this.bs2 = bs;
        }

        public void setDfc ( InputPinA07 dfc ) {
            this.dfc = dfc;
        }


        public void setDw ( InputPinA07 dw ) {
            this.dw = dw;
        }


        public void setHes ( InputPinA07 hes ) {
            this.hes = hes;
        }


        public void setCes ( InputPinA07 ces ) {
            this.ces = ces;
        }


        public void setLvcfb ( InputPinA07 lvcfb ) {
            this.lvcfb = lvcfb;
        }


        public void setThm ( InputPinA07 thm ) {
            this.thm = thm;
        }


        public void setEncf ( InputPinA07 encf ) {
            this.encf = encf;
        }


        public final void setUrl(InputPinA07 url) {
            this.url = url;
        }


        public final void setLrl(InputPinA07 lrl) {
            this.lrl = lrl;
        }
        
        public void setEpb(InputPinA07 epb) {
			this.epb = epb;
		}

		public void setUcmts(InputPinA07 ucmts) {
			this.ucmts = ucmts;
		}

		public void setUcmts2(InputPinA07 ucmts2) {
			this.ucmts2 = ucmts2;
		}

		public void setUcmts3(InputPinA07 ucmts3) {
			this.ucmts3 = ucmts3;
		}
		
    }




    public class ItemChangedListener implements ActionListener, ItemListener {
        public void itemStateChanged ( ItemEvent e ) {
            if ( settingPanel instanceof InputsA07Setting ) {
                ( ( InputsA07Setting )settingPanel ).updateStatus();
            }
        }


        @Override
        public void actionPerformed ( ActionEvent e ) {
            if ( settingPanel instanceof InputsA07Setting ) {
                ( ( InputsA07Setting )settingPanel ).updateStatus();
            }
        }
    }
}
