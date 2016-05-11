package ch.abertschi.sct.transformer;

import ch.abertschi.sct.parse.ParserCall;
import ch.abertschi.sct.parse.ParserContext;
import com.github.underscore.$;

/**
 * Created by abertschi on 11/05/16.
 */
public class CallContext
{
    private ParserCall storageCall;

    private Object requestObject;

    private ParserContext storage;

    public CallContext()
    {
    }

    public Object getRequestObject()
    {
        return requestObject;
    }

    public CallContext setRequestObject(Object requestObject)
    {
        this.requestObject = requestObject;
        return this;
    }

    public ParserContext getStorage()
    {
        return storage;
    }

    public CallContext setStorage(ParserContext storage)
    {
        this.storage = storage;
        return this;
    }

    public ParserCall getStorageCall()
    {
        return storageCall;
    }

    public CallContext setStorageCall(ParserCall storageCall)
    {
        this.storageCall = storageCall;
        return this;
    }

    public boolean hasStorageCallRequest()
    {
        return !$.isNull(storageCall) && !$.isNull(storageCall.getRequest());
    }

    public boolean hasStorageCallResponse()
    {
        return !$.isNull(storageCall) && !$.isNull(storageCall.getResponse());
    }

}
