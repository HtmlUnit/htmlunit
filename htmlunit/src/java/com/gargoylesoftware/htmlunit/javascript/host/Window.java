/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PromptHandler;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

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
     * The javascript function "confirm()"
     * @param message The message
     * @return true if ok was pressed, false if cancel was pressed
     */
    public boolean jsFunction_confirm( final String message ) {
        final ConfirmHandler handler = getJavaScriptEngine().getWebClient().getConfirmHandler();
        if( handler == null ) {
            getLog().warn("window.confirm(\""+message+"\") no confirm handler installed");
            return false;
        }
        else {
            return handler.handleConfirm(document_.getHtmlPage(), message);
        }
    }


    /**
     * The javascript function "prompt()"
     * @param message The message
     * @return true if ok was pressed, false if cancel was pressed
     */
    public String jsFunction_prompt( final String message ) {
        final PromptHandler handler = getJavaScriptEngine().getWebClient().getPromptHandler();
        if( handler == null ) {
            getLog().warn("window.prompt(\""+message+"\") no prompt handler installed");
            return null;
        }
        else {
            return handler.handlePrompt(document_.getHtmlPage(), message);
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
     * @param context The javascript Context
     * @param scriptable The object that the function was called on.
     * @param args The arguments passed to the function.
     * @param function The function object that was invoked.
     * @return The newly opened window
     */
    public static Object jsFunction_open(
        final Context context, final Scriptable scriptable, final Object[] args,  final Function function ) {

        final String url = getStringArg(0, args, null);
        final String windowName = getStringArg(1, args, "");
        final String features = getStringArg(2, args, null);
        final boolean replaceCurrentEntryInBrowsingHistory = getBooleanArg(3, args, false);
        final Window thisWindow = (Window)scriptable;

        if( features != null
                || replaceCurrentEntryInBrowsingHistory == true ) {

            thisWindow.getLog().debug(
                "Window.open: features and replaceCurrentEntryInBrowsingHistory "
                + "not implemented: url=["+url
                + "] windowName=["+windowName
                + "] features=["+features
                + "] replaceCurrentEntry=["+replaceCurrentEntryInBrowsingHistory
                + "]");
        }

        final URL newUrl = thisWindow.makeUrlForOpenWindow(url);
        final WebWindow newWebWindow=  thisWindow.webWindow_.getWebClient().openWindow(
            newUrl, windowName, thisWindow.webWindow_ );
        return (Window)newWebWindow.getScriptObject();
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
     * Set a chunk of javascript to be invoked at some specified time later.
     *
     * @param context The javascript Context
     * @param scriptable The object that the function was called on.
     * @param args The arguments passed to the function.
     * @param function The function object that was invoked.
     * @return The newly opened window
     */
    public static Object jsFunction_setTimeout(
        final Context context, final Scriptable scriptable, final Object[] args,  final Function function ) {
        ((Window)scriptable).getLog().debug( "Window.setTimeout() not implemented" );
        return NOT_FOUND;
/* commented out for now as this doesn't work - come back to it later
        final String script = getStringArg(0, args, null);
        final int timeout = getIntArg(1, args, 0);

        final Runnable runnable = new Runnable() {
            public void run() {
                final Window window = (Window)scriptable;
                try {
                    Thread.sleep(timeout);

                    final HtmlPage htmlPage = window.document_.getHtmlPage();
                    htmlPage.executeJavaScriptIfPossible(script, "Window.setTimeout()", true, null);
                }
                catch( final Exception e ) {
                    window.getLog().error("Caught exception in Window.setTimeout()", e);
                }
            }
        };
        new Thread(runnable).start();
        return null;
*/
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
        if( webWindow_ instanceof HtmlElement ) {
            setHtmlElement((HtmlElement)webWindow_);
        }

        document_ = (Document)makeJavaScriptObject("Document");
        document_.setHtmlElement(htmlPage);
        final PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange( final PropertyChangeEvent event ) {
                if( event.getPropertyName().equals(htmlPage.PROPERTY_ELEMENT) ) {
                    document_.initialize();
                    htmlPage.removePropertyChangeListener(this);
                }
            }
        };
        htmlPage.addPropertyChangeListener(listener);

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
     * Return the value of the opener property.
     * @return the value of window.opener
     */
    public Object jsGet_opener() {
        if( webWindow_ instanceof TopLevelWindow ) {
            final WebWindow opener = ((TopLevelWindow)webWindow_).getOpener();
            if( opener != null ) {
                return (Window)opener.getScriptObject();
            }
        }

        return NOT_FOUND;
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


    /**
     * Close this window
     */
    public void jsFunction_close() {
        getLog().debug( "Window.close() not implemented" );
    }


    public String jsGet_src() {
        final WebWindow webWindow = getWebWindow();
        if( webWindow instanceof HtmlInlineFrame ) {
            return ((HtmlInlineFrame)webWindow).getSrcAttribute();
        }

        return "";
    }


    public void jsSet_src( final String newValue ) {
        final WebWindow webWindow = getWebWindow();
        if( webWindow instanceof HtmlInlineFrame ) {
            ((HtmlInlineFrame)webWindow).setSrcAttribute(newValue);
        }
    }
}

