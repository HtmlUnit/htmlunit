/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.html.BaseFrame;

/**
 * A JavaScript object for a frame and iframe.
 * 
 * @version $Revision$
 * @author Marc Guillemot
 * @author Chris Erskine
 */
public class Frame extends HTMLElement {
    private static final long serialVersionUID = 3761121622400448304L;

    /**
     * Create an instance.  A default constructor is required for all javascript objects.
     */
    public Frame() { }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public final void jsConstructor() {
    }


    /**
     * Return the value of url loaded in the frame
     * @return The value of this attribute.
     */
    public String jsxGet_src() {
        return getFrame().getSrcAttribute();
    }


    /**
     * Set the value of the source of the contained frame.
     * @param src The new value.
     */
    public void jsxSet_src(final String src) {
        getFrame().setSrcAttribute(src);
    }

    private BaseFrame getFrame() {
        return (BaseFrame) getHtmlElementOrDie();
    }


    /**
     * Set the onload event handler for this element.
     * @param eventHandler the new handler
     */
    public void jsxSet_onload(final Function eventHandler) {
        getHtmlElementOrDie().setEventHandler("onload", eventHandler);
    }


    /**
     * Get the onload event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onload() {
        return getHtmlElementOrDie().getEventHandler("onload");
    }
}
