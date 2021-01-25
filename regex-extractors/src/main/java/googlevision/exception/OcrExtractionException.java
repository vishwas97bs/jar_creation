package googlevision.exception;

public class OcrExtractionException extends Exception
{

    public OcrExtractionException( String message, Throwable cause )
    {
        super( message, cause );
    }


    public OcrExtractionException( String message )
    {
        super( message );
    }
}
