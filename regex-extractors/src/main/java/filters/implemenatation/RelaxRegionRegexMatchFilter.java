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

public class RelaxRegionRegexMatchFilter implements ExtractionFilter {
    private static final Logger LOG = LoggerFactory.getLogger( RelaxRegionRegexMatchFilter.class );
    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {

        List<ExtractedValue> outputList = new ArrayList<>();

        if ( input != null && !input.isEmpty() ) {
            Iterator itr = input.iterator();
            while ( itr.hasNext() ) {
                ExtractedValue extVal = (ExtractedValue) itr.next();
                if ( extVal.getOperation().equals( FieldMatch.REGION_REGEX_MATCH.toString() ) )
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
