package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.ExtractionOperationValidator;

import java.util.*;


/**
 * @author Abhilash S
 */
public class PunctuationsInMerchantNameFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( PunctuationsInMerchantNameFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        LOG.trace( "Entering filter method" );
        if ( input.isEmpty() ) {
            LOG.warn( "Input list found to be empty" );
            return input;
        }

        List<ExtractedValue> response = new ArrayList<>();

        for ( ExtractedValue value : input ) {
            if ( ExtractionOperationValidator.isValidMerchant( value ) ) {
                LOG.trace( "Adding valid merchant: {}", value.getValue() );
                response.add( value );
            } else {
                if ( value.getValue()!=null && !isPunctuationPresent( ( (String) value.getValue() ).toLowerCase() ) ) {
                    LOG.trace( "Adding corrected merchant: {}", value.getValue() );
                    response.add( value );
                }
            }
        }

        LOG.trace( "Finishing filter method" );
        return response;
    }


    /**
     * checks if the merchantName contains any punctuations ("/", "\", ";", "`", "~", "!", "@", "$", "%",
     "^", "_", "+", "{", "}", "|", ":", "<", ">", "?", "=", "[", "]")
     * @param merchantName merchantName to check for punctuations
     * @return true if punctuationCount > punctuationCountThreshold(2) else false
     */
    private boolean isPunctuationPresent( String merchantName )
    {
        LOG.trace( "Entering isPunctuationPresent method" );
        Set<Character> punctutations = Collections.unmodifiableSet( new HashSet<Character>( Arrays.asList( '/', '\\', ';', '`',
            '~', '!', '@', '$', '%', '^', '_', '+', '{', '}', '|', ':', '<', '>', '?', '=', '[', ']' ) ) );
        int punctuationCount = 0;
        int punctuationThreshold = 2;

        for ( char c : merchantName.toCharArray() ) {
            if ( punctutations.contains( c ) ) {
                punctuationCount++;
            }

            if ( punctuationCount > punctuationThreshold ) {
                return true;
            }
        }
        LOG.debug( "Filtering merchantName - {}", merchantName );
        return false;
    }

}
