/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FRAME_CONTENT_DOCUMENT_ACCESS_DENIED_THROWS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_IFRAME_ALWAYS_EXECUTE_ONLOAD;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.FrameWindow.PageDenied;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.WindowProxy;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * A JavaScript object for {@link HtmlInlineFrame}.
 *
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlInlineFrame.class)
public class HTMLIFrameElement extends HTMLElement {

    /** During {@link #setOnload(Object)}, was the element attached to the page. */
    private boolean isAttachedToPageDuringOnload_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLIFrameElement() {
    }

    /**
     * Returns the value of URL loaded in the frame.
     * @return the value of this attribute
     */
    @JsxGetter
    public String getSrc() {
        return getFrame().getSrcAttribute();
    }

    /**
     * Sets the value of the source of the contained frame.
     * @param src the new value
     */
    @JsxSetter
    public void setSrc(final String src) {
        getFrame().setSrcAttribute(src);
        isAttachedToPageDuringOnload_ = false;
    }

    /**
     * Returns the document the frame contains, if any.
     * @return {@code null} if no document is contained
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_frame_ref4.html">Gecko DOM Reference</a>
     */
    @JsxGetter
    public DocumentProxy getContentDocument() {
        final FrameWindow frameWindow = getFrame().getEnclosedWindow();
        if (PageDenied.NONE != frameWindow.getPageDenied()) {
            if (getBrowserVersion().hasFeature(JS_FRAME_CONTENT_DOCUMENT_ACCESS_DENIED_THROWS)) {
                throw Context.reportRuntimeError("Error access denied");
            }
            return null;
        }
        return ((Window) frameWindow.getScriptableObject()).getDocument_js();
    }

    /**
     * Returns the window the frame contains, if any.
     * @return the window
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_frame_ref5.html">Gecko DOM Reference</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533692.aspx">MSDN documentation</a>
     */
    @JsxGetter
    public WindowProxy getContentWindow() {
        final FrameWindow frameWin = getFrame().getEnclosedWindow();
        if (frameWin.isClosed()) {
            return null;
        }
        return Window.getProxy(frameWin);
    }

    /**
     * Returns the value of the name attribute.
     * @return the value of this attribute
     */
    @JsxGetter
    @Override
    public String getName() {
        return getFrame().getNameAttribute();
    }

    /**
     * Sets the value of the name attribute.
     * @param name the new value
     */
    @JsxSetter
    @Override
    public void setName(final String name) {
        getFrame().setNameAttribute(name);
    }

    private BaseFrameElement getFrame() {
        return (BaseFrameElement) getDomNodeOrDie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnload(final Object eventHandler) {
        super.setOnload(eventHandler);
        isAttachedToPageDuringOnload_ = getDomNodeOrDie().isAttachedToPage();
    }

    /**
     * Gets the {@code border} attribute.
     * @return the {@code border} attribute
     */
    @JsxGetter(IE)
    public String getBorder() {
        final String border = getDomNodeOrDie().getAttributeDirect("border");
        return border;
    }

    /**
     * Sets the {@code border} attribute.
     * @param border the {@code border} attribute
     */
    @JsxSetter(IE)
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the {@code align} property.
     * @return the value of the {@code align} property
     */
    @JsxGetter
    public String getAlign() {
        return getAlign(true);
    }

    /**
     * Sets the value of the {@code align} property.
     * @param align the value of the {@code align} property
     */
    @JsxSetter
    public void setAlign(final String align) {
        setAlign(align, false);
    }

    /**
     * Returns the value of the {@code width} property.
     * @return the value of the {@code width} property
     */
    @JsxGetter(propertyName = "width")
    public String getWidth_js() {
        return getWidthOrHeight("width", Boolean.TRUE);
    }

    /**
     * Sets the value of the {@code width} property.
     * @param width the value of the {@code width} property
     */
    @JsxSetter
    public void setWidth(final String width) {
        setWidthOrHeight("width", width, true);
    }

    /**
     * Returns the value of the {@code width} property.
     * @return the value of the {@code width} property
     */
    @JsxGetter(propertyName = "height")
    public String getHeight_js() {
        return getWidthOrHeight("height", Boolean.TRUE);
    }

    /**
     * Sets the value of the {@code height} property.
     * @param height the value of the {@code height} property
     */
    @JsxSetter
    public void setHeight(final String height) {
        setWidthOrHeight("height", height, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeEventLocally(final Event event) {
        if (Event.TYPE_LOAD != event.getType()
                || !isAttachedToPageDuringOnload_ || getBrowserVersion().hasFeature(JS_IFRAME_ALWAYS_EXECUTE_ONLOAD)) {
            super.executeEventLocally(event);
        }
    }

    /**
     * To be called when the frame is being refreshed.
     */
    public void onRefresh() {
        isAttachedToPageDuringOnload_ = false;
    }
}
