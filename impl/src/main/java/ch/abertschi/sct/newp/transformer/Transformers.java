package ch.abertschi.sct.newp.transformer;

import ch.abertschi.sct.newp.parse.Node;

import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
public class Transformers
{
    public static void transform(Node node, List<Transformer> transformers, CallContext context)
    {
        if (node.isContainer())
        {
            node.getChildren().forEach(child -> transform(child, transformers, context));
        }
        else
        {
            transformers
                    .stream()
                    .filter(t -> t.canTransform(context, node.getValue()))
                    .forEach(t -> node.setValue(t.transform(context, node.getValue())));
        }
    }

    public static String transform(String input, List<Transformer> transformers, CallContext context)
    {
        for (Transformer transformer : transformers)
        {
            if (transformer.canTransform(context, input))
            {
                input = transformer.transform(context, input);
            }
        }
        return input;
    }
}
