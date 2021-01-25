package extractor.canidateinterface;

import filters.ExtractedValue;
import preprocessors.ExtractionData;

import java.util.List;

public interface CandidateValueExtractor {
    public List<ExtractedValue> extractValue(ExtractionData helper, String fieldName );
}
