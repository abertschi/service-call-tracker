package ch.abertschi.sct.invocation;

/**
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 * 
 */
public interface Interceptor {
	
	Object invoke(InvocationContext ctx);

}
