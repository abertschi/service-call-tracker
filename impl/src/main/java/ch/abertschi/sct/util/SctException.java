package ch.abertschi.sct.util;


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
