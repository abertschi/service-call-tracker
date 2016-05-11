package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.NodeUtil;
import ch.abertschi.sct.parse.ParserCall;
import ch.abertschi.sct.parse.ParserCallResponse;
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
public class ResponseEvaluator
{
    private List<Transformer> transformers;

    private ParserCall call;

    private CallContext context;

    public ResponseEvaluator(CallContext context, ParserCall call)
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
        ParserCallResponse response = this.call.getResponse();
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
        ParserCallResponse response = this.call.getResponse();
        if (isPayload())
        {
            Transformers.transform(response.getPayloadNode(), transformers, context);
            payload = NodeUtil.createObject(response.getPayloadType(), response.getPayloadNode());
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
        ParserCallResponse r = this.call.getResponse();
        return !$.isNull(r.getScript()) && !r.getScript().trim().isEmpty();
    }

    private boolean isStackTrace()
    {
        ParserCallResponse r = this.call.getResponse();
        return !$.isNull(r.getStacktrace()) && !r.getStacktrace().trim().isEmpty();
    }

    private boolean isPayload()
    {
        ParserCallResponse r = this.call.getResponse();
        return !$.isNull(r.getPayloadType())
                && !r.getPayloadType().trim().isEmpty()
                && !$.isNull(r.getPayloadNode());

    }
}
