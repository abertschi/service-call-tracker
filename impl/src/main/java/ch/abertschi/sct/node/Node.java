package ch.abertschi.sct.node;

import com.github.underscore.$;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by abertschi on 11/05/16.
 */
public class Node
{
    private String name;
    private String value;
    private String classType;

    private boolean isContainer;
    private List<Node> children = new LinkedList<>();

    public Node()
    {
    }

    public boolean doesMatchWith(Node other)
    {
        boolean success = false;
        if (this.name.equals(other.name) || true) // TODO: outer names are not compatible
        {
            if (this.isContainer && other.isContainer)
            {
                for (Node child : children)
                {
                    for (Node otherChild : other.getChildren())
                    {
                        if (child.doesMatchWith(otherChild))
                        {
                            success = true;
                            break;
                        }
                    }
                }
            }
            else if (other.isContainer)
            {
                // regex
                success = other.toXml().matches(this.value);
            }
            else
            {
                // compare values
                success = !$.isNull(other.getValue()) && other.getValue().matches(this.value);
            }
        }
        return success;
    }

    public String toXml()
    {
        return toXml(null);
    }

    public String toXml(Map<String, String> attributes)
    {
        if ($.isNull(attributes))
        {
            attributes = new HashMap<>();
        }
        if (!$.isNull(classType) && !classType.isEmpty())
        {
            attributes.put("class", classType);
        }
        StringBuilder attributeBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : attributes.entrySet())
        {
            attributeBuilder.append(String.format(" %s=\"%s\"", entry.getKey(), entry.getValue()));
        }
        StringBuilder childrenXml = new StringBuilder();
        if (!$.isEmpty(children))
        {
            for (Node child : children)
            {
                childrenXml.append(child.toXml());
            }
        }
        else
        {
            childrenXml.append(this.value);
        }
        return String.format("<%s%s>%s</%s>", name, attributeBuilder.toString(), childrenXml.toString(), name);
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


    public String getValue()
    {
        return value;
    }

    public Node setValue(String value)
    {
        this.value = value;
        return this;
    }

    public String getClassType()
    {
        return classType;
    }

    public Node setClassType(String classType)
    {
        this.classType = classType;
        return this;
    }
}