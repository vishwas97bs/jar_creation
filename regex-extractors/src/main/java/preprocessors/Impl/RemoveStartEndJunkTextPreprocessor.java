package preprocessors.Impl;


import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;


public class RemoveStartEndJunkTextPreprocessor implements TextPreprocessor
{

    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        String response = "";
        String lines[] = input.split( "[\r\n]" );
        if ( lines.length > 0 ) {
            String firstLine = lines[0];
            String firstLineReplacement = getReplacement( firstLine );
            input = input.replace( firstLine, firstLineReplacement );
            String lastLine = lines[lines.length - 1];
            String lastLineReplacement = getReplacement( lastLine );
            input = input.replace( lastLine, lastLineReplacement );
            response = input.trim();
        } else {
            response = input;
        }

        return response;
    }


    private String getReplacement( String line )
    {
        return line.replace( "ï»¿", "" );// To remove Byte Order Mark (ï»¿)
    }
}
