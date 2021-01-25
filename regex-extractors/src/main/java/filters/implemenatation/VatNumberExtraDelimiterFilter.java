package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;


/**
 * Filter class to remove extra delimiters in vat numbers
 * @author Sharanya
 *
 */
public class VatNumberExtraDelimiterFilter implements ExtractionFilter
{
    /**
     * Removes extra delimiters from vat numbers, if any.
     * @param input List of extracted vat numbers
     * @param helper
     * @return List of filtered vat numbers
     */
    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        List<ExtractedValue> outputList = new ArrayList<>();
        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue value : input ) {
                String modifiedValue = (String) value.getValue();
                /*Replace two spaces with one*/
                modifiedValue = modifiedValue.replaceAll( "[\\s]{2}", " " );
                /*Replace space preceded or followed by '.'and two '.' with '.'*/
                modifiedValue = modifiedValue.replaceAll( "(\\s\\.)|(\\.\\s)|(\\.\\.)", "." );
                value.setValue( modifiedValue );
            }
            outputList = input;
        }
        return outputList;
    }

}
