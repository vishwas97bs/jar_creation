package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.comparatorutil.FieldDetailsComparator;

import java.util.Collections;
import java.util.List;


public class SortFieldDetailFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( SortFieldDetailFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        if ( input != null && !input.isEmpty() ) {
            Collections.sort( input, new FieldDetailsComparator() );
            LOG.trace( "List after filteration: {}", input );
        }
        return input;
    }

}
