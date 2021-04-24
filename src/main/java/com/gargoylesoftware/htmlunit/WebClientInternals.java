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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.gargoylesoftware.htmlunit.javascript.host.WebSocket;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * Represents a ways to get notified about internal objects.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class WebClientInternals implements Serializable {

    private Collection<Listener> listeners_;

    WebClientInternals() {
    }

    /**
     * Adds a new {@link Listener}.
     *
     * @param listener the listener to add
     */
    public void addListener(final Listener listener) {
        synchronized (this) {
            if (listeners_ == null) {
                listeners_ = Collections.synchronizedList(new ArrayList<>());
            }
        }
        listeners_.add(listener);
    }

    /**
     * Removes the specified {@link Listener}.
     *
     * @param listener the listener to remove
     */
    public void removeListener(final Listener listener) {
        listeners_.remove(listener);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * To notify when a new {@link WebSocket} is created.
     *
     * @param webSocket the {@link WebSocket}
     */
    public void created(final WebSocket webSocket) {
        if (listeners_ != null) {
            synchronized (listeners_) {
                for (final Listener l : listeners_) {
                    l.webSocketCreated(webSocket);
                }
            }
        }
    }

    /**
     * A listener for internal events.
     */
    public interface Listener {

        /**
         * A new {@link WebSocket} is created.
         *
         * @param webSocket the created {@link WebSocket}
         */
        void webSocketCreated(WebSocket webSocket);
    }
}
