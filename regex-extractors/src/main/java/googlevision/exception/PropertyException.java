package googlevision.exception;

public class PropertyException extends RuntimeException
{
    /**
     * Property Exception with message and throwable parameters
     *
     * @param message exception message
     * @param cause   throwable cause
     */
    public PropertyException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
