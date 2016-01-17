package org.sct.xstream.path;

/**
 * Path tracking information used for reading and writing XML data.
 * 
 * @author Andrin Bertschi
 * @since 01.07.2014
 * 
 */
public class PathTrackerContext {

    private boolean isActive;

    private PathTracker tracker;

    public PathTrackerContext(PathTracker tracker) {
        isActive = false;
        this.tracker = tracker;
    }

    public void startTracker() {
        this.isActive = true;
        tracker.reset();
    }

    public void stopTracker() {
        this.isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public PathTracker getTracker() {
        return tracker;
    }

    public void setTracker(PathTracker tracker) {
        this.tracker = tracker;
    }
}
