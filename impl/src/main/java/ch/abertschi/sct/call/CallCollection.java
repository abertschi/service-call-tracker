package ch.abertschi.sct.call;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author Andrin Bertschi
 * @since 30.06.2014
 */
@XStreamAlias("calls")
public class CallCollection {

    @XStreamImplicit
    private List<Call> collection;

    @XStreamOmitField
    private XStream xstream;

    public CallCollection(XStream xstream) {
        this(xstream, null);
    }

    public CallCollection(XStream xstream, List<Call> calls) {
        this.collection = createList(calls);
        this.xstream = xstream;
    }

    /**
     * 
     * @param request
     * @param response
     */
    public void put(Object request, Object response) {
        CallObject reqObj = new CallObject(request);
        CallObject respObj = new CallObject(response);
        Call call = new Call(reqObj, respObj);
        collection.add(call);
    }
    
    /**
     * 
     * @param call
     */
    public void add(Call call) {
        collection.add(call);
    }

    /**
     * 
     * @param request
     * @return
     */
    public boolean contains(Object request) {
        return (get(request) == null) ? false : true;
    }

    /**
     * 
     * @param key
     * @return
     */
    public Object get(Object key) {
        CallComparator comparator = new CallComparator(xstream);
        for (Call call : collection) {
            if (comparator.compareRequestWithCallObject(key, call.getRequest())) {
                return call.getResponse().getObject();
            }
        }
        return null;
    }

    public List<Call> getCollection() {
        return collection;
    }

    public void setCollection(List<Call> collection) {
        this.collection = collection;
    }

    private List<Call> createList(List<Call> calls) {
        if (calls == null) {
            return new LinkedList<>();
        }
        else {
            return new LinkedList<>(calls);
        }
    }
}
