package slecon.inspect.logs;

import static logic.evlog.Level.CRITICAL;
import static logic.evlog.Level.WARNING;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logic.evlog.Level;




public enum TypeOptions {
    Warning( new Level[]{ WARNING } ), Critical( new Level[]{ CRITICAL } ), WarningAndCritical( new Level[]{ WARNING, CRITICAL } ),
    All( Level.values() );
    
    public final Set<Level> levels;
    private final String str;



    private TypeOptions ( Level[] levels ) {
        List<Level> list = Arrays.asList( levels );
        this.levels = new HashSet<Level>( list );
        this.str = Main.TEXT.getString( "TypeOption." + name() );
    }


    public String toString () {
        return str;
    }
}
