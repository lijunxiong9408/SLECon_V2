package slecon.component.iobar;
import static slecon.component.iobar.Type.INPUT;
import static slecon.component.iobar.Type.OUTPUT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.ColorConvertOp;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import net.miginfocom.swing.MigLayout;
import ocsjava.remote.configuration.Event;
import ocsjava.remote.configuration.Event.Item;
import ocsjava.remote.configuration.Event.Operator;
import slecon.StartUI;
import slecon.home.PosButton;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import org.jdesktop.swingx.image.ColorTintFilter;

import base.cfg.ImageFactory;




public class IOBar extends JPanel implements ActionListener {
    private static final long                                                  serialVersionUID = -2351907739963111401L;
    protected EventListenerList                                                listenerList     = new EventListenerList();
    private final JPanel                                                       main             = new JPanel();
    private Map<slecon.component.iobar.Type, Map<Integer, IOToggleButton>> btns             = new HashMap<>();
    private Type                                                               selectedType     = null;
    private int                                                                selectedIndex    = -1;
    private final JPopupMenu                                                   pmenuOperator    = new JPopupMenu();
    private final JMenuItem                                                    mntmAnd          = new JMenuItem( "And" );
    private final JMenuItem                                                    mntmOr           = new JMenuItem( "Or" );
    private final JMenuItem                                                    mntmXor          = new JMenuItem( "Xor" );
    private List<Item>                                                         inputs;
    private List<Item>                                                         outputs;
    private Event.Operator                                                     operator;
    private PosButton                                                          btnAdd;
    private final boolean                                                      readonly;
    private LockableUI                                                         lockUI;




    public IOBar ( boolean readonly ) {
        this.readonly = readonly;
        for ( Type type : Type.values() )
            btns.put( type, new HashMap<Integer, IOToggleButton>() );
        initGUI();
        update();
    }


    public IOBar ( boolean readonly, Event evt ) throws DataFormatException {
        this.readonly = readonly;
        initGUI();
        setEvent( evt );
    }


    public void addOperatorChangedListener ( OperatorChangedListener listener ) {
        listenerList.add( OperatorChangedListener.class, listener );
    }


    public void removeOperatorChangedListener ( OperatorChangedListener listener ) {
        listenerList.remove( OperatorChangedListener.class, listener );
    }


    public void addNewEventListener ( NewEventListener listener ) {
        listenerList.add( NewEventListener.class, listener );
    }


    public void removeNewEventListener ( NewEventListener listener ) {
        listenerList.remove( NewEventListener.class, listener );
    }


    public void addRemoveEventListener ( RemoveEventListener listener ) {
        listenerList.add( RemoveEventListener.class, listener );
    }


    public void removeRemoveEventListener ( RemoveEventListener listener ) {
        listenerList.remove( RemoveEventListener.class, listener );
    }


    public void addActionListener ( ActionListener listener ) {
        listenerList.add( ActionListener.class, listener );
    }


    public void removeActionListener ( ActionListener listener ) {
        listenerList.remove( ActionListener.class, listener );
    }


    public void addEventSelectionChangeListener ( EventSelectionChangeListener listener ) {
        listenerList.add( EventSelectionChangeListener.class, listener );
    }


    public void removeEventSelectionChangeListener ( EventSelectionChangeListener listener ) {
        listenerList.remove( EventSelectionChangeListener.class, listener );
    }


    protected void fireOperatorChanged ( OperatorChangedEvent evt ) {
        Object[] listeners = listenerList.getListenerList();
        for ( int i = 0 ; i < listeners.length ; i = i + 2 ) {
            if ( listeners[ i ] == OperatorChangedListener.class ) {
                ( ( OperatorChangedListener )listeners[ i + 1 ] ).operatorChangedOccurred( evt );
            }
        }
    }


    protected void fireNewEvent ( NewEvent evt ) {
        Object[] listeners = listenerList.getListenerList();
        for ( int i = 0 ; i < listeners.length ; i = i + 2 ) {
            if ( listeners[ i ] == NewEventListener.class ) {
                ( ( NewEventListener )listeners[ i + 1 ] ).newEventOccurred( evt );
            }
        }
    }


    protected void fireRemoveEvent ( RemoveEvent evt ) {
        Object[] listeners = listenerList.getListenerList();
        for ( int i = 0 ; i < listeners.length ; i = i + 2 ) {
            if ( listeners[ i ] == RemoveEventListener.class ) {
                ( ( RemoveEventListener )listeners[ i + 1 ] ).removeEventOccurred( evt );
            }
        }
    }


    protected void fireActionEvent ( ActionEvent evt ) {
        Object[] listeners = listenerList.getListenerList();
        for ( int i = 0 ; i < listeners.length ; i = i + 2 ) {
            if ( listeners[ i ] == ActionListener.class ) {
                ( ( ActionListener )listeners[ i + 1 ] ).actionPerformed( evt );
            }
        }
    }


    protected void fireEventSelectionChangedEvent ( EventSelectionChangeEvent evt ) {
        Object[] listeners = listenerList.getListenerList();
        for ( int i = 0 ; i < listeners.length ; i = i + 2 ) {
            if ( listeners[ i ] == EventSelectionChangeListener.class ) {
                ( ( EventSelectionChangeListener )listeners[ i + 1 ] ).selectionChanged( evt );
            }
        }
    }

    private void initGUI () {
        setOpaque( false );
        main.setOpaque( false );
        setLayout( new BorderLayout() );
        add( main, BorderLayout.CENTER );
        pmenuOperator.add( mntmAnd );
        pmenuOperator.add( mntmOr );
        pmenuOperator.add( mntmXor );

        final ActionListener pmenuActionListener = new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                if ( e.getSource() == mntmAnd )
                    fireOperatorChanged( new OperatorChangedEvent( IOBar.this, getEvent(), Operator.AND ) );
                if ( e.getSource() == mntmOr )
                    fireOperatorChanged( new OperatorChangedEvent( IOBar.this, getEvent(), Operator.OR ) );
                if ( e.getSource() == mntmXor )
                    fireOperatorChanged( new OperatorChangedEvent( IOBar.this, getEvent(), Operator.XOR ) );
            }
        };
        lockUI = new LockableUI();
        lockUI.setLockedCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );

        BufferedImageOpEffect effect1 = new BufferedImageOpEffect( new ColorConvertOp( ColorSpace.getInstance( ColorSpace.CS_GRAY ), null ) );
        BufferedImageOpEffect effect2 = new BufferedImageOpEffect( new ColorTintFilter( Color.GRAY, 0.2f ) );
        lockUI.setLockedEffects( effect1, effect2 );

        final JXLayer<JComponent> layer = new JXLayer<JComponent>( main, lockUI );
        layer.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                MouseWheelEvent newEvent = (MouseWheelEvent) SwingUtilities.convertMouseEvent(layer, e, IOBar.this);
                getParent().dispatchEvent(newEvent);
            }
        });
        add( layer );
        addPropertyChangeListener( "enabled", new PropertyChangeListener() {
            @Override
            public void propertyChange ( PropertyChangeEvent evt ) {
                for ( Component d : main.getComponents() )
                    d.setEnabled( IOBar.this.isEnabled() );
                lockUI.setLocked( ! IOBar.this.isEnabled() );
            }
        } );
        mntmAnd.addActionListener( pmenuActionListener );
        mntmOr.addActionListener( pmenuActionListener );
        mntmXor.addActionListener( pmenuActionListener );
        main.setLayout( new MigLayout( "ins 0, gap 0", "[][][][]", "[center]" ) );
        setEnabled( true );
    }


    private String getOperatorAbbrevation () {
        if ( getOperator() != null )
            switch ( getOperator() ) {
            case AND :
                return "&&";
            case OR :
                return "||";
            case XOR :
                return "\u22bb";
            default :
                break;
            }
        return getOperator().toString();
    }


    public void setEvent ( Event evt ) {
        Type type  = selectedType;
        int  index = selectedIndex;
        checkEventOperator( evt );
        try {
            setOperator( evt != null && evt.getOperator() != null
                         ? evt.getOperator()
                         : evt == null || evt.getInputCount() <= 1
                           ? Event.Operator.NON
                           : Event.Operator.OR );

            List<Item>
                inputs  = new ArrayList<>(),
                outputs = new ArrayList<>();
            for ( int i = 0 ; evt != null && i < evt.getInputCount() ; i++ ) {
                inputs.add( evt.getInput( i ) );
            }
            for ( int i = 0 ; evt != null && i < evt.getOutputCount() ; i++ ) {
                outputs.add( evt.getOutput( i ) );
            }
            setInputs( inputs );
            setOutputs( outputs );
        } catch ( DataFormatException e ) {
            e.printStackTrace();
        }
        setSelection( type, index );
    }


    private void checkEventOperator ( Event evt ) {
        try {
            if ( evt != null ) {
                if ( evt.getInputCount() <= 1 )
                    evt.setOperator( Operator.NON );
                else if ( evt.getInputCount() > 1 && ( evt.getOperator() == null || evt.getOperator() == Operator.NON ) )
                    evt.setOperator( Operator.OR );
                else
                    evt.setOperator( evt.getOperator() );
            }
        } catch ( DataFormatException e ) {
            e.printStackTrace();
        }
    }


    public Event getEvent () {
        Event evt = null;
        try {
            evt = new Event();
            evt.setOperator( getOperator() );
            if ( inputs != null )
                for ( Item itm : inputs )
                    evt.addInput( new Item( itm.getBus(), itm.getId(), itm.getBit(), itm.getValue() ) );
            if ( outputs != null )
                for ( Item itm : outputs )
                    evt.addOutput( new Item( itm.getBus(), itm.getId(), itm.getBit(), itm.getValue() ) );
        } catch ( DataFormatException e ) {
            e.printStackTrace();
        }
        checkEventOperator( evt );
        try {
            if ( evt.getOperator() == Operator.NON && evt.getInputCount() > 1
                || evt.getOperator() != Operator.NON && evt.getInputCount() <= 1 )
                throw new Exception();
        } catch ( DataFormatException e ) {
            e.printStackTrace();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return evt;
    }


    public Event.Operator getOperator () {
        return ( operator == null )
               ? Operator.NON
               : operator;
    }


    public void setOperator ( Event.Operator operator ) {
        this.operator = operator;
        update();
    }


    public void setInputs ( List<Item> inputs ) {
        this.inputs = inputs;
        update();
    }


    public void setOutputs ( List<Item> outputs ) {
        this.outputs = outputs;
        update();
    }


    public List<Item> getInputs () {
        return inputs;
    }


    public List<Item> getOutputs () {
        return outputs;
    }


    public void update () {
        main.removeAll();
        btns.get( INPUT ).clear();
        btns.get( OUTPUT ).clear();

        final ButtonGroup btnGroup = new ButtonGroup();
        if ( inputs != null ) {
            for ( final Item itm : inputs ) {
                if ( inputs.indexOf( itm ) != 0 ) {
                    if ( readonly ) {
                        main.add( new PosButton( getOperatorAbbrevation(), ImageFactory.EVENT_OPERATION.icon(30, 30) ), "flowx, cell 0 0" );
                    } else {
                        final PosButton btnOperator = new PosButton( getOperatorAbbrevation(), ImageFactory.EVENT_OPERATION.icon(30, 30) );
                        addPopup( btnOperator, pmenuOperator );
                        main.add( btnOperator, "flowx, cell 0 0" );
                    }
                }

                IOToggleButton btn = new IOToggleButton( INPUT, itm );
                btn.addActionListener( new ActionListener() {
                    @Override
                    public void actionPerformed ( ActionEvent e ) {
                        setSelection( INPUT, inputs.indexOf( itm ) );
                        fireActionEvent( new ActionEvent( IOBar.this, inputs.indexOf( itm ), "INPUT" ) );
                    }
                } );
                btn.addMouseListener( new MouseAdapter() {
                    @Override
                    public void mouseClicked ( MouseEvent e ) {
                        if ( e.getButton() == MouseEvent.BUTTON3 ) {
                            fireRemoveEvent( new RemoveEvent( IOBar.this, getEvent(), Type.INPUT, inputs.indexOf( itm ) ) );
                        }
                    }
                } );
                btns.get( INPUT ).put( inputs.indexOf( itm ), btn );
                btnGroup.add( btn );
                main.add( btn, "flowx, cell 0 0" );
            }
        }
        if ( inputs != null && inputs.size() != 0 && outputs != null && outputs.size() != 0 ) {
            main.add( new PosButton( ">", ImageFactory.EVENT_OPERATION.icon(30, 30) ), "cell 1 0, gap 10 10 0 0" );
        }
        if ( outputs != null )
            for ( final Item itm : outputs ) {
                IOToggleButton btn = new IOToggleButton( OUTPUT, itm );
                btn.addActionListener( new ActionListener() {
                    @Override
                    public void actionPerformed ( ActionEvent e ) {
                        setSelection( OUTPUT, outputs.indexOf( itm ) );
                        fireActionEvent( new ActionEvent( IOBar.this, outputs.indexOf( itm ), "OUTPUT" ) );
                    }
                } );
                btn.addMouseListener( new MouseAdapter() {
                    @Override
                    public void mouseClicked ( MouseEvent e ) {
                        if ( e.getButton() == MouseEvent.BUTTON3 ) {
                            fireRemoveEvent( new RemoveEvent( IOBar.this, getEvent(), Type.OUTPUT, outputs.indexOf( itm ) ) );
                        }
                    }
                } );
                btns.get( OUTPUT ).put( outputs.indexOf( itm ), btn );
                btnGroup.add( btn );
                main.add( btn, "flowx, cell 2 0" );
            }
        btnAdd = new PosButton( "+", ImageFactory.EVENT_OPERATION.icon(30, 30) );
        btnAdd.addActionListener( this );
        main.add( btnAdd, "cell 3 0, gap 10 10 0 0" );
        setSelection( selectedType, selectedIndex );
        for ( Component d : main.getComponents() )
            d.setEnabled( IOBar.this.isEnabled() );
        revalidate();
        updateUI();
    }


    public void actionPerformed ( final ActionEvent e ) {
        if ( e.getSource() == btnAdd ) {
            do_btnNew_actionPerformed( e );
        }
    }


    protected void do_btnNew_actionPerformed ( final ActionEvent e ) {
        fireNewEvent( new NewEvent( this, getEvent() ) );
    }


    public void setSelection ( slecon.component.iobar.Type inout, int index ) {
        if ( selectedType != inout || selectedIndex != index ) {
            selectedType  = inout;
            selectedIndex = index;
            fireEventSelectionChangedEvent( new EventSelectionChangeEvent( IOBar.this, getEvent(), selectedType, index ) );
        }
        if ( inout != null ) {
            IOToggleButton btn = btns.get( selectedType ).get( index );
            if ( btn != null ) {
                scrollRectToVisible(btn.getBounds());
                if( ! btn.isSelected() )
                    btn.setSelected( true );
            }
        } else {
            for ( IOToggleButton btn : btns.get( Type.INPUT ).values() )
                btn.setSelected( false );
            for ( IOToggleButton btn : btns.get( Type.OUTPUT ).values() )
                btn.setSelected( false );
        }
    }


    private static void addPopup ( final JButton button, final JPopupMenu popup ) {
        button.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                popup.show( button, 0, 0 + button.getHeight() );
            }
        } );
        button.addMouseListener( new MouseAdapter() {
            public void mousePressed ( MouseEvent e ) {
                if ( e.isPopupTrigger() ) {
                    showMenu( e );
                }
            }
            public void mouseReleased ( MouseEvent e ) {
                if ( e.isPopupTrigger() ) {
                    showMenu( e );
                }
            }
            private void showMenu ( MouseEvent e ) {
                popup.show( e.getComponent(), e.getX(), e.getY() );
            }
        } );
    }


    public interface EventSelectionChangeListener extends EventListener {
        public void selectionChanged ( EventSelectionChangeEvent evt );
    }




    public interface NewEventListener extends EventListener {
        public void newEventOccurred ( NewEvent evt );
    }




    public interface OperatorChangedListener extends EventListener {
        public void operatorChangedOccurred ( OperatorChangedEvent evt );
    }




    public interface RemoveEventListener extends EventListener {
        public void removeEventOccurred ( RemoveEvent evt );
    }




    @SuppressWarnings( "serial" )
    public static class EventSelectionChangeEvent extends EventObject {
        private Event event;
        Type          type;
        int           index;




        public EventSelectionChangeEvent ( Object source, Event evt, Type inout, int index ) {
            super( source );
            this.event = evt;
            this.type  = inout;
            this.index = index;
        }


        public final Event getEvent () {
            return event;
        }


        public final Type getType () {
            return type;
        }


        public final int getIndex () {
            return index;
        }


        @Override
        public String toString () {
            return "EventSelectionChangeEvent [getSource()=" + getSource() + ", getEvent()=" + getEvent() + ", getType()=" + getType()
                   + ", getIndex()=" + getIndex() + "]";
        }
    }




    @SuppressWarnings( "serial" )
    public static class NewEvent extends EventObject {
        private Event event;




        public NewEvent ( Object source, Event evt ) {
            super( source );
            this.event = evt;
        }


        public final Event getEvent () {
            return event;
        }


        @Override
        public String toString () {
            return "NewEvent [getSource()=" + getSource() + ", getEvent()=" + getEvent() + "]";
        }
    }




    @SuppressWarnings( "serial" )
    public static class OperatorChangedEvent extends EventObject {
        private Event    event;
        private Operator operator;




        public OperatorChangedEvent ( Object source, Event evt, Event.Operator opr ) {
            super( source );
            this.event    = evt;
            this.operator = opr;
        }


        public final Event getEvent () {
            return event;
        }


        public final Operator getOperator () {
            return operator;
        }


        @Override
        public String toString () {
            return "OperatorChangeEvent [event=" + event + ", operator=" + operator + "]";
        }
    }




    @SuppressWarnings( "serial" )
    public class RemoveEvent extends EventObject {
        private Event event;
        private Type  type;
        private int   item;




        public RemoveEvent ( Object source, Event evt, Type type, int col ) {
            super( source );
            this.event = evt;
            this.type  = type;
            this.item  = col;
        }


        public final Event getEvent () {
            return event;
        }


        public final Type getType () {
            return type;
        }


        public final int getIndex () {
            return item;
        }


        @Override
        public String toString () {
            return "RemoveEvent [event=" + event + ", type=" + type + ", item=" + item + "]";
        }
    }
}
