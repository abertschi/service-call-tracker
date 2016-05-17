package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.NodeUtils;
import ch.abertschi.sct.serial.*;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by abertschi on 13/05/16.
 */
public class StorageWriter
{
    private static final Logger LOG = LoggerFactory.getLogger(StorageWriter.class);

    private File target;

    private static final XStream XSTREAM = XStreamProvider.createXStream();

    private ParserContext parserContext;

    public StorageWriter(File target)
    {
        this.target = target;
    }

    public void dump(Call call)
    {
        FileOutputStream out;
        try
        {
            createFileIfNotExists(this.target);
            out = new FileOutputStream(this.target, true);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        XSTREAM.marshal(call, new XStreamProvider.MyPrettyWriter(new OutputStreamWriter(out)));
        logAsXml(call);
    }

    public void write(Call call)
    {
        List<Call> calls = new ArrayList<>();
        calls.add(call);
        write(calls);
    }

    public void write(List<Call> calls)
    {
        ParserContext context = getParserContext();
        calls.stream()
                .map(call -> ParserCall.createWithRawObjects(call.getRequest().getPayload(), call.getResponse().getPayload()))
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
                .setScript(parserCall.getResponse().getScript())
                .setStacktrace(parserCall.getResponse().getStacktrace());
        return new Call()
                .setRequest(req)
                .setResponse(res);
    }

    private ParserContext getParserContext()
    {
        if (this.parserContext == null)
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
            this.parserContext = context;
        }
        return this.parserContext;
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

        XSTREAM.marshal(storage, new XStreamProvider.MyPrettyWriter(new OutputStreamWriter(out)));
        logAsXml(storage);
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

    private void logAsXml(Object toLog)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug(XSTREAM.toXML(toLog));
        }
    }
}
