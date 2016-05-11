package ch.abertschi.sct.newp;

import com.github.underscore.$;
import com.thoughtworks.xstream.XStream;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by abertschi on 11/05/16.
 */
public class StorageParser
{
    public StorageContext parseXml(String xml) throws JDOMException, IOException
    {
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(new StringReader(xml));
        Element rootElement = document.getRootElement();

        List<StorageCall> storageCalls = new LinkedList<>();
        rootElement.getChild("calls").getChildren("call").forEach(callElement -> {

            StorageCall call = new StorageCall()
                    .setRequest(parseRequest(callElement))
                    .setResponse(parseResponse(callElement));

            //System.out.println(new XStream().toXML(call));

            storageCalls.add(call);
        });

        StorageContext context = new StorageContext();
        context.setCalls(storageCalls);

        return context;
    }

    private Node parseNodeTree(Element element, Node parent)
    {
        Node node = new Node();
        node.setName(element.getName());

        if (!$.isNull(parent))
        {
            parent.getChildren().add(node);
        }

        List<Node> nodeChildren = new LinkedList<>();
        List<Element> children = element.getChildren();

        if ($.isEmpty(children))
        {
            node.setIsContainer(false);
            node.setValue(element.getValue());
        }
        else
        {
            node.setIsContainer(true);
            for (Element child : children)
            {
                nodeChildren.add(parseNodeTree(child, node));
            }
        }
        node.setChildren(nodeChildren);
        return node;
    }


    private StorageCallRequest parseRequest(Element element)
    {
        XMLOutputter output = new XMLOutputter();

        Element request = element.getChild("request");
        Element payload = request.getChild("payload");
        Node payloadNode = null;

        if (!$.isNull(payload))
        {
            payloadNode = parseNodeTree(payload, null);
        }

        return new StorageCallRequest()
                .setPayloadRaw(output.outputString(payload))
                .setPayloadType(payload.getAttributeValue("class"))
                .setPayloadNode(payloadNode);

    }

    private StorageCallResponse parseResponse(Element element)
    {
        XMLOutputter output = new XMLOutputter();

        Element response = element.getChild("response");
        Element payload = response.getChild("payload");
        Element stacktrace = response.getChild("stacktrace");
        Element script = response.getChild("script");

        Node payloadNode = null;

        if (!$.isNull(payload))
        {
            payloadNode = parseNodeTree(payload, null);
        }

        String stacktraceValue = stacktrace != null ? stacktrace.getText() : null;
        String scriptValue = script != null ? script.getText() : null;
        String typeValue = payload.getAttributeValue("class");

        return new StorageCallResponse()
                .setPayloadRaw(output.outputString(payload))
                .setPayloadType(typeValue)
                .setScript(scriptValue)
                .setStacktrace(stacktraceValue)
                .setPayloadNode(payloadNode);
    }
}
