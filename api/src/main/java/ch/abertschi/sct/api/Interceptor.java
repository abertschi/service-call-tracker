package ch.abertschi.sct.api;

import ch.abertschi.sct.api.invocation.InvocationContext;

/**
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 * 
 */
public interface Interceptor {
	
	Object invoke(InvocationContext ctx);
}
