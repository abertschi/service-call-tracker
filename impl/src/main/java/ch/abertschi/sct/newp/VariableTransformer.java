package ch.abertschi.sct.newp;

import com.github.underscore.$;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.*;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by abertschi on 11/05/16.
 */
public class VariableTransformer implements Transformer
{
    private static final Pattern PATTERN_IS_EXPRESSION = Pattern.compile("[#|\\{]([^}]*)}");

    private String[] VARS = {"env", "system"};

    private SimpleContext elContext;

    private ExpressionFactory factory;

    public VariableTransformer()
    {
        createElContext();
    }

    public static void main(String[] args)
    {
        new VariableTransformer().createElContext();
    }

    protected void createElContext()
    {
        this.factory = new ExpressionFactoryImpl();
        this.elContext = new SimpleContext();

        elContext.setVariable("env", factory.createValueExpression(System.getenv(), Map.class));
        elContext.setVariable("system", factory.createValueExpression(System.getProperties(), Properties.class));

        //ValueExpression e = factory.createValueExpression(elContext, "Hello ${request}!", String.class);
        //System.out.println(e.getValue(elContext)); // --> Hello, bar!
        //System.out.println("out");
    }


    @Override
    public boolean canTransform(TransformingContext context, String input)
    {
        return PATTERN_IS_EXPRESSION.matcher(input).find() && $.indexOf(VARS, input) > -1;

    }

    @Override
    public String transform(TransformingContext context, String input)
    {
        ValueExpression expression = factory.createValueExpression(elContext, input, String.class);
        String transformed = null;
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
