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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.PopStateEvent;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Representation of the navigation history of a single window.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Adam Afeltowicz
 * @author Ronald Brill
 */
public class History implements Serializable {

    /**
     * The single entry in the history.
     */
    private static final class HistoryEntry implements Serializable {
        private final WebRequest webRequest_;

        private HistoryEntry(final WebRequest webRequest) {
            webRequest_ = webRequest;
        }

        private WebRequest getWebRequest() {
            return webRequest_;
        }

        private URL getUrl() {
            return webRequest_.getUrl();
        }

        /**
         * Returns the state object.
         * @return the state object
         */
        private Object getState() {
            return webRequest_.getState();
        }

        /**
         * Sets the state object.
         * @param state the state object to use
         */
        public void setState(final Object state) {
            webRequest_.setState(state);
        }
    }

    /** The window to which this navigation history belongs. */
    private final WebWindow window_;

    /**
     * The {@link HistoryEntry}s in this navigation history.
     */
    private final List<HistoryEntry> entries_ = new ArrayList<>();

    /** The current index within the list of pages which make up this navigation history. */
    private int index_ = -1;

    /**
     * Creates a new navigation history for the specified window.
     * @param window the window which owns the new navigation history
     */
    public History(final WebWindow window) {
        window_ = window;
    }

    /**
     * Returns the length of the navigation history.
     * @return the length of the navigation history
     */
    public int getLength() {
        return entries_.size();
    }

    /**
     * Returns the current (zero-based) index within the navigation history.
     * @return the current (zero-based) index within the navigation history
     */
    public int getIndex() {
        return index_;
    }

    /**
     * Returns the URL at the specified index in the navigation history, or {@code null} if the index is not valid.
     * @param index the index of the URL to be returned
     * @return the URL at the specified index in the navigation history, or {@code null} if the index is not valid
     */
    public URL getUrl(final int index) {
        if (index >= 0 && index < entries_.size()) {
            return UrlUtils.toUrlSafe(entries_.get(index).getUrl().toExternalForm());
        }
        return null;
    }

    /**
     * Goes back one step in the navigation history, if possible.
     * @return this navigation history, after going back one step
     * @throws IOException if an IO error occurs
     */
    public History back() throws IOException {
        if (index_ > 0) {
            index_--;
            goToUrlAtCurrentIndex();
        }
        return this;
    }

    /**
     * Goes forward one step in the navigation history, if possible.
     * @return this navigation history, after going forward one step
     * @throws IOException if an IO error occurs
     */
    public History forward() throws IOException {
        if (index_ < entries_.size() - 1) {
            index_++;
            goToUrlAtCurrentIndex();
        }
        return this;
    }

    /**
     * Goes forward or backwards in the navigation history, according to whether the specified relative index
     * is positive or negative. If the specified index is <tt>0</tt>, this method reloads the current page.
     * @param relativeIndex the index to move to, relative to the current index
     * @return this navigation history, after going forwards or backwards the specified number of steps
     * @throws IOException if an IO error occurs
     */
    public History go(final int relativeIndex) throws IOException {
        final int i = index_ + relativeIndex;
        if (i < entries_.size() && i >= 0) {
            index_ = i;
            goToUrlAtCurrentIndex();
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return entries_.toString();
    }

    /**
     * Removes the current URL from the history.
     */
    public void removeCurrent() {
        if (index_ >= 0 && index_ < entries_.size()) {
            entries_.remove(index_);
            if (index_ > 0) {
                index_--;
            }
        }
    }

    /**
     * Adds a new page to the navigation history.
     * @param page the page to add to the navigation history
     */
    protected void addPage(final Page page) {
        index_++;
        while (entries_.size() > index_) {
            entries_.remove(index_);
        }
        final WebRequest request = page.getWebResponse().getWebRequest();
        final WebRequest newRequest = new WebRequest(request.getUrl(), request.getHttpMethod());
        newRequest.setCloneForHistoryAPI(request.isCloneForHistoryAPI());
        newRequest.setState(request.getState());
        newRequest.setRequestParameters(request.getRequestParameters());
        entries_.add(new HistoryEntry(newRequest));
    }

    /**
     * Loads the URL at the current index into the window to which this navigation history belongs.
     * @throws IOException if an IO error occurs
     */
    private void goToUrlAtCurrentIndex() throws IOException {
        final HistoryEntry entry = entries_.get(index_);

        window_.getWebClient().getPage(window_, entry.getWebRequest(), false);
        final Window jsWindow = (Window) window_.getScriptableObject();
        if (jsWindow.hasEventHandlers("onpopstate")) {
            final Event event = new PopStateEvent(jsWindow, Event.TYPE_POPSTATE, entry.getState());
            jsWindow.executeEventLocally(event);
        }
    }

    /**
     * Allows to change history state and url if provided.
     *
     * @param state the new state to use
     * @param url the new url to use
     */
    public void replaceState(final Object state, final URL url) {
        final HistoryEntry entry = entries_.get(index_);
        entry.setState(state);

        if (url != null) {
            entry.getWebRequest().setUrl(url);
        }
    }

    /**
     * Returns current state object.
     *
     * @return the current state object
     */
    public Object getCurrentState() {
        if (index_ >= 0 && index_ < entries_.size()) {
            return entries_.get(index_).getState();
        }
        return null;
    }

    /**
     * Re-initializes transient fields when an object of this type is deserialized.
     * @param in the object input stream
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if an error occurs
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
