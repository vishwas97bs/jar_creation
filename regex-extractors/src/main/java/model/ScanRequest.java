package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.ScanRequestStatus;
import org.mongodb.morphia.annotations.Entity;
import org.springframework.data.annotation.Id;


@Entity("scanRequest")
public class ScanRequest implements Serializable
{

    @org.mongodb.morphia.annotations.Id
    @Id
    private String scanRequestId;
    private String userId;
    private String fileType;
    private String documentType;
    private String locale;
    private String s3ImagePath;
    private Date uploadDateTime;
    private Date lastModifyDateTime;
    private String status;
    private String category;
    private Map<String, Object> categoryDetails;
    private String possibleLanguages;
    private Map<String, Object> receiptData;
    private Map<String, Object> tabularData;
    private Map<String, Object> localeEntity;
    private Map<String, Object> updatedStructure;
    private Map<String, Object> scanRequestInputParameters;
    private double ocrConfidence;

    private String profile;

    private ScanRequestAdditionalInfo scanRequestAdditionalInfo;
    private int pageCount;
    @JsonIgnore private String process;

    public ScanRequest( String userId, String locale, String documentType, String fileType )
    {
        UUID scanReqId = UUID.randomUUID();
        this.scanRequestId = scanReqId.toString();
        this.userId = userId;
        this.uploadDateTime = new Date();
        this.lastModifyDateTime = new Date();
        this.status = ScanRequestStatus.NEW.getStatus();
        this.fileType = fileType;
        /*
         * TODO: Verify where the documentType should be obtained from.
         * Currently assigned value is a temporary fix.
         */
        this.documentType = documentType;
        this.fileType = fileType;
        this.locale = locale;
        this.ocrConfidence = Double.NaN;
        this.profile = "receipt";
    }


    public ScanRequest( String userId, String locale, String documentType, String fileType,
        ScanRequestAdditionalInfo scanRequestAdditionalInfo )
    {
        UUID scanReqId = UUID.randomUUID();
        this.scanRequestId = scanReqId.toString();
        this.userId = userId;
        this.uploadDateTime = new Date();
        this.lastModifyDateTime = new Date();
        this.status = ScanRequestStatus.NEW.getStatus();
        this.fileType = fileType;
        /*
         * TODO: Verify where the documentType should be obtained from.
         * Currently assigned value is a temporary fix.
         */
        this.documentType = documentType;
        this.fileType = fileType;
        this.locale = locale;
        this.ocrConfidence = Double.NaN;
        this.scanRequestAdditionalInfo = scanRequestAdditionalInfo;
        this.profile = "receipt";
    }


    public ScanRequest( String userId, String locale, String documentType, String fileType,
        ScanRequestAdditionalInfo scanRequestAdditionalInfo, String scanId )
    {

        this.scanRequestId = scanId;
        this.userId = userId;
        this.uploadDateTime = new Date();
        this.lastModifyDateTime = new Date();
        this.status = ScanRequestStatus.NEW.getStatus();
        this.fileType = fileType;
        /*
         * TODO: Verify where the documentType should be obtained from.
         * Currently assigned value is a temporary fix.
         */
        this.documentType = documentType;
        this.fileType = fileType;
        this.locale = locale;
        this.ocrConfidence = Double.NaN;
        this.scanRequestAdditionalInfo = scanRequestAdditionalInfo;
        this.profile = "receipt";
    }


    public ScanRequest( Map<String, Object> data )
    {
        // New scan request id logic user id + time stamp seperated by
        // underscore(_)
        this.scanRequestId = (String) data.get( "scanRequestId" );
        this.userId = (String) data.get( "userId" );

        this.s3ImagePath = (String) data.get( "s3ImagePath" );
        /*
         * The following boxing & autoboxing is done to solve the following issue:
         *
         * data.get("uploadDataTime").toString() returns 12 digit time epoch i.e., Ex: "123456789123"
         * Double.parseDouble("123456789123") returns 1.23*10^11 approx.
         *
         * if this value is given to new Date() then it throws an error, to fix that, the following is done
         * */
        Double uploadDateTimeDbl = Double.parseDouble( data.get( "uploadDateTime" ).toString() );
        this.uploadDateTime = new Date( uploadDateTimeDbl.longValue() );
        Double lastModifyDateTimeDbl = Double.parseDouble( data.get( "lastModifyDateTime" ).toString() );
        this.lastModifyDateTime = new Date( lastModifyDateTimeDbl.longValue() );
        this.status = (String) data.get( "status" );
        this.fileType = (String) data.get( "fileType" );
        /*
         * TODO: Verify where the documentType should be obtained from.
         * Currently assigned value is a temporary fix.
         */
        this.documentType = "receipt";
        this.locale = (String) data.get( "locale" );

        if ( data.containsKey( "category" ) && (Map<String, Object>) data.get( "category" ) != null ) {
            this.category = (String) data.get( "category" );
        }

        if ( data.containsKey( "categoryDetails" ) && (Map<String, Object>) data.get( "categoryDetails" ) != null ) {
            this.setCategoryDetails( (Map<String, Object>) data.get( "categoryDetails" ) );
        }


        this.possibleLanguages = (String) data.get( "possibleLanguages" );

        if ( data.containsKey( "localeEntity" ) ) {
            this.localeEntity = (Map<String, Object>) data.get( "localeEntity" );
            //if(localeEntity!=null && localeEntity.containsKey("localeValue"))
            //this.locale = (String) localeEntity.get("localeValue");
        }
        if ( data.containsKey( "ocrConfidence" ) ) {
            try {
                this.ocrConfidence = (double) data.get( "ocrConfidence" );
            } catch ( ClassCastException cce ) {
                this.ocrConfidence = Double.NaN;
            }
        } else {
            this.ocrConfidence = Double.NaN;
        }
        ObjectMapper mapper = new ObjectMapper();
        if ( data.containsKey( "scanRequestAdditionalInfo" ) ) {
            this.scanRequestAdditionalInfo = mapper
                .convertValue( data.get( "scanRequestAdditionalInfo" ), ScanRequestAdditionalInfo.class );
        } else {
            this.scanRequestAdditionalInfo = null;
        }
        if ( data.containsKey( "profile" ) )
            this.profile = (String) data.get( "profile" );

        if ( data.containsKey( "pageCount" ) )
            this.pageCount = ( (Number) data.get( "pageCount" ) ).intValue();
        if ( data.containsKey( "scanRequestInputParameters" ) ) {

            this.scanRequestInputParameters = (Map<String, Object>) data.get( "scanRequestInputParameters" );

        }
    }


    public ScanRequest()
    {
        //Empty Constructor
    }


    public String getScanRequestId()
    {
        return scanRequestId;
    }


    public Map<String, Object> getScanRequestInputParameters()
    {
        return scanRequestInputParameters;
    }


    public void setScanRequestInputParameters( Map<String, Object> scanRequestInputParameters )
    {
        this.scanRequestInputParameters = scanRequestInputParameters;
    }


    public void setScanRequestId( String scanRequestId )
    {
        this.scanRequestId = scanRequestId;
    }


    public String getUserId()
    {
        return userId;
    }


    public void setUserId( String userId )
    {
        this.userId = userId;
    }


    public String getDocumentType()
    {
        return documentType;
    }


    public void setDocumentType( String documentType )
    {
        this.documentType = documentType;
    }


    public String getFileType()
    {
        return fileType;
    }


    public void setFileType( String fileType )
    {
        this.fileType = fileType;
    }


    public String getLocale()
    {
        return locale;
    }


    public void setLocale( String locale )
    {
        this.locale = locale;
    }


    public String getS3ImagePath()
    {
        return s3ImagePath;
    }


    public void setS3ImagePath( String s3ImagePath )
    {
        this.s3ImagePath = s3ImagePath;
    }


    public Date getUploadDateTime()
    {
        return uploadDateTime;
    }


    public void setUploadDateTime( Date uploadDateTime )
    {
        this.uploadDateTime = uploadDateTime;
    }


    public Date getLastModifyDateTime()
    {
        return lastModifyDateTime;
    }


    public void setLastModifyDateTime( Date lastModifyDateTime )
    {
        this.lastModifyDateTime = lastModifyDateTime;
    }


    public String getStatus()
    {
        return status;
    }


    public void setStatus( String status )
    {
        this.status = status;
    }


    public Map<String, Object> getReceiptData()
    {
        return receiptData;
    }


    public void setReceiptData( Map<String, Object> receiptData )
    {
        this.receiptData = receiptData;
    }


    public Map<String, Object> getTabularData()
    {
        return tabularData;
    }


    public void setTabularData( Map<String, Object> tabularData )
    {
        this.tabularData = tabularData;
    }


    public String getCategory()
    {
        return category;
    }


    public void setCategory( String category )
    {
        this.category = category;
    }


    public String getPossibleLanguages()
    {
        return possibleLanguages;
    }


    public void setPossibleLanguages( String possibleLanguages )
    {
        this.possibleLanguages = possibleLanguages;
    }


    public Map<String, Object> getLocaleEntity()
    {
        return localeEntity;
    }


    public void setLocaleEntity( Map<String, Object> localeEntity )
    {
        this.localeEntity = localeEntity;
    }


    public Map<String, Object> getUpdatedStructure()
    {
        return updatedStructure;
    }


    public void setUpdatedStructure( Map<String, Object> updatedStructure )
    {
        this.updatedStructure = updatedStructure;
    }


    public double getOcrConfidence()
    {
        return ocrConfidence;
    }


    public void setOcrConfidence( double ocrConfidence )
    {
        this.ocrConfidence = ocrConfidence;
    }


    public ScanRequestAdditionalInfo getScanRequestAdditionalInfo()
    {
        return scanRequestAdditionalInfo;
    }


    public void setScanRequestAdditionalInfo( ScanRequestAdditionalInfo scanRequestAdditionalInfo )
    {
        this.scanRequestAdditionalInfo = scanRequestAdditionalInfo;
    }


    public String getProfile()
    {
        return profile;
    }


    public void setProfile( String profile )
    {
        this.profile = profile;
    }


    public int getPageCount()
    {
        return pageCount;
    }


    public void setPageCount( int pageCount )
    {
        this.pageCount = pageCount;
    }


    public Map<String, Object> getCategoryDetails()
    {
        return categoryDetails;
    }


    public void setCategoryDetails( Map<String, Object> categoryDetails )
    {
        this.categoryDetails = categoryDetails;
    }
    public String getProcess()
    {
        return process;
    }


    public void setProcess( String process )
    {
        this.process = process;
    }
}
