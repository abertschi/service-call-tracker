package ch.abertschi.sct.node;

import ch.abertschi.sct.parse.XStreamProvider;
import com.github.underscore.$;
import com.thoughtworks.xstream.XStream;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
public class NodeUtils
{
    private static final XStream XSTREAM = XStreamProvider.GET.createPreConfiguredXStream();

    private NodeUtils()
    {
    }

    public static Node createNodeFromObject(Object object)
    {
        Document document = xmlToDocument(toRawXml(object));
        Element rootElement = document.getRootElement();
        return parseDomToNode(rootElement);
    }

    public static String toRawXml(Object object)
    {
        return XSTREAM.toXML(object);
    }

    public static boolean doesNodeMatchWithObject(Node node, Object object)
    {
        Node otherNode = createNodeFromObject(object);
        return node.doesMatchWith(otherNode, true);
    }

    public static boolean doesNodeMatchWithObject(Node node, Object object, boolean ignoreMissingFieldsInNode)
    {
        Node otherNode = createNodeFromObject(object);
        return node.doesMatchWith(otherNode, true);
    }

    public static Node parseDomToNode(Element element)
    {
        return parseDomToNode(element, null);
    }

    private static Node parseDomToNode(Element element, Node parent)
    {
        Node node = new Node();
        node.setName(element.getName());
        if (!$.isNull(parent))
        {
            parent.getChildren().add(node);
        }
        List<Node> nodeChildren = new LinkedList<>();
        List<Element> children = element.getChildren();
        if ($.isEmpty(children))
        {
            node.setIsContainer(false);
            node.setValue(element.getValue());
        }
        else
        {
            node.setIsContainer(true);
            for (Element child : children)
            {
                nodeChildren.add(parseDomToNode(child, node));
            }
        }
        node.setChildren(nodeChildren);
        return node;
    }

    public static Object createObjectWithNode(Node node)
    {
        String xml = node.toXml();
        return XSTREAM.fromXML(xml);
    }

    /**
     * Find a node by key. The key represents the requested node in its hierarchy
     * within the rootNode. Example of a valid key for child2: rootNodeName.child1.child2
     */
    public static Node findNodeInTree(String key, Node rootNode)
    {
        return findNodeInTree(key, rootNode, null);
    }

    public static Document xmlToDocument(String xml)
    {
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try
        {
            document = builder.build(new StringReader(xml));
        }
        catch (JDOMException e)
        {
            throw new RuntimeException("Cant parse xml", e);
        }
        catch (IOException e)
        {
            throw new RuntimeException("IO Problems with xml", e);
        }
        return document;
    }

    protected static Node findNodeInTree(String key, Node node, LinkedList<Node> history)
    {
        Node found = null;
        if ($.isNull(history))
        {
            history = new LinkedList<>();
        }
        history.add(node);
        String historyKey = generateKey(history);
        if (key.equals(historyKey))
        {
            found = node;
        }
        else if (key.contains(historyKey))
        {
            if (!$.isEmpty(node.getChildren()))
            {
                for (Node child : node.getChildren())
                {
                    found = findNodeInTree(key, child, history);
                    if (!$.isNull(found))
                    {
                        break;
                    }
                }
            }
        }
        else
        {
            history.removeLast();
        }
        return found;
    }

    private static String generateKey(LinkedList<Node> history)
    {
        StringBuffer keyBuffer = new StringBuffer();
        history.forEach(node -> keyBuffer.append("." + node.getName()));
        String key = keyBuffer.toString();
        return key.length() > 0 ? key.substring(1) : null;
    }
}
