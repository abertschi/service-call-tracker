package org.sct.helper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.sct.xstream.XStreamProvider;
import com.thoughtworks.xstream.XStream;

public class TestHelp {

    private static final String TEST_RES = "src/test/resources/";

    private static XStream xstream = XStreamProvider.INSTANCE.createPlainXStream();

    private TestHelp() {
        throw new UnsupportedOperationException("Not allowed to instancation helper class.");
    }

    public static URL getTestResource(String child) {
        File rootF = new File(".");
        if (child.startsWith("/")) {
            throw new RuntimeException(
                    "Test resource can not start with slash (/). Use getResource() instead.");
        }
        File childF = new File(rootF, TEST_RES + child);
        try {
            return childF.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL createTempFile(String prefix, String ext) {
        try {
            return File.createTempFile(prefix, "." + ext).toURI().toURL();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean theSame(Object o1, Object o2) {
        String s1 = xstream.toXML(o1);
        String s2 = xstream.toXML(o2);
        if (s1.equals(s2)) {
            return true;
        }
        else {
            System.out.println("Object are not equal (string based)");
            System.out.println(s1);
            System.out.println("=================");
            System.out.println(s2);
            return false;
        }
    }
}
