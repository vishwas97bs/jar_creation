package preprocessors.Impl;


import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;

import java.util.List;


public class SubtextByPercentLinesPreprocessor implements TextPreprocessor
{

    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        String subText = input;

        /*
         * Get the line configuration which is of form percentOfLinesToExtractFromTop:percentOfLinesToExtractFromBottom
         */


        List<String> lineConfig = helper.getValueList( "subtext_lines_percent" ); //TODO: Remove hard coded property value

        /* From the configuration, parse the percent of lines */
        String[] percentOfLines = lineConfig.get( 0 ).split( ":" );
        int percentOfLinesToExtractFromTop = Integer.parseInt( percentOfLines[0] );
        int percentOfLinesToExtractFromBottom = Integer.parseInt( percentOfLines[1] );

        String[] textSplitByLine = input.split( "\n" );

        // to extract the top part of the text
        int noOfLinesToExtract = (int) ( Math
            .round( ( percentOfLinesToExtractFromTop * 0.01 ) * textSplitByLine.length ) >= textSplitByLine.length
                ? textSplitByLine.length : Math.round( ( percentOfLinesToExtractFromTop * 0.01 ) * textSplitByLine.length ) );
        String topHalf = "";
        for ( int index = 0; index < noOfLinesToExtract; index++ ) {
            topHalf = topHalf.concat( textSplitByLine[index] );
        }

        // to extract the bottom part of the text
        noOfLinesToExtract = (int) ( Math
            .round( ( percentOfLinesToExtractFromBottom * 0.01 ) * textSplitByLine.length ) >= textSplitByLine.length
                ? textSplitByLine.length
                : Math.round( ( percentOfLinesToExtractFromBottom * 0.01 ) * textSplitByLine.length ) );
        String bottomHalf = "";
        for ( int index = textSplitByLine.length - noOfLinesToExtract - 1; index < textSplitByLine.length; index++ ) {
            bottomHalf = bottomHalf.concat( textSplitByLine[index] );
        }

        subText = topHalf.concat( bottomHalf );
        return subText;
    }

}
