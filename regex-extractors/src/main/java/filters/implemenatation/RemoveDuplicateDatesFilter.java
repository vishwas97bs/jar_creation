package filters.implemenatation;


import constants.Constants;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.apache.commons.lang3.StringUtils;
import preprocessors.ExtractionData;
import util.dateutil.DateUtil;

import java.util.*;
import java.util.Map.Entry;


/**
 * Filter to remove duplicate dates
 * @author Madhury,Sharanya
 *
 */
public class RemoveDuplicateDatesFilter implements ExtractionFilter
{
    private static final double BOOSTED_CONFIDENCE = 0.69;
    private static final List<String> chineseDateCharacters = Arrays.asList("年","月","日");

    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        List<ExtractedValue> outputList = new ArrayList<>();
        LinkedHashMap<String, ExtractedValue> isoToExtractedDateMap = new LinkedHashMap<>();

        Map<String, Integer> dateCount = new HashMap<>();
        for ( int index1 = 0; index1 < input.size(); index1++ ) {
            for ( int index2 = index1 + 1; index2 < input.size(); index2++ ) {
                if ( Math.abs( input.get( index1 ).getIndex() - input.get( index2 ).getIndex() ) <= 4 ) {
                    if ( input.get( index1 ).getIndex() > input.get( index2 ).getIndex() ) {
                        input.remove( input.get( index1 ) );
                    } else
                        input.remove( input.get( index2 ) );
                    index2--;
                }
            }
        }

        for ( Iterator iterator = input.iterator(); iterator.hasNext(); ) {
            ExtractedValue newDateMatch = (ExtractedValue) iterator.next();
            newDateMatch = filterChineseCharacters( newDateMatch );

            String isoDate = "";

            if( Constants.CUSTOM_LANGUAGE_DATE_REGEX.containsKey( helper.getLocale() )){
                isoDate = DateUtil.formatCustomLanguageDates( newDateMatch.getValue(), helper.getLocale(), "" );
                newDateMatch.setValue( isoDate );
            }

            if ( StringUtils.isBlank( isoDate ) ) {
                isoDate = DateUtil.formatDateToISO( newDateMatch.getValue().toString() );
            }
            if ( isoDate != null ) {
                if ( isoToExtractedDateMap.containsKey( isoDate ) ) {
                    /*If date is already extracted, retain the value with lower index*/
                    ExtractedValue existingDateMatch = isoToExtractedDateMap.get( isoDate );
                    if (newDateMatch.getConfidence() >= existingDateMatch.getConfidence() && existingDateMatch.getIndex() > newDateMatch.getIndex() ) {
                        isoToExtractedDateMap.put( isoDate, newDateMatch );
                    }
                    dateCount.put( isoDate, dateCount.get( isoDate ) + 1 );

                } else {
                    /*If new date, add it to the map*/
                    isoToExtractedDateMap.put( isoDate, newDateMatch );
                    dateCount.put( isoDate, 1 );
                }
            }
        }

        for ( Entry<String, Integer> entry : dateCount.entrySet() ) {
            if ( entry.getValue() >= 2 ) {
                isoToExtractedDateMap.get( entry.getKey() ).setConfidence( BOOSTED_CONFIDENCE);
            }

        }
        for ( Entry<String, ExtractedValue> entry : isoToExtractedDateMap.entrySet() ) {
            outputList.add( entry.getValue() );
        }
        return outputList;
    }

    private ExtractedValue filterChineseCharacters(ExtractedValue extractedValue) {
        boolean dateContainsChineseCharacters = false;
        if(extractedValue.getValue() != null) {
            for (String chineseCharacter : chineseDateCharacters) {
                if (extractedValue.getValue().toString().contains(chineseCharacter)) {
                    extractedValue.setValue(extractedValue.getValue().toString().replace(chineseCharacter, "-"));
                    dateContainsChineseCharacters = true;
                }
            }
            if (extractedValue.getValue() != null && dateContainsChineseCharacters) {
                extractedValue.setValue(extractedValue.getValue().toString().replaceAll(" ", ""));
                String date = extractedValue.getValue().toString();
                if ( date.charAt(date.length() - 1 ) == '-' ){
                    extractedValue.setValue(date.substring(0, date.length() -1));
                }
            }
        }
        return extractedValue;
    }
}
