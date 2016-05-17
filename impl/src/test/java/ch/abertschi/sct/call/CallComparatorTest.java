//package ch.abertschi.sct.call;
//
//import ch.abertschi.sct.newp.serial.CallComparator;
//import ch.abertschi.sct.newp.serial.CallObject;
//import org.junit.Assert;
//import org.junit.Test;
//
//import ch.abertschi.sct.domain.Customer;
//import ch.abertschi.sct.xstream.XStreamProvider;
//import com.thoughtworks.xstream.XStream;
//
//public class CallComparatorTest {
//
//    /*
//     * Customer with name 'andrin', year is 1994
//     */
//    final static String XML_NO_EXCLUSION =
//            "<callobject>"
//                    + "<object class=\"ch.abertschi.sct.domain.Customer\"><name>andrin</name><yearOfBirth>1994</yearOfBirth></object>"
//                    + "</callobject>";
//
//
//    /*
//     * Customer with any name, Year is 1994
//     */
//    final static String XML_NAME_EXCLUSION =
//            "<callobject>"
//                    + "<object class=\"ch.abertschi.sct.domain.Customer\"><name>%ANY%</name><yearOfBirth>1994</yearOfBirth></object>"
//                    + "</callobject>";
//
//    /*
//     * Customer with name 'andrin', any year
//     */
//    final static String XML_YEAR_EXCLUSION =
//            "<callobject>"
//                    + "<object class=\"ch.abertschi.sct.domain.Customer\"><name>andrin</name><yearOfBirth>%ANY%</yearOfBirth></object>"
//                    + "</callobject>";
//
//    private CallComparator comparator;
//
//    private XStream xstream;
//
//    public CallComparatorTest() {
//        xstream = XStreamProvider.INSTANCE.createExclusionXStream();
//        comparator = new CallComparator(xstream);
//    }
//
//    @Test
//    public void test_exclude_nothing() {
//        Customer c = new Customer("andrin", 1994);
//        assertTrue(XML_NO_EXCLUSION, c);
//
//        c = new Customer("nicht-korrekter-name", 1994);
//        assertFalse(XML_NO_EXCLUSION, c);
//
//        c = new Customer("", 1994);
//        assertFalse(XML_NO_EXCLUSION, c);
//
//        c = new Customer(null, 1994);
//        assertFalse(XML_NO_EXCLUSION, c);
//
//        c = new Customer("nicht-korrekter-name", 0);
//        assertFalse(XML_NO_EXCLUSION, c);
//    }
//
//    @Test
//    public void test_exclude_name() {
//        Customer c = new Customer("andrin", 1994);
//        assertTrue(XML_NAME_EXCLUSION, c);
//
//        c = new Customer("any-name", 1994);
//        assertTrue(XML_NAME_EXCLUSION, c);
//
//        c = new Customer("", 1994);
//        assertTrue(XML_NAME_EXCLUSION, c);
//
//        c = new Customer(null, 1994);
//        assertTrue(XML_NAME_EXCLUSION, c);
//
//        c = new Customer("any-name", 0);
//        assertFalse(XML_NAME_EXCLUSION, c);
//    }
//
//    @Test
//    public void test_exclude_year() {
//        Customer c = new Customer("andrin", 0);
//        assertTrue(XML_YEAR_EXCLUSION, c);
//
//        c = new Customer("andrin", 1994);
//        assertTrue(XML_YEAR_EXCLUSION, c);
//    }
//
//    private boolean compareObject(String xml, Object request) {
//        return comparator.compareRequestWithCallObject(request, (CallObject) xstream.fromXML(xml));
//    }
//
//    private void assertTrue(String xml, Object request) {
//        Assert.assertTrue(compareObject(xml, request));
//    }
//
//    private void assertFalse(String xml, Object request) {
//        Assert.assertFalse(compareObject(xml, request));
//    }
//}
