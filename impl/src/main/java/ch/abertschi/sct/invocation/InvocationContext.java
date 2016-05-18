package ch.abertschi.sct.invocation;

import java.lang.reflect.Method;

/**
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 * 
 */
public interface InvocationContext {
    
    Object getProxy();
    
    Object getTarget();
    
    Method getMethod();
    
    Object[] getParameters();
    
    void setParameters(Object[] params);
    
    Object proceed() throws Exception;

}
