package helper.gimletcore.helper;

import filters.ExtractedValue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ExtractedValueHelper {
    private static final Logger LOG = LoggerFactory.getLogger( ExtractedValueHelper.class );


    private ExtractedValueHelper()
    {}


    public static List<ExtractedValue> getExtractedValueByOperation(List<ExtractedValue> list, String operation )
    {
        LOG.trace( "Entering getExtractedValueByOperation method" );
        List<ExtractedValue> evList = null;

        if ( list == null || list.isEmpty() ) {
            LOG.error( "The list cannot be empty / null\nFound: {}", operation );
            return list;
        }

        if ( StringUtils.isBlank( operation ) ) {
            LOG.error( "The operation cannot be null / empty\nFound: {}", operation );
        }

        evList = new ArrayList<>();
        for ( ExtractedValue ev : list ) {
            if ( ev.getOperation().equalsIgnoreCase( operation ) ) {
                evList.add( ev );
            }
        }

        LOG.trace( "Finishing getExtractedValueByOperation method" );
        return evList;
    }


    public static Boolean isEqual( ExtractedValue ev1, ExtractedValue ev2 )
    {
        return ev1 != null && ev2 != null && ev1.getValue().equals( ev2.getValue() )
                && Double.compare( ev1.getConfidence(), ev2.getConfidence() ) == 0
                && ev1.getOperation().equalsIgnoreCase( ev2.getOperation() ) && Long.compare( ev1.getIndex(), ev2.getIndex() ) == 0
                && ev1.getMatchedVicinity().equalsIgnoreCase( ev2.getMatchedVicinity() );
    }
}
