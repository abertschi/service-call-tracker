package ch.abertschi.sct;

import ch.abertschi.sct.api.Configuration;
import ch.abertschi.sct.parse.StorageParser;
import org.junit.Assert;
import org.junit.Test;
import ch.abertschi.sct.api.SctConfigurator;
import ch.abertschi.sct.domain.Customer;
import ch.abertschi.sct.domain.CustomerService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ServiceCallTrackerTest
{
    @Test
    public void test_simple_recording() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, IOException
    {
        // given: Using Java's dynamic Proxy API to proxy CustomerServiceImpl
        CustomerService service = getDefaultCustomerService();

        Configuration config = new Configuration();
        config.setRecordingEnabled(true);
        File recordingFile = File.createTempFile("recordings", ".xml");
        config.setRecordingSource(recordingFile);
        SctConfigurator.getInstance().setGlobalConfiguration(config);

        //when
        Customer parker = service.getCustomer("Parker", "hi peter!");
        Customer spiderman = service.getCustomer("Spiderman", "hi spiderman!");
        Customer venom = service.getCustomer("Venom", "hi venom!");

        //then
        String is = TestUtils.readFile(recordingFile.toURL());
        StorageParser parser = new StorageParser(recordingFile);

        Object[] parkerArgs = {"Parker", "hi peter!"};
        Customer parkerParsed = (Customer) parser.get(parkerArgs);
        TestUtils.assertCompareAsXml(parker, parkerParsed);

        Object[] spidermanArgs = {"Spiderman", "hi spiderman!"};
        Customer spidermanParsed = (Customer) parser.get(spidermanArgs);
        TestUtils.assertCompareAsXml(spiderman, spidermanParsed);

        Object[] venomArgs = {"Venom", "hi venom!"};
        Customer venomParsed = (Customer) parser.get(venomArgs);
        TestUtils.assertCompareAsXml(venom, venomParsed);
    }

    private CustomerService getDefaultCustomerService()
    {
        InvocationHandler handler = new ReflectionInvocationHandler(new CustomerService.CustomerServiceImpl());
        return createProxy(CustomerService.class, handler);
    }

    <T> T createProxy(Class<T> clazz, InvocationHandler handler)
    {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, handler);
    }


}
