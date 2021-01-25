package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.List;


//Removes the number keywords such as No,Nº , N°, which couldn't have been ignored in regexes.
//EX: in  F001 N° 123456 , we can't ignore N° and capture F001 123456 using regex.
public class RemoveNumberKeywordsFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( RemoveNumberKeywordsFilter.class );
    private static final String NUMBER_KEYWORD_REGEX = "(n[ .]{0,3}[ogº°er\"])";


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        LOG.trace( "Before Removing number keywords: {}", input );

        for ( ExtractedValue extractedValue : input ) {
            String value = extractedValue.getValue().toString();
            value = value.replaceAll( NUMBER_KEYWORD_REGEX, "" );
            extractedValue.setValue( value );
        }


        LOG.trace( "After Removing number keywords: {}", input );
        return input;
    }

}
