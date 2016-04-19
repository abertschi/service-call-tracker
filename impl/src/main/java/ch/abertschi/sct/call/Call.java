package ch.abertschi.sct.call;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * POJO representing a service call with request and response.
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 * 
 */
@XStreamAlias("call")
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
