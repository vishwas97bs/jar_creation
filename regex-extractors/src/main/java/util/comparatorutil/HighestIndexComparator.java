package util.comparatorutil;

import filters.ExtractedValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class HighestIndexComparator implements Comparator<ExtractedValue> {
    /**
     * Makes a comparison between two extracted values. If both extracted values are non-vicinity matches,value returned ensures the extracted value having highest index is at the top. If both the extracted values are vicinity matches, value returned ensures the extracted value having lowest index is at the top. If either one is a vicinity match, value returned ensures extracted value having vicinity is at the top.
     * @param value1 Extracted value 1
     * @param value2 Extracted value 2
     * @return Value resulting from comparison
     */
    @Override
    public int compare(ExtractedValue value1, ExtractedValue value2 )
    {
        if ( value1 != null && value2 != null ) {
            if ( !StringUtils.isEmpty( value1.getMatchedVicinity() ) && !StringUtils.isEmpty( value2.getMatchedVicinity() ) )
                return (int) ( value1.getIndex() - value2.getIndex() );
            if ( StringUtils.isEmpty( value1.getMatchedVicinity() ) && StringUtils.isEmpty( value2.getMatchedVicinity() ) )
                return (int) ( value2.getIndex() - value1.getIndex() );
            if ( !StringUtils.isEmpty( value1.getMatchedVicinity() ) )
                return -1;
            if ( !StringUtils.isEmpty( value2.getMatchedVicinity() ) )
                return 1;
        }
        return 0;
    }
}
