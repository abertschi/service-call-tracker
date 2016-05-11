package ch.abertschi.sct.newp.parse;

/**
 * Created by abertschi on 11/05/16.
 */
public class StorageCall
{
    private StorageCallRequest request = new StorageCallRequest();

    private StorageCallResponse response = new StorageCallResponse();

    public StorageCallRequest getRequest()
    {
        return request;
    }

    public StorageCall setRequest(StorageCallRequest request)
    {
        this.request = request;
        return this;
    }

    public StorageCallResponse getResponse()
    {
        return response;
    }

    public StorageCall setResponse(StorageCallResponse response)
    {
        this.response = response;
        return this;
    }
}
