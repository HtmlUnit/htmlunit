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

/**
 * A listener for WebWindowEvent's.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface WebWindowListener {

    /**
     * A web window has been opened.
     *
     * @param event the event (the oldPage and newPage properties will be {@code null}
     * because the event is generated when the window is opened before the content is loaded)
     */
    void webWindowOpened(WebWindowEvent event);

    /**
     * The contents of a web window has been changed.
     *
     * @param event the event
     */
    void webWindowContentChanged(WebWindowEvent event);

    /**
     * A web window has been closed.
     *
     * @param event the event
     */
    void webWindowClosed(WebWindowEvent event);
}

