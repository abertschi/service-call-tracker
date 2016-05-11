package ch.abertschi.sct.newp.parse;

import com.github.underscore.$;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

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


    private StorageCallRequest parseRequest(Element element)
    {
        XMLOutputter output = new XMLOutputter();

        Element request = element.getChild("request");
        Element payload = request.getChild("payload");
        Node payloadNode = null;

        if (!$.isNull(payload))
        {
            payloadNode = NodeUtils.parseDomToNode(payload);
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
            payloadNode = NodeUtils.parseDomToNode(payload);
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
