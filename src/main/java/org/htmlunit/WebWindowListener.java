/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit;

/**
 * A listener for WebWindowEvent's.
 * <p>
 * This listener informs when a new window is opened, when the content of a window changes
 * or when a window is closed.
 * </p>
 * <p>
 * Caution: The WebClient creates (and opens) the initial window as part of the construction
 * process. This implies, the initial window is already open at the time you attach this listener.
 * Therefore you will receive no open event for this.
 * </p>
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ronald Brill
 */
public interface WebWindowListener {

    /**
     * A web window has been opened.
     * <p>Caution: the {@link WebClient#getCurrentWindow()} might be not updated so far.
     * This usually takes place AFTER the event was processed</p>
     *
     * @param event the event (the oldPage and newPage properties will be {@code null}
     * because the event is generated after the window is opened but before the content is loaded)
     */
    void webWindowOpened(WebWindowEvent event);

    /**
     * The contents of a web window has been changed.
     *
     * @param event the event
     */
    void webWindowContentChanged(WebWindowEvent event);

    /**
     * A web window has been closed. Closing the last window of the WebClient will automatically open
     * a new one. You will receive an additional open event in this case.
     *
     * @param event the event
     */
    void webWindowClosed(WebWindowEvent event);
}

