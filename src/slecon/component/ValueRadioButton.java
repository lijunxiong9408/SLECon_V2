package slecon.component;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import base.cfg.FontFactory;

public class ValueRadioButton extends JRadioButton {

    private static final long serialVersionUID = 9188834410201676656L;
    public ValueRadioButton() {
        super();
        init();
    }
    
    public ValueRadioButton(Icon icon) {
        super(icon);
        init();
    }

    public ValueRadioButton(Icon icon, boolean selected) {
        super(icon, selected);
        init();
    }
    
    public ValueRadioButton(String text) {
        super(text);
        init();
    }

    private void init() {
        setOpaque( false );
        setFont( FontFactory.FONT_12_PLAIN );
        setForeground(Color.WHITE);
        this.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                changeStyle();
            }
            
        });
        originSelected = isSelected();
    }

    private boolean originSelected;
    private boolean firstOriginSelected;
    private int originSelectedCount = 0;
    public void setOriginSelected(boolean selected) {
        originSelectedCount += 1;
        originSelected = selected;
        if (originSelectedCount<=1)
            firstOriginSelected = originSelected;
        setSelected(originSelected);
    }
    
    public void changeStyle() {
        if (firstOriginSelected!=originSelected)
            setOriginChangedStyle();
        else {
            if (isSelected() != originSelected)
                setChangedStyle();
            else
                setDefaultStyle();
        }
    }
    

    protected void setChangedStyle() {
        setOpaque( true );
        this.setBackground( new Color( 0x99CCFF ) );
    }
    

    protected void setOriginChangedStyle() {
        setOpaque( true );
        this.setBackground( new Color( 0x88AA22 ) );
    }

    /**
     * if the input is valid, then chagne this style.
     */
    protected void setDefaultStyle () {
        setOpaque( false );
    }


}
