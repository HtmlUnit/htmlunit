/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
 * A response from a web server.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Brad Clarke
 * @author Noboru Sinohara
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @deprecated as of 2.8, use {@link WebResponse} instead
 */
@Deprecated
public class WebResponseImpl extends WebResponse {

    private static final long serialVersionUID = -4419537702781437059L;

    /**
     * Constructs with all data.
     *
     * @param responseData      Data that was send back
     * @param url               Where this response came from
     * @param requestMethod     the method used to get this response
     * @param loadTime          How long the response took to be sent
     */
    public WebResponseImpl(final WebResponseData responseData, final URL url,
            final HttpMethod requestMethod, final long loadTime) {
        this(responseData, new WebRequest(url, requestMethod), loadTime);
    }

    /**
     * Constructs with all data.
     *
     * @param responseData      Data that was send back
     * @param request           the request used to get this response
     * @param loadTime          How long the response took to be sent
     */
    public WebResponseImpl(final WebResponseData responseData,
            final WebRequest request, final long loadTime) {
        super(responseData, request, loadTime);
    }

}
