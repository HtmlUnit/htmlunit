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
 * An exception that is thrown when the server returns a failing status code.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 */
public class FailingHttpStatusCodeException extends RuntimeException {

    private static final long serialVersionUID = 4080165207084775250L;

    private final WebResponse response_;

    /**
     * Creates an instance.
     * @param failingResponse the failing response
     */
    public FailingHttpStatusCodeException(final WebResponse failingResponse) {
        response_ = failingResponse;
    }

    /**
     * Returns the failing status code.
     * @return the code
     */
    public int getStatusCode() {
        return response_.getStatusCode();
    }

    /**
     * Returns the message associated with the failing status code.
     * @return the message
     */
    public String getStatusMessage() {
        return response_.getStatusMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        final int code = getStatusCode();
        final String msg = getStatusMessage();
        final URL url = getResponse().getRequestSettings().getUrl();
        return code + " " + msg + " for " + url;
    }

    /**
     * Gets the failing response.
     * @return the response
     */
    public WebResponse getResponse() {
        return response_;
    }

}
