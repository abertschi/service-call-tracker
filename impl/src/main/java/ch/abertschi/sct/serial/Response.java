package ch.abertschi.sct.serial;

/**
 * Created by abertschi on 12/05/16.
 */
public class Response
{
    private Object payload;

    private String stacktrace;

    private String script;

    public String getScript()
    {
        return script;
    }

    public Response setScript(String script)
    {
        this.script = script;
        return this;
    }

    public String getStacktrace()
    {
        return stacktrace;
    }

    public Response setStacktrace(String stacktrace)
    {
        this.stacktrace = stacktrace;
        return this;
    }

    public Object getPayload()
    {
        return payload;
    }

    public Response setPayload(Object payload)
    {
        this.payload = payload;
        return this;
    }

}
