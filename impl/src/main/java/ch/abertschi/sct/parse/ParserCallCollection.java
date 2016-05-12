package ch.abertschi.sct.parse;

import ch.abertschi.sct.node.Node;
import ch.abertschi.sct.node.NodeUtil;
import ch.abertschi.sct.transformer.*;
import com.github.underscore.$;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
// TODO: better name?
public class ParserCallCollection
{
    private ParserContext parserContext;

    private List<Transformer> requestTransformers;

    public ParserCallCollection(File source)
    {
        XmlParser parser = new XmlParser();
        this.parserContext = parser.parseXml(readFile(source));
        this.requestTransformers = getRequestTransformers();
    }

    public Object get(Object request) throws Throwable
    {
        Object response = null;
        ParserCall call = findCall(request);
        if (!$.isNull(call))
        {
            ResponseExecutor eval = new ResponseExecutor(context, call);
            response = eval.evaluate();
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

            if (NodeUtil.doesNodeMatchWithObject(call.getRequest().getPayloadType(),
                    node, context.getCurrentRequest()))
            {
                found = call;
                break;
            }
        }
        return found;
    }

    protected List<Transformer> getRequestTransformers()
    {
        List<Transformer> transformers = new ArrayList<>();
        transformers.add(new VariableTransformer());
        transformers.add(new FieldReferenceTransformer());
        return transformers;
    }

    private String readFile(File source)
    {
        try
        {
            byte[] encoded = Files.readAllBytes(Paths.get(source.getAbsolutePath()));
            return new String(encoded);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Throwable
    {
        String hello = "hi";
        File file = new File(new File("."), "impl/src/main/java/ch/abertschi/sct/parserContext.xml");
        new ParserCallCollection(file).get(hello);
    }
}
