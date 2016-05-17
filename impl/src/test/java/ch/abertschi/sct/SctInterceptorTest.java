package ch.abertschi.sct;

import ch.abertschi.sct.api.Configuration;
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

        Configuration config = new Configuration();
        config.setRecordingMode(Configuration.RECORDING_MODE.OVERWRITE);
        config.setRecordingSourceType(Configuration.INPUT_SOURCE.DIRECTORY);
        config.setRecordingEnabled(true);
        config.setReplayingSourceType(Configuration.INPUT_SOURCE.DIRECTORY);
        config.setReplayingEnabled(true);

        SctConfigurator.getInstance().setGlobalConfiguration(config);
        File f = new File(new File("."), "recordings");
        config.setReplayingSource(f);
        config.setRecordingSource(f);


        //when
        Customer parker = service.getCustomer("Parker", "key2");
        Customer spiderman = service.getCustomer("Spiderman", "");
        Customer venom = service.getCustomer("Venom", "key2");

        // then
        //String is = TestHelp.readFile(f.toURL());
        //Assert.assertTrue(is.contains("Faked response"));

        Assert.assertEquals(parker.getComment(), "Parker");
        Assert.assertEquals(spiderman.getComment(), "Spiderman");
        Assert.assertEquals(venom.getComment(), "Venom");
        //System.out.println(is);


    }

    private CustomerService getProxiedCustomerService()
    {
        InvocationHandler handler = new ReflectionInvocationHandler(new CustomerService()
        {
            @Override
            public Customer getCustomer(String key, String key2)
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
