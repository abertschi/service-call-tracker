package ch.abertschi.sct.serial;

import com.thoughtworks.xstream.XStream;

/**
 * Created by abertschi on 12/05/16.
 */
public class XStreamProvider
{
    public static XStream createXStream()
    {
        XStream xstream = new XStream();
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


}
