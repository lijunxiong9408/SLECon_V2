package logic.io.crossbar;
public class CrossBar {
    public byte[] configure;


    public CrossBar ( byte[] configure ) {
        loadConfigure( configure );
    }

    public void loadConfigure ( byte[] configure ) {
        if ( configure.length != 116 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 116" );
        this.configure = configure.clone();
    }

    private int toUnsignedInt ( byte val ) {
        return ( ( int )val ) & 0xff;
    }

    public boolean isInputPresent ( byte[] rawIO, InputPinA03 pin ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "source is null." );

        int group     = ( pin.rawIOPos >> 4 ) & 0x7;
        int bitIndex  = pin.rawIOPos & 0xf;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
    }

    public boolean isInputPresent ( byte[] rawIO, InputPinA05 pin ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "source is null." );

        int group     = ( pin.rawIOPos >> 4 ) & 0x7;
        int bitIndex  = pin.rawIOPos & 0xf;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
    }

    public boolean isInputPresent ( byte[] rawIO, InputPinA07 pin ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "source is null." );

        int group     = ( pin.rawIOPos >> 4 ) & 0x7;
        int bitIndex  = pin.rawIOPos & 0xf;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
    }
    
    public boolean isInputPresent ( byte[] rawIO, InputPinC01 pin ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "source is null." );

        int group     = ( pin.rawIOPos >> 4 ) & 0x7;
        int bitIndex  = pin.rawIOPos & 0xf;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
    }
    
    public boolean isInputPresent ( byte[] rawIO, InputPinA01 pin ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "source is null." );

        int group     = ( pin.rawIOPos >> 4 ) & 0x7;
        int bitIndex  = pin.rawIOPos & 0xf;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
    }
    
    public boolean isInputPresent ( byte[] rawIO, InputPinD01 pin ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "source is null." );

        int group     = ( pin.rawIOPos >> 4 ) & 0x7;
        int bitIndex  = pin.rawIOPos & 0xf;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
    }
    public boolean isInputPresent ( byte[] rawIO, InputSourceA03 source ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( source == null )
            throw new IllegalArgumentException( "source is null." );

        boolean invert    = ( configure[ source.configureIndex ] >> 7 ) == 1;
        int     group     = ( configure[ source.configureIndex ] >> 4 ) & 0x7;
        int     bitIndex  = configure[ source.configureIndex ] & 0xf;
        int     groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        if ( invert ) {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x00;
        } else {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
        }
    }
    
    public boolean isInputPresent ( byte[] rawIO, InputSourceA05 source ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( source == null )
            throw new IllegalArgumentException( "source is null." );

        boolean invert    = ( configure[ source.configureIndex ] >> 7 ) == 1;
        int     group     = ( configure[ source.configureIndex ] >> 4 ) & 0x7;
        int     bitIndex  = configure[ source.configureIndex ] & 0xf;
        int     groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        if ( invert ) {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x00;
        } else {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
        }
    }
    
    public boolean isInputPresent ( byte[] rawIO, InputSourceA07 source ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( source == null )
            throw new IllegalArgumentException( "source is null." );

        boolean invert    = ( configure[ source.configureIndex ] >> 7 ) == 1;
        int     group     = ( configure[ source.configureIndex ] >> 4 ) & 0x7;
        int     bitIndex  = configure[ source.configureIndex ] & 0xf;
        int     groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        if ( invert ) {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x00;
        } else {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
        }
    }
    public boolean isInputPresent ( byte[] rawIO, InputSourceC01 source ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( source == null )
            throw new IllegalArgumentException( "source is null." );

        boolean invert    = ( configure[ source.configureIndex ] >> 7 ) == 1;
        int     group     = ( configure[ source.configureIndex ] >> 4 ) & 0x7;
        int     bitIndex  = configure[ source.configureIndex ] & 0xf;
        int     groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        
       // System.out.println("configure : "+configure[ source.configureIndex ] +" invert : "+invert+" group : "+group+" bitIndex : "+bitIndex);
        
        if ( invert ) {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x00;
        } else {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
        }
    }
    
    public boolean isInputPresent ( byte[] rawIO, InputSourceA01 source ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( source == null )
            throw new IllegalArgumentException( "source is null." );

        boolean invert    = ( configure[ source.configureIndex ] >> 7 ) == 1;
        int     group     = ( configure[ source.configureIndex ] >> 4 ) & 0x7;
        int     bitIndex  = configure[ source.configureIndex ] & 0xf;
        int     groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        
       // System.out.println("configure : "+configure[ source.configureIndex ] +" invert : "+invert+" group : "+group+" bitIndex : "+bitIndex);
        
        if ( invert ) {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x00;
        } else {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
        }
    }

    public boolean isInputPresent ( byte[] rawIO, InputSourceD01 source ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( source == null )
            throw new IllegalArgumentException( "source is null." );
        
        int value = configure[ source.configureIndex ] & 0xff;
        boolean invert    = ( value >> 7 ) == 1;
        int     group     = ( value >> 4 ) & 0x07;
        int     bitIndex  = value & 0xf;
        int     groupData = toUnsignedInt( rawIO[ group * 2 ] ) | toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8;
        
        if ( invert ) {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x00;
        } else {
            return ( ( groupData >> bitIndex ) & 0x01 ) == 0x01;
        }
    }

    public boolean isOutputPresent ( byte[] rawIO, OutputSourceD01 source ) throws ReferenceSourceNotFoundException {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( source == null )
            throw new IllegalArgumentException( "source is null." );
        for ( OutputPinD01 pin : OutputPinD01.values() ) {
            if ( configure[ pin.configureIndex ] == source.id ) {
                return isOutputPresent( rawIO, pin );
            }
        }
        throw new ReferenceSourceNotFoundException();
    }
    
    public boolean isOutputPresent ( byte[] rawIO, OutputSourceC01 source ) throws ReferenceSourceNotFoundException {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( source == null )
            throw new IllegalArgumentException( "source is null." );
        for ( OutputPinC01 pin : OutputPinC01.values() ) {
            if ( configure[ pin.configureIndex ] == source.id ) {
                return isOutputPresent( rawIO, pin );
            }
        }
        throw new ReferenceSourceNotFoundException();
    }

    public boolean isOutputPresent ( byte[] rawIO, OutputPinA03 pin ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "pin is null." );

        int group = 6;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | (toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8);
        return ((groupData >> pin.bitOrder) & 0x01 )== 0x01;
    }
    
    public boolean isOutputPresent ( byte[] rawIO, OutputPinA05 pin ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "pin is null." );

        int group = 6;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | (toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8);
        return ((groupData >> pin.bitOrder) & 0x01 )== 0x01;
        
    }
    
    public boolean isOutputPresent ( byte[] rawIO, OutputPinA07 pin ) {
    	if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "pin is null." );

        int group = 6;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | (toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8);
        return ((groupData >> pin.bitOrder) & 0x01 )== 0x01;
    }
    
    public boolean isOutputPresent ( byte[] rawIO, OutputPinC01 pin ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "pin is null." );
      
        int group = 6;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | (toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8);
        return ((groupData >> pin.bitOrder) & 0x01 )== 0x01;
 
    }
    
    public boolean isOutputPresent ( byte[] rawIO, OutputPinA01 pin ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "pin is null." );
      
        int group = 6;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | (toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8);
        return ((groupData >> pin.bitOrder) & 0x01 )== 0x01;
 
    }
    
    public boolean isOutputPresent ( byte[] rawIO, OutputPinD01 pin ) {
        if ( rawIO.length != 16 )
            throw new IllegalArgumentException( "the length of rawIO isn't equal to 16" );
        if ( pin == null )
            throw new IllegalArgumentException( "pin is null." );
      
        int group = 6;
        int groupData = toUnsignedInt( rawIO[ group * 2 ] ) | (toUnsignedInt( rawIO[ group * 2 + 1 ] ) << 8);
        return ((groupData >> pin.bitOrder) & 0x01 )== 0x01;
 
    }

    public InputPinA03 getInputPinA03 ( InputSourceA03 source ) {
        return InputPinA03.getByRawIOPosition( configure[ source.configureIndex ] & 0x7F );
    }
    
    public InputPinA05 getInputPinA05 ( InputSourceA05 source ) {
        return InputPinA05.getByRawIOPosition( configure[ source.configureIndex ] & 0x7F );
    }
    
    public InputPinA07 getInputPinA07 ( InputSourceA07 source ) {
        return InputPinA07.getByRawIOPosition( configure[ source.configureIndex ] & 0x7F );
    }
    
    public InputPinC01 getInputPinC01 ( InputSourceC01 source ) {
        return InputPinC01.getByRawIOPosition( configure[ source.configureIndex ] & 0x7F );
    }
    
    public InputPinC01 getInputPinA01 ( InputSourceA01 source ) {
        return InputPinC01.getByRawIOPosition( configure[ source.configureIndex ] & 0x7F );
    }
    
    public InputPinD01 getInputPinD01 ( InputSourceD01 source ) {
        return InputPinD01.getByRawIOPosition( configure[ source.configureIndex ] & 0x7F );
    }
    
    public InputPinA03 getInputPinA03 ( InputPinA03 pin ) {
        return InputPinA03.getByRawIOPosition( pin.rawIOPos );
    }
    
    public InputPinA05 getInputPinA05 ( InputPinA05 pin ) {
        return InputPinA05.getByRawIOPosition( pin.rawIOPos );
    }
    public InputPinA07 getInputPinA07 ( InputPinA07 pin ) {
        return InputPinA07.getByRawIOPosition( pin.rawIOPos );
    }
    
    public InputPinC01 getInputPinC01 ( InputPinC01 pin ) {
        return InputPinC01.getByRawIOPosition( pin.rawIOPos );
    }
    
    public InputPinC01 getInputPinA01 ( InputPinA01 pin ) {
        return InputPinC01.getByRawIOPosition( pin.rawIOPos );
    }
    
    public InputPinC01 getInputPinD01 ( InputPinD01 pin ) {
        return InputPinC01.getByRawIOPosition( pin.rawIOPos );
    }

    public boolean isInverted ( InputSourceA03 source ) {
        return ( configure[ source.configureIndex ] & 0x80 ) == 0x80;
    }
    
    public boolean isInverted ( InputSourceA05 source ) {
        return ( configure[ source.configureIndex ] & 0x80 ) == 0x80;
    }
    
    public boolean isInverted ( InputSourceA07 source ) {
        return ( configure[ source.configureIndex ] & 0x80 ) == 0x80;
    }
    
    public boolean isInverted ( InputSourceC01 source ) {
        return ( configure[ source.configureIndex ] & 0x80 ) == 0x80;
    }
    
    public boolean isInverted ( InputSourceA01 source ) {
        return ( configure[ source.configureIndex ] & 0x80 ) == 0x80;
    }
    
    public boolean isInverted ( InputSourceD01 source ) {
        return ( configure[ source.configureIndex ] & 0x80 ) == 0x80;
    }

    /*
     * TODO rip it
     */
    public boolean isInverted ( OutputPinA03 pin ) {
        return ( configure[ pin.configureIndex ] & 0x80 ) == 0x80;
    }
    
    public boolean isInverted ( OutputPinA05 pin ) {
        return ( configure[ pin.configureIndex ] & 0x80 ) == 0x80;
    }
    public boolean isInverted ( OutputPinA07 pin ) {
        return ( configure[ pin.configureIndex ] & 0x80 ) == 0x80;
    }
    public boolean isInverted ( OutputPinC01 pin ) {
        return ( configure[ pin.configureIndex ] & 0x80 ) == 0x80;
    }
    public boolean isInverted ( OutputPinA01 pin ) {
        return ( configure[ pin.configureIndex ] & 0x80 ) == 0x80;
    }
    public boolean isInverted ( OutputPinD01 pin ) {
        return ( configure[ pin.configureIndex ] & 0x80 ) == 0x80;
    }

    public void setInputPin ( InputSourceC01 source, InputPinA03 pin, boolean inverted ) {
        configure[ source.configureIndex ] = ( byte )( inverted
                                                       ? pin.rawIOPos
                                                       : ~ pin.rawIOPos );
    }

    public void setOutputSource ( OutputPinA05 pin, OutputSourceD01 source ) {
        configure[ pin.configureIndex ] = ( byte )source.id;
    }
    
    public OutputSourceD01 getOutputSource ( OutputPinA03 pin ) {
        return OutputSourceD01.getByID( configure[ pin.configureIndex ] );
    }

    public OutputSourceD01 getOutputSource ( OutputPinA05 pin ) {
        return OutputSourceD01.getByID( configure[ pin.configureIndex ] );
    }
    
    public OutputSourceD01 getOutputSource ( OutputPinA07 pin ) {
        return OutputSourceD01.getByID( configure[ pin.configureIndex ] );
    }
    
    public OutputSourceC01 getOutputSource ( OutputPinC01 pin ) {
        return OutputSourceC01.getByID( configure[ pin.configureIndex ] );
    }
    
    public OutputSourceD01 getOutputSource ( OutputPinA01 pin ) {
        return OutputSourceD01.getByID( configure[ pin.configureIndex ] );
    }
    
    public OutputSourceD01 getOutputSource ( OutputPinD01 pin ) {
        return OutputSourceD01.getByID( configure[ pin.configureIndex ] );
    }
}
