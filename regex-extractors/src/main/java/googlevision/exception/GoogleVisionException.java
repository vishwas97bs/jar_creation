package googlevision.exception;

public class GoogleVisionException extends OcrExtractionException
{

    public GoogleVisionException( String message, Throwable cause )
    {
        super( message, cause );
    }


    public GoogleVisionException( String message )
    {
        super( message );
    }

}
