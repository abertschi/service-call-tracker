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
        CustomerService service = getProxiedCustomerService();

        SctConfigurationImpl config = new SctConfigurationImpl();
        SctConfigurator.getInstance().setConfiguration(config);
        config.setCallRecording(true);
        config.setCallRecordingUrl(File.createTempFile("recording", "xml").toURI().toURL());

        //when
        Customer parker = service.getCustomer("Parker");
        Customer spiderman = service.getCustomer("Spiderman");
        Customer venom = service.getCustomer("Venom");

        // then
        String is = TestHelp.readFile(config.getCallRecordingUrl());
        Assert.assertTrue(is.contains("Faked response"));

        Assert.assertEquals(parker.getComment(), "Parker");
        Assert.assertEquals(spiderman.getComment(), "Spiderman");
        Assert.assertEquals(venom.getComment(), "Venom");


    }

    private CustomerService getProxiedCustomerService()
    {
        InvocationHandler handler = new ReflectionInvocationHandler(new CustomerService()
        {
            @Override
            public Customer getCustomer(String key)
            {
                Customer c = new Customer("Faked response", 1980);
                c.setComment(key);
                return c;
            }
        });
        return createProxy(CustomerService.class, handler);
    }

    <T> T createProxy(Class<T> clazz, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, handler);
    }


}
