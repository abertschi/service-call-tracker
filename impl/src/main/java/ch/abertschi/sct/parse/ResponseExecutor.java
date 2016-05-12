package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.NodeUtil;
import ch.abertschi.sct.transformer.*;
import ch.abertschi.unserialize.StackTraceUnserialize;
import com.github.underscore.$;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
public class ResponseExecutor
{
    private final Object currentRequest;
    private final TransformerContext transformerContext;
    private final ParserCall call;
    private List<Transformer> transformers;

    public ResponseExecutor(TransformerContext context, ParserCall call, Object currentRequest)
    {
        this.call = call;
        this.transformerContext = context;
        this.currentRequest = currentRequest;
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
        ParserResponse response = this.call.getResponse();
        Throwable exception = null;
        if (isStackTrace())
        {
            TransformerContext context = new TransformerContext();
            String stacktrace = Transformers.transform(response.getStacktrace(), transformers, context);
            response.setStacktrace(stacktrace);
            exception = StackTraceUnserialize.unserialize(stacktrace);
        }
        return exception;
    }

    private Object createPayload()
    {
        Object payload = null;
        ParserResponse response = call.getResponse();
        if (isPayload())
        {
            Transformers.transform(response.getPayloadNode(), transformers, transformerContext);
            payload = NodeUtil.createObject(response.getPayloadType(), response.getPayloadNode());
        }
        return payload;
    }

    private GroovyShell createShell(Throwable exception, Object payload)
    {
        Binding binding = new Binding();
        binding.setVariable("env", System.getenv());
        binding.setVariable("system", System.getProperties());
        binding.setVariable("request", currentRequest);
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
        ParserResponse r = call.getResponse();
        return !$.isNull(r.getScript()) && !r.getScript().trim().isEmpty();
    }

    private boolean isStackTrace()
    {
        ParserResponse r = call.getResponse();
        return !$.isNull(r.getStacktrace()) && !r.getStacktrace().trim().isEmpty();
    }

    private boolean isPayload()
    {
        ParserResponse r = call.getResponse();
        return !$.isNull(r.getPayloadType())
                && !r.getPayloadType().trim().isEmpty()
                && !$.isNull(r.getPayloadNode());

    }
}
