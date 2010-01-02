/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;

/**
 * The JavaScript object that represents an "HTMLScriptElement".
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLScriptElement extends HTMLElement {

    private static final long serialVersionUID = -4626517931702326308L;

    /**
     * Creates an instance.
     */
    public HTMLScriptElement() {
        // Empty.
    }

    /**
     * Returns the <tt>src</tt> attribute.
     * @return the <tt>src</tt> attribute
     */
    public String jsxGet_src() {
        return getDomNodeOrDie().getAttribute("src");
    }

    /**
     * Sets the <tt>src</tt> attribute.
     * @param src the <tt>src</tt> attribute
     */
    public void jsxSet_src(final String src) {
        getDomNodeOrDie().setAttribute("src", src);
    }

    /**
     * Returns the <tt>text</tt> attribute.
     * @return the <tt>text</tt> attribute
     */
    @Override
    public String jsxGet_text() {
        final DomNode firstChild = getDomNodeOrDie().getFirstChild();
        if (firstChild != null) {
            return firstChild.getNodeValue();
        }
        return "";
    }

    /**
     * Sets the <tt>text</tt> attribute.
     * @param text the <tt>text</tt> attribute
     */
    public void jsxSet_text(final String text) {
        final DomNode htmlElement = getDomNodeOrDie();
        DomNode firstChild = htmlElement.getFirstChild();
        if (firstChild == null) {
            firstChild = new DomText(htmlElement.getPage(), text);
            htmlElement.appendChild(firstChild);
        }
        else {
            firstChild.setNodeValue(text);
        }
    }

    /**
     * Returns the <tt>type</tt> attribute.
     * @return the <tt>type</tt> attribute
     */
    public String jsxGet_type() {
        return getDomNodeOrDie().getAttribute("type");
    }

    /**
     * Sets the <tt>type</tt> attribute.
     * @param type the <tt>type</tt> attribute
     */
    public void jsxSet_type(final String type) {
        getDomNodeOrDie().setAttribute("type", type);
    }

    /**
     * Returns the event handler that fires on every state change.
     * @return the event handler that fires on every state change
     */
    public Object jsxGet_onreadystatechange() {
        return getEventHandlerProp("onreadystatechange");
    }

    /**
     * Sets the event handler that fires on every state change.
     * @param handler the event handler that fires on every state change
     */
    public void jsxSet_onreadystatechange(final Object handler) {
        setEventHandlerProp("onreadystatechange", handler);
    }

    /**
     * Returns the event handler that fires on load.
     * @return the event handler that fires on load
     */
    public Object jsxGet_onload() {
        return getEventHandlerProp("onload");
    }

    /**
     * Sets the event handler that fires on load.
     * @param handler the event handler that fires on load
     */
    public void jsxSet_onload(final Object handler) {
        setEventHandlerProp("onload", handler);
    }

    /**
     * Returns the ready state of the script. This is an IE-only property.
     * @return the ready state of the script
     * @see DomNode#READY_STATE_UNINITIALIZED
     * @see DomNode#READY_STATE_LOADING
     * @see DomNode#READY_STATE_LOADED
     * @see DomNode#READY_STATE_INTERACTIVE
     * @see DomNode#READY_STATE_COMPLETE
     */
    public String jsxGet_readyState() {
        final DomNode node = getDomNodeOrDie();
        return node.getReadyState();
    }

}
