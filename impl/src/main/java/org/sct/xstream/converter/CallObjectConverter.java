package org.sct.xstream.converter;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sct.call.CallObject;
import org.sct.xstream.exclusion.FieldExclusionReader;
import org.sct.xstream.exclusion.FieldExclusionWriter;
import org.sct.xstream.exclusion.ReaderContext;
import org.sct.xstream.exclusion.WriterContext;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.path.Path;

/**
 * XStream converter which is capable to (un-) marshal {@code CallObject} instances by using
 * exclusion filters.
 * 
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 * @see FieldExclusionReader
 * @see FieldExclusionWriter
 * 
 */
public class CallObjectConverter extends ConverterWrapper {
    
    private static final Logger LOG = LoggerFactory.getLogger(CallObjectConverter.class);

    public CallObjectConverter(Converter converter) {
        super(converter);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public boolean canConvert(Class type) {
        return type.isAssignableFrom(CallObject.class);
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader reader,
            final UnmarshallingContext context) {

        CallObject callObj = null;

        if (reader.underlyingReader() instanceof FieldExclusionReader) {

            FieldExclusionReader exclusionReader = (FieldExclusionReader) reader.underlyingReader();
            ReaderContext ctx = exclusionReader.getContext();
            ctx.setExclusions(new LinkedList<Path>());

            /*
             * Apply PathTracker to track currently unmarshaled position
             * inside CallObject in XML DOM
             */
            ctx.getPathTrackerContext().startTracker();
            LOG.debug("Umarshaling known field CallObject");
            
            callObj = (CallObject) super.unmarshal(exclusionReader, context);
            ctx.getPathTrackerContext().stopTracker();
            
            LOG.debug("{} exclusion filters parsed.",
                    ctx.getExclusions() != null ? ctx.getExclusions().size() : 0);
            
            callObj.setFieldExclusion(ctx.getExclusions());
        }
        else {
            callObj = (CallObject) super.unmarshal(reader, context);
        }
        return callObj;
    }

    @Override
    public void marshal(Object original, final HierarchicalStreamWriter writer,
            final MarshallingContext context) {

        CallObject callObj = (CallObject) original;

        if (writer.underlyingWriter() instanceof FieldExclusionWriter) {

            FieldExclusionWriter exclusionWriter = (FieldExclusionWriter) writer.underlyingWriter();
            WriterContext ctx = exclusionWriter.getContext();
            ctx.setExclusions(callObj.getFieldExclusion());
            
            LOG.debug("Marshalling known field CallObject using {} exclusion filters."
                    , ctx.getExclusions() != null ? ctx.getExclusions().size() : 0);

            /*
             * Apply PathTracker to track currently marshaled position
             * inside CallObject in XML DOM
             */
            ctx.getPathTrackerContext().startTracker();
            super.marshal(original, writer, context);
            ctx.getPathTrackerContext().stopTracker();
        }
        else {
            super.marshal(original, writer, context);
        }
    }
}
