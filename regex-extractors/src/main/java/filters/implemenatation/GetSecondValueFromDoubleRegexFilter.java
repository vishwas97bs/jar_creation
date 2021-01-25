package filters.implemenatation;


import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;


public class GetSecondValueFromDoubleRegexFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( GetSecondValueFromDoubleRegexFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        LOG.debug( "Entering Get Second Value From Double Regex Filter" );
        List<ExtractedValue> outputList = new ArrayList<>();
        if ( input != null && !input.isEmpty() ) {
            List<String> doubleRegexVicinity = helper.getValueList( "double_regex_vicinity" );
            for ( ExtractedValue value : input ) {
                if ( value.getOperation().equals( FieldMatch.DOUBLE_REGEX_MATCH.toString() ) ) {
                    String stringValue = value.getValue().toString();
                    for ( String doubleRegexVici : doubleRegexVicinity ) {
                        if ( doubleRegexVici.length() == 1 && StringUtils.isAlpha( doubleRegexVici ) ) {
                            doubleRegexVici = " " + doubleRegexVici + " ";
                        }
                        if ( stringValue.contains( doubleRegexVici ) ) {
                            String[] split = stringValue.split( doubleRegexVici );
                            value.setValue( split[split.length - 1].trim() );
                            outputList.add( value );
                        }
                    }
                }
            }
        }

        if ( outputList.isEmpty() ) {
            outputList = input;
        }
        LOG.debug( "Completed Get Second Value From Double Regex Filter" );
        return outputList;
    }

}
