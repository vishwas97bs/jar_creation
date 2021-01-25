package util;

import constants.merchantsconstant.MerchantNameExtractorType;
import filters.ExtractedValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class ExtractionOperationValidator {
    private ExtractionOperationValidator()
    {

    }


    public static boolean isValidMerchant( ExtractedValue ev )
    {
        List<String> validOperationList = Arrays.asList( MerchantNameExtractorType.EXISTING_MERCHANT );
        return isValid( ev, validOperationList );
    }


    private static boolean isValid(ExtractedValue ev, List<String> validOperationList )
    {
        if ( ( ev != null && StringUtils.isNotBlank( ev.getOperation() ) ) ) {
            for ( String operationName : validOperationList ) {
                if ( ev.getOperation().equals( operationName ) ) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean isValid( ExtractedValue ev, String operationName )
    {
        return ( ev != null && StringUtils.isNotBlank( ev.getOperation() ) ) && ev.getOperation().equals( operationName );
    }
}
