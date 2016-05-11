package ch.abertschi.sct.newp;

/**
 * Created by abertschi on 11/05/16.
 */
public class TransformingContext
{
    private StorageCall storageCall;

    public StorageCall getStorageCall()
    {
        return storageCall;
    }

    public TransformingContext setStorageCall(StorageCall storageCall)
    {
        this.storageCall = storageCall;
        return this;
    }

    public Object getRequestObject()
    {
        return requestObject;
    }

    public TransformingContext setRequestObject(Object requestObject)
    {
        this.requestObject = requestObject;
        return this;
    }

    private Object requestObject;

    public TransformingContext()
    {

    }
}
