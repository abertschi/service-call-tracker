package ch.abertschi.sct.xstream.exclusion;

import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.WriterWrapper;

/**
 * Writer capable excluding XML tags.
 * 
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 */
public class FieldExclusionWriter extends WriterWrapper {

    private WriterContext context;

    public FieldExclusionWriter(HierarchicalStreamWriter writer, WriterContext context) {
        super(writer);
        this.context = context;
    }

    public boolean ignoreNode() {
        return this.context.isCurrentElementExcluded();
    }

    @Override
    public HierarchicalStreamWriter underlyingWriter() {
        return this;
    }

    @Override
    public void startNode(String name) {
        if (!ignoreNode()) {
            wrapped.startNode(name);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void startNode(String name, Class clazz) {
        if (!ignoreNode()) {
            ((ExtendedHierarchicalStreamWriter) wrapped).startNode(name, clazz);
        }
    }

    @Override
    public void endNode() {
        if (!ignoreNode()) {
            wrapped.endNode();
        }
    }

    @Override
    public void addAttribute(String key, String value) {
        if (!ignoreNode()) {
            wrapped.addAttribute(key, value);
        }
    }

    @Override
    public void setValue(String text) {
        if (!ignoreNode()) {
            wrapped.setValue(text);
        }
    }

    public WriterContext getContext() {
        return context;
    }
}
