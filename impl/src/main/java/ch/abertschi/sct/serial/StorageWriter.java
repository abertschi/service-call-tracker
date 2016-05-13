package ch.abertschi.sct.serial;

import ch.abertschi.sct.node.Node;
import ch.abertschi.sct.node.NodeUtils;
import ch.abertschi.sct.parse.*;
import com.github.underscore.$;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by abertschi on 13/05/16.
 */
public class StorageWriter
{
    private File target;

    private static final XStream XSTREAM = XStreamProvider.createXStream();

    public StorageWriter(File target)
    {
        this.target = target;
    }

    public void append(Call call)
    {
        List<Call> calls = new ArrayList<>();
        calls.add(call);
        append(calls);
    }

    public void append(List<Call> calls)
    {
        ParserContext context = getParserContext();
        context.getCalls().stream()
                .map(call -> ParserCall.createWithRawObjects(call.getRequest(), call.getResponse()))
                .forEach(call -> context.getCalls().add(call));
        persist(toStorage(context));
    }

    public Storage toStorage(ParserContext context)
    {
        List<Call> calls = context.getCalls()
                .stream().map(call -> toCall(call))
                .collect(Collectors.toList());
        return new Storage().setCalls(calls);
    }

    private Call toCall(ParserCall parserCall)
    {
        Request req = new Request()
                .setPayload(NodeUtils.createObjectWithNode(parserCall.getRequest().getPayloadNode()));
        Response res = new Response()
                .setPayload(NodeUtils.createObjectWithNode(parserCall.getResponse().getPayloadNode()))
                .setScript(toCdata(parserCall.getResponse().getScript()))
                .setStacktrace(toCdata(parserCall.getResponse().getStacktrace()));
        return new Call()
                .setRequest(req)
                .setResponse(res);
    }

    private ParserContext getParserContext()
    {
        ParserContext context;
        if (!target.exists())
        {
            context = new ParserContext();
        }
        else
        {
            XmlParser parser = new XmlParser();
            context = parser.parse(target);
        }
        return context;
    }

    private void persist(Storage storage)
    {
        FileOutputStream out = null;
        try
        {
            createFileIfNotExists(this.target);
            out = new FileOutputStream(this.target);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        XSTREAM.marshal(storage, new PrettyPrintWriter(new OutputStreamWriter(out)));
    }

    private void createFileIfNotExists(File file)
    {
        if (!file.exists())
        {
            file.getParentFile().mkdirs();
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private String toCdata(String input)
    {
        if (!$.isNull(input) && !input.trim().startsWith("<![CDATA]"))
        {
            StringBuilder builder = new StringBuilder();
            builder.append("<![CDATA[");
            builder.append(input);
            builder.append("]]>");
            return builder.toString();
        }
        else
        {
            return input;
        }
    }
}
