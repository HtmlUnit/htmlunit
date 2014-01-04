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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * <p>A collection of offline resources as defined in the
 * <a href="http://www.w3.org/TR/2008/WD-html5-20080122/#appcache">HTML5 spec</a>.
 * Intended to support offline web applications.</p>
 *
 * <p><b>NOTE:</b> This class is essentially a skeleton implementation providing minimal
 * compatibility while we wait for the HTML5 dust to settle. The first real browser we should
 * worry about supporting with this implementation will probably be either Firefox 3.5
 * or Firefox 4.</p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @see <a href="https://developer.mozilla.org/en/offline_resources_in_firefox">Offline Resources in Firefox</a>
 * @see <a href="https://developer.mozilla.org/en/nsIDOMOfflineResourceList">Mozilla Documentation</a>
 */
@JsxClass(browsers = @WebBrowser(FF))
public class ApplicationCache extends SimpleScriptable {

    /** The object isn't associated with an application cache. */
    public static final short STATUS_UNCACHED = 0;
    /** The application cache is not in the process of being updated. */
    public static final short STATUS_IDLE = 1;
    /** The application cache manifest is being fetched and checked for updates. */
    public static final short STATUS_CHECKING = 2;
    /** Resources are being downloaded to be added to the cache. */
    public static final short STATUS_DOWNLOADING = 3;
    /** There is a new version of the application cache available. */
    public static final short STATUS_UPDATEREADY = 4;
    /** The application cache group is now obsolete. */
    public static final short STATUS_OBSOLETE = 5;

    private short status_ = STATUS_UNCACHED;
    private Object onchecking_;
    private Object onerror_;
    private Object onnoupdate_;
    private Object ondownloading_;
    private Object onprogress_;
    private Object onupdateready_;
    private Object oncached_;

    /**
     * Returns the event listener to be called when fetching the application cache manifest and checking for updates.
     * @return the event listener to be called when fetching the application cache manifest and checking for updates
     */
    @JsxGetter
    public Object getOnchecking() {
        return onchecking_;
    }

    /**
     * Sets the event listener to be called when fetching the application cache manifest and checking for updates.
     * @param o the event listener to be called when fetching the application cache manifest and checking for updates
     */
    @JsxSetter
    public void setOnchecking(final Object o) {
        onchecking_ = o;
    }

    /**
     * Returns the event listener to be called when an error occurs during the caching process.
     * @return the event listener to be called when an error occurs during the caching process
     */
    @JsxGetter
    public Object getOnerror() {
        return onerror_;
    }

    /**
     * Sets the event listener to be called when an error occurs during the caching process.
     * @param o the event listener to be called when an error occurs during the caching process
     */
    @JsxSetter
    public void setOnerror(final Object o) {
        onerror_ = o;
    }

    /**
     * Returns the event listener to be called when there is no update to download.
     * @return the event listener to be called when there is no update to download
     */
    @JsxGetter
    public Object getOnnoupdate() {
        return onnoupdate_;
    }

    /**
     * Sets the event listener to be called when there is no update to download.
     * @param o the event listener to be called when there is no update to download
     */
    @JsxSetter
    public void setOnnoupdate(final Object o) {
        onnoupdate_ = o;
    }

    /**
     * Returns the event listener to be called when resources are being downloaded into the cache.
     * @return the event listener to be called when resources are being downloaded into the cache
     */
    @JsxGetter
    public Object getOndownloading() {
        return ondownloading_;
    }

    /**
     * Sets the event listener to be called when resources are being downloaded into the cache.
     * @param o the event listener to be called when resources are being downloaded into the cache
     */
    @JsxSetter
    public void setOndownloading(final Object o) {
        ondownloading_ = o;
    }

    /**
     * Returns the event listener to be called periodically throughout the download process.
     * @return the event listener to be called periodically throughout the download process
     */
    @JsxGetter
    public Object getOnprogress() {
        return onprogress_;
    }

    /**
     * Sets the event listener to be called periodically throughout the download process.
     * @param o the event listener to be called periodically throughout the download process
     */
    @JsxSetter
    public void setOnprogress(final Object o) {
        onprogress_ = o;
    }

    /**
     * Returns the event listener to be called when a resource update is ready.
     * @return the event listener to be called when a resource update is ready
     */
    @JsxGetter
    public Object getOnupdateready() {
        return onupdateready_;
    }

    /**
     * Sets the event listener to be called when a resource update is ready.
     * @param o the event listener to be called when a resource update is ready
     */
    @JsxSetter
    public void setOnupdateready(final Object o) {
        onupdateready_ = o;
    }

    /**
     * Returns the event listener to be called when caching is complete.
     * @return the event listener to be called when caching is complete
     */
    @JsxGetter
    public Object getOncached() {
        return oncached_;
    }

    /**
     * Sets the event listener to be called when caching is complete.
     * @param o the event listener to be called when caching is complete
     */
    @JsxSetter
    public void setOncached(final Object o) {
        oncached_ = o;
    }

    /**
     * Returns the status of the application cache.
     * @return the status of the application cache
     */
    @JsxGetter
    public short getStatus() {
        return status_;
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
     * Returns <tt>true</tt> if the specified URI represents a resource that's in the application cache's list.
     * @param uri the URI to check
     * @return <tt>true</tt> if the specified URI represents a resource that's in the application cache's list
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
