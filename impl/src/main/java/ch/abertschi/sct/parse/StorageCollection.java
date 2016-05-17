package ch.abertschi.sct.parse;

import ch.abertschi.sct.api.Configuration;
import ch.abertschi.sct.invocation.InvocationContext;
import ch.abertschi.sct.serial.Call;
import ch.abertschi.sct.serial.Request;
import ch.abertschi.sct.serial.Response;
import ch.abertschi.sct.serial.StorageWriter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abertschi on 17/05/16.
 */
public class StorageCollection
{

    private static final String DEFAULT = "def";

    private Map<String, StorageParser> parserMap = new HashMap<>();
    private Map<String, StorageWriter> writerMap = new HashMap<>();

    private Configuration config;

    public StorageCollection(Configuration config)
    {
        this.config = config;
    }

    public Object get(Object request, InvocationContext invocation)
    {
        StorageParser parser;
        if (Configuration.INPUT_SOURCE.SINGLE_FILE == config.getReplayingSourceType())
        {
            parser = parserMap.get(DEFAULT);
            if (parser == null)
            {
                parser = new StorageParser(config.getReplayingSource());
                parserMap.put(DEFAULT, parser);
            }
        }
        else
        {
            throw new UnsupportedOperationException();
        }
        return parser.get(request);
    }

    public void add(Object request, Object response, InvocationContext invocation)
    {
        StorageWriter writer;
        if (config.getRecordingSourceType() == Configuration.INPUT_SOURCE.SINGLE_FILE)
        {
            writer = writerMap.get(DEFAULT);
            if (writer == null)
            {
                writer = new StorageWriter(config.getRecordingSource());
                writerMap.put(DEFAULT, writer);
            }
        }
        else
        {
            throw new UnsupportedOperationException();
        }

        Call call = new Call()
                .setRequest(new Request().setPayload(request))
                .setResponse(new Response().setPayload(response));
        if (config.getRecordingMode() == Configuration.RECORDING_MODE.DUMP_CALLS)
        {
            writer.dump(call);
        }
        else
        {
            writer.write(call);
        }
    }
}
