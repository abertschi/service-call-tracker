package ch.abertschi.sct.newp;

import ch.abertschi.sct.newp.parse.*;
import ch.abertschi.sct.newp.transformer.*;
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
public class CallCollection
{
    private StorageContext storage;
    private List<Transformer> requestTransformers;

    public CallCollection() throws IOException, JDOMException
    {
        String path = new File(new File("."), "impl/src/main/java/ch/abertschi/sct/newp/storage.xml").getAbsolutePath();
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        String xml = new String(encoded);

        StorageParser parser = new StorageParser();
        this.storage = parser.parseXml(xml);
        this.requestTransformers = getRequestTransformers();
    }

    public static void main(String[] args) throws Throwable
    {
        String hello = "hi";
        new CallCollection().lookup(hello);

    }

    public Object lookup(Object request) throws Throwable
    {
        CallContext context = new CallContext();
        context.setRequestObject(request);

        StorageCall call = findMatchingCall(context);
        if (!$.isNull(call))
        {
            context.setStorageCall(call);
            ResponseEvaluator eval = new ResponseEvaluator(context, call);
            return eval.evaluate();
        }

        return null;
    }

    protected StorageCall findMatchingCall(CallContext context)
    {
        StorageCall found = null;

        for (StorageCall call : this.storage.getCalls())
        {
            context.setStorageCall(call); // TODO: not thread save, improve
            StorageCallRequest request = call.getRequest();
            Node node = request.getPayloadNode();
            Transformers.transform(node, this.requestTransformers, context);

            if (NodeUtils.doesNodeMatchWithObject(request.getPayloadType(), node, context.getRequestObject()))
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
