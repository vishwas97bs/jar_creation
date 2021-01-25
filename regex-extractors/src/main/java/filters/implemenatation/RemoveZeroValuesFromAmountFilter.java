package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import preprocessors.ExtractionData;

import java.util.Iterator;
import java.util.List;

public class RemoveZeroValuesFromAmountFilter implements ExtractionFilter {    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {
        if ( input != null && !input.isEmpty() ) {
            Iterator itr = input.iterator();
            while (itr.hasNext()) {
                ExtractedValue extVal = (ExtractedValue) itr.next();
                if (Double.compare(Double.parseDouble(extVal.getValue().toString()), 0) == 0) {
                    itr.remove();
                }
            }
        }
        return input;
    }
}
