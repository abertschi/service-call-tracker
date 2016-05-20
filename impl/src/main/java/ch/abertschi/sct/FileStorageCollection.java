package ch.abertschi.sct;

import ch.abertschi.sct.api.Configuration;
import ch.abertschi.sct.api.InvocationContext;
import ch.abertschi.sct.parse.StorageParser;
import ch.abertschi.sct.parse.StorageWriter;
import ch.abertschi.sct.serial.Call;
import ch.abertschi.sct.serial.Request;
import ch.abertschi.sct.serial.Response;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abertschi on 17/05/16.
 */
public class FileStorageCollection
{
    private static final String DEFAULT = "DEFAULT_IMPL";

    private Map<String, StorageParser> parserMap = new HashMap<>();
    private Map<String, StorageWriter> writerMap = new HashMap<>();

    private Configuration config;

    public FileStorageCollection(Configuration config)
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
            String key = createKey(invocation);
            parser = parserMap.get(key);
            if (parser == null)
            {
                createDirectory(config.getReplayingSource());
                File replaying = new File(config.getReplayingSource(), key);
                parser = new StorageParser(replaying);
                parserMap.put(key, parser);
            }
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
            String key = createKey(invocation);
            writer = writerMap.get(key);
            if (writer == null)
            {
                createDirectory(config.getRecordingSource());
                File recording = new File(config.getRecordingSource(), key);
                writer = new StorageWriter(recording);
                writerMap.put(key, writer);
            }
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

    private void createDirectory(File file)
    {
        if (!file.exists())
        {
            file.mkdirs();
        }
    }

    private String createKey(InvocationContext invocation)
    {
        String target = invocation.getMethod().getDeclaringClass().getCanonicalName().toLowerCase();
        String method = invocation.getMethod().getName().toLowerCase();
        return String.format("%s.%s.xml", target, method);
    }

}
