package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;


public class FirstValueSelectionFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( FirstValueSelectionFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {

        List<ExtractedValue> outputList = new ArrayList<>();

        if ( input != null && !input.isEmpty() ) {
            ExtractedValue extVal = input.get( 0 );
            outputList.add( extVal );
        }

        LOG.trace( "List after filtration: {}", outputList );
        return outputList;
    }

}
