package preprocessors.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;

import java.util.ArrayList;
import java.util.List;


public class GVSpaceSeparatorForVicinityWordsPreprocessor implements TextPreprocessor
{
    private static final Logger LOG = LoggerFactory.getLogger( GVSpaceSeparatorForVicinityWordsPreprocessor.class );


    @Override
    public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        LOG.trace( "Removing extra spaces in the input from google vision for vicinity words" );
        List<String> vicinityList = ( helper.getVicinityList( fieldName ) == null ) ? ( new ArrayList<String>() )
            : helper.getVicinityList( fieldName );
        for ( String vicinity : vicinityList ) {

            if ( vicinity.trim().split( "\\s{1,2}" ).length >= 2 ) {
                if ( vicinity.charAt( 0 ) == '!' )
                    vicinity = vicinity.substring( 1 );
                String incorrect = vicinity.trim().replaceAll( "\\s", "  " );
                String combinedVicinity = vicinity.trim().replaceAll( "\\s", "" );

                String correct = incorrect.trim().replaceAll( "\\s{2}", " " );
                if ( input.contains( incorrect ) ) {
                    input = input.replace( incorrect, correct );

                }
                if ( input.contains( combinedVicinity ) ) {
                    input = input.replace( combinedVicinity, vicinity );
                }


            }
        }
        return input;

    }

}
