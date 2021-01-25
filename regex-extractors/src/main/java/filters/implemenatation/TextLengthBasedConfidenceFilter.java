package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.List;

import static common.Utils.getLineCountOfOcrText;

/**
 * Sets the confidence to 0 if the text length is greater than 100 lines
 */
public class TextLengthBasedConfidenceFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( TextLengthBasedConfidenceFilter.class );
    private static final int LINE_THRESHOLD = 100;
    private static final double LOW_CONFIDENCE = 0;


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        int lineCount = getLineCountOfOcrText( helper.getOcrText() );
        LOG.trace( "line count of {} is {}", helper.getScanRequest().getScanRequestId(), lineCount );
        if ( lineCount > LINE_THRESHOLD ) {
            for ( ExtractedValue value : input )
                value.setConfidence( LOW_CONFIDENCE );
        }

        return input;
    }
}
