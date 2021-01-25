package common.helpers;

import common.PropertyFileHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestResourceProvider
{
    private static final String resourceHome;

    static {
        String mvnResourcePath = System.getProperty( "db.config.repo.path" );
        if( StringUtils.isBlank( mvnResourcePath ) ){
            resourceHome = PropertyFileHandler.getProperty( "config", "db.config.repo.path" ).trim();
        } else {
            resourceHome = mvnResourcePath.trim();
        }
    }

    private static String locateResource( String resource ) throws IOException
    {
        return Files.find( Paths.get( resourceHome ), 3, ( filePath, fileAttr ) -> fileAttr.isRegularFile() )
            .filter( path -> path.endsWith( resource ) ).findFirst().get().toFile().getAbsolutePath();
    }


    public static InputStream getDatabaseConfigurationAsStream( String resource ) throws IOException
    {
        return new FileInputStream( locateResource( resource ) );
    }
}
