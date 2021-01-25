package filters.implemenatation;


import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;


/**
 * Filter class to retain only values extracted by VICINITY_MATCH or REG_WITHOUT_VIC match type
 * @author Sharanya
 *
 */
public class StrictRegexMatchFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( StrictRegexMatchFilter.class );


    /**
     * Removes values which are not matched by VICINITY_MATCH or REG_WITHOUT_VIC
     * @param input Input list of extracted values
     * @param helper
     * @return List after filtering
     */
    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {

        List<ExtractedValue> outputList = new ArrayList<>();

        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue extVal : input ) {
                if ( extVal.getOperation().equals( FieldMatch.REG_WITHOUT_VIC.toString() )
                    || extVal.getOperation().equals( FieldMatch.VICINITY_MATCH.toString() ) ) {
                    outputList.add( extVal );
                }
            }
        }

        LOG.trace( "List after filteration: {}", outputList );
        return outputList;
    }

}
