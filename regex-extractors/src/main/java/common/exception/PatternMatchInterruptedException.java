package common.exception;

public class PatternMatchInterruptedException extends RuntimeException
{
    public PatternMatchInterruptedException()
    {
        super();
    }


    public PatternMatchInterruptedException( String message )
    {
        super( message );
    }


    public PatternMatchInterruptedException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
