package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class GimletCustomer {
    @org.mongodb.morphia.annotations.Id
    @Id
    private String customerId;
    private Date createdDateTime;
    private Date validTill;
    private String type;
    private long freeTrialsLeft;
    private String customerMailId;
    private String logo;
    private boolean allowConcurrentLogin;
    private long sessionTimeOut = (long) 10 * 1000 * 60;// 10 minutes by default
    private int maxConcurrentLogin;
    private Map<String, String> configuration;
    private List<String> dataEnrichmentClasses;

    // making customer profile as list to cover case when one customer wants
    // more then one profile from extraction that case profile will come as a
    // part of scan request
    private List<String> profileList;


    public GimletCustomer()
    {}


    @SuppressWarnings ( "unchecked")
    public GimletCustomer( Map<String, Object> gimCustomerObj )
    {
        customerId = (String) gimCustomerObj.get( "customerId" );
        type = (String) gimCustomerObj.get( "type" );
        configuration = (Map<String, String>) gimCustomerObj.get( "configuration" );
        dataEnrichmentClasses = (List<String>) gimCustomerObj.get( "dataEnrichmentClasses" );
        profileList = (List<String>) gimCustomerObj.get( "profileList" );
    }


    public Map<String, Object> toMap()
    {
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        return new ObjectMapper().convertValue( this, typeRef );
    }


    public String getCustomerId()
    {
        return customerId;
    }


    public void setCustomerId( String customerId )
    {
        this.customerId = customerId;
    }


    public Date getCreatedDateTime()
    {
        return createdDateTime;
    }


    public void setCreatedDateTime( Date createdDateTime )
    {
        this.createdDateTime = createdDateTime;
    }


    public Date getValidTill()
    {
        return validTill;
    }


    public void setValidTill( Date validTill )
    {
        this.validTill = validTill;
    }


    public String getType()
    {
        return type;
    }


    public void setType( String type )
    {
        this.type = type;
    }


    public long getFreeTrialsLeft()
    {
        return freeTrialsLeft;
    }


    public void setFreeTrialsLeft( long freeTrialsLeft )
    {
        this.freeTrialsLeft = freeTrialsLeft;
    }


    public String getCustomerMailId()
    {
        return customerMailId;
    }


    public void setCustomerMailId( String customerMailId )
    {
        this.customerMailId = customerMailId;
    }


    public String getLogo()
    {
        return logo;
    }


    public void setLogo( String logo )
    {
        this.logo = logo;
    }


    public boolean isAllowConcurrentLogin()
    {
        return allowConcurrentLogin;
    }


    public void setAllowConcurrentLogin( boolean allowConcurrentLogin )
    {
        this.allowConcurrentLogin = allowConcurrentLogin;
    }


    public long getSessionTimeOut()
    {
        return sessionTimeOut;
    }


    public void setSessionTimeOut( long sessionTimeOut )
    {
        this.sessionTimeOut = sessionTimeOut;
    }


    public int getMaxConcurrentLogin()
    {
        return maxConcurrentLogin;
    }


    public void setMaxConcurrentLogin( int maxConcurrentLogin )
    {
        this.maxConcurrentLogin = maxConcurrentLogin;
    }


    public Map<String, String> getConfiguration()
    {
        return configuration;
    }


    public void setConfiguration( Map<String, String> configuration )
    {
        this.configuration = configuration;
    }


    public List<String> getProfileList()
    {
        return profileList;
    }


    public void setProfileList( List<String> profileList )
    {
        this.profileList = profileList;
    }


    public List<String> getDataEnrichmentClasses()
    {
        return dataEnrichmentClasses;
    }


    public void setDataEnrichmentClasses( List<String> dataEnrichmentClass )
    {
        dataEnrichmentClasses = dataEnrichmentClass;
    }

}
