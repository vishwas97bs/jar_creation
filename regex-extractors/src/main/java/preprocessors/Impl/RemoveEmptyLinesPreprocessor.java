package preprocessors.Impl;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;

import java.util.ArrayList;
import java.util.List;


public class RemoveEmptyLinesPreprocessor implements TextPreprocessor
{

    private static final Logger LOG = LoggerFactory.getLogger( RemoveEmptyLinesPreprocessor.class );


    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        String preProcessedText = input;
        List<String> textWithoutEmptyLines = new ArrayList<>();

        if ( !StringUtils.isEmpty( input ) ) {
            LOG.trace( "Text before preprocessing: {}", preProcessedText );
            String[] textSplitByLine = preProcessedText.split( "\r?\n" );

            for ( String line : textSplitByLine ) {
                if ( !StringUtils.isEmpty( line.trim() ) ) {
                    textWithoutEmptyLines.add( line );
                }
            }

            if ( !textWithoutEmptyLines.isEmpty() ) {
                StringBuilder sb = new StringBuilder();
                for ( String s : textWithoutEmptyLines ) {
                    sb.append( s );
                    sb.append( System.lineSeparator() );
                }
                preProcessedText = sb.toString();
            }
            LOG.trace( "Text after preprocessing: {}", preProcessedText );
        }

        return preProcessedText;
    }

}
