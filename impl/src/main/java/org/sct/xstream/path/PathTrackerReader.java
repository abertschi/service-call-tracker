/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes. Copyright (C) 2006, 2007, 2009, 2011 XStream
 * Committers. All rights reserved.
 * 
 * The software in this package is published under the terms of the BSD style license a copy of
 * which has been included with this distribution in the LICENSE.txt file.
 * 
 * Created on 07. March 2004 by Joe Walnes
 */
package org.sct.xstream.path;

import org.sct.xstream.exclusion.ReaderContext;
import com.thoughtworks.xstream.converters.ErrorWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.ReaderWrapper;

/**
 * XStream's {@link PathTrackingReader} which is capable to handle events by {@link ReaderContext}.
 * 
 * @author Andrin Bertschi
 * @see PathTrackingReader
 *
 */
public class PathTrackerReader extends ReaderWrapper {

    private PathTracker pathTracker;
    private ReaderContext context;

    public PathTrackerReader(HierarchicalStreamReader reader, ReaderContext context) {
        super(reader);
        this.context = context;
        
        if (context.getPathTrackerContext() != null) {
            this.pathTracker = context.getPathTrackerContext().getTracker();
        }
        
        if (isActive()) {
            pathTracker.pushElement(getNodeName());
        }
    }

    public void moveDown() {
        super.moveDown();
        
        if (isActive()) {
            pathTracker.pushElement(getNodeName());
        }
    }

    public void moveUp() {
        super.moveUp();
        
        if (isActive()) {
            pathTracker.popElement();
        }
    }

    public void appendErrors(ErrorWriter errorWriter) {
        errorWriter.add("path", pathTracker.getPath().toString());
        super.appendErrors(errorWriter);
    }
    
    protected boolean isActive() {
        if (context.getPathTrackerContext() != null) {
            return context.getPathTrackerContext().isActive();
        }
        else {
            return false;
        }
    }
}
