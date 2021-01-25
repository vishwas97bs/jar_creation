/**
 * 
 */
package preprocessors.Impl;


import data.LineItemResult;
import data.Rule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;
import service.FindItemsWithNumbersRule;
import service.FindTablesRule;
import util.FindMaxCharsRule;
import util.lineitemutil.LineItemUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author infrrd
 *
 */
public class RemoveLineItemsPreProcessor implements TextPreprocessor
{

    private static final Logger LOG = LoggerFactory.getLogger( RemoveLineItemsPreProcessor.class );
    private static final String PLACEHOLDER_STR = "========REMOVE=========";


    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        String preProcessedText = input;
        List<Rule> lineItemFinders = Arrays.asList( new FindTablesRule(), new FindMaxCharsRule(),
            new FindItemsWithNumbersRule() );

        if ( !StringUtils.isEmpty( input ) ) {
            List<String> lines = new ArrayList<>( Arrays.asList( input.split( "\\r?\\n" ) ) );
            List<LineItemResult> results = new ArrayList<>();
            for ( Rule lineItemFinder : lineItemFinders ) {
                results.addAll( lineItemFinder.apply( lines ) );
            }
            results = LineItemUtils.convergeResults( lines, results );
            for ( LineItemResult result : results ) {
                int startIdx = result.getStartIdx();
                int endIdx = result.getEndIdx();
                LOG.info( "Removing items from line {} to {}", startIdx, endIdx );
                for ( int index = startIdx; index <= endIdx; index++ ) {
                    lines.set( index, PLACEHOLDER_STR );
                }

            }

            lines.removeAll( Arrays.asList( PLACEHOLDER_STR ) );

            if ( !lines.isEmpty() ) {
                StringBuilder sb = new StringBuilder();
                for ( String s : lines ) {
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
