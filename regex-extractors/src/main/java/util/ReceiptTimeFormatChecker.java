package util;

import constants.timeconstants.TimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;

public class ReceiptTimeFormatChecker {
    private static final Logger LOG = LoggerFactory.getLogger( ReceiptTimeFormatChecker.class );


    private ReceiptTimeFormatChecker()
    {}


    public static String getTimeInStandardFormat( String time )
    {

        String correctedTime = time;

        /* Remove space if present at the end of time string */
        correctedTime = correctedTime.trim();

        /* Retrieve period(AM or PM) present in time */
        String timePeriod = correctedTime.substring( correctedTime.length() - 2, correctedTime.length() );
        correctedTime = formatCorrectionBasedOnTimePeriod( correctedTime, timePeriod );

        /* Convert to hh:mm:ss format if not already present */
        correctedTime = convertToStandardFormat( correctedTime );

        return correctedTime;
    }


    public static String convertFormat( String[] separatedTimeValue )
    {

        String timeValue = null;
        int hour = 0;
        int min = 0;
        int sec = 0;
        String timePeriod = "";
        try {
            if ( separatedTimeValue.length == 3 ) {
                hour = Integer.parseInt( separatedTimeValue[0] );
                min = Integer.parseInt( separatedTimeValue[1] );
                if ( separatedTimeValue[2].length() >= 2 ) {
                    timePeriod = separatedTimeValue[2].substring( separatedTimeValue[2].length() - 2,
                            separatedTimeValue[2].length() );
                }
                sec = Integer.parseInt( separatedTimeValue[2].replaceAll( "[\\D]", "" ) );
                timeValue = new Time( hour, min, sec ).toString();

            } else {
                hour = Integer.parseInt( separatedTimeValue[0] );
                if ( separatedTimeValue[1].length() >= 2 ) {
                    timePeriod = separatedTimeValue[1].substring( separatedTimeValue[1].length() - 2,
                            separatedTimeValue[1].length() );
                }
                min = Integer.parseInt( separatedTimeValue[1].replaceAll( "[\\D]", "" ) );
                timeValue = new Time( hour, min, 0 ).toString();
            }
            if ( timePeriod.matches( TimeConstants.TIME_PERIOD_REGEX ) ) {
                timeValue = timeValue + " " + timePeriod;
            }
        } catch ( NumberFormatException e ) {
            LOG.error( "Could not parse time from the string", e );
        }
        return timeValue;
    }


    private static String formatCorrectionBasedOnTimePeriod( String time, String timePeriod )
    {

        String formattedTime = time;

        if ( timePeriod.matches( TimeConstants.TIME_PERIOD_REGEX ) ) {

            /* Remove the time period value */
            formattedTime = formattedTime.substring( 0, time.indexOf( timePeriod ) ).trim();

            if ( timePeriod.matches( TimeConstants.PM_REGEX ) ) {
                /* Convert to 24 hour format */
                int hour = Integer.parseInt( time.substring( 0, 2 ) );
                if ( hour < 12 )
                    hour = ( hour + 12 ) % 24;
                formattedTime = String.valueOf( hour ) + formattedTime.substring( 2, formattedTime.length() );
            }
        }

        return formattedTime;
    }


    private static String convertToStandardFormat( String time )
    {
        /* Convert to hh:mm:ss format */
        String stdFormatTime = time;

        if ( stdFormatTime.length() < 6 ) {
            stdFormatTime = stdFormatTime + ":00";
        }

        return stdFormatTime;
    }
}
