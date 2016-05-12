package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.Node;
import ch.abertschi.sct.node.NodeUtils;
import ch.abertschi.sct.transformer.*;
import ch.abertschi.unserialize.StackTraceUnserialize;
import com.github.underscore.$;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
public class StorageParser
{
    private List<Transformer> responseTransformers;
    private List<Transformer> requestTransformers;
    private ParserContext parserContext;

    public StorageParser(File source)
    {
        this.parserContext = new XmlParser().parse(source);
        this.requestTransformers = getRequestTransformers();
        this.responseTransformers = getResponseTransformers();
    }

    public Object get(Object request) throws Throwable
    {
        Object response = null;
        ParserCall call = findCall(request);
        if (!$.isNull(call))
        {
            try
            {
                response = executeResponse(call, request);
            }
            catch (Throwable th)
            {
                // response eventually throws exception on purpose
                throw th;
            }
        }
        return response;
    }

    protected ParserCall findCall(Object currentRequest)
    {
        ParserCall found = null;
        TransformerContext context = new TransformerContext().setCurrentRequest(currentRequest);
        for (ParserCall call : this.parserContext.getCalls())
        {
            // go sequentially through calls to match calls with lower index first
            context.setCall(call);
            Node node = call.getRequest().getPayloadNode();
            Transformers.transform(node, this.requestTransformers, context);

            if (NodeUtils.doesNodeMatchWithObject(call.getRequest().getPayloadType(),
                    node, context.getCurrentRequest()))
            {
                found = call;
                break;
            }
        }
        return found;
    }

    protected Object executeResponse(ParserCall call, Object currentRequest) throws Throwable
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

    protected List<Transformer> getRequestTransformers()
    {
        List<Transformer> transformers = new ArrayList<>();
        transformers.add(new VariableTransformer());
        transformers.add(new FieldReferenceTransformer());
        return transformers;
    }

    protected List<Transformer> getResponseTransformers()
    {
        List<Transformer> transformers = new ArrayList<>();
        transformers.add(new VariableTransformer());
        transformers.add(new FieldReferenceTransformer());
        return transformers;
    }

    private Throwable createResponseStackTrace(ParserCall call, TransformerContext transformerContext)
    {
        ParserResponse response = call.getResponse();
        Throwable exception = null;
        if (isStackTrace(call))
        {
            String stacktrace = Transformers.transform(response.getStacktrace(), this.requestTransformers, transformerContext);
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
            payload = NodeUtils.createObjectWithNode(response.getPayloadType(), response.getPayloadNode());
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
        return !$.isNull(r.getPayloadType())
                && !r.getPayloadType().trim().isEmpty()
                && !$.isNull(r.getPayloadNode());
    }

    public static void main(String[] args) throws Throwable
    {
        String hello = "hi";
        File file = new File(new File("."), "impl/src/main/java/ch/abertschi/sct/parserContext.xml");
        new StorageParser(file).get(hello);
    }
}
