//package org.sct.marshal;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import org.sct.$.SctXStream;
//import org.sct.$.tmp.ServiceCall;
//import CallCollection;
//import TestHelp;
//import org.sct.serialize.SerializerImpl;
//import com.thoughtworks.xstream.XStream;
//
///**
// * Unittests for {@link SerializerImpl}.
// * 
// * @author Andrin Bertschi
// */
//public class SerializerImplTest {
//    
//    XStream x = new SctXStream().getPlainXstream();
//
//    public void test_marshal_and_unmarshall_happycase() throws MalformedURLException {
//        List<ServiceCall> toMarshal = TestHelp.createRandomCalls(5);
//        URL url = TestHelp.createTempFile("serializer_impl_marshall_happycase", "xml");
//        SerializerImpl serial = new SerializerImpl(new SctXStream().getExcludeXstream());
//        serial.marshal(toMarshal, url);
//        List<ServiceCall> unmarshaled =  serial.unmarshal(url);
//        Assert.assertTrue(TestHelp.theSame(toMarshal, unmarshaled));
//    }
//    
//    
//    @Test
//    public void test_marshal() throws MalformedURLException {
//        CInOp1 in = TestHelp.createRandom(CInOp1.class);
//        CInOp1 out = TestHelp.createRandom(CInOp1.class);
//        CallCollection collection = new CallCollection();
//        collection.put(in, out);
//         
//        URL url = TestHelp.createTempFile("test_marshal", "xml");
//        SerializerImpl serial = new SerializerImpl(new SctXStream().getExcludeXstream());
//        serial.marshal(collection.getCollection(), url);
//        System.out.println(url);
//    }
//    
//    
//}
