package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;


public class NumberFormatCheckerFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( NumberFormatCheckerFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {

        List<ExtractedValue> outputList = new ArrayList<>();

        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue extVal : input ) {
                String value = ( (String) extVal.getValue() ).trim();
                value = formatCurrency( value );
                extVal.setValue( value );
                outputList.add( extVal );
            }
        }

        LOG.trace( "List after filteration: {}", outputList );
        return outputList;
    }


    private String formatCurrency( String currencyValue )
    {
        String value = currencyValue;
        if ( value.contains( "," ) || value.contains( "." ) ) {

            String decimalPart = value.substring( value.length() - 3, value.length() );
            String nonDecimalPart = value.substring( 0, value.length() - 3 );

            if ( decimalPart.contains( "," ) || decimalPart.contains( "." ) ) {
                decimalPart = decimalPart.replace( ",", "." );
            }

            if ( nonDecimalPart.contains( "," ) || nonDecimalPart.contains( "." ) ) {
                nonDecimalPart = nonDecimalPart.replaceAll( "[,.]", "" );
            }

            value = nonDecimalPart + decimalPart;
            LOG.trace( "Value after formatting: {}", value );
            return value;
        } else {
            LOG.trace( "Nothing to format. Returning the value without any change." );
            return value;
        }
    }

}
