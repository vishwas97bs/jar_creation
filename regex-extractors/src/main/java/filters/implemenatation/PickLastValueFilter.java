package filters.implemenatation;

import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;

public class PickLastValueFilter implements ExtractionFilter {
	private static final Logger LOG = LoggerFactory.getLogger(PickLastValueFilter.class);

	@Override
	public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper) {
		List<ExtractedValue> outputList = new ArrayList<>();
		List<ExtractedValue> tempList = new ArrayList<>();
		if (input != null && !input.isEmpty()) {
			for (ExtractedValue extVal : input) {
				if (extVal.getOperation().equals(FieldMatch.VICINITY_MATCH.toString()))
					tempList.add(extVal);
			}
		}

		long index = Integer.MIN_VALUE;
		for (ExtractedValue extractedValue : tempList) {
			if (index < extractedValue.getIndex()) {
				index = extractedValue.getIndex();
				outputList.clear();
				outputList.add(extractedValue);
			}
		}
		LOG.trace("List after filteration: " + outputList);
		return outputList;
	}

}
