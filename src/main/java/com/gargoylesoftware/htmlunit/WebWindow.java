/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

import com.gargoylesoftware.htmlunit.css.ComputedCssStyleDeclaration;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

/**
 * An interface that represents one window in a browser. It could be a top level window or a frame.
 *
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
    void setName(String name);

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
    void setEnclosedPage(Page page);

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
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Sets the JavaScript object that corresponds to this element. This is not guaranteed
     * to be set even if there is a JavaScript object for this HTML element.
     *
     * @param <T> the object type
     * @param scriptObject the JavaScript object
     */
    <T> void setScriptableObject(T scriptObject);

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns the JavaScript object that corresponds to this element.
     *
     * @param <T> the object type
     * @return the JavaScript object that corresponds to this element
     */
    <T> T getScriptableObject();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns the job manager for this window.
     *
     * @return the job manager for this window
     */
    JavaScriptJobManager getJobManager();

    /**
     * Indicates if this window is closed. No action should be performed on a closed window.
     * @return {@code true} when the window is closed
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
    void setInnerWidth(int innerWidth);

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
    void setOuterWidth(int outerWidth);

    /**
     * Returns the height (in pixels) of the browser window viewport including, if rendered, the horizontal scrollbar.
     * @return a inner height
     */
    int getInnerHeight();

    /**
     * Sets the height (in pixels) of the browser window viewport including, if rendered, the horizontal scrollbar.
     * @param innerHeight the inner height
     */
    void setInnerHeight(int innerHeight);

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
    void setOuterHeight(int outerHeight);

    /**
     * @return the screen this window belongs to
     */
    Screen getScreen();

    /**
    * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
    *
     * Returns computed style of the element. Computed style represents the final computed values
     * of all CSS properties for the element. This method's return value is of the same type as
     * that of <code>element.style</code>, but the value returned by this method is read-only.
     *
     * @param element the element
     * @param pseudoElement a string specifying the pseudo-element to match (may be {@code null});
     * e.g. ':before'
     * @return the computed style
     */
    ComputedCssStyleDeclaration getComputedStyle(DomElement element, String pseudoElement);
}
