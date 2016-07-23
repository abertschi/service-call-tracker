package ch.abertschi.sct.transformer;

import ch.abertschi.sct.node.Node;
import ch.abertschi.sct.node.NodeUtils;
import com.github.underscore.$;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abertschi on 11/05/16.
 */
public class FieldReferenceTransformer implements Transformer
{
    private static final Pattern PATTERN_IS_EXPRESSION = Pattern.compile("[#|\\{]([^}]*)}");

    private static final String FIELD_REFERENCE = "(%s([^ }]*)(?!\\} ))";

    @Override
    public boolean canTransform(TransformerContext context, String input)
    {
        return PATTERN_IS_EXPRESSION.matcher(input).find();
    }

    @Override
    public String transform(TransformerContext context, String input)
    {
        Matcher matcher;
        while ((matcher = PATTERN_IS_EXPRESSION.matcher(input)) != null && matcher.find())
        {
            String expression = matcher.group(1);
            if (!$.isNull(expression))
            {
                input = resolveRequestPayload(context, expression);
                input = resolveResponsePayload(context, input);
            }
        }
        return input;
    }


    private String resolveRequestPayload(TransformerContext context, String input)
    {
        if (context.hasStorageCallRequest())
        {
            for (String var : getProperties("request.payload", input))
            {
                String key = var.substring("request.".length());
                Node node = NodeUtils.findNodeInTree(key, context.getCall().getRequest().getPayloadNode());
                if (!$.isNull(node))
                {
                    input = input.replace(var, node.getValue());
                }
            }
        }
        return input;
    }

    private String resolveResponsePayload(TransformerContext context, String input)
    {
        if (context.hasStorageCallResponse())
        {
            for (String var : getProperties("response.payload", input))
            {
                String key = var.substring("response.".length());
                Node node = NodeUtils.findNodeInTree(key, context.getCall().getResponse().getPayloadNode());
                if (!$.isNull(node))
                {
                    input = input.replace(var, node.getValue());
                }
            }
        }
        return input;
    }

    private List<String> getProperties(String name, String input)
    {
        List<String> values = new LinkedList<>();
        Matcher matcher = Pattern.compile(String.format(FIELD_REFERENCE, name)).matcher(input);
        while (matcher.find())
        {
            values.add(matcher.group(1).trim());
        }
        return values;
    }
}
