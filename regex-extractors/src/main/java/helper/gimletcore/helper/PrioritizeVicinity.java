package helper.gimletcore.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrioritizeVicinity {
    private static final Logger LOG = LoggerFactory.getLogger( PrioritizeVicinity.class );


    public static Map<String, Double> setPriorities(double baseValue, List<String> vicinityWords )
    {
        Map<String, Double> confidenceMap = new HashMap<>();
        int divisor = getDivisor( baseValue );
        int wordLength = String.valueOf( vicinityWords.size() ).length();

        int n = divisor + 1;
        int x = wordLength - 1;
        LOG.debug( "Divide by {}", (int) Math.pow( 10, n + x ) );
        for ( String keyword : vicinityWords ) {
            double dividend = vicinityWords.size() - vicinityWords.indexOf( keyword );
            confidenceMap.put( keyword, baseValue + ( ( dividend / Math.pow( 10, n + x ) ) ) );
        }
        return confidenceMap;
    }


    public static int getDivisor( double baseValue )
    {
        String text = Double.toString( Math.abs( baseValue ) );
        int integerPlaces = text.indexOf( '.' );
        int decimalPlaces = text.length() - integerPlaces - 1;
        return decimalPlaces;
    }
}
