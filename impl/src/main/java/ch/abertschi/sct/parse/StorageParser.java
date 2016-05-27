package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.Node;
import ch.abertschi.sct.node.NodeUtils;
import ch.abertschi.sct.transformer.*;
import ch.abertschi.sct.util.ExceptionUtil;
import ch.abertschi.sct.util.ResultNotFoundException;
import com.github.underscore.$;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
public class StorageParser
{
    private ParserContext parserContext;
    private List<Transformer> requestAndResponseTransformers;
    private ResponseExecutor responseExecutor;

    public StorageParser(File source)
    {
        createFileIfNotExists(source);
        this.parserContext = new XmlParser().parse(source);
        init();
    }

    public StorageParser(InputStream source)
    {
        this(convertStreamToString(source));
    }

    public StorageParser(String source)
    {
        this.parserContext = new XmlParser().parse(source);
        init();
    }

    private void init()
    {
        this.requestAndResponseTransformers = getRequestAndResponseTransformers();
        this.responseExecutor = new ResponseExecutor(parserContext, requestAndResponseTransformers);
    }

    /**
     * Throws ResultNotFoundException if no matching response was found.
     */
    public Object get(Object request)
    {
        Object response = null;
        ParserCall call = findCall(request);
        if (!$.isNull(call))
        {
            try
            {
                response = executeResponse(call, request);
            }
            catch (Throwable throwable)
            {
                ExceptionUtil.throwException(throwable);
            }
        }
        else
        {
            throw new ResultNotFoundException();
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
            Transformers.transform(node, this.requestAndResponseTransformers, context);

            if (NodeUtils.doesNodeMatchWithObject(node, context.getCurrentRequest()))
            {
                found = call;
                break;
            }
        }
        return found;
    }

    protected Object executeResponse(ParserCall call, Object currentRequest) throws Throwable
    {
        return this.responseExecutor.execute(call, currentRequest);
    }

    protected List<Transformer> getRequestAndResponseTransformers()
    {
        List<Transformer> transformers = new ArrayList<>();
        transformers.add(new VariableTransformer());
        transformers.add(new FieldReferenceTransformer());
        return transformers;
    }

    private void createFileIfNotExists(File file)
    {
        if (!file.exists())
        {
            file.getParentFile().mkdirs();
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private static String convertStreamToString(InputStream is)
    {
        if (is == null)
        {
            return "";
        }
        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
