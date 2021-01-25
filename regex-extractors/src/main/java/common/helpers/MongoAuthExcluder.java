package common.helpers;

/**
 * Removes the authentication details from the MongoURL
 * @author NirupamaS
 */
public class MongoAuthExcluder
{
    private static final int MONGO_URL_LENGTH = 2;
    private static final String PLACEHOLDER_STR = "****";


    /**
     * private constructor
     */
    private MongoAuthExcluder()
    {

    }


    /**
     * Excludes the authentication in a mongoUrl, if exists
     * @param mongoUrl mongo url
     * @return mongo url without authentication details
     */
    public static String excludeMongoAuth( String mongoUrl )
    {
        String[] mongoPrefix = mongoUrl.split( "//" );
        String[] mongoPostfix = { "" };
        if ( mongoPrefix.length > 1 )
            mongoPostfix = mongoPrefix[1].split( "@" );
        String username = mongoPostfix[0].split( ":" )[0];
        if ( mongoPostfix.length == MONGO_URL_LENGTH ) {
            return mongoPrefix[0] + "//" + username + ":" + PLACEHOLDER_STR + "@" + mongoPostfix[1];
        }
        return mongoUrl;
    }
}
