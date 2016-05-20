package ch.abertschi.sct.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by abertschi on 18/05/16.
 */
public class ServiceCallTrackerFactory
{
    private static final String DEFAULT_IMPL = "ch.abertschi.sct.ServiceCallTracker";

    private ServiceCallTrackerFactory()
    {
    }

    /**
     * Lookup implementation of service call tracker
     */
    public static Interceptor lookupInterceptor(Configuration config) throws SctException
    {
        Interceptor interceptor = null;
        try
        {
            Class<?> clazz = Class.forName(DEFAULT_IMPL);
            Constructor<?> constructor = clazz.getConstructor(Configuration.class);
            interceptor = (Interceptor) constructor.newInstance(new Object[]{config});
        }
        catch (ClassNotFoundException e)
        {
            throw new SctException(e);
        }
        catch (NoSuchMethodException e)
        {
            throw new SctException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new SctException(e);
        }
        catch (InstantiationException e)
        {
            throw new SctException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new SctException(e);
        }
        return interceptor;
    }
}
