package ch.abertschi.sct.serial;

import ch.abertschi.sct.node.Node;
import ch.abertschi.sct.node.NodeUtils;
import ch.abertschi.sct.parse.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abertschi on 13/05/16.
 */
public class StorageWriter
{
    private File target;

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
        XmlParser parser = new XmlParser();
        ParserContext parserContext = parser.parse(this.target);
        calls.forEach(call -> {
            ParserCall parserCall = ParserCall.createWithRawObjects(call.getRequest(), call.getResponse());
            parserContext.getCalls().add(parserCall);
        });
        // todo: write

        Storage storage = new Storage();
//        parserContext.getCalls().stream().map(call -> new Call().set)
//            storage
//        });
    }

    public Call parserCallToStorageCall(ParserCall parserCall)
    {
        return null;
    }

    public void write(Storage storage)
    {

    }
}
