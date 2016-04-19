package ch.abertschi.sct.xstream.exclusion;

import java.util.List;

import ch.abertschi.sct.xstream.path.PathTrackerContext;
import com.thoughtworks.xstream.io.path.Path;

/**
 * Shared context information used for reading marshalled data.
 * 
 * @author Andrin Bertschi
 * @since 09.07.2014
 *
 */
public class ReaderContext {

    private List<Path> exclusions;
    
    private boolean isExcluded;

    /**
     * The java type of the element currently processed.
     */
    private Class<?> fieldType;
    
    private PathTrackerContext pathTrackerContext;
    
    public boolean isExcluded() {
        return isExcluded;
    }

    public void setExcluded(boolean isExcluded) {
        this.isExcluded = isExcluded;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    public List<Path> getExclusions() {
        return exclusions;
    }

    public void setExclusions(List<Path> exclusions) {
        this.exclusions = exclusions;
    }

    public PathTrackerContext getPathTrackerContext() {
        return pathTrackerContext;
    }

    public void setPathTrackerContext(PathTrackerContext pathTrackerContext) {
        this.pathTrackerContext = pathTrackerContext;
    }
}
