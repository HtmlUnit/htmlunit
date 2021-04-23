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
package com.gargoylesoftware.htmlunit.protocol;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Stream handler for URLs with unknown protocol which should simply not be opened.
 *
 * @author Marc Guillemot
 */
public class AnyHandler extends URLStreamHandler {

    /**
     * Shared instance.
     */
    public static final AnyHandler INSTANCE = new AnyHandler();

    /**
     * Returns a new URLConnection for this URL.
     * @param url the JavaScript URL
     * @return the connection
     */
    @Override
    protected URLConnection openConnection(final URL url) {
        throw new RuntimeException("Unsupported protocol for open connection to " + url);
    }
}
