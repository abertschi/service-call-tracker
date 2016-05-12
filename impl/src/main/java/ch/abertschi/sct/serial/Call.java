package ch.abertschi.sct.serial;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("call")
public class Call
{
    private Request request;

    public Request getRequest()
    {
        return request;
    }

    public Call setRequest(Request request)
    {
        this.request = request;
        return this;
    }

    public Response getResponse()
    {
        return response;
    }

    public Call setResponse(Response response)
    {
        this.response = response;
        return this;
    }

    private Response response;

    public Call()
    {
        this.request = new Request();
        this.response = new Response();
    }
}
