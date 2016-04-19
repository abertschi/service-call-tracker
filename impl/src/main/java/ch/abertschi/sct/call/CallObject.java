package ch.abertschi.sct.call;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.path.Path;

/**
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 */
@XStreamAlias("callobject")
public class CallObject {

    private Object object;

    @XStreamOmitField
    private List<Path> fieldExclusion;

    public CallObject() {
    }

    public CallObject(Object o) {
        this.object = o;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public List<Path> getFieldExclusion() {
        return fieldExclusion;
    }

    public void setFieldExclusion(List<Path> fieldExclusion) {
        this.fieldExclusion = fieldExclusion;
    }

    @Override
    public String toString() {
        return "CallObject [object=" + object + ", fieldExclusion=" + fieldExclusion + "]";
    }
}
