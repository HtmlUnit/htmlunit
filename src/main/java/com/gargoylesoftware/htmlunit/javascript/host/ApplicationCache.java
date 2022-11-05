/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

/**
 * <p>A collection of offline resources as defined in the HTML5 spec.
 * Intended to support offline web applications.</p>
 *
 * @author Daniel Gredler
 * @author Frank Danek
 * @author Ronald Brill
 * @author Jake Cobb
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/offline.html#application-cache-api">
 * HTML5 spec</a>
 * @see <a href="https://developer.mozilla.org/en/offline_resources_in_firefox">Offline Resources in Firefox</a>
 * @see <a href="https://developer.mozilla.org/en/nsIDOMOfflineResourceList">Mozilla Documentation</a>
 */
@JsxClass(IE)
@JsxClass(className = "OfflineResourceList", value = {FF, FF_ESR})
public class ApplicationCache extends EventTarget {

    /** The object isn't associated with an application cache. */
    @JsxConstant
    public static final short UNCACHED = 0;
    /** The application cache is not in the process of being updated. */
    @JsxConstant
    public static final short IDLE = 1;
    /** The application cache manifest is being fetched and checked for updates. */
    @JsxConstant
    public static final short CHECKING = 2;
    /** Resources are being downloaded to be added to the cache. */
    @JsxConstant
    public static final short DOWNLOADING = 3;
    /** There is a new version of the application cache available. */
    @JsxConstant
    public static final short UPDATEREADY = 4;
    /** The application cache group is now obsolete. */
    @JsxConstant
    public static final short OBSOLETE = 5;

    /**
     * The constructor.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public ApplicationCache() {
    }

    /**
     * Returns the event listener to be called when fetching the application cache manifest and checking for updates.
     * @return the event listener to be called when fetching the application cache manifest and checking for updates
     */
    @JsxGetter
    public Object getOnchecking() {
        return getHandlerForJavaScript("checking");
    }

    /**
     * Sets the event listener to be called when fetching the application cache manifest and checking for updates.
     * @param o the event listener to be called when fetching the application cache manifest and checking for updates
     */
    @JsxSetter
    public void setOnchecking(final Object o) {
        setHandlerForJavaScript("checking", o);
    }

    /**
     * Returns the event listener to be called when an error occurs during the caching process.
     * @return the event listener to be called when an error occurs during the caching process
     */
    @JsxGetter
    public Object getOnerror() {
        return getHandlerForJavaScript(Event.TYPE_ERROR);
    }

    /**
     * Sets the event listener to be called when an error occurs during the caching process.
     * @param o the event listener to be called when an error occurs during the caching process
     */
    @JsxSetter
    public void setOnerror(final Object o) {
        setHandlerForJavaScript(Event.TYPE_ERROR, o);
    }

    /**
     * Returns the event listener to be called when there is no update to download.
     * @return the event listener to be called when there is no update to download
     */
    @JsxGetter
    public Object getOnnoupdate() {
        return getHandlerForJavaScript("update");
    }

    /**
     * Sets the event listener to be called when there is no update to download.
     * @param o the event listener to be called when there is no update to download
     */
    @JsxSetter
    public void setOnnoupdate(final Object o) {
        setHandlerForJavaScript("update", o);
    }

    /**
     * Returns the event listener to be called when resources are being downloaded into the cache.
     * @return the event listener to be called when resources are being downloaded into the cache
     */
    @JsxGetter
    public Object getOndownloading() {
        return getHandlerForJavaScript("downloading");
    }

    /**
     * Sets the event listener to be called when resources are being downloaded into the cache.
     * @param o the event listener to be called when resources are being downloaded into the cache
     */
    @JsxSetter
    public void setOndownloading(final Object o) {
        setHandlerForJavaScript("downloading", o);
    }

    /**
     * Returns the event listener to be called periodically throughout the download process.
     * @return the event listener to be called periodically throughout the download process
     */
    @JsxGetter
    public Object getOnprogress() {
        return getHandlerForJavaScript(Event.TYPE_PROGRESS);
    }

    /**
     * Sets the event listener to be called periodically throughout the download process.
     * @param o the event listener to be called periodically throughout the download process
     */
    @JsxSetter
    public void setOnprogress(final Object o) {
        setHandlerForJavaScript(Event.TYPE_PROGRESS, o);
    }

    /**
     * Returns the event listener to be called when a resource update is ready.
     * @return the event listener to be called when a resource update is ready
     */
    @JsxGetter
    public Object getOnupdateready() {
        return getHandlerForJavaScript("updateready");
    }

    /**
     * Sets the event listener to be called when a resource update is ready.
     * @param o the event listener to be called when a resource update is ready
     */
    @JsxSetter
    public void setOnupdateready(final Object o) {
        setHandlerForJavaScript("updateready", o);
    }

    /**
     * Returns the event listener to be called when caching is complete.
     * @return the event listener to be called when caching is complete
     */
    @JsxGetter
    public Object getOncached() {
        return getHandlerForJavaScript("cached");
    }

    /**
     * Sets the event listener to be called when caching is complete.
     * @param o the event listener to be called when caching is complete
     */
    @JsxSetter
    public void setOncached(final Object o) {
        setHandlerForJavaScript("cached", o);
    }

    /**
     * Dispatches an event into the event system (standards-conformant browsers only). See
     * <a href="https://developer.mozilla.org/en-US/docs/DOM/element.dispatchEvent">the Gecko
     * DOM reference</a> for more information.
     *
     * @param event the event to be dispatched
     * @return {@code false} if at least one of the event handlers which handled the event
     *         called <code>preventDefault</code>; {@code true} otherwise
     */
    @Override
    @JsxFunction
    public boolean dispatchEvent(final Event event) {
        event.setTarget(this);
        final ScriptResult result = fireEvent(event);
        return !event.isAborted(result);
    }

    private Object getHandlerForJavaScript(final String eventName) {
        return getEventListenersContainer().getEventHandler(eventName);
    }

    private void setHandlerForJavaScript(final String eventName, final Object handler) {
        getEventListenersContainer().setEventHandler(eventName, handler);
    }

    /**
     * Returns the status of the application cache.
     * @return the status of the application cache
     */
    @JsxGetter
    public short getStatus() {
        return UNCACHED;
    }

    /**
     * Returns the number of entries in the dynamically managed offline resource list.
     * @return the number of entries in the dynamically managed offline resource list
     */
    @JsxGetter
    public int getLength() {
        return 0; // TODO: implement
    }

    /**
     * Adds an item to the dynamically managed entries. The resource will be fetched and added to the application cache.
     * @param uri the URI of the item to add to the dynamically managed entries
     */
    @JsxFunction
    public void add(final String uri) {
        // TODO: implement
    }

    /**
     * Returns {@code true} if the specified URI represents a resource that's in the application cache's list.
     * @param uri the URI to check
     * @return {@code true} if the specified URI represents a resource that's in the application cache's list
     */
    @JsxFunction
    public boolean hasItem(final String uri) {
        return false; // TODO: implement
    }

    /**
     * Returns the URI of the item at the specific offset into the list of cached resources.
     * @param index the index of the cached item whose URI should be returned
     * @return the URI of the item at the specific offset into the list of cached resources
     */
    @JsxFunction
    public String item(final int index) {
        return null; // TODO: implement
    }

    /**
     * Removes an item from the list of dynamically managed entries. If this was the last reference
     * to the given URI in the application cache, the cache entry is removed.
     * @param uri the URI to remove
     */
    @JsxFunction
    public void remove(final String uri) {
        // TODO: implement
    }

    /**
     * Swaps in the newest version of the application cache.
     */
    @JsxFunction
    public void swapCache() {
        // TODO: implement
    }

    /**
     * Begins the application cache update process.
     */
    @JsxFunction
    public void update() {
        // TODO: implement
    }
}
