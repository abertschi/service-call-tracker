package org.sct;

/**
 * Abstraction layer to load and record calls.
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 *
 */
public interface CallPersistence {
    
    Object load(Object in);
    
    void record(Object in, Object out);
    
}
