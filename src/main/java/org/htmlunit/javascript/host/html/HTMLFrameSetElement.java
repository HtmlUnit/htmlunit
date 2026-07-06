/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.html.HtmlFrameSet;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.event.Event;

/**
 * The JavaScript object {@code HTMLFrameSetElement}.
 *
 * @author Bruce Chapman
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLFrameSetElement">MDN Documentation</a>
 */
@JsxClass(domClass = HtmlFrameSet.class)
public class HTMLFrameSetElement extends HTMLElement {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEventHandlerOnWindow() {
        return true;
    }

    /**
     * Sets the {@code rows} property.
     *
     * @param rows the {@code rows} attribute value
     */
    @JsxSetter
    public void setRows(final String rows) {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        if (htmlFrameSet != null) {
            htmlFrameSet.setAttribute("rows", rows);
        }
    }

    /**
     * Gets the {@code rows} property.
     *
     * @return the {@code rows} attribute value
     */
    @JsxGetter
    public String getRows() {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        return htmlFrameSet.getRowsAttribute();
    }

    /**
     * Sets the {@code cols} property.
     *
     * @param cols the {@code cols} attribute value
     */
    @JsxSetter
    public void setCols(final String cols) {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        if (htmlFrameSet != null) {
            htmlFrameSet.setAttribute("cols", cols);
        }
    }

    /**
     * Gets the {@code cols} property.
     *
     * @return the {@code cols} attribute value
     */
    @JsxGetter
    public String getCols() {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        return htmlFrameSet.getColsAttribute();
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
     * @param beforeunload the {@code onbeforeunload} event handler for this element
     */
    @JsxSetter
    public void setOnbeforeunload(final Object beforeunload) {
        setEventHandler(Event.TYPE_BEFORE_UNLOAD, beforeunload);
    }

    /**
     * Returns the {@code ongamepadconnected} event handler for this element.
     * @return the {@code ongamepadconnected} event handler for this element
     */
    @JsxGetter
    public Function getOngamepadconnected() {
        return getEventHandler(Event.TYPE_GAMEPAD_CONNECTED);
    }

    /**
     * Sets the {@code ongamepadconnected} event handler for this element.
     * @param gamepadconnected the {@code ongamepadconnected} event handler for this element
     */
    @JsxSetter
    public void setOngamepadconnected(final Object gamepadconnected) {
        setEventHandler(Event.TYPE_GAMEPAD_CONNECTED, gamepadconnected);
    }

    /**
     * Returns the {@code ongamepaddisconnected} event handler for this element.
     * @return the {@code ongamepaddisconnected} event handler for this element
     */
    @JsxGetter
    public Function getOngamepaddisconnected() {
        return getEventHandler(Event.TYPE_GAMEPAD_DISCONNECTED);
    }

    /**
     * Sets the {@code ongamepaddisconnected} event handler for this element.
     * @param gamepaddisconnected the {@code ongamepaddisconnected} event handler for this element
     */
    @JsxSetter
    public void setOngamepaddisconnected(final Object gamepaddisconnected) {
        setEventHandler(Event.TYPE_GAMEPAD_DISCONNECTED, gamepaddisconnected);
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
     * @param hashchange the {@code onhashchange} event handler for this element
     */
    @JsxSetter
    public void setOnhashchange(final Object hashchange) {
        setEventHandler(Event.TYPE_HASH_CHANGE, hashchange);
    }

    /**
     * Returns the {@code onlanguagechange} event handler for this element.
     * @return the {@code onlanguagechange} event handler for this element
     */
    @JsxGetter
    public Function getOnlanguagechange() {
        return getEventHandler(Event.TYPE_LANGUAGECHANGE);
    }

    /**
     * Sets the {@code onlanguagechange} event handler for this element.
     * @param languagechange the {@code onlanguagechange} event handler for this element
     */
    @JsxSetter
    public void setOnlanguagechange(final Object languagechange) {
        setEventHandler(Event.TYPE_LANGUAGECHANGE, languagechange);
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
     * @param message the {@code onmessage} event handler for this element
     */
    @JsxSetter
    public void setOnmessage(final Object message) {
        setEventHandler(Event.TYPE_MESSAGE, message);
    }

    /**
     * Returns the {@code onmessageerror} event handler for this element.
     * @return the {@code onmessageerror} event handler for this element
     */
    @JsxGetter
    public Function getOnmessageerror() {
        return getEventHandler(Event.TYPE_ONMESSAGEERROR);
    }

    /**
     * Sets the {@code onmessageerror} event handler for this element.
     * @param onmessageerror the {@code onmessageerror} event handler for this element
     */
    @JsxSetter
    public void setOnmessageerror(final Object onmessageerror) {
        setEventHandler(Event.TYPE_ONMESSAGEERROR, onmessageerror);
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
     * @param offline the {@code onoffline} event handler for this element
     */
    @JsxSetter
    public void setOnoffline(final Object offline) {
        setEventHandler(Event.TYPE_OFFLINE, offline);
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
     * @param online the {@code ononline} event handler for this element
     */
    @JsxSetter
    public void setOnonline(final Object online) {
        setEventHandler(Event.TYPE_ONLINE, online);
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
     * @param pagehide the {@code onpagehide} event handler for this element
     */
    @JsxSetter
    public void setOnpagehide(final Object pagehide) {
        setEventHandler(Event.TYPE_PAGEHIDE, pagehide);
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
     * @param pageshow the {@code onpageshow} event handler for this element
     */
    @JsxSetter
    public void setOnpageshow(final Object pageshow) {
        setEventHandler(Event.TYPE_PAGESHOW, pageshow);
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
     * @param popstate the {@code onpopstate} event handler for this element
     */
    @JsxSetter
    public void setOnpopstate(final Object popstate) {
        setEventHandler(Event.TYPE_POPSTATE, popstate);
    }

    /**
     * Returns the {@code onrejectionhandled} event handler for this element.
     * @return the {@code onrejectionhandled} event handler for this element
     */
    @JsxGetter
    public Function getOnrejectionhandled() {
        return getEventHandler(Event.TYPE_REJECTIONHANDLED);
    }

    /**
     * Sets the {@code onrejectionhandled} event handler for this element.
     * @param rejectionhandled the {@code onrejectionhandled} event handler for this element
     */
    @JsxSetter
    public void setOnrejectionhandled(final Object rejectionhandled) {
        setEventHandler(Event.TYPE_REJECTIONHANDLED, rejectionhandled);
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
     * @param storage the {@code onstorage} event handler for this element
     */
    @JsxSetter
    public void setOnstorage(final Object storage) {
        setEventHandler(Event.TYPE_STORAGE, storage);
    }

    /**
     * Returns the {@code onunhandledrejection} event handler for this element.
     * @return the {@code onunhandledrejection} event handler for this element
     */
    @JsxGetter
    public Function getOnunhandledrejection() {
        return getEventHandler(Event.TYPE_UNHANDLEDREJECTION);
    }

    /**
     * Sets the {@code onunhandledrejection} event handler for this element.
     * @param unhandledrejection the {@code onunhandledrejection} event handler for this element
     */
    @JsxSetter
    public void setOnunhandledrejection(final Object unhandledrejection) {
        setEventHandler(Event.TYPE_UNHANDLEDREJECTION, unhandledrejection);
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
     * @param unload the {@code onunload} event handler for this element
     */
    @JsxSetter
    public void setOnunload(final Object unload) {
        setEventHandler(Event.TYPE_UNLOAD, unload);
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
     * @param afterprint the {@code onafterprint} event handler for this element
     */
    @JsxSetter
    public void setOnafterprint(final Object afterprint) {
        setEventHandler(Event.TYPE_AFTERPRINT, afterprint);
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
     * @param beforeprint the {@code onbeforeprint} event handler for this element
     */
    @JsxSetter
    public void setOnbeforeprint(final Object beforeprint) {
        setEventHandler(Event.TYPE_BEFOREPRINT, beforeprint);
    }
}
