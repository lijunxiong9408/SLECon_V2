package slecon.component.iobardialog;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.DataFormatException;

import ocsjava.remote.configuration.Event.Item;




final class QueryEventsFromItem {
    private TreeMap<Item, Set<Integer>> itemMap = new TreeMap<>( new ItemComparator() );




    public void add ( Item item, int eventID ) {
        Set<Integer> set;
        synchronized ( this ) {
            set = itemMap.get( item );
            if ( set == null ) {
                set = new TreeSet<>();
                itemMap.put( item, set );
            }
        }
        set.add( eventID );
    }


    public void clearAll () {
        synchronized ( this ) {
            itemMap.clear();
        }
    }


    public Set<Integer> getEvents ( Item item ) {
        Set<Integer> set;
        synchronized ( this ) {
            set = itemMap.get( item );
        }
        return set;
    }


    static class ItemComparator implements Comparator<Item> {
        @Override
        public int compare ( Item o1, Item o2 ) {
            if ( o1 == o2 )
                return 0;

            int result;
            try {
                result = new Integer( ( ( int )o1.getBusConstant() ) & 0xFFFF ).compareTo( ( ( int )o2.getBusConstant() ) & 0xFFFF );
            } catch ( DataFormatException e ) {
                result = Integer.compare( o1.hashCode(), o2.hashCode() );
            }
            if ( result != 0 )
                return result;
            result = Integer.compare( ( ( int )o1.getId() ) & 0xff, ( ( int )o2.getId() ) & 0xff );
            if ( result != 0 )
                return result;
            result = Integer.compare( ( ( int )o1.getBit() ) & 0xff, ( ( int )o2.getBit() ) & 0xff );
            if ( result != 0 )
                return result;
            result = Integer.compare( ( ( int )o1.getValue() ) & 0xff, ( ( int )o2.getValue() ) & 0xff );
            if ( result != 0 )
                return result;
            return 0;
        }
    }
}
