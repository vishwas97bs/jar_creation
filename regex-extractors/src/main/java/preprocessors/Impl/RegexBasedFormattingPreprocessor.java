package preprocessors.Impl;


import common.util.PatternExtractor;
import common.util.RegexMatchInfo;
import constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.PatternSyntaxException;


/**
 * Preprocessor to format text using regex's to correct distorted text
 * @author Sharanya
 */
public class RegexBasedFormattingPreprocessor implements TextPreprocessor
{
    private static final Logger LOG = LoggerFactory.getLogger( RegexBasedFormattingPreprocessor.class );
    private static final String FORMAT_EXTENSION = "_format";


    /**
     * Finds parts which have a similar pattern as the field and replaces the distorted section in the matched part with it's correct form
     * @param input Text to be processed
     * @param helper Extraction helper containing configuration details such as format regex's, format vicinity words and format maps
     * @param fieldName Field for which the formatting is to be performed
     */
    @Override public String preProcessText(String input, ExtractionData helper, String fieldName )
    {
        LOG.trace( "Entering preProcessText Method of Regex extraction with fieldName: {}", fieldName );

        if ( StringUtils.isEmpty( helper.getOcrText() ) ) {
            LOG.warn( "InputText can't be null or empty." );
            return input;
        }
        if ( StringUtils.isEmpty( fieldName ) ) {
            LOG.warn( "ExtractKey can't be null or empty." );
            return input;
        }

        String inputText = input;

        List<String> regexList = helper.getRegexList( fieldName + FORMAT_EXTENSION );
        if ( regexList == null || regexList.isEmpty() ) {
            LOG.warn( "Regex list can't be null or empty" );
            return input;
        }
        regexList = replaceMonths( helper, regexList );
        regexList = replaceCurrencies( helper, regexList );

        List<String> vicinityList = ( helper.getVicinityList( fieldName + FORMAT_EXTENSION ) == null ) ?
            ( new ArrayList<String>() ) :
            helper.getVicinityList( fieldName + FORMAT_EXTENSION );

        // Get related conversions for the matched values
        Map<String, String> correctionMap = helper.getFormatCorrectionMap( fieldName );

        for ( String pattern : regexList ) {
            if ( pattern.contains( Constants.REGEX_POS_VICINITY ) ) {
                for ( String vicinityWord : vicinityList ) {
                    String patternToBeMatched = pattern.replace( Constants.REGEX_POS_VICINITY, vicinityWord );
                    inputText = matchAndReplace( inputText, patternToBeMatched, correctionMap, helper );
                }
            } else {
                inputText = matchAndReplace( inputText, pattern, correctionMap, helper );
            }
        }
        return inputText;
    }


    /**
     * Replaces placeholder for months in regex with the actual month names
     * @param helper Extraction helper having configuration
     * @param regexList List of regex's where month placeholder is to be replaced
     * @return List of updated regex's
     */
    private List<String> replaceMonths( ExtractionData helper, List<String> regexList )
    {
        List<String> tempRegexList = new ArrayList<>();
        for ( String regex : regexList ) {
            regex = replaceMonthsInRegex( helper, regex );
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
    private List<String> replaceCurrencies( ExtractionData helper, List<String> regexList )
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
     * Replaces placeholder for months in regex with the actual month names
     * @param helper Extraction helper having configuration
     * @param regex where month placeholder is to be replaced
     * @return Updated regex
     */
    private String replaceMonthsInRegex( ExtractionData helper, String regex )
    {
        String updatedRegex = regex;
        String monthsRegex = helper.getMonthsRegex();
        while ( updatedRegex.contains( Constants.REGEX_POS_MONTHS ) ) {
            updatedRegex = updatedRegex.replace( Constants.REGEX_POS_MONTHS, monthsRegex );
        }
        return updatedRegex;
    }


    /**
     * Finds matches to the pattern. If found, corrects the distorted part in the match and replaces the match in the text with it's correct form.
     * @param inputText Text in which the pattern is to be matched
     * @param pattern Pattern to be matched
     * @param correctionMap Map containing distortions and the corresponding corrections
     * @param helper Extraction helper having configuration
     * @return Text after correction if distortion is present. Otherwise, the text is returned unchanged
     */
    private String matchAndReplace( String inputText, String pattern, Map<String, String> correctionMap, ExtractionData helper )
    {
        String text = inputText;
        Map<Integer, String> matchedWordList = matchPattern( pattern.trim(), text );
        LOG.trace( "MatchedWordList: {} ", matchedWordList );
        for ( Entry<Integer, String> entry : matchedWordList.entrySet() ) {
            //Get matched part
            String target = entry.getValue();

            for ( Entry<String, String> mapping : correctionMap.entrySet() ) {

                //Fetch the distortion and the corresponding correction from the correction map
                String distortion = replaceMonthsInRegex( helper, mapping.getKey() );
                String correction = mapping.getValue();

                //Find the distorted section in the matched part
                PatternExtractor patternExtractor = new PatternExtractor( distortion );
                List<RegexMatchInfo> regexMatchInfoList = patternExtractor.matchedPatterns( target );
                if ( regexMatchInfoList.size() > 0 ) {
                    //Replace the distorted part with it's correct form in the matched value
                    String replacement = target.replaceAll( distortion, correction );
                    // put the corrected value back in the text
                    text = text.replace( target, replacement );
                    target = replacement;
                }
            }
        }
        return text;
    }


    /**
     * Finds matches of pattern in the OcrText
     * @param pattVal Pattern to be matched
     * @param inputText Ocr text
     * @return Map containing matched values in the form (Index in text, Matched value)
     */
    private Map<Integer, String> matchPattern( String pattVal, String inputText )
    {
        String matchFound;
        Map<Integer, String> patternList = new LinkedHashMap<>();
        try {
            PatternExtractor patternExtractor = new PatternExtractor( pattVal );
            List<RegexMatchInfo> regexMatchInfoList = patternExtractor.matchedPatterns( inputText );
            for ( RegexMatchInfo regexMatchInfo : regexMatchInfoList ) {
                matchFound = regexMatchInfo.getMatchedString();
                int index = regexMatchInfo.getStartindex();
                patternList.put( index, matchFound.trim() );
                LOG.trace( "Found ({}) using pattern {} at index {}", matchFound, pattVal, index );
            }
        } catch ( PatternSyntaxException | IllegalStateException | IndexOutOfBoundsException e ) {
            LOG.error( "Exception occurred while matching pattern: ", e );
        }
        return patternList;
    }
}