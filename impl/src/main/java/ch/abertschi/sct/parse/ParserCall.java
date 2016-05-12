package ch.abertschi.sct.parse;

/**
 * Created by abertschi on 11/05/16.
 */
public class ParserCall
{
    private ParserRequest request = new ParserRequest();

    private ParserResponse response = new ParserResponse();

    public ParserRequest getRequest()
    {
        return request;
    }

    public ParserCall setRequest(ParserRequest request)
    {
        this.request = request;
        return this;
    }

    public ParserResponse getResponse()
    {
        return response;
    }

    public ParserCall setResponse(ParserResponse response)
    {
        this.response = response;
        return this;
    }
}
