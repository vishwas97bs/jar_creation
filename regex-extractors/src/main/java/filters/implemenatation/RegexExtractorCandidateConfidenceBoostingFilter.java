package filters.implemenatation;


import constants.Constants;
import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import helper.gimletcore.helper.ExtractedValueHelper;
import helper.gimletcore.helper.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.Collections;
import java.util.List;


public class RegexExtractorCandidateConfidenceBoostingFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( RegexExtractorCandidateConfidenceBoostingFilter.class );
    private static final String BOOST_REGEX_MATCH = "enableRegexMatchBoost";
    private static final double BOOSTED_CONFIDENCE = Constants.BOOSTED_CONFIDENCE;
    private ResourceLoader resourceLoader = new ResourceLoader();


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        LOG.trace( "List before filteration: {}", input );

        List<ExtractedValue> response = input;
        if ( input == null || input.isEmpty() ) {
            LOG.warn( "inputList cannot be null / empty" );
            LOG.trace( "Finishing filter method" );
            return Collections.emptyList();
        }

        List<ExtractedValue> vicinityMatchList = ExtractedValueHelper.getExtractedValueByOperation( input,
            FieldMatch.VICINITY_MATCH.toString() );
        List<ExtractedValue> withoutVicinityMatchList = ExtractedValueHelper.getExtractedValueByOperation( input,
            FieldMatch.REG_WITHOUT_VIC.toString() );
        List<ExtractedValue> regexMatchList = ExtractedValueHelper.getExtractedValueByOperation( input,
            FieldMatch.REG_EX_MATCH.toString() );

        //        if ( withoutVicinityMatchList.size() == 1 ) {
        //            boostConfidence( withoutVicinityMatchList );
        //        }

        Boolean boostRegexMatch = Boolean.parseBoolean( resourceLoader.getCoreProperty( BOOST_REGEX_MATCH ) );
        LOG.info( "Enable Boost Regex Match: {}", boostRegexMatch );
        if ( boostRegexMatch && regexMatchList.size() == 1 && vicinityMatchList.isEmpty()
            && withoutVicinityMatchList.isEmpty() ) {
            boostConfidence( regexMatchList );
        }
        if ( boostRegexMatch && !withoutVicinityMatchList.isEmpty() && vicinityMatchList.isEmpty()
            && regexMatchList.isEmpty() ) {
            boostConfidence( withoutVicinityMatchList );
        }
        LOG.trace( "List after filteration: {}", response );
        return response;
    }


    private void boostConfidence( List<ExtractedValue> list )
    {
        LOG.trace( "Entering boostConfidence method" );
        for ( ExtractedValue value : list ) {
            value.setConfidence( BOOSTED_CONFIDENCE );
        }
        LOG.trace( "Finishing boostConfidence method" );
    }
}
