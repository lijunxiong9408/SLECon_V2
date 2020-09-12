package slecon.setting.setup.io;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.io.crossbar.OutputPinA01;
import logic.io.crossbar.OutputSourceD01;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;

public class OutputsA01 extends JPanel {
    private static final long      serialVersionUID = -6123382229791452623L;
    
    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.io.OutputsA01" );

    private static final ImageIcon OUTPUT_ON_ICON    = ImageFactory.LIGHT_BRIGHT_GREEN.icon( 16, 16 );
    private static final ImageIcon OUTPUT_OFF_ICON   = ImageFactory.LIGHT_DARK_GREEN.icon( 16, 16 );
    private static final ImageIcon GRAY_ICON        = ImageFactory.LIGHT_BLACK.icon( 16, 16 );



    private SettingPanel<OutputsA01>   settingPanel;
    private JLabel                  cpt_relay_outputs;
    private JLabel                  lbl_k2m;
    private MyComboBox				cbo_k2m;
    private JLabel  				state_k2m;
    
    private JLabel                  lbl_k3m;
    private MyComboBox				cbo_k3m;
    private JLabel  			 	state_k3m;
    
    private JLabel                  lbl_gor;
    private MyComboBox 				cbo_gor;
    private JLabel  				state_gor;
    
    private JLabel                  lbl_br;
    private MyComboBox 				cbo_br;
    private JLabel  				state_br;
    
    private JLabel                  lbl_abr;
    private MyComboBox 				cbo_abr;
    private JLabel  				state_abr;
   
    private JLabel                  lbl_scr;
    private MyComboBox 				cbo_scr;
    private JLabel  				state_scr;
    
    private JLabel                  lbl_scr1;
    private MyComboBox 				cbo_scr1;
    private JLabel  				state_scr1;
    
    private JLabel                  lbl_scr2;
    private MyComboBox 				cbo_scr2;
    private JLabel  				state_scr2;
    
    private JLabel                  lbl_ccfr;
    private MyComboBox 				cbo_ccfr;
    private JLabel  				state_ccfr;
    
    private JLabel                  lbl_lvc1;
    private MyComboBox 				cbo_lvc1;
    private JLabel  				state_lvc1;
    
    private JLabel                  lbl_lvc2;
    private MyComboBox 				cbo_lvc2;
    private JLabel  				state_lvc2;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel                  cpt_digital_output;
    private JLabel                  lbl_enhw;
    private MyComboBox 				cbo_enhw;
    private JLabel  				state_enhw;
    
    private JLabel                  lbl_fwd;
    private MyComboBox 				cbo_fwd;
    private JLabel  				state_fwd;
    
    private JLabel                  lbl_rev;
    private MyComboBox 				cbo_rev;
    private JLabel  				state_rev;
    
    private JLabel                  lbl_epr;
    private MyComboBox 				cbo_epr;
    private JLabel  				state_epr;
    
    private JLabel                  lbl_rel;
    private MyComboBox 				cbo_rel;
    private JLabel  				state_rel;
    
    private JLabel                  lbl_ucmt;
    private MyComboBox 				cbo_ucmt;
    private JLabel  				state_ucmt;
    
    private JLabel                  lbl_ucmr;
    private MyComboBox				cbo_ucmr;
    private JLabel  				state_ucmr;

    private boolean 				started;


    public OutputsA01 () {
    	initGUI();
    }


    public void setSettingPanel ( SettingPanel<OutputsA01> panel ) {
        this.settingPanel = panel;
    }

    public void SetJComboBoxEnable(boolean Enable) {
    	this.cbo_k2m.setEnabled(Enable);
    	this.cbo_k3m.setEnabled(Enable);
    	this.cbo_gor.setEnabled(Enable);
    	this.cbo_br.setEnabled(Enable);
    	this.cbo_abr.setEnabled(Enable);
    	this.cbo_scr.setEnabled(Enable);
    	this.cbo_scr1.setEnabled(Enable);
    	this.cbo_scr2.setEnabled(Enable);
    	this.cbo_ccfr.setEnabled(Enable);
    	
    	this.cbo_enhw.setEnabled(Enable);
    	this.cbo_fwd.setEnabled(Enable);
    	this.cbo_rev.setEnabled(Enable);
    	this.cbo_epr.setEnabled(Enable);
    	this.cbo_rel.setEnabled(Enable);
    	this.cbo_ucmt.setEnabled(Enable);
    	this.cbo_ucmr.setEnabled(Enable);
    }
    
    
    private void initGUI () {
        OutputSourceD01[] combo_values = new OutputSourceD01[ OutputSourceD01.values().length ];

        // combo_values[0] = null;
        for ( int i = 0 ; i < OutputSourceD01.values().length ; i++ )
            combo_values[ i ] = OutputSourceD01.values()[ i ];
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "ins 24, gap 10 6", "[30::30][20::20][50::50][280::280][50::50][]" ) );
        cpt_relay_outputs = new JLabel();
        lbl_k2m           = new JLabel();
        cbo_k2m           = new MyComboBox( combo_values );

        lbl_k3m           = new JLabel();
        cbo_k3m           = new MyComboBox( combo_values );
        
        lbl_gor           = new JLabel();
        cbo_gor           = new MyComboBox( combo_values );
        
        lbl_br           = new JLabel();
        cbo_br           = new MyComboBox( combo_values );
        
        lbl_abr           = new JLabel();
        cbo_abr           = new MyComboBox( combo_values );
        
        lbl_scr           = new JLabel();
        cbo_scr           = new MyComboBox( combo_values );
        
        lbl_scr1           = new JLabel();
        cbo_scr1           = new MyComboBox( combo_values );
        
        lbl_scr2           = new JLabel();
        cbo_scr2           = new MyComboBox( combo_values );
        
        lbl_ccfr          = new JLabel();
        cbo_ccfr          = new MyComboBox( combo_values );
        
        lbl_lvc1          = new JLabel();
        cbo_lvc1          = new MyComboBox( combo_values );
        
        lbl_lvc2          = new JLabel();
        cbo_lvc2         = new MyComboBox( combo_values );
        
        state_k2m         = new JLabel( GRAY_ICON );
        state_k3m         = new JLabel( GRAY_ICON );
        state_gor         = new JLabel( GRAY_ICON );
        state_br         = new JLabel( GRAY_ICON );
        state_abr         = new JLabel( GRAY_ICON );
        state_scr         = new JLabel( GRAY_ICON );
        state_scr1         = new JLabel( GRAY_ICON );
        state_scr2         = new JLabel( GRAY_ICON );
        state_ccfr        = new JLabel( GRAY_ICON );
        state_lvc1        = new JLabel( GRAY_ICON );
        state_lvc2        = new JLabel( GRAY_ICON );
        
        state_enhw        = new JLabel( GRAY_ICON );
        state_fwd        = new JLabel( GRAY_ICON );
        state_rev        = new JLabel( GRAY_ICON );
        state_epr        = new JLabel( GRAY_ICON );
        state_rel        = new JLabel( GRAY_ICON );
        state_ucmt        = new JLabel( GRAY_ICON );
        state_ucmr        = new JLabel( GRAY_ICON );
        setCaptionStyle( cpt_relay_outputs );

        // @CompoentSetting( lbl_rl1, cbo_rl1 )
        setComboBoxLabelStyle( lbl_k2m );
        setComboBoxValueStyle( cbo_k2m );

        // @CompoentSetting( lbl_k3m, cbo_k3m )
        setComboBoxLabelStyle( lbl_k3m );
        setComboBoxValueStyle( cbo_k3m );
        
        // @CompoentSetting( lbl_gor, cbo_gor )
        setComboBoxLabelStyle( lbl_gor );
        setComboBoxValueStyle( cbo_gor );
        
        // @CompoentSetting( lbl_br, cbo_br )
        setComboBoxLabelStyle( lbl_br );
        setComboBoxValueStyle( cbo_br );
        
        // @CompoentSetting( lbl_abr, cbo_abr )
        setComboBoxLabelStyle( lbl_abr );
        setComboBoxValueStyle( cbo_abr );
        
        // @CompoentSetting( lbl_scr, cbo_scr )
        setComboBoxLabelStyle( lbl_scr );
        setComboBoxValueStyle( cbo_scr );
        
        // @CompoentSetting( lbl_scr1, cbo_scr1 )
        setComboBoxLabelStyle( lbl_scr1 );
        setComboBoxValueStyle( cbo_scr1 );
        
        // @CompoentSetting( lbl_scr2, cbo_scr2 )
        setComboBoxLabelStyle( lbl_scr2 );
        setComboBoxValueStyle( cbo_scr2 );
        
        // @CompoentSetting( lbl_ccfr, cbo_ccfr )
        setComboBoxLabelStyle( lbl_ccfr );
        setComboBoxValueStyle( cbo_ccfr );
        
        // @CompoentSetting( lbl_lvc1, cbo_lvc1 )
        setComboBoxLabelStyle( lbl_lvc1 );
        setComboBoxValueStyle( cbo_lvc1 );
        
        // @CompoentSetting( lbl_lvc2, cbo_lvc2 )
        setComboBoxLabelStyle( lbl_lvc2 );
        setComboBoxValueStyle( cbo_lvc2 );
        
        add( cpt_relay_outputs, "gapbottom 18-12, span, aligny center" );
        add( lbl_k2m, "cell 2 2" );
        add( cbo_k2m, "cell 3 2" );
        add(state_k2m, "cell 4 2");
        
        add( lbl_k3m, "cell 2 3" );
        add( cbo_k3m, "cell 3 3" );
        add(state_k3m, "cell 4 3");

      	add( lbl_gor, "cell 2 4" );
        add( cbo_gor, "cell 3 4" );
        add(state_gor, "cell 4 4");
        
        add( lbl_br, "cell 2 5" );
        add( cbo_br, "cell 3 5" );
        add(state_br, "cell 4 5");
        
        add( lbl_abr, "cell 2 6" );
        add( cbo_abr, "cell 3 6" );
        add(state_abr, "cell 4 6");

        add( lbl_scr, "cell 2 7" );
        add( cbo_scr, "cell 3 7" );
        add(state_scr, "cell 4 7");
        
        add( lbl_scr1, "cell 2 8" );
        add( cbo_scr1, "cell 3 8" );
        add(state_scr1, "cell 4 8");
        
        add( lbl_scr2, "cell 2 9" );
        add( cbo_scr2, "cell 3 9" );
        add(state_scr2, "cell 4 9");
      
        add( lbl_ccfr, "cell 2 10" );
        add( cbo_ccfr, "cell 3 10" );
        add(state_ccfr, "cell 4 10" );
        
        add( lbl_lvc2, "cell 2 11" );
        add( cbo_lvc2, "cell 3 11" );
        add(state_lvc1, "cell 4 11" );
        
        add( lbl_lvc2, "cell 2 12" );
        add( cbo_lvc2, "cell 3 12" );
        add(state_lvc2, "cell 4 12, wrap 30-12" );


        /* ---------------------------------------------------------------------------- */
        cpt_digital_output = new JLabel();
        lbl_enhw          = new JLabel();
        cbo_enhw          = new MyComboBox( combo_values );
        
        lbl_fwd          = new JLabel();
        cbo_fwd          = new MyComboBox( combo_values );
        
        lbl_rev          = new JLabel();
        cbo_rev          = new MyComboBox( combo_values );
        
        lbl_epr          = new JLabel();
        cbo_epr          = new MyComboBox( combo_values );
        
        lbl_rel          = new JLabel();
        cbo_rel          = new MyComboBox( combo_values );
        
        lbl_ucmt          = new JLabel();
        cbo_ucmt          = new MyComboBox( combo_values );
        
        lbl_ucmr          = new JLabel();
        cbo_ucmr          = new MyComboBox( combo_values );
        
        SetJComboBoxEnable(false);
        
        setCaptionStyle( cpt_digital_output );

        // @CompoentSetting( lbl_enhw, cbo_enhw )
        setComboBoxLabelStyle( lbl_enhw );
        setComboBoxValueStyle( cbo_enhw );
        
        // @CompoentSetting( lbl_fwd, cbo_fwd )
        setComboBoxLabelStyle( lbl_fwd );
        setComboBoxValueStyle( cbo_fwd );
        
        // @CompoentSetting( lbl_rev, cbo_rev )
        setComboBoxLabelStyle( lbl_rev );
        setComboBoxValueStyle( cbo_rev );

        // @CompoentSetting( lbl_epr, cbo_epr )
        setComboBoxLabelStyle( lbl_epr );
        setComboBoxValueStyle( cbo_epr );
        
        // @CompoentSetting( lbl_rel, cbo_rel )
        setComboBoxLabelStyle( lbl_rel );
        setComboBoxValueStyle( cbo_rel );

        // @CompoentSetting( lbl_ucmt, cbo_ucmt )
        setComboBoxLabelStyle( lbl_ucmt );
        setComboBoxValueStyle( cbo_ucmt );
        
        // @CompoentSetting( lbl_ucmr, cbo_ucmr )
        setComboBoxLabelStyle( lbl_ucmr );
        setComboBoxValueStyle( cbo_ucmr );
        
        add( cpt_digital_output, "gapbottom 18-12, span, aligny center" );
        add( lbl_enhw, "cell 2 14" );
        add( cbo_enhw, "cell 3 14" );
        add(state_enhw, "cell 4 14");
        
        add( lbl_fwd, "cell 2 15" );
        add( cbo_fwd, "cell 3 15" );
        add(state_fwd, "cell 4 15");
        
        add( lbl_rev, "cell 2 16" );
        add( cbo_rev, "cell 3 16" );
        add(state_rev, "cell 4 16");
        
        add( lbl_epr, "cell 2 17" );
        add( cbo_epr, "cell 3 17" );
        add(state_epr, "cell 4 17");
        
        add( lbl_rel, "cell 2 18" );
        add( cbo_rel, "cell 3 18" );
        add(state_rel, "cell 4 18");
        
        add( lbl_ucmt, "cell 2 19" );
        add( cbo_ucmt, "cell 3 19" );
        add(state_ucmt, "cell 4 19");
        
        add( lbl_ucmr, "cell 2 20" );
        add( cbo_ucmr, "cell 3 20" );
        add(state_ucmr, "cell 4 20");
        
        /* ---------------------------------------------------------------------------- */
        /*bindGroup( "Rl1", lbl_k2m, cbo_k2m );
        bindGroup( "Rl2", lbl_k3m, cbo_k3m );
        bindGroup( "Rl3", lbl_gor, cbo_gor );
        bindGroup( "Rl4", lbl_br, cbo_br );
        bindGroup( "Rl5", lbl_abr, cbo_abr );
        bindGroup( "Rl6", lbl_scr, cbo_scr );
        bindGroup( "Rl10", lbl_ccfr, cbo_ccfr );
        bindGroup( "P101", lbl_enhw, cbo_enhw );
        bindGroup( "P102", lbl_fwd, cbo_fwd );
        bindGroup( "P103", lbl_rev, cbo_rev );
        bindGroup( "P104", lbl_epr, cbo_epr );
        bindGroup( "P105", lbl_rel, cbo_rel );
        bindGroup( "P106", lbl_ucmt, cbo_ucmt );
        bindGroup( "P107", lbl_ucmr, cbo_ucmr );
        */
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_relay_outputs.setText( getBundleText( "LBL_cpt_relay_outputs", "Relay outputs" ) );
        lbl_k2m.setText( getBundleText( "LBL_lbl_k2m", "K2M" ) );
        lbl_k3m.setText( getBundleText( "LBL_lbl_k3m", "K3M" ) );
        lbl_gor.setText( getBundleText( "LBL_lbl_gor", "GOR" ) );
        lbl_br.setText( getBundleText( "LBL_lbl_br", "BR" ) );
        lbl_abr.setText( getBundleText( "LBL_lbl_abr", "ABR" ) );
        lbl_scr.setText( getBundleText( "LBL_lbl_scr", "SCR" ) );
        lbl_scr1.setText( getBundleText( "LBL_lbl_sc1r", "SCR1" ) );
        lbl_scr2.setText( getBundleText( "LBL_lbl_scr2", "SCR2" ) );
        lbl_ccfr.setText( getBundleText( "LBL_lbl_ccfr", "CCFR" ) );
        lbl_lvc1.setText( getBundleText( "LBL_lbl_lvc1", "LVC1" ) );
        lbl_lvc2.setText( getBundleText( "LBL_lbl_lvc2", "LVC2" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_digital_output.setText( getBundleText( "LBL_cpt_digital_output", "Digital Output" ) );
        lbl_enhw.setText( getBundleText( "LBL_lbl_enhw", "ENHW" ) );
        lbl_fwd.setText( getBundleText( "LBL_lbl_fwd", "FWD" ) );
        lbl_rev.setText( getBundleText( "LBL_lbl_rev", "REV" ) );
        lbl_epr.setText( getBundleText( "LBL_lbl_epr", "EPB" ) );
        lbl_rel.setText( getBundleText( "LBL_lbl_rel", "REL" ) );
        lbl_ucmt.setText( getBundleText( "LBL_lbl_ucmt", "UCMTS" ) );
        lbl_ucmr.setText( getBundleText( "LBL_lbl_ucmr", "UCMR" ) );

        /* ---------------------------------------------------------------------------- */
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
        c.setForeground(Color.WHITE);
    }


    private void setComboBoxLabelStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
        c.setForeground(Color.WHITE);
    }
    
    private void setComboBoxLabelStyle2 ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_BOLD );
        c.setForeground(Color.WHITE);
    }

    private void setComboBoxValueStyle ( JComboBox<?> c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
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


    public RelayOutputsBean getRelayOutputsBean () throws ConvertException {
        RelayOutputsBean bean_relayOutputs = new RelayOutputsBean();
        
        bean_relayOutputs.setK2m( ( OutputSourceD01 )cbo_k2m.getSelectedItem() );
        bean_relayOutputs.setK3m( ( OutputSourceD01 )cbo_k3m.getSelectedItem() );
        bean_relayOutputs.setGor( ( OutputSourceD01 )cbo_gor.getSelectedItem() );
        bean_relayOutputs.setBr( ( OutputSourceD01 )cbo_br.getSelectedItem() );
        bean_relayOutputs.setAbr( ( OutputSourceD01 )cbo_abr.getSelectedItem() );
        bean_relayOutputs.setScr( ( OutputSourceD01 )cbo_scr.getSelectedItem() );
        bean_relayOutputs.setScr1( ( OutputSourceD01 )cbo_scr1.getSelectedItem() );
        bean_relayOutputs.setScr2( ( OutputSourceD01 )cbo_scr2.getSelectedItem() );
        bean_relayOutputs.setCcfr( ( OutputSourceD01 )cbo_ccfr.getSelectedItem() );
        bean_relayOutputs.setLvc1( ( OutputSourceD01 )cbo_lvc1.getSelectedItem() );
        bean_relayOutputs.setLvc2( ( OutputSourceD01 )cbo_lvc2.getSelectedItem() );

        return bean_relayOutputs;
    }


    public DigitalOutputBean getDigitalOutputBean () throws ConvertException {
        DigitalOutputBean bean_digitalOutput = new DigitalOutputBean();
        
        bean_digitalOutput.setEnhw( ( OutputSourceD01 )cbo_enhw.getSelectedItem() );
        bean_digitalOutput.setFwd( ( OutputSourceD01 )cbo_fwd.getSelectedItem() );
        bean_digitalOutput.setRev( ( OutputSourceD01 )cbo_rev.getSelectedItem() );
        bean_digitalOutput.setEpr( ( OutputSourceD01 )cbo_epr.getSelectedItem() );
        bean_digitalOutput.setRel( ( OutputSourceD01 )cbo_rel.getSelectedItem() );
        bean_digitalOutput.setUcmt( ( OutputSourceD01 )cbo_ucmt.getSelectedItem() );
        bean_digitalOutput.setUcmr( ( OutputSourceD01 )cbo_ucmr.getSelectedItem() );
        
        return bean_digitalOutput;
    }


    public void setRelayOutputsBean ( RelayOutputsBean bean_relayOutputs ) {
        this.cbo_k2m.setSelectedItem( bean_relayOutputs.getK2m() );
        this.cbo_k3m.setSelectedItem( bean_relayOutputs.getK3m() );
        this.cbo_gor.setSelectedItem( bean_relayOutputs.getGor() );
        this.cbo_br.setSelectedItem( bean_relayOutputs.getBr() );
        this.cbo_abr.setSelectedItem( bean_relayOutputs.getAbr() );
        this.cbo_scr.setSelectedItem( bean_relayOutputs.getScr() );
        this.cbo_scr1.setSelectedItem( bean_relayOutputs.getScr1() );
        this.cbo_scr2.setSelectedItem( bean_relayOutputs.getScr2() );
        this.cbo_ccfr.setSelectedItem( bean_relayOutputs.getCcfr() );
        this.cbo_lvc1.setSelectedItem( bean_relayOutputs.getLvc1() );
        this.cbo_lvc2.setSelectedItem( bean_relayOutputs.getLvc2() );
    }


    public void setDigitalOutputBean ( DigitalOutputBean bean_digitalOutput ) {
    	
        this.cbo_enhw.setSelectedItem( bean_digitalOutput.getEnhw() );
        this.cbo_fwd.setSelectedItem( bean_digitalOutput.getFwd() );
        this.cbo_rev.setSelectedItem( bean_digitalOutput.getRev() );
        this.cbo_epr.setSelectedItem( bean_digitalOutput.getEpr() );
        this.cbo_rel.setSelectedItem( bean_digitalOutput.getRel() );
        this.cbo_ucmt.setSelectedItem( bean_digitalOutput.getUcmt() );
        this.cbo_ucmr.setSelectedItem( bean_digitalOutput.getUcmr() );
    	
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


    public static OutputsA01 createPanel ( SettingPanel<OutputsA01> panel ) {
        OutputsA01 gui = new OutputsA01();
        gui.setSettingPanel( panel );
        return gui;
    }


    private ImageIcon toIcon ( Boolean state ) {
        if ( Boolean.TRUE.equals( state ) )
            return OUTPUT_ON_ICON;
        else if ( Boolean.FALSE.equals( state ) )
            return OUTPUT_OFF_ICON;
        return GRAY_ICON;
    }


    public void setOutputSourceState ( HashMap<OutputPinA01, Boolean> result ) {
        state_k2m.setIcon( toIcon( result.get( OutputPinA01.RL1 ) ) );
        state_k3m.setIcon( toIcon( result.get( OutputPinA01.RL2 ) ) );
        state_gor.setIcon( toIcon( result.get( OutputPinA01.RL3 ) ) );
        state_br.setIcon( toIcon( result.get( OutputPinA01.RL4 ) ) );
        state_abr.setIcon( toIcon( result.get( OutputPinA01.RL5 ) ) );
        state_scr.setIcon( toIcon( result.get( OutputPinA01.RL6 ) ) );
        state_ccfr.setIcon( toIcon( result.get( OutputPinA01.RL10 ) ) );
        state_enhw.setIcon( toIcon( result.get( OutputPinA01.P10_1 ) ) );
        state_fwd.setIcon( toIcon( result.get( OutputPinA01.P10_2 ) ) );
        state_rev.setIcon( toIcon( result.get( OutputPinA01.P10_3 ) ) );
        state_epr.setIcon( toIcon( result.get( OutputPinA01.P10_4 ) ) );
        state_rel.setIcon( toIcon( result.get( OutputPinA01.P10_5 ) ) );
        state_ucmt.setIcon( toIcon( result.get( OutputPinA01.P10_6 ) ) );
        state_ucmr.setIcon( toIcon( result.get( OutputPinA01.P10_7 ) ) );
    }

    public void start () {
        started = true;
    }

    public void stop () {
        started = false;
    }

    public static class DigitalOutputBean {
        private OutputSourceD01 enhw;
        private OutputSourceD01 fwd;
        private OutputSourceD01 rev;
        private OutputSourceD01 epr;
        private OutputSourceD01 rel;
        private OutputSourceD01 ucmt;
        private OutputSourceD01 ucmr;
		public OutputSourceD01 getEnhw() {
			return enhw;
		}
		public OutputSourceD01 getFwd() {
			return fwd;
		}
		public OutputSourceD01 getRev() {
			return rev;
		}
		public OutputSourceD01 getEpr() {
			return epr;
		}
		public OutputSourceD01 getRel() {
			return rel;
		}
		public OutputSourceD01 getUcmt() {
			return ucmt;
		}
		public OutputSourceD01 getUcmr() {
			return ucmr;
		}
		public void setEnhw(OutputSourceD01 enhw) {
			this.enhw = enhw;
		}
		public void setFwd(OutputSourceD01 fwd) {
			this.fwd = fwd;
		}
		public void setRev(OutputSourceD01 rev) {
			this.rev = rev;
		}
		public void setEpr(OutputSourceD01 epr) {
			this.epr = epr;
		}
		public void setRel(OutputSourceD01 rel) {
			this.rel = rel;
		}
		public void setUcmt(OutputSourceD01 ucmt) {
			this.ucmt = ucmt;
		}
		public void setUcmr(OutputSourceD01 ucmr) {
			this.ucmr = ucmr;
		}
        
    }




    public static class RelayOutputsBean {
        private OutputSourceD01 k2m;
        private OutputSourceD01 k3m;
        private OutputSourceD01 gor;
        private OutputSourceD01 br;
        private OutputSourceD01 abr;
        private OutputSourceD01 scr;
        private OutputSourceD01 scr1;
        private OutputSourceD01 scr2;
        private OutputSourceD01 ccfr;
        private OutputSourceD01 lvc1;
        private OutputSourceD01 lvc2;
		public OutputSourceD01 getK2m() {
			return k2m;
		}
		public OutputSourceD01 getK3m() {
			return k3m;
		}
		public OutputSourceD01 getGor() {
			return gor;
		}
		public OutputSourceD01 getBr() {
			return br;
		}
		public OutputSourceD01 getAbr() {
			return abr;
		}
		public OutputSourceD01 getScr() {
			return scr;
		}
		public OutputSourceD01 getScr1() {
			return scr1;
		}
		public OutputSourceD01 getScr2() {
			return scr2;
		}
		public OutputSourceD01 getCcfr() {
			return ccfr;
		}
		public OutputSourceD01 getLvc1() {
			return lvc1;
		}
		public OutputSourceD01 getLvc2() {
			return lvc2;
		}
		public void setK2m(OutputSourceD01 k2m) {
			this.k2m = k2m;
		}
		public void setK3m(OutputSourceD01 k3m) {
			this.k3m = k3m;
		}
		public void setGor(OutputSourceD01 gor) {
			this.gor = gor;
		}
		public void setBr(OutputSourceD01 br) {
			this.br = br;
		}
		public void setAbr(OutputSourceD01 abr) {
			this.abr = abr;
		}
		public void setScr(OutputSourceD01 scr) {
			this.scr = scr;
		}
		public void setScr1(OutputSourceD01 scr1) {
			this.scr1 = scr1;
		}
		public void setScr2(OutputSourceD01 scr2) {
			this.scr2 = scr2;
		}
		public void setCcfr(OutputSourceD01 ccfr) {
			this.ccfr = ccfr;
		}
		public void setLvc1(OutputSourceD01 lvc1) {
			this.lvc1 = lvc1;
		}
		public void setLvc2(OutputSourceD01 lvc2) {
			this.lvc2 = lvc2;
		}
        
    }
    
}
