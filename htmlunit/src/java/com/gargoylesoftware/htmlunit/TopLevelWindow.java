/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;


/**
 * A window representing a top level browser window.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class TopLevelWindow implements WebWindow {

    private String name_;
    private Page enclosedPage_;
    private WebClient webClient_;

    private Object scriptObject_;


    /**
     * Create an instance.
     * @param name The name of the new window
     * @param webClient The web client that "owns" this window.
     */
    public TopLevelWindow( final String name, final WebClient webClient ) {
        if( name == null ) {
            throw new NullPointerException("name is null");
        }
        name_ = name;
        webClient_ = webClient;

        webClient_.registerWebWindow(this);
    }


    /**
     * Return the name of this window.
     *
     * @return The name of this window.
     */
    public String getName() {
        return name_;
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
        return "TopLevelWindow[name=\""+getName()+"\"]";
    }


    /**
     * Internal use only - subject to change without notice.<p>
     * Set the javascript object that corresponds to this element.  This is not guarenteed
     * to be set even if there is a javascript object for this html element.
     * @param scriptObject The javascript object.
     */
    public void setScriptObject( final Object scriptObject ) {
        scriptObject_ = scriptObject;
    }


    /**
     * Internal use only - subject to change without notice.<p>
     * Return the javascript object that corresponds to this element.
     * @return The javascript object that corresponsd to this element.
     */
    public Object getScriptObject() {
        return scriptObject_;
    }
}
