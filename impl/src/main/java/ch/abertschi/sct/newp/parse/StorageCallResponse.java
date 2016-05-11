package ch.abertschi.sct.newp.parse;

/**
 * Created by abertschi on 11/05/16.
 */
public class StorageCallResponse
{
    private String payloadRaw;

    private Node payloadNode;

    private String payloadType;

    private String script;

    public Node getPayloadNode()
    {
        return payloadNode;
    }

    public StorageCallResponse setPayloadNode(Node payloadNode)
    {
        this.payloadNode = payloadNode;
        return this;
    }

    public String getPayloadRaw()
    {
        return payloadRaw;
    }

    public StorageCallResponse setPayloadRaw(String payloadRaw)
    {
        this.payloadRaw = payloadRaw;
        return this;
    }

    public String getPayloadType()
    {
        return payloadType;
    }

    public StorageCallResponse setPayloadType(String payloadType)
    {
        this.payloadType = payloadType;
        return this;
    }

    public String getScript()
    {
        return script;
    }

    public StorageCallResponse setScript(String script)
    {
        this.script = script;
        return this;
    }

    public String getStacktrace()
    {
        return stacktrace;
    }

    public StorageCallResponse setStacktrace(String stacktrace)
    {
        this.stacktrace = stacktrace;
        return this;
    }

    private String stacktrace;

}
