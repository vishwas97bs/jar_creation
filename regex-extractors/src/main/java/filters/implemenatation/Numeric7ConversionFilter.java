package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;


public class Numeric7ConversionFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( Numeric7ConversionFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        //TODO Remove this filter or make it generic to convert all numerals
        List<ExtractedValue> outputList = new ArrayList<>();

        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue extVal : input ) {
                String changedFieldValue = ( (String) extVal.getValue() ).replaceAll( "[/]", "7" );
                extVal.setValue( changedFieldValue );
                outputList.add( extVal );
            }
        }

        LOG.trace( "List after filteration: {}", outputList );
        return outputList;
    }

}
