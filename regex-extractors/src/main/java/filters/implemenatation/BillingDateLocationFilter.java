package filters.implemenatation;

import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.dateutil.DateUtil;

import java.util.ArrayList;
import java.util.List;


public class BillingDateLocationFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( BillingDateLocationFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        List<ExtractedValue> outputList = new ArrayList<>();
        boolean vicinityFound = false;

        for ( ExtractedValue extVal : input ) {
            if ( FieldMatch.VICINITY_MATCH.toString().equals( extVal.getOperation() ) ) {
                outputList.add( extVal );
                vicinityFound = true;
            } else if ( !vicinityFound && DateUtil.isDateWithinFixedYearGapRange( (String) extVal.getValue() ) ) {
                outputList.add( extVal );
            }
        }

        if ( !outputList.isEmpty() ) {
            LOG.trace( "List after filtration: {}", outputList );
            return outputList;
        } else {
            LOG.trace( "List after filtration: {}", input );
            return input;
        }
    }

}
