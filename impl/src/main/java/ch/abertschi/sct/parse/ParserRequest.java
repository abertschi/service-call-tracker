package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.Node;

/**
 * Created by abertschi on 11/05/16.
 */
public class ParserRequest
{
    private String payloadRaw;

    private Node payloadNode;

    private String payloadType;

    public Node getPayloadNode()
    {
        return payloadNode;
    }

    public ParserRequest setPayloadNode(Node payloadNode)
    {
        this.payloadNode = payloadNode;
        return this;
    }

    public String getPayloadRaw()
    {
        return payloadRaw;
    }

    public ParserRequest setPayloadRaw(String payloadRaw)
    {
        this.payloadRaw = payloadRaw;
        return this;
    }

    public String getPayloadType()
    {
        return payloadType;
    }

    public ParserRequest setPayloadType(String payloadType)
    {
        this.payloadType = payloadType;
        return this;
    }
}
