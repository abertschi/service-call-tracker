package ch.abertschi.sct.parse;

/**
 * Created by abertschi on 11/05/16.
 */
public class ParserCall
{
    private ParserCallRequest request = new ParserCallRequest();

    private ParserCallResponse response = new ParserCallResponse();

    public ParserCallRequest getRequest()
    {
        return request;
    }

    public ParserCall setRequest(ParserCallRequest request)
    {
        this.request = request;
        return this;
    }

    public ParserCallResponse getResponse()
    {
        return response;
    }

    public ParserCall setResponse(ParserCallResponse response)
    {
        this.response = response;
        return this;
    }
}
