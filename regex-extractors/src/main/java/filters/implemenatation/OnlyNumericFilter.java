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
 * Filter validates whether there is a number present or not 
 * 
 */
public class OnlyNumericFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( OnlyNumericFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        List<ExtractedValue> output = Collections.emptyList();

        if ( input != null ) {
            output = input.stream().filter( value -> value.getValue().toString().chars().anyMatch( Character::isDigit ) )
                .collect( Collectors.toList() );
        }
        LOG.trace( "List after filteration: {}", output );
        return output;
    }
}
