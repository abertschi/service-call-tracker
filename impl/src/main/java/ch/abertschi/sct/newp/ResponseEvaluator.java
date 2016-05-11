package ch.abertschi.sct.newp;

import ch.abertschi.sct.newp.transformer.*;
import com.github.underscore.$;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abertschi on 11/05/16.
 */
public class ResponseEvaluator
{
    private List<Transformer> transformers;

    private StorageCall call;

    private CallContext context;

    public ResponseEvaluator(CallContext context, StorageCall call)
    {
        this.call = call;
        this.context = context;
        this.transformers = getResponseTransformers();
    }


    public Object evaluate()
    {
        StorageCallResponse response = call.getResponse();

        if (isStacktrace(response))
        {
            response.setStacktrace(Transformers.transform(response.getStacktrace(), transformers, context));
        }
        if (isPayload(response))
        {
            Transformers.transform(response.getPayloadNode(), transformers, context);
        }
        if (isScript(response))
        {
            ScriptInterpreter interpreter = new ScriptInterpreter(context, call);
        }

        return null;
    }

    protected List<Transformer> getResponseTransformers()
    {
        List<Transformer> transformers = new ArrayList<>();
        transformers.add(new VariableTransformer());
        transformers.add(new FieldReferenceTransformer());
        return transformers;
    }

    private boolean isScript(StorageCallResponse r)
    {
        return !$.isNull(r.getScript()) && !r.getScript().trim().isEmpty();
    }

    private boolean isStacktrace(StorageCallResponse r)
    {
        return !$.isNull(r.getStacktrace()) && !r.getStacktrace().trim().isEmpty();
    }

    private boolean isPayload(StorageCallResponse r)
    {
        return !$.isNull(r.getPayloadType())
                && !r.getPayloadType().trim().isEmpty()
                && !$.isNull(r.getPayloadNode());

    }
}
