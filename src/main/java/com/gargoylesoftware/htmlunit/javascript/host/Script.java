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

import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * The JavaScript object that represents a "script".
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class Script extends HTMLElement {

    private static final long serialVersionUID = -4626517931702326308L;

    /** The <tt>onreadystatechange</tt> handler. */
    private Function stateChangeHandler_;

    /**
     * Creates an instance.
     */
    public Script() {
        // Empty.
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because the Rhino
     * engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
        // Empty.
    }

    /**
     * Returns the <tt>src</tt> attribute.
     * @return The <tt>src</tt> attribute.
     */
    public String jsxGet_src() {
        return getHtmlElementOrDie().getAttributeValue("src");
    }

    /**
     * Sets the <tt>src</tt> attribute.
     * @param src The <tt>src</tt> attribute.
     */
    public void jsxSet_src(final String src) {
        getHtmlElementOrDie().setAttributeValue("src", src);
    }

    /**
     * Returns the <tt>type</tt> attribute.
     * @return The <tt>type</tt> attribute.
     */
    public String jsxGet_type() {
        return getHtmlElementOrDie().getAttributeValue("type");
    }

    /**
     * Sets the <tt>type</tt> attribute.
     * @param type The <tt>type</tt> attribute.
     */
    public void jsxSet_type(final String type) {
        getHtmlElementOrDie().setAttributeValue("type", type);
    }

    /**
     * Returns the event handler that fires on every state change.
     * @return The event handler that fires on every state change.
     */
    public Function jsxGet_onreadystatechange() {
        return stateChangeHandler_;
    }

    /**
     * Sets the event handler that fires on every state change.
     * @param stateChangeHandler The event handler that fires on every state change.
     */
    public void jsxSet_onreadystatechange(final Function stateChangeHandler) {
        stateChangeHandler_ = stateChangeHandler;
    }

    /**
     * Returns the ready state of the script. This is an IE-only property.
     * @return The ready state of the script.
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
