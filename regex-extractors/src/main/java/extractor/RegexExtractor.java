package extractor;



import common.Utils;
import common.util.PatternExtractor;
import common.util.RegexMatchInfo;
import constants.confidenceconstants.ConfidenceValueCollection;
import constants.Constants;
import data.FieldMatch;
import extractor.canidateinterface.CandidateValueExtractor;
import filters.ExtractedValue;
import helper.gimletcore.helper.PrioritizeVicinity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;

import java.util.*;
import java.util.regex.PatternSyntaxException;

public class RegexExtractor implements CandidateValueExtractor {

    private static final Logger LOG = LoggerFactory.getLogger( RegexExtractor.class );
    private static final String NEGATIVE_VICINITY = "NEGVIC";


    /**
     * Extracts candidate values for a field
     * @param helper Extraction helper containing OcrText and configuration
     * @param fieldName Name of the field for which candidate values are to be extracted
     * @return List of candidate values for the field
     */
    @Override
    public List<ExtractedValue> extractValue(ExtractionData helper, String fieldName )
    {
        List<ExtractedValue> patternMatches = new ArrayList<>();
        LOG.trace( "Entering extract Method of Regex extraction with fieldName: {}", fieldName );

        if ( StringUtils.isEmpty( helper.getOcrText() ) ) {
            LOG.error( "InputText can't be null or empty." );
            return patternMatches;
        }
        if ( StringUtils.isEmpty( fieldName ) ) {
            LOG.error( "ExtractKey can't be null or empty." );
            return patternMatches;
        }

        String inputText = helper.getOcrText();

        List<String> vicFoundPositive = new ArrayList<>();
        boolean hasNegativeVicinity = false;

        List<String> regexList = helper.getRegexList( fieldName );
        if ( regexList == null || regexList.isEmpty() ) {
            return patternMatches;
        }
        List<String> vicinityList = ( helper.getVicinityList( fieldName ) == null ) ? ( new ArrayList<String>() )
                : helper.getVicinityList( fieldName );
        regexList = replaceMonthsInRegexes( helper, regexList );
        regexList = replaceCurrencyInRegexes( helper, regexList );

        Map<String, Double> vicinityPriorityMap = PrioritizeVicinity.setPriorities( ConfidenceValueCollection.REGEX_EXTRACTOR,
                vicinityList );
        // Loop through vicinity words and add matching words to a list
        for ( String vicinity : vicinityList ) {
            if ( vicinity.charAt( 0 ) == '!' ) { // this is negative word
                vicinity = vicinity.substring( 1 );
                if ( inputText.contains( vicinity ) ) {
                    LOG.trace( "Negative vicinity word matched: '{}' for extractFIELD: {}", vicinity, fieldName );
                    inputText = inputText.replace( vicinity, NEGATIVE_VICINITY );
                    hasNegativeVicinity = true;
                }
            } else {
                if ( matchPattern( vicinity, inputText ).size() > 0 ) {
                    vicFoundPositive.add( vicinity );
                    LOG.trace( "Positive vicinity word matched: '{}' for extractFIELD: {}", vicinity, fieldName );
                }
            }
        }
        Set<ExtractedValue> patternMatchInfo = extractUsingRegexes( inputText, vicFoundPositive, hasNegativeVicinity,
                regexList );

        patternMatches.addAll( patternMatchInfo );
        LOG.trace( "Pattern matches in RegExExtractor: {}", patternMatches );
        for ( ExtractedValue patternMatch : patternMatches ) {
            if ( patternMatch.getOperation().equals( FieldMatch.VICINITY_MATCH.toString() ) )
                patternMatch.setConfidence( vicinityPriorityMap.get( patternMatch.getMatchedVicinity() ) );

        }

        removeNonStrictMatches( patternMatches, fieldName, helper );

//        get language from apikeyconfig
        ArrayList<String> lang =null ;
        Map<String,Object> config = helper.getApiKeyConfiguration() != null ? (Map<String,Object>)helper.getApiKeyConfiguration().get("configuration") : null;
        if( config!=null && config.get("apiConfig")!= null) {
            Map<String, Object> apiConfig = (Map<String, Object>) config.get("apiConfig");
            if (apiConfig.get("languages")!=null) {
                lang = (ArrayList<String>) apiConfig.get("languages");
            }
        }

//        only for korean billing date
        if (lang!=null && !lang.isEmpty() && fieldName.equalsIgnoreCase("billingdate_fallback") && lang.contains("ko")) {
            for (ExtractedValue date : patternMatches) {
                if (date.getValue() != null && date.getValue() != null) {
                    String dateValue = (String) date.getValue();
                    if (dateValue.contains("년") && dateValue.contains("월") && dateValue.contains("일")) {
                        dateValue = dateValue.replaceAll("[년월일]", ".");
                        date.setValue(dateValue);
                    }
                }
            }
        }

        return patternMatches;

    }


    /**
     * Replaces placeholder for months in regex with the actual month names
     * @param helper Extraction helper having configuration
     * @param regexList List of regexes where month placeholder is to be replaced
     * @return List of updated regexes
     */
    private List<String> replaceMonthsInRegexes( ExtractionData helper, List<String> regexList )
    {
        String monthsRegex = helper.getMonthsRegex();

        List<String> tempRegexList = new ArrayList<>();
        for ( String regex : regexList ) {
            while ( regex.contains( Constants.REGEX_POS_MONTHS ) ) {
                regex = regex.replace( Constants.REGEX_POS_MONTHS, monthsRegex );
            }
            tempRegexList.add( regex );
        }
        return tempRegexList;
    }


    /**
     * Replaces placeholder for currency in regex with the currency values
     * @param helper Extraction helper having configuration
     * @param regexList List of regexes where currency placeholder is to be replaced
     * @return List of updated regexes
     */
    private List<String> replaceCurrencyInRegexes( ExtractionData helper, List<String> regexList )
    {
        String currenciesRegex = helper.getCurrenciesRegex();

        List<String> tempRegexList = new ArrayList<>();
        List<String> finalRegexList = new ArrayList<>();
        for ( String regex : regexList ) {
            if ( regex.contains( Constants.REGEX_POS_CURRENCIES ) && !StringUtils.isEmpty( currenciesRegex ) ) {
                while ( regex.contains( Constants.REGEX_POS_CURRENCIES ) ) {
                    regex = regex.replace( Constants.REGEX_POS_CURRENCIES, currenciesRegex );
                }
                tempRegexList.add( regex );
            } else {
                finalRegexList.add( regex );
            }
        }
        finalRegexList.addAll( tempRegexList );
        return finalRegexList;
    }


    /**
     * Does extraction of candidate values using regexes
     * @param inputText OcrText
     * @param vicFoundPositive List of vicinity words
     * @param hasNegativeVicinity Boolean value indicating whether there are negative vicinity words in text
     * @param regexList List of regexes
     * @return A set of extracted values
     */
    private Set<ExtractedValue> extractUsingRegexes( String inputText, List<String> vicFoundPositive,
                                                     boolean hasNegativeVicinity, List<String> regexList )
    {
        // Loop through regex patterns combined with the vicinity word
        Set<ExtractedValue> patternMatchInfoList = new HashSet<>();

        for ( String patt : regexList ) {
            if ( patt.contains( Constants.REGEX_POS_VICINITY ) ) {
                if ( !vicFoundPositive.isEmpty() ) {
                    for ( String vic : vicFoundPositive ) {
                        // combining vicinity with pattern to make a new pattern
                        String vicPatt = patt.replace( Constants.REGEX_POS_VICINITY, vic );
                        LOG.debug( "Regex pattern for positive vicinity match:'{}'", vicPatt );
                        Set<ExtractedValue> values = findMatches( inputText, vicPatt, vic, FieldMatch.VICINITY_MATCH.toString(),
                                ConfidenceValueCollection.REGEX_EXTRACTOR );
                        //patternMatchInfoList.addAll(values);
                        keywordInAnotherWord( patternMatchInfoList, values );
                        patternMatchInfoList.addAll( values );
                    }
                }
            } else {
                LOG.debug( "Regex pattern without vicinity:'{}' ", patt );
                patternMatchInfoList.addAll( findMatches( inputText, patt, "", FieldMatch.REG_WITHOUT_VIC.toString(), 0.6 ) );
            }
        }


        patternMatchInfoList.addAll( findAllRegexMatches( inputText, regexList ) );
        //Remove values preceded with negative vicinity words
        if ( hasNegativeVicinity && !patternMatchInfoList.isEmpty() ) {
            removeNegativeVicinityMatches( inputText, patternMatchInfoList, regexList );
        }
        return patternMatchInfoList;
    }


    /**
     * Finds regex matches and creates a set containing extracted values
     * @param inputText OcrText
     * @param pattern Pattern that is to be matched
     * @param vic Vicinity word present in the pattern
     * @param matchType Type of match
     * @param confidence Score given to the match
     * @return Set of extracted values
     */
    private Set<ExtractedValue> findMatches( String inputText, String pattern, String vic, String matchType, Double confidence )
    {
        Set<ExtractedValue> patternMatchInfo = new HashSet<>();
        Map<Integer, String> matchedWordList = matchPattern( pattern.trim(), inputText );
        LOG.trace( "MatchedWordList: {} ", matchedWordList );
        if ( !matchedWordList.isEmpty() ) {
            for ( Map.Entry<Integer, String> entry : matchedWordList.entrySet() ) {
                ExtractedValue extractedValue = new ExtractedValue( matchedWordList.get( entry.getKey() ), matchType, vic,
                        entry.getKey(), confidence );
                LOG.trace( "FieldDetails with vicinity: {}", extractedValue );
                patternMatchInfo.add( extractedValue );
            }
        }
        return patternMatchInfo;
    }


    /**
     * Find all matches for all the regexes from the OcrText
     * @param inputText OcrText
     * @param regexList List of regexes to be matched
     * @return Set of extracted values
     */
    private Set<ExtractedValue> findAllRegexMatches( String inputText, List<String> regexList )
    {
        Set<ExtractedValue> patternMatchInfo = new HashSet<>();
        for ( String patt : regexList ) {
            for( String regex_pos_lookAhead : Constants.REGEX_POS_LOOKAHEAD_LIST) {
                if (patt.contains(regex_pos_lookAhead)) {
                    String mpatt = patt.replace(regex_pos_lookAhead, "");
                    Map<Integer, String> matchedWordList = matchPattern(mpatt, inputText);
                    if (!matchedWordList.isEmpty()) {
                        for (Map.Entry<Integer, String> entry : matchedWordList.entrySet()) {
                            ExtractedValue extractedValue = new ExtractedValue(matchedWordList.get(entry.getKey()),
                                    FieldMatch.REG_EX_MATCH.toString(), "", entry.getKey(), 0.4);
                            patternMatchInfo.add(extractedValue);
                        }
                    }
                }
            }
        }
        return patternMatchInfo;
    }


    /**
     * Removes values which are preceded with negative vicinity words from the set of extracted values
     * @param inputText OcrText
     * @param patternMatchInfo Set of extracted values
     * @param regexList List of regexes used to find negative vicinity matched values
     * @return Updated set of extracted values
     */
    private Set<ExtractedValue> removeNegativeVicinityMatches( String inputText, Set<ExtractedValue> patternMatchInfo,
                                                               List<String> regexList )
    {
        String vicPatt;
        for ( String patt : regexList ) {

            if ( patt.contains( "{0}" ) ) {
                vicPatt = patt.replace( "{0}", NEGATIVE_VICINITY );
                Map<Integer, String> matchedWordList = matchPattern( vicPatt, inputText );
                if ( !matchedWordList.isEmpty() ) {
                    for ( Map.Entry<Integer, String> entry : matchedWordList.entrySet() ) {
                        Iterator<ExtractedValue> patternMatchesIterator = patternMatchInfo.iterator();
                        while ( patternMatchesIterator.hasNext() ) {
                            ExtractedValue value = patternMatchesIterator.next();
                            if ( value.getIndex() == entry.getKey() ) {
                                patternMatchesIterator.remove();
                            }
                        }
                    }
                }
            }
        }
        return patternMatchInfo;
    }


    /**
     * Finds matches of pattern in the OcrText
     * @param pattVal Pattern to be matched
     * @param inputText Ocr text
     * @return Map containing matched values in the form (Index in text, Matched value)
     */
    private Map<Integer, String> matchPattern( String pattVal, String inputText )
    {
        String matchFound = null;
        Map<Integer, String> patternList = new LinkedHashMap<>();
        try {

            PatternExtractor patternExtractor = new PatternExtractor( pattVal );
            List<RegexMatchInfo> regexMatchInfoList = patternExtractor.matchedPatterns( inputText );
            for ( RegexMatchInfo regexMatchInfo : regexMatchInfoList ) {
                matchFound = regexMatchInfo.getMatchedString();
                int index = regexMatchInfo.getStartindex();
                patternList.put( index, matchFound.trim() );
                LOG.info( "Found ({}) using pattern {} at index {}", matchFound, pattVal, index );
            }
        } catch ( PatternSyntaxException | IllegalStateException | IndexOutOfBoundsException e ) {
            LOG.error( "Exception occured while matching pattern: ", e );
        }
        return patternList;
    }


    /**
     * Method to find if the keyword is a part of another keyword and replaces the extracted value with the complete word
     * Ex: If total is present in the patternMatchInfoList and net total is present in the values total is replaced with net total
     * @param patternMatchInfoList
     * @param values
     */

    private void keywordInAnotherWord( Set<ExtractedValue> patternMatchInfoList, Set<ExtractedValue> values )
    {

        for ( ExtractedValue extractedValue : patternMatchInfoList ) {

            for ( ExtractedValue extractedValue1 : values ) {
                if ( extractedValue1.getIndex() == extractedValue.getIndex()
                        && extractedValue1.getOperation().equals( extractedValue.getOperation() )
                        && Objects.equals( extractedValue1.getValue(), extractedValue.getValue() )
                        && extractedValue1.getMatchedVicinity().contains( extractedValue.getMatchedVicinity() ) ) {
                    System.out.println( "entered duplicated keyword condition" );
                    extractedValue.setMatchedVicinity( extractedValue1.getMatchedVicinity() );

                }


            }
        }

    }


    private void removeNonStrictMatches( List<ExtractedValue> patternMatches, String fieldName, ExtractionData helperData )
    {
        LOG.debug( "Entering removeNonStrictMatches method" );
        List<ExtractedValue> output = new ArrayList<>( patternMatches );
        List<String> nonStrictVicinityList = helperData.getNonStrictVicinityList( fieldName );
        if ( nonStrictVicinityList != null && !nonStrictVicinityList.isEmpty() ) {
            String regex = "(^(\\s{0,5}))";
            String text = helperData.getOcrText();
            List<String> stringLines = Utils.getStringLines( text );
            for ( ExtractedValue value : output ) {
                String matchedValue = value.getMatchedVicinity();
                if ( matchedValue != null && nonStrictVicinityList.contains( matchedValue ) ) {
                    boolean found = false;
                    for ( String line : stringLines ) {
                        PatternExtractor extractor = new PatternExtractor( regex + matchedValue );
                        List<RegexMatchInfo> matchedPatterns = extractor.matchedPatterns( line );
                        if ( line.contains( value.getValue().toString() ) ) {
                            if ( matchedPatterns == null || matchedPatterns.isEmpty() ) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if ( found ) {
                        patternMatches.remove( value );

                    }
                }
            }
        }
        LOG.debug( "Finished removeNonStrictMatches method" );
    }

}
