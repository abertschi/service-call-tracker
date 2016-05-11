package ch.abertschi.sct.newp;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by abertschi on 11/05/16.
 */
public class Node
{
    public enum NodeType
    {
        VALUE, REFERENCE, REGEX
    }

    private static final Pattern PATTERN_IS_REFERENCE = Pattern.compile("[#|\\{]([^}]*)}");

    private String name;
    private String value;
    private String regex;
    private boolean isContainer;
    private List<Node> children = new LinkedList<>();

    public Node()
    {
    }

    public NodeType determineType()
    {
        return determineType(this.value);
    }

    public NodeType determineType(String value)
    {
        NodeType type = NodeType.VALUE;

        if (value != null)
        {
            value = value.trim();
            if (PATTERN_IS_REFERENCE.matcher(value).find())
            {
                type = NodeType.REFERENCE;
            }
            else if (isRegex(value))
            {
                type = NodeType.REGEX;
            }
        }
        return type;
    }

    private boolean isRegex(String input)
    {
        boolean isRegex;
        try
        {
            Pattern.compile(input);
            isRegex = true;
        }
        catch (PatternSyntaxException e)
        {
            isRegex = false;
        }
        return isRegex;
    }

    public List<Node> getChildren()
    {
        return children;
    }

    public Node setChildren(List<Node> children)
    {
        this.children = children;
        return this;
    }

    public boolean isContainer()
    {
        return isContainer;
    }

    public Node setIsContainer(boolean isContainer)
    {
        this.isContainer = isContainer;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public Node setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getRegex()
    {
        return regex;
    }

    public Node setRegex(String regex)
    {
        this.regex = regex;
        return this;
    }


    public String getValue()
    {
        return value;
    }

    public Node setValue(String value)
    {
        this.value = value;
        //System.out.println(determineType());
        return this;
    }
}
