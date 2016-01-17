package org.sct.call;

import org.junit.Assert;
import org.junit.Test;

import org.sct.helper.TestHelp;
import org.sct.xstream.XStreamProvider;
import com.thoughtworks.xstream.XStream;

public class CallCollectionTest {

    private XStream exclusionXstream = XStreamProvider.INSTANCE.createExclusionXStream();

    private XStream plainXstream = XStreamProvider.INSTANCE.createPlainXStream();

    private static final String CORBA_SINGLE_CALL_MARKED = "org/sct/call/corba-single-call-exclude-filter.xml";

    private static final String CORBA_SINGLE_CALL = "org/sct/call/corba-single-call.xml";

    //@Test
    public void test_get_from_collection() {
        Call storedCall = (Call) exclusionXstream.fromXML(TestHelp.getTestResource(CORBA_SINGLE_CALL_MARKED));
        CallCollection collection = new CallCollection(exclusionXstream);
        collection.add(storedCall);
        
        Call requestCall = (Call) plainXstream.fromXML(TestHelp.getTestResource(CORBA_SINGLE_CALL));

        Object fromFile = collection.get(requestCall.getRequest().getObject());
        Assert.assertTrue("Rueckgabe nicht gefunden in XML datei, obwohl haette gefunden werden!"
                , TestHelp.theSame(fromFile, requestCall.getResponse().getObject()));
    }
}
