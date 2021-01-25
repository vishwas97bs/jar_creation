package util.comparatorutil;

import filters.ExtractedValue;

import java.util.Comparator;

public class ConfidenceValueComparator implements Comparator<ExtractedValue>
{

    @Override
    public int compare( ExtractedValue ev1, ExtractedValue ev2 )
    {
        if ( ev1.getConfidence() < ev2.getConfidence() )
            return 1;
        if ( ev1.getConfidence() > ev2.getConfidence() )
            return -1;
        return 0;
    }

}
