/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PromptHandler;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.ElementArray;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a Window.
 * 
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Darrell DeBoer
 * @author Marc Guillemot
 * @author Dierk Koenig
 * @author Daniel Gredler
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/obj_window.asp">
 * MSDN documentation</a>
 */
public final class Window extends SimpleScriptable {

    private static final long serialVersionUID = -7730298149962810325L;
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
    public void jsConstructor() {
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
        final String script = getStringArg(0, args, null);
        final int timeout = getIntArg(1, args, 0);

        final Runnable runnable = new Runnable() {
            public void run() {
                final Window window = (Window)scriptable;
                try {
                    Thread.sleep(timeout);

                    // Register this thread with the rhino engine
                    Context.enter();
                    final HtmlPage htmlPage = window.document_.getHtmlPage();
                    htmlPage.executeJavaScriptIfPossible(
                        script, "Window.setTimeout()", true, htmlPage.getDocumentElement());
                }
                catch( final Exception e ) {
                    window.getLog().error("Caught exception in Window.setTimeout()", e);
                }
                finally {
                    // Deregister this thread with the rhino engine
                    Context.exit();
                }
            }
        };
        new Thread(runnable).start();
        return null;
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
        if( webWindow_ instanceof DomNode ) {
            setDomNode((DomNode)webWindow_);
        }
        else {
            // Windows don't have corresponding DomNodes so set the domNode
            // variable to be the page.  If this isn't set then SimpleScriptable.get()
            // won't work properly
            setDomNode(htmlPage);
        }

        document_ = (Document)makeJavaScriptObject("Document");
        document_.setDomNode(htmlPage);

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
        final WebWindow topWebWindow = webWindow_.getTopWindow();
        return (SimpleScriptable)topWebWindow.getScriptObject();
    }


    /**
     * Return the value of the parent property
     * @return the value of window.parent
     */
    public SimpleScriptable jsGet_parent() {
        final WebWindow parentWebWindow = webWindow_.getParentWindow();
        return (SimpleScriptable)parentWebWindow.getScriptObject();
    }


    /**
     * Return the value of the opener property.
     * @return the value of window.opener, <code>null</code> for a top level window
     */
    public Object jsGet_opener() {
        if( webWindow_ instanceof TopLevelWindow ) {
            final WebWindow opener = ((TopLevelWindow)webWindow_).getOpener();
            if( opener != null ) {
                return (Window)opener.getScriptObject();
            }
        }

        return null;
    }


    /**
     * Return the value of the frames property.
     * @return The value of window.frames
     */
    public ElementArray jsGet_frames() {
        HtmlPage page = (HtmlPage) this.getWebWindow().getEnclosedPage();
        List list = page.getFrames();
        ElementArray frames = (ElementArray) makeJavaScriptObject("ElementArray");
        frames.setElements( list );
        return frames;
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
        webWindow_.getWebClient().setCurrentWindow(webWindow_);
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
        WebWindow window = ((HtmlPage) getDomNodeOrDie()).getEnclosingWindow();
        getWebWindow().getWebClient().deregisterWebWindow(window);
    }


    /**
     * Return the value of the src attribute
     * @return the value of the src attribute.
     */
    public String jsGet_src() {
        final WebWindow webWindow = getWebWindow();
        if( webWindow instanceof HtmlInlineFrame ) {
            return ((HtmlInlineFrame)webWindow).getSrcAttribute();
        }

        return "";
    }


    /**
     * Set the value of the src attribute.  In the case of an iframe, this will cause a reload of the page
     * @param newValue The new value
     */
    public void jsSet_src( final String newValue ) {
        final WebWindow webWindow = getWebWindow();
        if( webWindow instanceof HtmlInlineFrame ) {
            ((HtmlInlineFrame)webWindow).setSrcAttribute(newValue);
        }
    }

    /**
     * Does nothing.
     * @param x The horizontal position
     * @param y The vertical position
     */
    public void jsFunction_moveTo(final int x, final int y) {
        getLog().debug( "Window.moveTo() not implemented" );
    }

    /**
     * Does nothing.
     * @param x The horizontal position
     * @param y The vertical position
     */
    public void jsFunction_moveBy(final int x, final int y) {
        getLog().debug( "Window.moveBy() not implemented" );
    }

    /**
     * Does nothing.
     * @param width The width of the Window in pixel after resize.
     * @param height The height of the Window in pixel after resize.
     */
    public void jsFunction_resizeTo(final int width, final int height) {
        getLog().debug( "Window.resizeTo() not implemented" );
    }

    /**
     * Does nothing.
     * @param x The horizontal position to scroll to
     * @param y The vertical position to scroll to
     */
    public void jsFunction_scroll(final int x, final int y) {
        getLog().debug( "Window.scroll() not implemented" );
    }

    /**
     * Does nothing.
     * @param x The horizontal distance to scroll by
     * @param y The vertical distance to scroll by
     */
    public void jsFunction_scrollBy(final int x, final int y) {
        getLog().debug( "Window.scrollBy() not implemented" );
    }

    /**
     * Does nothing.
     * @param lines The number of lines to scroll down
     */
    public void jsFunction_scrollByLines(final int lines) {
        getLog().debug( "Window.scrollByLines() not implemented" );
    }

    /**
     * Does nothing.
     * @param pages The number of pages to scroll down
     */
    public void jsFunction_scrollByPages(final int pages) {
        getLog().debug( "Window.scrollByPages() not implemented" );
    }

    /**
     * Does nothing.
     * @param x The horizontal position to scroll to
     * @param y The vertical position to scroll to
     */
    public void jsFunction_scrollTo(final int x, final int y) {
        getLog().debug( "Window.scrollTo() not implemented" );
    }

    /**
     * Set the value of the onload property.
     * @param newValue The new value
     */
    public void jsSet_onload( final Object newValue ) {
        if( webWindow_.getEnclosedPage() instanceof HtmlPage ) {
            final HtmlPage page = (HtmlPage)webWindow_.getEnclosedPage();
            if ( newValue instanceof Function ) {
                page.setOnLoadAttribute( newValue );
            }
            else {
                getLog().error( "Invalid value set to window.onload.  Value class: " + newValue.getClass () );
            }
        }
    }


    /**
     * Return the value of the onload property.
     * @return the value of window.onload
     */
    public Object jsGet_onload() {
        if( webWindow_.getEnclosedPage() instanceof HtmlPage ) {
            final HtmlPage page = (HtmlPage)webWindow_.getEnclosedPage();
            return page.getOnLoadAttribute();
        }

        return "";
    }

    /**
     * Return the value of the name property
     * @return The name
     */
    public String jsGet_name() {
        return webWindow_.getName();
    }

    /**
     * Return the value of the onerror property
     * @return The value
     */
    public String jsGet_onerror() {
        getLog().debug("Window.onerror not implemented");
        return "";
    }

    /**
     * Return the value of the onerror property
     * @param newValue The value
     */
    public void jsSet_onerror( final String newValue) {
        getLog().debug("Window.onerror not implemented");
    }

    /**
     * Return the specified property or {@link #NOT_FOUND} if it could not be found.
     * @param name The name of the property
     * @param start The scriptable object that was originally queried for this property
     * @return The property.
     */
    public Object get( final String name, final Scriptable start ) {
        // If the DomNode hasn't been set yet then do it now.
        if( getDomNodeOrNull() == null && document_ != null ) {
            setDomNode( document_.getHtmlPage() );
        }

        Object result = super.get(name, start);

        // If we are in a frameset then this might be a frame name
        if( result == NOT_FOUND ) {
            final DomNode domNode = ((Window)start).getDomNodeOrNull();
            result = getFrameByName( domNode.getPage(), name );
        }

        // Ask the parent scope, as it may be a variable access like "window.myVar"
        if( result == NOT_FOUND ) {
            result = this.getParentScope().get(name, start);
        }
        return result;
    }

    private Object getFrameByName( final HtmlPage page, final String name ) {

        final Iterator iterator = page.getDocumentElement().getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement element = ( HtmlElement )iterator.next();
            final String tagName = element.getTagName();
            if( tagName.equals("body") ) {
                // Framesets don't contain body tags so if we find one of these
                // then we can leave early.
                return NOT_FOUND;
            }
            if( "frame".equals( tagName ) ) {
                if( element.getAttributeValue("name").equals(name) ) {
                    final Object scriptObject = element.getScriptObject();
                    if( scriptObject == null ) {
                        getLog().error("Window.getFrameByName() scriptObject not initialized yet");
                        return NOT_FOUND;
                    }
                    return scriptObject;
                }
            }
        }
        return NOT_FOUND;
    }
}
