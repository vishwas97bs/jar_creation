package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;


/**
 * Filter class to separate unique vat numbers from the list of extracted values
 * @author Sharanya
 *
 */
public class MultipleVatNumbersFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( MultipleVatNumbersFilter.class );


    /**
     * Filters multiple vat numbers (if present) from the list of extracted values
     * @param inputList List of extracted values
     * @param helper
     * @return Filtered list
     */
    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> inputList, ExtractionData helper )
    {
        List<ExtractedValue> outputList = new ArrayList<>();
        List<String> uniqueVatNumbers = new ArrayList<>();
        ExtractedValue vatNumValue;
        if ( inputList != null && !inputList.isEmpty() ) {
            uniqueVatNumbers.add( ( (String) inputList.get( 0 ).getValue() ).trim() );
            populateUniqueVatNumbers( inputList, uniqueVatNumbers );
        }
        if ( !uniqueVatNumbers.isEmpty() ) {
            /*When there are multiple vat numbers present, return the list*/
            vatNumValue = new ExtractedValue( uniqueVatNumbers, "", "", 0, 0.8 );
            outputList.add( vatNumValue );
        }
        return outputList;
    }


    /**
     * Separates unique vat numbers from the list of extracted values
     * @param input List of extracted values
     * @param uniqueVatNumbers List of unique vat numbers
     */
    private void populateUniqueVatNumbers( List<ExtractedValue> input, List<String> uniqueVatNumbers )
    {
        for ( ExtractedValue value : input ) {
            boolean alreadyExists = false;
            for ( String vatNum : uniqueVatNumbers ) {
                /*Removing the spaces,delimiters and country code to compare the two vat numbers*/
                String extractedValue = ( (String) value.getValue() ).replaceAll( "[^0-9]", "" );
                vatNum = vatNum.replaceAll( "[^0-9]", "" );
                if ( vatNum.contains( extractedValue ) || extractedValue.contains( vatNum ) ) {
                    alreadyExists = true;
                    break;
                }
            }
            if ( !alreadyExists ) {
                uniqueVatNumbers.add( (String) value.getValue() );
            }
        }
    }

}
