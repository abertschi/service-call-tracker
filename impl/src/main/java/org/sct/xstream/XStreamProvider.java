package org.sct.xstream;

import org.sct.xstream.converter.CallObjectConverter;
import org.sct.xstream.converter.ExclusionReflectionConverter;
import org.sct.xstream.driver.HierarchicalStreamDelegator;
import org.sct.xstream.exclusion.FieldExclusionReader;
import org.sct.xstream.exclusion.FieldExclusionWriter;
import org.sct.xstream.exclusion.ReaderContext;
import org.sct.xstream.exclusion.WriterContext;
import org.sct.xstream.path.PathTracker;
import org.sct.xstream.path.PathTrackerContext;
import org.sct.xstream.path.PathTrackerReader;
import org.sct.xstream.path.PathTrackerWriter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * XStream provider for {@code sct} capable serialization.
 * 
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 */
public enum XStreamProvider {

    INSTANCE;
    
    /**
     * @return Create plain xstream instance without additional converters.
     */
    public XStream createPlainXStream() {
        XStream xstream = new XStream();
        applyDefaultConfig(xstream);
        return xstream;
    }
    
    /**
     * @return Create xstream instance with {@code sct} capable converters.
     * @see CallObjectConverter
     * @see ExclusionReflectionConverter
     */
    public XStream createExclusionXStream() {
        HierarchicalStreamDriver driver = createExclusionDriver();
        XStream xstream = new XStream(driver);
        
        ExclusionReflectionConverter reflectionConverter = new ExclusionReflectionConverter(
                xstream.getMapper(), xstream.getReflectionProvider());
        
        CallObjectConverter callObjConverter = new CallObjectConverter(reflectionConverter);
        
        xstream.registerConverter(callObjConverter);
        xstream.registerConverter(reflectionConverter, XStream.PRIORITY_LOW);
        applyDefaultConfig(xstream);
        return xstream;
    }

    private void applyDefaultConfig(XStream xstream) {
        xstream.autodetectAnnotations(true);
        xstream.setClassLoader(Thread.currentThread().getContextClassLoader());
    }

    private HierarchicalStreamDriver createExclusionDriver() {
        HierarchicalStreamDriver driver = new HierarchicalStreamDelegator(new XppDriver()) {

            @Override
            protected HierarchicalStreamReader createReader(HierarchicalStreamReader reader) {
                PathTracker tracker = new PathTracker();
                PathTrackerContext trackerCtx = new PathTrackerContext(tracker);
                ReaderContext readerContext = new ReaderContext();
                readerContext.setPathTrackerContext(trackerCtx);
                
                HierarchicalStreamReader exclusionReader = new FieldExclusionReader(reader, readerContext);
                HierarchicalStreamReader trackerReader = new PathTrackerReader(exclusionReader, readerContext);
                return trackerReader;
            }

            @Override
            protected HierarchicalStreamWriter createWriter(HierarchicalStreamWriter writer) {
                WriterContext writerCtx = new WriterContext();
                PathTracker tracker = new PathTracker();
                PathTrackerContext trackerCtx = new PathTrackerContext(tracker);
                writerCtx.setPathTrackerContext(trackerCtx);
                
                FieldExclusionWriterMatcherImpl exclusionMatcher = new FieldExclusionWriterMatcherImpl();
                writerCtx.addExclusionMatcher(exclusionMatcher);
                
                HierarchicalStreamWriter exclusionWriter = new FieldExclusionWriter(writer, writerCtx);
                HierarchicalStreamWriter trackerWriter = new PathTrackerWriter(exclusionWriter, writerCtx);
                return trackerWriter;
            }
        };
        return driver;
    }
}
