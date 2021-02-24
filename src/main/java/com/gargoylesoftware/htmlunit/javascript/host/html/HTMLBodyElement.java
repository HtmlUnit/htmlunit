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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BODY_MARGINS_8;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.util.Locale;

import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TextRange;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;

import net.sourceforge.htmlunit.corejs.javascript.Function;

/**
 * The JavaScript object {@code HTMLBodyElement}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlBody.class)
public class HTMLBodyElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
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
            createEventHandler(attributeName.substring(2), value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEventHandlerOnWindow() {
        return true;
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
        return node.getAttributeDirect("background");
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
        return getDomNodeOrDie().getAttributeDirect("link");
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
        return getDomNodeOrDie().getAttributeDirect("text");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int getClientWidth() {
        return super.getClientWidth() + 16;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction(IE)
    public TextRange createTextRange() {
        return super.createTextRange();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Object getOnload() {
        return super.getOnload();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnload(final Object onload) {
        super.setOnload(onload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnblur(final Object handler) {
        super.setOnblur(handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Object getOnblur() {
        return super.getOnblur();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnfocus(final Object handler) {
        super.setOnfocus(handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Object getOnfocus() {
        return super.getOnfocus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnerror(final Object handler) {
        super.setOnerror(handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Object getOnerror() {
        return super.getOnerror();
    }

    /**
     * Returns the {@code onbeforeunload} event handler for this element.
     * @return the {@code onbeforeunload} event handler for this element
     */
    @JsxGetter
    public Function getOnbeforeunload() {
        return getEventHandler(Event.TYPE_BEFORE_UNLOAD);
    }

    /**
     * Sets the {@code onbeforeunload} event handler for this element.
     * @param onbeforeunload the {@code onbeforeunload} event handler for this element
     */
    @JsxSetter
    public void setOnbeforeunload(final Object onbeforeunload) {
        setEventHandler(Event.TYPE_BEFORE_UNLOAD, onbeforeunload);
    }

    /**
     * Returns the {@code onhashchange} event handler for this element.
     * @return the {@code onhashchange} event handler for this element
     */
    @JsxGetter
    public Function getOnhashchange() {
        return getEventHandler(Event.TYPE_HASH_CHANGE);
    }

    /**
     * Sets the {@code onhashchange} event handler for this element.
     * @param onhashchange the {@code onhashchange} event handler for this element
     */
    @JsxSetter
    public void setOnhashchange(final Object onhashchange) {
        setEventHandler(Event.TYPE_HASH_CHANGE, onhashchange);
    }

    /**
     * Returns the {@code onlanguagechange} event handler for this element.
     * @return the {@code onlanguagechange} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnlanguagechange() {
        return getEventHandler(Event.TYPE_LANGUAGECHANGE);
    }

    /**
     * Sets the {@code onlanguagechange} event handler for this element.
     * @param onlanguagechange the {@code onlanguagechange} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnlanguagechange(final Object onlanguagechange) {
        setEventHandler(Event.TYPE_LANGUAGECHANGE, onlanguagechange);
    }

    /**
     * Returns the {@code onmessage} event handler for this element.
     * @return the {@code onmessage} event handler for this element
     */
    @JsxGetter
    public Function getOnmessage() {
        return getEventHandler(Event.TYPE_MESSAGE);
    }

    /**
     * Sets the {@code onmessage} event handler for this element.
     * @param onmessage the {@code onmessage} event handler for this element
     */
    @JsxSetter
    public void setOnmessage(final Object onmessage) {
        setEventHandler(Event.TYPE_MESSAGE, onmessage);
    }

    /**
     * Returns the {@code onoffline} event handler for this element.
     * @return the {@code onoffline} event handler for this element
     */
    @JsxGetter
    public Function getOnoffline() {
        return getEventHandler(Event.TYPE_OFFLINE);
    }

    /**
     * Sets the {@code onoffline} event handler for this element.
     * @param onoffline the {@code onoffline} event handler for this element
     */
    @JsxSetter
    public void setOnoffline(final Object onoffline) {
        setEventHandler(Event.TYPE_OFFLINE, onoffline);
    }

    /**
     * Returns the {@code ononline} event handler for this element.
     * @return the {@code ononline} event handler for this element
     */
    @JsxGetter
    public Function getOnonline() {
        return getEventHandler(Event.TYPE_ONLINE);
    }

    /**
     * Sets the {@code ononline} event handler for this element.
     * @param ononline the {@code ononline} event handler for this element
     */
    @JsxSetter
    public void setOnonline(final Object ononline) {
        setEventHandler(Event.TYPE_ONLINE, ononline);
    }

    /**
     * Returns the {@code onpagehide} event handler for this element.
     * @return the {@code onpagehide} event handler for this element
     */
    @JsxGetter
    public Function getOnpagehide() {
        return getEventHandler(Event.TYPE_PAGEHIDE);
    }

    /**
     * Sets the {@code onpagehide} event handler for this element.
     * @param onpagehide the {@code onpagehide} event handler for this element
     */
    @JsxSetter
    public void setOnpagehide(final Object onpagehide) {
        setEventHandler(Event.TYPE_PAGEHIDE, onpagehide);
    }

    /**
     * Returns the {@code onpageshow} event handler for this element.
     * @return the {@code onpageshow} event handler for this element
     */
    @JsxGetter
    public Function getOnpageshow() {
        return getEventHandler(Event.TYPE_PAGESHOW);
    }

    /**
     * Sets the {@code onpageshow} event handler for this element.
     * @param onpageshow the {@code onpageshow} event handler for this element
     */
    @JsxSetter
    public void setOnpageshow(final Object onpageshow) {
        setEventHandler(Event.TYPE_PAGESHOW, onpageshow);
    }

    /**
     * Returns the {@code onpopstate} event handler for this element.
     * @return the {@code onpopstate} event handler for this element
     */
    @JsxGetter
    public Function getOnpopstate() {
        return getEventHandler(Event.TYPE_POPSTATE);
    }

    /**
     * Sets the {@code onpopstate} event handler for this element.
     * @param onpopstate the {@code onpopstate} event handler for this element
     */
    @JsxSetter
    public void setOnpopstate(final Object onpopstate) {
        setEventHandler(Event.TYPE_POPSTATE, onpopstate);
    }

    /**
     * Returns the {@code onrejectionhandled} event handler for this element.
     * @return the {@code onrejectionhandled} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnrejectionhandled() {
        return getEventHandler(Event.TYPE_REJECTIONHANDLED);
    }

    /**
     * Sets the {@code onrejectionhandled} event handler for this element.
     * @param onrejectionhandled the {@code onrejectionhandled} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnrejectionhandled(final Object onrejectionhandled) {
        setEventHandler(Event.TYPE_REJECTIONHANDLED, onrejectionhandled);
    }

    /**
     * Returns the {@code onstorage} event handler for this element.
     * @return the {@code onstorage} event handler for this element
     */
    @JsxGetter
    public Function getOnstorage() {
        return getEventHandler(Event.TYPE_STORAGE);
    }

    /**
     * Sets the {@code onstorage} event handler for this element.
     * @param onstorage the {@code onstorage} event handler for this element
     */
    @JsxSetter
    public void setOnstorage(final Object onstorage) {
        setEventHandler(Event.TYPE_STORAGE, onstorage);
    }

    /**
     * Returns the {@code onunhandledrejection} event handler for this element.
     * @return the {@code onunhandledrejection} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnunhandledrejection() {
        return getEventHandler(Event.TYPE_UNHANDLEDREJECTION);
    }

    /**
     * Sets the {@code onunhandledrejection} event handler for this element.
     * @param onunhandledrejection the {@code onunhandledrejection} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnunhandledrejection(final Object onunhandledrejection) {
        setEventHandler(Event.TYPE_UNHANDLEDREJECTION, onunhandledrejection);
    }

    /**
     * Returns the {@code onunload} event handler for this element.
     * @return the {@code onunload} event handler for this element
     */
    @JsxGetter
    public Function getOnunload() {
        return getEventHandler(Event.TYPE_UNLOAD);
    }

    /**
     * Sets the {@code onunload} event handler for this element.
     * @param onunload the {@code onunload} event handler for this element
     */
    @JsxSetter
    public void setOnunload(final Object onunload) {
        setEventHandler(Event.TYPE_UNLOAD, onunload);
    }

    /**
     * Returns the {@code onafterprint} event handler for this element.
     * @return the {@code onafterprint} event handler for this element
     */
    @JsxGetter
    public Function getOnafterprint() {
        return getEventHandler(Event.TYPE_AFTERPRINT);
    }

    /**
     * Sets the {@code onafterprint} event handler for this element.
     * @param onafterprint the {@code onafterprint} event handler for this element
     */
    @JsxSetter
    public void setOnafterprint(final Object onafterprint) {
        setEventHandler(Event.TYPE_AFTERPRINT, onafterprint);
    }

    /**
     * Returns the {@code onbeforeprint} event handler for this element.
     * @return the {@code onbeforeprint} event handler for this element
     */
    @JsxGetter
    public Function getOnbeforeprint() {
        return getEventHandler(Event.TYPE_BEFOREPRINT);
    }

    /**
     * Sets the {@code onbeforeprint} event handler for this element.
     * @param onbeforeprint the {@code onbeforeprint} event handler for this element
     */
    @JsxSetter
    public void setOnbeforeprint(final Object onbeforeprint) {
        setEventHandler(Event.TYPE_BEFOREPRINT, onbeforeprint);
    }

    /**
     * Returns the {@code onmessageerror} event handler for this element.
     * @return the {@code onmessageerror} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnmessageerror() {
        return getEventHandler(Event.TYPE_ONMESSAGEERROR);
    }

    /**
     * Sets the {@code onmessageerror} event handler for this element.
     * @param onmessageerror the {@code onmessageerror} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnmessageerror(final Object onmessageerror) {
        setEventHandler(Event.TYPE_ONMESSAGEERROR, onmessageerror);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnresize() {
        return super.getOnresize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE, IE})
    public void setOnresize(final Object onresize) {
        super.setOnresize(onresize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOnscroll() {
        return super.getOnscroll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnscroll(final Object onresize) {
        super.setOnscroll(onresize);
    }
}
