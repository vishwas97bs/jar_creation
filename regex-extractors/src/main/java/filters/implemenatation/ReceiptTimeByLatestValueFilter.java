package filters.implemenatation;


import filters.ExtractedValue;
import filters.filterinterface.ExtractionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preprocessors.ExtractionData;
import util.ReceiptTimeFormatChecker;

import java.sql.Time;
import java.util.*;


public class ReceiptTimeByLatestValueFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( ReceiptTimeByLatestValueFilter.class );
    private static final String CONFIDENCE_KEY = "confidence";
    private static final String OPERATION_KEY = "operation";


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, ExtractionData helper)
    {

        List<ExtractedValue> outputList = new ArrayList<ExtractedValue>();
        ExtractedValue output = new ExtractedValue();

        Map<Time, Map<String, Object>> timeValuesWithDetails = new HashMap<>();
        Time recentTimeValue = null;

        boolean notInFormat = false;
        LOG.trace( "List before filteration: {}", input );
        if ( input != null && !input.isEmpty() ) {

            for ( ExtractedValue value : input ) {

                Object extractedValue = value.getValue();
                if ( extractedValue != null ) {
                    String timeString = (String) extractedValue;
                    timeString = timeString.replaceAll( "[\\s]", "" );
                    /*
                     * Split the time into parts and check if it is a 2 digit value
                     */
                    String[] separatedTime = timeString.split( ":" );
                    for ( String part : separatedTime ) {
                        /* Checking if it is a 2 digit value */
                        if ( part.length() < 2 ) {
                            notInFormat = true;
                        }
                    }

                    if ( notInFormat ) {
                        /* Not in hh:mm:ss or hh:mm format */
                        timeString = ReceiptTimeFormatChecker.convertFormat( separatedTime );
                    }

                    /* Get time in hh:mm:ss format */
                    timeString = ReceiptTimeFormatChecker.getTimeInStandardFormat( timeString );

                    Time time = Time.valueOf( timeString );
                    insertTimeWithCorrespondingDetails( timeValuesWithDetails, time, value.getObjectToSave() );
                }
            }
            if ( !timeValuesWithDetails.isEmpty() ) {
                recentTimeValue = Collections.max( timeValuesWithDetails.keySet() );
                Map<String, Object> valueDetails = timeValuesWithDetails.get( recentTimeValue );

                LOG.debug( "Setting the details of the time value {} using the details {} ", recentTimeValue, valueDetails );
                output.setValue( recentTimeValue.toString() );
                output.setConfidence( (double) valueDetails.get( CONFIDENCE_KEY ) );
                output.setOperation( (String) valueDetails.get( OPERATION_KEY ) );
                outputList.add( output );
            } else {
                LOG.warn( "Could not find any valid time values in the list" );
            }
        } else {
            LOG.warn( "Input list was found to be empty or null" );
        }
        LOG.trace( "List after filteration: {}", outputList );
        return outputList;
    }


    /**
     * Inserts the time value along with the details
     * @param timeValuesWithConf Map of <Time, Details>
     * @param time Time value
     * @param details Details regarding the time
     */
    private void insertTimeWithCorrespondingDetails( Map<Time, Map<String, Object>> timeValuesWithConf, Time time,
        Map<String, Object> details )
    {

        if ( timeValuesWithConf.containsKey( time ) ) {
            if ( ( (Double) timeValuesWithConf.get( time ).get( CONFIDENCE_KEY ) )
                .compareTo( details.get( CONFIDENCE_KEY ) == null ? 0.0 : (Double) details.get( CONFIDENCE_KEY ) ) < 1 ) {
                LOG.debug( "Updating the details of the existing value {}. New details: {}", time, details );
                timeValuesWithConf.put( time, details );
            }
        } else {
            LOG.trace( "Inserting the new value {} with the corresponding details {}", time, details );
            if ( details.get( CONFIDENCE_KEY ) == null ) {
                details.put( CONFIDENCE_KEY, 0.0 );
            }
            if ( details.get( OPERATION_KEY ) == null ) {
                details.put( OPERATION_KEY, "" );
            }
            timeValuesWithConf.put( time, details );
        }
    }

}
