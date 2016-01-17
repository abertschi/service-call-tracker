package org.sct.xstream.converter;

import java.lang.reflect.Field;

import org.sct.xstream.exclusion.FieldExclusionWriter;
import org.sct.xstream.exclusion.ReaderContext;
import org.sct.xstream.exclusion.WriterContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sct.xstream.exclusion.FieldExclusionReader;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * XStream converter which provides reflection information for {@link ReaderContext} and
 * {@link WriterContext}.
 * 
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 */
public class ExclusionReflectionConverter extends ReflectionConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ExclusionReflectionConverter.class);

    private static final String EXCLUSION_READER_CTX = "EXCL_READ_CTX";

    private static final String EXCLUSION_WRITER_CTX = "EXCL_WRTR_CTX";

    public ExclusionReflectionConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
        super(mapper, reflectionProvider);
    }

    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return super.canConvert(type);
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader reader,
            final UnmarshallingContext context) {

        if (reader.underlyingReader() instanceof FieldExclusionReader) {
            FieldExclusionReader exclusionReader = (FieldExclusionReader) reader.underlyingReader();
            context.put(EXCLUSION_READER_CTX, exclusionReader.getContext());
        }

        return super.unmarshal(reader, context);
    }

    @Override
    protected Object unmarshallField(final UnmarshallingContext context, final Object result,
            @SuppressWarnings("rawtypes") Class type, Field field) {

        ReaderContext readerContext = (ReaderContext) context.get(EXCLUSION_READER_CTX);

        /*
         * In case of an XML value is marked as ignored field, the field can't be parsed.
         * (For example - Marker "%ANY% doesn't fit into an integer field)
         * The corresponding reader needs reflection information about the marked field.
         */
        if (readerContext != null) {
            readerContext.setFieldType(type);
            LOG.trace("Reflection information for field {} provied", type);
        }
        Object unmarshalledField = super.unmarshallField(context, result, type, field);

        LOG.trace(readerContext.getPathTrackerContext().getTracker().getPath().toString());

        return unmarshalledField;
    }

    @Override
    public void marshal(Object original, final HierarchicalStreamWriter writer,
            final MarshallingContext context) {

        if (writer.underlyingWriter() instanceof FieldExclusionWriter) {
            FieldExclusionWriter exclusionWriter = (FieldExclusionWriter) writer.underlyingWriter();
            context.put(EXCLUSION_WRITER_CTX, exclusionWriter.getContext());
        }

        super.marshal(original, writer, context);
    }

    @Override
    protected void marshallField(final MarshallingContext context, Object newObj, Field field) {
        WriterContext writerContext = (WriterContext) context.get(EXCLUSION_WRITER_CTX);

        if (writerContext != null)
            LOG.trace(writerContext.getPathTrackerContext().getTracker().getPath().toString());

        super.marshallField(context, newObj, field);
    }
}
