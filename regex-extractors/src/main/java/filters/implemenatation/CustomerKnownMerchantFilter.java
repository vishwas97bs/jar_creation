package filters.implemenatation;


import constants.merchantsconstant.MerchantNameExtractorType;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.List;
import java.util.stream.Collectors;


/**sorts extracted value to prioritize output from CUSTOMER_KNOWN_MERCHANT extractor
 * @author vipuldb
 */
public class CustomerKnownMerchantFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( CustomerKnownMerchantFilter.class );
    @Override public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        LOG.trace( "Entering filter method" );
        if (input == null || input.isEmpty() ) {
            LOG.warn( "Input list found to be empty" );
            return input;
        }
        return input.stream().sorted( (ExtractedValue value1,ExtractedValue value2)->{
            if(value1.getOperation().equals( MerchantNameExtractorType.CUSTOMER_KNOWN_MERCHANT ))
                return -1;
            if(value2.getOperation().equals( MerchantNameExtractorType.CUSTOMER_KNOWN_MERCHANT ))
                return 1;
            return 0;
        } ).collect( Collectors.toList());
    }
}
