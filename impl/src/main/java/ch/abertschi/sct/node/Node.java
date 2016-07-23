package ch.abertschi.sct.node;

import com.github.underscore.$;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOG = LoggerFactory.getLogger(Node.class);

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
        return doesMatchWith(other, false);
    }

    public boolean doesMatchWith(Node other, boolean ignoreRootNames)
    {
        // matching strategy:
        // this: node from storage
        // other: request object
        //
        // if this object contains different values than other, objects do not match

        boolean success = true;
        LOG.debug(String.format("comparing %s (%s) with other %s (%s)", this.getName(), this.getValue(), other.getName(), other.getValue()));
        if (this.name.equals(other.name) || ignoreRootNames) // outer names are not compatible
        {
            if (this.isContainer && other.isContainer)
            {
                for (Node child : children)
                {
                    boolean matchChild = false;
                    for (Node otherChild : other.getChildren())
                    {
                        LOG.debug(String.format("iterating through children %s (%s) with other %s (%s)", child.getName(), child.getValue(), otherChild.getName(), otherChild.getValue()));
                        if (child.doesMatchWith(otherChild))
                        {
                            LOG.debug("match TRUE " + child.getName() + " " + child.getValue() + " " + otherChild.getName() + " " + otherChild.getValue());
                            matchChild = true;
                            break;
                        }
                    }
                    if (!matchChild)
                    {
                        LOG.debug("nothing machted " + child.getName() + child.getValue());
                        return false; // success = false
                    }
                }
            }
            else if (other.isContainer)
            {
                // regex
                success = !$.isNull(this.value) && other.toXml().matches(this.value);

            }
            else
            {
                success = !$.isNull(other.getValue()) && !$.isNull(this.value)
                        && other.getValue().matches(this.value);

                // compare values
                //LOG.debug(String.format("comparing %s (%s) with other %s (%s): %s", this.getName(), this.getValue(), other.getName(), other.getValue(), success ? "true" : "false"));
            }
        }
        else {
            success = false;
        }

        LOG.debug(String.format("comparing %s (%s) with other %s (%s): %s", this.getName(), this.getValue(), other.getName(), other.getValue(), success ? "true" : "false"));
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