/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;


/**
 * A window that acts as a holding place for temporary files loaded from the server.
 * For example, this would be used to hold scripts that are loaded using the
 * src="" tag.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HiddenWebWindow implements WebWindow {

    private Page enclosedPage_;
    private WebClient webClient_;


    /**
     * Create an instance.
     * @param name The name of the new window
     * @param webClient The web client that "owns" this window.
     */
    public HiddenWebWindow( final WebClient webClient ) {
        webClient_ = webClient;
    }


    /**
     * Return the name of this window.
     *
     * @return The name of this window.
     */
    public String getName() {
        return "";
    }


    /**
     * Return the currently loaded page or null if no page has been loaded.
     *
     * @return The currently loaded page or null if no page has been loaded.
     */
    public Page getEnclosedPage() {
        return enclosedPage_;
    }


    /**
     * Set the currently loaded page.
     *
     * @param page The new page or null if there is no page (ie empty window)
     */
    public void setEnclosedPage( final Page page ) {
        enclosedPage_ = page;
    }


    /**
     * Return the web client that owns this window
     * @return The web client
     */
    public WebClient getWebClient() {
        return webClient_;
    }


    /**
     * Return a string representation of this object
     * @return A string representation of this object
     */
    public String toString() {
        return "HiddenWebWindow[name=\""+getName()+"\"]";
    }


    /**
     * Internal use only - subject to change without notice.<p>
     * Set the javascript object that corresponds to this element.  This is not guarenteed
     * to be set even if there is a javascript object for this html element.
     * @param scriptObject The javascript object.
     */
    public void setScriptObject( final Object scriptObject ) {
        throw new UnsupportedOperationException("This class doesn't support script objects");
    }


    /**
     * Internal use only - subject to change without notice.<p>
     * Return the javascript object that corresponds to this element.
     * @return The javascript object that corresponsd to this element.
     */
    public Object getScriptObject() {
        throw new UnsupportedOperationException("This class doesn't support script objects");
    }
}
