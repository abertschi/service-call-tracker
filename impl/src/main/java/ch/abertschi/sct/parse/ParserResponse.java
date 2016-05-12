package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.Node;

/**
 * Created by abertschi on 11/05/16.
 */
public class ParserResponse
{
    private String payloadRaw;

    private Node payloadNode;

    private String payloadType;

    private String script;

    public Node getPayloadNode()
    {
        return payloadNode;
    }

    public ParserResponse setPayloadNode(Node payloadNode)
    {
        this.payloadNode = payloadNode;
        return this;
    }

    public String getPayloadRaw()
    {
        return payloadRaw;
    }

    public ParserResponse setPayloadRaw(String payloadRaw)
    {
        this.payloadRaw = payloadRaw;
        return this;
    }

    public String getPayloadType()
    {
        return payloadType;
    }

    public ParserResponse setPayloadType(String payloadType)
    {
        this.payloadType = payloadType;
        return this;
    }

    public String getScript()
    {
        return script;
    }

    public ParserResponse setScript(String script)
    {
        this.script = script;
        return this;
    }

    public String getStacktrace()
    {
        return stacktrace;
    }

    public ParserResponse setStacktrace(String stacktrace)
    {
        this.stacktrace = stacktrace;
        return this;
    }

    private String stacktrace;

}
