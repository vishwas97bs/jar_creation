package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.ExtractionOperationValidator;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Abhilash S
 */
public class RepetitiveCharMerchantNameFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( RepetitiveCharMerchantNameFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        if ( input.isEmpty() ) {
            return input;
        }

        List<ExtractedValue> response = new ArrayList<>();

        for ( ExtractedValue value : input ) {
            if ( ExtractionOperationValidator.isValidMerchant( value) ) {
                response.add( value );
            } else {
                String originalVal = (String) value.getValue();
                if ( !containsRepetitiveChars( originalVal.toLowerCase() ) ) {
                    response.add( value );
                }
            }
        }

        return response;
    }


    //TODO To verify the impact of this check for valid merchantNames & decide the imminentCharRepThreshold
    /**
     * Checks for the count of imminent repetitions of a single character & returns
     * Ex: MerchantName = mnmmmmm, Here the char 'm' is repeated 5 times & is > 2 (imminentCharRepThreshold).Hence, False
     * @param merchantName String in which to check for imminent repetitive Char count
     * @return true if none of the characters in the merchant has an imminent repetitive char count > imminentCharRepThreshold else false
     */
    private boolean checkImminentCharRepetition( String merchantName )
    {
        int imminentCharRepThreshold = 2;
        int repetitiveCharCount;

        int index = 0;
        char[] charArray = merchantName.toCharArray();
        int length = merchantName.length() - 1;
        while ( index < length ) {
            repetitiveCharCount = 0;
            if ( charArray[index] == charArray[index + 1] ) {
                repetitiveCharCount++;
                while ( true ) {
                    if ( index < length && charArray[index] == charArray[index + 1] ) {
                        index++;
                        repetitiveCharCount++;
                    } else {
                        break;
                    }
                }
                if ( repetitiveCharCount > imminentCharRepThreshold ) {
                    return false;
                }
            }
            index++;
        }
        return true;
    }


    /**
     * Checks if there is a repetitive character which is non-alphanumeric excluding ? {Question Mark} and <SPACE>
     * @param merchantName string in which to check for repetitive character's
     * @return True, if merchantName contains repetitions of Non-AlphaNumeric , ? {Question Mark} & <SPACE> else False.
     */
    private boolean containsRepetitiveChars( String merchantName )
    {

        int index = 0;
        char[] charArray = merchantName.toCharArray();
        int length = merchantName.length() - 1;
        while ( index < length ) {
            if ( charArray[index] == charArray[index + 1] && isValidRepetitiveCharacter( charArray[index] ) ) {
                LOG.info( "Filtering merchantName - {}", merchantName );
                return true;
            }
            index++;
        }
        return false;
    }


    private boolean isValidRepetitiveCharacter( char c )
    {
        return Character.isLetterOrDigit( c ) ? false
            : ( Character.compare( c, '?' ) == 0 || Character.isSpaceChar( c ) ) ? false : true;
    }
}
