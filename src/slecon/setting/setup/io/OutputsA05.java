package slecon.setting.setup.io;
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

import logic.io.crossbar.OutputPinA05;
import logic.io.crossbar.OutputSourceD01;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.component.SettingPanel;
import slecon.interfaces.ConvertException;
import base.cfg.FontFactory;
import base.cfg.ImageFactory;

public class OutputsA05 extends JPanel {
    private static final long      serialVersionUID = -6123382229791452623L;
    
    private static final ResourceBundle TEXT             = ToolBox.getResourceBundle( "setting.io.Outputs" );

    private static final ImageIcon OUTPUT_ON_ICON    = ImageFactory.LIGHT_BRIGHT_GREEN.icon( 16, 16 );
    private static final ImageIcon OUTPUT_OFF_ICON   = ImageFactory.LIGHT_DARK_GREEN.icon( 16, 16 );
    private static final ImageIcon GRAY_ICON        = ImageFactory.LIGHT_BLACK.icon( 16, 16 );


    private SettingPanel<OutputsA05>   settingPanel;
    private JLabel                  cpt_relay_outputs;
    private JLabel                  lbl_rl1;
    private JComboBox<OutputSourceD01> cbo_rl1;
    
    private JLabel                  lbl_rl2;
    private JComboBox<OutputSourceD01> cbo_rl2;
    
    private JLabel                  lbl_rl3;
    private JComboBox<OutputSourceD01> cbo_rl3;
    
    private JLabel                  lbl_rl4;
    private JComboBox<OutputSourceD01> cbo_rl4;
    
    private JLabel                  lbl_rl5;
    private JComboBox<OutputSourceD01> cbo_rl5;
   
    private JLabel                  lbl_rl6;
    private JComboBox<OutputSourceD01> cbo_rl6;
    
    private JLabel                  lbl_rl10;
    private JComboBox<OutputSourceD01> cbo_rl10;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel                  cpt_digital_output;
    private JLabel                  lbl_p10_1;
    private JComboBox<OutputSourceD01> cbo_p10_1;
    
    private JLabel                  lbl_p10_2;
    private JComboBox<OutputSourceD01> cbo_p10_2;
    
    private JLabel                  lbl_p10_3;
    private JComboBox<OutputSourceD01> cbo_p10_3;
    
    private JLabel                  lbl_p10_4;
    private JComboBox<OutputSourceD01> cbo_p10_4;
    
    private JLabel                  lbl_p10_5;
    private JComboBox<OutputSourceD01> cbo_p10_5;
    
    private JLabel                  lbl_p10_6;
    private JComboBox<OutputSourceD01> cbo_p10_6;
  
    private JLabel                  lbl_p10_7;
    private JComboBox<OutputSourceD01> cbo_p10_7;
    
    /* ---------------------------------------------------------------------------- */
    private JLabel  state_rl1;
    private JLabel  state_rl2;
    private JLabel  state_rl3;
    private JLabel  state_rl4;
    private JLabel  state_rl5;
    private JLabel  state_rl6;
    private JLabel  state_rl10;
    private JLabel  state_p101;
    private JLabel  state_p102;
    private JLabel  state_p103;
    private JLabel  state_p104;
    private JLabel  state_p105;
    private JLabel  state_p106;
    private JLabel  state_p107;
    private boolean started;


    public OutputsA05 () {
        initGUI();
    }


    public void setSettingPanel ( SettingPanel<OutputsA05> panel ) {
        this.settingPanel = panel;
    }

    public void SetJComboBoxEnable(boolean Enable) {
    	this.cbo_rl1.setEnabled(Enable);
    	this.cbo_rl2.setEnabled(Enable);
    	this.cbo_rl3.setEnabled(Enable);
    	this.cbo_rl4.setEnabled(Enable);
    	this.cbo_rl5.setEnabled(Enable);
    	this.cbo_rl6.setEnabled(Enable);
    	this.cbo_rl10.setEnabled(Enable);
    	this.cbo_p10_1.setEnabled(Enable);
    	this.cbo_p10_2.setEnabled(Enable);
    	this.cbo_p10_3.setEnabled(Enable);
    	this.cbo_p10_4.setEnabled(Enable);
    	this.cbo_p10_5.setEnabled(Enable);
    	this.cbo_p10_6.setEnabled(Enable);
    	this.cbo_p10_7.setEnabled(Enable);
    }
    
    
    private void initGUI () {
        OutputSourceD01[] combo_values = new OutputSourceD01[ OutputSourceD01.values().length ];

        // combo_values[0] = null;
        for ( int i = 0 ; i < OutputSourceD01.values().length ; i++ )
            combo_values[ i ] = OutputSourceD01.values()[ i ];
        setBackground( StartUI.SUB_BACKGROUND_COLOR );
        setLayout( new MigLayout( "ins 24, gap 10 6", "[40::40][20::20][][]" ) );
        cpt_relay_outputs = new JLabel();
        lbl_rl1           = new JLabel();
        cbo_rl1           = new JComboBox<>( combo_values );

        lbl_rl2           = new JLabel();
        cbo_rl2           = new JComboBox<>( combo_values );
        
        lbl_rl3           = new JLabel();
        cbo_rl3           = new JComboBox<>( combo_values );
        
        lbl_rl4           = new JLabel();
        cbo_rl4           = new JComboBox<>( combo_values );
        
        
        lbl_rl5           = new JLabel();
        cbo_rl5           = new JComboBox<>( combo_values );
        
        
        lbl_rl6           = new JLabel();
        cbo_rl6           = new JComboBox<>( combo_values );
        
        
        lbl_rl10          = new JLabel();
        cbo_rl10          = new JComboBox<>( combo_values );
        
        
        state_rl1         = new JLabel( GRAY_ICON );
        state_rl2         = new JLabel( GRAY_ICON );
        state_rl3         = new JLabel( GRAY_ICON );
        state_rl4         = new JLabel( GRAY_ICON );
        state_rl5         = new JLabel( GRAY_ICON );
        state_rl6         = new JLabel( GRAY_ICON );
        state_rl10        = new JLabel( GRAY_ICON );
        state_p101        = new JLabel( GRAY_ICON );
        state_p102        = new JLabel( GRAY_ICON );
        state_p103        = new JLabel( GRAY_ICON );
        state_p104        = new JLabel( GRAY_ICON );
        state_p105        = new JLabel( GRAY_ICON );
        state_p106        = new JLabel( GRAY_ICON );
        state_p107        = new JLabel( GRAY_ICON );
        setCaptionStyle( cpt_relay_outputs );

        // @CompoentSetting( lbl_rl1, cbo_rl1 )
        setComboBoxLabelStyle( lbl_rl1 );
        setComboBoxValueStyle( cbo_rl1 );

        // @CompoentSetting( lbl_rl2, cbo_rl2 )
        setComboBoxLabelStyle( lbl_rl2 );
        setComboBoxValueStyle( cbo_rl2 );
        
        // @CompoentSetting( lbl_rl3, cbo_rl3 )
        setComboBoxLabelStyle( lbl_rl3 );
        setComboBoxValueStyle( cbo_rl3 );
        
        // @CompoentSetting( lbl_rl4, cbo_rl4 )
        setComboBoxLabelStyle( lbl_rl4 );
        setComboBoxValueStyle( cbo_rl4 );
        
        // @CompoentSetting( lbl_rl5, cbo_rl5 )
        setComboBoxLabelStyle( lbl_rl5 );
        setComboBoxValueStyle( cbo_rl5 );
        
        // @CompoentSetting( lbl_rl6, cbo_rl6 )
        setComboBoxLabelStyle( lbl_rl6 );
        setComboBoxValueStyle( cbo_rl6 );
        
        // @CompoentSetting( lbl_rl10, cbo_rl10 )
        setComboBoxLabelStyle( lbl_rl10 );
        setComboBoxValueStyle( cbo_rl10 );
        
        add( cpt_relay_outputs, "gapbottom 18-12, span, aligny center" );
        add( lbl_rl1, "cell 2 2" );
        add( cbo_rl1, "cell 3 2" );
        add(state_rl1, "cell 5 2");
        
        add( lbl_rl2, "cell 2 3" );
        add( cbo_rl2, "cell 3 3" );
        add(state_rl2, "cell 5 3");

      	add( lbl_rl3, "cell 2 4" );
        add( cbo_rl3, "cell 3 4" );
        add(state_rl3, "cell 5 4");
        
        add( lbl_rl4, "cell 2 5" );
        add( cbo_rl4, "cell 3 5" );
        add(state_rl4, "cell 5 5");
        
        add( lbl_rl5, "cell 2 6" );
        add( cbo_rl5, "cell 3 6" );
        add(state_rl5, "cell 5 6");

        add( lbl_rl6, "cell 2 7" );
        add( cbo_rl6, "cell 3 7" );
        add(state_rl6, "cell 5 7");
      
        add( lbl_rl10, "cell 2 8" );
        add( cbo_rl10, "cell 3 8, wrap 30-12" );
        add(state_rl10, "cell 5 8, right, wrap 30-12");


        /* ---------------------------------------------------------------------------- */
        cpt_digital_output = new JLabel();
        lbl_p10_1          = new JLabel();
        cbo_p10_1          = new JComboBox<>( combo_values );
        
        lbl_p10_2          = new JLabel();
        cbo_p10_2          = new JComboBox<>( combo_values );
        
        lbl_p10_3          = new JLabel();
        cbo_p10_3          = new JComboBox<>( combo_values );
        
        lbl_p10_4          = new JLabel();
        cbo_p10_4          = new JComboBox<>( combo_values );
        
        lbl_p10_5          = new JLabel();
        cbo_p10_5          = new JComboBox<>( combo_values );
        
        lbl_p10_6          = new JLabel();
        cbo_p10_6          = new JComboBox<>( combo_values );
        
        lbl_p10_7          = new JLabel();
        cbo_p10_7          = new JComboBox<>( combo_values );
        
        SetJComboBoxEnable(false);
        
        setCaptionStyle( cpt_digital_output );

        // @CompoentSetting( lbl_p10_1, cbo_p10_1 )
        setComboBoxLabelStyle( lbl_p10_1 );
        setComboBoxValueStyle( cbo_p10_1 );
        
        // @CompoentSetting( lbl_p10_2, cbo_p10_2 )
        setComboBoxLabelStyle( lbl_p10_2 );
        setComboBoxValueStyle( cbo_p10_2 );
        
        // @CompoentSetting( lbl_p10_3, cbo_p10_3 )
        setComboBoxLabelStyle( lbl_p10_3 );
        setComboBoxValueStyle( cbo_p10_3 );

        // @CompoentSetting( lbl_p10_4, cbo_p10_4 )
        setComboBoxLabelStyle( lbl_p10_4 );
        setComboBoxValueStyle( cbo_p10_4 );
        
        // @CompoentSetting( lbl_p10_5, cbo_p10_5 )
        setComboBoxLabelStyle( lbl_p10_5 );
        setComboBoxValueStyle( cbo_p10_5 );

        // @CompoentSetting( lbl_p10_6, cbo_p10_6 )
        setComboBoxLabelStyle( lbl_p10_6 );
        setComboBoxValueStyle( cbo_p10_6 );
        
        // @CompoentSetting( lbl_p10_7, cbo_p10_7 )
        setComboBoxLabelStyle( lbl_p10_7 );
        setComboBoxValueStyle( cbo_p10_7 );
        
        add( cpt_digital_output, "gapbottom 18-12, span, aligny center" );
        add( lbl_p10_1, "cell 2 10" );
        add( cbo_p10_1, "cell 3 10" );
        add( lbl_p10_2, "cell 2 11" );
        add( cbo_p10_2, "cell 3 11" );
        add( lbl_p10_3, "cell 2 12" );
        add( cbo_p10_3, "cell 3 12" );
        add( lbl_p10_4, "cell 2 13" );
        add( cbo_p10_4, "cell 3 13" );
        add( lbl_p10_5, "cell 2 14" );
        add( cbo_p10_5, "cell 3 14" );
        add( lbl_p10_6, "cell 2 15" );
        add( cbo_p10_6, "cell 3 15" );
        add( lbl_p10_7, "cell 2 16" );
        add( cbo_p10_7, "cell 3 16" );

        add(state_p101, "cell 5 10");
        add(state_p102, "cell 5 11");
        add(state_p103, "cell 5 12");
        add(state_p104, "cell 5 13");
        add(state_p105, "cell 5 14");
        add(state_p106, "cell 5 15");
        add(state_p107, "cell 5 16");

        /* ---------------------------------------------------------------------------- */
        bindGroup( "Rl1", lbl_rl1, cbo_rl1 );
        bindGroup( "Rl2", lbl_rl2, cbo_rl2 );
        bindGroup( "Rl3", lbl_rl3, cbo_rl3 );
        bindGroup( "Rl4", lbl_rl4, cbo_rl4 );
        bindGroup( "Rl5", lbl_rl5, cbo_rl5 );
        bindGroup( "Rl6", lbl_rl6, cbo_rl6 );
        bindGroup( "Rl10", lbl_rl10, cbo_rl10 );
        bindGroup( "P101", lbl_p10_1, cbo_p10_1 );
        bindGroup( "P102", lbl_p10_2, cbo_p10_2 );
        bindGroup( "P103", lbl_p10_3, cbo_p10_3 );
        bindGroup( "P104", lbl_p10_4, cbo_p10_4 );
        bindGroup( "P105", lbl_p10_5, cbo_p10_5 );
        bindGroup( "P106", lbl_p10_6, cbo_p10_6 );
        bindGroup( "P107", lbl_p10_7, cbo_p10_7 );
        loadI18N();
        revalidate();
    }


    private void loadI18N () {
        cpt_relay_outputs.setText( getBundleText( "LBL_cpt_relay_outputs", "Relay outputs" ) );
        lbl_rl1.setText( getBundleText( "LBL_lbl_rl1", "RL1" ) );
        lbl_rl2.setText( getBundleText( "LBL_lbl_rl2", "RL2" ) );
        lbl_rl3.setText( getBundleText( "LBL_lbl_rl3", "RL3" ) );
        lbl_rl4.setText( getBundleText( "LBL_lbl_rl4", "RL4" ) );
        lbl_rl5.setText( getBundleText( "LBL_lbl_rl5", "RL5" ) );
        lbl_rl6.setText( getBundleText( "LBL_lbl_rl6", "RL6" ) );
        lbl_rl10.setText( getBundleText( "LBL_lbl_rl10", "RL10" ) );

        /* ---------------------------------------------------------------------------- */
        cpt_digital_output.setText( getBundleText( "LBL_cpt_digital_output", "Digital Output" ) );
        lbl_p10_1.setText( getBundleText( "LBL_lbl_p10_1", "P10.1" ) );
        lbl_p10_2.setText( getBundleText( "LBL_lbl_p10_2", "P10.2" ) );
        lbl_p10_3.setText( getBundleText( "LBL_lbl_p10_3", "P10.3" ) );
        lbl_p10_4.setText( getBundleText( "LBL_lbl_p10_4", "P10.4" ) );
        lbl_p10_5.setText( getBundleText( "LBL_lbl_p10_5", "P10.5" ) );
        lbl_p10_6.setText( getBundleText( "LBL_lbl_p10_6", "P10.6" ) );
        lbl_p10_7.setText( getBundleText( "LBL_lbl_p10_7", "P10.7" ) );

        /* ---------------------------------------------------------------------------- */
    }


    private void setCaptionStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_14_PLAIN );
    }


    private void setComboBoxLabelStyle ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
    }
    
    private void setComboBoxLabelStyle2 ( JComponent c ) {
        c.setFont( FontFactory.FONT_12_BOLD );
    }

    private void setComboBoxValueStyle ( JComboBox<?> c ) {
        c.setFont( FontFactory.FONT_12_PLAIN );
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
        
        bean_relayOutputs.setRl1( ( OutputSourceD01 )cbo_rl1.getSelectedItem() );
        bean_relayOutputs.setRl2( ( OutputSourceD01 )cbo_rl2.getSelectedItem() );
        bean_relayOutputs.setRl3( ( OutputSourceD01 )cbo_rl3.getSelectedItem() );
        bean_relayOutputs.setRl4( ( OutputSourceD01 )cbo_rl4.getSelectedItem() );
        bean_relayOutputs.setRl5( ( OutputSourceD01 )cbo_rl5.getSelectedItem() );
        bean_relayOutputs.setRl6( ( OutputSourceD01 )cbo_rl6.getSelectedItem() );
        bean_relayOutputs.setRl10( ( OutputSourceD01 )cbo_rl10.getSelectedItem() );
        
        return bean_relayOutputs;
    }


    public DigitalOutputBean getDigitalOutputBean () throws ConvertException {
        DigitalOutputBean bean_digitalOutput = new DigitalOutputBean();
        
        bean_digitalOutput.setP101( ( OutputSourceD01 )cbo_p10_1.getSelectedItem() );
        bean_digitalOutput.setP102( ( OutputSourceD01 )cbo_p10_2.getSelectedItem() );
        bean_digitalOutput.setP103( ( OutputSourceD01 )cbo_p10_3.getSelectedItem() );
        bean_digitalOutput.setP104( ( OutputSourceD01 )cbo_p10_4.getSelectedItem() );
        bean_digitalOutput.setP105( ( OutputSourceD01 )cbo_p10_5.getSelectedItem() );
        bean_digitalOutput.setP106( ( OutputSourceD01 )cbo_p10_6.getSelectedItem() );
        bean_digitalOutput.setP107( ( OutputSourceD01 )cbo_p10_7.getSelectedItem() );
        
        return bean_digitalOutput;
    }


    public void setRelayOutputsBean ( RelayOutputsBean bean_relayOutputs ) {
    	
        this.cbo_rl1.setSelectedItem( bean_relayOutputs.getRl1() );
        this.cbo_rl2.setSelectedItem( bean_relayOutputs.getRl2() );
        this.cbo_rl3.setSelectedItem( bean_relayOutputs.getRl3() );
        this.cbo_rl4.setSelectedItem( bean_relayOutputs.getRl4() );
        this.cbo_rl5.setSelectedItem( bean_relayOutputs.getRl5() );
        this.cbo_rl6.setSelectedItem( bean_relayOutputs.getRl6() );
        this.cbo_rl10.setSelectedItem( bean_relayOutputs.getRl10() );
    	
    }


    public void setDigitalOutputBean ( DigitalOutputBean bean_digitalOutput ) {
    	
        this.cbo_p10_1.setSelectedItem( bean_digitalOutput.getP101() );
        this.cbo_p10_2.setSelectedItem( bean_digitalOutput.getP102() );
        this.cbo_p10_3.setSelectedItem( bean_digitalOutput.getP103() );
        this.cbo_p10_4.setSelectedItem( bean_digitalOutput.getP104() );
        this.cbo_p10_5.setSelectedItem( bean_digitalOutput.getP105() );
        this.cbo_p10_6.setSelectedItem( bean_digitalOutput.getP106() );
        this.cbo_p10_7.setSelectedItem( bean_digitalOutput.getP107() );
    	
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


    public static OutputsA05 createPanel ( SettingPanel<OutputsA05> panel ) {
        OutputsA05 gui = new OutputsA05();
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


    public void setOutputSourceState ( HashMap<OutputPinA05, Boolean> result ) {
        state_rl1.setIcon( toIcon( result.get( OutputPinA05.RL1 ) ) );
        state_rl2.setIcon( toIcon( result.get( OutputPinA05.RL2 ) ) );
        state_rl3.setIcon( toIcon( result.get( OutputPinA05.RL3 ) ) );
        state_rl4.setIcon( toIcon( result.get( OutputPinA05.RL4 ) ) );
        state_rl5.setIcon( toIcon( result.get( OutputPinA05.RL5 ) ) );
        state_rl6.setIcon( toIcon( result.get( OutputPinA05.RL6 ) ) );
        state_rl10.setIcon( toIcon( result.get( OutputPinA05.RL10 ) ) );
        state_p101.setIcon( toIcon( result.get( OutputPinA05.P10_1 ) ) );
        state_p102.setIcon( toIcon( result.get( OutputPinA05.P10_2 ) ) );
        state_p103.setIcon( toIcon( result.get( OutputPinA05.P10_3 ) ) );
        state_p104.setIcon( toIcon( result.get( OutputPinA05.P10_4 ) ) );
        state_p105.setIcon( toIcon( result.get( OutputPinA05.P10_5 ) ) );
        state_p106.setIcon( toIcon( result.get( OutputPinA05.P10_6 ) ) );
        state_p107.setIcon( toIcon( result.get( OutputPinA05.P10_7 ) ) );
    }


    public void start () {
        started = true;
    }


    public void stop () {
        started = false;
    }


    public static class DigitalOutputBean {
        private OutputSourceD01 p101;
        private OutputSourceD01 p102;
        private OutputSourceD01 p103;
        private OutputSourceD01 p104;
        private OutputSourceD01 p105;
        private OutputSourceD01 p106;
        private OutputSourceD01 p107;




        public OutputSourceD01 getP101 () {
            return this.p101;
        }


        public OutputSourceD01 getP102 () {
            return this.p102;
        }


        public OutputSourceD01 getP103 () {
            return this.p103;
        }


        public OutputSourceD01 getP104 () {
            return this.p104;
        }


        public OutputSourceD01 getP105 () {
            return this.p105;
        }


        public OutputSourceD01 getP106 () {
            return this.p106;
        }


        public OutputSourceD01 getP107 () {
            return this.p107;
        }


        public void setP101 ( OutputSourceD01 p101 ) {
            this.p101 = p101;
        }


        public void setP102 ( OutputSourceD01 p102 ) {
            this.p102 = p102;
        }


        public void setP103 ( OutputSourceD01 p103 ) {
            this.p103 = p103;
        }


        public void setP104 ( OutputSourceD01 p104 ) {
            this.p104 = p104;
        }


        public void setP105 ( OutputSourceD01 p105 ) {
            this.p105 = p105;
        }


        public void setP106 ( OutputSourceD01 p106 ) {
            this.p106 = p106;
        }


        public void setP107 ( OutputSourceD01 p107 ) {
            this.p107 = p107;
        }
    }




    public static class RelayOutputsBean {
        private OutputSourceD01 rl1;
        private OutputSourceD01 rl2;
        private OutputSourceD01 rl3;
        private OutputSourceD01 rl4;
        private OutputSourceD01 rl5;
        private OutputSourceD01 rl6;
        private OutputSourceD01 rl10;




        public OutputSourceD01 getRl1 () {
            return this.rl1;
        }


        public OutputSourceD01 getRl2 () {
            return this.rl2;
        }


        public OutputSourceD01 getRl3 () {
            return this.rl3;
        }


        public OutputSourceD01 getRl4 () {
            return this.rl4;
        }


        public OutputSourceD01 getRl5 () {
            return this.rl5;
        }


        public OutputSourceD01 getRl6 () {
            return this.rl6;
        }


        public OutputSourceD01 getRl10 () {
            return this.rl10;
        }


        public void setRl1 ( OutputSourceD01 rl1 ) {
            this.rl1 = rl1;
        }


        public void setRl2 ( OutputSourceD01 rl2 ) {
            this.rl2 = rl2;
        }


        public void setRl3 ( OutputSourceD01 rl3 ) {
            this.rl3 = rl3;
        }


        public void setRl4 ( OutputSourceD01 rl4 ) {
            this.rl4 = rl4;
        }


        public void setRl5 ( OutputSourceD01 rl5 ) {
            this.rl5 = rl5;
        }


        public void setRl6 ( OutputSourceD01 rl6 ) {
            this.rl6 = rl6;
        }


        public void setRl10 ( OutputSourceD01 rl10 ) {
            this.rl10 = rl10;
        }
    }
}
