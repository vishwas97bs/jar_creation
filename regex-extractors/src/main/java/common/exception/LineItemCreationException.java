package common.exception;

public class LineItemCreationException extends RuntimeException{
    public LineItemCreationException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
