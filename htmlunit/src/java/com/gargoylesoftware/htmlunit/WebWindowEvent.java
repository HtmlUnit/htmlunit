/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import java.util.EventObject;

/**
 * An event that will be fired when a WebWindow changes.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class WebWindowEvent extends EventObject {
    private final Page oldPage_;
    private final Page newPage_;


    /**
     * Create an instance
     *
     * @param source The WebWindow that caused the event
     * @param oldPage The old contents of the web window
     * @param newPage The new contents of the web window
     */
    public WebWindowEvent( final WebWindow source, final Page oldPage, final Page newPage ) {
        super(source);
        oldPage_ = oldPage;
        newPage_ = newPage;
    }


    /**
     * Return true if the two objects are equal
     *
     * @param object The object to compare against.
     * @return true if the two objects are equal.
     */
    public boolean equals( final Object object ) {
        if( getClass() == object.getClass() ) {
            final WebWindowEvent event = (WebWindowEvent)object;
            return isEqual(getSource(), event.getSource())
                && isEqual(getOldPage(), event.getOldPage())
                && isEqual(getNewPage(), event.getNewPage());
        }
        return false;
    }


    /**
     * Return the oldPage
     * @return the page or null if the window has no page
     */
    public Page getOldPage() {
        return oldPage_;
    }


    /**
     * Return the oldPage
     * @return the page or null if the window has no page
     */
    public Page getNewPage() {
        return newPage_;
    }


    /**
     * Return the web window that fired the event.
     * @return The web window that fired the event.
     */
    public WebWindow getWebWindow() {
        return (WebWindow)getSource();
    }


    private boolean isEqual( final Object object1, final Object object2 ) {
        final boolean result;

        if( object1 == null && object2 == null ) {
            result = true;
        }
        else if( object1 == null || object2 == null ) {
            result = false;
        }
        else {
            result = object1.equals(object2);
        }

        return result;
    }


    /**
     * Return a string representation of this event
     * @return A string representation of this event.
     */
    public String toString() {
        return "WebWindowEvent(source=["+getSource()+"] oldPage=["
            +getOldPage()+"] newPage=["+getNewPage()+"])";
    }
}

