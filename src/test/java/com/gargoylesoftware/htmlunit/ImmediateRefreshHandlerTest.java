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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;

/**
 * Tests for {@link ImmediateRefreshHandler}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public final class ImmediateRefreshHandlerTest extends WebTestCase {
    /**
     * Create an instance.
     * @param name The name of the test.
     */
    public ImmediateRefreshHandlerTest(final String name) {
        super(name);
    }

    /**
     * Regression test for bug 1211980: redirect on the same page after a post
     * @throws Exception if the test fails
     */
    public void testRefreshSamePageAfterPost() throws Exception {
        final WebClient client = new WebClient();
        client.setRefreshHandler(new ImmediateRefreshHandler());
        
        // connection will return a page with <meta ... refresh> for the first call
        // and the same page without it for the other calls
        final MockWebConnection webConnection = new MockWebConnection(client) {
            private int nbCalls_ = 0;
            public WebResponse getResponse(final WebRequestSettings settings) throws IOException {
                String content = "<html><head>\n";
                if (nbCalls_ == 0) {
                    content += "<meta http-equiv='refresh' content='0;url="
                        + URL_GARGOYLE.toExternalForm()
                        + "'>\n";
                }
                content += "</head><body></body></html>";
                nbCalls_++;
                return new StringWebResponse(content, settings.getURL()) {
                    public SubmitMethod getRequestMethod() {
                        return settings.getSubmitMethod();
                    }
                };
            }
        };
        client.setWebConnection(webConnection);
        
        final WebRequestSettings settings = new WebRequestSettings(URL_GARGOYLE);
        settings.setSubmitMethod(SubmitMethod.POST);
        client.getPage(settings);
    }
}
