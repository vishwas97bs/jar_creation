package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * Removes all the duplicates and subset values of the greater value from the extracted value list
 *
 */
public class RemoveDuplicatesFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( RemoveDuplicatesFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        List<ExtractedValue> output = new ArrayList<>();
        LOG.trace( "List before filteration: {}", input );
        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue extractedValue : input ) {
                if ( !exists( output, extractedValue ) ) {
                    output.add( extractedValue );
                } else {
                    LOG.debug( "Duplicate element found: {}", extractedValue );
                }
            }
        }
        LOG.trace( "List after filteration: {}", output );
        return output;
    }


    private static boolean exists( List<ExtractedValue> list, ExtractedValue element )
    {
        //Duplicate check based on Operation, MatchedVicinity, MatchedValue .

        return list.stream()
            .filter( ( ExtractedValue ev ) -> ev.getOperation().equals( element.getOperation() )
                && ev.getMatchedVicinity().equals( element.getMatchedVicinity() )
                && ev.getValue().toString().equalsIgnoreCase( element.getValue().toString() ) )
            .count() != 0;
    }
}
