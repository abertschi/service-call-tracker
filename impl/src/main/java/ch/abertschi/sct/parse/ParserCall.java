package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.NodeUtils;

/**
 * Created by abertschi on 11/05/16.
 */
public class ParserCall
{
    private ParserRequest request = new ParserRequest();

    private ParserResponse response = new ParserResponse();

    public static ParserCall createWithRawObjects(Object request, Object response)
    {
        ParserCall call = new ParserCall();
        call.request = new ParserRequest()
                .setPayloadRaw(NodeUtils.toRawXml(request))
                .setPayloadNode(NodeUtils.createNodeFromObject(request));
        call.response = new ParserResponse()
                .setPayloadRaw(NodeUtils.toRawXml(response))
                .setPayloadNode(NodeUtils.createNodeFromObject(response));
        return call;
    }

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
