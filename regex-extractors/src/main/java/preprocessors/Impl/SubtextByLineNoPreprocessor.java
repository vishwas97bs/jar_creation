package preprocessors.Impl;


import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;

import java.util.List;


public class SubtextByLineNoPreprocessor implements TextPreprocessor
{

    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        String subText = input;

        /*
         * Get the line configuration which is of form NoOfLinesToExtractFromTop:NoOfLinesToExtractFromBottom
         */


        List<String> lineConfig = helper.getValueList( fieldName + "_subtext_lines" );

        /* From the configuration, parse the line numbers */
        String[] lineNumbers = lineConfig.get( 0 ).split( ":" );
        int noOfLinesToExtractFromTop = Integer.parseInt( lineNumbers[0] );
        int noOfLinesToExtractFromBottom = Integer.parseInt( lineNumbers[1] );

        String[] textSplitByLine = input.split( "\r\n" );

        // to extract the top part of the text
        int noOfLinesToExtract = noOfLinesToExtractFromTop >= textSplitByLine.length ? textSplitByLine.length
            : noOfLinesToExtractFromTop;
        String topHalf = "";
        for ( int index = 0; index < noOfLinesToExtract; index++ ) {
            topHalf = topHalf.concat("\r\n"+ textSplitByLine[index] );
        }

        // to extract the bottom part of the text
        noOfLinesToExtract = noOfLinesToExtractFromBottom >= textSplitByLine.length ? textSplitByLine.length
            : noOfLinesToExtractFromBottom;
        String bottomHalf = "";
        for ( int index = textSplitByLine.length - noOfLinesToExtract; index < textSplitByLine.length; index++ ) {
            bottomHalf = bottomHalf.concat( "\r\n"+textSplitByLine[index] );
        }

        subText = topHalf.concat( bottomHalf );
        return subText;
    }
}
