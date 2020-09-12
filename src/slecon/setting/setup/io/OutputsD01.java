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

import logic.io.crossbar.OutputPinD01;
import logic.io.crossbar.OutputSourceD01;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.MyComboBox;
import slecon.component.SettingPanel;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;

public class OutputsD01 extends JPanel {
    private static final long      serialVersionUID = -6123382229791452623L;
    
    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.io.OutputsD01" );

    private static final ImageIcon OUTPUT_ON_ICON    = ImageFactory.LIGHT_BRIGHT_GREEN.icon( 16, 16 );
    private static final ImageIcon OUTPUT_OFF_ICON   = ImageFactory.LIGHT_DARK_GREEN.icon( 16, 16 );
    private static final ImageIcon GRAY_ICON        = ImageFactory.LIGHT_BLACK.icon( 16, 16 );



    private SettingPanel<OutputsD01>   settingPanel;
    private JLabel                  cpt_relay_outputs;
    private JLabel                  lbl_k1;
    private MyComboBox				cbo_k1;
    private JLabel  				state_k1;
    
    private JLabel                  lbl_k2;
    private MyComboBox				cbo_k2;
    private JLabel  			 	state_k2;
    
    private JLabel                  lbl_go;
    private MyComboBox 				cbo_go;
    private JLabel  				state_go;
    
    private JLabel                  lbl_br;
    private MyComboBox 				cbo_br;
    private JLabel  				state_br;
    
    private JLabel                  lbl_abr;
    private MyComboBox 				cbo_abr;
    private JLabel  				state_abr;
   
    private JLabel                  lbl_spc;
    private MyComboBox 				cbo_spc;
    private JLabel  				state_spc;
    
    private JLabel                  lbl_sc1;
    private MyComboBox 				cbo_sc1;
    private JLabel  				state_sc1;
    
    private JLabel                  lbl_sc2;
    private MyComboBox 				cbo_sc2;
    private JLabel  				state_sc2;

    private JLabel                  lbl_sc3;
    private MyComboBox 				cbo_sc3;
    private JLabel  				state_sc3;
    
    private JLabel                  lbl_ccf;
    private MyComboBox 				cbo_ccf;
    private JLabel  				state_ccf;
    
    private JLabel                  lbl_lvc;
    private MyComboBox 				cbo_lvc;
    private JLabel  				state_lvc;
    
    private JLabel                  lbl_lvc1;
    private MyComboBox 				cbo_lvc1;
    private JLabel  				state_lvc1;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel                  cpt_digital_output;
    private JLabel                  lbl_fwd;
    private MyComboBox 				cbo_fwd;
    private JLabel  				state_fwd;
    
    private JLabel                  lbl_rev;
    private MyComboBox 				cbo_rev;
    private JLabel  				state_rev;
    
    private JLabel                  lbl_epb;
    private MyComboBox 				cbo_epb;
    private JLabel  				state_epb;
    
    private JLabel                  lbl_do1;
    private MyComboBox 				cbo_do1;
    private JLabel  				state_do1;

    private boolean 				started;
    
    public OutputsD01 () {
    	initGUI();
    }


    public void setSettingPanel ( SettingPanel<OutputsD01> panel ) {
        this.settingPanel = panel;
    }

    public void SetJComboBoxEnable(boolean Enable) {
    	this.cbo_k1.setEnabled(Enable);
    	this.cbo_k2.setEnabled(Enable);
    	this.cbo_go.setEnabled(Enable);
    	this.cbo_br.setEnabled(Enable);
    	this.cbo_abr.setEnabled(Enable);
    	this.cbo_spc.setEnabled(Enable);
    	this.cbo_sc1.setEnabled(Enable);
    	this.cbo_sc2.setEnabled(Enable);
    	this.cbo_sc3.setEnabled(Enable);
    	this.cbo_ccf.setEnabled(Enable);
    	this.cbo_lvc.setEnabled(Enable);
    	this.cbo_lvc1.setEnabled(Enable);
    	
    	this.cbo_fwd.setEnabled(Enable);
    	this.cbo_rev.setEnabled(Enable);
    	this.cbo_epb.setEnabled(Enable);
    	this.cbo_do1.setEnabled(Enable);
    }
    
    
    private void initGUI () {
        OutputSourceD01[] combo_values = new OutputSourceD01[ OutputSourceD01.values().length ];

        // combo_values[0] = null;
        for ( int i = 0 ; i < OutputSourceD01.values().length ; i++ )
            combo_values[ i ] = OutputSourceD01.values()[ i ];
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "ins 24, gap 10 6", "[30::30][20::20][50::50][280::280][50::50][]" ) );
        cpt_relay_outputs = new JLabel();
        lbl_k1           = new JLabel();
        cbo_k1           = new MyComboBox( combo_values );

        lbl_k2           = new JLabel();
        cbo_k2           = new MyComboBox( combo_values );
        
        lbl_go           = new JLabel();
        cbo_go           = new MyComboBox( combo_values );
        
        lbl_br           = new JLabel();
        cbo_br           = new MyComboBox( combo_values );
        
        lbl_abr           = new JLabel();
        cbo_abr           = new MyComboBox( combo_values );
        
        lbl_spc           = new JLabel();
        cbo_spc           = new MyComboBox( combo_values );
        
        lbl_sc1           = new JLabel();
        cbo_sc1           = new MyComboBox( combo_values );
        
        lbl_sc2           = new JLabel();
        cbo_sc2           = new MyComboBox( combo_values );
        
        lbl_sc3           = new JLabel();
        cbo_sc3           = new MyComboBox( combo_values );
        
        lbl_ccf          = new JLabel();
        cbo_ccf          = new MyComboBox( combo_values );
        
        lbl_lvc          = new JLabel();
        cbo_lvc          = new MyComboBox( combo_values );
        
        lbl_lvc1          = new JLabel();
        cbo_lvc1         = new MyComboBox( combo_values );
        
        state_k1         = new JLabel( GRAY_ICON );
        state_k2         = new JLabel( GRAY_ICON );
        state_go         = new JLabel( GRAY_ICON );
        state_br         = new JLabel( GRAY_ICON );
        state_abr         = new JLabel( GRAY_ICON );
        state_spc         = new JLabel( GRAY_ICON );
        state_sc1         = new JLabel( GRAY_ICON );
        state_sc2         = new JLabel( GRAY_ICON );
        state_sc3         = new JLabel( GRAY_ICON );
        state_ccf        = new JLabel( GRAY_ICON );
        state_lvc        = new JLabel( GRAY_ICON );
        state_lvc1        = new JLabel( GRAY_ICON );
        
        state_fwd        = new JLabel( GRAY_ICON );
        state_rev        = new JLabel( GRAY_ICON );
        state_epb        = new JLabel( GRAY_ICON );
        state_do1        = new JLabel( GRAY_ICON );
        setCaptionStyle( cpt_relay_outputs );

        // @CompoentSetting( lbl_rl1, cbo_rl1 )
        setComboBoxLabelStyle( lbl_k1 );
        setComboBoxValueStyle( cbo_k1 );

        // @CompoentSetting( lbl_k2, cbo_k2 )
        setComboBoxLabelStyle( lbl_k2 );
        setComboBoxValueStyle( cbo_k2 );
        
        // @CompoentSetting( lbl_go, cbo_go )
        setComboBoxLabelStyle( lbl_go );
        setComboBoxValueStyle( cbo_go );
        
        // @CompoentSetting( lbl_br, cbo_br )
        setComboBoxLabelStyle( lbl_br );
        setComboBoxValueStyle( cbo_br );
        
        // @CompoentSetting( lbl_abr, cbo_abr )
        setComboBoxLabelStyle( lbl_abr );
        setComboBoxValueStyle( cbo_abr );
        
        // @CompoentSetting( lbl_spc, cbo_spc )
        setComboBoxLabelStyle( lbl_spc );
        setComboBoxValueStyle( cbo_spc );
        
        // @CompoentSetting( lbl_sc1, cbo_sc1 )
        setComboBoxLabelStyle( lbl_sc1 );
        setComboBoxValueStyle( cbo_sc1 );
        
        // @CompoentSetting( lbl_sc2, cbo_sc2 )
        setComboBoxLabelStyle( lbl_sc2 );
        setComboBoxValueStyle( cbo_sc2 );
        
        // @CompoentSetting( lbl_spc3, cbo_sc3 )
        setComboBoxLabelStyle( lbl_sc3 );
        setComboBoxValueStyle( cbo_sc3 );
        
        // @CompoentSetting( lbl_ccf, cbo_ccf )
        setComboBoxLabelStyle( lbl_ccf );
        setComboBoxValueStyle( cbo_ccf );
        
        // @CompoentSetting( lbl_lvc, cbo_lvc )
        setComboBoxLabelStyle( lbl_lvc );
        setComboBoxValueStyle( cbo_lvc );
        
        // @CompoentSetting( lbl_lvc1, cbo_lvc1 )
        setComboBoxLabelStyle( lbl_lvc1 );
        setComboBoxValueStyle( cbo_lvc1 );
        
        add( cpt_relay_outputs, "gapbottom 18-12, span, aligny center" );
        add( lbl_k1, "cell 2 2" );
        add( cbo_k1, "cell 3 2" );
        add(state_k1, "cell 4 2");
        
        add( lbl_k2, "cell 2 3" );
        add( cbo_k2, "cell 3 3" );
        add(state_k2, "cell 4 3");

      	add( lbl_go, "cell 2 4" );
        add( cbo_go, "cell 3 4" );
        add(state_go, "cell 4 4");
        
        add( lbl_br, "cell 2 5" );
        add( cbo_br, "cell 3 5" );
        add(state_br, "cell 4 5");
        
        add( lbl_abr, "cell 2 6" );
        add( cbo_abr, "cell 3 6" );
        add(state_abr, "cell 4 6");

        add( lbl_spc, "cell 2 7" );
        add( cbo_spc, "cell 3 7" );
        add(state_spc, "cell 4 7");
        
        add( lbl_sc1, "cell 2 8" );
        add( cbo_sc1, "cell 3 8" );
        add(state_sc1, "cell 4 8");
        
        add( lbl_sc2, "cell 2 9" );
        add( cbo_sc2, "cell 3 9" );
        add(state_sc2, "cell 4 9");
        
        add( lbl_sc3, "cell 2 10" );
        add( cbo_sc3, "cell 3 10" );
        add(state_sc3, "cell 4 10");
      
        add( lbl_ccf, "cell 2 11" );
        add( cbo_ccf, "cell 3 11" );
        add(state_ccf, "cell 4 11" );
        
        add( lbl_lvc, "cell 2 12" );
        add( cbo_lvc, "cell 3 12" );
        add(state_lvc, "cell 4 12" );
        
        add( lbl_lvc1, "cell 2 13" );
        add( cbo_lvc1, "cell 3 13" );
        add(state_lvc1, "cell 4 13, wrap 30-12" );


        /* ---------------------------------------------------------------------------- */
        cpt_digital_output = new JLabel();
        lbl_fwd          = new JLabel();
        cbo_fwd          = new MyComboBox( combo_values );
        
        lbl_rev          = new JLabel();
        cbo_rev          = new MyComboBox( combo_values );
        
        lbl_epb          = new JLabel();
        cbo_epb          = new MyComboBox( combo_values );
        
        lbl_do1          = new JLabel();
        cbo_do1          = new MyComboBox( combo_values );

        SetJComboBoxEnable(false);
        
        setCaptionStyle( cpt_digital_output );

        // @CompoentSetting( lbl_fwd, cbo_fwd )
        setComboBoxLabelStyle( lbl_fwd );
        setComboBoxValueStyle( cbo_fwd );
        
        // @CompoentSetting( lbl_rev, cbo_rev )
        setComboBoxLabelStyle( lbl_rev );
        setComboBoxValueStyle( cbo_rev );

        // @CompoentSetting( lbl_epb, cbo_epb )
        setComboBoxLabelStyle( lbl_epb );
        setComboBoxValueStyle( cbo_epb );

        // @CompoentSetting( lbl_do1, cbo_do1 )
        setComboBoxLabelStyle( lbl_do1 );
        setComboBoxValueStyle( cbo_do1 );
        
        add( cpt_digital_output, "gapbottom 18-12, span, aligny center" );
        add( lbl_fwd, "cell 2 15" );
        add( cbo_fwd, "cell 3 15" );
        add(state_fwd, "cell 4 15");
        
        add( lbl_rev, "cell 2 16" );
        add( cbo_rev, "cell 3 16" );
        add(state_rev, "cell 4 16");
        
        add( lbl_epb, "cell 2 17" );
        add( cbo_epb, "cell 3 17" );
        add(state_epb, "cell 4 17");
        
        add( lbl_do1, "cell 2 18" );
        add( cbo_do1, "cell 3 18" );
        add(state_do1, "cell 4 18");
        
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_relay_outputs.setText( getBundleText( "LBL_cpt_relay_outputs", "Relay outputs" ) );
        lbl_k1.setText( getBundleText( "LBL_lbl_k1", "K2M" ) );
        lbl_k2.setText( getBundleText( "LBL_lbl_k2", "K3M" ) );
        lbl_go.setText( getBundleText( "LBL_lbl_go", "GOR" ) );
        lbl_br.setText( getBundleText( "LBL_lbl_br", "BR" ) );
        lbl_abr.setText( getBundleText( "LBL_lbl_abr", "ABR" ) );
        lbl_spc.setText( getBundleText( "LBL_lbl_spc", "SPC" ) );
        lbl_sc1.setText( getBundleText( "LBL_lbl_sc1", "SC1" ) );
        lbl_sc2.setText( getBundleText( "LBL_lbl_sc2", "SC2" ) );
        lbl_sc3.setText( getBundleText( "LBL_lbl_sc3", "SC3" ) );
        lbl_ccf.setText( getBundleText( "LBL_lbl_ccf", "CCFR" ) );
        lbl_lvc.setText( getBundleText( "LBL_lbl_lvc", "LVC" ) );
        lbl_lvc1.setText( getBundleText( "LBL_lbl_lvc1", "LVC1" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_digital_output.setText( getBundleText( "LBL_cpt_digital_output", "Digital Output" ) );
        lbl_fwd.setText( getBundleText( "LBL_lbl_fwd", "FWD" ) );
        lbl_rev.setText( getBundleText( "LBL_lbl_rev", "REV" ) );
        lbl_epb.setText( getBundleText( "LBL_lbl_epb", "EPB" ) );
        lbl_do1.setText( getBundleText( "LBL_lbl_do1", "UCMTS" ) );

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
        
        bean_relayOutputs.setK1( ( OutputSourceD01 )cbo_k1.getSelectedItem() );
        bean_relayOutputs.setK2( ( OutputSourceD01 )cbo_k2.getSelectedItem() );
        bean_relayOutputs.setGo( ( OutputSourceD01 )cbo_go.getSelectedItem() );
        bean_relayOutputs.setBr( ( OutputSourceD01 )cbo_br.getSelectedItem() );
        bean_relayOutputs.setAbr( ( OutputSourceD01 )cbo_abr.getSelectedItem() );
        bean_relayOutputs.setSpc( ( OutputSourceD01 )cbo_spc.getSelectedItem() );
        bean_relayOutputs.setSc1( ( OutputSourceD01 )cbo_sc1.getSelectedItem() );
        bean_relayOutputs.setSc2( ( OutputSourceD01 )cbo_sc2.getSelectedItem() );
        bean_relayOutputs.setSc3( ( OutputSourceD01 )cbo_sc3.getSelectedItem() );
        bean_relayOutputs.setCcf( ( OutputSourceD01 )cbo_ccf.getSelectedItem() );
        bean_relayOutputs.setLvc( ( OutputSourceD01 )cbo_lvc.getSelectedItem() );
        bean_relayOutputs.setLvc1( ( OutputSourceD01 )cbo_lvc1.getSelectedItem() );

        return bean_relayOutputs;
    }


    public DigitalOutputBean getDigitalOutputBean () throws ConvertException {
        DigitalOutputBean bean_digitalOutput = new DigitalOutputBean();
        
        bean_digitalOutput.setFwd( ( OutputSourceD01 )cbo_fwd.getSelectedItem() );
        bean_digitalOutput.setRev( ( OutputSourceD01 )cbo_rev.getSelectedItem() );
        bean_digitalOutput.setEpb( ( OutputSourceD01 )cbo_epb.getSelectedItem() );
        bean_digitalOutput.setDo1( ( OutputSourceD01 )cbo_do1.getSelectedItem() );
        
        return bean_digitalOutput;
    }


    public void setRelayOutputsBean ( RelayOutputsBean bean_relayOutputs ) {
        this.cbo_k1.setSelectedItem( bean_relayOutputs.getK1() != null ? bean_relayOutputs.getK1() : OutputSourceD01.ALWAYS0 );
        this.cbo_k2.setSelectedItem( bean_relayOutputs.getK2() != null ? bean_relayOutputs.getK2() : OutputSourceD01.ALWAYS0 );
        this.cbo_go.setSelectedItem( bean_relayOutputs.getGo() != null ? bean_relayOutputs.getGo() : OutputSourceD01.ALWAYS0 );
        this.cbo_br.setSelectedItem( bean_relayOutputs.getBr() != null ? bean_relayOutputs.getBr() : OutputSourceD01.ALWAYS0 );
        this.cbo_abr.setSelectedItem( bean_relayOutputs.getAbr() != null ? bean_relayOutputs.getAbr() : OutputSourceD01.ALWAYS0 );
        this.cbo_spc.setSelectedItem( bean_relayOutputs.getSpc() != null ? bean_relayOutputs.getSpc() : OutputSourceD01.ALWAYS0 );
        this.cbo_sc1.setSelectedItem( bean_relayOutputs.getSc1() != null ? bean_relayOutputs.getSc1() : OutputSourceD01.ALWAYS0 );
        this.cbo_sc2.setSelectedItem( bean_relayOutputs.getSc2() != null ? bean_relayOutputs.getSc2() : OutputSourceD01.ALWAYS0 );
        this.cbo_sc3.setSelectedItem( bean_relayOutputs.getSc3() != null ? bean_relayOutputs.getSc3() : OutputSourceD01.ALWAYS0 );
        this.cbo_ccf.setSelectedItem( bean_relayOutputs.getCcf() != null ? bean_relayOutputs.getCcf() : OutputSourceD01.ALWAYS0 );
        this.cbo_lvc.setSelectedItem( bean_relayOutputs.getLvc() != null ? bean_relayOutputs.getLvc() : OutputSourceD01.ALWAYS0 );
        this.cbo_lvc1.setSelectedItem( bean_relayOutputs.getLvc1() != null ? bean_relayOutputs.getLvc1() : OutputSourceD01.ALWAYS0 );
    }


    public void setDigitalOutputBean ( DigitalOutputBean bean_digitalOutput ) {
        this.cbo_fwd.setSelectedItem( bean_digitalOutput.getFwd() != null ? bean_digitalOutput.getFwd() : OutputSourceD01.ALWAYS0 );
        this.cbo_rev.setSelectedItem( bean_digitalOutput.getRev() != null ? bean_digitalOutput.getRev() : OutputSourceD01.ALWAYS0 );
        this.cbo_epb.setSelectedItem( bean_digitalOutput.getEpb() != null ? bean_digitalOutput.getEpb() : OutputSourceD01.ALWAYS0 );
        this.cbo_do1.setSelectedItem( bean_digitalOutput.getDo1() != null ? bean_digitalOutput.getDo1() : OutputSourceD01.ALWAYS0 );
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


    public static OutputsD01 createPanel ( SettingPanel<OutputsD01> panel ) {
        OutputsD01 gui = new OutputsD01();
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


    public void setOutputSourceState ( HashMap<OutputPinD01, Boolean> result ) {
        state_k1.setIcon( toIcon( result.get( OutputPinD01.K1 ) ) );
        state_k2.setIcon( toIcon( result.get( OutputPinD01.K2 ) ) );
        state_go.setIcon( toIcon( result.get( OutputPinD01.GO ) ) );
        state_br.setIcon( toIcon( result.get( OutputPinD01.BR ) ) );
        state_abr.setIcon( toIcon( result.get( OutputPinD01.ABR ) ) );
        state_spc.setIcon( toIcon( result.get( OutputPinD01.SPC ) ) );
        state_sc1.setIcon( toIcon( result.get( OutputPinD01.SC1 ) ) );
        state_sc2.setIcon( toIcon( result.get( OutputPinD01.SC2 ) ) );
        state_sc3.setIcon( toIcon( result.get( OutputPinD01.SC3 ) ) );
        state_ccf.setIcon( toIcon( result.get( OutputPinD01.CCF ) ) );
        state_lvc.setIcon( toIcon( result.get( OutputPinD01.LVC ) ) );
        state_lvc1.setIcon( toIcon( result.get( OutputPinD01.LVC1 ) ) );
        state_fwd.setIcon( toIcon( result.get( OutputPinD01.FWD ) ) );
        state_rev.setIcon( toIcon( result.get( OutputPinD01.REV ) ) );
        state_epb.setIcon( toIcon( result.get( OutputPinD01.EPB ) ) );
        state_do1.setIcon( toIcon( result.get( OutputPinD01.DO1 ) ) );
    }

    public void start () {
        started = true;
    }

    public void stop () {
        started = false;
    }

    public static class DigitalOutputBean {
        private OutputSourceD01 fwd;
        private OutputSourceD01 rev;
        private OutputSourceD01 epb;
        private OutputSourceD01 do1;
		public OutputSourceD01 getFwd() {
			return fwd;
		}
		public OutputSourceD01 getRev() {
			return rev;
		}
		public OutputSourceD01 getEpb() {
			return epb;
		}
		public OutputSourceD01 getDo1() {
			return do1;
		}
		public void setFwd(OutputSourceD01 fwd) {
			this.fwd = fwd;
		}
		public void setRev(OutputSourceD01 rev) {
			this.rev = rev;
		}
		public void setEpb(OutputSourceD01 epb) {
			this.epb = epb;
		}
		public void setDo1(OutputSourceD01 do1) {
			this.do1 = do1;
		}
    }




    public static class RelayOutputsBean {
        private OutputSourceD01 k1;
        private OutputSourceD01 k2;
        private OutputSourceD01 go;
        private OutputSourceD01 br;
        private OutputSourceD01 abr;
        private OutputSourceD01 spc;
        private OutputSourceD01 sc1;
        private OutputSourceD01 sc2;
        private OutputSourceD01 sc3;
        private OutputSourceD01 ccf;
        private OutputSourceD01 lvc;
        private OutputSourceD01 lvc1;
		public OutputSourceD01 getK1() {
			return k1;
		}
		public OutputSourceD01 getK2() {
			return k2;
		}
		public OutputSourceD01 getGo() {
			return go;
		}
		public OutputSourceD01 getBr() {
			return br;
		}
		public OutputSourceD01 getAbr() {
			return abr;
		}
		public OutputSourceD01 getSpc() {
			return spc;
		}
		public OutputSourceD01 getSc1() {
			return sc1;
		}
		public OutputSourceD01 getSc2() {
			return sc2;
		}
		public OutputSourceD01 getSc3() {
			return sc3;
		}
		public OutputSourceD01 getCcf() {
			return ccf;
		}
		public OutputSourceD01 getLvc() {
			return lvc;
		}
		public OutputSourceD01 getLvc1() {
			return lvc1;
		}
		public void setK1(OutputSourceD01 k1) {
			this.k1 = k1;
		}
		public void setK2(OutputSourceD01 k2) {
			this.k2 = k2;
		}
		public void setGo(OutputSourceD01 go) {
			this.go = go;
		}
		public void setBr(OutputSourceD01 br) {
			this.br = br;
		}
		public void setAbr(OutputSourceD01 abr) {
			this.abr = abr;
		}
		public void setSpc(OutputSourceD01 spc) {
			this.spc = spc;
		}
		public void setSc1(OutputSourceD01 sc1) {
			this.sc1 = sc1;
		}
		public void setSc2(OutputSourceD01 sc2) {
			this.sc2 = sc2;
		}
		public void setSc3(OutputSourceD01 sc3) {
			this.sc3 = sc3;
		}
		public void setCcf(OutputSourceD01 ccf) {
			this.ccf = ccf;
		}
		public void setLvc(OutputSourceD01 lvc) {
			this.lvc = lvc;
		}
		public void setLvc1(OutputSourceD01 lvc1) {
			this.lvc1 = lvc1;
		}
        
    }
    
}
