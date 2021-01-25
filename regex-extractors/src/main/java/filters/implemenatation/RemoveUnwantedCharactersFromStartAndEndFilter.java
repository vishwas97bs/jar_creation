package filters.implemenatation;

import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.Arrays;
import java.util.List;


/**
 * 
 * Removes unwanted characters
 *
 */
public class RemoveUnwantedCharactersFromStartAndEndFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( RemoveUnwantedCharactersFromStartAndEndFilter.class );
    private final List<String> unwantedCharactersFromEnd = Arrays.asList( "." );
    private final List<String> unwantedCharactersFromStart = Arrays.asList( "-" );

    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        for ( ExtractedValue inputVal : input ) {
            String val = inputVal.getValue().toString();
            for ( int counter = val.length() - 1; counter >= 0; counter-- ) {
                if ( unwantedCharactersFromEnd.contains( val.charAt( counter ) + "" ) ) {
                    val = val.substring( 0, counter );
                } else {
                    break;
                }
            }
            inputVal.setValue( val );
        }


        for ( ExtractedValue inputVal : input ) {
            String val = inputVal.getValue().toString();
            for ( int counter = 0; counter < val.length(); counter++ ) {
                if ( unwantedCharactersFromStart.contains( val.charAt( counter ) + "" ) ) {
                    val = val.substring( counter + 1 );
                } else {
                    break;
                }
            }
            inputVal.setValue( val );
        }

        LOG.trace( "List after filteration: {}", input );
        return input;
    }
}
