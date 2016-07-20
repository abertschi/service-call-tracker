package ch.abertschi.sct;

import ch.abertschi.sct.parse.StorageWriter;
import ch.abertschi.sct.serial.Call;
import ch.abertschi.sct.serial.Request;
import ch.abertschi.sct.serial.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by abertschi on 20/07/16.
 *
 * A call recorder to dump request/responses manually to a file, stdout or string.
 */
public class CallDumper
{
    private final File file;
    private final StorageWriter writer;

    private CallDumper(File f)
    {
        this.file = f;
        this.writer = new StorageWriter(f);
    }

    public static CallDumper createDumper(File file)
    {
        return new CallDumper(file);
    }

    public static CallDumper createDumper()
    {
        try
        {
            return new CallDumper(File.createTempFile("dumper", ".xml"));

        }
        catch (IOException e)
        {
            throw new RuntimeException("Cant create file ", e);
        }
    }

    public void record(Object request, Object response)
    {
        Call call = new Call()
                .setRequest(new Request().setPayload(request))
                .setResponse(new Response().setPayload(response));
        writer.write(call);
    }

    public String toXml()
    {
        return fileToString(file);
    }

    public void toFile(File f)
    {
        String xml = toXml();
        try
        {
            FileWriter fw = new FileWriter(f);
            fw.write(xml);
            fw.close();
        }
        catch (IOException iox)
        {
            throw new RuntimeException(iox);
        }
    }

    public void toStdOut()
    {
        System.out.println(toXml());
    }

    private String fileToString(File f)
    {
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(f);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        String text = scanner.useDelimiter("\\A").next();
        scanner.close();
        return text;
    }
}
