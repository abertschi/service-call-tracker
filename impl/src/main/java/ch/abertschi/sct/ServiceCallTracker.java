package ch.abertschi.sct;

import ch.abertschi.sct.invocation.Interceptor;
import ch.abertschi.sct.invocation.InvocationContext;
import ch.abertschi.sct.util.ResultNotFoundExcetion;
import ch.abertschi.sct.util.SctException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.abertschi.sct.api.SctConfiguration;

public class ServiceCallTracker implements Interceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(ServiceCallTracker.class);

    private SctContext context;

    public ServiceCallTracker(SctConfiguration config)
    {
        this.context = createContext(config);
    }

    private SctContext createContext(SctConfiguration c)
    {
        SctContext context = new SctContext();
        context.setConfiguration(c);
        return context;
    }

    @Override
    public Object invoke(InvocationContext ctx)
    {
        final Object request = getInCorrectDimension(ctx.getParameters());
        final SctConfiguration config = context.getConfiguration();
        Object response = null;

        if (config.isResponseLoading() && config.isCallRecording())
        {
            try
            {
                response = loadResponse(request);
            }
            catch (ResultNotFoundExcetion e)
            { // TODO
                response = proceed(ctx);
                recordCall(request, response);
            }
        }
        else if (config.isResponseLoading())
        {
            try
            {
                response = loadResponse(request);
            }
            catch (ResultNotFoundExcetion e)
            {
                throw new SctException(e);
            }
        }
        else if (config.isCallRecording())
        {
            response = proceed(ctx);
            recordCall(request, response);
        }
        else
        {
            response = proceed(ctx);
        }
        return response;
    }

    private Object loadResponse(Object request) throws ResultNotFoundExcetion
    {
        return null;
    }

    private void recordCall(Object request, Object response)
    {
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
}
