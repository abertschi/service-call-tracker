package ch.abertschi.sct.util;


import ch.abertschi.sct.api.SctException;

public class ResultNotFoundException extends SctException
{
    public ResultNotFoundException(Exception e)
    {
        super(e);
    }

    public ResultNotFoundException(String msg)
    {
        super(msg, null);
    }
}
