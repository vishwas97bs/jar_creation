package common.helpers;


import common.MongoConnector;

import java.io.IOException;
import java.util.List;


/**
 *  Utils class for intialising and cleaning databases for tests
 * @author Jaskaranjit
 */
public class TestHelper
{
    /**
     * private constructor
     */
    private TestHelper()
    {

    }


    /** Helper method for initialising databases from files
     * @param fileNames list of filenames to load data from
     * @param dbNames list of database names to save data in
     * @param collectionNames list of collection names to save data in
     * @throws IOException
     */
    public static void intialiseDatabases( List<String> fileNames, List<String> dbNames, List<String> collectionNames,
        LineParser lineParser ) throws IOException
    {
        // the size of all the three lists should be same and index mapping should be maintained
        if ( fileNames.size() == dbNames.size() && fileNames.size() == collectionNames.size() ) {
            for ( int count = 0; count < fileNames.size(); count++ ) {
//                MongoConnector.load( TestHelper.class.getClassLoader().getResourceAsStream( fileNames.get( count ) ),
//                    dbNames.get( count ), collectionNames.get( count ), lineParser );
            }
        }
    }


    /** Helper method for initialising databases from files. The size of three lists should be same and same indexes should form an onject
     * @param fileNames list of fileNames
     * @param dbNames list of database names
     * @param collectionNames list of collections names
     * @throws IOException
     */
    public static void intialiseDatabases( List<String> fileNames, List<String> dbNames, List<String> collectionNames )
        throws IOException
    {
//        intialiseDatabases( fileNames, dbNames, collectionNames, MongoConnector.DEFAULT_CONVERTER );
    }


    /** Helper class to drop test databases
     * @param dbNames list of databases
     * @param collectionNames list of collections
     */
    public static void cleanUpDatabases( List<String> dbNames, List<String> collectionNames )
    {
        // the size of both the lists should be same and index mapping should be maintained
        if ( dbNames.size() == collectionNames.size() ) {
            for ( int count = 0; count < dbNames.size(); count++ ) {
                MongoConnector.getDB( dbNames.get( count ) ).getCollection( collectionNames.get( count ) ).drop();
            }
        }
    }


    /**
     * Drops list of databases
     * @param databases list of databases be dropped
     */
    public static void dropDatabases( List<String> databases )
    {
        for ( String database : databases ) {
            MongoConnector.getDB( database ).dropDatabase();
        }
    }

}