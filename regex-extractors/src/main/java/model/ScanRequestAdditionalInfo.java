package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dheeraj on 10/8/17.
 * Entity to pass additional information for version2
 */
public class ScanRequestAdditionalInfo implements Serializable
{
    private Map<String, Boolean> fieldsToProcess = new HashMap<>();
    private String hashId;
    private List<String> additionalFields = new ArrayList<>();;
    private List<String> additionalLineFields = new ArrayList<>();
    private List<String> additionalStoreDetails = new ArrayList<>();
    private Boolean enableMerchantEnrichment;
    private Boolean enableMerchantLookup;
    private Boolean getLines;
    private Boolean text;
    private Boolean willWait;
    private Boolean signedValues;
    private Boolean enableLogoDetection;
    private String callBackURL;
    private String internalCallBackURL;
    private boolean getCoordinates;
    private boolean absConfidence;
    private boolean requiredDuplicate;
    private List<String> parentScanIds;
    private boolean fixedHeaders;


    public ScanRequestAdditionalInfo()
    {

    }


    public ScanRequestAdditionalInfo( Map<String, Boolean> fieldsToProcess, List<String> additionalFields,List<String> additionalStoreDetails,
                                      Boolean enableMerchantEnrichment, Boolean enableMerchantLookup, Boolean getLines,
                                      Boolean text, Boolean willWait, Boolean signedValues, Boolean fixedHeaders )
    {
        this.fieldsToProcess = fieldsToProcess;
        this.additionalFields = additionalFields;
        this.additionalStoreDetails = additionalStoreDetails;
        this.enableMerchantEnrichment = enableMerchantEnrichment != null ? enableMerchantEnrichment : false;
        this.enableMerchantLookup = enableMerchantLookup != null ? enableMerchantLookup : true;
        this.getLines = getLines != null ? getLines : false;
        this.text = text != null ? text : false;
        this.willWait = willWait != null ? willWait : false;
        this.fixedHeaders = fixedHeaders != null ? fixedHeaders : false;
        this.signedValues = signedValues != null ? signedValues : false;
    }


    public Map<String, Boolean> getFieldsToProcess()
    {
        if ( fieldsToProcess != null ) {
            return fieldsToProcess;
        }
        return new HashMap<>();
    }


    public String getHashId()
    {
        return hashId;
    }


    public void setHashId( String hashId )
    {
        this.hashId = hashId;
    }


    public void setFieldsToProcess( Map<String, Boolean> fieldsToProcess )
    {
        this.fieldsToProcess = fieldsToProcess;
    }


    public List<String> getAdditionalFields()
    {
        if ( additionalFields != null ) {
            return additionalFields;
        }
        return new ArrayList<>();
    }


    public void setAdditionalFields( List<String> additionalFields )
    {
        this.additionalFields = additionalFields;
    }


    public List<String> getAdditionalStoreDetails()
    {
        if ( additionalStoreDetails != null ) {
            return additionalStoreDetails;
        }
        return new ArrayList<>();
    }


    public void setAdditionalStoreDetails( List<String> additionalStoreDetails )
    {
        this.additionalStoreDetails = additionalStoreDetails;
    }


    public Boolean getEnableMerchantEnrichment()
    {
        return enableMerchantEnrichment;
    }


    public void setEnableMerchantEnrichment( Boolean enableMerchantEnrichment )
    {
        if ( enableMerchantEnrichment == null )
            this.enableMerchantEnrichment = false;
        else
            this.enableMerchantEnrichment = enableMerchantEnrichment;
    }


    public List<String> getAdditionalLineFields()
    {
        if ( additionalLineFields != null ) {
            return additionalLineFields;
        }
        return new ArrayList<>();
    }


    public void setAdditionalLineFields( List<String> additionalLineFields )
    {
        this.additionalLineFields = additionalLineFields;
    }


    public Boolean getEnableMerchantLookup()
    {
        return enableMerchantLookup;
    }


    public void setEnableMerchantLookup( Boolean enableMerchantLookup )
    {
        if ( enableMerchantLookup == null )
            this.enableMerchantLookup = true;
        else
            this.enableMerchantLookup = enableMerchantLookup;
    }


    public Boolean getGetLines()
    {
        return getLines;
    }


    public void setGetLines( Boolean getLines )
    {
        if ( getLines == null )
            this.getLines = false;
        else
            this.getLines = getLines;
    }


    public Boolean getText()
    {
        return text;
    }


    public void setText( Boolean text )
    {
        if ( text == null )
            this.text = false;
        else
            this.text = text;
    }


    public Boolean getWillWait()
    {
        return willWait;
    }


    public void setWillWait( Boolean willWait )
    {
        if ( willWait == null )
            this.willWait = false;
        else
            this.willWait = willWait;
    }


    public Boolean getSignedValues()
    {
        return signedValues;
    }


    public void setSignedValues( Boolean signedValues )
    {
        if(signedValues == null)
            this.signedValues = false;
        else
            this.signedValues = signedValues;
    }


    public Boolean getEnableLogoDetection()
    {
        return enableLogoDetection;
    }


    public void setEnableLogoDetection( Boolean enableLogoDetection )
    {
        if ( enableLogoDetection == null )
            this.enableLogoDetection = true;
        else
            this.enableLogoDetection = enableLogoDetection;
    }


    public String getCallBackURL()
    {
        return callBackURL;
    }


    public void setCallBackURL( String callBackURL )
    {
        this.callBackURL = callBackURL;
    }


    public String getInternalCallBackURL()
    {
        return internalCallBackURL;
    }


    public void setInternalCallBackURL( String internalCallBackURL )
    {
        this.internalCallBackURL = internalCallBackURL;
    }


    public boolean getGetCoordinates()
    {
        return getCoordinates;
    }


    public void setGetCoordinates( boolean getCoordinates )
    {
        this.getCoordinates = getCoordinates;
    }


    public boolean getAbsConfidence()
    {
        return absConfidence;
    }


    public void setAbsConfidence( boolean getAbsConfidence )
    {
        this.absConfidence = getAbsConfidence;
    }


    public boolean isRequiredDuplicate()
    {
        return requiredDuplicate;
    }


    public void setRequiredDuplicate( boolean requiredDuplicate )
    {
        this.requiredDuplicate = requiredDuplicate;
    }


    public List<String> getParentScanIds()
    {
        return parentScanIds;
    }


    public void setParentScanIds( List<String> parentScanIds )
    {
        this.parentScanIds = parentScanIds;
    }


    public boolean getFixedHeaders()
    {
        return fixedHeaders;
    }


    public void setFixedHeaders( Boolean fixedHeaders )
    {
        if ( fixedHeaders == null )
            this.fixedHeaders = false;
        else
            this.fixedHeaders = fixedHeaders;
    }


    @Override
    public String toString()
    {
        return "ScanRequestAdditionalInfo{" + "fieldsToProcess=" + fieldsToProcess + ", additionalFields=" + additionalFields
                + ", enableMerchantEnrichment=" + enableMerchantEnrichment + ", enableMerchantLookup=" + enableMerchantLookup
                + ", getLines=" + getLines + ", text=" + text + ", willWait=" + willWait + ", signedValues=" + signedValues + ", getCoordinates=" + getCoordinates
                + ", callBAckURL=" + callBackURL + ", absConfidence=" + absConfidence + ", fixedHeaders=" + fixedHeaders+'}';
    }

}
