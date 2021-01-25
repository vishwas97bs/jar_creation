package filters.implemenatation;

import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.*;


/** Filters all the extractor responses and
 returns a List of filters.ExtractedValue
 whose first element holds the value
 containing list of max confidence extractor responses
 */
public class PossibleMerchantNameFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( PossibleMerchantNameFilter.class );


    @Override public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper )
    {
        LOG.trace( "Entering filter method" );
        if ( input == null || input.size() == 0 ) {
            LOG.warn( "Input list was found to be null" );
            return new ArrayList<>();
        }
        List<ExtractedValue> extractedValueList = new LinkedList<>();
        ExtractedValue possibleExtractedValue = new ExtractedValue();
        //add max confidence response for every extractor
        List<ExtractedValue> maxConfidenceExtractionList = getMaxConfidenceExtractionList( input );
        LOG.trace( "List after filteration: {}", maxConfidenceExtractionList );
        possibleExtractedValue.setValue( maxConfidenceExtractionList );
        possibleExtractedValue.setConfidence( input.get( 0 ).getConfidence() );
        extractedValueList.add( possibleExtractedValue );
        return extractedValueList;
    }


    private List<ExtractedValue> getMaxConfidenceExtractionList( List<ExtractedValue> input )
    {
        List<ExtractedValue> maxConfidenceExtractionList = new ArrayList<>();
        Iterator iterator = input.iterator();
        TreeMap<Double, ExtractedValue> existingMerchantNameTree = new TreeMap<>();
        TreeMap<Double, ExtractedValue> websiteDomainMerchantNameTree = new TreeMap<>();
        TreeMap<Double, ExtractedValue> merchantNameFromTopOfTextTree = new TreeMap<>();
        TreeMap<Double, ExtractedValue> merchantNamePrefixNSuffixBasedTree = new TreeMap<>();
        TreeMap<Double, ExtractedValue> merachantNameMLBasedTree = new TreeMap<>();
        TreeMap<Double, ExtractedValue> blockBasedTree = new TreeMap<>();
        TreeMap<Double, ExtractedValue> bestLookupBasedTree = new TreeMap<>();

        while ( iterator.hasNext() ) {
            ExtractedValue extractedValue = (ExtractedValue) iterator.next();
            //TODO: confirm on the switch
            switch ( extractedValue.getOperation() ) {
                case "GOOGLE_VISION_MERCHANT_NAME":
                    maxConfidenceExtractionList.add( extractedValue );
                    break;
                case "WEBSITE_DOMAIN_MERCHANT_NAME":
                    websiteDomainMerchantNameTree.put( extractedValue.getConfidence(), extractedValue );
                    break;
                case "POSSIBLE_MERCHANT_NAME_FROM_TOP":
                    merchantNameFromTopOfTextTree.put( extractedValue.getConfidence(), extractedValue );
                    break;
                case "BLOCK_BASED_MERCHANT_NAME":
                    blockBasedTree.put( extractedValue.getConfidence(), extractedValue );
                    break;
                case "EXISTING_MERCHANT_NAME":
                    existingMerchantNameTree.put( extractedValue.getConfidence(), extractedValue );
                    break;
                case "TAG_LINE":
                    merchantNamePrefixNSuffixBasedTree.put( extractedValue.getConfidence(), extractedValue );
                    break;
                case "TRAINING_BASED":
                    merachantNameMLBasedTree.put( extractedValue.getConfidence(), extractedValue );
                    break;
                case "BEST_GUESS_LOOKUP_BASED_MERCHANT_EXTRACTOR":
                    bestLookupBasedTree.put( extractedValue.getConfidence(), extractedValue );
                    break;

            }

        }
        if ( !existingMerchantNameTree.isEmpty() ) {
            maxConfidenceExtractionList.add( existingMerchantNameTree.get( existingMerchantNameTree.lastKey() ) );
        }
        if ( !merchantNameFromTopOfTextTree.isEmpty() ) {
            maxConfidenceExtractionList.add( merchantNameFromTopOfTextTree.get( merchantNameFromTopOfTextTree.lastKey() ) );
        }
        if ( !blockBasedTree.isEmpty() ) {
            maxConfidenceExtractionList.add( blockBasedTree.get( blockBasedTree.lastKey() ) );
        }
        if ( !websiteDomainMerchantNameTree.isEmpty() ) {
            maxConfidenceExtractionList.add( websiteDomainMerchantNameTree.get( websiteDomainMerchantNameTree.lastKey() ) );
        }
        if ( !merchantNamePrefixNSuffixBasedTree.isEmpty() ) {
            maxConfidenceExtractionList
                .add( merchantNamePrefixNSuffixBasedTree.get( merchantNamePrefixNSuffixBasedTree.lastKey() ) );
        }
        if ( !merachantNameMLBasedTree.isEmpty() ) {
            maxConfidenceExtractionList.add( merachantNameMLBasedTree.get( merachantNameMLBasedTree.lastKey() ) );
        }

        if ( !bestLookupBasedTree.isEmpty() ) {
            maxConfidenceExtractionList.add( bestLookupBasedTree.get( bestLookupBasedTree.lastKey() ) );
        }

        return maxConfidenceExtractionList;
    }
}

