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
package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link WaitingRefreshHandlerTest}.
 *
 * @version $Revision$
 * @author Brad Clarke
 */
public final class WaitingRefreshHandlerTest extends WebTestCase {
    /**
     * Create an instance.
     * @param name The name of the test.
     */
    public WaitingRefreshHandlerTest(final String name) {
        super(name);
    }

    /**
     * Trying to cause an interrupt on a javascript thread due to meta redirect navigation.
     * @throws Exception if the test fails
     */
    public void testRefreshOnJavscriptThread() throws Exception {
        final String firstContent = " <html>\n"
            + "<head><title>First Page</title>\n"
            + "<script>\n"
            + "function doRedirect() {\n"
            + "    window.location.href='" + URL_SECOND + "';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='setTimeout(\"doRedirect()\", 1);'>first page body</body>\n"
            + "</html>";
        final String secondContent = "<html>\n"
            + "<head><title>Meta Redirect Page</title>\n"
            + "<meta http-equiv='Refresh' content='1; URL=" + URL_THIRD + "'>\n"
            + "</head>\n"
            + "<body>redirect page body</body>\n"
            + "</html>";
        final String thirdContent = "<html>\n"
            + "<head><title>Expected Last Page</title></head>\n"
            + "<body>Success!</body>\n"
            + "</html>";

        final WebClient client = new WebClient();
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        conn.setResponse(URL_THIRD, thirdContent);
        client.setWebConnection(conn);
        client.setRefreshHandler(new WaitingRefreshHandler(0));

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        page.getEnclosingWindow().getThreadManager().joinAll(9999);
        final HtmlPage pageAfterWait = (HtmlPage) client.getCurrentWindow().getEnclosedPage();
        assertEquals("Expected Last Page", pageAfterWait.getTitleText());
    }
}
