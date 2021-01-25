package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.GimletUtil;

import java.util.ArrayList;
import java.util.List;


public class DecimalSupportConversionFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( DecimalSupportConversionFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {

        List<ExtractedValue> outputList = new ArrayList<>();

        for ( ExtractedValue extVal : input ) {
            ExtractedValue extVal1 = new ExtractedValue( extVal );
            extVal1.setValue( GimletUtil.convertStringAmtToDecimalSupport( (String) extVal1.getValue() ) );
            outputList.add( extVal1 );
        }

        LOG.trace( "List after filtration: {}", outputList );
        return outputList;
    }
}
