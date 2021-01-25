package common;

import com.google.gson.*;
import org.bson.types.ObjectId;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;


/**
 * Utility methods
 *
 * @author Sriram
 * @since 11/18/2016
 */
public class Utils
{

    public static final Gson GSON;

    static {
        JsonDeserializer<ObjectId> des = new JsonDeserializer<ObjectId>() {

            @Override
            public ObjectId deserialize( JsonElement je, Type type, JsonDeserializationContext jdc )
            {
                return new ObjectId( je.getAsJsonObject().get( "$oid" ).getAsString() );
            }
        };
        JsonSerializer<ObjectId> ser = new JsonSerializer<ObjectId>() {

            @Override
            public JsonElement serialize( ObjectId src, Type typeOfSrc, JsonSerializationContext context )
            {
                JsonObject jo = new JsonObject();
//                jo.addProperty( "$oid", src.toHexString() );
                return jo;
            }
        };
        GSON = new GsonBuilder().registerTypeAdapter( ObjectId.class, des ).registerTypeAdapter( ObjectId.class, ser ).create();
    }


    /**
     * private constructor
     */
    private Utils()
    {

    }


    /**
     * Converts a Object to a Map
     *
     * @param object the objecet
     * @return the map
     */
    @SuppressWarnings ( "unchecked")
    public static Map<String, Object> toMap( Object object )
    {
        return toObject( object, HashMap.class );
    }


    /**
     * Converts a json string to a Map
     *
     * @param json the json String
     * @return the map
     */
    @SuppressWarnings ( "unchecked")
    public static Map<String, Object> toMap( String json )
    {
        return GSON.fromJson( json, HashMap.class );
    }


    /**
     * Converts an Object to the specified class
     *
     * @param object the ibject
     * @param clazz  the class
     * @return the converted object
     */
    public static <T> T toObject( Object object, Class<T> clazz )
    {
        return GSON.fromJson( GSON.toJson( object ), clazz );

    }


    /**
     * Converts a JSON String to the specified class
     *
     * @param clazz  the class
     * @return the converted object
     */
    public static <T> T toObject( String json, Class<T> clazz )
    {
        return GSON.fromJson( json, clazz );

    }


    /**
     * Convert a object to its JSON representation
     *
     * @param object the object to convert
     * @return the JSON string
     */
    public static String toJson( Object object )
    {
        return GSON.toJson( object );
    }


    /**
     * Returns property if it is non-null, else returns the default val
     *
     * @param property   the property
     * @param defaultVal the default val
     * @return either property or default value
     */
    public static <T> T getOrDefault( T property, T defaultVal )
    {
        return property != null ? property : defaultVal;
    }


    /**
     * String Util method
     *
     * @param string the string
     * @return true if a string is null or an empty string
     */
    public static boolean isNullOrEmpty( String string )
    {
        return string == null || string.isEmpty();
    }


    /**
     * Reads lines from a text file
     *
     * @param fileName File name to read from
     * @return List of lines from the file
     * @throws IOException thrown if there is a problem accessing the file
     */
    public static List<String> getFileLines( String fileName ) throws IOException
    {
        List<String> text = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader( new FileReader( fileName ) ) ) {
            String line;
            while ( ( line = br.readLine() ) != null ) {
                text.add( line );
            }
        }
        return text;
    }


    /**
     * Splits a text block into lines
     *
     * @param text Text to split
     * @return List of lines from the text
     */
    public static List<String> getStringLines( String text )
    {
        return Arrays.asList( text.split( "\n" ) );
    }


    /**
     * Splits a text block into lines
     *
     * @param text Text to split
     * @return List of lines from the text
     */
    public static String[] getStringLinesFromOcrText( String text )
    {
        return text.split( "(\r\n)|\n" );
    }


    /**
     * Combines a list of strings into one, separated by given separator
     *
     * @param text      Text to combine
     * @param separator Separator
     * @return combined text
     */
    public static String mkString( List<String> text, String separator )
    {
        StringBuilder sb = new StringBuilder();
        for ( String str : text ) {
            if ( sb.length() > 0 )
                sb.append( separator );
            sb.append( str );
        }
        return sb.toString();
    }


    /**
     * This method rounds double variables having a lot of digits after decimal to number of places specified.
     * @param value
     * @param places
     * @return
     */
    public static double round( double value, int places )
    {
        if ( places < 0 )
            throw new IllegalArgumentException();

        long factor = (long) Math.pow( 10, places );
        value = value * factor;
        long tmp = Math.round( value );
        return (double) tmp / factor;
    }

    /**
     * This method returns the number of lines in the ocr Text
     * @param ocrText
     * @return
     */
    public static int getLineCountOfOcrText(String ocrText)
    {
        return getStringLinesFromOcrText(ocrText).length;
    }

}
