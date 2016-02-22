/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BODY_MARGINS_8;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.util.Locale;

import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventListenersContainer;

/**
 * The JavaScript object {@code HTMLBodyElement}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
@JsxClass(domClass = HtmlBody.class)
public class HTMLBodyElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public HTMLBodyElement() {
    }

    /**
     * Creates the event handler from the attribute value. This has to be done no matter which browser
     * is simulated to handle ill-formed HTML code with many body (possibly generated) elements.
     * @param attributeName the attribute name
     * @param value the value
     */
    public void createEventHandlerFromAttribute(final String attributeName, final String value) {
        // when many body tags are found while parsing, attributes of
        // different tags are added and should create an event handler when needed
        if (attributeName.toLowerCase(Locale.ROOT).startsWith("on")) {
            createEventHandler(attributeName, value);
        }
    }

    /**
     * Forwards the events to window.
     *
     * {@inheritDoc}
     *
     * @see HTMLFrameSetElement#getEventListenersContainer()
     */
    @Override
    public EventListenersContainer getEventListenersContainer() {
        return getWindow().getEventListenersContainer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(final ComputedCSSStyleDeclaration style) {
        if (getBrowserVersion().hasFeature(JS_BODY_MARGINS_8)) {
            style.setDefaultLocalStyleAttribute("margin", "8px");
            style.setDefaultLocalStyleAttribute("padding", "0px");
        }
        else {
            style.setDefaultLocalStyleAttribute("margin-left", "8px");
            style.setDefaultLocalStyleAttribute("margin-right", "8px");
            style.setDefaultLocalStyleAttribute("margin-top", "8px");
            style.setDefaultLocalStyleAttribute("margin-bottom", "8px");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLElement getOffsetParent_js() {
        return null;
    }

    /**
     * Returns the value of the {@code aLink} attribute.
     * @return the value of the {@code aLink} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533070.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getALink() {
        return getDomNodeOrDie().getAttribute("aLink");
    }

    /**
     * Sets the value of the {@code aLink} attribute.
     * @param aLink the value of the {@code aLink} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533070.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setALink(final String aLink) {
        setColorAttribute("aLink", aLink);
    }

    /**
     * Returns the value of the {@code background} attribute.
     * @return the value of the {@code background} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533498.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getBackground() {
        final HtmlElement node = getDomNodeOrDie();
        return node.getAttribute("background");
    }

    /**
     * Sets the value of the {@code background} attribute.
     * @param background the value of the {@code background} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533498.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setBackground(final String background) {
        getDomNodeOrDie().setAttribute("background", background);
    }

    /**
     * Returns the value of the {@code bgColor} attribute.
     * @return the value of the {@code bgColor} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getBgColor() {
        return getDomNodeOrDie().getAttribute("bgColor");
    }

    /**
     * Sets the value of the {@code bgColor} attribute.
     * @param bgColor the value of the {@code bgColor} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setBgColor(final String bgColor) {
        setColorAttribute("bgColor", bgColor);
    }

    /**
     * Returns the value of the {@code link} attribute.
     * @return the value of the {@code link} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534119.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getLink() {
        return getDomNodeOrDie().getAttribute("link");
    }

    /**
     * Sets the value of the {@code link} attribute.
     * @param link the value of the {@code link} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534119.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setLink(final String link) {
        setColorAttribute("link", link);
    }

    /**
     * Returns the value of the {@code text} attribute.
     * @return the value of the {@code text} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getText() {
        return getDomNodeOrDie().getAttribute("text");
    }

    /**
     * Sets the value of the {@code text} attribute.
     * @param text the value of the {@code text} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setText(final String text) {
        setColorAttribute("text", text);
    }

    /**
     * Returns the value of the {@code vLink} attribute.
     * @return the value of the {@code vLink} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getVLink() {
        return getDomNodeOrDie().getAttribute("vLink");
    }

    /**
     * Sets the value of the {@code vLink} attribute.
     * @param vLink the value of the {@code vLink} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setVLink(final String vLink) {
        setColorAttribute("vLink", vLink);
    }
}
