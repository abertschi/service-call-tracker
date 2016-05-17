package ch.abertschi.sct.parse;

import ch.abertschi.sct.serial.Call;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

import java.io.Writer;

/**
 * Created by abertschi on 12/05/16.
 */
public class XStreamProvider
{
    public static XStream createXStream()
    {
        XStream xstream = new XStream(
                new DomDriver()
                {
                    public HierarchicalStreamWriter createWriter(Writer out)
                    {
                        return new MyPrettyWriter(out);
                    }
                });
        applyDefaultConfig(xstream);
        registerAliases(xstream);
        return xstream;
    }

    private static void registerAliases(XStream xstream)
    {
        xstream.alias("call", Call.class);
        //xstream.alias("callobject", CallObject.class);
        //xstream.alias("calls", CallCollection.class);
    }

    private static void applyDefaultConfig(XStream xstream)
    {
        xstream.autodetectAnnotations(true);
        xstream.setClassLoader(Thread.currentThread().getContextClassLoader());
    }

    public static class MyPrettyWriter extends PrettyPrintWriter
    {
        public MyPrettyWriter(Writer writer)
        {
            super(writer);
        }

        protected void writeText(QuickWriter writer, String text)
        {
            if (text.indexOf('<') < 0)
            {
                writer.write(text);
            }
            else
            {
                writer.write("<![CDATA[");
                writer.write(text);
                writer.write("]]>");
            }
        }
    }
}
