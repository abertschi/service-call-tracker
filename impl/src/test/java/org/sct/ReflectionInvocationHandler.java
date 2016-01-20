package org.sct;

import org.sct.api.SctConfigurator;
import org.sct.invocation.DefaultInvocationContext;

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
        DefaultInvocationContext invocation = new DefaultInvocationContext(mTarget, method, args);
        invocation.setProxy(proxy);

        SctInterceptor interceptor = new SctInterceptor(SctConfigurator.getInstance().getConfiguration());
        return interceptor.invoke(invocation);
    }
}
