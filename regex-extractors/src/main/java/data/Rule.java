package data;

import java.util.List;

abstract public class Rule {
    abstract public List<LineItemResult> apply(List<String> lines );

}
