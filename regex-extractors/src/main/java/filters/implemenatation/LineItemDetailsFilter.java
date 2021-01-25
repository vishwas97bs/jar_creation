package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;


public class LineItemDetailsFilter implements ExtractionFilter
{

    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        List<ExtractedValue> outputList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue line : input ) {
                if ( sb.length() != 0 )
                    sb.append( "\n" );
                sb.append( line.getValue() );
            }
        }
        if ( sb.length() != 0 ) {
            ExtractedValue finalValue = new ExtractedValue();
            finalValue.setValue( sb.toString() );
            outputList.add( finalValue );
        }
        return outputList;
    }

}
