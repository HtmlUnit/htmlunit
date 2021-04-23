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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;

/**
 * A JavaScript object for {@code XMLHttpRequestEventTarget}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Thorsten Wendelmuth
 * @author Atsushi Nakagawa
 */
@JsxClass
public class XMLHttpRequestEventTarget extends EventTarget {

    /**
     * Creates an instance.
     */
    public XMLHttpRequestEventTarget() {
    }

    /**
     * @return the constructed object
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public static XMLHttpRequestEventTarget ctor() {
        throw ScriptRuntime.typeError("Illegal constructor.");
    }

    /**
     * Returns the event handler that fires on load.
     * @return the event handler that fires on load
     */
    @JsxGetter
    public Function getOnload() {
        return getEventHandler(Event.TYPE_LOAD);
    }

    /**
     * Sets the event handler that fires on load.
     * @param loadHandler the event handler that fires on load
     */
    @JsxSetter
    public void setOnload(final Function loadHandler) {
        setEventHandler(Event.TYPE_LOAD, loadHandler);
    }

    /**
     * Returns the event handler that fires on error.
     * @return the event handler that fires on error
     */
    @JsxGetter
    public Function getOnerror() {
        return getEventHandler(Event.TYPE_ERROR);
    }

    /**
     * Sets the event handler that fires on error.
     * @param errorHandler the event handler that fires on error
     */
    @JsxSetter
    public void setOnerror(final Function errorHandler) {
        setEventHandler(Event.TYPE_ERROR, errorHandler);
    }

    /**
     * Returns the event handler that fires on load start.
     * @return the event handler that fires on load start
     */
    @JsxGetter
    public Function getOnloadstart() {
        return getEventHandler(Event.TYPE_LOAD_START);
    }

    /**
     * Sets the event handler that fires on load start.
     * @param loadstartHandler the event handler that fires on load start
     */
    @JsxSetter
    public void setOnloadstart(final Function loadstartHandler) {
        setEventHandler(Event.TYPE_LOAD_START, loadstartHandler);
    }

    /**
     * Returns the event handler that fires on load end.
     * @return the event handler that fires on load end
     */
    @JsxGetter
    public Function getOnloadend() {
        return getEventHandler(Event.TYPE_LOAD_END);
    }

    /**
     * Sets the event handler that fires on load end.
     * @param loadendHandler the event handler that fires on load end
     */
    @JsxSetter
    public void setOnloadend(final Function loadendHandler) {
        setEventHandler(Event.TYPE_LOAD_END, loadendHandler);
    }

    /**
     * Returns the event handler that fires on progress.
     * @return the event handler that fires on progress
     */
    @JsxGetter
    public Function getOnprogress() {
        return getEventHandler(Event.TYPE_PROGRESS);
    }

    /**
     * Sets the event handler that fires on progress.
     * @param progressHandler the event handler that fires on progress
     */
    @JsxSetter
    public void setOnprogress(final Function progressHandler) {
        setEventHandler(Event.TYPE_PROGRESS, progressHandler);
    }

    /**
     * Returns the event handler that fires on timeout.
     * @return the event handler that fires on timeout
     */
    @JsxGetter
    public Function getOntimeout() {
        return getEventHandler(Event.TYPE_TIMEOUT);
    }

    /**
     * Sets the event handler that fires on timeout.
     * @param timeoutHandler the event handler that fires on timeout
     */
    @JsxSetter
    public void setOntimeout(final Function timeoutHandler) {
        setEventHandler(Event.TYPE_TIMEOUT, timeoutHandler);
    }

    /**
     * Returns the event handler that fires on ready state change.
     * @return the event handler that fires on ready state change
     */
    public Function getOnreadystatechange() {
        return getEventHandler(Event.TYPE_READY_STATE_CHANGE);
    }

    /**
     * Sets the event handler that fires on ready state change.
     * @param readyStateChangeHandler the event handler that fires on ready state change
     */
    public void setOnreadystatechange(final Function readyStateChangeHandler) {
        setEventHandler(Event.TYPE_READY_STATE_CHANGE, readyStateChangeHandler);
    }

    /**
     * Returns the event handler that fires on abort.
     * @return the event handler that fires on abort
     */
    @JsxGetter
    public Function getOnabort() {
        return getEventHandler(Event.TYPE_ABORT);
    }

    /**
     * Sets the event handler that fires on abort.
     * @param abortHandler the event handler that fires on abort
     */
    @JsxSetter
    public void setOnabort(final Function abortHandler) {
        setEventHandler(Event.TYPE_ABORT, abortHandler);
    }
}
