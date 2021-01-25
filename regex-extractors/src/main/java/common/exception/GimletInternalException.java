package common.exception;

/**
 * Thrown when there is an underlying exception which should break the application
 *
 * @author Sriram
 */
public class GimletInternalException extends RuntimeException
{
    private static final long serialVersionUID = 1L;


    public GimletInternalException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
