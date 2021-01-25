package filters.implemenatation;

import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.merchantutil.MerchantNameExtractionUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by madhury on 21/9/17.
 */
public class ValidateReciepientFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( ValidateReciepientFilter.class );
    @Override public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        List<ExtractedValue> outputList = new ArrayList<ExtractedValue>();

        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue extVal : input ) {
               String validName= MerchantNameExtractionUtil.extractPotentialName( extVal.getValue().toString() );
                if ( validName != null )
                    outputList.add( extVal );
            }
        }
        return outputList;
    }
}
