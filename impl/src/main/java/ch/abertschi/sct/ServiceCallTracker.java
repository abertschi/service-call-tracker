package ch.abertschi.sct;

import ch.abertschi.sct.invocation.Interceptor;
import ch.abertschi.sct.invocation.InvocationContext;
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
    private static final Logger LOG = LoggerFactory.getLogger(ServiceCallTracker.class);

    private SctConfiguration config;

    public ServiceCallTracker(SctConfiguration config)
    {
        this.config = config;
    }

    @Override
    public Object invoke(InvocationContext ctx)
    {
        final Object request = getInCorrectDimension(ctx.getParameters());
        Object response = null;

        if (config.isResponseLoading() && config.isCallRecording())
        {
            File responses = getFile(config.getResponseLoadingUrl());
            File recordings = getFile(config.getResponseLoadingUrl());
            if (responses.exists())
            {
                try
                {
                    response = loadResponse(responses, request);
                }
                catch (ResultNotFoundException e)
                {
                    response = proceed(ctx);
                    recordCall(recordings, request, response);
                }
            }
            else
            {
                response = proceed(ctx);
                recordCall(recordings, request, response);
            }
        }
        else if (config.isResponseLoading())
        {
            File responses = getFile(config.getResponseLoadingUrl());
            try
            {
                response = loadResponse(responses, request);
            }
            catch (ResultNotFoundException e)
            {
                String msg = "Service Call Tracker: No response was found for given request";
                throw new RuntimeException(msg, e);
            }
        }
        else if (config.isCallRecording())
        {
            File recordings = getFile(config.getCallRecordingUrl());
            response = proceed(ctx);
            recordCall(recordings, request, response);
        }
        else
        {
            response = proceed(ctx);
        }
        return response;
    }

    private Object loadResponse(File responses, Object request)
    {
        return new StorageParser(responses).get(request);
    }

    private void recordCall(File recordings, Object request, Object response)
    {
        Call call = new Call()
                .setRequest(new Request().setPayload(request))
                .setResponse(new Response().setPayload(response));

        new StorageWriter(recordings).append(call);
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

    private File getFile(URL url)
    {
        File result;
        try
        {
            result = new File(url.toURI());
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        return result;
    }
}
