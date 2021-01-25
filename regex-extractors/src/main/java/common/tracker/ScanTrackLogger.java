package common.tracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ScanTrackLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanTrackLogger.class);


    /**
     * Logs the entry and exit of scan request in the application.
     *
     * @param scanId is the scanId of the request.
     * @param status provides the status of the request.
     */
    public static void logScanStatus(String scanId, String status, String component) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("events {} {} {} {}", System.currentTimeMillis(), scanId, component, status);
        }
    }
}