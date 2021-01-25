package googlevision.util;

import googlevision.exception.PropertyException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PropertyFileHandler
{
    private static final Map<String, Properties> properties = new HashMap<>();


    /**
     * private constructor
     */
    private PropertyFileHandler()
    {}


    /**
     * Reads a property from Property file and returns value for the property
     *
     * @param file file to read property from
     * @param key  key to get value for
     * @return value for given key
     * @throws PropertyException thrown if a property could not be found
     */
    public static String getProperty( String file, String key ) throws PropertyException
    {
        try {
            if ( !properties.containsKey( file ) ) {
                synchronized ( properties ) {
                    if ( !properties.containsKey( file ) ) {
                        InputStream inputStream = PropertyFileHandler.class.getClassLoader()
                            .getResourceAsStream( file + ".properties" );
                        if ( inputStream == null ) {
                            throw new FileNotFoundException( "Property file " + file + ".properties not found" );
                        }
                        Properties prop = new Properties();
                        prop.load( inputStream );
                        properties.put( file, prop );
                    }
                }
            }
            return properties.get( file ).getProperty( key );
        } catch ( Exception e ) {
            throw new PropertyException( "There was a problem getting " + key + " from " + file + ".properties", e );
        }
    }


    public static Properties loadProperties( String fileName )
    {
        try {
            if ( !properties.containsKey( fileName ) ) {
                synchronized ( properties ) {
                    if ( !properties.containsKey( fileName ) ) {
                        InputStream inputStream = PropertyFileHandler.class.getClassLoader()
                            .getResourceAsStream( fileName + ".properties" );
                        if ( inputStream == null ) {
                            throw new FileNotFoundException( "Property file " + fileName + ".properties not found" );
                        }
                        Properties prop = new Properties();
                        prop.load( inputStream );
                        properties.put( fileName, prop );
                    }
                }
            }
            return properties.get( fileName );
        } catch ( Exception e ) {
            throw new PropertyException( "There was a problem getting file " + fileName + ".properties", e );
        }
    }


    /**
     * Reads "config.properties" and returns value for a given property
     *
     * @param key key to get value for
     * @return value for given key
     * @throws PropertyException
     */
    public static String getProperty( String key ) throws PropertyException
    {
        return getProperty( "config", key );
    }
}
