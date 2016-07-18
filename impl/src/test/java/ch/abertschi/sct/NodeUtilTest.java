package ch.abertschi.sct;

import ch.abertschi.sct.domain.SuperCustomer;
import ch.abertschi.sct.node.Node;
import ch.abertschi.sct.node.NodeUtils;
import ch.abertschi.sct.parse.XStreamProvider;
import com.thoughtworks.xstream.XStream;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by abertschi on 18/07/16.
 */
public class NodeUtilTest
{

    @Test
    public void test()
    {
        SuperCustomer c = new SuperCustomer("name", "comment3");
        c.setNickname("nickname");
        c.setYearOfBirth(1994);

        Node node = NodeUtils.createNodeFromObject(c);

        String xml = "<ch.abertschi.sct.node.Node>\n" +
                "  <name>ch.abertschi.sct.domain.SuperCustomer</name>\n" +
                "  <isContainer>true</isContainer>\n" +
                "  <children class=\"linked-list\">\n" +
                "    <ch.abertschi.sct.node.Node>\n" +
                "      <name>name</name>\n" +
                "      <value>name</value>\n" +
                "      <isContainer>false</isContainer>\n" +
                "      <children class=\"linked-list\"/>\n" +
                "    </ch.abertschi.sct.node.Node>\n" +
                "    <ch.abertschi.sct.node.Node>\n" +
                "      <name>yearOfBirth</name>\n" +
                "      <value>1994</value>\n" +
                "      <isContainer>false</isContainer>\n" +
                "      <children class=\"linked-list\"/>\n" +
                "    </ch.abertschi.sct.node.Node>\n" +
//                "    <ch.abertschi.sct.node.Node>\n" +
//                "      <name>comment</name>\n" +
//                "      <value>comment</value>\n" +
//                "      <isContainer>false</isContainer>\n" +
//                "      <children class=\"linked-list\"/>\n" +
//                "    </ch.abertschi.sct.node.Node>\n" +
                "    <ch.abertschi.sct.node.Node>\n" +
                "      <name>nickname</name>\n" +
                "      <value>.*</value>\n" +
                "      <isContainer>false</isContainer>\n" +
                "      <children class=\"linked-list\"/>\n" +
                "    </ch.abertschi.sct.node.Node>\n" +
                "  </children>\n" +
                "</ch.abertschi.sct.node.Node>";

        XStream x = XStreamProvider.GET.createPreConfiguredXStream();
        Node xmlNode = (Node) x.fromXML(xml);

        Assert.assertTrue(NodeUtils.doesNodeMatchWithObject(xmlNode, c));
    }

}
