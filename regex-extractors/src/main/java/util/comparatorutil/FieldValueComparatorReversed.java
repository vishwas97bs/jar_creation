package util.comparatorutil;

import filters.ExtractedValue;

import java.util.Comparator;

public class FieldValueComparatorReversed implements Comparator<ExtractedValue> {
    @Override
    public int compare( ExtractedValue o1, ExtractedValue o2 )
    {
        if ( o1.getMatchedVicinity().equalsIgnoreCase( o2.getMatchedVicinity() ) ) {
            return (int) ( Double.valueOf( o2.getValue().toString() ) - Double.valueOf( o1.getValue().toString() ) );
        }
        return 0;
    }

}
