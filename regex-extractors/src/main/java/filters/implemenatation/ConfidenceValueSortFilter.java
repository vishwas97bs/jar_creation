package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.comparatorutil.ConfidenceValueComparator;

import java.util.Collections;
import java.util.List;


public class ConfidenceValueSortFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( ConfidenceValueSortFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        if ( input != null && !input.isEmpty() ) {
            Collections.sort( input, new ConfidenceValueComparator() );
            LOG.trace( "List after filteration: {}", input );
        }
        return input;
    }
}
