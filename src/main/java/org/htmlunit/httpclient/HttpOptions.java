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
package org.htmlunit.httpclient;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * Customized HttpOptions for HtmlUnit to be an HttpEntityEnclosingRequestBase.
 *
 * @author Ronald Brill
 */
public class HttpOptions extends HttpEntityEnclosingRequestBase {

    public HttpOptions() {
        super();
    }

    public HttpOptions(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * @param uri the uri
     * @throws IllegalArgumentException if the uri is invalid.
     */
    public HttpOptions(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return org.apache.http.client.methods.HttpOptions.METHOD_NAME;
    }
}
