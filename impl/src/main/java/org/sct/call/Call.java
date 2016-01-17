package org.sct.call;

/**
 * POJO representing a service call with request and response.
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 * 
 */
public class Call {

    private CallObject request;

    private CallObject response;

    public Call() {
    }

    public Call(CallObject request, CallObject response) {
        this.request = request;
        this.response = response;
    }

    public CallObject getRequest() {
        return request;
    }

    public void setRequest(CallObject request) {
        this.request = request;
    }

    public CallObject getResponse() {
        return response;
    }

    public void setResponse(CallObject response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Call [request=" + request + ", response=" + response + "]";
    }

}
