package ch.abertschi.sct.domain;

import ch.abertschi.sct.api.Configuration;
import ch.abertschi.sct.api.Interceptor;
import ch.abertschi.sct.api.ServiceCallTrackerFactory;
import org.junit.Test;

/**
 * Created by abertschi on 25/05/16.
 */
public class FactoryTest
{
    
    @Test
    public void test_lookup()
    {
        Configuration c = new Configuration();

        Interceptor interceptor = ServiceCallTrackerFactory.lookupInterceptor(c);
        //System.out.println(interceptor.invoke(null));

    }
}
