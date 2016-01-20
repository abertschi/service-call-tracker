package org.sct;

import org.junit.Test;
import org.sct.api.SctConfiguration;
import org.sct.api.SctConfigurator;
import org.sct.invocation.DefaultInvocationContext;
import org.sct.invocation.InvocationContext;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CustomerServiceTest
{
    @Test
    public void test_call() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException
    {
        // Using Java's dynamic Proxy API to proxy CustomerServiceImpl
        InvocationHandler handler = new MyInvocationHandler(new CustomerService.CustomerServiceImpl());
        CustomerService s = (CustomerService) Proxy.newProxyInstance(CustomerService.class.getClassLoader(),
                new Class[] {CustomerService.class },
                handler);

        SctConfigurationImpl config = new SctConfigurationImpl();
        config.setCallRecording(true);
        config.setCallRecordingUrl(File.createTempFile("recording", "xml").toURI().toURL());

        SctConfigurator.getInstance().setConfiguration(config);
        System.out.println(s.getCustomer("Peter").getName());
        System.out.println( config.getCallRecordingUrl().toExternalForm());


    }


    class MyInvocationHandler implements InvocationHandler {

        private final Object mTarget;

        public MyInvocationHandler(Object target) {
            mTarget = target;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
        {
            DefaultInvocationContext invocation = new DefaultInvocationContext(mTarget, method, args);
            invocation.setProxy(proxy);

            SctInterceptor interceptor = new SctInterceptor(SctConfigurator.getInstance().getConfiguration());
            return interceptor.invoke(invocation);
        }
    }

}
