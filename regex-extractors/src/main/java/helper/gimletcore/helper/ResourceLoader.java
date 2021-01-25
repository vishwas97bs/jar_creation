package helper.gimletcore.helper;

import helper.gimletcore.constants.OcrWebConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class ResourceLoader implements ApplicationContextAware {
    private ApplicationContext context;


    @Override
    public void setApplicationContext( ApplicationContext context )
    {
        this.context = context;
    }


    public <T> T loadBean( Class<T> type )
    {
        return context.getBean( type );
    }


    public String getWebProperty( String key )
    {
        return getWebProperties().getProperty( key );
    }


    public Properties getWebProperties()
    {
        return loadPropertiesFromResource( new ClassPathResource( OcrWebConstants.WEB_PROP_FILE_NAME ) );
    }


    public String getCoreProperty( String key )
    {
        return getCoreProperties().getProperty( key );
    }


    public Properties getCoreProperties()
    {
        return loadPropertiesFromResource( new ClassPathResource( OcrWebConstants.CORE_PROP_FILE_NAME ) );
    }


    public String getKafkaProducerProperty( String key )
    {
        return getKafkaProducerProperties().getProperty( key );
    }


    public Properties getKafkaProducerProperties()
    {
        return loadPropertiesFromResource( new ClassPathResource( OcrWebConstants.KAFKA_PRODUCER_PROP_FILE_NAME ) );
    }


    public String getKafkaConsumerProperty( String key )
    {
        return getKafkaConsumerProperties().getProperty( key );
    }


    public Properties getKafkaConsumerProperties()
    {
        return loadPropertiesFromResource( new ClassPathResource( OcrWebConstants.KAFKA_CONSUMER_PROP_FILE_NAME ) );
    }


    public Properties getProperties( String propertyFileName )
    {
        return loadPropertiesFromResource( new ClassPathResource( "/" + propertyFileName ) );
    }


    private Properties loadPropertiesFromResource( Resource resource )
    {
        try {
            return PropertiesLoaderUtils.loadProperties( resource );
        } catch ( IOException e ) {
            throw new IllegalStateException( "Could not load properties from resource", e );
        }
    }

}
