/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for {@link HtmlInlineFrame}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlInlineFrame.class)
public class HTMLIFrameElement extends HTMLElement {

    /**
     * Returns the value of URL loaded in the frame.
     * @return the value of this attribute
     */
    @JsxGetter
    public String getSrc() {
        return getFrame().getSrcAttribute();
    }

    /**
     * Returns the document the frame contains, if any.
     * @return <code>null</code> if no document is contained
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_frame_ref4.html">Gecko DOM Reference</a>
     */
    @JsxGetter(@WebBrowser(FF))
    public DocumentProxy getContentDocument() {
        return getContentWindow().getDocument_js();
    }

    /**
     * Returns the window the frame contains, if any.
     * @return the window
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_frame_ref5.html">
     * Gecko DOM Reference</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533692.aspx">MSDN documentation</a>
     */
    @JsxGetter
    public Window getContentWindow() {
        return (Window) getFrame().getEnclosedWindow().getScriptObject();
    }

    /**
     * Sets the value of the source of the contained frame.
     * @param src the new value
     */
    @JsxSetter
    public void setSrc(final String src) {
        getFrame().setSrcAttribute(src);
    }

    /**
     * Returns the value of the name attribute.
     * @return the value of this attribute
     */
    @JsxGetter
    public String getName() {
        return getFrame().getNameAttribute();
    }

    /**
     * Sets the value of the name attribute.
     * @param name the new value
     */
    @JsxSetter
    public void setName(final String name) {
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
    public void setOnload(final Object eventHandler) {
        setEventHandlerProp("onload", eventHandler);
    }

    /**
     * Returns the <tt>onload</tt> event handler for this element.
     * @return the <tt>onload</tt> event handler for this element
     */
    @JsxGetter
    public Object getOnload() {
        return getEventHandlerProp("onload");
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBorder() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    @JsxGetter
    public String getAlign() {
        return getAlign(true);
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    @JsxSetter
    public void setAlign(final String align) {
        setAlign(align, false);
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    @JsxGetter(propertyName = "width")
    public String getWidth_js() {
        return getWidthOrHeight("width", Boolean.TRUE);
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    @JsxSetter
    public void setWidth(final String width) {
        setWidthOrHeight("width", width, true);
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    @JsxGetter(propertyName = "height")
    public String getHeight_js() {
        return getWidthOrHeight("height", Boolean.TRUE);
    }

    /**
     * Sets the value of the "height" property.
     * @param height the value of the "height" property
     */
    @JsxSetter
    public void setHeight(final String height) {
        setWidthOrHeight("height", height, true);
    }
}
