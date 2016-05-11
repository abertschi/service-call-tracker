package ch.abertschi.sct.newp.transformer;

import ch.abertschi.sct.newp.StorageCall;
import ch.abertschi.sct.newp.StorageContext;

/**
 * Created by abertschi on 11/05/16.
 */
public class CallContext
{
    private StorageCall storageCall;

    private Object requestObject;

    private StorageContext storage;

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

    public CallContext()
    {
    }
}
