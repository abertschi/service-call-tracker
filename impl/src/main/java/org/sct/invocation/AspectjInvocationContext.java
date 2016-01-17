package org.sct.invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 * 
 */
public class AspectjInvocationContext implements InvocationContext {

    private Object target;

    private Method method;

    private Object proxy;

    private Object[] parameters;
    
    private Callable<?> proceedCallable;

    public AspectjInvocationContext(Callable<?> proceedCallable) {
        this.proceedCallable = proceedCallable;
    }

    // -----------------------------------------------------------
    // interface InvocationContext
    // -----------------------------------------------------------

    @Override
    public Object getProxy() {
        return this.proxy;
    }

    @Override
    public Object getTarget() {
        return this.target;
    }

    @Override
    public Object getMethod() {
        return this.method;
    }

    @Override
    public Object[] getParameters() {
        return this.parameters;
    }

    @Override
    public void setParameters(Object[] params) {
     this.parameters = params;
    }

    @Override
    public Object proceed() throws Exception {
        return proceedCallable.call();        
    }

    // -----------------------------------------------------------
    // getters/ setters
    // -----------------------------------------------------------

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }


    public Callable<?> getProceedCallable() {
        return proceedCallable;
    }


    public void setProceedCallable(Callable<Object> proceedCallable) {
        this.proceedCallable = proceedCallable;
    }

    @Override
    public String toString() {
        return "AspectjInvocationContext [target=" + target + ", method=" + method + ", proxy="
                + proxy + ", parameters=" + Arrays.toString(parameters) + ", proceedCallable="
                + proceedCallable + "]";
    }

}
