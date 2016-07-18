package ch.abertschi.sct.parse;

import ch.abertschi.sct.serial.Call;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abertschi on 12/05/16.
 */
public enum XStreamProvider
{
    GET;

    private Map<String, Class<?>> aliases = new HashMap<>();

    public void addAlias(String alias, Class<?> type) {
        aliases.put(alias, type);
    }

    public XStream createPreConfiguredXStream()
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
        xstream.ignoreUnknownElements();
        return xstream;
    }

    private void registerAliases(XStream xstream)
    {
        xstream.alias("call", Call.class);
        for(Map.Entry<String, Class<?>> alias: aliases.entrySet()) {
            xstream.alias(alias.getKey(), alias.getValue());
        }
        //xstream.alias("callobject", CallObject.class);
        //xstream.alias("calls", CallCollection.class);
    }

    private void applyDefaultConfig(XStream xstream)
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
