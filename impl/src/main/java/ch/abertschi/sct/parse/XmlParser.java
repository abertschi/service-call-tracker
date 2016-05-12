package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.Node;
import ch.abertschi.sct.node.NodeUtils;
import com.github.underscore.$;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
public class XmlParser
{
    public ParserContext parse(File file)
    {
        try
        {
            byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            return this.parse(new String(encoded));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public ParserContext parse(String xml)
    {
        Document document = NodeUtils.xmlToDocument(xml);
        Element rootElement = document.getRootElement();
        List<ParserCall> storageCalls = new LinkedList<>();

        rootElement.getChild("calls")
                .getChildren("call")
                .forEach(callElement -> {
                    ParserCall call = new ParserCall()
                            .setRequest(parseRequest(callElement))
                            .setResponse(parseResponse(callElement));

                    storageCalls.add(call);
                });

        ParserContext context = new ParserContext();
        context.setCalls(storageCalls);
        return context;
    }

    private ParserRequest parseRequest(Element element)
    {
        XMLOutputter output = new XMLOutputter();
        Element request = element.getChild("request");
        Element payload = request.getChild("payload");
        Node payloadNode = null;

        if (!$.isNull(payload))
        {
            payloadNode = NodeUtils.parseDomToNode(payload);
        }
        return new ParserRequest()
                .setPayloadRaw(output.outputString(payload))
                .setPayloadType(payload.getAttributeValue("class"))
                .setPayloadNode(payloadNode);
    }

    private ParserResponse parseResponse(Element element)
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
        String stacktraceValue = $.isNull(stacktrace) ? null : stacktrace.getText();
        String scriptValue = $.isNull(script) ? null : script.getText();
        String typeValue = payload.getAttributeValue("class");

        return new ParserResponse()
                .setPayloadRaw(output.outputString(payload))
                .setPayloadType(typeValue)
                .setScript(scriptValue)
                .setStacktrace(stacktraceValue)
                .setPayloadNode(payloadNode);
    }
}
