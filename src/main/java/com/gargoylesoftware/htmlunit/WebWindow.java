/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

/**
 * An interface that represents one window in a browser. It could be a top level window or a frame.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author David D. Kilzer
 */
public interface WebWindow extends Serializable {

    /**
     * Returns the name of this window.
     *
     * @return the name of this window
     */
    String getName();

    /**
     * Sets the name of this window.
     *
     * @param name the new window name
     */
    void setName(final String name);

    /**
     * Returns the currently loaded page or null if no page has been loaded.
     *
     * @return the currently loaded page or null if no page has been loaded
     */
    Page getEnclosedPage();

    /**
     * Sets the currently loaded page.
     *
     * @param page the new page or null if there is no page (ie empty window)
     */
    void setEnclosedPage(final Page page);

    /**
     * Returns the window that contains this window. If this is a top
     * level window, then return this window.
     *
     * @return the parent window or this window if there is no parent
     */
    WebWindow getParentWindow();

    /**
     * Returns the top level window that contains this window. If this
     * is a top level window, then return this window.
     *
     * @return the top level window that contains this window or this
     * window if there is no parent.
     */
    WebWindow getTopWindow();

    /**
     * Returns the web client that "owns" this window.
     *
     * @return the web client or null if this window has been closed
     */
    WebClient getWebClient();

    /**
     * Returns this window's navigation history.
     *
     * @return this window's navigation history
     */
    History getHistory();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Sets the JavaScript object that corresponds to this element. This is not guaranteed
     * to be set even if there is a JavaScript object for this HTML element.
     *
     * @param scriptObject the JavaScript object
     */
    void setScriptObject(final Object scriptObject);

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the JavaScript object that corresponds to this element.
     *
     * @return the JavaScript object that corresponds to this element
     */
    Object getScriptObject();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the job manager for this window.
     *
     * @return the job manager for this window
     */
    JavaScriptJobManager getJobManager();

    /**
     * Indicates if this window is closed. No action should be performed on a closed window.
     * @return <code>true</code> when the window is closed
     */
    boolean isClosed();

    /**
     * Returns the width (in pixels) of the browser window viewport including, if rendered, the vertical scrollbar.
     * @return the inner width.
     */
    int getInnerWidth();

    /**
     * Sets the width (in pixels) of the browser window viewport including, if rendered, the vertical scrollbar.
     * @param innerWidth the inner width
     */
    void setInnerWidth(final int innerWidth);

    /**
     * Returns the width of the outside of the browser window.
     * It represents the width of the whole browser window including sidebar (if expanded),
     * window chrome and window resizing borders/handles.
     * @return the outer width
     */
    int getOuterWidth();

    /**
     * Sets the width of the outside of the browser window.
     * It represents the width of the whole browser window including sidebar (if expanded),
     * window chrome and window resizing borders/handles.
     * @param outerWidth the outer width
     */
    void setOuterWidth(final int outerWidth);

    /**
     * Returns the height (in pixels) of the browser window viewport including, if rendered, the horizontal scrollbar.
     * @return a inner height
     */
    int getInnerHeight();

    /**
     * Sets the height (in pixels) of the browser window viewport including, if rendered, the horizontal scrollbar.
     * @param innerHeight the inner height
     */
    void setInnerHeight(final int innerHeight);

    /**
     * Returns the height in pixels of the whole browser window.
     * It represents the height of the whole browser window including sidebar (if expanded),
     * window chrome and window resizing borders/handles.
     * @return the outer height
     */
    int getOuterHeight();

    /**
     * Sets the height in pixels of the whole browser window.
     * It represents the height of the whole browser window including sidebar (if expanded),
     * window chrome and window resizing borders/handles.
     * @param outerHeight the outer height
     */
    void setOuterHeight(final int outerHeight);
}
