package ch.abertschi.sct;

import java.net.URL;

/**
 * Serializer interface to marshal and unmarshal objects.
 * 
 * @author Andrin Bertschi
 * @since 30.06.2014
 * 
 */
public interface Serializer  {
    
    <T> T unmarshal(URL where);
    
    <T> void marshal(T toMarshal, URL target);

}


