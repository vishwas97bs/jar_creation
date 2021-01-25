package util;

import common.util.PatternExtractor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

public class GimletUtil {
    private static final Logger LOG = LoggerFactory.getLogger( GimletUtil.class );

    public static final String CURRENCY_SYMBOL = "currencySymbol";
    public static final String CURRENCY_VALUE = "currencyValue";


    private GimletUtil()
    {
    }


    public static String convertNumericSupportedValues( String text )
    {
        String response = text;
        if ( !StringUtils.isEmpty( response ) ) {
            response = response.toLowerCase();
            response = response.replaceAll( "[!li]", "1" );
            response = response.replaceAll( "[ocäüöau]", "0" );
            response = response.replaceAll( "[b]", "6" );
            response = response.replaceAll( "[q]", "9" );
            response = response.replaceAll( "[s]", "8" );
        }
        return response;
    }


    public static boolean ifContainsFewNumericVal( String inputText )
    {
        LOG.debug( "checking if extracted data is numeric atleast" );
        String pattVal = "\\d{3,}";
        boolean matchFound = false;
        try {
            PatternExtractor pRegex = new PatternExtractor( pattVal );
            if ( pRegex.isMatchedPatterns( inputText ) ) {
                matchFound = true;
            }
        } catch ( Exception e ) {
            LOG.error( "ERRor while matching pattern in containsAtleastAnumericVal", e );
        }
        return matchFound;
    }


    /**
     * Convert string amt to decimal.
     *
     * @param amount the amount
     * @return the big decimal
     */
    public static BigDecimal convertStringAmtToDecimal(String amount )
    {
        BigDecimal amountToReturn = BigDecimal.ZERO;
        if ( StringUtils.isEmpty( amount ) ) {
            return amountToReturn;
        }
        try {
            amountToReturn = new BigDecimal( amount );
        } catch ( NumberFormatException e ) {
            LOG.error( "error in method convertStringAmtToDecimal: for amount {} {}", amount, e );
        }
        return amountToReturn;
    }


    public static String convertStringAmtToDecimalSupport( String amount )
    {
        String response = amount;
        response = changeAmtToDecimalParsable( response ); /* TODO fixed temporarily : need to verify */
        BigDecimal amountToReturn = BigDecimal.ZERO;

        if ( StringUtils.isEmpty( response ) ) {
            return amountToReturn.toPlainString();
        }
        try {
            response = response.replaceAll( "[ocäüöau]", "0" );
            response = response.replaceAll( "[l!i]", "1" );
            response = response.replaceAll( "[/]", "7" );
            response = response.replaceAll( "[b]", "6" );
            response = response.replaceAll( "[q]", "9" );
            response = response.replaceAll( "[,\\s]+", "." );
            response = response.replaceAll( "[\\.]+", "." );
            if ( response.endsWith( "." ) ) {
                response = response.substring( 0, response.length() - 1 );
            }
            amountToReturn = new BigDecimal( response );
        } catch ( NumberFormatException e ) {
            LOG.error( "Error while converting String Amount To Decimal for amount {}", response, e );
        }

        return amountToReturn.toPlainString();
    }


    /**
     * Change amt to decimal parsable.
     *
     * @param amount
     *            the amount
     * @return the string
     */
    public static String changeAmtToDecimalParsable( String amount )
    {
        String strAmount = amount;
        try {
            if ( strAmount.length() > 3 ) {
                char sep = strAmount.charAt( strAmount.length() - 3 );

                if ( !Character.isDigit( sep ) && strAmount.substring( strAmount.lastIndexOf( sep ) ).length() <= 3 ) {
                    StringBuilder sbAmount = new StringBuilder( strAmount );
                    sbAmount.setCharAt( strAmount.length() - 3, '#' );
                    strAmount = sbAmount.toString();
                    strAmount = strAmount.replaceAll( "[\\.,; ]+", "" );
                    /* TODO: how to remove other separator */
                    strAmount = strAmount.replaceAll( "#", "." );
                    if ( StringUtils.countMatches( strAmount, "," ) == 1 ) {
                        strAmount = strAmount.replace( ",", "." );
                    }
                } else {
                    strAmount = strAmount.replace( ";", "" );
                    strAmount = strAmount.replace( ",", "." );
                }
            }
        } catch ( Exception e ) {
            LOG.error( "error in method convertStringAmtToDecimal: ", e );
        }
        if ( StringUtils.isEmpty( strAmount ) ) {
            return "0.00";
        }
        return strAmount;
    }


//    @SuppressWarnings ( "unchecked")
//    public static MLConfig getMLConfigForField( ExtractionData helper, String fieldName )
//    {
//        MLConfig mlConfigForField = null;
//        if ( null != helper && null != helper.getApiKeyConfiguration()
//                && null != helper.getApiKeyConfiguration().get( "configuration" ) ) {
//            Map<String, Object> configuration = (Map<String, Object>) helper.getApiKeyConfiguration().get( "configuration" );
//
//            if ( null != configuration.get( "apiConfig" ) ) {
//                Map<String, Object> apiConfig = (Map<String, Object>) configuration.get( "apiConfig" );
//                LOG.info( "APIConfig for ML approach {}", apiConfig.toString() );
//                Map<String, Object> mlConfig = null;
//
//                if ( null != apiConfig.get( "mlConfig" ) ) {
//                    mlConfig = (Map<String, Object>) apiConfig.get( "mlConfig" );
//
//                    //IC-1787, below condition is added to get the merchantName ML configuration for possibleMerchantName
//                    if ( fieldName != null && fieldName.equalsIgnoreCase( "possibleMerchantName" ) ) {
//                        fieldName = "merchantname";
//                    }
//
//                    if ( mlConfig.get( fieldName ) != null ) {
//                        mlConfigForField = new ObjectMapper().convertValue( mlConfig.get( fieldName ), MLConfig.class );
//                    }
//                }
//            }
//        }
//        return mlConfigForField;
//    }
}
