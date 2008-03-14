/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PromptHandler;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowNotFoundException;
import com.gargoylesoftware.htmlunit.html.BaseFrame;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
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
 * @author David D. Kilzer
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/obj_window.asp">
 * MSDN documentation</a>
 */
public class Window extends SimpleScriptable implements ScriptableWithFallbackGetter {

    private static final long serialVersionUID = -7730298149962810325L;
    private Document document_;
    private Navigator navigator_;
    private WebWindow webWindow_;
    private Screen screen_;
    private History history_;
    private Location location_;
    private Object event_;
    private String status_ = "";
    private HTMLCollection frames_; // has to be a member to have equality (==) working
    private Map<Class< ? extends SimpleScriptable>, Scriptable> prototypes_ =
        new HashMap<Class< ? extends SimpleScriptable>, Scriptable>();
    private final JavaScriptEngine scriptEngine_;
    private EventListenersContainer eventListenersContainer_;

    /**
     * Creates an instance.
     *
     * @param scriptEngine The JavaScript engine responsible for the new window instance.
     */
    public Window(final JavaScriptEngine scriptEngine) {
        scriptEngine_ = scriptEngine;
    }

    /**
     * Gets the Javascript Engine responsible for this object.
     * @return the javascript engine
     */
    public JavaScriptEngine getJavaScriptEngine() {
        return scriptEngine_;
    }

    /**
     * Returns the prototype object corresponding to the specified HtmlUnit class inside the window scope.
     * @param jsClass the class whose prototype is to be returned
     * @return the prototype object corresponding to the specified class inside the specified scope
     */
    @Override
    public Scriptable getPrototype(final Class< ? extends SimpleScriptable> jsClass) {
        return (Scriptable) prototypes_.get(jsClass);
    }

    /**
     * Sets the prototypes for HtmlUnit host classes
     * @param map a Map of ({@link Class}, {@link Scriptable})
     */
    public void setPrototypes(final Map<Class< ? extends SimpleScriptable>, Scriptable> map) {
        prototypes_ = map;
    }

    /**
     * The javascript function "alert()"
     * @param message The message
     */
    public void jsxFunction_alert(final Object message) {
        // use Object as parameter and perform String conversion by ourself
        // this allows to place breakpoint here and "see" the message object and its properties
        final String stringMessage = Context.toString(message);
        final AlertHandler handler = getWebWindow().getWebClient().getAlertHandler();
        if (handler == null) {
            getLog().warn("window.alert(\"" + stringMessage + "\") no alert handler installed");
        }
        else {
            handler.handleAlert(document_.getHtmlPage(), stringMessage);
        }
    }

    /**
     * The javascript function "confirm()"
     * @param message The message
     * @return true if ok was pressed, false if cancel was pressed
     */
    public boolean jsxFunction_confirm(final String message) {
        final ConfirmHandler handler = getWebWindow().getWebClient().getConfirmHandler();
        if (handler == null) {
            getLog().warn("window.confirm(\""
                    + message + "\") no confirm handler installed, simulating the OK button");
            return true;
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
    public String jsxFunction_prompt(final String message) {
        final PromptHandler handler = getWebWindow().getWebClient().getPromptHandler();
        if (handler == null) {
            getLog().warn("window.prompt(\"" + message + "\") no prompt handler installed");
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
    public Document jsxGet_document() {
        return document_;
    }

    /**
     * Return the current event
     * @return <code>null</code> if no event is currently available
     */
    public Object jsxGet_event() {
        return event_;
    }

    /**
     * Sets the current event
     * @param event the event
     */
    public void setEvent(final Object event) {
        event_ = event;
    }

    /**
     * Opens a new window.
     *
     * @param context The javascript Context
     * @param scriptable The object that the function was called on.
     * @param args The arguments passed to the function.
     * @param function The function object that was invoked.
     * @return the newly opened window, or <tt>null</tt> if popup windows have been disabled
     * @see WebClient#isPopupBlockerEnabled()
     */
    public static Object jsxFunction_open(
        final Context context, final Scriptable scriptable, final Object[] args, final Function function) {

        final String url = getStringArg(0, args, null);
        final String windowName = getStringArg(1, args, "");
        final String features = getStringArg(2, args, null);
        final boolean replaceCurrentEntryInBrowsingHistory = getBooleanArg(3, args, false);
        final Window thisWindow = (Window) scriptable;
        final WebClient webClient = thisWindow.webWindow_.getWebClient();

        if (webClient.isPopupBlockerEnabled()) {
            thisWindow.getLog().debug("Ignoring window.open() invocation because popups are blocked.");
            return null;
        }

        if (features != null || replaceCurrentEntryInBrowsingHistory) {
            thisWindow.getLog().debug(
                "Window.open: features and replaceCurrentEntryInBrowsingHistory "
                + "not implemented: url=[" + url
                + "] windowName=[" + windowName
                + "] features=[" + features
                + "] replaceCurrentEntry=[" + replaceCurrentEntryInBrowsingHistory
                + "]");
        }

        // if specified name is the name of an existing window, then hold it
        if (StringUtils.isEmpty(url) && !"".equals(windowName)) {
            final WebWindow webWindow;
            try {
                webWindow = webClient.getWebWindowByName(windowName);
                return webWindow.getScriptObject();
            }
            catch (final WebWindowNotFoundException e) {
                // nothing
            }
        }
        final URL newUrl = thisWindow.makeUrlForOpenWindow(url);
        final WebWindow newWebWindow = webClient.openWindow(newUrl, windowName, thisWindow.webWindow_);
        return newWebWindow.getScriptObject();
    }

    /**
     * Creates a popup window Open a new window
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/createpopup.asp">
     * MSDN documentation</a>
     * @param context The javascript Context
     * @param scriptable The object that the function was called on.
     * @param args The arguments passed to the function.
     * @param function The function object that was invoked.
     * @return The created popup
     */
    public static Popup jsxFunction_createPopup(
        final Context context, final Scriptable scriptable, final Object[] args, final Function function) {

        final Window thisWindow = (Window) scriptable;

        final Popup popup = new Popup();
        popup.setParentScope(thisWindow);
        popup.setPrototype(thisWindow.getPrototype(Popup.class));
        popup.init(thisWindow);
        
        return popup;
    }

    private URL makeUrlForOpenWindow(final String urlString) {
        if (urlString.isEmpty()) {
            // IE handles "" as "about:blank" in window.open
            if (getBrowserVersion().isIE()) {
                return WebClient.URL_ABOUT_BLANK;
            }
            else {
                return null;
            }
        }

        try {
            final Page page = webWindow_.getEnclosedPage();
            if (page != null && page instanceof HtmlPage) {
                return ((HtmlPage) page).getFullyQualifiedUrl(urlString);
            }
            else {
                return new URL(urlString);
            }
        }
        catch (final MalformedURLException e) {
            getLog().error("Unable to create url for openWindow: relativeUrl=[" + urlString + "]", e);
            return null;
        }
    }

    /**
     * Makes the job object for setTimeout and setInterval
     *
     * @param codeToExec either a Function or a String of the javascript code
     * @param timeout time to wait
     * @param thisWindow the window to associate the thread with
     * @param loopForever if the thread should keep looping (setTimeout vs setInterval)
     * @return the job
     */
    private static JavaScriptBackgroundJob createJavaScriptBackgroundJob(final Object codeToExec,
            final int timeout, final Window thisWindow, final boolean loopForever, final String label) {
        if (codeToExec == null) {
            throw Context.reportRuntimeError("Function not provided");
        }
        else if (codeToExec instanceof String) {
            final String scriptString = (String) codeToExec;
            return new JavaScriptBackgroundJob(thisWindow, timeout, scriptString, loopForever, label);
        }
        else if (codeToExec instanceof Function) {
            final Function scriptFunction = (Function) codeToExec;
            return new JavaScriptBackgroundJob(thisWindow, timeout, scriptFunction, loopForever, label);
        }
        else {
            throw Context.reportRuntimeError("Unknown type for function");
        }
    }

    /**
     * Set a chunk of javascript to be invoked at some specified time later.
     * The invocation occurs only if the window is opened after the delay
     * and does not contain an other page than the one that originated the setTimeout.
     *
     * JavaScript param 1: The code to execute, either a String or a Function.
     * JavaScript param 2: the delay in milliseconds to wait before executing the code.
     *
     * @param context The javascript Context
     * @param scriptable The object that the function was called on.
     * @param args The arguments passed to the function.
     * @param function The function object that was invoked.
     * @return the id of the created timer
     */
    public static int jsxFunction_setTimeout(final Context context, final Scriptable scriptable,
            final Object[] args, final Function function) {
        final Window thisWindow = (Window) scriptable;
        final Object codeToExec = getObjectArg(0, args, null);
        final int timeout = getIntArg(1, args, 0);

        thisWindow.getLog().debug("setTimeout(" + codeToExec + ", " + timeout + ")");
        final Runnable job = createJavaScriptBackgroundJob(codeToExec, timeout, thisWindow, false, "setTimeout");
        final int id = thisWindow.getWebWindow().getThreadManager().startThread(job, "window.setTimeout");
        return id;
    }

    /**
     * Cancels a time-out previously set with the <tt>setTimeout</tt> method.
     *
     * @param timeoutId identifier for the timeout to clear (returned by <tt>setTimeout</tt>)
     */
    public void jsxFunction_clearTimeout(final int timeoutId) {
        getWebWindow().getThreadManager().stopThread(timeoutId);
    }

    /**
     * Return the javascript property "navigator"
     * @return The document
     */
    public Navigator jsxGet_navigator() {
        return navigator_;
    }

    /**
     * Return the window property.  This is a synonym for "self"
     * @return A reference to this
     */
    public Window jsxGet_window() {
        return this;
    }

    /**
     * Return the "self" property
     * @return this
     */
    public Window jsxGet_self() {
        return this;
    }

    /**
     * Return the location property
     * @return The location property
     */
    public Location jsxGet_location() {
        return location_;
    }

    /**
     * Set the location property.  This will cause a reload of the window.
     * @param newLocation The url of the new content.
     * @throws IOException when location loading fails
     */
    public void jsxSet_location(final String newLocation) throws IOException {
        location_.jsxSet_href(newLocation);
    }

    /**
     * Return the "screen" property
     * @return the screen property
     */
    public Screen jsxGet_screen() {
        return screen_;
    }

    /**
     * Return the "history" property
     * @return the "history" property
     */
    public History jsxGet_history() {
        return history_;
    }

    /**
     * Initialize the object.
     * @param webWindow The web window containing the javascript.
     * @exception Exception If an error occurs.
     */
    public void initialize(final WebWindow webWindow) throws Exception {
        webWindow_ = webWindow;
        webWindow_.setScriptObject(this);

        document_ = new Document();
        document_.setParentScope(this);
        document_.setPrototype(getPrototype(Document.class));
        document_.setWindow(this);
        if (webWindow.getEnclosedPage() instanceof HtmlPage) {
            document_.setDomNode((DomNode) webWindow.getEnclosedPage());
        }

        navigator_ = new Navigator();
        navigator_.setParentScope(this);
        navigator_.setPrototype(getPrototype(Navigator.class));

        screen_ = new Screen();
        screen_.setParentScope(this);
        screen_.setPrototype(getPrototype(Screen.class));

        history_ = new History();
        history_.setParentScope(this);
        history_.setPrototype(getPrototype(History.class));

        location_ = new Location();
        location_.setParentScope(this);
        location_.setPrototype(getPrototype(Location.class));
        location_.initialize(this);
    }

    /**
     * Initialize the object.
     * @param enclosedPage The page containing the javascript.
     */
    public void initialize(final Page enclosedPage) {
        if (enclosedPage instanceof HtmlPage) {
            final HtmlPage htmlPage = (HtmlPage) enclosedPage;

            // Windows don't have corresponding DomNodes so set the domNode
            // variable to be the page.  If this isn't set then SimpleScriptable.get()
            // won't work properly
            setDomNode(htmlPage);

            WebAssert.notNull("document_", document_);
            document_.setDomNode(htmlPage);
        }
    }

    /**
     * Initialize the object.  Only call for Windows with no contents.
     */
    public void initialize() {
    }

    /**
     * Return the value of the top property
     * @return The value of "top"
     */
    public SimpleScriptable jsxGet_top() {
        final WebWindow topWebWindow = webWindow_.getTopWindow();
        return (SimpleScriptable) topWebWindow.getScriptObject();
    }

    /**
     * Return the value of the parent property
     * @return the value of window.parent
     */
    public SimpleScriptable jsxGet_parent() {
        final WebWindow parentWebWindow = webWindow_.getParentWindow();
        return (SimpleScriptable) parentWebWindow.getScriptObject();
    }

    /**
     * Return the value of the opener property.
     * @return the value of window.opener, <code>null</code> for a top level window
     */
    public Object jsxGet_opener() {
        if (webWindow_ instanceof TopLevelWindow) {
            final WebWindow opener = ((TopLevelWindow) webWindow_).getOpener();
            if (opener != null) {
                return opener.getScriptObject();
            }
        }

        return null;
    }

    /**
     * Return the (i)frame in which the window is contained.
     * @return <code>null</code> for a top level window
     */
    public Object jsxGet_frameElement() {
        final WebWindow window = getWebWindow();
        if (window instanceof FrameWindow) {
            return ((FrameWindow) window).getFrameElement().getScriptObject();
        }
        else {
            return null;
        }
    }

    /**
     * Return the value of the frames property.
     * @return The live collection of frames
     */
    public HTMLCollection jsxGet_frames() {
        if (frames_ == null) {
            final String xpath = ".//*[(name() = 'frame' or name() = 'iframe')]";
            final HtmlPage page = (HtmlPage) getWebWindow().getEnclosedPage();
            frames_ = new HTMLCollection(this);
            final Transformer toEnclosedWindow = new Transformer() {
                public Object transform(final Object obj) {
                    if (obj instanceof BaseFrame) {
                        return ((BaseFrame) obj).getEnclosedWindow();
                    }
                    else {
                        return ((FrameWindow) obj).getFrameElement().getEnclosedWindow();
                    }
                }
            };
            frames_.init(page, xpath, toEnclosedWindow);
        }

        return frames_;
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
    public void jsxFunction_focus() {
        webWindow_.getWebClient().setCurrentWindow(webWindow_);
    }

    /**
     * Remove focus from this element
     */
    public void jsxFunction_blur() {
        getLog().debug("Window.blur() not implemented");
    }

    /**
     * Close this window
     */
    public void jsxFunction_close() {
        getWebWindow().getWebClient().deregisterWebWindow(getWebWindow());
    }

    /**
     * Indicates if this window is closed
     * @return <code>true</code> if this window is closed
     */
    public boolean jsxGet_closed() {
        return !getWebWindow().getWebClient().getWebWindows().contains(getWebWindow());
    }

    /**
     * Does nothing.
     * @param x The horizontal position
     * @param y The vertical position
     */
    public void jsxFunction_moveTo(final int x, final int y) {
        getLog().debug("Window.moveTo() not implemented");
    }

    /**
     * Does nothing.
     * @param x The horizontal position
     * @param y The vertical position
     */
    public void jsxFunction_moveBy(final int x, final int y) {
        getLog().debug("Window.moveBy() not implemented");
    }

    /**
     * Does nothing.
     * @param width The width offset
     * @param height The height offset.
     */
    public void jsxFunction_resizeBy(final int width, final int height) {
        getLog().debug("Window.resizeBy() not implemented");
    }

    /**
     * Does nothing.
     * @param width The width of the Window in pixel after resize.
     * @param height The height of the Window in pixel after resize.
     */
    public void jsxFunction_resizeTo(final int width, final int height) {
        getLog().debug("Window.resizeTo() not implemented");
    }

    /**
     * Does nothing.
     * @param x The horizontal position to scroll to
     * @param y The vertical position to scroll to
     */
    public void jsxFunction_scroll(final int x, final int y) {
        getLog().debug("Window.scroll() not implemented");
    }

    /**
     * Does nothing.
     * @param x The horizontal distance to scroll by
     * @param y The vertical distance to scroll by
     */
    public void jsxFunction_scrollBy(final int x, final int y) {
        getLog().debug("Window.scrollBy() not implemented");
    }

    /**
     * Does nothing.
     * @param lines The number of lines to scroll down
     */
    public void jsxFunction_scrollByLines(final int lines) {
        getLog().debug("Window.scrollByLines() not implemented");
    }

    /**
     * Does nothing.
     * @param pages The number of pages to scroll down
     */
    public void jsxFunction_scrollByPages(final int pages) {
        getLog().debug("Window.scrollByPages() not implemented");
    }

    /**
     * Does nothing.
     * @param x The horizontal position to scroll to
     * @param y The vertical position to scroll to
     */
    public void jsxFunction_scrollTo(final int x, final int y) {
        getLog().debug("Window.scrollTo() not implemented");
    }

    /**
     * Set the value of the onload event handler.
     * @param newOnload The new handler
     */
    public void jsxSet_onload(final Object newOnload) {
        getEventListenersContainer().setEventHandlerProp("load", newOnload);
    }

    /**
     * Set the value of the onclick event handler.
     * @param newOnload The new handler
     */
    public void jsxSet_onclick(final Object newOnload) {
        getEventListenersContainer().setEventHandlerProp("click", newOnload);
    }

    /**
     * Return the onclick property (caution this is not necessary a function if something else has
     * been set)
     * @return the onclick property
     */
    public Object jsxGet_onclick() {
        return getEventListenersContainer().getEventHandlerProp("click");
    }

    /**
     * Set the value of the ondblclick event handler.
     * @param newHandler The new handler
     */
    public void jsxSet_ondblclick(final Object newHandler) {
        getEventListenersContainer().setEventHandlerProp("dblclick", newHandler);
    }

    /**
     * Return the ondblclick property (caution this is not necessary a function if something else has
     * been set)
     * @return the ondblclick property
     */
    public Object jsxGet_ondblclick() {
        return getEventListenersContainer().getEventHandlerProp("dblclick");
    }

    /**
     * Return the onload property (caution this is not necessary a function if something else has
     * been set)
     * @return the onload property
     */
    public Object jsxGet_onload() {
        final Object onload = getEventListenersContainer().getEventHandlerProp("load");
        if (onload == null) {
            // NB: for IE, the onload of window is the one of the body element but not for Mozilla.
            final HtmlPage page = (HtmlPage) webWindow_.getEnclosedPage();
            final HtmlElement body = page.getBody();
            if (body != null) {
                return body.getEventHandler("onload");
            }
            else {
                return null;
            }
        }
        else {
            return onload;
        }
    }

    /**
     * Gets the container for event listeners
     * @return the container (newly created if needed)
     */
    EventListenersContainer getEventListenersContainer() {
        if (eventListenersContainer_ == null) {
            eventListenersContainer_ = new EventListenersContainer(this);
        }
        return eventListenersContainer_;
    }

    /**
     * Allows the registration of event listeners on the event target
     * @param type the event type to listen for (like "load")
     * @param listener the event listener
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/attachevent.asp">
     * MSDN documentation</a>
     * @return <code>true</code> if the listener has been added
     */
    public boolean jsxFunction_attachEvent(final String type, final Function listener) {
        return getEventListenersContainer().addEventListener(StringUtils.substring(type, 2), listener, false);
    }

    /**
     * Allows the registration of event listeners on the event target
     * @param type the event type to listen for (like "onload")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture (not yet implemented)
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:element.addEventListener">Mozilla documentation</a>
     */
    public void jsxFunction_addEventListener(final String type, final Function listener, final boolean useCapture) {
        getEventListenersContainer().addEventListener(type, listener, useCapture);
    }

    /**
     * Allows the removal of event listeners on the event target
     * @param type the event type to listen for (like "onload")
     * @param listener the event listener
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/detachevent.asp">
     * MSDN documentation</a>
     */
    public void jsxFunction_detachEvent(final String type, final Function listener) {
        getEventListenersContainer().removeEventListener(StringUtils.substring(type, 2), listener, false);
    }

    /**
     * Allows the removal of event listeners on the event target
     * @param type the event type to listen for (like "load")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture (not yet implemented)
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:element.removeEventListener">Mozilla documentation</a>
     */
    public void jsxFunction_removeEventListener(final String type, final Function listener, final boolean useCapture) {
        getEventListenersContainer().removeEventListener(type, listener, useCapture);
    }

    /**
     * Return the value of the name property
     * @return The window name
     */
    public String jsxGet_name() {
        return webWindow_.getName();
    }

     /**
     * Set the value of the newName property
     * @param newName The new window name
     */
    public void jsxSet_name(final String newName) {
        webWindow_.setName(newName);
    }

    /**
     * Return the value of the onerror property
     * @return The value
     */
    public String jsxGet_onerror() {
        getLog().debug("Window.onerror not implemented");
        return "";
    }

    /**
     * Set the value of the onerror property
     * @param newValue The value
     */
    public void jsxSet_onerror(final String newValue) {
        getLog().debug("Window.onerror not implemented");
    }

    /**
     * Looks at attributes with the given name
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        Object result = NOT_FOUND;
        final DomNode domNode = getDomNodeOrNull();
        if (domNode != null) {
            result = getFrameByName((HtmlPage) domNode.getPage(), name);
        }

        // See if it is an attempt to access an element directly by name or id if we are emulating IE.
        if (result == NOT_FOUND) {
            // this tests are quite silly and should be removed when custom JS objects have a clean
            // way to get the WebClient they are running in.
            if (domNode != null && getBrowserVersion().isIE()) {
                final HTMLCollection array = (HTMLCollection) document_.jsxFunction_getElementsByName(name);
                final int length = array.jsxGet_length();
                if (length == 1) {
                    result = array.get(0, array);
                }
                else if (length > 1) {
                    result = array;
                }
                else {
                    result = document_.jsxFunction_getElementById(name);
                    if (result == null) {
                        result = NOT_FOUND;
                    }
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name, final Scriptable start) {
        // Hack to make eval work in other window scope when needed.
        // See unit test testEvalScopeOtherWindow().
        // TODO: Find a cleaner way to handle this.
        if ("eval".equals(name)) {
            final Window w = (Window) getTopScope(getStartingScope());
            if (w != this) {
                return getAssociatedValue("custom_eval");
            }
        }
        else if ("Option".equals(name)) {
            name = "HTMLOptionElement";
        }
        else if ("Image".equals(name)) {
            name = "HTMLImageElement";
        }
        final Object superValue = super.get(name, start);
        if (superValue == NOT_FOUND && getWebWindow() != null && getBrowserVersion().isIE()) {
            final Object element = jsxGet_document().jsxFunction_getElementById(name);
            if (element instanceof HTMLUnknownElement) {
                final HtmlElement unknownElement = ((HTMLUnknownElement) element).getHtmlElementOrDie();
                if (unknownElement.getNodeName().equals("xml")) {
                    final XMLDocument document = ActiveXObject.buildXMLDocument(getWebWindow());
                    document.setParentScope(this);
                    final Iterator<HtmlElement> children = unknownElement.getAllHtmlChildElements().iterator();
                    if (children.hasNext()) {
                        final HtmlElement root = children.next();
                        document.jsxFunction_loadXML(root.asXml().trim());
                    }
                    return document;
                }
            }
            
        }
        return superValue;
    }

    private Scriptable getTopScope(final Scriptable s) {
        Scriptable top = s;
        while (top != null && top.getParentScope() != null) {
            top = top.getParentScope();
        }
        return top;
    }

    private Object getFrameByName(final HtmlPage page, final String name) {
        try {
            return page.getFrameByName(name).getScriptObject();
        }
        catch (final ElementNotFoundException e) {
            return NOT_FOUND;
        }
    }

    /**
     * Executes the specified script code as long as the laguage is JavaScript or JScript. Does
     * nothing if the language specified is VBScript.
     * @param script the script code to execute
     * @param language the language of the specified code ("JavaScript", "JScript" or "VBScript")
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/execscript.asp">
     * MSDN documentation</a>
     * @return this method always returns <code>null</code>, like Internet Explorer
     */
    public Object jsxFunction_execScript(final String script, final String language) {
        if ("javascript".equalsIgnoreCase(language) || "jscript".equalsIgnoreCase(language)) {
            custom_eval(script);
            return null;
        }
        else if ("vbscript".equalsIgnoreCase(language)) {
            getLog().warn("VBScript not supported in Window.execScript().");
        }
        else {
            // Unrecognized language: use the IE error message ("Invalid class string").
            throw Context.reportRuntimeError("Invalid class string");
        }
        return null;
    }

    /**
     * Executes the specified script code in the scope of this window.
     * This is used only when eval() is called on a Window other than the starting scope
     * @param scriptCode some javascript code
     * @return the evaluation result
     */
    public Object custom_eval(final String scriptCode) {
        final Context context = Context.getCurrentContext();
        final org.mozilla.javascript.Script script = context.compileString(scriptCode, "eval body", 0, null);
        return script.exec(context, this);
    }

    /**
     * Return the text from the status line.
     * @return the status line text
     */
    public String jsxGet_status() {
        return status_;
    }

    /**
     * Set the text from the status line.
     * @param message the status line text
     */
    public void jsxSet_status(final String message) {
        status_ = message;

        final StatusHandler statusHandler = webWindow_.getWebClient().getStatusHandler();
        if (statusHandler != null) {
            statusHandler.statusMessageChanged(webWindow_.getEnclosedPage(), message);
        }
    }

    /**
     * Set a chunk of javascript to be invoked each time a specified number of milliseconds has elapsed
     * Current implementation does nothing.
     *
     * JavaScript param 1: The code to execute, either a String or a Function.
     * JavaScript param 2: the delay in milliseconds to wait before executing the code.
     *
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/setinterval.asp">
     * MSDN documentation</a>
     *
     * @param context The javascript Context
     * @param scriptable The object that the function was called on.
     * @param args The arguments passed to the function.
     * @param function The function object that was invoked.
     * @return the id of the created interval
     */
    public static int jsxFunction_setInterval(final Context context, final Scriptable scriptable,
            final Object[] args, final Function function) {
        final Window thisWindow = (Window) scriptable;
        final Object codeToExec = getObjectArg(0, args, null);
        final int timeout = getIntArg(1, args, 0);

        thisWindow.getLog().debug("setInterval(" + codeToExec + ", " + timeout + ")");
        final Runnable job = createJavaScriptBackgroundJob(codeToExec, timeout, thisWindow, true, "setInterval");
        final int id = thisWindow.getWebWindow().getThreadManager().startThread(job, "window.setInterval");
        return id;
    }

    /**
     * Cancels the interval previously started using the setInterval method.
     * Current implementation does nothing.
     * @param intervalID specifies the interval to cancel as returned by the setInterval method.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/clearinterval.asp">
     * MSDN documentation</a>
     */
    public void jsxFunction_clearInterval(final int intervalID) {
        getWebWindow().getThreadManager().stopThread(intervalID);
    }

    /**
     * Return the innerWidth.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref28.html">Mozilla doc</a>
     */
    public int jsxGet_innerWidth() {
        return 1276; // why this value? this is the current value of my Mozilla
    }

    /**
     * Return the outerWidth.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref79.html">Mozilla doc</a>
     */
    public int jsxGet_outerWidth() {
        return 1276; // why this value? this is the current value of my Mozilla
    }

    /**
     * Return the innerHeight.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref27.html">Mozilla doc</a>
     */
    public int jsxGet_innerHeight() {
        return 778; // why this value? this is the current value of my Mozilla
    }

    /**
     * Return the outer height.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref78.html">Mozilla doc</a>
     */
    public int jsxGet_outerHeight() {
        return 936; // why this value? this is the current value of my Mozilla
    }

    /**
     * Prints the current page. The current implementation does nothing.
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref85.html">
     * Mozilla documentation</a>
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/print.asp">
     * MSDN documentation</a>
     */
    public void jsxFunction_print() {
        getLog().debug("window.print() not implemented");
    }

    /**
     * Does nothing special anymore... just like FF.
     * @param type the type of events
     */
    public void jsxFunction_captureEvents(final String type) {
        // nothing
    }

    /**
     * Returns computed style of the element. Computed style represents the final computed values
     * of all CSS properties for the element. This method's return value is of the same type as
     * that of <tt>element.style</tt>, but the value returned by this method is read-only.
     *
     * @param element the element
     * @param pseudoElt a string specifying the pseudo-element to match (may be <tt>null</tt>)
     * @return the computed style
     */
    public ComputedCSSStyleDeclaration jsxFunction_getComputedStyle(final Node element, final String pseudoElt) {
        final HTMLElement e = (HTMLElement) element;
        final CSSStyleDeclaration original = (CSSStyleDeclaration) e.jsxGet_style();
        final ComputedCSSStyleDeclaration style = new ComputedCSSStyleDeclaration(original);

        final StyleSheetList sheets = (StyleSheetList) document_.jsxGet_styleSheets();
        for (int i = 0; i < sheets.jsxGet_length(); i++) {
            final Stylesheet sheet = sheets.jsxFunction_item(i);
            sheet.modifyIfNecessary(style, e);
        }

        return style;
    }

    /**
     * An undocumented function of IE
     */
    public void jsxFunction_CollectGarbage() {
        // nothing
    }
}
