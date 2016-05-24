package ch.abertschi.sct;

import ch.abertschi.sct.api.SctConfigurator;
import ch.abertschi.sct.api.invocation.MethodInvocationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ReflectionInvocationHandler implements InvocationHandler
{
    private final Object mTarget;

    public ReflectionInvocationHandler(Object target)
    {
        mTarget = target;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
    {
        MethodInvocationContext invocation = new MethodInvocationContext(mTarget, method, args);
        invocation.setProxy(proxy);

        ServiceCallTracker interceptor = new ServiceCallTracker(SctConfigurator.getInstance().getConfiguration());
        return interceptor.invoke(invocation);
    }
}
