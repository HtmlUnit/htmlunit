/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import org.apache.commons.httpclient.HttpState;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 *
 * An object that handles the actual communication portion of page
 * retrieval/submission.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
public abstract class WebConnectionImpl implements WebConnection {

    private final WebClient webClient_;

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Creates a new web connection instance.
     * @param webClient the WebClient that is using this connection
     */
    public WebConnectionImpl(final WebClient webClient) {
        webClient_ = webClient;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Submits a request and returns the corresponding response.
     *
     * @param webRequestSettings Settings to make the request with
     * @return the response corresponding to the submission of the specified request
     * @exception IOException if an IO error occurs
     */
    public abstract WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException;

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Return the web client.
     * @return the web client
     */
    public final WebClient getWebClient() {
        return webClient_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Return the {@link HttpState} that is being used.
     * @return the state
     */
    public abstract HttpState getState();

}
