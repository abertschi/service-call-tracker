package ch.abertschi.sct;

import ch.abertschi.sct.api.Interceptor;
import ch.abertschi.sct.api.SctException;
import ch.abertschi.sct.api.Configuration;
import ch.abertschi.sct.api.invocation.InvocationContext;
import ch.abertschi.sct.parse.XStreamProvider;
import ch.abertschi.sct.util.ResultNotFoundException;
import com.github.underscore.$;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ServiceCallTracker implements Interceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(ServiceCallTracker.class);

    private static final String CONFIG_SKIP = "sct.skip";
    private static final String CONFIG_SKIP_RECORDING = "sct.record.skip";
    private static final String CONFIG_SKIP_REPLAYING = "sct.replay.skip";
    private XStream xstream;

    private Configuration config;
    private FileStorageCollection storageCollection;

    public ServiceCallTracker(Configuration config)
    {
        if (config.isReplayingEnabled() || config.isRecordingEnabled())
        {
            this.storageCollection = new FileStorageCollection(config);
        }
        if (config.getMarshallInstructions() != null)
        {
            for (Map.Entry<String, Class<?>> alias : config.getMarshallInstructions().entrySet())
            {
                XStreamProvider.GET.addAlias(alias.getKey(), alias.getValue());
            }
        }
        this.config = config;
        this.xstream = XStreamProvider.GET.createPreConfiguredXStream();
    }

    @Override
    public Object invoke(InvocationContext ctx)
    {
        Object response;
        final Object request = getInCorrectDimension(ctx.getParameters());

        if (!isValidContext(ctx))
        {
            LOG.error("Invalid Invocation Context set. Can not perform interception with {}", stringify(ctx));
            response = proceed(ctx);

        }
        else if (isRecording() || isReplaying())
        {
            if (isRecording() && isReplaying())
            {
                LOG.info("recording and replaying call of {}", getTargetName(ctx));
                response = performRecordingAndReplaying(request, ctx);
            }
            else if (isReplaying())
            {
                LOG.info("replaying call of {}", getTargetName(ctx));
                response = performReplaying(request, ctx, config.isThrowExceptionOnNotFound());
            }
            else
            {
                LOG.info("recording call of {}", getTargetName(ctx));
                response = performRecording(request, ctx);
            }
            if (!doesReturnTypeMatch(ctx, response))
            {
                String msg = String.format("Stored response does not match with method signature %s" +
                        "The method return type was changed.", getTargetName(ctx));
                LOG.error(msg);

                if (config.isThrowExceptionOnIncompatibleReturnType())
                {
                    throw new SctException(msg, null);
                }
                else
                {
                    response = proceed(ctx);
                }
            }
        }
        else
        {
            response = proceed(ctx);
        }
        return response;
    }

    private boolean doesReturnTypeMatch(InvocationContext ctx, Object response)
    {
        return response == null || ctx.getMethod() == null ||
                ctx.getMethod().getReturnType().isAssignableFrom(response.getClass());
    }

    private Object performRecordingAndReplaying(Object request, InvocationContext ctx)
    {
        Object response;
        try
        {
            response = performReplaying(request, ctx, true);
        }
        catch (ResultNotFoundException e)
        {
            response = proceed(ctx);
            storageCollection.add(request, response, ctx);
        }
        return response;
    }

    private Object performReplaying(Object request, InvocationContext ctx, boolean throwExceptionOnNotFound)
    {
        Object response;
        try
        {
            response = storageCollection.get(request, ctx);
        }
        catch (ResultNotFoundException e)
        {
            if (throwExceptionOnNotFound)
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
        String result;
        if (!$.isNull(invocation.getMethod()))
        {
            String target = invocation.getMethod().getDeclaringClass().getName();
            String method = invocation.getMethod().getName();
            result = String.format("%s.%s(...)", target, method);
        }
        else
        {
            result = invocation.getTarget().getClass().getName();
        }
        return result;
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

    private boolean isRecording()
    {
        return !isSkipRecording() && config.isRecordingEnabled();
    }

    private boolean isReplaying()
    {
        return !isSkipReplaying() && config.isReplayingEnabled();
    }

    private boolean isValidContext(InvocationContext ctx)
    {
        return ctx.getMethod() != null || ctx.getTarget() != null;
    }

    private String stringify(Object obj)
    {
        return xstream.toXML(obj);
    }
}
