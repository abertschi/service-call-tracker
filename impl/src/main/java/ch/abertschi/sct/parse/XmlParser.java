package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.Node;
import ch.abertschi.sct.node.NodeUtils;
import com.github.underscore.$;
import com.thoughtworks.xstream.XStream;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(XmlParser.class);

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
        ParserContext context = new ParserContext();
        if ($.isNull(xml))
        {
            throw new IllegalArgumentException("Xml cant be null");
        }
        else if (!xml.trim().isEmpty())
        {
            Document document = NodeUtils.xmlToDocument(xml);
            Element rootElement = document.getRootElement();
            List<ParserCall> storageCalls = new LinkedList<>();

            Element calls = rootElement.getChild("calls");
            if (!$.isNull(calls))
            {
                calls.getChildren("call")
                        .stream()
                        .map(callElement -> new ParserCall()
                                .setRequest(parseRequest(callElement))
                                .setResponse(parseResponse(callElement)))
                        .forEach(call -> storageCalls.add(call));
            }

            context.setCalls(storageCalls);
        }
        if (LOG.isTraceEnabled())
        {
            LOG.trace("Parsing storage {}", XStreamProvider.GET.createPreConfiguredXStream().toXML(context));
        }
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
            payloadNode.setClassType(payload.getAttributeValue("class"));
        }
        return new ParserRequest()
                .setPayloadRaw(output.outputString(payload))
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
            String typeValue = payload.getAttributeValue("class");
            payloadNode.setClassType(typeValue);
        }
        String stacktraceValue = $.isNull(stacktrace) ? null : stacktrace.getText();
        String scriptValue = $.isNull(script) ? null : script.getText();
        String payloadRaw = $.isNull(payload) ? null : output.outputString(payload);

        return new ParserResponse()
                .setPayloadRaw(payloadRaw)
                .setScript(scriptValue)
                .setStacktrace(stacktraceValue)
                .setPayloadNode(payloadNode);
    }
}
