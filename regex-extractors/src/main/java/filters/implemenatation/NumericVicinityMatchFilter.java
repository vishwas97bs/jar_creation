package filters.implemenatation;


import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.GimletUtil;

import java.util.ArrayList;
import java.util.List;


public class NumericVicinityMatchFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( NumericVicinityMatchFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {

        List<ExtractedValue> outputList = new ArrayList<ExtractedValue>();

        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue extVal : input ) {
                if ( extVal.getOperation() == FieldMatch.VICINITY_MATCH.toString() ) {
                    extVal.setValue( GimletUtil.convertNumericSupportedValues( (String) extVal.getValue() ) );
                    outputList.add( extVal );
                }
            }
        }

        LOG.trace( "List after filteration: " + outputList );
        return outputList;
    }
}
