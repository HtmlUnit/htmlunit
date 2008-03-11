/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.List;

import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.WebResponseImpl;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link WebConnectionWrapper}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class WebConnectionWrapperTest extends WebTestCase {

    /**
     * @throws Exception If the test fails.
     */
    @Test
    public void wrapper() throws Exception {
        final HttpState state = new HttpState();
        final List<NameValuePair> emptyList = Collections.emptyList();
        final WebResponseData data = new WebResponseData(new byte[]{}, HttpStatus.SC_OK, "", emptyList);
        final WebResponse response = new WebResponseImpl(data, URL_FIRST, SubmitMethod.GET, 0);
        final WebClient webClient = new WebClient();
        final WebRequestSettings settings = new WebRequestSettings(URL_FIRST);
        final String[] lastMethodCalled = {""};

        final WebConnection realConnection = new WebConnection()
        {
            public WebResponse getResponse(final WebRequestSettings webRequestSettings) {
                assertSame(settings, webRequestSettings);
                lastMethodCalled[0] = "getResponse";
                return response;
            }
            public HttpState getState() {
                lastMethodCalled[0] = "getState";
                return state;
            }
            public WebClient getWebClient() {
                lastMethodCalled[0] = "getWebClient";
                return webClient;
            }
        };

        final WebConnectionWrapper wrapper = new WebConnectionWrapper(realConnection);

        assertSame(response, wrapper.getResponse(settings));
        assertEquals("getResponse", lastMethodCalled[0]);
        assertSame(state, wrapper.getState());
        assertEquals("getState", lastMethodCalled[0]);
        assertSame(webClient, wrapper.getWebClient());
        assertEquals("getWebClient", lastMethodCalled[0]);
    }

}
