package ch.abertschi.sct.serial;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by abertschi on 12/05/16.
 */
public class Serializer
{
    private static final Logger LOG = LoggerFactory.getLogger(Serializer.class);

    private XStream xstream;

    public Serializer(XStream xstream)
    {
        this.xstream = xstream;
    }

    public <T> T unmarshal(File where)
    {
        T object = null;

        try
        {
            object = (T) this.xstream.fromXML(where);
        }
        catch (StreamException e)
        {
            throw new RuntimeException(e);
        }
        return object;
    }

    public <T> void marshal(T object, File where)
    {
        FileOutputStream writer = null;
        try
        {
            writer = new FileOutputStream(where, false);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(String.format("File %s not found", where), e);
        }
        try
        {
            xstream.toXML(object, writer);
            writer.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
