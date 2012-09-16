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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.annotations.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.annotations.BrowserName.IE;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.annotations.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for {@link com.gargoylesoftware.htmlunit.html.HtmlInlineFrame}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class HTMLIFrameElement extends HTMLElement {

    /**
     * Creates an instance. A default constructor is required for all JavaScript objects.
     */
    public HTMLIFrameElement() { }

    /**
     * Returns the value of URL loaded in the frame.
     * @return the value of this attribute
     */
    @JsxGetter
    public String jsxGet_src() {
        return getFrame().getSrcAttribute();
    }

    /**
     * Returns the document the frame contains, if any.
     * @return <code>null</code> if no document is contained
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_frame_ref4.html">Gecko DOM Reference</a>
     */
    @JsxGetter(@WebBrowser(FF))
    public DocumentProxy jsxGet_contentDocument() {
        return ((Window) getFrame().getEnclosedWindow().getScriptObject()).jsxGet_document();
    }

    /**
     * Returns the window the frame contains, if any.
     * @return the window
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_frame_ref5.html">
     * Gecko DOM Reference</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533692.aspx">MSDN documentation</a>
     */
    @JsxGetter
    public Window jsxGet_contentWindow() {
        return (Window) getFrame().getEnclosedWindow().getScriptObject();
    }

    /**
     * Sets the value of the source of the contained frame.
     * @param src the new value
     */
    @JsxSetter
    public void jsxSet_src(final String src) {
        getFrame().setSrcAttribute(src);
    }

    /**
     * Returns the value of the name attribute.
     * @return the value of this attribute
     */
    @JsxGetter
    public String jsxGet_name() {
        return getFrame().getNameAttribute();
    }

    /**
     * Sets the value of the name attribute.
     * @param name the new value
     */
    @JsxSetter
    public void jsxSet_name(final String name) {
        getFrame().setNameAttribute(name);
    }

    private BaseFrameElement getFrame() {
        return (BaseFrameElement) getDomNodeOrDie();
    }

    /**
     * Sets the <tt>onload</tt> event handler for this element.
     * @param eventHandler the <tt>onload</tt> event handler for this element
     */
    @JsxSetter
    public void jsxSet_onload(final Object eventHandler) {
        setEventHandlerProp("onload", eventHandler);
    }

    /**
     * Returns the <tt>onload</tt> event handler for this element.
     * @return the <tt>onload</tt> event handler for this element
     */
    @JsxGetter
    public Object jsxGet_onload() {
        return getEventHandlerProp("onload");
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String jsxGet_border() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void jsxSet_border(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    @JsxGetter
    public String jsxGet_align() {
        return getAlign(true);
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    @JsxSetter
    public void jsxSet_align(final String align) {
        setAlign(align, false);
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    @JsxGetter
    public String jsxGet_width() {
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_IFRAME_GET_WIDTH_NEGATIVE_VALUES);
        final Boolean returnNegativeValues = ie ? Boolean.TRUE : null;
        return getWidthOrHeight("width", returnNegativeValues);
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    @JsxSetter
    public void jsxSet_width(final String width) {
        setWidthOrHeight("width", width, Boolean.TRUE);
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    @JsxGetter
    public String jsxGet_height() {
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_IFRAME_GET_HEIGHT_NEGATIVE_VALUES);
        final Boolean returnNegativeValues = ie ? Boolean.TRUE : null;
        return getWidthOrHeight("height", returnNegativeValues);
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    @JsxSetter
    public void jsxSet_height(final String width) {
        setWidthOrHeight("height", width, Boolean.TRUE);
    }

}
