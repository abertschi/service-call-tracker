package org.sct.xstream.driver;

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
public abstract class HierarchicalStreamDriverWrapper implements HierarchicalStreamDriver{

    private HierarchicalStreamDriver hierarchicalStreamDriver;

    public HierarchicalStreamDriverWrapper(HierarchicalStreamDriver hierarchicalStreamDriver) {
        this.hierarchicalStreamDriver = hierarchicalStreamDriver;
    }

    @Override
    public HierarchicalStreamReader createReader(Reader in) {
        return hierarchicalStreamDriver.createReader(in);
    }

    @Override
    public HierarchicalStreamReader createReader(InputStream in) {
        return hierarchicalStreamDriver.createReader(in);
    }

    @Override
    public HierarchicalStreamReader createReader(URL in) {
        return hierarchicalStreamDriver.createReader(in);
    }

    @Override
    public HierarchicalStreamReader createReader(File in) {
        return hierarchicalStreamDriver.createReader(in);
    }

    @Override
    public HierarchicalStreamWriter createWriter(Writer out) {
        return hierarchicalStreamDriver.createWriter(out);
    }

    @Override
    public HierarchicalStreamWriter createWriter(OutputStream out) {
        return hierarchicalStreamDriver.createWriter(out);
    }
}
