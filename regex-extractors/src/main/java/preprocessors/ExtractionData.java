package preprocessors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.Constants;
import entity.VisionLineBlock;
import model.GimletCustomer;
import model.ScanRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.*;

public class ExtractionData {
    private static final Logger LOG = LoggerFactory.getLogger( ExtractionData.class );
    private String ocrText;
    private String originalText;
    private String ocrXml;
    private List<VisionLineBlock> ocrCoordinates;
    private String locale;
    private ScanRequest scanRequest;
    private String localFilePath;
    private Map<String, Object> configuration;
    private Map<String, Double> logoData;
    private GimletCustomer customer;
    private Map<String, Object> rerunInfo;
    private List<String> defaultFieldsList;
    private List<Map<String, Object>> langBasedLocaleEntities;
    private Map<String, Object> apiKeyConfiguration;


    public ExtractionData( ExtractionData data )
    {
        this.ocrText = data.getOcrText();
        this.ocrXml = data.getOcrXml();
        this.originalText = data.getOriginalText();
        this.ocrCoordinates = data.getOcrCoordinates();
        this.locale = data.getLocale();
        this.scanRequest = data.getScanRequest();
        this.configuration = data.getConfiguration();
        this.localFilePath = data.getLocalFilePath();
        this.logoData = data.getLogoData();
        this.customer = data.getCustomer();
        this.rerunInfo = data.getRerunInfo();
        this.defaultFieldsList = data.getDefaultFieldsList();
        this.langBasedLocaleEntities = data.getLangBasedLocaleEntities();
        this.apiKeyConfiguration = data.getApiKeyConfiguration();
    }


    public ExtractionData( Map<String, Object> data )
    {
        this.ocrText = (String) data.get( "ocrText" );
        this.ocrXml = (String) data.get( "ocrXml" );

        this.originalText = (String) data.get( "originalText" );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        this.ocrCoordinates = objectMapper
                .convertValue( data.get( "ocrCoordinates" ), new TypeReference<List<VisionLineBlock>>()
                {
                } );

        this.locale = (String) data.get( "locale" );
        this.configuration = (Map<String, Object>) data.get( "configuration" );
        this.scanRequest = new ScanRequest( (Map<String, Object>) data.get( "scanRequest" ) );
        this.localFilePath = (String) data.get( "localFilePath" );
        this.logoData = (Map<String, Double>) data.get( "logoData" );
        this.rerunInfo = (Map<String, Object>) data.get( common.constants.Constants.RERUN_INFO );
        this.defaultFieldsList = (List<String>) data.get( "defaultList" );
        if ( data.containsKey( "customer" ) )
            this.customer = new GimletCustomer( (Map<String, Object>) data.get( "customer" ) );
        this.langBasedLocaleEntities = (List<Map<String, Object>>) data.get( "langBasedLocaleEntities" );
        this.apiKeyConfiguration = (Map<String, Object>) data.get( "customerConfiguration" );
    }


    public Map<String, Object> getMapObject()
    {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>()
        {
        };
        Map<String, Object> response = new HashMap<>();
        response.put( "ocrText", this.ocrText );
        response.put( "ocrXml", this.ocrXml );
        response.put( "originalText", this.originalText );
        response.put( "ocrCoordinates", this.ocrCoordinates );
        response.put( "locale", this.locale );
        response.put( "configuration", this.configuration );
        response.put( "scanRequest", mapper.convertValue( this.scanRequest, typeRef ) );
        response.put( "logoData", this.logoData );
        response.put( common.constants.Constants.RERUN_INFO, this.rerunInfo );
        response.put( "defaultList", this.defaultFieldsList );
        if ( this.customer != null )
            response.put( "customer", mapper.convertValue( this.customer, typeRef ) );
        response.put( "langBasedLocaleEntities", this.langBasedLocaleEntities );
        response.put( "customerConfiguration", this.apiKeyConfiguration );
        return response;
    }


    public String getOcrText()
    {
        return ocrText;
    }


    public void setOcrText( String ocrText )
    {
        this.ocrText = ocrText;
    }


    public String getOriginalText()
    {
        return originalText;
    }


    public void setOriginalText( String originalText )
    {
        this.originalText = originalText;
    }


    public String getOcrXml()
    {
        return ocrXml;
    }


    public void setOcrXml( String ocrXml )
    {
        this.ocrXml = ocrXml;
    }


    public List<VisionLineBlock> getOcrCoordinates()
    {
        return ocrCoordinates;
    }


    public void setOcrCoordinates( List<VisionLineBlock> ocrCoordinates )
    {
        this.ocrCoordinates = ocrCoordinates;
    }


    public String getLocale()
    {
        return locale;
    }


    public void setLocale( String locale )
    {
        this.locale = locale;
    }


    public ScanRequest getScanRequest()
    {
        return scanRequest;
    }


    public void setScanRequest( ScanRequest scanRequest )
    {
        this.scanRequest = scanRequest;
    }


    /*
     * This function will return default and locale specific List of regex for
     * given field
     */
    public List<String> getRegexList( String fieldName )
    {
        return getAllValueList( fieldName, Constants.EXTRACT_REGEX_EXTENSION );
    }


    /**
     * Gets additional month names( should be other than the default months) from the configuration, if any, for a language
     * @param language Language whose additional month names are to be fetched
     * @return List of additional months,if present. Otherwise, returns NULL
     */
    public List<String> getExtraMonths( String language )
    {
        return getValueList( language + Constants.EXTRA_MONTHS_EXTENSION );
    }


    /**
     * Gets additional short month names( should be other than the default short months) from the configuration, if any, for a language
     * @param language Language whose additional short month names are to be fetched
     * @return List of additional short months,if present. Otherwise, returns NULL
     */
    public List<String> getExtraShortMonths( String language )
    {
        return getValueList( language + Constants.EXTRA_SHORT_MONTHS_EXTENSION );
    }


    private List<String> getKeys( String key, String extension )
    {
        List<String> response = new ArrayList<>();
        if ( this.locale != null ) {
            if ( this.locale.contains( "_" ) ) {
                String value = this.locale + "_" + key + extension;
                response.add( value );
            } else {
                String valuePattern = this.getLocale() + ".{0,9}_" + key + extension;
                for ( String ky : this.configuration.keySet() ) {
                    if ( ky.matches( valuePattern ) ) {
                        response.add( ky );
                    }
                }
            }
        }
        if ( response.isEmpty() ) {
            String value = key + extension;
            response.add( value );
        }
        return response;
    }


    @SuppressWarnings ("unchecked") public List<String> getAllValueList( String key, String extension )
    {
        List<String> response = new ArrayList<>();

        List<String> keySet = getKeys( key, extension );

        for ( String prop : keySet ) {
            List<String> value = (List<String>) getExtractionConfiguration( prop );
            if ( value != null )
                response.addAll( new ArrayList<>( value ) );
        }
        return response;
    }


    @SuppressWarnings ("unchecked") public Map<String, Object> getAllValueMap( String key, String extension )
    {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        List<String> keySet = getKeys( key, extension );

        for ( String prop : keySet ) {
            Object extractionConfig = getExtractionConfiguration( prop );
            if ( extractionConfig != null ) {
                List<Map<String, Object>> mappings = (List<Map<String, Object>>) extractionConfig;
                for ( Map<String, Object> mapping : mappings ) {
                    String mappingKey = (String) mapping.get( "key" );
                    Object mappingValue = mapping.get( "value" );
                    if ( !StringUtils.isEmpty( mappingKey ) && mappingValue != null ) {
                        response.put( mappingKey, mappingValue );
                    }
                }
            }
        }
        return response;
    }


    /*
     * This function will return default and locale specific List of
     * vicinity/Cue words for given field
     */
    public List<String> getVicinityList( String fieldName )
    {
        return getAllValueList( fieldName, Constants.VICINITY_EXTENSION );
    }


    public Map<String, String> getFormatCorrectionMap( String fieldName )
    {
        LinkedHashMap<String, String> formatCorrectionMap = new LinkedHashMap<>();
        Map<String, Object> mappings = getAllValueMap( fieldName, "_format_map" );
        for ( Map.Entry<String, Object> mapping : mappings.entrySet() ) {
            String lhs = mapping.getKey();
            String rhs = (String) mapping.getValue();
            try {
                formatCorrectionMap.put( lhs, rhs );
            } catch ( NullPointerException | IllegalArgumentException e ) {
                LOG.error( "Error in mapping! Skipping the mapping: {} because {} exception was encountered", mapping, e );
            }

        }
        return formatCorrectionMap;
    }


    public List<String> getNonStrictVicinityList( String fieldName )
    {
        return getAllValueList( fieldName, Constants.NON_STRICT_VICINITY_EXTENSION );
    }


    @SuppressWarnings ("unchecked") public List<String> getValueList( String key )
    {
        List<String> response = (List<String>) getExtractionConfiguration( key );
        if ( response == null )
            response = new ArrayList<>();
        return new ArrayList<>( response );
    }


    public List<String> getMappedList( String value )
    {
        List<String> response = new ArrayList<>();
        if ( StringUtils.isNotEmpty( value ) ) {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<String>> configType = new TypeReference<List<String>>()
            {
            };
            try {
                response = mapper.readValue( value, configType );
            } catch ( IOException e ) {
                LOG.error( "", e );
            }
        }
        return response;
    }


    public Object getExtractionConfiguration( String key )
    {
        Object value = null;
        if ( this.configuration.containsKey( key ) ) {
            value = this.configuration.get( key );
        }
        return value;
    }


    public Map<String, Object> getConfiguration()
    {
        return configuration;
    }


    @SuppressWarnings ("unchecked") public Map<String, Object> getExtractionConfigurationMap( String key )
    {
        Object value = getExtractionConfiguration( key );
        Map<String, Object> mapObject = null;
        if ( value != null ) {
            mapObject = (Map<String, Object>) value;
        }
        return mapObject;
    }


    public void setConfiguration( Map<String, Object> configuration )
    {
        this.configuration = configuration;
    }


    public Locale getLocaleObject()
    {
        return getLocaleObject( this.getLocale() );
    }


    public Locale getLocaleObject( String localeVal )
    {
        Locale localeObject = null;
        if ( localeVal != null ) {
            String[] localeParts = localeVal.split( "[_-]" );
            if ( localeParts.length == 1 ) {
                localeObject = new Locale( localeParts[0] );
            } else if ( localeParts.length == 2 ) {
                localeObject = new Locale( localeParts[0], localeParts[1] );
            }
        } else {
            localeObject = Locale.ENGLISH;
        }
        return localeObject;
    }


    public List<Locale> getLocales()
    {
        List<Locale> locales = null;
        if ( this.scanRequest.getPossibleLanguages() != null ) {
            locales = new ArrayList<>();
            String[] languages = this.scanRequest.getPossibleLanguages().split( "[|]" );
            for ( String language : languages ) {
                Locale localeObject = getLocaleObject( language );
                locales.add( localeObject );
            }
        }
        return locales;
    }


    public String[] getMonths()
    {
        String[] response = getMonths( Locale.ENGLISH );

        List<Locale> locales = this.getLocales();
        if ( locales == null ) {
            locales = new ArrayList<>();
            locales.add( getLocaleObject() );
        }
        for ( Locale localeObject : locales ) {
            String[] additionalMonths = getExtraMonths( localeObject.getLanguage() ).toArray( new String[0] );
            if ( additionalMonths == null ) {
                additionalMonths = new String[0];
            }
            /*To prevent 'English' months from getting extracted twice*/
            if ( !localeObject.equals( Locale.ENGLISH ) ) {
                String[] vals = getMonths( localeObject );
                if ( response == null ) {
                    response = vals;
                } else {
                    response = (String[]) ArrayUtils.addAll( response, vals );
                }
            }
            response = (String[]) ArrayUtils.addAll( response, additionalMonths );
        }
        return response;
    }


    public String[] getMonths( Locale locale )
    {
        String[] response = null;
        if ( locale != null ) {
            response = new DateFormatSymbols( locale ).getMonths();
        }
        return response;
    }


    public String[] getShortMonths()
    {
        String[] response = getShortMonths( Locale.ENGLISH );

        List<Locale> locales = this.getLocales();
        if ( locales == null ) {
            locales = new ArrayList<>();
            locales.add( getLocaleObject() );
        }
        for ( Locale localeObject : locales ) {
            String[] additionalMonths = getExtraShortMonths( localeObject.getLanguage() ).toArray( new String[0] );
            if ( additionalMonths == null ) {
                additionalMonths = new String[0];
            }
            /*To prevent 'English' short months from getting extracted twice*/
            if ( !localeObject.equals( Locale.ENGLISH ) ) {
                String[] vals = getShortMonths( localeObject );
                if ( response == null ) {
                    response = vals;
                } else {
                    response = (String[]) ArrayUtils.addAll( response, vals );
                }
            }
            response = (String[]) ArrayUtils.addAll( response, additionalMonths );
        }
        return response;
    }


    public String[] getShortMonths( Locale locale )
    {
        String[] response = null;
        if ( locale != null ) {
            response = new DateFormatSymbols( locale ).getShortMonths();
        }
        return response;
    }


    public String getMonthsRegex()
    {
        String response = "";
        String[] months = getMonths();
        String[] shortMonths = getShortMonths();
        StringBuilder stringBuilder = new StringBuilder();
        if ( months != null ) {
            joinStringArray( stringBuilder, months );
        }
        if ( shortMonths != null ) {
            joinStringArray( stringBuilder, shortMonths );
        }
        stringBuilder.append( "|" + stringBuilder.toString().toLowerCase() );
        response = stringBuilder.toString();
        return response;
    }


    public String getCurrenciesRegex()
    {
        String response = "";
        String[] currencies = getAllValueList( Constants.CURRENCY, Constants.LIST_EXTENSION ).toArray( new String[0] );
        StringBuilder stringBuilder = new StringBuilder();
        if ( currencies != null ) {
            joinStringArray( stringBuilder, currencies );
        }
        response = stringBuilder.toString();
        return response;
    }


    private void joinStringArray( StringBuilder stringBuilder, String[] list )
    {
        if ( list == null )
            return;
        for ( String data : list ) {
            if ( data.trim().length() == 0 )
                continue;
            if ( stringBuilder.length() > 0 ) {
                stringBuilder.append( "|" );
            }
            stringBuilder.append( data );
        }
    }


    public String getLocalFilePath()
    {
        return localFilePath;
    }


    public void setLocalFilePath( String localFilePath )
    {
        this.localFilePath = localFilePath;
    }


    public Map<String, Double> getLogoData()
    {
        return logoData;
    }


    public void setLogoData( Map<String, Double> logoData )
    {
        this.logoData = logoData;
    }


    public GimletCustomer getCustomer()
    {
        return customer;
    }


    public void setCustomer( GimletCustomer customer )
    {
        this.customer = customer;
    }


    public Map<String, Object> getRerunInfo()
    {
        return rerunInfo;
    }


    public void setRerunInfo( Map<String, Object> rerunInfo )
    {
        this.rerunInfo = rerunInfo;
    }


    public List<String> getDefaultFieldsList()
    {
        return defaultFieldsList;
    }


    public void setDefaultFieldsList( List<String> defaultList )
    {
        this.defaultFieldsList = defaultList;
    }


    public List<Map<String, Object>> getLangBasedLocaleEntities()
    {
        return langBasedLocaleEntities;
    }


    public void setLangBasedLocaleEntities( List<Map<String, Object>> localeEntity )
    {
        this.langBasedLocaleEntities = localeEntity;
    }


    public Map<String, Object> getApiKeyConfiguration()
    {
        return apiKeyConfiguration;
    }


    public void setApiKeyConfiguration( Map<String, Object> apiKeyConfiguration )
    {
        this.apiKeyConfiguration = apiKeyConfiguration;
    }


    @Override public String toString()
    {
        return "ExtractionData{" + "ocrText='" + ocrText + '\'' + ", ocrXml='" + ocrXml + '\'' + ", ocrCoordinates="
                + ocrCoordinates + ", locale='" + locale + '\'' + ", scanRequest=" + scanRequest + ", localFilePath='"
                + localFilePath + '\'' + ", configuration=" + configuration + ", logoData=" + logoData + ", customer=" + customer
                + ", rerunInfo=" + rerunInfo + ", defaultFieldsList=" + defaultFieldsList + ", langBasedLocaleEntities="
                + langBasedLocaleEntities + ", apiKeyConfiguration=" + apiKeyConfiguration + '}';
    }
}
