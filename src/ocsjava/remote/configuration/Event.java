package ocsjava.remote.configuration;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import comm.constants.CANBus;
import comm.util.Endian;



public class Event {
    /**
     * Constant of AND operator in inputs. It's used for binary output.
     */
    private static final int AND = 1;

    /**
     * Constant of OR operator in inputs. It's used for binary output.
     */
    private static final int OR = 2;

    /**
     * Constant of XOR operator in inputs. It's used for binary output.
     */
    private static final int XOR = 3;

    /**
     * Constant of no operator and it works an input in inputs only. It's used for binary output.
     */
    private static final int NON = 4;

    /**
     * Enabled flag of event.
     */
    private byte enabled = 1;

    /**
     * Operator of inputs.
     */
    private byte operator = 0;

    /**
     * List of inputs.
     */
    private ArrayList<Item> inputs = new ArrayList<>();

    /**
     * List of outputs.
     */
    private ArrayList<Item> outputs = new ArrayList<>();




    /**
     * Catalog of I/O.
     */
    public static enum IO {
        /**
         * Input.
         */
        INPUT,

        /**
         * Output.
         */
        OUTPUT
    };

    /**
     * Operators for Input I/O.
     */
    public static enum Operator {
        /**
         * AND operator.
         */
        AND,

        /**
         * OR operator.
         */
        OR,

        /**
         * XOR operator.
         */
        XOR,

        /**
         * NON (no) operator.
         */
        NON
    };

    /**
     * Convert a binary to an event.
     * @param binary        Byte array.
     * @param offset        Read offset from <code>binary</code>.
     * @return Returns an event on success, otherwise, returns null.
     * @throws DataFormatException Once the operator from binary is invalid, this exception is thrown.
     */
    public static Event toEvent ( byte[] binary, int offset ) throws DataFormatException {
        int   pos   = 0;
        Event event = new Event();
        try {
            event.setEnabled( binary[ offset + pos++ ] == 0
                              ? false
                              : true );
            switch ( binary[ offset + pos++ ] ) {
            case AND :
                event.setOperator( Operator.AND );
                break;
            case OR :
                event.setOperator( Operator.OR );
                break;
            case XOR :
                event.setOperator( Operator.XOR );
                break;
            case NON :
                event.setOperator( Operator.NON );
                break;
            default :
                throw new DataFormatException();
            }
            for ( int i = 0, inputCount = binary[ offset + pos++ ] ; i < inputCount ; i++ ) {
                int  size   = Item.getSize();
                byte item[] = new byte[ size ];
                for ( int j = 0 ; j < size ; j++ )
                    item[ j ] = binary[ offset + pos++ ];
                event.addInput( Item.toItem( item ) );
            }
            for ( int i = 0, outputCount = binary[ offset + pos++ ] ; i < outputCount ; i++ ) {
                byte item[] = new byte[ Item.getSize() ];
                for ( int j = 0 ; j < Item.getSize() ; j++ )
                    item[ j ] = binary[ offset + pos++ ];
                event.addOutput( Item.toItem( item ) );
            }
        } catch ( IndexOutOfBoundsException e ) {
            e.printStackTrace( System.err );
            return null;
        }
        return event;
    }


    /**
     * Get the status of event.
     * @return Returns 1 once the event is enabled; otherwise, returns 0.
     */
    public byte getEnabled () {
        return this.enabled;
    }


    /**
     * Set the status of event.
     * @param enabled       New status of event.
     */
    public void setEnabled ( boolean enabled ) {
        this.enabled = ( byte )( enabled == true
                                 ? 1
                                 : 0 );
    }


    /**
     * Get the input operator.
     * @return Returns the input operator.
     * @throws DataFormatException Once operator is not initialed, this exception is thrown.
     */
    public Event.Operator getOperator () throws DataFormatException {
        switch ( this.operator ) {
        case AND :
            return Event.Operator.AND;
        case OR :
            return Event.Operator.OR;
        case XOR :
            return Event.Operator.XOR;
        case NON :
            return Event.Operator.NON;
        default :
            throw new DataFormatException();
        }
    }


    /**
     * Get the constant value of input operator. It's useful for binary output.
     * @return Returns the constant value of input operator.
     * @throws DataFormatException Once operator is not initialed, this exception is thrown.
     */
    public byte getOperatorConstant () throws DataFormatException {
        switch ( this.operator ) {
        case AND :
            return AND;
        case OR :
            return OR;
        case XOR :
            return XOR;
        case NON :
            return NON;
        default :
            throw new DataFormatException();
        }
    }


    /**
     * Set the input operator.
     * @param operator      New operator.
     * @throws DataFormatException Once argument <code>operator</code> is invalid, this exception is thrown.
     */
    public void setOperator ( Event.Operator operator ) throws DataFormatException {
        switch ( operator ) {
        case AND :
            this.operator = AND;
            break;
        case OR :
            this.operator = OR;
            break;
        case XOR :
            this.operator = XOR;
            break;
        case NON :
            if ( this.inputs.size() < 2 )
                this.operator = NON;
            else
                throw new DataFormatException( "NON operator only works for an input!" );
            break;
        default :
            throw new DataFormatException();
        }
    }


    /**
     * Add an input.
     * @param item          A new input.
     * @throws DataFormatException Once the number of input exceed 255, this exception is thrown.
     */
    public void addInput ( Item item ) throws DataFormatException {
        if ( this.getInputCount() >= 0xFF )
            throw new DataFormatException();
        this.inputs.add( item );
    }


    /**
     * Remove an input.
     * @param i             Index of input going to remove.
     * @return Returns the input removed.
     */
    public Item removeInput ( int i ) {
        return this.inputs.remove( i );
    }


    /**
     * Remove all inputs.
     */
    public void removeAllInputs () {
        while ( this.inputs.size() > 0 )
            this.inputs.remove( 0 );
    }


    /**
     * Add an output.
     * @param item          A new output.
     * @throws DataFormatException Once the number of output exceed 255, this exception is thrown.
     */
    public void addOutput ( Item item ) throws DataFormatException {
        if ( this.getOutputCount() >= 0xFF )
            throw new DataFormatException();
        this.outputs.add( item );
    }


    /**
     * Remove an output.
     * @param i             Index of output going to remove.
     * @return Returns the output removed.
     */
    public Item removeOutput ( int i ) {
        return this.outputs.remove( i );
    }


    /**
     * Remove all outputs.
     */
    public void removeAllOutputs () {
        while ( this.outputs.size() > 0 )
            this.outputs.remove( 0 );
    }


    /**
     * Get the number of input.
     * @return Returns the number of input.
     */
    public int getInputCount () {
        return this.inputs.size();
    }


    /**
     * Get an item from input.
     * @param index         Item's index.
     * @return Return an item from input.
     */
    public Item getInput ( int index ) {
        return this.inputs.get( index );
    }


    /**
     * Get an item from output.
     * @param index         Item's index.
     * @return Return an item from output.
     */
    public Item getOutput ( int index ) {
        return this.outputs.get( index );
    }


    /**
     * Get the number of output.
     * @return Returns the number of output.
     */
    public int getOutputCount () {
        return this.outputs.size();
    }
    
    public void setInputItem(int index, Item item) throws DataFormatException {
        if (index >= inputs.size())
            addInput(item);
        this.inputs.set(index, item);
    }


    public void setOutputItem(int index, Item item) throws DataFormatException {
        if (index >= outputs.size()) {
            addOutput(item);
        }
        this.outputs.set(index, item);
    }
    
    
    /**
     * Output an byte array of event.
     * @return Returns an event in byte array format.
     * @throws DataFormatException Once any invalid pattern inside, this exception is thrown.
     */
    public byte[] toByteArray () throws DataFormatException {
        // 1 byte for enabled flag.
        // 1 byte for operator flag.
        // 1 byte for number count of input.
        // 1 byte for number count of output.
        // Each item is 5 bytes.
        int    pos = 0;
        byte[] ret = new byte[ 4 + this.inputs.size() * Item.getSize() + this.outputs.size() * Item.getSize() ];
        ret[ pos++ ] = this.getEnabled();             // Enabled flag.
        ret[ pos++ ] = this.getOperatorConstant();    // Operator flag.

        // Input and output items.
        @SuppressWarnings("unchecked")
        ArrayList<Item> io[] = new ArrayList[ 2 ];
        io[ 0 ] = this.inputs;
        io[ 1 ] = this.outputs;
        for ( int i = 0 ; i < io.length ; i++ ) {
            int size = ( ( ArrayList<Item> )io[ i ] ).size();
            ret[ pos++ ] = ( byte )size;    // Input/output count.
            if ( i == 0 && size < 2 && this.getOperatorConstant() != Event.NON )
                throw new DataFormatException( "Invalid operator!" );
            for ( int count = 0 ; count < size ; count++ ) {
                Item   item     = ( ( ArrayList<Item> )io[ i ] ).get( count );
                byte[] itemByte = item.toByteArray();
                for ( int k = 0, len = itemByte.length ; k < len ; k++ )
                    ret[ pos++ ] = itemByte[ k ];
            }
        }
        if ( pos != ret.length )
            throw new DataFormatException();

//      // [DEBUG]
//      System.out.println( pos + ", " + ret.length );
//      for ( int i = 0 ; i < pos ; i++ )
//          System.out.printf( "0x%X ", ret[ i ] );
//      System.out.println();
        return ret;
    }


    @Override
    public int hashCode () {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + enabled;
        result = prime * result + ( ( inputs == null )
                                    ? 0
                                    : inputs.hashCode() );
        result = prime * result + operator;
        result = prime * result + ( ( outputs == null )
                                    ? 0
                                    : outputs.hashCode() );
        return result;
    }


    @Override
    public boolean equals ( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( ! ( obj instanceof Event ) ) {
            return false;
        }

        Event other = ( Event )obj;
        if ( enabled != other.enabled ) {
            return false;
        }
        if ( inputs == null ) {
            if ( other.inputs != null ) {
                return false;
            }
        } else if ( ! inputs.equals( other.inputs ) ) {
            return false;
        }
        if ( operator != other.operator ) {
            return false;
        }
        if ( outputs == null ) {
            if ( other.outputs != null ) {
                return false;
            }
        } else if ( ! outputs.equals( other.outputs ) ) {
            return false;
        }
        return true;
    }


    /**
     * Demo section.
     * @param args  It specifies the arguments read from command line.
     */
    public static void main ( String args[] ) {
        Event event = new Event();
        try {
            event.setOperator( Event.Operator.AND );
            event.addInput( new Item( CANBus.CAR, ( byte )2, ( byte )0, ( byte )1 ) );
            event.addInput( new Item( CANBus.HALL, ( byte )3, ( byte )31, ( byte )0 ) );
            event.addOutput( new Item( CANBus.HALL, ( byte )2, ( byte )0, ( byte )1 ) );
            event.addOutput( new Item( CANBus.CAR, ( byte )3, ( byte )31, ( byte )1 ) );
            event.toByteArray();
        } catch ( DataFormatException e ) {
            e.printStackTrace( System.err );
        }
        try {
            event = Event.toEvent( event.toByteArray(), 0 );
            System.out.println( "Enabled: " + event.getEnabled() );
            System.out.println( "Operator: " + event.getOperatorConstant() );

            // Inputs.
            System.out.println( "Inputs\n===================================" );
            for ( int i = 0, inputCount = event.getInputCount() ; i < inputCount ; i++ ) {
                Item item = event.getInput( i );
                System.out.println( "\tBus:" + item.getBusConstant() + ", id:" + item.getId() + ", bit:" + item.getBit() + ", value:"
                                    + item.getValue() );
            }

            // Ouptuts.
            System.out.println( "Outputs\n===================================" );
            for ( int i = 0, outputCount = event.getOutputCount() ; i < outputCount ; i++ ) {
                Item item = event.getOutput( i );
                System.out.println( "\tBus:" + item.getBusConstant() + ", id:" + item.getId() + ", bit:" + item.getBit() + ", value:"
                                    + item.getValue() );
            }
        } catch ( DataFormatException e ) {
            e.printStackTrace( System.err );
        }
    }



    public static class Item {
        /**
         * CAR bridge constant actually stored in binary file.
         */
        private static final short CAR_BRIDGE = 0x0100;

        /**
         * HALL bridge constant actually stored in binary file.
         */
        private static final short HALL_BRIDGE = 0x0200;

        /**
         * Size of an item.
         */
        private static final int SIZE = 5;

        /**
         * Item's bus.
         */
        private short bus;

        /**
         * Item's ID.
         */
        private byte id;

        /**
         * Item's bit.
         */
        private byte bit;

        /**
         * Item's value.
         */
        private byte value;




        /**
         * The basic element on input and output.
         * @param bus           Item's bus.
         * @param id            Item's ID.
         * @param bit           Item's bit.
         * @param value         Item's value.
         * @throws DataFormatException
         *  Once the argument <code>bus</code> is not <code>Item.Bus.CAR</code> and <code>Item.Bus.HALL</code>, this exception is thrown.
         */
        public Item ( CANBus bus, byte id, byte bit, byte value ) throws DataFormatException {
            this.id    = id;
            this.bit   = bit;
            this.value = value;
            switch ( bus ) {
            case CAR :
                this.bus = CAR_BRIDGE;
                break;
            case HALL :
                this.bus = HALL_BRIDGE;
                break;
            default :
                throw new DataFormatException();
            }
        }


        /**
         * Get the bus of item.
         * @return Returns the bus of item.
         * @throws DataFormatException Once the bus is not initialed, this exception is thrown.
         */
        public CANBus getBus () throws DataFormatException {
            switch ( this.bus ) {
            case CAR_BRIDGE :
                return CANBus.CAR;
            case HALL_BRIDGE :
                return CANBus.HALL;
            default :
                throw new DataFormatException();
            }
        }


        /**
         * Get the constant value of bus. It's useful for exporting binary format.
         * @return Returns the constant value of bus.
         * @throws DataFormatException Once the bus is not initialed, this exception is thrown.
         */
        public short getBusConstant () throws DataFormatException {
            switch ( this.bus ) {
            case CAR_BRIDGE :
                return Item.CAR_BRIDGE;
            case HALL_BRIDGE :
                return Item.HALL_BRIDGE;
            default :
                throw new DataFormatException();
            }
        }


        /**
         * Get the ID of item.
         * @return Returns the ID of item.
         */
        public byte getId () {
            return this.id;
        }


        /**
         * Get the bit value of item.
         * @return Returns the bit value of item.
         */
        public byte getBit () {
            return this.bit;
        }


        /**
         * Get the value of item.
         * @return Returns the value of item.
         */
        public byte getValue () {
            return this.value;
        }


        /**
         * Set the bus of item.
         * @param bus       New bus.
         * @throws DataFormatException
         *  Once the <code>bus</code> is not <code>Item.Bus.CAR</code> and <code>Item.Bus.HALL</code>, this exception is thrown.
         */
        public void setBus ( CANBus bus ) throws DataFormatException {
            switch ( bus ) {
            case CAR :
                this.bus = CAR_BRIDGE;
                break;
            case HALL :
                this.bus = HALL_BRIDGE;
                break;
            default :
                throw new DataFormatException();
            }
        }


        /**
         * Set the ID of item.
         * @param id        New ID.
         */
        public void setId ( byte id ) {
            this.id = id;
        }


        /**
         * Set the bit value of item.
         * @param bit       New bit value.
         */
        public void setBit ( byte bit ) {
            this.bit = bit;
        }


        /**
         * Set the value of item.
         * @param value     New value.
         */
        public void setValue ( byte value ) {
            this.value = value;
        }


        /**
         * Get the size which binary format will generate.
         * @return Returns the size which binary format will generate.
         */
        public static int getSize () {
            return SIZE;
        }


        /**
         * Output an byte array of item.
         * @return Returns an item in byte array format.
         * @throws DataFormatException Once any invalid pattern inside, this exception is thrown.
         */
        public byte[] toByteArray () throws DataFormatException {
            byte[] ret      = new byte[ getSize() ];
            byte[] busConst = Endian.getShortByteArray( this.getBusConstant() );
            ret[ 0 ] = busConst[ 0 ];      // Item's bus.
            ret[ 1 ] = busConst[ 1 ];
            ret[ 2 ] = this.getId();       // Item's ID.
            ret[ 3 ] = this.getBit();      // Item's bit.
            ret[ 4 ] = this.getValue();    // Item's value.
            return ret;
        }


        /**
         * Convert a binary to an item.
         * @param binary        Byte array.
         * @return Returns an item on success, otherwise, returns null.
         * @throws DataFormatException Once the bus from binary is invalid, this exception is thrown.
         */
        public static Item toItem ( byte[] binary ) throws DataFormatException {
            Item item = null;
            try {
                byte id    = binary[ 2 ];
                byte bit   = binary[ 3 ];
                byte value = binary[ 4 ];
                switch ( Endian.getShort( binary, 0 ) ) {
                case CAR_BRIDGE :
                    item = new Item( CANBus.CAR, id, bit, value );
                    break;
                case HALL_BRIDGE :
                    item = new Item( CANBus.HALL, id, bit, value );
                    break;
                default :
                    throw new DataFormatException();
                }
            } catch ( IndexOutOfBoundsException e ) {
                System.err.println( e.getMessage() );
                return null;
            }
            return item;
        }
    }
}
