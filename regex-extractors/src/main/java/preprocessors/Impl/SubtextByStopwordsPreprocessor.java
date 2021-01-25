package preprocessors.Impl;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;

import java.util.List;


/**
 * Preprocessor to get sub-text based on stop-words
 * @author Sharanya
 *
 */
public class SubtextByStopwordsPreprocessor implements TextPreprocessor
{
    private static final Logger LOG = LoggerFactory.getLogger( SubtextByStopwordsPreprocessor.class );


    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        String subText = input;
        if ( !StringUtils.isEmpty( subText ) ) {
            int minIndexOfStopword = subText.length();

            //Get the stop words configuration
            List<String> stopwords = helper.getValueList( fieldName + "_subtext_stopwords" );

            if ( stopwords != null ) {
                for ( String stopword : stopwords ) {
                    if ( input.contains( stopword ) ) {
                        int indexOfStopword = subText.indexOf( stopword );
                        LOG.trace( "Stopword {} was found at index {}", stopword, indexOfStopword );
                        if ( indexOfStopword < minIndexOfStopword ) {
                            LOG.debug( "Subtext index being changed from {} to {}", minIndexOfStopword, indexOfStopword );
                            minIndexOfStopword = indexOfStopword;
                        }
                    }
                }
            }

            subText = subText.substring( 0, minIndexOfStopword );
        }
        return subText;
    }
}
