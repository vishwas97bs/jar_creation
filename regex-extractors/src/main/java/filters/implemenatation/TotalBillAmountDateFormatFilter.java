package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;

public class TotalBillAmountDateFormatFilter implements ExtractionFilter {

	private static final Logger LOG = LoggerFactory.getLogger( TotalBillAmountDateFormatFilter.class );

	private  final String DateFormatRegex_DD_MM_YY = "(3[01]|[12][0-9]|0[1-9])[./-](1[0-2]|0[1-9])[./-](?:[0-9][0-9])?[0-9][0-9]";

	private  final String DateFormatRegex_MM_DD_YY = "(1[0-2]|0[1-9])[./-](3[01]|[12][0-9]|0[1-9])[./-](?:[0-9][0-9])?[0-9][0-9]";


	@Override
	public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
	{
		List<ExtractedValue> outputList = new ArrayList<>();

		if ( input != null && !input.isEmpty() ) {
			for ( ExtractedValue extVal : input ) {
				String value = ( (String) extVal.getValue() ).trim();
				if(!isDateformatAmount( value ) ) {
					extVal.setValue(value);
					outputList.add(extVal);
				}
			}
		}

		LOG.trace( "List after filteration: {}", outputList );
		return outputList;
	}


	private boolean isDateformatAmount( String amountValue)
	{
		if(StringUtils.isNotEmpty(amountValue)) {
			if(amountValue.length() >= 8) {
				amountValue = amountValue.trim().substring(0, 8);
				if(amountValue.matches(DateFormatRegex_DD_MM_YY) || amountValue.matches(DateFormatRegex_MM_DD_YY))
					return true;
			}

		}
		return false;
	}
}
