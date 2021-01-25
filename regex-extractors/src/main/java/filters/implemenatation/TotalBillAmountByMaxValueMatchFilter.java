package filters.implemenatation;


import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.GimletUtil;

import java.util.*;


public class TotalBillAmountByMaxValueMatchFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( TotalBillAmountByMaxValueMatchFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {

        Set<ExtractedValue> inputSet;
        List<ExtractedValue> outputList = new ArrayList<>();
        List<ExtractedValue> resultList = new ArrayList<>();
        Map<Object, ExtractedValue> repeated = new HashMap<>();
        Set<Object> set1 = new HashSet<>();


        ExtractedValue maxValue;

        if ( input != null && !input.isEmpty() ) {

            maxValue = input.get( 0 );
            for ( ExtractedValue extVal : input ) {
                if ( extVal.getOperation() == FieldMatch.VICINITY_MATCH.toString() ) {
                    outputList.add( extVal );
                }

                if ( GimletUtil.convertStringAmtToDecimal( (String) extVal.getValue() )
                    .compareTo( GimletUtil.convertStringAmtToDecimal( (String) maxValue.getValue() ) ) > 0 ) {
                    maxValue = extVal;
                }
            }
            boolean containsTotalVicinity = false;
            if ( outputList.size() == input.size() ) {
                inputSet = new HashSet<>( outputList );
                for ( ExtractedValue value : inputSet ) {
                    if(value.getMatchedVicinity().contains("total")) {
                        containsTotalVicinity = true;
                    }
                    if ( !set1.add( value.getValue() ) ) {
                        repeated.put( value.getValue(), value );
                    }
                }
                if ( repeated.size() == 1 && !containsTotalVicinity) {
                    resultList.add( repeated.get( repeated.keySet().iterator().next() ) );
                    return resultList;
                }
                outputList = new SortFieldDetailFilterReversed().filter( outputList, null );
                outputList = new SortBasedOnValueFilterReversed().filter( outputList, null );
                resultList.add( new ConfidenceValueSortFilter().filter( outputList, null ).get( 0 ) );
                return resultList;
            }
            outputList.add( maxValue );
        }

        LOG.trace( "List after filteration: {}", outputList );
        return outputList;
    }

}
