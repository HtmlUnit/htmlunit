/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * A javascript window class
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class Window extends SimpleScriptable {

    private Document document_;
    private Navigator navigator_;
    private WebWindow webWindow_;
    private Screen screen_;
    private History history_;
    private Location location_;

    /**
     * Create an instance.  The rhino engine requires all host objects
     * to have a default constructor.
     */
    public Window() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public final void jsConstructor() {
    }


    /**
     * The javascript function "alert()"
     * @param message The message
     */
    public void jsFunction_alert( final String message ) {
        final AlertHandler handler = getJavaScriptEngine().getWebClient().getAlertHandler();
        if( handler == null ) {
            getLog().warn("window.alert(\""+message+"\") no alert handler installed");
        }
        else {
            handler.handleAlert(document_.getHtmlPage(), message);
        }
    }


    /**
     * Return the javascript property "document"
     * @return The document
     */
    public Document jsGet_document() {
        return document_;
    }


    /**
     * Open a new window
     *
     * @param url The url that will be used to load the content of the new window.
     * @param windowName The name of the window
     * @param features The features - not currently implemented
     * @param replaceCurrentEntryInBrowsingHistory How will this affect the history object?
     */
    public void jsFunction_open(
            final String url,
            final String windowName,
            final String features,
            final boolean replaceCurrentEntryInBrowsingHistory ) {

        if( features.equals("undefined") == false
                || replaceCurrentEntryInBrowsingHistory == true ) {

            getLog().debug(
                "Window.open: features and replaceCurrentEntryInBrowsingHistory "
                + "not implemented: url=["+url
                + "] windowName=["+windowName
                + "] features=["+features
                + "] replaceCurrentEntry=["+replaceCurrentEntryInBrowsingHistory
                + "]");
        }

        try {
            webWindow_.getWebClient().openWindow( makeUrlForOpenWindow(url), windowName );
        }
        catch( final IOException e ) {
            // The window will have already been created.  Print out the stack
            // trace and carry on - there isn't much else we can do at this point.
            getLog().warn("error when calling openWindow()", e);
        }
    }


    private URL makeUrlForOpenWindow( final String urlString ) {
        if( urlString.length() == 0 ) {
            return null;
        }

        try {
            final Page page = webWindow_.getEnclosedPage();
            if( page != null && page instanceof HtmlPage ) {
                return ((HtmlPage)page).getFullyQualifiedUrl(urlString);
            }
            else {
                return new URL(urlString);
            }
        }
        catch( final MalformedURLException e ) {
            getLog().error("Unable to create url for openWindow: relativeUrl=["+urlString+"]", e);
            return null;
        }
    }


    /**
     * Return the javascript property "navigator"
     * @return The document
     */
    public Navigator jsGet_navigator() {
        return navigator_;
    }


    /**
     * Return the window property.  This is a synonym for "self"
     * @return A reference to this
     */
    public Window jsGet_window() {
        return this;
    }


    /**
     * Return the "self" property
     * @return this
     */
    public Window jsGet_self() {
        return this;
    }


    /**
     * Return the location property
     * @return The location property
     */
    public Location jsGet_location() {
        return location_;
    }


    /**
     * Set the location property.  This will cause a reload of the window.
     * @param newLocation The url of the new content.
     */
    public void jsSet_location( final String newLocation ) {
        try {
            final HtmlPage page = (HtmlPage)webWindow_.getEnclosedPage();
            final URL url = page.getFullyQualifiedUrl(newLocation);

            webWindow_.getWebClient().getPage(
                webWindow_, url, SubmitMethod.GET, Collections.EMPTY_LIST );
        }
        catch( final MalformedURLException e ) {
            getLog().error("jsSet_location(\""+newLocation+"\") Got MalformedURLException", e);
        }
        catch( final IOException e ) {
            getLog().error("jsSet_location(\""+newLocation+"\") Got IOException", e);
        }
    }


    /**
     * Return the "screen" property
     * @return the screen property
     */
    public Screen jsGet_screen() {
        return screen_;
    }


    /**
     * Return the "history" property
     * @return the "history" property
     */
    public History jsGet_history() {
        return history_;
    }


    /**
     * Initialize the object.
     * @param htmlPage The html page containing the javascript.
     * @exception Exception If an error occurs.
     */
    public void initialize( final HtmlPage htmlPage ) throws Exception {

        webWindow_ = htmlPage.getEnclosingWindow();
        webWindow_.setScriptObject(this);

        document_ = (Document)makeJavaScriptObject("Document");
        document_.setHtmlElement(htmlPage);
        document_.initialize();

        navigator_ = (Navigator)makeJavaScriptObject("Navigator");
        screen_ = (Screen)makeJavaScriptObject("Screen");
        history_ = (History)makeJavaScriptObject("History");

        location_ = (Location)makeJavaScriptObject("Location");
        location_.initialize(this);
    }


    /**
     * Return the value of the top property
     * @return The value of "top"
     */
    public SimpleScriptable jsGet_top() {
        WebWindow window = webWindow_;
        while( window != null ) {
            if( window instanceof TopLevelWindow ) {
                return (SimpleScriptable)window.getScriptObject();
            }

            window = ((HtmlElement)window).getPage().getEnclosingWindow();
        }
        throw new IllegalStateException("Couldn't find a TopLevelWindow!");
    }


    /**
     * Return the value of the parent property
     * @return the value of window.parent
     */
    public SimpleScriptable jsGet_parent() {
        final SimpleScriptable parent;
        if( webWindow_ instanceof TopLevelWindow ) {
            parent = this;
        }
        else {
            final WebWindow parentWebWindow
                = ((HtmlElement)webWindow_).getPage().getEnclosingWindow();
            parent = (SimpleScriptable)parentWebWindow.getScriptObject();
        }

        return parent;
    }


    /**
     * Return the value of the opener property.  Currently not implemented
     * @return the value of window.opener
     */
    public SimpleScriptable jsGet_opener() {
        getLog().debug("Window.jsGet_opener() Not implemented");
        return null;
    }


    /**
     * Return the value of the frames property.  Currently not implemented
     * @return The value of window.frames
     */
    public SimpleScriptable[] jsGet_frames() {
        final Page page = webWindow_.getEnclosedPage();
        if( page == null || page instanceof HtmlPage == false ) {
            return new SimpleScriptable[0];
        }

        final HtmlPage htmlPage = (HtmlPage)page;
        final List frames = htmlPage.getFrames();
        final int frameCount = frames.size();
        final SimpleScriptable[] jsFrames = new SimpleScriptable[frameCount];

        for( int i=0; i<frameCount; i++ ) {
            jsFrames[i] = (SimpleScriptable)((WebWindow)frames.get(i)).getScriptObject();
        }

        return jsFrames;
    }


    /**
     * Return the WebWindow associated with this Window
     * @return The WebWindow
     */
    public WebWindow getWebWindow() {
        return webWindow_;
    }


    /**
     * Set the focus to this element.
     */
    public void jsFunction_focus() {
        getLog().debug( "Window.focus() not implemented" );
    }


    /**
     * Remove focus from this element
     */
    public void jsFunction_blur() {
        getLog().debug( "Window.blur() not implemented" );
    }
}

