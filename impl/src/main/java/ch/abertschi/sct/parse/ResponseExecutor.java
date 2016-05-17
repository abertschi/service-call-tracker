package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.NodeUtils;
import ch.abertschi.sct.transformer.*;
import ch.abertschi.unserialize.StackTraceUnserialize;
import com.github.underscore.$;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abertschi on 17/05/16.
 */
public class ResponseExecutor
{

    private ParserContext parserContext;
    private List<Transformer> responseTransformers;

    public ResponseExecutor(ParserContext parserContext, List<Transformer> responseTransformers)
    {
        this.parserContext = parserContext;
        this.responseTransformers = responseTransformers;
    }

    public Object execute(ParserCall call, Object currentRequest) throws Throwable
    {
        TransformerContext transformerContext = new TransformerContext()
                .setParserContext(parserContext)
                .setCall(call)
                .setCurrentRequest(currentRequest);

        Throwable exception = createResponseStackTrace(call, transformerContext);
        Object payload = createResponsePayload(call, transformerContext);
        Object execute;

        if (isScript(call))
        {
            GroovyShell shell = createResponseScript(exception, payload, currentRequest);
            execute = shell.evaluate(call.getResponse().getScript());
        }
        else if (isStackTrace(call))
        {
            throw exception;
        }
        else if (isPayload(call))
        {
            execute = payload;
        }
        else
        {
            throw new RuntimeException("Invalid response configuration. Neither a script, nor stacktrace nor payload was set.");
        }
        return execute;
    }

    private Throwable createResponseStackTrace(ParserCall call, TransformerContext transformerContext)
    {
        ParserResponse response = call.getResponse();
        Throwable exception = null;
        if (isStackTrace(call))
        {
            String stacktrace = Transformers.transform(response.getStacktrace(), this.responseTransformers, transformerContext);
            response.setStacktrace(stacktrace);
            exception = StackTraceUnserialize.unserialize(stacktrace);
        }
        return exception;
    }

    private Object createResponsePayload(ParserCall call, TransformerContext transformerContext)
    {
        Object payload = null;
        ParserResponse response = call.getResponse();
        if (isPayload(call))
        {
            Transformers.transform(response.getPayloadNode(), this.responseTransformers, transformerContext);
            payload = NodeUtils.createObjectWithNode(response.getPayloadNode());
        }
        return payload;
    }

    private GroovyShell createResponseScript(Throwable exception, Object payload, Object request)
    {
        Binding binding = new Binding();
        binding.setVariable("env", System.getenv());
        binding.setVariable("system", System.getProperties());
        binding.setVariable("request", request);
        binding.setVariable("stacktrace", exception);
        binding.setVariable("response", payload);
        return new GroovyShell(binding);
    }

    private boolean isScript(ParserCall call)
    {
        ParserResponse r = call.getResponse();
        return !$.isNull(r.getScript()) && !r.getScript().trim().isEmpty();
    }

    private boolean isStackTrace(ParserCall call)
    {
        ParserResponse r = call.getResponse();
        return !$.isNull(r.getStacktrace()) && !r.getStacktrace().trim().isEmpty();
    }

    private boolean isPayload(ParserCall call)
    {
        ParserResponse r = call.getResponse();
        return !$.isNull(r.getPayloadNode());
    }

}
