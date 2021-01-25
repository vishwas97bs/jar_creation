package preprocessors.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;

import java.util.List;


/**
 * Preprocessor to retrieve part of text using start and end line numbers
 *
 */
public class SubtextSequenceByLineNoPreprocessor implements TextPreprocessor
{
    private static final Logger LOG = LoggerFactory.getLogger( SubtextSequenceByLineNoPreprocessor.class );
    private static final String SEQ_EXTENSION = "_subtext_lines_sequence";


    /**
     * Retrieves sub text of input text from start line to end line. Line numbers should be present in the configuration.
     * @param input Input text
     * @param helper Extraction helper having the line number configuration
     * @param fieldName Name of the field whose line number configuration must be used
     * @return SubText
     */
    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {

        String subText = input;

        /*
         * Get the line configuration which is of form startLine:EndLine
         */

        List<String> lineConfig = helper.getValueList( fieldName + SEQ_EXTENSION );

        if ( lineConfig != null && !lineConfig.isEmpty() && lineConfig.get( 0 ).split( ":" ).length == 2 ) {
            /* From the configuration, parse the line numbers */
            String[] lines = lineConfig.get( 0 ).split( ":" );
            int startLine = 0;
            int endLine = 0;
            try {
                startLine = Integer.parseInt( lines[0] ) - 1;
                endLine = Integer.parseInt( lines[1] );
            } catch ( NumberFormatException e ) {
                LOG.error( "Exception encountered while parsing line numbers: " + e );
                return subText;
            }
            if ( startLine < 0 || endLine <= 0 ) {
                LOG.error( "Invalid line numbers: ({},{})", startLine, endLine );
                return subText;
            }
            int beginIndex = 0;
            int endIndex;
            if ( input != null && !input.isEmpty() ) {

                beginIndex = calculateLastIndex( input, startLine, beginIndex );

                endIndex = beginIndex;
                endIndex = calculateLastIndex( input, endLine, endIndex );

                /* Retrieve the sub text */
                subText = input.substring( beginIndex, endIndex );
            }
        }
        return subText;
    }


    /**
     * Calculate last index of a particular line from the beginning index in the input text
     * @param input Input text
     * @param lineNumber Line whose last index is to be found
     * @param beginIndex Beginning index
     * @return Last index of the line
     */
    private int calculateLastIndex( String input, int lineNumber, int beginIndex )
    {
        int lastIndex = beginIndex;
        for ( int index = 0; index < lineNumber; index++ ) {
            int nextIndex = input.indexOf( '\n', lastIndex + 1 );
            if ( nextIndex > lastIndex ) {
                lastIndex = nextIndex + 1;
            } else {
                lastIndex = input.length();
            }
        }
        return lastIndex;
    }

}
