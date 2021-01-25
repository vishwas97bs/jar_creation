package common.exception;

/**
 * Exception class for Property File related Operations
 */
public class PropertyException extends RuntimeException
{
    private static final long serialVersionUID = 1L;


    /**
     * Property Exception with message and throwable parameters
     * @param message exception message
     * @param cause throwable cause
     */
    public PropertyException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
