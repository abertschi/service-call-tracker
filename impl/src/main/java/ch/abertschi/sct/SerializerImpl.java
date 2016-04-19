package ch.abertschi.sct;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import ch.abertschi.sct.util.SctException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;

/**
 * Serializer which encapsulates marshaling and unmarshalling.
 * 
 * @author Andrin Bertschi
 * @since 04.04.2014
 * 
 */
public class SerializerImpl implements Serializer {
    private static final Logger LOG = LoggerFactory.getLogger(SerializerImpl.class);

    private XStream xstream;

    public SerializerImpl(XStream xstream) {
        this.xstream = xstream;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unmarshal(URL where) {
        checkIfValidOrThrowException(where);
        T unmarshalled = null;

        try {
            unmarshalled = (T) this.xstream.fromXML(where.openStream());
        } catch (IOException e) {
            throw new SctException("Specified dataset was not found.", e);
        } catch (StreamException e) {
            LOG.info("Dataset for mocking record doesn't constist of any records or is invalid. "
                    + "Existing data will be ignored.");
        }
        return unmarshalled;
    }

    public <T> void marshal(T toMarshal, URL target) {
        checkIfValidOrThrowException(target);
        FileOutputStream writer = null;

        try {
            writer = new FileOutputStream(target.getFile(), false);
        } catch (FileNotFoundException e) {
            throw new SctException("Specified dataset was not found.", e);
        }
        try {
            xstream.toXML(toMarshal, writer);
            writer.close();
            
        } catch (IOException e) {
            throw new SctException(e);
        }
    }

    private void checkIfValidOrThrowException(URL url) {
        if (url == null) {
            throw new SctException(String.format("Specified dataset is null"));
        }
    }
}
