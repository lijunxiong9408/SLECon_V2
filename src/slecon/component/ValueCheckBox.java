package slecon.component;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import base.cfg.FontFactory;
import slecon.StartUI;

public class ValueCheckBox extends MyCheckBox {

    private static final long serialVersionUID = 9188834410201676656L;
    public ValueCheckBox() {
        super();
        init();
    }
    
    private void init() {
        setOpaque( false );
        setFont( FontFactory.FONT_12_PLAIN );
        setBackground(StartUI.SUB_BACKGROUND_COLOR);
        setForeground(Color.WHITE);
        getModel().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
            	originSelectedChanged = false;
                changeStyle();
            }
            
        });
        originSelected = isSelected();
    }

    private boolean originSelected;
    private boolean lastOriginSelected;
    private boolean originSelectedChanged;
    public void setOriginSelected(boolean selected) {
        originSelected = selected;
    	setSelectedValue(originSelected);
        lastOriginSelected = originSelected;
    }
    
    public void setSelectedValue( boolean selected ) {
    	super.setSelected( selected );
		changeStyle();
    }
    
    public void changeStyle() {
        if(!originSelectedChanged)
            originSelectedChanged = (lastOriginSelected!=originSelected);
        
        if (originSelectedChanged) {
            setOriginChangedStyle();
        }
        else {
            if (isSelected() != originSelected)
                setChangedStyle();
            else
                setDefaultStyle();
        }
    }
    

    protected void setChangedStyle() {
        setOpaque( true );
        this.setBackground( new Color( 0xD7DF01 ) );
    }
    

    protected void setOriginChangedStyle() {
        setOpaque( true );
        this.setBackground( new Color( 0x088A85 ) );
    }

    /**
     * if the input is valid, then chagne this style.
     */
    protected void setDefaultStyle () {
        setOpaque( false );
    }


}
