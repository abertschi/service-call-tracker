package ch.abertschi.sct.newp;

import com.github.underscore.$;

import java.util.LinkedList;

/**
 * Created by abertschi on 11/05/16.
 */
public class NodeUtils
{
    public static Node findNode(String key, Node rootNode)
    {
        return findNode(key, rootNode, null);
    }

    protected static Node findNode(String key, Node node, LinkedList<Node> history)
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
                    found = findNode(key, child, history);
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
