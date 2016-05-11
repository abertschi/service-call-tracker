package ch.abertschi.sct.newp.transformer;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by abertschi on 11/05/16.
 */
public class VariableTransformer implements Transformer
{
    private static final Pattern PATTERN_IS_EXPRESSION = Pattern.compile("[#|\\{]([^}]*)}");

    private SimpleContext elContext;

    private ExpressionFactory factory;

    public VariableTransformer()
    {
        createElContext();
    }

    protected void createElContext()
    {
        this.factory = new ExpressionFactoryImpl();
        this.elContext = new SimpleContext();

        elContext.setVariable("env", factory.createValueExpression(System.getenv(), Map.class));
        elContext.setVariable("system", factory.createValueExpression(System.getProperties(), Properties.class));
        elContext.setVariable("regex", factory.createValueExpression(getRegex(), Map.class));
    }

    protected Map<String, String> getRegex()
    {
        Map<String, String> regex = new HashMap<>();
        regex.put("any", ".*");
        regex.put("numeric", "[0-9]*");

        return regex;
    }


    @Override
    public boolean canTransform(CallContext context, String input)
    {
        return PATTERN_IS_EXPRESSION.matcher(input).find();

    }

    @Override
    public String transform(CallContext context, String input)
    {
        ValueExpression expression = factory.createValueExpression(elContext, input, String.class);
        String transformed;
        try
        {
            transformed = (String) expression.getValue(elContext);
        }
        catch (PropertyNotFoundException e)
        {
            transformed = input;
        }
        return transformed;
    }
}
