package preprocessors.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;

import java.util.List;


/**
 * Shortening the input text using the start and end points
 * 
 * @author parag
 *
 */
public class StrictShortenTextPreprocessor implements TextPreprocessor
{
    private static final Logger LOG = LoggerFactory.getLogger( StrictShortenTextPreprocessor.class );


    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        String textAfter = "";
        int startFinalIndex = 99999;
        int endFinalIndex = 99999;
        List<String> startPoint = helper.getAllValueList( fieldName, "_start" );
        List<String> endPoint = helper.getAllValueList( fieldName, "_end" );
        if ( startPoint.isEmpty() ) {
            LOG.debug( "Start Point not found" );
            startFinalIndex = 0;
            return textAfter;
        }
        for ( String str : startPoint ) {
            int startIndex = input.indexOf( str, 0 );
            startFinalIndex = ( startIndex < startFinalIndex && startIndex != -1 ) ? startIndex : startFinalIndex;
        }
        for ( String str : endPoint ) {
            int endIndex = input.indexOf( str, 0 );
            endFinalIndex = ( endIndex < endFinalIndex && endIndex != -1 ) ? endIndex : endFinalIndex;
        }
        if ( startFinalIndex == 99999 ) {
            LOG.debug( "Start Point not found" );
            return textAfter;
        }
        if ( endFinalIndex < startFinalIndex || endFinalIndex == 99999 ) {
            LOG.debug( "End point not found" );
            textAfter = input.substring( startFinalIndex );
        } else {
            LOG.debug( "start and end point found" );
            textAfter = input.substring( startFinalIndex, endFinalIndex );
        }
        LOG.trace( "Text after preprocessing: {}", textAfter );
        return textAfter;
    }

}
