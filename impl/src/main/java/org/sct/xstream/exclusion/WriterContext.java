package org.sct.xstream.exclusion;

import java.util.LinkedList;
import java.util.List;

import org.sct.xstream.path.PathTrackerContext;
import com.thoughtworks.xstream.io.path.Path;

/**
 * Shared context information used for writing data.
 * 
 * @author Andrin Bertschi
 * @since 09.07.2014
 * 
 */
public class WriterContext {
    
    private PathTrackerContext pathTrackerContext;

    private List<Path> exclusions;
    
    private List<FieldExclusionWriterMatcher> exclusionMatchers;
    
    public WriterContext() {
        this.exclusions = new LinkedList<Path>();
        this.exclusionMatchers = new LinkedList<FieldExclusionWriterMatcher>();
        exclusionMatchers.add(new FieldExclusionWriterMatcher.FieldExclusionWriterMatcherDefault());
    }
    
    /**
     * @return Returns true, if current element in DOM is excluded because of {@link FieldExclusionWriterMatcher}.
     */
    public boolean isCurrentElementExcluded() {
        Path current = pathTrackerContext.getTracker().getPath();
        boolean success = false;
        for (FieldExclusionWriterMatcher matcher : exclusionMatchers) {
            if (matcher.doesExclusionMatchForCurrent(current, exclusions)) {
                success = true;
                break;
            }
        }
        return success;
    }

    public List<Path> getExclusions() {
        return exclusions;
    }

    public void setExclusions(List<Path> exclusions) {
        this.exclusions = exclusions;
    }

    public List<FieldExclusionWriterMatcher> getExclusionMatchers() {
        return exclusionMatchers;
    }

    public void setExclusionMatchers(List<FieldExclusionWriterMatcher> exclusionMatchers) {
        this.exclusionMatchers = exclusionMatchers;
    }
    
    public void addExclusionMatcher(FieldExclusionWriterMatcher exclusionMatcher) {
        this.exclusionMatchers.add(exclusionMatcher);
    }

    public PathTrackerContext getPathTrackerContext() {
        return pathTrackerContext;
    }

    public void setPathTrackerContext(PathTrackerContext pathTrackerContext) {
        this.pathTrackerContext = pathTrackerContext;
    }
}
