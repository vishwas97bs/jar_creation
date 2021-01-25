package util.dateutil;

import constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.pojava.datetime.DateTime;
import org.pojava.datetime.DateTimeConfig;
import org.pojava.datetime.DateTimeConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RegexUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DateUtil {
    private static final Logger LOG = LoggerFactory.getLogger( DateUtil.class );
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final int MIN_YEAR = 1950;
    private static final int MAX_YEAR = 2050;
    private static final int[] DATES = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String US_DATE_FORMAT = "MM/dd/yyyy";


    private DateUtil()
    {
    }


    /**
     * Checks if is date within fixed year gap range.
     *
     * @param date the date
     * @return true, if is date within fixed year gap range
     */
    public static boolean isDateWithinFixedYearGapRange( String date )
    {
        LOG.trace( "checking if date {} is within one year range", date );
        boolean isaDateValid = false;
        SimpleDateFormat formatter = new SimpleDateFormat( DATE_FORMAT );
        Calendar currentDate = Calendar.getInstance();

        Calendar currentDateBeforeFixedYearGap = Calendar.getInstance();
        // TODO read year gap from property file, remove hard code
        // for time being checking if date is within three year range from current date
        currentDateBeforeFixedYearGap.add( Calendar.YEAR, -3 );
        try {
            Date dateParsed = formatter.parse( date );
            if ( dateParsed.after( currentDateBeforeFixedYearGap.getTime() ) && dateParsed.before( currentDate.getTime() ) ) {
                isaDateValid = true;
            }
        } catch ( ParseException e ) {
            LOG.error( "Error while parsing date", e );
        }
        return isaDateValid;
    }


    /**
     * Does a best-effort formatting of the given input date into a US date
     *
     * @param date Date to convert to US format
     * @return Date in the format MM-dd-yyyy
     */
    public static String formatDateToUS( String date )
    {
        try {
            Date dt = parseDateBestEffort( date );
            SimpleDateFormat toUsFormatter = new SimpleDateFormat( US_DATE_FORMAT );
            return toUsFormatter.format( dt );
        } catch ( ParseException e ) {
            LOG.warn( "Could not parse date with best effort, returning null", e );
            return null;
        }
    }


    /**
     * Does a best-effort formatting of the given input date into ISO date
     *
     * @param date Date to convert to ISO format
     * @return Date in the format yyyy-MM-dd
     */
    public static String formatDateToISO( String date )
    {
        try {
            Date dt = parseDateBestEffort( date );
            SimpleDateFormat toISOFormatter = new SimpleDateFormat( ISO_DATE_FORMAT );
            return toISOFormatter.format( dt );
        } catch ( ParseException e ) {
            LOG.warn( "Could not parse date with best effort, returning null", e );
            return null;
        }
    }


    public static Optional<Date> formatDate(String date, String format )
    {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat( format );
            formatter.setLenient( false );
            return Optional.ofNullable( formatter.parse( date ) );
        } catch ( Exception err ) {
            return Optional.empty();
        }
    }


    /**
     * Does a best-effort formatting of the given input date into format 1 i.e., dd/mm/yyyy
     *
     * @param date Date to convert to dd/mm/yyyy format
     * @return Date in the format yyyy-MM-dd
     */
    public static String getBillingDateWithFormat1( String date )
    {
        try {
            Date dt = parseDateBestEffort( date );
            SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
            return formatter.format( dt );
        } catch ( ParseException e ) {
            LOG.warn( "Could not parse date with best effort, returning null", e );
            return null;
        }
    }


    /**
     * Does a best-effort formatting of the given input date into format 2 i.e., mm/dd/yyyy
     *
     * @param date Date to convert to mm/dd/yyyy format
     * @return Date in the format yyyy-MM-dd
     */
    public static String getBillingDateWithFormat2( String date )
    {
        try {
            Date dt = parseDateBestEffort( date );
            SimpleDateFormat formatter = new SimpleDateFormat( "MM/dd/yyyy" );
            return formatter.format( dt );
        } catch ( ParseException e ) {
            LOG.warn( "Could not parse date with best effort, returning null", e );
            return null;
        }
    }


    /**
     * Does a best-effort parsing of the given input date
     *
     * @param date Date to parse
     * @return Date object containing the parsed date
     * @throws ParseException thrown if parsing failed
     */
    private static Date parseDateBestEffort( String date ) throws ParseException
    {
        Date dt;
        try {
            DateTimeConfigBuilder config = DateTimeConfigBuilder.newInstance();
            config.setDmyOrder( true );
            dt = new DateTime( date, DateTimeConfig.fromBuilder( config ) ).toDate();
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            //swapping date and year , if its a future date.
            if ( dt != null && dt.after( currentDate ) ) {
                Calendar calendarDate = DateUtils.toCalendar( dt );
                String extractedYear = Integer.toString( calendarDate.get( Calendar.YEAR ) );
                String extractedDate = Integer.toString( calendarDate.get( Calendar.DATE ) );
                int month = calendarDate.get( Calendar.MONTH );
                if ( extractedYear.length() == 4 && extractedDate.length() == 2 ) {
                    int day = Integer.parseInt( extractedYear.substring( 2 ) );
                    int year = Integer.parseInt( extractedYear.substring( 0, 2 ) + extractedDate );
                    if ( day <= DATES[month] || ( month == 1 && ( ( ( year % 4 == 0 ) && ( year % 100 != 0 ) ) || ( year % 400
                            == 0 ) ) && day == 29 ) ) {
                        calendar.set( year, month, day );
                        if ( !calendar.getTime().after( currentDate ) ) {
                            dt = calendar.getTime();
                        }
                    }
                }
            }
        } catch ( IllegalArgumentException e ) {
            LOG.warn( "Unable to parse as day-first, trying month-first", e );
            try {
                dt = new DateTime( date ).toDate();
            } catch ( IllegalArgumentException e1 ) {
                LOG.warn( "Unable to parse as day-first either, falling back to brute force", e1 );
                dt = parseDateBruteForce( date );
            }
        }
        return dt;
    }


    /**
     * accessible dateParser for parsing date string using datetime builder and brute-force approach.
     * @param date
     * @return
     */
    public static Optional<Date> parseDateString( String date )
    {

        try {
            return Optional.ofNullable( parseDateBestEffort( date ) );
        } catch ( ParseException e ) {
            return Optional.empty();
        }
    }


    /**
     * Breaks up the incoming date and attempts to parse using certain rules
     *
     * @param date Input date
     * @return Parsed date
     */
    private static Date parseDateBruteForce( String date ) throws ParseException
    {

        int[] dateParts;
        try {
            String[] datePartsString = breakUpDate( cleanupDate( date ) );
            if ( datePartsString.length != 3 ) {
                throw new ParseException( "Could not parse " + date, 0 );
            }
            dateParts = toInt( datePartsString );
        } catch ( NumberFormatException e ) {
            throw new ParseException( "Could not parse " + date, 0 );
        }
        int year;
        int month;
        int day;
        // Handle Year at first and Year at last separately
        if ( dateParts[0] > dateParts[2] ) {
            // Year at first
            year = dateParts[0];
            if ( dateParts[1] > 12 ) {
                month = dateParts[2];
                day = dateParts[1];
            } else {
                month = dateParts[1];
                day = dateParts[2];
            }
        } else {
            // Year at last
            year = dateParts[2];
            if ( dateParts[1] > 12 ) {
                month = dateParts[0];
                day = dateParts[1];
            } else {
                month = dateParts[1];
                day = dateParts[0];
            }
        }
        Calendar parsedDate = Calendar.getInstance();
        parsedDate.set( year, month - 1, day );
        return parsedDate.getTime();
    }


    /**
     * Breaks up a date given as 20160120 or 10/10/2015 or 20022017
     *
     * @param date Input date
     * @return Broken up date
     */
    private static String[] breakUpDate( String date )
    {
        String tempDate = date;
        tempDate = tempDate.trim();
        String[] dateParts = tempDate.split( "/" );
        if ( dateParts.length == 1 ) {
            if ( tempDate.length() == 7 ) {
                tempDate = "0" + tempDate;
            }
            if ( tempDate.length() != 6 && tempDate.length() != 8 ) {
                return dateParts;
            }
            dateParts = new String[] { tempDate.substring( 0, 2 ), tempDate.substring( 2, 4 ), tempDate.substring( 4 ) };
            int possibleYear = Integer.parseInt( dateParts[2] );
            if ( possibleYear < 100 ) {
                dateParts[2] = String.valueOf( possibleYear + 2000 );
            } else if ( possibleYear < MIN_YEAR || possibleYear > MAX_YEAR ) {
                dateParts = new String[] { tempDate.substring( 0, 4 ), tempDate.substring( 4, 6 ), tempDate.substring( 6 ) };
            }
        } else if ( dateParts.length == 3 && dateParts[0].length() == 2 && dateParts[2].length() == 2 ) {
            dateParts[2] = String.valueOf( Integer.parseInt( dateParts[2] ) + 2000 );
        }
        return dateParts;
    }


    /**
     * Converts an array of integers in String form to ints
     *
     * @param strings Strings to parse
     * @return Array of integers
     */
    private static int[] toInt( String[] strings )
    {
        int[] ints = new int[strings.length];
        for ( int i = 0; i < ints.length; i++ ) {
            ints[i] = Integer.parseInt( strings[i].trim() );
        }
        return ints;
    }


    /**
     * Standardizes multiple date separators, such as "." and "-" into "/"
     *
     * @param date Incoming date with different possible separators
     * @return Date with "/" separator
     */
    private static String cleanupDate( String date )
    {
        return date.trim().replace( "-", "/" ).replace( ".", "/" ).replace( " ", "/" );
    }


    public static String cleanupWhiteSpaces( String date )
    {
        return date.replaceAll( "\\s{1,}", "" );
    }


    public static boolean isDateParseable( String date, DateTimeFormatter dtf )
    {
        try {
            LocalDate.parse( date, dtf );
            return true;
        } catch ( Exception e ) {
            return false;
        }
    }

    public static String parseAmbigousOtherLocaleDateFormats( String date, String locale, String outputDateFormat )
    {
        AtomicReference<String> parsedDate = new AtomicReference<>( "" );

        String cleanDate = DateUtil.cleanupWhiteSpaces( date );
        List<String> customDateFormats = Constants.CUSTOM_LANGUAGE_DATE_FORMAT.getOrDefault( locale, Collections.emptyList() );

        for ( String customFormat : customDateFormats ) {
            DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern( customFormat ).withLocale( new Locale( locale ) );

            if ( DateUtil.isDateParseable( cleanDate, customDateFormatter ) ) {
                LocalDate formattedDate = LocalDate.parse( cleanDate, customDateFormatter );
                parsedDate.set( formattedDate.format( StringUtils.isNotEmpty( outputDateFormat ) ?
                        DateTimeFormatter.ofPattern(outputDateFormat) : DateTimeFormatter.ISO_LOCAL_DATE ) );
                break;
            }

        }
        return parsedDate.get();
    }

    public static String formatCustomLanguageDates( Object value, String locale, String outputDateFormat )
    {
        AtomicReference<String> parsedDate = new AtomicReference<>( "" );
        if ( null != value ) {

            String date = value.toString();

            if ( RegexUtils.checkIfStringContainsRegexPattern( date, Constants.CUSTOM_LANGUAGE_DATE_REGEX.get(locale) ) ) {
                String possibleDate = DateUtil.parseAmbigousOtherLocaleDateFormats( date, locale, outputDateFormat);
                if ( StringUtils.isNotEmpty( possibleDate ) ) {
                    parsedDate.set( possibleDate );
                }
            }
        }
        return parsedDate.get();
    }
}
