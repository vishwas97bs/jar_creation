package preprocessors.interf;

import preprocessors.ExtractionData;

public interface TextPreprocessor {
    public String preProcessText(String input, ExtractionData helper, String fieldName );

}
