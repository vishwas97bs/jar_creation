package common;

public enum ScanRequestStatus {
    ACKNOWLEDGED( "ACKNOWLEDGED" ), INITIATED( "INITIATED" ), NEW( "NEW" ), UPLOADED( "UPLOADED" ), OCRED( "OCRED" ), EXTRACTED(
            "EXTRACTED" ), QUEUED( "QUEUED" ), FAILED( "FAILED" ), RERUNNING( "RERUNNING" );

    private String status;


    private ScanRequestStatus( String status )
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }
}
