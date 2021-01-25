package preprocessors.Impl;



import constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import preprocessors.interf.TextPreprocessor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class BannedTextPreProcessor implements TextPreprocessor
{
    private static final Logger LOG = LoggerFactory.getLogger( BannedTextPreProcessor.class );


    @Override public String preProcessText(String ocrText, ExtractionData helper, String fieldName )
    {
        LOG.info( "From Banned Text Bolt" );

        Map<String, Object> bannedLanguagesMap = helper.getExtractionConfigurationMap( Constants.BANNED_LANGUAGES_REGEX );
        StringBuilder regex = new StringBuilder( "" );

        if ( helper != null && helper.getApiKeyConfiguration() != null && bannedLanguagesMap != null ) {
            bannedLanguagesMap = filterBannedLanguages( helper, bannedLanguagesMap );
        }

        //ToDo :can be changed to lambdas
        if ( bannedLanguagesMap != null ) {
            for ( Map.Entry<String, Object> unicode : bannedLanguagesMap.entrySet() ) {
                regex.append( unicode.getValue().toString() );

            }
        }

        String processedText = "";
        if ( ocrText != null && !ocrText.isEmpty() && regex != null ) {
            processedText = ocrText.replaceAll( "[" + regex + "]", " " );

        }

        return processedText;
    }


    /**
     * method to remove api-Congfigured languages from banned languages
     * @param helper
     * @param bannedLanguagesMap
     * @return
     */
    private Map<String, Object> filterBannedLanguages( ExtractionData helper, Map<String, Object> bannedLanguagesMap )
    {

        if ( helper.getApiKeyConfiguration().containsKey( Constants.CONFIGURATION )
            && helper.getApiKeyConfiguration().get( Constants.CONFIGURATION ) != null ) {
            Map<String, Object> configMap = (Map<String, Object>) helper.getApiKeyConfiguration()
                .get( Constants.CONFIGURATION );
            if ( configMap != null && configMap.containsKey( Constants.API_CONFIG )
                && configMap.get( Constants.API_CONFIG ) != null ) {
                Map<String, Object> apiConfigMap = (Map<String, Object>) configMap.get( Constants.API_CONFIG );
                if ( apiConfigMap != null && apiConfigMap.containsKey( Constants.LANGUAGES )
                    && apiConfigMap.get( Constants.LANGUAGES ) != null ) {
                    List<String> languages = (List<String>) apiConfigMap.get( Constants.LANGUAGES );
                    bannedLanguagesMap = filterBannedLanguages( languages, bannedLanguagesMap );
                }
            }
        }
        return bannedLanguagesMap;
    }


    /**
     * method to filter languages
     * @param configLanguages
     * @param bannedLanguagesMap
     * @return
     */
    private Map<String, Object> filterBannedLanguages( List<String> configLanguages, Map<String, Object> bannedLanguagesMap )
    {

        if ( !configLanguages.isEmpty() ) {

            Map<String, String> languageCodes = Constants.LANGUAGE_CODES;
            List<String> languages = configLanguages.stream().map( s -> languageCodes.getOrDefault( s, "" ) )
                .collect( Collectors.toList() );

            for ( String language : languages ) {
                if ( StringUtils.isNotEmpty( language ) ) {
                    Iterator<Map.Entry<String, Object>> iterator = bannedLanguagesMap.entrySet().iterator();
                    while ( iterator.hasNext() ) {
                        Map.Entry<String, Object> entry = iterator.next();
                        if ( entry.getKey().startsWith( language ) ) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
        return bannedLanguagesMap;
    }


}
