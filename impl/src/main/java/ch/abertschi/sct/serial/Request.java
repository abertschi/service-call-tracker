package ch.abertschi.sct.serial;

/**
 * Created by abertschi on 12/05/16.
 */
public class Request
{
    private Object payload;

    public Object getPayload()
    {
        return payload;
    }

    public Request setPayload(Object payload)
    {
        this.payload = payload;
        return this;
    }
}
