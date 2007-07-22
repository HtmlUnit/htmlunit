/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpState;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Provides a convenient implementation of the {@link WebConnection} interface that can be subclassed by developers
 * wishing to adapt a particular WebConnection.
 * This class implements the Wrapper or Decorator pattern. Methods default to calling through to the wrapped
 * web connection object.
 * @version $Revision$
 * @author Marc Guillemot
 */
public class WebConnectionWrapper implements WebConnection {
    private final WebConnection wrappedWebConnection_;

    /**
     * Constructs a WebConnection object wrapping provided WebConnection.
     * @param webConnection the webConnection that does the real work
     * @throws IllegalArgumentException if the connection is <code>null</code>
     */
    public WebConnectionWrapper(final WebConnection webConnection) throws IllegalArgumentException {
        if (webConnection == null) {
            throw new IllegalArgumentException("Wrapped connection can't be null");
        }
        wrappedWebConnection_ = webConnection;
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getResponse() on the wrapped connection object.
     */
    public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {
        return wrappedWebConnection_.getResponse(webRequestSettings);
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getState() on the wrapped connection object.
     */
    public HttpState getState() {
        return wrappedWebConnection_.getState();
    }

    /**
     * {@inheritDoc}
     * The default behavior of this method is to return getWebClient() on the wrapped connection object.
     */
    public WebClient getWebClient() {
        return wrappedWebConnection_.getWebClient();
    }

}
