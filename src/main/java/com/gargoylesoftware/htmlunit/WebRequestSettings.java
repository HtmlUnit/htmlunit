/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import java.net.URL;

/**
 * Parameter object for making web requests.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Hans Donner
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Rodney Gitzel
 * @deprecated as of 2.8, please use {@link WebRequest} instead.
 */
@Deprecated
public class WebRequestSettings extends WebRequest {

    /**
     * Instantiates a {@link WebRequestSettings} for the specified URL.
     * @param url the target URL
     */
    public WebRequestSettings(final URL url) {
        super(url);
    }

    /**
     * Instantiates a {@link WebRequestSettings} for the specified URL using the proxy configuration from the
     * specified original request.
     * @param originalRequest the original request
     * @param url the target URL
     */
    public WebRequestSettings(final WebRequestSettings originalRequest, final URL url) {
        super(originalRequest, url);
    }

    /**
     * Instantiates a {@link WebRequestSettings} for the specified URL using the specified HTTP submit method.
     * @param url the target URL
     * @param submitMethod the HTTP submit method to use
     */
    public WebRequestSettings(final URL url, final HttpMethod submitMethod) {
        super(url, submitMethod);
    }
}
