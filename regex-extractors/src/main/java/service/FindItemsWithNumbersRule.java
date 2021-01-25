package service;

import data.LineItemResult;
import data.Rule;

import java.util.ArrayList;
import java.util.List;

public class FindItemsWithNumbersRule extends Rule {
    @Override
    public List<LineItemResult> apply(List<String> lines )
    {
        return new ArrayList<>();
    }
}
