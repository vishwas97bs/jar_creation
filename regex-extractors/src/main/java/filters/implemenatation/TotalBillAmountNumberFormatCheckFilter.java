package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TotalBillAmountNumberFormatCheckFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( TotalBillAmountNumberFormatCheckFilter.class );

    //Locales which use comma as decimal separator.
    private static final List<String> DECIMAL_COMMA_LOCALES = Arrays.asList( "es" );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        List<ExtractedValue> outputList = new ArrayList<>();

        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue extVal : input ) {
                String value = ( (String) extVal.getValue() ).trim();
                value = formatAmount( value, helper.getLocale() );
                extVal.setValue( value );
                outputList.add( extVal );
            }
        }

        LOG.trace( "List after filteration: {}", outputList );
        return outputList;
    }


    private String formatAmount( String amountValue, String locale )
    {
        String amount = amountValue;
        if ( amount.contains( "," ) || amount.contains( "." ) ) {

            //Assuming that there will never be 3 digits after decimal point.
            if ( DECIMAL_COMMA_LOCALES.contains( locale ) && amount.length() - amount.lastIndexOf( '.' ) != 3
                && amount.length() - amount.lastIndexOf( ',' ) != 4 ) {
                //case of 1.234,56 1.234 which is mostly thousands.
                //decimal is `,` and thousands is `.` so Swap `,` and `.`
                amount = amount.replaceAll( "[,]", "*" );
                amount = amount.replaceAll( "[.]", "," );
                amount = amount.replaceAll( "[*]", "." );
            } //otherwise, it is of format 1,234 or 12.34 

            int decimalIndex = Math.max( amount.lastIndexOf( '.' ), amount.lastIndexOf( ',' ) );
            String decimalPart = amount.substring( decimalIndex, amount.length() );
            String nonDecimalPart = amount.substring( 0, decimalIndex );
            if ( nonDecimalPart.trim().equals( "0" ) ) {
                nonDecimalPart = nonDecimalPart.replaceAll( "[,.]", "." );
                amount = nonDecimalPart + decimalPart;
                LOG.trace( "Value after formatting: {}", amount );
                return amount;
            }
            if ( decimalPart.contains( "," ) || decimalPart.contains( "." ) ) {
                if ( decimalPart.length() < 4 )
                    decimalPart = decimalPart.replace( ",", "." );
                else {
                    decimalPart = decimalPart.replace( ",", "" );
                }
            }

            if ( nonDecimalPart.contains( "," ) || nonDecimalPart.contains( "." ) ) {
                nonDecimalPart = nonDecimalPart.replaceAll( "[,.]", "" );
            }

            amount = nonDecimalPart + decimalPart;
            LOG.trace( "Value after formatting: {}", amount );
            return amount;
        } else {
            LOG.trace( "Nothing to format. Returning the value without any change." );
            return amount;
        }
    }
}
