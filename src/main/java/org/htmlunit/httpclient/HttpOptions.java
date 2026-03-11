/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.httpclient;

import java.net.URI;

/**
 * Customized HttpOptions for HtmlUnit.
 * In HttpClient 5, HttpOptions natively supports entity bodies, so this class
 * is kept for backwards compatibility but just extends the standard class.
 *
 * @author Ronald Brill
 */
public class HttpOptions extends org.apache.hc.client5.http.classic.methods.HttpOptions {

    /**
     * Ctor.
     */
    public HttpOptions() {
        super();
    }

    /**
     * Ctor.
     * @param uri the uri
     */
    public HttpOptions(final URI uri) {
        super(uri);
    }

    /**
     * @param uri the uri
     * @throws IllegalArgumentException if the uri is invalid.
     */
    public HttpOptions(final String uri) {
        super(uri);
    }
}
