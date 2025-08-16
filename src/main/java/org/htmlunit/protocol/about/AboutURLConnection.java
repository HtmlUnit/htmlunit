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
package org.htmlunit.protocol.about;

import java.net.URL;
import java.net.URLConnection;

/**
 * A {@link URLConnection} for "about:" URLs.
 *
 * @author Chen Jun
 */
public class AboutURLConnection extends URLConnection {

    /**
     * Creates a new instance.
     * @param newUrl the "about:" URL
     */
    public AboutURLConnection(final URL newUrl) {
        super(newUrl);
    }

    /**
     * @see java.net.URLConnection#connect()
     */
    @Override
    public void connect() {
        // nothing to do
    }

}
