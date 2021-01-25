package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.comparatorutil.FieldDetailsComparatorReversed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortFieldDetailFilterReversed implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( filters.implemenatation.SortFieldDetailFilterReversed.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        List<ExtractedValue> outputList = new ArrayList<ExtractedValue>( input );
        Collections.sort( outputList, new FieldDetailsComparatorReversed() );
        LOG.trace( "List after filteration: " + outputList );
        return outputList;
    }

}

