package org.sct.call;

import org.junit.Assert;
import org.junit.Test;

import org.sct.domain.Customer;
import org.sct.xstream.XStreamProvider;
import com.thoughtworks.xstream.XStream;

public class CallComparatorTest {

    /*
     * Customer with name 'andrin', year is 1994
     */
    final static String XML_NO_EXCLUSION =
            "<CallObject>"
                    + "<object class=\"Customer\"><name>andrin</name><yearOfBirth>1994</yearOfBirth></object>"
                    + "</CallObject>";


    /*
     * Customer with any name, Year is 1994
     */
    final static String XML_NAME_EXCLUSION =
            "<CallObject>"
                    + "<object class=\"Customer\"><name>%ANY%</name><yearOfBirth>1994</yearOfBirth></object>"
                    + "</CallObject>";

    /*
     * Customer with name 'andrin', any year
     */
    final static String XML_YEAR_EXCLUSION =
            "<CallObject>"
                    + "<object class=\"Customer\"><name>andrin</name><yearOfBirth>%ANY%</yearOfBirth></object>"
                    + "</CallObject>";

    private CallComparator comparator;

    private XStream xstream;

    public CallComparatorTest() {
        xstream = XStreamProvider.INSTANCE.createExclusionXStream();
        comparator = new CallComparator(xstream);
    }

    @Test
    public void test_exclude_nothing() {
        Customer c = new Customer("andrin", 1994);
        assertTrue(XML_NO_EXCLUSION, c);

        c = new Customer("nicht-korrekter-name", 1994);
        assertFalse(XML_NO_EXCLUSION, c);

        c = new Customer("", 1994);
        assertFalse(XML_NO_EXCLUSION, c);

        c = new Customer(null, 1994);
        assertFalse(XML_NO_EXCLUSION, c);

        c = new Customer("nicht-korrekter-name", 0);
        assertFalse(XML_NO_EXCLUSION, c);
    }

    @Test
    public void test_exclude_name() {
        Customer c = new Customer("andrin", 1994);
        assertTrue(XML_NAME_EXCLUSION, c);

        c = new Customer("any-name", 1994);
        assertTrue(XML_NAME_EXCLUSION, c);

        c = new Customer("", 1994);
        assertTrue(XML_NAME_EXCLUSION, c);

        c = new Customer(null, 1994);
        assertTrue(XML_NAME_EXCLUSION, c);

        c = new Customer("any-name", 0);
        assertFalse(XML_NAME_EXCLUSION, c);
    }

    @Test
    public void test_exclude_year() {
        Customer c = new Customer("andrin", 0);
        assertTrue(XML_YEAR_EXCLUSION, c);

        c = new Customer("andrin", 1994);
        assertTrue(XML_YEAR_EXCLUSION, c);
    }

    private boolean compareObject(String xml, Object request) {
        CallObject fromXml = (CallObject) xstream.fromXML(xml);
        return comparator.compareRequestWithCallObject(request, fromXml);
    }

    private void assertTrue(String xml, Object request) {
        Assert.assertTrue(compareObject(xml, request));
    }

    private void assertFalse(String xml, Object request) {
        Assert.assertFalse(compareObject(xml, request));
    }
}
