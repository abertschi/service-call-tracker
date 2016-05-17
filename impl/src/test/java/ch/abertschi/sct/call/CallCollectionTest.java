//package ch.abertschi.sct.call;
//
//import ch.abertschi.sct.newp.serial.Call;
//import ch.abertschi.sct.newp.serial.CallCollection;
//import ch.abertschi.sct.serial.XStreamProvider;
//import org.junit.Assert;
//
//import ch.abertschi.sct.helper.TestHelp;
//import ch.abertschi.sct.xstream.XStreamProvider;
//import com.thoughtworks.xstream.XStream;
//
//// todo
//public class CallCollectionTest {
//
//
//    private XStream plainXstream = XStreamProvider.INSTANCE.createPlainXStream();
//
//    private static final String CORBA_SINGLE_CALL_MARKED = "org/sct/call/corba-single-call-exclude-filter.xml";
//
//    private static final String CORBA_SINGLE_CALL = "org/sct/call/corba-single-call.xml";
//
//    //@Test
//    public void test_get_from_collection() {
//        Call storedCall = (Call) exclusionXstream.fromXML(TestHelp.getTestResource(CORBA_SINGLE_CALL_MARKED));
//        CallCollection collection = new CallCollection(exclusionXstream);
//        collection.add(storedCall);
//
//        Call requestCall = (Call) plainXstream.fromXML(TestHelp.getTestResource(CORBA_SINGLE_CALL));
//
//        Object fromFile = collection.get(requestCall.getRequest().getObject());
//        Assert.assertTrue("Rueckgabe nicht gefunden in XML datei, obwohl haette gefunden werden!"
//                , TestHelp.theSame(fromFile, requestCall.getResponse().getObject()));
//    }
//}
