package ch.abertschi.sct;

import ch.abertschi.sct.api.Configuration;
import ch.abertschi.sct.api.SctConfigurationImpl;
import ch.abertschi.sct.invocation.Interceptor;
import ch.abertschi.sct.invocation.InvocationContext;
import ch.abertschi.sct.parse.StorageCollection;
import ch.abertschi.sct.parse.StorageParser;
import ch.abertschi.sct.serial.Call;
import ch.abertschi.sct.serial.Request;
import ch.abertschi.sct.serial.Response;
import ch.abertschi.sct.serial.StorageWriter;
import ch.abertschi.sct.util.ResultNotFoundException;
import ch.abertschi.sct.util.SctException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.abertschi.sct.api.SctConfiguration;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class ServiceCallTracker implements Interceptor
{
    private static final String CONFIG_SKIP = "sct.skip";
    private static final String CONFIG_SKIP_RECORDING = "sct.record.skip";
    private static final String CONFIG_SKIP_REPLAYING = "sct.replay.skip";

    private static final Logger LOG = LoggerFactory.getLogger(ServiceCallTracker.class);

    private Configuration config;
    private StorageCollection storageCollection;

    public ServiceCallTracker(Configuration config)
    {
        if (config.isReplayingEnabled() || config.isRecordingEnabled())
        {
            this.storageCollection = new StorageCollection(config);
        }
        this.config = config;
    }

    private boolean isSkip()
    {
        return isBooleanSystemPropertyTrue(CONFIG_SKIP);
    }

    private boolean isSkipRecording()
    {
        return isSkip() || isBooleanSystemPropertyTrue(CONFIG_SKIP_RECORDING);
    }

    private boolean isSkipReplaying()
    {
        return isSkip() || isBooleanSystemPropertyTrue(CONFIG_SKIP_REPLAYING);
    }

    @Override
    public Object invoke(InvocationContext ctx)
    {
        Object response = null;
        final Object request = getInCorrectDimension(ctx.getParameters());
        if (config.isReplayingEnabled()
                && config.isRecordingEnabled()
                && !isSkipRecording()
                && !isSkipReplaying())
        {
            response = performRecordingAndReplaying(request, ctx);
        }
        else if (config.isReplayingEnabled() && !isSkipReplaying())
        {
            response = performReplaying(request, ctx);
        }
        else if (config.isRecordingEnabled() && !isSkipRecording())
        {
            response = performRecording(request, ctx);
        }
        else
        {
            response = proceed(ctx);
        }
//            File responses = config.getRecordingFile();
//            File recordings = config.getReplayingFile();
//            if (responses.exists())
//            {
//                try
//                {
//                    response = loadResponse(responses, request);
//                }
//                catch (ResultNotFoundException e)
//                {
//                    response = proceed(ctx);
//                    recordCall(recordings, request, response);
//                }
//            }
//            else
//            {
//                System.out.println("recording");
//                response = proceed(ctx);
//                recordCall(recordings, request, response);
//            }
//        }
//        else if (config.isResponseLoading())
//        {
//            File responses = getFile(config.getResponseLoadingUrl());
//            try
//            {
//                response = loadResponse(responses, request);
//            }
//            catch (ResultNotFoundException e)
//            {
//                String msg = "Service Call Tracker: No response was found for given request";
//                throw new RuntimeException(msg, e);
//            }
//        }
//        else if (config.isCallRecording())
//        {
//            File recordings = getFile(config.getCallRecordingUrl());
//            response = proceed(ctx);
//            recordCall(recordings, request, response);
//        }
//        else
//        {
//            response = proceed(ctx);
//        }
        return response;
    }

    private Object performRecordingAndReplaying(Object request, InvocationContext ctx)
    {
        Object response;
        try
        {
            response = storageCollection.get(request, ctx);
        }
        catch (ResultNotFoundException e)
        {
            response = proceed(ctx);
            storageCollection.add(request, response, ctx);
        }
        return response;
    }

    private Object performReplaying(Object request, InvocationContext ctx)
    {
        Object response;
        try
        {
            response = storageCollection.get(request, ctx);
        }
        catch (ResultNotFoundException e)
        {
            if (config.isThrowExceptionOnNotFound())
            {
                throw e;
            }
            else
            {
                response = proceed(ctx);
            }
        }
        return response;
    }


    private Object performRecording(Object request, InvocationContext ctx)
    {
        Object response;
        response = proceed(ctx);
        storageCollection.add(request, response, ctx);
        return request;
    }

    private Object proceed(InvocationContext ctx)
    {
        try
        {
            return ctx.proceed();
        }
        catch (Exception e)
        {
            throw new SctException(e);
        }
    }

    /*
     * Treat object arrays as aggregates and non-aggregate parameter as
     * single parameter. This is important because of the request is used as a key to
     * identify the response.
     */
    private Object getInCorrectDimension(Object[] parameters)
    {
        Object result = null;
        if (parameters != null)
        {
            if (parameters.length > 1)
            {
                result = parameters;
            }
            else
            {
                result = parameters[0];
            }
        }
        return result;
    }

    private boolean isBooleanSystemPropertyTrue(String key)
    {
        String value = System.getProperty(key);
        boolean hasValue = value != null && !value.isEmpty();
        return hasValue && "true".equals(value.toLowerCase());
    }
}
