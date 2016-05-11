package ch.abertschi.sct.parse;

import ch.abertschi.sct.CallCollectable;
import ch.abertschi.sct.node.Node;
import ch.abertschi.sct.node.NodeUtil;
import ch.abertschi.sct.transformer.*;
import com.github.underscore.$;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
public class ParserCallCollection implements CallCollectable
{
    private ParserContext storage;

    private List<Transformer> requestTransformers;

    public ParserCallCollection() throws IOException, JDOMException
    {
        String path = new File(new File("."), "impl/src/main/java/ch/abertschi/sct/storage.xml").getAbsolutePath();
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        String xml = new String(encoded);

        DataSetParser parser = new DataSetParser();
        this.storage = parser.parseXml(xml);
        this.requestTransformers = getRequestTransformers();
    }

    public static void main(String[] args) throws Throwable
    {
        String hello = "hi";
        new ParserCallCollection().get(hello);

    }

    @Override
    public Object get(Object request) throws Throwable
    {
        Object response = null;
        CallContext context = new CallContext();
        context.setRequestObject(request);

        ParserCall call = findMatchingCall(context);
        if (!$.isNull(call))
        {
            context.setStorageCall(call);
            ResponseEvaluator eval = new ResponseEvaluator(context, call);
            response = eval.evaluate();
        }

        return response;
    }

    @Override
    public void put(Object request, Object response)
    {

    }


    protected ParserCall findMatchingCall(CallContext context)
    {
        ParserCall found = null;

        for (ParserCall call : this.storage.getCalls())
        {
            context.setStorageCall(call); // TODO: not thread save, improve
            ParserCallRequest request = call.getRequest();
            Node node = request.getPayloadNode();
            Transformers.transform(node, this.requestTransformers, context);

            if (NodeUtil.doesNodeMatchWithObject(request.getPayloadType(), node, context.getRequestObject()))
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
}
