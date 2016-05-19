package ch.abertschi.sct.api;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *
 * @author Andrin Bertschi
 * @since 30.06.2014
 *
 */
public class MethodInvocationContext implements InvocationContext {

    private Object target;

    private Method method;

    private Object proxy;

    private Object[] parameters;

    public MethodInvocationContext(Object target, Method method, Object[] parameters) {
        this.target = target;
        this.method = method;
        this.parameters = parameters;
    }

    public MethodInvocationContext() {
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
    public Method getMethod() {
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
        return this.method.invoke(target, getParameters());
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

    @Override
    public String toString() {
        return "DefaultInvocationContext [target=" + target + ", method=" + method + ", proxy="
                + proxy + ", parameters=" + Arrays.toString(parameters) + "]";
    }

}