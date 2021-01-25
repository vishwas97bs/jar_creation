package filters.implemenatation;


import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.GimletUtil;

import java.util.ArrayList;
import java.util.List;


public class GetSecondLargestValueFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( GetSecondLargestValueFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        List<ExtractedValue> outputList = new ArrayList<>();
        List<ExtractedValue> tempList = new ArrayList<>();
        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue extVal : input ) {
                if ( extVal.getOperation().equals( FieldMatch.VICINITY_MATCH.toString() ) )
                    tempList.add( extVal );
            }
        }

        LOG.trace( "List after filteration: " + outputList );
        if ( tempList != null || !tempList.isEmpty() ) {
            return input;
        } else {
            ExtractedValue secondLargest = input.get( 0 );
            ExtractedValue largest = input.get( 0 );
            for ( ExtractedValue extVal : input ) {

                if ( GimletUtil.convertStringAmtToDecimal( (String) extVal.getValue() )
                    .compareTo( GimletUtil.convertStringAmtToDecimal( (String) largest.getValue() ) ) == 1 ) {
                    secondLargest = largest;
                    largest = extVal;
                }
                if ( GimletUtil.convertStringAmtToDecimal( (String) extVal.getValue() )
                    .compareTo( GimletUtil.convertStringAmtToDecimal( (String) secondLargest.getValue() ) ) == 1
                    && extVal != largest ) {
                    secondLargest = extVal;
                }
            }
            outputList.add( secondLargest );
            return outputList;
        }
        // TODO Auto-generated method stub
    }

}
