package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 
 * @author Arun Gowda
 * 
 * Removes the values whose length is less than RECEIPT_NUMBER_MIN_LENGTH 
 *
 */
public class ReceiptNumberLengthFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( ReceiptNumberLengthFilter.class );

    private static final int RECEIPT_NUMBER_MIN_LENGTH = 2;


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        List<ExtractedValue> output = Collections.emptyList();

        if ( input != null ) {
            output = input.stream().filter( value -> value.getValue().toString().length() >= RECEIPT_NUMBER_MIN_LENGTH )
                .collect( Collectors.toList() );
        }
        LOG.trace( "List after filteration: {}", output );
        return output;
    }
}
