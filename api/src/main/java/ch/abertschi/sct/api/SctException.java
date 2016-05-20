package ch.abertschi.sct.api;

/**
 * Created by abertschi on 20/05/16.
 */
public class SctException extends RuntimeException
{
    public SctException(Exception e)
    {
        super(e);
    }

    public SctException(String msg, Exception e)
    {
        super(msg, e);
    }
}
