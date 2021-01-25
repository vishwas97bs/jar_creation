package filters.implemenatation;


import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Filter class to retain values extracted by REG_WITHOUT_VIC match
 * @author admin
 *
 */
public class RelaxRegexWithoutVicinityMatchFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( RelaxRegexWithoutVicinityMatchFilter.class );


    /**
     * Filters list to retrieve only REG_WITHOUT_VIC matched values if any,otherwise returns the input list without change
     * @param input Input list of extracted values
     * @param helper
     * @return List after filtering
     */
    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {

        List<ExtractedValue> outputList = new ArrayList<>();

        if ( input != null && !input.isEmpty() ) {
            Iterator itr = input.iterator();
            while ( itr.hasNext() ) {
                ExtractedValue extVal = (ExtractedValue) itr.next();
                if ( extVal.getOperation().equals( FieldMatch.REG_WITHOUT_VIC.toString() ) )
                {
                    outputList.add( extVal );

                }
            }
            if ( outputList.isEmpty() ) {
                outputList = input;
            }
        }

        LOG.trace( "List after filteration: {}", outputList );
        return outputList;
    }

}
