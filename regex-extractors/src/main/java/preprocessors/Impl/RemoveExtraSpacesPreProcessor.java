package preprocessors.Impl;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;


public class RemoveExtraSpacesPreProcessor implements TextPreprocessor
{

    private static final Logger LOG = LoggerFactory.getLogger( RemoveExtraSpacesPreProcessor.class );


    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        String preProcessedText = input;

        if ( !StringUtils.isEmpty( input ) ) {
            LOG.trace( "Text before preprocessing: {}", preProcessedText );
            // Remove extra spaces by replacing multiple spaces with 2 spaces
            preProcessedText = replaceMultipleSpaces( preProcessedText );
            LOG.trace( "Text after preprocessing: {}", preProcessedText );
        }

        return preProcessedText;
    }


    /**
     * Replace multiple spaces with 2 spaces
     *
     * @param data
     *            the data
     * @return the string
     */
    private String replaceMultipleSpaces( String data )
    {
        return data.replaceAll( " {3,}", "  " );
    }
}
