package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.GimletUtil;

import java.util.ArrayList;
import java.util.List;


public class NumericConversionFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( NumericConversionFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {

        List<ExtractedValue> outputList = new ArrayList<>();

        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue extVal : input ) {
                //TODO : Difference between Numeric7Conversion & this class
                extVal.setValue( GimletUtil.convertNumericSupportedValues( (String) extVal.getValue() ) );
                outputList.add( extVal );
            }
        }

        LOG.trace( "List after filteration: {}", outputList );
        return outputList;
    }

}
