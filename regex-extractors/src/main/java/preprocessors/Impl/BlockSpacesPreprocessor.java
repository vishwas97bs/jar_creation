package preprocessors.Impl;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;


/**
 * Replaces huge spaces which are block separators by 5 spaces. 
 * Replaces less than 5 spaces by 2 which are caused by raw text issues.
 * 
 * 
 * @author Arun Gowda
 *
 */
public class BlockSpacesPreprocessor implements TextPreprocessor
{

    private static final Logger LOG = LoggerFactory.getLogger( BlockSpacesPreprocessor.class );
    private static final String BLOCKS_SEPARATOR = "     ";
    private static final String TWO_SPACES = "  ";


    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        String preProcessedText = input;

        if ( !StringUtils.isEmpty( input ) ) {
            LOG.trace( "Text before preprocessing: {}", preProcessedText );
            // Replace huge spaces by 5 spaces
            preProcessedText = preProcessedText.replaceAll( "[ ]{5,}", BLOCKS_SEPARATOR );
            // Replace extra space caused by raw text issue by 2 spaces.
            preProcessedText = preProcessedText.replaceAll( "(?<! )[ ]{2,4}(?! )", TWO_SPACES );
            LOG.trace( "Text after preprocessing: {}", preProcessedText );
        }

        return preProcessedText;
    }

}
