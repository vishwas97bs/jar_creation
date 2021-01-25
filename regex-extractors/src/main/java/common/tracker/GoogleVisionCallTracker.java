package common.tracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GoogleVisionCallTracker
{
    private static final Logger LOGGER = LoggerFactory.getLogger( GoogleVisionCallTracker.class );


    /**
     *
     * @param timeInMs
     */
    public static void logTimeTaken(  long timeInMs )
    {
        if ( LOGGER.isInfoEnabled() ) {
            LOGGER.info( "currentTime {} timeTaken {}", System.currentTimeMillis(), timeInMs );
        }
    }


    /**
     *
     * @param errorMsg
     */
    public static void logErrorMsg( int code, String errorMsg )
    {
        if ( LOGGER.isInfoEnabled() ) {
            LOGGER.info( "currentTime {} errorCode {} errorMsg {}", System.currentTimeMillis(), code, errorMsg );
        }
    }
}
