/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF68;

import java.util.HashMap;
import java.util.Map;

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
 */
@JsxClass
public class XMLHttpRequestEventTarget extends EventTarget {

    private final Map<String, Function> eventMapping_ = new HashMap<>();

    /**
     * Creates an instance.
     */
    public XMLHttpRequestEventTarget() {
    }

    /**
     * @return the constructed object
     */
    @JsxConstructor({CHROME, EDGE, FF, FF68})
    public static XMLHttpRequestEventTarget ctor() {
        throw ScriptRuntime.typeError("Illegal constructor.");
    }

    public Function getFunctionForEvent(final String event) {
        return eventMapping_.get(event);
    }

    /**
     * Returns the event handler that fires on load.
     * @return the event handler that fires on load
     */
    @JsxGetter
    public Function getOnload() {
        return getFunctionForEvent(Event.TYPE_LOAD);
    }

    /**
     * Sets the event handler that fires on load.
     * @param loadHandler the event handler that fires on load
     */
    @JsxSetter
    public void setOnload(final Function loadHandler) {
        eventMapping_.put(Event.TYPE_LOAD, loadHandler);
    }

    /**
     * Returns the event handler that fires on error.
     * @return the event handler that fires on error
     */
    @JsxGetter
    public Function getOnerror() {
        return getFunctionForEvent(Event.TYPE_ERROR);
    }

    /**
     * Sets the event handler that fires on error.
     * @param errorHandler the event handler that fires on error
     */
    @JsxSetter
    public void setOnerror(final Function errorHandler) {
        eventMapping_.put(Event.TYPE_ERROR, errorHandler);
    }

    /**
     * Returns the event handler that fires on load start.
     * @return the event handler that fires on load start
     */
    @JsxGetter
    public Function getOnloadstart() {
        return getFunctionForEvent(Event.TYPE_LOAD_START);
    }

    /**
     * Sets the event handler that fires on load.
     * @param loadHandler the event handler that fires on load
     */
    @JsxSetter
    public void setOnloadstart(final Function loadHandler) {
        eventMapping_.put(Event.TYPE_LOAD_START, loadHandler);
    }

    /**
     * Returns the event handler that fires on load end.
     * @return the event handler that fires on load end
     */
    @JsxGetter
    public Function getOnloadend() {
        return getFunctionForEvent(Event.TYPE_LOAD_END);
    }

    /**
     * Sets the event handler that fires on load end.
     * @param loadHandler the event handler that fires on loadend
     */
    @JsxSetter
    public void setOnloadend(final Function loadHandler) {
        eventMapping_.put(Event.TYPE_LOAD_END, loadHandler);
    }

    /**
     * Returns the event handler that fires on progress.
     * @return the event handler that fires on progress
     */
    @JsxGetter
    public Function getOnprogress() {
        return getFunctionForEvent(Event.TYPE_PROGRESS);
    }

    /**
     * Sets the event handler that fires on progress.
     * @param loadHandler the event handler that fires on progress
     */
    @JsxSetter
    public void setOnprogress(final Function loadHandler) {
        eventMapping_.put(Event.TYPE_PROGRESS, loadHandler);
    }

    /**
     * Returns the event handler that fires on timeout.
     * @return the event handler that fires on timeout
     */
    @JsxGetter
    public Function getOntimeout() {
        return getFunctionForEvent(Event.TYPE_TIMEOUT);
    }

    /**
     * Sets the event handler that fires on timeout.
     * @param loadHandler the event handler that fires on timeout
     */
    @JsxSetter
    public void setOntimeout(final Function loadHandler) {
        eventMapping_.put(Event.TYPE_TIMEOUT, loadHandler);
    }

    /**
     * Returns the event handler that fires on timeout.
     * @return the event handler that fires on timeout
     */
    @JsxGetter
    public Function getOnreadystatechange() {
        return getFunctionForEvent(Event.TYPE_READY_STATE_CHANGE);
    }

    /**
     * Sets the event handler that fires on timeout.
     * @param loadHandler the event handler that fires on timeout
     */
    @JsxSetter
    public void setOnreadystatechange(final Function loadHandler) {
        eventMapping_.put(Event.TYPE_READY_STATE_CHANGE, loadHandler);
    }

    /**
     * Returns the event handler that fires on abort.
     * @return the event handler that fires on abort
     */
    @JsxGetter
    public Function getOnabort() {
        return getFunctionForEvent(Event.TYPE_ABORT);
    }

    /**
     * Sets the event handler that fires on abort.
     * @param loadHandler the event handler that fires on abort
     */
    @JsxSetter
    public void setOnabort(final Function loadHandler) {
        eventMapping_.put(Event.TYPE_ABORT, loadHandler);
    }
}
