package org.sct;

import java.net.URL;
import java.util.List;

import org.sct.api.SctConfiguration;
import org.sct.call.CallCollection;
import org.sct.call.Call;
import org.sct.xstream.XStreamProvider;
import com.thoughtworks.xstream.XStream;

/**
 * @see CallPersistence
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 * 
 */
public class CallPersistenceImpl implements CallPersistence {

    private Serializer service;

    private SctContext context;

    private XStream xstream;

    public CallPersistenceImpl(SctContext ctx) {
        this.xstream = XStreamProvider.INSTANCE.createExclusionXStream();
        this.service = new SerializerImpl(xstream);
        this.context = ctx;
    }

    @Override
    public Object load(Object in) {
        URL where = getConfig().getResponseLoadingUrl();
        CallCollection collection = new CallCollection(xstream, get(where));
        Object out = collection.get(in);
        if (out != null) {
            return out;
        }
        else {
            throw new RuntimeException("Response not found in stored calls");
        }
    }

    @Override
    public void record(Object in, Object out) {
        URL where = getConfig().getCallRecordingUrl();
        CallCollection collection = new CallCollection(xstream, get(where));
        collection.put(in, out);
        this.service.marshal(collection, where);
    }

    protected List<Call> get(URL url) {
        CallCollection c = this.service.unmarshal(url);
        return c != null ? c.getCollection(): null;
    }

    private SctConfiguration getConfig() {
        return this.context.getConfiguration();
    }

}
