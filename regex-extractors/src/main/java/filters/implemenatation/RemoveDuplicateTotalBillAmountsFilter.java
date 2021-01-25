package filters.implemenatation;


import data.FieldMatch;
import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import preprocessors.ExtractionData;

import java.util.ArrayList;
import java.util.List;

public class RemoveDuplicateTotalBillAmountsFilter implements ExtractionFilter {
    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper ) {
        List<ExtractedValue> inputList = new ArrayList<>();
        for (ExtractedValue value : input) {
            if (value.getOperation().equals(FieldMatch.REG_WITHOUT_VIC.toString()) || value.getOperation().equals(FieldMatch.REG_EX_MATCH.toString()))
                inputList.add(value);
        }
        if (input.size() == inputList.size()) {

            for (int index1 = 0; index1 < input.size(); index1++) {
                for (int index2 = index1 + 1; index2 < input.size(); index2++) {
                    if (input.get(index1).getValue().equals(input.get(index2).getValue())){
                        if (input.get(index1).getOperation().equals(FieldMatch.REG_EX_MATCH.toString()) )
                            input.remove(input.get(index1));
                        else
                            input.remove(input.get(index2));
                        index2--;
                    }
                }
            }


            return input;
        }
        return input;
    }
}
