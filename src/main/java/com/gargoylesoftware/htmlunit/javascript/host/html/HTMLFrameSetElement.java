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

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.html.HtmlFrameSet;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;

/**
 * The JavaScript object {@code HTMLFrameSetElement}.
 *
 * @author Bruce Chapman
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlFrameSet.class)
public class HTMLFrameSetElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLFrameSetElement() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEventHandlerOnWindow() {
        return true;
    }

    /**
     * Sets the rows property.
     *
     * @param rows the rows attribute value
     */
    @JsxSetter
    public void setRows(final String rows) {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        if (htmlFrameSet != null) {
            htmlFrameSet.setAttribute("rows", rows);
        }
    }

    /**
     * Gets the rows property.
     *
     * @return the rows attribute value
     */

    @JsxGetter
    public String getRows() {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        return htmlFrameSet.getRowsAttribute();
    }

    /**
     * Sets the cols property.
     *
     * @param cols the cols attribute value
     */
    @JsxSetter
    public void setCols(final String cols) {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        if (htmlFrameSet != null) {
            htmlFrameSet.setAttribute("cols", cols);
        }
    }

    /**
     * Gets the cols property.
     *
     * @return the cols attribute value
     */
    @JsxGetter
    public String getCols() {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        return htmlFrameSet.getColsAttribute();
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
     * Overwritten to throw an exception.
     * @param value the new value for replacing this node
     */
    @JsxSetter
    @Override
    public void setOuterHTML(final Object value) {
        throw Context.reportRuntimeError("outerHTML is read-only for tag 'frameset'");
    }

    /**
     * Returns the {@code onbeforeunload} event handler.
     * @return the {@code onbeforeunload} event handler
     */
    @JsxGetter
    public Function getOnbeforeunload() {
        return getEventHandler(Event.TYPE_BEFORE_UNLOAD);
    }

    /**
     * Sets the {@code onbeforeunload} event handler.
     * @param beforeunload the {@code onbeforeunload} event handler
     */
    @JsxSetter
    public void setOnbeforeunload(final Object beforeunload) {
        setEventHandler(Event.TYPE_BEFORE_UNLOAD, beforeunload);
    }

    /**
     * Returns the {@code onhashchange} event handler.
     * @return the {@code onhashchange} event handler
     */
    @JsxGetter
    public Function getOnhashchange() {
        return getEventHandler(Event.TYPE_HASH_CHANGE);
    }

    /**
     * Sets the {@code onhashchange} event handler.
     * @param hashchange the {@code onhashchange} event handler
     */
    @JsxSetter
    public void setOnhashchange(final Object hashchange) {
        setEventHandler(Event.TYPE_HASH_CHANGE, hashchange);
    }

    /**
     * Returns the {@code onlanguagechange} event handler.
     * @return the {@code onlanguagechange} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnlanguagechange() {
        return getEventHandler(Event.TYPE_LANGUAGECHANGE);
    }

    /**
     * Sets the {@code onlanguagechange} event handler.
     * @param languagechange the {@code onlanguagechange} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnlanguagechange(final Object languagechange) {
        setEventHandler(Event.TYPE_LANGUAGECHANGE, languagechange);
    }

    /**
     * Returns the {@code onmessage} event handler.
     * @return the {@code onmessage} event handler
     */
    @JsxGetter
    public Function getOnmessage() {
        return getEventHandler(Event.TYPE_MESSAGE);
    }

    /**
     * Sets the {@code onmessage} event handler.
     * @param message the {@code onmessage} event handler
     */
    @JsxSetter
    public void setOnmessage(final Object message) {
        setEventHandler(Event.TYPE_MESSAGE, message);
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
     * Returns the {@code onoffline} event handler.
     * @return the {@code onoffline} event handler
     */
    @JsxGetter
    public Function getOnoffline() {
        return getEventHandler(Event.TYPE_OFFLINE);
    }

    /**
     * Sets the {@code onoffline} event handler.
     * @param offline the {@code onoffline} event handler
     */
    @JsxSetter
    public void setOnoffline(final Object offline) {
        setEventHandler(Event.TYPE_OFFLINE, offline);
    }

    /**
     * Returns the {@code ononline} event handler.
     * @return the {@code ononline} event handler
     */
    @JsxGetter
    public Function getOnonline() {
        return getEventHandler(Event.TYPE_ONLINE);
    }

    /**
     * Sets the {@code ononline} event handler.
     * @param online the {@code ononline} event handler
     */
    @JsxSetter
    public void setOnonline(final Object online) {
        setEventHandler(Event.TYPE_ONLINE, online);
    }

    /**
     * Returns the {@code onpagehide} event handler.
     * @return the {@code onpagehide} event handler
     */
    @JsxGetter
    public Function getOnpagehide() {
        return getEventHandler(Event.TYPE_PAGEHIDE);
    }

    /**
     * Sets the {@code onpagehide} event handler.
     * @param pagehide the {@code onpagehide} event handler
     */
    @JsxSetter
    public void setOnpagehide(final Object pagehide) {
        setEventHandler(Event.TYPE_PAGEHIDE, pagehide);
    }

    /**
     * Returns the {@code onpageshow} event handler.
     * @return the {@code onpageshow} event handler
     */
    @JsxGetter
    public Function getOnpageshow() {
        return getEventHandler(Event.TYPE_PAGESHOW);
    }

    /**
     * Sets the {@code onpageshow} event handler.
     * @param pageshow the {@code onpageshow} event handler
     */
    @JsxSetter
    public void setOnpageshow(final Object pageshow) {
        setEventHandler(Event.TYPE_PAGESHOW, pageshow);
    }

    /**
     * Returns the {@code onpopstate} event handler.
     * @return the {@code onpopstate} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnpopstate() {
        return getEventHandler(Event.TYPE_POPSTATE);
    }

    /**
     * Sets the {@code onpopstate} event handler.
     * @param popstate the {@code onpopstate} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnpopstate(final Object popstate) {
        setEventHandler(Event.TYPE_POPSTATE, popstate);
    }

    /**
     * Returns the {@code onrejectionhandled} event handler.
     * @return the {@code onrejectionhandled} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnrejectionhandled() {
        return getEventHandler(Event.TYPE_REJECTIONHANDLED);
    }

    /**
     * Sets the {@code onrejectionhandled} event handler.
     * @param rejectionhandled the {@code onrejectionhandled} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnrejectionhandled(final Object rejectionhandled) {
        setEventHandler(Event.TYPE_REJECTIONHANDLED, rejectionhandled);
    }

    /**
     * Returns the {@code onstorage} event handler.
     * @return the {@code onstorage} event handler
     */
    @JsxGetter
    public Function getOnstorage() {
        return getEventHandler(Event.TYPE_STORAGE);
    }

    /**
     * Sets the {@code onstorage} event handler.
     * @param storage the {@code onstorage} event handler
     */
    @JsxSetter
    public void setOnstorage(final Object storage) {
        setEventHandler(Event.TYPE_STORAGE, storage);
    }

    /**
     * Returns the {@code onunhandledrejection} event handler.
     * @return the {@code onunhandledrejection} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnunhandledrejection() {
        return getEventHandler(Event.TYPE_UNHANDLEDREJECTION);
    }

    /**
     * Sets the {@code onunhandledrejection} event handler.
     * @param unhandledrejection the {@code onunhandledrejection} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnunhandledrejection(final Object unhandledrejection) {
        setEventHandler(Event.TYPE_UNHANDLEDREJECTION, unhandledrejection);
    }

    /**
     * Returns the {@code onunload} event handler.
     * @return the {@code onunload} event handler
     */
    @JsxGetter
    public Function getOnunload() {
        return getEventHandler(Event.TYPE_UNLOAD);
    }

    /**
     * Sets the {@code onunload} event handler.
     * @param unload the {@code onunload} event handler
     */
    @JsxSetter
    public void setOnunload(final Object unload) {
        setEventHandler(Event.TYPE_UNLOAD, unload);
    }

    /**
     * Returns the {@code onafterprint} event handler.
     * @return the {@code onafterprint} event handler
     */
    @JsxGetter
    public Function getOnafterprint() {
        return getEventHandler(Event.TYPE_AFTERPRINT);
    }

    /**
     * Sets the {@code onafterprint} event handler.
     * @param afterprint the {@code onafterprint} event handler
     */
    @JsxSetter
    public void setOnafterprint(final Object afterprint) {
        setEventHandler(Event.TYPE_AFTERPRINT, afterprint);
    }

    /**
     * Returns the {@code onbeforeprint} event handler.
     * @return the {@code onbeforeprint} event handler
     */
    @JsxGetter
    public Function getOnbeforeprint() {
        return getEventHandler(Event.TYPE_BEFOREPRINT);
    }

    /**
     * Sets the {@code onbeforeprint} event handler.
     * @param beforeprint the {@code onbeforeprint} event handler
     */
    @JsxSetter
    public void setOnbeforeprint(final Object beforeprint) {
        setEventHandler(Event.TYPE_BEFOREPRINT, beforeprint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(IE)
    public Function getOnresize() {
        return super.getOnresize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(IE)
    public void setOnresize(final Object resize) {
        super.setOnresize(resize);
    }

}
