package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PrioritizeCurrencyFilter implements ExtractionFilter
{
    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {

        List<ExtractedValue> currencyToremove = new ArrayList<>();
        List<ExtractedValue> unsortedCurrencies = new ArrayList<>( input );

        Collections.sort( input, ( s1, s2 ) -> {
            if ( s2.getValue().toString().length() == s1.getValue().toString().length() )
                return 0;

            return s2.getValue().toString().length() - s1.getValue().toString().length();
        } );
        if ( input.equals( unsortedCurrencies ) )
            return input;

        for ( int i = 0; i < input.size(); i++ ) {
            for ( int j = i + 1; j < input.size(); j++ ) {
                if ( input.get( i ).getValue().toString().contains( input.get( j ).getValue().toString() ) && !(input.get( i ).getValue().toString().equals( input.get( j ).getValue().toString() ))) {
                    currencyToremove.add( input.get( j ) );
                }

            }
        }
        input.removeAll( currencyToremove );


        return input;
    }

}
