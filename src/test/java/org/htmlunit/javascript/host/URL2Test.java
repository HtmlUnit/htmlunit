/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import org.htmlunit.HttpHeader;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.TopLevelWindow;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.UrlUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link URL}.
 *
 * @author Lai Quang Duong
 */
public class URL2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void createObjectURL() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, html);
        getMockWebConnection().setResponse(URL_SECOND, html);

        final WebClient wc = getWebClientWithMockWebConnection();
        final HtmlPage page = wc.getPage(URL_FIRST);
        final HtmlPage otherPage = (HtmlPage) wc.openWindow(URL_SECOND, "other").getEnclosedPage();

        final String blobUrl1 = (String) page.executeJavaScript(
                "URL.createObjectURL(new Blob(['blob1'], {type: 'text/plain'}))").getJavaScriptResult();
        final String blobUrl2 = (String) otherPage.executeJavaScript(
                "URL.createObjectURL(new Blob(['blob2'], {type: 'text/plain'}))").getJavaScriptResult();

        // each blob URL carries its creator's origin
        assertEquals(true, blobUrl1.startsWith(
                "blob:" + page.getUrl().getProtocol() + "://" + page.getUrl().getAuthority() + "/"));
        assertEquals(true, blobUrl2.startsWith(
                "blob:" + otherPage.getUrl().getProtocol() + "://" + otherPage.getUrl().getAuthority() + "/"));

        // every blob resolves through the one store, regardless of which window created it
        assertEquals("blob1", wc.loadWebResponse(new WebRequest(UrlUtils.toUrlUnsafe(blobUrl1))).getContentAsString());
        assertEquals("blob2", wc.loadWebResponse(new WebRequest(UrlUtils.toUrlUnsafe(blobUrl2))).getContentAsString());

        // the fragment is ignored when resolving a blob URL
        assertEquals("blob1",
                wc.loadWebResponse(new WebRequest(UrlUtils.toUrlUnsafe(blobUrl1 + "#foo=bar"))).getContentAsString());

        // closing a window drops only the blobs it created
        ((TopLevelWindow) otherPage.getEnclosingWindow()).close();
        assertEquals(null, wc.getBlobUrlStore().resolve(blobUrl2));
        assertEquals(true, wc.getBlobUrlStore().resolve(blobUrl1) != null);

        // navigating a window away drops the blobs it created
        page.executeJavaScript("window.location.href = '" + URL_SECOND + "';");
        assertEquals(null, wc.getBlobUrlStore().resolve(blobUrl1));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void createObjectURLFromFile() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body></body></html>";
        getMockWebConnection().setResponse(URL_FIRST, html);

        final WebClient wc = getWebClientWithMockWebConnection();
        final HtmlPage page = wc.getPage(URL_FIRST);

        final String blobUrl = (String) page.executeJavaScript(
                "URL.createObjectURL(new File(['file'], 'foo.txt', {type: 'text/plain'}))").getJavaScriptResult();

        final WebResponse response = wc.loadWebResponse(new WebRequest(UrlUtils.toUrlUnsafe(blobUrl)));
        assertEquals("file", response.getContentAsString());
        assertEquals("text/plain", response.getResponseHeaderValue(HttpHeader.CONTENT_TYPE));
        assertEquals("inline; filename=\"foo.txt\"", response.getResponseHeaderValue(HttpHeader.CONTENT_DISPOSITION));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void revokeObjectURL() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body></body></html>";
        getMockWebConnection().setResponse(URL_FIRST, html);

        final WebClient wc = getWebClientWithMockWebConnection();
        final HtmlPage page = wc.getPage(URL_FIRST);

        final String blobUrl = (String) page.executeJavaScript(
                "URL.createObjectURL(new Blob(['blob'], {type: 'text/plain'}))").getJavaScriptResult();
        assertEquals("blob", wc.loadWebResponse(new WebRequest(UrlUtils.toUrlUnsafe(blobUrl))).getContentAsString());

        page.executeJavaScript("URL.revokeObjectURL('" + blobUrl + "');");
        assertEquals(null, wc.getBlobUrlStore().resolve(blobUrl));
    }
}
