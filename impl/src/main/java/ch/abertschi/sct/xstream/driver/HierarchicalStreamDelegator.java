package ch.abertschi.sct.xstream.driver;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 */
public class HierarchicalStreamDelegator extends HierarchicalStreamDriverWrapper {

    public HierarchicalStreamDelegator(HierarchicalStreamDriver hierarchicalStreamDriver) {
        super(hierarchicalStreamDriver);
    }

    protected HierarchicalStreamReader createReader(HierarchicalStreamReader reader) {
        return reader;
    }
    
    protected HierarchicalStreamWriter createWriter(HierarchicalStreamWriter writer) {
        return writer;
    }

    @Override
    public HierarchicalStreamReader createReader(Reader in) {
        return createReader(super.createReader(in));
    }

    @Override
    public HierarchicalStreamReader createReader(InputStream in) {
        return createReader(super.createReader(in));
    }

    @Override
    public HierarchicalStreamReader createReader(URL in) {
        return createReader(super.createReader(in));
    }

    @Override
    public HierarchicalStreamReader createReader(File in) {
        return createReader(super.createReader(in));
    }

    @Override
    public HierarchicalStreamWriter createWriter(Writer out) {
        return createWriter(super.createWriter(out));
    }

    @Override
    public HierarchicalStreamWriter createWriter(OutputStream out) {
        return createWriter(super.createWriter(out));
    }
}
