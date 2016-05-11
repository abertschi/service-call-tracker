package ch.abertschi.sct.newp.transformer;

import ch.abertschi.sct.newp.parse.StorageCall;
import ch.abertschi.sct.newp.parse.StorageContext;
import com.github.underscore.$;

/**
 * Created by abertschi on 11/05/16.
 */
public class CallContext
{
    private StorageCall storageCall;

    private Object requestObject;

    private StorageContext storage;

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

    public StorageContext getStorage()
    {
        return storage;
    }

    public CallContext setStorage(StorageContext storage)
    {
        this.storage = storage;
        return this;
    }

    public StorageCall getStorageCall()
    {
        return storageCall;
    }

    public CallContext setStorageCall(StorageCall storageCall)
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
