package ch.abertschi.sct.serial;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("call")
public class Call
{
    private Request request;

    private Response response;

    public Call()
    {
        this.request = new Request();
        this.response = new Response();
    }
}
