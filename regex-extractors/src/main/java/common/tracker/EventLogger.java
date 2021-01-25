package common.tracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EventLogger
{

    private static final Logger LOG = LoggerFactory.getLogger( EventLogger.class );

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );


    /**
     * Logs the scan status
     *
     * @param scanId
     * @param status
     */
    public static void logScanIdEvent( String scanId, EventEnum status )
    {
        if ( LOG.isInfoEnabled() ) {
            LOG.info( "Scan Id: {}, Status: {}, Time: {}", scanId, status.getEvent(),
                DATE_FORMAT.format( new Date( System.currentTimeMillis() ) ) );
        }
    }


    public static void logScanIdFailureEvent( String scanId, Exception e )
    {
        if ( LOG.isInfoEnabled() ) {
            LOG.info( "Scan Id: {}, Status: {}, Time: {}, Cause: {}", scanId, EventEnum.FAILED.getEvent(),
                DATE_FORMAT.format( new Date( System.currentTimeMillis() ) ), e.getMessage() );
        }
    }


    public static void logScanIdEvent( String scanId, String field, EventEnum status )
    {
        if ( LOG.isInfoEnabled() ) {
            LOG.info( "Scan Id: {}, Status: {}, Field: {}, Time: {}", scanId, status.getEvent(), field,
                DATE_FORMAT.format( new Date( System.currentTimeMillis() ) ) );
        }
    }
}
