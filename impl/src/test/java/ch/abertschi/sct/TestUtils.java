package ch.abertschi.sct;

import ch.abertschi.sct.parse.XStreamProvider;
import com.thoughtworks.xstream.XStream;
import junit.framework.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by abertschi on 18/05/16.
 */
public class TestUtils
{
    private static final String TEST_RES = "src/test/resources/";

    private static final XStream XSTREAM = XStreamProvider.GET.createPreConfiguredXStream();

    private TestUtils()
    {
    }

    public static URL getTestResource(String child)
    {
        File rootF = new File(".");
        if (child.startsWith("/"))
        {
            throw new RuntimeException(
                    "Test resource can not start with slash (/). Use getResource() instead.");
        }
        File childF = new File(rootF, TEST_RES + child);
        try
        {
            return childF.toURI().toURL();
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String readFile(URL url)
    {
        try
        {
            return new Scanner(new File(url.getFile())).useDelimiter("\\Z").next();
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static URL createTempFile(String prefix, String ext)
    {
        try
        {
            return File.createTempFile(prefix, "." + ext).toURI().toURL();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void assertCompareAsXml(Object first, Object second)
    {
        String firstXml = XSTREAM.toXML(first);
        String secondXml = XSTREAM.toXML(second);
        Assert.assertEquals(firstXml, secondXml);
    }
}
