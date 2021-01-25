package util.comparatorutil;

import filters.ExtractedValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class FieldDetailsComparatorReversed implements Comparator<ExtractedValue> {
    @Override
    public int compare(ExtractedValue o1, ExtractedValue o2 )
    {

        if ( !StringUtils.isEmpty( o1.getMatchedVicinity() ) && !StringUtils.isEmpty( o2.getMatchedVicinity() ) )
            return (int) ( o2.getIndex() - o1.getIndex() );
        if ( StringUtils.isEmpty( o1.getMatchedVicinity() ) && StringUtils.isEmpty( o2.getMatchedVicinity() ) )
            return (int) ( o2.getIndex() - o1.getIndex() );
        if ( !StringUtils.isEmpty( o1.getMatchedVicinity() ) )
            return 1;
        if ( !StringUtils.isEmpty( o2.getMatchedVicinity() ) )
            return -1;
        return 0;
    }
}
