package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.Node;

/**
 * Created by abertschi on 11/05/16.
 */
public class ParserCallResponse
{
    private String payloadRaw;

    private Node payloadNode;

    private String payloadType;

    private String script;

    public Node getPayloadNode()
    {
        return payloadNode;
    }

    public ParserCallResponse setPayloadNode(Node payloadNode)
    {
        this.payloadNode = payloadNode;
        return this;
    }

    public String getPayloadRaw()
    {
        return payloadRaw;
    }

    public ParserCallResponse setPayloadRaw(String payloadRaw)
    {
        this.payloadRaw = payloadRaw;
        return this;
    }

    public String getPayloadType()
    {
        return payloadType;
    }

    public ParserCallResponse setPayloadType(String payloadType)
    {
        this.payloadType = payloadType;
        return this;
    }

    public String getScript()
    {
        return script;
    }

    public ParserCallResponse setScript(String script)
    {
        this.script = script;
        return this;
    }

    public String getStacktrace()
    {
        return stacktrace;
    }

    public ParserCallResponse setStacktrace(String stacktrace)
    {
        this.stacktrace = stacktrace;
        return this;
    }

    private String stacktrace;

}
