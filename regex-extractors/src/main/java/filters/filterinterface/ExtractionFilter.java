package filters.filterinterface;

import filters.ExtractedValue;
import preprocessors.ExtractionData;

import java.util.List;

public interface ExtractionFilter {

    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper);

}
