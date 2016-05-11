package ch.abertschi.sct.newp;

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
    public CallCollection()
    {


    }

    public static void main(String[] args) throws Exception
    {
        String hello = "hi";
        new CallCollection().lookup(hello);

    }

    public Object lookup(Object request) throws IOException, JDOMException
    {
        String path = new File(new File("."), "impl/src/main/java/ch/abertschi/sct/newp/storage.xml").getAbsolutePath();
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        String xml = new String(encoded);

        StorageParser parser = new StorageParser();
        StorageContext storage = parser.parseXml(xml);
        List<Transformer> transformers = new ArrayList<>();
        transformers.add(new VariableTransformer());
        transformers.add(new FieldReferenceTransformer());

        storage.getCalls().forEach(call -> {
            TransformingContext transformingContext = new TransformingContext();
            transformingContext.setRequestObject(request);
            transformingContext.setStorageCall(call);

            StorageCallRequest storageRequest = call.getRequest();
            Node node = storageRequest.getPayloadNode();

            System.out.println(node.getChildren().size());
            transform(node, transformers, transformingContext);

        });
        return null;
    }

    private void transform(Node node, List<Transformer> transformers, TransformingContext context)
    {
        System.out.println(node.isContainer() + node.getName() + node.getChildren().get(2).getName());
        if (node.isContainer())
        {
            for(Node n: node.getChildren())
            {
                //transform(n, transformers, context);
            }
            //node.getChildren().forEach(child -> transform(child, transformers, context));
        }
        else
        {
            transformers
                    .stream()
                    .filter(t -> t.canTransform(context, node.getValue()))
                    .forEach(t -> node.setValue(t.transform(context, node.getValue())));
        }
    }
}
