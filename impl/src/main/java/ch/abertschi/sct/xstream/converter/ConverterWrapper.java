package ch.abertschi.sct.xstream.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Converter delegate.
 * 
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 */
public abstract class ConverterWrapper implements Converter {

    private Converter converter;

    public ConverterWrapper(Converter converter) {
        this.converter = converter;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return converter.canConvert(type);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        converter.marshal(source, writer, context);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return converter.unmarshal(reader, context);
    }

}
