package filters.implemenatation;


import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


//When there are multiple currencies based on keyword and language based, keep only those which match with locale.
public class KeywordAndLanguageLocaleFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( KeywordAndLanguageLocaleFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        LOG.trace( "Before filtering: {}", input );
        String localeValue = "";

        if ( helper.getScanRequest().getLocaleEntity() != null
            && helper.getScanRequest().getLocaleEntity().get( "localeValue" ) != null ) {
            localeValue = helper.getScanRequest().getLocaleEntity().get( "localeValue" ).toString();
        }

        String[] langCountry = localeValue.split( "[ _]" );
        if ( langCountry.length != 2 ) {
            LOG.info( "Locale: {} is not good enough for currency. returning same input..", localeValue );
            return input;
        }

        List<ExtractedValue> keywordBased = input.stream()
            .filter( ev -> ev.getOperation().equals( FieldMatch.LANGUAGE_WORDS_MATCH.toString() ) )
            .collect( Collectors.toList() );

        List<ExtractedValue> outputList = new ArrayList<>( input );

        outputList.removeAll( keywordBased );
        boolean localeValidated = false;

        Currency localeCurrency = Currency.getInstance( new Locale( langCountry[0], langCountry[1] ) );

        for ( ExtractedValue extractedValue : keywordBased ) {
            if ( extractedValue.getValue().toString().equalsIgnoreCase( localeCurrency.toString() ) ) {
                outputList.add( extractedValue );
                extractedValue.setConfidence( 0.71d );
                localeValidated = true;
            }
        }
        if ( !localeValidated ) {
            //If no currency found for identified locale, keep all the language based matches.
            outputList.addAll( keywordBased );
        }

        LOG.trace( "After filtering: {}", outputList );
        //if the results is empty., return input.
        return outputList;
    }

}
