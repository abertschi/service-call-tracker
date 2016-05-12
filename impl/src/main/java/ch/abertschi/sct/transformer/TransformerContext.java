package ch.abertschi.sct.transformer;

import ch.abertschi.sct.parse.ParserCall;
import ch.abertschi.sct.parse.ParserContext;
import com.github.underscore.$;

/**
 * Created by abertschi on 11/05/16.
 */
public class TransformerContext
{
    private ParserCall call;

    private Object currentRequest;

    private ParserContext parserContext;

    public Object getCurrentRequest()
    {
        return currentRequest;
    }

    public TransformerContext setCurrentRequest(Object currentRequest)
    {
        this.currentRequest = currentRequest;
        return this;
    }

    public ParserContext getParserContext()
    {
        return parserContext;
    }

    public TransformerContext setParserContext(ParserContext parserContext)
    {
        this.parserContext = parserContext;
        return this;
    }

    public ParserCall getCall()
    {
        return call;
    }

    public TransformerContext setCall(ParserCall call)
    {
        this.call = call;
        return this;
    }

    public boolean hasStorageCallRequest()
    {
        return !$.isNull(call) && !$.isNull(call.getRequest());
    }

    public boolean hasStorageCallResponse()
    {
        return !$.isNull(call) && !$.isNull(call.getResponse());
    }

}
