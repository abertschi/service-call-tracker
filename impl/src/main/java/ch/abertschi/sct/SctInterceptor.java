package ch.abertschi.sct;

import ch.abertschi.sct.invocation.Interceptor;
import ch.abertschi.sct.invocation.InvocationContext;
import ch.abertschi.sct.util.ResultNotFoundExcetion;
import ch.abertschi.sct.util.SctException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.abertschi.sct.api.SctConfiguration;

/**
 * sct - ServiceCallTracker <br />
 * Interceptor for service response loading and service call recording.
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 * 
 */
public class SctInterceptor implements Interceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(SctInterceptor.class);

    private CallPersistence boundary;

    private SctContext context;

    public SctInterceptor(SctConfiguration config) {
        this.context = createContext(config);
        this.boundary = new CallPersistenceImpl(context);
    }
    
    public SctInterceptor(SctConfiguration config, CallPersistence boundary) {
        this.context = createContext(config);
        this.boundary = boundary;
    }

    private SctContext createContext(SctConfiguration c) {
        SctContext context = new SctContext();
        context.setConfiguration(c);
        return context;
    }

    @Override
    public Object invoke(InvocationContext ctx) {
        final Object request = getInCorrectDimension(ctx.getParameters());
        final SctConfiguration config = context.getConfiguration();
        Object response = null;
        
        printHeader(request, config);

        if (config.isResponseLoading() && config.isCallRecording()) {
            LOG.info("Performing response loading and call recording action.");

            try {
                response = loadResponse(request);
            } catch (ResultNotFoundExcetion e) {
                response = proceed(ctx);
                LOG.info("Response doesn't exist yet. Calling native service to record call...");
                recordCall(request, response);
            }
        } else if (config.isResponseLoading()) {
            LOG.info("Performing response loading action.");

            try {
                response = loadResponse(request);
            } catch (ResultNotFoundExcetion e) {
                throw new SctException("No matching response was found. Canceling execution.", e);
            }
        } else if (config.isCallRecording()) {
            LOG.info("Performing call recording action.");

            response = proceed(ctx);
            recordCall(request, response);
        }
        else {
            LOG.info("sct-Interceptor is not activated for call. "
                    + "Delegating to native service.");
            response = proceed(ctx);
        }
        return response;
    }

    private Object loadResponse(Object request) throws ResultNotFoundExcetion {
        return this.boundary.load(request);
    }

    private void recordCall(Object request, Object response) {
        this.boundary.record(request, response);
    }

    private Object proceed(InvocationContext ctx) {
        try {
            return ctx.proceed();
        } catch (Exception e) {
            throw new SctException(
                    "Unable to proceed call to native subsystem. Something unexpected went wrong ...", e);
        }
    }

    /*
     * Treat object arrays (with more than 1 element) as aggregates and non-aggregate parameter as
     * single parameter. This is important to look after because of the request is used as a key to
     * identify the response.
     */
    private Object getInCorrectDimension(Object[] parameters) {
        Object result = null;
        if (parameters != null) {
            if (parameters.length > 1) {
                result = parameters;
            } else {
                result = parameters[0];
            }
        }
        return result;
    }
    
    private void printHeader(Object request, SctConfiguration config) {
        final String HEADER = "------ sct runtime information ------";
        final String NEW_L = "\n";

        StringBuilder b = new StringBuilder();
        b.append(NEW_L)
            .append(HEADER)
            .append(NEW_L)
            .append("configuration:\t")
            .append(config)
            .append(NEW_L)
            .append("request-object:\t")
            .append(request);
        
        LOG.info(b.toString());
    }
}
