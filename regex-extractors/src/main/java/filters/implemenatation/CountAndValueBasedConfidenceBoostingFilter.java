package filters.implemenatation;


import constants.Constants;
import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.List;

import static java.lang.Math.abs;


/**
 * Filter to boost confidence of candidate with highest value based on it's count
 *
 */
public class CountAndValueBasedConfidenceBoostingFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( CountAndValueBasedConfidenceBoostingFilter.class );
    private static final double BOOSTED_CONFIDENCE = Constants.BOOSTED_CONFIDENCE;


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        if ( input != null && !input.isEmpty() ) {
            ExtractedValue highestValue = input.get( 0 );

            try {
                //Find the highest value in the list
                for ( ExtractedValue extractedValue : input ) {
                    if ( Double.parseDouble( extractedValue.getValue().toString() ) > Double
                        .parseDouble( highestValue.getValue().toString() ) ) {
                        highestValue = extractedValue;
                    }
                }
                LOG.info("Highest total value ={}",highestValue);
                //Get the count of the highest value
                int countOfHighest = 1;
                for ( ExtractedValue extractedValue : input ) {
                    if ( extractedValue.getValue().equals( highestValue.getValue() )
                        && abs( extractedValue.getIndex() - highestValue.getIndex() ) > ( (String) highestValue.getValue() )
                            .length() ) {
                        countOfHighest++;
                    }
                }
                LOG.info("highest value count ={}",countOfHighest);
                //If the count is greater than 2, increase the confidence of the value
                if ( countOfHighest >= 2 ) {
                    for ( ExtractedValue extractedValue : input ) {
                        if ( extractedValue.getValue().equals( highestValue.getValue() ) && (extractedValue.getOperation().equals(FieldMatch.REG_EX_MATCH.toString())||extractedValue.getOperation().equals(FieldMatch.REG_WITHOUT_VIC.toString())))
                        {
                            extractedValue.setConfidence( BOOSTED_CONFIDENCE );
                            LOG.info("boosted value ={}",extractedValue);
                        }
                    }
                }

            } catch ( NumberFormatException e ) {
                LOG.error(
                    "Values which are not in number format are present in the list. Cannot apply the filter. Exception: ", e );
            }


        }
        return input;
    }
}
