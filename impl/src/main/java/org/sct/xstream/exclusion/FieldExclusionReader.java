package org.sct.xstream.exclusion;

import org.sct.util.SctException;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.ReaderWrapper;

/**
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 */
public class FieldExclusionReader extends ReaderWrapper {

    /*
     * TODO: Enable to change
     */
    private static final String EXCLUDE_STR = "%ANY%";

    private ReaderContext context;

    public FieldExclusionReader(HierarchicalStreamReader reader, ReaderContext context) {
        super(reader);
        this.context = context;
    }

    @Override
    public String getValue() {
        String s = super.getValue();
        
        if (EXCLUDE_STR.equals(s)) {
            
            if (null == context.getFieldType()) {
                throw new SctException("No reflection information is available for current value " + s);
            }
            
            // Add field to exclusion list
            if (context != null ) {
                context.getExclusions().add(context.getPathTrackerContext().getTracker().getPath());
            }
            
            s = new PureJavaReflectionProvider().newInstance(context.getFieldType()).toString();
            context.setFieldType(null);
        }
        return s;
    }
    
    @Override
    public HierarchicalStreamReader underlyingReader() {
        return this;
    }

    public ReaderContext getContext() {
        return context;
    }
}
