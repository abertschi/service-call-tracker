package ch.abertschi.sct.call;

import ch.abertschi.sct.util.SctException;
import com.thoughtworks.xstream.XStream;

/**
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 */
public class CallComparator {

    private XStream xstream;

    public CallComparator(XStream xstream) {
        this.xstream = xstream;
    }

    public boolean compareRequestWithCallObject(Object object, CallObject given) {
        if (object instanceof CallObject) {
            throw new SctException("Request object can't be type of CallObject. Must be unwrapped object");
        }
        CallObject requestObj = new CallObject(object);
        String xmlCallObject = xstream.toXML(given);
        
        requestObj.setFieldExclusion(given.getFieldExclusion());
        String b = xstream.toXML(requestObj);
       
        xmlCallObject = removeSpaces(xmlCallObject);
        b = removeSpaces(b);
        
        return xmlCallObject.equals(b) ? Boolean.TRUE: Boolean.FALSE;
    }
    
    private String removeSpaces(String s) {
        return s.replaceAll(">\\s*<", "><").replaceAll("\\s+</", "</");
    }

}
