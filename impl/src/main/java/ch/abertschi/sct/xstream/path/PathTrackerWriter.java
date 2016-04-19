/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes. Copyright (C) 2006, 2007, 2009, 2011 XStream
 * Committers. All rights reserved.
 * 
 * The software in this package is published under the terms of the BSD style license a copy of
 * which has been included with this distribution in the LICENSE.txt file.
 * 
 * Created on 07. March 2004 by Joe Walnes
 */
package ch.abertschi.sct.xstream.path;


import ch.abertschi.sct.xstream.exclusion.WriterContext;
import com.thoughtworks.xstream.io.AbstractWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.WriterWrapper;

public class PathTrackerWriter extends WriterWrapper {

    private PathTracker pathTracker;

    private boolean isNameEncoding;

    private WriterContext context;

    public PathTrackerWriter(HierarchicalStreamWriter writer, WriterContext context) {
        super(writer);
        this.context = context;
        this.isNameEncoding = writer.underlyingWriter() instanceof AbstractWriter;

        if (context.getPathTrackerContext() != null) {
            this.pathTracker = context.getPathTrackerContext().getTracker();
        }
    }

    public void startNode(String name) {
        if (isActive()) {
            pathTracker.pushElement(isNameEncoding ? ((AbstractWriter) wrapped.underlyingWriter())
                    .encodeNode(name) : name);
        }
        super.startNode(name);
    }

    public void startNode(String name, Class clazz) {
        if (isActive()) {
            pathTracker.pushElement(isNameEncoding ? ((AbstractWriter) wrapped.underlyingWriter())
                    .encodeNode(name) : name);
        }
        super.startNode(name, clazz);
    }

    public void endNode() {
        super.endNode();
        if (isActive()) {
            pathTracker.popElement();
        }
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
