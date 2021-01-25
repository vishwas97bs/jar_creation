package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.comparatorutil.HighestIndexComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Filter class to sort end dates based on their indexes
 * @author Sharanya
 *
 */
public class SortEndDateFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( SortEndDateFilter.class );


    /**
     * Sorts end dates based on their indexes
     * @param input List of extracted values
     * @param helper
     * @return List containing the date having highest index on top
     */
    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        List<ExtractedValue> outputList = new ArrayList<>();
        if ( input != null && !input.isEmpty() ) {
            Collections.sort( input, new HighestIndexComparator() );
            outputList.addAll( input );
        }
        LOG.debug( "List after filteration: {}", outputList );
        return outputList;
    }

}
