package slecon.component;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import slecon.StartUI;
import slecon.ToolBox;
import slecon.home.PosButton;
import slecon.home.dashboard.HorizontalSrcollBarUI;
import slecon.home.dashboard.VerticalSrcollBarUI;
import slecon.setting.SetupPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;

import base.cfg.FontFactory;
import base.cfg.ImageFactory;
import logic.connection.LiftConnectionBean;

import com.jhlabs.image.BlurFilter;




public abstract class SettingPanel<T extends JPanel> extends JPanel implements ActionListener {
    private static final ResourceBundle TEXT       = ToolBox.getResourceBundle( "setting.SettingPanel" );
    private static final Logger         logger     = LogManager.getLogger( SettingPanel.class );
    private ImageIcon	 				BTN_IMAGE_PAUSE  = ImageFactory.BUTTON_PAUSE.icon(87,30);
    private ImageIcon	 				BTN_IMAGE_START  = ImageFactory.BUTTON_START.icon(87,30);
    private	LiftConnectionBean 			connBean;
    private HashMap<String, Class<? extends JPanel> >  navigation = null;
    private LockableUI                  busyLockUI = new LockableUI( new BufferedImageOpEffect( new BlurFilter() ) );
    private boolean                     enabled;
    private JLabel                      lblDetails;
    public  JPanel 						workSpace;
    public  JPanel                      main;
    protected PosButton                 btnOK;
    protected PosButton                 btnReset;
    private JXLayer<JComponent>         layer;
    protected T                         app;
    private SubtleSquareBorder			subBorder = new SubtleSquareBorder(true, StartUI.BORDER_COLOR);



    public SettingPanel (LiftConnectionBean connBean) {
    	this.connBean = connBean;
        initGUI();
        this.enabled = super.isEnabled();
        setMainPanel( getPanel() );
    }


    protected void setMainPanel ( T panel ) {
        main.removeAll();
        JScrollPane scrollApp = new JScrollPane( panel ) {
            private static final long serialVersionUID = 3728914992335934191L;
            {
            	setOpaque( false );
                getViewport().setOpaque( false );
                setViewportBorder( null );
                setBorder( null );
                getVerticalScrollBar().setUnitIncrement( 20 );
                getHorizontalScrollBar().setUnitIncrement(50);
                getVerticalScrollBar().setUI(new VerticalSrcollBarUI());
                getHorizontalScrollBar().setUI(new HorizontalSrcollBarUI());
            }
        };
        app = panel;
        main.add( scrollApp,"cell 0 0, span" );
        layer = new JXLayer<JComponent>( scrollApp );
        layer.setUI( busyLockUI );
        main.add( layer );
    }


    private void initGUI () {
        setLayout( new MigLayout( "ins 0 20 0 20", "[left, grow, fill]", "[30!]10[grow,fill]" ));
        setBackground(StartUI.SUB_BACKGROUND_COLOR);
        /*------------------------------------------------------------------------------------------------------------*/   
        navigation = getNavigation();
        if(navigation != null) {
        	JPanel panelNavigation = new JPanel(new MigLayout( "nogrid, w 985!, h 30!, gap 0", "[left]", "[center]" ));
            panelNavigation.setBackground(StartUI.SUB_BACKGROUND_COLOR);
            add(panelNavigation,"cell 0 0");
            int index = 0;
            for (final String title : navigation.keySet()) {
            	if(index > 0) {
            		PosButton icon = new PosButton(ImageFactory.ARROW_NAVIGATION.icon(11,12));
            		panelNavigation.add(icon);
    			}
            	
            	PosButton lab = new PosButton(title, StartUI.BORDER_COLOR, Color.WHITE);
            	lab.setForeground(StartUI.BORDER_COLOR);
            	lab.setFont(FontFactory.FONT_12_BOLD);
            	lab.addActionListener(new ActionListener() {
    				@Override
    				public void actionPerformed(ActionEvent e) {
    					// TODO Auto-generated method stub
    					if(navigation.get(title) != null) {
    						final SetupPanel panelBinder = SetupPanel.build(connBean, navigation.get(title));
    						StartUI.getTopMain().push(panelBinder);
    					}
    				}
    			});
    			panelNavigation.add(lab);
    			index += 1;
    		}
        }
        
        /*------------------------------------------------------------------------------------------------------------*/
        workSpace = new JPanel(new MigLayout( "ins 5 20 10 20", "[grow,fill]", "[20!, grow][grow,fill][20!][100px::100px,fill]" ));
        workSpace.setBackground(StartUI.SUB_BACKGROUND_COLOR);
        workSpace.setBorder(subBorder);
        add(workSpace,"cell 0 1");
        
        JLabel title = new JLabel();
        title.setFont(FontFactory.FONT_12_BOLD);
        title.setForeground(Color.WHITE);
        title.setText(getPanelTitle());
        workSpace.add( title, "cell 0 0, left" );
        
        main = new JPanel();
        main.setOpaque( false );
        main.setLayout( new MigLayout("gap 0, ins 0","[grow, fill]","[fill]") );
        main.setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR, 2));
        workSpace.add( main, "cell 0 1,grow" );
        
        JLabel desc = new JLabel();
        desc.setFont(FontFactory.FONT_12_BOLD);
        desc.setForeground(Color.WHITE);
        desc.setText(TEXT.getString( "Description.title" ));
        workSpace.add( desc, "cell 0 2, left" );
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque( false );
        workSpace.add( bottomPanel, "cell 0 3,growx,aligny top" );
        bottomPanel.setLayout( new MigLayout( "gap 0, ins 0", "[10px,grow,fill][120px::120px,fill]", "[100px,fill]" ) );
        
        JPanel DetailsPanel = new JPanel();
        DetailsPanel.setOpaque( false );
        DetailsPanel.setBorder( BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        bottomPanel.add( DetailsPanel, "cell 0 0,alignx left,aligny top" );
        DetailsPanel.setLayout( new MigLayout( "ins 12 24 12 24", "[grow,fill]", "[grow,fill]" ) );
        lblDetails = new JLabel();
        lblDetails.setVerticalAlignment( SwingConstants.TOP );
        lblDetails.setFont( FontFactory.FONT_11_PLAIN );
        lblDetails.setForeground(Color.WHITE);
        DetailsPanel.add( lblDetails, "cell 0 0,grow, wmax 560" );
        btnOK = new PosButton( TEXT.getString( "OK.text" ), BTN_IMAGE_PAUSE, BTN_IMAGE_START );
        btnOK.setFont( FontFactory.FONT_12_PLAIN );
        btnOK.addActionListener( this );
        bottomPanel.add( btnOK, "aligny center, gap 20 5 10 20, flowy,cell 1 0" );
        btnReset = new PosButton( TEXT.getString( "Reset.text" ), BTN_IMAGE_PAUSE, BTN_IMAGE_START );
        btnReset.setFont( FontFactory.FONT_12_PLAIN );
        btnReset.addActionListener( this );
        bottomPanel.add( btnReset, "gap 20 5 10 10, cell 1 0" );
    }


    @SuppressWarnings( "unchecked" )
    protected Class<? extends T> getPanelClass () {
        Type type = getClass().getGenericSuperclass();
        if ( type instanceof ParameterizedType )
            return ( Class<? extends T> )( ( ParameterizedType )type ).getActualTypeArguments()[ 0 ];
        return null;
    }


    protected abstract String getPanelTitle ();
    
    protected abstract HashMap<String, Class<? extends JPanel>> getNavigation ();

    public void setDescription ( String text ) {
        if ( text == null )
            text = "";
        lblDetails.setText( text );
    }


    @SuppressWarnings( "unchecked" )
    protected T getPanel () {
        Class<? extends T> localClass = getPanelClass();
        T                  result     = null;
        if ( result == null ) {
            try {
                Method localMethod = localClass.getDeclaredMethod( "createPanel", SettingPanel.class );
                result = ( T )localMethod.invoke( null, this );
            } catch ( IllegalArgumentException | NoSuchMethodException e ) {
            } catch ( IllegalAccessException | InvocationTargetException | SecurityException e ) {
                logger.error( "fail to invoke {}::createPanel", localClass );
            }
        }
        if ( result == null ) {
            try {
                result = localClass.newInstance();
            } catch ( InstantiationException | IllegalAccessException e ) {
                logger.info( "cannot initalize {} class", localClass );
            }
        }
        return result;
    }


    public void actionPerformed ( final ActionEvent e ) {
        if ( e.getSource() == btnReset ) {
            do_btnReset_actionPerformed( e );
        }
        if ( e.getSource() == btnOK ) {
            do_btnOK_actionPerformed( e );
        }
    }


    protected void do_btnOK_actionPerformed ( final ActionEvent e ) {
        onOK( app );
    }


    protected void do_btnReset_actionPerformed ( final ActionEvent e ) {
        onReset( app );
    }

    public boolean isEnabled () {
        return enabled;
    }

    public void setEnabled ( boolean enabled ) {
        this.enabled = enabled;
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                SettingPanel.super.setEnabled( SettingPanel.this.enabled );
                busyLockUI.setLocked( ! SettingPanel.this.enabled );
                btnOK.setEnabled( okButtonEnabled ? isEnabled() : false );
                btnReset.setEnabled( resetButtonEnabled ? isEnabled() : false );
            }
        });
    }
    
    private boolean okButtonEnabled = true;
    private boolean resetButtonEnabled = true;
    
    
    public void setOKButtonEnabled(boolean onoff) {
        okButtonEnabled = onoff;
        btnOK.setEnabled( okButtonEnabled ? isEnabled() : false );
    }

    
    public void setResetButtonEnabled(boolean onoff) {
        resetButtonEnabled = onoff;
        btnReset.setEnabled( resetButtonEnabled ? isEnabled() : false );
    }
    
    public void setResetText(String text) {
    	btnReset.setText(text);
    }

    public abstract void onOK ( T panel );


    public abstract void onReset ( T panel );


    public T getApp () {
        return app;
    }
}
