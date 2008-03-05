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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link Cache}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class CacheTest extends WebTestCase2 {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testIsDynamicContent() throws Exception {
        final Cache cache = new Cache();
        final Map<String, String> headers = new HashMap<String, String>();
        final WebResponse response = new DummyWebResponse() {
            @Override
            public String getResponseHeaderValue(final String headerName) {
                return (String) headers.get(headerName);
            }
        };

        assertTrue(cache.isDynamicContent(response));

        headers.put("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT");
        assertFalse(cache.isDynamicContent(response));

        headers.put("Last-Modified", DateUtil.formatDate(DateUtils.addMinutes(new Date(), -5)));
        assertTrue(cache.isDynamicContent(response));
        
        headers.put("Expires", DateUtil.formatDate(DateUtils.addMinutes(new Date(), 5)));
        assertTrue(cache.isDynamicContent(response));

        headers.put("Expires", DateUtil.formatDate(DateUtils.addHours(new Date(), 1)));
        assertFalse(cache.isDynamicContent(response));
        
        headers.remove("Last-Modified");
        assertFalse(cache.isDynamicContent(response));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testIsJavascript() throws Exception {
        final Cache cache = new Cache();

        final String[] contentType = {""};
        final URL[] url = {URL_FIRST};
        final WebResponse response = new DummyWebResponse() {
            @Override
            public String getContentType() {
                return contentType[0];
            }
            @Override
            public URL getUrl() {
                return url[0];
            }
        };
        
        assertFalse(cache.isJavaScript(response));

        contentType[0] = "text/javascript";
        assertTrue(cache.isJavaScript(response));

        contentType[0] = "application/x-javascript";
        assertTrue(cache.isJavaScript(response));
        
        contentType[0] = "text/plain";
        assertFalse(cache.isJavaScript(response));

        url[0] = new URL(URL_FIRST, "foo.js");
        assertTrue(cache.isJavaScript(response));

        url[0] = new URL(URL_FIRST, "foo.js?1_1_40C");
        assertTrue(cache.isJavaScript(response));
    }

    /**
     *@throws Exception if the test fails
     */
    @Test
    public void testUsage() throws Exception {
        final String content = "<html><head><title>page 1</title>\n"
            + "<script src='foo1.js' type='text/javascript'/>\n"
            + "<script src='foo2.js' type='text/javascript'/>\n"
            + "</head><body>\n"
            + "<a href='page2.html'>to page 2</a>\n"
            + "</body></html>";

        final String content2 = "<html><head><title>page 2</title>\n"
            + "<script src='foo2.js' type='text/javascript'/>\n"
            + "</head><body>\n"
            + "<a href='page1.html'>to page 1</a>\n"
            + "</body></html>";

        final String script1 = "alert('in foo1');";
        final String script2 = "alert('in foo2');";

        final WebClient webClient = new WebClient();
        final MockWebConnection connection = new MockWebConnection(webClient);
        webClient.setWebConnection(connection);

        final URL urlPage1 = new URL(URL_FIRST, "page1.html");
        connection.setResponse(urlPage1, content);
        final URL urlPage2 = new URL(URL_FIRST, "page2.html");
        connection.setResponse(urlPage2, content2);
        
        final List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT"));
        connection.setResponse(new URL(URL_FIRST, "foo1.js"), script1, 200, "ok", "text/javascript", headers);
        connection.setResponse(new URL(URL_FIRST, "foo2.js"), script2, 200, "ok", "text/javascript", headers);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page1 = (HtmlPage) webClient.getPage(urlPage1);
        final String[] expectedAlerts = {"in foo1", "in foo2"};
        assertEquals(expectedAlerts, collectedAlerts);
        
        collectedAlerts.clear();
        page1.getAnchors().get(0).click();
        
        assertEquals(new String[] {"in foo2"}, collectedAlerts);
        assertEquals("no request for scripts should have been performed",
                urlPage2, connection.getLastWebRequestSettings().getURL());
    }

    /**
     *@throws Exception if the test fails
     */
    @Test
    public void testMaxSizeMaintained() throws Exception {

        final String html = "<html><head><title>page 1</title>\n"
            + "<script src='foo1.js' type='text/javascript'/>\n"
            + "<script src='foo2.js' type='text/javascript'/>\n"
            + "</head><body>abc</body></html>";

        final WebClient client = new WebClient();
        client.getCache().setMaxSize(1);

        final MockWebConnection connection = new MockWebConnection(client);
        client.setWebConnection(connection);

        final URL pageUrl = new URL(URL_FIRST, "page1.html");
        connection.setResponse(pageUrl, html);

        final List<Header> headers =
            Collections.singletonList(new Header("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT"));
        connection.setResponse(new URL(URL_FIRST, "foo1.js"), ";", 200, "ok", "text/javascript", headers);
        connection.setResponse(new URL(URL_FIRST, "foo2.js"), ";", 200, "ok", "text/javascript", headers);

        client.getPage(pageUrl);
        assertEquals(1, client.getCache().getSize());

        client.getCache().clear();
        assertEquals(0, client.getCache().getSize());
    }

}

class DummyWebResponse implements WebResponse {

    public InputStream getContentAsStream() throws IOException {
        throw new RuntimeException("not implemented");
    }

    public String getContentAsString() {
        throw new RuntimeException("not implemented");
    }

    public String getContentCharSet() {
        throw new RuntimeException("not implemented");
    }

    public String getContentType() {
        throw new RuntimeException("not implemented");
    }

    public long getLoadTimeInMilliSeconds() {
        throw new RuntimeException("not implemented");
    }

    public SubmitMethod getRequestMethod() {
        throw new RuntimeException("not implemented");
    }

    public byte[] getResponseBody() {
        throw new RuntimeException("not implemented");
    }

    public List<NameValuePair> getResponseHeaders() {
        throw new RuntimeException("not implemented");
    }

    public String getResponseHeaderValue(final String headerName) {
        throw new RuntimeException("not implemented");
    }

    public int getStatusCode() {
        throw new RuntimeException("not implemented");
    }

    public String getStatusMessage() {
        throw new RuntimeException("not implemented");
    }

    public URL getUrl() {
        throw new RuntimeException("not implemented");
    }
}
