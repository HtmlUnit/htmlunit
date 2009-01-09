/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.BaseFrame;

/**
 * A JavaScript object for a {@link com.gargoylesoftware.htmlunit.html.HtmlFrame}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class HTMLFrameElement extends HTMLElement {
    private static final long serialVersionUID = 3761121622400448304L;

    /**
     * Create an instance. A default constructor is required for all JavaScript objects.
     */
    public HTMLFrameElement() {
    }

    /**
     * Returns the value of URL loaded in the frame.
     * @return the value of this attribute
     */
    public String jsxGet_src() {
        return getFrame().getSrcAttribute();
    }

    /**
     * Returns the document the frame contains, if any.
     * @return <code>null</code> if no document is contained
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_frame_ref4.html">
     * Gecko DOM Reference</a>
     */
    public HTMLDocument jsxGet_contentDocument() {
        return ((Window) getFrame().getEnclosedWindow().getScriptObject()).jsxGet_document();
    }

    /**
     * Returns the window the frame contains, if any.
     * @return the window
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_frame_ref5.html">
     * Gecko DOM Reference</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533692.aspx">MSDN documentation</a>
     */
    public Scriptable jsxGet_contentWindow() {
        return new WindowProxy(getFrame().getEnclosedWindow());
    }

    /**
     * Sets the value of the source of the contained frame.
     * @param src the new value
     */
    public void jsxSet_src(final String src) {
        getFrame().setSrcAttribute(src);
    }

    /**
     * Returns the value of the name attribute.
     * @return the value of this attribute
     */
    public String jsxGet_name() {
        return getFrame().getNameAttribute();
    }

    /**
     * Sets the value of the name attribute.
     * @param name the new value
     */
    public void jsxSet_name(final String name) {
        getFrame().setNameAttribute(name);
    }

    private BaseFrame getFrame() {
        return (BaseFrame) getDomNodeOrDie();
    }

    /**
     * Sets the <tt>onload</tt> event handler for this element.
     * @param eventHandler the <tt>onload</tt> event handler for this element
     */
    public void jsxSet_onload(final Object eventHandler) {
        setEventHandlerProp("onload", eventHandler);
    }

    /**
     * Returns the <tt>onload</tt> event handler for this element.
     * @return the <tt>onload</tt> event handler for this element
     */
    public Object jsxGet_onload() {
        return getEventHandlerProp("onload");
    }
}
