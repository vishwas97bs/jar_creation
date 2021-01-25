package common.tracker;

public enum EventEnum
{

    // first post request acknowledgement
    ACKNOWLEDGED( "acknowledged" ),

    // When the request is first obtained
    INITIATED( "initiated" ),

    // When a scan request is created
    NEW( "new" ),

    // When the image is uploaded to s3
    S3_UPLOADED( "uploaded_to_s3" ),

    // When the image reaches the topology and the processing starts
    PROCESSING( "processing" ),

    // When the scan request is saved into the database
    UPLOADED( "uploaded" ),

    // When request for text extraction is sent
    ENQUEUE_TO_OCR( "enqueue_to_ocr" ),

    // When GV request made
    GOOGLE_VISION_REQUEST( "google_vision_request" ),

    // When abbyy request is made
    ABBYY_REQUEST( "abbyy_request" ),

    // When GV request is obtained
    GOOGLE_VISION_RESPONSE( "google_vision_response" ),

    // When abbyy response is obtained
    ABBYY_RESPONSE( "abbyy_response" ),

    // When text is saved into db after text extraction
    OCRED( "ocred" ),

    // When document reaches the extraction topology
    RECEIVED_FOR_EXTRACTION( "received_for_extraction" ),

    // When a field extraction has ended
    FIELD_EXTRACTED( "field_extracted" ),

    // When data enrichment is enabled for a generic user
    ENRICHED_DATA_GENERIC( "enriched_data_generic" ),

    // When data enrichment is enabled for a customer
    ENRICHED_DATA_CUSTOMER( "enriched_data_customer" ),

    // When extracted data is saved
    SAVED_EXTRACTED_DATA( "saved_extracted_data" ),

    // When there is a failure 
    FAILED( "failed" ),

    // When it is a successful request
    EXTRACTED( "extracted" );


    private final String event;


    EventEnum( String event )
    {
        this.event = event;
    }


    public String getEvent()
    {
        return event;
    }
}
