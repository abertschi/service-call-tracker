package ch.abertschi.sct.newp.parse;

/**
 * Created by abertschi on 11/05/16.
 */
public class StorageCallRequest
{
    private String payloadRaw;

    private Node payloadNode;

    private String payloadType;

    public Node getPayloadNode()
    {
        return payloadNode;
    }

    public StorageCallRequest setPayloadNode(Node payloadNode)
    {
        this.payloadNode = payloadNode;
        return this;
    }

    public String getPayloadRaw()
    {
        return payloadRaw;
    }

    public StorageCallRequest setPayloadRaw(String payloadRaw)
    {
        this.payloadRaw = payloadRaw;
        return this;
    }

    public String getPayloadType()
    {
        return payloadType;
    }

    public StorageCallRequest setPayloadType(String payloadType)
    {
        this.payloadType = payloadType;
        return this;
    }
}
