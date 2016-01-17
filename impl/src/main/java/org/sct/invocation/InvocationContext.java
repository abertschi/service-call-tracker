package org.sct.invocation;

/**
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 * 
 */
public interface InvocationContext {
    
    Object getProxy();
    
    Object getTarget();
    
    Object getMethod();
    
    Object[] getParameters();
    
    void setParameters(Object[] params);
    
    Object proceed() throws Exception;

}
