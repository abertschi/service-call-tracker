package ch.abertschi.sct;

import junit.framework.Assert;
import org.junit.Test;
import ch.abertschi.sct.api.SctConfigurator;
import ch.abertschi.sct.domain.Customer;
import ch.abertschi.sct.domain.CustomerService;
import ch.abertschi.sct.helper.TestHelp;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class SctInterceptorTest
{
    @Test
    public void test_recording() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, IOException
    {
        // given: Using Java's dynamic Proxy API to proxy CustomerServiceImpl
        InvocationHandler handler = new ReflectionInvocationHandler(new CustomerService()
        {
            @Override
            public Customer getCustomer(String key)
            {
                return new Customer("Peter Parker", 1980);
            }
        });
        CustomerService service = createProxy(CustomerService.class, handler);

        SctConfigurationImpl config = new SctConfigurationImpl();
        SctConfigurator.getInstance().setConfiguration(config);
        config.setCallRecording(true);
        config.setCallRecordingUrl(File.createTempFile("recording", "xml").toURI().toURL());

        //when
        service.getCustomer("Peter");

        // then
        String is = TestHelp.readFile(config.getCallRecordingUrl());
        System.out.println(is);
        Assert.assertTrue(is.contains("Peter Parker"));
    }

    <T> T createProxy(Class<T> clazz, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, handler);
    }


}
