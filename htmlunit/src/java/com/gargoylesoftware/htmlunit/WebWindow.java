/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;


/**
 *  An interface that represents one window in a browser.  It could be a top level window
 *  or a frame.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface WebWindow {

    /**
     * Return the name of this window.
     *
     * @return The name of this window.
     */
    String getName();


    /**
     * Return the currently loaded page or null if no page has been loaded.
     *
     * @return The currently loaded page or null if no page has been loaded.
     */
    Page getEnclosedPage();


    /**
     * Set the currently loaded page.
     *
     * @param page The new page or null if there is no page (ie empty window)
     */
    void setEnclosedPage( final Page page );


    /**
     * Return the web client that "owns" this window.
     *
     * @return The web client or null if this window has been closed.
     */
    WebClient getWebClient();


    /**
     * Internal use only - subject to change without notice.<p>
     * Set the javascript object that corresponds to this element.  This is not guarenteed
     * to be set even if there is a javascript object for this html element.
     * @param scriptObject The javascript object.
     */
    void setScriptObject( final Object scriptObject );


    /**
     * Internal use only - subject to change without notice.<p>
     * Return the javascript object that corresponds to this element.
     * @return The javascript object that corresponsd to this element.
     */
    Object getScriptObject();
}

