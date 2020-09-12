package slecon.component;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolTip;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import base.cfg.FontFactory;
import slecon.StartUI;


/**
 * the input text box in setting panel.
 */
public class ValueTextField extends JFormattedTextField {
    private static final long serialVersionUID = -5073854202173718316L;
    private String            integer_pattern  = "#,###";
    private String            float_pattern    = "###0.00";
    private boolean           isEmptyAllow     = false;
    private Object            emptyValue;
    BigDecimal                lower_limit;
    BigDecimal                upper_limit;
    boolean                   min_equal;
    boolean                   max_equal;


    public JToolTip createToolTip() {
        JToolTip tooltip = super.createToolTip();
        tooltip.setFont(FontFactory.VALUETEXT_TOOLTIP_FONT);
        return tooltip;
    }

    public ValueTextField () {
        super();
        setFont( FontFactory.FONT_12_PLAIN );
        setCaretColor(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(StartUI.BORDER_COLOR));
        setBackground(StartUI.SUB_BACKGROUND_COLOR);
        setForeground(Color.WHITE);
        
        setFocusLostBehavior( JFormattedTextField.COMMIT );
        this.addFocusListener( new FocusListener() {
            @Override
            public void focusGained ( FocusEvent e ) {
            	originValueChanged = false;
                checkValue();
            }
            @Override
            public void focusLost ( FocusEvent e ) {
                checkValue();
            }
        } );
        this.addPropertyChangeListener( "value", new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                checkValue();
            }
        } );
    }


    public boolean isEmptyAllow () {
        return isEmptyAllow;
    }


    public void setEmptyAllow ( boolean isEmptyAllow ) {
        this.isEmptyAllow = isEmptyAllow;
    }


    public Object getEmptyValue () {
        return emptyValue;
    }


    public void setEmptyValue ( Object emptyValue ) {
        this.emptyValue = emptyValue;
        if ( getValue() == null )
            setValue( emptyValue );
        checkValue();
    }


    public <T extends Number> void setScope ( Class<T> claz, T min, T max ) {
        setScope( claz, min, max, true, true );
    }


    /**
     * set number format and min, max in quick way.
     * 
     * @param claz
     * @param min
     * @param max
     * @param min_equal
     * @param max_equal
     */
    public <T extends Number> void setScope ( Class<T> claz, T min, T max, boolean min_equal, boolean max_equal ) {
        this.min_equal = min_equal;
        this.max_equal = max_equal;
        if ( min != null )
            lower_limit = new BigDecimal( min.toString() );
        if ( max != null )
            upper_limit = new BigDecimal( max.toString() );
        if ( claz.equals( Long.class ) || claz.equals( Integer.class ) || claz.equals( Short.class ) || claz.equals( Byte.class ) ) {
            DecimalFormat   df = new DecimalFormat( integer_pattern );
            NumberFormatter nf = new NumberFormatter( df );
            nf.setValueClass( Long.class );      // Maybe BigInteger is better.
            setFormatterFactory( new DefaultFormatterFactory( nf ) );
            if ( min != null && max != null )
                setToolTipText( String.format( "<html>(Integer)   %d %s x %s %d", min.longValue(), ( min_equal )
                                                                                             ? "&le;"
                                                                                             : "&lt;", ( max_equal )
                                                                                                    ? "&le;"
                                                                                                    : "&lt;", max.longValue() ) );
            else if ( min != null && max == null )
                setToolTipText( String.format( "<html>(Integer)   x %s %d", ( min_equal )
                                                                      ? "&ge;"
                                                                      : "&gt;", min.longValue() ) );
            else if ( min == null && max != null )
                setToolTipText( String.format( "<html>(Integer)   x %s %d", ( max_equal )
                                                                      ? "&le;"
                                                                      : "&lt;", max.longValue() ) );
            else
                setToolTipText( "(Integer)" );
        } else if ( claz.equals( Double.class ) || claz.equals( Float.class ) ) {
            DecimalFormat   df = new DecimalFormat( float_pattern );
            NumberFormatter nf = new NumberFormatter( df );
            nf.setValueClass( Double.class );    // Maybe BigDecimal is better.
            setFormatterFactory( new DefaultFormatterFactory( nf ) );
            if ( min != null && max != null )
                setToolTipText( String.format( "<html>(Float)  %.1f %s x %s %.1f", min.doubleValue(), ( min_equal )
                                                                                                ? "&le;"
                                                                                                : "&lt;", ( max_equal )
                                                                                                       ? "&le;"
                                                                                                       : "&lt;", max.doubleValue() ) );
            else if ( min != null && max == null )
                setToolTipText( String.format( "<html>(Float)  x %s %.1f", ( min_equal )
                                                                     ? "&ge;"
                                                                     : "&gt;", min.doubleValue() ) );
            else if ( min == null && max != null )
                setToolTipText( String.format( "<html>(Float)  x %s %.1f", ( max_equal )
                                                                     ? "&le;"
                                                                     : "&lt;", max.doubleValue() ) );
            else
                setToolTipText( "(Float)" );
        } else {
            throw new RuntimeException( "undefined class." );
        }
        checkValue();
    }

    int setValueCount = 0;
    private Object originValue;
    private Object firstOriginValue;
    private boolean originValueChanged;
    
    public void setOriginValue( Object obj ) {
        setValueCount += 1;
        if (checkValue(obj)) {
            originValue = obj;
            setValue(obj);
            firstOriginValue = originValue;
        }
    }

    public Object getOriginValue () {
        return originValue;
    }
    
    public void setValue ( Object obj ) {
        super.setValue( obj );
        checkValue();
    }


    public boolean checkValue () {
        boolean isValid = isEditValid() && checkValue(getValue());

        if (!originValueChanged) 
            originValueChanged = firstOriginValue != null && !firstOriginValue.equals(getOriginValue());
        
        if (originValueChanged) {
            setOriginChangedStyle();
        }
        else {
            if ( isValid ) {
                if (getOriginValue()!=null && !getOriginValue().equals(getValue()) )
                    setChangedStyle();
                else
                    setOKStyle();
            } else
                setErrorStyle();
        }
        return isValid;
    }

    /**
     * check the textbox is valid input, and change background color in valid/invalid.
     *
     * @return true if the textbox is valid, otherwise false
     */
    protected boolean checkValue (Object value) {
        if ( value == null && ! isEmptyAllow && emptyValue != null )
            setValue( emptyValue );

        boolean isValid = true;
        try {
            if ( min_equal )
                isValid &= ( lower_limit == null ) || lower_limit.compareTo( new BigDecimal( value.toString() ) ) <= 0;
            else
                isValid &= ( lower_limit == null ) || lower_limit.compareTo( new BigDecimal( value.toString() ) ) < 0;
            if ( max_equal )
                isValid &= ( upper_limit == null ) || upper_limit.compareTo( new BigDecimal( value.toString() ) ) >= 0;
            else
                isValid &= ( upper_limit == null ) || upper_limit.compareTo( new BigDecimal( value.toString() ) ) > 0;
        
        } catch ( Exception e ) {
            isValid = false;
        }
        return isValid;
    }
    
    
    protected void setChangedStyle() {
        this.setBackground( new Color( 0xD7DF01 ) );
    }
    

    protected void setOriginChangedStyle() {
        this.setBackground( new Color( 0x088A85 ) );
    }

    /**
     * if the input is valid, then chagne this style.
     */
    protected void setOKStyle () {
        this.setBackground( StartUI.SUB_BACKGROUND_COLOR );
    }


    /**
     * if the input is invalid, then chagne this style.
     */
    protected void setErrorStyle () {
        this.setBackground( new Color( 0xDF3A01 ) );
    }


    public static void main ( String... arg ) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        final ValueTextField text = new ValueTextField();
        text.setScope( Long.class, null, 20L, true, false );
        text.setEmptyValue(10L);
        text.setColumns( 10 );
        f.add( text );

        JButton btn = new JButton( "OK" );
        btn.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                JOptionPane.showMessageDialog( text, text.getValue() );
            }
        } );
        f.add( btn, BorderLayout.SOUTH );
        f.pack();
        f.setVisible( true );
    }
}
