package preprocessors.Impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;


public class RateSetOnSymbolConversionPreprocessor implements TextPreprocessor
{

    private static final Logger LOG = LoggerFactory.getLogger( RateSetOnSymbolConversionPreprocessor.class );


    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        String preProcessedText = input;

        if ( !StringUtils.isEmpty( input ) && input != null ) {
            LOG.trace( "Text before preprocessing: " + preProcessedText );
            preProcessedText = preProcessedText.replace( "rat«", "rate" );
            preProcessedText = preProcessedText.replace( "b«", "be" );
            preProcessedText = preProcessedText.replace( "eat", "set" );
            LOG.trace( "Text after preprocessing: " + preProcessedText );
        }

        return preProcessedText;
    }

}
