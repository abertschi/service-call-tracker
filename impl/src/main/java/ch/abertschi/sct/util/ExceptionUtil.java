package ch.abertschi.sct.util;

/**
 * Created by abertschi on 14/05/16.
 */
public class ExceptionUtil
{
    private ExceptionUtil()
    {
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void wrapException(Throwable exception) throws T
    {
        throw (T) exception;
    }

    public static void throwException(Throwable exception)
    {
        ExceptionUtil.<RuntimeException>wrapException(exception);
    }
}
