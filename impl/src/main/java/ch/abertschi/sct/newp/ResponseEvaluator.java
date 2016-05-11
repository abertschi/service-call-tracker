package ch.abertschi.sct.newp;

import ch.abertschi.sct.newp.parse.NodeUtils;
import ch.abertschi.sct.newp.parse.StorageCall;
import ch.abertschi.sct.newp.parse.StorageCallResponse;
import ch.abertschi.sct.newp.transformer.*;
import ch.abertschi.unserialize.StackTraceUnserialize;
import com.github.underscore.$;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
public class ResponseEvaluator
{
    private List<Transformer> transformers;

    private StorageCall call;

    private CallContext context;

    public ResponseEvaluator(CallContext context, StorageCall call)
    {
        this.call = call;
        this.context = context;
        this.transformers = getResponseTransformers();
    }

    public Object evaluate() throws Throwable
    {
        Throwable exception = createStackTrace();
        Object payload = createPayload();

        Object evaluated;
        if (isScript())
        {
            GroovyShell shell = createShell(exception, payload);
            evaluated = shell.evaluate(call.getResponse().getScript());
        }
        else if (isStackTrace())
        {
            throw exception;
        }
        else if (isPayload())
        {
            evaluated = payload;
        }
        else
        {
            throw new RuntimeException("Misconfigured call. Neither a script, nor stacktrace nor payload was set.");
        }

        return evaluated;
    }

    private Throwable createStackTrace()
    {
        StorageCallResponse response = this.call.getResponse();
        Throwable exception = null;
        if (isStackTrace())
        {
            String stacktrace = Transformers.transform(response.getStacktrace(), transformers, context);
            response.setStacktrace(stacktrace);
            exception = StackTraceUnserialize.unserialize(stacktrace);
        }
        return exception;
    }

    private Object createPayload()
    {
        Object payload = null;
        StorageCallResponse response = this.call.getResponse();
        if (isPayload())
        {
            Transformers.transform(response.getPayloadNode(), transformers, context);
            payload = NodeUtils.createObject(response.getPayloadType(), response.getPayloadNode());
        }
        return payload;
    }

    private GroovyShell createShell(Throwable exception, Object payload)
    {
        Binding binding = new Binding();
        binding.setVariable("env", System.getenv());
        binding.setVariable("system", System.getProperties());
        binding.setVariable("request", this.context.getRequestObject());
        binding.setVariable("stacktrace", exception);
        binding.setVariable("response", payload);
        return new GroovyShell(binding);
    }

    protected List<Transformer> getResponseTransformers()
    {
        List<Transformer> transformers = new ArrayList<>();
        transformers.add(new VariableTransformer());
        transformers.add(new FieldReferenceTransformer());
        return transformers;
    }

    private boolean isScript()
    {
        StorageCallResponse r = this.call.getResponse();
        return !$.isNull(r.getScript()) && !r.getScript().trim().isEmpty();
    }

    private boolean isStackTrace()
    {
        StorageCallResponse r = this.call.getResponse();
        return !$.isNull(r.getStacktrace()) && !r.getStacktrace().trim().isEmpty();
    }

    private boolean isPayload()
    {
        StorageCallResponse r = this.call.getResponse();
        return !$.isNull(r.getPayloadType())
                && !r.getPayloadType().trim().isEmpty()
                && !$.isNull(r.getPayloadNode());

    }
}
