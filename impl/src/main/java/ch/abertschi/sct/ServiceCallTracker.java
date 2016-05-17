package ch.abertschi.sct;

import ch.abertschi.sct.api.Configuration;
import ch.abertschi.sct.invocation.Interceptor;
import ch.abertschi.sct.invocation.InvocationContext;
import ch.abertschi.sct.util.ResultNotFoundException;
import ch.abertschi.sct.util.SctException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceCallTracker implements Interceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(ServiceCallTracker.class);

    private static final String CONFIG_SKIP = "sct.skip";
    private static final String CONFIG_SKIP_RECORDING = "sct.record.skip";
    private static final String CONFIG_SKIP_REPLAYING = "sct.replay.skip";

    private Configuration config;
    private FileStorageCollection storageCollection;

    public ServiceCallTracker(Configuration config)
    {
        if (config.isReplayingEnabled() || config.isRecordingEnabled())
        {
            this.storageCollection = new FileStorageCollection(config);
        }
        this.config = config;
    }

    private boolean isSkip()
    {
        return isSystemPropertyBooleanSet(CONFIG_SKIP);
    }

    private boolean isSkipRecording()
    {
        return isSkip() || isSystemPropertyBooleanSet(CONFIG_SKIP_RECORDING);
    }

    private boolean isSkipReplaying()
    {
        return isSkip() || isSystemPropertyBooleanSet(CONFIG_SKIP_REPLAYING);
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
            LOG.info("recording and replaying call of {}", getTargetName(ctx));
            response = performRecordingAndReplaying(request, ctx);
        }
        else if (config.isReplayingEnabled() && !isSkipReplaying())
        {
            LOG.info("replaying call of {}", getTargetName(ctx));
            response = performReplaying(request, ctx);
        }
        else if (config.isRecordingEnabled() && !isSkipRecording())
        {
            LOG.info("recording call of {}", getTargetName(ctx));
            response = performRecording(request, ctx);
        }
        else
        {
            response = proceed(ctx);
        }
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
        Object response = proceed(ctx);
        storageCollection.add(request, response, ctx);
        return response;
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
     * Shrink arrays with only 1 argument to single object to match request.
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

    private boolean isSystemPropertyBooleanSet(String key)
    {
        String value = System.getProperty(key);
        boolean hasValue = value != null && !value.isEmpty();
        return hasValue && "true".equals(value.toLowerCase());
    }

    private String getTargetName(InvocationContext invocation)
    {
        String target = invocation.getMethod().getDeclaringClass().getName();
        String method = invocation.getMethod().getName();
        return String.format("%s.%s(...)", target, method);
    }
}
