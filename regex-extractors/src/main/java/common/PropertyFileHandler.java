package common;


import common.exception.PropertyException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Class for handling property files. Returns value for keys from property files
 * Created by SriramKeerthi on 4/15/2015.
 */
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
     * @throws PropertyException
     */
    public static String getProperty( String file, String key )
    {
        try {
            if ( !properties.containsKey( file ) ) {
                synchronized ( properties ) {
                    if ( !properties.containsKey( file ) ) {
                        loadProps( file );
                    }
                }
            }
            return properties.get( file ).getProperty( key );
        } catch ( IOException e ) {
            throw new PropertyException( "There was a problem getting " + key + " from " + file + ".properties", e );
        }
    }


    private static void loadProps( String file ) throws IOException
    {
        InputStream inputStream = PropertyFileHandler.class.getClassLoader().getResourceAsStream( file + ".properties" );
        if ( inputStream == null ) {
            throw new FileNotFoundException( "Property file " + file + ".properties not found" );
        }
        Properties prop = new Properties();
        prop.load( inputStream );
        properties.put( file, prop );
    }
}
