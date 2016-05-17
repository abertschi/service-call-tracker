package ch.abertschi.sct.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abertschi on 14/05/16.
 */
public class ThreadLocalContextHolder
{
    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    private ThreadLocalContextHolder()
    {
    }

    public static void put(String key, Object payload)
    {
        if (CONTEXT.get() == null)
        {
            CONTEXT.set(new HashMap<String, Object>());

        }
        CONTEXT.get().put(key, payload);
    }


    public static Object get(String key)
    {

        return CONTEXT.get() != null ? CONTEXT.get().get(key) : null;
    }

    public static void cleanupThread()
    {
        CONTEXT.remove();
    }
}
